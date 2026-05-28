package com.deliveryapp.service;

import com.deliveryapp.dao.CouponDAO;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CouponService {
    private CouponDAO couponDAO = new CouponDAO();

    public ResultSet getCoupon(String couponCode) throws SQLException {
        return couponDAO.getCoupon(couponCode);
    }
}