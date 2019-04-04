/**
 * Created by Jingwu Xu on 4/3/19.
 */

package mysql;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DBQueryTest {
    @BeforeAll
    public static void setUpClass() {
        DBQuery.change_to_test_database();
    }

    @Test
    void get_all_states() {
        List<String> states = DBQuery.get_all_states();
        assertEquals(51, states.size());
        assertTrue(this.check_string_in_list(states, "Colorado"));
        assertTrue(this.check_string_in_list(states, "Utah"));
        assertTrue(this.check_string_in_list(states, "New Mexico"));
        assertTrue(this.check_string_in_list(states, "Kentucky"));
        assertFalse(this.check_string_in_list(states, "Tulsa"));
        assertFalse(this.check_string_in_list(states, "blah"));
    }

    @Test
    void get_all_cities() {
        List<String> cities = DBQuery.get_all_cities();
        assertEquals(252, cities.size());
        assertTrue(this.check_string_in_list(cities, "Tulsa"));
        assertTrue(this.check_string_in_list(cities, "Racine"));
        assertTrue(this.check_string_in_list(cities, "Boulder"));
        assertTrue(this.check_string_in_list(cities, "Akron"));
        assertFalse(this.check_string_in_list(cities, "blah"));
        assertFalse(this.check_string_in_list(cities, "Utah"));
    }

    @Test
    void get_all_pois_names() {
        List<String> pois = DBQuery.get_all_pois_names();
        assertEquals(4672, pois.size());
        assertTrue(this.check_string_in_list(pois, "Otakon"));
        assertTrue(this.check_string_in_list(pois, "Providence College"));
        assertTrue(this.check_string_in_list(pois, "Inner Harbor"));
        assertTrue(this.check_string_in_list(pois, "Boyle Park"));
        assertFalse(this.check_string_in_list(pois, "Colorado"));
        assertFalse(this.check_string_in_list(pois, "Utah"));
    }

    @Test
    void get_cities_by_state_name() {
        List<String> co_cities = DBQuery.get_cities_by_state_name("Colorado");
        assertEquals(5, co_cities.size());
        assertTrue(this.check_string_in_list(co_cities, "Denver"));
        assertFalse(this.check_string_in_list(co_cities, "Akron"));

        List<String> nm_cities = DBQuery.get_cities_by_state_name("New Mexico");
        assertEquals(3, nm_cities.size());
        assertTrue(this.check_string_in_list(nm_cities, "Santa Fe"));
        assertFalse(this.check_string_in_list(nm_cities, "Akron"));
    }

    @Test
    void get_all_pois_details() {
        List<Map<String, String>> pois = DBQuery.get_all_pois_details();
        assertEquals(4672, pois.size());
    }

    @Test
    void get_pois_details_by_city_name() {
        List<Map<String, String>> de_pois = DBQuery.get_pois_details_by_city_name("Denver");
        assertEquals(41, de_pois.size());

        List<Map<String, String>> sd_pois = DBQuery.get_pois_details_by_city_name("San Diego");
        assertEquals(30, sd_pois.size());

        List<Map<String, String>> na_pois = DBQuery.get_pois_details_by_city_name("Nashville");
        assertEquals(40, na_pois.size());
    }

    private boolean check_string_in_list(List<String> alist, String target) {
        for (String astring : alist) {
            if (astring.equals(target))
                return true;
        }
        return false;
    }
}