package controller;

import dao.GradeDao;
import dao.StudentDao;
import dao.SubjectDao;
import javafx.collections.FXCollections;
import model.Grade;
import model.GradeReportRow;
import model.Student;
import model.Subject;
import util.AlertUtil;
import util.ValidationUtil;
import view.GradeView;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GradeController {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    private final GradeView view = new GradeView();
    private final GradeDao gradeDao = new GradeDao();
    private final StudentDao studentDao = new StudentDao();
    private final SubjectDao subjectDao = new SubjectDao();

    public GradeController() {
        configureActions();
        configureSelection();
        refresh();
    }

    public GradeView getView() {
        return view;
    }

    private void configureActions() {
        view.getAssignButton().setOnAction(event -> assignGrade());
        view.getUpdateButton().setOnAction(event -> updateGrade());
        view.getDeleteButton().setOnAction(event -> deleteGrade());
        view.getClearButton().setOnAction(event -> clearForm());
        view.getRefreshButton().setOnAction(event -> refresh());
    }

    private void configureSelection() {
        view.getStudentComboBox().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> {
            if (selected != null) {
                loadGradesForSelectedStudent();
            }
        });

        view.getTable().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> {
            if (selected != null) {
                selectSubject(selected.getSubjectId());
                view.getGradeField().setText(String.valueOf(selected.getGradeValue()));
            }
        });
    }

    private void refresh() {
        Student selectedStudent = view.getStudentComboBox().getSelectionModel().getSelectedItem();
        try {
            view.getStudentComboBox().setItems(FXCollections.observableArrayList(studentDao.findAll()));
            view.getSubjectComboBox().setItems(FXCollections.observableArrayList(subjectDao.findAll()));
            if (selectedStudent != null) {
                view.getStudentComboBox().getItems().stream()
                        .filter(student -> student.getId() == selectedStudent.getId())
                        .findFirst()
                        .ifPresent(student -> view.getStudentComboBox().getSelectionModel().select(student));
            }
            loadGradesForSelectedStudent();
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private void loadGradesForSelectedStudent() {
        Student student = view.getStudentComboBox().getSelectionModel().getSelectedItem();
        if (student == null) {
            view.getTable().setItems(FXCollections.observableArrayList());
            view.getAverageLabel().setText("Average: 0.00/20");
            return;
        }
        try {
            view.getTable().setItems(FXCollections.observableArrayList(gradeDao.findReportRowsByStudentId(student.getId())));
            double average = gradeDao.calculateAverage(student.getId());
            view.getAverageLabel().setText("Average: " + DECIMAL_FORMAT.format(average) + "/20");
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private void assignGrade() {
        Student student = selectedStudentOrWarn();
        Subject subject = selectedSubjectOrWarn();
        if (student == null || subject == null) {
            return;
        }
        List<String> errors = new ArrayList<>();
        double gradeValue = ValidationUtil.parseDouble(view.getGradeField().getText(), "Grade", errors);
        errors.addAll(ValidationUtil.validateGrade(gradeValue));
        if (!errors.isEmpty()) {
            AlertUtil.showWarning("Invalid grade", ValidationUtil.joinErrors(errors));
            return;
        }
        try {
            Optional<Grade> existing = gradeDao.findByStudentAndSubject(student.getId(), subject.getId());
            if (existing.isPresent()) {
                AlertUtil.showWarning("Grade exists", "This student already has a grade for this subject. Use Modify to update it.");
                return;
            }
            gradeDao.create(new Grade(student.getId(), subject.getId(), gradeValue));
            loadGradesForSelectedStudent();
            clearForm();
            AlertUtil.showInfo("Grade assigned", "The grade was assigned successfully.");
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private void updateGrade() {
        Student student = selectedStudentOrWarn();
        Subject subject = selectedSubjectOrWarn();
        GradeReportRow selectedRow = view.getTable().getSelectionModel().getSelectedItem();
        if (student == null || subject == null) {
            return;
        }
        if (selectedRow == null) {
            AlertUtil.showWarning("No selection", "Select a grade row to modify.");
            return;
        }
        List<String> errors = new ArrayList<>();
        double gradeValue = ValidationUtil.parseDouble(view.getGradeField().getText(), "Grade", errors);
        errors.addAll(ValidationUtil.validateGrade(gradeValue));
        if (!errors.isEmpty()) {
            AlertUtil.showWarning("Invalid grade", ValidationUtil.joinErrors(errors));
            return;
        }
        try {
            Grade grade = new Grade(selectedRow.getGradeId(), student.getId(), subject.getId(), gradeValue);
            gradeDao.update(grade);
            loadGradesForSelectedStudent();
            clearForm();
            AlertUtil.showInfo("Grade updated", "The grade was updated successfully.");
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private void deleteGrade() {
        GradeReportRow selectedRow = view.getTable().getSelectionModel().getSelectedItem();
        if (selectedRow == null) {
            AlertUtil.showWarning("No selection", "Select a grade to delete.");
            return;
        }
        if (!AlertUtil.confirm("Delete grade", "Delete this grade?")) {
            return;
        }
        try {
            gradeDao.delete(selectedRow.getGradeId());
            loadGradesForSelectedStudent();
            clearForm();
            AlertUtil.showInfo("Grade deleted", "The grade was deleted successfully.");
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private Student selectedStudentOrWarn() {
        Student student = view.getStudentComboBox().getSelectionModel().getSelectedItem();
        if (student == null) {
            AlertUtil.showWarning("No student", "Select a student first.");
        }
        return student;
    }

    private Subject selectedSubjectOrWarn() {
        Subject subject = view.getSubjectComboBox().getSelectionModel().getSelectedItem();
        if (subject == null) {
            AlertUtil.showWarning("No subject", "Select a subject first.");
        }
        return subject;
    }

    private void selectSubject(int subjectId) {
        view.getSubjectComboBox().getItems().stream()
                .filter(subject -> subject.getId() == subjectId)
                .findFirst()
                .ifPresent(subject -> view.getSubjectComboBox().getSelectionModel().select(subject));
    }

    private void clearForm() {
        view.getTable().getSelectionModel().clearSelection();
        view.getSubjectComboBox().getSelectionModel().clearSelection();
        view.getGradeField().clear();
    }
}
