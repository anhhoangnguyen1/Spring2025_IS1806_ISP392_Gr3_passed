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
        // No longer initializing connection here
    }

    // get username, password for authenticate
    public Users getUser(String username, String password) {
        try {
            String sql = "SELECT Users.id, Users.username, Users.password, Users.role "
                    + "FROM Users "
                    + "WHERE Users.username = ? AND Users.password = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Users login = new Users();
                login.setId(resultSet.getInt("id"));
                login.setUsername(resultSet.getString("username")); 
                login.setPassword(resultSet.getString("password"));
                login.setRole(resultSet.getString("role"));
                return login;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error during authentication: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return null;
    }

    @Override
    public List<Users> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
//        List<Users> users = new ArrayList<>();
//        String sql = "SELECT * FROM Users"; // Select all users
//  
//        try {
//            // Open connection
//            connection = getConnection();
//            statement = connection.prepareStatement(sql);
//            resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                users.add(getFromResultSet(resultSet));
//            }
//        } catch (SQLException e) {
//            LOGGER.log(Level.SEVERE, "Error retrieving all users: {0}", e.getMessage());
//        } finally {
//            // Close resources
//            closeResources();
//        }
//        return users;
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
        return new Users(
            resultSet.getInt("id"),
            resultSet.getString("username"), 
            resultSet.getString("password"),
            resultSet.getString("image"),
            resultSet.getString("name"),
            resultSet.getString("phone"),
            resultSet.getString("address"), 
            resultSet.getString("gender"),
            resultSet.getDate("dob"),
            resultSet.getString("role"),
            resultSet.getString("email"),
            resultSet.getDate("created_at"),
            resultSet.getDate("updated_at"),
            resultSet.getBoolean("isDeleted"),
            resultSet.getString("status"),
            resultSet.getDate("deletedAt"),
            null // Invoices list needs to be populated separately
        );
    }

    public Users findByEmail(Users userRequestEmail) {
        
        try {
            connection = getConnection();
            String sql = "SELECT * FROM Users WHERE email = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, userRequestEmail.getEmail());
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding user by email: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return null;
    }

    public boolean updatePassword(Users account) {
        try {
            connection = getConnection();
            String sql = "UPDATE Users SET password = ? WHERE email = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, account.getPassword());
            statement.setString(2, account.getEmail());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating password: {0}", e.getMessage());
        } finally {
            closeResources();
        }
        return false;
    }

    public static void main(String[] args) {
        AccountDAO dao = new AccountDAO();
        
        System.out.println(dao.updatePassword(Users.builder().email("4userfpt01@gmail.com").password("123456").build()));
    }
}
