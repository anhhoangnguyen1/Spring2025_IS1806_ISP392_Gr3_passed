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

@WebServlet(name = "AuthenController", urlPatterns = {"/authen", "/forgotpw", "/verifyOTP", "/newPassword", "/resendOTP"})
public class AuthenController extends HttpServlet {

    private AccountDAO accDAO;
    private final String FORGOT_PASSWORD_JSP_PAGE = "views/authen/forgotPassword.jsp";

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
            case "/verifyOTP":
                url = "views/authen/verifyOTP.jsp";
                break;
            case "/newPassword":
                url = "views/authen/newPassword.jsp";
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
            case "/resendOTP":
                url = resendOTP(request, response);
                break;
            case "/verifyOTP":
                url = verifyOTP(request, response);
                break;
            case "/forgotpw":
                url = forgotPassword(request, response);
                break;
            case "/newPassword":
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

        url = "views/authen/verifyOTP.jsp";
        return url;
    }

    private String verifyOTP(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String storedOTP = (String) session.getAttribute("otp");
        String email = (String) session.getAttribute("email");
        String enteredOTP = request.getParameter("otp");
        String purpose = (String) session.getAttribute("otp_purpose");
        
        // Get attempt count from session, initialize to 1 if not present
        Integer attemptCount = (Integer) session.getAttribute("otp_attempts");
        if (attemptCount == null) {
            attemptCount = 1;
        } else {
            attemptCount++;
        }
        
        // Update attempt count in session
        session.setAttribute("otp_attempts", attemptCount);
        
        // Check if max attempts reached
        if (attemptCount > 5) {
            // Clear OTP related data from session
            session.removeAttribute("otp");
            session.removeAttribute("otp_attempts");
            
            request.setAttribute("error", "Maximum attempts reached. Please request a new OTP.");
            return "views/authen/verifyOTP.jsp";
        }
        
        if (storedOTP != null && storedOTP.equals(enteredOTP)) {
            // OTP is correct
            session.removeAttribute("otp");
            session.removeAttribute("otp_attempts");

            if ("activation".equals(purpose)) {
                return handleAccountActivation(request, session);
            } else if ("password_reset".equals(purpose)) {
                return handlePasswordReset(request, session);
            } else {
                request.setAttribute("error", "Invalid OTP purpose.");
                return "view/authen/otp-verification.jsp";
            }
        } else {
            // Incorrect OTP
            request.setAttribute("error", "Incorrect OTP. Attempts remaining: " + (5 - attemptCount));
            return "views/authen/verifyOTP.jsp";
        }
    }
    
    private String handleAccountActivation(HttpServletRequest request, HttpSession session) {
        //Todo
        return null;
    }
    
    private String handlePasswordReset(HttpServletRequest request, HttpSession session) {
        // Redirect to password reset page
        return "views/authen/newPassword.jsp";
    }
    
    private String resetPassword(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        String newPassword = request.getParameter("new_password");
        String confirmPassword = request.getParameter("confirm_password");

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            return "views/authen/newPassword.jsp";
        }
        String hashedPassword = GlobalUtils.getMd5(newPassword);
        Users account = Users.builder()
                .email(email)
                .password(hashedPassword)
                .build();

        boolean updated = accDAO.updatePassword(account);
        if (updated) {
            // Set success message for the success page
            request.setAttribute("successMessage", "Your password has been successfully reset!");
            // Clear sensitive session data
            session.removeAttribute("email");
            session.removeAttribute("otp_purpose");
            session.removeAttribute("account_id");
            
            return "views/authen/resetSuccess.jsp";
        } else {
            // Set toast message for error
            session.setAttribute("toastMessage", "Failed to reset password. Please try again.");
            session.setAttribute("toastType", "error");
            return "views/authen/newPassword.jsp";
        }
    }

    private String resendOTP(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        String purpose = (String) session.getAttribute("otp_purpose");
        
        if (email == null || email.isEmpty()) {
            email = request.getParameter("email");
            if (email == null || email.isEmpty()) {
                request.setAttribute("error", "Email not found. Please try again.");
                return FORGOT_PASSWORD_JSP_PAGE;
            }
        }
        
        // Generate and send new OTP
        String newOTP = EmailUtils.sendOTPMail(email);
        
        // Update session with new OTP and reset attempt counter
        session.setAttribute("otp", newOTP);
        session.setAttribute("email", email);
        session.setAttribute("otp_attempts", 0);
        
        // If purpose is not set, default to password reset
        if (purpose == null) {
            session.setAttribute("otp_purpose", "password_reset");
        }
        
        // Reset session timeout
        session.setMaxInactiveInterval(15 * 60);
        
        request.setAttribute("message", "A new OTP has been sent to your email.");
        return "views/authen/verifyOTP.jsp";
    }
}
