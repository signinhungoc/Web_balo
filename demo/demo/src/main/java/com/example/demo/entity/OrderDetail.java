package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "orderdetails") // Đảm bảo tên bảng trùng với bảng hiện có trong cơ sở dữ liệu
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // Tên cột phải khớp với tên cột trong bảng
    private Integer id;

    @Column(name = "orderid", nullable = false)
    private Integer orderID; // Thay đổi để trùng với tên cột trong bảng

    @Column(name = "productid", nullable = false)
    private Integer productID;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    public String getPrice() {
        return price.stripTrailingZeros().toPlainString();
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
