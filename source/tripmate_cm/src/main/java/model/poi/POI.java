package model.poi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.mysql.QueryReader;
import model.mysql.QueryWriter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jingwu Xu on 2019-04-04
 */
public class POI {
    String id;
    public String name;
    String formatted_address;
    String wiki_url;
    public BigDecimal lat;
    public BigDecimal lng;
    int last_updated;
//    final static int UPDATE_FREQUENCY = 86400; // 1 days
    final static int UPDATE_FREQUENCY = 3600; // 1 h

    POI(Map<String,String> poi_info) {
        for (Map.Entry<String,String> entry : poi_info.entrySet()) {
            switch (entry.getKey()) {
                case "id":
                    this.id = entry.getValue();
                    break;
                case "name":
                    this.name = entry.getValue();
                    break;
                case "wiki_url":
                    this.wiki_url = entry.getValue();
                    break;
                case "lat":
                    this.lat = entry.getValue()!=null ? new BigDecimal(entry.getValue()) : BigDecimal.valueOf(0);
                    break;
                case "lng":
                    this.lng = entry.getValue()!=null ? new BigDecimal(entry.getValue()) : BigDecimal.valueOf(0);
                    break;
                case "last_updated":
                    this.last_updated = Integer.parseInt(entry.getValue());
                    break;
                default:
                    break;
            }
        }

        updatePOI();
    }

    /**
     * Update POI if hasn't been updated for a while
     */
    private void updatePOI() {
        if ((int) Instant.now().getEpochSecond() - last_updated > UPDATE_FREQUENCY) {
            if (POIUpdater.updatePOI(POIUpdater.requestEndPoint(name), this))
                QueryWriter.update_poi(this.toMap());
        }
    }

    /**
     * Convert all fields to k-v Map
     * @return
     */
    public Map<String,String> toMap() {
        Map<String,String> info = new HashMap<String, String>();
        info.put("id", id);
        info.put("name", name);
        info.put("formatted_address", formatted_address);
        info.put("wiki_url", wiki_url);
        info.put("lat", lat.toString());
        info.put("lng", lng.toString());
        info.put("last_updated", Integer.toString(last_updated));
        return info;
    }

    /**
     * Convert all fields to Json format
     * @return JsonNode
     */
    public JsonNode toJson() {
        return new ObjectMapper().valueToTree(toMap());
    }

    public String toString() {
        String format = "poi_id:\t %s\npoi_name:\t %s\nformatted_address:\t %s\npoi_wikiurl:\t %s\npoi_lat:\t %s\npoi_long:\t %s\nlast_updated:\t %s\n";
        return String.format(format, id, name, formatted_address, wiki_url, lat, lng, last_updated);
    }

    public static void main(String args[]) {
        JsonNode node = POIUpdater.requestEndPoint("Rillito River Park");
        Map<String, String> poi_info = QueryReader.get_all_pois_details().get(0);
        POI poi = new POI(poi_info);
    }
}
