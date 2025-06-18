package com.example.hospitalmanagementsystem;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class Register extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        // Create and show the splash screen
        Stage splashStage = new Stage();
        FXMLLoader splashLoader = new FXMLLoader(Register.class.getResource("splash.fxml"));
        Scene splashScene = new Scene(splashLoader.load());
        splashScene.setFill(Color.TRANSPARENT);
        splashStage.setScene(splashScene);
        splashStage.initStyle(StageStyle.TRANSPARENT);
        splashStage.setAlwaysOnTop(true);
        splashStage.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(5)); // Show splash for 3 seconds
        delay.setOnFinished(event -> {
            splashStage.hide();

            try {
                FXMLLoader mainLoader = new FXMLLoader(Register.class.getResource("FXMLDocument.fxml"));
                Scene mainScene = new Scene(mainLoader.load(), 330, 550);
                primaryStage.setTitle("United Healthcare Institute");
                primaryStage.setScene(mainScene);
                primaryStage.setMinWidth(340);
                primaryStage.setMinHeight(580);
                primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        delay.play();
    }

    public static void main(String[] args) {
        launch();
    }
}