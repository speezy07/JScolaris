package controller;

import dao.GradeDao;
import dao.StudentDao;
import javafx.collections.FXCollections;
import javafx.stage.DirectoryChooser;
import model.GradeReportRow;
import model.Student;
import util.AlertUtil;
import util.PdfReportExporter;
import util.ValidationUtil;
import view.ExportView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

public class ExportController {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    private final ExportView view = new ExportView();
    private final StudentDao studentDao = new StudentDao();
    private final GradeDao gradeDao = new GradeDao();
    private final PdfReportExporter exporter = new PdfReportExporter();

    public ExportController() {
        configureActions();
        loadStudents();
        setDefaultOutputDirectory();
    }

    public ExportView getView() {
        return view;
    }

    private void configureActions() {
        view.getChooseDirectoryButton().setOnAction(event -> chooseDirectory());
        view.getExportButton().setOnAction(event -> exportPdf());
        view.getStudentComboBox().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> {
            if (selected != null) {
                refreshPreview(selected);
            }
        });
    }

    private void loadStudents() {
        try {
            view.getStudentComboBox().setItems(FXCollections.observableArrayList(studentDao.findAll()));
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private void setDefaultOutputDirectory() {
        Path path = Path.of(System.getProperty("user.home"), "Documents", "JScolarisBulletins");
        view.getOutputDirectoryField().setText(path.toString());
    }

    private void chooseDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose PDF output folder");
        File directory = chooser.showDialog(view.getScene().getWindow());
        if (directory != null) {
            view.getOutputDirectoryField().setText(directory.toString());
        }
    }

    private void refreshPreview(Student student) {
        try {
            List<GradeReportRow> rows = gradeDao.findReportRowsByStudentId(student.getId());
            double average = gradeDao.calculateAverage(student.getId());
            StringBuilder builder = new StringBuilder();
            builder.append("Student: ").append(student.getFullName()).append(System.lineSeparator());
            builder.append("Class: ").append(student.getClassName()).append(System.lineSeparator());
            builder.append("Email: ").append(student.getEmail()).append(System.lineSeparator());
            builder.append("Grades:").append(System.lineSeparator());
            for (GradeReportRow row : rows) {
                builder.append("- ")
                        .append(row.getModuleName())
                        .append(" / ")
                        .append(row.getSubjectName())
                        .append(": ")
                        .append(DECIMAL_FORMAT.format(row.getGradeValue()))
                        .append("/20")
                        .append(System.lineSeparator());
            }
            builder.append("Average: ").append(DECIMAL_FORMAT.format(average)).append("/20");
            view.getPreviewArea().setText(builder.toString());
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private void exportPdf() {
        Student student = view.getStudentComboBox().getSelectionModel().getSelectedItem();
        if (student == null) {
            AlertUtil.showWarning("No student", "Select a student before exporting.");
            return;
        }
        String outputDirectory = ValidationUtil.clean(view.getOutputDirectoryField().getText());
        if (ValidationUtil.isBlank(outputDirectory)) {
            AlertUtil.showWarning("No folder", "Choose an output directory.");
            return;
        }
        try {
            List<GradeReportRow> rows = gradeDao.findReportRowsByStudentId(student.getId());
            double average = gradeDao.calculateAverage(student.getId());
            Path outputFile = Path.of(outputDirectory, fileNameFor(student));
            Path generated = exporter.exportReport(student, rows, average, outputFile);
            AlertUtil.showInfo("PDF generated", "Report card saved to: " + generated);
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        } catch (IOException exception) {
            AlertUtil.showError("PDF error", exception.getMessage());
        }
    }

    private String fileNameFor(Student student) {
        String raw = "bulletin_" + student.getFirstName() + "_" + student.getLastName() + "_" + student.getId() + ".pdf";
        return raw.replaceAll("[^A-Za-z0-9_.-]", "_");
    }
}
