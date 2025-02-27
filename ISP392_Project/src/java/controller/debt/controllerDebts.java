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
        if (service == null) {
            service = "debts";
        }
        if (service.equals("debts")) {
            String indexPage = request.getParameter("index");
            String pageSizeParam = request.getParameter("pageSize");
            // Nếu `index` không có, mặc định là 1
            int index = (indexPage != null) ? Integer.parseInt(indexPage) : 1;
            // Nếu `pageSize` có trong request, dùng giá trị đó; nếu không, mặc định là 10
            int pageSize = (pageSizeParam != null) ? Integer.parseInt(pageSizeParam) : 10;
            int count = debts.countDebts();
            int endPage = (int) Math.ceil(count * 1.0 / pageSize); // Đảm bảo làm tròn lên

            // Nếu `index` lớn hơn `endPage`, đưa về `endPage`
            if (index > endPage) {
                index = endPage;
            }
            Map<Integer, BigDecimal> totalAmountMap = debts.getTotalAmountByDebtNoteId();
            List<DebtNote> listDebts = debts.viewAllDebt(index, pageSize);
            List<String> listCustomer = customers.getAllCustomerNames();
            request.setAttribute("totalAmountMap", totalAmountMap);
            request.setAttribute("listName", listCustomer);
            request.setAttribute("list", listDebts);
            request.setAttribute("endPage", endPage);
            request.setAttribute("index", index);
            request.setAttribute("pageSize", pageSize); // Đảm bảo pageSize luôn được lưu

            // Giữ lại thông báo nếu có
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
            String idStr = request.getParameter("id");
            Integer sessionId = (Integer) request.getSession().getAttribute("id");

            int id;
            if (sessionId != null) {
                id = sessionId;
            } else if (idStr != null && !idStr.trim().isEmpty()) {
                id = Integer.parseInt(idStr);
            } else {
                request.getSession().setAttribute("Notification", "Debt ID is required.");
                response.sendRedirect("Debts?service=debtHistory");
                return;
            }

            String name = request.getParameter("name");
            String sessionName = (String) request.getSession().getAttribute("name");
            if (sessionName != null) {
                name = sessionName;
            }

            String phone = request.getParameter("phone");
            String sessionPhone = (String) request.getSession().getAttribute("phone");
            if (sessionPhone != null) {
                phone = sessionPhone;
            }

            String address = request.getParameter("address");
            String sessionAddress = (String) request.getSession().getAttribute("address");
            if (sessionAddress != null) {
                address = sessionAddress;
            }

            List<DebtNote> list = debts.getDebtById(id);
            request.setAttribute("list", list);
            request.setAttribute("name", name);
            request.setAttribute("phone", phone);
            request.setAttribute("address", address);

            request.getSession().removeAttribute("id");
            request.getSession().removeAttribute("name");
            request.getSession().removeAttribute("phone");
            request.getSession().removeAttribute("address");

            String notification = (String) request.getSession().getAttribute("Notification");
            if (notification != null) {
                request.setAttribute("Notification", notification);
            }

            request.getRequestDispatcher("views/debtHistory/debtHistory.jsp").forward(request, response);
        }

        if (service.equals("addDebtInCustomers")) {

            String type = request.getParameter("type");
            if (type == null || type.trim().isEmpty()) {
                request.getSession().setAttribute("Notification", "Type is required.");
                response.sendRedirect("Customers?service=customers");
                return; // Nếu type rỗng, không tiếp tục xử lý
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
                return; // Nếu created_at rỗng, không tiếp tục xử lý
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                createdAt = LocalDateTime.parse(createdAtStr, formatter);
            } catch (DateTimeParseException e) {
                request.getSession().setAttribute("Notification", "Invalid date format. Expected format: yyyy-MM-dd'T'HH:mm.");
                return; // Nếu không thể chuyển đổi định dạng ngày, không tiếp tục
            }

            LocalDateTime updatedAt = LocalDateTime.now();
            String description = request.getParameter("description");
            String createdBy = request.getParameter("createdBy");
            String status = request.getParameter("status");

            // Kiểm tra xem khách hàng đã có nợ hay chưa
            BigDecimal totalDebtAmount = BigDecimal.ZERO;
            // Danh sách bản ghi đã cập nhât
            List<DebtNote> existingDebts = debts.getComponent(customer_id);
            System.out.println("Existing debts size: " + existingDebts.size());
            for (DebtNote debt : existingDebts) {
                System.out.println("Debt ID: " + debt.getDebt_note_id() + ", Amount: " + debt.getAmount());
            }

            if (!existingDebts.isEmpty()) { // Kiểm tra danh sách không rỗng
                DebtNote debtToUpdate = existingDebts.get(0);  // Lấy phần tử đầu tiên trong danh sách
                int existingId = debtToUpdate.getDebt_note_id();
                debtToUpdate.setId(existingId);
                debtToUpdate.setType(type);
                debtToUpdate.setAmount(amount);
                debtToUpdate.setImage(imageFileName);
                debtToUpdate.setDescription(description);
                debtToUpdate.setCustomer_id(customer_id);
                debtToUpdate.setCreatedBy(createdBy);
                debtToUpdate.setCreatedAt(createdAt);
                debtToUpdate.setStatus(status);

                // Gọi phương thức cập nhật
                boolean isUpdated = debts.updateDebtInCustomer(debtToUpdate);
                if (isUpdated) {
                    request.getSession().setAttribute("Notification", "Debt added successfully.");
                    totalDebtAmount = totalDebtAmount.add(amount);
                    customerDAO.editCustomerBalance(totalDebtAmount, customer_id);
                } else {
                    request.getSession().setAttribute("Notification", "Debt added failed.");

                }
                // Cộng lại khoản nợ mới vào tổng nợ

            } else {
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
                // Cộng khoản nợ mới vào tổng nợ

            }
// Chỉ chuyển hướng sau khi hoàn tất cập nhật
            request.getSession().setAttribute("Notification", request.getAttribute("Notification"));
            response.sendRedirect("Customers?service=customers");

        }

        if (service.equals("addDebt")) {
            try {
                String type = request.getParameter("type");
                if (type == null || type.trim().isEmpty()) {
                    request.setAttribute("Notification", "Type is required.");
                    response.sendRedirect("Debts?service=debts");
                    return; // Không hợp lệ nếu type rỗng
                }

                // Kiểm tra amount
                String amountStr = request.getParameter("amount");
                if (amountStr == null || amountStr.trim().isEmpty()) {
                    request.setAttribute("Notification", "Amount is required.");
                    response.sendRedirect("Debts?service=debts");
                    return; // Không hợp lệ nếu amount rỗng
                }

                BigDecimal amount = null;
                amount = new BigDecimal(amountStr);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    request.setAttribute("Notification", "Amount must be greater than zero.");
                    response.sendRedirect("Debts?service=debts");
                    return; // Không hợp lệ nếu amount <= 0
                }
                if ("-".equals(type) && amount.compareTo(BigDecimal.ZERO) > 0) {
                    amount = amount.negate();
                }
                // Kiểm tra file image (image upload)
                Part file = request.getPart("image");
                String imageFileName = null;
                if (file != null && file.getSize() > 0) {
                    String fileType = file.getContentType();
                    List<String> ALLOWED_MIME_TYPES = List.of("image/jpeg", "image/png", "image/gif"); // Định nghĩa các loại mime cho phép
                    if (!ALLOWED_MIME_TYPES.contains(fileType)) {
                        request.setAttribute("Notification", "Invalid file type! Only JPG, PNG, and GIF are allowed.");
                        response.sendRedirect("Debts?service=debts");
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
                        response.sendRedirect("Debts?service=debts");
                        return;
                    }
                } else {
                    imageFileName = null;
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
                        response.sendRedirect("Debts?service=debts");
                        return; // Lỗi khi chuyển đổi ngày
                    }
                } else {
                    // Nếu không có giá trị, trả về thông báo lỗi
                    request.setAttribute("Notification", "Created date is required.");
                    response.sendRedirect("Debts?service=debts");
                    return; // Ngừng xử lý nếu không có ngày
                }

                LocalDateTime updatedAt = LocalDateTime.now();  // Current timestamp
                String debtorName = request.getParameter("debtorName");
                String debtorAddress = request.getParameter("debtorAddress");
                String debtorPhone = request.getParameter("debtorPhone");

                String debtorInfoJson = null;
                if (debtorName != null || debtorAddress != null || debtorPhone != null) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    ObjectNode debtorNode = objectMapper.createObjectNode();

                    if (debtorName != null) {
                        debtorNode.put("name", debtorName);
                    }
                    if (debtorAddress != null) {
                        debtorNode.put("address", debtorAddress);
                    }
                    if (debtorPhone != null) {
                        debtorNode.put("phone", debtorPhone);
                    }

                    debtorInfoJson = debtorNode.toString();
                }

                DebtNote debt = new DebtNote(0, type, amount, imageFileName, description, debtorName, debtorAddress, debtorPhone, createdAt, updatedAt, createdBy, status);
                boolean insert = debts.insertAndUpdateDebt(debt);
                if (insert) {
                    request.setAttribute("Notification", "Debt added successfully.");
                } else {
                    request.setAttribute("Notification", "Debt added failed.");
                }

                response.sendRedirect("Debts?service=debts");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("Notification", "Error occurred while adding the debt.");
                response.sendRedirect("Debts?service=debts");
            }
        }

        if (service.equals("updateDebt")) {
            String customerIdStr = request.getParameter("customer_id");
            int customer_id = Integer.parseInt(customerIdStr);
            int id = Integer.parseInt(request.getParameter("id"));
            request.getSession().setAttribute("id", id);
            String type = request.getParameter("type");
            String amountStr = request.getParameter("amount");
            if (amountStr == null || amountStr.trim().isEmpty()) {
                request.getSession().setAttribute("Notification", "Amount is required.");
                response.sendRedirect("Debts?service=debtHistory");
                return; // Không hợp lệ nếu amount rỗng
            }
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            request.getSession().setAttribute("name", name);
            request.getSession().setAttribute("phone", phone);
            request.getSession().setAttribute("address", address);
            BigDecimal amount = null;
            amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                request.getSession().setAttribute("Notification", "Amount must be greater than zero.");
                response.sendRedirect("Debts?service=debtHistory");
                return; // Không hợp lệ nếu amount <= 0
            }

            BigDecimal totalDebtAmount = BigDecimal.ZERO;
            Part file = request.getPart("image");
            String imageFileName = null;
            if (file != null && file.getSize() > 0) {
                String fileType = file.getContentType();
                List<String> ALLOWED_MIME_TYPES = List.of("image/jpeg", "image/png", "image/gif"); // Định nghĩa các loại mime cho phép
                if (!ALLOWED_MIME_TYPES.contains(fileType)) {
                    request.getSession().setAttribute("Notification", "Invalid file type! Only JPG, PNG, and GIF are allowed.");
                    response.sendRedirect("Debts?service=debtHistory");
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
                    response.sendRedirect("Debts?service=debtHistory");
                    return;
                }
            } else {
                imageFileName = null;
            }

            String description = request.getParameter("description");
            String createdBy = request.getParameter("createdBy");
            String status = request.getParameter("status");
            String createdAtStr = request.getParameter("created_at");
            LocalDateTime createdAt = null;
            if (createdAtStr == null || createdAtStr.trim().isEmpty()) {
                request.getSession().setAttribute("Notification", "Created date is required.");
                response.sendRedirect("Debts?service=debtHistory");
                return; // Ngừng xử lý nếu không có ngày
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd['T'][' ']HH:mm");
                createdAt = LocalDateTime.parse(createdAtStr, formatter); // LỖI NẾU createdAtStr = null
            } catch (DateTimeParseException e) {
                request.getSession().setAttribute("Notification", "Invalid date format. Expected format: yyyy-MM-dd'T'HH:mm.");
                response.sendRedirect("Debts?service=debtHistory");
                return; // Dừng xử lý nếu lỗi định dạng ngày
            }

            LocalDateTime updatedAt = LocalDateTime.now();

            String debtorName = request.getParameter("debtorName");
            String debtorAddress = request.getParameter("debtorAddress");
            String debtorPhone = request.getParameter("debtorPhone");

            String debtorInfoJson = null;
            if (debtorName != null || debtorAddress != null || debtorPhone != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode debtorNode = objectMapper.createObjectNode();

                if (debtorName != null) {
                    debtorNode.put("name", debtorName);
                }
                if (debtorAddress != null) {
                    debtorNode.put("address", debtorAddress);
                }
                if (debtorPhone != null) {
                    debtorNode.put("phone", debtorPhone);
                }

                debtorInfoJson = debtorNode.toString();
            }
            if ("-".equals(type) && amount.compareTo(BigDecimal.ZERO) > 0) {
                amount = amount.negate();
            }
            totalDebtAmount = totalDebtAmount.add(amount);

            DebtNote debt = new DebtNote(id, type, amount, imageFileName, description, debtorName, debtorAddress, debtorPhone, createdAt, updatedAt, createdBy, status);
            boolean update = debts.updateDebt(debt);
            if (update) {
                customerDAO.editCustomerBalance(totalDebtAmount, customer_id);
                request.getSession().setAttribute("Notification", "Debt updated successfully.");
            } else {
                request.getSession().setAttribute("Notification", "Failed to update debt.");
            }
            response.sendRedirect("Debts?service=debtHistory");
        }

        if (service.equals("deleteDebtCustomers")) {
            String[] selectedDebts = request.getParameterValues("id");

            if (selectedDebts != null && selectedDebts.length > 0) {
                for (String id : selectedDebts) {
                    int debtId = Integer.parseInt(id);
                    debts.deleteDebt(debtId);
                }

            }

            response.sendRedirect("Customers");
        }
        if (service.equals("deleteDebt")) {
            String[] selectedDebts = request.getParameterValues("id");

            if (selectedDebts != null && selectedDebts.length > 0) {
                for (String id : selectedDebts) {
                    int debtId = Integer.parseInt(id);
                    // Xóa nợ
                    debts.deleteDebt(debtId);
                }

            }

            response.sendRedirect("Debts");
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
