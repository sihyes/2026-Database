package com.deliveryapp.controller;

import com.deliveryapp.model.Review;
import com.deliveryapp.service.ReviewService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ReviewController {
    private Scanner sc;
    private ReviewService reviewService = new ReviewService();

    public ReviewController(Scanner sc) {
        this.sc = sc;
    }

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

    private void insertReview() {
        try {
            System.out.print("\n주문 ID 입력: ");
            int orderId = sc.nextInt();
            sc.nextLine();

            if (!reviewService.canCreateReview(orderId)) {
                System.out.println("리뷰 작성 불가: 배달 완료 주문이 아니거나 이미 리뷰가 존재합니다.");
                return;
            }

            System.out.print("평점 입력 (0~5): ");
            double rating = sc.nextDouble();
            sc.nextLine();

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
