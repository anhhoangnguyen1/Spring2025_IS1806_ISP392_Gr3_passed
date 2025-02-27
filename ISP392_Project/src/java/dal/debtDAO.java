/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.sql.Statement;
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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author phamh
 */
public class debtDAO extends DBContext {

    public List<DebtNote> getComponent(int customerId) {
        List<DebtNote> list = new ArrayList<>();

        String sqlDebt = "SELECT id, debt_note_id, type, amount, image, description, created_at, updated_at, created_by, status "
                + "FROM Debt_note_history "
                + "WHERE customers_id = ? AND change_type = 'UPDATE' "
                + "ORDER BY id DESC";  // Sắp xếp theo ID mới nhất

        try (PreparedStatement st = connection.prepareStatement(sqlDebt)) {
            st.setInt(1, customerId);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    BigDecimal amount = rs.getBigDecimal("amount");
                    String type = rs.getString("type");
                    if ("-".equals(type)) {
                        amount = amount.negate();  // Đảo dấu nếu là khoản trừ
                    }

                    DebtNote debt = new DebtNote(
                            rs.getInt("id"),
                            rs.getInt("debt_note_id"), // Nếu cần lưu debt_note_id
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

    public List<DebtNote> viewAllDebtInCustomer(String command, int customerId, int index) {
        List<DebtNote> list = new ArrayList<>();
        String sqlDebt = "SELECT debt_note_id, id, type, amount, image, description, created_at, updated_at, created_by, status "
                + "FROM Debt_note_history "
                + "WHERE customers_id = ? AND change_type = 'UPDATE' " // Lấy cả khách hàng và người ngoài
                + "ORDER BY " + command + " DESC "
                + "LIMIT ?";

        // Initialize connection (depending on your context, connection might be fetched from a connection pool)
        try (PreparedStatement st = connection.prepareStatement(sqlDebt)) {
            st.setInt(1, customerId);
            int limitValue = Math.max(10, (index - 1) * 10); // Đảm bảo limit >= 10
            st.setInt(2, limitValue);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    BigDecimal amount = rs.getBigDecimal("amount");
                    String type = rs.getString("type");
                    if ("-".equals(type)) {
                        amount = amount.negate();  // Thêm dấu âm
                    }

                    DebtNote debt = new DebtNote(
                            rs.getInt("debt_note_id"), // debt_note_id lên đầu
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
        // Always close connection outside try-with-resources to avoid leaking resources

        return list;
    }

    public List<DebtNote> viewDebtHistory(int customerId, int index) {
        List<DebtNote> list = new ArrayList<>();

        String sqlDebtHistory = "SELECT id, debt_note_id, type, amount, image, description, created_at, updated_at, created_by, status "
                + "FROM Debt_note_history "
                + "WHERE (customers_id = ? OR customers_id IS NULL) AND change_type = 'UPDATE' " // Lấy cả khách hàng và người ngoài
                + "ORDER BY created_at DESC "
                + "LIMIT 10 OFFSET ?";

        try (PreparedStatement st = connection.prepareStatement(sqlDebtHistory)) {
            st.setInt(1, customerId);
            st.setInt(2, Math.max(0, (index - 1) * 10));  // Đảm bảo OFFSET >= 0

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    BigDecimal amount = rs.getBigDecimal("amount");
                    String type = rs.getString("type");
                    if ("-".equals(type)) {
                        amount = amount.negate();  // Đảm bảo số âm nếu là khoản nợ
                    }

                    DebtNote debt = new DebtNote(
                            rs.getInt("id"), // ID thực của bản ghi lịch sử
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
            System.err.println("Error fetching debt history: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public List<DebtNote> viewAllDebt(int index, int pageSize) {
        List<DebtNote> list = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper(); // Xử lý JSON

        // Truy vấn lấy danh sách công nợ mới nhất của mỗi khách hàng
        String sqlDebt = """
        SELECT id, customers_id, debtor_info, type, amount, image, description, created_at, updated_at, created_by, status FROM Debt_note 
        WHERE (customers_id IS NOT NULL AND id = (SELECT MAX(id) FROM Debt_note dn WHERE dn.customers_id = Debt_note.customers_id)) 
        OR customers_id IS NULL 
        ORDER BY id DESC""";

        // Truy vấn lấy thông tin khách hàng
        String sqlCustomers = "SELECT id, name, phone, address FROM Customers";

        try (PreparedStatement stDebt = connection.prepareStatement(sqlDebt); PreparedStatement stCustomers = connection.prepareStatement(sqlCustomers)) {

            // Lấy thông tin khách hàng vào HashMap
            Map<Integer, String[]> customerMap = new HashMap<>();
            try (ResultSet rsCustomers = stCustomers.executeQuery()) {
                while (rsCustomers.next()) {
                    customerMap.put(rsCustomers.getInt("id"), new String[]{
                        rsCustomers.getString("name"),
                        rsCustomers.getString("phone"),
                        rsCustomers.getString("address")
                    });
                }
            }

            // Lưu danh sách công nợ đầy đủ trước khi phân trang
            List<DebtNote> fullDebtList = new ArrayList<>();
            ResultSet rsDebt = stDebt.executeQuery();
            while (rsDebt.next()) {
                int customerId = rsDebt.getInt("customers_id");
                String[] customerData = customerMap.getOrDefault(customerId, new String[]{"", "", ""});
                fullDebtList.add(parseDebtNote(rsDebt, objectMapper, customerData[0], customerData[1], customerData[2]));
            }

            // Áp dụng phân trang thủ công trong Java
            int start = (index - 1) * pageSize;
            int end = Math.min(start + pageSize, fullDebtList.size());
            if (start < fullDebtList.size()) {
                list = fullDebtList.subList(start, end);
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

    /**
     * Hàm hỗ trợ xử lý JSON và tạo đối tượng DebtNote
     */
    private DebtNote parseDebtNote(ResultSet rsDebt, ObjectMapper objectMapper,
            String customerName, String customerPhone, String customerAddress) throws SQLException {
        String debtorInfoJson = rsDebt.getString("debtor_info");
        String debtorName = "", debtorAddress = "", debtorPhone = "";

        if (debtorInfoJson != null && !debtorInfoJson.isEmpty()) {
            try {
                JsonNode debtorInfoNode = objectMapper.readTree(debtorInfoJson);
                debtorName = debtorInfoNode.has("name") ? debtorInfoNode.get("name").asText() : "";
                debtorAddress = debtorInfoNode.has("address") ? debtorInfoNode.get("address").asText() : "";
                debtorPhone = debtorInfoNode.has("phone") ? debtorInfoNode.get("phone").asText() : "";
            } catch (Exception e) {
                System.err.println("Error processing debtor_info JSON: " + e.getMessage());
            }
        }

        return new DebtNote(
                rsDebt.getInt("id"),
                rsDebt.getString("type"),
                rsDebt.getBigDecimal("amount"),
                rsDebt.getString("image"),
                rsDebt.getString("description"),
                rsDebt.getInt("customers_id"),
                customerName, // Tên khách hàng
                customerPhone, // Số điện thoại khách hàng
                customerAddress, // Địa chỉ khách hàng
                debtorName, // Tên con nợ
                debtorAddress, // Địa chỉ con nợ
                debtorPhone, // Số điện thoại con nợ
                rsDebt.getObject("created_at", LocalDateTime.class),
                rsDebt.getObject("updated_at", LocalDateTime.class),
                rsDebt.getString("created_by"),
                rsDebt.getString("status")
        );
    }

    public int countDebts() {
        int count = 0;
        String sqlCount = """
        SELECT COUNT(*) FROM Debt_note 
        WHERE (customers_id IS NOT NULL AND id = (SELECT MAX(id) FROM Debt_note dn WHERE dn.customers_id = Debt_note.customers_id)) 
        OR customers_id IS NULL
    """;

        try (PreparedStatement stCount = connection.prepareStatement(sqlCount); ResultSet rsCount = stCount.executeQuery()) {
            if (rsCount.next()) {
                count = rsCount.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting debts: " + e.getMessage());
            e.printStackTrace();
        }

        return count;
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

    public boolean insertDebtInCustomer(DebtNote debts) {
        String insertQuery = "INSERT INTO Debt_note (type, amount, image, description, customers_id, created_at, updated_at, created_by, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String updateQuery = "UPDATE Debt_note SET status = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            // Set dữ liệu cho INSERT
            ps.setString(1, debts.getType());
            ps.setBigDecimal(2, debts.getAmount());
            ps.setString(3, debts.getImage());
            ps.setString(4, debts.getDescription());
            ps.setInt(5, debts.getCustomer_id());
            ps.setObject(6, debts.getCreatedAt());
            ps.setObject(7, debts.getUpdatedAt());
            ps.setString(8, debts.getCreatedBy());
            ps.setString(9, debts.getStatus());

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int insertedId = generatedKeys.getInt(1); // Lấy ID vừa insert

                        // Cập nhật trạng thái
                        try (PreparedStatement updatePs = connection.prepareStatement(updateQuery)) {
                            updatePs.setString(1, debts.getStatus());
                            updatePs.setInt(2, insertedId);

                            int updateRows = updatePs.executeUpdate();
                            if (updateRows > 0) {
                                System.out.println("✅ Insert & Update successful for ID: " + insertedId);
                                return true;  // 🔥 Trả về true khi cả INSERT và UPDATE thành công
                            } else {
                                System.out.println("⚠ Update failed.");
                            }
                        }
                    }
                }
            } else {
                System.out.println("⚠ Insert failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Nếu có lỗi hoặc không insert/update thành công thì trả về false
    }

    public boolean insertAndUpdateDebt(DebtNote debts) {
        String insertDebtSQL = "INSERT INTO Debt_note (type, amount, image, description, created_at, updated_at, created_by, status, debtor_info) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String updateDebtSQL = "UPDATE Debt_note SET updated_at = ? WHERE id = ?";

        try (PreparedStatement stInsertDebt = connection.prepareStatement(insertDebtSQL, Statement.RETURN_GENERATED_KEYS)) {
            // Set values for INSERT query
            stInsertDebt.setString(1, debts.getType());
            stInsertDebt.setBigDecimal(2, debts.getAmount());
            stInsertDebt.setString(3, debts.getImage());
            stInsertDebt.setString(4, debts.getDescription());
            stInsertDebt.setObject(5, debts.getCreatedAt());
            stInsertDebt.setObject(6, debts.getUpdatedAt());
            stInsertDebt.setString(7, debts.getCreatedBy());
            stInsertDebt.setString(8, debts.getStatus());

            // Xử lý debtor_info JSON
            String debtorInfoJson = null;
            if (debts.getDebtorName() != null || debts.getDebtorAddress() != null || debts.getDebtorPhone() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
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
                debtorInfoJson = debtorNode.toString();
            }

            if (debtorInfoJson != null && !debtorInfoJson.isEmpty()) {
                stInsertDebt.setString(9, debtorInfoJson);
            } else {
                stInsertDebt.setNull(9, java.sql.Types.NULL);
            }

            // Thực hiện INSERT
            int rowsInserted = stInsertDebt.executeUpdate();
            if (rowsInserted == 0) {
                return false;
            }

            // Lấy ID của bản ghi vừa chèn
            try (ResultSet generatedKeys = stInsertDebt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newDebtId = generatedKeys.getInt(1);

                    // Thực hiện UPDATE ngay sau INSERT
                    try (PreparedStatement stUpdateDebt = connection.prepareStatement(updateDebtSQL)) {
                        stUpdateDebt.setObject(1, LocalDateTime.now());
                        stUpdateDebt.setInt(2, newDebtId);

                        int rowsUpdated = stUpdateDebt.executeUpdate();
                        return rowsUpdated > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi
    }

    public boolean updateDebtInCustomer(DebtNote debt) {
        String sql = "UPDATE Debt_note SET type = ?, amount = ?, image = ?, description = ?, customers_id = ?,created_at = ?, status = ? WHERE id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, debt.getType());
            st.setBigDecimal(2, debt.getAmount());
            st.setString(3, debt.getImage());
            st.setString(4, debt.getDescription());
            st.setInt(5, debt.getCustomer_id());
            st.setObject(6, debt.getCreatedAt());
            st.setString(7, debt.getStatus());
            st.setInt(8, debt.getId());
            System.out.println("Executing SQL: " + st.toString()); // Debug SQL statement

            int affectedRows = st.executeUpdate(); // Thực hiện update
            return affectedRows > 0; // Kiểm tra xem có bản ghi nào được cập nhật không
        } catch (SQLException e) {
            System.err.println("Error updating debt: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDebt(DebtNote debt) {
        String sqlUpdate = "UPDATE Debt_note "
                + "SET type = ?, amount = ?, image = ?, description = ?, created_at = ?, updated_at = ?, created_by = ?, status = ? "
                + "WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sqlUpdate)) {
            ps.setString(1, debt.getType());
            ps.setBigDecimal(2, debt.getAmount());
            ps.setString(3, debt.getImage());
            ps.setString(4, debt.getDescription());
            ps.setObject(5, debt.getCreatedAt());
            ps.setObject(6, LocalDateTime.now()); // Cập nhật thời gian hiện tại
            ps.setString(7, debt.getCreatedBy());
            ps.setString(8, debt.getStatus());

            ps.setInt(9, debt.getId()); // WHERE id = ?

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating debt: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Map<Integer, BigDecimal> getTotalAmountByDebtNoteId() {
        Map<Integer, BigDecimal> totalAmountMap = new HashMap<>();
        String sql = "SELECT \n"
                + "debt_note_id, \n"
                + "SUM(CASE WHEN type = '-' THEN -ABS(amount) ELSE ABS(amount) END) AS total_amount \n"
                + "FROM Debt_note_history \n"
                + "WHERE change_type = 'UPDATE' \n"
                + "GROUP BY debt_note_id;";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int debtNoteId = rs.getInt("debt_note_id");
                BigDecimal totalAmount = rs.getBigDecimal("total_amount");

                // Lưu tổng amount cho debt_note_id vào Map
                totalAmountMap.put(debtNoteId, totalAmount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalAmountMap;
    }

    public List<DebtNote> getDebtById(int id) {
        List<DebtNote> debts = new ArrayList<>();
        Map<Integer, String> customerMap = new HashMap<>();
        try {
            // Câu lệnh SQL để lấy thông tin khách hàng
            String sqlCustomers = "SELECT id, name FROM Customers";

            // Câu lệnh SQL để lấy thông tin công nợ từ Debt_note_history
            // Chúng ta thay đổi điều kiện để truy vấn khi không có customer_id hoặc debt_note_id
            String sql = "SELECT * \n"
                    + "FROM Debt_note_history \n"
                    + "WHERE debt_note_id = ? AND change_type = 'UPDATE';";

            // Lấy thông tin khách hàng và lưu vào HashMap
            try (PreparedStatement stCustomers = connection.prepareStatement(sqlCustomers); ResultSet rs = stCustomers.executeQuery()) {
                while (rs.next()) {
                    customerMap.put(rs.getInt("id"), rs.getString("name"));
                }
            }

            // Thực thi câu lệnh SQL để lấy công nợ
            try (PreparedStatement st = connection.prepareStatement(sql)) {
                // Đặt tham số customerId và debtNoteId
                st.setInt(1, id); // Nếu không có id hợp lệ, sử dụng 0

                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        int customerId = rs.getInt("customers_id");
                        String customerName = customerMap.getOrDefault(customerId, "Unknown");

                        // Tạo đối tượng DebtNote và thêm vào danh sách
                        DebtNote debt = new DebtNote(
                                rs.getInt("id"),
                                rs.getInt("debt_note_id"),
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
        String command = "created_at";
        int index = 3;
        int pageSize = 10;
        debtDAO dao = new debtDAO();
        List<DebtNote> debts = dao.viewAllDebt(index, 5);
        Map<Integer, BigDecimal> totalAmountByDebtNoteId = dao.getTotalAmountByDebtNoteId();
        for (Map.Entry<Integer, BigDecimal> entry : totalAmountByDebtNoteId.entrySet()) {
            System.out.println("debt_note_id: " + entry.getKey() + " - Total Amount: " + entry.getValue());
        }
        // 4️⃣ Gọi hàm cập nhật
        // Gọi phương thức updateDebtInCustomer
        // Insert debt into the database
        // Example: Use a sorting command for the query
        // Fetch the debts from the database
        for (DebtNote debtObj : debts) {
            System.out.println(debtObj);
        }
    }
}
