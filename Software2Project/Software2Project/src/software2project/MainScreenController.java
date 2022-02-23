/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2project;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

/*I also refer to this as the calendar. The class contains the functionality to sort by month and by week. 
The TableView starts by default with a view of all appointments and a view of all appointments can also be found by clicking the client appointments button.
Both of the lambda expressions for requirement G are located in this controller class.
*/
public class MainScreenController implements Initializable {

    ObservableList<Appointment> searchAppointmentList = FXCollections.observableArrayList();
    ObservableList<Appointment> sortedAppointmentList = FXCollections.observableArrayList();
    private static ArrayList<User> AllUsers = DBQuery.getAllUsers();
    private static int counter = 0;
    private static int MonthWeekCounter = 0;
    private static int initializeCounter = 0;
    private static int selectedMonthChangeCounter = 0;
    private static int selectedYearChangeCounter = 0;
    private static int appointmentCheck = 0; 
    private static int pageNum = 0;
    @FXML
    private ComboBox yearComboBox;
    @FXML
    private ComboBox monthComboBox;
    @FXML
    private ComboBox dayComboBox;
    @FXML
    Button MonthlyWeeklyViewButton;
    @FXML
    TableView<Appointment> CalendarTableView;
    @FXML
    TableColumn<Appointment, Integer> customerIdColumn;
    @FXML
    TableColumn<Customer, String> customerNameColumn;
    @FXML
    TableColumn<Appointment, String> consultantNameColumn;
    @FXML
    TableColumn<Appointment, String> appointmentTypeColumn;
    @FXML
    TableColumn<Appointment, String> startTimeColumn;
    @FXML
    TableColumn<Appointment, String> endTimeColumn;

    public void exitButtonPushed() {
        System.exit(0);
    }

    public static ArrayList getAllUsers() {
        return AllUsers;
    }

    public static void setPageNum1() {
        pageNum = 1;
    }

    public static void setPageNum2() {
        pageNum = 2;
    }

    public static void setPageNum3() {
        pageNum = 3;
    }
    
    public void reportsButtonPushed(ActionEvent event) throws IOException {

        Parent switchToReports = FXMLLoader.load(getClass().getResource("Reports.fxml"));
        Scene reportsScene = new Scene(switchToReports);
        Stage reportsWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
        reportsWindow.setScene(reportsScene);
    }

    public void clientRecordsButtonPushed(ActionEvent event) throws IOException {

        Parent switchToModifyRecords = FXMLLoader.load(getClass().getResource("ModifyRecords.fxml"));
        Scene ModifyRecordsScene = new Scene(switchToModifyRecords);
        Stage ModifyRecordsWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
        ModifyRecordsWindow.setScene(ModifyRecordsScene);
    }

    public void clientAppointmentsButtonPushed(ActionEvent event) throws IOException {

        Parent switchToModifyAppointments = FXMLLoader.load(getClass().getResource("ModifyAppointments.fxml"));
        Scene ModifyAppointmentsScene = new Scene(switchToModifyAppointments);
        Stage ModifyAppointmentsWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
        ModifyAppointmentsWindow.setScene(ModifyAppointmentsScene);
    }
/* Switches the calendar TableView to allow for a weekly view. I went with a rolling weekly view, meaning the week for the weekly view starts on the day selected. 
    Example: if you were to select the 1st of the month, then the TableView will show all appointments from the 1st of the month to the 7th of the month.   
    */
    public void weeklyViewButtonPushed() {
        sortedAppointmentList.clear();
        if (MonthWeekCounter == 0) {
            dayComboBox.setVisible(true);
            MonthlyWeeklyViewButton.setText("Switch To Monthly View");
            MonthWeekCounter++;
            String selectedMonth = monthComboBox.getSelectionModel().getSelectedItem().toString();           
            if (selectedMonth.equals("January") || selectedMonth.equals("March") || selectedMonth.equals("May") || selectedMonth.equals("July")
                    || selectedMonth.equals("August") || selectedMonth.equals("October") || selectedMonth.equals("December")) {
                dayComboBox.getItems().clear();
                dayComboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31");
                dayComboBox.getSelectionModel().selectFirst();
            } else if (selectedMonth.equals("April") || selectedMonth.equals("June") || selectedMonth.equals("September") || selectedMonth.equals("November")) {
                dayComboBox.getItems().clear();
                dayComboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30");
                dayComboBox.getSelectionModel().selectFirst();
            } else if (selectedMonth.equals("February") && (Integer.parseInt(yearComboBox.getSelectionModel().getSelectedItem().toString()) % 4 == 0)) {
                dayComboBox.getItems().clear();
                dayComboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29");
                dayComboBox.getSelectionModel().selectFirst();
            } else if (selectedMonth.equals("February") && (Integer.parseInt(yearComboBox.getSelectionModel().getSelectedItem().toString()) % 4 != 0)) {
                dayComboBox.getItems().clear();
                dayComboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28");
                dayComboBox.getSelectionModel().selectFirst();
            }

        } else if (MonthWeekCounter == 1) {
            MonthlyWeeklyViewButton.setText("Switch To Weekly View");
            MonthWeekCounter--;
            dayComboBox.setVisible(false);
            selectedMonthChange();
        }
        if (initializeCounter == 0) {
            initializeCounter++;
        } else {
            initializeCounter--;
        }
        selectedDayChanged();
    }
//Allows to view the calendar based on a selected month for up to 2 years ahead of the current calendar year. Also takes care of adding a day in February during leap years.  
    public void selectedYearChanged() {
        selectedYearChangeCounter = 1;
        String selectedMonth = monthComboBox.getSelectionModel().getSelectedItem().toString();
        if ((MonthWeekCounter == 1) && monthComboBox.getSelectionModel().getSelectedItem().toString().equals("February")) {
            if (Integer.parseInt(yearComboBox.getSelectionModel().getSelectedItem().toString()) % 4 == 0) {
                dayComboBox.getItems().clear();
                dayComboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29");
                dayComboBox.getSelectionModel().selectFirst();
            } else if (Integer.parseInt(yearComboBox.getSelectionModel().getSelectedItem().toString()) % 4 != 0) {
                dayComboBox.getItems().clear();
                dayComboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28");
                dayComboBox.getSelectionModel().selectFirst();
            }
        }

        if (MonthWeekCounter == 0) {
            sortedAppointmentList.clear();
            for (int i = 0; i < searchAppointmentList.size(); i++) {
                if (searchAppointmentList.get(i).getStartYear() == Integer.parseInt(yearComboBox.getSelectionModel().getSelectedItem().toString()) && searchAppointmentList.get(i).getStartMonth() == selectedMonthToInt.monthToInt(selectedMonth)) {
                    sortedAppointmentList.add(searchAppointmentList.get(i));
                }
            }
            CalendarTableView.setItems(sortedAppointmentList);
        }

        if (MonthWeekCounter == 1) {
            sortedAppointmentList.clear();
            String selectedYear = String.valueOf(yearComboBox.getValue());
            String selectedDay = String.valueOf(dayComboBox.getSelectionModel().getSelectedItem());
            LocalDate appointmentDate = LocalDate.of(Integer.parseInt(selectedYear), selectedMonthToInt.monthToInt(selectedMonth), Integer.parseInt(selectedDay));
            LocalDate appointmentDate2 = appointmentDate.plusDays(1);
            LocalDate appointmentDate3 = appointmentDate.plusDays(2);
            LocalDate appointmentDate4 = appointmentDate.plusDays(3);
            LocalDate appointmentDate5 = appointmentDate.plusDays(4);
            LocalDate appointmentDate6 = appointmentDate.plusDays(5);
            LocalDate appointmentDate7 = appointmentDate.plusDays(6);
            for (int i = 0; i < searchAppointmentList.size(); i++) {
                if (searchAppointmentList.get(i).getStartYear() == Integer.parseInt(yearComboBox.getSelectionModel().getSelectedItem().toString())
                        && searchAppointmentList.get(i).getStartMonth() == selectedMonthToInt.monthToInt(selectedMonth) && ((searchAppointmentList.get(i).getStartDay()
                        == appointmentDate.getDayOfMonth()) || (searchAppointmentList.get(i).getStartDay() == appointmentDate2.getDayOfMonth()) || (searchAppointmentList.get(i).getStartDay() == appointmentDate3.getDayOfMonth())
                        || (searchAppointmentList.get(i).getStartDay() == appointmentDate4.getDayOfMonth()) || (searchAppointmentList.get(i).getStartDay() == appointmentDate5.getDayOfMonth())
                        || (searchAppointmentList.get(i).getStartDay() == appointmentDate6.getDayOfMonth()) || (searchAppointmentList.get(i).getStartDay() == appointmentDate7.getDayOfMonth()))) {

                    sortedAppointmentList.add(searchAppointmentList.get(i));
                }

            }
            CalendarTableView.setItems(sortedAppointmentList);
        }
        selectedYearChangeCounter = 0;
    }
    //changes the calendar to display appointments based on the selected month and/or day.
    public void selectedMonthChange() {
        String selectedMonth = monthComboBox.getSelectionModel().getSelectedItem().toString();
        selectedMonthChangeCounter = 1;
        if ((MonthWeekCounter == 1)) {
            if (selectedMonth.equals("January") || selectedMonth.equals("March") || selectedMonth.equals("May") || selectedMonth.equals("July")
                    || selectedMonth.equals("August") || selectedMonth.equals("October") || selectedMonth.equals("December")) {
                dayComboBox.getItems().clear();
                dayComboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31");
                dayComboBox.getSelectionModel().selectFirst();
            } else if (selectedMonth.equals("April") || selectedMonth.equals("June") || selectedMonth.equals("September") || selectedMonth.equals("November")) {
                dayComboBox.getItems().clear();
                dayComboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30");
                dayComboBox.getSelectionModel().selectFirst();
            } else if (selectedMonth.equals("February") && (Integer.parseInt(yearComboBox.getSelectionModel().getSelectedItem().toString()) % 4 == 0)) {
                dayComboBox.getItems().clear();
                dayComboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29");
                dayComboBox.getSelectionModel().selectFirst();
            } else if (selectedMonth.equals("February") && (Integer.parseInt(yearComboBox.getSelectionModel().getSelectedItem().toString()) % 4 != 0)) {
                dayComboBox.getItems().clear();
                dayComboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28");
                dayComboBox.getSelectionModel().selectFirst();
            }
        }
        if (MonthWeekCounter == 0) {
            sortedAppointmentList.clear();
            for (int i = 0; i < searchAppointmentList.size(); i++) {
                if (searchAppointmentList.get(i).getStartYear() == Integer.parseInt(yearComboBox.getSelectionModel().getSelectedItem().toString()) && searchAppointmentList.get(i).getStartMonth() == selectedMonthToInt.monthToInt(selectedMonth)) {
                    sortedAppointmentList.add(searchAppointmentList.get(i));
                }
            }
            CalendarTableView.setItems(sortedAppointmentList);
        }
        if (MonthWeekCounter == 1) {
            sortedAppointmentList.clear();
            String selectedYear = String.valueOf(yearComboBox.getValue());
            String selectedDay = String.valueOf(dayComboBox.getSelectionModel().getSelectedItem());
            LocalDate appointmentDate = LocalDate.of(Integer.parseInt(selectedYear), selectedMonthToInt.monthToInt(selectedMonth), Integer.parseInt(selectedDay));
            LocalDate appointmentDate2 = appointmentDate.plusDays(1);
            LocalDate appointmentDate3 = appointmentDate.plusDays(2);
            LocalDate appointmentDate4 = appointmentDate.plusDays(3);
            LocalDate appointmentDate5 = appointmentDate.plusDays(4);
            LocalDate appointmentDate6 = appointmentDate.plusDays(5);
            LocalDate appointmentDate7 = appointmentDate.plusDays(6);
            for (int i = 0; i < searchAppointmentList.size(); i++) {
                if (searchAppointmentList.get(i).getStartYear() == Integer.parseInt(yearComboBox.getSelectionModel().getSelectedItem().toString())
                        && searchAppointmentList.get(i).getStartMonth() == selectedMonthToInt.monthToInt(selectedMonth) && ((searchAppointmentList.get(i).getStartDay()
                        == appointmentDate.getDayOfMonth()) || (searchAppointmentList.get(i).getStartDay() == appointmentDate2.getDayOfMonth()) || (searchAppointmentList.get(i).getStartDay() == appointmentDate3.getDayOfMonth())
                        || (searchAppointmentList.get(i).getStartDay() == appointmentDate4.getDayOfMonth()) || (searchAppointmentList.get(i).getStartDay() == appointmentDate5.getDayOfMonth())
                        || (searchAppointmentList.get(i).getStartDay() == appointmentDate6.getDayOfMonth()) || (searchAppointmentList.get(i).getStartDay() == appointmentDate7.getDayOfMonth()))) {

                    sortedAppointmentList.add(searchAppointmentList.get(i));
                }

            }
            CalendarTableView.setItems(sortedAppointmentList);
        }
        selectedMonthChangeCounter = 0;
    }
    // provides the functionality to select the start day for a rolling week view. 
    public void selectedDayChanged() {
        if (initializeCounter == 1 && selectedMonthChangeCounter == 0 && selectedYearChangeCounter == 0) {
            String selectedMonth = monthComboBox.getSelectionModel().getSelectedItem().toString();
            String selectedYear = String.valueOf(yearComboBox.getValue());
            String selectedDay = String.valueOf(dayComboBox.getSelectionModel().getSelectedItem());
            sortedAppointmentList.clear();

            LocalDate appointmentDate = LocalDate.of(Integer.parseInt(selectedYear), selectedMonthToInt.monthToInt(selectedMonth), Integer.parseInt(selectedDay));
            LocalDate appointmentDate2 = appointmentDate.plusDays(1);
            LocalDate appointmentDate3 = appointmentDate.plusDays(2);
            LocalDate appointmentDate4 = appointmentDate.plusDays(3);
            LocalDate appointmentDate5 = appointmentDate.plusDays(4);
            LocalDate appointmentDate6 = appointmentDate.plusDays(5);
            LocalDate appointmentDate7 = appointmentDate.plusDays(6);
            for (int i = 0; i < searchAppointmentList.size(); i++) {
                if (searchAppointmentList.get(i).getStartYear() == Integer.parseInt(yearComboBox.getSelectionModel().getSelectedItem().toString())
                        && searchAppointmentList.get(i).getStartMonth() == selectedMonthToInt.monthToInt(selectedMonth) && ((searchAppointmentList.get(i).getStartDay()
                        == appointmentDate.getDayOfMonth()) || (searchAppointmentList.get(i).getStartDay() == appointmentDate2.getDayOfMonth()) || (searchAppointmentList.get(i).getStartDay() == appointmentDate3.getDayOfMonth())
                        || (searchAppointmentList.get(i).getStartDay() == appointmentDate4.getDayOfMonth()) || (searchAppointmentList.get(i).getStartDay() == appointmentDate5.getDayOfMonth())
                        || (searchAppointmentList.get(i).getStartDay() == appointmentDate6.getDayOfMonth()) || (searchAppointmentList.get(i).getStartDay() == appointmentDate7.getDayOfMonth()))) {

                    sortedAppointmentList.add(searchAppointmentList.get(i));
                }

            }
            CalendarTableView.setItems(sortedAppointmentList);
        }
    }

    
      /* First Lambda Expression. Provides a single place in the code where input months as strings, such as "April" or "September" are converted into their corresponding integer month values.
      this helps me by managing it all in one place in the code and by allowing users more options for the input of month values.
      Through that, my program provides better utility and more flexible options for the users. 
     */
    public static MonthToIntInterface selectedMonthToInt = s -> {
        String month = String.valueOf(s).toLowerCase();
        int monthNum = 0;
        switch (month) {
            case "january":
                monthNum = 1;
                break;
            case "february":
                monthNum = 2;
                break;
            case "march":
                monthNum = 3;
                break;
            case "april":
                monthNum = 4;
                break;
            case "may":
                monthNum = 5;
                break;
            case "june":
                monthNum = 6;
                break;
            case "july":
                monthNum = 7;
                break;
            case "august":
                monthNum = 8;
                break;
            case "september":
                monthNum = 9;
                break;
            case "october":
                monthNum = 10;
                break;
            case "november":
                monthNum = 11;
                break;
            case "december":
                monthNum = 12;
                break;
            default:
                monthNum = Integer.parseInt(s);
        }

        return monthNum;
    };

    /*Second Lambda Expression. Consolidates all cancel buttons functionality into a single controller. 
    This helps me because it makes the code cleaner and more readable by reducing the amount of repetitive code in each of the other controller classes.
    Doing it this way also makes fixing bugs easier because all of the functionality is in one place, so its easier to see where potential problems could be occuring.
    */
    
    public CancelButtonInterface cancelButtonPushed = event -> {

        switch (pageNum) {
            case 1:
                Parent switchToMainScreen = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene MainScreenScene = new Scene(switchToMainScreen);
                Stage MainScreenWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
                MainScreenWindow.setScene(MainScreenScene);
                break;

            case 2:
 Parent switchToModifyRecords = FXMLLoader.load(getClass().getResource("ModifyRecords.fxml"));
            Scene ModifyRecordsScene = new Scene(switchToModifyRecords);
            Stage ModifyRecordsWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
            ModifyRecordsWindow.setScene(ModifyRecordsScene);
                break;

            case 3:
                 Parent switchToModifyAppointments = FXMLLoader.load(getClass().getResource("ModifyAppointments.fxml"));
        Scene ModifyAppointmentsScene = new Scene(switchToModifyAppointments);
        Stage ModifyAppointmentsWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
        ModifyAppointmentsWindow.setScene(ModifyAppointmentsScene);
                break;

        }
    };
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            yearComboBox.getItems().addAll(String.valueOf(LocalDate.now().getYear()), String.valueOf(Integer.parseInt(String.valueOf(LocalDate.now().getYear())) + 1), String.valueOf(Integer.parseInt(String.valueOf(LocalDate.now().getYear())) + 2));
            yearComboBox.getSelectionModel().selectFirst();
            monthComboBox.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
            monthComboBox.getSelectionModel().selectFirst();
            dayComboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28");
            dayComboBox.getSelectionModel().selectFirst();
            if (counter == 0) {
                DBQuery.retrieveAllAppointments();
                counter++;
            }
            for (Appointment allAppointments : CustomerAppointmentCoordinator.getAllAppointments()) {
                searchAppointmentList.add(allAppointments);
            }
            CalendarTableView.setItems(searchAppointmentList);
            customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            consultantNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
            appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
            startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            if (appointmentCheck == 0){
            for (int i = 0; i < CustomerAppointmentCoordinator.getAllAppointments().size(); i++) {
                if (CustomerAppointmentCoordinator.getAllAppointments().get(i).getUserId() == LoginFormController.getVerifiedUserId()) {
                    LocalDateTime checkAppointmentDateTime = CustomerAppointmentCoordinator.getAllAppointments().get(i).getZonedStartTime();
                    String userName = CustomerAppointmentCoordinator.getAllAppointments().get(i).getUserName();
                    String customerName = CustomerAppointmentCoordinator.getAllAppointments().get(i).getCustomerName();
                    long upcomingAppointment = ChronoUnit.MINUTES.between(LocalDateTime.now(), checkAppointmentDateTime);
                    if (upcomingAppointment <= 15 && upcomingAppointment >= 0) {
                        Alert upcomingAppointmentAlert = new Alert(Alert.AlertType.INFORMATION);
                        upcomingAppointmentAlert.setContentText("Hello " + userName + ". You have a consulting appointment with " + customerName + " within the next 15 minutes.");
                        upcomingAppointmentAlert.showAndWait();
                    }

                }
            }
            appointmentCheck++;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
