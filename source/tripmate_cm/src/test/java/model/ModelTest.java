package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Jingwu Xu on 2019-04-09
 */
class ModelTest {
    Model model = new Model();
    @BeforeAll
    public static void setUpClass() {
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void updateCity() {
        assertNull(model.pList);
        String city_name = "Denver";
        model.updateCity(city_name);
        assertNotNull(model.pList);
    }

    @Test
    void updateTrip() {
        assertNull(model.trip);
        String city_name = "Denver";
        model.updateCity(city_name);
        String[] pois = {"Great American Beer Festival", "Boettcher Memorial Tropical Conservatory", "Opera Colorado", "Denver Zoo"};
        model.updateTrip(pois);
        assertNotNull(model.trip);
        assertEquals(5, model.trip.getTrip().size());
    }

    @Test
    void planTrip() {
    }

}