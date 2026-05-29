package com.deliveryapp.model;

import java.sql.Timestamp;

public class CustomerHistory {
    private int historyId;
    private int customerId;
    private Integer oldRegionId;
    private Integer newRegionId;
    private String oldGrade;
    private String newGrade;
    private Timestamp changedAt;


    public CustomerHistory(int customerId, Integer oldRegionId, Integer newRegionId,
                           String oldGrade, String newGrade) {
        this.customerId = customerId;
        this.oldRegionId = oldRegionId;
        this.newRegionId = newRegionId;
        this.oldGrade = oldGrade;
        this.newGrade = newGrade;
    }


    public int getHistoryId() { return historyId; }
    public void setHistoryId(int historyId) { this.historyId = historyId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public Integer getOldRegionId() { return oldRegionId; }
    public void setOldRegionId(Integer oldRegionId) { this.oldRegionId = oldRegionId; }

    public Integer getNewRegionId() { return newRegionId; }
    public void setNewRegionId(Integer newRegionId) { this.newRegionId = newRegionId; }

    public String getOldGrade() { return oldGrade; }
    public void setOldGrade(String oldGrade) { this.oldGrade = oldGrade; }

    public String getNewGrade() { return newGrade; }
    public void setNewGrade(String newGrade) { this.newGrade = newGrade; }

    public Timestamp getChangedAt() { return changedAt; }
    public void setChangedAt(Timestamp changedAt) { this.changedAt = changedAt; }
}