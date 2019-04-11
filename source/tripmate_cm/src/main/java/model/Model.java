package model;

import com.fasterxml.jackson.databind.JsonNode;
import model.city.CityList;
import model.poi.POIList;
import model.state.StateList;
import model.trip.Trip;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Jingwu Xu on 2019-04-04
 */
public class Model {
    StateList sList = new StateList();
    CityList cList = new CityList();
    POIList pList;
    Trip trip;

    public Model() {}

    public void updateCity(String city_name) {
        pList = new POIList(city_name);
    }

    public void updateTrip(String[] poi_names) {
        trip = new Trip(pList, poi_names);
    }

    public List<JsonNode> planTrip() {
        return trip.getTrip();
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

    public boolean isCity(String name) {
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
//        String[] pois = {"Great American Beer Festival", "Boettcher Memorial Tropical Conservatory", "Opera Colorado", "Denver Zoo"};
        String[] pois = {"911 Media Arts Center", "Pike Place Market", "Seattle Great Wheel", "Blue Moon Tavern"};
        model.updateTrip(pois);
        List<JsonNode> result = model.planTrip();
        System.out.println("Here is your trip");
        for (JsonNode jn : result) {
            System.out.println(jn.toString());
        }
    }
}
