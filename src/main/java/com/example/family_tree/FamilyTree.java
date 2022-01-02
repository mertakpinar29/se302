package com.example.family_tree;

import java.io.Serializable;
import java.util.UUID;

public class FamilyTree implements Serializable {
    UUID id;
    String familyTreeName;
    Person.Male rootFather;
    Person.Female rootMother;

    public FamilyTree(String familyTreeName, Person.Male father, Person.Female mother){
        this.id = UUID.randomUUID();
        this.familyTreeName = familyTreeName;
        this.rootFather = father;
        this.rootMother = mother;
    }
}
