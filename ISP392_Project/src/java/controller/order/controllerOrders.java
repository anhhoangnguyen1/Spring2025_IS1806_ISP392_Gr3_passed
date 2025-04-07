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
        String userName = (String) session.getAttribute("username");
        int customerId = Integer.parseInt(request.getParameter("customerId"));
        int userId = (int) session.getAttribute("userID");

        String status;
        double totalDiscount = 0;

        if ("Export".equals(orderType)) {
            totalDiscount = Double.parseDouble(request.getParameter("totalDiscount"));
            if (totalDiscount >= 200000) {
                status = "CẢNH BÁO! Giảm giá: " + totalDiscount + "\n" + request.getParameter("status");
            } else {
                status = request.getParameter("status");
            }
        } else {
            status = request.getParameter("status");
        }

        double totalOrderPrice = Double.parseDouble(request.getParameter("totalOrderPriceHidden"));
        double paidAmount = Double.parseDouble(request.getParameter("paidAmount"));
        double debtAmount = Double.parseDouble(request.getParameter("balanceAmount"));
        String balanceAction = request.getParameter("balanceAction");
        double epsilon = 1e-9;

        try {
            String[] productIds = request.getParameterValues("productID");
            String[] productNames = request.getParameterValues("productName");
            String[] totalPrices = request.getParameterValues("totalPriceHidden");
            String[] unitPrices = request.getParameterValues("unitPriceHidden");
            String[] quantities = request.getParameterValues("totalWeight");
            String[] discounts = "1".equals(orderType) ? request.getParameterValues("discount") : null;

            List<OrderDetails> orderDetailsList = new ArrayList<>();
            double calculatedTotalAmount = 0;

            if (productIds != null) {
                for (int i = 0; i < productIds.length; i++) {
                    int productID = Integer.parseInt(productIds[i]);
                    String productName = productNames[i];
                    double price = Double.parseDouble(totalPrices[i]);
                    double unitPrice = Double.parseDouble(unitPrices[i]);
                    double discount = "1".equals(orderType) ? Double.parseDouble(discounts[i]) : 0.0;
                    int quantity = Integer.parseInt(quantities[i]);
                    double actualUnitPrice = 0;
                    double expectedPrice = 0;

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

                    OrderDetails od = OrderDetails.builder()
                            .productID(Products.builder().productId(productID).build())
                            .productName(productName)
                            .unitPrice(actualUnitPrice)
                            .price(expectedPrice)
                            .importPrice(actualUnitPrice)
                            .quantity(quantity)
                            .description("Chi tiết đơn hàng")
                            .status("ACTIVE")
                            .storeId(Stores.builder().id((int) session.getAttribute("storeID")).build())
                            .build();

                    orderDetailsList.add(od);
                }
            }

            if (Math.abs(calculatedTotalAmount - totalOrderPrice) > epsilon) {
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Tổng tiền tính toán không khớp.\"}");
                return;
            }

            // Tính lại nợ
            if (debtAmount < 0 || (debtAmount > 0 && "debt".equals(balanceAction))) {
                debtAmount = paidAmount - calculatedTotalAmount;
            } else {
                debtAmount = 0.0;
            }

            // Tạo đơn hàng
            Orders order = Orders.builder()
                    .customerID(Customers.builder().id(customerId).build())
                    .userID(Users.builder().id(userId).build())
                    .storeId(Stores.builder().id((int) session.getAttribute("storeID")).build())
                    .type("1".equals(orderType) ? "Export" : "Import")
                    .amount(calculatedTotalAmount)
                    .paidAmount(paidAmount)
                    .description(status)
                    .status("PENDING")
                    .createdBy(userName)
                    .build();

            // Gửi vào hàng đợi
            OrderWorker.startWorker();
            OrderWorker.clearProcessedOrder(userId);
            OrderQueue.addOrder(new OrderTask(order, orderDetailsList));

            response.getWriter().write("{\"status\": \"processing\"}");

        } catch (Exception ex) {
            Logger.getLogger(controllerOrders.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Lỗi hệ thống khi tạo đơn hàng.\"}");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
