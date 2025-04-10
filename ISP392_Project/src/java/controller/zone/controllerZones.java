package controller.zone;

import dal.zoneDAO;
import entity.Stores;
import entity.Zone;
import dal.productsDAO;
import java.io.IOException;
import java.io.PrintWriter;
import entity.Products;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    productsDAO productsDAO = new productsDAO();

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
        String storeIDStr = (String) session.getAttribute("storeID");
        int storeID = Integer.parseInt(storeIDStr);
        String fullName = (String) session.getAttribute("fullName");
        if (fullName == null) {
            response.sendRedirect("login");
            return;
        }
        String role = (String) session.getAttribute("role");
        String storeIdStr = (String) session.getAttribute("storeID");
        System.out.println("Role: " + role + ", Store ID: " + storeIdStr);

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

        // Lấy giá trị showInactive từ request hoặc session
        String showInactiveParam = request.getParameter("showInactive");
        Boolean showInactive = (Boolean) session.getAttribute("showInactive");
        if (showInactiveParam != null) {
            showInactive = "true".equalsIgnoreCase(showInactiveParam);
            session.setAttribute("showInactive", showInactive); // Lưu vào session
        } else if (showInactive == null) {
            showInactive = true; // Giá trị mặc định nếu chưa có trong session
            session.setAttribute("showInactive", showInactive);
        }

        // Lấy pageSize từ request, mặc định là 5
        int pageSize = 5;
        try {
            pageSize = Integer.parseInt(request.getParameter("pageSize"));
            if (pageSize <= 0) {
                pageSize = 5; // Đảm bảo pageSize không âm
            }
        } catch (NumberFormatException ignored) {
            pageSize = 5; // Mặc định nếu không parse được
        }

        switch (service) {
            case "zones": {
                String keyword = request.getParameter("searchZone");
                if (keyword == null) {
                    keyword = "";
                }

                int index = 1;
                try {
                    String indexParam = request.getParameter("index");
                    if (indexParam != null && !indexParam.trim().isEmpty()) {
                        index = Integer.parseInt(indexParam);
                        if (index < 1) {
                            index = 1;
                        }
                    }
                } catch (NumberFormatException ignored) {
                    index = 1;
                }

                Integer storeId = storeIdStr != null ? Integer.parseInt(storeIdStr) : null;

                int total = zoneDAO.countZones(keyword, showInactive, storeId);
                int endPage = (total % pageSize == 0) ? total / pageSize : (total / pageSize) + 1;

                if (index > endPage) {
                    index = endPage;
                }

                List<Zone> list = zoneDAO.searchZones(keyword, index, pageSize, sortBy, sortOrder, showInactive, storeId);
                System.out.println("Initial zones list size for store " + storeId + ": " + list.size());

                String notification = (String) session.getAttribute("Notification");
                if (notification != null) {
                    request.setAttribute("Notification", notification);
                    session.removeAttribute("Notification");
                }

                request.setAttribute("list", list);
                request.setAttribute("endPage", endPage);
                request.setAttribute("index", index);
                request.setAttribute("searchZone", keyword);
                request.setAttribute("sortBy", sortBy);
                request.setAttribute("sortOrder", sortOrder);
                request.setAttribute("pageSize", pageSize);
                request.setAttribute("totalRecords", total);
                request.setAttribute("showInactive", showInactive);

                request.getRequestDispatcher("views/zone/zones.jsp").forward(request, response);
                break;
            }
            case "searchZonesAjax": {
                System.out.println("Processing searchZonesAjax request...");
                String keyword = request.getParameter("keyword");
                if (keyword == null) {
                    keyword = "";
                }
                int index = 1;
                try {
                    String indexParam = request.getParameter("index");
                    if (indexParam != null && !indexParam.trim().isEmpty()) {
                        index = Integer.parseInt(indexParam);
                        if (index < 1) {
                            index = 1;
                        }
                    }
                } catch (NumberFormatException ignored) {
                    index = 1;
                }

                // Sử dụng showInactive từ session nếu không có trong request
                showInactiveParam = request.getParameter("showInactive");
                showInactive = showInactiveParam != null ? "true".equalsIgnoreCase(showInactiveParam) : (Boolean) session.getAttribute("showInactive");
                session.setAttribute("showInactive", showInactive); // Cập nhật session
                Integer storeId = storeIdStr != null ? Integer.parseInt(storeIdStr) : null;

                int total = zoneDAO.countZones(keyword, showInactive, storeID);
                int endPage = (total % pageSize == 0) ? total / pageSize : (total / pageSize) + 1;
                List<Zone> zones = zoneDAO.searchZones(keyword, index, pageSize, sortBy, sortOrder, showInactive, storeId);
                System.out.println("AJAX zones list size for store " + storeId + ": " + zones.size());

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print("{");
                out.print("\"zones\": [");
                for (int i = 0; i < zones.size(); i++) {
                    Zone zone = zones.get(i);
                    out.print("{");
                    out.print("\"id\": " + zone.getId() + ",");
                    out.print("\"name\": \"" + zone.getName().replace("\"", "\\\"") + "\",");
                    out.print("\"description\": \"" + zone.getDescription().replace("\"", "\\\"") + "\",");
                    out.print("\"createdBy\": \"" + zone.getCreatedBy().replace("\"", "\\\"") + "\",");
                    out.print("\"status\": \"" + zone.getStatus() + "\",");
                    out.print("\"productName\": \"" + (zone.getProductId() != null ? zone.getProductId().getName().replace("\"", "\\\"") : "N/A") + "\",");
                    out.print("\"history\": " + (zone.getHistory() != null ? zone.getHistory().toString() : "[]"));
                    out.print("}");
                    if (i < zones.size() - 1) {
                        out.print(",");
                    }
                }
                out.print("],");
                out.print("\"endPage\": " + endPage + ",");
                out.print("\"index\": " + index + ",");
                out.print("\"totalRecords\": " + total);
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
                    String historyPageIndexParam = request.getParameter("historyPageIndex");
                    if (historyPageIndexParam != null && !historyPageIndexParam.trim().isEmpty()) {
                        historyPageIndex = Integer.parseInt(historyPageIndexParam);
                        if (historyPageIndex < 1) {
                            historyPageIndex = 1;
                        }
                    }
                } catch (NumberFormatException ignored) {
                    historyPageIndex = 1;
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
                    String historyPageIndexParam = request.getParameter("historyPageIndex");
                    if (historyPageIndexParam != null && !historyPageIndexParam.trim().isEmpty()) {
                        historyPageIndex = Integer.parseInt(historyPageIndexParam);
                        if (historyPageIndex < 1) {
                            historyPageIndex = 1;
                        }
                    }
                } catch (NumberFormatException ignored) {
                    historyPageIndex = 1;
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
                String zoneIdParam = request.getParameter("zone_id");
                if (zoneIdParam == null || zoneIdParam.trim().isEmpty()) {
                    session.setAttribute("Notification", "Invalid zone ID.");
                    response.sendRedirect("zones?service=zones");
                    return;
                }
                
                try {
                    int zoneId = Integer.parseInt(zoneIdParam);
                    if (zoneId <= 0) {
                        session.setAttribute("Notification", "Invalid zone ID.");
                        response.sendRedirect("zones?service=zones");
                        return;
                    }
                    
                    Zone zoneForEdit = zoneDAO.getZoneById(zoneId);
                    if (zoneForEdit == null) {
                        session.setAttribute("Notification", "Zone not found.");
                        response.sendRedirect("zones?service=zones");
                        return;
                    }

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
                    request.setAttribute("pageSize", pageSize);

                    request.getRequestDispatcher("views/zone/editZone.jsp").forward(request, response);
                } catch (NumberFormatException e) {
                    session.setAttribute("Notification", "Invalid zone ID format.");
                    response.sendRedirect("zones?service=zones");
                }
                break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");
        HttpSession session = request.getSession();

        String role = (String) session.getAttribute("role");

        String fullName = (String) session.getAttribute("fullName");
        if (fullName == null) {
            response.sendRedirect("login");
            return;
        }

        // Lấy pageSize từ request, mặc định là 5
        int pageSize = 5;
        try {
            pageSize = Integer.parseInt(request.getParameter("pageSize"));
            if (pageSize <= 0) {
                pageSize = 5;
            }
        } catch (NumberFormatException ignored) {
        }

        if ("addZone".equals(service)) {
            // Staff không được phép thực hiện bất kỳ POST nào
            if ("staff".equals(role)) {
                session.setAttribute("Notification", "You do not have permission to perform this action.");
                response.sendRedirect("zones?service=zones");
                return;
            }

            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String status = request.getParameter("status");
            
            // Set default status to Active if not provided
            if (status == null || status.trim().isEmpty()) {
                status = "Active";
            }

            // Validate input
            String nameError = validateZoneName(name);
            String descriptionError = validateDescription(description);
            String statusError = validateStatus(status);

            if (nameError != null || descriptionError != null || statusError != null) {
                request.setAttribute("nameError", nameError);
                request.setAttribute("descriptionError", descriptionError);
                request.setAttribute("statusError", statusError);
                request.setAttribute("name", name);
                request.setAttribute("description", description);
                request.setAttribute("status", status);
                request.getRequestDispatcher("views/zone/addZone.jsp").forward(request, response);
                return;
            }

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
                response.sendRedirect("zones?service=addZone");
                return;
            }

            // Kiểm tra trùng tên với storeId từ session
            if (zoneDAO.checkNameExists(name, -1, storeId)) {
                request.setAttribute("nameError", "Zone name already exists for this store.");
                request.setAttribute("name", name);
                request.setAttribute("description", description);
                request.setAttribute("status", status);
                request.getRequestDispatcher("views/zone/addZone.jsp").forward(request, response);
                return;
            }

            Stores store = new Stores();
            store.setId(storeId);

            Zone zone = Zone.builder()
                    .name(name)
                    .description(description)
                    .createdBy(fullName)
                    .storeId(store)
                    .status(status)
                    .build();

            try {
                zoneDAO.insertZone(zone);
                System.out.println("Zone added successfully: " + name);
                
                // Lấy showInactive từ session
                Boolean showInactive = (Boolean) session.getAttribute("showInactive");
                if (showInactive == null) {
                    showInactive = true;
                }

                session.setAttribute("Notification", "Zone added successfully.");

                String sortBy = request.getParameter("sortBy");
                if (sortBy == null) {
                    sortBy = "id";
                }
                String sortOrder = request.getParameter("sortOrder");
                if (sortOrder == null) {
                    sortOrder = "ASC";
                }
                
                // Chuyển hướng về trang danh sách zone
                String redirectUrl = request.getContextPath() + "/zones?service=zones&sortBy=" + sortBy + "&sortOrder=" + sortOrder + "&index=1&pageSize=" + pageSize + "&showInactive=" + showInactive;
                System.out.println("Redirecting to: " + redirectUrl);
                response.sendRedirect(redirectUrl);
            } catch (Exception e) {
                System.err.println("Error adding zone: " + e.getMessage());
                e.printStackTrace();
                session.setAttribute("errorMessage", "Error adding zone. Please try again.");
                response.sendRedirect(request.getContextPath() + "/zones?service=addZone");
            }
            return;
        }

        if ("editZone".equals(service)) {
            // Staff không được phép thực hiện bất kỳ POST nào
            if ("staff".equals(role)) {
                session.setAttribute("Notification", "You do not have permission to perform this action.");
                response.sendRedirect("zones?service=zones");
                return;
            }

            String zoneIdParam = request.getParameter("zone_id");
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String status = request.getParameter("status");

            // Validate input
            String zoneIdError = validateZoneId(zoneIdParam);
            String nameError = validateZoneName(name);
            String descriptionError = validateDescription(description);
            String statusError = validateStatus(status);

            if (zoneIdError != null || nameError != null || descriptionError != null || statusError != null) {
                request.setAttribute("zoneIdError", zoneIdError);
                request.setAttribute("nameError", nameError);
                request.setAttribute("descriptionError", descriptionError);
                request.setAttribute("statusError", statusError);
                Zone zoneForEdit = zoneDAO.getZoneById(Integer.parseInt(zoneIdParam));
                request.setAttribute("zone", zoneForEdit);
                request.setAttribute("name", name);
                request.setAttribute("description", description);
                request.setAttribute("status", status);
                request.setAttribute("fullName", fullName);
                request.getRequestDispatcher("views/zone/editZone.jsp").forward(request, response);
                return;
            }

            int zoneId = Integer.parseInt(zoneIdParam);

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

            // Kiểm tra trùng tên
            if (zoneDAO.checkNameExists(name, zoneId, storeId)) {
                request.setAttribute("nameError", "Zone name already exists for this store.");
                Zone zoneForEdit = zoneDAO.getZoneById(zoneId);
                request.setAttribute("zone", zoneForEdit);
                request.setAttribute("name", name);
                request.setAttribute("description", description);
                request.setAttribute("status", status);
                request.setAttribute("fullName", fullName);
                request.getRequestDispatcher("views/zone/editZone.jsp").forward(request, response);
                return;
            }

            // Lấy Zone hiện tại
            Zone currentZone = zoneDAO.getZoneById(zoneId);
            if (currentZone == null) {
                session.setAttribute("errorMessage", "Zone not found.");
                response.sendRedirect("zones?service=zones");
                return;
            }

            Stores store = new Stores();
            store.setId(storeId);

            // Tạo đối tượng Zone mới
            Zone zone = Zone.builder()
                    .id(zoneId)
                    .name(name)
                    .description(description)
                    .storeId(store)
                    .status(status)
                    .createdAt(currentZone.getCreatedAt())
                    .createdBy(currentZone.getCreatedBy())
                    .history(currentZone.getHistory())
                    .productId(currentZone.getProductId())
                    .build();

            // Nếu trạng thái là "Inactive", set productId thành null
            if ("Inactive".equals(status)) {
                zone.setProductId(null);
            }

            // Cập nhật Zone
            zoneDAO.updateZone(zone);
            session.setAttribute("Notification", "Zone details updated successfully.");

            String currentIndex = request.getParameter("index");
            if (currentIndex == null || currentIndex.isEmpty()) {
                currentIndex = "1";
            }

            String sortBy = request.getParameter("sortBy");
            if (sortBy == null) {
                sortBy = "id";
            }
            String sortOrder = request.getParameter("sortOrder");
            if (sortOrder == null) {
                sortOrder = "ASC";
            }
            response.sendRedirect("zones?service=zones&sortBy=" + sortBy + "&sortOrder=" + sortOrder + "&index=" + currentIndex + "&pageSize=" + pageSize);
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

    private String validateZoneName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Zone name cannot be empty";
        }
        if (name.length() <= 0) {
            return "Zone name cannot be blank";
        }
        if (name.length() > 50) {
            return "Zone name cannot exceed 50 characters";
        }
        // Kiểm tra ký tự đặc biệt và SQL injection
        if (!name.matches("^[a-zA-Z0-9\\s\\-\\_]+$")) {
            return "Zone name can only contain letters, numbers, spaces, hyphens, and underscores";
        }
        // Kiểm tra từ khóa SQL nguy hiểm
        String[] sqlKeywords = {"SELECT", "INSERT", "UPDATE", "DELETE", "DROP", "UNION", "ALTER", "CREATE", "TRUNCATE"};
        for (String keyword : sqlKeywords) {
            if (name.toUpperCase().contains(keyword)) {
                return "Zone name contains invalid characters";
            }
        }
        return null;
    }

    private String validateDescription(String description) {
        if (description == null) {
            return null; // Cho phép mô tả trống
        }
        if (description.length() > 500) {
            return "Description cannot exceed 500 characters";
        }
        // Kiểm tra ký tự đặc biệt nguy hiểm
        if (description.matches(".*[<>'\"].*")) {
            return "Description cannot contain dangerous characters";
        }
        // Kiểm tra từ khóa SQL nguy hiểm
        String[] sqlKeywords = {"SELECT", "INSERT", "UPDATE", "DELETE", "DROP", "UNION", "ALTER", "CREATE", "TRUNCATE"};
        for (String keyword : sqlKeywords) {
            if (description.toUpperCase().contains(keyword)) {
                return "Description contains invalid characters";
            }
        }
        return null;
    }

    private String validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return "Status cannot be empty";
        }
        // Chỉ cho phép các giá trị cụ thể
        if (!status.equals("Active") && !status.equals("Inactive")) {
            return "Invalid status value";
        }
        // Kiểm tra từ khóa SQL nguy hiểm
        String[] sqlKeywords = {"SELECT", "INSERT", "UPDATE", "DELETE", "DROP", "UNION", "ALTER", "CREATE", "TRUNCATE"};
        for (String keyword : sqlKeywords) {
            if (status.toUpperCase().contains(keyword)) {
                return "Status contains invalid characters";
            }
        }
        return null;
    }

    private String validateZoneId(String zoneId) {
        if (zoneId == null || zoneId.trim().isEmpty()) {
            return "Zone ID cannot be empty";
        }
        try {
            int id = Integer.parseInt(zoneId);
            if (id <= 0) {
                return "Invalid zone ID format";
            }
        } catch (NumberFormatException e) {
            return "Zone ID must be a number";
        }
        return null;
    }

    private String validateStoreId(String storeId) {
        if (storeId == null || storeId.trim().isEmpty()) {
            return "Store ID cannot be empty";
        }
        try {
            int id = Integer.parseInt(storeId);
            if (id <= 0) {
                return "Invalid store ID format";
            }
        } catch (NumberFormatException e) {
            return "ID cửa hàng phải là số";
        }
        return null;
    }

    // Thêm phương thức mới để escape input
    private String escapeInput(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("'", "''")
                    .replace("\\", "\\\\")
                    .replace("%", "\\%")
                    .replace("_", "\\_");
    }
}
