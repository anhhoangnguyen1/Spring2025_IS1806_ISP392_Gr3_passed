package controller;

import dal.zoneDAO;
import dal.productsDAO;
import entity.Zone;
import entity.Products;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "controllerStockCheck", urlPatterns = {"/stockCheck"})
public class controllerStockCheck extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String service = request.getParameter("service");
        zoneDAO zoneDao = new zoneDAO();

        if (service == null || service.equals("stockCheck")) {
            String indexPage = request.getParameter("index");
            String pageSizeStr = request.getParameter("pageSize");

            // Xử lý giá trị mặc định cho pageSize
            if (pageSizeStr == null) {
                pageSizeStr = (String) session.getAttribute("stockCheckPageSize");
                if (pageSizeStr == null) {
                    pageSizeStr = "5"; // Mặc định 5 nếu chưa có
                }
            } else {
                session.setAttribute("stockCheckPageSize", pageSizeStr);
            }
            int pageSize = Integer.parseInt(pageSizeStr);

            // Xử lý giá trị mặc định cho indexPage
            if (indexPage == null || indexPage.isEmpty()) {
                indexPage = "1";
            }
            int index = Integer.parseInt(indexPage);

            // Tính tổng số zone
            int count = zoneDao.countZones("", true, null); // Đếm tất cả zone (bao gồm inactive)

            // Tính toán số trang dựa vào pageSize
            int endPage = count / pageSize;
            if (count % pageSize != 0) {
                endPage++;
            }

            // Lấy danh sách zone
            List<Zone> listZones = zoneDao.searchZones("", index, pageSize, "id", "ASC", true, null);

            // Đặt các thuộc tính cho request
            request.setAttribute("totalRecords", count);
            request.setAttribute("list", listZones);
            request.setAttribute("endPage", endPage);
            request.setAttribute("index", index);
            request.setAttribute("pageSize", pageSize);

            // Chuyển hướng tới trang stockCheck.jsp
            request.getRequestDispatcher("views/zone/stockCheck.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String service = request.getParameter("service");
        zoneDAO zoneDao = new zoneDAO();
        productsDAO productsDao = new productsDAO();

        if (service.equals("performStockCheck")) {
            // Lấy thông tin người thực hiện kiểm kho từ session
            String checkedBy = (String) session.getAttribute("username");
            if (checkedBy == null) {
                session.setAttribute("Notification", "You must be logged in to perform a stock check.");
                response.sendRedirect("stockCheck?service=stockCheck");
                return;
            }

            try {
                // Lấy danh sách zone từ session (nếu có) hoặc truy vấn lại
                List<Zone> zones = (List<Zone>) session.getAttribute("zonesList");
                if (zones == null) {
                    zones = zoneDao.searchZones("", 1, 5, "id", "ASC", true, null);
                    session.setAttribute("zonesList", zones);
                }

                boolean success = true;
                for (Zone zone : zones) {
                    if (zone.getProductId() == null) {
                        continue; // Bỏ qua zone không có sản phẩm
                    }

                    String actualQuantityStr = request.getParameter("actualQuantity_" + zone.getId());
                    String recordedQuantityStr = request.getParameter("recordedQuantity_" + zone.getId());
                    String notes = request.getParameter("notes_" + zone.getId());
                    String productIdStr = request.getParameter("productId_" + zone.getId());

                    if (actualQuantityStr != null && !actualQuantityStr.isEmpty() && productIdStr != null && !productIdStr.isEmpty()) {
                        int actualQuantity = Integer.parseInt(actualQuantityStr);
                        int recordedQuantity = Integer.parseInt(recordedQuantityStr);
                        int productId = Integer.parseInt(productIdStr);

                        try {
                            // Thêm bản ghi kiểm kho
                            zoneDao.addStockCheck(zone.getId(), productId, recordedQuantity, actualQuantity, checkedBy, notes);

                            // Cập nhật quantity trong bảng Products
                            Products product = productsDao.getProductByIdSimple(productId);
                            if (product != null) {
                                product.setQuantity(actualQuantity);
                                boolean updateSuccess = productsDao.updateProduct(product);
                                if (!updateSuccess) {
                                    success = false;
                                }
                            } else {
                                success = false;
                            }
                        } catch (Exception e) {
                            success = false;
                            e.printStackTrace();
                        }
                    }
                }

                if (success) {
                    session.setAttribute("Notification", "Stock check completed successfully!");
                } else {
                    session.setAttribute("Notification", "Error during stock check. Some zones may not have been updated.");
                }
            } catch (Exception e) {
                session.setAttribute("Notification", "Error during stock check: " + e.getMessage());
            }
            response.sendRedirect("stockCheck?service=stockCheck");
        }
    }

    @Override
    public String getServletInfo() {
        return "Stock Check Controller";
    }
}