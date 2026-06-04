package com.deliveryapp.dao;

import com.deliveryapp.common.DBUtil;

import java.sql.*;

public class CustomerDAO {

    private Connection conn = DBUtil.getConnection();

    /**
     * 고객 ID로 고객 정보를 조회한다.
     * @param customerId 조회할 고객 ID
     * @return 고객 정보 ResultSet
     */
    public ResultSet getCustomer(int customerId) throws SQLException {
        String sql = "SELECT * FROM customer WHERE customer_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, customerId);
        ResultSet rs = ps.executeQuery();
        return rs;
    }

    /**
     * 고객의 변경 이력을 조회한다.
     * customer_history + region 테이블을 JOIN하여 변경 전후 지역명을 함께 반환한다.
     * @param customerId 조회할 고객 ID
     * @return 변경 이력 ResultSet (history_id, changed_at, old/new_region, old/new_grade 포함)
     */
    public ResultSet getCustomerHistory(int customerId) throws SQLException {
        String sql = "SELECT ch.history_id, ch.changed_at, " +
                "r1.region_name AS old_region, r2.region_name AS new_region, " +
                "ch.old_region_id, ch.new_region_id, " +
                "ch.old_grade, ch.new_grade " +
                "FROM customer_history ch " +
                "LEFT JOIN region r1 ON ch.old_region_id = r1.region_id " +
                "LEFT JOIN region r2 ON ch.new_region_id = r2.region_id " +
                "WHERE ch.customer_id = ? " +
                "ORDER BY ch.changed_at";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, customerId);
        return pstmt.executeQuery();
    }

    /**
     * [REQ14] 고객 정보 변경 전 주문 통계를 조회한다.
     * 변경 시각(changedAt) 이전의 주문 수와 총 구매액을 집계한다.
     * @param customerId 조회할 고객 ID
     * @param changedAt 변경 기준 시각
     * @return 주문 수(order_count), 총 구매액(total_sales) ResultSet
     */
    public ResultSet getOrderStatBeforeChange(int customerId, Timestamp changedAt) throws SQLException {
        String sql = "SELECT COUNT(*) AS order_count, SUM(total_price) AS total_sales " +
                "FROM orders " +
                "WHERE customer_id = ? AND order_time < ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, customerId);
        pstmt.setTimestamp(2, changedAt);
        return pstmt.executeQuery();
    }

    /**
     * [REQ14] 고객 정보 변경 후 주문 통계를 조회한다.
     * 변경 시각(changedAt) 이후의 주문 수와 총 구매액을 집계한다.
     * @param customerId 조회할 고객 ID
     * @param changedAt 변경 기준 시각
     * @return 주문 수(order_count), 총 구매액(total_sales) ResultSet
     */
    public ResultSet getOrderStatAfterChange(int customerId, Timestamp changedAt) throws SQLException {
        String sql = "SELECT COUNT(*) AS order_count, SUM(total_price) AS total_sales " +
                "FROM orders " +
                "WHERE customer_id = ? AND order_time >= ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, customerId);
        pstmt.setTimestamp(2, changedAt);
        return pstmt.executeQuery();
    }

    /**
     * [REQ8] 고객 지역을 변경한다.
     * customer 테이블의 region_id와 updated_at을 UPDATE한다.
     * @param customerId 변경할 고객 ID
     * @param newRegionId 새 지역 ID
     */
    public void updateCustomerRegion(int customerId, int newRegionId) throws SQLException {
        String sql = "UPDATE customer SET region_id = ?, updated_at = NOW() WHERE customer_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, newRegionId);
        pstmt.setInt(2, customerId);
        pstmt.executeUpdate();
    }

    /**
     * [REQ8] 고객 등급을 변경한다.
     * customer 테이블의 current_grade와 updated_at을 UPDATE한다.
     * @param customerId 변경할 고객 ID
     * @param newGrade 새 등급 (Bronze/Silver/Gold/VIP)
     */
    public void updateCustomerGrade(int customerId, String newGrade) throws SQLException {
        String sql = "UPDATE customer SET current_grade = ?, updated_at = NOW() WHERE customer_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, newGrade);
        pstmt.setInt(2, customerId);
        pstmt.executeUpdate();
    }

    /**
     * 고객 변경 이력을 customer_history 테이블에 INSERT한다.
     * @param customerId 고객 ID
     * @param oldRegionId 변경 전 지역 ID
     * @param newRegionId 변경 후 지역 ID
     * @param oldGrade 변경 전 등급
     * @param newGrade 변경 후 등급
     */
    public void insertCustomerHistory(int customerId, Integer oldRegionId, Integer newRegionId,
                                      String oldGrade, String newGrade) throws SQLException {
        String sql = "INSERT INTO customer_history (customer_id, old_region_id, new_region_id, old_grade, new_grade) " +
                "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, customerId);
        pstmt.setObject(2, oldRegionId);
        pstmt.setObject(3, newRegionId);
        pstmt.setString(4, oldGrade);
        pstmt.setString(5, newGrade);
        pstmt.executeUpdate();
    }

    /**
     * [REQ14] 지역별 고객 구매 현황을 집계한다.
     * orders + customer + region 테이블을 JOIN하여 지역별 주문 수, 총 구매액, 평균 구매액을 GROUP BY로 집계한다.
     * @return 지역명, 도시, 주문 수, 총 구매액, 평균 구매액 ResultSet
     */
    public ResultSet getOrderStatByRegion() throws SQLException {
        String sql = "SELECT r.region_name, r.city, " +
                "COUNT(o.order_id) AS order_count, " +
                "SUM(o.total_price) AS total_sales, " +
                "AVG(o.total_price) AS avg_sales " +
                "FROM orders o " +
                "JOIN customer c ON o.customer_id = c.customer_id " +
                "JOIN region r ON c.region_id = r.region_id " +
                "GROUP BY r.region_id, r.region_name, r.city " +
                "ORDER BY total_sales DESC";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        return pstmt.executeQuery();
    }

    /**
     * [REQ14] 등급별 고객 구매 현황을 집계한다.
     * orders + customer 테이블을 JOIN하여 등급별 주문 수, 총 구매액, 평균 구매액을 GROUP BY로 집계한다.
     * @return 등급, 주문 수, 총 구매액, 평균 구매액 ResultSet
     */
    public ResultSet getOrderStatByGrade() throws SQLException {
        String sql = "SELECT c.current_grade, " +
                "COUNT(o.order_id) AS order_count, " +
                "SUM(o.total_price) AS total_sales, " +
                "AVG(o.total_price) AS avg_sales " +
                "FROM orders o " +
                "JOIN customer c ON o.customer_id = c.customer_id " +
                "GROUP BY c.current_grade " +
                "ORDER BY total_sales DESC";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        return pstmt.executeQuery();
    }

}
