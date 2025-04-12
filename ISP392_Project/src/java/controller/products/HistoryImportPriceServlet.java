package controller.products;

import dal.DAOProduct;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import entity.ProductPriceHistory;

public class HistoryImportPriceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userId");
        int currentPage = 1;
        int recordsPerPage = 5;

        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }

        String sortOrder = request.getParameter("sortOrder");
        if (sortOrder == null || (!sortOrder.equals("asc") && !sortOrder.equals("desc"))) {
            sortOrder = "desc";
        }

        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        if (startDate == null || startDate.isEmpty()) {
            startDate = "2025-01-01";
        }
        if (endDate == null || endDate.isEmpty()) {
            endDate = LocalDate.now().toString();
        }

        System.out.println("Debug - Parameters:");
        System.out.println("userId: " + userId);
        System.out.println("currentPage: " + currentPage);
        System.out.println("keyword: " + keyword);
        System.out.println("sortOrder: " + sortOrder);
        System.out.println("startDate: " + startDate);
        System.out.println("endDate: " + endDate);

        List<ProductPriceHistory> HistoryList = DAOProduct.INSTANCE.getImportPriceHistory(
                keyword, currentPage, recordsPerPage, userId, sortOrder, startDate, endDate
        );

        System.out.println("Debug - Results:");
        System.out.println("HistoryList size: " + (HistoryList != null ? HistoryList.size() : "null"));
        if (HistoryList != null && !HistoryList.isEmpty()) {
            System.out.println("First record: " + HistoryList.get(0).toString());
        }

        int totalRecords = DAOProduct.INSTANCE.getTotalHistoryRecords(keyword, userId, startDate, endDate);
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        request.setAttribute("HistoryList", HistoryList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("keyword", keyword);
        request.setAttribute("sortOrder", sortOrder);
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);

        request.getRequestDispatcher("/views/priceHistory/historyImportPrice.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userId");
        String userName = (String) session.getAttribute("fullName");
        
        String productIdStr = request.getParameter("productId");
        String importPriceStr = request.getParameter("importPrice");
        String orderIdStr = request.getParameter("orderId");
        
        if (productIdStr != null && importPriceStr != null && orderIdStr != null) {
            try {
                int productId = Integer.parseInt(productIdStr);
                double importPrice = Double.parseDouble(importPriceStr);
                int orderId = Integer.parseInt(orderIdStr);
                
                // Update import price and log history
                boolean success = DAOProduct.INSTANCE.importProduct(productId, importPrice, userId, orderId, userName);
                if (success) {
                    response.sendRedirect("HistoryImportPriceServlet?success=1");
                    return;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        
        response.sendRedirect("HistoryImportPriceServlet?error=1");
    }
} 