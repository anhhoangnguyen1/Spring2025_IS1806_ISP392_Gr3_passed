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
    public List<Zone> viewAllZones(String sortBy, int index) {
        List<Zone> zonesList = new ArrayList<>();
        String sql = "SELECT id, name, description, created_at, created_by, deletedAt, deleteBy, isDeleted, updated_at, store_id, status, product_id, history "
                + "FROM Zones WHERE isDeleted = 0 "
                + "ORDER BY " + (sortBy != null ? sortBy : "id") + " LIMIT 5 OFFSET ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, (index - 1) * 5);
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
    public int countZones(String keyword) {
        String sql;
        if (keyword == null || keyword.trim().isEmpty()) {
            sql = "SELECT COUNT(*) FROM Zones WHERE isDeleted = 0";
            try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            sql = "SELECT COUNT(*) FROM Zones WHERE name LIKE ? AND isDeleted = 0";
            try (PreparedStatement st = connection.prepareStatement(sql)) {
                st.setString(1, "%" + keyword + "%");
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    // Tìm kiếm zones theo từ khóa, hỗ trợ phân trang và sắp xếp
    public List<Zone> searchZones(String keyword, int pageIndex, int pageSize, String sortBy, String sortOrder) {
        List<Zone> list = new ArrayList<>();

        List<String> allowedSortColumns = List.of("id", "name", "created_at", "updated_at");
        if (sortBy == null || !allowedSortColumns.contains(sortBy)) {
            sortBy = "id";
        }
        if (sortOrder == null || (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC"))) {
            sortOrder = "ASC";
        }

        String sql = "SELECT z.id, z.name, z.description, z.created_at, z.created_by, z.deletedAt, z.deleteBy, z.isDeleted, z.updated_at, z.store_id, z.status, z.product_id, p.name AS product_name, z.history "
                + "FROM Zones z "
                + "LEFT JOIN Products p ON z.product_id = p.id "
                + "WHERE z.isDeleted = 0 ";

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += "AND z.name LIKE ? ";
        }

        sql += "ORDER BY z." + sortBy + " " + sortOrder + " LIMIT ? OFFSET ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            int paramIndex = 1;

            if (keyword != null && !keyword.trim().isEmpty()) {
                st.setString(paramIndex++, "%" + keyword + "%");
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
        String sql = "INSERT INTO Zones (name, description, store_id, created_at, created_by, status, history) "
                + "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?, '[]')";
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
            st.setString(4, zone.getCreatedBy());
            st.setString(5, zone.getStatus());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật zone
    public void updateZone(Zone zone) {
        String sql = "UPDATE Zones SET name = ?, description = ?, updated_at = CURRENT_TIMESTAMP, store_id = ?, status = ?, "
                + "deletedAt = ?, deleteBy = ?, isDeleted = ?, history = ? WHERE id = ?";
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
            if (zone.getDeleteAt() != null) {
                st.setDate(5, zone.getDeleteAt());
            } else {
                st.setNull(5, java.sql.Types.DATE);
            }
            if (zone.getDeleteBy() != null) {
                st.setString(6, zone.getDeleteBy());
            } else {
                st.setNull(6, java.sql.Types.VARCHAR);
            }
            st.setBoolean(7, zone.isDeleted());
            st.setString(8, zone.getHistory() != null ? zone.getHistory().toString() : "[]");
            st.setInt(9, zone.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    // Phương thức main để test
    public static void main(String[] args) {
        zoneDAO dao = new zoneDAO();

        // Test viewAllZones
        int pageIndex = 1;
        List<Zone> zones = dao.viewAllZones("id", pageIndex);

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
//        
//        System.out.println("--------------------------------------");
//        // 1. Test getAllZoneNames
//        System.out.println("=== Test getAllZoneNames ===");
//        List<String> zoneNames = dao.getAllZoneNames();
//        if (zoneNames.isEmpty()) {
//            System.out.println("Không có tên zone nào!");
//        } else {
//            System.out.println("Danh sách tên zones:");
//            for (String name : zoneNames) {
//                System.out.println("- " + name);
//            }
//        }
//        System.out.println("--------------------------------------");
//        // 3. Test countZones
//        System.out.println("=== Test countZones ===");
//        int totalZones = dao.countZones(null);
//        System.out.println("Tổng số zones: " + totalZones);
//        int filteredZones = dao.countZones("Gạo ST");
//        System.out.println("Số zones chứa 'Gạo ST': " + filteredZones);
//        System.out.println("--------------------------------------");
//
//        // 4. Test searchZones
//        System.out.println("=== Test searchZones ===");
//        List<Zone> searchResults = dao.searchZones("Gạo", 1, 5, "name", "ASC");
//        if (searchResults.isEmpty()) {
//            System.out.println("Không tìm thấy zone nào với từ khóa 'Gạo'!");
//        } else {
//            System.out.println("Kết quả tìm kiếm với từ khóa 'Gạo':");
//            for (Zone zone : searchResults) {
//                System.out.println("ID: " + zone.getId() + ", Name: " + zone.getName()
//                        + ", Created At: " + zone.getCreatedAt() + ", Status: " + zone.getStatus());
//            }
//        }
//        System.out.println("--------------------------------------");
//
//        // 5. Test getZoneById
//        System.out.println("=== Test getZoneById ===");
//        Zone zoneById = dao.getZoneById(1); // Giả sử ID 1 tồn tại trong DB
//        if (zoneById == null) {
//            System.out.println("Không tìm thấy zone với ID = 1!");
//        } else {
//            System.out.println("Zone với ID = 1: " + zoneById.getName()
//                    + ", Store ID: " + (zoneById.getStoreId() != null ? zoneById.getStoreId().getId() : "null")
//                    + ", Status: " + zoneById.getStatus());
//        }
//        System.out.println("--------------------------------------");
//
//        // 6. Test insertZone
//        System.out.println("=== Test insertZone ===");
//        Stores store = new Stores();
//        store.setId(1); // Giả sử store_id = 1 tồn tại
//        Zone newZone = Zone.builder()
//                .name("Gạo Test Mới")
//                .createdBy("Admin")
//                .storeId(store)
//                .status("Active")
//                .build();
//        dao.insertZone(newZone);
//        System.out.println("Đã thêm zone mới: Gạo Test Mới");
//        zones = dao.viewAllZones("id", 1); // Kiểm tra lại danh sách
//        System.out.println("Danh sách zones sau khi thêm:");
//        for (Zone zone : zones) {
//            System.out.println("ID: " + zone.getId() + ", Name: " + zone.getName());
//        }
//        System.out.println("--------------------------------------");
//
//        // 7. Test updateZone
//        System.out.println("=== Test updateZone ===");
//        Zone zoneToUpdate = dao.getZoneById(1); // Giả sử ID 1 tồn tại
//        if (zoneToUpdate != null) {
//            zoneToUpdate.setName("Gạo Test Cập Nhật");
//            zoneToUpdate.setStatus("Inactive");
//            dao.updateZone(zoneToUpdate);
//            System.out.println("Đã cập nhật zone ID = 1");
//            Zone updatedZone = dao.getZoneById(1);
//            System.out.println("Zone sau khi cập nhật: " + updatedZone.getName() + ", Status: " + updatedZone.getStatus());
//        } else {
//            System.out.println("Không tìm thấy zone để cập nhật!");
//        }
//        System.out.println("--------------------------------------");
//
    }
}
