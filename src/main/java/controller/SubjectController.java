package controller;

import dao.ModuleDao;
import dao.SubjectDao;
import javafx.collections.FXCollections;
import model.SchoolModule;
import model.Subject;
import util.AlertUtil;
import util.ValidationUtil;
import view.SubjectView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubjectController {
    private final SubjectView view = new SubjectView();
    private final SubjectDao subjectDao = new SubjectDao();
    private final ModuleDao moduleDao = new ModuleDao();

    public SubjectController() {
        configureActions();
        configureSelection();
        refresh();
    }

    public SubjectView getView() {
        return view;
    }

    private void configureActions() {
        view.getAddButton().setOnAction(event -> addSubject());
        view.getUpdateButton().setOnAction(event -> updateSubject());
        view.getDeleteButton().setOnAction(event -> deleteSubject());
        view.getClearButton().setOnAction(event -> clearForm());
        view.getRefreshButton().setOnAction(event -> refresh());
    }

    private void configureSelection() {
        view.getTable().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> {
            if (selected != null) {
                view.getSubjectNameField().setText(selected.getSubjectName());
                view.getCoefficientField().setText(String.valueOf(selected.getCoefficient()));
                selectModule(selected.getModuleId());
            }
        });
    }

    private void refresh() {
        try {
            view.getModuleComboBox().setItems(FXCollections.observableArrayList(moduleDao.findAll()));
            view.getTable().setItems(FXCollections.observableArrayList(subjectDao.findAll()));
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private void addSubject() {
        List<String> errors = new ArrayList<>();
        Subject subject = readForm(0, errors);
        errors.addAll(ValidationUtil.validateSubject(subject));
        if (!errors.isEmpty()) {
            AlertUtil.showWarning("Invalid subject", ValidationUtil.joinErrors(errors));
            return;
        }
        try {
            subjectDao.create(subject);
            refresh();
            clearForm();
            AlertUtil.showInfo("Subject added", "The subject was added successfully.");
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private void updateSubject() {
        Subject selected = view.getTable().getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("No selection", "Select a subject to update.");
            return;
        }
        List<String> errors = new ArrayList<>();
        Subject subject = readForm(selected.getId(), errors);
        errors.addAll(ValidationUtil.validateSubject(subject));
        if (!errors.isEmpty()) {
            AlertUtil.showWarning("Invalid subject", ValidationUtil.joinErrors(errors));
            return;
        }
        try {
            subjectDao.update(subject);
            refresh();
            clearForm();
            AlertUtil.showInfo("Subject updated", "The subject was updated successfully.");
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private void deleteSubject() {
        Subject selected = view.getTable().getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("No selection", "Select a subject to delete.");
            return;
        }
        if (!AlertUtil.confirm("Delete subject", "Delete " + selected.getSubjectName() + "? Related grades will also be deleted.")) {
            return;
        }
        try {
            subjectDao.delete(selected.getId());
            refresh();
            clearForm();
            AlertUtil.showInfo("Subject deleted", "The subject was deleted successfully.");
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private Subject readForm(int id, List<String> errors) {
        SchoolModule selectedModule = view.getModuleComboBox().getSelectionModel().getSelectedItem();
        double coefficient = ValidationUtil.parseDouble(view.getCoefficientField().getText(), "Coefficient", errors);
        return new Subject(
                id,
                ValidationUtil.clean(view.getSubjectNameField().getText()),
                selectedModule == null ? 0 : selectedModule.getId(),
                coefficient
        );
    }

    private void selectModule(int moduleId) {
        Optional<SchoolModule> module = view.getModuleComboBox().getItems().stream()
                .filter(item -> item.getId() == moduleId)
                .findFirst();
        module.ifPresent(value -> view.getModuleComboBox().getSelectionModel().select(value));
    }

    private void clearForm() {
        view.getTable().getSelectionModel().clearSelection();
        view.getSubjectNameField().clear();
        view.getCoefficientField().clear();
        view.getModuleComboBox().getSelectionModel().clearSelection();
    }
}
