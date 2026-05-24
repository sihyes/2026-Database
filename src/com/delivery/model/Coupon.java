package com.delivery.model;

import java.time.LocalDateTime;

public class Coupon {
    private int couponId;
    private String couponName;
    private String discountType;
    private int discountValue;
    private int minOrderAmount;
    private LocalDateTime expiredAt;

    public Coupon() {}

    // Getter/Setter (Alt+Insert로 생성 가능)
    public int getCouponId() { return couponId; }
    public void setCouponId(int couponId) { this.couponId = couponId; }
    public String getCouponName() { return couponName; }
    public void setCouponName(String couponName) { this.couponName = couponName; }
    public String getDiscountValue() { return "FIXED".equals(discountType) ? String.valueOf(discountValue) : ""; }
    public int getDiscountValueInt() { return discountValue; }
    public void setDiscountValue(int discountValue) { this.discountValue = discountValue; }
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
    public int getMinOrderAmount() { return minOrderAmount; }
    public void setMinOrderAmount(int minOrderAmount) { this.minOrderAmount = minOrderAmount; }
    public LocalDateTime getExpiredAt() { return expiredAt; }
    public void setExpiredAt(LocalDateTime expiredAt) { this.expiredAt = expiredAt; }
}