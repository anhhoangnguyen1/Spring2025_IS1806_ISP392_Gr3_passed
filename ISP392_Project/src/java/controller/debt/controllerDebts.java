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
import entity.Customers;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

/**
 *
 * @author phamh
 */
@WebServlet(name = "controllerDebts", urlPatterns = {"/Debts"})
@MultipartConfig
public class controllerDebts extends HttpServlet {

    customerDAO customerDAO = new customerDAO();
    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif");

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String service = request.getParameter("service");
        customerDAO customers = new customerDAO();
        debtDAO debts = new debtDAO();
        String storeIDStr = (String) session.getAttribute("storeID");
        int storeID = Integer.parseInt(storeIDStr);
        if (service == null) {
            service = "debts";
        }
        if (service.equals("debts")) {
            String indexPage = request.getParameter("index");
            String pageSizeParam = request.getParameter("pageSize");
            String commandParam = request.getParameter("command");
            List<String> validColumns = Arrays.asList("id", "type", "amount", "created_at", "updated_at", "created_by", "status");
            String command = (validColumns.contains(commandParam)) ? commandParam : "created_at";
            // Nếu `index` không có, mặc định là 1
            int index = (indexPage != null) ? Integer.parseInt(indexPage) : 1;
            // Nếu `pageSize` có trong request, dùng giá trị đó; nếu không, mặc định là 10
            int pageSize = (pageSizeParam != null) ? Integer.parseInt(pageSizeParam) : 10;
            int count = debts.countDebts(storeID, 0, null, null);
            int endPage = (int) Math.ceil(count * 1.0 / pageSize); // Đảm bảo làm tròn lên

            // Nếu `index` lớn hơn `endPage`, đưa về `endPage`
            if (index > endPage) {
                index = endPage;
            }

            List<DebtNote> listDebts = debts.viewAllDebt(command, index, pageSize, storeID);
            List<String> listCustomer = customers.getAllCustomerNames();
            request.setAttribute("totalDebts", count);
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
        if (service.equals("debtInCustomers")) {
            int customer_id = Integer.parseInt(request.getParameter("customer_id"));
            String indexPage = request.getParameter("index");
            int index = (indexPage != null && !indexPage.isEmpty()) ? Integer.parseInt(indexPage) : 1;

            // Lấy số lượng hiển thị trên mỗi trang (pageSize)
            String pageSizeParam = request.getParameter("pageSize");
            int pageSize = (pageSizeParam != null && !pageSizeParam.isEmpty()) ? Integer.parseInt(pageSizeParam) : 10;

            // Lấy ngày bắt đầu và ngày kết thúc từ request
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String command = null;
            if (command == null || command.isEmpty()) {
                command = "id desc"; // Giá trị mặc định
            }
            // Đếm tổng số nợ theo bộ lọc ngày
            int count = debts.countDebts(storeID, customer_id, startDate, endDate);
            int endPage = (int) Math.ceil(count * 1.0 / pageSize);

            // Kiểm tra nếu index vượt quá số trang hiện có
            if (index > endPage && endPage > 0) {
                index = endPage;
            }

            // Lọc danh sách nợ theo bộ lọc ngày
            List<DebtNote> listDebts = debts.viewAllDebtInCustomer(command, customer_id, storeID, startDate, endDate, index, pageSize);
            Customers customer = customers.getCustomerById(customer_id);

            // Gửi dữ liệu về JSP
            request.setAttribute("totalDebts", count);
            request.setAttribute("listName", customer);
            request.setAttribute("list", listDebts);
            request.setAttribute("endPage", endPage);
            request.setAttribute("index", index);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);

            request.getRequestDispatcher("views/customer/debtHistory.jsp").forward(request, response);
        }

        if (service.equals("searchDebts")) {
            String name = request.getParameter("browser");
            try {
                List<DebtNote> list = debts.searchDebts(name, storeID);
                request.setAttribute("list", list);
                request.setAttribute("name", name);
                request.getRequestDispatcher("views/debtHistory/debts.jsp").forward(request, response);
            } catch (NumberFormatException e) {
            }
        }
        if (service.equals("debtHistory")) {
            String idStr = request.getParameter("id");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");

            LocalDate startDate = null;
            LocalDate endDate = null;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            try {
                if (startDateStr != null && !startDateStr.isEmpty()) {
                    startDate = LocalDate.parse(startDateStr, formatter);
                }
                if (endDateStr != null && !endDateStr.isEmpty()) {
                    endDate = LocalDate.parse(endDateStr, formatter);
                }
            } catch (DateTimeParseException e) {
                request.getSession().setAttribute("Notification", "Invalid date format.");
                response.sendRedirect("Debts?service=debtHistory");
                return;
            }
            Integer sessionId = (Integer) request.getSession().getAttribute("customer_id");

            int id;
            if (sessionId != null) {
                id = sessionId;
            } else if (idStr != null && !idStr.trim().isEmpty()) {
                id = Integer.parseInt(idStr);
            } else {
                request.getSession().setAttribute("Notification", "Customer ID is required.");
                response.sendRedirect("Debts?service=debtHistory");
                return;
            }

            List<DebtNote> list = debts.getDebtByCustomerId(id, storeID,startDate, endDate);

            request.setAttribute("list", list);
            String notification = (String) request.getSession().getAttribute("Notification");
            if (notification != null) {
                request.setAttribute("Notification", notification);
            }
            request.getSession().setAttribute("customer_id", id);

            request.getRequestDispatcher("views/debtHistory/debtHistory.jsp").forward(request, response);
        }
        if (service.equals("addDebtInDebtDetails")) {
            String status = request.getParameter("status");
            String type;

            if (status == null || status.trim().isEmpty()) {
                request.getSession().setAttribute("Notification", "Status is required.");
                response.sendRedirect("Debts?service=debtHistory");
                return;
            }
            if ("Customer borrows debt".equalsIgnoreCase(status) || "Owner repays debt".equalsIgnoreCase(status)) {
                type = "+";
            } else if ("Customer repays debt".equalsIgnoreCase(status) || "Owner borrows debt".equalsIgnoreCase(status)) {
                type = "-";
            } else {
                request.getSession().setAttribute("Notification", "Status is required.");
                response.sendRedirect("Debts?service=debtHistory");
                return;
            }

            String amountStr = request.getParameter("amount");
            BigDecimal amount = null;
            if (amountStr == null || amountStr.trim().isEmpty()) {
                request.getSession().setAttribute("Notification", "Amount is required.");
                response.sendRedirect("Debts?service=debtHistory");
                return; // Nếu amount rỗng, không tiếp tục xử lý
            }
            amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                request.getSession().setAttribute("Notification", "Amount must be greater than zero.");
                response.sendRedirect("Debts?service=debtHistory");
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

            // Kiểm tra và chuyển đổi 'created_at'
            String createdAtStr = request.getParameter("created_at");
            LocalDateTime createdAt = null;
            if (createdAtStr == null || createdAtStr.trim().isEmpty()) {
                request.getSession().setAttribute("Notification", "Created date is required.");
                response.sendRedirect("Debts?service=debtHistory");
                return; // Nếu created_at rỗng, không tiếp tục xử lý
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                createdAt = LocalDateTime.parse(createdAtStr, formatter);
            } catch (DateTimeParseException e) {
                request.getSession().setAttribute("Notification", "Invalid date format. Expected format: yyyy-MM-dd'T'HH:mm.");
                response.sendRedirect("Debts?service=debtHistory");
                return; // Nếu không thể chuyển đổi định dạng ngày, không tiếp tục
            }

            LocalDateTime updatedAt = LocalDateTime.now();
            String description = request.getParameter("description");
            String createdBy = request.getParameter("createdBy");

            // Kiểm tra xem khách hàng đã có nợ hay chưa
            BigDecimal totalDebtAmount = BigDecimal.ZERO;

            // Nếu không có nợ cũ, thêm mới
            DebtNote newDebt = new DebtNote(0, type, amount, imageFileName, description, customer_id, createdAt, updatedAt, createdBy, status);
            boolean insert = debts.insertDebtInCustomer(newDebt, storeID);
            if (insert) {
                totalDebtAmount = totalDebtAmount.add(amount);
                customerDAO.editCustomerBalance(totalDebtAmount, customer_id);
                request.getSession().setAttribute("Notification", "Debt added successfully.");
            } else {
                request.getSession().setAttribute("Notification", "Debt added failed.");
            }
            response.sendRedirect("Debts?service=debtHistory");

        }
        if (service.equals("addDebt")) {
            String redirectUrl = "Debts?service=debts";
            try {
                // Kiểm tra và xử lý status
                String status = request.getParameter("status");
                if (status == null || status.trim().isEmpty()) {
                    request.getSession().setAttribute("Notification", "Status is required.");
                    response.sendRedirect(redirectUrl);
                    return;
                }

                String type;
                switch (status.toLowerCase()) {
                    case "customer borrows debt":
                    case "owner repays debt":
                        type = "+";
                        break;
                    case "customer repays debt":
                    case "owner borrows debt":
                        type = "-";
                        break;
                    default:
                        request.getSession().setAttribute("Notification", "Invalid status.");
                        response.sendRedirect(redirectUrl);
                        return;
                }

                // Kiểm tra và xử lý amount
                String amountStr = request.getParameter("amount");
                if (amountStr == null || amountStr.trim().isEmpty()) {
                    request.getSession().setAttribute("Notification", "Amount is required.");
                    System.out.println("---start process: amountStr is " + amountStr + "--");
                    response.sendRedirect(redirectUrl);
                    return;
                }

                BigDecimal amount;
                try {
                    amount = new BigDecimal(amountStr);
                    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                        request.getSession().setAttribute("Notification", "Amount must be greater than zero.");

                        response.sendRedirect(redirectUrl);
                        return;
                    }
                } catch (NumberFormatException e) {
                    request.getSession().setAttribute("Notification", "Invalid amount format.");
                    response.sendRedirect(redirectUrl);
                    return;
                }

                if ("-".equals(type)) {
                    amount = amount.negate();
                }

                // Xử lý upload hình ảnh
                Part file = request.getPart("image");
                String imageFileName = null;
                if (file != null && file.getSize() > 0) {
                    String fileType = file.getContentType();
                    if (!ALLOWED_MIME_TYPES.contains(fileType)) {
                        request.getSession().setAttribute("Notification", "Invalid file type! Only JPG, PNG, and GIF are allowed.");
                        response.sendRedirect(redirectUrl);
                        return;
                    }

                    imageFileName = UUID.randomUUID().toString() + "_" + getSubmittedFileName(file);
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
                        request.getSession().setAttribute("Notification", "File upload failed.");
                        response.sendRedirect(redirectUrl);
                        return;
                    }
                }

                // Xử lý created_at
                String createdAtStr = request.getParameter("created_at");
                LocalDateTime createdAt = null;
                if (createdAtStr == null || createdAtStr.trim().isEmpty()) {
                    request.getSession().setAttribute("Notification", "Created date is required.");
                    response.sendRedirect(redirectUrl);
                    return;
                }

                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                    createdAt = LocalDateTime.parse(createdAtStr, formatter);
                } catch (DateTimeParseException e) {
                    request.getSession().setAttribute("Notification", "Invalid date format. Expected format: yyyy-MM-dd'T'HH:mm.");
                    response.sendRedirect(redirectUrl);
                    return;
                }

                LocalDateTime updatedAt = LocalDateTime.now();
                String description = request.getParameter("description");
                String createdBy = request.getParameter("createdBy");

                // Xử lý khách hàng
                Integer customerId = 0;
                String phone = request.getParameter("phone");
                if (phone == null || phone.trim().isEmpty()) {
                    request.getSession().setAttribute("Notification", "Phone number is required.");
                    response.sendRedirect(redirectUrl);
                    return;
                }
                if (phone != null && !phone.trim().isEmpty()) {
                    boolean customerExists = customers.checkPhoneExists(phone, 0);

                    if (!customerExists) {
                        Customers newCustomer = new Customers();
                        newCustomer.setName(request.getParameter("name"));
                        newCustomer.setPhone(phone);
                        newCustomer.setAddress(request.getParameter("address"));
                        newCustomer.setBalance(0.00);
                        newCustomer.setCreatedBy(request.getParameter("createdBy"));
                        newCustomer.setDeleted(false);
                        newCustomer.setStatus("active");
                        customers.insertCustomer(newCustomer);
                        customerId = customers.getCustomerIdByPhone(phone);

                        if (customerId == null) {
                            request.getSession().setAttribute("Notification", "Error retrieving customer ID.");
                            response.sendRedirect(redirectUrl);
                            return;
                        }
                    } else {
                        customerId = customers.getCustomerIdByPhone(phone);
                    }
                }
                DebtNote newDebt = new DebtNote(0, type, amount, imageFileName, description, customerId, createdAt, updatedAt, createdBy, status);
                boolean insert = debts.insertDebt(newDebt, storeID);

                if (insert) {
                    customers.editCustomerBalance(amount, customerId);
                    request.getSession().setAttribute("Notification", "Debt added successfully.");
                } else {
                    request.getSession().setAttribute("Notification", "Debt added failed.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.getSession().setAttribute("Notification", "An error occurred while processing your request.");
            } finally {
                if (!response.isCommitted() && !request.getRequestURI().equals(redirectUrl)) {
                    System.out.println("----Finnaly: done ");
                    response.sendRedirect("Debts");
                }
            }
        }

        if (service.equals("addDebtInCustomers")) {
            String status = request.getParameter("status");
            String type;
            String customerIdStr = request.getParameter("customer_id");
            int customer_id = Integer.parseInt(customerIdStr);
            if (status == null || status.trim().isEmpty()) {
                request.getSession().setAttribute("Notification", "Status is required.");
                response.sendRedirect("Debts?service=debtInCustomers&customer_id=" + customer_id);
                return;
            }
            if ("Customer borrows debt".equalsIgnoreCase(status) || "Owner repays debt".equalsIgnoreCase(status)) {
                type = "+";
            } else if ("Customer repays debt".equalsIgnoreCase(status) || "Owner borrows debt".equalsIgnoreCase(status)) {
                type = "-";
            } else {
                request.getSession().setAttribute("Notification", "Status is required.");
                response.sendRedirect("Debts?service=debtInCustomers&customer_id=" + customer_id);
                return;
            }

            String amountStr = request.getParameter("amount");
            BigDecimal amount = null;
            if (amountStr == null || amountStr.trim().isEmpty()) {
                request.getSession().setAttribute("Notification", "Amount is required.");
                response.sendRedirect("Debts?service=debtInCustomers&customer_id=" + customer_id);
                return; // Nếu amount rỗng, không tiếp tục xử lý
            }
            amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                request.getSession().setAttribute("Notification", "Amount must be greater than zero.");
                response.sendRedirect("Debts?service=debtInCustomers&customer_id=" + customer_id);
                return; // Nếu amount <= 0, không tiếp tục
            }

            // Nếu type là "-", thì cần đổi dấu của amount
            if ("-".equals(type) && amount.compareTo(BigDecimal.ZERO) > 0) {
                amount = amount.negate();
            }

            // Kiểm tra phần tệp tin ảnh (image upload)
            Part file = request.getPart("image");
            String imageFileName = null;
            if (file != null && file.getSize() > 0) {
                String fileType = file.getContentType();
                List<String> ALLOWED_MIME_TYPES = List.of("image/jpeg", "image/png", "image/gif"); // Định nghĩa các loại mime cho phép
                if (!ALLOWED_MIME_TYPES.contains(fileType)) {
                    request.getSession().setAttribute("Notification", "Invalid file type! Only JPG, PNG, and GIF are allowed.");
                    response.sendRedirect("Debts?service=debtInCustomers&customer_id=" + customer_id);
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
                    response.sendRedirect("Debts?service=debtInCustomers&customer_id=" + customer_id);
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
                response.sendRedirect("Debts?service=debtInCustomers&customer_id=" + customer_id);
                return; // Nếu created_at rỗng, không tiếp tục xử lý
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                createdAt = LocalDateTime.parse(createdAtStr, formatter);
            } catch (DateTimeParseException e) {
                request.getSession().setAttribute("Notification", "Invalid date format. Expected format: yyyy-MM-dd'T'HH:mm.");
                response.sendRedirect("Debts?service=debtInCustomers&customer_id=" + customer_id);
                return; // Nếu không thể chuyển đổi định dạng ngày, không tiếp tục
            }

            LocalDateTime updatedAt = LocalDateTime.now();
            String description = request.getParameter("description");
            String createdBy = request.getParameter("createdBy");

            // Kiểm tra xem khách hàng đã có nợ hay chưa
            BigDecimal totalDebtAmount = BigDecimal.ZERO;

            // Nếu không có nợ cũ, thêm mới
            DebtNote newDebt = new DebtNote(0, type, amount, imageFileName, description, customer_id, createdAt, updatedAt, createdBy, status);
            boolean insert = debts.insertDebtInCustomer(newDebt, storeID);
            if (insert) {
                totalDebtAmount = totalDebtAmount.add(amount);
                customerDAO.editCustomerBalance(totalDebtAmount, customer_id);
                request.getSession().setAttribute("Notification", "Debt added successfully.");
            } else {
                request.getSession().setAttribute("Notification", "Debt added failed.");
            }
            response.sendRedirect("Debts?service=debtInCustomers&customer_id=" + customer_id);

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
