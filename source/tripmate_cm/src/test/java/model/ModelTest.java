package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.mysql.QueryReader;
import model.poi.POI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Jingwu Xu on 2019-04-09
 */
class ModelTest {
    Model model;

    @BeforeAll
    static void initAll() {
        QueryReader.change_to_test_database();
        POI.POI_UPDATE_FLAG = false;
    }

    @BeforeEach
    void init() {
        model = new Model();
    }

    @Test
    void updateCity() {
        assertNull(model.pList);
        String city_name = "Seattle";
        model.updateCity(city_name);
        assertNotNull(model.pList);
        List<String> poi_names = model.pList.getPOINameList();
        assertEquals(46, poi_names.size());
        assertTrue(poi_names.contains("Seattle Central Library"));
        assertTrue(poi_names.contains("Blue Moon Tavern"));
    }

    @Test
    void updateTrip() {
        assertNull(model.trip);
        String city_name = "Seattle";
        model.updateCity(city_name);
        String[] pois = {"A Sound Garden", "University of Washington Quad", "Seattle Aquarium", "Benaroya Hall", "Washington State Convention Center", "Beacon Food Forest"};
        model.updateTrip(pois);
        assertNotNull(model.trip);
        assertEquals(6, model.trip.planTrip().size());
    }

    @Test
    void updateTripWithStart() {
        assertNull(model.trip);
        String city_name = "Seattle";
        model.updateCity(city_name);

        ObjectNode loc = new ObjectMapper().createObjectNode();
        loc.put("name", "Space Needle");
        loc.put("lat", "47.62027777");
        loc.put("lng", "-122.34916600");

        String[] pois = {"A Sound Garden", "University of Washington Quad", "Seattle Aquarium", "Benaroya Hall", "Washington State Convention Center", "Beacon Food Forest"};
        model.updateTrip(pois, loc);
        assertNotNull(model.trip);
        List<Location> locations = model.trip.planTrip();
        assertEquals(8, locations.size());
        assertEquals(locations.get(0), locations.get(locations.size()-1));
    }

    @Test
    void updateTripWithStartAndStop() {
        assertNull(model.trip);
        String city_name = "Seattle";
        model.updateCity(city_name);

        ObjectNode loc_1 = new ObjectMapper().createObjectNode();
        loc_1.put("name", "Space Needle");
        loc_1.put("lat", "47.62027777");
        loc_1.put("lng", "-122.34916600");

        ObjectNode loc_2 = new ObjectMapper().createObjectNode();
        loc_2.put("name", "Blue Moon Tavern");
        loc_2.put("lat", "47.66138888");
        loc_2.put("lng", "-122.32000000");

        String[] pois = {"A Sound Garden", "University of Washington Quad", "Seattle Aquarium", "Benaroya Hall", "Washington State Convention Center", "Beacon Food Forest"};
        model.updateTrip(pois, loc_1, loc_2);
        assertNotNull(model.trip);
        List<Location> locations = model.trip.planTrip();
        assertEquals(8, locations.size());
        assertEquals(new Location(loc_1), locations.get(0));
        assertEquals(new Location(loc_2), locations.get(locations.size()-1));
    }

    @Test
    void planTrip() {
        String city_name = "Seattle";
        model.updateCity(city_name);
        String[] pois = {"A Sound Garden", "University of Washington Quad", "Seattle Aquarium", "Benaroya Hall", "Washington State Convention Center", "Beacon Food Forest"};
        model.updateTrip(pois);
        List<JsonNode> itinerary = model.planTrip();
        assertEquals(6, itinerary.size());
        for (JsonNode node : itinerary) {
            assertTrue(Arrays.asList(pois).contains(node.get("name").textValue()));
        }
        String[] sorted_pois = {"A Sound Garden", "University of Washington Quad", "Washington State Convention Center", "Benaroya Hall", "Seattle Aquarium", "Beacon Food Forest"};
        for (int i = 0; i < itinerary.size(); i++) {
            assertTrue(itinerary.get(i).get("name").textValue().equals(sorted_pois[i]));
        }

    }
}