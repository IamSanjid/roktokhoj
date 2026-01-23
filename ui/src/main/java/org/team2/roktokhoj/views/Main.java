package org.team2.roktokhoj.views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Main implements Initializable {
    @FXML
    private TabPane tabView;

    @FXML
    private VBox searchView;

    @FXML
    private VBox profileView;

    @FXML
    private VBox registerView;

    @FXML
    private Search searchViewController;

    @FXML
    private Profile profileViewController;

    @FXML
    private Register registerViewController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert searchViewController.getView() == searchView;
        assert registerViewController.getView() == registerView;
        assert profileViewController.getView() == profileView;
    }

    @FXML
    protected void onTabSelectionChanged() {
    }
}
