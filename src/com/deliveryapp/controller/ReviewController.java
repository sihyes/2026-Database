package com.deliveryapp.controller;

import com.deliveryapp.model.Review;
import com.deliveryapp.service.ReviewService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * 리뷰 관련 메뉴를 처리하는 컨트롤러
 * REQ5 (INSERT), REQ6 (SELECT + JOIN + VIEW), REQ7 (SELECT + GROUP BY),
 * REQ9 (DELETE) 담당
 */
public class ReviewController {
    private Scanner sc;
    private ReviewService reviewService = new ReviewService();

    public ReviewController(Scanner sc) {
        this.sc = sc;
    }

    // 리뷰 관련 메뉴를 출력하고 사용자 입력에 따라 기능을 실행
    public void showMenu() {
        while (true) {
            System.out.println("\n===== 리뷰 메뉴 =====");
            System.out.println("1. 리뷰 등록");
            System.out.println("2. 리뷰 조회 (고객)");
            System.out.println("3. 리뷰 조회 (식당)");
            System.out.println("4. 식당 별 평점 집계");
            System.out.println("5. 리뷰 삭제");
            System.out.println("0. 이전 메뉴");
            System.out.print("선택: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> insertReview();
                case 2 -> selectReviewByCustomer();
                case 3 -> selectReviewByRestaurant();
                case 4 -> selectRestaurantRatingStat();
                case 5 -> deleteReview();
                case 0 -> { return; }
                default -> System.out.println("없는 메뉴예요.");
            }
        }
    }

    /**
     * [REQ5] 리뷰 등록
     * 주문 ID를 입력받아 배달 완료 여부 및 중복 리뷰 여부를 검증한 후
     * 평점(0~5)과 코멘트를 입력받아 review 테이블에 INSERT
     */
    private void insertReview() {
        try {
            System.out.print("\n주문 ID 입력: ");
            int orderId = sc.nextInt();
            sc.nextLine();

            // 배달 완료 주문인지, 이미 리뷰가 존재하는지 검증
            if (!reviewService.canCreateReview(orderId)) {
                System.out.println("리뷰 작성 불가: 배달 완료 주문이 아니거나 이미 리뷰가 존재합니다.");
                return;
            }

            System.out.print("평점 입력 (0~5): ");
            double rating = sc.nextDouble();
            sc.nextLine();

            // 평점 범위 검증
            if (!reviewService.isRatingValid(rating)) {
                System.out.println("평점은 0~5 범위여야 합니다.");
                return;
            }

            System.out.print("리뷰 내용 입력: ");
            String comment = sc.nextLine();

            Review review = new Review(orderId, rating, comment);
            reviewService.insertReview(review);
        } catch (SQLException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }

    /**
     * [REQ6] 고객별 리뷰 조회
     * 고객 ID를 입력받아 review_detail VIEW와 customer 테이블을 JOIN하여
     * 해당 고객의 리뷰 목록을 출력한다.
     */
    private void selectReviewByCustomer() {
        try {
            System.out.print("\n고객 ID 입력: ");
            int customerId = sc.nextInt();
            sc.nextLine();

            ResultSet rs = reviewService.getReviewDetailByCustomer(customerId);
            printReviewDetail(rs);
        } catch (SQLException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }

    /**
     * [REQ6] 식당별 리뷰 조회
     * 식당 ID를 입력받아 review_detail VIEW와 restaurant 테이블을 JOIN하여
     * 해당 식당의 리뷰 목록을 출력한다.
     */
    private void selectReviewByRestaurant() {
        try {
            System.out.print("\n식당 ID 입력: ");
            int restaurantId = sc.nextInt();
            sc.nextLine();

            ResultSet rs = reviewService.getReviewDetailByRestaurant(restaurantId);
            printReviewDetail(rs);
        } catch (SQLException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }

    /**
     * [REQ7] 식당별 평점 집계 review + restaurant 테이블을 JOIN하여
     * 식당별 평균 평점과 리뷰 수를 GROUP BY로 집계하여 출력한다.
     */
    private void selectRestaurantRatingStat() {
        try {
            ResultSet rs = reviewService.getRestaurantRatingStat();
            System.out.println("\n식당ID | 식당명           | 평균평점 | 리뷰수");
            System.out.println("---------------------------------------------");
            while (rs.next()) {
                double avg = rs.getDouble("avg_rating");
                System.out.printf("%-6d | %-14s | %7.2f | %5d%n",
                        rs.getInt("restaurant_id"),
                        rs.getString("restaurant_name"),
                        avg,
                        rs.getInt("review_count"));
            }
        } catch (SQLException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }

    /**
     * [REQ9] 리뷰 삭제
     * 리뷰 ID를 입력받아 확인 후 review 테이블에서 DELETE
     */
    private void deleteReview() {
        System.out.print("\n삭제할 리뷰 ID 입력: ");
        int reviewId = sc.nextInt();
        sc.nextLine();

        System.out.print("정말 삭제하시겠습니까? (y/n): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            reviewService.deleteReview(reviewId);
        } else {
            System.out.println("삭제를 중단했어요.");
        }
    }

    // 리뷰 상세 정보를 표 형식으로 출력하는 공통 메서드
    private void printReviewDetail(ResultSet rs) throws SQLException {
        System.out.println("\n리뷰ID | 주문ID | 평점 | 작성시각           | 고객명 | 식당명 | 코멘트");
        System.out.println("--------------------------------------------------------------------------");
        while (rs.next()) {
            System.out.printf("%-6d | %-6d | %4.1f | %-18s | %-6s | %-6s | %s%n",
                    rs.getInt("review_id"),
                    rs.getInt("order_id"),
                    rs.getDouble("rating"),
                    rs.getString("created_at"),
                    rs.getString("name"),
                    rs.getString("restaurant_name"),
                    rs.getString("comment"));
        }
    }
}
