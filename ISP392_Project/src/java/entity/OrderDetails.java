package entity;

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
    private Integer orderDetailID;
    private Integer quantity;
    private Integer price;
    private Integer orderID;
    private Integer productID;
}
