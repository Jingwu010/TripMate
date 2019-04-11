package model.city;

import model.mysql.QueryReader;

import java.util.List;

/**
 * Created by Jingwu Xu on 2019-04-04
 */
public class CityList {
    /**
     * Query the database and return full list of city names
     * @return A list of String indicates city name
     */
    public List<String> getCityList(){
        return QueryReader.get_all_cities();
    }

    /**
     * Query the database and return a list of city names within given state
     * @return A list of String indicates city name
     */
    public List<String> getCityList(String state_name){
        return QueryReader.get_cities_by_state_name(state_name);
    }
}
