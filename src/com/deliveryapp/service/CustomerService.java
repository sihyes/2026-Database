package com.deliveryapp.service;

import com.deliveryapp.common.DBUtil;
import com.deliveryapp.dao.CustomerDAO;

import java.sql.*;

public class CustomerService {

    private CustomerDAO customerDAO = new CustomerDAO();
    private Connection conn = DBUtil.getConnection();

    // 고객 조회
    public ResultSet getCustomer(int customerId) throws SQLException {
        return customerDAO.getCustomer(customerId);
    }

    // 고객 변경 이력 조회
    public ResultSet getCustomerHistory(int customerId) throws SQLException {
        return customerDAO.getCustomerHistory(customerId);
    }

    // REQ14 - 변경 전 매출
    public ResultSet getOrderStatBeforeChange(int customerId, Timestamp changedAt) throws SQLException {
        return customerDAO.getOrderStatBeforeChange(customerId, changedAt);
    }

    // REQ14 - 변경 후 매출
    public ResultSet getOrderStatAfterChange(int customerId, Timestamp changedAt) throws SQLException {
        return customerDAO.getOrderStatAfterChange(customerId, changedAt);
    }

    // 지역 변경 + 이력 저장
    public void updateCustomerRegion(int customerId, int oldRegionId, int newRegionId, String currentGrade, String newAddress) {
        try {
            conn.setAutoCommit(false);
            customerDAO.updateCustomerRegion(customerId, newRegionId);
            customerDAO.insertCustomerHistory(customerId, oldRegionId, newRegionId, currentGrade, currentGrade);
            customerDAO.resetDefaultAddress(customerId);
            customerDAO.insertDeliveryAddress(customerId, newAddress);
            conn.commit();
            System.out.println("지역 및 배달 주소 변경 완료!");
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.out.println("변경 실패: " + e.getMessage());
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // 등급 변경 + 이력 저장
    public void updateCustomerGrade(int customerId, String oldGrade, String newGrade, Integer currentRegionId) {
        try {
            conn.setAutoCommit(false);
            customerDAO.updateCustomerGrade(customerId, newGrade);
            customerDAO.insertCustomerHistory(customerId, currentRegionId, currentRegionId, oldGrade, newGrade);
            conn.commit();
            System.out.println("등급 변경 완료!");
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.out.println("변경 실패: " + e.getMessage());
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // 지역별 매출 분석
    public ResultSet getOrderStatByRegion() throws SQLException {
        return customerDAO.getOrderStatByRegion();
    }

    // 등급별 매출 분석
    public ResultSet getOrderStatByGrade() throws SQLException {
        return customerDAO.getOrderStatByGrade();
    }



}