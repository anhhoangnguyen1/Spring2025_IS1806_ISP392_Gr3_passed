/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.debt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
        if (service == null) {
            service = "debts";
        }
        if (service.equals("debts")) {
            String indexPage = request.getParameter("index");
            String command = request.getParameter("command");

            if (command == null || command.isEmpty()) {
                command = (String) session.getAttribute("command");
                if (command == null || command.isEmpty()) {
                    command = "id";
                }
            } else {
                session.setAttribute("command", command);
            }

            if (indexPage == null) {
                indexPage = "1";
            }

            int index = Integer.parseInt(indexPage);
            int count = debts.countDebts();
            int endPage = count / 10;
            if (count % 10 != 0) {
                endPage++;
            }
            List<DebtNote> listDebts = debts.viewAllDebt(command, index);
            List<String> listCustomer = customers.getAllCustomerNames();
            request.setAttribute("listName", listCustomer);
            request.setAttribute("list", listDebts);
            request.setAttribute("endPage", endPage);
            request.setAttribute("index", index);

            String notification = (String) request.getAttribute("Notification");
            if (notification != null && !notification.isEmpty()) {
                request.setAttribute("Notification", notification);
            }
            request.getRequestDispatcher("views/debtHistory/debts.jsp").forward(request, response);
        }
        if (service.equals("searchDebts")) {
            int id = Integer.parseInt(request.getParameter("browser"));
            try {
                List<DebtNote> list = debts.searchDebts(id);
                request.setAttribute("list", list);
                request.getRequestDispatcher("views/debtHistory/debts.jsp").forward(request, response);
            } catch (NumberFormatException e) {
            }
        }
        if (service.equals("debtHistory")) {
            int id = Integer.parseInt(request.getParameter("id"));
            List<DebtNote> list = debts.getDebtById(id);
            request.setAttribute("list", list);
            request.getRequestDispatcher("views/debtHistory/debtHistory.jsp").forward(request, response);
        }
        if (service.equals("addDebtInCustomers")) {
            String type = request.getParameter("type");
            if (type == null || type.trim().isEmpty()) {
                request.setAttribute("Notification", "Type is required.");
                request.getRequestDispatcher("Debts?service=debts").forward(request, response);
                return; // Nếu type rỗng, không tiếp tục xử lý
            }

            String amountStr = request.getParameter("amount");
            BigDecimal amount = null;
            if (amountStr == null || amountStr.trim().isEmpty()) {
                request.setAttribute("Notification", "Amount is required.");
                request.getRequestDispatcher("Debts?service=debts").forward(request, response);
                return; // Nếu amount rỗng, không tiếp tục xử lý
            }

            try {
                amount = new BigDecimal(amountStr);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    request.setAttribute("Notification", "Amount must be greater than zero.");
                    request.getRequestDispatcher("Debts?service=debts").forward(request, response);
                    return; // Nếu amount <= 0, không tiếp tục
                }
            } catch (NumberFormatException e) {
                request.setAttribute("Notification", "Amount must be a valid number.");
                request.getRequestDispatcher("Debts?service=debts").forward(request, response);
                return; // Nếu amount không phải là số hợp lệ, không tiếp tục
            }

// Nếu type là "-", thì cần đổi dấu của amount
            if ("-".equals(type)) {
                amount = amount.negate();
            }

            String customerIdStr = request.getParameter("customer_id");
            int customer_id = -1;
            try {
                if (customerIdStr != null && !customerIdStr.trim().isEmpty()) {
                    customer_id = Integer.parseInt(customerIdStr);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("Notification", "Invalid customer ID.");
                request.getRequestDispatcher("Debts?service=debts").forward(request, response);
                return; // Nếu customer_id không hợp lệ, không tiếp tục
            }

// Kiểm tra phần tệp tin ảnh (image upload)
            Part file = request.getPart("image");
            String imageFileName = null;
            if (file != null && file.getSize() > 0) {
                String fileType = file.getContentType();
                List<String> ALLOWED_MIME_TYPES = List.of("image/jpeg", "image/png", "image/gif"); // Định nghĩa các loại mime cho phép
                if (!ALLOWED_MIME_TYPES.contains(fileType)) {
                    request.setAttribute("Notification", "Invalid file type! Only JPG, PNG, and GIF are allowed.");
                    request.getRequestDispatcher("Debts?service=debts").forward(request, response);
                    return; // Nếu file type không hợp lệ, không tiếp tục
                }

                // Tạo tên file duy nhất nếu cần để tránh xung đột tên file
                imageFileName = System.currentTimeMillis() + "_" + getSubmittedFileName(file);

                String uploadDirectory = getServletContext().getRealPath("/") + "images";
                File uploadDir = new File(uploadDirectory);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String uploadPath = uploadDirectory + File.separator + imageFileName;
                try (FileOutputStream fos = new FileOutputStream(uploadPath); InputStream is = file.getInputStream()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("Notification", "File upload failed.");
                    request.getRequestDispatcher("Debts?service=debts").forward(request, response);
                    return;
                }
            }

// Kiểm tra và chuyển đổi 'created_at'
            String createdAtStr = request.getParameter("created_at");
            LocalDateTime createdAt = null;
            if (createdAtStr == null || createdAtStr.trim().isEmpty()) {
                request.setAttribute("Notification", "Created date is required.");
                request.getRequestDispatcher("Debts?service=debts").forward(request, response);
                return; // Nếu created_at rỗng, không tiếp tục xử lý
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                createdAt = LocalDateTime.parse(createdAtStr, formatter);
            } catch (DateTimeParseException e) {
                request.setAttribute("Notification", "Invalid date format. Expected format: yyyy-MM-dd'T'HH:mm.");
                request.getRequestDispatcher("Debts?service=debts").forward(request, response);
                return; // Nếu không thể chuyển đổi định dạng ngày, không tiếp tục
            }

// Sử dụng thời gian hiện tại cho 'updatedAt'
            LocalDateTime updatedAt = LocalDateTime.now();

// Các tham số còn lại
            String description = request.getParameter("description");
            String createdBy = request.getParameter("createdBy");
            String status = request.getParameter("status");

// Tiến hành lưu dữ liệu vào cơ sở dữ liệu hoặc xử lý tiếp theo...
            // Create DebtNote object and insert it into the database
            DebtNote debt = new DebtNote(0, type, amount, imageFileName, description, customer_id, createdAt, updatedAt, createdBy, status);
            customerDAO.editCustomerBalance(amount, customer_id);
            debts.insertDebtInCustomer(debt);

            // Redirect to the customers list page
            response.sendRedirect("Customers?service=customers");
        }

        if (service.equals("addDebt")) {
            try {
                // Extract parameters from the request
                String type = request.getParameter("type");
                if (type == null || type.trim().isEmpty()) {
                    request.setAttribute("Notification", "Type is required.");
                    request.getRequestDispatcher("Debts?service=debts").forward(request, response);
                    return; // Không hợp lệ nếu type rỗng
                }

                // Kiểm tra amount
                String amountStr = request.getParameter("amount");
                if (amountStr == null || amountStr.trim().isEmpty()) {
                    request.setAttribute("Notification", "Amount is required.");
                    request.getRequestDispatcher("Debts?service=debts").forward(request, response);
                    return; // Không hợp lệ nếu amount rỗng
                }

                BigDecimal amount = null;
                try {
                    amount = new BigDecimal(amountStr);
                    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                        request.setAttribute("Notification", "Amount must be greater than zero.");
                        request.getRequestDispatcher("Debts?service=debts").forward(request, response);
                        return; // Không hợp lệ nếu amount <= 0
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("Notification", "Amount must be a valid number.");
                    request.getRequestDispatcher("Debts?service=debts").forward(request, response);
                    return; // Không hợp lệ nếu amount không phải là số hợp lệ
                }

                // Kiểm tra file image (image upload)
                Part file = request.getPart("image");
                String imageFileName = null;
                if (file != null && file.getSize() > 0) {
                    String fileType = file.getContentType();
                    List<String> ALLOWED_MIME_TYPES = List.of("image/jpeg", "image/png", "image/gif"); // Định nghĩa các loại mime cho phép
                    if (!ALLOWED_MIME_TYPES.contains(fileType)) {
                        request.setAttribute("Notification", "Invalid file type! Only JPG, PNG, and GIF are allowed.");
                        request.getRequestDispatcher("Debts?service=debts").forward(request, response);
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
                        request.setAttribute("Notification", "File upload failed.");
                        request.getRequestDispatcher("Debts?service=debts").forward(request, response);
                        return;
                    }
                }

                // Retrieve other form parameters
                String description = request.getParameter("description");
                String createdBy = request.getParameter("createdBy");
                String status = request.getParameter("status");
                String createdAtStr = request.getParameter("created_at");
                LocalDateTime createdAt = null;

                if (createdAtStr != null && !createdAtStr.trim().isEmpty()) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                        createdAt = LocalDateTime.parse(createdAtStr, formatter);
                    } catch (DateTimeParseException e) {
                        request.setAttribute("Notification", "Invalid date format. Expected format: yyyy-MM-dd'T'HH:mm.");
                        request.getRequestDispatcher("Debts?service=debts").forward(request, response);
                        return; // Lỗi khi chuyển đổi ngày
                    }
                } else {
                    // Nếu không có giá trị, trả về thông báo lỗi
                    request.setAttribute("Notification", "Created date is required.");
                    request.getRequestDispatcher("Debts?service=debts").forward(request, response);
                    return; // Ngừng xử lý nếu không có ngày
                }

                LocalDateTime updatedAt = LocalDateTime.now();  // Current timestamp

                // Retrieve debtor_info parameters
                String debtorName = request.getParameter("debtorName");
                String debtorAddress = request.getParameter("debtorAddress");
                String debtorPhone = request.getParameter("debtorPhone");

                // Initialize debtorInfo JSON string
                String debtorInfoJson = null;
                if (debtorName != null || debtorAddress != null || debtorPhone != null) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    // Create an object to represent the debtor details
                    ObjectNode debtorNode = objectMapper.createObjectNode();

                    // Add fields if provided
                    if (debtorName != null) {
                        debtorNode.put("name", debtorName);
                    }
                    if (debtorAddress != null) {
                        debtorNode.put("address", debtorAddress);
                    }
                    if (debtorPhone != null) {
                        debtorNode.put("phone", debtorPhone);
                    }

                    // Convert the object to a JSON string
                    debtorInfoJson = debtorNode.toString();
                }

                // Create the DebtNote object
                DebtNote debt = new DebtNote(0, type, amount, imageFileName, description, debtorName, debtorAddress, debtorPhone, createdAt, updatedAt, createdBy, status);
                debts.insertDebt(debt);

                // Redirect after insertion
                response.sendRedirect("Debts?service=debts");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("Notification", "Error occurred while adding the debt.");
                request.getRequestDispatcher("Debts?service=debts").forward(request, response);
            }
        }

        if (service.equals("deleteDebtCustomers")) {
            String[] selectedDebts = request.getParameterValues("id");

            if (selectedDebts != null && selectedDebts.length > 0) {
                try {
                    for (String id : selectedDebts) {
                        int debtId = Integer.parseInt(id);
                        // Xóa nợ
                        debts.deleteDebt(debtId);  // Gọi phương thức deleteDebt từ service của bạn
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            response.sendRedirect("Customers");  // Chuyển hướng đến trang quản lý nợ sau khi xóa
        }
        if (service.equals("deleteDebt")) {
            String[] selectedDebts = request.getParameterValues("id");

            if (selectedDebts != null && selectedDebts.length > 0) {
                try {
                    for (String id : selectedDebts) {
                        int debtId = Integer.parseInt(id);
                        // Xóa nợ
                        debts.deleteDebt(debtId);  // Gọi phương thức deleteDebt từ service của bạn
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            response.sendRedirect("Debts");  // Chuyển hướng đến trang quản lý nợ sau khi xóa
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
