package com.delivery;

import com.delivery.controller.OrderController;

class Main{
    public static void main(String[] args) {
        System.out.println("=== 배달 앱 시스템 시작 ===");

        // 컨트롤러를 실행하여 전체적인 배달 앱 서비스 흐름을 시작.
        OrderController orderController = new OrderController();
        orderController.startOrderFlow();
    }
}