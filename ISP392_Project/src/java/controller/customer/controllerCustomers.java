/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.customer;

import dal.customerDAO;
import dal.debtDAO;
import entity.Customers;
import entity.DebtNote;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Comparator;
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
        HttpSession session = request.getSession();
        String service = request.getParameter("service");
        if (service == null) {
            service = "customers";
        }
        String sortBy = request.getParameter("sortBy");
        if (sortBy == null || !sortBy.equals("balance")) {
            sortBy = "id"; 
        }

        String sortOrder = request.getParameter("sortOrder");
        if (sortOrder == null || (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC"))) {
            sortOrder = "ASC"; 
        }

        switch (service) {
            case "customers": {
               
                String debtAction = request.getParameter("debtAction");
                if (debtAction != null && !debtAction.trim().isEmpty()) {
                
                    request.setAttribute("Notification", "Debt added successfully.");
                }

                String keyword = request.getParameter("searchCustomer");
                if (keyword == null) {
                    keyword = "";
                }

                int index = 1;
                try {
                    index = Integer.parseInt(request.getParameter("index"));
                } catch (NumberFormatException ignored) {
                }

                int total = customerDAO.countCustomers(keyword);
                int endPage = (total % 5 == 0) ? total / 5 : (total / 5) + 1;

                List<Customers> list = customerDAO.searchCustomers(keyword, index, 5, sortBy, sortOrder);
                String notification = (String) request.getSession().getAttribute("Notification");
                if (notification != null) {
                    request.setAttribute("Notification", notification);
                }

                request.setAttribute("list", list);
                request.setAttribute("endPage", endPage);
                request.setAttribute("index", index);
                request.setAttribute("searchCustomer", keyword);
                request.setAttribute("sortBy", sortBy);
                request.setAttribute("sortOrder", sortOrder);

                request.getRequestDispatcher("views/customer/customers.jsp").forward(request, response);
                break;
            }
            case "getCustomerById": {

                int id = Integer.parseInt(request.getParameter("customer_id"));
                Customers customer = customerDAO.getCustomerById(id);
                request.setAttribute("customer", customer);
                request.getRequestDispatcher("views/customer/detailCustomer.jsp").forward(request, response);
                break;
            }
            case "addCustomer": {
                String createdBy = (String) session.getAttribute("username");
                request.setAttribute("userName", createdBy);
                request.getRequestDispatcher("views/customer/addCustomer.jsp").forward(request, response);
                break;
            }

            case "editCustomer": {
                int customerId = Integer.parseInt(request.getParameter("customer_id"));
                Customers customerForEdit = customerDAO.getCustomerById(customerId);
                request.setAttribute("customer", customerForEdit);
                String userName = (String) session.getAttribute("username");
                request.setAttribute("userName", userName);
                request.setAttribute("sortOrder", sortOrder);

                request.getRequestDispatcher("views/customer/editCustomer.jsp").forward(request, response);
                break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");

        if ("addCustomer".equals(service)) {
            Customers customer = Customers.builder()
                    .name(request.getParameter("name"))
                    .phone(request.getParameter("phone"))
                    .address(request.getParameter("address"))
                    .balance(Double.parseDouble(request.getParameter("balance")))
                    .createdBy(request.getParameter("createdBy"))
                    .updatedBy(request.getParameter("createdBy"))
                    .status(request.getParameter("status"))
                    .build();
            customerDAO.insertCustomer(customer);
            response.sendRedirect("Customers?service=customers");
        }

        if ("editCustomer".equals(service)) {

            int customerId = Integer.parseInt(request.getParameter("customer_id"));
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String status = request.getParameter("status");
            double balance = Double.parseDouble(request.getParameter("balance"));
            String updatedBy = request.getParameter("updatedBy");

            if (status == null || status.isEmpty()) {
                status = "Active";
            }
            boolean phoneExists = customerDAO.checkPhoneExists(phone, customerId);

            if (phoneExists) {
                request.setAttribute("phoneError", "Phone number already exists.");
                request.setAttribute("customer", getCustomerFromRequest(request));
                request.getRequestDispatcher("views/customer/editCustomer.jsp").forward(request, response);
                return;
            }
            Customers customer = Customers.builder()
                    .id(Integer.parseInt(request.getParameter("customer_id")))
                    .name(request.getParameter("name"))
                    .phone(request.getParameter("phone"))
                    .address(request.getParameter("address"))
                    .balance(balance)
                    .updatedBy(updatedBy)
                    .status(status)
                    .build();
            customerDAO.editCustomer(customer);
            HttpSession session = request.getSession();
            session.setAttribute("successMessage", "Customer details updated successfully.");
            response.sendRedirect("Customers?service=customers&sortOrder=" + request.getParameter("sortOrder"));
        }
//        if ("search".equals(service)) {
//            String searchCustomer = request.getParameter("searchCustomer"); 
//            List<Customers> list;
//
//           
//            if (searchCustomer != null && !searchCustomer.isEmpty()) {
//                list = customerDAO.searchCustomers(searchCustomer);  
//            } else {
//                list = customerDAO.viewAllCustomersWithDebts("name",1); 
//            }
//
//            request.setAttribute("list", list);
//            request.getRequestDispatcher("views/customer/customers.jsp").forward(request, response);
//        }
    }

    private Customers getCustomerFromRequest(HttpServletRequest request) {
        Customers customer = new Customers();
        customer.setId(Integer.parseInt(request.getParameter("customer_id")));
        customer.setName(request.getParameter("name"));
        customer.setPhone(request.getParameter("phone"));
        customer.setAddress(request.getParameter("address"));
        customer.setUpdatedBy(request.getParameter("updateBy"));
        customer.setStatus(request.getParameter("status"));

        return customer;
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
