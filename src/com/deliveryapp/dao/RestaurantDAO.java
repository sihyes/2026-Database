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

    // 메뉴 단가 조회
    public ResultSet getMenuPrice(int menuId) throws SQLException {
        String sql = "SELECT current_price FROM menu WHERE menu_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, menuId);
        return pstmt.executeQuery();
    }

    // 식당 메뉴 검증
    public ResultSet getMenuByIdAndRestaurant(int menuId, int restaurantId) throws SQLException {
        String sql = "SELECT current_price FROM menu " +
                "WHERE menu_id = ? AND restaurant_id = ? AND is_available = TRUE";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, menuId);
        pstmt.setInt(2, restaurantId);
        return pstmt.executeQuery();
    }
}
