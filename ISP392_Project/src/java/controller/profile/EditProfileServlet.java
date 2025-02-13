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
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;

/**
 *
 * @author THC
 */
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB - Kích thước tối thiểu trước khi lưu vào file
    maxFileSize = 1024 * 1024 * 10,      // 10MB - Kích thước tối đa cho 1 file
    maxRequestSize = 1024 * 1024 * 50    // 50MB - Tổng kích thước request
)

public class EditProfileServlet extends HttpServlet {

    private final profileDAO profileDAO = new profileDAO();

    private static final String AVATAR_DIR = "avatars";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Users user = profileDAO.INSTANCE.getUserById(userId);
        if (user == null) {
            request.setAttribute("errorMessage", "User not found!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        request.setAttribute("user", user);
        request.getRequestDispatcher("Views/editProfile.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method. Updates the user profile with
     * the submitted data.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy thông tin từ form
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        // Xử lý upload avatar
        Part filePart = request.getPart("avatar");
        String avatarFileName = userId + ".png";
        String uploadPath = getServletContext().getRealPath("/") + File.separator + AVATAR_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();
        if (filePart.getSize() > 0) {
            filePart.write(uploadPath + File.separator + avatarFileName);
        }
        // Cập nhật user bằng Builder
        Users updatedUser = Users.builder()
                .id(userId)
                .name(name)
                .email(email)
                .phone(phone)
                .image("/avatars/" + avatarFileName) // Lưu đường dẫn avatar
                .build();

        // Gọi DAO cập nhật
        boolean updateSuccess = profileDAO.updateUser(updatedUser);
        if (updateSuccess) {
            session.setAttribute("successMessage", "Profile updated successfully!");
            response.sendRedirect("profile");
        } else {
            request.setAttribute("errorMessage", "Failed to update profile!");
            request.setAttribute("user", updatedUser);
            request.getRequestDispatcher("Views/profile/editProfile.jsp").forward(request, response);
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
