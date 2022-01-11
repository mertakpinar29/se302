package com.example.family_tree;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;


import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class Controller {
    Stage stage;
    Scene scene;


    @FXML
    //Button createButton, importButton;

    public void importButton(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("import.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void helpButton(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("help.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void madeByButton(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("made_by.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void createButton(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("create.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void openCreationForm(ActionEvent event){
        // Creation form in a popup will be displayed.
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        Label familyNameLabel = new Label("FamilyTree Name");
        TextField familyName = new TextField();

        Button submitButton = new Button("CREATE");
        // event handler for submit button
        submitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            // get the values from form
            String familyTreeName = familyName.getText();

            // check if there is missing information
            if(familyTreeName.equals("")){
                Alert alert = new Alert(Alert.AlertType.WARNING, "All fields of the form must be filled", ButtonType.OK);
                alert.show();
            }else {
                try {
                    // creating a FamilyTree object with given information
                    FamilyTree familyTree = new FamilyTree(familyTreeName);

                    // write created family tree object to text
                    FileOutputStream f = new FileOutputStream(familyTree.id + ".txt");
                    ObjectOutputStream o = new ObjectOutputStream(f);

                    o.writeObject(familyTree);
                    o.close();
                    f.close();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Family Tree has been created.", ButtonType.OK);
                    alert.show();
                    dialog.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                    // show an alert if error occurred
                    Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK);
                    alert.show();
                }
            }
        });

        // create a vbox and put all input fileds in it
        VBox dialogVBox = new VBox();
        dialogVBox.setSpacing(10);
        dialogVBox.setAlignment(Pos.CENTER);
        dialogVBox.setPadding(new Insets(10,10,10,10));
        dialogVBox.getChildren().addAll( familyNameLabel, familyName, submitButton);
        Scene dialogScene = new Scene(dialogVBox, 500, 450);
        dialog.setScene(dialogScene);
        dialog.show();
    }

}


