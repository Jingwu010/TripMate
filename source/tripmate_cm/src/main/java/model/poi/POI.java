package model.poi;

import com.fasterxml.jackson.databind.JsonNode;
import model.Location;
import model.mysql.QueryWriter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jingwu Xu on 2019-04-04
 */
public class POI extends Location {
    private Map<String,String> extras = new HashMap<>();
    public static boolean POI_UPDATE_FLAG = true;

    POI(Map<String,String> poi_info) {
        for (Map.Entry<String,String> entry : poi_info.entrySet()) {
            switch (entry.getKey()) {
                case "name":
                    this.name = entry.getValue();
                    break;
                case "lat":
                    this.lat = entry.getValue()!=null ? new BigDecimal(entry.getValue()) : null;
                    break;
                case "lng":
                    this.lng = entry.getValue()!=null ? new BigDecimal(entry.getValue()) : null;
                    break;
                default:
                    if (entry.getValue() != null)
                        this.extras.put(entry.getKey(), entry.getValue());
                    break;
            }
        }

        updatePOI();
    }

    /**
     * Update POI if hasn't been updated for a while
     */
    private void updatePOI() {
        if (!POI_UPDATE_FLAG) return;
        if (lat != null && lng != null) return;

        System.out.println("[POIUpdater] -- Updating " + name + " data ...");

        JsonNode node = POIUpdater.requestEndPoint(name);

        if (node.get("status").toString().equals("ZERO_RESULTS") || node.get("results").size() != 1) {
            System.out.println("[POIUpdater] No result found");
            return;
        }

        JsonNode info = node.get("results").get(0);
        lat = new BigDecimal(info.get("geometry").get("location").get("lat").asText());
        lng = new BigDecimal(info.get("geometry").get("location").get("lng").asText());
        extras.put("last_updated", Long.toString(Instant.now().getEpochSecond()));
        extras.put("formatted_address", info.get("formatted_address").textValue());
        QueryWriter.update_poi(this.toMap());
        return;
    }

    /**
     * Convert all fields to k-v Map
     * @return
     */
    protected Map<String,String> toMap() {
        Map<String,String> info = super.toMap();
        info.putAll(extras);
        return info;
    }
}
