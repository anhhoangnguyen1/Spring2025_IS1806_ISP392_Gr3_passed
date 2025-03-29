package controller.settings;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "SettingsController", urlPatterns = {"/settings"})
public class SettingsController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String service = request.getParameter("service");
        
        if (service == null) {
            // Hiển thị trang settings
            HttpSession session = request.getSession();
            Integer lowStockThreshold = (Integer) session.getAttribute("lowStockThreshold");
            if (lowStockThreshold == null) {
                lowStockThreshold = 20; // Giá trị mặc định
            }
            request.setAttribute("lowStockThreshold", lowStockThreshold);
            request.getRequestDispatcher("views/settings/settings.jsp").forward(request, response);
        } else if (service.equals("updateLowStockThreshold")) {
            // Cập nhật ngưỡng low stock
            try {
                int newThreshold = Integer.parseInt(request.getParameter("lowStockThreshold"));
                if (newThreshold > 0) {
                    HttpSession session = request.getSession();
                    session.setAttribute("lowStockThreshold", newThreshold);
                    request.setAttribute("successMessage", "Low stock threshold updated successfully!");
                } else {
                    request.setAttribute("errorMessage", "Threshold must be greater than 0!");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid threshold value!");
            }
            
            // Chuyển hướng lại về trang settings
            request.getRequestDispatcher("views/settings/settings.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
} 