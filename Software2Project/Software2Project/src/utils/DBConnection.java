/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Nathan
 */
public class DBConnection {

    //JDBC URL Parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//3.227.166.251/U06v4g";

    //JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress;

    //Driver Connection and Interface Reference
    private static final String mySQLJDBCDRIVER = "com.mysql.jdbc.Driver";
    private static Connection conn = null;

    private static final String Username = "U06v4g"; //Username
    private static final String Password = "53688881431"; //Password
    private static boolean flag = false;

    //Tracks whether the connection was successful.
    public static boolean getFlag() {
        return flag;
    }

    public static Connection startConnection() {
        try {
            flag = true;
            Class.forName(mySQLJDBCDRIVER);
            conn = (Connection) DriverManager.getConnection(jdbcURL, Username, Password);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void closeConnection() {
        try {
            flag = false;
            conn.close();
            System.out.println("Connection Closed");
        } catch (SQLException e) {
            System.out.println("Connection Closed" + e.getMessage());
        }
    }
}
