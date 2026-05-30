package com.deliveryapp.controller;

import com.deliveryapp.service.RestaurantService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;

public class FoodMenuController {
    private Scanner sc;
    private RestaurantService restaurantService = new RestaurantService();

    public FoodMenuController(Scanner sc) {
        this.sc = sc;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n===== 메뉴/가격 관련 =====");
            System.out.println("1. 메뉴 가격 변경");
            System.out.println("2. 가격 변경 전후 매출");
            System.out.println("0. 이전 메뉴");
            System.out.print("선택: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> updateMenuPrice();
                case 2 -> selectSalesBeforeAfter();
                case 0 -> { return; }
                default -> System.out.println("없는 메뉴예요.");
            }
        }
    }

    private void updateMenuPrice() {
        try {
            System.out.print("\n메뉴 ID 입력: ");
            int menuId = sc.nextInt();
            sc.nextLine();

            ResultSet info = restaurantService.getMenuInfo(menuId);
            if (!info.next()) {
                System.out.println("해당 메뉴가 없습니다.");
                return;
            }
            int oldPrice = info.getInt("current_price");
            String menuName = info.getString("menu_name");

            System.out.printf("현재 가격: %,.0f원 (%s)%n", (double) oldPrice, menuName);
            System.out.print("변경할 가격 입력: ");
            int newPrice = sc.nextInt();
            sc.nextLine();

            if (newPrice <= 0) {
                System.out.println("가격은 0보다 커야 합니다.");
                return;
            }
            if (newPrice == oldPrice) {
                System.out.println("현재 가격과 동일합니다.");
                return;
            }

            restaurantService.updateMenuPrice(menuId, oldPrice, newPrice);
        } catch (SQLException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }

    private void selectSalesBeforeAfter() {
        try {
            System.out.print("\n메뉴 ID 입력: ");
            int menuId = sc.nextInt();
            sc.nextLine();

            System.out.print("기준 변경 시각 입력 (예: 2025-03-01 09:00:00): ");
            String changedAtStr = sc.nextLine();
            Timestamp changedAt = Timestamp.valueOf(changedAtStr);

            ResultSet rs = restaurantService.getSalesBeforeAfterPriceChange(menuId, changedAt);
            if (rs.next()) {
                double before = rs.getDouble("sales_before");
                double after = rs.getDouble("sales_after");
                System.out.printf("변경 전 매출: %,.0f원%n", before);
                System.out.printf("변경 후 매출: %,.0f원%n", after);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("날짜 형식이 올바르지 않습니다.");
        } catch (SQLException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }
}
