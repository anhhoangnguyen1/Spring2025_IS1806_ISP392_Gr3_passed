/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author THC
 */
import entity.Orders;
import entity.OrderDetails;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTask {

    private Orders order;
    private List<OrderDetails> orderDetails;
    private String balanceAction; // 👈 Thêm dòng này

}
