package org.team2.roktokhoj.views;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.team2.roktokhoj.ServiceAPI;
import org.team2.roktokhoj.components.AddressSelector;
import org.team2.roktokhoj.models.AddressSelection;
import org.team2.roktokhoj.models.BloodDonor;
import org.team2.roktokhoj.models.BloodGroup;
import org.team2.roktokhoj.models.FindBloodDonor;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Search implements Initializable, ViewController {
    @FXML
    private VBox view;

    @FXML
    private TabPane searchTabPane;

    @FXML
    private Tab searchResultsTab;

    @FXML
    private ComboBox<BloodGroup> searchCbBloodGroup;

    @FXML
    private AddressSelector searchAddressSelector;

    @FXML
    private TableView<BloodDonor> searchResultTable;

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
        searchAddressSelector.reset();
        searchCbBloodGroup.getItems().clear();
        searchCbBloodGroup.getItems().setAll(BloodGroup.values());
        searchResultTable.getItems().clear();
    }

    @FXML
    protected void onSearchResetClicked() {
        reset();
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
        view.setDisable(true);
        request.handle((r, e) -> {
            Platform.runLater(() -> view.setDisable(false));
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
