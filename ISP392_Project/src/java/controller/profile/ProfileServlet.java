/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.profile;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import entity.*;
import dal.*;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author THC
 */
public class ProfileServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        // Kiểm tra nếu user chưa đăng nhập
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }

        // Lấy thông tin người dùng từ database
        Users user = profileDAO.INSTANCE.getUserById(userId);
            System.out.println("User fetched from DB: " + user);
        // Kiểm tra user có tồn tại không
        if (user == null) {
            request.setAttribute("errorMessage", "User not found!");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        // Gửi thông tin user đến trang JSP
        request.setAttribute("user", user);
        request.getRequestDispatcher("/views/profile/profile.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
