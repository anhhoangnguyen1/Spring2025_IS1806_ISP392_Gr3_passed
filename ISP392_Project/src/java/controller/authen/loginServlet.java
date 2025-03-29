/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.authen;

import dal.AccountDAO;
import dal.storeDAO;
import entity.*;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.GlobalUtils;

/**
 *
 * @author admin
 */
public class loginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = GlobalUtils.getMd5(request.getParameter("password"));

        // Ghi log thông tin người dùng đang đăng nhập
        System.out.println("Attempting login for username: " + username);

        Users login = AccountDAO.INSTANCE.getUser(username, password);


         if (login != null) {             
             if ("Inactive".equalsIgnoreCase(login.getStatus())) {
        
            request.setAttribute("error", "Your account is inactive. Please contact admin.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }

            // Kiểm tra nếu người dùng là admin
            if (!"admin".equalsIgnoreCase(login.getRole())) {
                // Nếu không phải admin, kiểm tra cửa hàng (store)
                Stores store = login.getStoreId();
                storeDAO storeDAO = new storeDAO();
                if (store != null) {
                    System.out.println("User's store found with ID: " + store.getId());
                    store = storeDAO.getStoreById(store.getId());  // Lấy cửa hàng mới nhất từ database

                    // Kiểm tra trạng thái của cửa hàng (store)
                    if ("Inactive".equalsIgnoreCase(store.getStatus())) {
                        System.out.println("Store with ID " + store.getId() + " is deactivated (Inactive). Login denied.");
                        request.setAttribute("error", "Your Store is inactive. Please contact admin.");
                        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
                        return;
                    }
                    System.out.println("Store with ID " + store.getId() + " is active. Proceeding with login.");
                }
            }

            // Nếu mọi thứ ổn, lưu thông tin vào session và tiếp tục với đăng nhập
            String storeID = login.getStoreId() != null ? String.valueOf(login.getStoreId().getId()) : null;
            HttpSession session = request.getSession();
            session.setAttribute("userId", login.getId());
            session.setAttribute("user", login);
            session.setAttribute("username", login.getUsername());
            session.setAttribute("fullName", login.getName());
            session.setAttribute("storeID", storeID);
            session.setAttribute("role", login.getRole());
            session.setMaxInactiveInterval(30 * 60);  // Session hết hạn sau 30 phút

            // Lưu tên người dùng vào cookie (thời gian sống 30 phút)
            Cookie userCookie = new Cookie("username", login.getUsername());
            userCookie.setMaxAge(30 * 60);
            response.addCookie(userCookie);

            // Chuyển hướng đến trang quản lý cửa hàng (Stores)
            System.out.println("Redirecting user " + username + " to /Stores.");
            response.sendRedirect(request.getContextPath() + "/Stores");
        } else {
            // Nếu xác thực thất bại (username hoặc password sai)
            System.out.println("Invalid login attempt for username: " + username);
            request.setAttribute("error", "Your username or password is wrong");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
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
