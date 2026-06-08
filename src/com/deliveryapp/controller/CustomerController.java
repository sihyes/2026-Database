package com.deliveryapp.controller;

import com.deliveryapp.service.CustomerService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;


/**
 * 고객 관련 메뉴를 처리하는 컨트롤러
 * 고객 정보 조회/수정 및 REQ14 (인구통계 기반 구매 분석) 담당
 */
public class CustomerController {

    private Scanner sc;
    private CustomerService customerService = new CustomerService();

    public CustomerController(Scanner sc) {
        this.sc = sc;
    }

    // 고객 관련 메뉴 출력 후 사용자 입력에 따라 기능 실행
    public void showMenu() {
        while (true) {
            System.out.println("\n===== 고객 통계 분석 메뉴 =====");
            System.out.println("1. 고객 정보 조회");
            System.out.println("2. 고객 지역 변경");
            System.out.println("3. 고객 등급 변경");
            System.out.println("4. 고객 정보 변화 전후 구매 분석");
            System.out.println("5. 등급별 고객 구매 현황");
            System.out.println("6. 지역별 고객 구매 현황");
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

    // 고객 ID를 통해 정보 조회
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
                System.out.printf("지역: %s (%s)%n",
                        rs.getString("region_name"),
                        rs.getString("city"));
                System.out.printf("등급: %s%n", rs.getString("current_grade"));
            } else {
                System.out.println("존재하지 않는 고객이에요.");
            }
        } catch (SQLException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }

    /**
     * [REQ8] 고객 지역 변경
     * 현재 지역 ID를 조회한 후 새 지역 ID로 UPDATE
     * 변경 이력을 customer_history에 INSERT
     */
    private void updateCustomerRegion() {
        try {
            System.out.print("\n고객 ID 입력: ");
            int customerId = sc.nextInt();
            sc.nextLine();

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

            // 새 배달 주소 입력
            System.out.print("새 배달 주소 입력: ");
            String newAddress = sc.nextLine();

            customerService.updateCustomerRegion(customerId, oldRegionId, newRegionId,
                    currentGrade, newAddress);
        } catch (SQLException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }

    /**
     * [REQ8] 고객 등급 변경
     * 현재 등급을 조회한 후 새 등급으로 UPDATE
     * 변경 이력을 customer_history에 INSERT (트랜잭션 처리는 CustomerService 담당)
     */
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
            System.out.print("새 등급 입력 (Bronze/Silver/Gold/VIP): ");
            String newGrade = sc.nextLine();

            customerService.updateCustomerGrade(customerId, oldGrade, newGrade, currentRegionId);

        } catch (SQLException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }

    /**
     * [REQ14] 고객 정보 변화 전후 구매 분석
     * customer_history에서 변경 이력을 조회하고
     * 사용자가 선택한 변경 시점을 기준으로 orders 테이블에서 구매 통계를 비교한다.
     * 변경 유형(지역/등급/지역+등급)을 자동으로 판별하여 출력한다.
     */
    private void selectOrderStatByDemographic() {
        try {
            System.out.print("\n고객 ID 입력: ");
            int customerId = sc.nextInt();
            sc.nextLine();

            // 고객 변경 이럭 조회
            ResultSet historyRs = customerService.getCustomerHistory(customerId);

            if (!historyRs.next()) {
                System.out.println("변경 이력이 없어요.");
                return;
            }

            // 고객 이력 목록 저장
            List<Object[]> historyList = new ArrayList<>();
            do {
                // old/new region_id, grade 비교로 변경 유형 판단
                String changeType = "";
                boolean regionChanged = historyRs.getInt("old_region_id") != historyRs.getInt("new_region_id");
                boolean gradeChanged = !historyRs.getString("old_grade").equals(historyRs.getString("new_grade"));

                if (regionChanged && gradeChanged) changeType = "지역+등급 변경";
                else if (regionChanged) changeType = "지역 변경";
                else if (gradeChanged) changeType = "등급 변경";
                else changeType = "기타";

                historyList.add(new Object[]{
                        historyRs.getInt("history_id"),
                        historyRs.getTimestamp("changed_at"),
                        changeType,
                        historyRs.getString("old_region"),
                        historyRs.getString("new_region"),
                        historyRs.getString("old_grade"),
                        historyRs.getString("new_grade")
                });
            } while (historyRs.next());

            // 이력 출력
            System.out.println("\n===== 고객 변경 이력 =====");
            System.out.println("번호 | 변경일              | 변경 유형     | 변경 전            | 변경 후");
            System.out.println("------------------------------------------------------------------------");
            for (int i = 0; i < historyList.size(); i++) {
                Object[] h = historyList.get(i);
                String before = h[2].toString().contains("지역") ? (String) h[3] : "" +
                        (h[2].toString().contains("등급") ? " / " + h[5] : "");
                String after  = h[2].toString().contains("지역") ? (String) h[4] : "" +
                        (h[2].toString().contains("등급") ? " / " + h[6] : "");
                System.out.printf("%3d  | %-20s | %-12s | %-18s | %-10s%n",
                        i + 1,
                        h[1],
                        h[2],
                        h[3] + " / " + h[5],
                        h[4] + " / " + h[6]);
            }

            // 분석할 이력 번호 선택
            System.out.print("\n분석할 변경 이력 번호 선택: ");
            int selected = sc.nextInt();
            sc.nextLine();

            if (selected < 1 || selected > historyList.size()) {
                System.out.println("잘못된 번호예요.");
                return;
            }

            Timestamp changedAt = (Timestamp) historyList.get(selected - 1)[1];
            String changeType = (String) historyList.get(selected - 1)[2];

            // 선택한 시점 기준으로 변경 전후 구매 분석
            System.out.println("\n===== " + changeType + " 전후 구매 분석 =====");

            ResultSet beforeRs = customerService.getOrderStatBeforeChange(customerId, changedAt);
            if (beforeRs.next()) {
                System.out.printf("변경 전 → 주문 수: %d건 / 총 구매액: %,.0f원%n",
                        beforeRs.getInt("order_count"),
                        beforeRs.getDouble("total_sales"));
            }

            ResultSet afterRs = customerService.getOrderStatAfterChange(customerId, changedAt);
            if (afterRs.next()) {
                System.out.printf("변경 후 → 주문 수: %d건 / 총 구매액: %,.0f원%n",
                        afterRs.getInt("order_count"),
                        afterRs.getDouble("total_sales"));
            }

        } catch (SQLException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }

    /**
     * [REQ14] 지역별 고객 구매 현황
     * customer + orders + region 테이블을 JOIN하여
     * 지역별 주문 수, 총 구매액, 평균 구매액을 GROUP BY로 집계
     */
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

    /**
     * [REQ14] 등급별 고객 구매 현황
     * customer + orders 테이블을 JOIN하여
     * 등급별 주문 수, 총 구매액, 평균 구매액을 GROUP BY로 집계
     */
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