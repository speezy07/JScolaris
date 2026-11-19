package view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Student;

public class ImportView extends BorderPane {
    private final TextField filePathField = ViewFactory.textField("Selected file");
    private final Button chooseCsvButton = ViewFactory.secondaryButton("Choose CSV");
    private final Button chooseExcelButton = ViewFactory.secondaryButton("Choose Excel");
    private final Button importButton = ViewFactory.primaryButton("Import Students");
    private final TableView<Student> previewTable = new TableView<>();
    private final TextArea logArea = new TextArea();

    public ImportView() {
        getStyleClass().add("content");
        filePathField.setEditable(false);
        filePathField.setPrefWidth(480);
        logArea.setEditable(false);
        logArea.setPrefRowCount(8);
        setTop(createHeader());
        setCenter(createPreviewPanel());
        setBottom(createLogPanel());
    }

    public TextField getFilePathField() {
        return filePathField;
    }

    public Button getChooseCsvButton() {
        return chooseCsvButton;
    }

    public Button getChooseExcelButton() {
        return chooseExcelButton;
    }

    public Button getImportButton() {
        return importButton;
    }

    public TableView<Student> getPreviewTable() {
        return previewTable;
    }

    public TextArea getLogArea() {
        return logArea;
    }

    private VBox createHeader() {
        HBox row = new HBox(8, filePathField, chooseCsvButton, chooseExcelButton, importButton);
        VBox header = new VBox(10, ViewFactory.pageTitle("Import Students"), row);
        BorderPane.setMargin(header, new Insets(0, 0, 14, 0));
        return header;
    }

    private VBox createPreviewPanel() {
        previewTable.getColumns().add(ViewFactory.<Student, String>column("First Name", "firstName"));
        previewTable.getColumns().add(ViewFactory.<Student, String>column("Last Name", "lastName"));
        previewTable.getColumns().add(ViewFactory.<Student, String>column("Email", "email"));
        previewTable.getColumns().add(ViewFactory.<Student, String>column("Phone", "phone"));
        previewTable.getColumns().add(ViewFactory.<Student, String>column("Class", "className"));
        previewTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox panel = ViewFactory.panel(12);
        panel.getChildren().addAll(ViewFactory.sectionTitle("Preview Valid Rows"), previewTable);
        VBox.setVgrow(previewTable, Priority.ALWAYS);
        return panel;
    }

    private VBox createLogPanel() {
        VBox panel = ViewFactory.panel(10);
        panel.getChildren().addAll(ViewFactory.sectionTitle("Import Log"), logArea);
        BorderPane.setMargin(panel, new Insets(14, 0, 0, 0));
        return panel;
    }
}
