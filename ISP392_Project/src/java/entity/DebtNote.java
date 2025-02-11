package entity;

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

public class DebtNote {
    private int id;
    private String type;
    private double amount;
    private Customers customer;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String status;
    private String description;
    private String image;


    
}
