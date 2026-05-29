package com.deliveryapp.model;

import java.sql.Timestamp;

public class Order {
    private int orderId;
    private int customerId;
    private int restaurantId;
    private double totalPrice;
    private Timestamp orderTime;
    private String deliveryStatus;
    private Integer deliveryFeeId;
    private Integer addressId;
    private Integer couponId;
    private double discountAmount;

    // 생성자
    public Order(int customerId, int restaurantId, double totalPrice,
                 String deliveryStatus, Integer deliveryFeeId,
                 Integer addressId, Integer couponId, double discountAmount) {
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.totalPrice = totalPrice;
        this.deliveryStatus = deliveryStatus;
        this.deliveryFeeId = deliveryFeeId;
        this.addressId = addressId;
        this.couponId = couponId;
        this.discountAmount = discountAmount;
    }
    // Getter / Setter
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public Timestamp getOrderTime() { return orderTime; }
    public void setOrderTime(Timestamp orderTime) { this.orderTime = orderTime; }

    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }

    public Integer getDeliveryFeeId() { return deliveryFeeId; }
    public void setDeliveryFeeId(Integer deliveryFeeId) { this.deliveryFeeId = deliveryFeeId; }

    public Integer getAddressId() { return addressId; }
    public void setAddressId(Integer addressId) { this.addressId = addressId; }

    public Integer getCouponId() { return couponId; }
    public void setCouponId(Integer couponId) { this.couponId = couponId; }

    public double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }
}

