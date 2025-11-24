package com.example.tabassum_2207034_cvbuilder;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;


public class HomeController {

    private MainApp mainApp;
    private CVService cvService = new CVService();

    @FXML private Button createBtn;
    @FXML private Button viewRecordsBtn;
    @FXML private Text welcomeText;
    @FXML private TableView<CVModel> cvTableView;
    @FXML private TableColumn<CVModel, Integer> idColumn;
    @FXML private TableColumn<CVModel, String> nameColumn;
    @FXML private TableColumn<CVModel, String> emailColumn;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;
    @FXML private Button previewBtn;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        welcomeText.setText("Welcome to CV Builder\\nCreate a professional CV quickly using an interactive form.");
        
        // Setup table if present
        if (cvTableView != null && idColumn != null && nameColumn != null && emailColumn != null) {
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameColumn.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());
            emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
            
            if (editBtn != null) editBtn.setDisable(true);
            if (deleteBtn != null) deleteBtn.setDisable(true);
            if (previewBtn != null) previewBtn.setDisable(true);
            
            cvTableView.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
                boolean selected = newVal != null;
                if (editBtn != null) editBtn.setDisable(!selected);
                if (deleteBtn != null) deleteBtn.setDisable(!selected);
                if (previewBtn != null) previewBtn.setDisable(!selected);
            });
        }
    }

    @FXML
    private void onCreateNewCV() {
        if (mainApp != null) {
            mainApp.showCreate(null);
        }
    }

    @FXML
    private void onViewRecords() {
        if (cvTableView == null) return;
        
        var task = cvService.createLoadAllTask();
        task.setOnSucceeded(e -> {
            ObservableList<CVModel> cvList = task.getValue();
            cvTableView.setItems(cvList);
            if (cvList.isEmpty()) {
                showInfo("No Records", "No saved CV records found.");
            } else {
                showInfo("Records Loaded", "Loaded " + cvList.size() + " CV record(s).");
            }
        });
        task.setOnFailed(e -> showError("Error", "Failed to load records: " + task.getException().getMessage()));
        new Thread(task).start();
    }

    @FXML
    private void onEditCV() {
        CVModel selected = cvTableView.getSelectionModel().getSelectedItem();
        if (selected != null && mainApp != null) {
            mainApp.showCreate(selected);
        }
    }

    @FXML
    private void onDeleteCV() {
        CVModel selected = cvTableView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete CV");
        confirm.setHeaderText("Delete CV for " + selected.fullName + "?");
        confirm.setContentText("This action cannot be undone.");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                var task = cvService.createDeleteTask(selected.id);
                task.setOnSucceeded(e -> {
                    if (task.getValue()) {
                        showInfo("Deleted", "CV deleted successfully.");
                        onViewRecords();
                    } else {
                        showError("Error", "Failed to delete CV.");
                    }
                });
                task.setOnFailed(e -> showError("Error", "Error: " + task.getException().getMessage()));
                new Thread(task).start();
            }
        });
    }

    @FXML
    private void onPreviewCV() {
        CVModel selected = cvTableView.getSelectionModel().getSelectedItem();
        if (selected != null && mainApp != null) {
            mainApp.showPreview(selected);
        }
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
