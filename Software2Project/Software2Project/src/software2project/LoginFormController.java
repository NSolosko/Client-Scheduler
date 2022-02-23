/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2project;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DBQuery;

/**
 * FXML Controller class
 *
 * @author Nathan
 */
public class LoginFormController implements Initializable {

    //   private ArrayList<Integer> userIdList = new ArrayList<Integer>();²
    @FXML
    private Label welcomeLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Button signInButton;
    ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
    private static int verifiedUserId;
    private String logUserLogin = "src/Logs/userLogs.log";
    private String logUser = "";

    public static int getVerifiedUserId() throws IOException {
        return verifiedUserId;
    }

    /*Method authenticates a user and keeps track of which user is authenticated and granted access to the program. Also appends to the Log file that said user logged in at the specified local day and time. 
      Also contains the code that converts the login form to French (The second language I chose for requirement A is French) if the system is in the French Locale.   
     */
    public void loggedIn(ActionEvent event) throws IOException {
        try {
            boolean verified = false;
            if (Locale.getDefault().getLanguage().equals("fr")) {

                String enteredUsername = usernameTextField.getText();
                String enteredPassword = passwordTextField.getText();
                String translatedUsername = rb.getString(enteredUsername);
                String translatedPassword = rb.getString(enteredPassword);
                verified = DBQuery.logIn(translatedUsername, translatedPassword);
            } else {
                String enteredUsername = usernameTextField.getText();
                String enteredPassword = passwordTextField.getText();
                verified = DBQuery.logIn(enteredUsername, enteredPassword);
            }

            if (verified == true) {

                verifiedUserId = DBQuery.getUserId(usernameTextField.getText());

                int minute;
                int hour = 0;
                int second = 0;
                second = LocalTime.now().getSecond();
                minute = LocalTime.now().getMinute();
                hour = LocalTime.now().getHour();
                FileWriter logFileWriter = new FileWriter(logUserLogin, true);
                logUser = "User " + DBQuery.getUsername(verifiedUserId) + " logged in on " + LocalDate.now() + " at " + LocalTime.of(hour, minute, second);
                logFileWriter.append(logUser);
                logFileWriter.append(String.format("%n"));

                logFileWriter.close();
                Parent switchToMainScreen = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene MainScreenScene = new Scene(switchToMainScreen);
                Stage MainScreenWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
                MainScreenWindow.setScene(MainScreenScene);

            } else {
                Alert loginFailedAlert = new Alert(Alert.AlertType.WARNING);
                loginFailedAlert.setContentText("Incorrect Username or Password.");
                loginFailedAlert.showAndWait();
            }
        } catch (MissingResourceException e) {
            if (Locale.getDefault().getLanguage().equals("fr")) {
                System.out.println("Identifiant ou mot de passe incorrect.");
                Alert loginFailedAlert = new Alert(Alert.AlertType.WARNING);
                loginFailedAlert.setContentText("Identifiant ou mot de passe incorrect.");
                loginFailedAlert.showAndWait();
            } else {
                System.out.println("Incorrect Username or Password.");
            }
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (Locale.getDefault().getLanguage().equals("fr")) {
            welcomeLabel.setText("Bienvenu");
            usernameLabel.setText("Nom d'utilisateur:");
            passwordLabel.setText("Mot de passe:");
            signInButton.setText("S'identifier");
            usernameTextField.setPromptText("Nom d'utilisateur");
            passwordTextField.setPromptText("Mot de passe");
        }

    }
}
