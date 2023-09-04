package Controller;

import Common.Alerts;
import Common.Data;
import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class ModifyPart {

    Stage stage;
    Parent scene;

    @FXML
    private Label partTypeLbl;
    @FXML // fx:id="inHouseRadioBtn"
    private RadioButton inHouseRadioBtn; // Value injected by FXMLLoader

    @FXML // fx:id="partsSource"
    private ToggleGroup partsSource; // Value injected by FXMLLoader

    @FXML // fx:id="outsourcedRadioBtn"
    private RadioButton outsourcedRadioBtn; // Value injected by FXMLLoader

    @FXML // fx:id="partIdTxt"
    private TextField partIdTxt; // Value injected by FXMLLoader

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

    @FXML // fx:id="partSourceTxt"
    private TextField partSourceTxt; // Value injected by FXMLLoader

    @FXML // fx:id="savePartBtn"
    private Button savePartBtn; // Value injected by FXMLLoader

    @FXML // fx:id="cancelBtn"
    private Button cancelBtn; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {}

    public void setPart(Part partToModify) {
        if (partToModify instanceof InHousePart) {
            inHouseRadioBtn.setSelected(true);
            partIdTxt.setText(String.valueOf(partToModify.getId()));
            partNameTxt.setText(partToModify.getName());
            partInvTxt.setText(String.valueOf(partToModify.getStock()));
            partPriceTxt.setText(String.format("%.2f", partToModify.getPrice()));
            partInvMaxTxt.setText(String.valueOf(partToModify.getMax()));
            partInvMinTxt.setText(String.valueOf(partToModify.getMin()));
            partTypeLbl.setText("Machine ID:");
            partSourceTxt.setText(String.valueOf(((InHousePart) partToModify).getMachineId()));
        }
        if (partToModify instanceof OutSourced) {
            outsourcedRadioBtn.setSelected(true);
            partIdTxt.setText(String.valueOf(partToModify.getId()));
            partNameTxt.setText(partToModify.getName());
            partInvTxt.setText(String.valueOf(partToModify.getStock()));
            partPriceTxt.setText(String.valueOf(partToModify.getPrice()));
            partInvMaxTxt.setText(String.valueOf(partToModify.getMax()));
            partInvMinTxt.setText(String.valueOf(partToModify.getMin()));
            partTypeLbl.setText("Company Name:");
            partSourceTxt.setText(String.valueOf(((OutSourced) partToModify).getCompanyName()));
        }
    }

    @FXML
    void changePartType() {
        if (((RadioButton) partsSource.getSelectedToggle()).getText().equals("In-House")) {
            partTypeLbl.setText("Machine ID:");
            partSourceTxt.setPromptText("Machine ID");
        }
        if (((RadioButton) partsSource.getSelectedToggle()).getText().equals("Outsourced")) {
            partTypeLbl.setText("Company Name:");
            partSourceTxt.setPromptText("Company Name");
        }
    }

    @FXML
    void cancelModifyPart() throws IOException {
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
        String name = partNameTxt.getText();
        int id, min, max, stock;
        double price;

        if(Data.isNumeric(name)){
            Alerts.error();
            return;
        }
        try {
            id = Integer.parseInt(partIdTxt.getText());
            price = Double.parseDouble(partPriceTxt.getText());
            min = Integer.parseInt(partInvMinTxt.getText());
            max = Integer.parseInt(partInvMaxTxt.getText());
            stock = Integer.parseInt(partInvTxt.getText());

            if(min > stock || max < stock) {
                Alerts.information("Issue with stock settings.");
                return;
            }
            if (type.equals("In-House")) {
                int machineId = Integer.parseInt(partSourceTxt.getText());

                int index = 0;
                for(Part part : Inventory.getAllParts()){
                    if(part.getId() == id) {
                        Inventory.updatePart(index, new InHousePart(id, name, price, stock, min, max, machineId));
                    }
                    index++;
                }
            }
            if (type.equals("Outsourced")) {
                String company = partSourceTxt.getText();

                if(Data.isNumeric(company)) {
                    Alerts.error();
                    return;
                }

                int index = 0;
                for(Part part : Inventory.getAllParts()){
                    if(part.getId() == id) {
                        Inventory.updatePart(index, new OutSourced(id, name, price, stock, min, max, company));
                    }
                    index++;
                }
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
