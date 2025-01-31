package entity;


import entity.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Invoice {
    private int id;
    private LocalDateTime createdAt;
    private int quantity;
    private BigDecimal price;
    private BigDecimal payment;
    private BigDecimal total;
    private Customers customer;
    private Staffs staff;
    private List<InvoiceDetails> invoiceDetails;
    private List<PaymentHistory> paymentHistories;

    // Getters and Setters

    public Invoice() {
    }

    public Invoice(int id, LocalDateTime createdAt, int quantity, BigDecimal price, BigDecimal payment, BigDecimal total, Customers customer, Staffs staff, List<InvoiceDetails> invoiceDetails, List<PaymentHistory> paymentHistories) {
        this.id = id;
        this.createdAt = createdAt;
        this.quantity = quantity;
        this.price = price;
        this.payment = payment;
        this.total = total;
        this.customer = customer;
        this.staff = staff;
        this.invoiceDetails = invoiceDetails;
        this.paymentHistories = paymentHistories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Customers getCustomer() {
        return customer;
    }

    public void setCustomer(Customers customer) {
        this.customer = customer;
    }

    public Staffs getStaff() {
        return staff;
    }

    public void setStaff(Staffs staff) {
        this.staff = staff;
    }

    public List<InvoiceDetails> getInvoiceDetails() {
        return invoiceDetails;
    }

    public void setInvoiceDetails(List<InvoiceDetails> invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
    }

    public List<PaymentHistory> getPaymentHistories() {
        return paymentHistories;
    }

    public void setPaymentHistories(List<PaymentHistory> paymentHistories) {
        this.paymentHistories = paymentHistories;
    }
    
    
}
