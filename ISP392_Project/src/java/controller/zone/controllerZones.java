/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.zone;

import dao.zoneDAO;
import entity.Stores;
import entity.Zone;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 *
 * @author bsd12418
 */
public class controllerZones extends HttpServlet {

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
            out.println("<title>Servlet controllerZones</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet controllerZones at " + request.getContextPath() + "</h1>");
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
    zoneDAO zoneDAO = new zoneDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String service = request.getParameter("service");
        if (service == null) {
            service = "zones";
        }

        String sortBy = request.getParameter("sortBy");
        if (sortBy == null || !sortBy.equals("name")) {
            sortBy = "id"; // Mặc định sắp xếp theo id
        }

        String sortOrder = request.getParameter("sortOrder");
        if (sortOrder == null || (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC"))) {
            sortOrder = "ASC"; // Mặc định tăng dần
        }

        switch (service) {
            case "zones": {
                String keyword = request.getParameter("searchZone");
                if (keyword == null) {
                    keyword = "";
                }

                int index = 1;
                try {
                    index = Integer.parseInt(request.getParameter("index"));
                } catch (NumberFormatException ignored) {
                }

                int total = zoneDAO.countZones(keyword);
                int endPage = (total % 5 == 0) ? total / 5 : (total / 5) + 1;

                List<Zone> list = zoneDAO.searchZones(keyword, index, 5, sortBy, sortOrder);
                String notification = (String) session.getAttribute("Notification");
                if (notification != null) {
                    request.setAttribute("Notification", notification);
                    session.removeAttribute("Notification"); // Xóa thông báo sau khi hiển thị
                }

                request.setAttribute("list", list);
                request.setAttribute("endPage", endPage);
                request.setAttribute("index", index);
                request.setAttribute("searchZone", keyword);
                request.setAttribute("sortBy", sortBy);
                request.setAttribute("sortOrder", sortOrder);

                request.getRequestDispatcher("views/zone/zones.jsp").forward(request, response);
                break;
            }
            case "getZoneById": {
                int id = Integer.parseInt(request.getParameter("zone_id"));
                Zone zone = zoneDAO.getZoneById(id);
                request.setAttribute("zone", zone);
                request.getRequestDispatcher("views/zone/detailZone.jsp").forward(request, response);
                break;
            }
            case "addZone": {
                String createdBy = (String) session.getAttribute("username");
                request.setAttribute("userName", createdBy);
                request.getRequestDispatcher("views/zone/addZone.jsp").forward(request, response);
                break;
            }
            case "editZone": {
                int zoneId = Integer.parseInt(request.getParameter("zone_id"));
                Zone zoneForEdit = zoneDAO.getZoneById(zoneId);
                request.setAttribute("zone", zoneForEdit);
                String userName = (String) session.getAttribute("username");
                request.setAttribute("userName", userName);
                request.setAttribute("sortOrder", sortOrder);
                request.getRequestDispatcher("views/zone/editZone.jsp").forward(request, response);
                break;
            }
            case "deleteZone": { // Thêm case mới để xử lý xóa zone
                int zoneId;
                try {
                    zoneId = Integer.parseInt(request.getParameter("zone_id"));
                } catch (NumberFormatException e) {
                    session.setAttribute("Notification", "Invalid zone ID!");
                    response.sendRedirect("zones?service=zones&sortBy=" + sortBy + "&sortOrder=" + sortOrder);
                    return;
                }

                Zone zone = zoneDAO.getZoneById(zoneId);
                if (zone == null) {
                    session.setAttribute("Notification", "Zone not found or already deleted!");
                } else {
                    zoneDAO.deleteZone(zoneId); // Gọi phương thức soft delete từ zoneDAO
                    session.setAttribute("Notification", "Zone deleted successfully!");
                }
                response.sendRedirect("zones?service=zones&sortBy=" + sortBy + "&sortOrder=" + sortOrder + "&index=" + request.getParameter("index"));
                break;
            }

        }
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
        String service = request.getParameter("service");
        HttpSession session = request.getSession();

        if ("addZone".equals(service)) {
            String name = request.getParameter("name");
            Integer storeId = (request.getParameter("store_id") != null && !request.getParameter("store_id").isEmpty())
                    ? Integer.parseInt(request.getParameter("store_id")) : null;

            // Kiểm tra trùng tên
            if (zoneDAO.checkNameExists(name, -1, storeId)) {

                request.setAttribute("nameError", "Zone name already exists.");
                request.setAttribute("userName", session.getAttribute("username"));
                request.setAttribute("name", name);
                request.setAttribute("store_id", storeId);

                request.getRequestDispatcher("views/zone/addZone.jsp").forward(request, response);
                return;
            }

            Zone zone = getZoneFromRequest(request, true);
            zoneDAO.insertZone(zone);
            session.setAttribute("Notification", "Zone added successfully.");
            response.sendRedirect("zones?service=zones");
            return;
        }

        if ("editZone".equals(service)) {
            int zoneId = Integer.parseInt(request.getParameter("zone_id"));
            String name = request.getParameter("name");
            Integer storeId = (request.getParameter("store_id") != null && !request.getParameter("store_id").isEmpty())
                    ? Integer.parseInt(request.getParameter("store_id")) : null;

            // Kiểm tra trùng tên
            if (zoneDAO.checkNameExists(name, zoneId, storeId)) {
                request.setAttribute("nameError", "Zone name already exists.");
                Zone zoneForEdit = zoneDAO.getZoneById(zoneId);
                request.setAttribute("zone", zoneForEdit);
                request.setAttribute("userName", session.getAttribute("username"));
                request.setAttribute("sortOrder", request.getParameter("sortOrder"));
                request.getRequestDispatcher("views/zone/editZone.jsp").forward(request, response);
                return;
            }

            Zone zone = getZoneFromRequest(request, false);
            zoneDAO.updateZone(zone);
            session.setAttribute("Notification", "Zone details updated successfully.");
            response.sendRedirect("zones?service=zones&sortOrder=" + request.getParameter("sortOrder"));
        }
    }

    private Zone getZoneFromRequest(HttpServletRequest request, boolean isNew) {
        Zone.ZoneBuilder zoneBuilder = Zone.builder();

        if (!isNew) {
            zoneBuilder.id(Integer.parseInt(request.getParameter("zone_id")));
        }

        Stores store = null;
        if (request.getParameter("store_id") != null && !request.getParameter("store_id").isEmpty()) {
            store = new Stores();
            store.setId(Integer.parseInt(request.getParameter("store_id")));
        }

        return zoneBuilder
                .name(request.getParameter("name"))
                .createdBy(isNew ? request.getParameter("createdBy") : null)
                .storeId(store)
                .status(request.getParameter("status") != null ? request.getParameter("status") : "Active")
                .build();
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
