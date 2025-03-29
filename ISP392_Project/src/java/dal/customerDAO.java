/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import dal.debtDAO;
import entity.Customers;
import entity.DebtNote;
import entity.Stores;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class customerDAO extends DBContext {

    debtDAO debtDao = new debtDAO();

    public List<Customers> findAll() {
        List<Customers> customersList = new ArrayList<>();
        String sql = "SELECT id, name, phone, address, balance, created_at, updated_at, updated_by, created_by, isDeleted, deleteBy, store_id, status FROM customers";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Customers customer = mapResultSetToCustomer(rs);
                customersList.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customersList;
    }

    public List<String> getAllCustomerNames() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT name FROM Customers";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching customer names: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public List<Customers> viewAllCustomersWithDebts(String command, int index, int storeID) {
        List<Customers> customersList = new ArrayList<>();
        String sqlCustomers = "SELECT id, name, phone, address, balance, created_at, updated_at, updated_by, created_by, isDeleted, deleteBy, store_id, status "
                + "FROM customers "
                + "WHERE store_id = ? ORDER BY id LIMIT 5 OFFSET ?";

        try (PreparedStatement st = connection.prepareStatement(sqlCustomers)) {
            st.setInt(1, storeID);
            st.setInt(2, (index - 1) * 5);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Customers customer = mapResultSetToCustomer(rs);


                    customersList.add(customer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customersList;
    }

   public int countCustomers(String keyword, String balanceFilter, int storeID) {
    StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM customers WHERE store_id = ?");

    if (keyword != null && !keyword.trim().isEmpty()) {
        sql.append(" AND (name LIKE ? OR phone LIKE ?)");
    }

    if (balanceFilter != null && !balanceFilter.equals("all")) {
        if (balanceFilter.equals("0")) {
            sql.append(" AND balance = 0");
        } else if (balanceFilter.equals("positive")) {
            sql.append(" AND balance > 0");
        } else if (balanceFilter.equals("negative")) {
            sql.append(" AND balance < 0");
        }
    }

    try (PreparedStatement st = connection.prepareStatement(sql.toString())) {
        int paramIndex = 1;
        st.setInt(paramIndex++, storeID);

        if (keyword != null && !keyword.trim().isEmpty()) {
            String param = "%" + keyword + "%";
            st.setString(paramIndex++, param);
            st.setString(paramIndex++, param);
        }

        try (ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return 0;
}

   public List<Customers> searchCustomers(String keyword, int pageIndex, int pageSize, String sortBy, String sortOrder, int storeID, String balanceFilter) {
    List<Customers> list = new ArrayList<>();
    List<String> allowedSortColumns = List.of("id", "name", "phone", "balance", "created_at", "updated_at");

    if (sortBy == null || !allowedSortColumns.contains(sortBy)) {
        sortBy = "id";
    }

    if (sortOrder == null || (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC"))) {
        sortOrder = "ASC";
    }

    StringBuilder sql = new StringBuilder("SELECT * FROM customers WHERE store_id = ?");

    if (keyword != null && !keyword.trim().isEmpty()) {
        sql.append(" AND (name LIKE ? OR phone LIKE ?)");
    }

    if (balanceFilter != null && !balanceFilter.equals("all")) {
        switch (balanceFilter) {
            case "0":
                sql.append(" AND balance = 0");
                break;
            case "positive":
                sql.append(" AND balance > 0");
                break;
            case "negative":
                sql.append(" AND balance < 0");
                break;
        }
    }

    sql.append(" ORDER BY ").append(sortBy).append(" ").append(sortOrder);
    sql.append(" LIMIT ? OFFSET ?");

    try (PreparedStatement st = connection.prepareStatement(sql.toString())) {
        int paramIndex = 1;

        st.setInt(paramIndex++, storeID); 

        if (keyword != null && !keyword.trim().isEmpty()) {
            String param = "%" + keyword + "%";
            st.setString(paramIndex++, param);
            st.setString(paramIndex++, param);
        }

        st.setInt(paramIndex++, pageSize);
        st.setInt(paramIndex, (pageIndex - 1) * pageSize);

        try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Customers customer = mapResultSetToCustomer(rs);

                list.add(customer);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}


    public Customers getCustomerById(int id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void insertCustomer(Customers customer) {
        String sql = "INSERT INTO Customers (name, phone, address, balance, created_at, created_by, deletedAt, deleteBy, isDeleted, updated_at, updated_by, store_id, status) "
                + "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, customer.getName());
            st.setString(2, customer.getPhone());
            st.setString(3, customer.getAddress());
            st.setDouble(4, customer.getBalance());
            st.setString(5, customer.getCreatedBy());

            if (customer.getDeleteAt() != null) {
                st.setDate(6, customer.getDeleteAt());
            } else {
                st.setNull(6, java.sql.Types.DATE);
            }

            if (customer.getDeleteBy() != null) {
                st.setString(7, customer.getDeleteBy());
            } else {
                st.setNull(7, java.sql.Types.VARCHAR);
            }

            st.setBoolean(8, customer.isDeleted());
            st.setString(9, customer.getUpdateBy());

            if (customer.getStoreId() != null) {
                st.setInt(10, customer.getStoreId().getId());
            } else {
                st.setNull(10, java.sql.Types.INTEGER);
            }

            st.setString(11, customer.getStatus());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editCustomer(Customers customer) {
        String sql = "UPDATE customers SET name = ?, phone = ?, address = ?, balance = ?, updated_at = CURRENT_TIMESTAMP, "
                + "updated_by = ?, status = ?, store_id = ?, deletedAt = ?, deleteBy = ?, isDeleted = ? WHERE id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, customer.getName());
            st.setString(2, customer.getPhone());
            st.setString(3, customer.getAddress());
            st.setDouble(4, customer.getBalance());
            st.setString(5, customer.getUpdateBy());
            st.setString(6, customer.getStatus());

            if (customer.getStoreId() != null) {
                st.setInt(7, customer.getStoreId().getId());
            } else {
                st.setNull(7, java.sql.Types.INTEGER);
            }

            if (customer.getDeleteAt() != null) {
                st.setDate(8, customer.getDeleteAt());
            } else {
                st.setNull(8, java.sql.Types.DATE);
            }

            if (customer.getDeleteBy() != null) {
                st.setString(9, customer.getDeleteBy());
            } else {
                st.setNull(9, java.sql.Types.VARCHAR);
            }

            st.setBoolean(10, customer.isDeleted());
            st.setInt(11, customer.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editCustomerBalance(BigDecimal balance, int customerId) {
        String sql = "UPDATE customers SET  balance = balance +  ?  WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setBigDecimal(1, balance);
            st.setInt(2, customerId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkPhoneExists(String phone, int customerId) {
        String sql = "SELECT COUNT(*) FROM customers WHERE phone = ? AND id != ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, phone);
            st.setInt(2, customerId);
            ResultSet rs = st.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Customers mapResultSetToCustomer(ResultSet rs) throws SQLException {
        // Xử lý store_id (có thể null)
        Stores store = null;
        int storeId = rs.getInt("store_id");
        if (!rs.wasNull()) {
            store = new Stores();
            store.setId(storeId);
        }

        return Customers.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .phone(rs.getString("phone"))
                .address(rs.getString("address"))
                .balance(rs.getDouble("balance"))
                .createdAt(rs.getDate("created_at"))
                .updatedAt(rs.getDate("updated_at"))
                .createdBy(rs.getString("created_by"))
                .updateBy(rs.getString("updated_by"))
                .isDeleted(rs.getBoolean("isDeleted"))
                .deleteBy(rs.getString("deleteBy"))
                .storeId(store)
                .status(rs.getString("status"))
                .build();
    }

    public static void main(String[] args) {
//        customerDAO dao = new customerDAO();
//
    }

    private Customers getFromResulset(ResultSet rs) throws SQLException {
        return Customers.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .phone(rs.getString("phone"))
                .address(rs.getString("address"))
                .balance(rs.getDouble("balance"))
                .createdAt(rs.getDate("created_at"))
                .createdBy(rs.getString("created_by"))
                .deleteAt(rs.getDate("deletedAt"))
                .deleteBy(rs.getString("deleteBy"))
                .isDeleted(rs.getBoolean("isDeleted"))
                .updatedAt(rs.getDate("updated_at"))
                .updateBy(rs.getString("updated_by"))
                .storeId(Stores.builder().id(rs.getInt("store_id")).build())
                .status(rs.getString("status"))
                .invoices(new ArrayList<>())
                .debtNotes(new ArrayList<>())
                .build();
    }

    /**
     * Search customers by name
     *
     * @param query The name to search for
     * @return List of customers matching the search criteria
     */
    public List<Customers> searchCustomersByName(String query) {
        List<Customers> customers = new ArrayList<>();

        // Ensure connection is open
        if (connection == null) {
            System.err.println("Error: Cannot connect to database!");
            return customers;
        }

        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Customers WHERE name LIKE ? AND isDeleted = false";

        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, "%" + query + "%");
            rs = pst.executeQuery();

            while (rs.next()) {
                customers.add(getFromResulset(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error searching customers by name: " + query + "\n" + ex.getMessage());
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
                System.err.println("Error closing ResultSet or PreparedStatement: " + ex.getMessage());
            }
        }

        return customers;
    }

    public Integer getCustomerIdByPhone(String phone) {
        String sql = "SELECT id FROM Customers WHERE phone = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, phone);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }
}
