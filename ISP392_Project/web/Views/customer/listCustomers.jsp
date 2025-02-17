<%-- 
    Document   : listCustomers
    Created on : Feb 14, 2025, 5:03:55 PM
    Author     : THC
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Customer List</title>
    </head>
    <body>
        <h1>Customers</h1>
        <div class="search-box">
            <input type="text" id="myInput" class="input-box" list="browsers" name="browser" id="browser" placeholder="Search for a product..."
                   autocomplete="off"
                   />
            <datalist id="browsers">
                <c:forEach var="product" items="${list}">
                    <option value="${product.name}">
                        ${product.name}
                    </option>
                </c:forEach>
            </datalist>
            <button type="submit" class="search-btn">
                <i class="fa-solid fa-search"></i>
            </button>
        </div>
        <div class="action-bar d-flex align-items-center">
            <div class="dropdown">
                <button type="button" class="btn btn-outline-primary dropdown-toggle mr-2" data-toggle="dropdown">Choose arrange</button>
                <div class="dropdown-menu">
                    <form action="Customers" method="POST" class="dropdown-item">
                        <input type="hidden" name="service" value="customers" />
                        <input type="hidden" name="command" value="name" />
                        <button class="btn btn-outline-primary" type="submit">Name A-Z</button>
                    </form>
                    <form action="Customers" method="POST" class="dropdown-item">
                        <input type="hidden" name="service" value="customers" />
                        <input type="hidden" name="command" value="balance DESC" />
                        <button class="btn btn-outline-primary" type="submit">Balance High-Low</button>
                    </form>
                </div>
            </div>
            <button type="button" class="btn btn-outline-primary mr-lg-auto" data-toggle="modal" data-target="#addCustomerModal">Add Customer</button>
        </div>

        <!-- Table Container -->
        <div class="table-container">
            <form action="Customers" method="POST">
                <table class="table table-striped table-hover table-bordered" id="myTable">
                    <thead>
                        <tr>
                            <th class="resizable" onclick="sortTable(0)">ID</th>
                            <th class="resizable" onclick="sortTable(1)">Name</th>
                            <th class="resizable">Phone</th>
                            <th class="resizable">Address</th>
                            <th class="resizable" onclick="sortTable(3)">Balance</th>
                            <th class="resizable" onclick="sortTable(4)">Created At</th>
                            <th >Created By</th>
                            <th class="sticky-col">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="customer" items="${list}">
                            <tr>
                                <td>${customer.id}</td>
                                <td>${customer.name}</td>
                                <td>${customer.phone}</td>
                                <td>${customer.address}</td>
                                <td><fmt:formatNumber value="${customer.balance}" type="number" pattern="#,###" /></td>
                                <td>${customer.createdAt}</td>
                                <td>${customer.createdBy}</td>
                                <td class="sticky-col" >
                                    <button type="button" class="btn btn-outline-primary mr-lg-auto" data-toggle="modal" data-target="#">
                                        View
                                    </button>
                                    <button type="button" class="btn btn-outline-danger mr-lg-auto" data-toggle="modal" data-target="#debtListModal${customer.id}">
                                        Debt note
                                    </button>
                                    <button type="button" class="btn btn-outline-warning" data-toggle="modal" data-target="#editCustomerModal${customer.id}">Edit</button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </form>
        </div>

        <!-- Pagination -->
        <div class="container d-flex justify-content-center mt-4">
            <ul class="pagination">
                <c:if test="${index > 1}">
                    <li class="page-item">
                        <form action="Customers" method="POST" style="display: inline;">
                            <input type="hidden" name="service" value="customers" />
                            <input type="hidden" name="index" value="${index - 1}" />
                            <button type="submit" class="page-link"><<</button>
                        </form>
                    </li>
                </c:if>

                <c:forEach begin="1" end="${endPage}" var="page">
                    <li class="page-item ${index == page ? 'active' : ''}">
                        <form action="Customers" method="POST" style="display: inline;">
                            <input type="hidden" name="service" value="customers" />
                            <input type="hidden" name="index" value="${page}" />
                            <button type="submit" class="page-link">${page}</button>
                        </form>
                    </li>
                </c:forEach>

                <c:if test="${index < endPage}">
                    <li class="page-item">
                        <form action="Customers" method="POST" style="display: inline;">
                            <input type="hidden" name="service" value="customers" />
                            <input type="hidden" name="index" value="${index + 1}" />
                            <button type="submit" class="page-link">>></button>
                        </form>
                    </li>
                </c:if>
            </ul>
        </div>
    </body>
</html>
