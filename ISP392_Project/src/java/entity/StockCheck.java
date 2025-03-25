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

    private int stockCheckId, zoneId, productId, actualQuantity, recordedQuantity, discrepancy;
    private Date checkedDate;
    private String notes;
}
