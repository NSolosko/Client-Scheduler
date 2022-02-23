/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2project;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

/**
 *
 * @author Nathan
 */
public class Appointment {

    private int appointmentId;
    private int customerId;
    private int userId;
    private String userName;
    private String customerName;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Appointment(int customerId, int userId, String title, String description, String location, String contact, String type, String url, LocalDateTime start, LocalDateTime end) {
        this.customerId = customerId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.startTime = start;
        this.endTime = end;
    }

    public int getAppointmentId() {
        return this.appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return this.contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStartDay() {
        int day = this.startTime.getDayOfMonth();
        return day;
    }

    public int getStartMonth() {
        int month = this.startTime.getMonthValue();
        return month;
    }

    public int getStartYear() {
        int year = this.startTime.getYear();
        return year;
    }

    public LocalTime getSelectedAppointmentStartTime() {
        int hour = this.startTime.getHour();
        int minute = this.startTime.getMinute();
        LocalTime startTime = LocalTime.of(hour, minute);
        return startTime;
    }

    public LocalTime getSelectedAppointmentEndTime() {
        int hour = this.endTime.getHour();
        int minute = this.endTime.getMinute();
        LocalTime endTime = LocalTime.of(hour, minute);
        return endTime;
    }

    //returns the duration in hours for an appointment so that it can be updated if needed.
    public long getSelectedAppointmentDurationHours() {
        long hours = ChronoUnit.MINUTES.between(startTime, endTime);

        hours = hours / 60;

        return hours;
    }

    //returns the duration in minutes for an appointment so that it can be updated if needed.
    public long getSelectedAppointmentDurationMinutes() {
        long minutes = ChronoUnit.MINUTES.between(startTime, endTime);

        minutes = minutes % 60;

        return minutes;
    }
    //Converts the start time from UTC time in the database to local time based on that UTC offset. Returns a String object to be used for display in the TableViews. 

    public String getStartTime() {

        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        ZonedDateTime localZonedDateTime = ZonedDateTime.ofInstant(this.startTime, ZoneOffset.UTC, localZoneId);

        LocalDate zonedStartDate = LocalDate.of(localZonedDateTime.getYear(), localZonedDateTime.getMonth(), localZonedDateTime.getDayOfMonth());
        LocalTime zonedStartTime = LocalTime.of(localZonedDateTime.getHour(), localZonedDateTime.getMinute());

        String returnedStartTime = zonedStartDate + " " + zonedStartTime;
        return returnedStartTime;
    }
    //Does the same as above but returns a LocalDateTime to be used so that the local dates and times can be used elsewhere.

    public LocalDateTime getZonedStartTime() {
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        ZonedDateTime localZonedDateTime = ZonedDateTime.ofInstant(this.startTime, ZoneOffset.UTC, localZoneId);
        LocalDate zonedStartDate = LocalDate.of(localZonedDateTime.getYear(), localZonedDateTime.getMonth(), localZonedDateTime.getDayOfMonth());
        LocalTime zonedStartTime = LocalTime.of(localZonedDateTime.getHour(), localZonedDateTime.getMinute());

        LocalDateTime zonedStartDateTime = LocalDateTime.of(zonedStartDate, zonedStartTime);
        return zonedStartDateTime;
    }

    public void setStartTime(LocalDateTime start) {
        this.startTime = start;
    }

    public String getEndTime() {

        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        ZonedDateTime localZonedDateTime = ZonedDateTime.ofInstant(this.endTime, ZoneOffset.UTC, localZoneId);
        LocalDate zonedEndDate = LocalDate.of(localZonedDateTime.getYear(), localZonedDateTime.getMonth(), localZonedDateTime.getDayOfMonth());
        LocalTime zonedEndTime = LocalTime.of(localZonedDateTime.getHour(), localZonedDateTime.getMinute());

        String returnedEndTime = zonedEndDate + " " + zonedEndTime;
        return returnedEndTime;
    }

    public void setEndTime(LocalDateTime end) {
        this.endTime = end;
    }

    //Initializes the customer ArrayList and returns customerName with the ability to match a customer name to the correct customer object.
    public String getName() {
        String name = "";
        if (ModifyRecordsController.getCounter() == 0) {
            ModifyRecordsController.retrieveAllCustomers();
            ModifyRecordsController.incrementCounter();
        }
        for (int i = 0; i < CustomerAppointmentCoordinator.getAllCustomers().size(); i++) {
            if (CustomerAppointmentCoordinator.getAllCustomers().get(i).getCustomerId() == this.customerId) {
                name = CustomerAppointmentCoordinator.getAllCustomers().get(i).getName();
            }
        }
        return name;
    }

    // returns username
    public String getConsultantName() {

        return userName;
    }
}
