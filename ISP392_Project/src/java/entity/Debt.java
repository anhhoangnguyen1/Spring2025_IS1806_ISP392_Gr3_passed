package entity;

import entity.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Debt {
    private int id;
    private String type;
    private BigDecimal amount;
    private PaymentHistory paymentHistory;
    private ImportExportProduct importExportProduct;
    private Customers customer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String status;


    public Debt() {
    }

    public Debt(int id, String type, BigDecimal amount, PaymentHistory paymentHistory, ImportExportProduct importExportProduct, Customers customer, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String status) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.paymentHistory = paymentHistory;
        this.importExportProduct = importExportProduct;
        this.customer = customer;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.status = status;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentHistory getPaymentHistory() {
        return paymentHistory;
    }

    public void setPaymentHistory(PaymentHistory paymentHistory) {
        this.paymentHistory = paymentHistory;
    }

    public ImportExportProduct getImportExportProduct() {
        return importExportProduct;
    }

    public void setImportExportProduct(ImportExportProduct importExportProduct) {
        this.importExportProduct = importExportProduct;
    }

    public Customers getCustomer() {
        return customer;
    }

    public void setCustomer(Customers customer) {
        this.customer = customer;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
