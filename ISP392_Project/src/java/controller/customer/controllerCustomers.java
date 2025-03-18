/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.customer;

import dal.*;
import entity.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
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
        String role = (String) session.getAttribute("role");

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
                    if (index < 1) {
                        index = 1; 
                    }
                } catch (NumberFormatException ignored) {
                    
                }

                int total = customerDAO.countCustomers(keyword);
                int endPage = (total % 5 == 0) ? total / 5 : (total / 5) + 1;

                if (index > endPage) {
                    index = endPage;
                }
                List<Customers> list = customerDAO.searchCustomers(keyword, index, 5, sortBy, sortOrder);
                if ("staff".equals(role)) {
                    for (Customers customer : list) {
                        String phone = customer.getPhone();
                        if (phone != null && phone.length() > 6) {
                            customer.setPhone(phone.substring(0, 3) + "xxxxx" + phone.substring(phone.length() - 2));
                        }
                    }
                }

                String notification = (String) request.getSession().getAttribute("Notification");
                if (notification != null) {
                    request.setAttribute("Notification", notification);
                    request.getSession().removeAttribute("Notification");
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
            case "searchCustomersAjax": {
                String keyword = request.getParameter("searchCustomer");
                if (keyword == null) {
                    keyword = "";
                }
                int index = 1;
                try {
                    index = Integer.parseInt(request.getParameter("index"));
                } catch (NumberFormatException ignored) {
                }

                int pageSize = 5;
                int total = customerDAO.countCustomers(keyword);
                int endPage = (total % pageSize == 0) ? total / pageSize : (total / pageSize) + 1;
                List<Customers> customers = customerDAO.searchCustomers(keyword, index, pageSize, sortBy, sortOrder);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print("{");
                out.print("\"customers\": [");
                for (int i = 0; i < customers.size(); i++) {
                    Customers customer = customers.get(i);
                    out.print("{");
                    out.print("\"id\": " + customer.getId() + ",");
                    out.print("\"name\": \"" + customer.getName() + "\",");
                    out.print("\"phone\": \"" + customer.getPhone() + "\",");
                    out.print("\"address\": \"" + customer.getAddress() + "\",");
                    out.print("\"balance\": \"" + customer.getBalance() + "\",");
                    out.print("\"createdAt\": \"" + customer.getCreatedAt() + "\",");
                    out.print("\"createdBy\": \"" + customer.getCreatedBy() + "\"");

                    out.print("}");
                    if (i < customers.size() - 1) {
                        out.print(",");
                    }
                }
                out.print("],");
                out.print("\"endPage\": " + endPage + ",");
                out.print("\"index\": " + index);
                out.print("}");
                out.flush();
                break;

            }
            case "getCustomerById": {

                int id = Integer.parseInt(request.getParameter("customer_id"));
                Customers customer = customerDAO.getCustomerById(id);

                if ("staff".equals(role)) {
                    String phone = customer.getPhone();
                    if (phone != null && phone.length() > 6) {
                        customer.setPhone(phone.substring(0, 3) + "xxxxx" + phone.substring(phone.length() - 2));
                    }
                }

                request.setAttribute("customer", customer);
                request.getRequestDispatcher("views/customer/detailCustomer.jsp").forward(request, response);
                break;
            }
            case "addCustomer": {
                String fullName = (String) session.getAttribute("fullName");
                request.setAttribute("fullName", fullName);
                request.setAttribute("sortOrder", sortOrder);
                request.getRequestDispatcher("views/customer/addCustomer.jsp").forward(request, response);
                break;
            }

            case "editCustomer": {
                int customerId = Integer.parseInt(request.getParameter("customer_id"));
                Customers customer = customerDAO.getCustomerById(customerId);
                if ("staff".equals(role)) {
                    String phone = customer.getPhone();
                    if (phone != null && phone.length() > 6) {
                        customer.setPhone(phone);
                    }
                }
                request.setAttribute("customer", customer);
                String fullName = (String) session.getAttribute("fullName");
                request.setAttribute("fullName", fullName);

         
                request.setAttribute("sortBy", sortBy);
                request.setAttribute("sortOrder", sortOrder);
                request.setAttribute("searchCustomer", request.getParameter("searchCustomer"));
                request.setAttribute("index", request.getParameter("index"));

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
            String phone = request.getParameter("phone");

            if (customerDAO.checkPhoneExists(phone, 0)) {
                request.setAttribute("phoneError", "Phone number already exists.");
                request.setAttribute("customer", getCustomerFromRequest(request, true));
                request.getRequestDispatcher("views/customer/addCustomer.jsp").forward(request, response);
                return;
            }

            if (phone == null || !phone.matches("^0\\d{9}$")) {
                request.setAttribute("phoneError", "Invalid phone number format.");
                request.setAttribute("customer", getCustomerFromRequest(request, true));
                request.getRequestDispatcher("views/customer/addCustomer.jsp").forward(request, response);
                return;
            }
            HttpSession session = request.getSession();
            String fullName = (String) session.getAttribute("fullName");
            Customers customer = getCustomerFromRequest(request, true);
            customer.setCreatedBy(fullName);
            customer.setBalance(0.0);
            customerDAO.insertCustomer(customer);

            int total = customerDAO.countCustomers("");
            int pageSize = 5; 
            int endPage = (total % pageSize == 0) ? total / pageSize : (total / pageSize) + 1;
        

            String sortBy = request.getParameter("sortBy");
            if (sortBy == null) {
                sortBy = "id";
            }
            String sortOrder = request.getParameter("sortOrder");
            if (sortOrder == null) {
                sortOrder = "ASC"; 
            }
      
            session.setAttribute("successMessage", "Customer added successfully.");
            response.sendRedirect("Customers?service=customers&sortBy=" + sortBy + "&sortOrder=" + sortOrder + "&index=" + endPage);

            return;
        }

        if ("editCustomer".equals(service)) {
            int customerId = Integer.parseInt(request.getParameter("customer_id"));
            String phone = request.getParameter("phone");
            String searchCustomer = request.getParameter("searchCustomer");
            if (searchCustomer == null) {
                searchCustomer = ""; 
            }
            if (customerDAO.checkPhoneExists(phone, customerId)) {
                request.setAttribute("phoneError", "Phone number already exists.");
                request.setAttribute("customer", getCustomerFromRequest(request, false));
                request.getRequestDispatcher("views/customer/editCustomer.jsp").forward(request, response);
                return;
            }
            if (phone == null || !phone.matches("^0\\d{9}$")) {
                request.setAttribute("phoneError", "Invalid phone number format.");
                request.setAttribute("customer", getCustomerFromRequest(request, false));
                request.getRequestDispatcher("views/customer/editCustomer.jsp").forward(request, response);
                return;
            }
            HttpSession session = request.getSession();
            Customers customer = getCustomerFromRequest(request, false);
            customerDAO.editCustomer(customer);

         
            String indexParam = request.getParameter("index");
            int index = 1; 

            if (indexParam != null && !indexParam.isEmpty()) {
                try {
                    index = Integer.parseInt(indexParam);
                    if (index < 1) {
                        index = 1; 
                    }
                } catch (NumberFormatException ignored) {
                    
                }
            }
            System.out.println("Index from request: " + request.getParameter("index"));
            System.out.println("Parsed index: " + index);
            String sortBy = request.getParameter("sortBy");
            if (sortBy == null) {
                sortBy = "id"; 
            }
            String sortOrder = request.getParameter("sortOrder");
            if (sortOrder == null) {
                sortOrder = "ASC"; 
            }

            session.setAttribute("successMessage", "Customer updated successfully.");

            response.sendRedirect("Customers?service=customers&searchCustomer=" + searchCustomer
                    + "&sortBy=" + sortBy + "&sortOrder=" + sortOrder + "&index=" + index);
        }
    }

    private Customers getCustomerFromRequest(HttpServletRequest request, boolean isNew) {
        Customers.CustomersBuilder customerBuilder = Customers.builder();

        if (!isNew) {
            customerBuilder.id(Integer.parseInt(request.getParameter("customer_id")));
        }

        HttpSession session = request.getSession();
        String storeID = (String) session.getAttribute("storeID");
        Stores store = null;
        if (storeID != null && !storeID.isEmpty()) {
            store = new Stores();
            store.setId(Integer.parseInt(storeID));
        }

        String fullName = (String) session.getAttribute("fullName");

        return customerBuilder
                .name(request.getParameter("name"))
                .phone(request.getParameter("phone"))
                .address(request.getParameter("address"))
                .balance(isNew ? 0.0 : Double.parseDouble(request.getParameter("balance")))
                .createdBy(isNew ? fullName : null)
                .updateBy(request.getParameter("updatedBy"))
                .status(request.getParameter("status") != null ? request.getParameter("status") : "Active")
                .storeId(store)
                .build();
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
