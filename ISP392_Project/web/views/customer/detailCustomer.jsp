<%-- 
    Document   : detailCustomer
    Created on : Feb 14, 2025, 5:03:07 PM
    Author     : THC
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css?v=1.0" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
    <title>Customer Details</title>
</head>
<body>
    <style>
        .customer-container {
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background-color: #f9f9f9;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .customer-detail {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #ddd;
        }
        .customer-detail label {
            font-weight: bold;
            color: #333;
        }
        .customer-detail span {
            text-align: right;
        }
    </style>
    <a href="Customers?service=customers" class="btn btn-outline-secondary">Exit</a>
    <div class="container">
        <div class="customer-container">
            <h2>Customer Details</h2>
            <c:forEach var="customer" items="${list}">
                <div class="customer-detail"><label>Customer ID:</label><span>${customer.id}</span></div>
                <div class="customer-detail"><label>Name:</label><span>${customer.name}</span></div>
                <div class="customer-detail"><label>Phone:</label><span>${customer.phone}</span></div>
                <div class="customer-detail"><label>Address:</label><span>${customer.address}</span></div>
                <div class="customer-detail"><label>Balance:</label><span>$${customer.balance}</span></div>
                <div class="customer-detail"><label>Created By:</label><span>${customer.createdBy}</span></div>
                <div class="customer-detail"><label>Updated By:</label><span>${customer.updatedBy}</span></div>
                <div class="customer-detail"><label>Status:</label><span>${customer.status}</span></div>
                <div style="display: flex; gap: 20px;">
                    <button type="button" class="btn btn-outline-warning" data-toggle="modal" data-target="#editCustomerModal${customer.id}">Edit Customer</button>
                </div>
            </c:forEach>
        </div>
    </div>
    <c:import url="editCustomer.jsp" />
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
