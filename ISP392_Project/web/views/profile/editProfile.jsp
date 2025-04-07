<%-- 
    Document   : editProfile
    Created on : Feb 4, 2025, 2:15:00 PM
    Author     : THC
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="entity.Users"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Edit Profile</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
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
            <!--   *** Top Bar Ends ***   -->

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

                        <li>
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
                            <a href="sale">
                                <span class="nav-icon">
                                    <i class="fa-solid fa-file-invoice"></i>
                                </span>
                                <span class="nav-text">Orders</span>
                            </a>
                        </li>
                        <li>
                            <a href="orders">
                                <span class="nav-icon">
                                    <i class="fa-solid fa-file-invoice"></i>
                                </span>
                                <span class="nav-text">Orders History</span>
                            </a>
                        </li>
                        <li>
                            <a href="/ISP392_Project/Imports">
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
            </aside>
            <!--   === Side Bar Ends ===   -->

            <!--   === Profile Content Starts ===   -->
            <div class="contents">
                <div class="panel-bar1">
                    <h2>Edit Profile</h2>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f4f4;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            height: 100vh;
                            margin: 0;
                        }
                        .profile-container {
                            display: flex;
                            flex-direction: column;
                            align-items: center;
                            background: #ffffff;
                            padding: 30px;
                            border-radius: 15px;
                            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                            width: 40%;
                        }


                        .avatar {
                            width: 150px;
                            height: 150px;
                            border-radius: 50%;
                            border: 3px solid #007BFF;
                            background-size: cover;
                            background-position: center;
                            background-repeat: no-repeat;
                            display: inline-block;
                            margin-bottom: 15px;
                        }

                        .form-group {
                            width: 100%;
                            margin-bottom: 15px;
                        }
                        .form-group label {
                            font-weight: bold;
                            display: block;
                            margin-bottom: 5px;
                        }
                        .form-group input {
                            width: 100%;
                            padding: 10px;
                            border: 1px solid #ccc;
                            border-radius: 10px;
                            font-size: 16px;
                        }
                        .button-container {
                            text-align: left;
                            width: 100%;
                            margin-top: 15px;
                        }
                        .button {
                            padding: 10px 20px;
                            border-radius: 5px;
                            border: none;
                            font-size: 16px;
                            color: white;
                            cursor: pointer;
                        }

                    </style>

                    <div id="avatar-preview" class="avatar" 
                         style="background-image: url('${pageContext.request.contextPath}/views/profile/image/${user.image}?timestamp=${System.nanoTime()}');">
                    </div>


                    <form id="profileForm" action="${pageContext.request.contextPath}/editprofile" method="post" enctype="multipart/form-data" onsubmit="confirmSave(event)">
                        <div class="form-group">
                            <label for="avatar">Change Avatar:</label>
                            <input type="file" id="avatar" name="image" accept="image/*" onchange="previewImage(event)">
                        </div>


                        <div class="form-group">
                            <label for="name">Name:</label>
                            <input type="text" id="name" name="name" value="${user.name}">
                        </div>


                        <div class="form-group">
                            <label for="gender">Gender</label>
                            <select class="form-control" name="gender">
                                <option value="Male" ${user.gender == 'Male' ? 'selected' : ''}>Male</option>
                                <option value="Female" ${user.gender == 'Female' ? 'selected' : ''}>Female</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="dob">Date of Birth:</label>
                            <input type="date" id="dob" name="dob" value="${user.dob}">
                        </div>
                        <div class="form-group">
                            <label for="phone">Phone:</label>
                            <input type="text" id="phone" name="phone" value="${user.phone}">
                            <c:if test="${not empty phoneError}">
                                <span style="color: red;">${phoneError}</span>
                            </c:if>
                        </div>


                        <div class="form-group">
                            <label for="email">Email:</label>
                            <input type="text" id="email" name="email" value="${user.email}">
                            <c:if test="${not empty emailError}">
                                <span style="color: red;">${emailError}</span>
                            </c:if>
                        </div>

                        <div class="form-group">
                            <label for="address">Address:</label>
                            <input type="text" id="address" name="address" value="${user.address}">
                        </div>

                        <div class="button-container">
                            <button type="submit" class="btn btn-primary" style="background-color: #007bff ">Save Changes</button>


                            <a href="${pageContext.request.contextPath}/views/profile/profile.jsp" class="btn btn-primary" style="background-color: #6c757d ">Back</a>
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
                            Are you sure to save the changes?
                        </div>
                        <div class="modal-footer">

                            <button type="button" class="btn btn-primary" style="background-color: #007bff" id="saveChangesBtn">Save</button>
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                        </div>
                    </div>
                </div>
            </div>


            <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
            <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
            <script type="text/javascript" src="<%= request.getContextPath() %>/css/script.js"></script>
            <script type="text/javascript">
                                document.getElementById('saveChangesBtn').onclick = function () {
                                    $('#confirmModal').modal('hide');  // Ẩn modal xác nhận
                                    document.getElementById('profileForm').submit();  // Tiến hành gửi form
                                }



                                function confirmSave(event) {
                                    event.preventDefault();
                                    $('#confirmModal').modal('show');
                                }
                                function previewImage(event) {
                                    const file = event.target.files[0];
                                    const reader = new FileReader();

                                    reader.onload = function (e) {
                                        const avatarPreview = document.getElementById('avatar-preview');
                                        avatarPreview.style.backgroundImage = 'url(' + e.target.result + ')';
                                    };

                                    reader.readAsDataURL(file);
                                }
            </script>
        </div>
    </body>
</html>

