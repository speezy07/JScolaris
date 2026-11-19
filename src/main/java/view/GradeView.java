package view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.GradeReportRow;
import model.Student;
import model.Subject;

public class GradeView extends BorderPane {
    private final TableView<GradeReportRow> table = new TableView<>();
    private final ComboBox<Student> studentComboBox = new ComboBox<>();
    private final ComboBox<Subject> subjectComboBox = new ComboBox<>();
    private final TextField gradeField = ViewFactory.textField("Grade /20");
    private final Label averageLabel = new Label("Average: 0.00/20");
    private final Button assignButton = ViewFactory.primaryButton("Assign");
    private final Button updateButton = ViewFactory.secondaryButton("Modify");
    private final Button deleteButton = ViewFactory.dangerButton("Delete");
    private final Button clearButton = ViewFactory.secondaryButton("Clear");
    private final Button refreshButton = ViewFactory.secondaryButton("Refresh");

    public GradeView() {
        getStyleClass().add("content");
        studentComboBox.setPromptText("Select student");
        studentComboBox.setPrefWidth(260);
        subjectComboBox.setPromptText("Select subject");
        subjectComboBox.setPrefWidth(260);
        averageLabel.getStyleClass().add("section-title");
        setTop(createHeader());
        setCenter(createTablePanel());
        setRight(createFormPanel());
    }

    public TableView<GradeReportRow> getTable() {
        return table;
    }

    public ComboBox<Student> getStudentComboBox() {
        return studentComboBox;
    }

    public ComboBox<Subject> getSubjectComboBox() {
        return subjectComboBox;
    }

    public TextField getGradeField() {
        return gradeField;
    }

    public Label getAverageLabel() {
        return averageLabel;
    }

    public Button getAssignButton() {
        return assignButton;
    }

    public Button getUpdateButton() {
        return updateButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getClearButton() {
        return clearButton;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    private VBox createHeader() {
        HBox row = new HBox(8, studentComboBox, refreshButton);
        VBox header = new VBox(10, ViewFactory.pageTitle("Grade Management"), row);
        BorderPane.setMargin(header, new Insets(0, 0, 14, 0));
        return header;
    }

    private VBox createTablePanel() {
        table.getColumns().add(ViewFactory.<GradeReportRow, String>column("Module", "moduleName"));
        table.getColumns().add(ViewFactory.<GradeReportRow, String>column("Subject", "subjectName"));
        table.getColumns().add(ViewFactory.<GradeReportRow, Double>column("Module Coef.", "moduleCoefficient"));
        table.getColumns().add(ViewFactory.<GradeReportRow, Double>column("Subject Coef.", "subjectCoefficient"));
        table.getColumns().add(ViewFactory.<GradeReportRow, Double>column("Grade", "gradeValue"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox panel = ViewFactory.panel(12);
        panel.getChildren().addAll(ViewFactory.sectionTitle("Student Grades"), table, averageLabel);
        VBox.setVgrow(table, Priority.ALWAYS);
        return panel;
    }

    private VBox createFormPanel() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Subject"), 0, 0);
        grid.add(subjectComboBox, 1, 0);
        grid.add(new Label("Grade"), 0, 1);
        grid.add(gradeField, 1, 1);

        HBox buttons = new HBox(8, assignButton, updateButton, deleteButton, clearButton);
        VBox panel = ViewFactory.panel(14);
        panel.setPrefWidth(390);
        panel.getChildren().addAll(ViewFactory.sectionTitle("Grade Form"), grid, buttons);
        BorderPane.setMargin(panel, new Insets(0, 0, 0, 14));
        return panel;
    }
}
