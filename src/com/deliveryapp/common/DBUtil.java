package com.deliveryapp.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

//    private static final String URL = "jdbc:mysql://localhost:3306/food_delivery";
//    private static final String USER = "root";
//    private static final String PASSWORD = "0000";

    private static String URL = "";
    private static String USER = "";
    private static String PASSWORD = "";

    public static void init(String host, String port, String database, String user, String password) {
        URL = "jdbc:mysql://" + host + ":" + port + "/" + database;
        USER = user;
        PASSWORD = password;
    }

    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("DB 연결 성공!");
            } catch (SQLException e) {
                System.out.println("DB 연결 실패: " + e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("DB 연결 종료");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}