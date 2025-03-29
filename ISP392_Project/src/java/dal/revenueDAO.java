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
    public double getRevenueToday() {
        String sql = "SELECT COALESCE(SUM(TotalAmount), 0) FROM Orders WHERE DATE(OrderDate) = CURDATE()";
        return getSingleRevenue(sql);
    }

    // Số lượng đơn hàng hôm nay
    public int getOrderCountToday() {
        String sql = "SELECT COUNT(*) FROM Orders WHERE DATE(OrderDate) = CURDATE()";
        return getSingleCount(sql);
    }

    // Doanh thu hôm qua
    public double getRevenueYesterday() {
        String sql = "SELECT COALESCE(SUM(TotalAmount), 0) FROM Orders WHERE DATE(OrderDate) = CURDATE() - INTERVAL 1 DAY";
        return getSingleRevenue(sql);
    }

    // Doanh thu 7 ngày qua
    public List<Revenue> getRevenueLast7Days() {
        String sql = "SELECT DATE(OrderDate) AS sale_date, COALESCE(SUM(TotalAmount), 0) AS revenue "
                + "FROM Orders "
                + "WHERE OrderDate >= CURDATE() - INTERVAL 6 DAY "
                + "GROUP BY DATE(OrderDate) ORDER BY sale_date";
        return getRevenueList(sql);
    }

    // Doanh thu từng ngày trong tháng này
    public List<Revenue> getRevenueThisMonth() {
        String sql = "SELECT DATE(OrderDate) AS sale_date, COALESCE(SUM(TotalAmount), 0) AS revenue_this_month "
                + "FROM Orders "
                + "WHERE YEAR(OrderDate) = YEAR(CURDATE()) AND MONTH(OrderDate) = MONTH(CURDATE()) "
                + "GROUP BY DATE(OrderDate) ORDER BY sale_date";
        return getRevenueList(sql);
    }

    // Doanh thu từng ngày trong tháng trước
    public List<Revenue> getRevenueLastMonth() {
        String sql = "SELECT DATE(OrderDate) AS sale_date, COALESCE(SUM(TotalAmount), 0) AS revenue_last_month "
                + "FROM Orders "
                + "WHERE YEAR(OrderDate) = YEAR(CURDATE() - INTERVAL 1 MONTH) "
                + "AND MONTH(OrderDate) = MONTH(CURDATE() - INTERVAL 1 MONTH) "
                + "GROUP BY DATE(OrderDate) ORDER BY sale_date";
        return getRevenueList(sql);
    }

    // Phương thức lấy 1 giá trị doanh thu duy nhất
    private double getSingleRevenue(String sql) {
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Phương thức lấy danh sách doanh thu theo ngày
    private List<Revenue> getRevenueList(String sql) {
        List<Revenue> list = new ArrayList<>();
        try {
            ps = connection.prepareStatement(sql);
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
    private int getSingleCount(String sql) {
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
        revenueDAO dao = new revenueDAO();
        System.out.println("Revenue Today: " + dao.getRevenueToday());
        System.out.println("Invoice Count Today: " + dao.getOrderCountToday());
//        System.out.println("Revenue Yesterday: " + dao.getRevenueYesterday());

//        System.out.println("Revenue Last 7 Days:");
//        for (Revenue r : dao.getRevenueLast7Days()) {
//            System.out.println(r.getSaleDate() + " - " + r.getRevenue());
//        }
//
//        System.out.println("Revenue This Month:");
//        for (Revenue r : dao.getRevenueThisMonth()) {
//            System.out.println(r.getSaleDate() + " - " + r.getRevenue());
//        }
//
//        System.out.println("Revenue Last Month:");
//        for (Revenue r : dao.getRevenueLastMonth()) {
//            System.out.println(r.getSaleDate() + " - " + r.getRevenue());
//        }
    }
}
