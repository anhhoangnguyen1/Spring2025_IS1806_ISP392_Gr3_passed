package entity;

import entity.*;
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
    private int id;
    private String name;
    private String image;
    private BigDecimal price;
    private int quantity;
    private int zone_id;
    private String description;
    private Date created_at;
    private Date updated_at;
    private boolean isDeleted;
    private Date deletedAt;
    private String status;

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
