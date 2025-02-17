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

           
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }




    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
