/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.imports;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import dal.OrderDetailsDAO;
import dal.OrdersDAO;
import dal.customerDAO;
import dal.productsDAO;
import dal.userDAO;
import entity.Customers;
import entity.OrderDetails;
import entity.Orders;
import entity.Products;
import entity.Users;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
@WebServlet(name = "importProduct", urlPatterns = {"/Imports"})
public class importProduct extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(importProduct.class.getName());
    private productsDAO daoProduct;
    private OrdersDAO daoOrder;
    private OrderDetailsDAO daoOrderDetails;
    private customerDAO daoCustomer;
    private userDAO daoAccount;

    // Định nghĩa hằng số
    private static final Integer DEFAULT_CUSTOMER_ID = 1; // ID cho khách hàng mặc định
    private static final Integer GUEST_CUSTOMER_ID = null; // Khách lẻ không có ID

    @Override
    public void init() throws ServletException {
        super.init();
        daoProduct = new productsDAO();
        daoOrder = new OrdersDAO();
        daoOrderDetails = new OrderDetailsDAO();
        daoCustomer = new customerDAO();
        daoAccount = new userDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String action = req.getParameter("action");

            // Handle AJAX search request
            if ("searchCustomers".equals(action)) {
                searchCustomers(req, resp);
                return;
            } else if ("searchProducts".equals(action)) {
                searchProducts(req, resp);
                return;
            }

            // Đặt mã lấy danh sách sản phẩm ở đây, bên ngoài các điều kiện if-else
            // để đảm bảo luôn được thực hiện cho tất cả các request khác
            Vector<Products> products = daoProduct.findAll();
            req.setAttribute("products", products);

            // Nếu không có action, hoặc action không phải là search, chuyển hướng tới trang import.jsp
            if (action == null || action.isEmpty()) {
                req.getRequestDispatcher("views/import/import.jsp").forward(req, resp);
                return;
            }

            // Chuyển hướng đến trang import.jsp cho các trường hợp khác
            req.getRequestDispatcher("views/import/import.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving products for import page", e);
            if (!resp.isCommitted()) {
                resp.sendRedirect(req.getContextPath() + "/error.jsp");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Xử lý các hành động POST như thêm sản phẩm vào giỏ hàng, thanh toán, v.v.
        String action = req.getParameter("action");

        if (action == null) {
            resp.sendRedirect(req.getContextPath() + "/Imports");
            return;
        }

        try {
            switch (action) {
                case "addToCart":
                    addToCart(req, resp);
                    break;
                case "prepareCheckout":
                    processCheckout(req, resp);
                    break;
                case "showPayment":
                    showPaymentPage(req, resp);
                    break;
                case "checkout":
                    processCheckout(req, resp);
                    break;
                case "applyDiscount":
                    applyDiscountCode(req, resp);
                    break;
                case "applyDiscountAjax":
                    applyDiscountCodeAjax(req, resp);
                    break;
                case "clearToast":
                    clearToastMessage(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/Imports");
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing POST request", e);
            // Sử dụng toast thông báo lỗi và redirect về trang ịmport
            setToastMessage(req, "Đã xảy ra lỗi khi xử lý yêu cầu: " + e.getMessage(), "error");
            resp.sendRedirect(req.getContextPath() + "/Imports");
        }
    }

    /**
     * Đặt thông báo toast message vào session
     *
     * @param request HttpServletRequest
     * @param message Nội dung thông báo
     * @param type Loại thông báo (success/error)
     */
    private void setToastMessage(HttpServletRequest request, String message, String type) {
        HttpSession session = request.getSession();
        session.setAttribute("toastMessage", message);
        session.setAttribute("toastType", type);
    }

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    private void addToCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(req.getParameter("productId"));
            int quantity = Integer.parseInt(req.getParameter("quantity"));

            // TODO: Thêm logic để thêm sản phẩm vào giỏ hàng (session)
            // Chuyển hướng trở lại trang bán hàng
            resp.sendRedirect(req.getContextPath() + "/Imports");

        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid product ID or quantity", e);
            setToastMessage(req, "ID sản phẩm hoặc số lượng không hợp lệ", "error");
            resp.sendRedirect(req.getContextPath() + "/Imports");
        }
    }

    /**
     * Hiển thị trang thanh toán với thông tin giỏ hàng và khách hàng
     */
    private void showPaymentPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Lấy thông tin từ request
            String cartItemsJson = req.getParameter("cartItems");
            String customerName = req.getParameter("customerName");
            String customerPhone = req.getParameter("customerPhone");
            String customerId = req.getParameter("customerId");

            // Lưu thông tin vào session
            HttpSession session = req.getSession();
            session.setAttribute("cartItemsJson", cartItemsJson);
            session.setAttribute("customerName", customerName);
            session.setAttribute("customerPhone", customerPhone);
            session.setAttribute("customerId", customerId);

            // Kiểm tra và xử lý tên khách hàng
            if (customerName == null || customerName.trim().isEmpty()) {
                // Nếu không có tên khách hàng, thử lấy từ database nếu có ID
                if (customerId != null && !customerId.trim().isEmpty() && !customerId.equals("0")) {
                    try {
                        int customerIdInt = Integer.parseInt(customerId);
                        Customers customer = daoCustomer.getCustomerById(customerIdInt);
                        if (customer != null) {
                            customerName = customer.getName();
                            customerPhone = customer.getPhone();
                        }
                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.WARNING, "Invalid customer ID: " + customerId, e);
                    }
                }

                // Nếu vẫn không có tên khách hàng, đặt là "Khách lẻ"
                if (customerName == null || customerName.trim().isEmpty()) {
                    customerName = "Khách lẻ";
                }
            }

            // Parse JSON cart items
            List<Map<String, Object>> cartItems = new ArrayList<>();
            double totalAmount = 0;

            if (cartItemsJson != null && !cartItemsJson.isEmpty()) {
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Map<String, Object>>>() {
                    }.getType();
                    cartItems = gson.fromJson(cartItemsJson, type);

                    // Tính tổng tiền
                    for (Map<String, Object> item : cartItems) {
                        Object priceObj = item.get("price");
                        Object quantityObj = item.get("quantity");

                        double price = 0;
                        int quantity = 0;

                        if (priceObj instanceof Double) {
                            price = (Double) priceObj;
                        } else if (priceObj instanceof String) {
                            price = Double.parseDouble((String) priceObj);
                        } else if (priceObj instanceof Integer) {
                            price = (Integer) priceObj;
                        }

                        if (quantityObj instanceof Double) {
                            quantity = ((Double) quantityObj).intValue();
                        } else if (quantityObj instanceof String) {
                            quantity = Integer.parseInt((String) quantityObj);
                        } else if (quantityObj instanceof Integer) {
                            quantity = (Integer) quantityObj;
                        }

                        double itemTotal = price * quantity;
                        item.put("total", itemTotal);
                        totalAmount += itemTotal;
                    }
                } catch (JsonSyntaxException | NumberFormatException e) {
                    LOGGER.log(Level.SEVERE, "Error parsing cart items JSON", e);
                }
            }

            // Kiểm tra nếu giỏ hàng trống
            if (cartItems.isEmpty()) {
                setToastMessage(req, "Giỏ hàng trống, không thể thanh toán", "error");
                resp.sendRedirect(req.getContextPath() + "/Imports");
                return;
            }

            // Đặt các thuộc tính vào request
            req.setAttribute("cartItems", cartItems);
            req.setAttribute("cartItemsJson", cartItemsJson);  // Pass the JSON to the page
            req.setAttribute("totalAmount", totalAmount);
            req.setAttribute("customerId", customerId);
            req.setAttribute("customerName", customerName);
            req.setAttribute("customerPhone", customerPhone);
            req.setAttribute("discount", 0); // Mặc định không có giảm giá

            // Chuyển hướng đến trang thanh toán
            req.getRequestDispatcher("views/import/import-payment.jsp").forward(req, resp);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error showing payment page", e);
            setToastMessage(req, "Lỗi hiển thị trang thanh toán: " + e.getMessage(), "error");
            resp.sendRedirect(req.getContextPath() + "/Imports");
        }
    }

    /**
     * Xử lý thanh toán
     */
    private void processCheckout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        try {
            // Lấy thông tin từ request hoặc session nếu không có
            String cartItemsJson = req.getParameter("cartItems");
            String customerName = req.getParameter("customerName");
            String customerPhone = req.getParameter("customerPhone");
            String customerId = req.getParameter("customerId");
            String paymentMethod = req.getParameter("paymentMethod");
            String totalStr = req.getParameter("total");
            String customerPaidStr = req.getParameter("customerPaid");
            String voucherCode = req.getParameter("discountCode");

            // Sử dụng dữ liệu từ session nếu request không có
            if (cartItemsJson == null || cartItemsJson.trim().isEmpty()) {
                cartItemsJson = (String) session.getAttribute("cartItemsJson");
            }
            if (customerName == null || customerName.trim().isEmpty()) {
                customerName = (String) session.getAttribute("customerName");
            }
            if (customerPhone == null || customerPhone.trim().isEmpty()) {
                customerPhone = (String) session.getAttribute("customerPhone");
            }
            if (customerId == null || customerId.trim().isEmpty()) {
                customerId = (String) session.getAttribute("customerId");
            }

            // Xử lý số tiền - sửa lỗi NumberFormatException
            double total = 0;
            double customerPaid = 0;

            try {
                // Xử lý chuỗi số có thể chứa dấu thập phân
                if (totalStr != null && !totalStr.isEmpty()) {
                    // Loại bỏ tất cả ký tự không phải số và dấu thập phân
                    totalStr = totalStr.replaceAll("[^0-9.]", "");
                    total = Double.parseDouble(totalStr) * -1;
                }

                if (customerPaidStr != null && !customerPaidStr.isEmpty()) {
                    // Loại bỏ tất cả ký tự không phải số và dấu thập phân
                    customerPaidStr = customerPaidStr.replaceAll("[^0-9.]", "");
                    customerPaid = Double.parseDouble(customerPaidStr);
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error parsing payment amounts", e);
                // Sử dụng giá trị mặc định nếu có lỗi
            }

            // Chuyển đổi sang int nếu cần
            int totalInt = (int) Math.round(total);
            int customerPaidInt = (int) Math.round(customerPaid);

            // Parse JSON cart items
            List<Map<String, Object>> orderItems = new ArrayList<>();
            if (cartItemsJson != null && !cartItemsJson.isEmpty()) {
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Map<String, Object>>>() {
                    }.getType();
                    orderItems = gson.fromJson(cartItemsJson, type);

                    for (int i = 0; i < orderItems.size(); i++) {
                        Map<String, Object> item = orderItems.get(i);
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error parsing cart items JSON", e);
                    setToastMessage(req, "Lỗi xử lý dữ liệu giỏ hàng: " + e.getMessage(), "error");
                    resp.sendRedirect(req.getContextPath() + "/Imports");
                    return;
                }
            }

            // Kiểm tra nếu giỏ hàng trống
            if (orderItems.isEmpty()) {
                setToastMessage(req, "Giỏ hàng trống, không thể thanh toán", "error");
                resp.sendRedirect(req.getContextPath() + "/Imports");
                return;
            }

            int employeeId = 3; // Mặc định ID nhân viên là 3

            // Chuyển đổi customerId từ String sang Integer
            Integer customerIdInt = GUEST_CUSTOMER_ID; // Mặc định là khách lẻ
            if (customerId != null && !customerId.isEmpty() && !customerId.equals("0")) {
                try {
                    customerIdInt = Integer.parseInt(customerId);
                    // Kiểm tra xem customer có tồn tại không
                    Customers customer = daoCustomer.getCustomerById(customerIdInt);
                    if (customer == null) {
                        LOGGER.warning("Customer with ID " + customerIdInt + " not found. Using guest customer.");
                        customerIdInt = GUEST_CUSTOMER_ID; // Sử dụng khách lẻ nếu không tìm thấy
                    } else {
                        // Nếu tìm thấy customer, lấy thông tin cập nhật
                        customerName = customer.getName();
                        customerPhone = customer.getPhone();
                    }
                } catch (NumberFormatException e) {
                    LOGGER.log(Level.WARNING, "Invalid customer ID format: " + customerId, e);
                    customerIdInt = GUEST_CUSTOMER_ID; // Sử dụng khách lẻ nếu ID không hợp lệ
                }
            }

            // Xử lý ID nhân viên
            employeeId = 3;
            // Kiểm tra xem employee có tồn tại không
            Users employee = daoAccount.getUserById(employeeId);
            if (employee == null) {
                setToastMessage(req, "Không tìm thấy nhân viên với ID " + employeeId, "error");
                resp.sendRedirect(req.getContextPath() + "/Imports");
                return;
            }

            // Tạo đối tượng Order
            Orders order = Orders.builder()
                    .orderDate(new Date())
                    .totalAmount(totalInt)
                    .customerID(customerIdInt)
                    .employeeID(employeeId)
                    .build();

            // Thêm đơn hàng vào database và lấy ID
            int orderId = daoOrder.insertOrder(order);

            if (orderId <= 0) {
                setToastMessage(req, "Không thể tạo đơn hàng", "error");
                resp.sendRedirect(req.getContextPath() + "/Imports");
                return;
            }

            LOGGER.info("Created new order with ID: " + orderId);

            // Xử lý từng sản phẩm trong giỏ hàng
            boolean allItemsProcessed = true;

            for (Map<String, Object> item : orderItems) {
                try {
                    // Lấy thông tin sản phẩm từ item - xử lý an toàn các kiểu dữ liệu
                    int productId = 0;
                    int price = 0;
                    int quantity = 0;
                    String productName = "";

                    // Xử lý productId
                    Object productIdObj = item.get("productId");
                    if (productIdObj instanceof Double) {
                        productId = ((Double) productIdObj).intValue();
                    } else if (productIdObj instanceof String) {
                        productId = Integer.parseInt((String) productIdObj);
                    } else if (productIdObj instanceof Integer) {
                        productId = (Integer) productIdObj;
                    }

                    // Xử lý productName
                    productName = (String) item.get("productName");

                    // Xử lý price
                    Object priceObj = item.get("price");
                    if (priceObj instanceof Double) {
                        price = ((Double) priceObj).intValue();
                    } else if (priceObj instanceof String) {
                        price = Integer.parseInt((String) priceObj);
                    } else if (priceObj instanceof Integer) {
                        price = (Integer) priceObj;
                    }

                    // Xử lý quantity
                    Object quantityObj = item.get("quantity");
                    if (quantityObj instanceof Double) {
                        quantity = ((Double) quantityObj).intValue();
                    } else if (quantityObj instanceof String) {
                        quantity = Integer.parseInt((String) quantityObj);
                    } else if (quantityObj instanceof Integer) {
                        quantity = (Integer) quantityObj;
                    }

                    LOGGER.info("Processing product: ID=" + productId
                            + ", Name=" + productName
                            + ", Quantity=" + quantity
                            + ", Price=" + price
                            + ", OrderID=" + orderId);

                    // Kiểm tra tồn kho
                    Products product = daoProduct.getProductById02(productId);
                    if (product == null || product.getProductId() <= 0) {
                        throw new Exception("Không tìm thấy sản phẩm với ID: " + productId);
                    }

                    // Tạo chi tiết đơn hàng
                    OrderDetails orderDetail = OrderDetails.builder()
                            .orderID(orderId)
                            .productID(productId)
                            .quantity(quantity)
                            .price(price)
                            .build();

                    LOGGER.info("Saving order detail: " + orderDetail.toString());

                    // Lưu chi tiết đơn hàng
                    boolean detailSaved = daoOrderDetails.addOrderDetail(orderDetail);
                    if (!detailSaved) {
                        LOGGER.severe("Failed to save order detail for product: " + productName);
                        allItemsProcessed = false;
                        break;
                    }

                    // Cập nhật tồn kho
                    int newStock = product.getQuantity() + quantity;

                    product.setQuantity(newStock);
                    boolean stockUpdated = daoProduct.updateProduct(product);

                    if (!stockUpdated) {
                        throw new Exception("Không thể cập nhật tồn kho cho sản phẩm: " + product.getName());
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error processing item", e);
                    allItemsProcessed = false;
                    break;
                }
            }

            // Nếu có lỗi xảy ra khi xử lý sản phẩm, xóa đơn hàng
            if (!allItemsProcessed) {
                daoOrder.deleteOrder(orderId);
                setToastMessage(req, "Có lỗi xảy ra khi xử lý sản phẩm trong giỏ hàng", "error");
                resp.sendRedirect(req.getContextPath() + "/Imports");
                return;
            }

            // Đặt các thuộc tính vào request để hiển thị trên trang hóa đơn
            req.setAttribute("orderId", orderId);
            req.setAttribute("orderDate", new Date());
            req.setAttribute("customerName", customerName);
            req.setAttribute("customerPhone", customerPhone);
            req.setAttribute("employeeName", employee.getName());
            req.setAttribute("orderItems", orderItems);
            req.setAttribute("total", total);
            req.setAttribute("discount", 0); // Mặc định không có giảm giá
            req.setAttribute("totalPayable", total);
            req.setAttribute("customerPaid", customerPaid);
            req.setAttribute("paymentMethod", paymentMethod);
            setToastMessage(req, "Success", "success");

            // Chuyển hướng đến trang hóa đơn
            resp.sendRedirect(req.getContextPath() + "/Imports");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing checkout", e);
            setToastMessage(req, "Đã xảy ra lỗi khi xử lý thanh toán: " + e.getMessage(), "error");
            resp.sendRedirect(req.getContextPath() + "/Imports");
        }
    }

    /**
     * Xử lý áp dụng mã giảm giá
     */
    private void applyDiscountCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession();

            // Lấy thông tin từ request hoặc session nếu không có
            String discountCode = req.getParameter("discountCode");
            String cartItemsJson = req.getParameter("cartItems");
            String customerName = req.getParameter("customerName");
            String customerPhone = req.getParameter("customerPhone");
            String customerId = req.getParameter("customerId");

            // Sử dụng dữ liệu từ session nếu request không có
            if (cartItemsJson == null || cartItemsJson.trim().isEmpty()) {
                cartItemsJson = (String) session.getAttribute("cartItemsJson");
            }
            if (customerName == null || customerName.trim().isEmpty()) {
                customerName = (String) session.getAttribute("customerName");
            }
            if (customerPhone == null || customerPhone.trim().isEmpty()) {
                customerPhone = (String) session.getAttribute("customerPhone");
            }
            if (customerId == null || customerId.trim().isEmpty()) {
                customerId = (String) session.getAttribute("customerId");
            }

            // Cập nhật lại session với dữ liệu mới nhất
            session.setAttribute("cartItemsJson", cartItemsJson);
            session.setAttribute("customerName", customerName);
            session.setAttribute("customerPhone", customerPhone);
            session.setAttribute("customerId", customerId);
            session.setAttribute("discountCode", discountCode);

            // Tính tổng tiền từ giỏ hàng
            List<Map<String, Object>> cartItems = new ArrayList<>();
            double totalAmount = 0;

            if (cartItemsJson != null && !cartItemsJson.isEmpty()) {
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Map<String, Object>>>() {
                    }.getType();
                    cartItems = gson.fromJson(cartItemsJson, type);

                    // Tính tổng tiền
                    for (Map<String, Object> item : cartItems) {
                        Object priceObj = item.get("price");
                        Object quantityObj = item.get("quantity");

                        double price = 0;
                        int quantity = 0;

                        if (priceObj instanceof Double) {
                            price = (Double) priceObj;
                        } else if (priceObj instanceof String) {
                            price = Double.parseDouble((String) priceObj);
                        } else if (priceObj instanceof Integer) {
                            price = (Integer) priceObj;
                        }

                        if (quantityObj instanceof Double) {
                            quantity = ((Double) quantityObj).intValue();
                        } else if (quantityObj instanceof String) {
                            quantity = Integer.parseInt((String) quantityObj);
                        } else if (quantityObj instanceof Integer) {
                            quantity = (Integer) quantityObj;
                        }

                        double itemTotal = price * quantity;
                        item.put("total", itemTotal);
                        totalAmount += itemTotal;
                    }
                } catch (JsonSyntaxException | NumberFormatException e) {
                    LOGGER.log(Level.SEVERE, "Error parsing cart items JSON", e);
                }
            }

            // Kiểm tra mã giảm giá
            int totalAmountInt = (int) Math.round(totalAmount);

            int discountAmount = 0;

            // Đặt các thuộc tính vào request
            req.setAttribute("cartItems", cartItems);
            req.setAttribute("cartItemsJson", cartItemsJson);  // Pass the JSON back to the page
            req.setAttribute("totalAmount", totalAmount);
            req.setAttribute("customerId", customerId);
            req.setAttribute("customerName", customerName);
            req.setAttribute("customerPhone", customerPhone);
            req.setAttribute("discount", discountAmount);
            req.setAttribute("discountCode", discountCode);

            // Chuyển hướng đến trang thanh toán
            req.getRequestDispatcher("views/import/import-payment.jsp").forward(req, resp);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error applying discount code", e);
            setToastMessage(req, "Lỗi khi áp dụng mã giảm giá: " + e.getMessage(), "error");
            resp.sendRedirect(req.getContextPath() + "/Imports");
        }
    }

    /**
     * Xử lý áp dụng mã giảm giá qua AJAX
     */
    private void applyDiscountCodeAjax(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            HttpSession session = req.getSession();

            // Lấy thông tin từ request hoặc session
            String discountCode = req.getParameter("discountCode");
            String cartItemsJson = req.getParameter("cartItems");
            String customerName = req.getParameter("customerName");
            String customerPhone = req.getParameter("customerPhone");
            String customerId = req.getParameter("customerId");

            // Sử dụng dữ liệu từ session nếu request không có
            if (cartItemsJson == null || cartItemsJson.trim().isEmpty()) {
                cartItemsJson = (String) session.getAttribute("cartItemsJson");
            }

            // Tính tổng tiền từ giỏ hàng
            List<Map<String, Object>> cartItems = new ArrayList<>();
            double totalAmount = 0;

            if (cartItemsJson != null && !cartItemsJson.isEmpty()) {
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Map<String, Object>>>() {
                    }.getType();
                    cartItems = gson.fromJson(cartItemsJson, type);

                    // Tính tổng tiền
                    for (Map<String, Object> item : cartItems) {
                        Object priceObj = item.get("price");
                        Object quantityObj = item.get("quantity");

                        double price = 0;
                        int quantity = 0;

                        if (priceObj instanceof Double) {
                            price = (Double) priceObj;
                        } else if (priceObj instanceof String) {
                            price = Double.parseDouble((String) priceObj);
                        } else if (priceObj instanceof Integer) {
                            price = (Integer) priceObj;
                        }

                        if (quantityObj instanceof Double) {
                            quantity = ((Double) quantityObj).intValue();
                        } else if (quantityObj instanceof String) {
                            quantity = Integer.parseInt((String) quantityObj);
                        } else if (quantityObj instanceof Integer) {
                            quantity = (Integer) quantityObj;
                        }

                        double itemTotal = price * quantity;
                        item.put("total", itemTotal);
                        totalAmount += itemTotal;
                    }
                } catch (JsonSyntaxException | NumberFormatException e) {
                    LOGGER.log(Level.SEVERE, "Error parsing cart items JSON", e);
                }
            }

            // Kiểm tra mã giảm giá
            int totalAmountInt = (int) Math.round(totalAmount);
            int discountAmount = 0;
            int voucherId = 0;
            boolean success = false;

            // Logging for debugging
            // Chuẩn bị JSON response
            Gson gson = new Gson();
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", success);
            responseData.put("discount", discountAmount);
            responseData.put("voucherId", voucherId);
            responseData.put("totalAmount", totalAmountInt);
            responseData.put("totalPayable", totalAmountInt - discountAmount);

            String jsonResponse = gson.toJson(responseData);

            out.print(jsonResponse);
            out.flush();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error applying discount code via AJAX", e);

            Gson gson = new Gson();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Lỗi khi áp dụng mã giảm giá: " + e.getMessage());

            out.print(gson.toJson(errorResponse));
            out.flush();
        }
    }

    // Add this new method for AJAX customer search
    private void searchCustomers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String query = req.getParameter("query");
            if (query == null) {
                query = "";
            }

            List<Customers> customers = daoCustomer.searchCustomersByName(query);

            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("[");

            for (int i = 0; i < customers.size(); i++) {
                Customers customer = customers.get(i);
                jsonBuilder.append("{");
                jsonBuilder.append("\"id\":").append(customer.getId()).append(",");
                jsonBuilder.append("\"customerName\":\"").append(customer.getName().replace("\"", "\\\"")).append("\",");
                jsonBuilder.append("\"phone\":\"").append(customer.getPhone() != null ? customer.getPhone() : "").append("\"");
                jsonBuilder.append("}");

                if (i < customers.size() - 1) {
                    jsonBuilder.append(",");
                }
            }

            jsonBuilder.append("]");

            String jsonResponse = jsonBuilder.toString();
            // System.out.println("JSON Response: " + jsonResponse); // Debug log

            out.print(jsonResponse);
            out.flush();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching customers", e);
            out.print("[]");
            out.flush();
        }
    }

    // Cập nhật phương thức searchProducts
    private void searchProducts(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String query = req.getParameter("query");

            if (query == null) {
                query = "";
            }

            List<Products> products = daoProduct.searchProductsByName(query);

            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("[");

            for (int i = 0; i < products.size(); i++) {
                Products product = products.get(i);
                jsonBuilder.append("{");
                jsonBuilder.append("\"id\":").append(product.getProductId()).append(",");
                jsonBuilder.append("\"productName\":\"").append(product.getName().replace("\"", "\\\"")).append("\",");
                jsonBuilder.append("\"price\":").append(product.getPrice()).append(",");
                jsonBuilder.append("\"stockQuantity\":").append(product.getQuantity()).append(",");
                jsonBuilder.append("\"imageURL\":\"")
                        .append(product.getImage() != null
                                ? "/ISP392_Project/views/product/images/" + product.getImage().replace("\"", "\\\"")
                                : "")
                        .append("\"");
                jsonBuilder.append("}");

                if (i < products.size() - 1) {
                    jsonBuilder.append(",");
                }
            }

            jsonBuilder.append("]");

            String jsonResponse = jsonBuilder.toString();

            out.print(jsonResponse);
            out.flush();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching products", e);
            out.print("[]");
            out.flush();
        }
    }

    // Add this new method to clear toast messages
    private void clearToastMessage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        session.removeAttribute("toastMessage");
        session.removeAttribute("toastType");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
