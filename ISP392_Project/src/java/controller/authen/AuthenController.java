package controller.authen;

import dal.AccountDAO;
import entity.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import utils.EmailUtils;
import utils.GlobalUtils;

@WebServlet(name = "AuthenController", urlPatterns = {"/authen", "/forgotpw", "/verifyOTP", "/newPassword"})
public class AuthenController extends HttpServlet {

    private AccountDAO accDAO;
    private final String FORGOT_PASSWORD_JSP_PAGE = "views/authen/forgotPassword.jsp";
    private final String RESET_PASSWORD_JSP_PAGE = "views/authen/newPassword.jsp";
    private final String VERIFY_OTP_JSP_PAGE = "views/authen/verifyOTP.jsp";

    @Override
    public void init() throws ServletException {
        accDAO = new AccountDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // get ve action
        String action = request.getServletPath() != null
                ? request.getServletPath()
                : "";
        // dua theo action set URL trang can chuyen den
        String url;
        switch (action) {
            case "login":
                //TODO:...
                url = "";
                break;
            case "sign-up":
                //TODO
                url = "";
                break;
            case "/forgotpw":
                url = FORGOT_PASSWORD_JSP_PAGE;
                break;
            default:
                url = "view/authen/login.jsp";
        }

        // chuyen trang
        request.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // get ve action
        String action = request.getServletPath() != null
                ? request.getServletPath()
                : "";
        // dựa theo action để xử lí request 
        String url;
        switch (action) {
            case "login":
                //TODO:...
                url = "";
                break;
            case "sign-up":
                //TODO:...
                url = "";
                break;
            case "/verifyOTP":
                //TODO:...
                url = verifyOTP(request, response);
                break;
            case "/forgotpw":
                url = forgotPassword(request, response);
                break;
            case "/newPassword":
                //TODO:...
                url = resetPassword(request, response);
                break;
            default:
                url = "home";
        }
        request.getRequestDispatcher(url).forward(request, response);
    }

    private String forgotPassword(HttpServletRequest request, HttpServletResponse response) {
        String url;
        String email = request.getParameter("email");

        // Kiểm tra xem email có tồn tại trong cơ sở dữ liệu không
        Users userRequestEmail = new Users();
        userRequestEmail.setEmail(email);

        Users foundAccount = accDAO.findByEmail(userRequestEmail);

        if (foundAccount == null) {
            // Email không tìm thấy trong cơ sở dữ liệu
            request.setAttribute("error", "No account found with this email address.");
            url = FORGOT_PASSWORD_JSP_PAGE;
            return url;
        }

        // Gửi OTP
        HttpSession session = request.getSession();
        String otp = EmailUtils.sendOTPMail(email);

        // Lưu thông tin vào session
        session.setAttribute("otp", otp);
        session.setAttribute("email", email);
        session.setAttribute("otp_purpose", "password_reset");
        session.setAttribute("account_id", foundAccount.getId());

        // Đặt thời gian hết hạn cho session (ví dụ: 15 phút)
        session.setMaxInactiveInterval(15 * 60);

        url = VERIFY_OTP_JSP_PAGE;
        return url;
    }

    private String verifyOTP(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String storedOTP = (String) session.getAttribute("otp");
        String email = (String) session.getAttribute("email");
        String enteredOTP = request.getParameter("otp");
        String purpose = (String) session.getAttribute("otp_purpose");

        if (storedOTP != null && storedOTP.equals(enteredOTP)) {
            // OTP is correct
            session.removeAttribute("otp");

            if ("activation".equals(purpose)) {
                return handleAccountActivation(request, session);
            } else if ("password_reset".equals(purpose)) {
                return handlePasswordReset(request, session);
            } else {
                request.setAttribute("error", "Invalid OTP purpose.");
                return VERIFY_OTP_JSP_PAGE;
            }
        } else {
            // Incorrect OTP
            request.setAttribute("error", "Incorrect OTP. Please try again.");
            return VERIFY_OTP_JSP_PAGE;
        }
    }

    private String handleAccountActivation(HttpServletRequest request, HttpSession session) {
        //Todo
        return null;
    }

    private String handlePasswordReset(HttpServletRequest request, HttpSession session) {
        // Redirect to password reset page
        return RESET_PASSWORD_JSP_PAGE;
    }

    private String resetPassword(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        String newPassword = request.getParameter("new_password");
        String confirmPassword = request.getParameter("confirm_password");

        if (newPassword == null || newPassword.length() < 6) {
            request.setAttribute("error", "Password must be at least 6 characters long.");
            return RESET_PASSWORD_JSP_PAGE; // Stop execution
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            return RESET_PASSWORD_JSP_PAGE;
        }

        String hashedPassword = GlobalUtils.getMd5(newPassword);

        Users account = Users.builder()
                .email(email)
                .password(hashedPassword)
                .build();

        boolean updated = accDAO.updatePassword(account);
        if (updated) {
            request.setAttribute("message", "Your password has been successfully reset.");
            return "/views/login.html";
        } else {
            request.setAttribute("error", "Failed to reset password. Please try again.");
            return RESET_PASSWORD_JSP_PAGE;
        }
    }
}
