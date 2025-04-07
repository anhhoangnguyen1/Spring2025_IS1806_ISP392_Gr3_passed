package entity;

import java.sql.Date;
import lombok.Data;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderDetails {

    private int id;
    private Orders orderID;
    private Stores storeId;
    private Products productID;
    private String productName;
    private double importPrice;
    private double price;
    private double unitPrice;
    private int quantity;
    private String description;
    private Date createdAt;
    private String createdBy;
    private Date deleteAt;
    private String deleteBy;
    private boolean isDeleted;
    private Date updatedAt;
    private String status;
}
