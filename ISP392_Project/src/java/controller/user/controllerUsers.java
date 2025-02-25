/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.user;

import dal.userDAO;
import entity.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author THC
 */
@WebServlet(name = "controllerUsers", urlPatterns = {"/Users"})
public class controllerUsers extends HttpServlet {

    userDAO userDAO = new userDAO();

    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    HttpSession session = request.getSession();
    String service = request.getParameter("service");
    if (service == null) {
        service = "users";
    }

    switch (service) {
        case "users":{
            String keyword = request.getParameter("searchUser");
            if (keyword == null) {
                keyword = "";
            }

            int index = 1;
            try {
                index = Integer.parseInt(request.getParameter("index"));
            } catch (NumberFormatException ignored) {
            }

            int total = userDAO.countUsers(keyword);
            int endPage = (total % 5 == 0) ? total / 5 : (total / 5) + 1;

            List<Users> list = userDAO.searchUsers(keyword, index, 5);

            request.setAttribute("list", list);
            request.setAttribute("endPage", endPage);
            request.setAttribute("index", index);
            request.setAttribute("searchUser", keyword);

            request.getRequestDispatcher("views/user/users.jsp").forward(request, response);
            break;
        }


            
//                int index = 1;
//                try {
//                    index = Integer.parseInt(request.getParameter("index"));
//                } catch (NumberFormatException ignored) {
//                }
//
//                List<Users> list = userDAO.viewAllUsers(index);
//
//                int total = userDAO.countUsers();
//                int endPage = (total % 10 == 0) ? total / 10 : (total / 10) + 1;
//
//                request.setAttribute("userList", list);
//                request.setAttribute("endPage", endPage);
//                request.setAttribute("index", index);
//
//                request.getRequestDispatcher("views/user/users.jsp").forward(request, response);
//                break;

            case "getUserById":
            {
                int id = Integer.parseInt(request.getParameter("user_id"));
                Users user = userDAO.getUserById(id);
                request.setAttribute("user", user);

                request.getRequestDispatcher("views/user/detailUser.jsp").forward(request, response);
                break;
            }
            case "editUser":{
          
                int userId = Integer.parseInt(request.getParameter("user_id"));
                Users userForEdit = userDAO.getUserById(userId);
                request.setAttribute("user", userForEdit);

                // Lấy tên người dùng từ session
                String userName = (String) session.getAttribute("username"); // Lấy username từ session
                request.setAttribute("userName", userName); // Đưa username vào request

                request.getRequestDispatcher("views/user/detailUser.jsp").forward(request, response);
                break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");

        if ("editUser".equals(service)) {

            int userId = Integer.parseInt(request.getParameter("user_id"));
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            String gender = request.getParameter("gender");
            String status = request.getParameter("status");
            String role = request.getParameter("role");
            String updatedBy = request.getParameter("updatedBy");
            java.sql.Date dob = java.sql.Date.valueOf(request.getParameter("dob"));

            boolean emailExists = userDAO.checkEmailExists(email, userId);
            boolean phoneExists = userDAO.checkPhoneExists(phone, userId);

            if (emailExists) {
                request.setAttribute("emailError", "Email is existed.");
                request.setAttribute("user", getUserFromRequest(request));
                request.getRequestDispatcher("views/user/detailUser.jsp").forward(request, response);
                return;
            }
            if (phoneExists) {
                request.setAttribute("phoneError", "Phone is existed.");
                request.setAttribute("user", getUserFromRequest(request));
                request.getRequestDispatcher("views/user/detailUser.jsp").forward(request, response);
                return;
            }


            Users user = Users.builder()
                    .id(Integer.parseInt(request.getParameter("user_id")))
                    .name(request.getParameter("name"))
                    .phone(request.getParameter("phone"))
                    .address(request.getParameter("address"))
                    .gender(request.getParameter("gender"))
                    .dob(java.sql.Date.valueOf(request.getParameter("dob"))) // chuyển đổi từ String thành Date
                    .role(request.getParameter("role"))
                    .email(request.getParameter("email"))
                    .status(request.getParameter("status"))
                    .build();
            userDAO.editUser(user);
            
            HttpSession session = request.getSession();
            session.setAttribute("successMessage", "User details updated successfully.");
            response.sendRedirect("Users?service=users");
        }
//        if ("search".equals(service)) {
//            String searchStaff = request.getParameter("searchStaff"); // Lấy giá trị tìm kiếm từ form
//            List<Users> list;
//
//            // Kiểm tra nếu có từ khóa tìm kiếm
//            if (searchStaff != null && !searchStaff.isEmpty()) {
//                list = userDAO.searchUsers(searchStaff);  // Gọi phương thức tìm kiếm
//            } else {
//                list = userDAO.viewAllUsers(1);  // Hiển thị tất cả người dùng nếu không có tìm kiếm
//            }
//
//            request.setAttribute("userList", list);
//            request.getRequestDispatcher("views/user/users.jsp").forward(request, response);
//        }
    }

    private Users getUserFromRequest(HttpServletRequest request) {
        Users user = new Users();
        user.setId(Integer.parseInt(request.getParameter("user_id")));
        user.setName(request.getParameter("name"));
        user.setEmail(request.getParameter("email"));
        user.setPhone(request.getParameter("phone"));
        user.setAddress(request.getParameter("address"));
        user.setDob(java.sql.Date.valueOf(request.getParameter("dob")));
        user.setGender(request.getParameter("gender"));
        user.setStatus(request.getParameter("status"));
        user.setRole(request.getParameter("role"));
        return user;
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
