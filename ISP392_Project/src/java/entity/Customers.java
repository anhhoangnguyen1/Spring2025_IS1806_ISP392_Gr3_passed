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

public class Customers {

    private int id;
    private String name;
    private String phone;
    private String address;
    private double balance;
    private Date createdAt;
    private String createdBy;
    private Date deleteAt;
    private String deleteBy;
    private boolean isDeleted;
    private Date updatedAt;
    private String updateBy;
    private Stores storeId;
    private String status;
    private List<Invoice> invoices;
    private List<DebtNote> debtNotes;

}
