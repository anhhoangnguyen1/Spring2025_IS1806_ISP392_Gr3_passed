package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Staffs {
    private int id;
    private String name;
    private String phone;
    private String address;
    private String gender;
    private LocalDate dob;
    private Users user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
    private LocalDateTime deletedAt;
    private List<Invoice> invoices;

    // Getters and Setters

    public Staffs() {
    }

    public Staffs(int id, String name, String phone, String address, String gender, LocalDate dob, Users user, LocalDateTime createdAt, LocalDateTime updatedAt, String status, LocalDateTime deletedAt, List<Invoice> invoices) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.dob = dob;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.deletedAt = deletedAt;
        this.invoices = invoices;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
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

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }
    
}
