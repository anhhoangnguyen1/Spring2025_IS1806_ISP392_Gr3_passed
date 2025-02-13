/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import entity.Users;
import java.util.logging.Logger;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author THC
 */
public class profileDAO extends DBContext {

    public static final profileDAO INSTANCE = new profileDAO();
    private static final Logger LOGGER = Logger.getLogger(profileDAO.class.getName());
    private Connection connection;

    public profileDAO() {
        connection = getConnection();
    }

    public Users getUserById(int userId) {
        Users user = null;
        String sql = "SELECT * FROM users WHERE id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            if (rs.next()) {
                user = Users.builder()
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
                        .createdAt(rs.getDate("createdAt"))
                        .updatedAt(rs.getDate("updatedAt"))
                        .status(rs.getString("status"))
                        .deletedAt(rs.getDate("deletedAt"))
                        .image(rs.getString("image")) // Nếu database có avatar
                        .build();
            }
        } catch (SQLException e) {
            LOGGER.severe("Error" + e.getMessage());
        } finally {
            closeResources(ps, rs);
        }
        return user;
    }

    public String getAvatarByUserId(int userId) {
        String avatarPath = "/avatars/default-avatar.png"; 
        String sql = "SELECT avatar FROM users WHERE id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            if (rs.next() && rs.getString("avatar") != null) {
                avatarPath = "/avatars/" + rs.getString("avatar");
            }
        } catch (SQLException e) {
            LOGGER.severe("Error" + e.getMessage());
        } finally {
            closeResources(ps, rs);
        }
        return avatarPath;
    }
    
    public boolean updateUser(Users user) {
    String sql = "UPDATE users SET name = ?, email = ?, phone = ?, avatar = ? WHERE id = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, user.getName());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getPhone());
        ps.setString(4, user.getImage());
        ps.setInt(5, user.getId());
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}


    /**
     * Đóng các tài nguyên (PreparedStatement, ResultSet) sau khi truy vấn
     */
    private void closeResources(PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            LOGGER.severe("Error" + e.getMessage());
        }
    }
}

