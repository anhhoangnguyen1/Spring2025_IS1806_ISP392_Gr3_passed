<%-- 
    Document   : productsList
    Created on : Jan 30, 2025, 6:03:10 PM
    Author     : phamh
--%>


<!DOCTYPE html>
<html>
    <head>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
        <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <style>.search-box {
                position: absolute;
                top: 20px; /* D�ng gi� tr? �m ?? ??y l�n tr�n */
                left: 50%;
                transform: translateX(-50%);
                width: 400px;
                height: 37px;
                background-color: var(--bg-ternary);
                border-radius: 50px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                z-index: 999; /* ??m b?o hi?n th? tr�n c�ng */
            }
            .alert {
                position: fixed;
                top: 10px;
                left: 50%;
                transform: translateX(-50%);
                max-width: 500px;
                z-index: 1050;
                opacity: 1;
                transition: opacity 0.5s ease-in-out;
            }
        </style>
        <h1>Products</h1>
        <form action="${pageContext.request.contextPath}/Products" method="POST">
            <div class="search-box">
                <input type="hidden" name="service" value="searchProducts" />
                <input type="text" class="input-box" name="browser"
                       placeholder="Search for products."
                       value="${name}" autocomplete="off" />

                <button type="button" class="clear-btn"
                        onclick="window.location.href = '${pageContext.request.contextPath}/Products'">
                    <i class="fa-solid fa-xmark"></i>
                </button>

                <button type="submit" class="search-btn">
                    <i class="fa-solid fa-search"></i>
                </button>
            </div>
        </form>

        <!-- Other content comes after this -->

        <div class="action-bar d-flex align-items-center">
            <c:if test="${sessionScope.role == 'owner'}">
                <button type="button" class="btn btn-outline-primary mr-lg-auto" data-toggle="modal" data-target="#addProductModal">
                    Add Product
                </button>  
            </c:if>

        </div>
        <br>
        <div class="table-container">

            <table id="myTable" class="table table-striped table-hover table-bordered" style="color: var(--heading-clr);">
                <thead>
                    <tr>
                        <th class="checkbox-column" style="display: none; width: 20px">
                            <input type="checkbox" id="select-all" onclick="toggleSelectAll(this)" />
                        </th>
                        <th class="resizable" style="width: 50px" onclick="sortTable(1)">ID</th>
                        <th class="resizable" style="width: 150px" onclick="sortTable(2)">Name</th>
                        <th class="resizable" style="width: 150px" onclick="sortTable(3)">Image</th>
                        <th class="resizable" style="width: 70px" onclick="sortTable(4)">Price</th>
                        <th class="resizable" style="width: 70px" onclick="sortTable(5)">Zone name</th>
                        <th class="resizable" style="width: 85px" onclick="sortTable(6)">Quantity</th>
                        <th class="resizable" style="width: 400px" onclick="sortTable(7)">Description</th>
                        <th class="resizable" style="width: 150px" onclick="sortTable(8)">Created At</th>
                        <th class="resizable" style="width: 150px" onclick="sortTable(9)">Updated At</th>
                        <th class="resizable" style="width: 150px" onclick="sortTable(10)">Status</th>
                            <c:if test="${sessionScope.role == 'owner'}">
                            <th class="sticky-col1">Action</th>
                            </c:if>

                    </tr>

                </thead>
                <tbody id="myTable">
                    <c:choose>
                        <c:when test="${empty list}">
                            <tr>
                                <td colspan="12" class="text-center text-muted">
                                    No products found
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="product" items="${list}">
                                <tr>
                                    <td class="checkbox-column" style="display: none;">
                                        <input type="checkbox" name="selectedProducts" value="${product.productId}" class="product-checkbox" />
                                    </td>
                                    <td>${product.productId}</td>
                                    <td class="bold-row">${product.name}</td>
                                    <td>
                                        <img src="/ISP392_Project/views/product/images/${product.image}" class="myImg" style="width: 100px; height: 100px; object-fit: cover;" alt="Product Image" />
                                    </td>
                                    <td><fmt:formatNumber value="${product.price}" pattern="###,##0"/></td>
                                    <td>${product.zoneName}</td>
                                    <td>${product.quantity} Kg</td>
                                    <td>${product.description}</td>
                                    <td>${product.createdAt}</td>
                                    <td>${product.updatedAt}</td>
                                    <td>
                                        <h5>
                                            <div class="badge rounded-pill bg-secondary" style="color: white">${product.status}</div>
                                        </h5>
                                    </td>
                                    <c:if test="${sessionScope.role == 'owner'}">
                                        <td class="sticky-col1">
                                            <div class="btn-group">
                                                <form action="Products" method="POST">
                                                    <button type="button" class="btn btn-outline-warning" data-toggle="modal" data-target="#editProductModal${product.productId}">
                                                        <i class="fas fa-edit"></i>
                                                    </button>
                                                </form>
                                                        <form action="Products" method="POST">
                                                            <input type="hidden" name="service" value="UpdateStatusServlet">
                                                            <input type="hidden" name="productId" value="${product.productId}" />
                                                            <input type="hidden" name="status" value="${product.status == 'Active' ? 'Inactive' : 'Active'}" />
                                                            <button type="submit" class="btn ${product.status == 'Active' ? 'btn-primary' : 'btn-secondary'}">
                                                                ${product.status}
                                                            </button>
                                                        </form>
                                                
                                            </div>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>

            </table>




        </div>
        <form action="${pageContext.request.contextPath}/Products" method="POST">
            <span class="mr-2">Showing:</span>
            <input type="hidden" name="index" value="${index}" />
            <select class="btn-outline-primary mr-lg-auto" id="pageSize" name="pageSize" onchange="this.form.submit()">
                <option value="5" ${pageSize == 5 ? 'selected' : ''}>5</option>
                <option value="10" ${pageSize == 10 ? 'selected' : ''}>10</option>
                <option value="15" ${pageSize == 15 ? 'selected' : ''}>15</option>
                <option value="20" ${pageSize == 20 ? 'selected' : ''}>20</option>
            </select>
            <span>of ${totalProducts} products</span>
        </form>
        <div class="container d-flex justify-content-center mt-4" >
            <ul class="pagination" >
                <!-- Previous Page -->
                <c:if test="${index > 1}">
                    <li class="page-item">
                        <form action="Products" method="POST" style="display: inline;">
                            <input type="hidden" name="service" value="products" />
                            <input type="hidden" name="index" value="${index - 1}" />
                            <button type="submit" class="page-link" ><<</button>
                        </form>
                    </li>
                </c:if>

                <!-- Page Numbers -->

                <c:forEach begin="1" end="${endPage}" var="page">
                    <li class="page-item ${index == page ? 'active' : ''}">
                        <form action="Products" method="POST" style="display: inline;">
                            <input type="hidden" name="service" value="products" />
                            <input type="hidden" name="index" value="${page}" />
                            <button type="submit" class="page-link">${page}</button>
                        </form>
                    </li>
                </c:forEach>

                <!-- Next Page -->
                <c:if test="${index < endPage}">
                    <li class="page-item">
                        <form action="Products" method="POST" style="display: inline;">
                            <input type="hidden" name="service" value="products" />
                            <input type="hidden" name="index" value="${index + 1}" />
                            <button type="submit" class="page-link">>></button>
                        </form>
                    </li>
                </c:if>
            </ul>
        </div>

    </body>
</html>