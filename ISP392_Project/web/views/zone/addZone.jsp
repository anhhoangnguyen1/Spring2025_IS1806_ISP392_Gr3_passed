<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Add Zone</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    </head>
    <body>
        <div class="page-wrapper">
            <!-- Top Bar -->
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
            <!-- Side Bar -->
            <aside class="side-bar">
                <span class="menu-label">MENU</span>
                <ul class="navbar-links navbar-links-1">
                    <li><a href="/ISP392_Project/dashboard"><span class="nav-icon"><i class="fa-solid fa-house"></i></span><span class="nav-text">Dashboard</span></a></li>
                    <li><a href="/ISP392_Project/Products"><span class="nav-icon"><i class="fas fa-box"></i></span><span class="nav-text">Products</span></a></li>
                    <li><a href="#"><span class="nav-icon"><i class="fa-solid fa-table"></i></span><span class="nav-text">Zones</span></a></li>
                    <li><a href="#"><span class="nav-icon"><i class="fa-solid fa-user"></i></span><span class="nav-text">Customers</span></a></li>
                    <li><a href="#"><span class="nav-icon"><i class="fa-solid fa-file-invoice"></i></span><span class="nav-text">Invoices</span></a></li>
                    <li><a href="/ISP392_Project/Debts"><span class="nav-icon"><i class="fa-solid fa-money-bill"></i></span><span class="nav-text">Debts History</span></a></li>
                </ul>
                <span class="menu-label">OWNER ZONE</span>
                <ul class="navbar-links navbar-links-2">
                    <li><a href="#"><span class="nav-icon"><i class="fa-solid fa-user-tie"></i></span><span class="nav-text">Staffs</span></a></li>
                </ul>
                <div class="sidebar-footer">
                    <div class="settings">
                        <span class="gear-icon"><i class="fa-solid fa-gear"></i></span>
                        <span class="text">Settings</span>
                    </div>
                    <div class="logoutBtn">
                        <a href="/ISP392_Project/logout"><span class="logout-icon"><i class="fa-solid fa-right-from-bracket"></i></span><span class="text">Logout</span></a>
                    </div>
                </div>
            </aside>
            <!-- Main Content -->
            <div class="contents">
                <div class="panel-bar1">
                    <h2>Add Zone</h2>
                    <c:if test="${not empty requestScope.nameError}">
                        <div class="alert alert-danger">${requestScope.nameError}</div>
                    </c:if>
                    <form action="${pageContext.request.contextPath}/zones" method="POST" onsubmit="return confirmAdd()">
                        <input type="hidden" name="service" value="addZone"/>
                        <div class="form-group">
                            <label for="name">Name</label>
                            <input type="text" class="form-control" name="name" value="${requestScope.name}" required>
                        </div>
                        <div class="form-group">
                            <label for="description">Description</label>
                            <textarea class="form-control" name="description" rows="3">${requestScope.description}</textarea>
                        </div>
                        <!-- Không cần trường Store ID -->
                        <div class="form-group">
                            <label for="status">Status</label>
                            <select class="form-control" name="status" required>
                                <option value="Active" ${requestScope.status == 'Active' ? 'selected' : ''}>Active</option>
                                <option value="Inactive" ${requestScope.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="createdBy">Created By</label>
                            <span class="form-control" id="createdBy">${fullName}</span>
                        </div>
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary">Add Zone</button>
                            <a href="${pageContext.request.contextPath}/zones?service=zones" class="btn btn-secondary">Back to Zones List</a>
                        </div>
                    </form>
                </div>
            </div>
            <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
            <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
            <script type="text/javascript" src="<%= request.getContextPath() %>/css/script.js"></script>
            <script>
                        function confirmAdd() {
                            return confirm("Are you sure you want to add this zone?");
                        }
            </script>
    </body>
</html>