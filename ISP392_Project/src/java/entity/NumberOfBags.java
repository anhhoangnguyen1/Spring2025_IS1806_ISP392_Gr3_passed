package entity;

import Model.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class NumberOfBags {
    private int id;
    private String type;
    private int quantity;
    private BigDecimal price;
    private ImportExportProduct importExportProduct;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String note;

    // Getters and Setters

    public NumberOfBags() {
    }

    public NumberOfBags(int id, String type, int quantity, BigDecimal price, ImportExportProduct importExportProduct, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String note) {
        this.id = id;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.importExportProduct = importExportProduct;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ImportExportProduct getImportExportProduct() {
        return importExportProduct;
    }

    public void setImportExportProduct(ImportExportProduct importExportProduct) {
        this.importExportProduct = importExportProduct;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
}
