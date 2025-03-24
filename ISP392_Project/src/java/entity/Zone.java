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

public class Zone {

    private int id;
    private String name;
    private String description;
    private Date createdAt;
    private String createdBy;
    private Date deleteAt;
    private String deleteBy;
    private boolean isDeleted;
    private Date updatedAt;
    private Stores storeId;
    private String status;
    private Products productId; // Một Zone chỉ thuộc về một Product
}