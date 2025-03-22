<%-- 
    Document   : productsList
    Created on : Jan 30, 2025, 6:03:10 PM
    Author     : phamh
--%>


<!DOCTYPE html>
<html>
    <head>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
        <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Debts</h1>
        <div class="search-box">
            <input type="text" id="myInput" class="input-box" list="browsers" name="browser" id="browser" placeholder="Search for a product..."
                   autocomplete="off"
                   />
            <button type="submit" class="search-btn">
                <i class="fa-solid fa-search"></i>
            </button>
        </div>
        <div class="action-bar d-flex align-items-center">
            <button type="button" class="btn btn-outline-primary mr-lg-auto" data-toggle="modal" data-target="#addDebtModal">
                Add Debt
            </button>
        </div>
        <c:remove var="customer_id" scope="session" />

        <br>
        <div class="table-container">
            <form action="Debts" method="POST">
                <table class="table table-striped table-hover table-bordered" style="color: var(--heading-clr);">
                    <thead>
                        <tr>
                            <th style="width: 50px;" class="resizable" onclick="sortTable(0)">ID</th>
                            <th style="width: 150px;" class="resizable" onclick="sortTable(1)">Total amount</th>
                            <th style="width: 150px;" class="resizable" onclick="sortTable(2)">Description</th>
                            <th style="width: 150px;" class="resizable" onclick="sortTable(3)">Created At</th>
                            <th style="width: 150px;" class="resizable" onclick="sortTable(4)">Updated At</th>
                            <th style="width: 120px;" class="resizable" onclick="sortTable(5)">Created By</th>
                            <th style="width: 120px;" class="resizable">Status</th>
                            <th class="sticky-col1">Action</th>
                        </tr>
                    </thead>
                    <tbody id="myTable">
                        <c:forEach var="debt" items="${list}">
                            <tr>
                                <td>${debt.getId()}</td>
                                <td>
                                   <fmt:formatNumber value="${debt.amount}" pattern="###,##0"/>
                                </td>
                                <td style="white-space: normal; word-wrap: break-word; max-width: 200px;">
                                    ${debt.description}
                                </td>
                                <td>${debt.getCreatedAt()}</td>
                                <td>${debt.getUpdatedAt()}</td>
                                <td>${debt.getCreatedBy()}</td>
                                <td>${debt.getStatus()}</td>
                                <td class="sticky-col1">
                                    <div class="btn-group">
                                        <a class="btn btn-outline-info"
                                           href="Debts?service=debtHistory&id=${debt.getCustomer_id()}">
                                            <i class="fas fa-info-circle"></i>
                                        </a>

                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </form>
        </div>

        <div class="container d-flex justify-content-center mt-4">
            <ul class="pagination">
                <!-- N�t Previous -->
                <c:if test="${index > 1}">
                    <li class="page-item">
                        <a class="page-link" href="Debts?service=debts&index=${index - 1}&pageSize=${pageSize}">&laquo;</a>
                    </li>
                </c:if>

                <!-- C�c trang -->
                <c:forEach var="i" begin="1" end="${endPage}">
                    <li class="page-item ${i == index ? 'active' : ''}">
                        <a class="page-link" href="Debts?service=debts&index=${i}&pageSize=${pageSize}">${i}</a>
                    </li>
                </c:forEach>

                <!-- N�t Next -->
                <c:if test="${index < endPage}">
                    <li class="page-item">
                        <a class="page-link" href="Debts?service=debts&index=${index + 1}&pageSize=${pageSize}">&raquo;</a>
                    </li>
                </c:if>
            </ul>
        </div>


    </body>
</html>
