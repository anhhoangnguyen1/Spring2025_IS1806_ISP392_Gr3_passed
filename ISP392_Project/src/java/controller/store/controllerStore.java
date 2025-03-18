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

        // Kiểm tra phân quyền
        if ("admin".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/dashboard"); // Admin không được truy cập vào store
            return;
        }

        if (service == null) {
            service = "storeInfo";
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
                            // Đưa role người dùng vào request để phân quyền hiển thị
                            request.setAttribute("userRole", role);
                        } else {
                            System.out.println("No store found with ID: " + storeID);
                        }
                        // Đưa thông tin cửa hàng vào request nếu tìm thấy
                        request.setAttribute("store", store);
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
                // Chỉ owner mới có thể chỉnh sửa thông tin store
                if (!"owner".equals(role)) {
                    session.setAttribute("errorMessage", "You do not have permission to edit store information.");
                    response.sendRedirect("Stores?service=storeInfo");
                    return;
                }
                if (request.getParameter("store_id") != null) {
                    int storeId = Integer.parseInt(request.getParameter("store_id"));
                    Stores storeForEdit = storeDAO.getStoreById(storeId);
                    request.setAttribute("store", storeForEdit);
                    request.getRequestDispatcher("views/store/editStore.jsp").forward(request, response);
                } else {
                    response.sendRedirect("Stores?service=storeInfo");
                }
                break;
            }
            case "createStore": {
                // Chỉ owner mới có thể tạo store
                if (!"owner".equals(role)) {
                    session.setAttribute("errorMessage", "You do not have permission to create a store.");
                    response.sendRedirect("Home");
                    return;
                }
                // Kiểm tra xem user đã có store chưa
                Users user = userDAO.getUserByUsername(username);
                if (user != null && user.getStoreId() != null) {
                    // Nếu user đã có store, chuyển hướng về trang thông tin store
                    response.sendRedirect("Stores?service=storeInfo");
                } else {
                    // Nếu chưa có store, chuyển đến trang tạo store
                    request.getRequestDispatcher("views/store/createStore.jsp").forward(request, response);
                }
                break;
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

        // Kiểm tra phân quyền - chỉ owner mới có thể thực hiện các thao tác POST
        if (!"owner".equals(role)) {
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

            // Kiểm tra các ràng buộc
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

            // Cập nhật thông tin store
            Stores store = new Stores();
            store.setId(storeId);
            store.setName(name);
            store.setAddress(address);
            store.setPhone(phone);
            store.setEmail(email);
            storeDAO.editStore(store);

            session.setAttribute("successMessage", "Store details updated successfully.");
            response.sendRedirect("Stores?service=storeInfo");
        } else if ("createStore".equals(service)) {
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

            // Tạo đối tượng store mới
            Stores store = new Stores();
            store.setName(name);
            store.setAddress(address);
            store.setPhone(phone);
            store.setEmail(email);
            store.setStatus("Active");

            // Thêm store vào database và lấy ID được tạo
            int newStoreId = storeDAO.createStore(store);

            if (newStoreId > 0) {
                // Lấy thông tin store vừa tạo
                store = storeDAO.getStoreById(newStoreId);

                // Lấy thông tin user từ session
                Users user = userDAO.getUserByUsername(username);

                if (user != null) {
                    // Gán store cho user
                    user.setStoreId(store);

                    // Cập nhật user với store_id mới
                    boolean isUserUpdated = userDAO.updateUser(user);
                    session.setAttribute("storeID", String.valueOf(newStoreId));

                    if (isUserUpdated) {
                        System.out.println("User with username: " + username + " has been updated with Store ID: " + newStoreId);
                        // Add the storeID to the session
                        session.setAttribute("storeID", String.valueOf(newStoreId));
                        session.setAttribute("successMessage", "Store created successfully, and your store ID has been assigned.");
                        response.sendRedirect("Stores?service=storeInfo");
                    }
                } else {
                    System.out.println("Error updating user with username: " + username + " and Store ID: " + newStoreId);
                    session.setAttribute("errorMessage", "Error updating user with store ID.");
                    response.sendRedirect("Stores?service=createStore");
                }
            } else {
                session.setAttribute("errorMessage", "User not found.");
                response.sendRedirect("Stores?service=createStore");
            }
        } else {
            session.setAttribute("errorMessage", "Error creating store.");
            response.sendRedirect("Stores?service=createStore");
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
