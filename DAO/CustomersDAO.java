/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author phamh
 */
import Entity.customers;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomersDAO extends DBcontext{
    private Connection connection;

    public CustomersDAO(Connection connection) {
        this.connection = connection;
    }

    public List<customers> getAll() {
        List<customers> list = new ArrayList<>();
        String sql = "SELECT * FROM Customers";

        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                customers customer = new customers(
                        rs.getInt("id"),
                        customers.Type.valueOf(rs.getString("type")), // Lấy giá trị từ Enum
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at"),
                        rs.getString("created_by"),
                        rs.getBoolean("isDelete"),
                        rs.getString("deletedBy"),
                        rs.getTimestamp("deletedAt"),
                        rs.getString("status")
                );
                list.add(customer);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching customers: " + e.getMessage());
        }

        return list;
    }

    public static void main(String[] args) {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/rice_sales_management";  // Your DB URL
        String username = "Huy15"; // Your DB username
        String password = "Phamhaihuy123"; // Your DB password

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Instantiate the DAO
            CustomersDAO customersDAO = new CustomersDAO(connection);
            
            // Fetch all customers
            List<customers> customerList = customersDAO.getAll();
            
            // Print out the customers
            if (customerList.isEmpty()) {
                System.out.println("No customers found.");
            } else {
                for (customers customer : customerList) {
                    System.out.println(customer);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }
}
