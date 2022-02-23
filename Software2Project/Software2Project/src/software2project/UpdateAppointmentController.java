/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2project;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static software2project.ModifyRecordsController.retrieveAllCustomers;
import utils.DBQuery;

/**
 * FXML Controller class
 *
 * @author Nathan
 */
public class UpdateAppointmentController implements Initializable {

    private int appointmentId;
    @FXML
    TextField StartDayTextField;
    @FXML
    TextField StartMonthTextField;
    @FXML
    TextField StartYearTextField;
    @FXML
    TextField StartTimeTextField;
    @FXML
    TextField appointmentDurationHoursTextField;
    @FXML
    TextField appointmentDurationMinutesTextField;
    @FXML
    ComboBox ConsultantComboBox;
    @FXML
    ComboBox CustomerNameComboBox;
    @FXML
    TextField TitleTextField;
    @FXML
    TextField DescriptionTextField;
    @FXML
    TextField LocationTextField;
    @FXML
    TextField ContactTextField;
    @FXML
    TextField AppointmentTypeTextField;
    @FXML
    TextField UrlTextField;

    public void cancelButtonPushed(ActionEvent event) throws IOException {
        MainScreenController MSC = new MainScreenController();
     MSC.cancelButtonPushed.cancelButtonPushed(event);
    }

    public void saveButtonPushed(ActionEvent event) throws IOException {
        try {
            boolean nonOverlappingAppointment = true;
            boolean appointmentWithinBusinessHours = true;
            boolean allFieldsFilled = true;
            int alertCount = 0;
            int overlappingAppointmentCount = 0;

            appointmentId = ModifyAppointmentsController.getSelectedAppointmentId();
            for (Appointment selectedAppointment : CustomerAppointmentCoordinator.getAllAppointments()) {
                if (selectedAppointment.getAppointmentId() == appointmentId) {

                    int appointmentStartDay = Integer.parseInt(StartDayTextField.getText());
                    int appointmentStartMonth = MainScreenController.selectedMonthToInt.monthToInt(StartMonthTextField.getText());
                    int appointmentStartYear = Integer.parseInt(StartYearTextField.getText());
                    LocalDate insertedStartDate = LocalDate.of(appointmentStartYear, appointmentStartMonth, appointmentStartDay);
                    LocalTime insertedStartTime = LocalTime.parse(StartTimeTextField.getText());

                    LocalDate localStartDate = LocalDate.of(appointmentStartYear, appointmentStartMonth, appointmentStartDay);
                    LocalTime localStartTime = LocalTime.parse(StartTimeTextField.getText());
                    LocalDateTime localStartDateTime = LocalDateTime.of(localStartDate, localStartTime);

                    if (appointmentStartYear < LocalDate.now().getYear()) {
                        throw new DateTimeException("Invalid year");
                    }

                    ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
                    ZoneId utcZoneID = ZoneId.of("UTC");
                    ZonedDateTime localToUtcDateTime = ZonedDateTime.of(insertedStartDate, insertedStartTime, localZoneId);
                    Instant convertToUTC = localToUtcDateTime.toInstant();
                    localToUtcDateTime = ZonedDateTime.ofInstant(convertToUTC, utcZoneID);
                    insertedStartDate = LocalDate.of(localToUtcDateTime.getYear(), localToUtcDateTime.getMonth(), localToUtcDateTime.getDayOfMonth());
                    insertedStartTime = LocalTime.of(localToUtcDateTime.getHour(), localToUtcDateTime.getMinute());

                    LocalDateTime startDateTime = LocalDateTime.of(insertedStartDate, insertedStartTime);
                    int appointmentDurationHours = Integer.parseInt(appointmentDurationHoursTextField.getText());
                    int appointmentDurationMinutes = Integer.parseInt(appointmentDurationMinutesTextField.getText());
                    LocalDateTime endDateTime = LocalDateTime.of(insertedStartDate, insertedStartTime);
                    endDateTime = endDateTime.plusHours(appointmentDurationHours);
                    endDateTime = endDateTime.plusMinutes(appointmentDurationMinutes);

                    String ConsultantName = ConsultantComboBox.getSelectionModel().getSelectedItem().toString();
                    int ConsultantId = DBQuery.getUserId(ConsultantName);
                    String customerName = CustomerNameComboBox.getSelectionModel().getSelectedItem().toString();
                    int customerId = DBQuery.getCustomerId(customerName);
                    String title = TitleTextField.getText();
                    String description = DescriptionTextField.getText();
                    String location = LocationTextField.getText();
                    String contact = ContactTextField.getText();
                    String appointmentType = AppointmentTypeTextField.getText();
                    String url = UrlTextField.getText();
                    int userId = DBQuery.getUserId(ConsultantName);
                    LocalDate endDate = endDateTime.toLocalDate();
                    LocalTime endTime = endDateTime.toLocalTime();

                    //get appointment times in local time so we can check for overlapping appointments.
                    LocalDateTime localEndDateTime = LocalDateTime.of(localStartDate, localStartTime);
                    localEndDateTime = localEndDateTime.plusHours(appointmentDurationHours);
                    localEndDateTime = localEndDateTime.plusMinutes(appointmentDurationMinutes);

                    for (int i = 0; i < CustomerAppointmentCoordinator.getAllAppointments().size(); i++) {

                        LocalDateTime savedAppointmentStartDateTime = LocalDateTime.of(CustomerAppointmentCoordinator.getAllAppointments().get(i).getStartYear(), CustomerAppointmentCoordinator.getAllAppointments().get(i).getStartMonth(),
                                CustomerAppointmentCoordinator.getAllAppointments().get(i).getStartDay(), CustomerAppointmentCoordinator.getAllAppointments().get(i).getSelectedAppointmentStartTime().getHour(),
                                CustomerAppointmentCoordinator.getAllAppointments().get(i).getSelectedAppointmentStartTime().getMinute());

                        ZonedDateTime localZonedDateTime = ZonedDateTime.ofInstant(savedAppointmentStartDateTime, ZoneOffset.UTC, localZoneId);
                        savedAppointmentStartDateTime = localZonedDateTime.toLocalDateTime();

                        LocalDateTime savedAppointmentEndDateTime = savedAppointmentStartDateTime.plusHours(CustomerAppointmentCoordinator.getAllAppointments().get(i).getSelectedAppointmentDurationHours());
                        savedAppointmentEndDateTime = savedAppointmentEndDateTime.plusMinutes(CustomerAppointmentCoordinator.getAllAppointments().get(i).getSelectedAppointmentDurationMinutes());

                        LocalTime businessHoursOpen = LocalTime.of(7, 59);
                        LocalTime businessHoursClose = LocalTime.of(17, 01);

                        if (((localStartDateTime.toLocalTime().isAfter(businessHoursOpen) && localStartDateTime.toLocalTime().isBefore(businessHoursClose)) && (localEndDateTime.toLocalTime().isBefore(businessHoursClose)))
                                && appointmentWithinBusinessHours == true) {
                            if(CustomerAppointmentCoordinator.getAllAppointments().get(i).getUserId() == ConsultantId){
                            if ( ((savedAppointmentStartDateTime.isBefore(localStartDateTime) && (savedAppointmentEndDateTime.isBefore(localStartDateTime)))
                                    || (savedAppointmentStartDateTime.isAfter(localEndDateTime)))) {

                            } else {
                                if (!(CustomerAppointmentCoordinator.getAllAppointments().get(i).getAppointmentId() == appointmentId)) {
                                    if (overlappingAppointmentCount == 0) {
                                        nonOverlappingAppointment = false;
                                        overlappingAppointmentCount++;
                                        Alert conflictingAppointmentAlert = new Alert(Alert.AlertType.ERROR);
                                        conflictingAppointmentAlert.setContentText("An appointment has already been scheduled for user " + ConsultantName + " between " + savedAppointmentStartDateTime.toLocalDate()
                                                + " " + savedAppointmentStartDateTime.toLocalTime() + " and " + savedAppointmentEndDateTime.toLocalDate() + " " + savedAppointmentEndDateTime.toLocalTime()
                                                + ". Please consult the calendar for more information on scheduled appointment times for this user.");
                                        conflictingAppointmentAlert.showAndWait();
                                    }
                                }
                            }
                            }
                        } else {
                            if (alertCount == 0) {
                                alertCount++;
                                appointmentWithinBusinessHours = false;
                                Alert withinBusinessHoursAlert = new Alert(Alert.AlertType.ERROR);

                                withinBusinessHoursAlert.setContentText("Business hours are from 8:00 am to 5:00 pm (08:00 to 17:00) local time. Please schedule your appointment within this timeframe.");
                                withinBusinessHoursAlert.showAndWait();
                            }
                        }
                    }

                    //prevent empty fields.
                    if (UrlTextField.getText().trim().equals("") || AppointmentTypeTextField.getText().trim().equals("") || ContactTextField.getText().trim().equals("")
                            || LocationTextField.getText().trim().equals("") || DescriptionTextField.getText().trim().equals("") || TitleTextField.getText().trim().equals("")) {
                        allFieldsFilled = false;
                        Alert emptyFieldAlert = new Alert(Alert.AlertType.ERROR);
                        emptyFieldAlert.setContentText("Please enter valid values for all fields. If a field is not applicable to an appointment, please enter N/A.");
                        emptyFieldAlert.showAndWait();
                    }
                    if (nonOverlappingAppointment == true && appointmentWithinBusinessHours == true && allFieldsFilled == true) {
                        DBQuery.updateAppointment(appointmentId, customerId, userId, title, description, location, contact, appointmentType, url, insertedStartDate, insertedStartTime, endDate, endTime);

                        if (DBQuery.getExceptionControlCounter() == 1) {
                            DBQuery.resetExceptionControlCounter();
                            throw new SQLException();
                        }

                        selectedAppointment.setStartTime(startDateTime);
                        selectedAppointment.setEndTime(endDateTime);
                        selectedAppointment.setCustomerId(customerId);
                        selectedAppointment.setCustomerName(customerName);
                        selectedAppointment.setUserId(userId);
                        selectedAppointment.setUserName(ConsultantName);
                        selectedAppointment.setTitle(title);
                        selectedAppointment.setDescription(description);
                        selectedAppointment.setLocation(location);
                        selectedAppointment.setContact(contact);
                        selectedAppointment.setType(appointmentType);
                        selectedAppointment.setUrl(url);

                        Parent switchToModifyAppointments = FXMLLoader.load(getClass().getResource("ModifyAppointments.fxml"));
                        Scene ModifyAppointmentsScene = new Scene(switchToModifyAppointments);
                        Stage ModifyAppointmentsWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
                        ModifyAppointmentsWindow.setScene(ModifyAppointmentsScene);
                    }
                }
            }

        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            Alert upcomingAppointmentAlert = new Alert(Alert.AlertType.ERROR);
            upcomingAppointmentAlert.setContentText("Please input correct values for all fields.");
            upcomingAppointmentAlert.showAndWait();
        } catch (DateTimeParseException e) {
            System.out.println(e.getMessage());
            Alert appointmentTimeAlert = new Alert(Alert.AlertType.ERROR);
            appointmentTimeAlert.setContentText("Please correctly input the appointment start time in the format HH:MM.");
            appointmentTimeAlert.showAndWait();
        } catch (DateTimeException e) {
            System.out.println(e.getMessage());
            Alert wrongDateAlert = new Alert(Alert.AlertType.ERROR);
            wrongDateAlert.setContentText("Please check that all input date values are valid.");
            wrongDateAlert.showAndWait();
        } catch (SQLException e) {
            Alert invalidCustomerOrUser = new Alert(Alert.AlertType.ERROR);
            invalidCustomerOrUser.setContentText("Invalid Username or Customer Name.");
            invalidCustomerOrUser.showAndWait();
        }
    }
//Get the data from the associated appointment object to populate the TextFields and then potentially be updated. 
    public void setSelectedAppointmentData() {

        appointmentId = ModifyAppointmentsController.getSelectedAppointmentId();
        for (Appointment selectedAppointment : CustomerAppointmentCoordinator.getAllAppointments()) {
            if (selectedAppointment.getAppointmentId() == appointmentId) {
                int startDay = selectedAppointment.getStartDay();
                int startMonth = selectedAppointment.getStartMonth();
                int startYear = selectedAppointment.getStartYear();
                LocalTime startTime = selectedAppointment.getSelectedAppointmentStartTime();
                LocalDateTime selectedAppointmentDateTime = LocalDateTime.of(startYear, startMonth, startDay, startTime.getHour(), startTime.getMinute());

                ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
                ZonedDateTime localZonedDateTime = ZonedDateTime.ofInstant(selectedAppointmentDateTime, ZoneOffset.UTC, localZoneId);
                LocalTime zonedStartTime = LocalTime.of(localZonedDateTime.getHour(), localZonedDateTime.getMinute());

                StartDayTextField.setText(String.valueOf(localZonedDateTime.getDayOfMonth()));
                StartMonthTextField.setText(String.valueOf(MainScreenController.selectedMonthToInt.monthToInt(localZonedDateTime.getMonth().toString())));
                StartYearTextField.setText(String.valueOf(localZonedDateTime.getYear()));
                StartTimeTextField.setText(String.valueOf(zonedStartTime));
                appointmentDurationHoursTextField.setText(String.valueOf(selectedAppointment.getSelectedAppointmentDurationHours()));
                appointmentDurationMinutesTextField.setText(String.valueOf(selectedAppointment.getSelectedAppointmentDurationMinutes()));
                ConsultantComboBox.getSelectionModel().select(selectedAppointment.getConsultantName());
                CustomerNameComboBox.getSelectionModel().select(selectedAppointment.getCustomerName());
                TitleTextField.setText(selectedAppointment.getTitle());
                DescriptionTextField.setText(selectedAppointment.getDescription());
                LocationTextField.setText(selectedAppointment.getLocation());
                ContactTextField.setText(selectedAppointment.getContact());
                AppointmentTypeTextField.setText(selectedAppointment.getType());
                UrlTextField.setText(selectedAppointment.getUrl());

            }
        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       MainScreenController.setPageNum3();
        ArrayList<User> AllUsers = MainScreenController.getAllUsers();
        int numUsers = AllUsers.size();
        for (int i = 0; i < numUsers; i++) {
            ConsultantComboBox.getItems().addAll(AllUsers.get(i).getUserName());
    }
        ConsultantComboBox.getSelectionModel().selectFirst();
        for (int i = 0; i < CustomerAppointmentCoordinator.getAllCustomers().size(); i++) {
            CustomerNameComboBox.getItems().addAll(CustomerAppointmentCoordinator.getAllCustomers().get(i).getName());
        }
        CustomerNameComboBox.getSelectionModel().selectFirst();
        setSelectedAppointmentData();
    }

}
