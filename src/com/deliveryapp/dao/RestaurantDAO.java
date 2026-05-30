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

    // 메뉴 가격 변경
    public void updateMenuPrice(int menuId, int newPrice) throws SQLException {
        String sql = "UPDATE menu SET current_price = ? WHERE menu_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, newPrice);
        pstmt.setInt(2, menuId);
        pstmt.executeUpdate();
    }

    // 메뉴 가격 변경 이력 추가
    public void insertMenuPriceHistory(int menuId, int oldPrice, int newPrice) throws SQLException {
        String sql = "INSERT INTO menu_price_history (menu_id, old_price, new_price, changed_at) " +
                "VALUES (?, ?, ?, NOW())";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, menuId);
        pstmt.setInt(2, oldPrice);
        pstmt.setInt(3, newPrice);
        pstmt.executeUpdate();
    }

    // 메뉴 현재 가격 조회
    public int getCurrentMenuPrice(int menuId) throws SQLException {
        String sql = "SELECT current_price FROM menu WHERE menu_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, menuId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) return rs.getInt("current_price");
        return -1;
    }

    // 가격 변경 전후 매출
    public ResultSet getSalesBeforeAfterPriceChange(int menuId, Timestamp changedAt) throws SQLException {
        String sql = "SELECT " +
                "SUM(CASE WHEN o.order_time < ? THEN oi.quantity * oi.ordered_unit_price ELSE 0 END) AS sales_before, " +
                "SUM(CASE WHEN o.order_time >= ? THEN oi.quantity * oi.ordered_unit_price ELSE 0 END) AS sales_after " +
                "FROM order_item oi " +
                "JOIN orders o ON oi.order_id = o.order_id " +
                "WHERE oi.menu_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setTimestamp(1, changedAt);
        pstmt.setTimestamp(2, changedAt);
        pstmt.setInt(3, menuId);
        return pstmt.executeQuery();
    }

    // 메뉴 정보 조회
    public ResultSet getMenuInfo(int menuId) throws SQLException {
        String sql = "SELECT menu_id, menu_name, current_price FROM menu WHERE menu_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, menuId);
        return pstmt.executeQuery();
    }
}
