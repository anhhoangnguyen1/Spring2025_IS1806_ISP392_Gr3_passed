package entity;

import entity.*;
import java.time.LocalDateTime;
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

    private int userId;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String address;
    private String gender;
    private Date dob;
    private String role;
    private String email;
    private Date createdAt;
    private Date updatedAt;
    private String status;
    private Date deletedAt;
    private List<Invoice> invoices;
    private String avatar;
}
