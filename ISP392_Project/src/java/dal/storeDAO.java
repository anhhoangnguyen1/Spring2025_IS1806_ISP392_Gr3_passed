package dal;

import entity.Stores;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class storeDAO extends DBContext {

    public List<Stores> getAllStores() {
        List<Stores> storeList = new ArrayList<>();
        String sql = "SELECT * FROM stores WHERE isDeleted = false";  // Lọc cửa hàng chưa bị xóa (nếu có điều kiện như vậy)

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Stores store = mapResultSetToStore(rs);  // Sử dụng hàm mapResultSetToStore để ánh xạ dữ liệu
                storeList.add(store);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return storeList;  // Trả về danh sách cửa hàng
    }

    public List<Stores> searchStores(String searchTerm) {
        List<Stores> storeList = new ArrayList<>();
        String sql = "SELECT * FROM stores WHERE name LIKE ? AND isDeleted = false";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, "%" + searchTerm + "%"); // Tìm kiếm theo từ khóa
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Stores store = mapResultSetToStore(rs);
                    storeList.add(store);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storeList;
    }

    // Method lọc cửa hàng theo trạng thái
    public List<Stores> filterStoresByStatus(String status) {
        List<Stores> storeList = new ArrayList<>();
        String sql = "SELECT * FROM stores WHERE isDeleted = false";

        // Thêm điều kiện lọc theo trạng thái nếu có
        if (status != null && !status.isEmpty()) {
            sql += " AND status = ?";
        }

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            if (status != null && !status.isEmpty()) {
                st.setString(1, status);
            }

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Stores store = mapResultSetToStore(rs);
                    storeList.add(store);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storeList;
    }

    // Lọc cửa hàng theo ngày tạo (created_at)
    public List<Stores> filterStoresByDate(String fromDate, String toDate) {
        List<Stores> storeList = new ArrayList<>();
        String sql = "SELECT * FROM stores WHERE isDeleted = false";

        if (fromDate != null && !fromDate.isEmpty()) {
            sql += " AND created_at >= ?";
        }
        if (toDate != null && !toDate.isEmpty()) {
            sql += " AND created_at <= ?";
        }

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            int index = 1;
            if (fromDate != null && !fromDate.isEmpty()) {
                st.setString(index++, fromDate);
            }
            if (toDate != null && !toDate.isEmpty()) {
                st.setString(index++, toDate);
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Stores store = mapResultSetToStore(rs);
                    storeList.add(store);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storeList;
    }

    // Lấy thông tin cửa hàng theo ID
    public Stores getStoreById(int storeId) {
        String sql = "SELECT * FROM stores WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, storeId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStore(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy cửa hàng
    }

    // Thêm mới cửa hàng
    public boolean insertStore(Stores store) {
        String sql = "INSERT INTO stores (name, address, phone, email, status, created_at) VALUES (?, ?, ?, ?, ?, NOW())";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, store.getName());
            st.setString(2, store.getAddress());
            st.setString(3, store.getPhone());
            st.setString(4, store.getEmail());
            st.setString(5, store.getStatus());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Sửa thông tin cửa hàng
    public boolean editStore(Stores store) {
        String sql = "UPDATE stores SET name = ?, address = ?, phone = ?, email = ?, updated_at = NOW() WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, store.getName());
            st.setString(2, store.getAddress());
            st.setString(3, store.getPhone());
            st.setString(4, store.getEmail());
            st.setInt(5, store.getId());
            int rowsUpdated = st.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);  // Log number of rows affected

            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStoreStatus(int storeId, String status) {
        String sql = "UPDATE stores SET status = ?, updated_at = NOW() WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, status);
            st.setInt(2, storeId);
            int rowsUpdated = st.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Thêm mới cửa hàng và trả về ID được tạo
    public int createStore(Stores store) {
        String sql = "INSERT INTO stores (name, address, phone, email, status, created_by, created_at) VALUES (?, ?, ?, ?, ?, ?, NOW())";
        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, store.getName());
            st.setString(2, store.getAddress());
            st.setString(3, store.getPhone());
            st.setString(4, store.getEmail());
            st.setString(5, store.getStatus());
            st.setString(6, store.getCreatedBy());

            int affectedRows = st.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int storeId = generatedKeys.getInt(1);
                        store.setId(storeId); // Cập nhật ID cho đối tượng store
                        return storeId;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu thêm mới thất bại
    }

    public boolean isStoreNameExists(String name) {
        String sql = "SELECT COUNT(*) FROM stores WHERE name = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, name);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra tên cửa hàng có tồn tại và ngoại trừ cửa hàng có ID cho trước
    public boolean isStoreNameExists(String name, int storeId) {
        String sql = "SELECT COUNT(*) FROM stores WHERE name = ? AND id != ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, name);
            st.setInt(2, storeId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

// Kiểm tra số điện thoại đã tồn tại hay chưa
    public boolean isPhoneExists(String phone) {
        String sql = "SELECT COUNT(*) FROM stores WHERE phone = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, phone);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isPhoneExists(String phone, int storeId) {
        String sql = "SELECT COUNT(*) FROM stores WHERE phone = ? AND id != ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, phone);
            st.setInt(2, storeId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra email đã tồn tại hay chưa
    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM stores WHERE email = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isEmailExists(String email, int storeId) {
        String sql = "SELECT COUNT(*) FROM stores WHERE email = ? AND id != ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, email);
            st.setInt(2, storeId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Ánh xạ ResultSet thành đối tượng Store
    private Stores mapResultSetToStore(ResultSet rs) throws SQLException {
        return Stores.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .address(rs.getString("address"))
                .phone(rs.getString("phone"))
                .email(rs.getString("email"))
                .createdAt(rs.getDate("created_at"))
                .createdBy(rs.getString("created_by"))
                .updatedAt(rs.getDate("updated_at"))
                .deleteAt(rs.getDate("deletedAt"))
                .deleteBy(rs.getString("deleteBy"))
                .isDeleted(rs.getBoolean("isDeleted"))
                .status(rs.getString("status"))
                .build();
    }

    public static void main(String[] args) {
        // Tạo đối tượng storeDAO để gọi phương thức getStoreById
        storeDAO dao = new storeDAO();

        // Giả sử chúng ta muốn lấy cửa hàng có ID = 1
        int storeId = 1;
//        String name1 = "anh";
//        String add = "a0";
//        String phone = "0248754984";
//        String email = "email";
//
//        Stores store1 = new Stores();
//        store1.setId(storeId);
//        store1.setName(name1);
//        store1.setAddress(add);
//        store1.setPhone(phone);
//        store1.setEmail(email);
//        dao.editStore(store1);
//        System.out.println("Name: " + store1.getName());
//        System.out.println("Address: " + store1.getAddress());
//        System.out.println("Phone: " + store1.getPhone());
//        System.out.println("Email: " + store1.getEmail());

        // Gọi hàm getStoreById để lấy thông tin cửa hàng
        Stores store = dao.getStoreById(storeId);

        // Kiểm tra nếu tìm thấy cửa hàng và in thông tin
        if (store != null) {
            System.out.println("Store found:");
            System.out.println("ID: " + store.getId());
            System.out.println("Name: " + store.getName());
            System.out.println("Address: " + store.getAddress());
            System.out.println("Phone: " + store.getPhone());
            System.out.println("Email: " + store.getEmail());
            System.out.println("Status: " + store.getStatus());
            System.out.println("Created At: " + store.getCreatedAt());
            System.out.println("Updated At: " + store.getUpdatedAt());
        } else {
            System.out.println("Store not found with ID: " + storeId);
        }
    }
}
