package dal;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBContext {

    public Connection connection;
    public PreparedStatement statement;
    public ResultSet resultSet;

    public DBContext() {
        try {
            //Change the username password and url to connect your own database
            String username = "root";
            String password = "123456";
            String url = "jdbc:mysql://localhost:3306/ISP392_Project2";
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        System.out.println(new DBContext().connection);
    }
    
    public Connection getConnection() {
        return new DBContext().connection;
    }
    
    public void closeResources() {
        try {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }
            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
