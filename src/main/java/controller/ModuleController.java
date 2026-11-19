package controller;

import dao.ModuleDao;
import javafx.collections.FXCollections;
import model.SchoolModule;
import util.AlertUtil;
import util.ValidationUtil;
import view.ModuleView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModuleController {
    private final ModuleView view = new ModuleView();
    private final ModuleDao moduleDao = new ModuleDao();

    public ModuleController() {
        configureActions();
        configureSelection();
        loadModules();
    }

    public ModuleView getView() {
        return view;
    }

    private void configureActions() {
        view.getAddButton().setOnAction(event -> addModule());
        view.getUpdateButton().setOnAction(event -> updateModule());
        view.getDeleteButton().setOnAction(event -> deleteModule());
        view.getClearButton().setOnAction(event -> clearForm());
        view.getRefreshButton().setOnAction(event -> loadModules());
    }

    private void configureSelection() {
        view.getTable().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> {
            if (selected != null) {
                view.getModuleNameField().setText(selected.getModuleName());
                view.getCoefficientField().setText(String.valueOf(selected.getCoefficient()));
            }
        });
    }

    private void loadModules() {
        try {
            view.getTable().setItems(FXCollections.observableArrayList(moduleDao.findAll()));
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private void addModule() {
        List<String> errors = new ArrayList<>();
        SchoolModule module = readForm(0, errors);
        errors.addAll(ValidationUtil.validateModule(module));
        if (!errors.isEmpty()) {
            AlertUtil.showWarning("Invalid module", ValidationUtil.joinErrors(errors));
            return;
        }
        try {
            moduleDao.create(module);
            loadModules();
            clearForm();
            AlertUtil.showInfo("Module added", "The module was added successfully.");
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private void updateModule() {
        SchoolModule selected = view.getTable().getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("No selection", "Select a module to update.");
            return;
        }
        List<String> errors = new ArrayList<>();
        SchoolModule module = readForm(selected.getId(), errors);
        errors.addAll(ValidationUtil.validateModule(module));
        if (!errors.isEmpty()) {
            AlertUtil.showWarning("Invalid module", ValidationUtil.joinErrors(errors));
            return;
        }
        try {
            moduleDao.update(module);
            loadModules();
            clearForm();
            AlertUtil.showInfo("Module updated", "The module was updated successfully.");
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private void deleteModule() {
        SchoolModule selected = view.getTable().getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("No selection", "Select a module to delete.");
            return;
        }
        if (!AlertUtil.confirm("Delete module", "Delete " + selected.getModuleName() + "?")) {
            return;
        }
        try {
            moduleDao.delete(selected.getId());
            loadModules();
            clearForm();
            AlertUtil.showInfo("Module deleted", "The module was deleted successfully.");
        } catch (SQLException exception) {
            AlertUtil.showSqlError(exception);
        }
    }

    private SchoolModule readForm(int id, List<String> errors) {
        double coefficient = ValidationUtil.parseDouble(view.getCoefficientField().getText(), "Coefficient", errors);
        return new SchoolModule(
                id,
                ValidationUtil.clean(view.getModuleNameField().getText()),
                coefficient
        );
    }

    private void clearForm() {
        view.getTable().getSelectionModel().clearSelection();
        view.getModuleNameField().clear();
        view.getCoefficientField().clear();
    }
}
