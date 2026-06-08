package com.deliveryapp;

import com.deliveryapp.common.DBUtil;
import com.deliveryapp.controller.AppMenuController;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== DB 연결 설정 =====");
        System.out.print("Host (예: localhost): ");
        String host = sc.nextLine();

        System.out.print("Port (예: 3306): ");
        String port = sc.nextLine();

        System.out.print("Database 이름: ");
        String database = sc.nextLine();

        System.out.print("User: ");
        String user = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        DBUtil.init(host, port, database, user, password);

        // DB 연결 테스트
        DBUtil.getConnection();

        // 메뉴 시작
        AppMenuController controller = new AppMenuController();
        controller.start();

        // 종료 시 연결 닫기
        DBUtil.closeConnection();
    }
}