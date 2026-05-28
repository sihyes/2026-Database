package com.deliveryapp.service;

import com.deliveryapp.common.DBUtil;
import com.deliveryapp.dao.OrderDAO;
import com.deliveryapp.model.Order;

import java.sql.*;
import java.util.List;

public class OrderService {
    private OrderDAO orderDAO = new OrderDAO();
    private Connection conn = DBUtil.getConnection();

    public int insertOrder(Order order, List<int[]> orderItems){
        try{
            conn.setAutoCommit(false);

            // 1. orders 테이블에 INSERT
            int orderId = orderDAO.insertOrder(order);
            if (orderId == -1) throw new SQLException("주문 등록 실패");

            // 2. order_item 테이블에 INSERT
            for (int[] item: orderItems){
                // item[0] = menuId, item[1] = quantity, item[2] = unitPrice
                orderDAO.insertOrderItem(orderId, item[0], item[1], item[2]);
            }

            conn.commit();
            System.out.println("주문 완료! 주문 ID: " + orderId);
            return orderId;
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("주문 실패!" + e.getMessage());
            return -1;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // 배달 상태 변경
    public void updateDeliveryStatus(int orderId, String status) {
        try{
            conn.setAutoCommit(false);
            orderDAO.updateDeliveryStatus(orderId, status);
            conn.commit();
            System.out.println("배달 상태 변경 완료");
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.out.println("변경 실패: "+e.getMessage());
        } finally {
            try {conn.setAutoCommit(true);} catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // 주문 취소
    public void deleteOrder(int orderId) {
        try{
            conn.setAutoCommit(false);
            // item 먼저 삭제 후 삭제
            orderDAO.deleteOrderItem(orderId);
            orderDAO.deleteOrder(orderId);
            conn.commit();
            System.out.println("주문 취소 완료");
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.out.println("취소 실패: "+e.getMessage());
        } finally {
            try {conn.setAutoCommit(true);} catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public ResultSet getOrderStatByPeriod(String startDate, String endDate) throws SQLException{
        return orderDAO.getOrderStatByPeriod(startDate, endDate);
    }
}
