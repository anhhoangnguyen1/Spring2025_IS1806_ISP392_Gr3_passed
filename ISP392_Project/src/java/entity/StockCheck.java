package entity;

import java.sql.Timestamp;
import java.util.Date;
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
public class StockCheck {

    private int stockCheckId;
    private int zoneId;
    private int productId;
    private Date checkedDate;
    private int actualQuantity;
    private int recordedQuantity;
    private int discrepancy;
    private String notes;
}
