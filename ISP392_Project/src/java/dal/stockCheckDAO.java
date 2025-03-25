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

    // Create (Thêm mới bản ghi kiểm kho)
    public void addStockCheck(StockCheck sc) throws SQLException {
        String sql = "INSERT INTO StockChecks (zoneId, productId, checkedDate, actualQuantity, recordedQuantity, discrepancy, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, sc.getZoneId());
        ps.setInt(2, sc.getProductId());
        ps.setDate(3, new Date(sc.getCheckedDate().getTime()));
        ps.setInt(4, sc.getActualQuantity());
        ps.setInt(5, sc.getRecordedQuantity());
        ps.setInt(6, sc.getDiscrepancy());
        ps.setString(7, sc.getNotes());
        ps.executeUpdate();
    }

    // Read (Lấy danh sách kiểm kho)
    public List<StockCheck> getAllStockChecks() throws SQLException {
        List<StockCheck> list = new ArrayList<>();
        String sql = "SELECT * FROM StockChecks";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            StockCheck sc = new StockCheck();
            sc.setStockCheckId(rs.getInt("stockCheckId"));
            sc.setZoneId(rs.getInt("zoneId"));
            sc.setProductId(rs.getInt("productId"));
            sc.setCheckedDate(rs.getTimestamp("checkedDate"));
            sc.setActualQuantity(rs.getInt("actualQuantity"));
            sc.setRecordedQuantity(rs.getInt("recordedQuantity"));
            sc.setDiscrepancy(rs.getInt("discrepancy"));
            sc.setNotes(rs.getString("notes"));
            list.add(sc);
        }
        return list;
    }

    // Update (Cập nhật bản ghi kiểm kho)
    public void updateStockCheck(StockCheck sc) throws SQLException {
        String sql = "UPDATE StockChecks SET zoneId=?, productId=?, checkedDate=?, actualQuantity=?, recordedQuantity=?, discrepancy=?, notes=? WHERE stockCheckId=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, sc.getZoneId());
        ps.setInt(2, sc.getProductId());
        ps.setDate(3, new Date(sc.getCheckedDate().getTime()));
        ps.setInt(4, sc.getActualQuantity());
        ps.setInt(5, sc.getRecordedQuantity());
        ps.setInt(6, sc.getDiscrepancy());
        ps.setString(7, sc.getNotes());
        ps.setInt(8, sc.getStockCheckId());
        ps.executeUpdate();
    }

    // Delete (Xóa bản ghi kiểm kho)
    public void deleteStockCheck(int stockCheckId) throws SQLException {
        String sql = "DELETE FROM StockChecks WHERE stockCheckId=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, stockCheckId);
        ps.executeUpdate();
    }

    // Lấy bản ghi theo ID
    public StockCheck getStockCheckById(int stockCheckId) throws SQLException {
        String sql = "SELECT * FROM StockChecks WHERE stockCheckId=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, stockCheckId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            StockCheck sc = new StockCheck();
            sc.setStockCheckId(rs.getInt("stockCheckId"));
            sc.setZoneId(rs.getInt("zoneId"));
            sc.setProductId(rs.getInt("productId"));
            sc.setCheckedDate(rs.getTimestamp("checkedDate"));
            sc.setActualQuantity(rs.getInt("actualQuantity"));
            sc.setRecordedQuantity(rs.getInt("recordedQuantity"));
            sc.setDiscrepancy(rs.getInt("discrepancy"));
            sc.setNotes(rs.getString("notes"));
            return sc;
        }
        return null;
    }
}
