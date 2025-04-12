package controller.order;

import dal.OrdersDAO;
import entity.OrderDetails;
import entity.Orders;
import entity.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "invoiceHistoryServlet", urlPatterns = {"/InvoiceHistory"})
public class invoiceHistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Kiểm tra đăng nhập
        Users user = (Users) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Kiểm tra quyền truy cập
        if (!user.getRole().equals("owner") && !user.getRole().equals("staff")) {
            request.setAttribute("errorMessage", "You don't have permission to access this page");
            request.getRequestDispatcher("views/error/accessDenied.jsp").forward(request, response);
            return;
        }

        OrdersDAO ordersDAO = new OrdersDAO();
        String service = request.getParameter("service");
        String search = request.getParameter("searchInvoice");
        String type = request.getParameter("type");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");

        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception ignored) {
        }

        int pageSize = 5;
        int offset = (page - 1) * pageSize;

        List<Orders> pagedOrders;
        int totalSize;

        // Lấy store_id của user hiện tại
        int storeId = user.getStoreId().getId();

        if ("detail".equals(service)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("order_id"));
                Orders order = ordersDAO.getOrderById(orderId);
                
                // Kiểm tra quyền truy cập chi tiết hóa đơn
                if (order != null && order.getStoreId().getId() != storeId) {
                    request.setAttribute("errorMessage", "You don't have permission to view this invoice");
                    request.getRequestDispatcher("views/invoice/invoiceDetail.jsp").forward(request, response);
                    return;
                }
                
                if (order != null) {
                    List<OrderDetails> details = ordersDAO.getOrderDetailsByOrderId(orderId);
                    double totalAmount = details.stream().mapToDouble(d -> d.getUnitPrice() * d.getQuantity()).sum();
                    request.setAttribute("order", order);
                    request.setAttribute("details", details);
                    request.setAttribute("totalAmount", totalAmount);
                    request.getRequestDispatcher("views/invoice/invoiceDetailFragment.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid order ID");
            }
        }

        if (search != null && !search.trim().isEmpty()) {
            pagedOrders = ordersDAO.getOrdersByCustomerNamePaging(search, offset, pageSize);
            totalSize = ordersDAO.getTotalOrdersCountBySearch(search);
        } else if (type != null && !type.trim().isEmpty()) {
            List<Orders> all = ordersDAO.getOrdersByType(type);
            // Lọc theo store_id
            all = all.stream().filter(order -> order.getStoreId().getId() == storeId).toList();
            totalSize = all.size();
            pagedOrders = all.stream().skip(offset).limit(pageSize).toList();
        } else if (fromDate != null && toDate != null && !fromDate.trim().isEmpty() && !toDate.trim().isEmpty()) {
            try {
                List<Orders> all = ordersDAO.getOrdersByDateRange(fromDate, toDate);
                // Lọc theo store_id
                all = all.stream().filter(order -> order.getStoreId().getId() == storeId).toList();
                totalSize = all.size();
                pagedOrders = all.stream().skip(offset).limit(pageSize).toList();
            } catch (IllegalArgumentException e) {
                request.setAttribute("errorMessage", e.getMessage());
                pagedOrders = ordersDAO.getOrdersByStoreIdPaging(storeId, offset, pageSize);
                totalSize = ordersDAO.getTotalOrdersCountByStoreId(storeId);
            }
        } else {
            pagedOrders = ordersDAO.getOrdersByStoreIdPaging(storeId, offset, pageSize);
            totalSize = ordersDAO.getTotalOrdersCountByStoreId(storeId);
        }

        int endPage = (int) Math.ceil((double) totalSize / pageSize);
        request.setAttribute("orders", pagedOrders);
        request.setAttribute("totalSize", totalSize);
        request.setAttribute("index", page);
        request.setAttribute("endPage", endPage);

        if ("ajaxPaging".equals(service)) {
            request.getRequestDispatcher("views/invoice/invoiceHistory.jsp?t=body").forward(request, response);
        } else {
            request.getRequestDispatcher("views/invoice/invoiceHistory.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Invoice History Servlet";
    }
}
