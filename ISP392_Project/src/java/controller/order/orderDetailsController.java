package controller.order;

import dal.*;
import entity.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author Admin
 */
@WebServlet(name="orderDetailsController", urlPatterns={"/orderDetailsController"})
public class orderDetailsController extends HttpServlet {
    private OrderDetailsDAO orderDetailsDAO;

    public void init() {
        orderDetailsDAO = new OrderDetailsDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        List<OrderDetails> details = orderDetailsDAO.getOrderDetailsByOrderId(orderId);

        PrintWriter out = response.getWriter();
        for (OrderDetails detail : details) {
            out.println("<tr>"
                    + "<td>" + detail.getOrderDetailID() + "</td>"
                    + "<td>" + detail.getQuantity() + "</td>"
                    + "<td>" + detail.getPrice() + " VND</td>"
                    + "<td>" + detail.getOrderID() + "</td>"
                    + "<td>" + detail.getProductID() + "</td>"
                    + "</tr>");
        }
    }
}