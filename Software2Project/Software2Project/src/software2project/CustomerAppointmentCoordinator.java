/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Nathan
 */

//class contains customer, appointment and user lists and related methods.
public class CustomerAppointmentCoordinator {
    private static ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    private static ObservableList<User> userList = FXCollections.observableArrayList();
    public static void addCustomer(Customer newCustomer){
    customerList.add(newCustomer);
    }
    public static void addAppointment(Appointment newAppointment){
    appointmentList.add(newAppointment);
    }
    public static void addUser(User newUser){
    userList.add(newUser);
    }
    public static ObservableList<User>getAllUsers(){
    return userList;
    }
    public static ObservableList<Appointment> getAllAppointments(){
    return appointmentList;
    }
    
    public static ObservableList<Customer> getAllCustomers() {
        return customerList;
    }
}
