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
public class Users {

    private int id;
    private String username;
    private String password;
    private String image;
    private String name;
    private String phone;
    private String address;
    private String gender;
    private Date dob;
    private String role;
    private String email;
    private Stores storeId;
    private Date createdAt;    
    private String createdBy;
    private Date deleteAt;
    private String deleteBy;
    private boolean isDeleted;    
    private Date updatedAt;
    private String status;
    private List<Invoice> invoices;
    
}
