package com.example.family_tree;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

public class Controller {
    Stage stage;
    Scene scene;
    Parent root;

    @FXML
    //Button createButton, importButton;

    public void importButton(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("import.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 750, 750);
        stage.setScene(scene);
        stage.show();
    }
    public void helpButton(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("help.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 750, 750);
        stage.setScene(scene);
        stage.show();
    }
    public void madeByButton(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("made_by.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 750, 750);
        stage.setScene(scene);
        stage.show();
    }
    public void createButton(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 750, 750);
        stage.setScene(scene);
        stage.show();
    }
}

    /* public void handleButton1(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("import.fxml"));
        Stage window =(Stage) createButton.getScene().getWindow();
        window.setScene(new Scene(root,750,750));
    }*/

