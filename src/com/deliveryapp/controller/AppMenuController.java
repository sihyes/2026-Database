package com.deliveryapp.controller;

import com.deliveryapp.model.Customer;

import java.util.Scanner;

/**
 * 애플리케이션 메인 메뉴를 처리하는 컨트롤러
 * 각 기능별 Controller를 호출하는 진입점 역할
 */
public class AppMenuController {
    private Scanner sc = new Scanner(System.in);

    // 각 기능별 컨트롤러 초기화
    private OrderController orderController = new OrderController(sc);
    private CustomerController customerController = new CustomerController(sc);
    private ReviewController reviewController = new ReviewController(sc);
    private FoodMenuController foodMenuController = new FoodMenuController(sc);

    // 메인 메뉴를 출력하고 사용자 입력에 따라 각 컨트롤러를 호출
    // 0 입력 시 애플리케이션을 종료한다.
    public void start() {
        while (true) {
            System.out.println("\n===== 배달 서비스 =====");
            System.out.println("1. 주문 관리");
            System.out.println("2. 고객 관리");
            System.out.println("3. 리뷰 관리");
            System.out.println("4. 식당/메뉴 관리");
            System.out.println("0. 종료");
            System.out.println("======================");


            System.out.print("메뉴를 선택하세요 : ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> orderController.showMenu();
                case 2 -> customerController.showMenu();
                case 3 -> reviewController.showMenu();
                case 4 -> foodMenuController.showMenu();
                case 0 -> {
                    System.out.println("종료합니다.");
                    return;
                }
                default -> System.out.println("다시 선택하세요.");

            }
        }
    }
}