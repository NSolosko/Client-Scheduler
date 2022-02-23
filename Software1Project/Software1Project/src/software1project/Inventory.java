/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software1project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Nathan
 */
public class Inventory {

    private static ObservableList<Part> partList = FXCollections.observableArrayList();
    private static ObservableList<Product> productList = FXCollections.observableArrayList();

    public static void addPart(Part newPart) {
        partList.add(newPart);

    }

    public static void addProduct(Product newProduct) {
        productList.add(newProduct);
    }

    public static Part lookupPart(int partId) {
        for (Part value : partList) {
            if (partId == value.getId()) {
                return value;
            }
        }
        return null;
    }

    public static Product lookupProduct(int productId) {
        for (Product value : productList) {
            if (productId == value.getId()) {
                return value;
            }
        }
        return null;
    }

    public static ObservableList<Part> lookupPart(String Name) {
        for (Part value : partList) {
            if (value.getName().equals(Name)) {
                ObservableList<Part> specificPart = FXCollections.observableArrayList();
                specificPart.add(value);
                return specificPart;
            }
        }
        return null;
    }

    public static ObservableList<Product> lookupProduct(String productName) {
        for (Product value : productList) {
            if (value.getName().equals(productName)) {
                ObservableList<Product> specificProduct = FXCollections.observableArrayList();
                specificProduct.add(value);
                return specificProduct;
            }
        }
        return null;
    }

    public static void updatePart(int index, Part selectedPart) {
        for (Part value : partList) {
            if (value == selectedPart) {
                value.setId(selectedPart.getId());
                value.setName(selectedPart.getName());
                value.setPrice(selectedPart.getPrice());
                value.setStock(selectedPart.getStock());
                value.setMin(selectedPart.getMin());
                value.setMax(selectedPart.getMax());
            }
        }
    }

    public static void updateProduct(int index, Product newProduct) {
        for (Product value : productList) {
            if (value == newProduct) {
                value.setId(newProduct.getId());
                value.setName(newProduct.getName());
                value.setPrice(newProduct.getPrice());
                value.setStock(newProduct.getStock());
                value.setMin(newProduct.getMin());
                value.setMax(newProduct.getMax());
            }
        }
    }

    public static boolean deletePart(Part selectedPart) {
        for (Part value : partList) {
            if (value == selectedPart) {
                partList.remove(value);
                return true;
            }
        }
        return false;
    }

    public static boolean deleteProduct(Product selectedProduct) {
        for (Product value : productList) {
            if (value == selectedProduct) {
                productList.remove(value);
                return true;
            }
        }
        return false;
    }

    public static ObservableList<Part> getAllParts() {
        return partList;

    }

    public static ObservableList<Product> getAllProducts() {
        return productList;
    }
}
