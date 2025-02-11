package entity;

import entity.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    private int userId;
    private String role;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String avatar;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String createdBy;
    private boolean isDelete;
    private String deletedBy;
    private LocalDateTime deletedAt;
    private String status;
    private List<Staffs> staffs;

//    // Getters and Setters
//    public Users() {
//    }
//
//    public Users(int userId, String username, String password, String email, String phone, String avatar) {
//        this.userId = userId;
//        this.username = username;
//        this.password = password;
//        this.email = email;
//        this.phone = phone;
//        this.avatar = avatar;
//    }
//
//    public Users(int userId, Role role, String username, String password, String email, String phone, String avatar, LocalDateTime createdAt, LocalDateTime updatedAt, String updatedBy, String createdBy, boolean isDelete, String deletedBy, LocalDateTime deletedAt, String status, List<Staffs> staffs) {
//        this.userId = userId;
//        this.role = role;
//        this.username = username;
//        this.password = password;
//        this.email = email;
//        this.phone = phone;
//        this.avatar = avatar;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//        this.updatedBy = updatedBy;
//        this.createdBy = createdBy;
//        this.isDelete = isDelete;
//        this.deletedBy = deletedBy;
//        this.deletedAt = deletedAt;
//        this.status = status;
//        this.staffs = staffs;
//    }
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public Role getRole() {
//        return role;
//    }
//
//    public void setRole(Role role) {
//        this.role = role;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//    
//    public String getAvatar() {
//        return avatar;
//    }
//
//    public void setAvatar(String avatar) {
//        this.avatar = avatar;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public LocalDateTime getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(LocalDateTime updatedAt) {
//        this.updatedAt = updatedAt;
//    }
//
//    public String getUpdatedBy() {
//        return updatedBy;
//    }
//
//    public void setUpdatedBy(String updatedBy) {
//        this.updatedBy = updatedBy;
//    }
//
//    public String getCreatedBy() {
//        return createdBy;
//    }
//
//    public void setCreatedBy(String createdBy) {
//        this.createdBy = createdBy;
//    }
//
//    public boolean isIsDelete() {
//        return isDelete;
//    }
//
//    public void setIsDelete(boolean isDelete) {
//        this.isDelete = isDelete;
//    }
//
//    public String getDeletedBy() {
//        return deletedBy;
//    }
//
//    public void setDeletedBy(String deletedBy) {
//        this.deletedBy = deletedBy;
//    }
//
//    public LocalDateTime getDeletedAt() {
//        return deletedAt;
//    }
//
//    public void setDeletedAt(LocalDateTime deletedAt) {
//        this.deletedAt = deletedAt;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public List<Staffs> getStaffs() {
//        return staffs;
//    }
//
//    public void setStaffs(List<Staffs> staffs) {
//        this.staffs = staffs;
//    }
//
//    @Override
//    public String toString() {
//        return "Users{" + "userId=" + userId + ", role=" + role + ", username=" + username + ", password=" + password + ", email=" + email + ", phone=" + phone + ", avatar=" + avatar + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", updatedBy=" + updatedBy + ", createdBy=" + createdBy + ", isDelete=" + isDelete + ", deletedBy=" + deletedBy + ", deletedAt=" + deletedAt + ", status=" + status + ", staffs=" + staffs + '}';
//    }
}