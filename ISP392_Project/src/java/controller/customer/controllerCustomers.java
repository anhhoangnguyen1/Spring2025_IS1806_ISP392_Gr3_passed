/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.customer;

import dal.customerDAO;
import entity.Customers;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author THC
 */
@WebServlet(name = "controllerCustomers", urlPatterns = {"/Customers"})
public class controllerCustomers extends HttpServlet {

    customerDAO customerDAO = new customerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      
    String service = request.getParameter("service");
    if (service == null) {
        service = "customers"; // Mặc định hiển thị danh sách khách hàng nếu không có service
    }

    switch (service) {
        case "customers":
            // Logic xem danh sách khách hàng
            int index = 1;
            try {
                index = Integer.parseInt(request.getParameter("index"));
            } catch (NumberFormatException ignored) {
            }

            // Lấy danh sách khách hàng từ database
            List<Customers> list = customerDAO.viewAllCustomers("name", index);
            int total = customerDAO.countCustomers();
            int endPage = (total % 10 == 0) ? total / 10 : (total / 10) + 1;

            // Chuyển thông tin vào request
            request.setAttribute("list", list);
            request.setAttribute("endPage", endPage);
            request.setAttribute("index", index);

            // Chuyển tiếp đến JSP danh sách khách hàng
            request.getRequestDispatcher("views/customer/customers.jsp").forward(request, response);
            break;

        case "getCustomerById":
            // Lấy thông tin khách hàng theo ID
            int id = Integer.parseInt(request.getParameter("customer_id"));
            Customers customer = customerDAO.getCustomerById(id);
            request.setAttribute("customer", customer);

            // Chuyển tiếp đến trang chi tiết khách hàng
            request.getRequestDispatcher("views/customer/detailCustomer.jsp").forward(request, response);
            break;

        case "editCustomer":
            // Lấy thông tin khách hàng cần chỉnh sửa
            int customerId = Integer.parseInt(request.getParameter("customer_id"));
            Customers customerForEdit = customerDAO.getCustomerById(customerId);
            request.setAttribute("customer", customerForEdit);

            // Lấy tên người dùng từ session
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("username"); // Lấy username từ session
            request.setAttribute("userName", userName); // Đưa username vào request

          
            request.getRequestDispatcher("views/customer/editCustomer.jsp").forward(request, response);
            break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");

        if ("addCustomer".equals(service)) {
            // Thêm khách hàng mới
            Customers customer = Customers.builder()
                    .name(request.getParameter("name"))
                    .phone(request.getParameter("phone"))
                    .address(request.getParameter("address"))
                    .balance(Double.parseDouble(request.getParameter("balance")))
                    .createdBy(request.getParameter("createdBy"))
                    .updatedBy(request.getParameter("createdBy")) // Ban đầu updatedBy = createdBy
                    .status(request.getParameter("status"))
                    .build();
            customerDAO.insertCustomer(customer);
            response.sendRedirect("Customers?service=customers");
        }

        if ("editCustomer".equals(service)) {
            // Chỉnh sửa thông tin khách hàng
            Customers customer = Customers.builder()
                    .id(Integer.parseInt(request.getParameter("customer_id")))
                    .name(request.getParameter("name"))
                    .phone(request.getParameter("phone"))
                    .address(request.getParameter("address"))
                    .balance(Double.parseDouble(request.getParameter("balance")))
                    .updatedBy(request.getParameter("updatedBy")) // Người chỉnh sửa là tên người dùng hiện tại
                    .status(request.getParameter("status"))
                    .build();
            customerDAO.editCustomer(customer);
            response.sendRedirect("Customers?service=customers");
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
