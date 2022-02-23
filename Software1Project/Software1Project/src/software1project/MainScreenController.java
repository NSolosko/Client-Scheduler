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
 *
 * @author Nathan
 */
public class MainScreenController implements Initializable {

    @FXML
    TextField searchPartTextField;
    @FXML
    TextField searchProductTextField;

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
    TableView<Product> productsTable;
    @FXML
    TableColumn<Product, Integer> productIDColumn;
    @FXML
    TableColumn<Product, String> productNameColumn;
    @FXML
    TableColumn<Product, Integer> productInventoryLevelColumn;
    @FXML
    TableColumn<Product, Integer> productPricePerUnitColumn;

    private static int partIDHolder;
    private static int productIDHolder;

    private static ObservableList<Part> searchPartList = FXCollections.observableArrayList();
    private static ObservableList<Product> searchProductList = FXCollections.observableArrayList();

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

    public void productSearch() {
        if (searchProductTextField.getText().trim().isEmpty() == false) {
            searchProductList.clear();
            for (Product searchProduct : Inventory.getAllProducts()) {
                if (searchProduct.getName().toLowerCase().contains(searchProductTextField.getText().trim())) {
                    searchProductList.add(searchProduct);
                } else if (String.valueOf(searchProduct.getId()).contains(searchProductTextField.getText().trim())) {
                    searchProductList.add(searchProduct);
                }
            }
            productsTable.setItems(searchProductList);
        } else {
            productsTable.setItems(Inventory.getAllProducts());
        }
    }

    public int getSelectedPartIdNum() {
        int idNum = partsTable.getSelectionModel().getSelectedItem().getId();
        partIDHolder = idNum;
        return partIDHolder;
    }

    public static int getPartIDHolder() {
        return partIDHolder;
    }

    public static int getProductIDHolder() {
        return productIDHolder;
    }

    public int getSelectedProductIdNum() {
        int idNum = productsTable.getSelectionModel().getSelectedItem().getId();
        this.productIDHolder = idNum;
        return this.productIDHolder;
    }

    public void deletePartButtonPushed() {
        try {
            if (!(partsTable.getSelectionModel().getSelectedItem().equals(null))) {
                Alert deletePartAlert = new Alert(Alert.AlertType.CONFIRMATION);
                deletePartAlert.setTitle("Delete Part?");
                deletePartAlert.setContentText("Are you sure you want to delete this part?");

                Optional<ButtonType> chosenOption = deletePartAlert.showAndWait();

                if (chosenOption.isPresent() && chosenOption.get() == ButtonType.OK) {
                    Inventory.deletePart(partsTable.getSelectionModel().getSelectedItem());
                    searchPartList.remove(partsTable.getSelectionModel().getSelectedItem());
                }
            }
        } catch (NullPointerException e) {
            Alert nothingSelectedAlert = new Alert(Alert.AlertType.ERROR);
            nothingSelectedAlert.setTitle("Selection Error");
            nothingSelectedAlert.setContentText("Please select a part you would like to delete.");
            nothingSelectedAlert.showAndWait();
        }
    }

    public void deleteProductButtonPushed() {
        try {
            if (!(productsTable.getSelectionModel().getSelectedItem().equals(null))) {
                Alert deleteProductAlert = new Alert(Alert.AlertType.CONFIRMATION);
                deleteProductAlert.setTitle("Delete Product?");
                deleteProductAlert.setContentText("Are you sure you want to delete this product?");

                Optional<ButtonType> chosenOption = deleteProductAlert.showAndWait();

                if (chosenOption.isPresent() && chosenOption.get() == ButtonType.OK) {
                    Inventory.deleteProduct(productsTable.getSelectionModel().getSelectedItem());
                    searchProductList.remove(productsTable.getSelectionModel().getSelectedItem());
                }
            }
        } catch (NullPointerException e) {
            Alert nothingSelectedAlert = new Alert(Alert.AlertType.ERROR);
            nothingSelectedAlert.setTitle("Selection Error");
            nothingSelectedAlert.setContentText("Please select a product you would like to delete.");
            nothingSelectedAlert.showAndWait();
        }
    }

    public void addPartButtonPushed(ActionEvent event) throws IOException {
        Parent switchToAddPart = FXMLLoader.load(getClass().getResource("Add Part.fxml"));
        Scene addPartScene = new Scene(switchToAddPart);
        Stage addPartWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
        addPartWindow.setScene(addPartScene);
    }

    public void modifyPartButtonPushed(ActionEvent event) throws IOException {
        try {
            if (!(partsTable.getSelectionModel().getSelectedItem().equals(null))) {

                Parent switchToModifyPart = FXMLLoader.load(getClass().getResource("ModifyPart.fxml"));
                Scene modifyPartScene = new Scene(switchToModifyPart);
                Stage modifyPartWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
                modifyPartWindow.setScene(modifyPartScene);
            }
        } catch (NullPointerException e) {

            Alert modifyPartAlert = new Alert(Alert.AlertType.WARNING);
            modifyPartAlert.setTitle("No Part Selected.");
            modifyPartAlert.setContentText("Please select a part to modify.");
            modifyPartAlert.showAndWait();
        }
    }

    public void addProductButtonPushed(ActionEvent event) throws IOException {
        Parent switchToAddProduct = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Scene addProductScene = new Scene(switchToAddProduct);
        Stage addProductwindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
        addProductwindow.setScene(addProductScene);
    }

    public void modifyProductButtonPushed(ActionEvent event) throws IOException {
        try {
            if (!(productsTable.getSelectionModel().getSelectedItem().equals(null))) {

                Parent switchToModifyProduct = FXMLLoader.load(getClass().getResource("ModifyProduct.fxml"));
                Scene ModifyProductScene = new Scene(switchToModifyProduct);
                Stage ModifyProductWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
                ModifyProductWindow.setScene(ModifyProductScene);
            }
        } catch (NullPointerException e) {

            Alert modifyProductAlert = new Alert(Alert.AlertType.WARNING);
            modifyProductAlert.setTitle("No Product Selected.");
            modifyProductAlert.setContentText("Please select the product you would like to modify.");
            modifyProductAlert.showAndWait();
        }
    }

    public void exitButtonPushed() {
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.setTitle("Exit?");
        exitAlert.setContentText("Are you sure you want to quit?");

        Optional<ButtonType> chosenOption = exitAlert.showAndWait();

        if (chosenOption.isPresent() && chosenOption.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        productsTable.setItems(Inventory.getAllProducts());
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPricePerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        partsTable.setItems(Inventory.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPricePerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

}
