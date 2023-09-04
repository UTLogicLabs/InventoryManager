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

public class AddProduct {

    Stage stage;
    Parent scene;

    ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    @FXML // fx:id="productNameTxt"
    private TextField productNameTxt; // Value injected by FXMLLoader

    @FXML // fx:id="productInvTxt"
    private TextField productInvTxt; // Value injected by FXMLLoader

    @FXML // fx:id="productPriceTxt"
    private TextField productPriceTxt; // Value injected by FXMLLoader

    @FXML // fx:id="productInvMaxTxt"
    private TextField productInvMaxTxt; // Value injected by FXMLLoader

    @FXML // fx:id="productInvMinTxt"
    private TextField productInvMinTxt; // Value injected by FXMLLoader

    @FXML // fx:id="searchPartTxt"
    private TextField searchPartTxt; // Value injected by FXMLLoader

    @FXML // fx:id="unassociatedPartsTable"
    private TableView<Part> unassociatedPartsTable; // Value injected by FXMLLoader

    @FXML // fx:id="unassociatedPartsIdCol"
    private TableColumn<Part, Integer> unassociatedPartsIdCol; // Value injected by FXMLLoader

    @FXML // fx:id="unassociatedPartsNameCol"
    private TableColumn<Part, String> unassociatedPartsNameCol; // Value injected by FXMLLoader

    @FXML // fx:id="unassociatedPartsInventoryCol"
    private TableColumn<Part, Integer> unassociatedPartsInventoryCol; // Value injected by FXMLLoader

    @FXML // fx:id="unassociatedPartsPriceCol"
    private TableColumn<Part, Double> unassociatedPartsPriceCol; // Value injected by FXMLLoader

    @FXML // fx:id="associatedPartsTable"
    private TableView<Part> associatedPartsTable; // Value injected by FXMLLoader

    @FXML // fx:id="associatedPartIdCol"
    private TableColumn<Part, Integer> associatedPartIdCol; // Value injected by FXMLLoader

    @FXML // fx:id="associatedPartNameCol"
    private TableColumn<Part, String> associatedPartNameCol; // Value injected by FXMLLoader

    @FXML // fx:id="associatedPartInventoryCol"
    private TableColumn<Part, Integer> associatedPartInventoryCol; // Value injected by FXMLLoader

    @FXML // fx:id="associatedPartPriceCol"
    private TableColumn<Part, Double> associatedPartPriceCol; // Value injected by FXMLLoader

    @FXML // fx:id="saveProductBtn"
    private Button saveProductBtn; // Value injected by FXMLLoader

    @FXML // fx:id="cancelBtn"
    private Button cancelBtn; // Value injected by FXMLLoader

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        unassociatedPartsTable.setItems(Inventory.getAllParts());
        unassociatedPartsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        unassociatedPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        unassociatedPartsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        unassociatedPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        unassociatedPartsPriceCol.setCellFactory(tc -> new TableCell<Part, Double>() {
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

        associatedPartsTable.setItems(associatedParts);
        associatedPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedPartPriceCol.setCellFactory(tc -> new TableCell<Part, Double>() {
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
    void searchParts() {
        String searchCriteria = searchPartTxt.getText();

        if(searchCriteria == null) {
            unassociatedPartsTable.setItems(Inventory.getAllParts());
        } else if (Data.isNumeric(searchCriteria)) {
            ObservableList<Part> foundPart = FXCollections.observableArrayList();
            foundPart.add(Inventory.lookupPart(Integer.parseInt(searchCriteria)));
            unassociatedPartsTable.setItems(foundPart);
        } else {
            unassociatedPartsTable.setItems(Inventory.lookupPart(searchCriteria));
        }
    }

    @FXML
    void addPartToProduct() {
       Part part = unassociatedPartsTable.getSelectionModel().getSelectedItem();
       if(part == null)
           Alerts.information("Must select part to add to product");
       if(!associatedParts.contains(part))
            associatedParts.add(part);
    }

    @FXML
    void removePartFromProduct() {
        if(Alerts.confirmation(Alerts.confirmTypes.DELETE)) {
            Part part = associatedPartsTable.getSelectionModel().getSelectedItem();
            associatedParts.remove(part);
        }
    }

    @FXML
    void cancelNewProduct() throws IOException {
        if (Alerts.confirmation(Alerts.confirmTypes.CANCEL)) {
            stage = (Stage) cancelBtn.getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    @FXML
    void saveNewProduct() throws IOException {
        int id = Inventory.generateUniqueId(), stock, stockMin, stockMax;
        double price;


        String name = productNameTxt.getText();
        if (Data.isNumeric(name)) {
            Alerts.error();
            return;
        }

        try {
            stock = Integer.parseInt(productInvTxt.getText());
            stockMin = Integer.parseInt(productInvMinTxt.getText());
            stockMax = Integer.parseInt(productInvMaxTxt.getText());
            if (stock < stockMin || stock > stockMax) {
                Alerts.information("Issue with stock settings.");
                return;
            }

            double minProductPrice = 0.0;
            price = Double.parseDouble(productPriceTxt.getText());
            for (Part part : associatedParts) {
                minProductPrice += part.getPrice();
            }
            if (price < minProductPrice) {
                Alerts.information("Price cannot be less than parts.");
                return;
            }
        } catch (NumberFormatException e) {
            Alerts.error();
            return;
        }

        Product newProduct = new Product(id, name, price, stock, stockMin, stockMax);
        for(Part part: associatedParts) {
            newProduct.addAssociatedPart(part);
        }
        Inventory.addProduct(newProduct);

        stage = (Stage) saveProductBtn.getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}