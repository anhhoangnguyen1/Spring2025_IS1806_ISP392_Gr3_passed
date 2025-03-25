/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.authen;

import dal.AccountDAO;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = GlobalUtils.getMd5(request.getParameter("password"));

        Users login = AccountDAO.INSTANCE.getUser(username, password);

        //if authenticate success 
         if (login != null) {
             
             if ("Deactive".equals(login.getStatus())) {
            // Nếu tài khoản bị ban, từ chối đăng nhập và thông báo lỗi
            request.setAttribute("error", "Your account is deactivated. Please contact admin.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }
              Stores store = login.getStoreId();  // Lấy đối tượng Stores
            String storeID = store != null ? String.valueOf(store.getId()) : null;  // Lấy storeID từ đối tượng Stores

            
            HttpSession session = request.getSession();
            session.setAttribute("userId", login.getId());
            session.setAttribute("user", login);
            session.setAttribute("username", login.getUsername());
            session.setAttribute("fullName", login.getName());
            session.setAttribute("storeID", storeID);
            session.setAttribute("role", login.getRole());
            session.setMaxInactiveInterval(30 * 60);

            Cookie userCookie = new Cookie("username", login.getUsername());
            userCookie.setMaxAge(30 * 60);
            response.addCookie(userCookie);

            response.sendRedirect(request.getContextPath() + "/Stores");
        } else {// if authenticate fail
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/views/login.jsp");
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
