package model.poi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.settings.Secretes;
import model.settings.SettingReader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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
            return node;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
