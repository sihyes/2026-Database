package com.deliveryapp.model;

import java.sql.Timestamp;

public class Customer {
    private int customerId;
    private String name;
    private int age;
    private String gender;
    private Integer regionId;
    private String currentGrade;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // 생성자
    public Customer(String name, int age, String gender, Integer regionId, String currentGrade) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.regionId = regionId;
        this.currentGrade = currentGrade;
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Integer getRegionId() { return regionId; }
    public void setRegionId(Integer regionId) { this.regionId = regionId; }

    public String getCurrentGrade() { return currentGrade; }
    public void setCurrentGrade(String currentGrade) { this.currentGrade = currentGrade; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

}
