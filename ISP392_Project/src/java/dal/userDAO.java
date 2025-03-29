/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author THC
 */
import entity.Stores;
import entity.Users;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class userDAO extends DBContext {

    public List<Users> viewAllUsers(int index) {
        List<Users> usersList = new ArrayList<>();
        String sqlUsers = "SELECT * FROM users ORDER BY id LIMIT 10 OFFSET ?";

        try (PreparedStatement st = connection.prepareStatement(sqlUsers)) {
            st.setInt(1, (index - 1) * 10);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Users user = mapResultSetToUser(rs);
                    usersList.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usersList;
    }

    public int countUsers(String keyword, String statusFilter, int storeID) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM users WHERE store_id = ?");

        // Thêm điều kiện tìm kiếm nếu có từ khóa
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (name LIKE ? OR phone LIKE ?)");
        }

        // Thêm bộ lọc trạng thái
        if (statusFilter != null && !statusFilter.equals("all")) {
            sql.append(" AND status = ?");
        }

        try (PreparedStatement st = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            st.setInt(paramIndex++, storeID);

            // Nếu có từ khóa, thêm tham số tìm kiếm vào PreparedStatement
            if (keyword != null && !keyword.trim().isEmpty()) {
                String param = "%" + keyword + "%";
                st.setString(paramIndex++, param);
                st.setString(paramIndex++, param);
            }

            // Nếu có bộ lọc trạng thái, thêm tham số vào PreparedStatement
            if (statusFilter != null && !statusFilter.equals("all")) {
                st.setString(paramIndex++, statusFilter);
            }

            // Thực thi câu lệnh SQL và lấy kết quả
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void deactivateUser(int userId) {
        String sql = "UPDATE users SET status = 'Inactive' WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, userId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void activateUser(int userId) {
        String sql = "UPDATE users SET status = 'Active' WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, userId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Users> searchUsers(String keyword, int pageIndex, int pageSize, String sortBy, String sortOrder, int storeID, String statusFilter) {
        List<Users> list = new ArrayList<>();
        List<String> allowedSortColumns = List.of("id", "role", "name", "phone", "address", "gender", "dob", "email", "status");

        // Kiểm tra cột sắp xếp hợp lệ
        if (sortBy == null || !allowedSortColumns.contains(sortBy)) {
            sortBy = "id";
        }

        // Kiểm tra thứ tự sắp xếp hợp lệ
        if (sortOrder == null || (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC"))) {
            sortOrder = "ASC";
        }

        // Bắt đầu câu lệnh SQL
        StringBuilder sql = new StringBuilder("SELECT * FROM users WHERE store_id = ?");

        // Thêm điều kiện tìm kiếm nếu có từ khóa
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (name LIKE ? OR phone LIKE ?)");
        }

        // Thêm bộ lọc trạng thái
        if (statusFilter != null && !statusFilter.equals("all")) {
            sql.append(" AND status = ?");
        }

        // Thêm phần sắp xếp và phân trang
        sql.append(" ORDER BY ").append(sortBy).append(" ").append(sortOrder);
        sql.append(" LIMIT ? OFFSET ?");

        try (PreparedStatement st = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            // Gán giá trị cho storeID
            st.setInt(paramIndex++, storeID);

            // Nếu có từ khóa, thêm tham số tìm kiếm vào PreparedStatement
            if (keyword != null && !keyword.trim().isEmpty()) {
                String param = "%" + keyword + "%";
                st.setString(paramIndex++, param);
                st.setString(paramIndex++, param);
            }

            // Nếu có bộ lọc trạng thái, thêm tham số vào PreparedStatement
            if (statusFilter != null && !statusFilter.equals("all")) {
                st.setString(paramIndex++, statusFilter);
            }

            // Thêm tham số phân trang
            st.setInt(paramIndex++, pageSize);
            st.setInt(paramIndex, (pageIndex - 1) * pageSize);

            // Thực thi câu lệnh và xử lý kết quả
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Users user = mapResultSetToUser(rs);  // Hàm này giả sử đã được định nghĩa để chuyển ResultSet thành đối tượng User
                    list.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Method to count users by specific role
    public int countUsersByRole(String role, String keyword, String statusFilter) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Users u WHERE 1=1 ";

            // Add role filter
            sql += " AND u.role = ? ";

            // Add keyword filter if not empty
            if (!keyword.isEmpty()) {
                sql += " AND (u.name LIKE ? OR u.email LIKE ? OR u.phone LIKE ?) ";
            }

            // Add status filter
            if (!"all".equals(statusFilter)) {
                sql += " AND u.status = ? ";
            }

            PreparedStatement ps = connection.prepareStatement(sql);
            int paramIndex = 1;

            // Set role parameter
            ps.setString(paramIndex++, role);

            // Set keyword parameters if not empty
            if (!keyword.isEmpty()) {
                String searchParam = "%" + keyword + "%";
                ps.setString(paramIndex++, searchParam);
                ps.setString(paramIndex++, searchParam);
                ps.setString(paramIndex++, searchParam);
            }

            // Set status parameter
            if (!"all".equals(statusFilter)) {
                ps.setString(paramIndex++, statusFilter);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

// Modify existing searchUsersByRole method or add a new one
    public List<Users> searchUsersByRole(String role, String keyword, int index, int pageSize,
            String sortBy, String sortOrder, int storeId, String statusFilter) {
        List<Users> list = new ArrayList<>();
        try {
            String sql = "SELECT u.* FROM Users u WHERE 1=1 ";

            // Add role filter
            sql += " AND u.role = ? ";

            // Add keyword filter if not empty
            if (!keyword.isEmpty()) {
                sql += " AND (u.name LIKE ? OR u.email LIKE ? OR u.phone LIKE ?) ";
            }

            // Add status filter
            if (!"all".equals(statusFilter)) {
                sql += " AND u.status = ? ";
            }

            // Add sorting
            sql += " ORDER BY " + (sortBy.equals("name") ? "u.name" : "u.id") + " " + sortOrder;

            // Add pagination
            sql += " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

            PreparedStatement ps = connection.prepareStatement(sql);
            int paramIndex = 1;

            // Set role parameter
            ps.setString(paramIndex++, role);

            // Set keyword parameters if not empty
            if (!keyword.isEmpty()) {
                String searchParam = "%" + keyword + "%";
                ps.setString(paramIndex++, searchParam);
                ps.setString(paramIndex++, searchParam);
                ps.setString(paramIndex++, searchParam);
            }

            // Set status parameter
            if (!"all".equals(statusFilter)) {
                ps.setString(paramIndex++, statusFilter);
            }

            // Set pagination parameters
            ps.setInt(paramIndex++, (index - 1) * pageSize);
            ps.setInt(paramIndex++, pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Users user = new Users();
                // Populate user object from ResultSet
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setGender(rs.getString("gender"));
                user.setDob(rs.getDate("dob"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));

                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Users getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Users user = mapResultSetToUser(rs);

                    // Kiểm tra xem storeId có null không trước khi truy cập
                    if (user.getStoreId() != null) {
                        // Lấy thông tin cửa hàng từ bảng Stores
                        Stores store = getStoreById(user.getStoreId().getId());
                        user.setStoreId(store); // Gán thông tin cửa hàng vào người dùng
                    } else {
                        System.out.println("Store ID is null for the user.");
                        // Xử lý khi không có storeId, ví dụ: trả về lỗi hoặc thông báo cho người dùng
                    }

                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Stores getStoreById(int storeId) {
        String sql = "SELECT * FROM stores WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, storeId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStore(rs);  // Trả về đối tượng Store đầy đủ
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Stores getStoreIDByUser(int userId) {
        Stores store = null;
        String sql = "SELECT store_id FROM users WHERE user_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            // Set giá trị cho parameter user_id
            st.setInt(1, userId);
            // Thực thi truy vấn
            ResultSet rs = st.executeQuery();
            // Nếu có kết quả, lấy store_id và tìm thông tin cửa hàng
            if (rs.next()) {
                int storeId = rs.getInt("store_id");
                store = getStoreById(storeId);  // Lấy thông tin cửa hàng đầy đủ
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return store;  // Trả về đối tượng cửa hàng đầy đủ
    }

    public Users getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertUsers(Users user) {
        // Câu lệnh SQL không gán store_id nếu storeId là null
        String sql = "INSERT INTO users (username, password, image, name, phone, address, gender, dob, role, email, created_at, status"
                + (user.getStoreId() != null ? ", store_id" : "") + ") "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?"
                + (user.getStoreId() != null ? ", ?" : "") + ")";  // Chỉ thêm store_id vào VALUES khi storeId không phải null

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, user.getUsername());
            st.setString(2, user.getPassword());
            st.setString(3, user.getImage());
            st.setString(4, user.getName());
            st.setString(5, user.getPhone());
            st.setString(6, user.getAddress());
            st.setString(7, user.getGender());
            if (user.getDob() != null) {
                st.setDate(8, new java.sql.Date(user.getDob().getTime()));
            } else {
                st.setNull(8, java.sql.Types.DATE);
            }
            st.setString(9, user.getRole());
            st.setString(10, user.getEmail());
            st.setString(11, user.getStatus());

            if (user.getStoreId() != null) {
                st.setInt(12, user.getStoreId().getId());
            }

            int rowAffected = st.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUser(Users user) {
        String sql = "UPDATE users SET store_id = ? WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {

            if (user.getStoreId() != null) {
                st.setInt(1, user.getStoreId().getId());
            } else {
                st.setNull(1, java.sql.Types.INTEGER);
            }
            st.setInt(2, user.getId());

            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isStoreIdValid(int storeId) {
        String sql = "SELECT COUNT(*) FROM stores WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, storeId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean editUser(Users user) {
        String sql = "UPDATE users SET name = ?, phone = ?, address = ?, gender = ?, dob = ?, role = ?, email = ?, updated_at = NOW(), status = ? "
                + "WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, user.getName());
            st.setString(2, user.getPhone());
            st.setString(3, user.getAddress());
            st.setString(4, user.getGender());
            st.setDate(5, new java.sql.Date(user.getDob().getTime()));
            st.setString(6, user.getRole());
            st.setString(7, user.getEmail());
            st.setString(8, user.getStatus());
            st.setInt(9, user.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkEmailExists(String email, int userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND id != ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, email);
            st.setInt(2, userId);
            ResultSet rs = st.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkPhoneExists(String phone, int userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE phone = ? AND id != ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, phone);
            st.setInt(2, userId);
            ResultSet rs = st.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkUsernameExists(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkEmailExists(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUserInfo(Users user) {
        String sql = "UPDATE users SET name = ?, phone = ?, email = ?, address = ?, gender = ?, dob = ?, image = ? WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, user.getName());
            st.setString(2, user.getPhone());
            st.setString(3, user.getEmail());
            st.setString(4, user.getAddress());
            st.setString(5, user.getGender());
            st.setDate(6, new java.sql.Date(user.getDob().getTime()));
            st.setString(7, user.getImage());
            st.setInt(8, user.getId());

            int rowAffected = st.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Users mapResultSetToUser(ResultSet rs) throws SQLException {
        return Users.builder()
                .id(rs.getInt("id"))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .name(rs.getString("name"))
                .phone(rs.getString("phone"))
                .address(rs.getString("address"))
                .gender(rs.getString("gender"))
                .dob(rs.getDate("dob"))
                .role(rs.getString("role"))
                .email(rs.getString("email"))
                .createdAt(rs.getDate("created_at"))
                .updatedAt(rs.getDate("updated_at"))
                .status(rs.getString("status"))
                .build();
    }

    // Phương thức ánh xạ ResultSet thành đối tượng Store
    private Stores mapResultSetToStore(ResultSet rs) throws SQLException {
        return Stores.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .address(rs.getString("address"))
                .createdAt(rs.getDate("created_at"))
                .updatedAt(rs.getDate("updated_at"))
                .status(rs.getString("status"))
                .build();
    }

    public static void main(String[] args) {
        userDAO dao = new userDAO();

    }
}
