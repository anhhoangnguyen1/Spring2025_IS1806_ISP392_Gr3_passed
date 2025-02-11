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
    private Date createdAt;
    private String name;
    private String type;
    private Date transactionDate;
    private int quantity;
    private int weight;
    private double unitPrice;
    private double payment;
    private double total;
    private Customers customer;
    private Users user;
    private List<InvoiceDetail> invoiceDetails;

 
}
