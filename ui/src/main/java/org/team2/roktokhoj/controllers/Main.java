package org.team2.roktokhoj.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.team2.roktokhoj.ServiceAPI;
import org.team2.roktokhoj.components.AddressSelector;
import org.team2.roktokhoj.models.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Main implements Initializable {
    @FXML
    private TabPane tabView;

    @FXML
    private Tab searchDonorView;

    @FXML
    private TabPane searchTabPane;

    @FXML
    private Tab searchResultsTab;

    @FXML
    private Tab registerDonorView;

    @FXML
    private ComboBox<BloodGroup> searchCbBloodGroup;

    @FXML
    private AddressSelector searchAddressSelector;

    @FXML
    private TableView<BloodDonor> searchResultTable;

    @FXML
    private TextField registerTxtName;

    @FXML
    private TextField registerTxtEmail;

    @FXML
    private TextField registerTxtPhone;

    @FXML
    public ComboBox<BloodGroup> registerCbBloodGroup;

    @FXML
    public AddressSelector registerAddressSelector;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSearchView();
        initializeRegisterView();
    }

    @FXML
    protected void onTabSelectionChanged() throws Exception {
    }

    @FXML
    protected void onSearchResetClicked() {
        resetSearchView();
    }

    @FXML
    protected void onSearchClicked() {
        var bloodGroup = searchCbBloodGroup.getSelectionModel().getSelectedItem();
        if (bloodGroup == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please choose a blood group.", ButtonType.OK);
            alert.setHeaderText("Error");
            alert.showAndWait();
            return;
        }

        AddressSelection addressSelection;
        try {
            addressSelection = searchAddressSelector.getSelection();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Address selection error: " + ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return;
        }

        var request = ServiceAPI.findBloodDonor(new FindBloodDonor(addressSelection, bloodGroup));
        searchDonorView.setDisable(true);
        request.handle((r, e) -> {
            Platform.runLater(() -> searchDonorView.setDisable(false));
            if (e == null) {
                Platform.runLater(() -> onSearchDonorResult(r));
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
    protected void onRegisterClicked() throws Exception {
        var name = registerTxtName.getText().strip();
        var email = registerTxtEmail.getText().strip();
        var phone = registerTxtPhone.getText().strip();
        var bloodGroup = registerCbBloodGroup.getSelectionModel().getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || bloodGroup == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "One or more required fields are empty or wrong, please provide the required fields!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        AddressSelection addressSelection;
        try {
            addressSelection = registerAddressSelector.getSelection();
        }   catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Address selection error: " + ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return;
        }

        var request = ServiceAPI.registerBloodDonor(new RegisterBloodDonor(new BloodDonor(name, phone, email, bloodGroup),
                addressSelection));
        registerDonorView.setDisable(true);
        request.handle((r, e) -> {
            Platform.runLater(() -> registerDonorView.setDisable(false));
            if (e == null) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully registered as " + r.getName() + "!", ButtonType.OK);
                    alert.showAndWait();
                    resetRegisterView();
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
        resetRegisterView();
    }

    private void initializeSearchView() {
        resetSearchView();
    }

    private void initializeRegisterView() {
        resetRegisterView();
    }

    private void resetSearchView() {
        searchAddressSelector.reset();
        searchCbBloodGroup.getItems().clear();
        searchCbBloodGroup.getItems().setAll(BloodGroup.values());

        searchResultTable.getItems().clear();
        var nameColumn = new TableColumn<BloodDonor, String>("Name");
        var phoneColumn = new TableColumn<BloodDonor, String>("Phone");
        var emailColumn = new TableColumn<BloodDonor, String>("Email");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        @SuppressWarnings("unchecked")
        TableColumn<BloodDonor, ?>[] columns = new TableColumn[]{nameColumn, phoneColumn, emailColumn};
        searchResultTable.getColumns().addAll(columns);
    }

    private void resetRegisterView() {
        registerAddressSelector.reset();
        registerTxtName.setText("");
        registerTxtEmail.setText("");
        registerTxtPhone.setText("");

        registerCbBloodGroup.getItems().clear();
        registerCbBloodGroup.getItems().setAll(BloodGroup.values());
    }

    private void onSearchDonorResult(List<BloodDonor> donorList) {
        System.out.println("Found donors: " + donorList.size());
        searchResultTable.getItems().clear();
        searchResultTable.getItems().addAll(donorList);
        if (!donorList.isEmpty()) {
            searchTabPane.getSelectionModel().select(searchResultsTab);
        } else {
            new Alert(Alert.AlertType.INFORMATION, "No search result was found!").showAndWait();
        }
    }
}
