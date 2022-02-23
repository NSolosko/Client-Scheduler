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
import static software2project.ModifyRecordsController.retrieveAllCustomers;
import utils.DBQuery;

/**
 * FXML Controller class
 *
 * @author Nathan
 */
public class ModifyAppointmentsController implements Initializable {

private static int appointmentId = 0;
    @FXML TableView <Appointment> AppointmentTableView;
    @FXML TableColumn<Appointment, Integer> customerIdColumn;
    @FXML TableColumn<Customer, String> customerNameColumn;
    @FXML TableColumn<Appointment, String> consultantNameColumn;
    @FXML TableColumn<Appointment, String> appointmentTypeColumn;
    @FXML TableColumn<Appointment, String> startTimeColumn;
    @FXML TableColumn<Appointment, String> endTimeColumn;
    
     public void cancelButtonPushed(ActionEvent event) throws IOException{
     MainScreenController MSC = new MainScreenController();
     MSC.cancelButtonPushed.cancelButtonPushed(event);
    }
    
     public void addAppointmentButtonPushed(ActionEvent event) throws IOException{
     Parent switchToAddAppointment = FXMLLoader.load(getClass().getResource("AddAppointment.fxml"));
            Scene addAppointmentScene = new Scene(switchToAddAppointment);
            Stage addAppointmentWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
            addAppointmentWindow.setScene(addAppointmentScene);
     }
     
    public void updateAppointmentButtonPushed(ActionEvent event) throws IOException{
        try{
        if(!(AppointmentTableView.getSelectionModel().getSelectedItem().equals(null))){
    Parent switchToUpdateAppointment = FXMLLoader.load(getClass().getResource("UpdateAppointment.fxml"));
            Scene updateAppointmentScene = new Scene(switchToUpdateAppointment);
            Stage updateAppointmentWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
            updateAppointmentWindow.setScene(updateAppointmentScene);
        }
        }
        catch(NullPointerException e){
    Alert nothingSelectedAlert = new Alert(Alert.AlertType.ERROR);
            nothingSelectedAlert.setTitle("Selection Error");
            nothingSelectedAlert.setContentText("Please select an appointment that you would like to update.");
            nothingSelectedAlert.showAndWait();
    }
    }
    public void deleteAppointmentButtonPushed(){
        try{
        if(!(AppointmentTableView.getSelectionModel().getSelectedItem().equals(null))){
        setSelectedAppointmentId();       
    for (int i = 0; i < CustomerAppointmentCoordinator.getAllAppointments().size(); i++){
    if  (this.appointmentId == CustomerAppointmentCoordinator.getAllAppointments().get(i).getAppointmentId()){
    DBQuery.deleteAppointment(this.appointmentId);
        CustomerAppointmentCoordinator.getAllAppointments().remove(CustomerAppointmentCoordinator.getAllAppointments().get(i));
    
    }
    }
        }
        }
        catch(NullPointerException e){
         Alert nothingSelectedAlert = new Alert(Alert.AlertType.ERROR);
            nothingSelectedAlert.setTitle("Selection Error");
            nothingSelectedAlert.setContentText("Please select an appointment that you would like to delete.");
            nothingSelectedAlert.showAndWait();
        }
    }
    public void setSelectedAppointmentId(){
    this.appointmentId = AppointmentTableView.getSelectionModel().getSelectedItem().getAppointmentId();
    }
     public static int getSelectedAppointmentId(){
         return appointmentId;
     }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MainScreenController.setPageNum1();
      if (ModifyRecordsController.getCounter() == 0) {
            retrieveAllCustomers();
            ModifyRecordsController.incrementCounter();
        }
        
        
       AppointmentTableView.setItems(CustomerAppointmentCoordinator.getAllAppointments());
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        consultantNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    }
    
}


