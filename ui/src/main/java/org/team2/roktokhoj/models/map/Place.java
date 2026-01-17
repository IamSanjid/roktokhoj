package org.team2.roktokhoj.models.map;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;

class DoubleAsString implements JsonSerializer<Double>, JsonDeserializer<Double> {
    @Override
    public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return Double.parseDouble(json.getAsString());
    }

    @Override
    public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(String.valueOf(src));
    }
}

public class Place {
    @SerializedName("place_id")
    private long placeId;

    private String licence;

    @SerializedName("osm_type")
    private String osmType;

    @SerializedName("osm_id")
    private long osmId;

    @JsonAdapter(DoubleAsString.class)
    private Double lat;
    @JsonAdapter(DoubleAsString.class)
    private Double lon;

    @SerializedName("class")
    private String placeClass;

    private String type;

    @SerializedName("place_rank")
    private int placeRank;

    private double importance;
    private String addresstype;
    private String name;

    @SerializedName("display_name")
    private String displayName;

    private List<String> boundingbox;

    // Getters and Setters
    public long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getOsmType() {
        return osmType;
    }

    public void setOsmType(String osmType) {
        this.osmType = osmType;
    }

    public long getOsmId() {
        return osmId;
    }

    public void setOsmId(long osmId) {
        this.osmId = osmId;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getPlaceClass() {
        return placeClass;
    }

    public void setPlaceClass(String placeClass) {
        this.placeClass = placeClass;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPlaceRank() {
        return placeRank;
    }

    public void setPlaceRank(int placeRank) {
        this.placeRank = placeRank;
    }

    public double getImportance() {
        return importance;
    }

    public void setImportance(double importance) {
        this.importance = importance;
    }

    public String getAddresstype() {
        return addresstype;
    }

    public void setAddresstype(String addresstype) {
        this.addresstype = addresstype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getBoundingbox() {
        return boundingbox;
    }

    public void setBoundingbox(List<String> boundingbox) {
        this.boundingbox = boundingbox;
    }
}
