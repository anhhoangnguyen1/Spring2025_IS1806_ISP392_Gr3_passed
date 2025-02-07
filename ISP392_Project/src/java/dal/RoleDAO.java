/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import entity.Role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class RoleDAO extends DBContext implements I_DAO<Role> {

    @Override
    public List<Role> findAll() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM Role"; // Assuming you have a Role table
        try {
            connection = new DBContext().connection;
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                roles.add(getFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return roles;
    }

    @Override
    public boolean update(Role t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean delete(Role t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int insert(Role t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Role findById(Role t) {
        Role role = null;
        String sql = "SELECT * FROM Role WHERE role_id = ?";
        try {
            // Open connection
            connection = new DBContext().connection;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, t.getRoleId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                role = getFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            closeResources();
        }
        return role;
    }

    @Override
    public Role getFromResultSet(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        role.setRoleId(resultSet.getInt("role_id"));
        role.setName(resultSet.getString("name"));
        return role;
    }
    
    public static void main(String[] args) {
        RoleDAO roleDAO = new RoleDAO();

        // Test findAll()
        System.out.println("Testing findAll():");
        List<Role> roles = roleDAO.findAll();
        for (Role role : roles) {
            System.out.println("Role ID: " + role.getRoleId() + ", Name: " + role.getName());
        }

        // Test findById()
        int testRoleId = 1; // Thay đổi giá trị này để kiểm tra với các role_id khác nhau
        System.out.println("\nTesting findById() with role_id = " + testRoleId + ":");
        Role role = roleDAO.findById(new Role(testRoleId, null, null)); // Truyền Role có ID cần tìm
        if (role != null) {
            System.out.println("Role Found -> Role ID: " + role.getRoleId() + ", Name: " + role.getName());
        } else {
            System.out.println("No Role found with role_id = " + testRoleId);
        }
    }

}
