
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css?v=1.0" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
        <title>Product Details</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
                padding: 20px;
            }
            table {
                width: 100%;
                border-collapse: collapse;
            }
            th, td {
                border: 1px solid #ddd;
                padding: 10px;
                text-align: left;
            }
            th {
                background-color: #f4f4f4;
            }
            .product-image {
                max-width: 100px;
            }
            .action-buttons a {
                margin-right: 10px;
                text-decoration: none;
                padding: 5px 10px;
                border-radius: 5px;
            }
            .edit {
                background-color: #ffc107;
                color: black;
            }
            .delete {
                background-color: #dc3545;
                color: white;
            }
            .show-arrow i {
                opacity: 0; /* Ẩn mũi tên */
                transition: opacity 0.3s ease-in-out;
            }

            .show-arrow:hover i {
                opacity: 1; /* Hiện mũi tên khi trỏ vào */
            }

        </style>
    </head>
    <body>
        <div class="page-wrapper">
            <div class="top-bar">
                <div class="top-bar-left">
                    <div class="hamburger-btn">
                        <span></span>
                        <span></span>
                        <span></span>
                    </div>
                    <div class="logo">
                        <img src="/ISP392_Project/views/dashboard/images/logo.png" style="width: 170px; height: 70px" />
                    </div>
                </div>


                <div class="top-bar-right">
                    <div class="mode-switch">
                        <i class="fa-solid fa-moon"></i>
                    </div>
                    <div class="notifications">
                        <i class="fa-solid fa-bell"></i>
                    </div>
                    <div class="profile">
                        <img src="/ISP392_Project/views/dashboard/images/profile-img.jpg" id="profile-img" />
                        <div class="profile-menu">
                            <ul>
                                <li><a href="/ISP392_Project/user"><i class="fa-solid fa-pen"></i> User Profile</a></li>
                                <li><a href="/ISP392_Project/change-password"><i class="fa-solid fa-pen"></i> Change Password</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

            <!--   === Side Bar Starts ===   -->
            <aside class="side-bar">
                <!--   === Nav Bar Links Starts ===   -->
                <span class="menu-label">MENU</span>
                <ul class="navbar-links navbar-links-1">
                    <c:if test="${sessionScope.role == 'owner' || sessionScope.role == 'staff'}">
                        <li>
                            <a href="/ISP392_Project/dashboard">
                                <span class="nav-icon">
                                    <i class="fa-solid fa-house"></i>
                                </span>
                                <span class="nav-text">Dashboard</span>
                            </a>
                        </li>

                        <li class="active">
                            <a href="/ISP392_Project/Products">
                                <span class="nav-icon">
                                    <i class="fas fa-box"></i>
                                </span>
                                <span class="nav-text">Products</span>
                            </a>
                        </li>
                        <li>
                            <a href="/ISP392_Project/zones">
                                <span class="nav-icon">
                                    <i class="fa-solid fa-table"></i>
                                </span>
                                <span class="nav-text">Zones</span>
                            </a>
                        </li>
                        <li>
                            <a href="/ISP392_Project/Customers">
                                <span class="nav-icon">
                                    <i class="fa-solid fa-user"></i>
                                </span>
                                <span class="nav-text">Customers</span>
                            </a>
                        </li>
                        <li>
                            <a href="/ISP392_Project/Orders">
                                <span class="nav-icon">
                                    <i class="fa-solid fa-file-invoice"></i>
                                </span>
                                <span class="nav-text">Orders</span>
                            </a>
                        </li>
                        <li>
                            <a href="/ISP392_Project/InvoiceHistory">
                                <span class="nav-icon">
                                    <i class="fa-solid fa-file-invoice"></i>
                                </span>
                                <span class="nav-text">Invoice History</span>
                            </a>
                        </li>
                        <li>
                            <a href="/ISP392_Project/Import">
                                <span class="nav-icon">
                                    <i class="fa-solid fa-file-invoice"></i>
                                </span>
                                <span class="nav-text">Imports</span>
                            </a>
                        </li>
                    </c:if>
                    <li>
                        <a href="/ISP392_Project/Stores">
                            <span class="nav-icon">
                                <i class="fa-solid fa-store"></i>
                            </span>
                            <span class="nav-text">Stores</span>
                        </a>
                    </li>
                    <c:if test="${sessionScope.role == 'owner' || sessionScope.role == 'admin'}">
                        <li>
                            <a href="/ISP392_Project/Users">
                                <span class="nav-icon">
                                    <i class="fa-solid fa-user-tie"></i>
                                </span>
                                <span class="nav-text">Staffs</span>
                            </a>
                        </li>
                    </c:if>
                </ul>
                <!--   === Nav Bar Links Ends ===   -->
                <!--   === Side Bar Footer Starts ===   -->
                <div class="sidebar-footer">
                    <div class="logoutBtn">
                        <a href="/ISP392_Project/logout">
                            <span class="logout-icon">
                                <i class="fa-solid fa-right-from-bracket"></i>
                            </span>
                            <span class="text"><a href="/ISP392_Project/logout">Logout</a></span>
                    </div>
                </div>
                <!--   === Side Bar Footer Ends ===   -->
            </aside>
            <!--   === Side Bar Ends ===   -->
        </aside>
        <div class="contents">
            <div class="panel-bar1">
                <a href="/ISP392_Project/Products" class="text-secondary show-arrow">
                    <i class="fa-solid fa-arrow-left"></i> Products
                </a>/ Product details

                <c:forEach var="product" items="${list}">

                    <table>
                        <tr>
                            <th style="width: 50px">ID</th>
                            <td>${product.productId}</td>
                        </tr>
                        <tr>
                            <th style="width: 150px">Name</th>
                            <td>${product.name}</td>
                        </tr>
                        <tr>
                            <th style="width: 150px">Image</th>
                            <td>
                                <img src="/ISP392_Project/views/product/images/${product.image}" class="myImg" 
                                     style="width: 100px; height: 100px; object-fit: cover; border-radius: 5px;" 
                                     alt="Product Image">
                            </td>
                        </tr>

                        <tr>
                            <th style="width: 70px">Price</th>
                            <td>${product.price}</td>
                        </tr>
                        <tr>
                            <th style="width: 85px">Weight</th>
                            <td>${product.quantity} Kg</td>
                        </tr>
                        <tr>
                            <th style="width: 400px">Description</th>
                            <td>${product.description}</td>
                        </tr>
                        <tr>
                            <th style="width: 150px">Created At</th>
                            <td>${product.createdAt}</td>
                        </tr>
                        <tr>
                            <th style="width: 150px">Updated At</th>
                            <td>${product.updatedAt}</td>
                        </tr>
                        <tr>
                            <th style="width: 150px">Status</th>
                            <td>${product.status}</td>
                        </tr>

                    </table>
                </c:forEach>
            </div>
        </div>
    </div>
    <div id="myModal" class="modalImage">
        <span class="close">&times;</span>
        <img class="modalImage-content" id="img01">
        <div id="caption"></div>
    </div>

</body>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/css/script.js"></script>
<script>
    $(".myImg").on("click", function () {
        var modal = $("#myModal");
        var modalImg = $("#img01");
        var captionText = $("#caption");

        modal.show();
        modalImg.attr("src", this.src);
        captionText.text(this.alt);
    });

    // Đóng modal khi bấm vào nền đen
    $("#myModal").on("click", function (e) {
        if (e.target === this) {
            $(this).hide();
        }
    });
</script>
</html>
