/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.SQLException;
import entity.DebtNote;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author phamh
 */
public class debtDAO extends DBContext {

    public List<DebtNote> viewAllDebt(String command, int customerId, int index) {
        List<DebtNote> list = new ArrayList<>();

        String sqlDebt = "SELECT id, type, amount, image, description, created_at, updated_at, created_by, status "
                + "FROM Debt_note "
                + "WHERE customers_id = ? "
                + "ORDER BY " + command + " DESC "
                + "LIMIT 10 OFFSET ?";

        try (PreparedStatement st = connection.prepareStatement(sqlDebt)) {
            st.setInt(1, customerId);
            st.setInt(2, (index - 1) * 10);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    BigDecimal amount = rs.getBigDecimal("amount");
                    String type = rs.getString("type");
                    if ("-".equals(type)) {
                        amount = amount.negate();  // Thêm dấu âm
                    }

                    DebtNote debt = new DebtNote(
                            rs.getInt("id"),
                            rs.getString("type"),
                            amount,
                            rs.getString("image"),
                            rs.getString("description"),
                            customerId,
                            rs.getObject("created_at", LocalDateTime.class),
                            rs.getObject("updated_at", LocalDateTime.class),
                            rs.getString("created_by"),
                            rs.getString("status")
                    );
                    list.add(debt);
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

    public List<DebtNote> searchDebts(int customerId) {
        List<DebtNote> list = new ArrayList<>();

        String sqlDebt = "SELECT id, type, amount, image, description, created_at, updated_at, created_by, status "
                + "FROM Debt_note "
                + "WHERE customers_id = ? "
                + "ORDER BY created_at DESC";

        try (PreparedStatement st = connection.prepareStatement(sqlDebt)) {
            st.setInt(1, customerId);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    DebtNote debt = new DebtNote(
                            rs.getInt("id"),
                            rs.getString("type"),
                            rs.getBigDecimal("amount"),
                            rs.getString("image"),
                            rs.getString("description"),
                            customerId,
                            rs.getObject("created_at", LocalDateTime.class),
                            rs.getObject("updated_at", LocalDateTime.class),
                            rs.getString("created_by"),
                            rs.getString("status")
                    );
                    list.add(debt);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching debts: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public void insertDebt(DebtNote debts) {
        String insertDebtSQL = "INSERT INTO Debt_note (type, amount, customers_id, image, description, created_at, updated_at, created_by, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stInsertDebt = connection.prepareStatement(insertDebtSQL)) {
            stInsertDebt.setString(1, debts.getType());
            stInsertDebt.setBigDecimal(2, debts.getAmount());
            stInsertDebt.setInt(3, debts.getCustomer_id());  // Lấy trực tiếp từ DebtNote
            stInsertDebt.setString(4, debts.getImage());
            stInsertDebt.setString(5, debts.getDescription());
            stInsertDebt.setObject(6, debts.getCreatedAt());
            stInsertDebt.setObject(7, debts.getUpdatedAt());
            stInsertDebt.setString(8, debts.getCreatedBy());
            stInsertDebt.setString(9, debts.getStatus());

            int rowsAffected = stInsertDebt.executeUpdate();
            System.out.println("Debt inserted successfully! Rows affected: " + rowsAffected);
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

    public void deleteDebt(int debtId) {
        String deleteDebtSQL = "DELETE FROM Debt_note WHERE id = ?";

        try (PreparedStatement stDeleteDebt = connection.prepareStatement(deleteDebtSQL)) {
            stDeleteDebt.setInt(1, debtId);  

            int rowsAffected = stDeleteDebt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Debt deleted successfully! Rows affected: " + rowsAffected);
            } else {
                System.out.println("No debt found with the provided ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        debtDAO dao = new debtDAO();
        DebtNote newDebt = new DebtNote();

        String command = "id";

        List<DebtNote> debts = dao.viewAllDebt(command, 1, 1);
        for (DebtNote debt : debts) {
            System.out.println(debt);
        }
        DebtNote debt = DebtNote.builder()
                .type("-")
                .amount(new BigDecimal("500000"))
                .customer_id(1) // Giả sử khách hàng có ID = 1
                .image("test_image.jpg")
                .description("Test debt insertion")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy("admin")
                .status("unpaid")
                .build();
    }
}
