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
                        rs.getDate("updated_at"),
                        rs.getBoolean("isDeleted"),
                        rs.getDate("deletedAt"),
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
   

        public int countProducts(){
         String sql ="Select count(*) from products";
         try {
            PreparedStatement st=connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                    return rs.getInt(1);
            }
      
             } catch (SQLException e) {
        e.printStackTrace();
    }
         return 0;
     }
        public List<Products> searchProducts(String name) {
    String sql = "SELECT * FROM products WHERE name LIKE ?;";
    List<Products> productsList = new ArrayList<>();
    try (PreparedStatement st = connection.prepareStatement(sql)) {
        st.setString(1, "%" + name + "%");
        try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Products products = new Products(
                      rs.getInt("id"),            
                        rs.getString("name"),          
                        rs.getString("image"),          
                        rs.getBigDecimal("price"),               
                        rs.getInt("quantity"),                
                        rs.getInt("zone_id"),      
                        rs.getString("description"),        
                        rs.getDate("created_at"),   
                        rs.getDate("updated_at"),    
                        rs.getBoolean("isDelete"),         
                        rs.getDate("deletedAt"),       
                        rs.getString("status") 
                );
                productsList.add(products);
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
                        rs.getDate("updated_at"),
                        rs.getBoolean("isDelete"),
                        rs.getDate("deletedAt"),
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

    public void editProduct(Products products) {
    String sql = "UPDATE products " +
                 "SET `name` = ?, " +
                 "`image` = ?, " +
                 "`price` = ?, " +
                 "`quantity` = ?, " +
                 "`zone_id` = ?, " +
                 "`description` = ?, " +
                 "`updated_at` = CURRENT_TIMESTAMP, " +
                 "`isDelete` = ?, " +
                 "`deletedAt` = ?, " +
                 "`status` = ? " +
                 "WHERE `id` = ?;";

    try (PreparedStatement st = connection.prepareStatement(sql)) {
        st.setString(1, products.getName());
        st.setString(2, products.getImage());
        st.setBigDecimal(3, products.getPrice());
        st.setInt(4, products.getQuantity());
        st.setInt(5, products.getZone_id());
        st.setString(6, products.getDescription());

        // You don't need to set 'created_at' here since it's handled by the database (unless needed explicitly)
        // st.setDate(9, new java.sql.Date(products.getCreatedAt().getTime())); // Removed

        st.setBoolean(7, products.isIsDeleted());  // Adjusted index since created_at was removed
        if (products.getDeletedAt() != null) {
            st.setDate(8, new java.sql.Date(products.getDeletedAt().getTime()));
        } else {
            st.setNull(9, java.sql.Types.DATE);
        }
        st.setString(10, products.getStatus());
        st.setInt(11, products.getId()); // Adjusted index

        st.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
public void insertProduct(Products products) {
    String sql = "INSERT INTO products (name, image, price, quantity, zone_id, description, created_at, updated_at, status) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try {
        PreparedStatement st = connection.prepareStatement(sql);
        st.setString(1, products.getName());
        st.setString(2, products.getImage());
        st.setBigDecimal(3, products.getPrice());
        st.setInt(4, products.getQuantity());
        st.setInt(5, products.getZone_id());
        st.setString(6, products.getDescription());
        st.setDate(7, new java.sql.Date(System.currentTimeMillis())); 
        st.setNull(8, java.sql.Types.DATE);
        st.setString(9, products.getStatus());
        st.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace(); // Log the error for debugging
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


    public static void main(String[] args) {
        productsDAO productsDAO = new productsDAO();
        
        // Get the list of products with the given ID (for example, 1)
        List<Products> products = productsDAO.viewAllProducts("price", 1);
            
            // Print the products to verify the result
            for (Products product : products) {
                System.out.println(product);  // Ensure Products class has a toString() method
            }
        try {


        } catch (Exception e) {
            e.printStackTrace(); // Handle any errors during database connection or insertion
        }
    }

    
}
