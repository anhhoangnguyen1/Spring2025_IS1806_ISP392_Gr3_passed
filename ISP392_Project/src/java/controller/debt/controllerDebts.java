/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.debt;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dal.debtDAO;
import entity.DebtNote;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import dal.customerDAO;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

/**
 *
 * @author phamh
 */
@WebServlet(name = "controllerDebts", urlPatterns = {"/Debts"})
@MultipartConfig
public class controllerDebts extends HttpServlet {

    customerDAO customerDAO = new customerDAO();
    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif");

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String service = request.getParameter("service");
        customerDAO customers = new customerDAO();
        debtDAO debts = new debtDAO();

        if (service.equals("searchDebts")) {
            int id = Integer.parseInt(request.getParameter("browser"));
            try {
                List<DebtNote> list = debts.searchDebts(id);
                request.setAttribute("list", list);
                request.getRequestDispatcher("views/debtHistory/debts.jsp").forward(request, response);
            } catch (NumberFormatException e) {
            }
        }

        if (service.equals("addDebtInCustomers")) {
            String status = request.getParameter("status");
            String type;

            if (status == null || status.trim().isEmpty()) {
                request.getSession().setAttribute("Notification", "Status is required.");
                response.sendRedirect("Customers?service=customers");
                return;
            }
            if ("Customer takes a loan".equals(status) || "Customer pays".equals(status)) {
                type = "+";
            } else {
                type = "-";
            }

            String amountStr = request.getParameter("amount");
            BigDecimal amount = null;
            if (amountStr == null || amountStr.trim().isEmpty()) {
                request.getSession().setAttribute("Notification", "Amount is required.");
                response.sendRedirect("Customers?service=customers");
                return; // Nếu amount rỗng, không tiếp tục xử lý
            }
            amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                request.getSession().setAttribute("Notification", "Amount must be greater than zero.");
                response.sendRedirect("Customers?service=customers");
                return; // Nếu amount <= 0, không tiếp tục
            }

            // Nếu type là "-", thì cần đổi dấu của amount
            if ("-".equals(type) && amount.compareTo(BigDecimal.ZERO) > 0) {
                amount = amount.negate();
            }

            String customerIdStr = request.getParameter("customer_id");
            int customer_id = Integer.parseInt(customerIdStr);

            // Kiểm tra phần tệp tin ảnh (image upload)
            Part file = request.getPart("image");
            String imageFileName = null;
            if (file != null && file.getSize() > 0) {
                String fileType = file.getContentType();
                List<String> ALLOWED_MIME_TYPES = List.of("image/jpeg", "image/png", "image/gif"); // Định nghĩa các loại mime cho phép
                if (!ALLOWED_MIME_TYPES.contains(fileType)) {
                    request.getSession().setAttribute("Notification", "Invalid file type! Only JPG, PNG, and GIF are allowed.");
                    response.sendRedirect("Customers?service=customers");
                    return;
                }

                // Lấy tên file từ đối tượng Part
                imageFileName = getSubmittedFileName(file);

                // Đường dẫn để lưu tệp
                String uploadDirectory = getServletContext().getRealPath("/") + "images";
                File uploadDir = new File(uploadDirectory);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // Đường dẫn lưu tệp
                String uploadPath = uploadDirectory + File.separator + imageFileName;
                try (FileOutputStream fos = new FileOutputStream(uploadPath); InputStream is = file.getInputStream()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    request.getSession().setAttribute("Notification", "File upload failed.");
                    response.sendRedirect("Customers?service=customers");
                    return;
                }
            } else {
                imageFileName = null;
            }

            // Kiểm tra và chuyển đổi 'created_at'
            String createdAtStr = request.getParameter("created_at");
            LocalDateTime createdAt = null;
            if (createdAtStr == null || createdAtStr.trim().isEmpty()) {
                request.getSession().setAttribute("Notification", "Created date is required.");
                response.sendRedirect("Customers?service=customers");
                return; // Nếu created_at rỗng, không tiếp tục xử lý
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                createdAt = LocalDateTime.parse(createdAtStr, formatter);
            } catch (DateTimeParseException e) {
                request.getSession().setAttribute("Notification", "Invalid date format. Expected format: yyyy-MM-dd'T'HH:mm.");
                response.sendRedirect("Customers?service=customers");
                return; // Nếu không thể chuyển đổi định dạng ngày, không tiếp tục
            }

            LocalDateTime updatedAt = LocalDateTime.now();
            String description = request.getParameter("description");
            String createdBy = request.getParameter("createdBy");

            // Kiểm tra xem khách hàng đã có nợ hay chưa
            BigDecimal totalDebtAmount = BigDecimal.ZERO;


          
                // Nếu không có nợ cũ, thêm mới
                DebtNote newDebt = new DebtNote(0, type, amount, imageFileName, description, customer_id, createdAt, updatedAt, createdBy, status);
                boolean insert = debts.insertDebtInCustomer(newDebt);
                if (insert) {
                    totalDebtAmount = totalDebtAmount.add(amount);
                    customerDAO.editCustomerBalance(totalDebtAmount, customer_id);
                    request.getSession().setAttribute("Notification", "Debt added successfully.");
                } else {
                    request.getSession().setAttribute("Notification", "Debt added failed.");
                }
            response.sendRedirect("Customers?service=customers");

        }

    }

    private static String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
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
