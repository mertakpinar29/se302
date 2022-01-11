package com.example.family_tree;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.stage.Stage;

import java.io.*;

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
        TableView<Object> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Creating table columns
        TableColumn<Object, Object> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(pane.getPrefWidth() / 4);

        TableColumn familyTreeNameColumn = new TableColumn<>("Family Tree Name");
        familyTreeNameColumn.setCellValueFactory(new PropertyValueFactory<>("familyTreeName"));
        familyTreeNameColumn.setPrefWidth(pane.getPrefWidth() / 4);

        TableColumn buttonColumn = new TableColumn("");
        buttonColumn.setCellValueFactory(new PropertyValueFactory<>("selectButton"));
        buttonColumn.setPrefWidth(pane.getPrefWidth() /  4);

        TableColumn deleteColumn = new TableColumn("");
        deleteColumn.setCellValueFactory(new PropertyValueFactory<>("deleteButton"));
        buttonColumn.setPrefWidth(pane.getPrefWidth() /  4);

        tableView.getColumns().addAll(idColumn, familyTreeNameColumn, buttonColumn, deleteColumn);
        pane.getChildren().add(tableView);

        for(File file : files) {
            if(file.isFile()) {
                try {
                    // read the family tree object from text
                    input = new ObjectInputStream(new FileInputStream(file));
                    FamilyTree familyTree = (FamilyTree) input.readObject();

                    Button selectButton = new Button("SELECT");
                    Button deleteButton = new Button("DELETE");
                    ObjectInputStream finalInput = input;
                    deleteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

                        try {
                            finalInput.close();
                            file.delete();


                        }
                        catch (Exception e) {

                            e.printStackTrace();

                        };
                        tableView.getItems().removeAll(tableView.getSelectionModel().getSelectedItems());
                        Parent root = null;
                        try {
                            root = FXMLLoader.load(getClass().getResource("import.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    });




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
                    tableView.getItems().add(new VisualFamilyTree(new Label(familyTree.getId().toString()), new Label(familyTree.getFamilyTreeName()), selectButton,deleteButton));
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
