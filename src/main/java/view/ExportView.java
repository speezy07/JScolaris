package view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Student;

public class ExportView extends BorderPane {
    private final ComboBox<Student> studentComboBox = new ComboBox<>();
    private final TextField outputDirectoryField = ViewFactory.textField("Output directory");
    private final Button chooseDirectoryButton = ViewFactory.secondaryButton("Choose Folder");
    private final Button exportButton = ViewFactory.primaryButton("Generate PDF");
    private final TextArea previewArea = new TextArea();

    public ExportView() {
        getStyleClass().add("content");
        studentComboBox.setPromptText("Select student");
        studentComboBox.setPrefWidth(300);
        outputDirectoryField.setPrefWidth(460);
        previewArea.setEditable(false);
        previewArea.setPrefRowCount(18);
        setTop(createHeader());
        setCenter(createPanel());
    }

    public ComboBox<Student> getStudentComboBox() {
        return studentComboBox;
    }

    public TextField getOutputDirectoryField() {
        return outputDirectoryField;
    }

    public Button getChooseDirectoryButton() {
        return chooseDirectoryButton;
    }

    public Button getExportButton() {
        return exportButton;
    }

    public TextArea getPreviewArea() {
        return previewArea;
    }

    private VBox createHeader() {
        VBox header = new VBox(10, ViewFactory.pageTitle("Export Report Card"));
        BorderPane.setMargin(header, new Insets(0, 0, 14, 0));
        return header;
    }

    private VBox createPanel() {
        HBox studentRow = new HBox(8, studentComboBox, exportButton);
        HBox folderRow = new HBox(8, outputDirectoryField, chooseDirectoryButton);
        VBox panel = ViewFactory.panel(12);
        panel.getChildren().addAll(
                ViewFactory.sectionTitle("Bulletin PDF"),
                studentRow,
                folderRow,
                previewArea
        );
        return panel;
    }
}
