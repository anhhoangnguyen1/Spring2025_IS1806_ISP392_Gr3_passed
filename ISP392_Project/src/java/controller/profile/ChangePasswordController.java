package controller.profile;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import entity.Users;
import dal.AccountDAO;
import jakarta.servlet.http.HttpSession;
import utils.GlobalUtils;

@WebServlet(urlPatterns = { "/change-password" })
public class ChangePasswordController extends HttpServlet {

    private AccountDAO accountDAO;
    private final String CHANGE_PASSWORD_SUCCESS_URL = "/views/profile/change-password.jsp";

    public void init() throws ServletException {
        super.init();
        accountDAO = new AccountDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("views/profile/change-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null) {
            switch (action) {
                case "change-password":
                    changePassword(request, response);
                    break;
                // Add more cases as needed
                default:
                    // Handle unknown action
                    break;
            }
        }
    }

    private void changePassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        String hashedCurrentPassword = GlobalUtils.getMd5(currentPassword);
        String hashedNewPassword = GlobalUtils.getMd5(newPassword);
        String hashedConfirmPassword = GlobalUtils.getMd5(confirmPassword);

        // check if the fields are empty
        if (currentPassword == null || newPassword == null || confirmPassword == null) {
            request.setAttribute("error", "All fields are required");
            request.getRequestDispatcher(CHANGE_PASSWORD_SUCCESS_URL).forward(request, response);
            return;
        }

        // check if the new password and confirm password are the same
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "New password and confirm password do not match");
            request.getRequestDispatcher(CHANGE_PASSWORD_SUCCESS_URL).forward(request, response);
            return;
        }
        // check if the new password is the same as the current password
        if (currentPassword.equals(newPassword)) {
            request.setAttribute("error", "New password cannot be the same as the current password");
            request.getRequestDispatcher(CHANGE_PASSWORD_SUCCESS_URL).forward(request, response);
            return;
        }

        // check if the current password is correct
        Users user = Users.builder().email("phanngocmai2411@gmail.com").build();
        Users user1 = accountDAO.findByEmail(user);
        if (user1 == null || !user1.getPassword().equals(hashedCurrentPassword) ) {
            request.setAttribute("error", "Current password is incorrect");
            request.getRequestDispatcher(CHANGE_PASSWORD_SUCCESS_URL).forward(request, response);
            return;
        }

        // change the password
        // Users user = (Users) request.getSession().getAttribute("user");
        user.setPassword(hashedNewPassword);

        accountDAO.updatePassword(user);

        request.setAttribute("success", "Password changed successfully");
        request.getRequestDispatcher(CHANGE_PASSWORD_SUCCESS_URL).forward(request, response);

    }

}
