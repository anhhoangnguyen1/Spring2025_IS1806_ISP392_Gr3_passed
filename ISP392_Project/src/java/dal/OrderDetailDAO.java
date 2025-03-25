package dal;

import entity.OrderDetail;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for OrderDetail
 */
public class OrderDetailDAO extends DBContext {
    
    private static final Logger LOGGER = Logger.getLogger(OrderDetailDAO.class.getName());
    private static final String RED_COLOR = "\u001B[31m";
    private static final String RESET_COLOR = "\u001B[0m";
    
    /**
     * Constructor
     */
    public OrderDetailDAO() {
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
     * Get OrderDetail object from ResultSet
     * @param rs ResultSet to extract data from
     * @return OrderDetail object
     * @throws SQLException if a database access error occurs
     */
    private OrderDetail getFromResultSet(ResultSet rs) throws SQLException {
        return OrderDetail.builder()
                .orderDetailID(rs.getInt("ID"))
                .quantity(rs.getInt("Quantity"))
                .price(rs.getInt("Price"))
                .orderID(rs.getInt("OrdersID"))
                .productID(rs.getInt("ProductsID"))
                .build();
    }

    /**
     * Get all order details from database
     * @return List containing all order details
     */
    public List<OrderDetail> getAllOrderDetails() {
        List<OrderDetail> orderDetails = new ArrayList<>();
        
        // Ensure connection is open
        if (connection == null) {
            logSevere("Error: Cannot connect to database!");
            return orderDetails;
        }

        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM OrderDetails";
        
        try {
            pst = connection.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                orderDetails.add(getFromResultSet(rs));
            }
        } catch (SQLException ex) {
            logSevere("Error retrieving order detail list", ex);
        } finally {
            // Close ResultSet and PreparedStatement
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
            } catch (SQLException ex) {
                logSevere("Error closing ResultSet or PreparedStatement", ex);
            }
        }

        return orderDetails;
    }

    /**
     * Get order detail information by ID
     * @param orderDetailId ID of the order detail to retrieve
     * @return OrderDetail object or null if not found
     */
    public OrderDetail getOrderDetailById(Integer orderDetailId) {
        OrderDetail orderDetail = null;
        String sql = "SELECT * FROM OrderDetails WHERE ID = ?";

        // Ensure connection is open
        if (connection == null) {
            logSevere("Error: Cannot connect to database!");
            return null;
        }

        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, orderDetailId);  // Set parameter for PreparedStatement
            
            rs = pst.executeQuery();
            if (rs.next()) {
                orderDetail = getFromResultSet(rs);
            }
        } catch (SQLException ex) {
            logSevere("Error retrieving order detail with ID: " + orderDetailId, ex);
        } finally {
            // Close ResultSet and PreparedStatement
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
            } catch (SQLException ex) {
                logSevere("Error closing ResultSet or PreparedStatement", ex);
            }
        }

        return orderDetail;
    }
    
    /**
     * Add new order detail to database
     * @param orderDetail OrderDetail object to add
     * @return true if successful, false if failed
     */
    public boolean addOrderDetail(OrderDetail orderDetail) {
        String sql = "INSERT INTO OrderDetails (Quantity, Price, OrdersID, ProductsID) "
                   + "VALUES (?, ?, ?, ?)";
        
        // Ensure connection is open
        if (connection == null) {
            logSevere("Error: Cannot connect to database!");
            return false;
        }
        
        PreparedStatement pst = null;
        
        try {
            pst = connection.prepareStatement(sql);
            
            pst.setInt(1, orderDetail.getQuantity());
            pst.setInt(2, orderDetail.getPrice());
            pst.setInt(3, orderDetail.getOrderID());
            pst.setInt(4, orderDetail.getProductID());
            
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            logSevere("Error adding new order detail", ex);
            return false;
        } finally {
            // Close PreparedStatement
            try {
                if (pst != null) pst.close();
            } catch (SQLException ex) {
                logSevere("Error closing PreparedStatement", ex);
            }
        }
    }
    
    /**
     * Update order detail information
     * @param orderDetail OrderDetail object to update
     * @return true if successful, false if failed
     */
    public boolean updateOrderDetail(OrderDetail orderDetail) {
        String sql = "UPDATE OrderDetails SET Quantity = ?, Price = ?, OrdersID = ?, "
                   + "ProductsID = ? WHERE ID = ?";
        
        // Ensure connection is open
        if (connection == null) {
            logSevere("Error: Cannot connect to database!");
            return false;
        }
        
        PreparedStatement pst = null;
        
        try {
            pst = connection.prepareStatement(sql);
            
            pst.setInt(1, orderDetail.getQuantity());
            pst.setInt(2, orderDetail.getPrice());
            pst.setInt(3, orderDetail.getOrderID());
            pst.setInt(4, orderDetail.getProductID());
            pst.setInt(5, orderDetail.getOrderDetailID());
            
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            logSevere("Error updating order detail with ID: " + orderDetail.getOrderDetailID(), ex);
            return false;
        } finally {
            // Close PreparedStatement
            try {
                if (pst != null) pst.close();
            } catch (SQLException ex) {
                logSevere("Error closing PreparedStatement", ex);
            }
        }
    }
    
    /**
     * Delete order detail from database
     * @param orderDetailId ID of the order detail to delete
     * @return true if successful, false if failed
     */
    public boolean deleteOrderDetail(Integer orderDetailId) {
        String sql = "DELETE FROM OrderDetails WHERE ID = ?";
        
        // Ensure connection is open
        if (connection == null) {
            logSevere("Error: Cannot connect to database!");
            return false;
        }
        
        PreparedStatement pst = null;
        
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, orderDetailId);
            
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            logSevere("Error deleting order detail with ID: " + orderDetailId, ex);
            return false;
        } finally {
            // Close PreparedStatement
            try {
                if (pst != null) pst.close();
            } catch (SQLException ex) {
                logSevere("Error closing PreparedStatement", ex);
            }
        }
    }
    
    /**
     * Get order details by order ID
     * @param orderId ID of the order
     * @return List containing order details for the order
     */
    public List<OrderDetail> getOrderDetailsByOrderId(Integer orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT * FROM OrderDetails WHERE OrdersID = ?";
        
        // Ensure connection is open
        if (connection == null) {
            logSevere("Error: Cannot connect to database!");
            return orderDetails;
        }
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, orderId);
            
            rs = pst.executeQuery();
            
            while (rs.next()) {
                orderDetails.add(getFromResultSet(rs));
            }
        } catch (SQLException ex) {
            logSevere("Error retrieving order details for order ID: " + orderId, ex);
        } finally {
            // Close ResultSet and PreparedStatement
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
            } catch (SQLException ex) {
                logSevere("Error closing ResultSet or PreparedStatement", ex);
            }
        }
        
        return orderDetails;
    }
    
    /**
     * Get order details by product ID
     * @param productId ID of the product
     * @return List containing order details for the product
     */
    public List<OrderDetail> getOrderDetailsByProductId(Integer productId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT * FROM OrderDetails WHERE ProductsID = ?";
        
        // Ensure connection is open
        if (connection == null) {
            logSevere("Error: Cannot connect to database!");
            return orderDetails;
        }
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, productId);
            
            rs = pst.executeQuery();
            
            while (rs.next()) {
                orderDetails.add(getFromResultSet(rs));
            }
        } catch (SQLException ex) {
            logSevere("Error retrieving order details for product ID: " + productId, ex);
        } finally {
            // Close ResultSet and PreparedStatement
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
            } catch (SQLException ex) {
                logSevere("Error closing ResultSet or PreparedStatement", ex);
            }
        }
        
        return orderDetails;
    }
    
    /**
     * Insert multiple order details for an order
     * @param orderDetails List of OrderDetail objects to insert
     * @return true if all insertions were successful, false otherwise
     */
    public boolean insertOrderDetails(List<OrderDetail> orderDetails) {
        if (orderDetails == null || orderDetails.isEmpty()) {
            return false;
        }
        
        boolean allSuccessful = true;
        
        for (OrderDetail detail : orderDetails) {
            if (!addOrderDetail(detail)) {
                allSuccessful = false;
            }
        }
        
        return allSuccessful;
    }

    /**
     * Main method to test the DAO
     */
    public static void main(String[] args) {
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
        List<OrderDetail> orderDetails = orderDetailDAO.getAllOrderDetails();
        for (OrderDetail orderDetail : orderDetails) {
            System.out.println(orderDetail);
        }
    }
} 