/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import entity.Products;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author phamh
 */
public class productsDAO extends DBContext {

    public List<Products> viewAllProducts(String command, int index) {
        List<Products> list = new ArrayList<>();

        // Modify the SQL query to include the search filter (using LIKE)
        String sql = "SELECT * FROM products b ORDER BY " + command + " LIMIT 10 OFFSET ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            // Set the parameters for the query

            st.setInt(1, (index - 1) * 10);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Products product = new Products(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("image"),
                            rs.getBigDecimal("price"),
                            rs.getInt("quantity"),
                            rs.getInt("zone_id"),
                            rs.getString("description"),
                            rs.getDate("created_at"),
                            rs.getString("created_by"),
                            rs.getDate("deletedAt"),
                            rs.getString("deleteBy"),
                            rs.getBoolean("isDeleted"),
                            rs.getDate("updated_at"),
                            rs.getString("status")
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
                            rs.getInt("zone_id"),
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
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Products product = new Products(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("image"),
                            rs.getBigDecimal("price"),
                            rs.getInt("quantity"),
                            rs.getInt("zone_id"),
                            rs.getString("description"),
                            rs.getDate("created_at"),
                            rs.getString("created_by"),
                            rs.getDate("deletedAt"),
                            rs.getString("deleteBy"),
                            rs.getBoolean("isDeleted"),
                            rs.getDate("updated_at"),
                            rs.getString("status")
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

    public void editProduct(Products products) {
        String sql = "UPDATE products "
                + "SET `name` = ?, "
                + "`image` = ?, "
                + "`price` = ?, "
                + "`quantity` = ?, "
                + "`zone_id` = ?, "
                + "`description` = ?, "
                + "`updated_at` = CURRENT_TIMESTAMP, " // ❌ Không có ?
                + "`isDeleted` = ?, "
                + "`status` = ? "
                + "WHERE `id` = ?;";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, products.getName());
            st.setString(2, products.getImage());
            st.setBigDecimal(3, products.getPrice());
            st.setInt(4, products.getQuantity());
            st.setInt(5, products.getZone_id());
            st.setString(6, products.getDescription());
            st.setBoolean(7, products.isIsDeleted()); // ✅ Chỉ còn 9 setX()
            st.setString(8, products.getStatus());
            st.setInt(9, products.getProductId()); // ✅ Giờ id là tham số thứ 9

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean insertProduct(Products product) {
        String sql = "INSERT INTO products (name, image, price, quantity, zone_id, description, created_at, created_by, deletedAt, deleteBy, isDeleted, updated_at, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, product.getName());
            st.setString(2, product.getImage());
            st.setBigDecimal(3, product.getPrice());
            st.setInt(4, product.getQuantity());
            st.setInt(5, product.getZone_id());
            st.setString(6, product.getDescription());
            st.setDate(7, new java.sql.Date(product.getCreatedAt().getTime()));
            st.setString(8, product.getCreatedBy());
            st.setDate(9, product.getDeleteAt() != null ? new java.sql.Date(product.getDeleteAt().getTime()) : null);
            st.setString(10, product.getDeleteBy());
            st.setBoolean(11, product.isIsDeleted());
            st.setDate(12, new java.sql.Date(product.getUpdatedAt().getTime()));
            st.setString(13, product.getStatus());

            int rowsInserted = st.executeUpdate();
            return rowsInserted > 0; // Trả về true nếu có ít nhất 1 dòng được thêm
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
        Products newProduct = new Products(
                1, // ID sẽ tự động tăng
                "Bóng đá Adidas", // name
                "adidas-ball.jpg", // image
                new BigDecimal("499000"), // price
                20, // quantity
                1, // zone_id (giả sử khu vực 1 tồn tại)
                "Bóng đá Adidas chính hãng", // description
                new java.sql.Date(System.currentTimeMillis()), // createdAt
                "admin", // createdBy
                null, // deleteAt (chưa bị xóa)
                null, // deleteBy
                false, // isDeleted (chưa bị xóa)
                new java.sql.Date(System.currentTimeMillis()), // updatedAt
                "available" // status
        );

        // Gọi hàm insertProduct để thêm sản phẩm
        productsDAO.editProduct(newProduct);

        String name = "Gạo";
        int threshold = 1300; // Ngưỡng cảnh báo sản phẩm sắp hết hàng
        List<Products> lowStockProducts = productsDAO.searchProducts(name);

        System.out.println("Low Stock Products:");
        for (Products p : lowStockProducts) {
            System.out.println(p);
        }
    }
}
