package com.example.family_tree;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public abstract class Person implements Serializable {
    UUID id;
    String firstname;
    String lastname;
    Female mother;
    Male father;
    ArrayList<Person> siblings;
    ArrayList<Person> children;
    Date birthDate;

    public Person(String firstname, String lastname, Date birthDate) {
        id = UUID.randomUUID();
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        children = new ArrayList<>();
        siblings = new ArrayList<>();
    }

    public Person getPartner() {
        return null;
    }

    public void setPartner(Person person){}

    // mother, father, partner, birth date, id, children

    public static class Male extends Person {
        Female partner;

        public Male(String firstname, String lastname, Date birthDate) {
            super(firstname, lastname, birthDate);
        }

        @Override
        public Person getPartner() {
            return this.partner;
        }

        @Override
        public void setPartner(Person person) {
            this.partner = (Female) person;
        }
    }

    public static class Female extends Person{
        Male partner;

        public Female(String firstname, String lastname, Date birthDate) {
            super(firstname, lastname, birthDate);
        }

        @Override
        public Person getPartner() {
            return this.partner;
        }

        @Override
        public void setPartner(Person person){
            this.partner = (Male) person;
        }
    }
}
