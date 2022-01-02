package com.example.family_tree;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("create.fxml"));
        stage.setScene(new Scene(root,800,600));
        stage.setTitle("Family Tree Application");
       // stage.initStyle(StageStyle.DECORATED);

        stage.show();
        stage.setResizable(true);

    }

    public static void main(String[] args) {
        launch();
    }
}