package model.poi;

import model.mysql.QueryReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Jingwu Xu on 2019-04-04
 */
public class POIList {
    List<POI> pList = new ArrayList<POI>();
    String city_name;

    /**
     * Construct a list of POIs according to the city name
     * @param city_name A String indicates the city name where POIs reside
     */
    public POIList(String city_name) {
        this.city_name = city_name;
        List<Map<String,String>> poi_details = QueryReader.get_pois_details_by_city_name(city_name);
        for (Map<String,String> poi : poi_details) {
            pList.add(new POI(poi));
        }
    }

    /**
     * Get a list of POI name for current POI List
     * @return
     */
    public List<String> getPOINameList() {
        return pList.stream().map(poi -> poi.name).collect(Collectors.toList());
    }

    /**
     * Get a list of POI details for current POI List
     * @return
     */
    public List<Map<String, String>> getPOIList() {
        return pList.stream().map(poi -> poi.toMap()).collect(Collectors.toList());
    }

    /**
     * Given a list of POI names, return a sublist of POI with exact name match
     * @param POINames A list of desired POI names
     * @return
     */
    public List<POI> getSubListOfPOI(String[] poi_names) {
        List<POI> subpList = new ArrayList<POI>();
        for (POI poi : pList) {
            if (Arrays.asList(poi_names).contains(poi.name)) {
                subpList.add(poi);
            }
        }
        return subpList;
    }
}
