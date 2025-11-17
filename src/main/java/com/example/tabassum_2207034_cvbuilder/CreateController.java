package com.example.tabassum_2207034_cvbuilder;

import com.example.tabassum_2207034_cvbuilder.MainApp;
import com.example.tabassum_2207034_cvbuilder.CVModel;
//import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

//import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;


public class CreateController {

    private MainApp mainApp;
    private CVModel model = new CVModel();

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextArea addressArea;
    @FXML private TextArea skillsArea;
    @FXML private TextArea projectsArea;
    @FXML private VBox educationBox;
    @FXML private VBox experienceBox;
    @FXML private Button addEducationBtn;
    @FXML private Button addExperienceBtn;
    @FXML private Button uploadPhotoBtn;
    @FXML private ImageView profileImageView;
    @FXML private Button generateBtn;
    @FXML private Button cancelBtn;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }


    public void setModel(CVModel model) {
        if (model == null) return;
        this.model = model;
        fullNameField.setText(model.fullName);
        emailField.setText(model.email);
        phoneField.setText(model.phone);
        addressArea.setText(model.address);
        if (model.skills != null) {
            skillsArea.setText(String.join(", ", model.skills));
        }
        if (model.projects != null) {
            projectsArea.setText(String.join("\n", model.projects));
        }
        // image optionalll
        if (model.profileImage != null) {
            profileImageView.setImage(model.profileImage);
        }

        educationBox.getChildren().clear();
        for (CVModel.Education e : model.educations) {
            addEducationNode(e);
        }
        experienceBox.getChildren().clear();
        for (CVModel.Experience ex : model.experiences) {
            addExperienceNode(ex);
        }
    }

    @FXML
    private void initialize() {

        addEducationNode(null);
        addExperienceNode(null);
    }

    @FXML
    private void onAddEducation() {
        addEducationNode(null);
    }

    private void addEducationNode(CVModel.Education prefill) {
        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(6);
        gp.setPadding(new Insets(8));
        TextField degree = new TextField();
        degree.setPromptText("Degree / Qualification");
        TextField institution = new TextField();
        institution.setPromptText("Institution");
        TextField year = new TextField();
        year.setPromptText("Year");
        TextArea details = new TextArea();
        details.setPromptText("Details / Notes");
        details.setPrefRowCount(2);
        Button remove = new Button("Remove");
        remove.setOnAction(e -> educationBox.getChildren().remove(gp));
        gp.add(new Label("Degree:"), 0, 0);
        gp.add(degree, 1, 0);
        gp.add(new Label("Institution:"), 0, 1);
        gp.add(institution, 1, 1);
        gp.add(new Label("Year:"), 0, 2);
        gp.add(year, 1, 2);
        gp.add(new Label("Details:"), 0, 3);
        gp.add(details, 1, 3);
        gp.add(remove, 1, 4);

        if (prefill != null) {
            degree.setText(prefill.degree);
            institution.setText(prefill.institution);
            year.setText(prefill.year);
            details.setText(prefill.details);
        }

        educationBox.getChildren().add(gp);
    }

    @FXML
    private void onAddExperience() {
        addExperienceNode(null);
    }

    private void addExperienceNode(CVModel.Experience prefill) {
        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(6);
        gp.setPadding(new Insets(8));
        TextField title = new TextField();
        title.setPromptText("Job Title");
        TextField company = new TextField();
        company.setPromptText("Company");
        TextField duration = new TextField();
        duration.setPromptText("Duration (e.g., 2019-2021)");
        TextArea details = new TextArea();
        details.setPromptText("Details / Responsibilities");
        details.setPrefRowCount(2);
        Button remove = new Button("Remove");
        remove.setOnAction(e -> experienceBox.getChildren().remove(gp));
        gp.add(new Label("Title:"), 0, 0);
        gp.add(title, 1, 0);
        gp.add(new Label("Company:"), 0, 1);
        gp.add(company, 1, 1);
        gp.add(new Label("Duration:"), 0, 2);
        gp.add(duration, 1, 2);
        gp.add(new Label("Details:"), 0, 3);
        gp.add(details, 1, 3);
        gp.add(remove, 1, 4);

        if (prefill != null) {
            title.setText(prefill.title);
            company.setText(prefill.company);
            duration.setText(prefill.duration);
            details.setText(prefill.details);
        }

        experienceBox.getChildren().add(gp);
    }

    // alerttt er jonno method thakbe ekhanee

    @FXML
    private void onGenerate() {
        if (!validate()) {
            showErrorAlert("Validation Error", "Please fill in all required fields:\n- Full Name\n- Email");
            return;
        }
        collectToModel();
        showSuccessAlert("CV Generated!", "Your CV has been generated successfully and is ready for preview.");
        mainApp.showPreview(model);
    }



    @FXML
    private void onCancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setContentText("All unsaved changes will be lost.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                mainApp.showHome();
            }
        });
    }

    private boolean validate() {
        String name = fullNameField.getText();
        String email = emailField.getText();
        if (name == null || name.isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Full name is required.");
            return false;
        }
        if (email == null || email.isBlank() || !email.contains("@")) {
            showAlert(Alert.AlertType.ERROR, "A valid email is required.");
            return false;
        }
        return true;
    }

    private void collectToModel() {
        model.fullName = fullNameField.getText().trim();
        model.email = emailField.getText().trim();
        model.phone = phoneField.getText().trim();
        model.address = addressArea.getText().trim();
        //model.profileImage = (profileImageView != null) ? profileImageView.getImage() : null;


        String skillsText = skillsArea.getText();
        model.skills = Arrays.stream(skillsText.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());


        model.projects = Arrays.stream(projectsArea.getText().split("\\r?\\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

       // degree, instition
        model.educations.clear();
        for (var node : educationBox.getChildren()) {
            if (!(node instanceof GridPane)) continue;
            GridPane gp = (GridPane) node;
            CVModel.Education e = new CVModel.Education();
            var degree = (TextField) gp.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == 1 && GridPane.getRowIndex(n) == 0).findFirst().orElse(null);
            var institution = (TextField) gp.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == 1 && GridPane.getRowIndex(n) == 1).findFirst().orElse(null);
            var year = (TextField) gp.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == 1 && GridPane.getRowIndex(n) == 2).findFirst().orElse(null);
            var details = (TextArea) gp.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == 1 && GridPane.getRowIndex(n) == 3).findFirst().orElse(null);
            e.degree = degree != null ? degree.getText().trim() : "";
            e.institution = institution != null ? institution.getText().trim() : "";
            e.year = year != null ? year.getText().trim() : "";
            e.details = details != null ? details.getText().trim() : "";
            if (!e.degree.isBlank() || !e.institution.isBlank()) model.educations.add(e);
        }

        // title, company bla bla...
        model.experiences.clear();
        for (var node : experienceBox.getChildren()) {
            if (!(node instanceof GridPane)) continue;
            GridPane gp = (GridPane) node;
            CVModel.Experience ex = new CVModel.Experience();
            var title = (TextField) gp.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == 1 && GridPane.getRowIndex(n) == 0).findFirst().orElse(null);
            var company = (TextField) gp.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == 1 && GridPane.getRowIndex(n) == 1).findFirst().orElse(null);
            var duration = (TextField) gp.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == 1 && GridPane.getRowIndex(n) == 2).findFirst().orElse(null);
            var details = (TextArea) gp.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == 1 && GridPane.getRowIndex(n) == 3).findFirst().orElse(null);
            ex.title = title != null ? title.getText().trim() : "";
            ex.company = company != null ? company.getText().trim() : "";
            ex.duration = duration != null ? duration.getText().trim() : "";
            ex.details = details != null ? details.getText().trim() : "";
            if (!ex.title.isBlank() || !ex.company.isBlank()) model.experiences.add(ex);
        }
    }

    // alert dite hobe ekhane
    private void showAlert(Alert.AlertType type, String msg) {
        Alert a = new Alert(type);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
    
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}