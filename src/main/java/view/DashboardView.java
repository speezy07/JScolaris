package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DashboardView extends VBox {
    private final Label studentCountLabel = numberLabel();
    private final Label teacherCountLabel = numberLabel();
    private final Label moduleCountLabel = numberLabel();
    private final Label subjectCountLabel = numberLabel();
    private final Label gradeCountLabel = numberLabel();

    public DashboardView() {
        setSpacing(18);
        getChildren().addAll(
                ViewFactory.pageTitle("Dashboard"),
                ViewFactory.muted("Overview of the school database."),
                createCardsRow()
        );
    }

    public void setCounts(int students, int teachers, int modules, int subjects, int grades) {
        studentCountLabel.setText(String.valueOf(students));
        teacherCountLabel.setText(String.valueOf(teachers));
        moduleCountLabel.setText(String.valueOf(modules));
        subjectCountLabel.setText(String.valueOf(subjects));
        gradeCountLabel.setText(String.valueOf(grades));
    }

    public void showDatabaseUnavailable() {
        studentCountLabel.setText("-");
        teacherCountLabel.setText("-");
        moduleCountLabel.setText("-");
        subjectCountLabel.setText("-");
        gradeCountLabel.setText("-");
    }

    private HBox createCardsRow() {
        HBox row = new HBox(14,
                card("Students", studentCountLabel),
                card("Teachers", teacherCountLabel),
                card("Modules", moduleCountLabel),
                card("Subjects", subjectCountLabel),
                card("Grades", gradeCountLabel)
        );
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private VBox card(String title, Label numberLabel) {
        VBox card = new VBox(8, ViewFactory.muted(title), numberLabel);
        card.getStyleClass().add("dashboard-card");
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    private Label numberLabel() {
        Label label = new Label("0");
        label.getStyleClass().add("card-number");
        return label;
    }
}
