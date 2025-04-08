package dal;

import entity.Orders;
import entity.Customers;
import entity.OrderDetails;
import entity.Stores;
import entity.Users;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdersDAO extends DBContext {

    public List<Orders> findAll() {
        List<Orders> ordersList = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE isDeleted = false";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Orders order = mapResultSetToOrder(rs);
                ordersList.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordersList;
    }

    public Orders mapResultSetToOrder(ResultSet rs) throws SQLException {
        return Orders.builder()
                .id(rs.getInt("id"))
                .customerID(Customers.builder()
                        .id(rs.getInt("customers_id"))
                        .name(rs.getString("customer_name")) // 👈 Thêm dòng này
                        .build())
                .userID(Users.builder()
                        .id(rs.getInt("user_id"))
                        .build())
                .storeId(Stores.builder()
                        .id(rs.getInt("store_id"))
                        .build())
                .type(rs.getString("type"))
                .amount(rs.getDouble("amount"))
                .paidAmount(rs.getDouble("paidAmount"))
                .description(rs.getString("description"))
                .status(rs.getString("status"))
                .createdAt(rs.getDate("created_at"))
                .createdBy(rs.getString("created_by"))
                .updatedAt(rs.getDate("updated_at"))
                .deleteAt(rs.getDate("deletedAt"))
                .deleteBy(rs.getString("deleteBy"))
                .isDeleted(rs.getBoolean("isDeleted"))
                .build();
    }

    public void updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE Orders SET status = ? WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, status);
            st.setInt(2, orderId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int insertOrder(Orders order) {
        String sql = "INSERT INTO Orders (customers_id, user_id, store_id, type, amount, paidAmount, description, status, created_at, created_by, isDeleted) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, ?, false)";

        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, order.getCustomerID().getId());
            st.setInt(2, order.getUserID().getId());
            st.setInt(3, order.getStoreId().getId());
            st.setString(4, order.getType());
            st.setDouble(5, order.getAmount());
            st.setDouble(6, order.getPaidAmount());
            st.setString(7, order.getDescription());
            st.setString(8, order.getStatus());
            st.setString(9, order.getCreatedBy());

            int affectedRows = st.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // trả về ID vừa insert
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // lỗi
    }

    // Lấy tất cả đơn hàng
    public List<Orders> getAllOrders() {
        List<Orders> list = new ArrayList<>();
        String sql = "SELECT o.*, c.name AS customer_name\n"
                + "FROM Orders o\n"
                + "JOIN Customers c ON o.customers_id = c.id\n"
                + "WHERE o.isDeleted = false";
        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Orders order = mapResultSetToOrder(rs);
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

// Tìm đơn hàng theo tên khách hàng (LIKE)
    public List<Orders> getOrdersByCustomerName(String keyword) {
        List<Orders> list = new ArrayList<>();
        String sql = """
        SELECT o.*, c.name AS customer_name
        FROM orders o
        JOIN customers c ON o.customers_id = c.id
        WHERE LOWER(c.name) LIKE ?
        AND o.isDeleted = false
        ORDER BY o.id DESC
    """;
        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, "%" + keyword.toLowerCase() + "%");
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Orders order = mapResultSetToOrder(rs);
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Orders> getOrdersByType(String type) {
        List<Orders> list = new ArrayList<>();
        String sql = """
        SELECT o.*, c.name AS customer_name
        FROM Orders o
        JOIN Customers c ON o.customers_id = c.id
        WHERE o.type = ? AND o.isDeleted = false
        ORDER BY o.id DESC
    """;

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, type);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToOrder(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Orders> getOrdersByCustomerNamePaging(String name, int offset, int limit) {
        List<Orders> list = new ArrayList<>();
        String sql = """
        SELECT o.*, c.name AS customer_name
        FROM orders o
        JOIN customers c ON o.customers_id = c.id
        WHERE LOWER(c.name) LIKE ?
        LIMIT ? OFFSET ?
    """;
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name.toLowerCase() + "%");
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToOrder(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getTotalOrdersCountBySearch(String name) {
        String sql = "SELECT COUNT(*) FROM orders o JOIN customers c ON o.customers_id = c.id "
                + "WHERE c.name LIKE ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Orders> getOrdersPaging(int offset, int limit) {
        List<Orders> list = new ArrayList<>();
        String sql = """
        SELECT o.*, c.name AS customer_name
        FROM orders o
        JOIN customers c ON o.customers_id = c.id
        WHERE o.isDeleted = false
        ORDER BY o.id DESC               
        LIMIT ? OFFSET ?
    """;

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, limit);   // LIMIT là tham số đầu tiên
            st.setInt(2, offset);  // OFFSET là tham số thứ hai

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToOrder(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int getTotalOrdersCount() {
        String sql = "SELECT COUNT(*) FROM Orders WHERE isDeleted = false";
        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Orders> getOrdersByDateRange(String fromDate, String toDate) {
        List<Orders> list = new ArrayList<>();
        // Kiểm tra null hoặc chuỗi rỗng
        if (fromDate == null || fromDate.trim().isEmpty() || toDate == null || toDate.trim().isEmpty()) {
            return getAllOrders(); // fallback
        }
        String sql = """
        SELECT o.*, c.name AS customer_name
        FROM Orders o
        JOIN Customers c ON o.customers_id = c.id
        WHERE o.isDeleted = false
        AND o.created_at BETWEEN ? AND ?
        ORDER BY o.id DESC
    """;

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setDate(1, java.sql.Date.valueOf(fromDate));
            st.setDate(2, java.sql.Date.valueOf(toDate));
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToOrder(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Orders getOrderById(int id) {
        String sql = """
        SELECT o.*, c.name AS customer_name
        FROM Orders o
        JOIN Customers c ON o.customers_id = c.id
        WHERE o.id = ? AND o.isDeleted = false
    """;

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOrder(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<OrderDetails> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetails> list = new ArrayList<>();
        String sql = """
        SELECT od.*, p.name AS productName
        FROM orderdetails od
        JOIN products p ON od.product_id = p.id
        WHERE od.order_id = ?
        AND od.isDeleted = 0
    """;
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, orderId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                OrderDetails detail = OrderDetails.builder()
                        .id(rs.getInt("id"))
                        .orderID(Orders.builder().id(rs.getInt("order_id")).build())
                        .productName(rs.getString("productName"))
                        .price(rs.getDouble("price"))
                        .unitPrice(rs.getDouble("unitPrice"))
                        .quantity(rs.getInt("quantity"))
                        .build();
                list.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        OrdersDAO dao = new OrdersDAO();

        System.out.println("=== Test getAllOrders() ===");
        List<Orders> allOrders = dao.getAllOrders();
        for (Orders o : allOrders) {
            System.out.printf("Order ID: %d | Customer: %s | Type: %s | Created At: %s%n",
                    o.getId(), o.getCustomerID().getName(), o.getType(), o.getCreatedAt());
        }

        System.out.println("\n=== Test getOrdersByCustomerName(\"Minh\") ===");
        List<Orders> searchOrders = dao.getOrdersByCustomerName("Minh");
        for (Orders o : searchOrders) {
            System.out.printf("Order ID: %d | Customer: %s | Type: %s | Created At: %s%n",
                    o.getId(), o.getCustomerID().getName(), o.getType(), o.getCreatedAt());
        }

        System.out.println("\n=== Test getOrdersByCustomerNamePaging(\"Minh\", offset=0, limit=3) ===");
        List<Orders> pagedSearch = dao.getOrdersByCustomerNamePaging("Minh", 0, 3);
        for (Orders o : pagedSearch) {
            System.out.printf("Order ID: %d | Customer: %s | Type: %s | Created At: %s%n",
                    o.getId(), o.getCustomerID().getName(), o.getType(), o.getCreatedAt());
        }

        System.out.println("\n=== Test getOrdersByType(\"Export\") ===");
        List<Orders> exportOrders = dao.getOrdersByType("Export");
        for (Orders o : exportOrders) {
            System.out.printf("Order ID: %d | Customer: %s | Type: %s | Created At: %s%n",
                    o.getId(), o.getCustomerID().getName(), o.getType(), o.getCreatedAt());
        }

        System.out.println("\n=== Test getOrdersByDateRange(\"2024-01-01\", \"2025-12-31\") ===");
        List<Orders> dateFiltered = dao.getOrdersByDateRange("2024-01-01", "2025-12-31");
        for (Orders o : dateFiltered) {
            System.out.printf("Order ID: %d | Customer: %s | Type: %s | Created At: %s%n",
                    o.getId(), o.getCustomerID().getName(), o.getType(), o.getCreatedAt());
        }

        System.out.println("\n=== Test getOrdersPaging(offset=0, limit=5) ===");
        List<Orders> pagingTest = dao.getOrdersPaging(0, 5);
        for (Orders o : pagingTest) {
            System.out.printf("Order ID: %d | Customer: %s | Type: %s | Created At: %s%n",
                    o.getId(), o.getCustomerID().getName(), o.getType(), o.getCreatedAt());
        }

        System.out.println("\n=== Test getTotalOrdersCount() ===");
        int total = dao.getTotalOrdersCount();
        System.out.println("Tổng số đơn hàng: " + total);

        System.out.println("\n=== Test getTotalOrdersCountBySearch(\"Minh\") ===");
        int countBySearch = dao.getTotalOrdersCountBySearch("Minh");
        System.out.println("Tổng số đơn có tên khách chứa 'Minh': " + countBySearch);
    }

}
