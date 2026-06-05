package com.deliveryapp.controller;

import com.deliveryapp.model.Order;
import com.deliveryapp.service.CouponService;
import com.deliveryapp.service.OrderService;
import com.deliveryapp.service.RestaurantService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 주문 관련 메뉴 처리 컨트롤러
 * REQ05 (INSERT), REQ06 (SELECT + JOIN + VIEW),
 * REQ07 (SELECT + GROUP BY), REQ08 (UPDATE + 트랜잭션), REQ09 (DELETE)
 */

public class OrderController {

    private Scanner sc;
    private OrderService orderService = new OrderService();
    private RestaurantService restaurantService = new RestaurantService();
    private CouponService couponService = new CouponService();

    public OrderController(Scanner sc) {
        this.sc = sc;
    }

    // 주문 관련 메뉴 출력 후 사용자 입력에 따라 기능 실행
    public void showMenu() {
        while (true) {
            System.out.println("\n===== 주문 관련 메뉴 =====");
            System.out.println("1. 주문하기");
            System.out.println("2. 주문 상세 조회");
            System.out.println("3. 기간별 주문 통계");
            System.out.println("4. 배달 상태 변경");
            System.out.println("5. 주문 취소");
            System.out.println("0. 이전 메뉴");
            System.out.print("선택: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> insertOrder();
                case 2 -> selectOrderDetail();
                case 3 -> selectOrderStat();
                case 4 -> updateDeliveryStatus();
                case 5 -> deleteOrder();
                case 0 -> { return; }
                default -> System.out.println("없는 메뉴예요.");
            }
        }
    }

    /**
     * [REQ5] 주문 등록
     * 고객 ID, 식당, 메뉴, 수량, 쿠폰을 입력받아 orders + order_item 테이블에 INSERT
     * orders와 order_item INSERT는 트랜잭션으로 처리 (OrderService)
     */
    /**
     * [REQ5] 주문 등록
     * 고객 ID, 식당, 메뉴, 수량, 배달 유형, 쿠폰을 입력받아
     * orders + order_item 테이블에 INSERT
     * orders와 order_item INSERT는 트랜잭션으로 처리 (OrderService)
     */
    private void insertOrder() {
        try {
            // 1. 고객 ID 입력
            System.out.print("\n고객 ID 입력: ");
            int customerId = sc.nextInt();
            sc.nextLine();

            // 2. 식당 목록 출력 후 선택
            System.out.println("\n===== 식당 목록 =====");
            ResultSet restaurantRs = restaurantService.getRestaurantList();
            while (restaurantRs.next()) {
                System.out.printf("[%d] %s (%s)%n",
                        restaurantRs.getInt("restaurant_id"),
                        restaurantRs.getString("restaurant_name"),
                        restaurantRs.getString("category"));
            }
            System.out.print("식당 ID 선택: ");
            int restaurantId = sc.nextInt();
            sc.nextLine();

            // 3. 선택한 식당의 메뉴 목록 출력
            System.out.println("\n===== 메뉴 목록 =====");
            ResultSet menuRs = restaurantService.getMenuList(restaurantId);
            while (menuRs.next()) {
                System.out.printf("[%d] %s - %,.0f원%n",
                        menuRs.getInt("menu_id"),
                        menuRs.getString("menu_name"),
                        menuRs.getDouble("current_price"));
            }

            // 4. 메뉴 ID + 수량 입력 (0 입력 시 종료)
            // ordered_unit_price에 주문 시점 가격 스냅샷 저장 (REQ13 대응)
            List<int[]> orderItems = new ArrayList<>();
            double totalPrice = 0;

            while (true) {
                System.out.print("\n메뉴 ID 입력 (완료시 0): ");
                int menuId = sc.nextInt();
                sc.nextLine();
                if (menuId == 0) break;

                // 해당 식당의 메뉴인지 검증
                ResultSet validateRs = restaurantService.getMenuByIdAndRestaurant(menuId, restaurantId);
                if (!validateRs.next()) {
                    System.out.println("해당 식당의 메뉴가 아니에요. 다시 입력해주세요.");
                    continue;
                }

                System.out.print("수량 입력: ");
                int quantity = sc.nextInt();
                sc.nextLine();

                // 현재 가격 조회 후 스냅샷으로 저장
                double unitPrice = validateRs.getDouble("current_price");
                orderItems.add(new int[]{menuId, quantity, (int) unitPrice});
                totalPrice += unitPrice * quantity;
                System.out.printf("추가됨: %,.0f원 x %d개%n", unitPrice, quantity);
            }

            // 5. 배달 유형 선택
            System.out.println("\n===== 배달 유형 선택 =====");
            System.out.println("1. 한집배달");
            System.out.println("2. 알뜰배달");
            System.out.println("3. 가게배달");
            System.out.println("4. 무료배달");
            System.out.print("선택: ");
            int deliveryTypeChoice = sc.nextInt();
            sc.nextLine();

            String deliveryType = switch (deliveryTypeChoice) {
                case 1 -> "한집배달";
                case 2 -> "알뜰배달";
                case 3 -> "가게배달";
                case 4 -> "무료배달";
                default -> "한집배달";
            };

            // 무료배달은 거리 선택 불필요
            String distanceCategory = "전체";
            if (!deliveryType.equals("무료배달")) {
                System.out.println("\n===== 거리 선택 =====");
                System.out.println("1. 단거리  2. 중거리  3. 장거리");
                System.out.print("선택: ");
                int distanceChoice = sc.nextInt();
                sc.nextLine();
                distanceCategory = switch (distanceChoice) {
                    case 1 -> "단거리";
                    case 2 -> "중거리";
                    case 3 -> "장거리";
                    default -> "단거리";
                };
            }

            // 배달비 조회
            int deliveryFeeId = 0;
            double deliveryFee = 0;
            ResultSet feeRs = orderService.getDeliveryFee(deliveryType, distanceCategory);
            if (feeRs.next()) {
                deliveryFeeId = feeRs.getInt("delivery_fee_id");
                deliveryFee = feeRs.getDouble("fee");
                System.out.printf("배달비: %,.0f원%n", deliveryFee);
            }

            // 6. 쿠폰 코드 입력 (선택사항)
            // fixed: 정액 할인 / percent: 정률 할인
            System.out.print("\n쿠폰 코드 입력 (없으면 엔터): ");
            String couponCode = sc.nextLine().trim();

            int couponId = 0;
            double discountAmount = 0;

            if (!couponCode.isEmpty()) {
                ResultSet couponRs = couponService.getCoupon(couponCode);
                if (couponRs.next()) {
                    double minOrder = couponRs.getDouble("min_order_amount");
                    if (totalPrice >= minOrder) {
                        couponId = couponRs.getInt("coupon_id");
                        String discountType = couponRs.getString("discount_type");
                        double discountValue = couponRs.getDouble("discount_value");
                        // 할인 금액 계산
                        discountAmount = discountType.equals("fixed")
                                ? discountValue
                                : totalPrice * discountValue / 100;
                        System.out.printf("쿠폰 적용! 할인: %,.0f원%n", discountAmount);
                    } else {
                        System.out.printf("최소 주문금액 %,.0f원 미달%n", minOrder);
                    }
                } else {
                    System.out.println("유효하지 않은 쿠폰이에요.");
                }
            }

            // 7. 최종 금액 출력 및 주문 확인
            double finalPrice = totalPrice - discountAmount + deliveryFee;
            System.out.printf("%n상품 금액: %,.0f원 / 할인: %,.0f원 / 배달비: %,.0f원 / 최종: %,.0f원%n",
                    totalPrice, discountAmount, deliveryFee, finalPrice);
            System.out.print("주문하시겠습니까? (y/n): ");
            String confirm = sc.nextLine();

            if (!confirm.equalsIgnoreCase("y")) {
                System.out.println("주문이 취소됐어요.");
                return;
            }

            // 8. orders + order_item INSERT (트랜잭션)
            Order order = new Order(customerId, restaurantId, finalPrice,
                    "pending", deliveryFeeId == 0 ? null : deliveryFeeId, null,
                    couponId == 0 ? null : couponId, discountAmount);
            orderService.insertOrder(order, orderItems);

        } catch (SQLException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }

    /**
     * [REQ6] 주문 상세 조회
     * order_detail_view(VIEW)와 customer 테이블을 JOIN하여 조회
     * 고객 ID를 사용자 입력으로 받아 해당 고객의 주문 내역을 출력
     */
    private void selectOrderDetail() {
        try {
            System.out.print("\n고객 ID 입력: ");
            int customerId = sc.nextInt();
            sc.nextLine();

            ResultSet rs = orderService.getOrderDetail(customerId);

            System.out.println("\n고객명 | 등급   | 주문ID | 주문시각         | 상태      | 메뉴명     | 수량 | 단가    | 소계    | 배달비  | 배달유형 | 쿠폰 | 할인");
            System.out.println("----------------------------------------------------------------------------------------------------");
            while (rs.next()) {
                String couponName = rs.getString("coupon_name");
                couponName = (couponName == null) ? "없음" : couponName;

                System.out.printf("%-6s | %-6s | %-5d | %-16s | %-9s | %-10s | %3d | %,6.0f | %,6.0f | %,6.0f | %-8s | %-12s | %,.0f원%n",
                        rs.getString("customer_name"),
                        rs.getString("current_grade"),
                        rs.getInt("order_id"),
                        rs.getString("order_time"),
                        rs.getString("delivery_status"),
                        rs.getString("menu_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("ordered_unit_price"),
                        rs.getDouble("item_total"),
                        rs.getDouble("delivery_fee"),
                        rs.getString("delivery_type"),
                        couponName,
                        rs.getDouble("discount_amount"));
            }
        } catch (SQLException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }

    /**
     * [REQ7] 기간별 주문 통계
     * 시작일 ~ 종료일 입력받아 날짜별 주문 수, 총 금액을 GROUP BY로 집계
     */
    private void selectOrderStat() {
        try {
            System.out.print("\n시작 날짜 입력 (예: 2025-01-01): ");
            String startDate = sc.nextLine();
            System.out.print("종료 날짜 입력 (예: 2025-12-31): ");
            String endDate = sc.nextLine();

            ResultSet rs = orderService.getOrderStatByPeriod(startDate, endDate);

            System.out.println("\n날짜           | 주문수 | 금액");
            System.out.println("----------------------------------");
            while (rs.next()) {
                System.out.printf("%-14s | %4d  | %,.0f원%n",
                        rs.getString("order_date"),
                        rs.getInt("order_count"),
                        rs.getDouble("total_sales"));
            }
        } catch (SQLException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }

    /**
     * [REQ8] 배달 상태 변경
     * 주문 ID와 변경할 상태를 입력받아 orders 테이블의 delivery_status UPDATE
     * 트랜잭션 처리는 OrderService에서 담당
     */
    private void updateDeliveryStatus() {
        System.out.print("\n주문 ID 입력: ");
        int orderId = sc.nextInt();
        sc.nextLine();

        System.out.println("변경할 상태 선택");
        System.out.println("1. pending  2. delivering  3. delivered");
        System.out.print("선택: ");
        int choice = sc.nextInt();
        sc.nextLine();

        String status = switch (choice) {
            case 1 -> "pending";
            case 2 -> "delivering";
            case 3 -> "delivered";
            default -> null;
        };

        if (status == null) {
            System.out.println("잘못된 선택이에요.");
            return;
        }
        orderService.updateDeliveryStatus(orderId, status);
    }

    /**
     * [REQ9] 주문 취소
     * 주문 ID를 입력받아 order_item 먼저 삭제 후 orders 삭제
     * FK 제약조건으로 인해 order_item을 먼저 삭제해야 함
     * 트랜잭션 처리는 OrderService에서 담당
     */
    private void deleteOrder() {
        System.out.print("\n취소할 주문 ID 입력: ");
        int orderId = sc.nextInt();
        sc.nextLine();

        System.out.print("정말 취소하시겠습니까? (y/n): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            orderService.deleteOrder(orderId);
        } else {
            System.out.println("취소를 중단했어요.");
        }
    }
}