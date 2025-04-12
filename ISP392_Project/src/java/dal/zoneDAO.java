package dal;

import dal.DBContext;
import entity.Products;
import entity.Stores;
import entity.Zone;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class zoneDAO extends DBContext {

    // Lấy tất cả tên của zones
    public List<String> getAllZoneNames() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT name FROM Zones WHERE isDeleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching zone names: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public List<Zone> getAllZones() {
        List<Zone> zones = new ArrayList<>();
        String sql = "SELECT * FROM Zones";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Zone zone = Zone.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .createdBy(rs.getString("created_by"))
                        .status(rs.getString("status"))
                        .history(rs.getString("history") != null ? new JSONArray(rs.getString("history")) : null)
                        .build();
                zones.add(zone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zones;
    }

    // Lấy tất cả zones, hỗ trợ phân trang
    public List<Zone> viewAllZones(String sortBy, int index, Integer storeId) {
        List<Zone> zonesList = new ArrayList<>();
        String sql = "SELECT id, name, description, created_at, created_by, deletedAt, deleteBy, isDeleted, updated_at, store_id, status, product_id, history "
                + "FROM Zones WHERE isDeleted = 0 ";

        if (storeId != null) {
            sql += "AND store_id = ? ";
        }

        // Mặc định sắp xếp theo ID tăng dần
        if (sortBy == null || sortBy.trim().isEmpty()) {
            sortBy = "id";
        }

        sql += "ORDER BY " + sortBy + " ASC LIMIT 5 OFFSET ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            int paramIndex = 1;
            if (storeId != null) {
                st.setInt(paramIndex++, storeId);
            }
            st.setInt(paramIndex, (index - 1) * 5);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Zone zone = mapResultSetToZone(rs);
                    zonesList.add(zone);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return zonesList;
    }

    // Đếm số lượng zones
    public int countZones(String keyword, boolean showInactive, Integer storeId) {
        String sql = "SELECT COUNT(*) "
                + "FROM Zones z "
                + "LEFT JOIN Products p ON z.product_id = p.id "
                + "WHERE (LOWER(z.name) LIKE ? OR LOWER(p.name) LIKE ?) "
                + (storeId != null ? "AND z.store_id = ? " : "")
                + (showInactive ? "" : "AND z.status = 'Active'");
        try {
            connection = new DBContext().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);

            int paramIndex = 1;
            // Chuẩn hóa từ khóa tìm kiếm thành chữ thường và thêm ký tự % cho LIKE
            String searchKeyword = "%" + keyword.toLowerCase() + "%";
            ps.setString(paramIndex++, searchKeyword); // Tìm kiếm Zone Name
            ps.setString(paramIndex++, searchKeyword); // Tìm kiếm Product Name
            if (storeId != null) {
                ps.setInt(paramIndex++, storeId);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Tìm kiếm zones theo từ khóa, hỗ trợ phân trang và sắp xếp
    public List<Zone> searchZones(String keyword, int pageIndex, int pageSize, String sortBy, String sortOrder, boolean showInactive, Integer storeId) {
        List<Zone> list = new ArrayList<>();

        // Validate sortBy parameter
        List<String> allowedSortColumns = List.of("id", "name", "created_at", "updated_at");
        if (sortBy == null || !allowedSortColumns.contains(sortBy)) {
            sortBy = "id";
        }
        if (sortOrder == null || (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC"))) {
            sortOrder = "ASC";
        }

        // Escape keyword for LIKE clause
        String escapedKeyword = keyword != null ? keyword.replace("%", "\\%").replace("_", "\\_") : "";

        String sql = "SELECT z.id, z.name, z.description, z.created_at, z.created_by, z.deletedAt, z.deleteBy, z.isDeleted, z.updated_at, z.store_id, z.status, z.product_id, p.name AS product_name, z.history "
                + "FROM Zones z "
                + "LEFT JOIN Products p ON z.product_id = p.id "
                + "WHERE z.isDeleted = 0 ";

        if (escapedKeyword != null && !escapedKeyword.trim().isEmpty()) {
            sql += "AND (LOWER(z.name) LIKE ? OR LOWER(p.name) LIKE ? OR LOWER(z.description) LIKE ?) ";
        }

        if (!showInactive) {
            sql += "AND z.status = 'Active' ";
        }

        if (storeId != null) {
            sql += "AND z.store_id = ? ";
        }

        sql += "ORDER BY z." + sortBy + " " + sortOrder + " LIMIT ? OFFSET ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            int paramIndex = 1;

            if (escapedKeyword != null && !escapedKeyword.trim().isEmpty()) {
                String searchKeyword = "%" + escapedKeyword.toLowerCase() + "%";
                st.setString(paramIndex++, searchKeyword);
                st.setString(paramIndex++, searchKeyword);
                st.setString(paramIndex++, searchKeyword);
            }

            if (storeId != null) {
                st.setInt(paramIndex++, storeId);
            }

            st.setInt(paramIndex++, pageSize);
            st.setInt(paramIndex, (pageIndex - 1) * pageSize);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Zone zone = mapResultSetToZone(rs);
                    list.add(zone);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Zone getZoneById(int id) {
        String sql = "SELECT z.*, p.name AS product_name, z.history "
                + "FROM Zones z "
                + "LEFT JOIN Products p ON z.product_id = p.id "
                + "WHERE z.id = ? AND z.isDeleted = 0";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToZone(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertZone(Zone zone) {
        try {
            System.out.println("Starting to insert zone: " + zone.getName());
            connection.setAutoCommit(false);
            
            // Lấy ID lớn nhất hiện tại
            String maxIdSql = "SELECT MAX(id) FROM Zones WHERE isDeleted = 0";
            int maxId = 0;
            try (PreparedStatement st = connection.prepareStatement(maxIdSql)) {
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    maxId = rs.getInt(1);
                }
            }
            System.out.println("Current max zone ID: " + maxId);
            
            // Đẩy tất cả các zone hiện tại xuống một đơn vị
            String updateSql = "UPDATE Zones SET id = id + 1 WHERE isDeleted = 0 ORDER BY id DESC";
            try (PreparedStatement st = connection.prepareStatement(updateSql)) {
                int updatedRows = st.executeUpdate();
                System.out.println("Updated " + updatedRows + " existing zones");
            }
            
            // Thêm zone mới với ID = 1
            String insertSql = "INSERT INTO Zones (id, name, description, store_id, created_at, created_by, status, history) "
                    + "VALUES (1, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, '[]')";
            try (PreparedStatement st = connection.prepareStatement(insertSql)) {
                st.setString(1, zone.getName());
                if (zone.getDescription() != null) {
                    st.setString(2, zone.getDescription());
                } else {
                    st.setNull(2, java.sql.Types.VARCHAR);
                }
                if (zone.getStoreId() != null) {
                    st.setInt(3, zone.getStoreId().getId());
                } else {
                    st.setNull(3, java.sql.Types.INTEGER);
                }
                st.setString(4, zone.getCreatedBy());
                st.setString(5, zone.getStatus());
                int insertedRows = st.executeUpdate();
                System.out.println("Inserted new zone with ID 1. Rows affected: " + insertedRows);
            }
            
            connection.commit();
            System.out.println("Successfully committed zone insertion");
        } catch (SQLException e) {
            System.err.println("Error inserting zone: " + e.getMessage());
            try {
                connection.rollback();
                System.out.println("Rolled back zone insertion");
            } catch (SQLException rollbackEx) {
                System.err.println("Error rolling back: " + rollbackEx.getMessage());
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
                System.out.println("Reset auto-commit to true");
            } catch (SQLException autoCommitEx) {
                System.err.println("Error resetting auto-commit: " + autoCommitEx.getMessage());
                autoCommitEx.printStackTrace();
            }
        }
    }

    // Cập nhật zone
    public void updateZone(Zone zone) {
        String sql = "UPDATE Zones SET name = ?, description = ?, updated_at = CURRENT_TIMESTAMP, store_id = ?, status = ?, "
                + "product_id = ?, deletedAt = ?, deleteBy = ?, isDeleted = ?, history = ? WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, zone.getName());
            if (zone.getDescription() != null) {
                st.setString(2, zone.getDescription());
            } else {
                st.setNull(2, java.sql.Types.VARCHAR);
            }
            if (zone.getStoreId() != null) {
                st.setInt(3, zone.getStoreId().getId());
            } else {
                st.setNull(3, java.sql.Types.INTEGER);
            }
            st.setString(4, zone.getStatus());
            // Xử lý product_id
            if (zone.getProductId() != null) {
                st.setInt(5, zone.getProductId().getProductId());
            } else {
                st.setNull(5, java.sql.Types.INTEGER); // Set product_id thành NULL nếu null
            }
            if (zone.getDeleteAt() != null) {
                st.setDate(6, zone.getDeleteAt());
            } else {
                st.setNull(6, java.sql.Types.DATE);
            }
            if (zone.getDeleteBy() != null) {
                st.setString(7, zone.getDeleteBy());
            } else {
                st.setNull(7, java.sql.Types.VARCHAR);
            }
            st.setBoolean(8, zone.isDeleted());
            st.setString(9, zone.getHistory() != null ? zone.getHistory().toString() : "[]");
            st.setInt(10, zone.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getActiveZoneNames(int storeId) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT name FROM Zones WHERE status = 'Active' AND isDeleted = 0 AND store_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, storeId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching active zone names: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Ánh xạ ResultSet sang Zone
    private Zone mapResultSetToZone(ResultSet rs) throws SQLException {
        Stores store = null;
        int storeId = rs.getInt("store_id");
        if (!rs.wasNull()) {
            store = new Stores();
            store.setId(storeId);
        }

        Products product = null;
        int productId = rs.getInt("product_id");
        if (!rs.wasNull()) {
            product = new Products();
            product.setProductId(productId);
            product.setName(rs.getString("product_name"));
        }

        String historyJson = rs.getString("history");
        JSONArray history = (historyJson == null || historyJson.isEmpty()) ? new JSONArray() : new JSONArray(historyJson);
        List<Map<String, String>> historyList = new ArrayList<>();
        for (int i = 0; i < history.length(); i++) {
            JSONObject entry = history.getJSONObject(i);
            Map<String, String> historyEntry = new HashMap<>();
            historyEntry.put("id", entry.optString("id", String.valueOf(i + 1))); // Nếu không có id, dùng index tạm thời
            historyEntry.put("productName", entry.getString("productName"));
            historyEntry.put("startDate", entry.getString("startDate"));
            historyEntry.put("endDate", entry.optString("endDate", null));
            historyEntry.put("updatedBy", entry.optString("updatedBy", "N/A")); // Nếu không có, mặc định "N/A"
            historyList.add(historyEntry);
        }

        return Zone.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .createdAt(rs.getDate("created_at"))
                .createdBy(rs.getString("created_by"))
                .deleteAt(rs.getDate("deletedAt"))
                .deleteBy(rs.getString("deleteBy"))
                .isDeleted(rs.getBoolean("isDeleted"))
                .updatedAt(rs.getDate("updated_at"))
                .storeId(store)
                .status(rs.getString("status"))
                .productId(product)
                .history(history)
                .historyList(historyList) // Set the new field
                .build();
    }

    public String getProductNameById(int productId) {
        String productName = "N/A";
        String sql = "SELECT name FROM Product WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                productName = rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productName;
    }

    public void updateZoneHistory(Zone zone) {
        String sql = "UPDATE Zone SET history = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, zone.getHistory().toString());
            ps.setInt(2, zone.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra tên zone có tồn tại không
    public boolean checkNameExists(String name, int zoneId, Integer storeId) {
        String sql = "SELECT COUNT(*) FROM Zones WHERE name = ? AND id != ? AND isDeleted = 0";
        if (storeId != null) {
            sql += " AND store_id = ?";
        }
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, name);
            st.setInt(2, zoneId);
            if (storeId != null) {
                st.setInt(3, storeId);
            }
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Thêm bản ghi kiểm kho mới
    public void addStockCheck(int zoneId, int productId, int systemQuantity, int actualQuantity, String checkedBy, String note) throws SQLException {
        String sql = "INSERT INTO StockChecks (zoneId, productId, recordedQuantity, actualQuantity, discrepancy, checked_by, notes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, zoneId);
            ps.setInt(2, productId);
            ps.setInt(3, systemQuantity);
            ps.setInt(4, actualQuantity);
            ps.setInt(5, actualQuantity - systemQuantity);
            ps.setString(6, checkedBy);
            ps.setString(7, note != null ? note : null);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to insert stock check record.");
            }
        } catch (SQLException e) {
            System.err.println("Error adding stock check: " + e.getMessage());
            throw e;
        }
    }

    // Lấy lịch sử kiểm kho của một zone
    public List<Map<String, Object>> getStockCheckHistory(int zoneId) {
        List<Map<String, Object>> history = new ArrayList<>();
        String sql = "SELECT sc.stockCheckId, sc.productId, p.name AS product_name, sc.recordedQuantity, sc.actualQuantity, "
                + "sc.discrepancy, sc.checked_by, sc.checkedDate, sc.notes, sc.status "
                + "FROM StockChecks sc "
                + "LEFT JOIN Products p ON sc.productId = p.id "
                + "WHERE sc.zoneId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, zoneId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("check_id", rs.getInt("stockCheckId"));
                    entry.put("product_id", rs.getInt("productId"));
                    entry.put("product_name", rs.getString("product_name"));
                    entry.put("system_quantity", rs.getInt("recordedQuantity"));
                    entry.put("actual_quantity", rs.getInt("actualQuantity"));
                    entry.put("difference", rs.getInt("discrepancy"));
                    entry.put("checked_by", rs.getString("checked_by"));
                    entry.put("check_date", rs.getTimestamp("checkedDate") != null ? rs.getTimestamp("checkedDate").toString() : "N/A");
                    entry.put("note", rs.getString("notes"));
                    entry.put("status", rs.getString("status"));
                    history.add(entry);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    public void updateProductQuantity(int productId, int newQuantity) {
        String sql = "UPDATE Products SET quantity = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Thêm phương thức mới để escape input
    private String escapeInput(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("'", "''")
                    .replace("\\", "\\\\")
                    .replace("%", "\\%")
                    .replace("_", "\\_");
    }

    // Phương thức main để test
    public static void main(String[] args) {
        zoneDAO dao = new zoneDAO();

        // Test viewAllZones
        int pageIndex = 1;
        List<Zone> zones = dao.viewAllZones("id", pageIndex,1);

        if (zones.isEmpty()) {
            System.out.println("Không có zone nào được tìm thấy!");
        } else {
            System.out.println("Danh sách zones:");
            for (Zone zone : zones) {
                System.out.println("------------------------------------------------------");
                System.out.println("Zone ID: " + zone.getId());
                System.out.println("Name: " + zone.getName());
                System.out.println("Created By: " + zone.getCreatedBy());
                System.out.println("Created At: " + zone.getCreatedAt());
                System.out.println("Store ID: " + (zone.getStoreId() != null ? zone.getStoreId().getId() : "null"));
                System.out.println("Status: " + zone.getStatus());
                System.out.println("History: " + zone.getHistory());
            }
        }
    }
}
