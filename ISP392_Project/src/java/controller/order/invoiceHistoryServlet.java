package controller.order;

import dal.OrdersDAO;
import entity.OrderDetails;
import entity.Orders;
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

        OrdersDAO ordersDAO = new OrdersDAO(); // ✅ KHỞI TẠO Ở ĐÂY

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
        if ("detail".equals(service)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("order_id"));
                Orders order = ordersDAO.getOrderById(orderId);
                List<OrderDetails> details = ordersDAO.getOrderDetailsByOrderId(orderId);
                double total = details.stream().mapToDouble(d -> d.getUnitPrice() * d.getQuantity()).sum();

                request.setAttribute("order", order);
                request.setAttribute("details", details);
                request.setAttribute("totalAmount", total);

                request.getRequestDispatcher("/views/invoice/invoiceDetailFragment.jsp").forward(request, response);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Ưu tiên: search > type > date > all
        if (search != null && !search.trim().isEmpty()) {
            pagedOrders = ordersDAO.getOrdersByCustomerNamePaging(search, offset, pageSize);
            totalSize = ordersDAO.getTotalOrdersCountBySearch(search);
        } else if (type != null && !type.trim().isEmpty()) {
            List<Orders> all = ordersDAO.getOrdersByType(type);
            totalSize = all.size();
            pagedOrders = all.stream().skip(offset).limit(pageSize).toList();
        } else if (fromDate != null && toDate != null && !fromDate.trim().isEmpty() && !toDate.trim().isEmpty()) {
            List<Orders> all = ordersDAO.getOrdersByDateRange(fromDate, toDate);
            totalSize = all.size();
            pagedOrders = all.stream().skip(offset).limit(pageSize).toList();
        } else {
            pagedOrders = ordersDAO.getOrdersPaging(offset, pageSize);
            totalSize = ordersDAO.getTotalOrdersCount();
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
