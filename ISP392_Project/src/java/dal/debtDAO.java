/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.SQLException;
import entity.Debt;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author phamh
 */
public class debtDAO extends DBcontext {

    public List<Debt> viewAllDebt(String command, int index) {
        List<Debt> list = new ArrayList<>();
        Map<Integer, String> customerMap = new HashMap<>();

        String sqlCustomers = "SELECT id, name FROM Customers";
        String sqlDebt = "SELECT id, type, amount,customers_id,image,description, created_at, updated_at, created_by, status "
                + "FROM Debt_note ORDER BY " + command + " LIMIT 10 OFFSET ?";

        try {
            try (PreparedStatement stCustomers = connection.prepareStatement(sqlCustomers); ResultSet rs = stCustomers.executeQuery()) {
                while (rs.next()) {
                    customerMap.put(rs.getInt("id"), rs.getString("name"));
                }
            }
            try (PreparedStatement st = connection.prepareStatement(sqlDebt)) {
                st.setInt(1, (index - 1) * 10);

                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        int customerId = rs.getInt("customers_id");
                        String customerName = customerMap.getOrDefault(customerId, "Unknown");

                        Debt debts = new Debt(
                                rs.getInt("id"),
                                rs.getString("type"),
                                rs.getBigDecimal("amount"),
                                rs.getString("image"),
                                rs.getString("description"),
                                customerName,
                                rs.getObject("created_at", LocalDateTime.class),
                                rs.getObject("updated_at", LocalDateTime.class),
                                rs.getString("created_by"),
                                rs.getString("status")
                        );
                        list.add(debts);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching debts: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public int countDebts() {
        String sql = "Select count(*) from Debt_note";
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

    public List<Debt> searchDebts(String name) {
    List<Debt> list = new ArrayList<>();
    Map<Integer, String> customerMap = new HashMap<>();

    String sqlCustomers = "SELECT id, name FROM customers WHERE name LIKE ?";
    try (PreparedStatement stCustomers = connection.prepareStatement(sqlCustomers)) {
        stCustomers.setString(1, "%" + name + "%");
        try (ResultSet rs = stCustomers.executeQuery()) {
            while (rs.next()) {
                customerMap.put(rs.getInt("id"), rs.getString("name"));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    String placeholders = customerMap.keySet().stream().map(id -> "?").collect(Collectors.joining(","));
    String sqlDebt = "SELECT * FROM Debt_note WHERE customer_id IN (" + placeholders + ")";

    try (PreparedStatement st = connection.prepareStatement(sqlDebt)) {
        int index = 1;
        for (int customerId : customerMap.keySet()) {
            st.setInt(index++, customerId);
        }

        try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                int customerId = rs.getInt("customer_id");
                String customerName = customerMap.getOrDefault(customerId, "Unknown");

                Debt debt = new Debt(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getBigDecimal("amount"),
                        rs.getString("image"),
                        rs.getString("description"),
                        customerName,
                        rs.getObject("created_at", LocalDateTime.class),
                        rs.getObject("updated_at", LocalDateTime.class),
                        rs.getString("created_by"),
                        rs.getString("status")
                );
                list.add(debt);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return list;
}


    public void insertDebt(Debt debts) {
        String findCustomerIdSQL = "SELECT id FROM Customers WHERE name = ?";
        String insertDebtSQL = "INSERT INTO Debt_note (type, amount, customers_id,image,description, created_at, updated_at, created_by, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stFindCustomer = connection.prepareStatement(findCustomerIdSQL)) {
            stFindCustomer.setString(1, debts.getCustomerName());
            try (ResultSet rs = stFindCustomer.executeQuery()) {
                if (rs.next()) {
                    int customerId = rs.getInt("id");
                    try (PreparedStatement stInsertDebt = connection.prepareStatement(insertDebtSQL)) {
                        stInsertDebt.setString(1, debts.getType());
                        stInsertDebt.setBigDecimal(2, debts.getAmount());
                        stInsertDebt.setInt(3, customerId);
                        stInsertDebt.setString(4, debts.getImage());
                        stInsertDebt.setString(5, debts.getDescription());
                        stInsertDebt.setObject(6, debts.getCreatedAt());
                        stInsertDebt.setObject(7, debts.getUpdatedAt());
                        stInsertDebt.setString(8, debts.getCreatedBy());
                        stInsertDebt.setString(9, debts.getStatus());
                        stInsertDebt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        debtDAO dao = new debtDAO();
Debt newDebt = new Debt();
    newDebt.setCustomerName("Phạm Văn D");
    // Change the type to one of the allowed values ('debt' or 'repay')
    newDebt.setType("debt");  
    newDebt.setAmount(new BigDecimal("1"));
    newDebt.setImage("path/to/image.jpg");
    newDebt.setDescription("Loan for business expansion");
    newDebt.setCreatedAt(LocalDateTime.now());
    newDebt.setUpdatedAt(null);
    newDebt.setCreatedBy("Admin");
    newDebt.setStatus("Pending");
        dao.insertDebt(newDebt);
        String command = null;
        List<Debt> debts = dao.viewAllDebt(command, 1);
        for (Debt debt : debts) {
            System.out.println(debt);
        }

    }
}
