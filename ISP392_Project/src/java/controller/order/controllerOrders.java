/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.order;

import dal.productsDAO;
import entity.OrderDetails;
import entity.Orders;
import entity.Products;
import entity.Customers;
import entity.Stores;
import entity.Users;
import service.OrderTask;
import service.OrderQueue;
import service.OrderWorker;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "controllerOrders", urlPatterns = {"/Orders"})
public class controllerOrders extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (role != null && role.equals("admin")) {
            response.sendRedirect("/dashboard.jsp");
            return;
        }

        String invoiceNumber = request.getParameter("invoice");
        if (invoiceNumber == null) {
            invoiceNumber = "1";
        }

        request.setAttribute("invoiceNumber", invoiceNumber);
        request.getRequestDispatcher("views/invoice/addOrder.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();

        String orderType = request.getParameter("orderType"); // 1: xuất, 2: nhập
        String fullName = (String) session.getAttribute("fullName");
        int customerId = Integer.parseInt(request.getParameter("customerId"));
        int userId = (int) session.getAttribute("userId");
        String status;

        // Validate customer
        if (customerId <= 0) {
            request.setAttribute("message", "Please choose customer!");
            request.getRequestDispatcher("views/invoice/addOrder.jsp").forward(request, response);
            return;
        }

        // Validate paid amount
        String paidAmountStr = request.getParameter("paidAmount");
        double paidAmount = 0;
        if (paidAmountStr != null && !paidAmountStr.trim().isEmpty()) {
            try {
                paidAmount = Double.parseDouble(paidAmountStr.trim());
            } catch (NumberFormatException e) {
                request.setAttribute("message", "Payment error!");
                request.getRequestDispatcher("views/invoice/addOrder.jsp").forward(request, response);
                return;
            }
        }

        if ("Export".equals(orderType)) {
            String discountStr = request.getParameter("totalDiscount");
            double totalDiscount = 0;
            if (discountStr != null && !discountStr.trim().isEmpty()) {
                try {
                    totalDiscount = Double.parseDouble(discountStr.trim());
                } catch (NumberFormatException e) {
                    totalDiscount = 0; // hoặc log lỗi
                    System.err.println("Invalid discount value: " + discountStr);
                }
            }
            if (totalDiscount >= 200000) {
                status = "CẢNH BÁO! Giảm giá: " + totalDiscount + "\n" + request.getParameter("status");
            } else {
                status = request.getParameter("status");
            }
        } else {
            status = request.getParameter("status");
        }

        String totalPriceStr = request.getParameter("totalOrderPriceHidden");
        double totalOrderPrice = 0;
        if (totalPriceStr != null && !totalPriceStr.trim().isEmpty()) {
            try {
                totalOrderPrice = Double.parseDouble(totalPriceStr.trim());
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format for totalOrderPriceHidden: " + totalPriceStr);
            }
        }

        if (paidAmountStr != null && !paidAmountStr.trim().isEmpty()) {
            paidAmount = Double.parseDouble(paidAmountStr);
        }
        double debtAmount = Double.parseDouble(request.getParameter("balanceAmount"));
        String balanceAction = request.getParameter("balanceAction");
        double epsilon = 1e-9;

        try {
            String[] productIds = request.getParameterValues("productID");
            String[] productNames = request.getParameterValues("productName");
            String[] totalPrices = request.getParameterValues("totalPriceHidden");
            String[] unitPrices = request.getParameterValues("unitPriceHidden");
            String[] quantities = request.getParameterValues("totalWeight");
            String[] discounts = "Export".equals(orderType) ? request.getParameterValues("discount") : null;
            // Validate order items
            if (productIds == null || productIds.length == 0) {
                request.setAttribute("message", "Please choose product!");
                request.getRequestDispatcher("views/invoice/addOrder.jsp").forward(request, response);
                return;
            }
            List<OrderDetails> orderDetailsList = new ArrayList<>();
            double calculatedTotalAmount = 0;

            if (productIds != null) {
                for (int i = 0; i < productIds.length; i++) {
                    int productID = Integer.parseInt(productIds[i]);
                    String productName = productNames[i];
                    double price = Double.parseDouble(totalPrices[i]);
                    double unitPrice = Double.parseDouble(unitPrices[i]);
                    double discount = "Export".equals(orderType) ? Double.parseDouble(discounts[i]) : 0.0;
                    int quantity = Integer.parseInt(quantities[i]);
                    double actualUnitPrice = 0;
                    double expectedPrice = 0;
                    System.out.println(">>>>> DEBUG ORDER START <<<<<");
                    System.out.println("customerId: " + customerId);
                    System.out.println("userId: " + userId);
                    System.out.println("storeId: " + session.getAttribute("storeID"));
                    System.out.println("totalOrderPrice: " + totalOrderPrice);
                    System.out.println("paidAmount: " + paidAmount);
                    System.out.println("balanceAmount: " + debtAmount);
                    System.out.println("orderDetailsList.size: " + orderDetailsList.size());
                    System.out.println(">>>>> DEBUG ORDER END <<<<<");

                    if ("Export".equals(orderType)) {
                        int availableQuantity = productsDAO.INSTANCE.getProductQuantity(productID);
                        actualUnitPrice = productsDAO.INSTANCE.getProductPrice(productID);
                        expectedPrice = (actualUnitPrice - discount) * quantity;

                        if (quantity > availableQuantity || Math.abs(expectedPrice - price) > epsilon || Math.abs(actualUnitPrice - unitPrice) > epsilon) {
                            response.getWriter().write("{\"status\": \"error\", \"message\": \"Lỗi dữ liệu sản phẩm, vui lòng kiểm tra lại.\"}");
                            return;
                        }

                    } else {
                        actualUnitPrice = unitPrice;
                        expectedPrice = unitPrice * quantity;
                    }

                    if (quantity <= 0 || unitPrice <= 0 || price < 0 || discount < 0) {
                        response.getWriter().write("{\"status\": \"error\", \"message\": \"Dữ liệu không hợp lệ.\"}");
                        return;
                    }

                    calculatedTotalAmount += expectedPrice;
                    int storeId = 0;
                    try {
                        storeId = Integer.parseInt((String) session.getAttribute("storeID"));
                    } catch (NumberFormatException | NullPointerException e) {
                        response.getWriter().write("{\"status\": \"error\", \"message\": \"Không lấy được storeID từ session.\"}");
                        return;
                    }
                    OrderDetails od = OrderDetails.builder()
                            .productID(Products.builder().productId(productID).build())
                            .productName(productName)
                            .unitPrice(actualUnitPrice)
                            .price(expectedPrice)
                            .importPrice(actualUnitPrice)
                            .quantity(quantity)
                            .description("Order detail")
                            .status("ACTIVE")
                            .storeId(Stores.builder().id(storeId).build())
                            .build();

                    orderDetailsList.add(od);
                    //  Trừ số lượng tồn kho
                    int availableQuantity = productsDAO.INSTANCE.getProductQuantity(productID);
                    int newQuantity = availableQuantity - quantity;
                    boolean updated = productsDAO.INSTANCE.updateProductQuantity(productID, newQuantity);
                    if (!updated) {
                        request.setAttribute("message", "Không thể cập nhật số lượng tồn kho");
                        request.getRequestDispatcher("views/invoice/addOrder.jsp").forward(request, response);
                        return;
                    }

                }
            }

            if (Math.abs(calculatedTotalAmount - totalOrderPrice) > epsilon) {
                request.setAttribute("message", "Tổng tiền tính toán không khớp");
                request.getRequestDispatcher("views/invoice/addOrder.jsp").forward(request, response);
                return;
            }

            // Tính lại nợ
            if (debtAmount < 0 || (debtAmount > 0 && "debt".equals(balanceAction))) {
                debtAmount = paidAmount - calculatedTotalAmount;
            } else {
                debtAmount = 0.0;
            }
            int storeId = Integer.parseInt((String) session.getAttribute("storeID")); // ✅ đúng

            // Tạo đơn hàng
            Orders order = Orders.builder()
                    .customerID(Customers.builder().id(customerId).build())
                    .userID(Users.builder().id(userId).build())
                    .storeId(Stores.builder().id(storeId).build())
                    .type("Export".equals(orderType) ? "Export" : "Import")
                    .amount(calculatedTotalAmount)
                    .paidAmount(paidAmount)
                    .description(status)
                    .status("PENDING")
                    .createdBy(fullName)
                    .build();

            // Gửi vào hàng đợi
            OrderWorker.startWorker();
            OrderWorker.clearProcessedOrder(userId);
            OrderQueue.addOrder(new OrderTask(order, orderDetailsList, balanceAction, fullName));

            request.setAttribute("message", "success");
            request.getRequestDispatcher("views/invoice/addOrder.jsp").forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace(); // In log chi tiết ra console
            Logger.getLogger(controllerOrders.class.getName()).log(Level.SEVERE, "Lỗi khi tạo đơn hàng", ex);
            request.setAttribute("message", "error");
            request.getRequestDispatcher("views/invoice/addOrder.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
