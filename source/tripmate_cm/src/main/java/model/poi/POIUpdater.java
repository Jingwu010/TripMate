package model.poi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.mysql.QueryReader;
import model.settings.Secretes;
import model.settings.SettingReader;

import java.io.IOException;
import java.math.BigDecimal;
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
//            URL obj = new URL(url);
//            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
//            conn.setRequestMethod("GET");
//
            ObjectMapper mapper = new ObjectMapper();
//
//            JsonNode node = mapper.readTree(conn.getInputStream());
            String data = "{\"results\":[{\"address_components\":[{\"long_name\":\"RillitoRiverPark\",\"short_name\":\"RillitoRiverPark\",\"types\":[\"establishment\",\"park\",\"point_of_interest\"]},{\"long_name\":\"Tucson\",\"short_name\":\"Tucson\",\"types\":[\"locality\",\"political\"]},{\"long_name\":\"PimaCounty\",\"short_name\":\"PimaCounty\",\"types\":[\"administrative_area_level_2\",\"political\"]},{\"long_name\":\"Arizona\",\"short_name\":\"AZ\",\"types\":[\"administrative_area_level_1\",\"political\"]},{\"long_name\":\"UnitedStates\",\"short_name\":\"US\",\"types\":[\"country\",\"political\"]},{\"long_name\":\"85712\",\"short_name\":\"85712\",\"types\":[\"postal_code\"]}],\"formatted_address\":\"RillitoRiverPark,Tucson,AZ85712,USA\",\"geometry\":{\"location\":{\"lat\":32.2745943,\"lng\":-110.9037221},\"location_type\":\"GEOMETRIC_CENTER\",\"viewport\":{\"northeast\":{\"lat\":32.2759432802915,\"lng\":-110.9023731197085},\"southwest\":{\"lat\":32.2732453197085,\"lng\":-110.9050710802915}}},\"place_id\":\"ChIJXyFgyYZz1oYRShGMi2Y4Ju0\",\"plus_code\":{\"compound_code\":\"73FW+RGTucson,Arizona,UnitedStates\",\"global_code\":\"854F73FW+RG\"},\"types\":[\"establishment\",\"park\",\"point_of_interest\"]}],\"status\":\"OK\"}";
            JsonNode node = mapper.readTree(data);
            System.out.println(node.toString());
            return node;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updatePOI(JsonNode node, POI poi) {
        if (node.get("status").toString().equals("ZERO_RESULTS"))
            return;
        if (node.get("results").size() != 1)
            return;
        JsonNode info = node.get("results").get(0);
        System.out.println(info.get("geometry").get("location"));
        System.out.println(info.get("geometry").get("location").get("lat").textValue());
        BigDecimal lat = new BigDecimal(info.get("geometry").get("location").get("lat").asText());
        BigDecimal lng = new BigDecimal(info.get("geometry").get("location").get("lng").asText());
        String formatted_address = info.get("formatted_address").textValue();
        poi.last_updated = (int) Instant.now().getEpochSecond();
        poi.latitude =lat;
        poi.longtitude = lng;
        poi.formatted_address = formatted_address;

    }
    public static void main(String[] args) {
        JsonNode node = POIUpdater.requestEndPoint("Rillito River Park");
        Map<String, String> poi_info = QueryReader.get_all_pois_details().get(0);
        POI poi = new POI(poi_info);
        updatePOI(node, poi);
        System.out.println(poi);
    }
}
