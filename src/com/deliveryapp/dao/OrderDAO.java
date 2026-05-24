package com.deliveryapp.dao;

import com.deliveryapp.common.DBUtil;

import java.sql.*;
import java.sql.SQLException;

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

    // 기간별 주문 통계
    public ResultSet getOrderStatByPeriod(String startDate, String endDate) throws SQLException{

    }
}
