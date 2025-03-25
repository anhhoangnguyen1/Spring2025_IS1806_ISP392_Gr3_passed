/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;
import entity.StockCheck;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author bsd12418
 */
public class stockCheckDAO extends DBContext {

    // Create
    public void createStockCheck(StockCheck stockCheck) throws SQLException {
        String sql = "INSERT INTO StockChecks (zoneId, productId, checkedDate, actualQuantity, recordedQuantity, discrepancy, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, stockCheck.getZoneId());
            ps.setInt(2, stockCheck.getProductId());
            ps.setDate(3, (Date) stockCheck.getCheckedDate());
            ps.setInt(4, stockCheck.getActualQuantity());
            ps.setInt(5, stockCheck.getRecordedQuantity());
            ps.setInt(6, stockCheck.getDiscrepancy());
            ps.setString(7, stockCheck.getNotes());
            ps.executeUpdate();
        }
    }

    // Read
    public List<StockCheck> getAllStockChecks(String sortBy, String order, String search) throws SQLException {
        List<StockCheck> stockChecks = new ArrayList<>();
        String sql = "SELECT * FROM StockChecks WHERE notes LIKE ? ORDER BY " + sortBy + " " + order;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + (search != null ? search : "") + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                StockCheck stockCheck = new StockCheck();
                stockCheck.setStockCheckId(rs.getInt("stockCheckId"));
                stockCheck.setZoneId(rs.getInt("zoneId"));
                stockCheck.setProductId(rs.getInt("productId"));
                stockCheck.setCheckedDate(rs.getTimestamp("checkedDate"));
                stockCheck.setActualQuantity(rs.getInt("actualQuantity"));
                stockCheck.setRecordedQuantity(rs.getInt("recordedQuantity"));
                stockCheck.setDiscrepancy(rs.getInt("discrepancy"));
                stockCheck.setNotes(rs.getString("notes"));
                stockChecks.add(stockCheck);
            }
        }
        return stockChecks;
    }

    // Update (chỉ sửa notes)
    public void updateStockCheck(StockCheck stockCheck) throws SQLException {
        String sql = "UPDATE StockChecks SET notes = ? WHERE stockCheckId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, stockCheck.getNotes());
            ps.setInt(2, stockCheck.getStockCheckId());
            ps.executeUpdate();
        }
    }

    // Delete
    public void deleteStockCheck(int stockCheckId) throws SQLException {
        String sql = "DELETE FROM StockChecks WHERE stockCheckId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, stockCheckId);
            ps.executeUpdate();
        }
    }

    // Get by ID
    public StockCheck getStockCheckById(int stockCheckId) throws SQLException {
        StockCheck stockCheck = null;
        String sql = "SELECT * FROM StockChecks WHERE stockCheckId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, stockCheckId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                stockCheck = new StockCheck();
                stockCheck.setStockCheckId(rs.getInt("stockCheckId"));
                stockCheck.setZoneId(rs.getInt("zoneId"));
                stockCheck.setProductId(rs.getInt("productId"));
                stockCheck.setCheckedDate(rs.getTimestamp("checkedDate"));
                stockCheck.setActualQuantity(rs.getInt("actualQuantity"));
                stockCheck.setRecordedQuantity(rs.getInt("recordedQuantity"));
                stockCheck.setDiscrepancy(rs.getInt("discrepancy"));
                stockCheck.setNotes(rs.getString("notes"));
            }
        }
        return stockCheck;
    }
}
