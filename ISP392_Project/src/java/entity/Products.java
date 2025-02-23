package entity;

import java.math.BigDecimal;
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
    private int zone_id;
    private String description;
    private java.util.Date createdAt;
    private java.util.Date updatedAt;
    private boolean isDelete;
    private String status;
    
    public Products(int productId, String name, int quantity) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
    }
}
