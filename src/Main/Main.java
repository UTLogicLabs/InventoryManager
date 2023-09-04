package Main;

import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
         Inventory inv = new Inventory();
         addTestData(inv);

        Parent root = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Inventory Management");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void addTestData(Inventory inv) {
        Part a = new InHousePart(Inventory.generateUniqueId(), "Part A", 1.99, 6, 1, 10, 1);
        Part b = new InHousePart(Inventory.generateUniqueId(), "Part B", 2.99, 6, 1, 10, 2);
        Part c = new InHousePart(Inventory.generateUniqueId(), "Part C", 3.99, 6, 1, 10, 3);
        Part d = new OutSourced(Inventory.generateUniqueId(), "Part D", 4.99, 6, 1, 10, "Company A");
        Part e = new OutSourced(Inventory.generateUniqueId(), "Part E", 5.99, 6, 1, 10, "Company B");
        Part f = new OutSourced(Inventory.generateUniqueId(), "Part F", 6.99, 6, 1, 10, "Company C");
        inv.addPart(a);
        inv.addPart(b);
        inv.addPart(c);
        inv.addPart(d);
        inv.addPart(e);
        inv.addPart(f);
        inv.addPart(new InHousePart(Inventory.generateUniqueId(), "Part G", 7.99, 6, 1, 10, 4));
        inv.addPart(new InHousePart(Inventory.generateUniqueId(), "Part H", 8.99, 6, 1, 10, 5));
        inv.addPart(new InHousePart(Inventory.generateUniqueId(), "Part I", 9.99, 6, 1, 10, 6));
        inv.addPart(new OutSourced(Inventory.generateUniqueId(), "Part J", 10.99, 6, 1, 10, "Company D"));
        inv.addPart(new OutSourced(Inventory.generateUniqueId(), "Part K", 11.99, 6, 1, 10, "Company E"));
        inv.addPart(new OutSourced(Inventory.generateUniqueId(), "Part L", 12.99, 6, 1, 10, "Company F"));
        Product productA = new Product(Inventory.generateUniqueId(), "Product A", 59.99, 6, 1, 100);
        productA.addAssociatedPart(a);
        productA.addAssociatedPart(b);
        productA.addAssociatedPart(c);
        productA.addAssociatedPart(d);
        Product productB = new Product(Inventory.generateUniqueId(), "Product B", 49.99, 1, 1, 100);
        productB.addAssociatedPart(e);
        productB.addAssociatedPart(f);
        inv.addProduct(productA);
        inv.addProduct(productB);
        inv.addProduct(new Product(Inventory.generateUniqueId(), "Product C", 49.99, 1, 1, 100));
        inv.addProduct(new Product(Inventory.generateUniqueId(), "Product D", 49.99, 1, 1, 100));
    }
}