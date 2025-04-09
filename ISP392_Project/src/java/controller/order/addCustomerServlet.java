/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.order;

import com.google.gson.JsonObject;
import dal.customerDAO;
import dal.userDAO;
import entity.Customers;
import entity.Stores;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author admin
 */
@WebServlet(name = "AddCustomer", urlPatterns = {"/AddCustomer"})
public class addCustomerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        double balance = Double.parseDouble(request.getParameter("total"));
        String createdBy = (String) request.getSession().getAttribute("fullName");
        String status = "Active";  // Hoặc trạng thái mặc định cho khách hàng
        String storeIdString = (String) request.getSession().getAttribute("storeID"); // Lấy store_id dưới dạng String
        customerDAO customerDAO = new customerDAO();

        if (customerDAO.checkPhoneExists(phone, 0)) {
            request.setAttribute("phoneError", "Phone number already exists.");
            request.getRequestDispatcher(request.getHeader("Referer")).forward(request, response);
            return;
        }

        if (phone == null || !phone.matches("^0\\d{9}$")) {
            request.setAttribute("phoneError", "Invalid phone number format.");
            request.getRequestDispatcher(request.getHeader("Referer")).forward(request, response);
            return;
        }
        Integer storeId = null;

        if (storeIdString != null) {
            // Nếu store_id không null, chuyển storeIdString thành Integer
            try {
                storeId = Integer.parseInt(storeIdString); // Chuyển đổi String thành Integer
            } catch (NumberFormatException e) {
                request.setAttribute("storeError", "Invalid Store ID format.");
                request.getRequestDispatcher(request.getHeader("Referer")).forward(request, response);
                return;
            }
        } else {
            // Nếu store_id là null, gán store_id của người mới là null
            storeId = null;
        }
        // Lấy storeID từ session
        userDAO dao = new userDAO();
        Stores store = null;
        if (storeId != null) {
            store = dao.getStoreById(storeId);
            if (store == null) {
                request.setAttribute("storeError", "Invalid store ID.");
                request.getRequestDispatcher(request.getHeader("Referer")).forward(request, response);
                return;
            }
        }
        // Tạo đối tượng Customer từ thông tin nhận được từ form
        Customers newCustomer = new Customers();
        newCustomer.setName(name);
        newCustomer.setPhone(phone);
        newCustomer.setAddress(address);
        newCustomer.setBalance(balance);
        newCustomer.setCreatedBy(createdBy);
        newCustomer.setStatus(status);
        newCustomer.setStoreId(store);

        // Tạo đối tượng CustomerDAO và gọi hàm insertCustomer
        // Truyền storeID vào hàm insertCustomer (nếu cần)
        customerDAO.insertCustomer(newCustomer);  // Gọi hàm chèn khách hàng vào DB

        // Phản hồi cho client
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Trả lại thông báo thành công
        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", "success");
        jsonResponse.addProperty("message", "Customer added successfully.");
        out.print(jsonResponse);
        out.flush();
    }
}
