package controller;

import dao.StudentDao;
import javafx.collections.FXCollections;
import javafx.stage.FileChooser;
import model.Student;
import util.AlertUtil;
import util.CsvStudentImporter;
import util.ExcelStudentImporter;
import util.ImportResult;
import view.ImportView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImportController {
    private final ImportView view = new ImportView();
    private final StudentDao studentDao = new StudentDao();
    private final CsvStudentImporter csvImporter = new CsvStudentImporter();
    private final ExcelStudentImporter excelImporter = new ExcelStudentImporter();
    private List<Student> previewStudents = new ArrayList<>();

    public ImportController() {
        configureActions();
    }

    public ImportView getView() {
        return view;
    }

    private void configureActions() {
        view.getChooseCsvButton().setOnAction(event -> chooseCsvFile());
        view.getChooseExcelButton().setOnAction(event -> chooseExcelFile());
        view.getImportButton().setOnAction(event -> importPreviewStudents());
    }

    private void chooseCsvFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose CSV file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
        File file = fileChooser.showOpenDialog(view.getScene().getWindow());
        if (file != null) {
            loadPreview(file.toPath(), false);
        }
    }

    private void chooseExcelFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Excel file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel files", "*.xlsx"));
        File file = fileChooser.showOpenDialog(view.getScene().getWindow());
        if (file != null) {
            loadPreview(file.toPath(), true);
        }
    }

    private void loadPreview(Path filePath, boolean excel) {
        try {
            ImportResult result = excel
                    ? excelImporter.importStudents(filePath)
                    : csvImporter.importStudents(filePath);
            previewStudents = new ArrayList<>(result.getStudents());
            view.getPreviewTable().setItems(FXCollections.observableArrayList(previewStudents));
            view.getFilePathField().setText(filePath.toString());
            view.getLogArea().setText(buildImportLog(result));
        } catch (IOException exception) {
            previewStudents = new ArrayList<>();
            view.getPreviewTable().setItems(FXCollections.observableArrayList());
            view.getLogArea().setText("Import failed: " + exception.getMessage());
            AlertUtil.showError("Import error", exception.getMessage());
        }
    }

    private String buildImportLog(ImportResult result) {
        StringBuilder builder = new StringBuilder(result.summary());
        if (result.hasErrors()) {
            builder.append(System.lineSeparator()).append(System.lineSeparator()).append("Details:");
            for (String error : result.getErrors()) {
                builder.append(System.lineSeparator()).append("- ").append(error);
            }
        }
        return builder.toString();
    }

    private void importPreviewStudents() {
        if (previewStudents.isEmpty()) {
            AlertUtil.showWarning("No data", "Choose a valid CSV or Excel file first.");
            return;
        }

        int inserted = 0;
        int duplicates = 0;
        StringBuilder log = new StringBuilder();
        try {
            for (Student student : previewStudents) {
                if (studentDao.existsByEmail(student.getEmail(), null)) {
                    duplicates++;
                    log.append("Skipped duplicate: ").append(student.getEmail()).append(System.lineSeparator());
                    continue;
                }
                studentDao.create(student);
                inserted++;
            }
            log.insert(0, "Inserted students: " + inserted + System.lineSeparator()
                    + "Database duplicates skipped: " + duplicates + System.lineSeparator()
                    + System.lineSeparator());
            view.getLogArea().setText(log.toString());
            view.getPreviewTable().setItems(FXCollections.observableArrayList());
            previewStudents = new ArrayList<>();
            AlertUtil.showInfo("Import complete", inserted + " students imported.");
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }
}
