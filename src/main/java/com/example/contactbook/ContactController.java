package com.example.contactbook;

import com.example.contactbook.datamodel.Contact;
import com.example.contactbook.datamodel.ContactData;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

import java.util.List;
import java.util.Optional;

public class ContactController {
    @FXML
    private ContextMenu tableContextMenu;


    @FXML
    private TableView<Contact> tableView;

    @FXML
    TableColumn<Contact, String> firstNameCol;

    @FXML
    TableColumn<Contact, String> lastNameCol;

    @FXML
    TableColumn<Contact, String> phoneNumberCol;

    @FXML
    TableColumn<Contact, String> notesCol;

    public void initialize() {
        //binding our ObservableList with TableView
        tableView.setItems(ContactData.getInstance().getContacts());

        // Here we associate fxml-columns with Contact's fields
        firstNameCol.setCellValueFactory(p -> p.getValue().firstNameProperty());
        lastNameCol.setCellValueFactory(p -> p.getValue().lastNameProperty());
        phoneNumberCol.setCellValueFactory(p -> p.getValue().phoneNumberProperty());
        notesCol.setCellValueFactory(p -> p.getValue().notesProperty());



        // Adjusting our Cells as editable TextFields
        // 1st step - to prepare Callback, to pass it as a parameter to Cell Factory
        // the example of Callback was grabbed from Oracle doc TableView
        Callback<TableColumn<Contact,String>,TableCell<Contact,String>>
                myCallback = new Callback<TableColumn<Contact, String>, TableCell<Contact, String>>() {
            @Override
            public TableCell<Contact, String> call(TableColumn<Contact, String> param) {
                TableCell cell = new TextFieldTableCell(new DefaultStringConverter());
 //if cell is not empty, it possible to call ContextMenu
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if(isNowEmpty) {
                                cell.setContextMenu(null);
                            } else {
                                cell.setContextMenu(tableContextMenu);
                            }

                        });

                return cell;
            }
        };
//2nd step - setup Cell Factory
        firstNameCol.setCellFactory(myCallback);
        lastNameCol.setCellFactory(myCallback);
        phoneNumberCol.setCellFactory(myCallback);
        notesCol.setCellFactory(myCallback);

//creating ContextMenu and menu items
        tableContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("DELETE");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Contact contact = tableView.getSelectionModel().getSelectedItem();
               deleteContact (contact);
            }
        });



        tableContextMenu.getItems().addAll(deleteMenuItem);


        //A FEW IMPLEMENTATIONS OF THE EDIT CELL EVENTHANDED
        // We can create new EventHandler according to examples from Oracle doc for TableView
        lastNameCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Contact, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Contact, String> event) {
                ((Contact) event.getTableView().getItems().get(
                        event.getTablePosition().getRow())
                ).setLastName(event.getNewValue());
            }
        });

// Or we can convert above EventHandler  lyambda method
        notesCol.setOnEditCommit(cellEditEvent -> cellEditEvent.getTableView().getItems().get(
                cellEditEvent.getTablePosition().getRow())
                .setNotes(cellEditEvent.getNewValue()));


        // selecting first Contact record
        tableView.getSelectionModel().selectFirst();
    }


//  We could  pass this EventHandler to setOnEditCommit() method
//    @FXML
//    private EventHandler<TableColumn.CellEditEvent<Contact, String>> editHandler() {
//        return new EventHandler<TableColumn.CellEditEvent<Contact, String>>() {
//            @Override
//            public void handle(TableColumn.CellEditEvent<Contact, String> event) {
//                ((Contact) event.getTableView().getItems().get(
//                        event.getTablePosition().getRow())
//                ).setPhoneNumber(event.getNewValue());
//            }
//        };
//    }

    // Or we can directly bind this Handler to fxml file
    @FXML
    public void editHandler(TableColumn.CellEditEvent<Contact, String> event) {
        Contact contact = tableView.getSelectionModel().getSelectedItem();
        TableColumn<Contact, String> eventOccurredColumn = event.getTableColumn();
        String newValue = event.getNewValue();

        if (eventOccurredColumn == firstNameCol) contact.setFirstName(newValue);
        if (eventOccurredColumn == lastNameCol) contact.setLastName(newValue);
        if (eventOccurredColumn == phoneNumberCol) contact.setPhoneNumber(newValue);
        if (eventOccurredColumn == notesCol) contact.setNotes(newValue);
    }

    public void deleteContact (Contact contact){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(tableView.getScene().getWindow());
        alert.setTitle("Delete Contact");
        alert.setHeaderText("Delete " + contact);
        alert.setContentText("Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get()==ButtonType.OK) {
            ContactData.getInstance().deleteContactFromBase(contact);
        }
    }

    @FXML
    public void handleTableViewKeyPressed(KeyEvent keyEvent){
        Contact selectedContact = tableView.getSelectionModel().getSelectedItem();
        if (selectedContact!=null){
            if (keyEvent.getCode()== KeyCode.DELETE || keyEvent.getCode()==KeyCode.BACK_SPACE) {
                deleteContact(selectedContact);
            }
        }
    }

    @FXML
    public void handleNewContact(){
        ContactData.getInstance().getContacts()
                .add(new Contact("","","",""));
    }

}