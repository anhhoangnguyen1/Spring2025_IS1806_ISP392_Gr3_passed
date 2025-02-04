package entity;

import java.math.BigDecimal;

public class InvoiceDetails {
    private int id;
    private Invoice invoice;
    private Products product;
    private int quantity;
    private BigDecimal price;

    // Getters and Setters

    public InvoiceDetails() {
    }

    public InvoiceDetails(int id, Invoice invoice, Products product, int quantity, BigDecimal price) {
        this.id = id;
        this.invoice = invoice;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
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
    
    
}
