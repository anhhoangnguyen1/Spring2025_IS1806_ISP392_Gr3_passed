/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import dal.zoneDAO;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import entity.Products;
import entity.Zone;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author phamh
 */
public class productsDAO extends DBContext {

    zoneDAO zones = new zoneDAO();

    // Lấy productId hiện tại của Zone
    private int getCurrentProductId(int zoneId) {
        String sql = "SELECT product_id FROM Zones WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, zoneId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("product_id") : -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Lấy productName từ productId
    private String getProductName(int productId) {
        String sql = "SELECT name FROM Products WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString("name") : "Unknown";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    // Lấy Zone ID từ tên Zone
    private int getZoneIdByName(String zoneName) {
        String sql = "SELECT id FROM Zones WHERE name = ? AND isDeleted = 0";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, zoneName);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("id") : -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Cập nhật lịch sử JSON cho Zone
    private void updateZoneHistory(int zoneId, int oldProductId, String oldProductName, String updatedBy) {
        String selectSql = "SELECT history FROM Zones WHERE id = ?";
        String updateSql = "UPDATE Zones SET history = ? WHERE id = ?";
        try {
            // Lấy lịch sử hiện tại
            JSONArray historyArray;
            try (PreparedStatement psSelect = connection.prepareStatement(selectSql)) {
                psSelect.setInt(1, zoneId);
                ResultSet rs = psSelect.executeQuery();
                if (rs.next()) {
                    String historyJson = rs.getString("history");
                    historyArray = (historyJson == null || historyJson.isEmpty()) ? new JSONArray() : new JSONArray(historyJson);
                } else {
                    historyArray = new JSONArray();
                }
            }

            // Tìm id lớn nhất hiện tại
            int maxId = 0;
            for (int i = 0; i < historyArray.length(); i++) {
                JSONObject entry = historyArray.getJSONObject(i);
                if (entry.has("id")) {
                    int currentId = Integer.parseInt(entry.getString("id"));
                    if (currentId > maxId) {
                        maxId = currentId;
                    }
                }
            }

            // Tạo id mới (tăng maxId lên 1)
            String historyId = String.valueOf(maxId + 1);

            // Thêm thông tin sản phẩm cũ vào lịch sử
            JSONObject historyEntry = new JSONObject();
            historyEntry.put("id", historyId); // Thêm ID
            historyEntry.put("productName", oldProductName);
            historyEntry.put("startDate", getZoneStartDate(zoneId, oldProductId));
            historyEntry.put("endDate", new Timestamp(System.currentTimeMillis()).toString());
            historyEntry.put("updatedBy", updatedBy != null ? updatedBy : "System"); // Thêm updatedBy, mặc định "System" nếu null
            historyArray.put(historyEntry);

            // Cập nhật lịch sử mới
            try (PreparedStatement psUpdate = connection.prepareStatement(updateSql)) {
                psUpdate.setString(1, historyArray.toString());
                psUpdate.setInt(2, zoneId);
                psUpdate.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Có thể thêm log hoặc ném ngoại lệ để xử lý ở lớp trên
        }
    }

    // Hàm giả định lấy startDate (có thể thay bằng createdAt hoặc logic khác)
    private String getZoneStartDate(int zoneId, int productId) {
        return new Timestamp(System.currentTimeMillis() - 86400000).toString(); // Ví dụ: 1 ngày trước
    }

    public List<Products> viewAllProducts(String command, int index, int pageSize) {
        List<Products> list = new ArrayList<>();
        Map<Integer, List<String>> zoneMap = new HashMap<>();

        // Chỉ lấy Zone có status = 'Active' và isDeleted = 0
        String zoneQuery = "SELECT product_id, name FROM Zones WHERE status = 'Active' AND isDeleted = 0";

        try (PreparedStatement st = connection.prepareStatement(zoneQuery); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                int productId = rs.getInt("product_id");
                if (!rs.wasNull()) { 
                    String zoneName = rs.getString("name");

                    zoneMap.putIfAbsent(productId, new ArrayList<>());
                    zoneMap.get(productId).add(zoneName);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching active zones: " + e.getMessage());
        }

        // Lấy danh sách sản phẩm
        String productQuery = "SELECT * FROM Products WHERE isDeleted = 0 ORDER BY "
                + (command != null && !command.isEmpty() ? command : "id")
                + " LIMIT ? OFFSET ?";

        try (PreparedStatement st = connection.prepareStatement(productQuery)) {
            st.setInt(1, pageSize);
            st.setInt(2, (index - 1) * pageSize);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("id");
                    List<String> zones = zoneMap.getOrDefault(productId, new ArrayList<>());
                    String zoneNames = zones.isEmpty() ? "N/A" : String.join(",", zones); // Nếu không có Zone Active, để trống

                    Products product = new Products(
                            productId,
                            rs.getString("name"),
                            rs.getString("image"),
                            rs.getBigDecimal("price"),
                            rs.getInt("quantity"),
                            rs.getString("description"),
                            rs.getDate("created_at"),
                            rs.getString("created_by"),
                            rs.getDate("deletedAt"),
                            rs.getString("deleteBy"),
                            rs.getBoolean("isDeleted"),
                            rs.getDate("updated_at"),
                            rs.getString("status"),
                            zoneNames
                    );

                    list.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching products: " + e.getMessage());
        }

        return list;
    }

    public List<Products> viewAllProductsEditHistory(String command, int index, int pageSize) {
        List<Products> list = new ArrayList<>();
        Map<Integer, List<String>> zoneMap = new HashMap<>();

        // Chỉ lấy Zone có status = 'Active' và isDeleted = 0
        String zoneQuery = "SELECT product_id, name FROM Zones WHERE status = 'Active' AND isDeleted = 0";

        try (PreparedStatement st = connection.prepareStatement(zoneQuery); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                int productId = rs.getInt("product_id");
                if (!rs.wasNull()) { // Chỉ thêm nếu product_id không null
                    String zoneName = rs.getString("name");
                    zoneMap.putIfAbsent(productId, new ArrayList<>());
                    zoneMap.get(productId).add(zoneName);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching active zones: " + e.getMessage());
        }

        // Lấy danh sách sản phẩm đã chỉnh sửa
        String productQuery = "SELECT * FROM Products WHERE updated_at IS NOT NULL AND isDeleted = 0 "
                + "ORDER BY " + (command != null && !command.isEmpty() ? command : "id")
                + " LIMIT ? OFFSET ?";

        try (PreparedStatement st = connection.prepareStatement(productQuery)) {
            st.setInt(1, pageSize);
            st.setInt(2, (index - 1) * pageSize);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("id");
                    List<String> zones = zoneMap.getOrDefault(productId, new ArrayList<>());
                    String zoneNames = zones.isEmpty() ? "N/A" : String.join(",", zones);

                    Products product = new Products(
                            productId,
                            rs.getString("name"),
                            rs.getString("image"),
                            rs.getBigDecimal("price"),
                            rs.getInt("quantity"),
                            rs.getString("description"),
                            rs.getDate("created_at"),
                            rs.getString("created_by"),
                            rs.getDate("deletedAt"),
                            rs.getString("deleteBy"),
                            rs.getBoolean("isDeleted"),
                            rs.getDate("updated_at"),
                            rs.getString("status"),
                            zoneNames
                    );

                    list.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching products: " + e.getMessage());
        }

        return list;
    }

    public int countProducts(String condition) {
        // Nếu không có điều kiện, chỉ lấy tổng số sản phẩm
        String sql = "SELECT COUNT(*) FROM products";
        if (condition != null && !condition.isEmpty()) {
            sql += " WHERE " + condition;
        }

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Products> searchProducts(String name, boolean onlyEdited) {
        String sql = "SELECT * FROM products WHERE MATCH(name, description) AGAINST (? IN NATURAL LANGUAGE MODE) "
                + "AND isDeleted = FALSE";

        if (onlyEdited) {
            sql += " AND updated_at IS NOT NULL";  // Chỉ lấy sản phẩm đã được chỉnh sửa
        }

        List<Products> productsList = new ArrayList<>();

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, name);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Products product = new Products(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("image"),
                            rs.getBigDecimal("price"),
                            rs.getInt("quantity"),
                            rs.getString("description"),
                            rs.getDate("created_at"),
                            rs.getString("created_by"),
                            rs.getDate("deletedAt"),
                            rs.getString("deleteBy"),
                            rs.getBoolean("isDeleted"),
                            rs.getDate("updated_at"),
                            rs.getString("status")
                    );
                    productsList.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productsList;
    }

    public List<Products> getProductById(int id) {
        List<Products> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE id = ?";
        Map<Integer, List<String>> zoneMap = new HashMap<>();
        String zoneQuery = "SELECT product_id, name FROM zones WHERE product_id = ?";  // Filter zones by product_id

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("id");

                    // Fetching zone names for the product
                    try (PreparedStatement zoneSt = connection.prepareStatement(zoneQuery)) {
                        zoneSt.setInt(1, productId);
                        try (ResultSet zoneRs = zoneSt.executeQuery()) {
                            List<String> zones = new ArrayList<>();
                            while (zoneRs.next()) {
                                zones.add(zoneRs.getString("name"));
                            }
                            zoneMap.put(productId, zones);
                        }
                    }

                    // Getting zone names or default value
                    List<String> zones = zoneMap.getOrDefault(productId, List.of("Unknown"));
                    String zoneNames = String.join(",", zones);

                    Products product = new Products(
                            productId,
                            rs.getString("name"),
                            rs.getString("image"),
                            rs.getBigDecimal("price"),
                            rs.getInt("quantity"),
                            rs.getString("description"),
                            rs.getDate("created_at"),
                            rs.getString("created_by"),
                            rs.getDate("deletedAt"),
                            rs.getString("deleteBy"),
                            rs.getBoolean("isDeleted"),
                            rs.getDate("updated_at"),
                            rs.getString("status"),
                            zoneNames // Adding the zone names to the product
                    );

                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<String[]> getTopSellingProductNamesOfMonth() {
        List<String[]> topProducts = new ArrayList<>();
        String sql = "SELECT p.name, SUM(idt.quantity) AS total_quantity_sold "
                + "FROM Invoice_detail idt "
                + "JOIN Invoice i ON idt.invoice_id = i.id "
                + "JOIN Products p ON idt.product_id = p.id "
                + "WHERE i.type = 'export' "
                + "AND MONTH(i.created_at) = MONTH(CURRENT_DATE()) "
                + "AND YEAR(i.created_at) = YEAR(CURRENT_DATE()) "
                + "GROUP BY p.name "
                + "ORDER BY total_quantity_sold DESC "
                + "LIMIT 3";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("name");
                int totalQuantitySold = rs.getInt("total_quantity_sold");
                topProducts.add(new String[]{name, String.valueOf(totalQuantitySold)});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return topProducts;
    }

    public boolean removeZoneFromProduct(Products product, List<Zone> zonesToRemove, String updatedBy) {
        PreparedStatement ps = null;
        try {
            connection.setAutoCommit(false);

            // Lấy danh sách Zone hiện tại của Product
            String selectZonesSql = "SELECT zones FROM Products WHERE product_id = ?";
            ps = connection.prepareStatement(selectZonesSql);
            ps.setInt(1, product.getProductId());
            ResultSet rs = ps.executeQuery();
            List<Integer> currentZoneIds = new ArrayList<>();
            if (rs.next()) {
                String zonesJson = rs.getString("zones");
                if (zonesJson != null && !zonesJson.isEmpty()) {
                    JSONArray zonesArray = new JSONArray(zonesJson);
                    for (int i = 0; i < zonesArray.length(); i++) {
                        currentZoneIds.add(zonesArray.getInt(i));
                    }
                }
            }

            // Loại bỏ các Zone trong zonesToRemove
            for (Zone zone : zonesToRemove) {
                currentZoneIds.remove(Integer.valueOf(zone.getId()));
            }

            // Cập nhật lại cột zones trong Products
            String updateSql = "UPDATE Products SET zones = ?, history = JSON_ARRAY_APPEND(history, '$', ?) WHERE product_id = ?";
            ps = connection.prepareStatement(updateSql);
            ps.setString(1, currentZoneIds.isEmpty() ? "[]" : new JSONArray(currentZoneIds).toString());
            String historyEntry = "Zone " + zonesToRemove.get(0).getId() + " removed by " + updatedBy + " on " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            ps.setString(2, historyEntry);
            ps.setInt(3, product.getProductId());
            int rowsAffected = ps.executeUpdate();

            connection.commit();
            return rowsAffected > 0;
        } catch (SQLException | JSONException e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean editProduct(Products product, List<Zone> zones, String updatedBy) {
        String sqlProduct = "UPDATE products "
                + "SET `name` = ?, "
                + "`image` = ?, "
                + "`price` = ?, "
                + "`quantity` = ?, "
                + "`description` = ?, "
                + "`updated_at` = CURRENT_TIMESTAMP, "
                + "`isDeleted` = ?, "
                + "`status` = ? "
                + "WHERE `id` = ?;";

        String sqlUpdateZone = "UPDATE Zones SET product_id = ? WHERE name = ?";

        // Wrap in transaction
        try {
            connection.setAutoCommit(false); // Turn off auto-commit to handle transaction manually

            // Update the product in the products table
            try (PreparedStatement stProduct = connection.prepareStatement(sqlProduct)) {
                stProduct.setString(1, product.getName());
                stProduct.setString(2, product.getImage());
                stProduct.setBigDecimal(3, product.getPrice());
                stProduct.setInt(4, product.getQuantity());
                stProduct.setString(5, product.getDescription());
                stProduct.setBoolean(6, product.isIsDeleted());
                stProduct.setString(7, product.getStatus());
                stProduct.setInt(8, product.getProductId());

                int rowsUpdated = stProduct.executeUpdate();
                if (rowsUpdated == 0) {
                    connection.rollback();
                    return false; // No rows were updated, something went wrong
                }
            }

            // Update the zones based on the product's name
            if (zones != null && !zones.isEmpty()) {
                try (PreparedStatement updateZoneStmt = connection.prepareStatement(sqlUpdateZone)) {
                    for (Zone zone : zones) {
                        if (zone.getName() != null && !zone.getName().trim().isEmpty()) {
                            int zoneId = getZoneIdByName(zone.getName());
                            if (zoneId != -1) {
                                int oldProductId = getCurrentProductId(zoneId);
                                if (oldProductId != -1 && oldProductId != product.getProductId()) {
                                    String oldProductName = getProductName(oldProductId);
                                    updateZoneHistory(zoneId, oldProductId, oldProductName, updatedBy); // Truyền updatedBy
                                }
                                updateZoneStmt.setInt(1, product.getProductId());
                                updateZoneStmt.setString(2, zone.getName());
                                updateZoneStmt.executeUpdate();
                            }
                        }
                    }
                }
            }

            // Commit the transaction
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback if any exception occurs
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace(); // Log rollback error if it happens
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true); // Restore auto-commit behavior
            } catch (SQLException ex) {
                ex.printStackTrace(); // Log error if setting auto-commit fails
            }
        }
    }

    public boolean insertProduct(Products product, List<Zone> zones, String updatedBy) {
        String sqlProduct = "INSERT INTO products (name, image, price, quantity, description, created_at, created_by, deletedAt, deleteBy, isDeleted, updated_at, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlUpdateZone = "UPDATE Zones SET product_id = ? WHERE name = ?";

        try {
            // 🔴 Tắt auto-commit để xử lý transaction
            connection.setAutoCommit(false);

            int productId;
            try (PreparedStatement stProduct = connection.prepareStatement(sqlProduct, Statement.RETURN_GENERATED_KEYS)) {
                stProduct.setString(1, product.getName());
                stProduct.setString(2, product.getImage());
                stProduct.setBigDecimal(3, product.getPrice());
                stProduct.setInt(4, product.getQuantity());
                stProduct.setString(5, product.getDescription());
                stProduct.setTimestamp(6, new java.sql.Timestamp(product.getCreatedAt().getTime()));
                stProduct.setString(7, product.getCreatedBy());

                if (product.getDeleteAt() != null) {
                    stProduct.setTimestamp(8, new java.sql.Timestamp(product.getDeleteAt().getTime()));
                } else {
                    stProduct.setNull(8, java.sql.Types.TIMESTAMP);
                }

                stProduct.setString(9, product.getDeleteBy());
                stProduct.setBoolean(10, product.isIsDeleted());
                stProduct.setNull(11, java.sql.Types.TIMESTAMP);
                stProduct.setString(12, product.getStatus());

                int rowsInserted = stProduct.executeUpdate();

                if (rowsInserted == 0) {
                    connection.rollback();
                    return false;
                }

                ResultSet generatedKeys = stProduct.getGeneratedKeys();
                if (generatedKeys.next()) {
                    productId = generatedKeys.getInt(1);
                } else {
                    connection.rollback();
                    return false;
                }
            }

            // 🏀 Chỉ cần UPDATE nếu zone đã tồn tại
            if (zones != null && !zones.isEmpty()) {
                try (PreparedStatement updateZoneStmt = connection.prepareStatement(sqlUpdateZone)) {
                    for (Zone zone : zones) {
                        if (zone.getName() != null && !zone.getName().trim().isEmpty()) {
                            int zoneId = getZoneIdByName(zone.getName());
                            if (zoneId != -1) {
                                int oldProductId = getCurrentProductId(zoneId);
                                if (oldProductId != -1 && oldProductId != productId) {
                                    String oldProductName = getProductName(oldProductId);
                                    updateZoneHistory(zoneId, oldProductId, oldProductName, updatedBy);
                                }
                                updateZoneStmt.setInt(1, productId);
                                updateZoneStmt.setString(2, zone.getName());
                                updateZoneStmt.executeUpdate();
                            }
                        }
                    }
                }
            }

            // Commit transaction
            connection.commit();
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                autoCommitEx.printStackTrace();
            }
        }
    }

    public void deleteProduct(int id) {
        String disableFKChecks = "SET FOREIGN_KEY_CHECKS = 0;";
        String deleteProduct = "DELETE FROM Products WHERE id = ?";
        String enableFKChecks = "SET FOREIGN_KEY_CHECKS = 1;";

        try {
            // Disable foreign key checks
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(disableFKChecks);
            }

            // Delete the product from Products table
            try (PreparedStatement st = connection.prepareStatement(deleteProduct)) {
                st.setInt(1, id);
                st.executeUpdate();
            }

            // Enable foreign key checks again
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(enableFKChecks);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Products> getLowStockProducts(int threshold) {
        List<Products> products = new ArrayList<>();
        String sql = "SELECT id, name, quantity FROM Products WHERE quantity <= ? ORDER BY quantity ASC";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, threshold);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                products.add(new Products(rs.getInt("id"), rs.getString("name"), rs.getInt("quantity")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    public static void main(String[] args) {
        productsDAO productsDAO = new productsDAO();

        String name = "id";
        int threshold = 1300; // Ngưỡng cảnh báo sản phẩm sắp hết hàng
        List<Products> topProducts = productsDAO.viewAllProductsEditHistory(name, 1, 15);

        System.out.println("Low Stock Products:");
        for (Products p : topProducts) {
            System.out.println(p);
        }
    }
}
