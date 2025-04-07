/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.order;

import dal.customerDAO;
import dal.productsDAO;
import entity.Customers;
import entity.Products;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author THC
 */
@WebServlet(name = "SearchServlet", urlPatterns = {"/SearchServlet"})
public class SearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (role != null && role.equals("admin")) {
            response.sendRedirect("/dashboard.jsp");
            return;
        }

        String storeIDStr = (String) session.getAttribute("storeID");
        int storeID = Integer.parseInt(storeIDStr);

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String keyword = request.getParameter("searchProduct");

        String orderType = request.getParameter("orderType"); // Export hoặc Import

        int userId = (int) session.getAttribute("userID");

        if (keyword != null && !keyword.trim().isEmpty()) {
            List<Products> products = productsDAO.INSTANCE.searchProductsByNameO(keyword);

            if (products.isEmpty()) {
                out.println("<p>Can not find product.</p>");
            } else {
                // Bạn có thể bọc toàn bộ danh sách trong 1 container lớn
                out.println("<div class='search-suggestions'>");

                for (Products product : products) {

                    // Lấy danh sách unitSizes từ bảng ProductUnits (đã trả về List<Integer>)
                    List<Integer> unitSizes = productsDAO.INSTANCE.getProductUnitsByProductID(product.getProductId());

                    // Lấy danh sách zones từ bảng ProductZones
                    List<String> zones = productsDAO.INSTANCE.getZonesByProductID(product.getProductId());
                    // Chuyển danh sách unitSizes thành chuỗi JavaScript Array
                    StringBuilder unitSizesStr = new StringBuilder("[");
                    for (int i = 0; i < unitSizes.size(); i++) {
                        unitSizesStr.append(unitSizes.get(i));
                        if (i < unitSizes.size() - 1) {
                            unitSizesStr.append(",");
                        }
                    }
                    unitSizesStr.append("]");
                    // Chuyển danh sách zones thành chuỗi JavaScript Array
                    StringBuilder zonesStr = new StringBuilder("[");
                    for (int i = 0; i < zones.size(); i++) {
                        zonesStr.append("\"").append(zones.get(i)).append("\"");
                        if (i < zones.size() - 1) {
                            zonesStr.append(",");
                        }
                    }
                    zonesStr.append("]");

                    if ("Export".equalsIgnoreCase(orderType)) {
                        // Xuất HTML với unitSizes được truyền vào addProductToOrder()
                        out.println("<div class='product-item' onclick=\"addProductToOrder('"
                                + product.getProductId() + "','"
                                + product.getName() + "',"
                                + unitSizesStr.toString() + ")\">");

                        // Ảnh sản phẩm
                        out.println("<div class='product-image'>"
                                + "<img src='" + product.getImage() + "' alt='Product Image' />"
                                + "</div>");

                        // Container chứa nội dung
                        out.println("<div class='product-content'>");

                        // Hàng chứa tên, số lượng và giá
                        out.println("<div class='product-info'>");
                        out.println("<h3 class='product-name'>" + product.getName() + "</h3>");
                        out.println("<p class='product-quantity'>Số lượng: " + product.getQuantity() + "</p>");
                        out.println("<p class='product-price'>Giá Bán: " + formatter.format(product.getPrice()) + "</p>");
                        out.println("</div>");

                        // Mô tả sản phẩm
                        out.println("<p class='product-description'>"
                                + (product.getDescription() != null ? product.getDescription() : "")
                                + "</p>");
                        // Hiển thị danh sách zones
                        out.println("<p class='product-zones'>Kho: " + String.join(", ", zones) + "</p>");

                        out.println("</div>"); // đóng div .product-content
                        out.println("</div>"); // đóng div .product-item

                    } else {

                        // Xuất HTML với unitSizes được truyền vào addProductToOrder()
                        out.println("<div class='product-item' onclick=\"addProductToOrder('"
                                + product.getProductId() + "','"
                                + product.getName() + "','"
                                + (BigDecimal) product.getPrice() + "','"
                                + product.getQuantity() + "',"
                                + unitSizesStr.toString() + ")\">");
                        // Ảnh sản phẩm
                        out.println("<div class='product-image'>"
                                + "<img src='" + product.getImage() + "' alt='Product Image' />"
                                + "</div>");

                        // Container chứa nội dung
                        out.println("<div class='product-content'>");

                        // Hàng chứa tên, số lượng và giá
                        out.println("<div class='product-info'>");
                        out.println("<h3 class='product-name'>" + product.getName() + "</h3>");
                        out.println("<p class='product-quantity'>Số lượng: " + product.getQuantity() + "</p>");
                        out.println("<p class='product-price'>Giá Bán: " + formatter.format(product.getPrice()) + "</p>");
                        out.println("</div>");

                        // Mô tả sản phẩm
                        out.println("<p class='product-description'>"
                                + (product.getDescription() != null ? product.getDescription() : "")
                                + "</p>");
                        // Hiển thị danh sách zones
                        out.println("<p class='product-zones'>Kho: " + String.join(", ", zones) + "</p>");

                        out.println("</div>"); // đóng div .product-content
                        out.println("</div>"); // đóng div .product-item
                    }

                }// kết thúc for

                out.println("</div>"); // đóng container lớn
            }
        }
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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (role != null && role.equals("admin")) {
            response.sendRedirect("/dashboard.jsp");
            return;
        }

        String storeIDStr = (String) session.getAttribute("storeID");
        int storeID = Integer.parseInt(storeIDStr);
        String searchValue = request.getParameter("keyword");

        if (searchValue != null && !searchValue.trim().isEmpty()) {
            List<Customers> customers = customerDAO.INSTANCE.findByNameOrPhone(searchValue, storeID);

            if (customers.isEmpty()) {
                out.println("<p>Can not find customer.</p>");
                out.println("<div class='customer-item add-new' onclick='openAddCustomerPopup()'>");
                out.println("<p>Add new customer</p>");
                out.println("</div>");
            } else {
                for (Customers customer : customers) {
                    out.println("<div class='customer-item' onclick='selectCustomer("
                            + customer.getId() + ", \""
                            + customer.getName() + "\", \""
                            + customer.getPhone() + "\", "
                            + customer.getBalance() + ")'>");
                    out.println("<h3>" + customer.getName() + "</h3>");

                    // Kiểm tra nếu role là "staff" thì ẩn 5 số ở giữa
                    if (role.equals("staff")) {
                        String phone = customer.getPhone();
                        if (phone.length() >= 10) {
                            // Ẩn 5 số giữa số điện thoại
                            phone = phone.substring(0, 3) + "xxxxx" + phone.substring(8);
                        }
                        out.println("<p>Phone: " + phone + "</p>");
                    } else {
                        // Hiển thị số điện thoại đầy đủ cho owner hoặc các role khác
                        out.println("<p>Phone: " + customer.getPhone() + "</p>");
                    }

                    out.println("</div>");
                }
            }
        }
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
