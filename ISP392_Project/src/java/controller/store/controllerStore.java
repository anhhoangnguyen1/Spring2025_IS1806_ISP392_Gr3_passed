package controller.store;

import dal.storeDAO;
import dal.userDAO;
import entity.Stores;
import entity.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "controllerStore", urlPatterns = {"/Stores"})
@MultipartConfig
public class controllerStore extends HttpServlet {

    storeDAO storeDAO = new storeDAO();
    userDAO userDAO = new userDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String service = request.getParameter("service");
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

//        // Kiểm tra phân quyền
//        if ("admin".equals(role)) {
//            response.sendRedirect(request.getContextPath() + "/Users"); // Admin không được truy cập vào store
//            return;
//        }
        if (service == null) {
            if (!"admin".equals(role)) {
                service = "storeInfo";
            } else {
                service = "storeList";
            }
        }

        switch (service) {
            case "storeInfo": {
                // Lấy storeID từ session
                String storeID = (String) session.getAttribute("storeID");
                // Log storeID để kiểm tra giá trị
                System.out.println("StoreID from session: " + storeID);

                if (storeID != null) {
                    try {
                        // Lấy thông tin cửa hàng từ storeID
                        Stores store = storeDAO.getStoreById(Integer.parseInt(storeID));

                        // Log thông tin cửa hàng nếu tìm thấy
                        if (store != null) {
                            System.out.println("Store found: " + store);

                            // Lấy thông tin owner của store
                            Users owner = userDAO.getOwnerByStoreId(store.getId());
                            System.out.println("Owner for store ID " + store.getId() + ": " + (owner != null ? owner.getName() : "No owner"));

                            // Đưa thông tin cửa hàng và owner vào request
                            request.setAttribute("store", store);
                            request.setAttribute("owner", owner);  // Truyền tên owner vào request
                            // Đưa role người dùng vào request để phân quyền hiển thị
                            request.setAttribute("userRole", role);
                        } else {
                            System.out.println("No store found with ID: " + storeID);
                        }
                    } catch (NumberFormatException e) {
                        // Log lỗi nếu storeID không hợp lệ
                        System.out.println("Invalid storeID format: " + storeID);
                    }
                } else {
                    // Log nếu không có storeID trong session
                    System.out.println("No storeID found in session.");
                }

                // Forward request đến trang JSP
                request.getRequestDispatcher("views/store/storeInfo.jsp").forward(request, response);
                break;
            }
            case "editStore": {
                // owner và admin mới có thể chỉnh sửa thông tin store
                if ("staff".equals(role)) {
                    session.setAttribute("errorMessage", "You do not have permission to edit store information.");
                    response.sendRedirect("Stores?service=storeInfo");
                    return;
                }
                if (request.getParameter("store_id") != null) {
                    try {
                        int storeId = Integer.parseInt(request.getParameter("store_id"));
                        Stores storeForEdit = storeDAO.getStoreById(storeId);

                        // Kiểm tra quyền chỉnh sửa của owner
                        if ("owner".equals(role)) {
                            // Lấy storeID từ session của owner
                            String ownerStoreId = (String) session.getAttribute("storeID");
                            if (ownerStoreId == null || !String.valueOf(storeId).equals(ownerStoreId)) {
                                session.setAttribute("errorMessage", "Bạn chỉ được chỉnh sửa cửa hàng của riêng mình.");
                                response.sendRedirect("Stores?service=storeInfo");
                                return;
                            }
                        }

                        // Log để kiểm tra
                        System.out.println("Editing Store - ID: " + storeId);
                        System.out.println("Store Details: " + storeForEdit);

                        request.setAttribute("store", storeForEdit);

                        // Nếu là admin, cung cấp danh sách các owner
                        if ("admin".equals(role)) {
                            List<Users> ownerList = userDAO.getOwnersWithoutStore();
                            request.setAttribute("ownerList", ownerList);
                        }

                        request.getRequestDispatcher("views/store/editStore.jsp").forward(request, response);
                    } catch (NumberFormatException e) {
                        // Xử lý lỗi định dạng ID
                        System.out.println("Lỗi: ID cửa hàng không hợp lệ");
                        response.sendRedirect("Stores?service=storeInfo");
                    }
                } else if ("owner".equals(role)) {
                    response.sendRedirect("Stores?service=storeInfo");
                } else {
                    response.sendRedirect("Stores?service=storeList");
                }

                break;
            }
            case "createStore": {
                // Chỉ admin mới có thể tạo store
                if (!"admin".equals(role)) {
                    session.setAttribute("errorMessage", "You do not have permission to create a store.");
                    response.sendRedirect("Home");
                    return;
                }

                request.getRequestDispatcher("views/store/createStore.jsp").forward(request, response);
                break;
            }
            case "searchStore": {
                // Chỉ admin có thể truy cập chức năng tìm kiếm
                if (!"admin".equals(role)) {
                    session.setAttribute("errorMessage", "You do not have permission to search stores.");
                    response.sendRedirect("Home");
                    return;
                }
                // Lấy từ khóa tìm kiếm từ request
                String searchTerm = request.getParameter("searchStore");
                // Gọi phương thức tìm kiếm từ storeDAO (giả sử đã được cài đặt)
                List<Stores> storeList = storeDAO.searchStores(searchTerm);
                request.setAttribute("storeList", storeList);
                // Lấy thông tin owner cho từng cửa hàng tìm được
                List<Map<String, Object>> ownerDetails = new ArrayList<>();
                for (Stores store : storeList) {
                    Users owner = userDAO.getOwnerByStoreId(store.getId());
                    Map<String, Object> ownerData = new HashMap<>();
                    if (owner != null) {
                        ownerData.put("storeId", store.getId());
                        ownerData.put("ownerName", owner.getName());
                    } else {
                        ownerData.put("storeId", store.getId());
                        ownerData.put("ownerName", "No owner");
                    }
                    ownerDetails.add(ownerData);
                }
                request.setAttribute("ownerDetails", ownerDetails);
                request.getRequestDispatcher("views/store/listStores.jsp").forward(request, response);
                break;
            }
            case "filterStoreByDate": {
                if (!"admin".equals(role)) {
                    session.setAttribute("errorMessage", "You do not have permission to filter stores.");
                    response.sendRedirect("Home");
                    return;
                }
                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");
                // Gọi hàm lọc theo ngày tạo
                List<Stores> storeList = storeDAO.filterStoresByDate(fromDate, toDate);
                request.setAttribute("storeList", storeList);
                // Xử lý owner cho từng store
                List<Map<String, Object>> ownerDetails = new ArrayList<>();
                for (Stores store : storeList) {
                    Users owner = userDAO.getOwnerByStoreId(store.getId());
                    Map<String, Object> ownerData = new HashMap<>();
                    if (owner != null) {
                        ownerData.put("storeId", store.getId());
                        ownerData.put("ownerName", owner.getName());
                    } else {
                        ownerData.put("storeId", store.getId());
                        ownerData.put("ownerName", "No owner");
                    }
                    ownerDetails.add(ownerData);
                }
                request.setAttribute("ownerDetails", ownerDetails);
                request.getRequestDispatcher("views/store/listStores.jsp").forward(request, response);
                break;
            }
            case "toggleBan": {
                String storeIdParam = request.getParameter("store_id");
                if (storeIdParam != null) {
                    try {
                        int storeId = Integer.parseInt(storeIdParam);
                        Stores store = storeDAO.getStoreById(storeId);
                        if (store != null) {
                            String newStatus;
                            // Nếu cửa hàng đang Active thì chuyển thành Inactive, ngược lại chuyển thành Active
                            if ("Active".equalsIgnoreCase(store.getStatus())) {
                                newStatus = "Inactive";
                            } else if ("Inactive".equalsIgnoreCase(store.getStatus())) {
                                newStatus = "Active";
                            } else {
                                // Nếu trạng thái không xác định, có thể đặt mặc định là Active
                                newStatus = "Active";
                            }
                            // Gọi hàm cập nhật trạng thái
                            boolean updateSuccess = storeDAO.updateStoreStatus(storeId, newStatus);
                            if (updateSuccess) {
                                session.setAttribute("successMessage", "Store status updated successfully.");
                            } else {
                                session.setAttribute("errorMessage", "Failed to update store status.");
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid store ID: " + storeIdParam);
                    }
                }
                response.sendRedirect("Stores?service=storeList");
                break;
            }
            case "filterStoreByStatus": {
                String status = request.getParameter("status");

                // Gọi hàm lọc cửa hàng theo status
                List<Stores> storeList = storeDAO.filterStoresByStatus(status);
                request.setAttribute("storeList", storeList);

                // Xử lý owner cho từng store
                List<Map<String, Object>> ownerDetails = new ArrayList<>();
                for (Stores store : storeList) {
                    Users owner = userDAO.getOwnerByStoreId(store.getId());
                    Map<String, Object> ownerData = new HashMap<>();
                    if (owner != null) {
                        ownerData.put("storeId", store.getId());
                        ownerData.put("ownerName", owner.getName());
                    } else {
                        ownerData.put("storeId", store.getId());
                        ownerData.put("ownerName", "No owner");
                    }
                    ownerDetails.add(ownerData);
                }
                request.setAttribute("ownerDetails", ownerDetails);
                request.getRequestDispatcher("views/store/listStores.jsp").forward(request, response);
                break;
            }
            case "storeList": {
                // Chỉ admin mới có thể xem list store
                if (!"admin".equals(role)) {
                    session.setAttribute("errorMessage", "You do not have permission to create a store.");
                    response.sendRedirect("Home");
                    return;
                }
                if ("storeList".equals(service)) {
                    // Lấy danh sách tất cả store
                    List<Stores> storeList = storeDAO.getAllStores();
                    request.setAttribute("storeList", storeList);

                    // Lấy thông tin owner (chỉ tên owner) cho từng store
                    List<Map<String, Object>> ownerDetails = new ArrayList<>();
                    for (Stores store : storeList) {
                        Users owner = userDAO.getOwnerByStoreId(store.getId());
                        Map<String, Object> ownerData = new HashMap<>();
                        if (owner != null) {
                            ownerData.put("storeId", store.getId());
                            ownerData.put("ownerName", owner.getName());
                        } else {
                            ownerData.put("storeId", store.getId());
                            ownerData.put("ownerName", "No owner");
                        }
                        ownerDetails.add(ownerData);
                    }

                    request.setAttribute("ownerDetails", ownerDetails);  // Truyền ownerDetails vào request

                    request.getRequestDispatcher("views/store/listStores.jsp").forward(request, response);
                    break;
                }

            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String service = request.getParameter("service");
        String role = (String) session.getAttribute("role");
        String fullname = (String) session.getAttribute("fullName");

        System.out.println("Service: " + service);  // Để kiểm tra giá trị của service

        // Kiểm tra phân quyền - Staff không thể thực hiện các thao tác POST
        if ("staff".equals(role)) {
            session.setAttribute("errorMessage", "You do not have permission to perform this action.");
            response.sendRedirect("Stores?service=storeInfo");
            return;
        }

        if ("editStore".equals(service)) {
            int storeId = Integer.parseInt(request.getParameter("store_id"));
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String ownerParam = request.getParameter("owner");

            System.out.println("Name: " + name);
            System.out.println("Address: " + address);
            System.out.println("Phone: " + phone);
            System.out.println("Email: " + email);
            System.out.println("Owner ID: " + ownerParam);

            // Validate store name, phone, and email
            if (storeDAO.isStoreNameExists(name, storeId)) {
                request.setAttribute("nameError", "Store name already exists.");
                request.setAttribute("store", getStoreFromRequest(request));
                request.getRequestDispatcher("views/store/editStore.jsp").forward(request, response);
                return;
            }

            if (storeDAO.isPhoneExists(phone, storeId)) {
                request.setAttribute("phoneError", "Phone number already exists.");
                request.setAttribute("store", getStoreFromRequest(request));
                request.getRequestDispatcher("views/store/editStore.jsp").forward(request, response);
                return;
            }

            if (phone != null && !phone.isEmpty() && !phone.matches("^0\\d{9}$")) {
                request.setAttribute("phoneError", "Invalid phone number format.");
                request.setAttribute("store", getStoreFromRequest(request));
                request.getRequestDispatcher("views/store/editStore.jsp").forward(request, response);
                return;
            }

            if (storeDAO.isEmailExists(email, storeId)) {
                request.setAttribute("emailError", "Email already exists.");
                request.setAttribute("store", getStoreFromRequest(request));
                request.getRequestDispatcher("views/store/editStore.jsp").forward(request, response);
                return;
            }

            // Update store details
            Stores store = new Stores();
            store.setId(storeId);
            store.setName(name);
            store.setAddress(address);
            store.setPhone(phone);
            store.setEmail(email);
            storeDAO.editStore(store);

            // Thêm log chi tiết để debug
            System.out.println("Đang cập nhật cửa hàng:");
            System.out.println("ID: " + storeId);
            System.out.println("Tên: " + name);
            System.out.println("Địa chỉ: " + address);
            System.out.println("Điện thoại: " + phone);
            System.out.println("Email: " + email);

            // Nếu là admin và có chọn owner thì cập nhật owner của cửa hàng
            if ("admin".equals(role) && ownerParam != null && !ownerParam.trim().isEmpty()) {
                int ownerId = Integer.parseInt(ownerParam);

                Users currentOwner = userDAO.getOwnerByStoreId(storeId);
                if (currentOwner != null && currentOwner.getId() != ownerId) {
                    // Nếu owner hiện tại khác với owner được chọn, hủy liên kết owner cũ
                    currentOwner.setStoreId(null);
                    userDAO.updateUser(currentOwner);
                }

                Users selectedOwner = userDAO.getUserById(ownerId);
                if (selectedOwner != null) {
                    // Gán cửa hàng cho owner được chọn
                    selectedOwner.setStoreId(store);
                    userDAO.updateUser(selectedOwner);
                }
            }

            session.setAttribute("successMessage", "Store details updated successfully.");

            if ("admin".equals(role)) {
                response.sendRedirect("Stores?service=storeList");
            } else {
                response.sendRedirect("Stores?service=storeInfo");
            }
        }
        if ("createStore".equals(service)) {
            if (!"admin".equals(role)) {
                session.setAttribute("errorMessage", "You do not have permission to perform this action.");
                response.sendRedirect("Stores?service=storeInfo");
                return;
            }
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");

            // Validation
            if (storeDAO.isStoreNameExists(name)) {
                request.setAttribute("nameError", "Store name already exists.");
                request.setAttribute("store", getStoreFromRequest(request));
                request.getRequestDispatcher("views/store/createStore.jsp").forward(request, response);
                return;
            }

            if (phone != null && !phone.isEmpty() && !phone.matches("^0\\d{9}$")) {
                request.setAttribute("phoneError", "Invalid phone number format.");
                request.setAttribute("store", getStoreFromRequest(request));
                request.getRequestDispatcher("views/store/createStore.jsp").forward(request, response);
                return;
            }

            if (phone != null && !phone.isEmpty() && storeDAO.isPhoneExists(phone)) {
                request.setAttribute("phoneError", "Phone number already exists.");
                request.setAttribute("store", getStoreFromRequest(request));
                request.getRequestDispatcher("views/store/createStore.jsp").forward(request, response);
                return;
            }

            if (email != null && !email.isEmpty() && storeDAO.isEmailExists(email)) {
                request.setAttribute("emailError", "Email already exists.");
                request.setAttribute("store", getStoreFromRequest(request));
                request.getRequestDispatcher("views/store/createStore.jsp").forward(request, response);
                return;
            }
            String creatorName = fullname; // Người tạo cửa hàng là username hiện tại

            // Tạo đối tượng store mới
            Stores store = new Stores();
            store.setName(name);
            store.setAddress(address);
            store.setPhone(phone);
            store.setEmail(email);
            store.setStatus("Active");
            store.setCreatedBy(creatorName); // Gán tên người tạo vào trường create_by

            // Thêm store vào database và lấy ID được tạo
            int newStoreId = storeDAO.createStore(store);

            if (newStoreId > 0) {
                session.setAttribute("successMessage", "Store created successfully.");
                response.sendRedirect("Stores?service=storeList");
            } else {
                session.setAttribute("errorMessage", "Error creating store.");
                response.sendRedirect("Stores?service=createStore");
            }
        }
    }

    private Stores getStoreFromRequest(HttpServletRequest request) {
        Stores store = new Stores();
        try {
            String storeIdParam = request.getParameter("store_id");
            if (storeIdParam != null && !storeIdParam.isEmpty()) {
                store.setId(Integer.parseInt(storeIdParam));
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid store_id format: " + request.getParameter("store_id"));
        }
        store.setName(request.getParameter("name"));
        store.setAddress(request.getParameter("address"));
        store.setEmail(request.getParameter("email"));
        store.setPhone(request.getParameter("phone"));
        store.setStatus(request.getParameter("status"));
        return store;
    }

    @Override
    public String getServletInfo() {
        return "Controller for managing store information";
    }
}
