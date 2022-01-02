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
    Parent root;

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

        Label information = new Label("You need to enter information about first two members of the family, mother and father.");
        information.setStyle("-fx-text-fill: red; -fx-font-style: italic");

        Label familyNameLabel = new Label("FamilyTree Name");
        TextField familyName = new TextField();

        // mother information
        Label motherNameLabel = new Label("Mother name");
        TextField rootMotherName = new TextField();
        Label motherBirthDateLabel = new Label("Birth date of mother (dd/MM/yyyy)");
        TextField rootMotherBirthDate = new TextField();

        // father information
        Label fatherNameLabel = new Label("Father name");
        TextField rootFatherName = new TextField();
        Label lastNameLabel = new Label("Last name");
        TextField lastname = new TextField();
        Label fatherBirthDateLabel = new Label("Birth date of father (dd/MM/yyyy)");
        TextField rootFatherBirthDate = new TextField();

        Button submitButton = new Button("CREATE");
        // event handler for submit button
        submitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            // get the values from form
            String familyTreeName = familyName.getText();
            String motherName = rootMotherName.getText();
            String motherBirthDate = rootMotherBirthDate.getText();
            String fatherName = rootFatherName.getText();
            String fatherBirthDate = rootFatherBirthDate.getText();
            String fatherLastName = lastname.getText();
            // check if there is missing information
            if(fatherLastName.equals("") || familyTreeName.equals("") || motherName.equals("") || motherBirthDate.equals("") || fatherName.equals("") || fatherBirthDate.equals("")){
                Alert alert = new Alert(Alert.AlertType.WARNING, "All fields of the form must be filled", ButtonType.OK);
                alert.show();
            }else {
                Person.Male father;
                Person.Female mother;
                try {
                    // create father and mother objects with given information
                    // these two objects will be the root elements of the family tree
                    father = new Person.Male(fatherName, fatherLastName, null, null, true, new SimpleDateFormat("dd/MM/yyyy").parse(fatherBirthDate));
                    mother = new Person.Female(motherName, fatherLastName, null, null, true, new SimpleDateFormat("dd/MM/yyyy").parse(motherBirthDate));
                    father.setPartner(mother);
                    mother.setPartner(father);
                    // creating a FamilyTree object with given information
                    FamilyTree familyTree = new FamilyTree(familyTreeName, father, mother);

                    // write created family tree object to text
                    FileOutputStream f = new FileOutputStream(familyTree.id + ".txt");
                    ObjectOutputStream o = new ObjectOutputStream(f);

                    o.writeObject(familyTree);
                    o.close();
                    f.close();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Family Tree has been created.", ButtonType.OK);
                    alert.show();
                    dialog.close();
                } catch (ParseException | IOException exception) {
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
        dialogVBox.getChildren().addAll(information, familyNameLabel, familyName, motherNameLabel, rootMotherName, motherBirthDateLabel, rootMotherBirthDate,
                fatherNameLabel, rootFatherName, fatherBirthDateLabel, rootFatherBirthDate, lastNameLabel, lastname,  submitButton);
        Scene dialogScene = new Scene(dialogVBox, 500, 450);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}


