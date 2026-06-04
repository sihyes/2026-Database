package com.deliveryapp.dao;

import com.deliveryapp.common.DBUtil;

import java.sql.*;

public class CouponDAO {

    private Connection conn = DBUtil.getConnection();

    /**
     * 쿠폰 코드로 유효한 쿠폰을 조회한다.
     * 만료되지 않은 쿠폰만 반환한다. (expired_at > NOW())
     * @param couponCode 사용자가 입력한 쿠폰 코드 (coupon_name)
     * @return 쿠폰 정보 ResultSet (coupon_id, discount_type, discount_value, min_order_amount 포함)
     */
    public ResultSet getCoupon(String couponCode) throws SQLException {
        String sql = "SELECT * FROM coupon WHERE coupon_name = ? AND expired_at > NOW()";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, couponCode);
        return pstmt.executeQuery();
    }

}
