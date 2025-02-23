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

        switch (service) {
            case "customers": {
                String keyword = request.getParameter("searchCustomer");
                if (keyword == null) {
                    keyword = "";
                }

                int index = 1;
                try {
                    index = Integer.parseInt(request.getParameter("index"));
                } catch (NumberFormatException ignored) { }

                int total = customerDAO.countCustomers(keyword);
                int endPage = (total % 5 == 0) ? total / 5 : (total / 5) + 1;

                List<Customers> list = customerDAO.searchCustomers(keyword, index, 5);

                request.setAttribute("list", list);
                request.setAttribute("endPage", endPage);
                request.setAttribute("index", index);
                request.setAttribute("searchCustomer", keyword);

                request.getRequestDispatcher("views/customer/customers.jsp").forward(request, response);
                break;
            }
            case "getCustomerById":{

                int id = Integer.parseInt(request.getParameter("customer_id"));
                Customers customer = customerDAO.getCustomerById(id);
                request.setAttribute("customer", customer);
                request.getRequestDispatcher("views/customer/detailCustomer.jsp").forward(request, response);
                break;
            }
            case "addCustomer":{
                String createdBy = (String) session.getAttribute("username");
                request.setAttribute("userName", createdBy);
                request.getRequestDispatcher("views/customer/addCustomer.jsp").forward(request, response);
                break;}

            case "editCustomer":
            {
                int customerId = Integer.parseInt(request.getParameter("customer_id"));
                Customers customerForEdit = customerDAO.getCustomerById(customerId);
                request.setAttribute("customer", customerForEdit);
                String userName = (String) session.getAttribute("username");
                request.setAttribute("userName", userName);
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
            Customers customer = Customers.builder()
                    .id(Integer.parseInt(request.getParameter("customer_id")))
                    .name(request.getParameter("name"))
                    .phone(request.getParameter("phone"))
                    .address(request.getParameter("address"))
                    .balance(Double.parseDouble(request.getParameter("balance")))
                    .updatedBy(request.getParameter("updatedBy"))
                    .status(request.getParameter("status"))
                    .build();
            customerDAO.editCustomer(customer);
            response.sendRedirect("Customers?service=customers");
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

