/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import entity.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class AccountDAO implements I_DAO<Users> {

    public static AccountDAO INSTANCE = new AccountDAO();
    private static final Logger LOGGER = Logger.getLogger(AccountDAO.class.getName());
    private DBContext dbContext;
    private Connection connection;

    public AccountDAO() {
        dbContext = new DBContext();
        connection = dbContext.connection;  // Lấy kết nối từ DBContext
    }

    // authenticate the login avaiable in the database
    public Users authenticate(String username, String password) {
        String sql = "SELECT Users.user_id, Users.username, Users.password, Role.name "
                + "FROM Users "
                + "JOIN Role ON Users.role_id = Role.role_id "
                + "WHERE Users.username = ? AND Users.password = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Users login = new Users();
                    login.setUserId(rs.getInt("user_id"));
                    login.setUsername(rs.getString("username"));
                    login.setPassword(rs.getString("password"));

                    Role role = new Role();
                    role.setName(rs.getString("name"));
                    login.setRole(role);

                    return login;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error during authentication: {0}", e.getMessage());
        }
        return null;
    }

    public Users getUserById(int userId) {
        String sql = "SELECT u.user_id, u.username, u.password, u.email, u.phone, "
                + "u.created_at, u.updated_at, u.updated_by, u.created_by, u.is_delete, "
                + "u.deleted_by, u.deleted_at, u.status, r.role_id, r.name "
                + "FROM Users u "
                + "JOIN Role r ON u.role_id = r.role_id "
                + "WHERE u.user_id = ? AND u.is_delete = 0";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Role role = new Role();
                    role.setRoleId(rs.getInt("role_id"));
                    role.setName(rs.getString("name"));
                    return new Users(
                            rs.getInt("user_id"),
                            role,
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("avatar"),
                            rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                            rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                            rs.getString("updated_by"),
                            rs.getString("created_by"),
                            rs.getBoolean("is_delete"),
                            rs.getString("deleted_by"),
                            rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null,
                            rs.getString("status"),
                            null // Staff list can be loaded separately if needed
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving user by ID: {0}", e.getMessage());
        }
        return null;
    }

        public boolean updateUser(Users user) {
    String sql = "UPDATE Users SET username = ?, email = ?, phone = ?, password = ?, avatar = ?, status = ?, updated_at = CURRENT_TIMESTAMP, updated_by = ? WHERE user_id = ? AND is_delete = 0";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getPhone());
        ps.setString(4, user.getPassword());
        ps.setString(5, user.getAvatar()); 
        ps.setString(6, user.getStatus());
        ps.setString(7, user.getUpdatedBy());
        ps.setInt(8, user.getUserId());
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error updating user: {0}", e.getMessage());
    }
    return false;
}

    @Override
    public List<Users> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean update(Users t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean delete(Users t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int insert(Users t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Users findById(Users t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Users getFromResultSet(ResultSet resultSet) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static void main(String[] args) {
<<<<<<< HEAD
//        AccountDAO dao = new AccountDAO();
//        
//        Users user1 = dao.authenticate("admin", "123456");
//        if (user1 != null) {
//            System.out.println("Đăng nhập thành công: " + user1.getUsername() + " - Role: " + user1.getRole().getName());
//        } else {
//            System.out.println("Đăng nhập thất bại (admin, 123456)");
//        }
=======
        AccountDAO dao = new AccountDAO();

        Users user1 = dao.authenticate("admin", "123456");
        if (user1 != null) {
            System.out.println("Đăng nhập thành công: " + user1.getUsername() + " - Role: " + user1.getRole().getName());
        } else {
            System.out.println("Đăng nhập thất bại (admin, 123456)");
        }
>>>>>>> 4e48a49a944e934af3f91ab1a119cef7dfd9a2e7
    }
}
