package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import entity.ProductPriceHistory;

public class DAOProduct extends DBContext {

    public static DAOProduct INSTANCE = new DAOProduct();

    public List<ProductPriceHistory> getAllExportPriceHistory(int userId, String priceType) {
        List<ProductPriceHistory> historyList = new ArrayList<>();
        int storeId = getStoreIdByUserId(userId);

        String sql = """
        SELECT pph.id, p.id as productID, p.name as productName, p.image, 
               pph.price, pph.type, pph.created_at, u.name as changedByName
        FROM ProductPriceHistory pph
        JOIN Products p ON pph.product_id = p.id
        JOIN Users u ON pph.created_by = u.id
        WHERE p.store_id = ? 
          AND pph.type = ? 
          AND pph.isDeleted = 0
        ORDER BY pph.created_at DESC
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            ps.setString(2, priceType);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    historyList.add(new ProductPriceHistory(
                            rs.getInt("id"),
                            rs.getInt("productID"),
                            rs.getString("productName"),
                            rs.getString("image"),
                            rs.getDouble("price"),
                            rs.getString("type"),
                            rs.getString("created_at"),
                            rs.getString("changedByName")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historyList;
    }

    public List<ProductPriceHistory> getAllImportPriceHistory(int userId, String priceType) {
        List<ProductPriceHistory> historyList = new ArrayList<>();
        int storeId = getStoreIdByUserId(userId);

        String sql = """
        SELECT pph.id, p.id as productID, p.name as productName, p.image, 
               pph.importPrice as price, pph.type, pph.created_at, 
               u.name as changedByName, c.name as supplierName
        FROM ProductPriceHistory pph
        JOIN Products p ON pph.product_id = p.id
        JOIN Users u ON pph.created_by = u.id
        LEFT JOIN Orders o ON pph.order_id = o.id
        LEFT JOIN Customers c ON o.customers_id = c.id
        WHERE p.store_id = ? 
          AND pph.type = ? 
          AND pph.isDeleted = 0
        ORDER BY pph.created_at DESC
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            ps.setString(2, priceType);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    historyList.add(new ProductPriceHistory(
                            rs.getInt("id"),
                            rs.getInt("productID"),
                            rs.getString("productName"),
                            rs.getString("image"),
                            rs.getDouble("price"),
                            rs.getString("type"),
                            rs.getString("created_at"),
                            rs.getString("changedByName"),
                            rs.getString("supplierName")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historyList;
    }

    public List<ProductPriceHistory> getImportPriceHistory(String keyword, int page, int recordsPerPage, int userId, String sortOrder, String startDate, String endDate) {
        int storeId = getStoreIdByUserId(userId);
        List<ProductPriceHistory> historyList = new ArrayList<>();
        int offset = (page - 1) * recordsPerPage;

        System.out.println("Debug getImportPriceHistory:");
        System.out.println("storeId: " + storeId);
        System.out.println("keyword: " + keyword);
        System.out.println("offset: " + offset);
        System.out.println("recordsPerPage: " + recordsPerPage);
        System.out.println("startDate: " + startDate);
        System.out.println("endDate: " + endDate);

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT pph.id, p.id as productID, p.name as productName, p.image, ");
        sqlBuilder.append("pph.importPrice as price, pph.type, pph.created_at, ");
        sqlBuilder.append("pph.created_by as changedByName, c.name as supplierName ");
        sqlBuilder.append("FROM ProductPriceHistory pph ");
        sqlBuilder.append("JOIN Products p ON pph.product_id = p.id ");
        sqlBuilder.append("LEFT JOIN Orders o ON pph.order_id = o.id ");
        sqlBuilder.append("LEFT JOIN Customers c ON o.customers_id = c.id ");
        sqlBuilder.append("WHERE p.store_id = ? ");
        sqlBuilder.append("AND pph.type = 'import' ");
        sqlBuilder.append("AND pph.isDeleted = 0 ");
        sqlBuilder.append("AND (p.name LIKE ? OR ? = '') ");
        sqlBuilder.append("AND (pph.created_at BETWEEN ? AND DATE_ADD(?, INTERVAL 1 DAY)) ");
        sqlBuilder.append("ORDER BY pph.created_at ").append(sortOrder).append(" ");
        sqlBuilder.append("LIMIT ? OFFSET ?");

        try (PreparedStatement ps = connection.prepareStatement(sqlBuilder.toString())) {
            ps.setInt(1, storeId);
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, keyword);
            ps.setString(4, startDate);
            ps.setString(5, endDate);
            ps.setInt(6, recordsPerPage);
            ps.setInt(7, offset);

            System.out.println("Executing SQL: " + sqlBuilder.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductPriceHistory history = new ProductPriceHistory(
                            rs.getInt("id"),
                            rs.getInt("productID"),
                            rs.getString("productName"),
                            rs.getString("image"),
                            rs.getDouble("price"),
                            rs.getString("type"),
                            rs.getString("created_at"),
                            rs.getString("changedByName"),
                            rs.getString("supplierName")
                    );
                    historyList.add(history);
                    System.out.println("Found record: " + history.toString());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getImportPriceHistory: " + e.getMessage());
            e.printStackTrace();
        }
        return historyList;
    }

    public List<ProductPriceHistory> getExportPriceHistory(String keyword, int page, int recordsPerPage, int userId, String sortOrder, String startDate, String endDate) {
        int storeId = getStoreIdByUserId(userId);
        List<ProductPriceHistory> historyList = new ArrayList<>();
        int offset = (page - 1) * recordsPerPage;

        // Debug info
        System.out.println("Debug getExportPriceHistory:");
        System.out.println("storeId: " + storeId);
        System.out.println("keyword: " + keyword);
        System.out.println("offset: " + offset);
        System.out.println("recordsPerPage: " + recordsPerPage);
        System.out.println("startDate: " + startDate);
        System.out.println("endDate: " + endDate);

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT pph.id, p.id as productID, p.name as productName, p.image, ");
        sqlBuilder.append("pph.price, pph.type, pph.created_at, pph.created_by as changedByName ");
        sqlBuilder.append("FROM ProductPriceHistory pph ");
        sqlBuilder.append("JOIN Products p ON pph.product_id = p.id ");
        sqlBuilder.append("WHERE p.store_id = ? ");
        sqlBuilder.append("AND pph.type = 'sell' ");
        sqlBuilder.append("AND pph.isDeleted = 0 ");
        sqlBuilder.append("AND (p.name LIKE ? OR ? = '') ");
        sqlBuilder.append("AND (pph.created_at BETWEEN ? AND DATE_ADD(?, INTERVAL 1 DAY)) ");
        sqlBuilder.append("ORDER BY pph.created_at ").append(sortOrder).append(" ");
        sqlBuilder.append("LIMIT ").append(recordsPerPage).append(" ");
        sqlBuilder.append("OFFSET ").append(offset);

        try (PreparedStatement ps = connection.prepareStatement(sqlBuilder.toString())) {
            ps.setInt(1, storeId);
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, keyword);
            ps.setString(4, startDate);
            ps.setString(5, endDate);

            System.out.println("Executing SQL: " + sqlBuilder.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    historyList.add(new ProductPriceHistory(
                            rs.getInt("id"),
                            rs.getInt("productID"),
                            rs.getString("productName"),
                            rs.getString("image"),
                            rs.getDouble("price"),
                            rs.getString("type"),
                            rs.getString("created_at"),
                            rs.getString("changedByName")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getExportPriceHistory: " + e.getMessage());
            e.printStackTrace();
        }
        return historyList;
    }

    public boolean logPriceChange(int productId, double newPrice, String priceType, int userId, Integer orderId, String userName) {
        // Kiểm tra sản phẩm có tồn tại không
        String checkProductSql = "SELECT id FROM Products WHERE id = ? AND isDeleted = 0";
        try (PreparedStatement checkPs = connection.prepareStatement(checkProductSql)) {
            checkPs.setInt(1, productId);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Product not found or deleted: " + productId);
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        int storeId = getStoreIdByUserId(userId);
        if (storeId == -1) {
            System.out.println("Store not found for user: " + userId);
            return false;
        }

        String sql = """
        INSERT INTO ProductPriceHistory 
        (product_id, price, importPrice, type, created_by, store_id, order_id) 
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            if ("sell".equals(priceType)) {
                stmt.setDouble(2, newPrice);
                stmt.setDouble(3, 0);
            } else {
                stmt.setDouble(2, 0);
                stmt.setDouble(3, newPrice);
            }
            stmt.setString(4, priceType);
            stmt.setString(5, userName);
            stmt.setInt(6, storeId);
            if (orderId == null) {
                stmt.setNull(7, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(7, orderId);
            }

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
        }
        return false;
    }

    public boolean updateProductPrice(int productId, double newPrice, int userId, String userName) {
        String sql = "UPDATE Products SET price = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, newPrice);
            stmt.setInt(2, productId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Log price change to history
                return logPriceChange(productId, newPrice, "sell", userId, null, userName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean importProduct(int productId, double importPrice, int userId, int orderId, String userName) {
        return logPriceChange(productId, importPrice, "import", userId, orderId, userName);
    }

    private int getStoreIdByUserId(int userId) {
        String sql = "SELECT store_id FROM Users WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("store_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getTotalHistoryExportRecords(String keyword, int userId, String startDate, String endDate) {
        int storeId = getStoreIdByUserId(userId);
        String sql = """
        SELECT COUNT(*) as total
        FROM ProductPriceHistory pph
        JOIN Products p ON pph.product_id = p.id
        WHERE p.store_id = ? 
          AND pph.type = 'sell'
          AND pph.isDeleted = 0
          AND p.name LIKE ?
          AND (pph.created_at BETWEEN ? AND DATE_ADD(?, INTERVAL 1 DAY))
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, startDate);
            ps.setString(4, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalHistoryRecords(String keyword, int userId, String startDate, String endDate) {
        int storeId = getStoreIdByUserId(userId);
        String sql = """
        SELECT COUNT(*) as total
        FROM ProductPriceHistory pph
        JOIN Products p ON pph.product_id = p.id
        WHERE p.store_id = ? 
          AND pph.type = 'import'
          AND pph.isDeleted = 0
          AND p.name LIKE ?
          AND (pph.created_at BETWEEN ? AND DATE_ADD(?, INTERVAL 1 DAY))
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, startDate);
            ps.setString(4, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
        // Khởi tạo instance của DAOProduct
        DAOProduct dao = DAOProduct.INSTANCE;

        // Test userId - thay đổi giá trị này theo user thực tế trong database của bạn
        int testUserId = 2; // Giả sử userId = 2

        System.out.println("=== BẮT ĐẦU KIỂM TRA CÁC PHƯƠNG THỨC ===\n");

        // 1. Test getStoreIdByUserId
        System.out.println("1. Test getStoreIdByUserId:");
        int storeId = dao.getStoreIdByUserId(testUserId);
        System.out.println("Store ID của user " + testUserId + ": " + storeId);
        System.out.println();

        // 2. Test getAllImportPriceHistory
        System.out.println("2. Test getAllImportPriceHistory:");
        List<ProductPriceHistory> importHistory = dao.getAllImportPriceHistory(testUserId, "import");
        System.out.println("Số lượng lịch sử giá nhập: " + importHistory.size());
        if (!importHistory.isEmpty()) {
            System.out.println("Chi tiết record đầu tiên:");
            ProductPriceHistory firstRecord = importHistory.get(0);
            printProductPriceHistory(firstRecord);
        }
        System.out.println();

        // 3. Test getAllExportPriceHistory
        System.out.println("3. Test getAllExportPriceHistory:");
        List<ProductPriceHistory> exportHistory = dao.getAllExportPriceHistory(testUserId, "sell");
        System.out.println("Số lượng lịch sử giá bán: " + exportHistory.size());
        if (!exportHistory.isEmpty()) {
            System.out.println("Chi tiết record đầu tiên:");
            ProductPriceHistory firstRecord = exportHistory.get(0);
            printProductPriceHistory(firstRecord);
        }
        System.out.println();

        // 4. Test getImportPriceHistory với phân trang
        System.out.println("4. Test getImportPriceHistory với phân trang:");
        String keyword = "";
        int page = 1;
        int recordsPerPage = 5;
        String sortOrder = "desc";
        String startDate = "2025-01-01";
        String endDate = "2025-12-31";

        List<ProductPriceHistory> pagedImportHistory = dao.getImportPriceHistory(
                keyword, page, recordsPerPage, testUserId, sortOrder, startDate, endDate
        );
        System.out.println("Số lượng record trên trang " + page + ": " + pagedImportHistory.size());
        if (!pagedImportHistory.isEmpty()) {
            System.out.println("Chi tiết các record:");
            for (ProductPriceHistory record : pagedImportHistory) {
                printProductPriceHistory(record);
            }
        }
        System.out.println();

        // 5. Test getTotalHistoryRecords
        System.out.println("5. Test getTotalHistoryRecords:");
        int totalRecords = dao.getTotalHistoryRecords(keyword, testUserId, startDate, endDate);
        System.out.println("Tổng số records: " + totalRecords);
        System.out.println();

        // 6. Test logPriceChange và updateProductPrice
        System.out.println("6. Test logPriceChange và updateProductPrice:");
        // Thay đổi productId theo database của bạn
        int testProductId = 17; // Giả sử productId = 17
        double newPrice = 50000.0;

        boolean updateResult = dao.updateProductPrice(testProductId, newPrice, testUserId, "John Doe");
        System.out.println("Kết quả cập nhật giá bán: " + updateResult);
        System.out.println();

        // 7. Test importProduct
        System.out.println("7. Test importProduct:");
        // Thay đổi orderId theo database của bạn
        int testOrderId = 1; // Giả sử orderId = 1
        double importPrice = 45000.0;

        boolean importResult = dao.importProduct(testProductId, importPrice, testUserId, testOrderId, "Jane Smith");
        System.out.println("Kết quả cập nhật giá nhập: " + importResult);

        System.out.println("\n=== KẾT THÚC KIỂM TRA ===");
    }

    private static void printProductPriceHistory(ProductPriceHistory history) {
        System.out.println("----------------------------------------");
        System.out.println("ID: " + history.getHistoryID());
        System.out.println("Product ID: " + history.getProductID());
        System.out.println("Product Name: " + history.getProductName());
        System.out.println("Price: " + history.getPrice());
        System.out.println("Type: " + history.getPriceType());
        System.out.println("Created At: " + history.getChangedAt());
        System.out.println("Changed By: " + history.getChangedBy());
        if (history.getSupplier() != null) {
            System.out.println("Supplier Name: " + history.getSupplier());
        }
        System.out.println("----------------------------------------");
    }
}
