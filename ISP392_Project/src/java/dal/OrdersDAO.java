package dal;

import entity.Orders;
import entity.Customers;
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
                .deleteAt(rs.getDate("delete_at"))
                .deleteBy(rs.getString("delete_by"))
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
}
