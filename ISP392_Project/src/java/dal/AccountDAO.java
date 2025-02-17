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
                    login.setId(rs.getInt("id"));
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
        String sql = "SELECT * FROM Users WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userRequestEmail.getEmail());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding user by email: {0}", e.getMessage());
        }
        return null;
    }

    public boolean updatePassword(Users account) {
        String sql = "UPDATE Users SET password = ? WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, account.getPassword());
            ps.setString(2, account.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating password: {0}", e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        AccountDAO dao = new AccountDAO();
        
        Users user1 = dao.getUser("admin", "password123");
        if (user1 != null) {
            System.out.println("Đăng nhập thành công: " + user1.getUsername() + " - Role: " + user1.getRole());
        } else {
            System.out.println("Đăng nhập thất bại (admin, 123456)");
        }

    }
}