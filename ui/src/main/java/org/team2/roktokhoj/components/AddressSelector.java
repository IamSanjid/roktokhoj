package org.team2.roktokhoj.components;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.team2.roktokhoj.MapsAPI;
import org.team2.roktokhoj.Utils;
import org.team2.roktokhoj.models.AddressSelection;
import org.team2.roktokhoj.models.map.IpGeo;
import org.team2.roktokhoj.models.map.Place;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddressSelector extends GridPane {
    @FXML
    private MapView mapView;

    @FXML
    private TextField txtAddress;

    @FXML
    private ChoiceBox<String> cbSearchResults;

    @FXML
    private Slider sbRadius;

    @FXML
    private TextField txtRadius;

    private List<Place> currentPlaces;
    private CustomMapLayer customMapLayer = null;
    private double currentRadius = 5000.;
    private int selectedPlace = -1;
    private int customMaps = 0;

    private static final double DEFAULT_STARTING_ZOOM = 13.;

    public static final IpGeo DEFAULT_IP_GEO = new IpGeo("success", 23.7104, 90.4074);

    public AddressSelector() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "address-selector.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        setupElements();
    }

    private void setupElements() {
        assert mapView != null;
        var lastOnScroll = mapView.getOnScroll();
        assert lastOnScroll != null;
        mapView.setOnScroll(e -> {
            // MapView has a lot of issues, so we just disable zooming when a result is selected.
            if (selectedPlace < 0) {
                lastOnScroll.handle(e);
            }
            //lastOnScroll.handle(e);
            //if (customMapLayer != null) {
            //    System.out.println(mapView.getZoom());
            //    customMapLayer.updateArea(currentRadius, mapView.getZoom());
            //}
        });

        mapView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                var mapPoint = mapView.getMapPosition(e.getX(), e.getY());
                var newPlace = new Place();
                newPlace.setDisplayName("#" + customMaps + ".Custom-Map");
                newPlace.setLat(mapPoint.getLatitude());
                newPlace.setLon(mapPoint.getLongitude());
                customMaps += 1;

                if (selectedPlace < 0) {
                    onNewAddressResult(new ArrayList<>(List.of(newPlace)), 0);
                } else {
                    currentPlaces.add(newPlace);
                    onNewAddressResult(currentPlaces, currentPlaces.size() - 1);
                }
            }
        });

        cbSearchResults.getSelectionModel().selectedIndexProperty().addListener((_, _, newIndex) -> {
            if (selectedPlace < 0 || newIndex.intValue() < 0) {
                return;
            }
            selectedPlace = newIndex.intValue();
            var place = currentPlaces.get(selectedPlace);
            mapView.setCenter(place.getLat(), place.getLon());
            mapView.setZoom(13);

            var markerImg = new Image(Objects.requireNonNull(getClass().getResource("marker-icon.png")).toExternalForm());
            var marker = new ImageView(markerImg);
            marker.setFitHeight(41);
            marker.setFitWidth(25);
            marker.setSmooth(true);
            marker.setPreserveRatio(false);
            customMapLayer.setPlace(currentPlaces.get(selectedPlace), marker, Utils.getScreenCircleRadius(place.getLat(), currentRadius, mapView.getZoom()));

            updateArea();
            txtRadius.setText(String.valueOf(currentRadius));
        });

        sbRadius.valueProperty().addListener((_, _, newRadius) -> {
            if (selectedPlace < 0) {
                return;
            }
            currentRadius = newRadius.doubleValue();
            if (!txtRadius.getText().equals(String.valueOf(currentRadius))) {
                txtRadius.setText(String.valueOf(currentRadius));
            }
        });

        Runnable updateRadiusFromTxt = () -> {
            try {
                currentRadius = Double.parseDouble(txtRadius.getText());
                sbRadius.setValue(currentRadius);
                updateArea();
            } catch (Exception _) {
                // ignore...
            }
        };

        txtRadius.focusedProperty().addListener((_, _, _) -> {
            updateRadiusFromTxt.run();
        });

        txtRadius.setOnAction(_ -> {
            updateRadiusFromTxt.run();
        });

        MapsAPI.getCurrentAddressByIp().handle((r, e) -> {
            if (e == null) {
                Platform.runLater(() -> onAddressByIp(r));
                return r;
            }
            Platform.runLater(() -> onAddressByIp(DEFAULT_IP_GEO));
            return null;
        });
    }

    @FXML
    protected void onAddressSearchClicked() throws Exception {
        var address = txtAddress.getText().strip();
        if (address.isEmpty()) return;
        MapsAPI.searchAddress(address, 10).handle((r, e) -> {
            Platform.runLater(() -> this.setDisable(false));
            if (e == null) {
                Platform.runLater(() -> onNewAddressResult(r, 0));
                return r;
            }
            new Alert(Alert.AlertType.ERROR, "Error finding address: " + e.getMessage(), ButtonType.OK)
                    .showAndWait();
            return null;
        });
        this.setDisable(true);
    }

    @FXML
    protected void onClearClicked() {
        reset();
    }

    private void onAddressByIp(IpGeo ipGeo) {
        if (ipGeo == null || !ipGeo.getStatus().equalsIgnoreCase("success")) {
            ipGeo = DEFAULT_IP_GEO;
        }

        mapView.setCenter(ipGeo.getLat(), ipGeo.getLon());
        mapView.setZoom(DEFAULT_STARTING_ZOOM);
    }

    private void onNewAddressResult(List<Place> places, int defaultSelect) {
        currentPlaces = places;
        if (currentPlaces.isEmpty()) return;
        var cbItems = cbSearchResults.getItems();
        cbItems.clear();
        for (int i = 0; i < currentPlaces.size(); i++) {
            var place = currentPlaces.get(i);
            cbItems.add(i, place.getDisplayName());
        }
        var place = currentPlaces.get(defaultSelect);
        selectedPlace = defaultSelect;

        if (customMapLayer == null) {
            customMapLayer = new CustomMapLayer();
            mapView.addLayer(customMapLayer);
        }

        cbSearchResults.setValue(place.getDisplayName());
        sbRadius.setDisable(false);
        txtRadius.setDisable(false);

        updateArea();
    }

    private void updateArea() {
        if (selectedPlace < 0) return;
        var place = currentPlaces.get(selectedPlace);
        var zoom = DEFAULT_STARTING_ZOOM;
        var mapViewWidth = mapView.getLayoutBounds().getWidth();
        var mapViewHeight = mapView.getLayoutBounds().getHeight();
        double radius = Utils.getScreenCircleRadius(place.getLat(), currentRadius, zoom);
        double radius2 = radius * 2.;
        while (radius2 + 2 > mapViewWidth || radius2 + 2 > mapViewHeight) {
            zoom -= 0.5;
            radius = Utils.getScreenCircleRadius(place.getLat(), currentRadius, zoom);
            radius2 = radius * 2.;
        }
        mapView.setCenter(place.getLat(), place.getLon());
        mapView.setZoom(zoom);
        customMapLayer.updateArea(radius);
    }

    public void reset() {
        if (currentPlaces == null) return;
        currentPlaces.clear();
        customMaps = 0;
        if (customMapLayer != null) {
            mapView.removeLayer(customMapLayer);
            customMapLayer = null;
        }
        currentRadius = 5000.;
        selectedPlace = -1;

        txtRadius.setText(String.valueOf(currentRadius));
        txtRadius.setDisable(true);
        sbRadius.setValue(currentRadius);
        sbRadius.setDisable(true);
        cbSearchResults.getItems().clear();
        //mapView.setZoom(3);

        txtAddress.setText("");
    }

    public AddressSelection getSelection() throws Exception {
        if (selectedPlace < 0) throw new Exception("No address is selected yet!");
        var place = currentPlaces.get(selectedPlace);
        var selection = new AddressSelection();
        selection.setLat(place.getLat());
        selection.setLon(place.getLon());
        selection.setRadius(currentRadius);
        return selection;
    }

    static class CustomMapLayer extends MapLayer {
        Place place;
        ImageView icon;
        Circle area;

        public void setPlace(Place place, ImageView icon, Double radius) {
            if (this.icon != null) {
                this.getChildren().removeAll(this.icon, area);
            }
            this.place = place;
            this.icon = icon;
            area = new Circle(radius, Color.color(76 / 255., 175 / 255., 80 / 255., 0.2));
            area.setStroke(Color.color(101 / 255., 182 / 255., 103 / 255.));
            area.setStrokeWidth(2.);

            this.getChildren().add(this.icon);
            this.getChildren().add(area);
            this.markDirty();
        }

        public void updateArea(Double radius) {
            area.setRadius(radius);
            this.markDirty();
        }

        @Override
        protected void layoutLayer() {
            Point2D mapPoint = getMapPoint(place.getLat(), place.getLon());
            icon.setVisible(true);
            icon.setTranslateX(mapPoint.getX() - (icon.getFitWidth() / 2));
            icon.setTranslateY(mapPoint.getY() - icon.getFitHeight());
            area.setVisible(true);
            area.setTranslateX(mapPoint.getX());
            area.setTranslateY(mapPoint.getY());
        }
    }

}
