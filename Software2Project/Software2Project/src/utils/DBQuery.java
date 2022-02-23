/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import software2project.Appointment;
import software2project.Customer;
import software2project.CustomerAppointmentCoordinator;
import software2project.UpdateCustomerController;
import software2project.User;

/**
 *
 * @author Nathan
 */
public class DBQuery {

    private static ArrayList<User> userList = new ArrayList<User>();
    private static Statement statement;
    private static Connection conn = DBConnection.startConnection();
    private static ArrayList<String> customerNameArrayList = new ArrayList<>();
    private static int exceptionControlCounter = 0;

    public static void setStatement(Connection conn) throws SQLException {

        statement = conn.createStatement();

    }

    //Used for flow 
    public static int getExceptionControlCounter() {
        return exceptionControlCounter;
    }

    public static void resetExceptionControlCounter() {
        exceptionControlCounter = 0;
    }

    public static Statement getStatement() {
        return statement;
    }

    public static Statement prepareQuery() throws SQLException {
        DBQuery.setStatement(conn);
        return statement;
    }

    public static ArrayList<User> getAllUsers() {
        try {
            User newUser;

            String getAllUsers = "Select userId, userName from user";
            prepareQuery().execute(getAllUsers);
            ResultSet getAllUsersResultSet = statement.getResultSet();
            while (getAllUsersResultSet.next() == true) {
                newUser = new User(getAllUsersResultSet.getInt("userId"), getAllUsersResultSet.getString("userName"));
                userList.add(newUser);
            }
            return userList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userList;
    }

    public static void retrieveAllCustomers() {
        try {
            Customer newCustomer;
            int customerId = 0;
            String customerName = "";
            String address = "";
            String address2 = "";
            String postalCode = "";
            String phoneNumber = "";
            String city = "";
            String country = "";
            String getAllCustomers = "select customerId, customerName, address.addressId, address, address2, postalCode, "
                    + "phone, address.cityId, city, country.countryId, country from customer, address, city, country where customer.addressId = "
                    + "address.addressId and address.cityId = city.cityId and city.countryId = country.countryId";
            prepareQuery().execute(getAllCustomers);
            ResultSet getAllCustomersResultSet = statement.getResultSet();
            while (getAllCustomersResultSet.next() == true) {
                customerId = getAllCustomersResultSet.getInt("customerId");
                customerName = getAllCustomersResultSet.getString("customerName");
                phoneNumber = getAllCustomersResultSet.getString("phone");
                address = getAllCustomersResultSet.getString("address");
                address2 = getAllCustomersResultSet.getString("address2");
                postalCode = getAllCustomersResultSet.getString("postalCode");
                city = getAllCustomersResultSet.getString("city");
                country = getAllCustomersResultSet.getString("country");
                newCustomer = new Customer(customerId, customerName, phoneNumber, address, address2, postalCode, city, country);
                CustomerAppointmentCoordinator.addCustomer(newCustomer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void retrieveAllAppointments() {
        try {
            Appointment newAppointment;
            int appointmentId = 0;
            int customerId = 0;
            int userId = 0;
            String title = "";
            String description = "";
            String location = "";
            String contact = "";
            String type = "";
            String url = "";
            String userName = "";
            String customerName = "";
            LocalDate startDate;
            LocalTime startTime;
            LocalDate endDate;
            LocalTime endTime;
            LocalDateTime start;
            LocalDateTime end;
            String getAllAppointments = "select appointmentId, appointment.customerId, appointment.userId, title, description, location, contact, type, url, start, end, "
                    + "customer.customerId, user.userId, customerName, userName from appointment, customer, user where appointment.customerId = customer.customerId and appointment.userId = user.userId";
            prepareQuery().execute(getAllAppointments);
            ResultSet getAllAppointmentsResultSet = statement.getResultSet();
            while (getAllAppointmentsResultSet.next() == true) {
                appointmentId = getAllAppointmentsResultSet.getInt("appointmentId");
                customerId = getAllAppointmentsResultSet.getInt("customerId");
                userId = getAllAppointmentsResultSet.getInt("userId");
                title = getAllAppointmentsResultSet.getString("title");
                description = getAllAppointmentsResultSet.getString("description");
                location = getAllAppointmentsResultSet.getString("location");
                contact = getAllAppointmentsResultSet.getString("contact");
                type = getAllAppointmentsResultSet.getString("type");
                url = getAllAppointmentsResultSet.getString("url");
                userName = getAllAppointmentsResultSet.getString("userName");
                customerName = getAllAppointmentsResultSet.getString("customerName");
                startDate = getAllAppointmentsResultSet.getDate("start").toLocalDate();
                startTime = getAllAppointmentsResultSet.getTime("start").toLocalTime();
                endDate = getAllAppointmentsResultSet.getDate("end").toLocalDate();
                endTime = getAllAppointmentsResultSet.getTime("end").toLocalTime();
                start = LocalDateTime.of(startDate, startTime);
                end = LocalDateTime.of(endDate, endTime);
                newAppointment = new Appointment(customerId, userId, title, description, location, contact, type, url, start, end);
                newAppointment.setUserName(userName);
                newAppointment.setCustomerName(customerName);
                newAppointment.setAppointmentId(appointmentId);
                CustomerAppointmentCoordinator.addAppointment(newAppointment);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int getCountryId(String country) {
        try {
            int countryId = 0;
            String getCountryId = "Select countryId from country Where country = '" + country + "'";
            prepareQuery().execute(getCountryId);
            ResultSet countryIdResultSet = statement.getResultSet();
            while (countryIdResultSet.next() == true) {
                countryId = countryIdResultSet.getInt("countryId");

            }
            return countryId;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public static int getCountryId(int cityId) {
        try {
            int countryId = 0;
            String getCountryId = "Select countryId from city where cityId = " + cityId;
            prepareQuery().execute(getCountryId);
            ResultSet getCountryIdResultSet = statement.getResultSet();
            while (getCountryIdResultSet.next() == true) {
                countryId = getCountryIdResultSet.getInt("countryId");
            }
            return countryId;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public static int getCityId(String city) {
        try {
            int cityId = 0;
            String getCityId = "Select cityId from city Where city = '" + city + "'";
            prepareQuery().execute(getCityId);
            ResultSet cityIdResultSet = statement.getResultSet();
            while (cityIdResultSet.next() == true) {
                cityId = cityIdResultSet.getInt("cityId");
            }
            return cityId;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public static int getCityId(int addressId) {
        try {
            int cityId = 0;
            String getCityId = "Select cityId from address where addressId = " + addressId;
            prepareQuery().execute(getCityId);
            ResultSet getCityIdResultSet = statement.getResultSet();
            while (getCityIdResultSet.next() == true) {
                cityId = getCityIdResultSet.getInt("cityId");
            }
            return cityId;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public static int getAddressId(String Address) {
        try {
            int addressId = 0;
            String getAddressId = "Select addressId from address Where address = '" + Address + "'";
            prepareQuery().execute(getAddressId);
            ResultSet addressIdResultSet = statement.getResultSet();
            while (addressIdResultSet.next() == true) {
                addressId = addressIdResultSet.getInt("addressId");
            }
            return addressId;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public static int getAddressId(int CustomerId) {
        try {
            int addressId = 0;
            String getAddressId = "Select addressId from customer where customerId = " + CustomerId;
            prepareQuery().execute(getAddressId);
            ResultSet getAddressIdResultSet = statement.getResultSet();
            while (getAddressIdResultSet.next() == true) {
                addressId = getAddressIdResultSet.getInt("addressId");
            }
            return addressId;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public static int getCustomerId(String Customer) {
        try {
            int customerId = 0;
            String getCustomerId = "Select customerId from customer Where customerName = '" + Customer + "'";
            prepareQuery().execute(getCustomerId);
            ResultSet customerIdResultSet = statement.getResultSet();
            while (customerIdResultSet.next() == true) {
                customerId = customerIdResultSet.getInt("customerId");
            }
            return customerId;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public static ArrayList<String> getCustomerName() {
        try {

            String getCustomerName = "Select customerName from customer";
            prepareQuery().execute(getCustomerName);

            ResultSet getCustomerNameResultSet = statement.getResultSet();
            while (getCustomerNameResultSet.next() == true) {
                customerNameArrayList.add(getCustomerNameResultSet.getString("customerName"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customerNameArrayList;
    }

    public static String getCustomerName(int customerId) {
        try {
            String customerName = "";
            String getCustomerName = "Select customerName from customer where customerId = " + customerId;
            prepareQuery().execute(getCustomerName);
            ResultSet getCustomerNameResultSet = statement.getResultSet();
            while (getCustomerNameResultSet.next() == true) {
                customerName = getCustomerNameResultSet.getString("customerName");
            }
            return customerName;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public static String getCustomerAddress(int addressId) {
        try {
            String address = "";

            String getCustomerAddress = "Select address from address where addressId = " + addressId;
            prepareQuery().execute(getCustomerAddress);
            ResultSet getCustomerAddressResultSet = statement.getResultSet();
            while (getCustomerAddressResultSet.next() == true) {
                address = getCustomerAddressResultSet.getString("address");
            }
            return address;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "Customer Address Not Found.";
    }

    public static String getCustomerAddress2(int addressId) {
        try {
            String address2 = "";
            String getCustomerAddress2 = "Select address2 from address where addressId = " + addressId;
            prepareQuery().execute(getCustomerAddress2);
            ResultSet getCustomerAddress2ResultSet = statement.getResultSet();
            while (getCustomerAddress2ResultSet.next() == true) {
                address2 = getCustomerAddress2ResultSet.getString("address2");
            }
            return address2;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "Customer Address2 Not Found.";
    }

    public static String getCustomerPhoneNumber(int addressId) {
        String phoneNumber = "";
        try {
            String getCustomerPhoneNumber = "Select phone from address where addressId = " + addressId;
            prepareQuery().execute(getCustomerPhoneNumber);
            ResultSet customerPhoneNumberResultSet = statement.getResultSet();
            while (customerPhoneNumberResultSet.next() == true) {
                phoneNumber = customerPhoneNumberResultSet.getString("phone");
            }
            return phoneNumber;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "Customer Phone Number Not Found.";
    }

    public static String getCustomerPostalCode(int addressId) {
        String postalCode = "";
        try {
            String getCustomerPostalCode = "Select postalCode from address where addressId = " + addressId;
            prepareQuery().execute(getCustomerPostalCode);
            ResultSet customerPostalCodeResultSet = statement.getResultSet();
            while (customerPostalCodeResultSet.next() == true) {
                postalCode = customerPostalCodeResultSet.getString("postalCode");
            }
            return postalCode;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "Customer Postal Code Was Not Found.";
    }

    public static String getCustomerCity(int addressId) {
        try {
            int cityId = getCityId(addressId);
            String city = "";
            String getCustomerCity = "Select city from city where cityId = " + cityId;
            prepareQuery().execute(getCustomerCity);
            ResultSet getCustomerCityResultSet = statement.getResultSet();
            while (getCustomerCityResultSet.next() == true) {
                city = getCustomerCityResultSet.getString("city");
            }
            return city;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "Customer City Could Not Be Found";
    }

    public static String getCustomerCountry(int cityId) {
        try {
            int countryId = getCountryId(cityId);
            String country = "";
            String getCustomerCountry = "Select country from country where countryId = " + countryId;
            prepareQuery().execute(getCustomerCountry);
            ResultSet getCustomerCountryResultSet = statement.getResultSet();
            while (getCustomerCountryResultSet.next() == true) {
                country = getCustomerCountryResultSet.getString("country");
            }
            return country;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "Customer Country Could Not Be Found.";
    }

    public static int getNumberOfCustomers() {
        try {
            int numCustomers = 0;
            String getNumberOfCustomers = "Select customerId from customer";
            prepareQuery().execute(getNumberOfCustomers);
            ResultSet numCustomersResultSet = statement.getResultSet();
            while (numCustomersResultSet.next() == true) {
                numCustomers++;
            }
            return numCustomers;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public static int getUserId(String userName) {
        try {
            int userId = 0;
            String getUserId = "Select userId from user where userName = '" + userName + "'";
            prepareQuery().execute(getUserId);
            ResultSet getUserIdResultSet = statement.getResultSet();
            while (getUserIdResultSet.next() == true) {
                userId = getUserIdResultSet.getInt("userId");
            }
            return userId;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public static int getUserId(int appointmentId) {
        try {
            int userId = 0;
            String getUserId = "Select userId from appointment where appointmentId = " + appointmentId;
            prepareQuery().execute(getUserId);
            ResultSet getUserIdResultSet = statement.getResultSet();
            while (getUserIdResultSet.next() == true) {

                userId = getUserIdResultSet.getInt("userId");
            }
            return userId;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public static String getUsername(int userId) {
        try {
            String username = "";
            String getUsername = "Select userName from user where userId = " + userId;
            prepareQuery().execute(getUsername);
            ResultSet getUsernameResultSet = statement.getResultSet();
            while (getUsernameResultSet.next() == true) {
                username = getUsernameResultSet.getString("userName");
            }
            return username;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public static int getAppointmentId(int customerId, int userId, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        try {
            int appointmentId = 0;
            String getAppointmentId = "Select appointmentId from appointment where customerId = " + customerId + " AND userId = " + userId + " AND start = '" + startDate + " " + startTime
                    + "' AND end = '" + endDate + " " + endTime + "';";

            prepareQuery().execute(getAppointmentId);
            ResultSet getAppointmentIdResultSet = statement.getResultSet();
            while (getAppointmentIdResultSet.next() == true) {
                appointmentId = getAppointmentIdResultSet.getInt("appointmentId");
            }
            return appointmentId;
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return -1;
    }

    public static void addCountry(String Country) {
        try {
            String getCountryName = "Select country from country";
            boolean exists = false;
            prepareQuery().execute(getCountryName);
            ResultSet countryNameResultSet = statement.getResultSet();
            while (countryNameResultSet.next() == true) {
                String countryName = countryNameResultSet.getString("country");
                if (countryName.toLowerCase().equals(Country.toLowerCase())) {
                    System.out.println("Country already exists in the country table.");
                    exists = true;
                }
            }
            if (exists == false) {
                String addCountry = "Insert Into country (country, createDate, createdBy, lastUpdateBy) Values ('" + Country + "', Sysdate(), 'test', 'test')";
                prepareQuery().execute(addCountry);
            }

        } catch (SQLException e) {
            System.out.println("Add country Didn't work.");
        }
    }

    public static void addCity(String City, int countryId) {
        try {

            String getCityName = "Select city from city";
            boolean exists = false;
            prepareQuery().execute(getCityName);
            ResultSet cityNameResultSet = statement.getResultSet();
            while (cityNameResultSet.next() == true) {
                String cityName = cityNameResultSet.getString("city");
                if (cityName.toLowerCase().equals(City.toLowerCase())) {
                    System.out.println("City already exists in the city table.");
                    exists = true;
                }
            }
            if (exists == false) {
                String addCity = "Insert Into city (city, countryId, createDate, createdBy, lastUpdateBy) values ('" + City + "', " + countryId + " , Sysdate(), 'test', 'test')";
                prepareQuery().execute(addCity);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addAddress(String Address, String Address2, int cityId, String postalCode, String PhoneNumber) {
        try {
            String getAddress = "Select address from address";
            boolean exists = false;
            prepareQuery().execute(getAddress);
            ResultSet addressResultSet = statement.getResultSet();

            while (addressResultSet.next() == true) {
                String checkExistingAddresses = addressResultSet.getString("address");
                if (checkExistingAddresses.toLowerCase().equals(Address.toLowerCase())) {
                    exists = true;
                    System.out.println("Address already exists in the address table.");
                }
            }
            if (exists == false) {
                String addAddressPlusPhoneNumber = "Insert Into address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy) values "
                        + "('" + Address + "','" + Address2 + "', " + cityId + ",'" + postalCode + "', '" + PhoneNumber + "', Sysdate(), 'test', 'test')";

                prepareQuery().execute(addAddressPlusPhoneNumber);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addCustomer(String Name, int addressId) throws SQLException {
        String checkExistingCustomer = "Select customerName from customer";
        boolean exists = false;
        prepareQuery().execute(checkExistingCustomer);
        ResultSet checkCustomerResultSet = statement.getResultSet();

        while (checkCustomerResultSet.next() == true) {
            String checkExistingCustomerNames = checkCustomerResultSet.getString("customerName");
            if (checkExistingCustomerNames.toLowerCase().equals(Name.toLowerCase())) {
                exists = true;
                System.out.println("Customer already exists in the customer table.");
            }
        }
        if (exists == false) {
            String addCustomerName = "Insert Into customer (customerName, addressId, active, createDate, createdBy, lastUpdateBy) values ('" + Name + "', " + addressId + ", 1, Sysdate(), 'test', 'test' )";
            prepareQuery().execute(addCustomerName);
        }
    }

    public static void updateCustomer(int idNumber, String name, String phoneNumber, String address, String address2, String postalCode, String city, String country) {
        try {
            int addressId = getAddressId(idNumber);
            int cityId = getCityId(addressId);
            String updateCustomerName = "Update customer set customerName = '" + name + "' Where customerId = " + idNumber;
            prepareQuery().execute(updateCustomerName);
            String updateCustomerPhoneNumber = "Update address set phone = '" + phoneNumber + "' Where addressId = " + addressId;
            prepareQuery().execute(updateCustomerPhoneNumber);
            String updateCustomerAddress = "Update address set address = '" + address + "' Where addressId = " + addressId;
            prepareQuery().execute(updateCustomerAddress);
            String updateCustomerAddress2 = "Update address set address2 = '" + address2 + "' Where addressId = " + addressId;
            prepareQuery().execute(updateCustomerAddress2);
            String updateCustomerPostalCode = "Update address set postalCode = '" + postalCode + "' Where addressId = " + addressId;
            prepareQuery().execute(updateCustomerPostalCode);
            String updateCustomerCity = "Update city set city = '" + city + "' where cityId = " + cityId;
            prepareQuery().execute(updateCustomerCity);
            String updateCustomerCountry = "Update country set country = '" + country + "' where countryId = " + getCountryId(cityId);
            prepareQuery().execute(updateCustomerCountry);
        } catch (SQLException e) {
            UpdateCustomerController.setExceptionControlCounter();
            System.out.println(e.getMessage());
            Alert SQLAlert = new Alert(Alert.AlertType.ERROR);
            SQLAlert.setContentText("Please input valid values for all fields. If no address2 exists for the customer, please enter N/A.");
            SQLAlert.showAndWait();

        }
    }

    public static void deleteCustomer(int idNumber) {
        try {
            int addressId = getAddressId(idNumber);
            String deleteCustomerName = "Delete from customer where customerId = " + idNumber;
            prepareQuery().execute(deleteCustomerName);

            String deleteCustomerAddressesAndPhoneNumber = "Delete from address where addressId = " + addressId;
            prepareQuery().execute(deleteCustomerAddressesAndPhoneNumber);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addAppointment(int customerId, int userId, String title, String description, String location, String contact, String type, String url, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        try {

            String addAppointment = "Insert into appointment (customerId, userId, title, description, location, contact,  type, url,  start, end, createDate, createdBy, lastUpdateBy) values "
                    + "(" + customerId + ", " + userId + ", '" + title + "' , '" + description + "' , '" + location + "' , '" + contact + "' , '"
                    + type + "' , '" + url + "' , '" + startDate + " " + startTime + "' , '" + endDate + " " + endTime + "' , Sysdate(), 'test', 'test') ";

            prepareQuery().execute(addAppointment);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            exceptionControlCounter = 1;

        }
    }

    public static void updateAppointment(int appointmentId, int customerId, int userId, String title, String description, String location, String contact, String type, String url, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        try {
            String updateAppointment = "update appointment set customerId = " + customerId + ", userId = " + userId + ", title = '" + title + "', description = '" + description + "',"
                    + " location = '" + location + "', contact = '" + contact + "', type = '" + type + "', url = '" + url + "', start = '" + startDate + " " + startTime + "', end = '" + endDate + " " + endTime + "' where appointmentId = " + appointmentId;
            prepareQuery().execute(updateAppointment);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteAppointment(int appointmentId) {
        try {
            String deleteAppointment = "delete from appointment where appointmentId = " + appointmentId;
            prepareQuery().execute(deleteAppointment);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean logIn(String inputUsername, String inputPassword) {
        try {
            boolean verified = false;
            String username = "";
            String password = "";

            String logIn = "Select userName, password from user";
            prepareQuery().execute(logIn);
            ResultSet logInResultSet = statement.getResultSet();
            while (logInResultSet.next() == true) {
                username = logInResultSet.getString("userName");
                password = logInResultSet.getString("password");
                if (username.equals(inputUsername) && password.equals(inputPassword)) {
                    verified = true;
                }
            }
            return verified;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
