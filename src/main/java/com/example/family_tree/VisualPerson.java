package com.example.family_tree;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class VisualPerson {
    Label id;
    Label firstName;
    Label lastName;
    Button selectButton;

    public VisualPerson(Label id, Label firstName, Label lastName, Button selectButton) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.selectButton = selectButton;
    }

    public Label getId() {
        return id;
    }

    public void setId(Label id) {
        this.id = id;
    }

    public Label getFirstName() {
        return firstName;
    }

    public void setFirstName(Label firstName) {
        this.firstName = firstName;
    }

    public Label getLastName() {
        return lastName;
    }

    public void setLastName(Label lastName) {
        this.lastName = lastName;
    }

    public Button getSelectButton() {
        return selectButton;
    }

    public void setSelectButton(Button selectButton) {
        this.selectButton = selectButton;
    }
}
