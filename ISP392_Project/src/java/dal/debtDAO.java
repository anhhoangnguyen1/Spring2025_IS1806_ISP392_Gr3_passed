/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import dal.DBContext;
import java.sql.SQLException;
import entity.DebtNote;
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
public class debtDAO extends DBContext {

    public List<DebtNote> viewAllDebt(String command, int index) {
        List<DebtNote> list = new ArrayList<>();
        Map<Integer, String> customerMap = new HashMap<>();
        String sqlCustomers = "SELECT id, name FROM Customers";
        String sqlDebt = "SELECT id, type, amount, customers_id, image, description, created_at, updated_at, created_by, status "
                + "FROM Debt_note "
                + "WHERE (customers_id, created_at) IN ("
                + "    SELECT customers_id, MAX(created_at) AS latest_created_at "
                + "    FROM Debt_note "
                + "    GROUP BY customers_id"
                + ") "
                + "ORDER BY " + command
                + " LIMIT 10 OFFSET ?";

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

                        DebtNote debts = new DebtNote(
                                rs.getInt("id"),
                                rs.getString("type"),
                                rs.getBigDecimal("amount"),
                                rs.getString("image"),
                                rs.getString("description"),
                                customerId,
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
        String sql = "SELECT COUNT(*) FROM (\n"
                + "    SELECT customers_id, MAX(created_at)\n"
                + "    FROM Debt_note\n"
                + "    GROUP BY customers_id\n"
                + ") AS latest_debts;";
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

    public List<DebtNote> searchDebts(String name) {
        List<DebtNote> list = new ArrayList<>();
        Map<Integer, String> customerMap = new HashMap<>();

        // Tìm danh sách khách hàng theo tên
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
            return list; // Trả về danh sách rỗng nếu lỗi
        }

        // Nếu không có khách hàng nào, trả về danh sách rỗng ngay
        if (customerMap.isEmpty()) {
            System.out.println("Không tìm thấy khách hàng nào với tên: " + name);
            return list;
        }

        // Tạo danh sách tham số '?' cho câu truy vấn
        String placeholders = customerMap.keySet().stream().map(id -> "?").collect(Collectors.joining(","));
        String sqlDebt = "SELECT d.* FROM Debt_note d "
                + "INNER JOIN ( "
                + "    SELECT customers_id, MAX(created_at) AS latest_created_at "
                + "    FROM Debt_note "
                + "    WHERE customers_id IN (" + placeholders + ") "
                + "    GROUP BY customers_id "
                + ") latest_debts "
                + "ON d.customers_id = latest_debts.customers_id AND d.created_at = latest_debts.latest_created_at";

        try (PreparedStatement st = connection.prepareStatement(sqlDebt)) {
            int index = 1;
            for (int customerId : customerMap.keySet()) {
                st.setInt(index++, customerId);
            }

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    int customerId = rs.getInt("customers_id");
                    String customerName = customerMap.getOrDefault(customerId, "Unknown");

                    DebtNote debt = new DebtNote(
                            rs.getInt("id"),
                            rs.getString("type"),
                            rs.getBigDecimal("amount"),
                            rs.getString("image"),
                            rs.getString("description"),
                            customerId,
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

    public void insertDebt(DebtNote debts) {
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

    public List<DebtNote> getDebtById(int id) {
        List<DebtNote> debts = new ArrayList<>();
        Map<Integer, String> customerMap = new HashMap<>();
        try {
            String sqlCustomers = "SELECT id, name FROM Customers";
            String sql = "SELECT * FROM Debt_note WHERE id = ?";
            try (PreparedStatement stCustomers = connection.prepareStatement(sqlCustomers); ResultSet rs = stCustomers.executeQuery()) {
                while (rs.next()) {
                    customerMap.put(rs.getInt("id"), rs.getString("name"));
                }
            }

            try (PreparedStatement st = connection.prepareStatement(sql)) {
                st.setInt(1, id);
                try (ResultSet rs = st.executeQuery()) {

                    while (rs.next()) {
                        int customerId = rs.getInt("customers_id");
                        String customerName = customerMap.getOrDefault(customerId, "Unknown");
                        DebtNote debt = new DebtNote(
                                rs.getInt("id"),
                                rs.getString("type"),
                                rs.getBigDecimal("amount"),
                                rs.getString("image"),
                                rs.getString("description"),
                                customerId,
                                customerName,
                                rs.getObject("created_at", LocalDateTime.class),
                                rs.getObject("updated_at", LocalDateTime.class),
                                rs.getString("created_by"),
                                rs.getString("status")
                        );
                        debts.add(debt);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching debts: " + ex.getMessage());
            ex.printStackTrace();
        }
        return debts;
    }

    public static void main(String[] args) {
        debtDAO dao = new debtDAO();
        DebtNote newDebt = new DebtNote();

        String command = null;
        List<DebtNote> debts = dao.viewAllDebt(command, 1);
        for (DebtNote debt : debts) {
            System.out.println(debt);
        }

    }
}
