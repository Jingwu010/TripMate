package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jingwu Xu on 2019-04-11
 */
public class Location {
    public String name;
    public BigDecimal lat;
    public BigDecimal lng;

    public Location(String name, BigDecimal lat, BigDecimal lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public Location(String name, String lat, String lng) {
        this.name = name;
        this.lat = new BigDecimal(lat);
        this.lng = new BigDecimal(lng);
    }

    public Location(JsonNode node) {
        this.name = node.get("name").asText();
        this.lat = new BigDecimal(node.get("lat").asText());
        this.lng = new BigDecimal(node.get("lng").asText());
    }

    public Location() {}

    @Override
    public boolean equals(Object obj){
        if (obj == this) return true;
        if (! (obj instanceof Location)) return false;
        Location loc = (Location) obj;
        return lat.compareTo(loc.lat) == 0 &&
                lng.compareTo(loc.lng) == 0 &&
                name.equals(loc.name);

    }

    protected Map<String,String> toMap() {
        Map<String,String> info = new HashMap<>();
        info.put("name", name);
        info.put("lat", lat.toString());
        info.put("lng", lng.toString());
        return info;
    }

    public JsonNode toJson() {
        return new ObjectMapper().valueToTree(toMap());
    }

    public String toString() {
        return String.format("%-30s", name) + "(" + lat.toString() + ", " + lng.toString() + ")";
    }
}
