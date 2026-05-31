package com.deliveryapp.dao;

import com.deliveryapp.common.DBUtil;

import java.sql.*;

public class CustomerDAO {

    private Connection conn = DBUtil.getConnection();

    public ResultSet getCustomer(int customerId) throws SQLException {
        String sql = "SELECT * FROM customer WHERE customer_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, customerId);
        ResultSet rs = ps.executeQuery();
        return rs;
    }

    // 고객 변경 이력 조회
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

    // 변경 전 매출
    public ResultSet getOrderStatBeforeChange(int customerId, Timestamp changedAt) throws SQLException {
        String sql = "SELECT COUNT(*) AS order_count, SUM(total_price) AS total_sales " +
                "FROM orders " +
                "WHERE customer_id = ? AND order_time < ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, customerId);
        pstmt.setTimestamp(2, changedAt);
        return pstmt.executeQuery();
    }

    // 변경 후 매출
    public ResultSet getOrderStatAfterChange(int customerId, Timestamp changedAt) throws SQLException {
        String sql = "SELECT COUNT(*) AS order_count, SUM(total_price) AS total_sales " +
                "FROM orders " +
                "WHERE customer_id = ? AND order_time >= ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, customerId);
        pstmt.setTimestamp(2, changedAt);
        return pstmt.executeQuery();
    }

    // 고객 지역 변경
    public void updateCustomerRegion(int customerId, int newRegionId) throws SQLException {
        String sql = "UPDATE customer SET region_id = ?, updated_at = NOW() WHERE customer_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, newRegionId);
        pstmt.setInt(2, customerId);
        pstmt.executeUpdate();
    }

    // 고객 등급 변경
    public void updateCustomerGrade(int customerId, String newGrade) throws SQLException {
        String sql = "UPDATE customer SET current_grade = ?, updated_at = NOW() WHERE customer_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, newGrade);
        pstmt.setInt(2, customerId);
        pstmt.executeUpdate();
    }

    // 이력 저장
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

    // 지역별 매출 분석
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

    // 등급별 매출 분석
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
