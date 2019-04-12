package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.city.CityList;
import model.poi.POIList;
import model.state.StateList;
import model.trip.Trip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jingwu Xu on 2019-04-04
 */
public class Model {
    StateList sList = new StateList();
    CityList cList = new CityList();
    POIList pList;
    List<Location> locList = new ArrayList<>();
    Trip trip;

    public Model() {}

    public void updateCity(String city_name) {
        pList = new POIList(city_name);
    }

    public void updateTrip(String[] poi_names) {
        trip = new Trip(getListOfLocation(poi_names));
    }

    public void updateTrip(String[] poi_names, JsonNode start) {
        trip = new Trip(getListOfLocation(poi_names), new Location(start));
    }

    public void updateTrip(String[] poi_names, JsonNode start, JsonNode stop) {
        trip = new Trip(getListOfLocation(poi_names), new Location(start), new Location(stop));
    }

    private List<Location> getListOfLocation(String[] poi_names) {
        locList.addAll(pList.getSubListOfPOI(poi_names));
        return locList;
    }

    public List<JsonNode> planTrip() {
        List<Location> locations = trip.planTrip();
        return locations.stream().map(Location::toJson).collect(Collectors.toList());
    }

    public List<String> getCityList(String state_name) {
        return cList.getCityList(state_name);
    }

    public List<String> getCityList() {
        return cList.getCityList();
    }

    public List<String> getStateList() {
        return sList.getStateList();
    }

    private boolean isCity(String name) {
        for(String str: getCityList()) {
            if(str.trim().equals(name))
               return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Model model = new Model();
        List<String> choices = model.getCityList();
        choices.addAll(model.getStateList());
        System.out.println("Please select your state or city");
        System.out.println(Arrays.toString(choices.toArray()));
        String name = "Washington (state)";
        boolean is_city = model.isCity(name);
        System.out.println("name = " + name + " \t is city? " + is_city);
        if(!is_city) {
            choices = model.getCityList(name);
            System.out.println("Please select your city");
            System.out.println(Arrays.toString(choices.toArray()));
            name = "Seattle";
        }
        model.updateCity(name);
        List<String> poi_choices = model.pList.getPOINameList();
        System.out.println("Please select your POI");
        System.out.println(Arrays.toString(poi_choices.toArray()));
        // String[] pois = {"Great American Beer Festival", "Boettcher Memorial Tropical Conservatory", "Opera Colorado", "Denver Zoo"};
        String[] pois = {"911 Media Arts Center", "Pike Place Market", "Seattle Great Wheel", "Blue Moon Tavern"};

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode loc1 = mapper.createObjectNode();
        loc1.put("name", "Seattle-Tacoma International Airport");
        loc1.put("lat", "47.2495711");
        loc1.put("lng", "-122.7370075");

        ObjectNode loc2 = mapper.createObjectNode();
        loc2.put("name", "University of Washington");
        loc2.put("lat", "47.5945691");
        loc2.put("lng", "-122.297217");

        ObjectNode loc3 = mapper.createObjectNode();
        loc3.put("name", "Blue Moon Tavern");
        loc3.put("lat", "47.66138888");
        loc3.put("lng", "-122.32000000");

        model.updateTrip(pois, loc1, loc3);
        List<JsonNode> result = model.planTrip();
        System.out.println("Here is your trip");
        for (JsonNode jn : result) {
            System.out.println(jn.toString());
        }
    }
}
