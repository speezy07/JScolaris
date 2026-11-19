package controller;

import dao.TeacherDao;
import javafx.collections.FXCollections;
import model.Teacher;
import util.AlertUtil;
import util.ValidationUtil;
import view.TeacherView;

import java.sql.SQLException;
import java.util.List;

public class TeacherController {
    private final TeacherView view = new TeacherView();
    private final TeacherDao teacherDao = new TeacherDao();

    public TeacherController() {
        configureActions();
        configureSelection();
        loadTeachers();
    }

    public TeacherView getView() {
        return view;
    }

    private void configureActions() {
        view.getAddButton().setOnAction(event -> addTeacher());
        view.getUpdateButton().setOnAction(event -> updateTeacher());
        view.getDeleteButton().setOnAction(event -> deleteTeacher());
        view.getClearButton().setOnAction(event -> clearForm());
        view.getSearchButton().setOnAction(event -> searchTeachers());
        view.getRefreshButton().setOnAction(event -> loadTeachers());
    }

    private void configureSelection() {
        view.getTable().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> {
            if (selected != null) {
                populateForm(selected);
            }
        });
    }

    private void loadTeachers() {
        try {
            view.getTable().setItems(FXCollections.observableArrayList(teacherDao.findAll()));
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private void searchTeachers() {
        String keyword = ValidationUtil.clean(view.getSearchField().getText());
        try {
            view.getTable().setItems(FXCollections.observableArrayList(
                    ValidationUtil.isBlank(keyword) ? teacherDao.findAll() : teacherDao.search(keyword)
            ));
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private void addTeacher() {
        Teacher teacher = readForm(0);
        List<String> errors = ValidationUtil.validateTeacher(teacher);
        if (!errors.isEmpty()) {
            AlertUtil.showWarning("Invalid teacher", ValidationUtil.joinErrors(errors));
            return;
        }
        try {
            if (teacherDao.existsByEmail(teacher.getEmail(), null)) {
                AlertUtil.showWarning("Duplicate teacher", "A teacher with this email already exists.");
                return;
            }
            teacherDao.create(teacher);
            loadTeachers();
            clearForm();
            AlertUtil.showInfo("Teacher added", "The teacher was added successfully.");
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private void updateTeacher() {
        Teacher selected = view.getTable().getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("No selection", "Select a teacher to update.");
            return;
        }
        Teacher teacher = readForm(selected.getId());
        List<String> errors = ValidationUtil.validateTeacher(teacher);
        if (!errors.isEmpty()) {
            AlertUtil.showWarning("Invalid teacher", ValidationUtil.joinErrors(errors));
            return;
        }
        try {
            if (teacherDao.existsByEmail(teacher.getEmail(), teacher.getId())) {
                AlertUtil.showWarning("Duplicate teacher", "Another teacher already uses this email.");
                return;
            }
            teacherDao.update(teacher);
            loadTeachers();
            clearForm();
            AlertUtil.showInfo("Teacher updated", "The teacher was updated successfully.");
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private void deleteTeacher() {
        Teacher selected = view.getTable().getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("No selection", "Select a teacher to delete.");
            return;
        }
        if (!AlertUtil.confirm("Delete teacher", "Delete " + selected.getFullName() + "?")) {
            return;
        }
        try {
            teacherDao.delete(selected.getId());
            loadTeachers();
            clearForm();
            AlertUtil.showInfo("Teacher deleted", "The teacher was deleted successfully.");
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private Teacher readForm(int id) {
        return new Teacher(
                id,
                ValidationUtil.clean(view.getFirstNameField().getText()),
                ValidationUtil.clean(view.getLastNameField().getText()),
                ValidationUtil.clean(view.getSpecialtyField().getText()),
                ValidationUtil.clean(view.getEmailField().getText()).toLowerCase()
        );
    }

    private void populateForm(Teacher teacher) {
        view.getFirstNameField().setText(teacher.getFirstName());
        view.getLastNameField().setText(teacher.getLastName());
        view.getSpecialtyField().setText(teacher.getSpecialty());
        view.getEmailField().setText(teacher.getEmail());
    }

    private void clearForm() {
        view.getTable().getSelectionModel().clearSelection();
        view.getFirstNameField().clear();
        view.getLastNameField().clear();
        view.getSpecialtyField().clear();
        view.getEmailField().clear();
    }
}
