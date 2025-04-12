<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container-fluid p-3">
    <h4 class="mb-3">Order ID: ${order.id} - ${order.type}</h4>

    <div class="table-responsive" style="max-height: 400px; overflow-y: auto;">
        <table class="table table-bordered table-hover">
            <thead class="thead-light sticky-top bg-light">
                <tr>
                    <th style="min-width: 150px;">Product Name</th>
                    <th style="min-width: 120px;">Unit Price</th>
                    <th style="min-width: 100px;">Weight</th>
                    <th style="min-width: 120px;">Total</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${details}">
                    <tr>
                        <td>${item.productName}</td>
                        <td><fmt:formatNumber value="${item.unitPrice}" pattern="#,###" /></td>
                        <td>${item.quantity} Kg</td>
                        <td><fmt:formatNumber value="${item.unitPrice * item.quantity}" pattern="#,###" /></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="mt-3">
        <p><strong>Total Amount:</strong> <fmt:formatNumber value="${totalAmount}" pattern="#,###" /></p>
        <p><strong>Paid:</strong> <fmt:formatNumber value="${order.paidAmount}" pattern="#,###" /></p>
    </div>
</div>
