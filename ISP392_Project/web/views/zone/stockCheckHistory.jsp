<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <title>Stock Check History - ${zone.name}</title>
</head>
<body>
    <div class="container mt-4">
        <h1>Stock Check History for Zone: ${zone.name}</h1>
        <c:if test="${not empty sessionScope.Notification}">
            <div class="alert alert-success">${sessionScope.Notification}</div>
            <% session.removeAttribute("Notification"); %>
        </c:if>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Product Name</th>
                    <th>System Quantity</th>
                    <th>Actual Quantity</th>
                    <th>Difference</th>
                    <th>Checked By</th>
                    <th>Check Date</th>
                    <th>Note</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="entry" items="${stockCheckHistory}">
                    <tr>
                        <td>${entry.check_id}</td>
                        <td>${entry.product_name}</td>
                        <td>${entry.system_quantity}</td>
                        <td>${entry.actual_quantity}</td>
                        <td>${entry.difference}</td>
                        <td>${entry.checked_by}</td>
                        <td>${entry.check_date}</td>
                        <td>${entry.note}</td>
                        <td>${entry.status}</td>
                    </tr>
                </c:forEach>
                <c:if test="${empty stockCheckHistory}">
                    <tr><td colspan="9">No stock check history available.</td></tr>
                </c:if>
            </tbody>
        </table>
        <a href="zones?service=zones" class="btn btn-secondary">Back to Zones</a>
    </div>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
</body>
</html>