/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2project;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.DBQuery;

/**
 * FXML Controller class
 *
 * @author Nathan
 */
//Class provides the reports functionality for requirement I 
public class ReportsController implements Initializable {

    @FXML
    Label reportLabel;
    @FXML
    ComboBox selectedConsultantComboBox;
    @FXML
    ComboBox ClientsPerConsultantComboBox;
    @FXML
    TextArea AppointmentTypesThisMonthTextArea;
    @FXML
    TextArea ClientsPerConsultantTextArea;
    @FXML
    TableView UserAppointmentsTableView;
    @FXML
    TableColumn selectedUserTableColumn;
    @FXML
    TableColumn customerTableColumn;
    @FXML
    TableColumn appointmentTypeTableColumn;
    @FXML
    TableColumn appointmentStartTimeTableColumn;
    @FXML
    TableColumn appointmentEndTimeTableColumn;
    int initialize = 0;
    int userListControl = 0;
    int userListControl2 = 0;
    int appointmentCounter = 0;
    String AppointmentType = "";

    public void cancelButtonPushed(ActionEvent event) throws IOException {
        MainScreenController MSC = new MainScreenController();
        MSC.cancelButtonPushed.cancelButtonPushed(event);
    }

    public void MonthlyAppointmentTypesButtonPushed() {
        try {
            reportLabel.setText("Monthly Appointment Types");
            selectedConsultantComboBox.setVisible(false);
            UserAppointmentsTableView.setVisible(false);
            ClientsPerConsultantComboBox.setVisible(false);
            ClientsPerConsultantTextArea.setVisible(false);
            ArrayList<String> appointmentTypesThisMonthArrayList = new ArrayList<String>();

            for (Appointment allAppointments : CustomerAppointmentCoordinator.getAllAppointments()) {
                if (allAppointments.getStartMonth() == LocalDate.now().getMonthValue()) {
                    appointmentTypesThisMonthArrayList.add(allAppointments.getType());
                }
            }
            ArrayList<String> SortedTypeList = new ArrayList<String>(new LinkedHashSet<String>(appointmentTypesThisMonthArrayList));
            AppointmentType = "There are " + SortedTypeList.size() + " different types of appointments this month. The types of appointments this month are: ";
            if (SortedTypeList.size() == 0) {
                AppointmentType = "There are " + SortedTypeList.size() + " different types of appointments this month.";
            }
            for (int i = 0; i < SortedTypeList.size(); i++) {

                if (i < SortedTypeList.size() - 1 && i + 1 != SortedTypeList.size() - 1) {
                    AppointmentType = AppointmentType + " " + SortedTypeList.get(i) + ",";
                } else if (i < SortedTypeList.size() - 1 && i + 1 == SortedTypeList.size() - 1) {
                    AppointmentType = AppointmentType + " " + SortedTypeList.get(i) + " and";
                } else if (i == SortedTypeList.size() - 1) {
                    AppointmentType = AppointmentType + " " + SortedTypeList.get(i) + ".";
                }

            }
            AppointmentTypesThisMonthTextArea.setText(AppointmentType);
            AppointmentTypesThisMonthTextArea.setWrapText(true);
            AppointmentTypesThisMonthTextArea.setVisible(true);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

    }

    public void ConsultantSchedulesButtonPushed() {
        try {
            AppointmentTypesThisMonthTextArea.setVisible(false);
            ClientsPerConsultantComboBox.setVisible(false);
            ClientsPerConsultantTextArea.setVisible(false);
            ClientsPerConsultantComboBox.setVisible(false);
            reportLabel.setText("Consultant Schedules");

            ArrayList<User> AllUsers = MainScreenController.getAllUsers();
            int numUsers = AllUsers.size();
            if (userListControl == 0) {
                for (int i = 0; i < numUsers; i++) {
                    selectedConsultantComboBox.getItems().addAll(AllUsers.get(i).getUserName());
                }
                userListControl++;
            }
            ObservableList<Appointment> sortedAppointmentList = sortAppointmentsByUser(AllUsers.get(0).getUserName());

            selectedConsultantComboBox.getSelectionModel().selectFirst();
            selectedConsultantComboBox.setVisible(true);
            UserAppointmentsTableView.setVisible(true);

            UserAppointmentsTableView.setItems(sortedAppointmentList);
            selectedUserTableColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));;
            customerTableColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));;
            appointmentTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
            appointmentStartTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            appointmentEndTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    public void clientsPerConsultantButtonPushed() {
        reportLabel.setText("Clients Per Consultant");
        AppointmentTypesThisMonthTextArea.setVisible(false);
        selectedConsultantComboBox.setVisible(false);
        UserAppointmentsTableView.setVisible(false);
        ClientsPerConsultantComboBox.setVisible(true);
        ClientsPerConsultantTextArea.setVisible(true);
        ArrayList<User> AllUsers = MainScreenController.getAllUsers();
        int numUsers = AllUsers.size();
        if (userListControl2 == 0) {
            for (int i = 0; i < numUsers; i++) {
                ClientsPerConsultantComboBox.getItems().addAll(AllUsers.get(i).getUserName());
            }
            userListControl2++;
        }
        ClientsPerConsultantComboBox.getSelectionModel().selectFirst();
        ObservableList<Appointment> UserAppointmentList = sortAppointmentsByUser(AllUsers.get(0).getUserName());
        ArrayList<String> CustomersPerConsultantList = new ArrayList<String>();
        for (int i = 0; i < UserAppointmentList.size(); i++) {
            if (UserAppointmentList.get(i).getConsultantName().equals(ClientsPerConsultantComboBox.getSelectionModel().getSelectedItem().toString())) {
                CustomersPerConsultantList.add(UserAppointmentList.get(i).getCustomerName());
            }
        }
        ArrayList<String> sortedCustomersPerConsultantList = new ArrayList<String>(new LinkedHashSet<String>(CustomersPerConsultantList));

        String clientsPerConsultant = "Consultant " + ClientsPerConsultantComboBox.getSelectionModel().getSelectedItem().toString() + " currently works with " + sortedCustomersPerConsultantList.size() + " client(s).";
        if (sortedCustomersPerConsultantList.size() == 1) {
            clientsPerConsultant = clientsPerConsultant + " The client's name is: ";
        }
        if (sortedCustomersPerConsultantList.size() > 1) {
            clientsPerConsultant = clientsPerConsultant + " The clients names are: ";
        }
        for (int i = 0; i < sortedCustomersPerConsultantList.size(); i++) {
            if (i < sortedCustomersPerConsultantList.size() - 2) {
                clientsPerConsultant = clientsPerConsultant + sortedCustomersPerConsultantList.get(i) + ", ";
            } else if (i == sortedCustomersPerConsultantList.size() - 2) {
                clientsPerConsultant = clientsPerConsultant + sortedCustomersPerConsultantList.get(i) + " and ";
            } else if (i == sortedCustomersPerConsultantList.size() - 1) {
                clientsPerConsultant = clientsPerConsultant + sortedCustomersPerConsultantList.get(i) + ".";
            }
        }
        ClientsPerConsultantTextArea.setText(clientsPerConsultant);
        ClientsPerConsultantTextArea.setWrapText(true);

    }

    public ObservableList<Appointment> sortAppointmentsByUser(String userName) {
        ObservableList<Appointment> sortedAppointmentList = FXCollections.observableArrayList();

        for (Appointment allAppointments : CustomerAppointmentCoordinator.getAllAppointments()) {
            if (allAppointments.getConsultantName().equals(userName)) {
                sortedAppointmentList.add(allAppointments);
            }
        }
        return sortedAppointmentList;
    }
    //Updates the TableView when a different user is selected from the ComboBox.

    public void sortAppointmentsBySelectedUser() {

        UserAppointmentsTableView.setItems(sortAppointmentsByUser(selectedConsultantComboBox.getSelectionModel().getSelectedItem().toString()));
        UserAppointmentsTableView.refresh();
    }
    //Updates the TextArea when a different user is selected from the ComboBox.

    public void sortClientsBySelectedUser() {

        ObservableList<Appointment> UserAppointmentList = sortAppointmentsByUser(ClientsPerConsultantComboBox.getSelectionModel().getSelectedItem().toString());

        ArrayList<String> CustomersPerConsultantList = new ArrayList<>();
        for (int i = 0; i < UserAppointmentList.size(); i++) {
            if (UserAppointmentList.get(i).getConsultantName().equals(ClientsPerConsultantComboBox.getSelectionModel().getSelectedItem().toString())) {
                CustomersPerConsultantList.add(UserAppointmentList.get(i).getCustomerName());
            }
        }
        ArrayList<String> sortedCustomersPerConsultantList = new ArrayList<>(new LinkedHashSet<>(CustomersPerConsultantList));

        String clientsPerConsultant = "Consultant " + ClientsPerConsultantComboBox.getSelectionModel().getSelectedItem().toString() + " currently works with " + sortedCustomersPerConsultantList.size() + " client(s).";
        if (sortedCustomersPerConsultantList.size() == 1) {
            clientsPerConsultant = clientsPerConsultant + " The client's name is: ";
        }
        if (sortedCustomersPerConsultantList.size() > 1) {
            clientsPerConsultant = clientsPerConsultant + " The clients names are: ";
        }
        for (int i = 0; i < sortedCustomersPerConsultantList.size(); i++) {
            if (i < sortedCustomersPerConsultantList.size() - 2) {
                clientsPerConsultant = clientsPerConsultant + sortedCustomersPerConsultantList.get(i) + ", ";
            } else if (i == sortedCustomersPerConsultantList.size() - 2) {
                clientsPerConsultant = clientsPerConsultant + sortedCustomersPerConsultantList.get(i) + " and ";
            } else if (i == sortedCustomersPerConsultantList.size() - 1) {
                clientsPerConsultant = clientsPerConsultant + sortedCustomersPerConsultantList.get(i) + ".";
            }
        }
        ClientsPerConsultantTextArea.setText(clientsPerConsultant);

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MainScreenController.setPageNum1();
        if (initialize == 0) {
            ConsultantSchedulesButtonPushed();
        }
    }

}
