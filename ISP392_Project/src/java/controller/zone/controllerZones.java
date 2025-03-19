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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
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

    zoneDAO zoneDAO = new zoneDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        String fullName = (String) session.getAttribute("fullName");
        if (fullName == null) {
            response.sendRedirect("login");
            return;
        }

        String service = request.getParameter("service");
        if (service == null) {
            service = "zones";
        }

        String sortBy = request.getParameter("sortBy");
        if (sortBy == null || !sortBy.equals("name")) {
            sortBy = "id";
        }

        String sortOrder = request.getParameter("sortOrder");
        if (sortOrder == null || (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC"))) {
            sortOrder = "ASC";
        }

        switch (service) {
            case "zones": {
                String keyword = request.getParameter("searchZone");
                if (keyword == null) {
                    keyword = "";
                }

                int index = 1;
                try {
                    index = Integer.parseInt(request.getParameter("index")); // Lấy index từ URL
                    if (index < 1) {
                        index = 1; // Đảm bảo index không âm
                    }
                } catch (NumberFormatException ignored) {
                    // Nếu không parse được, giữ mặc định index = 1
                }

                int total = zoneDAO.countZones(keyword);
                int endPage = (total % 5 == 0) ? total / 5 : (total / 5) + 1;

                // Đảm bảo index không vượt quá endPage
                if (index > endPage) {
                    index = endPage;
                }

                List<Zone> list = zoneDAO.searchZones(keyword, index, 5, sortBy, sortOrder);
                String notification = (String) session.getAttribute("Notification");
                if (notification != null) {
                    request.setAttribute("Notification", notification);
                    session.removeAttribute("Notification");
                }

                request.setAttribute("list", list);
                request.setAttribute("endPage", endPage);
                request.setAttribute("index", index); // Đảm bảo index được truyền vào JSP
                request.setAttribute("searchZone", keyword);
                request.setAttribute("sortBy", sortBy);
                request.setAttribute("sortOrder", sortOrder);

                request.getRequestDispatcher("views/zone/zones.jsp").forward(request, response);
                break;
            }
            case "searchZonesAjax": {
                String keyword = request.getParameter("keyword");
                if (keyword == null) {
                    keyword = "";
                }
                int index = 1;
                try {
                    index = Integer.parseInt(request.getParameter("index"));
                } catch (NumberFormatException ignored) {
                }

                int pageSize = 5;
                int total = zoneDAO.countZones(keyword);
                int endPage = (total % pageSize == 0) ? total / pageSize : (total / pageSize) + 1;
                List<Zone> zones = zoneDAO.searchZones(keyword, index, pageSize, sortBy, sortOrder);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print("{");
                out.print("\"zones\": [");
                for (int i = 0; i < zones.size(); i++) {
                    Zone zone = zones.get(i);
                    out.print("{");
                    out.print("\"id\": " + zone.getId() + ",");
                    out.print("\"name\": \"" + zone.getName() + "\",");
                    out.print("\"storeId\": " + (zone.getStoreId() != null ? "{\"id\": " + zone.getStoreId().getId() + "}" : "null") + ",");
                    out.print("\"createdBy\": \"" + zone.getCreatedBy() + "\",");
                    out.print("\"status\": \"" + zone.getStatus() + "\",");
                    out.print("\"productName\": \"" + (zone.getProductId() != null ? zone.getProductId().getName() : "N/A") + "\"");
                    out.print("}");
                    if (i < zones.size() - 1) {
                        out.print(",");
                    }
                }
                out.print("],");
                out.print("\"endPage\": " + endPage + ",");
                out.print("\"index\": " + index);
                out.print("}");
                out.flush();
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
                request.setAttribute("fullName", fullName);
                request.getRequestDispatcher("views/zone/addZone.jsp").forward(request, response);
                break;
            }
            case "editZone": {
                int zoneId = Integer.parseInt(request.getParameter("zone_id"));
                Zone zoneForEdit = zoneDAO.getZoneById(zoneId);
                request.setAttribute("zone", zoneForEdit);
                request.setAttribute("fullName", fullName);
                request.setAttribute("sortOrder", sortOrder);
                request.getRequestDispatcher("views/zone/editZone.jsp").forward(request, response);
                break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");
        HttpSession session = request.getSession();

        String fullName = (String) session.getAttribute("fullName");
        if (fullName == null) {
            response.sendRedirect("login");
            return;
        }

        if ("addZone".equals(service)) {
            String name = request.getParameter("name");
            String description = request.getParameter("description");

            // Lấy storeId từ session (được gán từ controllerStore)
            String storeIdStr = (String) session.getAttribute("storeID");
            if (storeIdStr == null) {
                session.setAttribute("errorMessage", "No store associated with your account. Please create a store first.");
                response.sendRedirect("Stores?service=createStore");
                return;
            }

            Integer storeId;
            try {
                storeId = Integer.parseInt(storeIdStr);
            } catch (NumberFormatException e) {
                session.setAttribute("errorMessage", "Invalid store ID in session.");
                response.sendRedirect("zones?service=addZone");
                return;
            }

            // Kiểm tra trùng tên với storeId từ session
            if (zoneDAO.checkNameExists(name, -1, storeId)) {
                request.setAttribute("nameError", "Zone name already exists for this store.");
                request.setAttribute("name", name);
                request.setAttribute("description", description);
                request.getRequestDispatcher("views/zone/addZone.jsp").forward(request, response);
                return;
            }

            Stores store = new Stores();
            store.setId(storeId); // Gán storeId từ session vào entity Stores

            Zone zone = Zone.builder()
                    .name(name)
                    .description(description)
                    .createdBy(fullName)
                    .storeId(store) // Gán entity Stores với storeId từ session
                    .status(request.getParameter("status") != null ? request.getParameter("status") : "Active")
                    .build();

            zoneDAO.insertZone(zone);

            int total = zoneDAO.countZones("");
            int pageSize = 5;
            int endPage = (total % pageSize == 0) ? total / pageSize : (total / pageSize) + 1;

            session.setAttribute("Notification", "Zone added successfully.");

            String sortBy = request.getParameter("sortBy");
            if (sortBy == null) {
                sortBy = "id";
            }
            String sortOrder = request.getParameter("sortOrder");
            if (sortOrder == null) {
                sortOrder = "ASC";
            }
            response.sendRedirect("zones?service=zones&sortBy=" + sortBy + "&sortOrder=" + sortOrder + "&index=" + endPage);
            return;
        }

        if ("editZone".equals(service)) {
            int zoneId = Integer.parseInt(request.getParameter("zone_id"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");

            // Lấy storeId từ session
            String storeIdStr = (String) session.getAttribute("storeID");
            if (storeIdStr == null) {
                session.setAttribute("errorMessage", "No store associated with your account. Please create a store first.");
                response.sendRedirect("Stores?service=createStore");
                return;
            }

            Integer storeId;
            try {
                storeId = Integer.parseInt(storeIdStr);
            } catch (NumberFormatException e) {
                session.setAttribute("errorMessage", "Invalid store ID in session.");
                response.sendRedirect("zones?service=editZone&zone_id=" + zoneId);
                return;
            }

            // Kiểm tra trùng tên với storeId từ session
            if (zoneDAO.checkNameExists(name, zoneId, storeId)) {
                request.setAttribute("nameError", "Zone name already exists for this store.");
                Zone zoneForEdit = zoneDAO.getZoneById(zoneId);
                request.setAttribute("zone", zoneForEdit);
                request.setAttribute("fullName", fullName);
                request.setAttribute("sortOrder", request.getParameter("sortOrder"));
                request.getRequestDispatcher("views/zone/editZone.jsp").forward(request, response);
                return;
            }

            Stores store = new Stores();
            store.setId(storeId); // Gán storeId từ session vào entity Stores

            Zone zone = Zone.builder()
                    .id(zoneId)
                    .name(name)
                    .description(description)
                    .storeId(store) // Gán entity Stores với storeId từ session
                    .status(request.getParameter("status") != null ? request.getParameter("status") : "Active")
                    .build();

            zoneDAO.updateZone(zone);
            session.setAttribute("Notification", "Zone details updated successfully.");

            String sortBy = request.getParameter("sortBy");
            String sortOrder = request.getParameter("sortOrder");
            String index = request.getParameter("index");

            response.sendRedirect("zones?service=zones&sortBy=" + sortBy + "&sortOrder=" + sortOrder + "&index=" + index);
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
                .storeId(store)
                .status(request.getParameter("status") != null ? request.getParameter("status") : "Active")
                .build();
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
