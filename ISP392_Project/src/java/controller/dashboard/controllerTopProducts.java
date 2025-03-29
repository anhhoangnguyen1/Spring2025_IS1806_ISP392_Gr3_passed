/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.dashboard;

import com.google.gson.Gson;
import dal.productsDAO;
import dal.revenueDAO;
import entity.Products;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;
import dal.debtDAO;
import entity.DebtNote;
import jakarta.servlet.http.HttpSession;

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
        int invoiceCountToday = revenueDAO.getOrderCountToday();

        request.setAttribute("revenueToday", revenueToday);
        request.setAttribute("invoiceCountToday", invoiceCountToday);
        
        // Tính tổng tiền vào/ra từ nợ
        debtDAO debtDAO = new debtDAO();
        BigDecimal totalMoneyIn = BigDecimal.ZERO;
        BigDecimal totalMoneyOut = BigDecimal.ZERO;
        
        // Lấy storeID từ session
        HttpSession session = request.getSession();
        String storeIDStr = (String) session.getAttribute("storeID");
        int storeID = Integer.parseInt(storeIDStr);
        
        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();
        
        // Lấy tất cả các giao dịch nợ của ngày hôm nay
        List<DebtNote> todayDebts = debtDAO.getAllDebtsByDateRange(today, today, storeID);
        
        // Tính toán tổng thu và tổng chi cho ngày hôm nay
        for (DebtNote debt : todayDebts) {
            if ("+".equals(debt.getType())) {
                totalMoneyIn = totalMoneyIn.add(debt.getAmount().abs());
            } else if ("-".equals(debt.getType())) {
                totalMoneyOut = totalMoneyOut.add(debt.getAmount().abs());
            }
        }
        
        // Cộng thêm doanh thu từ đơn hàng vào totalMoneyIn
        totalMoneyIn = totalMoneyIn.add(BigDecimal.valueOf(revenueToday));
        
        // Tính lợi nhuận thực tế (Net Profit)
        BigDecimal netProfit = totalMoneyIn.subtract(totalMoneyOut);
        
        // Set các giá trị vào request
        request.setAttribute("todayDebts", todayDebts);
        request.setAttribute("totalMoneyIn", totalMoneyIn);
        request.setAttribute("totalMoneyOut", totalMoneyOut);
        request.setAttribute("netProfit", netProfit);
        
        // Xử lý lọc theo thời gian cho lịch sử dòng tiền
        String timeRange = request.getParameter("timeRange");
        if (timeRange == null) {
            timeRange = "last7days"; // Mặc định là 7 ngày gần nhất
        }
        
        // Xử lý phân trang
        int page = 1;
        int recordsPerPage = 10;
        String pageStr = request.getParameter("page");
        if (pageStr != null) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        
        // Tính toán vị trí bắt đầu
        int start = (page - 1) * recordsPerPage;
        
        // Lấy tổng số bản ghi để tính số trang
        int totalRecords = debtDAO.getTotalDebtCount(storeID);
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
        
        // Lấy dữ liệu theo khoảng thời gian
        List<DebtNote> debtHistory;
        BigDecimal periodTotalIn = BigDecimal.ZERO;
        BigDecimal periodTotalOut = BigDecimal.ZERO;
        
        LocalDate startDate;
        LocalDate endDate = LocalDate.now();
        
        switch (timeRange) {
            case "today":
                startDate = endDate;
                break;
            case "yesterday":
                startDate = endDate.minusDays(1);
                endDate = startDate;
                break;
            case "last7days":
                startDate = endDate.minusDays(7);
                break;
            case "thismonth":
                startDate = endDate.withDayOfMonth(1);
                break;
            case "lastmonth":
                startDate = endDate.minusMonths(1).withDayOfMonth(1);
                endDate = startDate.plusMonths(1).minusDays(1);
                break;
            default:
                startDate = endDate.minusDays(7);
                break;
        }
        
        // Lấy dữ liệu phân trang và tính tổng tiền trong khoảng thời gian
        debtHistory = debtDAO.getDebtsByDateRange(startDate, endDate, start, recordsPerPage, storeID);
        List<DebtNote> allDebtsInRange = debtDAO.getAllDebtsByDateRange(startDate, endDate, storeID);
        
        // Tính toán tổng thu và tổng chi
        for (DebtNote debt : allDebtsInRange) {
            if ("+".equals(debt.getType())) {
                periodTotalIn = periodTotalIn.add(debt.getAmount().abs());
            } else if ("-".equals(debt.getType())) {
                periodTotalOut = periodTotalOut.add(debt.getAmount().abs());
            }
        }
        
        // Thêm doanh thu từ đơn hàng vào periodTotalIn nếu đang xem ngày hôm nay
        if ("today".equals(timeRange)) {
            periodTotalIn = periodTotalIn.add(BigDecimal.valueOf(revenueToday));
        }
        
        // Đặt các thuộc tính cho view
        request.setAttribute("timeRange", timeRange);
        request.setAttribute("debtHistory", debtHistory);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("periodTotalIn", periodTotalIn);
        request.setAttribute("periodTotalOut", periodTotalOut);
        
        int threshold = 20; // Ngưỡng cảnh báo sản phẩm sắp hết hàng
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
