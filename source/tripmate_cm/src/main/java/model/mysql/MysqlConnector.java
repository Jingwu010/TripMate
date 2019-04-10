package model.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by Jingwu Xu on 2019-04-05
 */
class MysqlConnector {
    private static String REGEX = "jdbc:mysql://%s:%s/%s?user=%s&password=%s&serverTimezone=UTC";
    private static String IP = "127.0.0.1";
    private static String PORT = "3306";
    private static String DATABASE = "tripmate";
    private static String USERNAME = "tripmate";
    private static String PASSWORD = "tripmate";
    static boolean DEBUG = false;
    static Connection conn = null;
    static Statement stmt = connect_mysql();

    /**
     * This method is only called for testing purpose
     */
    public static void change_to_test_database() {
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
}
