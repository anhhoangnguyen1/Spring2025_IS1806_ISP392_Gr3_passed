/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import javax.lang.model.util.Types;

/**
 *
 * @author phamh
 */
public class debtDAO extends DBContext {

    public List<DebtNote> viewAllDebtInCustomer(String command, int customerId, int index) {
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

    public List<DebtNote> viewAllDebt(String command, int index) {
        List<DebtNote> list = new ArrayList<>();
        Map<Integer, String[]> customerMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper(); // Xử lý JSON

        // Truy vấn lấy thông tin khách hàng
        String sqlCustomers = "SELECT id, name, phone, address FROM Customers";

        // Truy vấn lấy thông tin công nợ
        String sqlDebt = "SELECT id, customers_id, debtor_info, type, amount, image, description, created_at, updated_at, created_by, status "
                + "FROM Debt_note "
                + "ORDER BY " + command + " "
                + "LIMIT 10 OFFSET ?";

        try {
            // Lấy thông tin khách hàng vào HashMap
            try (PreparedStatement stCustomers = connection.prepareStatement(sqlCustomers); ResultSet rs = stCustomers.executeQuery()) {
                while (rs.next()) {
                    customerMap.put(rs.getInt("id"), new String[]{
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("address")
                    });
                }
            }

            // Truy vấn danh sách công nợ
            try (PreparedStatement st = connection.prepareStatement(sqlDebt)) {
                st.setInt(1, (index - 1) * 10);  // Phân trang

                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        // Xử lý debtor_info JSON
                        String debtorInfoJson = rs.getString("debtor_info");
                        String debtorName = "", debtorAddress = "", debtorPhone = "";

                        if (debtorInfoJson != null && !debtorInfoJson.isEmpty()) {
                            try {
                                JsonNode debtorInfoNode = objectMapper.readTree(debtorInfoJson);
                                if (debtorInfoNode.has("name")) {
                                    debtorName = debtorInfoNode.get("name").asText();
                                }
                                if (debtorInfoNode.has("address")) {
                                    debtorAddress = debtorInfoNode.get("address").asText();
                                }
                                if (debtorInfoNode.has("phone")) {
                                    debtorPhone = debtorInfoNode.get("phone").asText();
                                }
                            } catch (Exception e) {
                                System.err.println("Error processing debtor_info JSON: " + e.getMessage());
                            }
                        }

                        // Lấy thông tin khách hàng từ HashMap
                        int customerId = rs.getInt("customers_id");
                        String[] customerData = customerMap.getOrDefault(customerId, new String[]{"", "", ""});
                        String customerName = customerData[0];
                        String customerPhone = customerData[1];
                        String customerAddress = customerData[2];

                        // Tạo đối tượng DebtNote
                        DebtNote debt = new DebtNote(
                                rs.getInt("id"),
                                rs.getString("type"),
                                rs.getBigDecimal("amount"),
                                rs.getString("image"),
                                rs.getString("description"),
                                customerId,
                                customerName, // Tên khách hàng
                                customerPhone, // Số điện thoại khách hàng
                                customerAddress, // Địa chỉ khách hàng
                                debtorName, // Tên con nợ
                                debtorAddress, // Địa chỉ con nợ
                                debtorPhone, // Số điện thoại con nợ
                                rs.getObject("created_at", LocalDateTime.class),
                                rs.getObject("updated_at", LocalDateTime.class),
                                rs.getString("created_by"),
                                rs.getString("status")
                        );

                        list.add(debt);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching debts: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error processing JSON or other operations: " + e.getMessage());
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

    public void insertDebtInCustomer(DebtNote debts) {
        String insertQuery = "INSERT INTO Debt_note (type, amount, image, description, customers_id, created_at, updated_at, created_by, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
            ps.setString(1, debts.getType());
            ps.setBigDecimal(2, debts.getAmount());
            ps.setString(3, debts.getImage());
            ps.setString(4, debts.getDescription());
            ps.setInt(5, debts.getCustomer_id());
            ps.setObject(6, debts.getCreatedAt());
            ps.setObject(7, debts.getUpdatedAt());
            ps.setString(8, debts.getCreatedBy());
            ps.setString(9, debts.getStatus());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Insert successful.");
            } else {
                System.out.println("No rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void insertDebt(DebtNote debts) {
        // Update the SQL query to exclude the customer_id column
        String insertDebtSQL = "INSERT INTO Debt_note (type, amount, image, description, created_at, updated_at, created_by, status, debtor_info) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stInsertDebt = connection.prepareStatement(insertDebtSQL)) {
            // Set values from the DebtNote object
            stInsertDebt.setString(1, debts.getType());
            stInsertDebt.setBigDecimal(2, debts.getAmount());
            stInsertDebt.setString(3, debts.getImage()); // No customer_id here anymore
            stInsertDebt.setString(4, debts.getDescription());
            stInsertDebt.setObject(5, debts.getCreatedAt());
            stInsertDebt.setObject(6, debts.getUpdatedAt());
            stInsertDebt.setString(7, debts.getCreatedBy());
            stInsertDebt.setString(8, debts.getStatus());

            String debtorInfoJson = null;

            // Construct debtorInfo JSON if debtorName, debtorAddress, or debtorPhone are set
            if (debts.getDebtorName() != null || debts.getDebtorAddress() != null || debts.getDebtorPhone() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                // Create an object to represent the debtor details
                ObjectNode debtorNode = objectMapper.createObjectNode();
                if (debts.getDebtorName() != null) {
                    debtorNode.put("name", debts.getDebtorName());
                }
                if (debts.getDebtorAddress() != null) {
                    debtorNode.put("address", debts.getDebtorAddress());
                }
                if (debts.getDebtorPhone() != null) {
                    debtorNode.put("phone", debts.getDebtorPhone());
                }

                // Convert the object to a JSON string
                debtorInfoJson = debtorNode.toString();
            }

            // Set debtor_info as a JSON string or NULL
            if (debtorInfoJson != null && !debtorInfoJson.isEmpty()) {
                stInsertDebt.setString(9, debtorInfoJson);  // Set as JSON string
            } else {
                stInsertDebt.setNull(9, java.sql.Types.NULL);  // Set as NULL if no debtor info is provided
            }

            // Execute the insert statement
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
        // Initialize the DAO (Data Access Object)
        debtDAO dao = new debtDAO();

        // Create a DebtNote object with sample data
        DebtNote debtNote = new DebtNote(
            0, // ID (Sẽ tự động tạo bởi cơ sở dữ liệu)
            "+", // Type: Debt (Giả sử đây là khoản nợ)
            new BigDecimal("1000.00"), // Amount: 1000 VND
            "debt_image.jpg", // Image (Tên ảnh liên quan đến khoản nợ)
            "Test debt description", // Description
            1, // customer_id: ID của khách hàng liên quan
            LocalDateTime.now(), // Created at
            LocalDateTime.now(), // Updated at
            "admin", // Created by
            "Pending" // Status
        );
        dao.insertDebt(debtNote);
        // Insert debt into the database
        // Example: Use a sorting command for the query
        String command = "id";

        // Fetch the debts from the database
        List<DebtNote> debts = dao.viewAllDebt(command, 1);

        // Print out each DebtNote object
        for (DebtNote debtObj : debts) {
            System.out.println(debtObj);
        }
    }
}
