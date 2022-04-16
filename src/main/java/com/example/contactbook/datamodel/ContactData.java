package com.example.contactbook.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ContactData {
    private static ContactData instance = new ContactData();
    private ObservableList<Contact> contacts= FXCollections.observableArrayList();

    private ContactData() {
    }

    public void loadDataToInstance (){

        contacts.add(new Contact("Nikta", "Ponomar",
                "+19165636444","It is me, ypu are looking for"));
        contacts.add(new Contact("Karina", "Ponomar", "+19031591904",
                "my darling"));
        contacts.add(new Contact("Some", "Guy", "556456456",
                "I do not know him!"));

    }

    public static ContactData getInstance() {
        return instance;
    }

    public ObservableList<Contact> getContacts() {
        return contacts;
    }

    public void addContact (Contact contact) {
        contacts.add(contact);
    }

    public void deleteContactFromBase(Contact contact) {
        this.contacts.remove(contact);
    }


}
