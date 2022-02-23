/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2project;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.DBQuery;

/**
 * FXML Controller class
 *
 * @author Nathan
 */

//Class provides a customer TableView displaying all customers and some related information. 
public class ModifyRecordsController implements Initializable {

     private static int counter = 0;
     private static int selectedCustomerId;
     
    @FXML TableView <Customer> customerTableView;
    @FXML TableColumn<Customer, Integer> customerIdColumn;
    @FXML TableColumn<Customer, String> customerNameColumn;
    @FXML TableColumn<Customer, String> customerAddressColumn;
    @FXML TableColumn<Customer, String> customerAddress2Column;
    @FXML TableColumn<Customer, String> customerPhoneNumberColumn;
   
    
    public void getCustomerId(){
    selectedCustomerId = customerTableView.getSelectionModel().getSelectedItem().getCustomerId();
    }
    //allows for tracking of selected customer so that the objects can be properly updated or deleted.
    public static int getSelectedCustomerId(){
    return selectedCustomerId;
    }
    //counter used to retrieve all of the customer data from the database only once per program session.
    public static void incrementCounter(){
    if (counter == 0){
    counter++;
    } 
    }
    public static int getCounter(){
    return counter;
    }
   public static void retrieveAllCustomers(){
       DBQuery.retrieveAllCustomers();
        }
    
    public void cancelButtonPushed(ActionEvent event) throws IOException{
     MainScreenController MSC = new MainScreenController();
     MSC.cancelButtonPushed.cancelButtonPushed(event);
    }
    
    public void addCustomerRecordButtonPushed(ActionEvent event) throws IOException{
    Parent switchAddCustomer = FXMLLoader.load(getClass().getResource("AddCustomer.fxml"));
            Scene addCustomerScene = new Scene(switchAddCustomer);
            Stage MainScreenWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
            MainScreenWindow.setScene(addCustomerScene);
    }
    
    public void updateCustomerRecordButtonPushed(ActionEvent event) throws IOException{
        try{
            if (!(customerTableView.getSelectionModel().getSelectedItem().equals(null))){
    Parent switchAddCustomer = FXMLLoader.load(getClass().getResource("UpdateCustomer.fxml"));
            Scene addCustomerScene = new Scene(switchAddCustomer);
            Stage MainScreenWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
            MainScreenWindow.setScene(addCustomerScene);
            }
        }   catch(NullPointerException e){
    Alert nothingSelectedAlert = new Alert(Alert.AlertType.ERROR);
            nothingSelectedAlert.setTitle("Selection Error");
            nothingSelectedAlert.setContentText("Please select the customer record that you would like to update.");
            nothingSelectedAlert.showAndWait();
    }
    }
    
    public void deleteCustomerRecordButtonPushed(ActionEvent event) throws IOException{
        try{
        if (!(customerTableView.getSelectionModel().getSelectedItem().equals(null))){
        for (int i = 0; i < CustomerAppointmentCoordinator.getAllCustomers().size(); i++){
     Customer tempCustomer = CustomerAppointmentCoordinator.getAllCustomers().get(i);
            
            if (tempCustomer.getCustomerId() == getSelectedCustomerId()){
     CustomerAppointmentCoordinator.getAllCustomers().remove(i);
     }
        }
     }
        DBQuery.deleteCustomer(getSelectedCustomerId());
        }
        catch (NullPointerException e){
        Alert nothingSelectedAlert = new Alert(Alert.AlertType.ERROR);
            nothingSelectedAlert.setTitle("Selection Error");
            nothingSelectedAlert.setContentText("Please select the customer record that you would like to delete.");
            nothingSelectedAlert.showAndWait();
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MainScreenController.setPageNum1();
        if (counter == 0){
        retrieveAllCustomers();
        counter++;
        }
       customerTableView.setItems(CustomerAppointmentCoordinator.getAllCustomers());
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerAddress2Column.setCellValueFactory(new PropertyValueFactory<>("address2"));
        customerPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customerTableView.refresh();
    }    
    
}
