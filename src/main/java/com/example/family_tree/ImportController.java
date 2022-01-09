package com.example.family_tree;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.UUID;

public class ImportController {
    Stage stage;
    Scene scene;
    Parent root;

    @FXML
    Pane pane;

    // when page loads, get all the txt files, that way user will be able to select a family tree
    @FXML
    public void initialize(){
        // file name filter for getting all txt files that has been created
        FilenameFilter textFilter = (dir, name) -> name.toLowerCase().endsWith(".txt");
        // get all text files and put it in an array called Files
        File[] files = new File("./").listFiles(textFilter);
        ObjectInputStream input = null;

        // Creating a TableView to show Family trees
        TableView tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Creating table columns
        TableColumn idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(pane.getPrefWidth() / 3);

        TableColumn familyTreeNameColumn = new TableColumn<>("Family Tree Name");
        familyTreeNameColumn.setCellValueFactory(new PropertyValueFactory<>("familyTreeName"));
        familyTreeNameColumn.setPrefWidth(pane.getPrefWidth() / 3);

        TableColumn buttonColumn = new TableColumn("");
        buttonColumn.setCellValueFactory(new PropertyValueFactory<>("selectButton"));
        buttonColumn.setPrefWidth(pane.getPrefWidth() /  3);

        tableView.getColumns().addAll(idColumn, familyTreeNameColumn, buttonColumn);
        pane.getChildren().add(tableView);

        for(File file : files) {
            if(file.isFile()) {
                try {
                    // read the family tree object from text
                    input = new ObjectInputStream(new FileInputStream(file));
                    FamilyTree familyTree = (FamilyTree) input.readObject();

                    Button selectButton = new Button("SELECT");
                    selectButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("visualFamilyTree.fxml"));
                        try {
                            root = loader.load();
                            VisualController visualController = loader.getController();
                            visualController.drawFamilyTree(familyTree);
                            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                            scene = new Scene(root);
                            String css = this.getClass().getResource("visual.css").toExternalForm();
                            scene.getStylesheets().add(css);
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    });
                    tableView.getItems().add(new VisualFamilyTree(new Label(familyTree.getId().toString()), new Label(familyTree.getFamilyTreeName()), selectButton));
                } catch (IOException | ClassNotFoundException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

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
}
