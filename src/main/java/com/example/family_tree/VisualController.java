package com.example.family_tree;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class VisualController {
    @FXML
    AnchorPane mainPane;

    FamilyTree currentFamilyTree;

    Stage stage;
    Scene scene;
    Parent root;

    public void drawFamilyTree(FamilyTree familyTree) {
        // setting the local variable
        this.currentFamilyTree = familyTree;

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefWidth(mainPane.getPrefWidth());
        mainPane.getChildren().add(scrollPane);
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setSpacing(30);
        scrollPane.setContent(vBox);

        vBox.prefWidthProperty().bind(mainPane.widthProperty());
        vBox.prefHeightProperty().bind(mainPane.heightProperty());

        Label familyTreeName = new Label(familyTree.getFamilyTreeName());
        familyTreeName.setStyle("-fx-font: 30px Tahoma; -fx-padding: 15,0,0,0;");
        familyTreeName.setTextAlignment(TextAlignment.CENTER);

        Button createMemberButton = new Button("Add new member");
        Button navigationButton = new Button("Go to Home Screen");

        navigationButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("create.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        });

        createMemberButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);

            Label createNewMemberLabel = new Label("Create new member");
            createNewMemberLabel.setFont(Font.font("Lucida Sans Unicode", FontPosture.ITALIC, 20));
            createNewMemberLabel.setTextFill(Color.RED);

            Label firstNameLabel = new Label("First Name");
            TextField firstNameInput = new TextField();
            Label lastNameLabel = new Label("Last Name");
            TextField lastNameInput = new TextField();
            Label birthDateLabel = new Label("Birth Date");
            TextField birthDateInput = new TextField();

            Label genderLabel = new Label("Gender");
            ChoiceBox genderChoiceBox = new ChoiceBox();
            genderChoiceBox.getItems().addAll("Male", "Female");

            Button submitButton = new Button("CREATE");

            submitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                String firstName = firstNameInput.getText();
                String lastName = lastNameInput.getText();
                String birthDate = birthDateInput.getText();
                String gender = (String) genderChoiceBox.getValue();

                if(firstName.equals("") || lastName.equals("") || birthDate.equals("") || gender == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "All fields must be filled.", ButtonType.OK);
                    alert.show();
                }else {
                    Alert alert;
                    FileOutputStream f;
                    ObjectOutputStream o;
                    if(gender.equals("Male")){
                        // Create a new Male object, add it to members list
                        try {
                            Person.Male malePerson = new Person.Male(firstName, lastName,new SimpleDateFormat("dd/MM/yyyy").parse(birthDate));
                            // add created member to list and save it
                            currentFamilyTree.members.add(malePerson);
                            f = new FileOutputStream(currentFamilyTree.id + ".txt");
                            o = new ObjectOutputStream(f);

                            o.writeObject(currentFamilyTree);
                            o.close();
                            f.close();

                            alert = new Alert(Alert.AlertType.CONFIRMATION, "Member has been created.", ButtonType.OK);
                            alert.show();
                            dialog.close();
                            // redraw family tree
                            drawFamilyTree(currentFamilyTree);
                        } catch (ParseException | IOException exception ) {
                            exception.printStackTrace();
                            // show an alert if error occurs
                            alert = new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK);
                            alert.show();
                        }
                    } else {
                        try {
                            Person.Female femalePerson = new Person.Female(firstName, lastName, new SimpleDateFormat("dd/MM/yyyy").parse(birthDate));
                            currentFamilyTree.members.add(femalePerson);
                            f = new FileOutputStream(currentFamilyTree.id + ".txt");
                            o = new ObjectOutputStream(f);

                            o.writeObject(currentFamilyTree);
                            o.close();
                            f.close();

                            alert = new Alert(Alert.AlertType.CONFIRMATION, "Member has been created.", ButtonType.OK);
                            alert.show();
                            dialog.close();
                            drawFamilyTree(currentFamilyTree);

                        } catch (ParseException | IOException exception) {
                            exception.printStackTrace();
                            // show an alert if error occurs
                            alert = new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK);
                            alert.show();
                        }
                    }
                }
            });

            VBox dialogVBox = new VBox();
            dialogVBox.setSpacing(10);
            dialogVBox.setAlignment(Pos.CENTER);
            dialogVBox.setPadding(new Insets(10,10,10,10));
            dialogVBox.getChildren().addAll(createNewMemberLabel, firstNameLabel ,firstNameInput, lastNameLabel, lastNameInput, birthDateLabel, birthDateInput,
                    genderLabel, genderChoiceBox, submitButton);
            Scene dialogScene = new Scene(dialogVBox, 500, 450);
            dialog.setScene(dialogScene);
            dialog.show();
        });

        Accordion accordion = new Accordion();
        for(int i = 0; i < familyTree.getMembers().size(); i++) {
            Person currentMember = familyTree.getMembers().get(i);
            TitledPane personPane = drawPersonPane(currentMember, true);
            accordion.getPanes().add(personPane);
        }
        vBox.getChildren().add(familyTreeName);
        vBox.getChildren().add(createMemberButton);
        vBox.getChildren().add(navigationButton);
        VBox accordionBox = new VBox(accordion);
        vBox.getChildren().add(accordionBox);
    }

    public TitledPane drawPartnerPane(Person partner){
        VBox personInformationBox = new VBox();
        personInformationBox.setSpacing(20);

        Text id = new Text("id: " + partner.id);
        Text mother = new Text("Mother: No Data");
        Text father = new Text("Father: No Data");
        // checking if mother data exists
        if(partner.mother != null) {
            mother.setText("Mother: " + partner.mother.firstname.toUpperCase()
                    + " " + partner.mother.lastname.toUpperCase());
        }
        // checking if father data exists
        if(partner.father != null) {
            father.setText("Father: " + partner.father.firstname.toUpperCase()
                    + " " + partner.father.lastname.toUpperCase());
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Text birthDate = new Text("Birth date: " + dateFormat.format(partner.birthDate));

        // show list of children inside of accordion
        Label childrenLabel = new Label("Children");
        Accordion childrenAccordion = new Accordion();
        childrenAccordion.getStyleClass().add("children");
        if (partner.children.size() > 0) {
            for(int i = 0; i < partner.children.size(); i++) {
                TitledPane child = drawPersonPane(partner.children.get(i), false);
                childrenAccordion.getPanes().add(child);
            }
        } else {
            TitledPane noChildren = new TitledPane("Children", new Label("Children data not found."));
            childrenAccordion.getPanes().add(noChildren);
        }
        personInformationBox.getChildren().addAll(id, mother, father, birthDate, childrenLabel, childrenAccordion);
        TitledPane partnerPane = new TitledPane("Partner: " + partner.firstname.toUpperCase() + " " + partner.lastname.toUpperCase(), personInformationBox);
        if(partner instanceof Person.Male) {
            partnerPane.getStyleClass().add("male");
        }else {
            partnerPane.getStyleClass().add("female");
        }
        return partnerPane;
    }

    public TitledPane drawPersonPane(Person currentMember, boolean drawParents) {

        VBox personInformationBox = new VBox();
        personInformationBox.setSpacing(10);

        Text id = new Text("id: " + currentMember.id);
        Accordion motherAccordion = new Accordion();
        Accordion fatherAccordion = new Accordion();
        if(drawParents) {
            // checking if mother data exists, if it does not show a button "Add mother"
            if(currentMember.mother != null) {
                TitledPane motherPane = drawPersonPane(currentMember.mother, true);
                motherAccordion.getPanes().add(motherPane);
            } else {
                Button addMotherButton = new Button("Add mother");
                addMotherButton.setPadding(new Insets(5,5,5,5));
                // add an event listener for addMotherButton
                // it will open a new pop-up and allows users to select a mother
                addMotherButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                    final Stage dialog = new Stage();
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.initOwner(stage);

                    Label selectMotherLabel  = new Label("Select mother");
                    selectMotherLabel.setTextFill(Color.RED);
                    selectMotherLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

                    VBox motherContainer = new VBox();

                    // hold partners inside of a table
                    TableView females = new TableView();
                    females.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                    // Creating table columns
                    TableColumn idColumn = new TableColumn<>("ID");
                    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                    idColumn.setPrefWidth(motherContainer.getPrefWidth() / 3);

                    TableColumn firstNameColumn = new TableColumn<>("First Name");
                    firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
                    firstNameColumn.setPrefWidth(motherContainer.getPrefWidth() / 3);

                    TableColumn lastNameColumn = new TableColumn<>("Last Name");
                    lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
                    lastNameColumn.setPrefWidth(motherContainer.getPrefWidth() / 3);

                    TableColumn buttonColumn = new TableColumn("");
                    buttonColumn.setCellValueFactory(new PropertyValueFactory<>("selectButton"));
                    buttonColumn.setPrefWidth(motherContainer.getPrefWidth() /  3);

                    females.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, buttonColumn);

                    motherContainer.getChildren().add(selectMotherLabel);
                    motherContainer.getChildren().add(females);

                    // User can not set mother as node itself
                    // User can not set node's mother as its partner
                    for(int i = 0; i < currentFamilyTree.members.size(); i++){

                        Person currentFemale = currentFamilyTree.members.get(i);

                        // checking if current member's partner is current female, if so this female object won't show up in the table.
                        if((currentMember.getPartner() != null && currentMember.getPartner().equals(currentFemale)
                                || currentFemale instanceof Person.Male
                                || currentMember.equals(currentFemale))) {
                            continue;
                        }

                        Button selectButton = new Button("SELECT");
                        selectButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            // first get the index of the current member
                            int indexOfCurrentMember = currentFamilyTree.members.indexOf(currentMember);
                            // get the index of the mother
                            int indexOfMother = currentFamilyTree.members.indexOf(currentFemale);

                            // set current member's mother
                            currentFamilyTree.members.get(indexOfCurrentMember).mother = (Person.Female) currentFemale;

                            // add current member to mother's children list
                            currentFamilyTree.members.get(indexOfMother).children.add(currentMember);

                            // After the changes write it to text
                            try {
                                FileOutputStream f = new FileOutputStream(currentFamilyTree.id + ".txt");
                                ObjectOutputStream o = new ObjectOutputStream(f);

                                o.writeObject(currentFamilyTree);
                                o.close();
                                f.close();

                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Family updated.", ButtonType.OK);
                                alert.show();
                                dialog.close();

                                drawFamilyTree(currentFamilyTree);
                            } catch (IOException exception) {
                                exception.printStackTrace();
                                // show an alert if error occurred
                                Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK);
                                alert.show();
                            }

                        });

                        females.getItems().add(new VisualPerson(new Label(currentFemale.id.toString()), new Label(currentFemale.firstname.toUpperCase()),
                                new Label(currentFemale.lastname.toUpperCase()), selectButton));
                    }

                    motherContainer.setSpacing(10);
                    motherContainer.setAlignment(Pos.CENTER);
                    motherContainer.setPadding(new Insets(10, 10, 10, 10));
                    Scene dialogScene = new Scene(motherContainer, 500, 450);
                    dialog.setScene(dialogScene);
                    dialog.show();
                });

                motherAccordion.getPanes().add(new TitledPane("Mother data not found.", addMotherButton));
            }
            // checking if father data exists
            if(currentMember.father != null) {
                TitledPane fatherPane = drawPersonPane(currentMember.father, true);
                fatherAccordion.getPanes().add(fatherPane);
            } else {
                Button addFatherButton = new Button("Add father");
                addFatherButton.setPadding(new Insets(5,5,5,5));
                // add an event listener for addFatherButton
                // it will open a new pop-up and allows users to select a father
                addFatherButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                    final Stage dialog = new Stage();
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.initOwner(stage);

                    Label selectFatherLabel  = new Label("Select father");
                    selectFatherLabel.setTextFill(Color.RED);
                    selectFatherLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

                    VBox fatherContainer = new VBox();

                    // hold partners inside of a table
                    TableView males = new TableView();
                    males.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                    // Creating table columns
                    TableColumn idColumn = new TableColumn<>("ID");
                    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                    idColumn.setPrefWidth(fatherContainer.getPrefWidth() / 3);

                    TableColumn firstNameColumn = new TableColumn<>("First Name");
                    firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
                    firstNameColumn.setPrefWidth(fatherContainer.getPrefWidth() / 3);

                    TableColumn lastNameColumn = new TableColumn<>("Last Name");
                    lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
                    lastNameColumn.setPrefWidth(fatherContainer.getPrefWidth() / 3);

                    TableColumn buttonColumn = new TableColumn("");
                    buttonColumn.setCellValueFactory(new PropertyValueFactory<>("selectButton"));
                    buttonColumn.setPrefWidth(fatherContainer.getPrefWidth() /  3);

                    males.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, buttonColumn);

                    fatherContainer.getChildren().add(selectFatherLabel);
                    fatherContainer.getChildren().add(males);

                    // User can not set mother as node itself
                    // User can not set node's mother as its partner
                    for(int i = 0; i < currentFamilyTree.members.size(); i++){

                        Person currentMale = currentFamilyTree.members.get(i);

                        // user can not add father as his children,
                        // user can not set father as hisself
                        // user can not set her father as her partner
                        if((currentMember.getPartner() != null && currentMember.getPartner().equals(currentMale)
                                || currentMale instanceof Person.Female
                                || currentMember.equals(currentMale))
                                || currentMember.children.contains(currentMale)) {
                            continue;
                        }

                        Button selectButton = new Button("SELECT");
                        selectButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            // first get the index of the current member
                            int indexOfCurrentMember = currentFamilyTree.members.indexOf(currentMember);
                            // get the index of the mother
                            int indexOfFather = currentFamilyTree.members.indexOf(currentMale);

                            // set current member's mother
                            currentFamilyTree.members.get(indexOfCurrentMember).father = (Person.Male) currentMale;

                            // add current member to mother's children list
                            currentFamilyTree.members.get(indexOfFather).children.add(currentMember);

                            // After the changes write it to text
                            try {
                                FileOutputStream f = new FileOutputStream(currentFamilyTree.id + ".txt");
                                ObjectOutputStream o = new ObjectOutputStream(f);

                                o.writeObject(currentFamilyTree);
                                o.close();
                                f.close();

                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Family updated.", ButtonType.OK);
                                alert.show();
                                dialog.close();

                                drawFamilyTree(currentFamilyTree);
                            } catch (IOException exception) {
                                exception.printStackTrace();
                                // show an alert if error occurred
                                Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK);
                                alert.show();
                            }

                        });

                        males.getItems().add(new VisualPerson(new Label(currentMale.id.toString()), new Label(currentMale.firstname.toUpperCase()),
                                new Label(currentMale.lastname.toUpperCase()), selectButton));
                    }

                    fatherContainer.setSpacing(10);
                    fatherContainer.setAlignment(Pos.CENTER);
                    fatherContainer.setPadding(new Insets(10, 10, 10, 10));
                    Scene dialogScene = new Scene(fatherContainer, 500, 450);
                    dialog.setScene(dialogScene);
                    dialog.show();
                });

                fatherAccordion.getPanes().add(new TitledPane("Father data not found.", addFatherButton));
            }
        }

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Text birthDate = new Text("Birth date: " + dateFormat.format(currentMember.birthDate));

        // PARTNER
        Accordion partnerAccordion = new Accordion();
        if(currentMember.getPartner() != null) {
            TitledPane partnerPane = drawPartnerPane(currentMember.getPartner());
            partnerAccordion.getPanes().add(partnerPane);
        }else {
            // Creating a button that will allow user to open Add Partner form.
            // In that form, user will be able to select which member of the tree is going to be assigned as Partner.
            Button addPartnerButton = new Button("Add partner");
            addPartnerButton.setPadding(new Insets(5,5,5,5));
            // when user clicks add partner button, a pop-up will come up with selectable partners from members of the tree
            addPartnerButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(stage);

                Label selectPartnerLabel  = new Label("Select a partner");
                selectPartnerLabel.setTextFill(Color.RED);
                selectPartnerLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

                VBox partnerContainer = new VBox();

                // hold partners inside of a table
                TableView partners = new TableView();
                partners.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                // Creating table columns
                TableColumn idColumn = new TableColumn<>("ID");
                idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                idColumn.setPrefWidth(partnerContainer.getPrefWidth() / 3);

                TableColumn firstNameColumn = new TableColumn<>("First Name");
                firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
                firstNameColumn.setPrefWidth(partnerContainer.getPrefWidth() / 3);

                TableColumn lastNameColumn = new TableColumn<>("Last Name");
                lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
                lastNameColumn.setPrefWidth(partnerContainer.getPrefWidth() / 3);

                TableColumn buttonColumn = new TableColumn("");
                buttonColumn.setCellValueFactory(new PropertyValueFactory<>("selectButton"));
                buttonColumn.setPrefWidth(partnerContainer.getPrefWidth() /  3);

                partners.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, buttonColumn);

                partnerContainer.getChildren().add(selectPartnerLabel);
                partnerContainer.getChildren().add(partners);

                for(Person person : currentFamilyTree.members){
                    Button selectButton = new Button("SELECT");
                    selectButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                        // first get the index of the current member
                        int indexOfCurrentMember = currentFamilyTree.members.indexOf(currentMember);
                        // get the index of the partner
                        int indexOfPartner = currentFamilyTree.members.indexOf(person);

                        // set current member's partner
                        currentFamilyTree.members.get(indexOfCurrentMember).setPartner(person);

                        // set selected partner's partner as current member
                        currentFamilyTree.members.get(indexOfPartner).setPartner(currentMember);

                        // After the changes write it to text
                        try {
                            FileOutputStream f = new FileOutputStream(currentFamilyTree.id + ".txt");
                            ObjectOutputStream o = new ObjectOutputStream(f);

                            o.writeObject(currentFamilyTree);
                            o.close();
                            f.close();

                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Partner updated.", ButtonType.OK);
                            alert.show();
                            dialog.close();

                            drawFamilyTree(currentFamilyTree);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                            // show an alert if error occurred
                            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK);
                            alert.show();
                        }

                    });
                    if(currentMember instanceof Person.Male && person instanceof Person.Female && person.getPartner() == null) {
                        partners.getItems().add(new VisualPerson(new Label(person.id.toString()), new Label(person.firstname.toUpperCase()),
                                new Label(person.lastname.toUpperCase()), selectButton));
                    } else if (currentMember instanceof Person.Female && person instanceof Person.Male && person.getPartner() == null) {
                        partners.getItems().add(new VisualPerson(new Label(person.id.toString()), new Label(person.firstname.toUpperCase()),
                                new Label(person.lastname.toUpperCase()), selectButton));
                    }
                }

                partnerContainer.setSpacing(10);
                partnerContainer.setAlignment(Pos.CENTER);
                partnerContainer.setPadding(new Insets(10, 10, 10, 10));
                Scene dialogScene = new Scene(partnerContainer, 500, 450);
                dialog.setScene(dialogScene);
                dialog.show();
            });
            partnerAccordion.getPanes().add(new TitledPane("Partner data not found.", addPartnerButton));
        }

        // show list of children inside of accordion
        Accordion childrenAccordion = new Accordion();
        childrenAccordion.getStyleClass().add("children");
        if (currentMember.children.size() > 0) {
            for(int i = 0; i < currentMember.children.size(); i++) {
                TitledPane child = drawPersonPane(currentMember.children.get(i), false);
                childrenAccordion.getPanes().add(child);
            }
        } else {
            TitledPane noChildren = new TitledPane("Children", new Label("Children data not found."));
            childrenAccordion.getPanes().add(noChildren);
        }
        Label motherLabel = new Label("Mother");
        Label fatherLabel = new Label("Father");
        Label childrenLabel = new Label("Children");


        if(drawParents) {
            personInformationBox.getChildren().addAll(id, birthDate, partnerAccordion, childrenLabel, childrenAccordion, motherLabel, motherAccordion, fatherLabel, fatherAccordion);
        } else {
            personInformationBox.getChildren().addAll(id, birthDate, partnerAccordion, childrenLabel, childrenAccordion);
        }


        TitledPane personPane = new TitledPane(currentMember.firstname.toUpperCase() + " " + currentMember.lastname.toUpperCase(), personInformationBox);
        if(currentMember instanceof Person.Male) {
            personPane.getStyleClass().add("male");
        }else {
            personPane.getStyleClass().add("female");
        }
        return personPane;
    }
}
