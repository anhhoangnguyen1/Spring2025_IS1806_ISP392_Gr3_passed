package entity;

import entity.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ImportExportProduct {
    private int id;
    private String type;
    private LocalDateTime transactionDate;
    private int quantity;
    private BigDecimal price;
    private Invoice invoice;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private boolean isDelete;
    private String deletedBy;
    private LocalDateTime deletedAt;
    private String status;
    private List<ImportExportDetails> importExportDetails;
    private List<Debt> debts;
    private List<NumberOfBags> numberOfBags;

    // Getters and Setters

    public ImportExportProduct() {
    }

    public ImportExportProduct(int id, String type, LocalDateTime transactionDate, int quantity, BigDecimal price, Invoice invoice, String note, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, boolean isDelete, String deletedBy, LocalDateTime deletedAt, String status, List<ImportExportDetails> importExportDetails, List<Debt> debts, List<NumberOfBags> numberOfBags) {
        this.id = id;
        this.type = type;
        this.transactionDate = transactionDate;
        this.quantity = quantity;
        this.price = price;
        this.invoice = invoice;
        this.note = note;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.isDelete = isDelete;
        this.deletedBy = deletedBy;
        this.deletedAt = deletedAt;
        this.status = status;
        this.importExportDetails = importExportDetails;
        this.debts = debts;
        this.numberOfBags = numberOfBags;
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

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
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

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public boolean isIsDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
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

    public List<ImportExportDetails> getImportExportDetails() {
        return importExportDetails;
    }

    public void setImportExportDetails(List<ImportExportDetails> importExportDetails) {
        this.importExportDetails = importExportDetails;
    }

    public List<Debt> getDebts() {
        return debts;
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }

    public List<NumberOfBags> getNumberOfBags() {
        return numberOfBags;
    }

    public void setNumberOfBags(List<NumberOfBags> numberOfBags) {
        this.numberOfBags = numberOfBags;
    }
    
    
}
