package com.deliveryapp.service;

import com.deliveryapp.dao.CouponDAO;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CouponService {
    private CouponDAO couponDAO = new CouponDAO();

    // 쿠폰 코드로 쿠폰을 조회
    public ResultSet getCoupon(String couponCode) throws SQLException {
        return couponDAO.getCoupon(couponCode);
    }
}