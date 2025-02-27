/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author THC
 */
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

    public List<Users> searchUsers(String keyword, int pageIndex, int pageSize) {
        List<Users> list = new ArrayList<>();
        String sql;
        if (keyword == null || keyword.trim().isEmpty()) {
            sql = "SELECT *"
                    + "FROM users "
                    + "ORDER BY id "
                    + "LIMIT ? OFFSET ?";
            try (PreparedStatement st = connection.prepareStatement(sql)) {
                st.setInt(1, pageSize);
                st.setInt(2, (pageIndex - 1) * pageSize);
                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToUser(rs));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            sql = "SELECT * "
                    + "FROM users "
                    + "WHERE name LIKE ? OR phone LIKE ? "
                    + "ORDER BY id "
                    + "LIMIT ? OFFSET ?";
            try (PreparedStatement st = connection.prepareStatement(sql)) {
                String param = "%" + keyword + "%";
                st.setString(1, param);
                st.setString(2, param);
                st.setInt(3, pageSize);
                st.setInt(4, (pageIndex - 1) * pageSize);
                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToUser(rs));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public Users getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
        String sql = "INSERT INTO users (username, password, image, name, phone, address, gender, dob, role, email, created_at, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?)";
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
            int rowAffected = st.executeUpdate();
            return rowAffected > 0;
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
        String sql = "UPDATE users SET name = ?, phone = ?, address = ?, gender = ?, dob = ?, image = ? WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, user.getName());
            st.setString(2, user.getPhone());
            st.setString(3, user.getAddress());
            st.setString(4, user.getGender());
            st.setDate(5, new java.sql.Date(user.getDob().getTime())); // Chuyển đổi từ java.util.Date sang java.sql.Date
            st.setString(6, user.getImage());
            st.setInt(7, user.getId());

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

    public static void main(String[] args) {
        userDAO dao = new userDAO();

        int pageIndex = 1;
        String keyword = null;
        List<Users> users = dao.searchUsers(keyword, pageIndex, pageIndex);

        if (users.isEmpty()) {
            System.out.println("Không có người dùng nào được tìm thấy!");
        } else {
            System.out.println("Danh sách người dùng:");
            for (Users user : users) {
                System.out.println("------------------------------------------------------");
                System.out.println("User ID: " + user.getId());

                System.out.println("Name: " + user.getName());
                System.out.println("Phone: " + user.getPhone());
                System.out.println("Address: " + user.getAddress());
                System.out.println("Email: " + user.getEmail());
                System.out.println("Role: " + user.getRole());
                System.out.println("Status: " + user.getStatus());
                System.out.println("Created At: " + user.getCreatedAt());
            }
        }
    }
}
