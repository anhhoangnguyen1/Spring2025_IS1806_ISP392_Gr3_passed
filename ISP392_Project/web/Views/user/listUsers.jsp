<%-- 
    Document   : listUsers
    Created on : Feb 18, 2025, 12:30:58 AM
    Author     : THC
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>User List</title>
    </head>
    <body>
        <div class="container mt-4">
            <h1>Staff List</h1>

            <!-- Search Box -->
            <div class="search-box">
                <input type="text" id="myInput" class="input-box" placeholder="Search for a user..." autocomplete="off" />
                <button type="submit" class="search-btn">
                    <i class="fa-solid fa-search"></i>
                </button>
            </div>

            <!-- Table Container -->
            <div class="table-container mt-4">
                <form action="Users" method="POST">
                    <table class="table table-striped table-hover table-bordered" id="myTable">
                        <thead>
                            <tr>
                                <th class="resizable">ID</th>
                                <th class="resizable">Role</th>
                                <th class="resizable">Name</th>
                                <th class="resizable">Phone</th>
                                <th class="resizable">Address</th>
                                <th class="resizable">Gender</th>
                                <th class="resizable">Date of birth</th>
                                <th class="resizable">Email</th>
                                <th class="resizable">Status</th>
                                <th class="sticky-col">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="user" items="${userList}">
                                <tr>
                                    <td>${user.id}</td>
                                    <td>${user.role}</td>
                                    <td>${user.name}</td>
                                    <td>${user.phone}</td>
                                    <td>${user.address}</td>
                                    <td>${user.gender}</td>
                                    <td>${user.dob}</td>
                                    <td>${user.email}</td>
                                    <td>${user.status}</td>
                                    <td class="sticky-col">
                                        <a href="${pageContext.request.contextPath}/Users?service=editUser&user_id=${user.id}" class="btn btn-outline-primary">
                                            View
                                        </a>
                                        
                                        <button type="button" class="btn btn-outline-danger" data-toggle="modal" data-target="#">
                                            Ban
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </form>
            </div>
        </div>

        <!-- Pagination -->
        <div class="container d-flex justify-content-center mt-4">
            <ul class="pagination">
                <c:if test="${index > 1}">
                    <li class="page-item">
                        <form action="Users" method="POST" style="display: inline;">
                            <input type="hidden" name="service" value="users" />
                            <input type="hidden" name="index" value="${index - 1}" />
                            <button type="submit" class="page-link"><<</button>
                        </form>
                    </li>
                </c:if>

                <c:forEach begin="1" end="${endPage}" var="page">
                    <li class="page-item ${index == page ? 'active' : ''}">
                        <form action="Users" method="POST" style="display: inline;">
                            <input type="hidden" name="service" value="users" />
                            <input type="hidden" name="index" value="${page}" />
                            <button type="submit" class="page-link">${page}</button>
                        </form>
                    </li>
                </c:forEach>

                <c:if test="${index < endPage}">
                    <li class="page-item">
                        <form action="Users" method="POST" style="display: inline;">
                            <input type="hidden" name="service" value="users" />
                            <input type="hidden" name="index" value="${index + 1}" />
                            <button type="submit" class="page-link">>></button>
                        </form>
                    </li>
                </c:if>
            </ul>
        </div>

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <!-- Bootstrap JS -->
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
