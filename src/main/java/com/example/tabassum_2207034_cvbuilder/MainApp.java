package com.example.tabassum_2207034_cvbuilder;

import com.example.tabassum_2207034_cvbuilder.CVModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class MainApp extends Application {

    private Stage primaryStage;
    //private static final String STYLE = "/com/example/tabassum_2207034_cvbuilder/style.css";

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("CV Builder");
        showHome();
        primaryStage.show();
    }

    public void showHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tabassum_2207034_cvbuilder/home.fxml"));
            Parent root = loader.load();
            com.example.tabassum_2207034_cvbuilder.HomeController controller = loader.getController();
            controller.setMainApp(this);
            Scene scene = new Scene(root, 900, 650);
            scene.getStylesheets().add(getClass().getResource("/com/example/tabassum_2207034_cvbuilder/style.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showCreate(CVModel model) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tabassum_2207034_cvbuilder/create.fxml"));
            Parent root = loader.load();
            com.example.tabassum_2207034_cvbuilder.CreateController controller = loader.getController();
            controller.setMainApp(this);
            if (model != null) controller.setModel(model);
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/com/example/tabassum_2207034_cvbuilder/style.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPreview(CVModel model) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tabassum_2207034_cvbuilder/preview.fxml"));
            Parent root = loader.load();
            com.example.tabassum_2207034_cvbuilder.PreviewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setModel(model);
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/com/example/tabassum_2207034_cvbuilder/style.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
