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
import model.SchoolModule;
import model.Subject;

public class SubjectView extends BorderPane {
    private final TableView<Subject> table = new TableView<>();
    private final TextField subjectNameField = ViewFactory.textField("Subject name");
    private final ComboBox<SchoolModule> moduleComboBox = new ComboBox<>();
    private final TextField coefficientField = ViewFactory.textField("Coefficient");
    private final Button addButton = ViewFactory.primaryButton("Add");
    private final Button updateButton = ViewFactory.secondaryButton("Update");
    private final Button deleteButton = ViewFactory.dangerButton("Delete");
    private final Button clearButton = ViewFactory.secondaryButton("Clear");
    private final Button refreshButton = ViewFactory.secondaryButton("Refresh");

    public SubjectView() {
        getStyleClass().add("content");
        moduleComboBox.setPromptText("Select module");
        moduleComboBox.setPrefWidth(230);
        setTop(createHeader());
        setCenter(createTablePanel());
        setRight(createFormPanel());
    }

    public TableView<Subject> getTable() {
        return table;
    }

    public TextField getSubjectNameField() {
        return subjectNameField;
    }

    public ComboBox<SchoolModule> getModuleComboBox() {
        return moduleComboBox;
    }

    public TextField getCoefficientField() {
        return coefficientField;
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

    public Button getRefreshButton() {
        return refreshButton;
    }

    private VBox createHeader() {
        HBox row = new HBox(8, refreshButton);
        VBox header = new VBox(10, ViewFactory.pageTitle("Subject Management"), row);
        BorderPane.setMargin(header, new Insets(0, 0, 14, 0));
        return header;
    }

    private VBox createTablePanel() {
        table.getColumns().add(ViewFactory.<Subject, Integer>column("ID", "id"));
        table.getColumns().add(ViewFactory.<Subject, String>column("Subject Name", "subjectName"));
        table.getColumns().add(ViewFactory.<Subject, Integer>column("Module ID", "moduleId"));
        table.getColumns().add(ViewFactory.<Subject, Double>column("Coefficient", "coefficient"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox panel = ViewFactory.panel(12);
        panel.getChildren().addAll(ViewFactory.sectionTitle("Subjects"), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return panel;
    }

    private VBox createFormPanel() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        addTextRow(grid, 0, "Name", subjectNameField);
        grid.add(new Label("Module"), 0, 1);
        grid.add(moduleComboBox, 1, 1);
        addTextRow(grid, 2, "Coefficient", coefficientField);

        HBox buttons = new HBox(8, addButton, updateButton, deleteButton, clearButton);
        VBox panel = ViewFactory.panel(14);
        panel.setPrefWidth(370);
        panel.getChildren().addAll(ViewFactory.sectionTitle("Subject Form"), grid, buttons);
        BorderPane.setMargin(panel, new Insets(0, 0, 0, 14));
        return panel;
    }

    private void addTextRow(GridPane grid, int row, String label, TextField field) {
        grid.add(new Label(label), 0, row);
        grid.add(field, 1, row);
    }
}
