package com.example.cakeshopper;

public class OrderModel {
    String url;
    String cakeName;
    String orderDate;
    String id;
    String orderTime;
    boolean status;



    public OrderModel(String cakeName, String orderDate, String orderTime, String id,boolean status) {
        this.orderTime=orderTime;
        this.cakeName = cakeName;
        this.orderDate = orderDate;
        this.id = id;
        this.status=status;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCakeName() {
        return cakeName;
    }

    public void setCakeName(String cakeName) {
        this.cakeName = cakeName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
