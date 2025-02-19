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

    public int countUsers() {
        String sql = "SELECT COUNT(*) FROM users";
        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Users> searchUsers(String keyword) {
        List<Users> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE name LIKE ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, "%" + keyword + "%");
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToUser(rs));
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
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm người dùng mới vào cơ sở dữ liệu
    public boolean insertUsers(Users user) {
        String sql = "INSERT INTO users (username, password, name, phone, address, gender, dob, role, email, created_at, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, user.getUsername());
            st.setString(2, user.getPassword());
            st.setString(3, user.getName());
            st.setString(4, user.getPhone());
            st.setString(5, user.getAddress());
            st.setString(6, user.getGender());
            st.setDate(7, new java.sql.Date(user.getDob().getTime()));
            st.setString(8, user.getRole());
            st.setString(9, user.getEmail());
            st.setString(10, user.getStatus());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Chỉnh sửa thông tin người dùng
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
        List<Users> users = dao.viewAllUsers(pageIndex);

        if (users.isEmpty()) {
            System.out.println("Không có người dùng nào được tìm thấy!");
        } else {
            System.out.println("Danh sách người dùng:");
            for (Users user : users) {
                System.out.println("------------------------------------------------------");
                System.out.println("User ID: " + user.getId());
                System.out.println("Username: " + user.getUsername());
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
