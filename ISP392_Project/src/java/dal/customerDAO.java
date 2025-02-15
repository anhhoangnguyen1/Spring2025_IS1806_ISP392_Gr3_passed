/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import entity.Customers;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author phamh
 */
public class customerDAO extends DBContext {

    public List<String> getAllCustomerNames() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT name FROM Customers";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching customer names: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public List<Customers> viewAllCustomers(String command, int index) {
        List<Customers> list = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY " + command + " LIMIT 10 OFFSET ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, (index - 1) * 10);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToCustomer(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countCustomers() {
        String sql = "SELECT COUNT(*) FROM customers";
        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Customers> searchCustomers(String keyword) {
        List<Customers> list = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE name LIKE ? OR phone LIKE ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, "%" + keyword + "%");
            st.setString(2, "%" + keyword + "%");
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToCustomer(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Customers getCustomerById(int id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertCustomer(Customers customer) {
        String sql = "INSERT INTO customers (name, phone, address, balance, created_at, updated_at, created_by, updated_by, status) "
                + "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, customer.getName());
            st.setString(2, customer.getPhone());
            st.setString(3, customer.getAddress());
            st.setDouble(4, customer.getBalance());
            st.setString(5, customer.getCreatedBy());
            st.setString(6, customer.getUpdatedBy());
            st.setString(7, customer.getStatus());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editCustomer(Customers customer) {
        String sql = "UPDATE customers SET name = ?, phone = ?, address = ?, balance = ?, updated_at = CURRENT_TIMESTAMP, "
                + "updated_by = ?, status = ? WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, customer.getName());
            st.setString(2, customer.getPhone());
            st.setString(3, customer.getAddress());
            st.setDouble(4, customer.getBalance());
            st.setString(5, customer.getUpdatedBy());
            st.setString(6, customer.getStatus());
            st.setInt(7, customer.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Customers mapResultSetToCustomer(ResultSet rs) throws SQLException {
        return Customers.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .phone(rs.getString("phone"))
                .address(rs.getString("address"))
                .balance(rs.getDouble("balance"))
                .createdAt(rs.getDate("created_at"))
                .updatedAt(rs.getDate("updated_at"))
                .createdBy(rs.getString("created_by"))
                .updatedBy(rs.getString("updated_by"))
                .isDeleted(rs.getBoolean("isDeleted"))
                .deletedBy(rs.getString("deletedBy"))
                .deletedAt(rs.getDate("deletedAt"))
                .status(rs.getString("status"))
                .build();
    }
    
  

    public static void main(String[] args) {
        customerDAO dao = new customerDAO();
        
        int testCustomerId = 2; // Thay ID này bằng ID có trong database
        Customers customer = dao.getCustomerById(testCustomerId);
        
        if (customer != null) {
            System.out.println("Lấy dữ liệu khách hàng thành công:");
            System.out.println("Customer ID: " + customer.getId());
            System.out.println("Name: " + customer.getName());
            System.out.println("Phone: " + customer.getPhone());
            System.out.println("Address: " + customer.getAddress());
            System.out.println("Balance: " + customer.getBalance());
            System.out.println("Created By: " + customer.getCreatedBy());
            System.out.println("Updated By: " + customer.getUpdatedBy());
            System.out.println("Status: " + customer.getStatus());
            System.out.println("Created At: " + customer.getCreatedAt());
            System.out.println("Updated At: " + customer.getUpdatedAt());
        } else {
            System.out.println("Không tìm thấy khách hàng có ID = " + testCustomerId);
        }
    }
}


