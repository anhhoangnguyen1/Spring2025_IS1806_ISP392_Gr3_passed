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
import java.util.List;

public class controllerStockChecks extends HttpServlet {

    private stockCheckDAO stockCheckDAO = new stockCheckDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        try {
            if (action == null || action.equals("/")) {
                listStockChecks(request, response); // Hiển thị danh sách
            } else if (action.equals("/new")) {
                showNewForm(request, response); // Hiển thị form thêm mới
            } else if (action.equals("/edit")) {
                showEditForm(request, response); // Hiển thị form sửa
            } else if (action.equals("/delete")) {
                deleteStockCheck(request, response); // Xóa bản ghi
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }


    
        
    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String fullName = (String) session.getAttribute("fullName");
        if (fullName == null) {
            response.sendRedirect("login");
            return;
        }

        String service = request.getParameter("service");
        int pageSize = 5;
        try {
            pageSize = Integer.parseInt(request.getParameter("pageSize"));
            if (pageSize <= 0) {
                pageSize = 5;
            }
        } catch (NumberFormatException ignored) {
        }

        if ("add".equals(service)) {
            List<StockCheckTemp> tempList = (List<StockCheckTemp>) session.getAttribute("tempStockCheckList");
            if (tempList == null || tempList.isEmpty()) {
                session.setAttribute("errorMessage", "No zones selected for stock check.");
                response.sendRedirect("StockChecks?service=add");
                return;
            }

            for (StockCheckTemp temp : tempList) {
                StockCheck stockCheck = StockCheck.builder()
                        .zoneId(temp.getZone())
                        .systemQuantity(temp.getSystemQuantity())
                        .actualQuantity(temp.getActualQuantity())
                        .difference(temp.getActualQuantity() - temp.getSystemQuantity())
                        .checkedBy(fullName)
                        .note(temp.getNote())
                        .status("Completed")
                        .build();
                stockCheckDAO.insertStockCheck(stockCheck);
            }

            // Xóa danh sách tạm sau khi lưu
            session.removeAttribute("tempStockCheckList");
            session.setAttribute("Notification", "Stock Check added successfully.");
            response.sendRedirect("StockChecks?service=list&pageSize=" + pageSize);
        } else if ("edit".equals(service)) {
            int id = Integer.parseInt(request.getParameter("id"));
            int zoneId = Integer.parseInt(request.getParameter("zone_id"));
            int systemQuantity = Integer.parseInt(request.getParameter("system_quantity"));
            int actualQuantity = Integer.parseInt(request.getParameter("actual_quantity"));
            String note = request.getParameter("note");
            String status = request.getParameter("status");

            Zone zone = zoneDAO.getZoneById(zoneId);
            StockCheck stockCheck = StockCheck.builder()
                    .id(id)
                    .zoneId(zone)
                    .systemQuantity(systemQuantity)
                    .actualQuantity(actualQuantity)
                    .difference(actualQuantity - systemQuantity)
                    .checkedBy(fullName)
                    .note(note)
                    .status(status)
                    .build();

            stockCheckDAO.updateStockCheck(stockCheck);
            session.setAttribute("Notification", "Stock Check updated successfully.");
            String index = request.getParameter("index");
            String sortBy = request.getParameter("sortBy");
            String sortOrder = request.getParameter("sortOrder");
            response.sendRedirect("StockChecks?service=list&index=" + index + "&sortBy=" + sortBy + "&sortOrder=" + sortOrder + "&pageSize=" + pageSize);

}
    }

    // Class tạm để lưu thông tin kiểm kho trong session
    public static class StockCheckTemp {

    private Zone zone;
    private int systemQuantity;
    private int actualQuantity;
    private String note;

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public int getSystemQuantity() {
        return systemQuantity;
    }

    public void setSystemQuantity(int systemQuantity) {
        this.systemQuantity = systemQuantity;
    }

    public int getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(int actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
}
