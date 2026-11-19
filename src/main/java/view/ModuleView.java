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
import model.SchoolModule;

public class ModuleView extends BorderPane {
    private final TableView<SchoolModule> table = new TableView<>();
    private final TextField moduleNameField = ViewFactory.textField("Module name");
    private final TextField coefficientField = ViewFactory.textField("Coefficient");
    private final Button addButton = ViewFactory.primaryButton("Add");
    private final Button updateButton = ViewFactory.secondaryButton("Update");
    private final Button deleteButton = ViewFactory.dangerButton("Delete");
    private final Button clearButton = ViewFactory.secondaryButton("Clear");
    private final Button refreshButton = ViewFactory.secondaryButton("Refresh");

    public ModuleView() {
        getStyleClass().add("content");
        setTop(createHeader());
        setCenter(createTablePanel());
        setRight(createFormPanel());
    }

    public TableView<SchoolModule> getTable() {
        return table;
    }

    public TextField getModuleNameField() {
        return moduleNameField;
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
        VBox header = new VBox(10, ViewFactory.pageTitle("Module Management"), row);
        BorderPane.setMargin(header, new Insets(0, 0, 14, 0));
        return header;
    }

    private VBox createTablePanel() {
        table.getColumns().add(ViewFactory.<SchoolModule, Integer>column("ID", "id"));
        table.getColumns().add(ViewFactory.<SchoolModule, String>column("Module Name", "moduleName"));
        table.getColumns().add(ViewFactory.<SchoolModule, Double>column("Coefficient", "coefficient"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox panel = ViewFactory.panel(12);
        panel.getChildren().addAll(ViewFactory.sectionTitle("Modules"), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return panel;
    }

    private VBox createFormPanel() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        addRow(grid, 0, "Name", moduleNameField);
        addRow(grid, 1, "Coefficient", coefficientField);

        HBox buttons = new HBox(8, addButton, updateButton, deleteButton, clearButton);
        VBox panel = ViewFactory.panel(14);
        panel.setPrefWidth(350);
        panel.getChildren().addAll(ViewFactory.sectionTitle("Module Form"), grid, buttons);
        BorderPane.setMargin(panel, new Insets(0, 0, 0, 14));
        return panel;
    }

    private void addRow(GridPane grid, int row, String label, TextField field) {
        grid.add(new Label(label), 0, row);
        grid.add(field, 1, row);
    }
}
