package entity;

import java.math.BigDecimal;
import java.sql.Date;
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

public class Products {

    private int productId;
    private String name;
    private String image;
    private BigDecimal price;
    private int quantity;
    private Stores storeId;
    private String description;
    private Date createdAt;
    private String createdBy;
    private Date deleteAt;
    private String deleteBy;
    private boolean isDeleted;
    private Date updatedAt;
    private String status;
     private String zoneName;

    public Products(int productId, String name, String image, BigDecimal price, int quantity, String description, Date createdAt, String createdBy, Date deleteAt, String deleteBy, boolean isDeleted, Date updatedAt, String status, String zoneName) {
        this.productId = productId;
        this.name = name;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.deleteAt = deleteAt;
        this.deleteBy = deleteBy;
        this.isDeleted = isDeleted;
        this.updatedAt = updatedAt;
        this.status = status;
        this.zoneName = zoneName;
    }
   

     
    public Products(int productId, String name, String image, BigDecimal price, int quantity, String description, Date createdAt, String createdBy, Date deleteAt, String deleteBy, boolean isDeleted, Date updatedAt, String status) {
        this.productId = productId;
        this.name = name;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.deleteAt = deleteAt;
        this.deleteBy = deleteBy;
        this.isDeleted = isDeleted;
        this.updatedAt = updatedAt;
        this.status = status;
    }

    public Products(int productId, String name, int quantity) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
    }

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
