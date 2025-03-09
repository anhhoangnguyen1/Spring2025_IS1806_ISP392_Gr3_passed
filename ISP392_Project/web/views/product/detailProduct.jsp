<%-- 
    Document   : detailProduct
    Created on : Jan 27, 2025, 9:28:37 PM
    Author     : phamh
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css?v=1.0" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
        <title>JSP Page</title>
    </head>
    <body>
        <style>
            .product-container {
                max-width: 800px;
                margin: 20px auto;
                padding: 20px;
                Exitground-color: #f9f9f9;
                border-radius: 10px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            }
            .product-container h2 {
                text-align: center;
                color: #5c6bc0;
                margin-bottom: 20px;
            }
            .product-detail {
                display: flex;
                justify-content: space-between;
                padding: 10px 0;
                border-bottom: 1px solid #ddd;
            }
            .product-detail:last-child {
                border-bottom: none;
            }
            .product-detail label {
                font-weight: bold;
                color: #333;
            }
            .product-detail span, .product-detail img {
                text-align: right;
            }
            .product-detail img {
                max-width: 150px;
                border-radius: 5px;
            }
        </style>
        <a href="Products?service=products" class="btn btn-outline-secondary">Exit</a>
        <div class="container">
            <div class="product-container">
                <h2>Product Details</h2>
                <c:forEach var="product" items="${list}">

                    <div class="product-detail">
                        <label>Product ID:</label>
                        <span>${product.getProductId()}</span>
                    </div>
                    <div class="product-detail">
                        <label>Name:</label>
                        <span>${product.getName()}</span>
                    </div>
                    <div class="product-detail">
                        <label>Image:</label>
                        <img src="${pageContext.request.contextPath}/images/${product.getImage()}" alt="Product Image" style="width: 30%">
                    </div>
                    <div class="product-detail">
                        <label>Price:</label>
                        <span>$${product.getPrice()}</span>
                    </div>
                    <div class="product-detail">
                        <label>Wholesale Price:</label>
                        <span>$${product.getWholesalePrice()}</span>
                    </div>
                    <div class="product-detail">
                        <label>Retail Price:</label>
                        <span>$${product.getRetailPrice()}</span>
                    </div>
                    <div class="product-detail">
                        <label>Weight:</label>
                        <span>${product.getWeight()} kg</span>
                    </div>
                    <div class="product-detail">
                        <label>Location:</label>
                        <span>${product.getLocation()}</span>
                    </div>
                    <div class="product-detail">
                        <label>Description:</label>
                        <span>${product.getDescription()}</span>
                    </div>
                    <div class="product-detail">
                        <label>Status:</label>
                        <span>${product.getStatus()}</span>
                    </div>
                    <div style="display: flex; gap: 450px; align-items: center;">
                        <button type="button" class="btn btn-outline-warning" data-toggle="modal" data-target="#editProductModal${product.getProductId()}">
                            Edit product
                        </button> 
                        <form action="Products" method="POST" onsubmit="return doDelete(${product.productId})">
                            <input type="hidden" name="service" value="deleteProduct" />
                            <input type="hidden" name="id" value="${product.productId}" />
                            <input type="submit" class="btn btn-outline-danger" value="Delete product" />
                        </form>
                    </div>

                </c:forEach>


            </div>
        </div>
         <c:import url="editProduct.jsp" />
        <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/css/script.js"></script>
        
    </body>
</html>
