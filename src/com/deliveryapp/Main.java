package com.deliveryapp;

import com.deliveryapp.common.DBUtil;
import com.deliveryapp.controller.AppMenuController;

public class Main {
    public static void main(String[] args) {
        // DB 연결 테스트
        DBUtil.getConnection();

        // 메뉴 시작
        AppMenuController controller = new AppMenuController();
        controller.start();

        // 종료 시 연결 닫기
        DBUtil.closeConnection();
    }
}