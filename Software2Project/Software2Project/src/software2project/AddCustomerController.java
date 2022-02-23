/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2project;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DBQuery;

/**
 * FXML Controller class
 *
 * @author Nathan
 */
public class AddCustomerController implements Initializable {

     @FXML private TextField customerIdTextField;
     @FXML private TextField customerNameTextField;
     @FXML private TextField customerPhoneNumberTextField;
     @FXML private TextField countryTextField;       
     @FXML private TextField cityTextField;
     @FXML private TextField addressTextField;       
     @FXML private TextField address2TextField;       
     @FXML private TextField postalCodeTextField;       
     
     //Creates a new customer object and adds it to both a local ArrayList and to the database.
     public void saveButtonPushed(ActionEvent event) throws IOException, SQLException{
         try{
         boolean fieldCheck = true;
         String name = customerNameTextField.getText();
         String phoneNumber = customerPhoneNumberTextField.getText();
         String country = countryTextField.getText();
         String city = cityTextField.getText();
         String address = addressTextField.getText();   
         String address2 = address2TextField.getText();
         String postalCode =  postalCodeTextField.getText();
         
         if (name.trim().equals("") || phoneNumber.trim().equals("") || country.trim().equals("") || city.trim().equals("") || address.trim().equals("") || address2.trim().equals("") || postalCode.trim().equals("")){
         Alert emptyFieldAlert = new Alert(Alert.AlertType.ERROR);
         emptyFieldAlert.setContentText("Please input valid values for all fields. If no address2 exists for the customer, please enter N/A.");
         emptyFieldAlert.showAndWait();
         fieldCheck = false;
         } 
         
         if (fieldCheck == true){
         DBQuery.addCountry(country);
         int countryId = DBQuery.getCountryId(country);
         DBQuery.addCity(city, countryId);
       int cityId = DBQuery.getCityId(city);
         DBQuery.addAddress(address, address2, cityId, postalCode, phoneNumber);
    int addressId = DBQuery.getAddressId(address);
         DBQuery.addCustomer(name, addressId);
         
         Customer newCustomer = new Customer(DBQuery.getCustomerId(name), name, phoneNumber, address, address2, postalCode, city, country);
         
      
         CustomerAppointmentCoordinator.addCustomer(newCustomer);
         
          Parent switchToModifyRecords = FXMLLoader.load(getClass().getResource("ModifyRecords.fxml"));
            Scene ModifyRecordsScene = new Scene(switchToModifyRecords);
            Stage ModifyRecordsWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
            ModifyRecordsWindow.setScene(ModifyRecordsScene);
         }
         }
         catch(SQLException e){
         System.out.println(e.getMessage());
         Alert SQLAlert = new Alert(AlertType.ERROR);
         SQLAlert.setContentText("Please input valid values for all fields. If no address2 exists for the customer, please enter N/A.");
         SQLAlert.showAndWait();
         }
     }
    
    public void cancelButtonPushed(ActionEvent event) throws IOException{
      MainScreenController MSC = new MainScreenController();
     MSC.cancelButtonPushed.cancelButtonPushed(event);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MainScreenController.setPageNum2();
       this.customerIdTextField.setEditable(false);
       this.customerIdTextField.setMouseTransparent(true);
       this.customerIdTextField.setFocusTraversable(false);
    }    
    
}
