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
public class ModifyPartController implements Initializable {

    MainScreenController MC = new MainScreenController();

    @FXML
    private TextField idField;
    @FXML
    private RadioButton inHouseRadioButton;
    @FXML
    private RadioButton outsourcedRadioButton;
    @FXML
    private Label cNameLabel;
    @FXML
    private TextField cNameTextField;
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
    private ToggleGroup identityGroup = new ToggleGroup();

    public void getPartData() {
        if (MainScreenController.getPartIDHolder() != 0) {

            for (Part thisPart : Inventory.getAllParts()) {
                if (thisPart.getId() == MainScreenController.getPartIDHolder()) {
                    if (thisPart instanceof InHouse) {
                        InHouse newPart = (InHouse) thisPart;
                        newPart.getMachineId();
                        this.cNameTextField.setText(String.valueOf(newPart.getMachineId()));
                        this.inHouseRadioButton.setSelected(true);
                    }
                    if (thisPart instanceof Outsourced) {
                        Outsourced newPart = (Outsourced) thisPart;
                        this.cNameTextField.setText(newPart.getCompanyName());
                        this.outsourcedRadioButton.setSelected(true);
                    }
                    idField.setText(String.valueOf(thisPart.getId()));
                    partNameTextField.setText(thisPart.getName());
                    inventoryTextField.setText(String.valueOf(thisPart.getStock()));
                    priceTextField.setText(String.valueOf(thisPart.getPrice()));
                    maxTextField.setText(String.valueOf(thisPart.getMax()));
                    minTextField.setText(String.valueOf(thisPart.getMin()));
                    inHouseOrOutsourceChange();

                }
            }
        }
    }

    public void cancelButtonPushed(ActionEvent event) throws IOException {
        Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
        cancelAlert.setTitle("Warning");
        cancelAlert.setContentText("Your changes will not be saved. Are you sure you want to cancel?");

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
            if (inHouseRadioButton.isSelected()) {
                if (!(Integer.parseInt(this.minTextField.getText()) > Integer.parseInt(this.maxTextField.getText()))) {
                    Part getSelectedPart = Inventory.lookupPart(Integer.parseInt(idField.getText()));
                    int getIndex = Inventory.getAllParts().indexOf(getSelectedPart);

                    InHouse modifiedPart = new InHouse(Integer.parseInt(idField.getText()), partNameTextField.getText(), Double.parseDouble(priceTextField.getText()), 
                            Integer.parseInt(inventoryTextField.getText()), Integer.parseInt(minTextField.getText()), Integer.parseInt(maxTextField.getText()), Integer.parseInt(cNameTextField.getText()));
                    Inventory.getAllParts().remove(getIndex);
                    Inventory.getAllParts().add(getIndex, modifiedPart);

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

            if (outsourcedRadioButton.isSelected()) {
                if (!(Integer.parseInt(this.minTextField.getText()) > Integer.parseInt(this.maxTextField.getText()))) {
                    Part getSelectedPart = Inventory.lookupPart(Integer.parseInt(idField.getText()));
                    int getIndex = Inventory.getAllParts().indexOf(getSelectedPart);

                    Outsourced modifiedPart = new Outsourced(Integer.parseInt(idField.getText()), partNameTextField.getText(), Double.parseDouble(priceTextField.getText()), 
                            Integer.parseInt(inventoryTextField.getText()), Integer.parseInt(minTextField.getText()), Integer.parseInt(maxTextField.getText()), cNameTextField.getText());
                    Inventory.getAllParts().remove(getIndex);
                    Inventory.getAllParts().add(getIndex, modifiedPart);

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
            Alert invalidValueAlert = new Alert(Alert.AlertType.ERROR);
            invalidValueAlert.setTitle("Error");
            invalidValueAlert.setContentText("Please enter valid values for all fields.");
            invalidValueAlert.showAndWait();
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
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        identityGroup = new ToggleGroup();
        this.outsourcedRadioButton.setSelected(true);
        this.inHouseRadioButton.setToggleGroup(identityGroup);
        this.outsourcedRadioButton.setToggleGroup(identityGroup);
        getPartData();
    }

}
