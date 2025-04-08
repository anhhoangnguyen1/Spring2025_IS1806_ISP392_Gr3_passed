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
                    <th style="min-width: 100px;">Quantity</th>
                    <th style="min-width: 120px;">Total</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${details}">
                    <tr>
                        <td>${item.productName}</td>
                        <td><fmt:formatNumber value="${item.unitPrice}" type="currency"/></td>
                        <td>${item.quantity}</td>
                        <td><fmt:formatNumber value="${item.unitPrice * item.quantity}" type="currency"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="mt-3">
        <p><strong>Total Amount:</strong> <fmt:formatNumber value="${totalAmount}" type="currency"/></p>
        <p><strong>Paid:</strong> <fmt:formatNumber value="${order.paidAmount}" type="currency"/></p>
    </div>
</div>
