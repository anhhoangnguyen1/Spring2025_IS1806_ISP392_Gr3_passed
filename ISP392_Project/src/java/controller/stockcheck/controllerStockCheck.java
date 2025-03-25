package controller.stockcheck;

import dal.stockCheckDAO;
import dal.zoneDAO;
import entity.StockCheck;
import entity.Zone;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class controllerStockCheck extends HttpServlet {
    private stockCheckDAO stockCheckDAO;
    private zoneDAO zoneDAO;

    @Override
    public void init() throws ServletException {
        stockCheckDAO = new stockCheckDAO();
        zoneDAO = new zoneDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "create":
                    showCreateForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteStockCheck(request, response);
                    break;
                default:
                    listStockChecks(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "create":
                    createStockCheck(request, response);
                    break;
                case "update":
                    updateStockCheck(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listStockChecks(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String sortBy = request.getParameter("sortBy") != null ? request.getParameter("sortBy") : "stockCheckId";
        String order = request.getParameter("order") != null ? request.getParameter("order") : "ASC";
        String search = request.getParameter("search");

        List<StockCheck> stockChecks = stockCheckDAO.getAllStockChecks(sortBy, order, search);
        request.setAttribute("stockChecks", stockChecks);
        request.getRequestDispatcher("views/stockcheck/list.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<Zone> zones = zoneDAO.getAllZones();
        request.setAttribute("zones", zones);
        request.getRequestDispatcher("views/stockcheck/create.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int stockCheckId = Integer.parseInt(request.getParameter("id"));
        StockCheck stockCheck = stockCheckDAO.getStockCheckById(stockCheckId);
        request.setAttribute("stockCheck", stockCheck);
        request.getRequestDispatcher("views/stockcheck/edit.jsp").forward(request, response);
    }

    private void createStockCheck(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        StockCheck stockCheck = new StockCheck();
        stockCheck.setZoneId(Integer.parseInt(request.getParameter("zoneId")));
        stockCheck.setProductId(Integer.parseInt(request.getParameter("productId")));
//        stockCheck.setCheckedDate(Date(request.getParameter("checkedDate")));
        stockCheck.setActualQuantity(Integer.parseInt(request.getParameter("actualQuantity")));
        stockCheck.setRecordedQuantity(Integer.parseInt(request.getParameter("recordedQuantity")));
        stockCheck.setDiscrepancy(stockCheck.getActualQuantity() - stockCheck.getRecordedQuantity());
        stockCheck.setNotes(request.getParameter("notes"));

        stockCheckDAO.createStockCheck(stockCheck);
        response.sendRedirect("stockcheck");
    }

    private void updateStockCheck(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        StockCheck stockCheck = new StockCheck();
        stockCheck.setStockCheckId(Integer.parseInt(request.getParameter("stockCheckId")));
        stockCheck.setNotes(request.getParameter("notes"));

        stockCheckDAO.updateStockCheck(stockCheck);
        response.sendRedirect("stockcheck");
    }

    private void deleteStockCheck(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int stockCheckId = Integer.parseInt(request.getParameter("id"));
        stockCheckDAO.deleteStockCheck(stockCheckId);
        response.sendRedirect("stockcheck");
    }
}
