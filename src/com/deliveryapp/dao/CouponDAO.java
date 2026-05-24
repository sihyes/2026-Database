package com.deliveryapp.dao;

import com.deliveryapp.common.DBUtil;

import java.sql.*;

public class CouponDAO {

    private Connection conn = DBUtil.getConnection();

    public ResultSet getCoupon(String couponCode) throws SQLException {
        String sql = "SELECT * FROM coupon WHERE coupon_name = ? AND expired_at > NOW()";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, couponCode);
        return pstmt.executeQuery();
    }

}
