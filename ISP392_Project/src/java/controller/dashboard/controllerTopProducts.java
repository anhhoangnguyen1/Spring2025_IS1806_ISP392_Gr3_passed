/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.dashboard;

import com.google.gson.Gson;
import dal.productsDAO;
import entity.Products;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author bsd12418
 */
public class controllerTopProducts extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet controllerTopProducts</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet controllerTopProducts at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        productsDAO dao = new productsDAO();
        List<String[]> topProducts = dao.getTopSellingProductNamesOfMonth();
        request.setAttribute("topProducts", topProducts);

        // Lấy doanh thu
        revenueDAO revenueDAO = new revenueDAO();

        // Kiểm tra period từ request
        String period = request.getParameter("period");

        // Nếu không có period từ request, lấy từ session
        if (period == null) {
            period = (String) request.getSession().getAttribute("selectedPeriod");
        }

        // Nếu vẫn chưa có, đặt mặc định là "last7days"
        if (period == null) {
            period = "last7days";
        }

        // Lưu lựa chọn vào session
        request.getSession().setAttribute("selectedPeriod", period);

        // Lấy dữ liệu doanh thu theo period
        Object revenueData = null;
        switch (period) {
            case "today":
                revenueData = revenueDAO.getRevenueToday();
                break;
            case "yesterday":
                revenueData = revenueDAO.getRevenueYesterday();
                break;
            case "last7days":
                revenueData = revenueDAO.getRevenueLast7Days();
                break;
            case "thismonth":
                revenueData = revenueDAO.getRevenueThisMonth();
                break;
            case "lastmonth":
                revenueData = revenueDAO.getRevenueLastMonth();
                break;
            default:
                revenueData = "Invalid period";
                break;
        }

        if (revenueData == null) {
            revenueData = "[]";  // Tránh lỗi JSON null
        }

        request.setAttribute("revenueData", new Gson().toJson(revenueData));
        request.setAttribute("selectedPeriod", period);  // Gửi về JSP để đánh dấu mặc định

        double revenueToday = revenueDAO.getRevenueToday();
        int invoiceCountToday = revenueDAO.getInvoiceCountToday();

        request.setAttribute("revenueToday", revenueToday);
        request.setAttribute("invoiceCountToday", invoiceCountToday);
        
        
        
        int threshold = 1400; // Ngưỡng cảnh báo sản phẩm sắp hết hàng
        List<Products> lowStockProducts = dao.getLowStockProducts(threshold);
        
        request.setAttribute("lowStockProducts", lowStockProducts);
        request.getRequestDispatcher("views/dashboard/dashboard.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
