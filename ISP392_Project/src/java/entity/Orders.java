package entity;

import java.util.Date;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Orders {

    private int id;
    private Customers customerID;
    private Users userID;
    private Stores storeId;
    private String type;
    private double amount;
    private double paidAmount;
    private String description;
    private java.sql.Date createdAt;
    private String createdBy;
    private java.sql.Date deleteAt;
    private String deleteBy;
    private boolean isDeleted;
    private java.sql.Date updatedAt;
    private String status;
}
