package entity;

import java.util.Date;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Orders {
    private Integer orderID;
    private Date orderDate;
    private Integer totalAmount;
    private Integer customerID;
    private Integer employeeID;
}