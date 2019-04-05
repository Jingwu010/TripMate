package model.poi;

import model.mysql.QueryReader;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Jingwu Xu on 2019-04-04
 */
public class POI {
    String name;
    String formatted_address;
    String wikiurl;
    BigDecimal latitude;
    BigDecimal longtitude;
    int last_updated;

    protected POI(Map<String,String> poi_info) {
        for (Map.Entry<String,String> entry : poi_info.entrySet()) {
            switch (entry.getKey()) {
                case "poi_name":
                    this.name = entry.getValue();
                    break;
                case "poi_wikiurl":
                    this.wikiurl = entry.getValue();
                    break;
                case "poi_lat":
                    this.latitude = entry.getValue()!=null ? new BigDecimal(entry.getValue()) : BigDecimal.valueOf(0);
                    break;
                case "poi_long":
                    this.longtitude = entry.getValue()!=null ? new BigDecimal(entry.getValue()) : BigDecimal.valueOf(0);
                    break;
                case "last_updated":
                    this.last_updated = Integer.parseInt(entry.getValue());
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * [TODO] Update POI if hasn't been updated for a while
     */
    private void updatePOI() {

    }

    public String toString() {
        String format = "poi_name:\t %s\nformatted_address:\t %s\npoi_wikiurl:\t %s\npoi_lat:\t %s\npoi_long:\t %s\nlast_updated:\t %s\n";
        return String.format(format, name, formatted_address, wikiurl, latitude, longtitude, last_updated);
    }

    public static void main(String args[]) {
        Map<String, String> poi_info = QueryReader.get_all_pois_details().get(0);
        POI poi = new POI(poi_info);
        System.out.println(poi);
        Map<String, String> poi_info_2 = QueryReader.get_all_pois_details().get(10);
        POI poi_2 = new POI(poi_info_2);
        System.out.println(poi_2);
    }
}
