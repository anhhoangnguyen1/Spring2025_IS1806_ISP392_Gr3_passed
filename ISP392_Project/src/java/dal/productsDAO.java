/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import dao.zoneDAO;
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

/**
 *
 * @author phamh
 */
public class productsDAO extends DBContext {

    zoneDAO zones = new zoneDAO();

    public List<Products> viewAllProducts(String command, int index) {
        List<Products> list = new ArrayList<>();
        Map<Integer, List<String>> zoneMap = new HashMap<>();
        String zoneQuery = "SELECT product_id, name FROM zones";

        try (PreparedStatement st = connection.prepareStatement(zoneQuery); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String zoneName = rs.getString("name");

                zoneMap.putIfAbsent(productId, new ArrayList<>());
                zoneMap.get(productId).add(zoneName);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching zones: " + e.getMessage());
        }
        String productQuery = "SELECT * FROM products ORDER BY " + command + " LIMIT 10 OFFSET ?";

        try (PreparedStatement st = connection.prepareStatement(productQuery)) {
            st.setInt(1, (index - 1) * 10);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("id");

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

    public int countProducts() {
        String sql = "Select count(*) from products";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Products> searchProducts(String name) {
        String sql = "SELECT * FROM products WHERE MATCH(name, description) AGAINST (? IN NATURAL LANGUAGE MODE) AND isDeleted = FALSE;";
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

    public boolean editProduct(Products product, List<Zone> zones) {
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
                            updateZoneStmt.setInt(1, product.getProductId());
                            updateZoneStmt.setString(2, zone.getName());
                            updateZoneStmt.executeUpdate();
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

    public boolean insertProduct(Products product, List<Zone> zones) {
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
                stProduct.setTimestamp(11, new java.sql.Timestamp(product.getUpdatedAt().getTime()));
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
                        updateZoneStmt.setInt(1, productId);
                        updateZoneStmt.setString(2, zone.getName());
                        updateZoneStmt.executeUpdate();
                    }
                }
            }

            // ✅ Commit transaction
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
        List<Products> topProducts = productsDAO.getProductById(9);

        System.out.println("Low Stock Products:");
        for (Products p : topProducts) {
            System.out.println(p);
        }
    }
}
