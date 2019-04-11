package model.trip;

import com.fasterxml.jackson.databind.JsonNode;
import model.Location;
import model.poi.POI;
import model.poi.POIList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jingwu Xu on 2019-04-04
 */
public class Trip {
    List<POI> pList;
    Location start;
    Location stop;
    int size;
    int[] route;
    TripPlanner tp;

    /**
     * Construct the trip using a list of POI_names
     * @param pList
     * @param poi_names
     */
    public Trip(POIList pList, String[] poi_names) {
        this.pList = pList.getSubListOfPOI(poi_names);
    }

    public Trip(POIList pList, String[] poi_names, Location start) {
        this(pList, poi_names);
        this.start = start;
        if (getLocation(start)==-1) this.pList.add((POI) start);
    }

    public Trip(POIList pList, String[] poi_names, Location start, Location stop) {
        this(pList, poi_names);
        this.start = start;
        this.stop = stop;
        if (getLocation(start)==-1) this.pList.add((POI) start);
        if (getLocation(stop)==-1) this.pList.add((POI) stop);
    }

    int getLocation(Location loc) {
        for (int i = 0; i < size; i++) {
            if (pList.get(i).name.equals(loc.name))
                return i;
        }
        return -1;
    }

    public List<JsonNode> getTrip() {
        this.size = pList.size();
        tp = new NearestNeighbourPlanner(this);
        this.route = tp.planTrip();

        List<JsonNode> tripJson = new ArrayList<>();
        for (int i = 0; i < route.length; i++) {
            tripJson.add(pList.get(route[i]).toJson());
        }
        return tripJson;
    }
}
