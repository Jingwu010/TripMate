package model.poi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.mysql.QueryReader;
import model.settings.Secretes;
import model.settings.SettingReader;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.Map;

/**
 * Created by Jingwu Xu on 2019-04-04
 */
public class POIUpdater {
    final static String END_POINT = "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s";
    final static Secretes secretes = new SettingReader().getSecretes();

    public static JsonNode requestEndPoint(String poi_name) {
        poi_name = poi_name.trim().replace(" ", "+");
        String url = String.format(END_POINT, poi_name, secretes.getGoogle_api_key());

        try {
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("GET");
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(conn.getInputStream());
            System.out.println(node.toString());
            return node;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updatePOI(JsonNode node, POI poi) {
        if (node.get("status").toString().equals("ZERO_RESULTS"))
            return false;
        if (node.get("results").size() != 1)
            return false;
        System.out.println("[POIUpdater] -- Updating " + poi.name + " data ...");
        JsonNode info = node.get("results").get(0);
        System.out.println(info.get("geometry").get("location"));
        System.out.println(info.get("geometry").get("location").get("lat").textValue());
        BigDecimal lat = new BigDecimal(info.get("geometry").get("location").get("lat").asText());
        BigDecimal lng = new BigDecimal(info.get("geometry").get("location").get("lng").asText());
        String formatted_address = info.get("formatted_address").textValue();
        poi.last_updated = (int) Instant.now().getEpochSecond();
        poi.lat =lat;
        poi.lng = lng;
        poi.formatted_address = formatted_address;
        return true;
    }


    public static void main(String[] args) {
        JsonNode node = POIUpdater.requestEndPoint("Rillito River Park");
        Map<String, String> poi_info = QueryReader.get_all_pois_details().get(0);
        POI poi = new POI(poi_info);
        updatePOI(node, poi);
        System.out.println(poi);
    }
}
