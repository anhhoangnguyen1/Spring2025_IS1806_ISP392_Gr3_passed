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
    private int zone_id;
    private Stores storeId;
    private String description;
    private Date createdAt;
    private String createdBy;
    private Date deleteAt;
    private String deleteBy;
    private boolean isDeleted;
    private Date updatedAt;
    private String status;
    
}
