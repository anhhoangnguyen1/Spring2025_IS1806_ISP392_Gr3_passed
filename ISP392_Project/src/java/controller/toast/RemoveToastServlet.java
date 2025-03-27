package controller.toast;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet xử lý xóa thông báo toast khỏi session sau khi đã hiển thị
 */
@WebServlet("/remove-toast")
public class RemoveToastServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Lấy session hiện tại
        HttpSession session = request.getSession(false);
        
        // Xóa thông báo toast khỏi session nếu tồn tại
        if (session != null) {
            session.removeAttribute("toastMessage");
            session.removeAttribute("toastType");
        }
        
        // Trả về response trạng thái 200 OK
        response.setStatus(HttpServletResponse.SC_OK);
    }
} 