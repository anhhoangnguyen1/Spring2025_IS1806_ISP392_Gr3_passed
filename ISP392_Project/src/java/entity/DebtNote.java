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
    private String type;
    private BigDecimal amount;
    private String image;
    private String description;
    private int customer_id;  
    private String customerName; 
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String status;

  
    public DebtNote(String customerName, String type, BigDecimal amount, String imageFileName, String description, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String status) {
        this.customerName = customerName;
        this.type = type;
        this.amount = amount;
        this.image = imageFileName;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.status = status;
    }
}
