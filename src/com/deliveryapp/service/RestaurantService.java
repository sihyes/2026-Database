package com.deliveryapp.service;

import com.deliveryapp.common.DBUtil;
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

    public ResultSet getMenuPrice(int menuId) throws SQLException {
        return restaurantDAO.getMenuPrice(menuId);
    }

    public ResultSet getMenuByIdAndRestaurant(int menuId, int restaurantId) throws SQLException {
        return restaurantDAO.getMenuByIdAndRestaurant(menuId, restaurantId);
    }

    public int getCurrentMenuPrice(int menuId) throws SQLException {
        return restaurantDAO.getCurrentMenuPrice(menuId);
    }

    public void updateMenuPrice(int menuId, int oldPrice, int newPrice) {
        try {
            Connection conn = DBUtil.getConnection();
            conn.setAutoCommit(false);
            restaurantDAO.insertMenuPriceHistory(menuId, oldPrice, newPrice);
            restaurantDAO.updateMenuPrice(menuId, newPrice);
            conn.commit();
            System.out.println("메뉴 가격 변경 완료");
        } catch (SQLException e) {
            try { DBUtil.getConnection().rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.out.println("가격 변경 실패: " + e.getMessage());
        } finally {
            try { DBUtil.getConnection().setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public ResultSet getSalesBeforeAfterPriceChange(int menuId, Timestamp changedAt) throws SQLException {
        return restaurantDAO.getSalesBeforeAfterPriceChange(menuId, changedAt);
    }

    public ResultSet getMenuInfo(int menuId) throws SQLException {
        return restaurantDAO.getMenuInfo(menuId);
    }
}
