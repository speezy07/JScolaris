package view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Student;

public class StudentView extends BorderPane {
    private final TableView<Student> table = new TableView<>();
    private final TextField searchField = ViewFactory.textField("Search student");
    private final TextField firstNameField = ViewFactory.textField("First name");
    private final TextField lastNameField = ViewFactory.textField("Last name");
    private final TextField emailField = ViewFactory.textField("Email");
    private final TextField phoneField = ViewFactory.textField("Phone");
    private final TextField classNameField = ViewFactory.textField("Class");
    private final Button addButton = ViewFactory.primaryButton("Add");
    private final Button updateButton = ViewFactory.secondaryButton("Update");
    private final Button deleteButton = ViewFactory.dangerButton("Delete");
    private final Button clearButton = ViewFactory.secondaryButton("Clear");
    private final Button searchButton = ViewFactory.secondaryButton("Search");
    private final Button refreshButton = ViewFactory.secondaryButton("Refresh");

    public StudentView() {
        getStyleClass().add("content");
        setTop(createHeader());
        setCenter(createTablePanel());
        setRight(createFormPanel());
    }

    public TableView<Student> getTable() {
        return table;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public TextField getFirstNameField() {
        return firstNameField;
    }

    public TextField getLastNameField() {
        return lastNameField;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public TextField getPhoneField() {
        return phoneField;
    }

    public TextField getClassNameField() {
        return classNameField;
    }

    public Button getAddButton() {
        return addButton;
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

    public Button getSearchButton() {
        return searchButton;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    private VBox createHeader() {
        HBox searchRow = new HBox(8, searchField, searchButton, refreshButton);
        VBox header = new VBox(10, ViewFactory.pageTitle("Student Management"), searchRow);
        BorderPane.setMargin(header, new Insets(0, 0, 14, 0));
        return header;
    }

    private VBox createTablePanel() {
        table.getColumns().add(ViewFactory.<Student, Integer>column("ID", "id"));
        table.getColumns().add(ViewFactory.<Student, String>column("First Name", "firstName"));
        table.getColumns().add(ViewFactory.<Student, String>column("Last Name", "lastName"));
        table.getColumns().add(ViewFactory.<Student, String>column("Email", "email"));
        table.getColumns().add(ViewFactory.<Student, String>column("Phone", "phone"));
        table.getColumns().add(ViewFactory.<Student, String>column("Class", "className"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox panel = ViewFactory.panel(12);
        panel.getChildren().addAll(ViewFactory.sectionTitle("Students"), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return panel;
    }

    private VBox createFormPanel() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        addRow(grid, 0, "First name", firstNameField);
        addRow(grid, 1, "Last name", lastNameField);
        addRow(grid, 2, "Email", emailField);
        addRow(grid, 3, "Phone", phoneField);
        addRow(grid, 4, "Class", classNameField);

        HBox buttons = new HBox(8, addButton, updateButton, deleteButton, clearButton);
        VBox panel = ViewFactory.panel(14);
        panel.setPrefWidth(360);
        panel.getChildren().addAll(ViewFactory.sectionTitle("Student Form"), grid, buttons);
        BorderPane.setMargin(panel, new Insets(0, 0, 0, 14));
        return panel;
    }

    private void addRow(GridPane grid, int row, String label, TextField field) {
        grid.add(new Label(label), 0, row);
        grid.add(field, 1, row);
    }
}
