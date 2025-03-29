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
import jakarta.servlet.http.Part;
import java.io.File;

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
    
    if (userId == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    // Always get fresh data from database
    Users user = profileDAO.INSTANCE.getUserById(userId);
    if (user == null) {
        request.setAttribute("errorMessage", "User not found!");
        request.getRequestDispatcher("/error.jsp").forward(request, response);
        return;
    }
    
        if (user.getImage() == null || user.getImage().isEmpty()) {
            user.setImage("default.jpg"); // Set default avatar
            profileDAO.INSTANCE.updateUser(user); // Update the user record in DB
        }
    
    // Update the session with fresh data
    session.setAttribute("user", user);
    request.setAttribute("user", user);
    request.getRequestDispatcher("/views/profile/profile.jsp").forward(request, response);
}


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy phần ảnh từ form
        Part filePart = request.getPart("imageFile");  // "imageFile" là name của input file trong form
        String fileName = filePart.getSubmittedFileName();

            if (filePart == null || filePart.getSize() == 0) {
            fileName = "default.jpg"; // Sử dụng ảnh mặc định
        }
        
        // Đặt thư mục upload
        String uploadDir = getServletContext().getRealPath("/") + "views/profile/image";
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdir();
        }

        // Lưu file vào thư mục avatars
        String filePath = uploadDir + File.separator + fileName;
        filePart.write(filePath);

        // Cập nhật đường dẫn ảnh trong CSDL
        Users user = profileDAO.INSTANCE.getUserById(userId);
        if (user != null) {
            user.setImage(fileName);  // Giả sử bạn có setter cho ảnh
            profileDAO.INSTANCE.updateUser(user);  // Cập nhật thông tin người dùng vào CSDL
        }

        // Cập nhật lại ảnh trong session
        session.setAttribute("user", user);

        // Chuyển hướng lại trang profile
        response.sendRedirect(request.getContextPath() + "/views/profile/profile.jsp");
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
