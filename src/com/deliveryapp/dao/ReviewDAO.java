package com.deliveryapp.dao;

import com.deliveryapp.common.DBUtil;
import com.deliveryapp.model.Review;

import java.sql.*;

public class ReviewDAO {
    private Connection conn = DBUtil.getConnection();

    // 리뷰 등록 (배달 완료 주문만)
    public int insertReview(Review review) throws SQLException {
        String sql = "INSERT INTO review (order_id, rating, comment) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, review.getOrderId());
        pstmt.setDouble(2, review.getRating());
        pstmt.setString(3, review.getComment());
        pstmt.executeUpdate();

        ResultSet rs = pstmt.getGeneratedKeys();
        if (rs.next()) return rs.getInt(1);
        return -1;
    }

    // 리뷰 조회 - 고객 기준 (VIEW)
    public ResultSet getReviewDetailByCustomer(int customerId) throws SQLException {
        String sql = "SELECT * FROM review_detail WHERE customer_id = ? ORDER BY created_at DESC";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, customerId);
        return pstmt.executeQuery();
    }

    // 리뷰 조회 - 식당 기준 (VIEW)
    public ResultSet getReviewDetailByRestaurant(int restaurantId) throws SQLException {
        String sql = "SELECT * FROM review_detail WHERE restaurant_id = ? ORDER BY created_at DESC";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, restaurantId);
        return pstmt.executeQuery();
    }

    // 식당별 평점 집계
    public ResultSet getRestaurantRatingStat() throws SQLException {
        String sql = "SELECT res.restaurant_id, res.restaurant_name, " +
                "AVG(r.rating) AS avg_rating, COUNT(r.review_id) AS review_count " +
                "FROM restaurant res " +
                "LEFT JOIN orders o ON res.restaurant_id = o.restaurant_id " +
                "LEFT JOIN review r ON o.order_id = r.order_id " +
                "GROUP BY res.restaurant_id, res.restaurant_name " +
                "ORDER BY avg_rating DESC";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        return pstmt.executeQuery();
    }

    // 리뷰 삭제
    public void deleteReview(int reviewId) throws SQLException {
        String sql = "DELETE FROM review WHERE review_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, reviewId);
        pstmt.executeUpdate();
    }

    // 리뷰 작성 가능 주문 검증
    public ResultSet getDeliveredOrder(int orderId) throws SQLException {
        String sql = "SELECT order_id FROM orders WHERE order_id = ? AND delivery_status = 'delivered'";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, orderId);
        return pstmt.executeQuery();
    }

    // 이미 리뷰 등록 여부
    public ResultSet getReviewByOrderId(int orderId) throws SQLException {
        String sql = "SELECT review_id FROM review WHERE order_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, orderId);
        return pstmt.executeQuery();
    }
}
