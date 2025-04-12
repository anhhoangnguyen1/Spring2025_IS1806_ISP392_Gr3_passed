/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import dal.DAOProduct;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import entity.Products;
import dal.productsDAO;
import dal.zoneDAO;
import entity.Zone;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author phamh
 */
@WebServlet(name = "controllerProducts", urlPatterns = {"/Products"})
@MultipartConfig
public class controllerProducts extends HttpServlet {

    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] VALID_STATUSES = {"Active", "Inactive"};
    private static final int MIN_QUANTITY = 0;
    private static final BigDecimal MIN_PRICE = BigDecimal.ZERO;

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
        productsDAO products = new productsDAO();
        zoneDAO zonesDao = new zoneDAO();

        String fullName = (String) session.getAttribute("fullName");
        String storeIDStr = (String) session.getAttribute("storeID");
        
        // Validate storeID
        if (storeIDStr == null || storeIDStr.trim().isEmpty()) {
            session.setAttribute("Notification", "Cannot find store information");
            response.sendRedirect("Products?service=products");
            return;
        }
        int storeID = Integer.parseInt(storeIDStr);

        if (service == null) {
            service = "products";
        }
        if (service.equals("products")) {
            String indexPage = request.getParameter("index");
            String pageSizeStr = request.getParameter("pageSize");
            String command = request.getParameter("command");

            // Xử lý giá trị mặc định cho command
            if (command == null || command.isEmpty()) {
                command = (String) session.getAttribute("command");
                if (command == null || command.isEmpty()) {
                    command = "id DESC";
                }
            } else {
                session.setAttribute("command", command);
            }

            // Xử lý giá trị mặc định cho pageSize
            if (pageSizeStr == null) {
                pageSizeStr = (String) session.getAttribute("pageSize");
                if (pageSizeStr == null) {
                    pageSizeStr = "5"; // Mặc định 5 nếu chưa có
                }
            } else {
                session.setAttribute("pageSize", pageSizeStr);
            }
            int pageSize = Integer.parseInt(pageSizeStr);

            // Xử lý giá trị mặc định cho indexPage
            if (indexPage == null || indexPage.isEmpty()) {
                indexPage = "1";
            }
            int index = Integer.parseInt(indexPage);

            // Tính tổng số sản phẩm
            int count = products.countProducts(null, storeID);

            // Tính toán số trang dựa vào pageSize
            int endPage = count / pageSize;
            if (count % pageSize != 0) {
                endPage++;
            }

            // Lấy danh sách sản phẩm và Zone Active
            List<Products> listProducts = products.viewAllProducts(command, index, pageSize, storeID);
            List<String> listZoneName = zonesDao.getActiveZoneNames(storeID);

            // Đặt các thuộc tính cho request
            request.setAttribute("totalProducts", count);
            request.setAttribute("zoneName", listZoneName);
            request.setAttribute("list", listProducts);
            request.setAttribute("endPage", endPage);
            request.setAttribute("index", index);
            request.setAttribute("pageSize", pageSize);

            // Kiểm tra và truyền thông báo nếu có
            String notification = (String) request.getAttribute("Notification");
            if (notification != null && !notification.isEmpty()) {
                request.setAttribute("Notification", notification);
            }

            // Chuyển hướng tới trang JSP
            request.getRequestDispatcher("views/product/products.jsp").forward(request, response);
        }

        if (service.equals("searchProducts")) {
            String name = request.getParameter("browser");
            try {
                List<Products> list = products.searchProductsByNameO(name, storeID);
                request.setAttribute("list", list);
                request.setAttribute("name", name);
                request.getRequestDispatcher("views/product/products.jsp").forward(request, response);
            } catch (NumberFormatException e) {
            }
        }
        if (service.equals("UpdateStatusServlet")) {
            String indexStr = request.getParameter("index");
            int index = Integer.parseInt(indexStr);
            int productId = Integer.parseInt(request.getParameter("productId"));
            String newStatus = request.getParameter("status");
            boolean isUpdated = products.updateStatus(productId, newStatus);

            if (isUpdated) {
                response.sendRedirect("Products?service=products&index=" + index); // Load lại trang sau khi cập nhật
            } else {
                response.getWriter().println("Error updating product status.");
            }
        }
        if (service.equals("searchProductsEditHistory")) {
            String name = request.getParameter("browser");
            try {
                List<Products> list = products.searchProductsByNameO(name, storeID);
                request.setAttribute("listHistory", list);
                request.setAttribute("name", name);
                request.getRequestDispatcher("views/product/productEditHistory.jsp").forward(request, response);
            } catch (NumberFormatException e) {
            }
        }

        if (service.equals("getProductById")) {
            String id_raw = request.getParameter("product_id");
            int id;
            try {
                id = Integer.parseInt(id_raw);
                List<Products> product = products.getProductById(id, storeID);
                request.setAttribute("list", product);
                request.getRequestDispatcher("views/product/detailProduct.jsp").forward(request, response);
            } catch (NumberFormatException e) {
            }
        }

        if (service.equals("detailProduct")) {
            int id = Integer.parseInt(request.getParameter("product_id"));
            List<Products> list = products.getProductById(id, storeID);
            request.setAttribute("product", list);
            request.getRequestDispatcher("views/product/editProduct.jsp").forward(request, response);

        }

        if (service.equals("editProduct")) {
            try {
                int productId = Integer.parseInt(request.getParameter("product_id"));
                String name = request.getParameter("name");
                Part file = request.getPart("image");
                String currentImage = request.getParameter("current_image");
                String imageFileName = (currentImage != null && !currentImage.isEmpty()) ? currentImage : "";
                int index = Integer.parseInt(request.getParameter("index"));

                // Kiểm tra xem tên sản phẩm có bị thay đổi không
                Products existingProduct = products.getProductByIdSimple(productId, storeID);
                if (existingProduct != null && !existingProduct.getName().equals(name)) {
                    session.setAttribute("Notification", "Cannot change product name!");
                    response.sendRedirect("Products?service=products");
                    return;
                }

                if (file != null && file.getSize() > 0) {
                    // Validate file size
                    if (file.getSize() > MAX_FILE_SIZE) {
                        session.setAttribute("Notification", "File size cannot exceed 5MB");
                        response.sendRedirect("Products?service=products&index=" + index);
                        return;
                    }

                    // Validate file type
                    String fileType = file.getContentType();
                    if (!ALLOWED_MIME_TYPES.contains(fileType)) {
                        session.setAttribute("Notification", "Only image files (JPG, PNG, GIF) are allowed");
                        response.sendRedirect("Products?service=products&index=" + index);
                        return;
                    }

                    // 2. Lấy đường dẫn upload từ context (tương đối)
                    String uploadDirectory = getServletContext().getRealPath("/views/product/images");
                    if (uploadDirectory == null) {
                        // Fallback nếu không lấy được realPath
                        uploadDirectory = "C:\\Users\\phamh\\OneDrive\\Desktop\\gitest3\\ISP392_Project\\web\\views\\product\\images";
                    }

                    // 3. Tạo thư mục nếu chưa tồn tại
                    File uploadDir = new File(uploadDirectory);
                    if (!uploadDir.exists()) {
                        if (!uploadDir.mkdirs()) {
                            session.setAttribute("Notification", "Cannot create image directory");
                            response.sendRedirect("Products?service=products&index=" + index);
                            return;
                        }
                    }

                    // 4. Tạo tên file duy nhất
                    String originalFileName = Paths.get(getSubmittedFileName(file)).getFileName().toString();
                    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                    String uniqueFileName = "product_" + System.currentTimeMillis() + "_" + new Random().nextInt(1000) + fileExtension;
                    imageFileName = uniqueFileName;

                    // 5. Lưu file
                    Path destination = Paths.get(uploadDirectory, imageFileName);
                    try (InputStream is = file.getInputStream()) {
                        Files.copy(is, destination, StandardCopyOption.REPLACE_EXISTING);

                        // 6. Xóa ảnh cũ nếu có (trừ khi đó là ảnh mặc định)
                        if (!currentImage.isEmpty() && !currentImage.equals("default.jpg")) {
                            Path oldImagePath = Paths.get(uploadDirectory, currentImage);
                            if (Files.exists(oldImagePath)) {
                                Files.delete(oldImagePath);
                            }
                        }
                    } catch (IOException e) {
                        session.setAttribute("Notification", "Error saving image: " + e.getMessage());
                        response.sendRedirect("Products?service=products&index=" + index);
                        return;
                    }
                }

                BigDecimal price = new BigDecimal(request.getParameter("price"));
                if (price.compareTo(MIN_PRICE) <= 0) {
                    session.setAttribute("Notification", "Price must be greater than 0");
                    response.sendRedirect("Products?service=products&index=" + index);
                    return;
                }
                String quantityStr = request.getParameter("quantity");
                int quantity = Integer.parseInt(quantityStr);
                if (quantity < MIN_QUANTITY) {
                    session.setAttribute("Notification", "Quantity cannot be negative");
                    response.sendRedirect("Products?service=products&index=" + index);
                    return;
                }
                String createBy = request.getParameter("createBy");
                String deletedBy = request.getParameter("deletedBy");
                String description = request.getParameter("description");
                if (description == null || description.trim().isEmpty()) {
                    session.setAttribute("Notification", "Description cannot be empty");
                    response.sendRedirect("Products?service=products&index=" + index);
                    return;
                }
                String status = request.getParameter("status");
                if (!Arrays.asList(VALID_STATUSES).contains(status)) {
                    session.setAttribute("Notification", "Invalid status");
                    response.sendRedirect("Products?service=products&index=" + index);
                    return;
                }

                Date deletedAt = new java.sql.Date(System.currentTimeMillis());
                Date updatedAt = new java.sql.Date(System.currentTimeMillis());
                boolean isDelete = false;
                String createdAtStr = request.getParameter("createdAt");
                java.sql.Date createdAt = null;

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
                    java.util.Date utilDate = sdf.parse(createdAtStr);        
                    createdAt = new java.sql.Date(utilDate.getTime());         
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String[] zoneNames = request.getParameterValues("zoneName");

                Products product = new Products(productId, name, imageFileName, price, quantity, description, createdAt, createBy, deletedAt, deletedBy, isDelete, updatedAt, status);
                List<Zone> zones = new ArrayList<>();
                for (String zoneName : zoneNames) {
                    if (zoneName != null && !zoneName.trim().isEmpty()) {
                        Zone zone = new Zone();
                        zone.setName(zoneName);
                        zones.add(zone);
                    }
                }
//                boolean success = products.editProduct(product, zones);

                // Truyền fullName làm updatedBy
                boolean success = products.editProduct(product, zones, fullName, storeID);

                if (success) {
                    session.setAttribute("Notification", "Product updated successfully.");
                } else {
                    session.setAttribute("Notification", "Error updating product. Please try again.");
                }
                response.sendRedirect("Products?service=products&index=" + index);
            } catch (NumberFormatException e) {
                session.setAttribute("Notification", "Invalid number format.");
                response.sendRedirect("Products");
            } catch (NullPointerException e) {
                session.setAttribute("Notification", "Missing required parameters.");
                response.sendRedirect("Products");
            }
        }
        if (service.equals("addProduct")) {
            String name = request.getParameter("name");
            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("Notification", "Product name cannot be empty");
                request.getRequestDispatcher("Products?service=products").forward(request, response);
                return;
            }

            if (products.isProductNameExists(name, storeID)) {
                request.setAttribute("Notification", "Product name already exists");
                request.getRequestDispatcher("Products?service=products").forward(request, response);
                return;
            }

            Part file = request.getPart("image");
            String imageFileName = null;

            if (file != null && file.getSize() > 0) {
                // Validate file size
                if (file.getSize() > MAX_FILE_SIZE) {
                    request.setAttribute("Notification", "File size cannot exceed 5MB");
                    request.getRequestDispatcher("Products?service=products").forward(request, response);
                    return;
                }

                // Validate file type
                String fileType = file.getContentType();
                if (!ALLOWED_MIME_TYPES.contains(fileType)) {
                    request.setAttribute("Notification", "Only image files (JPG, PNG, GIF) are allowed");
                    request.getRequestDispatcher("Products?service=products").forward(request, response);
                    return;
                }

                // 2. Sử dụng đường dẫn tương đối trong webapp thay vì đường dẫn tuyệt đối
                String uploadDirectory = getServletContext().getRealPath("/views/product/images");

                // 3. Tạo tên file duy nhất để tránh trùng lặp
                String originalFileName = getSubmittedFileName(file);
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                imageFileName = "img_" + System.currentTimeMillis() + fileExtension;

                // 4. Tạo thư mục nếu chưa tồn tại
                File uploadDir = new File(uploadDirectory);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // 5. Lưu file
                try (InputStream is = file.getInputStream(); FileOutputStream fos = new FileOutputStream(uploadDirectory + File.separator + imageFileName)) {

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }

                    // 6. Đảm bảo dữ liệu được ghi hoàn toàn
                    fos.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("Notification", "Error uploading image: " + e.getMessage());
                    request.getRequestDispatcher("Products?service=products").forward(request, response);
                    return;
                }
            }

            String priceRaw = request.getParameter("price");
            BigDecimal price;
            try {
                price = new BigDecimal(priceRaw);
                if (price.compareTo(MIN_PRICE) <= 0) {
                    request.setAttribute("Notification", "Price must be greater than 0");
                    request.getRequestDispatcher("Products?service=products").forward(request, response);
                    return;
                }
            } catch (NumberFormatException | NullPointerException e) {
                request.setAttribute("Notification", "Invalid price");
                request.getRequestDispatcher("Products?service=products").forward(request, response);
                return;
            }

            int quantity;
            try {
                String quantityRaw = request.getParameter("quantity");
                quantity = (quantityRaw != null && !quantityRaw.isEmpty()) ? Integer.parseInt(quantityRaw) : 0;
                if (quantity < MIN_QUANTITY) {
                    request.setAttribute("Notification", "Quantity cannot be negative");
                    request.getRequestDispatcher("Products?service=products").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("Notification", "Invalid quantity");
                request.getRequestDispatcher("Products?service=products").forward(request, response);
                return;
            }

            String description = request.getParameter("description");
            if (description == null || description.trim().isEmpty()) {
                request.setAttribute("Notification", "Description cannot be empty");
                request.getRequestDispatcher("Products?service=products").forward(request, response);
                return;
            }


            String[] zoneNames = request.getParameterValues("zoneName");

            String createdBy = fullName; // Sử dụng fullName từ session
            String deletedBy = null; // Không cần deletedBy khi thêm mới
            String status = request.getParameter("status") != null ? request.getParameter("status") : "Active";
            String isDeleteRaw = request.getParameter("isDelete");
            boolean isDelete = Boolean.parseBoolean(isDeleteRaw);
            Date createdAt = new java.sql.Date(System.currentTimeMillis());
            Date updatedAt = null; // Chưa cập nhật khi mới thêm
            Date deletedAt = null; // Chưa xóa khi mới thêm

            Products product = new Products(0, name, imageFileName, price, quantity, description, createdAt, createdBy, deletedAt, deletedBy, isDelete, updatedAt, status);

            // Lấy danh sách Zone Active để kiểm tra
            List<String> activeZoneNames = zonesDao.getActiveZoneNames(storeID);
            List<Zone> zones = new ArrayList<>();

            for (String zoneName : zoneNames) {
                if (zoneName != null && !zoneName.trim().isEmpty() && activeZoneNames.contains(zoneName)) {
                    Zone zone = new Zone();
                    zone.setName(zoneName);
                    zones.add(zone);
                }
            }

            boolean success = products.insertProduct(product, zones, fullName, storeID);

            if (success) {
                // Lấy ID của sản phẩm vừa thêm
                int productId = products.getLastInsertedProductId();
                // Thêm quy cách đóng gói mặc định
                products.insertDefaultProductUnits(productId);
                request.setAttribute("Notification", "Product added successfully!");
            } else {
                request.setAttribute("Notification", "Failed to add product!");
            }

            request.getRequestDispatcher("Products?service=products").forward(request, response);
        }

        if (service.equals("deleteProduct")) {
            String[] selectedProducts = request.getParameterValues("id");

            if (selectedProducts != null && selectedProducts.length > 0) {
                try {
                    for (String id : selectedProducts) {
                        int product_id = Integer.parseInt(id);
                        // Xóa sản phẩm
                        products.deleteProduct(product_id);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            response.sendRedirect("Products");
        }

        if (service.equals("updateProduct")) {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String image = request.getParameter("image");
            BigDecimal price = new BigDecimal(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String description = request.getParameter("description");
            String status = request.getParameter("status");
            boolean isDelete = Boolean.parseBoolean(request.getParameter("isDelete"));
            String updatedBy = fullName;
            Date updatedAt = new java.sql.Date(System.currentTimeMillis());

            Products product = new Products(id, name, image, price, quantity, description, null, null, null, null, isDelete, updatedAt, status, null);

            // Lấy giá cũ của sản phẩm
            Products oldProduct = products.getProductByIdSimple(id, storeID);
            if (oldProduct != null && !oldProduct.getPrice().equals(price)) {
                // Lấy userId từ session
                int userId = Integer.parseInt((String) session.getAttribute("userID"));
                // Nếu giá thay đổi, ghi log vào lịch sử
                DAOProduct.INSTANCE.logPriceChange(id, price.doubleValue(), "sell", userId, null, fullName);
            }

            boolean success = products.updateProduct(product);
            if (success) {
                request.setAttribute("Notification", "Product updated successfully!");
            } else {
                request.setAttribute("Notification", "Failed to update product!");
            }
            request.getRequestDispatcher("Products?service=products").forward(request, response);
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
