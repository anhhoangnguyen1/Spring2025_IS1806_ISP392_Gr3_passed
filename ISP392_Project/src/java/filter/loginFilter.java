package filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class loginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Kiểm tra session để xác định người dùng đã đăng nhập chưa
        HttpSession session = req.getSession(false); 
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/views/login.html");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // Có thể không cần làm gì trong method này
    }
}
