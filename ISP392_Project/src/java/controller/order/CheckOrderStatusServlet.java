package controller.order;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.OrderWorker;

@WebServlet(name = "CheckOrderStatusServlet", urlPatterns = {"/CheckOrderStatusServlet"})
public class CheckOrderStatusServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if ("admin".equals(role)) {
            response.sendRedirect("dashboard.jsp");
            return;
        }

        int userId = Integer.parseInt(request.getParameter("userId"));
        boolean clearStatus = request.getParameter("clear") != null;

        if (clearStatus) {
            OrderWorker.clearProcessedOrder(userId);
            response.getWriter().write("{\"status\": \"cleared\"}");
            return;
        }

        String status = OrderWorker.getStatus(userId);

        switch (status) {
            case "done":
                OrderWorker.clearProcessedOrder(userId);
                request.setAttribute("message", "success");
                request.getRequestDispatcher("views/invoice/addOrder.jsp").forward(request, response);
                break;
            case "error":
                OrderWorker.clearProcessedOrder(userId);
                request.setAttribute("message", "error");
                request.getRequestDispatcher("views/invoice/addOrder.jsp").forward(request, response);
                break;
            default:
                request.getRequestDispatcher("views/invoice/addOrder.jsp").forward(request, response);
                break;
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet to check background order processing status for current user";
    }
}
