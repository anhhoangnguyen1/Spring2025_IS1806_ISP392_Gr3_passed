/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.customer;

import dal.customerDAO;
import dal.debtDAO;
import entity.*;
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
                String fullName = (String) session.getAttribute("fullName");
                request.setAttribute("fullName", fullName);
                request.setAttribute("sortOrder", sortOrder);
                request.getRequestDispatcher("views/customer/addCustomer.jsp").forward(request, response);
                break;
            }

            case "editCustomer": {
                int customerId = Integer.parseInt(request.getParameter("customer_id"));
                request.setAttribute("customer", customerDAO.getCustomerById(customerId));

                String fullName = (String) session.getAttribute("fullName");
                request.setAttribute("fullName", fullName);
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
            String phone = request.getParameter("phone");

            if (customerDAO.checkPhoneExists(phone, 0)) {
                request.setAttribute("phoneError", "Phone number already exists.");
                request.setAttribute("customer", getCustomerFromRequest(request, true));
                request.getRequestDispatcher("views/customer/addCustomer.jsp").forward(request, response);
                return;
            }

            if (!phone.matches("^0\\d{9}$")) {
                request.setAttribute("phoneError", "Invalid phone number format.");
                request.setAttribute("customer", getCustomerFromRequest(request, true));
                request.getRequestDispatcher("views/customer/addCustomer.jsp").forward(request, response);
                return;
            }

            Customers customer = getCustomerFromRequest(request, true);
            customer.setBalance(0.0);
            customerDAO.insertCustomer(customer);

            response.sendRedirect("Customers?service=customers");
            return;
        }

        if ("editCustomer".equals(service)) {
            int customerId = Integer.parseInt(request.getParameter("customer_id"));
            String phone = request.getParameter("phone");

            if (customerDAO.checkPhoneExists(phone, customerId)) {
                request.setAttribute("phoneError", "Phone number already exists.");
                request.setAttribute("customer", getCustomerFromRequest(request, false));
                request.getRequestDispatcher("views/customer/editCustomer.jsp").forward(request, response);
                return;
            }

            if (!phone.matches("^0\\d{9}$")) {
                request.setAttribute("phoneError", "Invalid phone number format.");
                request.setAttribute("customer", getCustomerFromRequest(request, false));
                request.getRequestDispatcher("views/customer/editCustomer.jsp").forward(request, response);
                return;
            }

            Customers customer = getCustomerFromRequest(request, false);
            customerDAO.editCustomer(customer);

            HttpSession session = request.getSession();
            session.setAttribute("successMessage", "Customer details updated successfully.");
            response.sendRedirect("Customers?service=customers&sortOrder=" + request.getParameter("sortOrder"));
        }
    }

    private Customers getCustomerFromRequest(HttpServletRequest request, boolean isNew) {
        Customers.CustomersBuilder customerBuilder = Customers.builder();

        if (!isNew) {
            customerBuilder.id(Integer.parseInt(request.getParameter("customer_id")));
        }

        Stores store = null;
        if (request.getParameter("store_id") != null && !request.getParameter("store_id").isEmpty()) {
            store = new Stores();
            store.setId(Integer.parseInt(request.getParameter("store_id")));
        }

        HttpSession session = request.getSession();
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
