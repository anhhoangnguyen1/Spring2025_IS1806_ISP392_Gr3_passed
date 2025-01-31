package entity;

import Model.*;
import java.time.LocalDateTime;

public class ProductZone {
    private int id;
    private Products product;
    private Zone zone;
    private int quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDelete;
    private LocalDateTime deletedAt;
    private String status;

    // Getters and Setters

    public ProductZone() {
    }

    public ProductZone(int id, Products product, Zone zone, int quantity, LocalDateTime createdAt, LocalDateTime updatedAt, boolean isDelete, LocalDateTime deletedAt, String status) {
        this.id = id;
        this.product = product;
        this.zone = zone;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDelete = isDelete;
        this.deletedAt = deletedAt;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isIsDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
