/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software1project;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Nathan
 */
public class AddPartController implements Initializable {

    @FXML
    private TextField idField;
    @FXML
    private RadioButton inHouseRadioButton;
    @FXML
    private RadioButton outsourcedRadioButton;
    @FXML
    private TextField partNameTextField;
    @FXML
    private TextField inventoryTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField maxTextField;
    @FXML
    private TextField minTextField;

    @FXML
    private Label cNameLabel;
    @FXML
    private TextField cNameTextField;
    private ToggleGroup identityGroup = new ToggleGroup();

    public void cancelButtonPushed(ActionEvent event) throws IOException {
        Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
        cancelAlert.setTitle("Warning");
        cancelAlert.setContentText("Your data will not be saved. Are you sure you want to cancel?");

        Optional<ButtonType> chosenOption = cancelAlert.showAndWait();

        if (chosenOption.isPresent() && chosenOption.get() == ButtonType.OK) {
            Parent switchToMainScreen = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene MainScreenScene = new Scene(switchToMainScreen);
            Stage MainScreenWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
            MainScreenWindow.setScene(MainScreenScene);
        }

    }

    public void saveButtonPushed(ActionEvent event) throws IOException {
        try {
            if (this.inHouseRadioButton.isSelected()) {
                if (!(Integer.parseInt(this.minTextField.getText()) > Integer.parseInt(this.maxTextField.getText()))) {
                    InHouse savedPart = new InHouse(IdGenerator.getNextPartID(), this.partNameTextField.getText(), Double.parseDouble(this.priceTextField.getText()), Integer.parseInt(this.inventoryTextField.getText()),
                            Integer.parseInt(this.minTextField.getText()), Integer.parseInt(this.maxTextField.getText()), Integer.parseInt(this.cNameTextField.getText()));

                    Inventory.addPart(savedPart);

                    Parent switchToMainScreen = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                    Scene MainScreenScene = new Scene(switchToMainScreen);
                    Stage MainScreenWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
                    MainScreenWindow.setScene(MainScreenScene);
                } else {
                    Alert minMaxAlert = new Alert(Alert.AlertType.ERROR);
                    minMaxAlert.setTitle("Error");
                    minMaxAlert.setContentText("Minimum value cannot be greater than the maximum value for a given part.");
                    minMaxAlert.showAndWait();
                }
            }

            if (this.outsourcedRadioButton.isSelected()) {
                if (!(Integer.parseInt(this.minTextField.getText()) > Integer.parseInt(this.maxTextField.getText()))) {
                    Outsourced savedPart = new Outsourced(IdGenerator.getNextPartID(), this.partNameTextField.getText(), Double.parseDouble(this.priceTextField.getText()), Integer.parseInt(this.inventoryTextField.getText()),
                            Integer.parseInt(this.minTextField.getText()), Integer.parseInt(this.maxTextField.getText()), (this.cNameTextField.getText()));

                    Inventory.addPart(savedPart);

                    Parent switchToMainScreen = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                    Scene MainScreenScene = new Scene(switchToMainScreen);
                    Stage MainScreenWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
                    MainScreenWindow.setScene(MainScreenScene);
                } else {
                    Alert minMaxAlert = new Alert(Alert.AlertType.ERROR);
                    minMaxAlert.setTitle("Error");
                    minMaxAlert.setContentText("Minimum value cannot be greater than the maximum value for a given part.");
                    minMaxAlert.showAndWait();

                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter valid values for all fields.");
            Alert minMaxAlert = new Alert(Alert.AlertType.ERROR);
            minMaxAlert.setTitle("Error");
            minMaxAlert.setContentText("Please enter valid values for all fields.");
            minMaxAlert.showAndWait();
            IdGenerator.handleAddPartError();
        }

    }

    public void inHouseOrOutsourceChange() {
        if (this.inHouseRadioButton.isSelected()) {
            cNameLabel.setText("Machine ID");
            cNameTextField.setPromptText("Mach ID");
        }
        if (this.outsourcedRadioButton.isSelected()) {
            cNameLabel.setText("Company Name");
            cNameTextField.setPromptText("Comp Nm");
        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.idField.setEditable(false);

        this.idField.setMouseTransparent(true);
        this.idField.setFocusTraversable(false);

        identityGroup = new ToggleGroup();
        this.outsourcedRadioButton.setSelected(true);
        this.inHouseRadioButton.setToggleGroup(identityGroup);
        this.outsourcedRadioButton.setToggleGroup(identityGroup);
    }

}
