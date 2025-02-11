<!-- <%-- 
    Document   : dashboard.jsp
    Created on : Jan 28, 2025, 10:04:28 AM
    Author     : bsd12418
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%> -->
<!DOCTYPE html>
<html>
  <head>
    <!--   ***** Link To Custom Stylesheet *****   -->

    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css"
    />
    <link
      rel="stylesheet"
      type="text/css"
      href="<%= request.getContextPath() %>/product/css/style.css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"
    />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Dashboard</title>
  </head>
  <body>
    <!--   *** Page Wrapper Starts ***   -->
    <div class="page-wrapper">
      <!--   *** Top Bar Starts ***   -->
      <div class="top-bar">
        <div class="top-bar-left">
          <div class="hamburger-btn">
            <span></span>
            <span></span>
            <span></span>
          </div>
          <div class="logo mr-5">
            <img src="images/logo.png" style="width: 120px; height: 70px" />
          </div>
        </div>
        <form action="Products" method="POST" class="custom-search-form">
          <input type="hidden" name="service" value="searchProducts" />
          <div class="search-box">
            <input
              type="text"
              id="myInput"
              class="input-box"
              list="browsers"
              name="browser"
              id="browser"
              placeholder="Search for a product..."
              autocomplete="off"
            />
            <datalist id="browsers">
              <c:forEach var="product" items="${list}">
                <option value="${product.getName()}">
                  ${product.getName()}
                </option>
              </c:forEach>
            </datalist>
            <button type="submit" class="search-btn">
              <i class="fa-solid fa-search"></i>
            </button>
          </div>
        </form>
        <div class="top-bar-right">
          <div class="mode-switch">
            <i class="fa-solid fa-moon"></i>
          </div>
          <div class="notifications">
            <i class="fa-solid fa-bell"></i>
          </div>
          <div class="profile">
            <img src="images/profile-img.jpg" />
          </div>
        </div>
      </div>
      <!--   *** Top Bar Ends ***   -->

      <!--   === Side Bar Starts ===   -->
      <aside class="side-bar">
        <!--   === Nav Bar Links Starts ===   -->
        <span class="menu-label">MENU</span>
        <ul class="navbar-links navbar-links-1">
          <li>
            <a href="#">
              <span class="nav-icon">
                <i class="fa-solid fa-house"></i>
              </span>
              <span class="nav-text">Dashboard</span>
            </a>
          </li>
          <li class="active">
            <a href="Products?service=products">
              <span class="nav-icon">
                <i class="fas fa-box"></i>
              </span>
              <span class="nav-text">Products</span>
            </a>
          </li>
          <li>
            <a href="#">
              <span class="nav-icon">
                <i class="fa-solid fa-table"></i>
              </span>
              <span class="nav-text">Zones</span>
            </a>
          </li>
          <li>
            <a href="#">
              <span class="nav-icon">
                <i class="fa-solid fa-user"></i>
              </span>
              <span class="nav-text">Customers</span>
            </a>
          </li>
          <li>
            <a href="#">
              <span class="nav-icon">
                <i class="fa-solid fa-file-invoice"></i>
              </span>
              <span class="nav-text">Invoices</span>
            </a>
          </li>
          <li>
            <a href="#">
              <span class="nav-icon">
                <i class="fa-solid fa-bag-shopping"></i>
              </span>
              <span class="nav-text">Number of bags</span>
            </a>
          </li>
          <li>
            <a href="Debts?service=debts">
              <span class="nav-icon">
                <i class="fa-solid fa-money-bill"></i>
              </span>
              <span class="nav-text">Debts</span>
            </a>
          </li>
        </ul>
        <span class="menu-label">OWNER ZONE</span>
        <ul class="navbar-links navbar-links-2">
          <li>
            <a href="#">
              <span class="nav-icon">
                <i class="fa-solid fa-user-tie"></i>
              </span>
              <span class="nav-text">Staffs</span>
            </a>
          </li>
        </ul>
        <!--   === Nav Bar Links Ends ===   -->
        <!--   === Side Bar Footer Starts ===   -->

        <div class="sidebar-footer">
          <div class="settings">
            <span class="gear-icon">
              <i class="fa-solid fa-gear"></i>
            </span>
            <span class="text">Settings</span>
          </div>
          <div class="logoutBtn">
            <span class="logout-icon">
              <i class="fa-solid fa-right-from-bracket"></i>
            </span>
            <span class="text">Logout</span>
          </div>
        </div>
      </aside>

      <div class="contents">
        <div class="panel-bar1">
          <c:if test="${not empty requestScope.Notification}">
            <div class="alert alert-warning alert-dismissible">
              <button type="button" class="close" data-dismiss="alert">
                &times;
              </button>
              <strong>${requestScope.Notification}</strong>
            </div>
          </c:if>
          <c:if test="${not empty sessionScope.Notification}">
            <div class="alert alert-warning alert-dismissible">
              <button type="button" class="close" data-dismiss="alert">
                &times;
              </button>
              <strong>${sessionScope.Notification}</strong>
            </div>
            <c:remove var="Notification" scope="session" />
          </c:if>

          <c:import url="listProducts.jsp" />
        </div>
      </div>
    </div>
    <c:import url="addProduct.jsp" />
    <c:import url="editProduct.jsp" />
    <!--   === java script ===   -->
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <script
      type="text/javascript"
      src="<%= request.getContextPath() %>/product/css/script.js"
    ></script>
  </body>
</html>
