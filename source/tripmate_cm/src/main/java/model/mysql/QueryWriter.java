package model.mysql;

import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Jingwu Xu on 2019-04-04
 */
public class QueryWriter extends MysqlConnector{
    public static void update_poi(Map<String,String> poi_info) {
        String condition = "";
        for (Map.Entry entry : poi_info.entrySet()) {
            if (entry.getKey().equals("id"))
                condition = entry.getKey() + " = " + entry.getValue();
        }
        update_table("poi", poi_info, condition);
    }

    private static void update_table(String table_name, Map<String,String> column_kv, String condition) {
        String kv_pairs = "";
        for (Map.Entry kv : column_kv.entrySet()) {
            if (kv.getKey().toString().equals("id")) continue;
            kv_pairs += kv.getKey() + " = '" + kv.getValue().toString().replace("'", "\"") + "', ";
        }
        kv_pairs = kv_pairs.substring(0, kv_pairs.length() - 2);
        String query = String.format("UPDATE %s SET %s WHERE %s", table_name, kv_pairs, condition);
        System.out.println(query);
        try {
            stmt.execute(query);
        } catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
        }
    }
}
