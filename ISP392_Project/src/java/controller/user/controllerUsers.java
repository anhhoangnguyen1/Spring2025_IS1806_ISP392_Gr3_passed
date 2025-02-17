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
            case "users":
                int index = 1;
                try {
                    index = Integer.parseInt(request.getParameter("index"));
                } catch (NumberFormatException ignored) {
                }

                List<Users> list = userDAO.viewAllUsers(index);

                int total = userDAO.countUsers();
                int endPage = (total % 10 == 0) ? total / 10 : (total / 10) + 1;

                request.setAttribute("userList", list);
                request.setAttribute("endPage", endPage);
                request.setAttribute("index", index);

                request.getRequestDispatcher("views/user/users.jsp").forward(request, response);
                break;

            case "getUserById":
                int id = Integer.parseInt(request.getParameter("user_id"));
                Users user = userDAO.getUserById(id);
                request.setAttribute("user", user);

                request.getRequestDispatcher("views/user/detailUser.jsp").forward(request, response);
                break;

            case "editUser":
                // Lấy thông tin người dùng cần chỉnh sửa
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");

        if ("editUser".equals(service)) {
            // Lấy thông tin người dùng từ form
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
            
            // Chỉnh sửa thông tin người dùng
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
            response.sendRedirect("Users?service=users");
        }
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
