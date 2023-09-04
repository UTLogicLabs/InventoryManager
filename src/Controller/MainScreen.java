package Controller;

import Common.Alerts;
import Common.Data;
import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;


public class MainScreen {

    Stage stage;
    Parent scene;

    @FXML
    private TextField searchPartsField;

    @FXML
    private TableView<Part> partsTableView;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Integer> partInvCol;

    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    private Button addPartBtn;

    @FXML
    private Button modifyPartBtn;

    @FXML
    private TextField searchProductsField;

    @FXML
    private TableView<Product> productsTableView;

    @FXML
    private TableColumn<Product, Integer> productIdCol;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TableColumn<Product, Integer> productInvCol;

    @FXML
    private TableColumn<Product, Double> productPriceCol;

    @FXML
    private Button modifyProductBtn;

    @FXML
    private Button exitBtn;

    @FXML
    void initialize() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        // Populate Parts Table
        partsTableView.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partPriceCol.setCellFactory(tc -> new TableCell<Part, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });

        //Populate Product Table
        productsTableView.setItems(Inventory.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productPriceCol.setCellFactory(tc -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
    }

    @FXML
    void exitApplication() {
        if (Alerts.confirmation(Alerts.confirmTypes.EXIT)) {
            stage = (Stage) exitBtn.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void deletePart() {
        if (Alerts.confirmation(Alerts.confirmTypes.DELETE) && !Inventory.deletePart(partsTableView.getSelectionModel().getSelectedItem())) {
            Alerts.information("Part Not Found");
        }
    }

    @FXML
    void deleteProduct() {
        if (Alerts.confirmation(Alerts.confirmTypes.DELETE) && !Inventory.deleteProduct(productsTableView.getSelectionModel().getSelectedItem())) {
            Alerts.information("Product Not Found");
        }
    }

    @FXML
    void searchParts() {
        String searchCriteria = searchPartsField.getText();

        if (searchCriteria == null) {
            partsTableView.setItems(Inventory.getAllParts());
        } else if (Data.isNumeric(searchCriteria)) {
            ObservableList<Part> foundPart = FXCollections.observableArrayList();
            foundPart.add(Inventory.lookupPart(Integer.parseInt(searchCriteria)));
            partsTableView.setItems(foundPart);
        } else {
            partsTableView.setItems(Inventory.lookupPart(searchCriteria));
        }
    }

    @FXML
    void searchProducts() {
        String searchCriteria = searchProductsField.getText();

        if (searchCriteria == null) {
            productsTableView.setItems(Inventory.getAllProducts());
        } else if (Data.isNumeric(searchCriteria)) {
            ObservableList<Product> foundProduct = FXCollections.observableArrayList();
            foundProduct.add(Inventory.lookupProduct(Integer.parseInt(searchCriteria)));
            productsTableView.setItems(foundProduct);
        } else {
            productsTableView.setItems(Inventory.lookupProduct(searchCriteria));
        }
    }

    @FXML
    void addPart() throws IOException {
        stage = (Stage) exitBtn.getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/AddPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void modifyPart() throws IOException {
        Part part = partsTableView.getSelectionModel().getSelectedItem();

        if (part == null) {
            Alerts.information("You must select part to modify.");
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/ModifyPart.fxml"));
        loader.load();

        ModifyPart controller = loader.getController();
        controller.setPart(part);

        stage = (Stage) modifyPartBtn.getScene().getWindow();
        scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void addProduct() throws IOException {

        stage = (Stage) addPartBtn.getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    @FXML
    void modifyProduct() throws IOException {
        Product product = productsTableView.getSelectionModel().getSelectedItem();

        if (product == null) {
            Alerts.information("Must select Product to Modify");
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/ModifyProduct.fxml"));
        loader.load();

        ModifyProduct controller = loader.getController();
        controller.setProduct(product);

        stage = (Stage) modifyProductBtn.getScene().getWindow();
        scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }
}