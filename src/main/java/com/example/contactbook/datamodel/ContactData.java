package com.example.contactbook.datamodel;

import com.example.contactbook.ContactApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ContactData {
    private static ContactData instance = new ContactData();
    private ObservableList<Contact> contacts = FXCollections.observableArrayList();

    private ContactData() {
    }

    public static ContactData getInstance() {
        return instance;
    }

    public ObservableList<Contact> getContacts() {
        return contacts;
    }

    public boolean addContact(Contact contact) {
        if (!contacts.contains(contact)) {
            contacts.add(contact);
            return true;
        }
        return false;
    }

    public void deleteContactFromBase(Contact contact) {
        this.contacts.remove(contact);
    }

    public void storeToFile(File file) throws IOException {
        //       Path path = Paths.get(filename);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<soap:Envelope xmlns:soap= \"https://www.svoy-med.ru/ContactBook-soap\">\n" +
                    "<soap:body>");
            Iterator<Contact> iterator = contacts.iterator();
            while (iterator.hasNext()) {
                Contact contact = iterator.next();
                bw.write("<Contact>\n" +
                        "    <firstName>" + contact.getFirstName() + "</firstName>\n" +
                        "    <lastName>" + contact.getLastName() + "</lastName>\n" +
                        "    <phoneNumber>" + contact.getPhoneNumber() + "</phoneNumber>\n" +
                        "    <notes>" + contact.getNotes() + "</notes>\n" +
                        "</Contact>");
                bw.newLine();
            }
            bw.write("</soap:body>\n" +
                    "</soap:Envelope>");
            bw.newLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Alert.AlertType loadFromFile(File file) {
        // Path path = Paths.get(filename);

        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                stringBuilder.append(br.readLine());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        String commonString = new String(stringBuilder);
        if (!commonString.contains("<Contact>")) {
            return Alert.AlertType.WARNING;
        }
        String[] parsingContactList = new String(stringBuilder).split("<Contact>");
        Alert.AlertType processResult = Alert.AlertType.WARNING;

        for (String str : parsingContactList) {
            String firstName = parseStringValue(str, "<firstName>", "</firstName>");
            String lastName = parseStringValue(str, "<lastName>", "</lastName>");
            String phoneNumber = parseStringValue(str, "<phoneNumber>", "</phoneNumber>");
            String notes = parseStringValue(str, "<notes>", "</notes>");

            if (firstName.isEmpty() && firstName.isEmpty() && phoneNumber.isEmpty() && notes.isEmpty()) continue;
            Contact contact = new Contact(firstName, lastName, phoneNumber, notes);
            if (addContact(contact)==true){
                processResult = Alert.AlertType.INFORMATION;
            }

        }
        Collections.sort(contacts);
        return processResult;
    }

    private String parseStringValue(String str, String firstTag, String secondTag) {
        if (str.contains(firstTag) && str.contains(secondTag)) {
            int start = str.lastIndexOf(firstTag) + firstTag.length();
            int end = str.lastIndexOf(secondTag);
            if (end <= start) return "";
            else return str.substring(start, end);
        }
        return "";
    }
}
