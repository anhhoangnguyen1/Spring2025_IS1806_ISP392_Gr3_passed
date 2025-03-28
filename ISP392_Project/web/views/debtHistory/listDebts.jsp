
<!DOCTYPE html>
<html>
    <head>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
        <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Debts History</h1>
        <form action="${pageContext.request.contextPath}/Debts" method="POST">
            <div class="search-box">
                <input type="hidden" name="service" value="searchDebts" />
                <input type="text" class="input-box" name="browser"
                       placeholder="Search for products."
                       value="${name}" autocomplete="off" />

                <button type="button" class="clear-btn"
                        onclick="window.location.href = '${pageContext.request.contextPath}/Debts'">
                    <i class="fa-solid fa-xmark"></i>
                </button>

                <button type="submit" class="search-btn">
                    <i class="fa-solid fa-search"></i>
                </button>
            </div>
        </form>
        <div class="action-bar d-flex align-items-center">
            <button type="button" class="btn btn-outline-primary mr-lg-auto" data-toggle="modal" data-target="#addDebtModal">
                Add Debt
            </button>
        </div>
        <c:remove var="customer_id" scope="session" />

        <br>
        <div class="table-container">
            <form action="Debts" method="POST">
                <table id="myTable" class="table table-striped table-hover table-bordered" style="color: var(--heading-clr);">
                    <thead>
                        <tr>
                            <th style="width: 50px;" class="resizable" onclick="sortTable(0)">ID</th>
                            <th style="width: 150px;" class="resizable" onclick="sortTable(1)">Total amount</th>
                            <th style="width: 50px;" class="resizable" onclick="sortTable(2)">Customer name</th>
                            <th style="width: 50px;" class="resizable" onclick="sortTable(3)">Phone</th>
                            <th style="width: 50px;" class="resizable" onclick="sortTable(4)">Address</th>
                            <th style="width: 150px;" class="resizable" onclick="sortTable(5)">Description</th>
                            <th style="width: 150px;" class="resizable" onclick="sortTable(6)">Created At</th>
                            <th style="width: 150px;" class="resizable" onclick="sortTable(7)">Updated At</th>
                            <th style="width: 120px;" class="resizable" onclick="sortTable(8)">Created By</th>
                            <th style="width: 120px;" class="resizable">Status</th>
                            <th class="sticky-col1">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty list}">
                                <c:forEach var="debt" items="${list}">
                                    <tr>
                                        <td>${debt.getId()}</td>
                                        <td>
                                            <fmt:formatNumber value="${debt.amount}" pattern="###,##0"/>
                                        </td>
                                        <td>${debt.name}</td>
                                        <td>${debt.phone}</td>
                                        <td>${debt.address}</td>
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
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="11" class="text-center text-muted">No debts found</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </form>
        </div>
        <form action="${pageContext.request.contextPath}/Debts" method="POST">
            <span class="mr-2">Showing:</span>
            <input type="hidden" name="index" value="${index}" />
            <select class="btn-outline-primary mr-lg-auto" id="pageSize" name="pageSize" onchange="this.form.submit()">
                <option value="5" ${pageSize == 5 ? 'selected' : ''}>5</option>
                <option value="10" ${pageSize == 10 ? 'selected' : ''}>10</option>
                <option value="15" ${pageSize == 15 ? 'selected' : ''}>15</option>
                <option value="20" ${pageSize == 20 ? 'selected' : ''}>20</option>
            </select>
            <span>of ${totalDebts} debts</span>
        </form>
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
