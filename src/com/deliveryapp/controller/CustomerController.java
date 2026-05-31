package com.deliveryapp.controller;

import com.deliveryapp.service.CustomerService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;

public class CustomerController {

    private Scanner sc;
    private CustomerService customerService = new CustomerService();

    public CustomerController(Scanner sc) {
        this.sc = sc;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n===== 고객 통계 분석 메뉴 =====");
            System.out.println("1. 고객 정보 조회");
            System.out.println("2. 고객 지역 변경");
            System.out.println("3. 고객 등급 변경");
            System.out.println("4. 고객 정보 변화 전후 구매 분석");
            System.out.println("5. 지역별 고객 구매 현황");
            System.out.println("6. 등급별 고객 구매 현황");
            System.out.println("0. 이전 메뉴");
            System.out.print("선택: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> selectCustomer();
                case 2 -> updateCustomerRegion();
                case 3 -> updateCustomerGrade();
                case 4 -> selectOrderStatByDemographic();
                case 5 -> selectOrderStatByGrade();
                case 6 -> selectOrderStatByRegion();
                case 0 -> { return; }
                default -> System.out.println("없는 메뉴예요.");
            }
        }
    }

    // 고객 정보 조회
    private void selectCustomer() {
        try {
            System.out.print("\n고객 ID 입력: ");
            int customerId = sc.nextInt();
            sc.nextLine();

            ResultSet rs = customerService.getCustomer(customerId);
            if (rs.next()) {
                System.out.println("\n===== 고객 정보 =====");
                System.out.printf("이름: %s%n", rs.getString("name"));
                System.out.printf("나이: %d%n", rs.getInt("age"));
                System.out.printf("성별: %s%n", rs.getString("gender"));
                System.out.printf("등급: %s%n", rs.getString("current_grade"));
            } else {
                System.out.println("존재하지 않는 고객이에요.");
            }
        } catch (SQLException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }

    // 지역 변경
    private void updateCustomerRegion() {
        try {
            System.out.print("\n고객 ID 입력: ");
            int customerId = sc.nextInt();
            sc.nextLine();

            // 현재 정보 조회
            ResultSet rs = customerService.getCustomer(customerId);
            if (!rs.next()) {
                System.out.println("존재하지 않는 고객이에요.");
                return;
            }

            int oldRegionId = rs.getInt("region_id");
            String currentGrade = rs.getString("current_grade");

            System.out.printf("현재 지역 ID: %d%n", oldRegionId);
            System.out.print("새 지역 ID 입력: ");
            int newRegionId = sc.nextInt();
            sc.nextLine();

            customerService.updateCustomerRegion(customerId, oldRegionId, newRegionId, currentGrade);

        } catch (SQLException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }

    // 등급 변경
    private void updateCustomerGrade() {
        try {
            System.out.print("\n고객 ID 입력: ");
            int customerId = sc.nextInt();
            sc.nextLine();

            // 현재 정보 조회
            ResultSet rs = customerService.getCustomer(customerId);
            if (!rs.next()) {
                System.out.println("존재하지 않는 고객이에요.");
                return;
            }

            String oldGrade = rs.getString("current_grade");
            int currentRegionId = rs.getInt("region_id");

            System.out.printf("현재 등급: %s%n", oldGrade);
            System.out.println("새 등급 입력 (Bronze/Silver/Gold/VIP): ");
            String newGrade = sc.nextLine();

            customerService.updateCustomerGrade(customerId, oldGrade, newGrade, currentRegionId);

        } catch (SQLException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }

    // 고객 정보 변화 전후 매출 비교
    private void selectOrderStatByDemographic() {
        try {
            System.out.print("\n고객 ID 입력: ");
            int customerId = sc.nextInt();
            sc.nextLine();

            // 변경 이력 조회
            ResultSet historyRs = customerService.getCustomerHistory(customerId);

            if (!historyRs.next()) {
                System.out.println("변경 이력이 없어요.");
                return;
            }

            System.out.println("\n===== 고객 변경 이력 =====");
            System.out.println("변경일              | 변경 전 지역 | 변경 후 지역 | 변경 전 등급 | 변경 후 등급");
            System.out.println("------------------------------------------------------------------------");

            Timestamp changedAt = null;
            do {
                changedAt = historyRs.getTimestamp("changed_at");
                System.out.printf("%-20s | %-10s | %-10s | %-10s | %-10s%n",
                        changedAt,
                        historyRs.getString("old_region"),
                        historyRs.getString("new_region"),
                        historyRs.getString("old_grade"),
                        historyRs.getString("new_grade"));
            } while (historyRs.next());

            // 가장 최근 변경일 기준으로 매출 비교
            System.out.println("\n===== 변경 전후 매출 비교 =====");

            ResultSet beforeRs = customerService.getOrderStatBeforeChange(customerId, changedAt);
            if (beforeRs.next()) {
                System.out.printf("변경 전 → 주문 수: %d건 / 총 매출: %,.0f원%n",
                        beforeRs.getInt("order_count"),
                        beforeRs.getDouble("total_sales"));
            }

            ResultSet afterRs = customerService.getOrderStatAfterChange(customerId, changedAt);
            if (afterRs.next()) {
                System.out.printf("변경 후 → 주문 수: %d건 / 총 매출: %,.0f원%n",
                        afterRs.getInt("order_count"),
                        afterRs.getDouble("total_sales"));
            }

        } catch (SQLException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }

    // 지역별 고객 구매 현황
    private void selectOrderStatByRegion() {
        try {
            ResultSet rs = customerService.getOrderStatByRegion();
            System.out.println("\n지역명     | 도시 | 주문수 | 총구매액       | 평균구매액");
            System.out.println("--------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-10s | %-4s | %4d  | %,12.0f원 | %,8.0f원%n",
                        rs.getString("region_name"),
                        rs.getString("city"),
                        rs.getInt("order_count"),
                        rs.getDouble("total_sales"),
                        rs.getDouble("avg_sales"));
            }
        } catch (SQLException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }

    // 등급별 고객 구매 현황
    private void selectOrderStatByGrade() {
        try {
            ResultSet rs = customerService.getOrderStatByGrade();
            System.out.println("\n등급     | 주문수 | 총구매액       | 평균구매액");
            System.out.println("------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-8s | %4d  | %,12.0f원 | %,8.0f원%n",
                        rs.getString("current_grade"),
                        rs.getInt("order_count"),
                        rs.getDouble("total_sales"),
                        rs.getDouble("avg_sales"));
            }
        } catch (SQLException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }
}