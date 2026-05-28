package com.deliveryapp.dao;

import com.deliveryapp.common.DBUtil;

import java.sql.*;

public class RestaurantDAO {

    private Connection conn = DBUtil.getConnection();

    // 식당 목록 조회
    public ResultSet getRestaurantList() throws SQLException {
        String sql = "SELECT restaurant_id, restaurant_name, category FROM restaurant";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        return pstmt.executeQuery();
    }

    // 메뉴 목록 조회
    public ResultSet getMenuList(int restaurantId) throws SQLException{
        String sql = "SELECT menu_id, menu_name, current_price FROM menu " +
                "WHERE restaurant_id = ? AND is_available = TRUE";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, restaurantId);
        return pstmt.executeQuery();
    }
}
