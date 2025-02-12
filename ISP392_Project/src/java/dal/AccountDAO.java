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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class AccountDAO extends DBContext implements I_DAO<Users> {

    public static AccountDAO INSTANCE = new AccountDAO();
    private static final Logger LOGGER = Logger.getLogger(AccountDAO.class.getName());

    public AccountDAO() {
        connection = getConnection();
    }

    // get username, password for authenticate
    public Users getUser(String username, String password) {
        String sql = "SELECT Users.id, Users.username, Users.password, Users.role "
                + "FROM Users "
                + "WHERE Users.username = ? AND Users.password = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Users login = new Users();
                    login.setUserId(rs.getInt("id"));
                    login.setUsername(rs.getString("username"));
                    login.setPassword(rs.getString("password"));
                    login.setRole("role");

                    return login;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error during authentication: {0}", e.getMessage());
        }
        return null;
    }



    @Override
    public List<Users> findAll() {
        List<Users> users = new ArrayList<>();
        String sql = "SELECT * FROM Users"; // Select all users

        try {
            // Open connection
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(getFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all users: {0}", e.getMessage());
        } finally {
            // Close resources
            closeResources();
        }
        return users;
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

//        //find role
//        Role role = new Role();
//        role.setRoleId(resultSet.getInt("role_id"));

        Users user = new Users();
        user.setUserId(resultSet.getInt("user_id"));
        user.setRoleId(resultSet.getString("role"));

        user.setRole(roleDAO.findById(role));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        user.setPhone(resultSet.getString("phone"));
        user.setCreatedAt(resultSet.getTimestamp("created_at") != null
                ? resultSet.getTimestamp("created_at").toLocalDateTime() : null);
        user.setUpdatedAt(resultSet.getTimestamp("updated_at") != null
                ? resultSet.getTimestamp("updated_at").toLocalDateTime() : null);
        user.setUpdatedBy(resultSet.getString("updated_by"));
        user.setCreatedBy(resultSet.getString("created_by"));
        user.setDelete(resultSet.getBoolean("isDelete"));
        user.setDeletedBy(resultSet.getString("deletedBy"));
        user.setDeletedAt(resultSet.getTimestamp("deletedAt") != null
                ? resultSet.getTimestamp("deletedAt").toLocalDateTime() : null);
        user.setStatus(resultSet.getString("status"));

        return user;
    }

    public Users findByEmail(Users userRequestEmail) {
        List<Users> users = new ArrayList<>();
        String sql = "SELECT * FROM Users\n"
                + "where email = ?";

        try {
            // Open connection
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setObject(1, userRequestEmail.getEmail());
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            closeResources();
        }
        return null;
    }

    public boolean updatePassword(Users account) {
        String sql = "UPDATE Users SET password = ? WHERE email = ?";
        try {
            // Open connection
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, account.getPassword());
            statement.setObject(2, account.getEmail());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating password: {0}", e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        AccountDAO dao = new AccountDAO();
        
        Users user1 = dao.getUser("admin1", "password123");
        if (user1 != null) {
            System.out.println("Đăng nhập thành công: " + user1.getUsername() + " - Role: " + user1.getRole());
        } else {
            System.out.println("Đăng nhập thất bại (admin, 123456)");
        }

    }
}
