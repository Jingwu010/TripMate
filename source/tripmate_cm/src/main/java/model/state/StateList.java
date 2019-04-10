package model.state;

import model.mysql.QueryReader;

import java.util.List;

/**
 * Created by Jingwu Xu on 2019-04-04
 */
public class StateList {
    /**
     * [TODO] Query the database and return full list of state names
     * @return A list of String indicates state name
     */
    public List<String> getStateList(){
        return QueryReader.get_all_states();
    }
}
