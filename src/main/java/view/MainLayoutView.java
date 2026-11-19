package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainLayoutView extends BorderPane {
    private final Button dashboardButton = navButton("Dashboard");
    private final Button studentsButton = navButton("Students");
    private final Button teachersButton = navButton("Teachers");
    private final Button modulesButton = navButton("Modules");
    private final Button subjectsButton = navButton("Subjects");
    private final Button gradesButton = navButton("Grades");
    private final Button importButton = navButton("Import");
    private final Button exportButton = navButton("Export PDF");
    private final StackPane contentPane = new StackPane();

    public MainLayoutView() {
        getStyleClass().add("root");
        setTop(createTopBar());
        setLeft(createSidebar());
        contentPane.getStyleClass().add("content");
        setCenter(contentPane);
    }

    public void setPage(Node node) {
        contentPane.getChildren().setAll(node);
    }

    public Button getDashboardButton() {
        return dashboardButton;
    }

    public Button getStudentsButton() {
        return studentsButton;
    }

    public Button getTeachersButton() {
        return teachersButton;
    }

    public Button getModulesButton() {
        return modulesButton;
    }

    public Button getSubjectsButton() {
        return subjectsButton;
    }

    public Button getGradesButton() {
        return gradesButton;
    }

    public Button getImportButton() {
        return importButton;
    }

    public Button getExportButton() {
        return exportButton;
    }

    private HBox createTopBar() {
        Label title = new Label("JScolaris");
        title.getStyleClass().add("app-title");
        Label subtitle = ViewFactory.muted("Desktop school management");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(12, title, subtitle, spacer);
        topBar.getStyleClass().add("top-bar");
        topBar.setAlignment(Pos.CENTER_LEFT);
        return topBar;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(
                dashboardButton,
                studentsButton,
                teachersButton,
                modulesButton,
                subjectsButton,
                gradesButton,
                importButton,
                exportButton
        );
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPadding(new Insets(18, 12, 18, 12));
        return sidebar;
    }

    private Button navButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("nav-button");
        return button;
    }
}
