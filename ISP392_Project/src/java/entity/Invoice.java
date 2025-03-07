package entity;

import java.sql.Date;
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

public class Invoice {

    private int id;
    private String type;
    private double total;
    private double payment;
    private Customers customer;
    private Users user;
    private Stores storeId;
    private Date createdAt;
    private String createdBy;
    private Date deleteAt;
    private String deleteBy;
    private boolean isDeleted;

}
