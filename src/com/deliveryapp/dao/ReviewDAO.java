package com.deliveryapp.dao;

import com.deliveryapp.common.DBUtil;
import com.deliveryapp.model.Review;

import java.sql.*;

public class ReviewDAO {
    private Connection conn = DBUtil.getConnection();

    /**
     * [REQ5] 리뷰를 review 테이블에 INSERT한다.
     * order_id는 UNIQUE 제약조건으로 주문당 하나의 리뷰만 허용한다.
     * @param review 리뷰 정보 객체
     * @return 생성된 review_id, 실패 시 -1 반환
     */
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

    /**
     * [REQ6] 고객별 리뷰 상세 정보를 조회한다.
     * review_detail VIEW를 사용하여 고객명, 식당명 등 상세 정보를 함께 반환한다.
     * @param customerId 조회할 고객 ID
     * @return 리뷰 상세 ResultSet (review_id, rating, comment, customer명, restaurant명 포함)
     */
    public ResultSet getReviewDetailByCustomer(int customerId) throws SQLException {
        String sql = "SELECT * FROM review_detail WHERE customer_id = ? ORDER BY created_at DESC";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, customerId);
        return pstmt.executeQuery();
    }


    /**
     * [REQ6] 식당별 리뷰 상세 정보를 조회한다.
     * review_detail VIEW를 사용하여 고객명, 식당명 등 상세 정보를 함께 반환한다.
     * @param restaurantId 조회할 식당 ID
     * @return 리뷰 상세 ResultSet (review_id, rating, comment, customer명, restaurant명 포함)
     */
    public ResultSet getReviewDetailByRestaurant(int restaurantId) throws SQLException {
        String sql = "SELECT * FROM review_detail WHERE restaurant_id = ? ORDER BY created_at DESC";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, restaurantId);
        return pstmt.executeQuery();
    }

    /**
     * [REQ7] 식당별 평균 평점과 리뷰 수를 집계한다.
     * restaurant + orders + review 테이블을 JOIN하여 식당별 평균 평점(avg_rating)과 리뷰 수(review_count)를 GROUP BY로 집계한다.
     * LEFT JOIN으로 리뷰가 없는 식당도 포함한다.
     * @return 식당 ID, 식당명, 평균 평점, 리뷰 수 ResultSet
     */
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

    /**
     * [REQ9] 리뷰를 review 테이블에서 DELETE한다.
     * @param reviewId 삭제할 리뷰 ID
     */
    public void deleteReview(int reviewId) throws SQLException {
        String sql = "DELETE FROM review WHERE review_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, reviewId);
        pstmt.executeUpdate();
    }

    /**
     * 주문이 배달 완료 상태인지 검증한다.
     * 리뷰 등록 전 delivery_status = 'delivered' 여부를 확인한다.
     * @param orderId 검증할 주문 ID
     * @return 배달 완료 주문 ResultSet (없으면 empty)
     */
    public ResultSet getDeliveredOrder(int orderId) throws SQLException {
        String sql = "SELECT order_id FROM orders WHERE order_id = ? AND delivery_status = 'delivered'";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, orderId);
        return pstmt.executeQuery();
    }

    /**
     * 해당 주문에 이미 리뷰가 존재하는지 확인한다.
     * order_id UNIQUE 제약조건으로 중복 리뷰를 방지하기 위해 사전 검증한다.
     * @param orderId 확인할 주문 ID
     * @return 기존 리뷰 ResultSet (없으면 empty)
     */
    public ResultSet getReviewByOrderId(int orderId) throws SQLException {
        String sql = "SELECT review_id FROM review WHERE order_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, orderId);
        return pstmt.executeQuery();
    }
}
