/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

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
import dao.zoneDAO;
import entity.Zone;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author phamh
 */
@WebServlet(name = "controllerProducts", urlPatterns = {"/Products"})
@MultipartConfig
public class controllerProducts extends HttpServlet {

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
        productsDAO products = new productsDAO();
        zoneDAO zonesDao = new zoneDAO();
        if (service == null) {
            service = "products";
        }
        if (service.equals("products")) {
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
            int count = products.countProducts();
            int endPage = count / 10;
            if (count % 10 != 0) {
                endPage++;
            }
            List<Products> listProducts = products.viewAllProducts(command, index);
            List<String> listZoneName = zonesDao.getAllZoneNames();
            request.setAttribute("zoneName", listZoneName);
            request.setAttribute("list", listProducts);
            request.setAttribute("endPage", endPage);
            request.setAttribute("index", index);
            String notification = (String) request.getAttribute("Notification");
            if (notification != null && !notification.isEmpty()) {
                request.setAttribute("Notification", notification);
            }
            request.getRequestDispatcher("views/product/products.jsp").forward(request, response);
        }
        if (service.equals("searchProducts")) {
            String name = request.getParameter("browser");
            try {
                List<Products> list = products.searchProducts(name);
                request.setAttribute("list", list);
                request.setAttribute("name", name);
                request.getRequestDispatcher("views/product/products.jsp").forward(request, response);
            } catch (NumberFormatException e) {
            }
        }
        if (service.equals("getProductById")) {
            String id_raw = request.getParameter("product_id");
            int id;
            try {
                id = Integer.parseInt(id_raw);
                List<Products> product = products.getProductById(id);
                request.setAttribute("list", product);
                request.getRequestDispatcher("views/product/detailProduct.jsp").forward(request, response);
            } catch (NumberFormatException e) {
            }
        }
        if (service.equals("detailProduct")) {
            int id = Integer.parseInt(request.getParameter("product_id"));
            List<Products> list = products.getProductById(id);
            request.setAttribute("product", list);
            request.getRequestDispatcher("views/product/editProduct.jsp").forward(request, response);

        }
        if (service.equals("editProduct")) {
            try {
                int productId = Integer.parseInt(request.getParameter("product_id"));
                String name = request.getParameter("name");
                Part file = request.getPart("image");
                String currentImage = request.getParameter("current_image");
                String imageFileName = (currentImage != null) ? currentImage : "";

                if (file != null && file.getSize() > 0) {
                    String fileType = file.getContentType();
                    if (!ALLOWED_MIME_TYPES.contains(fileType)) {
                        session.setAttribute("Notification", "Invalid file type! Only JPG, PNG, and GIF are allowed.");
                        response.sendRedirect("Products");
                        return;
                    }

                    imageFileName = getSubmittedFileName(file);
                    String uploadDirectory = "C:\\Users\\phamh\\OneDrive\\Desktop\\gitest1\\ISP392_Project\\web\\views\\product\\images";
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
                    }
                }

                BigDecimal price = new BigDecimal(request.getParameter("price"));
                if (price.compareTo(BigDecimal.ZERO) <= 0) {
                    session.setAttribute("Notification", "Price must be greater than 0.");
                    response.sendRedirect("Products");
                    return;
                }
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                if (quantity <= 0) {
                    session.setAttribute("Notification", "Weight must be greater than 0.");
                    response.sendRedirect("Products");
                    return;
                }
                String createBy = request.getParameter("createBy");
                String deletedBy = request.getParameter("deletedBy");
                String description = request.getParameter("description");
                String status = request.getParameter("status");
 
                Date deletedAt = new java.sql.Date(System.currentTimeMillis());
                Date updatedAt = new java.sql.Date(System.currentTimeMillis());
                boolean isDelete = false;
                Date createdAt = new java.sql.Date(System.currentTimeMillis());
                String[] zoneNames = request.getParameterValues("zoneName");
                Products product = new Products(productId, name, imageFileName, price, quantity, description, createdAt, createBy, deletedAt, deletedBy, isDelete, updatedAt, status);
                List<Zone> zones = new ArrayList<>();
                if (zoneNames != null && zoneNames.length > 0) {
                    for (String zoneName : zoneNames) {
                        if (zoneName != null && !zoneName.trim().isEmpty()) {
                            Zone zone = new Zone();
                            zone.setName(zoneName);
                            zones.add(zone);
                        }
                    }
                }
                boolean success = products.editProduct(product, zones);

                if (success) {
                    session.setAttribute("Notification", "Product updated successfully.");
                } else {
                    session.setAttribute("Notification", "Error updating product. Please try again.");
                }
                response.sendRedirect("Products");
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

            Part file = request.getPart("image");

            String imageFileName = null;

            if (file != null && file.getSize() > 0) {
                String fileType = file.getContentType();
                if (!ALLOWED_MIME_TYPES.contains(fileType)) {
                    request.setAttribute("Notification", "Invalid file type! Only JPG, PNG, and GIF are allowed.");
                    request.getRequestDispatcher("Products?service=products").forward(request, response);
                    return;
                }
                imageFileName = getSubmittedFileName(file);

                String uploadDirectory = "C:\\Users\\phamh\\OneDrive\\Desktop\\gitest1\\ISP392_Project\\web\\views\\product\\images";
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
                }
            }

            String priceRaw = request.getParameter("price");
            BigDecimal price = new BigDecimal(priceRaw);
            price = new BigDecimal(priceRaw);
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                request.setAttribute("Notification", "Price must be greater than 0.");
                request.getRequestDispatcher("Products?service=products").forward(request, response);
                return;
            }

            int quantity = Integer.parseInt(request.getParameter("quantity"));
            if (quantity <= 0) {
                request.setAttribute("Notification", "Quantity must be greater than 0.");
                request.getRequestDispatcher("Products?service=products").forward(request, response);
                return;
            }
            String description = request.getParameter("description");
            String createdBy = request.getParameter("createdBy");
            String deletedBy = request.getParameter("deletedBy");
            String status = request.getParameter("status");
            String isDeleteRaw = request.getParameter("isDelete");
            String[] zoneNames = request.getParameterValues("zoneName"); // Lấy nhiều zone

            boolean isDelete = Boolean.parseBoolean(isDeleteRaw);
            Date deletedAt = new java.sql.Date(System.currentTimeMillis());
            Date updatedAt = new java.sql.Date(System.currentTimeMillis());
            Date createdAt = new java.sql.Date(System.currentTimeMillis());
            Products product = new Products(0, name, imageFileName, price, quantity, description, createdAt, createdBy, deletedAt, deletedBy, isDelete, updatedAt, status);
            List<Zone> zones = new ArrayList<>();
            if (zoneNames != null) {
                for (String zoneName : zoneNames) {
                    Zone zone = new Zone();
                    zone.setName(zoneName);
                    zones.add(zone);
                }
            }
            boolean success = products.insertProduct(product, zones);

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
