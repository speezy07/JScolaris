package controller;

import view.MainLayoutView;

public class MainController {
    private final MainLayoutView view = new MainLayoutView();

    public MainController() {
        configureNavigation();
        showDashboard();
    }

    public MainLayoutView getView() {
        return view;
    }

    private void configureNavigation() {
        view.getDashboardButton().setOnAction(event -> showDashboard());
        view.getStudentsButton().setOnAction(event -> view.setPage(new StudentController().getView()));
        view.getTeachersButton().setOnAction(event -> view.setPage(new TeacherController().getView()));
        view.getModulesButton().setOnAction(event -> view.setPage(new ModuleController().getView()));
        view.getSubjectsButton().setOnAction(event -> view.setPage(new SubjectController().getView()));
        view.getGradesButton().setOnAction(event -> view.setPage(new GradeController().getView()));
        view.getImportButton().setOnAction(event -> view.setPage(new ImportController().getView()));
        view.getExportButton().setOnAction(event -> view.setPage(new ExportController().getView()));
    }

    private void showDashboard() {
        DashboardController dashboardController = new DashboardController();
        view.setPage(dashboardController.getView());
    }
}
