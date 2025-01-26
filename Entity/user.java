package Entity;

import java.sql.Timestamp;

public class user {
    private int id;
    private int id_role;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String createdBy;
    private boolean isDelete;
    private String deletedBy;
    private Timestamp deletedAt;
    private String status;

    public user(int id, int id_role, String username, String password, String email, String phone, Timestamp createdAt, Timestamp updatedAt, String createdBy, boolean isDelete, String deletedBy, Timestamp deletedAt, String status) {
        this.id = id;
        this.id_role = id_role;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.isDelete = isDelete;
        this.deletedBy = deletedBy;
        this.deletedAt = deletedAt;
        this.status = status;
    }


    

    // Getters and Setters
    public int getId() {
        return id;
    }

 

    public void setId(int id) {
        this.id = id;
    }
   public int getId_role() {
        return id_role;
    }

    public void setId_role(int id_role) {
        this.id_role = id_role;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", createdBy='" + createdBy + '\'' +
                ", isDelete=" + isDelete +
                ", deletedBy='" + deletedBy + '\'' +
                ", deletedAt=" + deletedAt +
                ", status='" + status + '\'' +
                '}';
    }
}
