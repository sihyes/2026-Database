package com.deliveryapp.dao;

import com.deliveryapp.common.DBUtil;

import java.sql.*;
import java.sql.SQLException;

import com.deliveryapp.model.Order;

public class OrderDAO {

    private Connection conn = DBUtil.getConnection();

    /**
     * [REQ5] 주문을 orders 테이블에 INSERT한다.
     * Statement.RETURN_GENERATED_KEYS로 생성된 order_id를 반환한다.
     * @param order 주문 정보 객체
     * @return 생성된 order_id, 실패 시 -1 반환
     */
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

    /**
     * [REQ5] 주문 항목을 order_item 테이블에 INSERT한다.
     * @param orderId 주문 ID
     * @param menuId 메뉴 ID
     * @param quantity 수량
     * @param unitPrice 주문 시점 단가 (스냅샷)
     */
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

    /**
     * [REQ8] 배달 상태를 UPDATE한다.
     * @param orderId 주문 ID
     * @param status 변경할 배달 상태 (pending/delivering/delivered)
     */
    public void updateDeliveryStatus(int orderId, String status) throws SQLException{
        String sql = "UPDATE orders SET delivery_status = ? WHERE order_id = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, status);
        pstmt.setInt(2, orderId);
        pstmt.executeUpdate();
    }

    /**
     * [REQ9] 주문을 orders 테이블에서 DELETE한다.
     * @param orderId 삭제할 주문 ID
     */
    public void deleteOrder(int orderId) throws SQLException{
        String sql = "DELETE FROM orders WHERE order_id = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, orderId);
        pstmt.executeUpdate();
    }

    /**
     * [REQ9] 주문 항목을 order_item 테이블에서 DELETE한다.
     * @param orderId 삭제할 주문 ID
     */
    public void deleteOrderItem(int orderId) throws SQLException{
        String sql = "DELETE FROM order_item WHERE order_id = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, orderId);
        pstmt.executeUpdate();
    }


    /**
     * [REQ7] 기간별 주문 통계를 조회한다.
     * 시작일 ~ 종료일 사이의 주문을 날짜별로 GROUP BY하여 집계한다.
     * @param startDate 시작 날짜 (yyyy-MM-dd)
     * @param endDate 종료 날짜 (yyyy-MM-dd)
     * @return 날짜별 주문 수(order_count), 총 매출(total_sales) ResultSet
     */
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

    /**
     * [REQ6] 주문 상세 정보를 조회한다.
     * order_detail_view(VIEW)와 customer 테이블을 JOIN하여
     * 고객명, 등급, 주문 항목 정보를 함께 반환한다.
     * @param customerId 조회할 고객 ID
     * @return 주문 상세 ResultSet (customer_name, current_grade, order_id, menu_name 등 포함)
     */
    public ResultSet getOrderDetail(int customerId) throws SQLException {
        String sql = "SELECT v.*, c.name AS customer_name, c.current_grade, " +
                "df.fee AS delivery_fee, df.delivery_type, " +
                "cp.coupon_name, v.discount_amount " +
                "FROM order_detail_view v " +
                "JOIN customer c ON v.customer_id = c.customer_id " +
                "LEFT JOIN orders o ON v.order_id = o.order_id " +
                "LEFT JOIN delivery_fee df ON o.delivery_fee_id = df.delivery_fee_id " +
                "LEFT JOIN coupon cp ON o.coupon_id = cp.coupon_id " +
                "WHERE v.customer_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, customerId);
        return pstmt.executeQuery();
    }

    /**
     * 배달 유형과 거리 구간으로 배달비 정보를 조회한다.
     * @param deliveryType 배달 유형 (한집배달/알뜰배달/가게배달/무료배달)
     * @param distanceCategory 거리 구간 (단거리/중거리/장거리/전체)
     * @return delivery_fee_id, fee ResultSet
     */
    public ResultSet getDeliveryFee(String deliveryType, String distanceCategory) throws SQLException {
        String sql = "SELECT delivery_fee_id, fee FROM delivery_fee " +
                "WHERE delivery_type = ? AND distance_category = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, deliveryType);
        pstmt.setString(2, distanceCategory);
        return pstmt.executeQuery();
    }
}
