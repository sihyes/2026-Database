package com.deliveryapp.service;

import com.deliveryapp.dao.CustomerDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CustomerService {

    private CustomerDAO customerDAO = new CustomerDAO();

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
}