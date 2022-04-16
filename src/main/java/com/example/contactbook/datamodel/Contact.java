package com.example.contactbook.datamodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Contact implements Comparable<Contact> {
    private SimpleStringProperty  firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty phoneNumber;
    private SimpleStringProperty notes;

    public Contact(String firstName, String lastName, String phoneNumber, String notes) {
        // checking if property is null, if it is true, setting it as empty string
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
        setNotes(notes);
    }

    public SimpleStringProperty firstNameProperty() {
        if (firstName == null) firstName = new SimpleStringProperty(this, "firstName");
        return firstName;
    }

    public SimpleStringProperty lastNameProperty() {
        if (lastName == null) lastName = new SimpleStringProperty(this, "lastName");
        return lastName;
    }

    public SimpleStringProperty phoneNumberProperty() {
        if (phoneNumber == null) phoneNumber = new SimpleStringProperty(this, "phoneNumber");
        return phoneNumber;
    }

    public SimpleStringProperty notesProperty() {
        if (notes == null) notes = new SimpleStringProperty(this, "notes");
        return notes;
    }

    public String getFirstName() {
        return firstNameProperty().get();
    }


    public String getLastName() {
        return lastNameProperty().get();
    }

    public String getPhoneNumber() {
        return phoneNumberProperty().get();
    }

    public String getNotes() {
        return notesProperty().get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (!getFirstName().equals(contact.getFirstName())) return false;
        if (!getLastName().equals(contact.getLastName())) return false;
        return getPhoneNumber().equals(contact.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        int result = getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + getPhoneNumber().hashCode();
        return result;
    }

    @Override
    public int compareTo(Contact o) {
        return this.getLastName().compareTo(o.getLastName());
    }

    public void setFirstName(String firstName) {
        firstNameProperty().set(firstName);
    }

    public void setLastName(String lastName) {
        lastNameProperty().set(lastName);
    }

    public void setPhoneNumber(String phoneNumber) {
        phoneNumberProperty().set(phoneNumber);
    }

    public void setNotes(String notes) {
        notesProperty().set(notes);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "firstName=" + getFirstName() +
                ", lastName=" + getLastName() +
                ", phoneNumber=" + getPhoneNumber() +
                ", notes=" + getNotes() +
                '}';
    }
}
