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
import model.Teacher;

public class TeacherView extends BorderPane {
    private final TableView<Teacher> table = new TableView<>();
    private final TextField searchField = ViewFactory.textField("Search teacher");
    private final TextField firstNameField = ViewFactory.textField("First name");
    private final TextField lastNameField = ViewFactory.textField("Last name");
    private final TextField specialtyField = ViewFactory.textField("Specialty");
    private final TextField emailField = ViewFactory.textField("Email");
    private final Button addButton = ViewFactory.primaryButton("Add");
    private final Button updateButton = ViewFactory.secondaryButton("Update");
    private final Button deleteButton = ViewFactory.dangerButton("Delete");
    private final Button clearButton = ViewFactory.secondaryButton("Clear");
    private final Button searchButton = ViewFactory.secondaryButton("Search");
    private final Button refreshButton = ViewFactory.secondaryButton("Refresh");

    public TeacherView() {
        getStyleClass().add("content");
        setTop(createHeader());
        setCenter(createTablePanel());
        setRight(createFormPanel());
    }

    public TableView<Teacher> getTable() {
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

    public TextField getSpecialtyField() {
        return specialtyField;
    }

    public TextField getEmailField() {
        return emailField;
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
        VBox header = new VBox(10, ViewFactory.pageTitle("Teacher Management"), searchRow);
        BorderPane.setMargin(header, new Insets(0, 0, 14, 0));
        return header;
    }

    private VBox createTablePanel() {
        table.getColumns().add(ViewFactory.<Teacher, Integer>column("ID", "id"));
        table.getColumns().add(ViewFactory.<Teacher, String>column("First Name", "firstName"));
        table.getColumns().add(ViewFactory.<Teacher, String>column("Last Name", "lastName"));
        table.getColumns().add(ViewFactory.<Teacher, String>column("Specialty", "specialty"));
        table.getColumns().add(ViewFactory.<Teacher, String>column("Email", "email"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox panel = ViewFactory.panel(12);
        panel.getChildren().addAll(ViewFactory.sectionTitle("Teachers"), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return panel;
    }

    private VBox createFormPanel() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        addRow(grid, 0, "First name", firstNameField);
        addRow(grid, 1, "Last name", lastNameField);
        addRow(grid, 2, "Specialty", specialtyField);
        addRow(grid, 3, "Email", emailField);

        HBox buttons = new HBox(8, addButton, updateButton, deleteButton, clearButton);
        VBox panel = ViewFactory.panel(14);
        panel.setPrefWidth(360);
        panel.getChildren().addAll(ViewFactory.sectionTitle("Teacher Form"), grid, buttons);
        BorderPane.setMargin(panel, new Insets(0, 0, 0, 14));
        return panel;
    }

    private void addRow(GridPane grid, int row, String label, TextField field) {
        grid.add(new Label(label), 0, row);
        grid.add(field, 1, row);
    }
}
