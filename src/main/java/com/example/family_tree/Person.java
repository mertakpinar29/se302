package com.example.family_tree;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Person implements Serializable {
    UUID id;
    String firstname;
    String lastname;
    Female mother;
    Male father;
    boolean isMarried;
    ArrayList<Person> children;
    Date birthDate;

    public Person(String firstname, String lastname, Female mother, Male father, boolean isMarried, Date birthDate) {
        id = UUID.randomUUID();
        this.firstname = firstname;
        this.lastname = lastname;
        this.mother = mother;
        this.father = father;
        this.isMarried = isMarried;
        this.birthDate = birthDate;
        children = new ArrayList<>();
    }

    public static class Male extends Person {
        Female partner;

        public Male(String firstname, String lastname, Female mother, Male father, boolean isMarried, Date birthDate) {
            super(firstname, lastname, mother, father, isMarried, birthDate);
        }

        public void setPartner(Female partner) {
            this.partner = partner;
        }
    }

    public static class Female extends Person{
        Male partner;

        public Female(String firstname, String lastname, Female mother, Male father, boolean isMarried, Date birthDate) {
            super(firstname, lastname, mother, father, isMarried, birthDate);
        }

        public void setPartner(Male partner) {
            this.partner = partner;
        }
    }
}
