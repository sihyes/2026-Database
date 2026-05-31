package com.deliveryapp.controller;

import com.deliveryapp.model.Customer;

import java.util.Scanner;

public class AppMenuController {
    private Scanner sc = new Scanner(System.in);

    private OrderController orderController = new OrderController(sc);
    private CustomerController customerController = new CustomerController();
    private ReviewController reviewController = new ReviewController(sc);
    private FoodMenuController foodMenuController = new FoodMenuController(sc);

    public void start() {
        while (true) {
            System.out.println("\n===== 배달 서비스 =====");
            System.out.println("1. 주문 메뉴");
            System.out.println("2. 고객 통계 분석 메뉴");
            System.out.println("3. 리뷰 메뉴");
            System.out.println("4. 메뉴/가격 관련");
            System.out.println("0. 종료");
            System.out.println("======================");


            System.out.print("메뉴를 선택하세요 : ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> orderController.showMenu();
                //case 2 -> customerController.showMenu();
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