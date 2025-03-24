package entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.json.JSONArray;

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
    private JSONArray history; // Thêm thuộc tính để lưu lịch sử JSON
    private List<Map<String, String>> historyList; // New field for JSP
    private List<Map<String, String>> nameHistory;

    public List<Map<String, String>> getNameHistoryList() {
        return nameHistory != null ? nameHistory : new ArrayList<>();
    }
}
