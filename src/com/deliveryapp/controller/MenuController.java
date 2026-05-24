package com.deliveryapp.controller;

import java.util.Scanner;

public class MenuController {
    private Scanner sc = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n===== 배달 서비스 =====");
            System.out.println("1. 주문하기");
            System.out.println("0. 종료");
            System.out.print("선택: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 0 -> {
                    System.out.println("종료합니다.");
                    return;
                }
                default -> System.out.println("없는 메뉴예요.");
            }
        }
    }
}