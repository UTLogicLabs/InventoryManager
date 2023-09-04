package Controller;

import Common.Alerts;
import Common.Data;
import Model.InHousePart;
import Model.Inventory;
import Model.OutSourced;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class AddPart {

    Stage stage;
    Parent scene;

    @FXML // fx:id="partsSource"
    private ToggleGroup partsSource; // Value injected by FXMLLoader

    @FXML // fx:id="partNameTxt"
    private TextField partNameTxt; // Value injected by FXMLLoader

    @FXML // fx:id="partInvTxt"
    private TextField partInvTxt; // Value injected by FXMLLoader

    @FXML // fx:id="partPriceTxt"
    private TextField partPriceTxt; // Value injected by FXMLLoader

    @FXML // fx:id="partInvMaxTxt"
    private TextField partInvMaxTxt; // Value injected by FXMLLoader

    @FXML // fx:id="partInvMinTxt"
    private TextField partInvMinTxt; // Value injected by FXMLLoader

    @FXML // fx:id="partTypeLbl"
    private Label partTypeLbl; // Value injected by FXMLLoader

    @FXML // fx:id="partSourceTxt"
    private TextField partSourceTxt; // Value injected by FXMLLoader

    @FXML // fx:id="savePartBtn"
    private Button savePartBtn; // Value injected by FXMLLoader

    @FXML // fx:id="cancelBtn"
    private Button cancelBtn; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        partTypeLbl.setText("Machine ID");
        partSourceTxt.setPromptText("Machine ID");
    }

    @FXML
    void changePartType() {
        if(((RadioButton) partsSource.getSelectedToggle()).getText().equals("In-House")) {
            partTypeLbl.setText("Machine ID:");
            partSourceTxt.setPromptText("Machine ID");
        }
        if(((RadioButton) partsSource.getSelectedToggle()).getText().equals("Outsourced")) {
            partTypeLbl.setText("Company Name:");
            partSourceTxt.setPromptText("Company Name");
        }
    }

    @FXML
    void cancelNewPart() throws IOException {
        if(Alerts.confirmation(Alerts.confirmTypes.CANCEL)) {
            stage = (Stage) cancelBtn.getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    @FXML
    void savePart() throws IOException {
        String type = ((RadioButton) partsSource.getSelectedToggle()).getText();
        int id = Inventory.generateUniqueId();
        String name = partNameTxt.getText();
        if(Data.isNumeric(name)){
            Alerts.error();
            return;
        }
        try {
            double price = Double.parseDouble(partPriceTxt.getText());
            int min = Integer.parseInt(partInvMinTxt.getText());
            int max = Integer.parseInt(partInvMaxTxt.getText());
            int stock = Integer.parseInt(partInvTxt.getText());

            if(min > stock || max < stock) {
                System.out.println(min);
                System.out.println(max);
                System.out.println(stock);
                Alerts.information("Issue with stock settings.");
                return;
            }
            if (type.equals("In-House")) {
                int machineId = Integer.parseInt(partSourceTxt.getText());

                Inventory.addPart(new InHousePart(id, name, price, stock, min, max, machineId));
            }
            if (type.equals("Outsourced")) {
                String company = partSourceTxt.getText();

                if(Data.isNumeric(company)) {
                    Alerts.error();
                    return;
                }

                Inventory.addPart(new OutSourced(id, name, price, stock, min, max, company));
            }

            stage = (Stage) savePartBtn.getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NumberFormatException err) {
            Alerts.error();
        }
    }
}