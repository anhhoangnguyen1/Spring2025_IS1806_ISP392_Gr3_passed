package dal;

import entity.Orders;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Data Access Object for Order
 */
public class OrdersDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(OrdersDAO.class.getName());
    private static final String RED_COLOR = "\u001B[31m";
    private static final String RESET_COLOR = "\u001B[0m";

    /**
     * Constructor
     */
    public OrdersDAO() {
        super();
    }

    private void logSevere(String message) {
        System.err.println(RED_COLOR + "SEVERE: " + message + RESET_COLOR);
        LOGGER.severe(message);
    }

    private void logSevere(String message, Exception ex) {
        System.err.println(RED_COLOR + "SEVERE: " + message + "\n" + ex.getMessage() + RESET_COLOR);
        LOGGER.log(Level.SEVERE, message, ex);
    }

    /**
     * Get all orders from database
     *
     * @return List containing all orders
     */
    public List<Orders> getAllOrders() {
        List<Orders> orders = new ArrayList<>();

        // Ensure connection is open
        if (connection == null) {
            logSevere("Error: Cannot connect to database!");
            return orders;
        }

        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Orders ORDER BY ID DESC";

        try {
            pst = connection.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("ID");
                Date orderDate = rs.getDate("OrderDate");
                Integer totalAmount = rs.getInt("TotalAmount");
                Integer customerId = rs.getInt("CustomerID");
                Integer employeeId = rs.getInt("EmployeesID");

                Orders order = Orders.builder()
                        .orderID(id)
                        .orderDate(orderDate)
                        .totalAmount(totalAmount)
                        .customerID(customerId)
                        .employeeID(employeeId)
                        .build();

                orders.add(order);
            }
        } catch (SQLException ex) {
            logSevere("Error retrieving order list", ex);
        } finally {
            // Close ResultSet and PreparedStatement
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                logSevere("Error closing ResultSet or PreparedStatement", ex);
            }
        }

        return orders;
    }

    public List<Orders> getOrdersWithPagination(int start, int recordsPerPage) {
        List<Orders> orders = new ArrayList<>();

        // Ensure connection is open
        if (connection == null) {
            logSevere("Error: Cannot connect to database!");
            return orders;
        }

        String query = "SELECT * FROM Orders ORDER BY ID DESC LIMIT ?, ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, start);
            ps.setInt(paramIndex++, recordsPerPage);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Orders order = Orders.builder()
                            .orderID(rs.getInt("ID"))
                            .orderDate(rs.getDate("OrderDate"))
                            .totalAmount(rs.getInt("TotalAmount"))
                            .customerID(rs.getInt("CustomerID"))
                            .employeeID(rs.getInt("EmployeesID"))
                            .build();
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            logSevere("Error retrieving paginated orders", e);
        }
        return orders;
    }

    public int getTotalOrderCount() {
        int totalOrders = 0;
        String sql = "SELECT COUNT(*) FROM Orders"; // No status filtering

        // Ensure connection is open
        if (connection == null) {
            logSevere("Error: Cannot connect to database!");
            return totalOrders;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalOrders = rs.getInt(1);
            }
        } catch (SQLException e) {
            logSevere("Error retrieving total order count", e);
        }

        return totalOrders;
    }

    /**
     * Get order information by ID
     *
     * @param orderId ID of the order to retrieve
     * @return Order object or null if not found
     */
    public Orders getOrderById(Integer orderId) {
        Orders order = null;
        String sql = "SELECT * FROM Orders WHERE ID = ?";

        // Ensure connection is open
        if (connection == null) {
            logSevere("Error: Cannot connect to database!");
            return null;
        }

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, orderId);  // Set parameter for PreparedStatement

            rs = pst.executeQuery();
            if (rs.next()) {
                Integer id = rs.getInt("ID");
                Date orderDate = rs.getDate("OrderDate");
                Integer totalAmount = rs.getInt("TotalAmount");
                Integer customerId = rs.getInt("CustomerID");
                Integer employeeId = rs.getInt("EmployeesID");

                order = Orders.builder()
                        .orderID(id)
                        .orderDate(orderDate)
                        .totalAmount(totalAmount)
                        .customerID(customerId)
                        .employeeID(employeeId)
                        .build();
            }
        } catch (SQLException ex) {
            logSevere("Error retrieving order with ID: " + orderId, ex);
        } finally {
            // Close ResultSet and PreparedStatement
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                logSevere("Error closing ResultSet or PreparedStatement", ex);
            }
        }

        return order;
    }

    /**
     * Add new order to database
     *
     * @param order Order object to add
     * @return true if successful, false if failed
     */
    public boolean addOrder(Orders order) {
        String sql = "INSERT INTO Orders (OrderDate, TotalAmount, CustomerID, EmployeesID) "
                + "VALUES (?, ?, ?, ?)";

        // Ensure connection is open
        if (connection == null) {
            logSevere("Error: Cannot connect to database!");
            return false;
        }

        PreparedStatement pst = null;

        try {
            pst = connection.prepareStatement(sql);

            // Convert java.util.Date to java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(order.getOrderDate().getTime());

            pst.setDate(1, sqlDate);
            pst.setInt(2, order.getTotalAmount());
            pst.setInt(3, order.getCustomerID());
            pst.setInt(4, order.getEmployeeID());

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            logSevere("Error adding new order", ex);
            return false;
        } finally {
            // Close PreparedStatement
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                logSevere("Error closing PreparedStatement", ex);
            }
        }
    }

    /**
     * Update order information
     *
     * @param order Order object to update
     * @return true if successful, false if failed
     */
    public boolean updateOrder(Orders order) {
        String sql = "UPDATE Orders SET OrderDate = ?, TotalAmount = ?, CustomerID = ?, "
                + "EmployeesID = ? WHERE ID = ?";

        // Ensure connection is open
        if (connection == null) {
            logSevere("Error: Cannot connect to database!");
            return false;
        }

        PreparedStatement pst = null;

        try {
            pst = connection.prepareStatement(sql);

            // Convert java.util.Date to java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(order.getOrderDate().getTime());

            pst.setDate(1, sqlDate);
            pst.setInt(2, order.getTotalAmount());
            pst.setInt(3, order.getCustomerID());
            pst.setInt(4, order.getEmployeeID());
            pst.setInt(5, order.getOrderID());

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            logSevere("Error updating order with ID: " + order.getOrderID(), ex);
            return false;
        } finally {
            // Close PreparedStatement
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                logSevere("Error closing PreparedStatement", ex);
            }
        }
    }

    /**
     * Delete order from database
     *
     * @param orderId ID of the order to delete
     * @return true if successful, false if failed
     */
    public boolean deleteOrder(Integer orderId) {
        String sql = "DELETE FROM Orders WHERE ID = ?";

        // Ensure connection is open
        if (connection == null) {
            logSevere("Error: Cannot connect to database!");
            return false;
        }

        PreparedStatement pst = null;

        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, orderId);

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            logSevere("Error deleting order with ID: " + orderId, ex);
            return false;
        } finally {
            // Close PreparedStatement
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                logSevere("Error closing PreparedStatement", ex);
            }
        }
    }

    /**
     * Get orders by customer ID
     *
     * @param customerId ID of the customer
     * @return List containing orders for the customer
     */
    public List<Orders> getOrdersByCustomerId(Integer customerId) {
        List<Orders> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE CustomerID = ?";

        // Ensure connection is open
        if (connection == null) {
            logSevere("Error: Cannot connect to database!");
            return orders;
        }

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, customerId);

            rs = pst.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("ID");
                Date orderDate = rs.getDate("OrderDate");
                Integer totalAmount = rs.getInt("TotalAmount");
                Integer employeeId = rs.getInt("EmployeesID");

                Orders order = Orders.builder()
                        .orderID(id)
                        .orderDate(orderDate)
                        .totalAmount(totalAmount)
                        .customerID(customerId)
                        .employeeID(employeeId)
                        .build();

                orders.add(order);
            }
        } catch (SQLException ex) {
            logSevere("Error retrieving orders for customer ID: " + customerId, ex);
        } finally {
            // Close ResultSet and PreparedStatement
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                logSevere("Error closing ResultSet or PreparedStatement", ex);
            }
        }

        return orders;
    }

    /**
     * Get orders for report with filtering options
     *
     * @param fromDate Start date for filtering
     * @param toDate End date for filtering
     * @param orderIdSearch Order ID to search for
     * @param customerSearch Customer name to search for
     * @param employeeSearch Employee name to search for
     * @return List of filtered orders
     */
    public List<Orders> getOrdersForReport(Date fromDate, Date toDate, String orderIdSearch, String customerSearch, String employeeSearch) {
        List<Orders> orders = new ArrayList<>();
        String sql = "SELECT o.* FROM Orders o "
                + "LEFT JOIN Customer c ON o.CustomerID = c.ID "
                + "LEFT JOIN Employees e ON o.EmployeesID = e.ID "
                + "WHERE 1=1 ";

        List<Object> params = new ArrayList<>();

        // Add date filter conditions
        if (fromDate != null) {
            sql += "AND o.OrderDate >= ? ";
            params.add(fromDate);
        }
        if (toDate != null) {
            sql += "AND o.OrderDate <= ? ";
            params.add(toDate);
        }

        // Add order ID search condition
        if (orderIdSearch != null && !orderIdSearch.isEmpty()) {
            sql += "AND o.ID = ? ";
            params.add(Integer.parseInt(orderIdSearch));
        }

        // Add customer name search condition
        if (customerSearch != null && !customerSearch.isEmpty()) {
            sql += "AND c.CustomerName LIKE ? ";
            params.add("%" + customerSearch + "%");
        }

        // Add employee name search condition
        if (employeeSearch != null && !employeeSearch.isEmpty()) {
            sql += "AND e.EmployeeName LIKE ? ";
            params.add("%" + employeeSearch + "%");
        }

        sql += "ORDER BY o.OrderDate DESC";

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = connection.prepareStatement(sql);

            // Set parameters for the query
            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof Date) {
                    pst.setDate(i + 1, new java.sql.Date(((Date) param).getTime()));
                } else if (param instanceof Integer) {
                    pst.setInt(i + 1, (Integer) param);
                } else {
                    pst.setString(i + 1, param.toString());
                }
            }

            rs = pst.executeQuery();
            while (rs.next()) {
                Orders order = Orders.builder()
                        .orderID(rs.getInt("ID"))
                        .orderDate(rs.getDate("OrderDate"))
                        .totalAmount(rs.getInt("TotalAmount"))
                        .customerID(rs.getInt("CustomerID"))
                        .employeeID(rs.getInt("EmployeesID"))
                        .build();
                orders.add(order);
            }
        } catch (SQLException ex) {
            logSevere("Error getting orders for report", ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                logSevere("Error closing ResultSet or PreparedStatement", ex);
            }
        }
        return orders;
    }

    /**
     * Insert order into database and return the generated ID
     *
     * @param order Order object to insert
     * @return ID of the inserted order, or 0 if insertion failed
     */
    public int insertOrder(Orders order) {
        int generatedId = 0;
        String sql = "INSERT INTO Orders (OrderDate, TotalAmount, CustomerID, EmployeesID) "
                + "VALUES (?, ?, ?, ?)";

        // Ensure connection is open
        if (connection == null) {
            logSevere("Error: Cannot connect to database!");
            return 0;
        }

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Convert java.util.Date to java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(order.getOrderDate().getTime());

            pst.setDate(1, sqlDate);
            pst.setInt(2, order.getTotalAmount());
            pst.setObject(3, order.getCustomerID());
            pst.setInt(4, order.getEmployeeID());

            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                    order.setOrderID(generatedId);
                }
            }

        } catch (SQLException ex) {
            logSevere("Error inserting order", ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                logSevere("Error closing ResultSet or PreparedStatement", ex);
            }
        }

        return generatedId;
    }

    /**
     * Main method to test the DAO
     */
    public static void main(String[] args) {
        OrdersDAO daoOrder = new OrdersDAO();
        List<Orders> orders = daoOrder.getAllOrders();
        for (Orders order : orders) {
            System.out.println(order);
        }
    }
}
