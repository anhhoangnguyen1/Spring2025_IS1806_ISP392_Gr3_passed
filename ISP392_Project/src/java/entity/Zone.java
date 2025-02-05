package entity;

import entity.*;
import java.time.LocalDateTime;
import java.util.List;

public class Zone {
    private int id;
    private String name;
    private int capacity;
    private int remainCapacity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDelete;
    private LocalDateTime deletedAt;
    private String status;
    private List<ProductZone> productZones;

    // Getters and Setters

    public Zone() {
    }

    public Zone(int id, String name, int capacity, int remainCapacity, LocalDateTime createdAt, LocalDateTime updatedAt, boolean isDelete, LocalDateTime deletedAt, String status, List<ProductZone> productZones) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.remainCapacity = remainCapacity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDelete = isDelete;
        this.deletedAt = deletedAt;
        this.status = status;
        this.productZones = productZones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getRemainCapacity() {
        return remainCapacity;
    }

    public void setRemainCapacity(int remainCapacity) {
        this.remainCapacity = remainCapacity;
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

    public List<ProductZone> getProductZones() {
        return productZones;
    }

    public void setProductZones(List<ProductZone> productZones) {
        this.productZones = productZones;
    }
    
}
