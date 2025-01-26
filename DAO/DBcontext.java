package DAO;

    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.SQLException;

    public class DBcontext {
        public static void main(String[] args) {
            // Thông tin kết nối MySQL
            String url = "jdbc:mysql://localhost:3306/rice_sales_management"; // Thay 'Test' bằng tên database của bạn
            String username = "Huy15"; // Thay bằng tài khoản MySQL của bạn
            String password = "Phamhaihuy123"; // Thay bằng mật khẩu MySQL của bạn

            Connection connection = null;

            try {
                // Tải MySQL JDBC Driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Kết nối tới MySQL
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Kết nối MySQL thành công!");
            } catch (ClassNotFoundException e) {
                System.out.println("Không tìm thấy Driver: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Lỗi kết nối MySQL: " + e.getMessage());
            } finally {
                // Đóng kết nối nếu mở
                if (connection != null) {
                    try {
                        connection.close();
                        System.out.println("Đã đóng kết nối.");
                    } catch (SQLException e) {
                        System.out.println("Lỗi khi đóng kết nối: " + e.getMessage());
                    }
                }
            }
        }
    }
