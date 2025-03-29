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

    public List<Users> findAll() {
        List<Users> usersList = new ArrayList<>();
        String sqlUsers = "SELECT * FROM users";

        try (PreparedStatement st = connection.prepareStatement(sqlUsers); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Users user = mapResultSetToUser(rs);
                usersList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usersList;
    }

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

    public int countUsers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            String sql = "SELECT COUNT(*) FROM users";
            try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {

            String sql = "SELECT COUNT(*) FROM users WHERE name LIKE ? OR phone LIKE ?";
            try (PreparedStatement st = connection.prepareStatement(sql)) {
                String param = "%" + keyword + "%";
                st.setString(1, param);
                st.setString(2, param);

                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

    public List<Users> searchUsers(String keyword, int pageIndex, int pageSize, String sortBy, String sortOrder) {
        List<Users> list = new ArrayList<>();
        List<String> allowedSortColumns = List.of("id", "role", "name", "phone", "address", "gender", "dob", "email", "status");
        if (sortBy == null || !allowedSortColumns.contains(sortBy)) {
            sortBy = "id";
        }

        if (sortOrder == null || (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC"))) {
            sortOrder = "ASC";
        }
        String sql = "SELECT * FROM users ";
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += "WHERE name LIKE ? OR phone LIKE ? ";
        }
        sql += "ORDER BY " + sortBy + " " + sortOrder + " LIMIT ? OFFSET ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            int paramIndex = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                String param = "%" + keyword + "%";
                st.setString(paramIndex++, param);
                st.setString(paramIndex++, param);
            }
            st.setInt(paramIndex++, pageSize);
            st.setInt(paramIndex, (pageIndex - 1) * pageSize);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Users user = mapResultSetToUser(rs);
                    list.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
public List<Users> searchUsersByRole(String role, String keyword, int pageIndex, int pageSize, String sortBy, String sortOrder) {
    List<Users> list = new ArrayList<>();
    List<String> allowedSortColumns = List.of("id", "role", "name", "phone", "address", "gender", "dob", "email", "status");
    
    if (sortBy == null || !allowedSortColumns.contains(sortBy)) {
        sortBy = "id";
    }

    if (sortOrder == null || (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC"))) {
        sortOrder = "ASC";
    }

    // SQL truy vấn với điều kiện lọc theo role
    String sql = "SELECT * FROM users WHERE role = ? ";
    
    // Nếu có keyword, thêm điều kiện tìm kiếm theo tên hoặc số điện thoại
    if (keyword != null && !keyword.trim().isEmpty()) {
        sql += "AND (name LIKE ? OR phone LIKE ?) ";
    }
    
    // Sắp xếp theo cột và thứ tự
    sql += "ORDER BY " + sortBy + " " + sortOrder + " LIMIT ? OFFSET ?";

    try (PreparedStatement st = connection.prepareStatement(sql)) {
        int paramIndex = 1;
        
        // Gán giá trị cho role và keyword (nếu có)
        st.setString(paramIndex++, role);
        if (keyword != null && !keyword.trim().isEmpty()) {
            String param = "%" + keyword + "%";
            st.setString(paramIndex++, param);
            st.setString(paramIndex++, param);
        }
        
        // Gán giá trị cho pageSize và OFFSET (cho phân trang)
        st.setInt(paramIndex++, pageSize);
        st.setInt(paramIndex, (pageIndex - 1) * pageSize);

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

//    public String getStoreIDByUser(int userId) {
//        String storeID = null;
//        String sql = "SELECT store_id FROM users WHERE user_id = ?";
//
//        try (PreparedStatement st = connection.prepareStatement(sql)) {
//
//            // Set giá trị cho parameter user_id
//            st.setInt(1, userId);
//
//            // Thực thi truy vấn
//            ResultSet rs = st.executeQuery();
//
//            // Nếu có kết quả, lấy storeID
//            if (rs.next()) {
//                storeID = rs.getString("store_id");  // Lấy store_id từ kết quả
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();  // Bạn có thể xử lý lỗi theo cách của riêng bạn
//        }
//
//        return storeID;  // Trả về storeID của người dùng
//    }
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

            // Gán store_id nếu storeId không phải null
            if (user.getStoreId() != null) {
                st.setInt(12, user.getStoreId().getId());  // Gọi getId() của storeId nếu nó không phải null
            }

            // Thực thi câu lệnh SQL
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
            // Kiểm tra nếu storeId có giá trị, nếu không thì gán NULL
            if (user.getStoreId() != null) {
                st.setInt(1, user.getStoreId().getId());  // Lấy store ID nếu có
            } else {
                st.setNull(1, java.sql.Types.INTEGER);  // Gán NULL nếu không có store
            }
            st.setInt(2, user.getId());  // Lấy ID của người dùng

            return st.executeUpdate() > 0;  // Kiểm tra xem có cập nhật thành công không
        } catch (SQLException e) {
            e.printStackTrace();  // In lỗi nếu có
        }
        return false;  // Nếu có lỗi hoặc không cập nhật thành công
    }

    public boolean isStoreIdValid(int storeId) {
        String sql = "SELECT COUNT(*) FROM stores WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, storeId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true; // Nếu có kết quả trả về, tức là store_id hợp lệ
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Nếu không có kết quả, tức là store_id không hợp lệ
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

    // Kiểm tra xem tên người dùng đã tồn tại chưa
    public boolean checkUsernameExists(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Nếu có kết quả trả về thì username đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra xem email đã tồn tại chưa
    public boolean checkEmailExists(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Nếu có kết quả trả về thì email đã tồn tại
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
            st.setDate(6, new java.sql.Date(user.getDob().getTime())); // Chuyển đổi từ java.util.Date sang java.sql.Date
            st.setString(7, user.getImage()); // Lưu tên ảnh nếu có
            st.setInt(8, user.getId());

            int rowAffected = st.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Users getOwnerByStoreId(int storeId) {
        Users owner = null;
        String sql = "SELECT * FROM users WHERE store_id = ? AND role = 'owner'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, storeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                owner = new Users();
                owner.setId(rs.getInt("id"));
                owner.setName(rs.getString("name"));
                // set other fields as needed...
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return owner;
    }

    public List<Users> getOwnersWithoutStore() {
        List<Users> owners = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE store_id IS NULL AND role = 'owner'";

        try (PreparedStatement pst = connection.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Users user = new Users();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                owners.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return owners;
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
//
//        int pageIndex = 1;
//        String keyword = null;
//        List<Users> users = dao.searchUsers(keyword, pageIndex, pageIndex);
//
//        if (users.isEmpty()) {
//            System.out.println("Không có người dùng nào được tìm thấy!");
//        } else {
//            System.out.println("Danh sách người dùng:");
//            for (Users user : users) {
//                System.out.println("------------------------------------------------------");
//                System.out.println("User ID: " + user.getId());
//
//                System.out.println("Name: " + user.getName());
//                System.out.println("Phone: " + user.getPhone());
//                System.out.println("Address: " + user.getAddress());
//                System.out.println("Email: " + user.getEmail());
//                System.out.println("Role: " + user.getRole());
//                System.out.println("Status: " + user.getStatus());
//                System.out.println("Created At: " + user.getCreatedAt());
//            }
//        }

    }
}
