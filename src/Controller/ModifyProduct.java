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

public class ModifyProduct {

    Stage stage;
    Parent scene;

    Product receivedProduct;
    ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    @FXML // fx:id="productIdTxt"
    private TextField productIdTxt; // Value injected by FXMLLoader

    @FXML // fx:id="productNameTxt"
    private TextField productNameTxt; // Value injected by FXMLLoader

    @FXML
    private TextField productPriceTxt;

    @FXML // fx:id="productInvTxt"
    private TextField productInvTxt; // Value injected by FXMLLoader

    @FXML // fx:id="productInvMaxTxt"
    private TextField productInvMaxTxt; // Value injected by FXMLLoader

    @FXML // fx:id="productInvMinTxt"
    private TextField productInvMinTxt; // Value injected by FXMLLoader

    @FXML // fx:id="searchPartsTxt"
    private TextField searchPartsTxt; // Value injected by FXMLLoader

    @FXML // fx:id="unassociatedParts"
    private TableView<Part> unassociatedPartsTable; // Value injected by FXMLLoader

    @FXML // fx:id="unassociatedPartsIdCol"
    private TableColumn<Part, Integer> unassociatedPartsIdCol; // Value injected by FXMLLoader

    @FXML // fx:id="unassociatedPartsNameCol"
    private TableColumn<Part, String> unassociatedPartsNameCol; // Value injected by FXMLLoader

    @FXML // fx:id="unassociatedPartsInvCol"
    private TableColumn<Part, Integer> unassociatedPartsInvCol; // Value injected by FXMLLoader

    @FXML // fx:id="unassociatedPartsPriceCol"
    private TableColumn<Part, Double> unassociatedPartsPriceCol; // Value injected by FXMLLoader

    @FXML // fx:id="associatedPartsTable"
    private TableView<Part> associatedPartsTable; // Value injected by FXMLLoader

    @FXML // fx:id="associatedPartsIdCol"
    private TableColumn<Part, Integer> associatedPartsIdCol; // Value injected by FXMLLoader

    @FXML // fx:id="associatedPartsNameCol"
    private TableColumn<Part, String> associatedPartsNameCol; // Value injected by FXMLLoader

    @FXML // fx:id="associatedPartsInvCol"
    private TableColumn<Part, Integer> associatedPartsInvCol; // Value injected by FXMLLoader

    @FXML // fx:id="associatedPartsPriceCol"
    private TableColumn<Part, Double> associatedPartsPriceCol; // Value injected by FXMLLoader

    @FXML // fx:id="saveProductBtn"
    private Button saveProductBtn; // Value injected by FXMLLoader

    @FXML // fx:id="cancelBtn"
    private Button cancelBtn; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        unassociatedPartsTable.setItems(Inventory.getAllParts());
        unassociatedPartsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        unassociatedPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        unassociatedPartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        unassociatedPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        unassociatedPartsPriceCol.setCellFactory(tc -> new TableCell<Part, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if(empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });

        associatedPartsTable.setItems(associatedParts);
        associatedPartsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedPartsPriceCol.setCellFactory(tc -> new TableCell<Part, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if(empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
    }

    public void setProduct(Product productToModify) {
        receivedProduct = productToModify;
        productIdTxt.setText(String.valueOf(receivedProduct.getId()));
        productNameTxt.setText(receivedProduct.getName());
        productPriceTxt.setText(String.format("%.2f", receivedProduct.getPrice()));
        productInvTxt.setText(String.valueOf(receivedProduct.getStock()));
        productInvMaxTxt.setText(String.valueOf(receivedProduct.getMax()));
        productInvMinTxt.setText(String.valueOf(receivedProduct.getMin()));
        associatedParts.addAll(receivedProduct.getAllAssociatedParts());
    }

    @FXML
    void searchParts() {
        String searchCriteria = searchPartsTxt.getText();

        if(searchCriteria == null) {
            unassociatedPartsTable.setItems(Inventory.getAllParts());
        }
        else if(Data.isNumeric(searchCriteria)) {
            ObservableList<Part> foundPart = FXCollections.observableArrayList();
            foundPart.add(Inventory.lookupPart(Integer.parseInt(searchCriteria)));
            unassociatedPartsTable.setItems(foundPart);
        }
        else {
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
    void deletePartFromProduct() {
        Part part = associatedPartsTable.getSelectionModel().getSelectedItem();
        if(Alerts.confirmation(Alerts.confirmTypes.DELETE)) {
            associatedParts.remove(part);
        }
    }

    @FXML
    void cancelModifyProduct() throws IOException {
        if(Alerts.confirmation(Alerts.confirmTypes.CANCEL)) {
            stage = (Stage) cancelBtn.getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    @FXML
    void saveProduct() throws IOException {
        int id, stock, stockMin, stockMax;
        double price;

        String productName = productNameTxt.getText();

        if(Data.isNumeric(productName)){
            Alerts.error();
            return;
        }

        try {
            id = Integer.parseInt(productIdTxt.getText());
            stock = Integer.parseInt(productInvTxt.getText());
            stockMin = Integer.parseInt(productInvMinTxt.getText());
            stockMax = Integer.parseInt(productInvMaxTxt.getText());
            if(stock < stockMin || stock > stockMax) {
                Alerts.information("Issue with stock settings.");
                return;
            }

            double minProductPrice = 0.0;
            price = Double.parseDouble(productPriceTxt.getText());
            for(Part part : associatedParts) {
                minProductPrice += part.getPrice();
            }
            if(price < minProductPrice) {
                Alerts.information("Price cannot be less than parts.");
                return;
            }
        } catch (NumberFormatException e) {
            Alerts.error();
            return;
        }

        Product updatedProduct = new Product(id, productName, price, stock, stockMin, stockMax);
        for(Part part : associatedParts) {
            updatedProduct.addAssociatedPart(part);
        }

        int index = 0;
        for(Product product: Inventory.getAllProducts()){
            if(product.getId() == id) {
                Inventory.updateProduct(index, updatedProduct);
                break;
            }
            index++;
        }


        stage = (Stage) saveProductBtn.getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }
}
