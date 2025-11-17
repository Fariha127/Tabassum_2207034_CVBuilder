package com.example.tabassum_2207034_cvbuilder;

import com.example.tabassum_2207034_cvbuilder.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;


public class HomeController {

    private MainApp mainApp;

    @FXML
    private Button createBtn;

    @FXML
    private Text welcomeText;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        welcomeText.setText("Welcome to CV Builder\nCreate a professional CV quickly using an interactive form.");
    }

    @FXML
    private void onCreateNewCV() {
        if (mainApp != null) {
            mainApp.showCreate(null);
        }
    }
}
