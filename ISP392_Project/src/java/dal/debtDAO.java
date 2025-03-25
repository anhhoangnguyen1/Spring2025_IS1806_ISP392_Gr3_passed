/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Statement;
import java.sql.SQLException;
import entity.DebtNote;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

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
        String sqlDebt = "SELECT id, type, amount, image, description, created_at, updated_at, created_by, status "
                + "FROM Debt_note "
                + "WHERE customers_id = ? "
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

    public List<DebtNote> viewAllDebt(String command, int index, int pageSize) {
        List<DebtNote> list = new ArrayList<>();
        int offset = (index - 1) * pageSize;

        String sqlDebt = "SELECT d.id, d.type, c.balance, d.image, d.description, d.created_at, d.updated_at, "
                + "d.created_by, d.status, c.id AS customer_id, c.name, c.phone, c.address "
                + "FROM Debt_note d "
                + "JOIN Customers c ON d.customers_id = c.id "
                + "WHERE d.created_at = (SELECT MAX(dn.created_at) FROM Debt_note dn WHERE dn.customers_id = d.customers_id) "
                + "ORDER BY " + command + " DESC "
                + "LIMIT ? OFFSET ?";

        try (PreparedStatement st = connection.prepareStatement(sqlDebt)) {
            st.setInt(1, pageSize);
            st.setInt(2, offset);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    BigDecimal balance = rs.getBigDecimal("balance");

                    int customerId = rs.getInt("customer_id");
                    String customerName = rs.getString("name");
                    String customerPhone = rs.getString("phone");
                    String customerAddress = rs.getString("address");

                    DebtNote debt = new DebtNote(
                            rs.getInt("id"),
                            rs.getString("type"),
                            balance,
                            rs.getString("image"),
                            rs.getString("description"),
                            customerId,
                            customerName,
                            customerPhone,
                            customerAddress,
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

    public List<DebtNote> getDebtByCustomerId(int customerId) {
        List<DebtNote> debts = new ArrayList<>();

        String sql = "SELECT d.id, d.type, d.amount, d.image, d.description, d.created_at, d.updated_at, "
                + "d.created_by, d.status, c.name, c.phone, c.address "
                + "FROM Debt_note d "
                + "JOIN Customers c ON d.customers_id = c.id "
                + "WHERE d.customers_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, customerId);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    BigDecimal amount = rs.getBigDecimal("amount");
                    String type = rs.getString("type");

                    // Nếu type là "-", thì đổi dấu số tiền
                    if ("-".equals(type)) {
                        amount = amount.negate();
                    }

                    // Lấy thông tin khách hàng
                    String customerName = rs.getString("name");
                    String customerPhone = rs.getString("phone");
                    String customerAddress = rs.getString("address");

                    // Tạo đối tượng DebtNote và thêm vào danh sách
                    DebtNote debt = new DebtNote(
                            rs.getInt("id"),
                            type,
                            amount,
                            rs.getString("image"),
                            rs.getString("description"),
                            customerId,
                            customerName,
                            customerPhone,
                            customerAddress,
                            rs.getObject("created_at", LocalDateTime.class),
                            rs.getObject("updated_at", LocalDateTime.class),
                            rs.getString("created_by"),
                            rs.getString("status")
                    );
                    debts.add(debt);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching debts: " + e.getMessage());
            e.printStackTrace();
        }

        return debts;
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

   public List<DebtNote> searchDebts(String name) {
    List<DebtNote> list = new ArrayList<>();

    String sqlDebt = "SELECT d.id, d.type, c.balance, d.image, d.description, d.created_at, d.updated_at, "
            + "d.created_by, d.status, c.id AS customer_id, c.name, c.phone, c.address "
            + "FROM Debt_note d "
            + "JOIN Customers c ON d.customers_id = c.id "
            + "WHERE d.created_at = (SELECT MAX(dn.created_at) FROM Debt_note dn WHERE dn.customers_id = d.customers_id) "
            + "AND c.name LIKE ?";

    try (PreparedStatement st = connection.prepareStatement(sqlDebt)) {
        st.setString(1, "%" + name + "%");  // Tìm kiếm gần đúng theo tên khách hàng

        try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                BigDecimal balance = rs.getBigDecimal("balance");

                int customerId = rs.getInt("customer_id");
                String customerName = rs.getString("name");
                String customerPhone = rs.getString("phone");
                String customerAddress = rs.getString("address");

                DebtNote debt = new DebtNote(
                        rs.getInt("id"),
                        rs.getString("type"),
                        balance,
                        rs.getString("image"),
                        rs.getString("description"),
                        customerId,
                        customerName,
                        customerPhone,
                        customerAddress,
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


    public boolean insertDebt(DebtNote debts) {
        String insertQuery = "INSERT INTO Debt_note (type, amount, image, description, customers_id, created_at, updated_at, created_by, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
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
                        int insertedId = generatedKeys.getInt(1);
                        System.out.println("✅ Insert successful for ID: " + insertedId);
                        return true;
                    }
                }
            } else {
                System.out.println("⚠ Insert failed.");
            }
        } catch (SQLException e) {
            System.err.println("❌ SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // Không đóng connection ở đây
    }

    public boolean insertDebtInCustomer(DebtNote debts) {
        String insertQuery = "INSERT INTO Debt_note (type, amount, image, description, customers_id, created_at, updated_at, created_by, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
                        System.out.println("✅ Insert successful for ID: " + insertedId);
                        return true;  // 🔥 Trả về true khi INSERT thành công
                    }
                }
            } else {
                System.out.println("⚠ Insert failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Nếu có lỗi hoặc không insert thành công thì trả về false
    }

    public static void main(String[] args) {
        // Initialize the DAO (Data Access Object)
        String command = "id";
        int index = 1;
        int pageSize = 10;
        debtDAO dao = new debtDAO();
        List<DebtNote> debts = dao.viewAllDebt(command, 1, 1);
        for (DebtNote debtObj : debts) {
            System.out.println(debtObj);
        }
    }
}
