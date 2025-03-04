package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private int debt_note_id;
    private String type;
    private BigDecimal amount;
    private String image;
    private String description;
    private int customer_id;
    private String customers_name;
    private String phone;
    private String address;
    private String debtorName;
    private String debtorAddress;
    private String debtorPhone;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String status;

    public DebtNote(int id,int debt_note_id, String type, BigDecimal amount, String image, String description, int customer_id, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String status) {
        this.id = id;
        this.debt_note_id=debt_note_id;
        this.type = type;
        this.amount = amount;
        this.image = image;
        this.description = description;
        this.customer_id = customer_id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.status = status;
    }
    public DebtNote(int id, String type, BigDecimal amount, String image, String description, int customer_id, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String status) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.image = image;
        this.description = description;
        this.customer_id = customer_id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.status = status;
    }

    public DebtNote(int id, String type, BigDecimal amount, String image, String description, String debtorName, String debtorAddress, String debtorPhone, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String status) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.image = image;
        this.description = description;
        this.debtorName = debtorName;
        this.debtorAddress = debtorAddress;
        this.debtorPhone = debtorPhone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.status = status;
    }

    public DebtNote(int id, String type, BigDecimal amount, String image, String description, int customer_id, String customers_name, String phone, String address, String debtorName, String debtorAddress, String debtorPhone, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String status) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.image = image;
        this.description = description;
        this.customer_id = customer_id;
        this.customers_name = customers_name;
        this.phone = phone;
        this.address = address;
        this.debtorName = debtorName;
        this.debtorAddress = debtorAddress;
        this.debtorPhone = debtorPhone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.status = status;
    }

    

}
