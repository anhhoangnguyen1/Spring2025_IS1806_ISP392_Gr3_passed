/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.profile;

import java.io.IOException;
import java.nio.file.Paths;
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
import java.util.regex.Pattern;

/**
 *
 * @author THC
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)

public class EditProfileServlet extends HttpServlet {

    private final profileDAO profileDAO = new profileDAO();

    private static final String AVATAR_DIR = "avatars";
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

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
        request.getRequestDispatcher("/views/profile/editProfile.jsp").forward(request, response);
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

        Users existingUser = profileDAO.INSTANCE.getUserById(userId);
        if (existingUser == null) {
            request.setAttribute("errorMessage", "User not found!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String gender = request.getParameter("gender");
        String dob = request.getParameter("dob");
        String status = request.getParameter("status");

        name = (name != null && !name.trim().isEmpty()) ? name : existingUser.getName();
        email = (email != null && !email.trim().isEmpty()) ? email : existingUser.getEmail();
        phone = (phone != null && !phone.trim().isEmpty()) ? phone : existingUser.getPhone();
        address = (address != null && !address.trim().isEmpty()) ? address : existingUser.getAddress();
        gender = (gender != null && !gender.trim().isEmpty()) ? gender : existingUser.getGender();
        status = (status != null && !status.trim().isEmpty()) ? status : existingUser.getStatus();

        java.sql.Date sqlDob = null;
        try {
            if (dob != null && !dob.trim().isEmpty()) {
                sqlDob = java.sql.Date.valueOf(dob);
            } else {
                sqlDob = existingUser.getDob();
            }
        } catch (IllegalArgumentException e) {
            sqlDob = existingUser.getDob();
        }

        // Kiểm tra trùng email và số điện thoại
        boolean emailExists = profileDAO.checkEmailExists(email, userId);
        boolean phoneExists = profileDAO.checkPhoneExists(phone, userId);

        if (emailExists) {
            request.setAttribute("emailError", "Email already exists.");
            request.setAttribute("user", existingUser);
            request.getRequestDispatcher("/views/profile/editProfile.jsp").forward(request, response);
            return;
        }

        if (phoneExists) {
            request.setAttribute("phoneError", "Phone number already exists.");
            existingUser.setPhone(phone);
            request.setAttribute("user", existingUser);
            request.getRequestDispatcher("/views/profile/editProfile.jsp").forward(request, response);
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            request.setAttribute("emailError", "Invalid email format.");
            existingUser.setEmail(email);
            request.setAttribute("user", existingUser);
            request.getRequestDispatcher("/views/profile/editProfile.jsp").forward(request, response);
            return;
        }

        if (!phone.matches("^0\\d{9}$")) {
            request.setAttribute("phoneError", "Invalid phone format.");
            existingUser.setPhone(phone);
            request.setAttribute("user", existingUser);
            request.getRequestDispatcher("/views/profile/editProfile.jsp").forward(request, response);
            return;
        }

        Part filePart = request.getPart("image"); // Image part
        String avatarFileName = existingUser.getImage();  // Default to old image if no new image

        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

            if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif")) {
                avatarFileName = userId + "." + extension;  // Ensure unique filename
                String uploadPath = getServletContext().getRealPath("/") + File.separator + AVATAR_DIR;
                File uploadDir = new File(uploadPath);

                if (!uploadDir.exists()) {
                    uploadDir.mkdir();  // Create directory if it doesn't exist
                }

                filePart.write(uploadPath + File.separator + avatarFileName);  // Save the image
            }
        }

        Users updatedUser = Users.builder()
                .id(userId)
                .name(name)
                .email(email)
                .phone(phone)
                .address(address)
                .gender(gender)
                .dob(sqlDob)
                .status(status)
                .image(avatarFileName) // Update image path
                .build();

        session.setAttribute("user", updatedUser);
        boolean updateSuccess = profileDAO.updateUser(updatedUser);
        if (updateSuccess) {
            session.setAttribute("successMessage", "Profile updated successfully!");
            session.setAttribute("user", updatedUser);  // Update session
            response.sendRedirect(request.getContextPath() + "/user");
        } else {
            request.setAttribute("errorMessage", "Failed to update profile!");
            request.setAttribute("user", updatedUser);
            request.getRequestDispatcher("/views/profile/editProfile.jsp").forward(request, response);
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
