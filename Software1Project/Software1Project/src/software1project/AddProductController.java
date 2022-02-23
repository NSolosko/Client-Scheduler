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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Nathan
 */
public class AddProductController implements Initializable {

    private Product newProductTemplate = new Product(Integer.MAX_VALUE, "Sample", 2.0, 1, 1, 1);

    @FXML
    TableView<Part> partsTable;
    @FXML
    TableColumn<Part, Integer> partIDColumn;
    @FXML
    TableColumn<Part, String> partNameColumn;
    @FXML
    TableColumn<Part, Integer> partInventoryLevelColumn;
    @FXML
    TableColumn<Part, Integer> partPricePerUnitColumn;

    @FXML
    TableView<Part> associatedPartsTable;
    @FXML
    TableColumn<Part, Integer> associatedPartIDColumn;
    @FXML
    TableColumn<Part, String> associatedPartNameColumn;
    @FXML
    TableColumn<Part, Integer> associatedPartInventoryLevelColumn;
    @FXML
    TableColumn<Part, Integer> associatedPartPricePerUnitColumn;

    @FXML
    private TextField productNameTextField;
    @FXML
    private TextField inventoryTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField maxTextField;
    @FXML
    private TextField minTextField;
    @FXML
    TextField searchPartTextField;

    private static ObservableList<Part> searchPartList = FXCollections.observableArrayList();

    public void partSearch() {
        if (searchPartTextField.getText().trim().isEmpty() == false) {
            searchPartList.clear();
            for (Part searchPart : Inventory.getAllParts()) {
                if (searchPart.getName().toLowerCase().contains(searchPartTextField.getText().trim())) {
                    searchPartList.add(searchPart);
                } else if (String.valueOf(searchPart.getId()).contains(searchPartTextField.getText().trim())) {
                    searchPartList.add(searchPart);
                }
            }
            partsTable.setItems(searchPartList);
        } else {
            partsTable.setItems(Inventory.getAllParts());
        }
    }

    public void addToAssociatedParts() {
        int holder = partsTable.getSelectionModel().getSelectedItem().getId();
        for (Part selectedPart : Inventory.getAllParts()) {
            if (selectedPart.getId() == holder) {
                this.newProductTemplate.addAssociatedPart(selectedPart);
            }
        }
         associatedPartsTable.getSortOrder().add(partIDColumn);
    }

    public void removeFromAssociatedParts() {
        try {
            if (!(associatedPartsTable.getSelectionModel().getSelectedItem().equals(null))) {
                Alert deleteProductAlert = new Alert(Alert.AlertType.CONFIRMATION);
                deleteProductAlert.setTitle("Delete Associated Part?");
                deleteProductAlert.setContentText("Are you sure you want to remove the association between this part and product?");

                Optional<ButtonType> chosenOption = deleteProductAlert.showAndWait();

                if (chosenOption.isPresent() && chosenOption.get() == ButtonType.OK) {
                    this.newProductTemplate.deleteAssociatedPart(associatedPartsTable.getSelectionModel().getSelectedItem());
                }
            }
        } catch (NullPointerException e) {

            Alert modifyPartAlert = new Alert(Alert.AlertType.WARNING);
            modifyPartAlert.setTitle("No Part Selected.");
            modifyPartAlert.setContentText("Please select the associated part for this product which you would like to delete.");
            modifyPartAlert.showAndWait();
        }
    }

    public void addNewProduct(ActionEvent event) throws IOException {
        try {
            if (!(Integer.parseInt(maxTextField.getText()) < Integer.parseInt(minTextField.getText()))) {
                Product newProduct = new Product(IdGenerator.getNextProductID(), productNameTextField.getText(),
                        Double.parseDouble(priceTextField.getText()), Integer.parseInt(inventoryTextField.getText()), Integer.parseInt(minTextField.getText()), Integer.parseInt(maxTextField.getText()));

                for (Part associatedParts : newProductTemplate.getAllAssociatedParts()) {
                    newProduct.addAssociatedPart(associatedParts);
                }

                Inventory.addProduct(newProduct);

                Parent switchToMainScreen = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene MainScreenScene = new Scene(switchToMainScreen);
                Stage MainScreenWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
                MainScreenWindow.setScene(MainScreenScene);
            } else {
                Alert minMaxAlert = new Alert(Alert.AlertType.ERROR);
                minMaxAlert.setTitle("Error");
                minMaxAlert.setContentText("Minimum value cannot be greater than the maximum value for a given product.");
                minMaxAlert.showAndWait();
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter valid values for all fields.");
            Alert invalidValueAlert = new Alert(Alert.AlertType.ERROR);
            invalidValueAlert.setTitle("Error");
            invalidValueAlert.setContentText("Please enter valid values for all fields.");
            invalidValueAlert.showAndWait();
            IdGenerator.handleAddProductError();
        }
    }

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        partsTable.setItems(Inventory.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPricePerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartsTable.setItems(this.newProductTemplate.getAllAssociatedParts());
        associatedPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPricePerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

}
