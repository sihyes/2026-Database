package com.deliveryapp.controller;

import java.util.Scanner;

public class AppMenuController {
    private Scanner sc = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n===== 배달 서비스 =====");
            System.out.println("1. 주문 관련");
            System.out.println("2. 고객 분석");
            System.out.println("3. 리뷰 관련");
            System.out.println("4. 메뉴/가격 관련");
            System.out.println("0. 종료");

            switch (choice) {
                case 1 -> orderController.showMenu();
                case 2 -> customerController.showMenu();
                case 3 -> reviewController.showMenu();
                case 4 -> menuItemController.showMenu();
            }
        }
    }
}