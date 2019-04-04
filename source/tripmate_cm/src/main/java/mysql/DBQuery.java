/**
 * Created by Jingwu Xu on 4/3/19.
 */

package mysql;

import java.sql.*;
import java.util.*;

public class DBQuery {
    static String REGEX = "jdbc:mysql://%s:%s/%s?user=%s&password=%s&serverTimezone=UTC";
    static String IP = "127.0.0.1";
    static String PORT = "3306";
    static String DATABASE = "tripmate";
    static String USERNAME = "tripmate";
    static String PASSWORD = "tripmate";
    static boolean DEBUG = false;
    private static Connection conn = null;
    private static Statement stmt = connect_mysql();

    /**
     * This method is only called for testing purpose
     */
    protected static void change_to_test_database() {
        disconnect_mysql();
        DATABASE = "tripmate_test";
        stmt = connect_mysql();
    }

    /**
     * Establish a connection to mysql database and return a Statement interface
     * @return A Statement interface
     */
    private static Statement connect_mysql() {
        String url = String.format(REGEX, IP, PORT, DATABASE, USERNAME, PASSWORD);
        System.out.println("[SQL CONNECTOR] -- url: " + url);
        System.out.println("[SQL CONNECTOR] -- Connecting to database : " + DATABASE);
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("[SQL CONNECTOR] -- Database connected!");
            Statement stmt = conn.createStatement();
            return stmt;
        } catch (Exception e) {
            System.out.println("[SQL CONNECTOR] -- Error connecting to mysql database!");
            System.out.println(e);
            return null;
        }
    }

    private static void disconnect_mysql() {
        try{
            conn.close();
            stmt.close();
            System.out.println("[SQL CONNECTOR] -- Disconnected from mysql database!");
        } catch (Exception e) {
            System.out.println("[SQL CONNECTOR] -- Error disconnecting from mysql database!");
            System.out.println(e);
        }
    }

    /**
     * Query the 'state' table and retrieve full list of state names
     * @return A list of string indicates state name 
     */
    protected static List<String> get_all_states() {
        return query_field_from_table("state_name", "state", "");
    }

    /**
     * Query the 'state' table and retrieve full list of state names
     * @return A list of string indicates city name 
     */
    protected static List<String> get_all_cities() {
        return query_field_from_table("city_name", "city", "");
    }

    /**
     * Query the 'poi' table and retrieve full list of poi names
     * @return A list of string indicates poi name      
     */
    protected static List<String> get_all_pois_names() {
        return query_field_from_table("poi_name", "poi", "");
    }

    /**
     * Query the 'city' table and retrieve a list of city names within given state
     * @param  state_name A string that restricts the cities within a given state
     * @return            A list of string indicates city name 
     */
    protected static List<String> get_cities_by_state_name(String state_name) {
        int state_id = get_state_id(state_name);
        String constraint = String.format(" WHERE state_id_fk = %d", state_id);
        return query_field_from_table("city_name", "city", constraint);
    }

    /**
     * Query the 'poi' table and retrieve full list of poi details
     * @return A list of pois, each poi has multiple key-value pairs containing detailed information
     */
    protected static List<Map<String, String>> get_all_pois_details() {
        return query_all_fields_from_table("poi", "");
    }

    /**
     * Query the 'poi' table and retrieve a list of poi details
     * @param  city_name A string that, if not empty, restricts the pois for a given city
     * @return           A list of pois, each poi has multiple key-value pairs containing detailed information
     */
    protected static List<Map<String, String>> get_pois_details_by_city_name(String city_name) {
        int city_id = get_city_id(city_name);
        String constraint = String.format(" WHERE city_id_fk = %d", city_id);
        return query_all_fields_from_table("poi", constraint);
    }

    /**
     * Query the database, retrieve all data records for a specific field in a specific table, 
     * other restrictions are allowed to append at the end
     * The query follows the format "SELECT field_name FROM table_name extras"
     * @param  field_name A string indicates the field that query operates on
     * @param  table_name A string indicates the table that query operates on
     * @param  extras     A string contains other specific restrictions on query
     * @return            A list of string that contains records in the field of that table with restrictions
     */
    private static List<String> query_field_from_table(String field_name, String table_name, String extras) {
        String query = String.format("SELECT %s FROM %s %s", field_name, table_name, extras);
        List<String> entries = new LinkedList<String>();
        try {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                entries.add(rs.getString(field_name));
            }
        } catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
        }
        return entries;
    }

    /**
     * Query the database, retrieve all data records for all field in a specific table, 
     * other restrictions are allowed to append at the end
     * The query follows the format "SELECT * FROM table_name extras"
     * @param  table_name A string indicates the table that query operates on
     * @param  extras     A string contains other specific restrictions on query
     * @return            A list of string that contains records in the field of that table with restrictions
     */
    private static List<Map<String, String>> query_all_fields_from_table(String table_name, String extras) {
        String query = String.format("SELECT * FROM %s %s", table_name, extras);
        List<Map<String, String>> entries = new LinkedList<Map<String, String>>();
        try {
            ResultSet rs = stmt.executeQuery(query);
            List<String> fields = new LinkedList<String>();
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                fields.add(rsmd.getColumnName(i));
            }
            while (rs.next()) {
                Map<String, String> dict = new HashMap<String, String>();
                for (String field : fields) {
                    dict.put(field, rs.getString(field));
                }
                entries.add(dict);
            }
        } catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
        }
        return entries;
    }

    /**
     * Return the state_id for a given state_name from database
     * @param  state_name A string describes the state name
     * @return            An integer that indicates the state_id
     * [TODO] handle illegal case
     */
    private static int get_state_id(String state_name) {
        String constraint = String.format(" WHERE state_name = '%s'", state_name);
        return Integer.parseInt(query_field_from_table("state_id", "state", constraint).get(0));
    }

    /**
     * Return the city_id for a given city_name from database
     * @param  city_name A string describes the city name
     * @return           An integer that indicates the state_id
     * [TODO] handle illegal case
     */
    private static int get_city_id(String city_name) {
        String constraint = String.format(" WHERE city_name = '%s'", city_name);
        return Integer.parseInt(query_field_from_table("city_id", "city", constraint).get(0));
    }

    public static void main(String args[]){
        // DBQuery con = new DBQuery();
        List<String> states = DBQuery.get_all_states();
        System.out.println(states.get(0));
        System.out.println(DBQuery.get_state_id("Colorado"));
        List<String> cities = DBQuery.get_all_cities();
        System.out.println(cities.get(10));
        List<String> co_cities = DBQuery.get_cities_by_state_name("Colorado");
        System.out.println(co_cities.size());
        System.out.println(co_cities.get(3));
        System.out.println(DBQuery.get_city_id("Vancouver"));
        List<Map<String, String>> co_pois = DBQuery.get_all_pois_details();
        System.out.println(co_pois.get(1));
    }
}