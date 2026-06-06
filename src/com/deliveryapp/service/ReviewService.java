package com.deliveryapp.service;

import com.deliveryapp.common.DBUtil;
import com.deliveryapp.dao.ReviewDAO;
import com.deliveryapp.model.Review;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewService {
    private ReviewDAO reviewDAO = new ReviewDAO();
    private Connection conn = DBUtil.getConnection();

    // 리뷰 등록
    public void insertReview(Review review) {
        try {
            conn.setAutoCommit(false);
            reviewDAO.insertReview(review);
            conn.commit();
            System.out.println("리뷰 등록 완료");
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.out.println("리뷰 등록 실패: " + e.getMessage());
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // 고객 ID로 리뷰 조회
    public ResultSet getReviewDetailByCustomer(int customerId) throws SQLException {
        return reviewDAO.getReviewDetailByCustomer(customerId);
    }

    // 식당 ID로 리뷰 조회
    public ResultSet getReviewDetailByRestaurant(int restaurantId) throws SQLException {
        return reviewDAO.getReviewDetailByRestaurant(restaurantId);
    }

    // 별점 조회
    public ResultSet getRestaurantRatingStat() throws SQLException {
        return reviewDAO.getRestaurantRatingStat();
    }

    // 리뷰 삭제
    public void deleteReview(int reviewId) {
        try {
            conn.setAutoCommit(false);
            reviewDAO.deleteReview(reviewId);
            conn.commit();
            System.out.println("리뷰 삭제 완료");
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.out.println("리뷰 삭제 실패: " + e.getMessage());
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // 리뷰 등록 가능 검증
    public boolean canCreateReview(int orderId) throws SQLException {
        ResultSet delivered = reviewDAO.getDeliveredOrder(orderId);
        if (!delivered.next()) return false;
        ResultSet exists = reviewDAO.getReviewByOrderId(orderId);
        return !exists.next();
    }

    // 별점 범위 검증
    public boolean isRatingValid(double rating) {
        return rating >= 0.0 && rating <= 5.0;
    }
}
