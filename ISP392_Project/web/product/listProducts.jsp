<%-- 
    Document   : productsList
    Created on : Jan 30, 2025, 6:03:10 PM
    Author     : phamh
--%>


<!DOCTYPE html>
<html>
    <head>
        <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Products</h1>
        <div class="action-bar d-flex align-items-center">
            <div class="dropdown">
                <button type="button" class="btn btn-outline-primary dropdown-toggle mr-2" color: var(--heading-clr); data-toggle="dropdown">
                    Choose arrange
                </button>
                <div class="dropdown-menu">
                    <form action="Products" method="POST" class="dropdown-item">
                        <input type="hidden" name="service" value="products" />
                        <input type="hidden" name="command" value="name" />
                        <button class="btn btn-outline-primary" type="submit">A-Z</button>
                    </form>
                    <form action="Products" method="POST" class="dropdown-item">
                        <input type="hidden" name="service" value="products" />
                        <input type="hidden" name="command" value="price DESC" />
                        <button class="btn btn-outline-primary" type="submit">Price from high to low</button>
                    </form>
                    <form action="Products" method="POST" class="dropdown-item">
                        <input type="hidden" name="service" value="products" />
                        <input type="hidden" name="command" value="price" />
                        <button class="btn btn-outline-primary" type="submit">Price from low to high</button>
                    </form>

                </div>
            </div>

            <button type="button" class="btn btn-outline-primary mr-lg-auto" data-toggle="modal" data-target="#addProductModal">
                Add Product
            </button>

            <div class="btn-group">
                <!-- Nút Toggle Checkboxes -->
                <button type="button" class="btn btn-outline-primary" id="toggle-checkbox-btn" title="Show Checkboxes">
                    <i class="fa-solid fa-list-check"></i>
                </button>
                <a class="btn btn-outline-danger checkbox-column" onclick="confirmDeleteSelected(event)"  style="display: none;"  title="Delete Selected">
                    <i class="fa-solid fa-trash"></i>
                </a>
            </div>
        </div>
        <br>
        <div class="table-container">
            <form action="Products" method="POST">
                <table class="table table-striped table-hover table-bordered" style="color: var(--heading-clr);">
                    <thead>
                        <tr>
                            <th class="checkbox-column" style="display: none;">
                                <input type="checkbox" id="select-all" onclick="toggleSelectAll(this)" />
                            </th>
                            <th style="width: 50px">ID</th>
                            <th style="width: 150px">Name</th>
                            <th style="width: 150px">Image</th>
                            <th style="width: 70px">Price</th>
                            <th style="width: 85px">Weight</th>
                            <th style="width: 100px">Location</th>
                            <th>Description</th>
                            <th>Created At</th>
                            <th>Updated At</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody id="myTable">
                        <c:forEach var="product" items="${list}">
                            <tr>
                                <td class="checkbox-column" style="display: none; width: 200px">
                                    <input type="checkbox" name="selectedProducts" value="${product.getId()}" class="product-checkbox" />
                                </td>
                                <td>${product.getId()}</td>
                                <td class="bold-row">${product.getName()}</td>
                                <td>
                                    <img src="images/${product.getImage()}" style="width: 100px; height: 100px; object-fit: cover;" alt="Product Image" />
                                </td>
                                <td>${product.getPrice()}</td>
                                <td>${product.getQuantity()} Kg</td>
                                <td>${product.getZone_id()}</td>
                                <td>${product.getDescription()}</td>
                                <td>${product.getCreatedAt()}</td>
                                <td>${product.getUpdatedAt()}</td>
                                <td>
                                    <h5>
                                        <div class="badge rounded-pill bg-secondary" style="color: white">${product.getStatus()}</div>
                                    </h5>
                                </td>
                                <td>
                                    <div class="btn-group">
                                        <a class="btn btn-outline-info" href="Products?service=getProductById&product_id=${product.getId()}">
                                            <i class="fas fa-info-circle"></i>
                                        </a>
                                        <button type="button" class="btn btn-outline-warning" data-toggle="modal" data-target="#editProductModal${product.getId()}">
                                            <i class="fas fa-edit"></i>
                                        </button>
                                        <a class="btn btn-outline-danger" href="Products?service=deleteProduct&id=${product.getId()}" onclick="return doDelete(${product.getId()})">
                                            <i class="fas fa-trash"></i>
                                        </a>
                                    </div>
                                </td>
                            </tr>

                        </c:forEach>
                    </tbody>
                </table>


            </form>

        </div>
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
