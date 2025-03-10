package dao;

import dal.DBContext;
import entity.Zone;
import entity.Stores;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class zoneDAO extends DBContext {

    public List<Zone> getAllZones() {
        List<Zone> zones = new ArrayList<>();
        String query = "SELECT * FROM Zones";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Stores store = null;
                if (rs.getObject("store_id") != null) {
                    store = new Stores();
                    store.setId(rs.getInt("store_id"));
                }

                zones.add(Zone.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .createdAt(rs.getDate("created_at"))
                        .createdBy(rs.getString("created_by"))
                        .deleteAt(rs.getDate("deletedAt"))
                        .deleteBy(rs.getString("deleteBy"))
                        .isDeleted(rs.getBoolean("isDeleted"))
                        .updatedAt(rs.getDate("updated_at"))
                        .storeId(store) // Chỉ lấy storeId
                        .status(rs.getString("status"))
                        .build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zones;
    }

    public Zone getZoneById(int id) {
        String query = "SELECT * FROM Zones WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Stores store = null;
                    if (rs.getObject("store_id") != null) {
                        store = new Stores();
                        store.setId(rs.getInt("store_id"));
                    }

                    return Zone.builder()
                            .id(rs.getInt("id"))
                            .name(rs.getString("name"))
                            .createdAt(rs.getDate("created_at"))
                            .createdBy(rs.getString("created_by"))
                            .deleteAt(rs.getDate("deletedAt"))
                            .deleteBy(rs.getString("deleteBy"))
                            .isDeleted(rs.getBoolean("isDeleted"))
                            .updatedAt(rs.getDate("updated_at"))
                            .storeId(store) // Chỉ lấy storeId
                            .status(rs.getString("status"))
                            .build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertZone(Zone zone) {
        String query = "INSERT INTO Zones (name, created_by, store_id, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, zone.getName());
            ps.setString(2, zone.getCreatedBy());
            ps.setObject(3, (zone.getStoreId() != null) ? zone.getStoreId().getId() : null);
            ps.setString(4, zone.getStatus());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateZone(Zone zone) {
        String query = "UPDATE Zones SET name = ?, updated_at = NOW(), store_id = ?, status = ? WHERE id = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            if (conn == null || conn.isClosed()) {
                System.out.println("🔴 Connection is closed! Reconnecting...");
                return false;
            }

            ps.setString(1, zone.getName());
            ps.setObject(2, (zone.getStoreId() != null) ? zone.getStoreId().getId() : null);
            ps.setString(3, zone.getStatus());
            ps.setInt(4, zone.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteZone(int id) {
        String query = "UPDATE Zones SET isDeleted = 1, deletedAt = NOW() WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        zoneDAO zoneDAO = new zoneDAO();

//        // 1. Test getAllZones()
//        System.out.println("=== Get All Zones ===");
        List<Zone> zones = zoneDAO.getAllZones();
//        for (Zone z : zones) {
//            System.out.println(z);
//        }
//
//        // 2. Test insertZone()
//        System.out.println("\n=== Insert Zone ===");
//        Stores store = new Stores();
//        store.setId(1); // Chỉ gán storeId
//
//        Zone newZone = Zone.builder()
//                .name("Gạo Hữu Cơ")
//                .createdBy("Admin")
//                .storeId(store)
//                .status("Active")
//                .build();
//
//        boolean insertResult = zoneDAO.insertZone(newZone);
//        System.out.println("Insert result: " + insertResult);
//
//        // 3. Test getZoneById()
//        System.out.println("\n=== Get Zone By ID ===");
//        int testZoneId = 5; // Thay ID hợp lệ từ DB
//        Zone foundZone = zoneDAO.getZoneById(testZoneId);
//        if (foundZone != null) {
//            System.out.println("Zone found: " + foundZone);
//        } else {
//            System.out.println("Zone not found!");
//        }
//        System.out.println("\n=== Update Zone ===");
//        int testZoneId = 5; // ID hợp lệ
//        Zone foundZone = zoneDAO.getZoneById(testZoneId);
//        if (foundZone != null) {
//            foundZone.setName("Gạo Nhật Bản Hữu Cơ");
//            foundZone.setStatus("Inactive");
//            boolean updateResult = zoneDAO.updateZone(foundZone);
//            System.out.println("Update result: " + updateResult);
//        } else {
//            System.out.println("❌ Zone not found!");
//        }


//        // 5. Test deleteZone()
//        System.out.println("\n=== Delete Zone ===");
//        int deleteZoneId = 5; // Thay ID hợp lệ từ DB
//        boolean deleteResult = zoneDAO.deleteZone(deleteZoneId);
//        System.out.println("Delete result: " + deleteResult);

        // 6. Kiểm tra lại danh sách zones sau khi thực hiện CRUD
        System.out.println("\n=== Zones After Operations ===");
        zones = zoneDAO.getAllZones();
        for (Zone z : zones) {
            System.out.println(z);
        }
    }
}
