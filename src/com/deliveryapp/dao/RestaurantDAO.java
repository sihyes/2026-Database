package com.deliveryapp.dao;

import com.deliveryapp.common.DBUtil;

import java.sql.*;

public class RestaurantDAO {

    private Connection conn = DBUtil.getConnection();

    /**
     * 전체 식당 목록을 조회한다.
     * @return 식당 ID, 식당명, 카테고리 ResultSet
     */
    public ResultSet getRestaurantList() throws SQLException {
        String sql = "SELECT restaurant_id, restaurant_name, category FROM restaurant";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        return pstmt.executeQuery();
    }

    /**
     * 특정 식당의 판매 가능한 메뉴 목록을 조회한다.
     * @param restaurantId 조회할 식당 ID
     * @return 메뉴 ID, 메뉴명, 현재 가격 ResultSet
     */
    public ResultSet getMenuList(int restaurantId) throws SQLException{
        String sql = "SELECT menu_id, menu_name, current_price FROM menu " +
                "WHERE restaurant_id = ? AND is_available = TRUE";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, restaurantId);
        return pstmt.executeQuery();
    }

    /**
     * 메뉴 ID로 현재 단가를 조회한다.
     * 주문 등록 시 ordered_unit_price 스냅샷 저장에 사용한다.
     * @param menuId 조회할 메뉴 ID
     * @return 현재 가격 ResultSet
     */
    public ResultSet getMenuPrice(int menuId) throws SQLException {
        String sql = "SELECT current_price FROM menu WHERE menu_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, menuId);
        return pstmt.executeQuery();
    }


    /**
     * 메뉴 ID와 식당 ID로 해당 식당의 메뉴인지 검증한다.
     * 주문 등록 시 다른 식당 메뉴가 선택되지 않도록 방지한다.
     * @param menuId 검증할 메뉴 ID
     * @param restaurantId 선택한 식당 ID
     * @return 조건에 맞는 메뉴 ResultSet (없으면 empty)
     */
    public ResultSet getMenuByIdAndRestaurant(int menuId, int restaurantId) throws SQLException {
        String sql = "SELECT current_price FROM menu " +
                "WHERE menu_id = ? AND restaurant_id = ? AND is_available = TRUE";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, menuId);
        pstmt.setInt(2, restaurantId);
        return pstmt.executeQuery();
    }

    /**
     * [REQ8] 메뉴 현재 가격을 UPDATE한다.
     * 트랜잭션 처리는 RestaurantService에서 담당한다.
     * @param menuId 변경할 메뉴 ID
     * @param newPrice 새 가격
     */
    public void updateMenuPrice(int menuId, int newPrice) throws SQLException {
        String sql = "UPDATE menu SET current_price = ? WHERE menu_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, newPrice);
        pstmt.setInt(2, menuId);
        pstmt.executeUpdate();
    }


    /**
     * [REQ8] 메뉴 가격 변경 이력을 menu_price_history 테이블에 INSERT한다.
     * 과거 가격 데이터를 보존하여 REQ13 분석에 활용한다.
     * @param menuId 메뉴 ID
     * @param oldPrice 변경 전 가격
     * @param newPrice 변경 후 가격
     */
    public void insertMenuPriceHistory(int menuId, int oldPrice, int newPrice) throws SQLException {
        String sql = "INSERT INTO menu_price_history (menu_id, old_price, new_price, changed_at) " +
                "VALUES (?, ?, ?, NOW())";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, menuId);
        pstmt.setInt(2, oldPrice);
        pstmt.setInt(3, newPrice);
        pstmt.executeUpdate();
    }

    /**
     * 메뉴의 현재 가격을 int로 반환한다.
     * 가격 변경 전 oldPrice 조회에 사용한다.
     * @param menuId 조회할 메뉴 ID
     * @return 현재 가격, 메뉴가 없으면 -1 반환
     */
    public int getCurrentMenuPrice(int menuId) throws SQLException {
        String sql = "SELECT current_price FROM menu WHERE menu_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, menuId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) return rs.getInt("current_price");
        return -1;
    }

    /**
     * [REQ13] 가격 변경 전후 매출을 집계한다.
     * @param menuId 분석할 메뉴 ID
     * @param changedAt 가격 변경 기준 시각
     * @return 변경 전 매출(sales_before), 변경 후 매출(sales_after) ResultSet
     */
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

    /**
     * 메뉴 ID로 메뉴 상세 정보를 조회한다.
     * 가격 변경 전 현재 가격 및 메뉴명 확인에 사용한다.
     * @param menuId 조회할 메뉴 ID
     * @return 메뉴 ID, 메뉴명, 현재 가격 ResultSet
     */
    public ResultSet getMenuInfo(int menuId) throws SQLException {
        String sql = "SELECT menu_id, menu_name, current_price FROM menu WHERE menu_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, menuId);
        return pstmt.executeQuery();
    }


    /** 메뉴의 가격 변경 이력 조회 (전후 분석의 기준 시각을 사용자에게 묻지 않고 여기서 가져온다)
     *
     * @param menuId 조회할 메뉴 ID
     * @return
     * @throws SQLException
     */
    public ResultSet getPriceChangeHistory(int menuId) throws SQLException {
        String sql = "SELECT history_id, old_price, new_price, changed_at " +
                "FROM menu_price_history WHERE menu_id = ? ORDER BY changed_at";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, menuId);
        return pstmt.executeQuery();
    }
}
