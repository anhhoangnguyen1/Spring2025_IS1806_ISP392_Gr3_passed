/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.sql.Date;
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

public class Stores {

    private int id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private Date createdAt;  
    private String createdBy;
    private Date updatedAt;
    private Date deleteAt;
    private String deleteBy;
    private boolean isDeleted;
    private String status;

}
