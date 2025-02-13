/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import entity.Customers;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author phamh
 */
public class customerDAO extends DBContext{
    public List<String> getAllCustomerNames() {
    List<String> list = new ArrayList<>();
    String sql = "SELECT name FROM Customers"; 

    try (PreparedStatement st = connection.prepareStatement(sql);
         ResultSet rs = st.executeQuery()) {
        while (rs.next()) {
            list.add(rs.getString("name")); 
        }
    } catch (SQLException e) {
        System.out.println("Error fetching customer names: " + e.getMessage());
        e.printStackTrace();
    }

    return list;
}

}
