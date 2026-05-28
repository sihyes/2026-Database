package com.deliveryapp.dao;

import com.deliveryapp.common.DBUtil;

import java.sql.*;
import java.sql.SQLException;

import com.deliveryapp.model.Order;

public class OrderDAO {

    private Connection conn = DBUtil.getConnection();

    // order 등록
    public int insertOrder(Order order) throws SQLException{
        String sql = "INSERT INTO orders (customer_id, restaurant_id, total_price, order_time, " +
                "delivery_status, delivery_fee_id, address_id, coupon_id, discount_amount) " +
                "VALUES (?, ?, ?, NOW(), 'pending', ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, order.getCustomerId());
        pstmt.setInt(2, order.getRestaurantId());
        pstmt.setDouble(3, order.getTotalPrice());
        pstmt.setObject(4, order.getDeliveryFeeId());
        pstmt.setObject(5, order.getAddressId());
        pstmt.setObject(6, order.getCouponId());
        pstmt.setDouble(7, order.getDiscountAmount());
        pstmt.executeUpdate();

        // 생성된 order_id 반환
        ResultSet rs = pstmt.getGeneratedKeys();
        if (rs.next()) return rs.getInt(1);
        return -1;
    }

    // order_item 등록
    public void insertOrderItem(int orderId, int menuId, int quantity, double unitPrice) throws SQLException{
        String sql = "INSERT INTO order_item (order_id, menu_id, quantity, ordered_unit_price) " +
                "VALUES (?, ?, ?, ?)";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, orderId);
        pstmt.setInt(2, menuId);
        pstmt.setInt(3, quantity);
        pstmt.setDouble(4, unitPrice);
        pstmt.executeUpdate();
    }

    // 배달 상태 변경
    public void updateDeliveryStatus(int orderId, String status) throws SQLException{
        String sql = "UPDATE orders SET delivery_status = ? WHERE order_id = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, status);
        pstmt.setInt(2, orderId);
        pstmt.executeUpdate();
    }

    // 주문 취소
    public void deleteOrder(int orderId) throws SQLException{
        String sql = "DELETE FROM orders WHERE order_id = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, orderId);
        pstmt.executeUpdate();
    }

    // 주문 상품 취소
    public void deleteOrderItem(int orderId) throws SQLException{
        String sql = "DELETE FROM order_item WHERE order_id = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, orderId);
        pstmt.executeUpdate();
    }

    // 기간별 주문 통계 (REQ7)
    public ResultSet getOrderStatByPeriod(String startDate, String endDate) throws SQLException{
        String sql = "SELECT DATE(order_time) AS order_date, COUNT(*) AS order_count, SUM(total_price) AS total_sales " +
                "FROM orders WHERE order_time BETWEEN ? AND ? " +
                "GROUP BY DATE(order_time) ORDER BY order_date";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, startDate);
        pstmt.setString(2, endDate);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    // 주문 상세 조회 (VIEW 사용)
    public ResultSet getOrderDetail(int customerId) throws SQLException {
        String sql = "SELECT * FROM order_detail_view WHERE customer_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, customerId);
        return pstmt.executeQuery();
    }
}
