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
public class productZone {
    private int id;
    private int productId;
    private int zoneId;
    private int quantity;
    private Timestamp createdAt;
    private Timestamp updateAt;

    public productZone(int id, int productId, int zoneId, int quantity, Timestamp createdAt, Timestamp updateAt) {
        this.id = id;
        this.productId = productId;
        this.zoneId = zoneId;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Timestamp updateAt) {
        this.updateAt = updateAt;
    }

    @Override
    public String toString() {
        return "productZone{" + "id=" + id + ", productId=" + productId + ", zoneId=" + zoneId + ", quantity=" + quantity + ", createdAt=" + createdAt + ", updateAt=" + updateAt + '}';
    }
    
}
