package model.poi;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Jingwu Xu on 2019-04-04
 */
public class POIList {
    List<POI> pList;

    /**
     * [TODO] Construct a list of POIs according to the city name
     * @param city_name A String indicates the city name where POIs reside
     */
    public POIList(String city_name) {

    }

    /**
     * [TODO] Get a list of POI name for current POI List
     * @return
     */
    public List<String> getPOINameList() {
        return null;
    }

    /**
     * [TODO] Get a list of POI details for current POI List
     * @return
     */
    public List<HashMap<String, String>> getPOIList() {
        return null;
    }

    /**
     * [TODO] Given a list of POI names, return a sublist of POI with exact name match
     * @param POINames A list of desired POI names
     * @return
     */
    public List<POI> getSubListOfPOI(String[] POINames) {
        return null;
    }
}
