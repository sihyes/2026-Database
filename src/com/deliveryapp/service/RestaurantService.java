package com.deliveryapp.service;

import com.deliveryapp.dao.RestaurantDAO;

import java.sql.*;

public class RestaurantService {
    private RestaurantDAO restaurantDAO = new RestaurantDAO();

    public ResultSet getRestaurantList() throws SQLException {
        return restaurantDAO.getRestaurantList();
    }

    public ResultSet getMenuList(int restaurantId) throws SQLException {
        return restaurantDAO.getMenuList(restaurantId);
    }
}
