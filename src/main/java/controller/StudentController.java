package controller;

import dao.StudentDao;
import javafx.collections.FXCollections;
import model.Student;
import util.AlertUtil;
import util.ValidationUtil;
import view.StudentView;

import java.sql.SQLException;
import java.util.List;

public class StudentController {
    private final StudentView view = new StudentView();
    private final StudentDao studentDao = new StudentDao();

    public StudentController() {
        configureActions();
        configureSelection();
        loadStudents();
    }

    public StudentView getView() {
        return view;
    }

    private void configureActions() {
        view.getAddButton().setOnAction(event -> addStudent());
        view.getUpdateButton().setOnAction(event -> updateStudent());
        view.getDeleteButton().setOnAction(event -> deleteStudent());
        view.getClearButton().setOnAction(event -> clearForm());
        view.getSearchButton().setOnAction(event -> searchStudents());
        view.getRefreshButton().setOnAction(event -> loadStudents());
    }

    private void configureSelection() {
        view.getTable().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> {
            if (selected != null) {
                populateForm(selected);
            }
        });
    }

    private void loadStudents() {
        try {
            view.getTable().setItems(FXCollections.observableArrayList(studentDao.findAll()));
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private void searchStudents() {
        String keyword = ValidationUtil.clean(view.getSearchField().getText());
        try {
            view.getTable().setItems(FXCollections.observableArrayList(
                    ValidationUtil.isBlank(keyword) ? studentDao.findAll() : studentDao.search(keyword)
            ));
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private void addStudent() {
        Student student = readForm(0);
        List<String> errors = ValidationUtil.validateStudent(student);
        if (!errors.isEmpty()) {
            AlertUtil.showWarning("Invalid student", ValidationUtil.joinErrors(errors));
            return;
        }
        try {
            if (studentDao.existsByEmail(student.getEmail(), null)) {
                AlertUtil.showWarning("Duplicate student", "A student with this email already exists.");
                return;
            }
            studentDao.create(student);
            loadStudents();
            clearForm();
            AlertUtil.showInfo("Student added", "The student was added successfully.");
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private void updateStudent() {
        Student selected = view.getTable().getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("No selection", "Select a student to update.");
            return;
        }
        Student student = readForm(selected.getId());
        List<String> errors = ValidationUtil.validateStudent(student);
        if (!errors.isEmpty()) {
            AlertUtil.showWarning("Invalid student", ValidationUtil.joinErrors(errors));
            return;
        }
        try {
            if (studentDao.existsByEmail(student.getEmail(), student.getId())) {
                AlertUtil.showWarning("Duplicate student", "Another student already uses this email.");
                return;
            }
            studentDao.update(student);
            loadStudents();
            clearForm();
            AlertUtil.showInfo("Student updated", "The student was updated successfully.");
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private void deleteStudent() {
        Student selected = view.getTable().getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("No selection", "Select a student to delete.");
            return;
        }
        if (!AlertUtil.confirm("Delete student", "Delete " + selected.getFullName() + "? Related grades will also be deleted.")) {
            return;
        }
        try {
            studentDao.delete(selected.getId());
            loadStudents();
            clearForm();
            AlertUtil.showInfo("Student deleted", "The student was deleted successfully.");
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private Student readForm(int id) {
        return new Student(
                id,
                ValidationUtil.clean(view.getFirstNameField().getText()),
                ValidationUtil.clean(view.getLastNameField().getText()),
                ValidationUtil.clean(view.getEmailField().getText()).toLowerCase(),
                ValidationUtil.clean(view.getPhoneField().getText()),
                ValidationUtil.clean(view.getClassNameField().getText())
        );
    }

    private void populateForm(Student student) {
        view.getFirstNameField().setText(student.getFirstName());
        view.getLastNameField().setText(student.getLastName());
        view.getEmailField().setText(student.getEmail());
        view.getPhoneField().setText(student.getPhone());
        view.getClassNameField().setText(student.getClassName());
    }

    private void clearForm() {
        view.getTable().getSelectionModel().clearSelection();
        view.getFirstNameField().clear();
        view.getLastNameField().clear();
        view.getEmailField().clear();
        view.getPhoneField().clear();
        view.getClassNameField().clear();
    }
}
