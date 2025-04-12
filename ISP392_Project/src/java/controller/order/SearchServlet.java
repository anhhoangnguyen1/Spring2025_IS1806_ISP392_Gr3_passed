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
import java.util.Arrays;
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
        System.out.println("OrderType: " + orderType); // để xác nhận

        System.out.println("Search Request - Keyword: " + keyword + ", OrderType: " + orderType + ", StoreID: " + storeID);

        if (keyword != null && !keyword.trim().isEmpty()) {
            // Thêm storeID vào tìm kiếm sản phẩm để lọc theo cửa hàng
            List<Products> products = productsDAO.INSTANCE.searchProductsByNameO(keyword, storeID);
            // Hoặc nếu bạn muốn lọc theo cửa hàng:
            // List<Products> products = productsDAO.INSTANCE.searchProductsByNameAndStore(keyword, storeID);

            System.out.println("Từ khóa: " + keyword);
            System.out.println("Số sản phẩm tìm thấy: " + products.size());

            if (products.isEmpty()) {
                out.println("<p>Can not find product.</p>");
            } else {
                // Bọc toàn bộ danh sách trong 1 container lớn

                for (Products product : products) {
                    String escapedName = product.getName().replace("'", "\\'").replace("\"", "\\\"");

                    try {
                        // Lấy danh sách unitSizes từ bảng ProductUnits
                        List<Integer> unitSizes = productsDAO.INSTANCE.getProductUnitsByProductID(product.getProductId());
                        if (unitSizes == null || unitSizes.isEmpty()) {
                            unitSizes = Arrays.asList(1); // mặc định 1kg
                        }
                        System.out.println("unitSizes for Product " + product.getName() + ": " + unitSizes);

                        // Lấy danh sách zones từ bảng ProductZones
                        List<String> zones = productsDAO.INSTANCE.getZonesByProductID(product.getProductId());

                        // Chuyển danh sách unitSizes thành chuỗi JavaScript Array
                        StringBuilder unitSizesStr = new StringBuilder("[");
                        System.out.println("Product: " + product.getName());

                        for (int i = 0; i < unitSizes.size(); i++) {
                            unitSizesStr.append(unitSizes.get(i));
                            if (i < unitSizes.size() - 1) {
                                unitSizesStr.append(",");
                            }
                        }
                        unitSizesStr.append("]");
                        System.out.println("unitSizesStr: " + unitSizesStr.toString());

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
                            out.println("<div class='product-item' onclick=\"addProductToOrder('"
                                    + product.getProductId() + "','"
                                    + escapedName + "',"
                                    + product.getPrice() + ","
                                    + product.getQuantity() + ","
                                    + unitSizesStr.toString() + ")\">");

//                            // Container chứa nội dung
//
//                            // Hàng chứa tên, số lượng và giá
                            out.println("<h3>" + product.getName() + "</h3>");
                            out.println("<p>Quantity: " + product.getQuantity() + "</p>");

                            out.println("</div>"); // đóng div .product-item

                        } else {
                            // Code cho Import
                            // Xuất HTML với unitSizes được truyền vào addProductToOrder()
                            out.println("<div class='product-item' onclick=\"addProductToOrder("
                                    + product.getProductId() + ", '"
                                    + escapedName.replace("'", "&#39;").replace("\"", "&quot;") + "', "
                                    + product.getPrice() + ", "
                                    + product.getQuantity() + ", "
                                    + unitSizesStr.toString() + ")\">");

                            // Hàng chứa tên, số lượng và giá
                            out.println("<h3>" + product.getName() + "</h3>");
                            out.println("<p>Quantity: " + product.getQuantity() + "</p>");

                            out.println("</div>"); // đóng div .product-item
                        }
                    } catch (Exception e) {
                        // Log lỗi nếu có
                        System.err.println("Error processing product: " + product.getProductId());
                        e.printStackTrace();
                    }
                }// kết thúc for

            }
        } else {
            out.println("<p>Please enter a search keyword.</p>");
        }
    }

    // Các phương thức khác giữ nguyên
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
            response.sendRedirect("/dashboard");
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
