/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software1project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Nathan
 */
public class Software1Project extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        InHouse newPart = new InHouse(IdGenerator.getNextPartID(), "Mouse", 12.0, 11, 1, 10, 12048);
        Outsourced newPart2 = new Outsourced(IdGenerator.getNextPartID(), "Monitor", 98.0, 10, 1, 1, "Asus");
        InHouse newPart3 = new InHouse(IdGenerator.getNextPartID(), "Hard Drive", 41.0, 10, 1, 11, 11211);
        Outsourced newPart4 = new Outsourced(IdGenerator.getNextPartID(), "RAM", 45.0, 10, 1, 10, "G.Skill");
        Outsourced newPart5 = new Outsourced(IdGenerator.getNextPartID(), "Motherboard", 89.0, 10, 1, 10, "Asus");
        Inventory.addPart(newPart);
        Inventory.addPart(newPart2);
        Inventory.addPart(newPart3);
        Inventory.addPart(newPart4);
        Inventory.addPart(newPart5);

        Product newProduct1 = new Product(IdGenerator.getNextProductID(), "Dell Computer", 240.0, 1, 1, 1);
        Product newProduct2 = new Product(IdGenerator.getNextProductID(), "HP Computer", 265.0, 1, 1, 1);
        Product newProduct3 = new Product(IdGenerator.getNextProductID(), "Asus Computer", 312.0, 10, 10, 10);
        Product newProduct4 = new Product(IdGenerator.getNextProductID(), "Acer Computer", 416.0, 10, 10, 10);
        Inventory.addProduct(newProduct1);
        Inventory.addProduct(newProduct2);
        Inventory.addProduct(newProduct3);
        Inventory.addProduct(newProduct4);
        launch(args);

    }
}
