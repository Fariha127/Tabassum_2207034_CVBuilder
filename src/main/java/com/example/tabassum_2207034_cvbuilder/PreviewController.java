package com.example.tabassum_2207034_cvbuilder;

import com.example.tabassum_2207034_cvbuilder.MainApp;
import com.example.tabassum_2207034_cvbuilder.CVModel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.stream.Collectors;


public class PreviewController {

    private MainApp mainApp;
    private CVModel model;

    @FXML private BorderPane rootPane;
    @FXML private VBox leftColumn;
    @FXML private VBox rightColumn;
    @FXML private Button backBtn;
    @FXML private Button homeBtn;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setModel(CVModel model) {
        this.model = model;
        render();
    }

    @FXML
    private void initialize() {
        leftColumn.setSpacing(10);
        leftColumn.setPadding(new Insets(12));
        rightColumn.setSpacing(10);
        rightColumn.setPadding(new Insets(12));
    }

    private void render() {
        leftColumn.getChildren().clear();
        rightColumn.getChildren().clear();


        Label nameLabel = new Label(model.fullName != null ? model.fullName : "");
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        Label contactLabel = new Label(
                (model.email != null ? model.email : "") +
                        (model.phone != null && !model.phone.isBlank() ? " | " + model.phone : "") +
                        (model.address != null && !model.address.isBlank() ? "\n" + model.address : "")
        );
        contactLabel.setWrapText(true);

        VBox header = new VBox(4, nameLabel, contactLabel, new Separator());
        header.setPadding(new Insets(12));
        rootPane.setTop(header);


        if (model.profileImage != null) {
            ImageView iv = new ImageView(model.profileImage);
            iv.setFitWidth(150);
            iv.setFitHeight(150);
            iv.setPreserveRatio(true);
            leftColumn.getChildren().add(iv);
        }

        Label skillsTitle = new Label("Skills");
        skillsTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        leftColumn.getChildren().add(skillsTitle);
        String skillsText = model.skills != null && !model.skills.isEmpty() ?
                model.skills.stream().collect(Collectors.joining(", ")) :
                "";
        Label skills = new Label(skillsText);
        skills.setWrapText(true);
        leftColumn.getChildren().add(skills);
        leftColumn.getChildren().add(new Separator());


        if (model.projects != null && !model.projects.isEmpty()) {
            Label projTitle = new Label("Projects");
            projTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
            leftColumn.getChildren().add(projTitle);
            for (String p : model.projects) {
                Label pl = new Label("\u2022 " + p);
                pl.setWrapText(true);
                leftColumn.getChildren().add(pl);
            }
            leftColumn.getChildren().add(new Separator());
        }


        Label eduTitle = new Label("Education");
        eduTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        rightColumn.getChildren().add(eduTitle);
        for (CVModel.Education e : model.educations) {
            VBox box = new VBox(2);
            Label line1 = new Label((e.degree != null ? e.degree : "") +
                    (e.year != null && !e.year.isBlank() ? " • " + e.year : ""));
            line1.setFont(Font.font("System", FontWeight.BOLD, 13));
            Label line2 = new Label((e.institution != null ? e.institution : ""));
            line2.setStyle("-fx-font-style: italic;");
            Label desc = new Label(e.details != null ? e.details : "");
            desc.setWrapText(true);
            box.getChildren().addAll(line1, line2, desc);
            rightColumn.getChildren().add(box);
        }
        rightColumn.getChildren().add(new Separator());

        Label expTitle = new Label("Work Experience");
        expTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        rightColumn.getChildren().add(expTitle);
        for (CVModel.Experience ex : model.experiences) {
            VBox box = new VBox(2);
            Label line1 = new Label((ex.title != null ? ex.title : "") +
                    (ex.duration != null && !ex.duration.isBlank() ? " • " + ex.duration : ""));
            line1.setFont(Font.font("System", FontWeight.BOLD, 13));
            Label line2 = new Label((ex.company != null ? ex.company : ""));
            line2.setStyle("-fx-font-style: italic;");
            Label desc = new Label(ex.details != null ? ex.details : "");
            desc.setWrapText(true);
            box.getChildren().addAll(line1, line2, desc);
            rightColumn.getChildren().add(box);
        }
        rightColumn.getChildren().add(new Separator());


    }

    @FXML
    private void onBack() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Return to Edit");
        alert.setHeaderText("Do you want to go back and edit your CV?");
        alert.setContentText("You can make changes to your CV information.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                mainApp.showCreate(model);
            }
        });
    }

    @FXML
    private void onHome() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Return to Home");
        alert.setHeaderText("Are you sure you want to return to home?");
        alert.setContentText("Your current CV preview will be lost if not saved.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                showSuccessAlert("CV Completed", "Your CV has been successfully created!");
                mainApp.showHome();
            }
        });
    }
    
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
