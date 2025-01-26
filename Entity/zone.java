/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.sql.Timestamp;

/**
 *
 * @author phamh
 */
public class zone {
    private int id;
    private String name;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String createdBy;
    private boolean isDelete;
    private String deletedBy;
    private Timestamp deletedAt;
    private String status;

    public zone(int id, String name, Timestamp createdAt, Timestamp updatedAt, String createdBy, boolean isDelete, String deletedBy, Timestamp deletedAt, String status) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.isDelete = isDelete;
        this.deletedBy = deletedBy;
        this.deletedAt = deletedAt;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public boolean isIsDelete() {
        return isDelete;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Zone{" + "id=" + id + ", name=" + name + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", createdBy=" + createdBy + ", isDelete=" + isDelete + ", deletedBy=" + deletedBy + ", deletedAt=" + deletedAt + ", status=" + status + '}';
    }
    
}
