package com.deliveryapp.service;

import com.deliveryapp.common.DBUtil;
import com.deliveryapp.dao.RestaurantDAO;

import java.sql.*;

public class RestaurantService {
    private RestaurantDAO restaurantDAO = new RestaurantDAO();

    // 식당 리스트 조회
    public ResultSet getRestaurantList() throws SQLException {
        return restaurantDAO.getRestaurantList();
    }

    // 식당의 메뉴 조회
    public ResultSet getMenuList(int restaurantId) throws SQLException {
        return restaurantDAO.getMenuList(restaurantId);
    }

    // 메뉴 가격 조회
    public ResultSet getMenuPrice(int menuId) throws SQLException {
        return restaurantDAO.getMenuPrice(menuId);
    }

    // 입력한 메뉴 ID가 선택한 식당의 메뉴가 맞는지 검증
    public ResultSet getMenuByIdAndRestaurant(int menuId, int restaurantId) throws SQLException {
        return restaurantDAO.getMenuByIdAndRestaurant(menuId, restaurantId);
    }

    // 현재 메뉴 가격 가져오기
    public int getCurrentMenuPrice(int menuId) throws SQLException {
        return restaurantDAO.getCurrentMenuPrice(menuId);
    }

    // 메뉴 가격 변경
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

    // 가격 변경 전후 매출 비교
    public ResultSet getSalesBeforeAfterPriceChange(int menuId, Timestamp changedAt) throws SQLException {
        return restaurantDAO.getSalesBeforeAfterPriceChange(menuId, changedAt);
    }

    // 메뉴 정보 가져오기
    public ResultSet getMenuInfo(int menuId) throws SQLException {
        return restaurantDAO.getMenuInfo(menuId);
    }

    public ResultSet getPriceChangeHistory(int menuId) throws SQLException {
        return restaurantDAO.getPriceChangeHistory(menuId);
    }
}
