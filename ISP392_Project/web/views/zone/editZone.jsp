<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Edit Zone</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    </head>
    <body>
        <div class="page-wrapper">
            <!-- Top Bar Starts -->
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
            <!-- Side Bar Starts -->
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
                    <h2>Edit Zone</h2>
                    <c:if test="${not empty requestScope.nameError}">
                        <div class="alert alert-danger">${requestScope.nameError}</div>
                    </c:if>
                    <form id="zoneForm" action="${pageContext.request.contextPath}/zones" method="POST" onsubmit="confirmSave(event)">
                        <input type="hidden" name="service" value="editZone" />
                        <input type="hidden" name="zone_id" value="${zone.id}" />
                        <input type="hidden" name="sortOrder" value="${param.sortOrder}" />
                        <div class="form-group">
                            <label for="name">Name</label>
                            <input type="text" class="form-control" name="name" value="${zone.name}" required>
                        </div>
                        <div class="form-group">
                            <label for="description">Description</label>
                            <textarea class="form-control" name="description" rows="3">${zone.description}</textarea>
                        </div>
                        <div class="form-group">
                            <label for="store_id">Store ID</label>
                            <input type="number" class="form-control" name="store_id" value="${zone.storeId != null ? zone.storeId.id : ''}">
                        </div>
                        <div class="form-group">
                            <label for="status">Status</label>
                            <select class="form-control" name="status" required>
                                <option value="Active" ${zone.status == 'Active' ? 'selected' : ''}>Active</option>
                                <option value="Inactive" ${zone.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                            </select>
                        </div>
                        <input type="hidden" name="updateBy" value="${userName}" />
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary" style="background-color: #007bff">Save Changes</button>
                            <a href="${pageContext.request.contextPath}/zones?service=zones&sortOrder=${param.sortOrder}" class="btn btn-secondary">Back to Zones List</a>
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal fade" id="confirmModal" tabindex="-1" aria-labelledby="confirmModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="confirmModalLabel">Confirm Changes</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            Are you sure to save the changes to this zone?
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary" style="background-color: #007bff" id="saveChangesBtn">Save</button>
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                        </div>
                    </div>
                </div>
            </div>
            <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"></script>
            <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
            <script type="text/javascript" src="<%= request.getContextPath() %>/css/script.js"></script>
            <script type="text/javascript">
                document.getElementById('saveChangesBtn').onclick = function () {
                    document.getElementById('zoneForm').submit();
                }
                function confirmSave(event) {
                    event.preventDefault();
                    $('#confirmModal').modal('show');
                }
            </script>
    </body>
</html>