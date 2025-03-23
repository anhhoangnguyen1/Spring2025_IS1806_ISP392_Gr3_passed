package controller.zone;

import dal.zoneDAO;
import entity.Stores;
import entity.Zone;
import dal.productsDAO; // Thêm import
import java.io.IOException;
import java.io.PrintWriter;
import entity.Products; // Thêm import nếu cần
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    productsDAO productsDAO = new productsDAO(); // Khai báo productsDAO

    // Hàm loại bỏ dấu từ chuỗi tiếng Việt
    private String removeDiacritics(String str) {
        if (str == null) {
            return null;
        }
        String normalized = java.text.Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

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
                    out.print("\"productName\": \"" + (zone.getProductId() != null ? zone.getProductId().getName() : "N/A") + "\",");
                    out.print("\"history\": " + (zone.getHistory() != null ? zone.getHistory().toString() : "[]"));
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
            case "viewZoneHistory": {
                int zoneId = Integer.parseInt(request.getParameter("zone_id"));
                Zone zone = zoneDAO.getZoneById(zoneId);

                // Lấy tham số tìm kiếm, sắp xếp và phân trang
                String searchHistory = request.getParameter("searchHistory");
                if (searchHistory == null) {
                    searchHistory = "";
                }
                String filterId = request.getParameter("filterId"); // Thêm filterId
                String sortHistoryBy = request.getParameter("sortHistoryBy");
                if (sortHistoryBy == null) {
                    sortHistoryBy = "productName";
                }
                String sortHistoryOrderTemp = request.getParameter("sortHistoryOrder");
                if (sortHistoryOrderTemp == null || (!sortHistoryOrderTemp.equalsIgnoreCase("ASC") && !sortHistoryOrderTemp.equalsIgnoreCase("DESC"))) {
                    sortHistoryOrderTemp = "ASC";
                }
                final String sortHistoryOrder = sortHistoryOrderTemp;

                int historyPageIndex = 1;
                try {
                    historyPageIndex = Integer.parseInt(request.getParameter("historyPageIndex"));
                    if (historyPageIndex < 1) {
                        historyPageIndex = 1;
                    }
                } catch (NumberFormatException ignored) {
                    historyPageIndex = 1; // Giá trị mặc định nếu parse thất bại
                }

                // Lọc và sắp xếp historyList
                List<Map<String, String>> historyList = zone.getHistoryList();
                List<Map<String, String>> filteredHistoryList = new ArrayList<>();

                // Chuẩn hóa từ khóa tìm kiếm
                String searchHistoryNoDiacritics = removeDiacritics(searchHistory).toLowerCase();

                // Lọc theo productName (không phân biệt dấu)
                if (searchHistory.trim().isEmpty()) {
                    filteredHistoryList.addAll(historyList);
                } else {
                    for (Map<String, String> entry : historyList) {
                        String productNameNoDiacritics = removeDiacritics(entry.get("productName")).toLowerCase();
                        String historyId = entry.get("id");
                        // Sửa: So sánh filterId với historyId chính xác hơn
                        boolean matchesId = (filterId == null || filterId.trim().isEmpty() || historyId.equals(filterId));
                        if ((searchHistory.trim().isEmpty() || productNameNoDiacritics.contains(searchHistoryNoDiacritics)) && matchesId) {
                            filteredHistoryList.add(entry);
                        }
                    }
                }

                // Sắp xếp theo productName
                if (sortHistoryBy.equals("productName")) {
                    Collections.sort(filteredHistoryList, (o1, o2) -> {
                        int result = o1.get("productName").compareToIgnoreCase(o2.get("productName"));
                        return sortHistoryOrder.equalsIgnoreCase("ASC") ? result : -result;
                    });
                } else if (sortHistoryBy.equals("id")) { // Thêm sắp xếp theo id
                    Collections.sort(filteredHistoryList, (o1, o2) -> {
                        int result = o1.get("id").compareTo(o2.get("id"));
                        return sortHistoryOrder.equalsIgnoreCase("ASC") ? result : -result;
                    });
                }

                // Phân trang
                int pageSize = 5;
                int totalHistoryRecords = filteredHistoryList.size();
                int historyEndPage = (totalHistoryRecords % pageSize == 0) ? totalHistoryRecords / pageSize : (totalHistoryRecords / pageSize) + 1;
                if (historyPageIndex > historyEndPage) {
                    historyPageIndex = historyEndPage;
                }

                int startIndex = (historyPageIndex - 1) * pageSize;
                int endIndex = Math.min(startIndex + pageSize, totalHistoryRecords);

                // Đảm bảo startIndex không âm và endIndex hợp lệ
                if (startIndex < 0) {
                    startIndex = 0;
                }
                if (totalHistoryRecords == 0) {
                    startIndex = 0;
                    endIndex = 0;
                } else if (startIndex >= totalHistoryRecords) {
                    startIndex = totalHistoryRecords - 1;
                    endIndex = totalHistoryRecords;
                }

                List<Map<String, String>> paginatedHistoryList;
                if (filteredHistoryList.isEmpty()) {
                    paginatedHistoryList = new ArrayList<>();
                } else {
                    startIndex = (historyPageIndex - 1) * pageSize;
                    endIndex = Math.min(startIndex + pageSize, totalHistoryRecords);
                    if (startIndex < 0) {
                        startIndex = 0;
                    }
                    paginatedHistoryList = filteredHistoryList.subList(startIndex, endIndex);
                }

                request.setAttribute("zone", zone);
                request.setAttribute("historyList", paginatedHistoryList);
                request.setAttribute("searchHistory", searchHistory);
                request.setAttribute("filterId", filterId); // Truyền filterId vào JSP
                request.setAttribute("sortHistoryBy", sortHistoryBy);
                request.setAttribute("sortHistoryOrder", sortHistoryOrder);
                request.setAttribute("historyPageIndex", historyPageIndex);
                request.setAttribute("historyEndPage", historyEndPage);
                request.getRequestDispatcher("views/zone/zoneHistory.jsp").forward(request, response);
                break;
            }
            case "searchHistoryAjax": {
                int zoneId = Integer.parseInt(request.getParameter("zone_id"));
                Zone zone = zoneDAO.getZoneById(zoneId);

                // Lấy tham số tìm kiếm, sắp xếp và phân trang
                String searchHistory = request.getParameter("searchHistory");
                if (searchHistory == null) {
                    searchHistory = "";
                }
                String filterId = request.getParameter("filterId"); // Thêm filterId
                String sortHistoryBy = request.getParameter("sortHistoryBy");
                if (sortHistoryBy == null) {
                    sortHistoryBy = "productName";
                }
                String sortHistoryOrderTemp = request.getParameter("sortHistoryOrder");
                if (sortHistoryOrderTemp == null || (!sortHistoryOrderTemp.equalsIgnoreCase("ASC") && !sortHistoryOrderTemp.equalsIgnoreCase("DESC"))) {
                    sortHistoryOrderTemp = "ASC";
                }
                final String sortHistoryOrder = sortHistoryOrderTemp;

                int historyPageIndex = 1;
                try {
                    historyPageIndex = Integer.parseInt(request.getParameter("historyPageIndex"));
                    if (historyPageIndex < 1) {
                        historyPageIndex = 1;
                    }
                } catch (NumberFormatException ignored) {
                }

                // Lọc và sắp xếp historyList
                List<Map<String, String>> historyList = zone.getHistoryList();
                List<Map<String, String>> filteredHistoryList = new ArrayList<>();

                // Chuẩn hóa từ khóa tìm kiếm
                String searchHistoryNoDiacritics = removeDiacritics(searchHistory).toLowerCase();

                // Lọc theo productName (không phân biệt dấu)
                if (searchHistory.trim().isEmpty()) {
                    filteredHistoryList.addAll(historyList);
                } else {
                    for (Map<String, String> entry : historyList) {
                        String productNameNoDiacritics = removeDiacritics(entry.get("productName")).toLowerCase();
                        String historyId = entry.get("id");
                        // Sửa: So sánh filterId với historyId chính xác hơn
                        boolean matchesId = (filterId == null || filterId.trim().isEmpty() || historyId.equals(filterId));
                        if ((searchHistory.trim().isEmpty() || productNameNoDiacritics.contains(searchHistoryNoDiacritics)) && matchesId) {
                            filteredHistoryList.add(entry);
                        }
                    }
                }

                // Sắp xếp theo productName
                if (sortHistoryBy.equals("productName")) {
                    Collections.sort(filteredHistoryList, (o1, o2) -> {
                        int result = o1.get("productName").compareToIgnoreCase(o2.get("productName"));
                        return sortHistoryOrder.equalsIgnoreCase("ASC") ? result : -result;
                    });
                } else if (sortHistoryBy.equals("id")) {
                    Collections.sort(filteredHistoryList, (o1, o2) -> {
                        int result = o1.get("id").compareTo(o2.get("id"));
                        return sortHistoryOrder.equalsIgnoreCase("ASC") ? result : -result;
                    });
                }

                // Phân trang
                int pageSize = 5;
                int totalHistoryRecords = filteredHistoryList.size();
                int historyEndPage = (totalHistoryRecords % pageSize == 0) ? totalHistoryRecords / pageSize : (totalHistoryRecords / pageSize) + 1;
                if (historyPageIndex > historyEndPage) {
                    historyPageIndex = historyEndPage;
                }

                int startIndex = (historyPageIndex - 1) * pageSize;
                int endIndex = Math.min(startIndex + pageSize, totalHistoryRecords);

                // Đảm bảo startIndex không âm và endIndex hợp lệ
                if (startIndex < 0) {
                    startIndex = 0;
                }
                if (totalHistoryRecords == 0) {
                    startIndex = 0;
                    endIndex = 0;
                } else if (startIndex >= totalHistoryRecords) {
                    startIndex = totalHistoryRecords - 1;
                    endIndex = totalHistoryRecords;
                }

                List<Map<String, String>> paginatedHistoryList = filteredHistoryList.subList(startIndex, endIndex);

                // Trả về JSON
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print("{");
                out.print("\"historyList\": [");
                for (int i = 0; i < paginatedHistoryList.size(); i++) {
                    Map<String, String> entry = paginatedHistoryList.get(i);
                    out.print("{");
                    out.print("\"id\": \"" + entry.get("id") + "\",");
                    out.print("\"productName\": \"" + entry.get("productName") + "\",");
                    out.print("\"startDate\": \"" + entry.get("startDate") + "\",");
                    out.print("\"endDate\": \"" + (entry.get("endDate") != null ? entry.get("endDate") : "Now") + "\",");
                    out.print("\"updatedBy\": \"" + entry.get("updatedBy") + "\"");
                    out.print("}");
                    if (i < paginatedHistoryList.size() - 1) {
                        out.print(",");
                    }
                }
                out.print("],");
                out.print("\"historyPageIndex\": " + historyPageIndex + ",");
                out.print("\"historyEndPage\": " + historyEndPage);
                out.print("}");
                out.flush();
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

                String currentIndex = request.getParameter("index");
                if (currentIndex == null) {
                    currentIndex = "1";
                }

                String currentSortBy = request.getParameter("sortBy");
                if (currentSortBy == null) {
                    currentSortBy = "id";
                }

                String currentSortOrder = request.getParameter("sortOrder");
                if (currentSortOrder == null) {
                    currentSortOrder = "ASC";
                }

                request.setAttribute("zone", zoneForEdit);
                request.setAttribute("fullName", fullName);
                request.setAttribute("index", currentIndex);
                request.setAttribute("sortBy", currentSortBy);
                request.setAttribute("sortOrder", currentSortOrder);

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
            String productIdStr = request.getParameter("productId"); // Giả sử form gửi productId

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

            // Lấy Zone hiện tại để so sánh productId
            Zone currentZone = zoneDAO.getZoneById(zoneId);
            Integer newProductId = (productIdStr != null && !productIdStr.isEmpty()) ? Integer.parseInt(productIdStr) : null;
            Integer oldProductId = (currentZone.getProductId() != null) ? currentZone.getProductId().getProductId() : null;

            Zone zone = Zone.builder()
                    .id(zoneId)
                    .name(name)
                    .description(description)
                    .storeId(store) // Gán entity Stores với storeId từ session
                    .status(request.getParameter("status") != null ? request.getParameter("status") : "Active")
                    .build();

            // Nếu productId thay đổi, cập nhật lịch sử qua productsDAO
            if (newProductId != null && !newProductId.equals(oldProductId)) {
                List<Zone> zones = new ArrayList<>();
                zones.add(zone); // Chỉ cập nhật cho Zone hiện tại
                Products product = new Products();
                product.setProductId(newProductId);
                // Gọi editProduct để cập nhật productId và lịch sử
                boolean success = productsDAO.editProduct(product, zones, fullName); // Truyền fullName làm updatedBy
                if (!success) {
                    session.setAttribute("errorMessage", "Failed to update product history.");
                    response.sendRedirect("zones?service=editZone&zone_id=" + zoneId);
                    return;
                }
            }

            // Cập nhật thông tin Zone
            zoneDAO.updateZone(zone);
            session.setAttribute("Notification", "Zone details updated successfully.");

            // Lấy index hiện tại từ request (truyền từ form)
            String currentIndex = request.getParameter("index");
            if (currentIndex == null || currentIndex.isEmpty()) {
                currentIndex = "1"; // Mặc định về trang 1 nếu không có index
            }

            String sortBy = request.getParameter("sortBy");
            if (sortBy == null) {
                sortBy = "id";
            }
            String sortOrder = request.getParameter("sortOrder");
            if (sortOrder == null) {
                sortOrder = "ASC";
            }
            response.sendRedirect("zones?service=zones&sortBy=" + sortBy + "&sortOrder=" + sortOrder + "&index=" + currentIndex);
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
