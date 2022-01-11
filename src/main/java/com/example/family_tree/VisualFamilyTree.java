package com.example.family_tree;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class VisualFamilyTree {
    Label id;
    Label familyTreeName;
    Button selectButton;
    Button deleteButton;





    public VisualFamilyTree(Label id, Label familyTreeName, Button selectButton, Button deleteButton) {
        this.id = id;
        this.familyTreeName = familyTreeName;
        this.selectButton = selectButton;
        this.deleteButton =deleteButton;
    }

    public Label getId() {
        return id;
    }

    public void setId(Label id) {
        this.id = id;
    }

    public Label getFamilyTreeName() {
        return familyTreeName;
    }

    public void setFamilyTreeName(Label familyTreeName) {
        this.familyTreeName = familyTreeName;
    }

    public Button getSelectButton() {
        return selectButton;
    }

    public void setSelectButton(Button selectButton) {
        this.selectButton = selectButton;
    }
    public Button getDeleteButton() {
        return deleteButton;
    }
    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }
}
