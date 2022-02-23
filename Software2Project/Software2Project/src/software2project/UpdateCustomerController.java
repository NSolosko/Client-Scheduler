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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DBQuery;

/**
 * FXML Controller class
 *
 * @author Nathan
 */
public class UpdateCustomerController implements Initializable {

   
    
      @FXML private TextField customerIdTextField;
     @FXML private TextField customerNameTextField;
     @FXML private TextField customerPhoneNumberTextField;
     @FXML private TextField countryTextField;       
     @FXML private TextField cityTextField;
     @FXML private TextField addressTextField;       
     @FXML private TextField address2TextField;       
     @FXML private TextField postalCodeTextField;     
     private static int exceptionControlCounter = 0;
     
     public static void setExceptionControlCounter(){
     exceptionControlCounter = 1;
     }
     //Get the data from the associated customer object to populate the TextFields and then potentially be updated. 
     public void setSelectedCustomerData(int customerId){
     for (Customer selectedCustomer : CustomerAppointmentCoordinator.getAllCustomers()){
     if (selectedCustomer.getCustomerId() == customerId){
     customerIdTextField.setText(Integer.toString(customerId));
     customerNameTextField.setText(selectedCustomer.getName());
     customerPhoneNumberTextField.setText(selectedCustomer.getPhoneNumber());
     countryTextField.setText(selectedCustomer.getCountry());
     cityTextField.setText(selectedCustomer.getCity());
     addressTextField.setText(selectedCustomer.getAddress());
     address2TextField.setText(selectedCustomer.getAddress2());
     postalCodeTextField.setText(selectedCustomer.getPostalCode());
     }
     }
     }
     
    public void saveButtonPushed(ActionEvent event) throws IOException, SQLException{
         boolean fieldCheck = true;
         exceptionControlCounter = 0;
        int customerId = Integer.parseInt(customerIdTextField.getText());
        String name = customerNameTextField.getText();
        String phoneNumber = customerPhoneNumberTextField.getText();
        String country = countryTextField.getText();
        String city = cityTextField.getText();
        String address = addressTextField.getText();
        String address2 = address2TextField.getText();
        String postalCode = postalCodeTextField.getText();
        
         if (name.trim().equals("") || phoneNumber.trim().equals("") || country.trim().equals("") || city.trim().equals("") || address.trim().equals("") || address2.trim().equals("") || postalCode.trim().equals("")){
         Alert emptyFieldAlert = new Alert(Alert.AlertType.ERROR);
         emptyFieldAlert.setContentText("Please input alid Values for all fields. If no address2 exists for the customer, please enter N/A.");
         emptyFieldAlert.showAndWait();
         fieldCheck = false;
         } 
        if (fieldCheck == true){
        for (Customer selectedCustomer : CustomerAppointmentCoordinator.getAllCustomers()){
     if (selectedCustomer.getCustomerId() == customerId){
     selectedCustomer.setName(name);
     selectedCustomer.setPhoneNumber(phoneNumber);
     selectedCustomer.setCountry(country);
     selectedCustomer.setCity(city);
     selectedCustomer.setAddress(address);
     selectedCustomer.setAddress2(address2);
     selectedCustomer.setPostalCode(postalCode);
     }
        }
        
        DBQuery.updateCustomer(customerId, name, phoneNumber, address, address2, postalCode, city, country);
        if (exceptionControlCounter == 0){
         Parent switchToModifyRecords = FXMLLoader.load(getClass().getResource("ModifyRecords.fxml"));
            Scene ModifyRecordsScene = new Scene(switchToModifyRecords);
            Stage ModifyRecordsWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
            ModifyRecordsWindow.setScene(ModifyRecordsScene);
     }
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
        customerIdTextField.setEditable(false);
        MainScreenController.setPageNum2();
        setSelectedCustomerData(ModifyRecordsController.getSelectedCustomerId());
    }    
    
}
