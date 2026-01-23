package org.team2.roktokhoj.views;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.team2.roktokhoj.ServiceAPI;
import org.team2.roktokhoj.components.AddressSelector;
import org.team2.roktokhoj.models.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Register implements Initializable, ViewController {
    @FXML
    private VBox view;

    @FXML
    private TextField registerTxtName;

    @FXML
    private TextField registerTxtEmail;

    @FXML
    private TextField registerTxtPhone;

    @FXML
    private ComboBox<BloodGroup> registerCbBloodGroup;

    @FXML
    private PasswordField registerPassword;

    @FXML
    private ComboBox<Availability> registerCbAvailability;

    @FXML
    private AddressSelector registerAddressSelector;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reset();
    }

    @Override
    public Node getView() {
        return view;
    }

    @Override
    public void reset() {
        registerAddressSelector.reset();
        registerTxtName.setText("");
        registerTxtEmail.setText("");
        registerTxtPhone.setText("");
        registerPassword.setText("");

        registerCbBloodGroup.getItems().clear();
        registerCbBloodGroup.getItems().setAll(BloodGroup.values());
        registerCbAvailability.getItems().clear();
        registerCbAvailability.getItems().setAll(Availability.values());
        registerCbAvailability.getSelectionModel().select(Availability.AVAILABLE);
    }

    @FXML
    protected void onRegisterClicked() throws Exception {
        var name = registerTxtName.getText().strip();
        var email = registerTxtEmail.getText().strip();
        var phone = registerTxtPhone.getText().strip();
        var password = registerPassword.getText().strip();
        var availability = registerCbAvailability.getSelectionModel().getSelectedItem();
        var bloodGroup = registerCbBloodGroup.getSelectionModel().getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || bloodGroup == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "One or more required fields are empty or wrong, please provide the required fields!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        AddressSelection addressSelection;
        try {
            addressSelection = registerAddressSelector.getSelection();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Address selection error: " + ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return;
        }

        var request = ServiceAPI.registerBloodDonor(new NewBloodDonor(new ProfileBloodDonor(
                new BloodDonor(name, phone, email, bloodGroup, availability), addressSelection), password));
        view.setDisable(true);
        request.handle((r, e) -> {
            Platform.runLater(() -> view.setDisable(false));
            if (e == null) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully registered as " + r.getInfo().getName() + "!", ButtonType.OK);
                    alert.showAndWait();
                    reset();
                });
                return r;
            }

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error when registering: " + e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            });
            return null;
        });
    }

    @FXML
    protected void onRegisterViewClearClicked() {
        reset();
    }
}
