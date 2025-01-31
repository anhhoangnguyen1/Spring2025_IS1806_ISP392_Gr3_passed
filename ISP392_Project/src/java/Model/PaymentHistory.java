package Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PaymentHistory {
    private int id;
    private String note;
    private Invoice invoice;
    private BigDecimal totalInvoice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
    private List<Debt> debts;

    public PaymentHistory() {
    }

    public PaymentHistory(int id, String note, Invoice invoice, BigDecimal totalInvoice, LocalDateTime createdAt, LocalDateTime updatedAt, String status, List<Debt> debts) {
        this.id = id;
        this.note = note;
        this.invoice = invoice;
        this.totalInvoice = totalInvoice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.debts = debts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public BigDecimal getTotalInvoice() {
        return totalInvoice;
    }

    public void setTotalInvoice(BigDecimal totalInvoice) {
        this.totalInvoice = totalInvoice;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Debt> getDebts() {
        return debts;
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }

   
}
