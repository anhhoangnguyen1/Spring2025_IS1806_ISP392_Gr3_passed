package dal;

import entity.OrderDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderDetailsDAO extends DBContext {

    public void insertOrderDetail(OrderDetails od) {
        String sql = "INSERT INTO OrderDetails (order_id, store_id, product_id, product_name, price, unit_price, quantity, import_price, created_at, status, isDeleted) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, ?, false)";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, od.getOrderID().getId());
            st.setInt(2, od.getStoreId().getId());
            st.setInt(3, od.getProductID().getProductId());
            st.setString(4, od.getProductName());
            st.setDouble(5, od.getPrice());
            st.setDouble(6, od.getUnitPrice());
            st.setInt(7, od.getQuantity());
            st.setDouble(8, od.getImportPrice());
            st.setString(9, od.getStatus());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
