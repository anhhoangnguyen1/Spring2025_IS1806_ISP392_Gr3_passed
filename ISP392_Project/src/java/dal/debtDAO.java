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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author phamh
 */
public class debtDAO extends DBContext {

    public debtDAO() {
        try {
            this.connection = getConnection(); // ✅ FIX lỗi NullPointerException
        } catch (Exception e) {
            System.err.println("❌ Lỗi kết nối DB trong debtDAO constructor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<DebtNote> viewAllDebtInCustomer(String command, int customerId, int storeId, String startDate, String endDate, int index, int pageSize) {
        List<DebtNote> list = new ArrayList<>();

        // Xác thực command hợp lệ để tránh SQL Injection
        List<String> validColumns = Arrays.asList("id", "amount", "created_at", "updated_at", "created_by", "status");
        if (!validColumns.contains(command)) {
            command = "id"; // Giá trị mặc định nếu command không hợp lệ
        }

        // Xây dựng câu lệnh SQL với bộ lọc ngày
        StringBuilder sqlDebt = new StringBuilder("""
        SELECT id, type, amount, image, description, created_at, updated_at, created_by, status 
        FROM Debt_note 
        WHERE customers_id = ? AND store_id = ?
    """);

        if (startDate != null && !startDate.isEmpty()) {
            sqlDebt.append(" AND created_at >= ? ");
        }
        if (endDate != null && !endDate.isEmpty()) {
            sqlDebt.append(" AND created_at <= ? ");
        }

        sqlDebt.append(" ORDER BY ").append(command).append(" DESC LIMIT ? OFFSET ?");

        try (PreparedStatement st = connection.prepareStatement(sqlDebt.toString())) {
            int paramIndex = 1;
            st.setInt(paramIndex++, customerId);
            st.setInt(paramIndex++, storeId);

            if (startDate != null && !startDate.isEmpty()) {
                st.setDate(paramIndex++, java.sql.Date.valueOf(startDate));
            }
            if (endDate != null && !endDate.isEmpty()) {
                st.setDate(paramIndex++, java.sql.Date.valueOf(endDate));
            }

            st.setInt(paramIndex++, pageSize);
            st.setInt(paramIndex, (index - 1) * pageSize);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    BigDecimal amount = rs.getBigDecimal("amount");
                    String type = rs.getString("type");
                    if ("-".equals(type)) {
                        amount = amount.negate();
                    }

                    DebtNote debt = new DebtNote(
                            rs.getInt("id"),
                            type,
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

    public void insertDebtNote(int customerId, double amount, String description, String createdBy, int storeId) {
        String sql = "INSERT INTO debt_note (customers_id, amount, description, created_by, store_id, created_at, isDeleted) "
                + "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, 0)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, customerId);
            st.setDouble(2, amount);
            st.setString(3, description);
            st.setString(4, createdBy);
            st.setInt(5, storeId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }

    public List<DebtNote> viewAllDebt(String command, int index, int pageSize, int storeID) {
        List<DebtNote> list = new ArrayList<>();
        int offset = (index - 1) * pageSize;

        String sqlDebt = "SELECT d.id, d.type, c.balance, d.image, d.description, d.created_at, d.updated_at, "
                + "d.created_by, d.status, c.id AS customer_id, c.name, c.phone, c.address "
                + "FROM Debt_note d "
                + "JOIN Customers c ON d.customers_id = c.id "
                + "WHERE d.created_at = (SELECT MAX(dn.created_at) FROM Debt_note dn WHERE dn.customers_id = d.customers_id AND dn.store_id = ?) "
                + "ORDER BY " + command + " DESC "
                + "LIMIT ? OFFSET ?";

        try (PreparedStatement st = connection.prepareStatement(sqlDebt)) {
            st.setInt(1, storeID);
            st.setInt(2, pageSize);
            st.setInt(3, offset);

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

    public List<DebtNote> getDebtByCustomerId(int customerId, int storeId, LocalDate startDate, LocalDate endDate) {
        List<DebtNote> debts = new ArrayList<>();

        String sql = "SELECT d.id, d.type, d.amount, d.image, d.description, d.created_at, d.updated_at, "
                + "d.created_by, d.status, c.name, c.phone, c.address "
                + "FROM Debt_note d "
                + "JOIN Customers c ON d.customers_id = c.id "
                + "WHERE d.customers_id = ? AND d.store_id = ?";

        // 🔹 Nếu có khoảng thời gian, thêm điều kiện lọc theo ngày
        if (startDate != null) {
            sql += " AND d.created_at >= ?";
        }
        if (endDate != null) {
            sql += " AND d.created_at <= ?";
        }

        sql += " ORDER BY d.created_at DESC"; // 🔹 Sắp xếp theo thời gian giảm dần

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, customerId);
            st.setInt(2, storeId);

            int index = 3;
            if (startDate != null) {
                st.setDate(index++, java.sql.Date.valueOf(startDate)); // ✅ Đúng cú pháp
            }

            if (endDate != null) {
                st.setDate(index++, java.sql.Date.valueOf(endDate)); // ✅ Đúng cú pháp
            }

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    BigDecimal amount = rs.getBigDecimal("amount");
                    String type = rs.getString("type");

                    // Nếu type là "-", đổi dấu số tiền
                    if ("-".equals(type)) {
                        amount = amount.negate();
                    }

                    DebtNote debt = new DebtNote(
                            rs.getInt("id"),
                            type,
                            amount,
                            rs.getString("image"),
                            rs.getString("description"),
                            customerId,
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("address"),
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

    public int countDebts(int storeId, int customerId, String startDate, String endDate) {
        int count = 0;
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM Debt_note WHERE store_id = ? ");

        if (customerId > 0) {
            sqlCount.append(" AND customers_id = ? ");
        } else {
            sqlCount.append("""
            AND ((customers_id IS NOT NULL 
                AND id = (SELECT MAX(id) FROM Debt_note dn WHERE dn.customers_id = Debt_note.customers_id AND dn.store_id = ?)) 
            OR customers_id IS NULL)
        """);
        }

        // Thêm điều kiện lọc theo ngày nếu có
        if (startDate != null && !startDate.isEmpty()) {
            sqlCount.append(" AND created_at >= ? ");
        }
        if (endDate != null && !endDate.isEmpty()) {
            sqlCount.append(" AND created_at <= ? ");
        }

        try (PreparedStatement stCount = connection.prepareStatement(sqlCount.toString())) {
            int paramIndex = 1;
            stCount.setInt(paramIndex++, storeId);

            if (customerId > 0) {
                stCount.setInt(paramIndex++, customerId);
            } else {
                stCount.setInt(paramIndex++, storeId);
            }

            if (startDate != null && !startDate.isEmpty()) {
                stCount.setDate(paramIndex++, java.sql.Date.valueOf(startDate));
            }
            if (endDate != null && !endDate.isEmpty()) {
                stCount.setDate(paramIndex++, java.sql.Date.valueOf(endDate));
            }

            try (ResultSet rsCount = stCount.executeQuery()) {
                if (rsCount.next()) {
                    count = rsCount.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting debts: " + e.getMessage());
            e.printStackTrace();
        }  
        return count;
    }

    public List<DebtNote> searchDebts(String name, int storeId) {
        List<DebtNote> list = new ArrayList<>();

        String sqlDebt = """
        SELECT d.id, d.type, c.balance, d.image, d.description, d.created_at, d.updated_at, 
               d.created_by, d.status, c.id AS customer_id, c.name, c.phone, c.address 
        FROM Debt_note d 
        JOIN Customers c ON d.customers_id = c.id 
        WHERE d.store_id = ? 
        AND d.created_at = (SELECT MAX(dn.created_at) FROM Debt_note dn WHERE dn.customers_id = d.customers_id AND dn.store_id = ?) 
        AND (c.name LIKE ? OR c.phone LIKE ?)
    """;

        try (PreparedStatement st = connection.prepareStatement(sqlDebt)) {
            st.setInt(1, storeId);
            st.setInt(2, storeId); // Đảm bảo chỉ lấy dữ liệu trong store
            st.setString(3, "%" + name + "%");
            st.setString(4, "%" + name + "%");

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

    public boolean insertDebt(DebtNote debts, int storeID) {
        String insertQuery = "INSERT INTO Debt_note (type, amount, image, description, customers_id, created_at, updated_at, created_by, status, store_id) \n"
                + "        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            ps.setInt(10, storeID);

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

    public boolean insertDebtInCustomer(DebtNote debts, int storeID) {
        String insertQuery = "INSERT INTO Debt_note (type, amount, image, description, customers_id, created_at, updated_at, created_by, status, store_id) \n"
                + "        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            ps.setInt(10, storeID);

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

    public int getTotalDebtCount(int storeID) {
        String sql = "SELECT COUNT(*) FROM Debt_note WHERE store_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, storeID);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("getTotalDebtCount: " + e.getMessage());
        } 
        return 0;
    }

    public List<DebtNote> getDebtsByDateRange(LocalDate startDate, LocalDate endDate, int offset, int limit, int storeID) {
        List<DebtNote> list = new ArrayList<>();
        String sql = "SELECT id, type, amount, description, created_at, status FROM Debt_note "
                + "WHERE store_id = ? AND DATE(created_at) BETWEEN ? AND ? "
                + "ORDER BY created_at DESC LIMIT ? OFFSET ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, storeID);
            st.setString(2, startDate.toString());
            st.setString(3, endDate.toString());
            st.setInt(4, limit);
            st.setInt(5, offset);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                DebtNote debt = new DebtNote();
                debt.setId(rs.getInt("id"));
                debt.setAmount(rs.getBigDecimal("amount"));
                debt.setType(rs.getString("type"));
                debt.setDescription(rs.getString("description"));
                debt.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                debt.setStatus(rs.getString("status"));
                list.add(debt);
            }
        } catch (SQLException e) {
            System.out.println("getDebtsByDateRange: " + e.getMessage());
        } 
        return list;
    }

    public List<DebtNote> getAllDebtsByDateRange(LocalDate startDate, LocalDate endDate, int storeID) {
        List<DebtNote> list = new ArrayList<>();
        String sql = "SELECT id, type, amount, description, created_at, status FROM Debt_note "
                + "WHERE store_id = ? AND DATE(created_at) BETWEEN ? AND ? "
                + "ORDER BY created_at DESC";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, storeID);
            st.setString(2, startDate.toString());
            st.setString(3, endDate.toString());

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                DebtNote debt = new DebtNote();
                debt.setId(rs.getInt("id"));
                debt.setAmount(rs.getBigDecimal("amount"));
                debt.setType(rs.getString("type"));
                debt.setDescription(rs.getString("description"));
                debt.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                debt.setStatus(rs.getString("status"));
                list.add(debt);
            }
        } catch (SQLException e) {
            System.out.println("getAllDebtsByDateRange: " + e.getMessage());
        } 
        return list;
    }

    public static void main(String[] args) {
        // Initialize the DAO (Data Access Object)
        String command = "created_at";
        int index = 1;
        int pageSize = 10;
        debtDAO dao = new debtDAO();

    }
}
