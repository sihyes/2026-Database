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
    public ResultSet getOrderStatBeforeChange(int customerId) throws SQLException {
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

}
