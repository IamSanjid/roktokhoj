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

public class Profile implements Initializable, ViewController {

    @FXML
    private VBox view;

    @FXML
    private TabPane profileTabPane;

    @FXML
    private Tab loginTab;

    @FXML
    private TextField loginTxtPhone;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private TextField profileTxtName;

    @FXML
    private TextField profileTxtEmail;

    @FXML
    private TextField profileTxtPhone;

    @FXML
    public Tab profileTab;

    @FXML
    private ComboBox<BloodGroup> profileCbBloodGroup;

    @FXML
    private ComboBox<Availability> profileCbAvailability;

    @FXML
    private PasswordField profilePassword;

    @FXML
    private AddressSelector profileAddressSelector;

    private ProfileBloodDonor currentProfile = null;

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
        loginTxtPhone.setText("");
        loginPassword.setText("");

        profileAddressSelector.reset();
        profileTxtName.setText("");
        profileTxtEmail.setText("");
        profileTxtPhone.setText("");
        profilePassword.setText("");

        profileCbBloodGroup.getItems().clear();
        profileCbBloodGroup.getItems().setAll(BloodGroup.values());
        profileCbAvailability.getItems().clear();
        profileCbAvailability.getItems().setAll(Availability.values());
        profileCbAvailability.getSelectionModel().select(Availability.AVAILABLE);

        loginTab.setDisable(false);
        profileTabPane.getSelectionModel().select(loginTab);
        profileTab.setDisable(true);

        currentProfile = null;
    }

    @FXML
    protected void onProfileUpdateClicked() {
        if (currentProfile == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Needs to login first!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        var name = profileTxtName.getText().strip();
        var email = profileTxtEmail.getText().strip();
        var phone = profileTxtPhone.getText().strip();
        var password = profilePassword.getText().strip();
        var availability = profileCbAvailability.getSelectionModel().getSelectedItem();
        var bloodGroup = profileCbBloodGroup.getSelectionModel().getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || bloodGroup == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "One or more required fields are empty or wrong, please provide the required fields!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        AddressSelection addressSelection;
        try {
            addressSelection = profileAddressSelector.getSelection();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Address selection error: " + ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return;
        }

        var info = currentProfile.getInfo();
        info.setName(name);
        info.setPhone(phone);
        info.setEmail(email);
        info.setAvailability(availability);
        info.setBloodGroup(bloodGroup);
        currentProfile.setInfo(info);
        currentProfile.setAddressSelection(addressSelection);

        var request = ServiceAPI.updateProfile(new NewBloodDonor(currentProfile, password));
        view.setDisable(true);
        profilePassword.setText("");
        request.handle((r, e) -> {
            Platform.runLater(() -> view.setDisable(false));
            if (e == null) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully updated new information!", ButtonType.OK);
                    alert.showAndWait();
                });
                return r;
            }

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error when updating: " + e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            });
            return null;
        });
    }

    @FXML
    protected void onProfileLogoutClicked() {
        reset();
    }

    @FXML
    protected void onLoginClicked() {
        var phone = loginTxtPhone.getText().strip();
        var password = loginPassword.getText().strip();

        if (phone.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "One or more required fields are empty or wrong, please provide the required fields!", ButtonType.OK).showAndWait();
            return;
        }

        var response = ServiceAPI.loginBloodDonor(new LoginBloodDonor(phone, password));
        view.setDisable(true);
        response.handle((r, e) -> {
            Platform.runLater(() -> view.setDisable(false));
            if (e == null) {
                Platform.runLater(() -> onLoggedIn(r));
                return r;
            }
            Platform.runLater(() -> {
                new Alert(Alert.AlertType.ERROR, "Error when logging in: " + e.getMessage(), ButtonType.OK).showAndWait();
            });
            return null;
        });
    }

    private void onLoggedIn(ProfileBloodDonor profile) {
        currentProfile = profile;
        if (currentProfile == null) {
            return;
        }
        profileTabPane.getSelectionModel().select(profileTab);
        loginTab.setDisable(true);
        profileTab.setDisable(false);

        var info = profile.getInfo();
        profileTxtName.setText(info.getName());
        profileTxtPhone.setText(info.getPhone());
        profileTxtEmail.setText(info.getEmail());
        profileCbBloodGroup.getSelectionModel().select(info.getBloodGroup());
        profileCbAvailability.getSelectionModel().select(info.getAvailability());
        profileAddressSelector.reset();

        var addressSelection = profile.getAddressSelection();
        profileAddressSelector.addCustomMapMarker(addressSelection.getLat(), addressSelection.getLon());
        profileAddressSelector.updateRadius(addressSelection.getRadius());
    }

}
