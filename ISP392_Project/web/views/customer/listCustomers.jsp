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
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Customer List</title>
    </head>
    <body>
        <div class="container mt-4">
            <h1>Customers List</h1>
            <c:if test="${not empty sessionScope.Notification}">
                <div class="alert alert-warning alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert">
                        &times;
                    </button>
                    <strong>${sessionScope.Notification}</strong>
                </div>
                <c:remove var="Notification" scope="session" />
            </c:if>
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success">
                    <strong>Success!</strong> ${sessionScope.successMessage}
                </div>
                <c:set var="successMessage" value="${sessionScope.successMessage}" />
                <c:remove var="successMessage" />
            </c:if>
            <form action="${pageContext.request.contextPath}/Customers" method="GET">
                <div class="search-box">

                    <input type="hidden" name="service" value="customers" />
                    <input type="text" id="myInput" class="input-box" name="searchCustomer"
                           placeholder="Search for information."
                           value="${searchCustomer}" autocomplete="off" />

                    <button type="button" class="clear-btn"
                            onclick="window.location.href = '${pageContext.request.contextPath}/Customers?service=customers'">
                        <i class="fa-solid fa-xmark"></i>
                    </button>

                    <button type="submit" class="search-btn">
                        <i class="fa-solid fa-search"></i>
                    </button>

                </div>
            </form>

            <div class="action-bar d-flex align-items-center">
                <a href="${pageContext.request.contextPath}/Customers?service=addCustomer" class="btn btn-outline-primary mr-lg-auto">
                    Add Customer
                </a>
            </div>
            <!-- Table Container -->
            <div class="table-container mt-4">
                <form action="Customers" method="POST">
                    <table class="table table-striped table-hover table-bordered" id="myTable">
                        <thead>
                            <tr>
                                <th class="resizable">
                                    <a href="Customers?service=customers&searchCustomer=${searchCustomer}&index=${index}&sortBy=id&sortOrder=${sortBy == 'id' && sortOrder == 'ASC' ? 'DESC' : 'ASC'}">
                                        ID <i class="fa ${sortBy == 'id' && sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down'}"></i>
                                    </a>
                                </th>
                                <th class="resizable">Name</th>
                                <th class="resizable">Phone</th>
                                <th class="resizable">Address</th>
                                <th class="resizable">
                                    <a href="Customers?service=customers&searchCustomer=${searchCustomer}&index=${index}&sortBy=balance&sortOrder=${sortBy == 'balance' && sortOrder == 'ASC' ? 'DESC' : 'ASC'}">
                                        Balance <i class="fa ${sortBy == 'balance' && sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down'}"></i>
                                    </a>
                                </th>
                                <th class="resizable">Created At</th>
                                <th class="resizable">Created By</th>
                                <th class="sticky-col">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="customer" items="${list}">
                                <tr>
                                    <td>${customer.id}</td>
                                    <td>${customer.name}</td>
                                    <td>
                                        <c:if test="${sessionScope.role == 'owner'}">
                                            ${customer.phone} 
                                        </c:if>
                                        <c:if test="${sessionScope.role == 'staff'}">
                                            ${customer.phone.substring(0, 3)}xxxxx${customer.phone.substring(customer.phone.length() - 2)} <!-- Hiển thị số điện thoại ẩn nếu là staff -->
                                        </c:if>
                                    </td>
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
                                        <a href="${pageContext.request.contextPath}/Customers?service=editCustomer&customer_id=${customer.id}" class="btn btn-outline-primary">
                                            Edit
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </form>
            </div>
        </div>
        <div class="container d-flex justify-content-center mt-4">
            <ul class="pagination">

                <c:if test="${index > 1}">
                    <li class="page-item">
                        <a class="page-link" href="Customers?service=customers&searchCustomer=${searchCustomer}&index=${index - 1}&sortBy=${sortBy}&sortOrder=${sortOrder}">
                            <i class="fa fa-angle-left"></i>
                        </a>
                    </li>
                </c:if>

                <li class="page-item ${index == 1 ? 'active' : ''}">
                    <a class="page-link" href="Customers?service=customers&searchCustomer=${searchCustomer}&index=1&sortBy=${sortBy}&sortOrder=${sortOrder}">1</a>
                </li>

                <c:if test="${index > 3}">
                    <li class="page-item disabled">
                        <span class="page-link">...</span>
                    </li>
                </c:if>

                <c:forEach begin="${index - 1}" end="${index + 1}" var="page">
                    <c:if test="${page > 1 && page < endPage}">
                        <li class="page-item ${index == page ? 'active' : ''}">
                            <a class="page-link" href="Customers?service=customers&searchCustomer=${searchCustomer}&index=${page}&sortBy=${sortBy}&sortOrder=${sortOrder}">
                                ${page}
                            </a>
                        </li>
                    </c:if>
                </c:forEach>


                <c:if test="${index < endPage - 2}">
                    <li class="page-item disabled">
                        <span class="page-link">...</span>
                    </li>
                </c:if>


                <c:if test="${endPage > 1}">
                    <li class="page-item ${index == endPage ? 'active' : ''}">
                        <a class="page-link" href="Customers?service=customers&searchCustomer=${searchCustomer}&index=${endPage}&sortBy=${sortBy}&sortOrder=${sortOrder}">
                            ${endPage}
                        </a>
                    </li>
                </c:if>


                <c:if test="${index < endPage}">
                    <li class="page-item">
                        <a class="page-link" href="Customers?service=customers&searchCustomer=${searchCustomer}&index=${index + 1}&sortBy=${sortBy}&sortOrder=${sortOrder}">
                            <i class="fa fa-angle-right"></i>
                        </a>
                    </li>
                </c:if>
            </ul>
        </div>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
