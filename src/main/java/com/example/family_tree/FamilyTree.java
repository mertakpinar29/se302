package com.example.family_tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class FamilyTree implements Serializable {
    UUID id;
    String familyTreeName;
    // first two members of the family tree
    ArrayList<Person> members;

    public FamilyTree(String familyTreeName){
        this.id = UUID.randomUUID();
        this.familyTreeName = familyTreeName;
        this.members = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFamilyTreeName() {
        return familyTreeName;
    }

    public void setFamilyTreeName(String familyTreeName) {
        this.familyTreeName = familyTreeName;
    }

    public ArrayList<Person> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Person> members) {
        this.members = members;
    }
}
