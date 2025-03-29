package controller.order;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dal.*;
import entity.*;

@WebServlet("/orders")
public class ordersController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private OrdersDAO ordersDAO;
    private userDAO userDAO;
    private OrderDetailsDAO orderDetailsDAO;
    private productsDAO productsDAO;
    private customerDAO customerDAO;
    private static final int RECORDS_PER_PAGE = 10;

    @Override
    public void init() {
        ordersDAO = new OrdersDAO();
        userDAO = new userDAO();
        orderDetailsDAO = new OrderDetailsDAO();
        productsDAO = new productsDAO();
        customerDAO = new customerDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = 1; // Default to page 1
        int recordsPerPage = RECORDS_PER_PAGE;

        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
                if (page <= 0) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        // Fetch total record count (without status filter)
        int totalRecords = ordersDAO.getTotalOrderCount();
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        // Get paginated order list
        int start = (page - 1) * recordsPerPage;
        if (start >= totalRecords) {
            start = Math.max(0, totalRecords - recordsPerPage);
        }

        List<Orders> orders = ordersDAO.getOrdersWithPagination(start, recordsPerPage);

        // Fetch supporting data
        List<Users> usersList = userDAO.findAll();
        List<OrderDetails> orderDetailsList = orderDetailsDAO.getAllOrderDetails();
        List<Products> products = productsDAO.findAllAsList();
        List<Customers> customersList = customerDAO.findAll();

        // Lấy tham số lọc từ request (amountFilter)
        String amountFilter = request.getParameter("amountFilter");

        // Log amountFilter để kiểm tra
        System.out.println("Amount Filter: " + amountFilter);

        // Nếu amountFilter null hoặc rỗng thì không lọc
        List<Orders> filteredOrders = (amountFilter == null || amountFilter.isEmpty())
                ? orders // Nếu không lọc, sử dụng danh sách orders ban đầu
                : ordersDAO.getOrdersByAmount(amountFilter); // Lọc theo giá trị âm hoặc dương

        // Log kiểm tra danh sách đơn hàng sau khi lọc
        System.out.println("Filtered Orders Size: " + filteredOrders.size());

        // Lưu trữ danh sách orders vào request để hiển thị trong JSP
        request.setAttribute("orders", filteredOrders); // Lưu đúng danh sách đã lọc

        // Set attributes for JSP
        request.setAttribute("orders", orders);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("usersList", usersList);
        request.setAttribute("orderDetailsList", orderDetailsList);
        request.setAttribute("products", products);
        request.setAttribute("customersList", customersList);

        // Forward to JSP
        request.getRequestDispatcher("views/sale/orderHistory.jsp").forward(request, response);
    }

    public static void main(String[] args) {
        // Instantiate DAO classes
        OrdersDAO orderDAO = new OrdersDAO();
        userDAO userDAO = new userDAO();
        OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAO();
        productsDAO productsDAO = new productsDAO();
        customerDAO customerDAO = new customerDAO();

        // Test Order retrieval
        System.out.println("=== Fetching Orders ===");
        List<Orders> orders = orderDAO.getAllOrders();
        for (Orders order : orders) {
            System.out.println(order);
        }

        // Test User retrieval
        System.out.println("\n=== Fetching Users ===");
        List<Users> usersList = userDAO.findAll();
        for (Users user : usersList) {
            System.out.println(user);
        }

        // Test Order Details retrieval
        System.out.println("\n=== Fetching Order Details ===");
        List<OrderDetails> orderDetails = orderDetailsDAO.getAllOrderDetails();
        for (OrderDetails detail : orderDetails) {
            System.out.println(detail);
        }

        // Test Products retrieval using List instead of Vector
        System.out.println("\n=== Fetching Products ===");
        List<Products> products = productsDAO.findAllAsList();
        for (Products product : products) {
            System.out.println(product);
        }

        // Test Customer List retrieval
        System.out.println("=== Fetching Customers ===");
        List<Customers> customersList = customerDAO.findAll();
        for (Customers customer : customersList) {
            System.out.println(customer);
        }
    }
}
