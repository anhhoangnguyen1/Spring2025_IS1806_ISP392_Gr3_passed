/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import entity.Revenue;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author bsd12418
 */
public class revenueDAO extends DBContext {

    private PreparedStatement ps;
    private ResultSet rs;

    // Doanh thu hôm nay
    public double getRevenueToday(int storeId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM Orders WHERE DATE(created_at) = CURDATE() AND type = 'Export' AND store_id = ?";
        return getSingleRevenue(sql, storeId);
    }

    // Số lượng đơn hàng hôm nay
    public int getOrderCountToday(int storeId) {
        String sql = "SELECT COUNT(*) FROM Orders WHERE DATE(created_at) = CURDATE() AND type = 'Export' AND store_id = ?";
        return getSingleCount(sql, storeId);
    }

    // Doanh thu hôm qua
    public double getRevenueYesterday(int storeId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM Orders WHERE DATE(created_at) = DATE_SUB(CURDATE(), INTERVAL 1 DAY) AND type = 'Export' AND store_id = ?";
        return getSingleRevenue(sql, storeId);
    }

    // Doanh thu 7 ngày qua
    public List<Revenue> getRevenueLast7Days(int storeId) {
        String sql = "SELECT DATE(created_at) AS sale_date, COALESCE(SUM(amount), 0) AS revenue "
                + "FROM Orders "
                + "WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) "
                + "AND type = 'Export' "
                + "AND store_id = ? "
                + "GROUP BY DATE(created_at) ORDER BY sale_date";
        return getRevenueList(sql, storeId);
    }

    // Doanh thu từng ngày trong tháng này
    public List<Revenue> getRevenueThisMonth(int storeId) {
        String sql = "SELECT DATE(OrderDate) AS sale_date, COALESCE(SUM(TotalAmount), 0) AS revenue_this_month "
                + "FROM Orders "
                + "WHERE YEAR(OrderDate) = YEAR(CURDATE()) AND MONTH(OrderDate) = MONTH(CURDATE()) "
                + "AND store_id = ? "
                + "GROUP BY DATE(OrderDate) ORDER BY sale_date";
        return getRevenueList(sql, storeId);
    }

    // Doanh thu từng ngày trong tháng trước
    public List<Revenue> getRevenueLastMonth(int storeId) {
        String sql = "SELECT DATE(OrderDate) AS sale_date, COALESCE(SUM(TotalAmount), 0) AS revenue_last_month "
                + "FROM Orders "
                + "WHERE YEAR(OrderDate) = YEAR(CURDATE() - INTERVAL 1 MONTH) "
                + "AND MONTH(OrderDate) = MONTH(CURDATE() - INTERVAL 1 MONTH) "
                + "AND store_id = ? "
                + "GROUP BY DATE(OrderDate) ORDER BY sale_date";
        return getRevenueList(sql, storeId);
    }

    // Doanh thu nhập hàng hôm nay
    public double getImportRevenueToday(int storeId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM Orders WHERE DATE(created_at) = CURDATE() AND type = 'Import' AND store_id = ?";
        return getSingleRevenue(sql, storeId);
    }

    // Số lượng đơn nhập hàng hôm nay
    public int getImportOrderCountToday(int storeId) {
        String sql = "SELECT COUNT(*) FROM Orders WHERE DATE(created_at) = CURDATE() AND type = 'Import' AND store_id = ?";
        return getSingleCount(sql, storeId);
    }

    // Phương thức lấy 1 giá trị doanh thu duy nhất
    private double getSingleRevenue(String sql, int storeId) {
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, storeId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            System.out.println("Error in getSingleRevenue: " + e.getMessage());
        }
        return 0;
    }

    // Phương thức lấy danh sách doanh thu theo ngày
    private List<Revenue> getRevenueList(String sql, int storeId) {
        List<Revenue> list = new ArrayList<>();
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, storeId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Revenue(rs.getString(1), rs.getDouble(2)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Phương thức lấy 1 số lượng duy nhất
    private int getSingleCount(String sql, int storeId) {
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, storeId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error in getSingleCount: " + e.getMessage());
        }
        return 0;
    }

    public void insertSampleOrders() {
        String sql = "INSERT INTO Orders (customers_id, store_id, user_id, type, amount, paidAmount, created_at, created_by, status) VALUES "
                + "(1, 1, 2, 'Export', 1000000, 1000000, CURDATE(), 'System', 'Completed'),"
                + "(2, 1, 3, 'Export', 2000000, 2000000, CURDATE(), 'System', 'Completed'),"
                + "(3, 1, 2, 'Export', 1500000, 1500000, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'System', 'Completed'),"
                + "(4, 1, 3, 'Export', 2500000, 2500000, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'System', 'Completed'),"
                + "(5, 1, 2, 'Export', 1800000, 1800000, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'System', 'Completed')";
        
        try {
            ps = connection.prepareStatement(sql);
            ps.executeUpdate();
            System.out.println("Sample orders inserted successfully");
        } catch (Exception e) {
            System.out.println("Error inserting sample orders: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        revenueDAO dao = new revenueDAO();
        dao.insertSampleOrders();
        
        System.out.println("Revenue today: " + dao.getRevenueToday(1));
        System.out.println("Order count today: " + dao.getOrderCountToday(1));
        System.out.println("Revenue yesterday: " + dao.getRevenueYesterday(1));
        
        List<Revenue> last7Days = dao.getRevenueLast7Days(1);
        System.out.println("Revenue last 7 days:");
        for (Revenue r : last7Days) {
            System.out.println(r.getSaleDate() + ": " + r.getRevenue());
        }
    }
}
