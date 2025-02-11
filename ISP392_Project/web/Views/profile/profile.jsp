<%-- 
    Document   : profile
    Created on : Feb 4, 2025, 1:51:14 PM
    Author     : THC
--%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="entity.Users"%>

<!DOCTYPE html>
<html>
    <head>
        <title>User Profile</title>
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
                align-items: center;
                background: #ffffff;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                width: 50%;
            }
            .avatar-section {
                text-align: center;
                padding: 20px;
                flex: 1;
            }
            .avatar {
                width: 150px;
                height: 150px;
                border-radius: 50%;
                object-fit: cover;
                border: 3px solid #007BFF;
            }
            .user-name {
                font-size: 20px;
                font-weight: bold;
                margin-top: 10px;
            }
            .username {
                font-size: 16px;
                color: gray;
            }
            .user-info {
                flex: 2;
                padding: 20px;
            }
            .info {
                font-size: 18px;
                margin: 10px 0;
            }
            .button-container {
                text-align: left;
                width: 100%;
                margin-top: 15px;
            }
            .button {
                display: inline-block;
                padding: 10px 15px;
                color: white;
                background-color: #007BFF;
                text-decoration: none;
                border-radius: 5px;
                font-size: 16px;
                margin-right: 10px;
            }
            .button:hover {
                background-color: #0056b3;
            }
            .back-button {
                background-color: #6c757d;
            }
            .back-button:hover {
                background-color: #5a6268;
            }
        </style>
    </head>
    <body>
        <div class="profile-container">
            <div class="avatar-section">
                <img src="<%= request.getContextPath() %>/avatars/<%= user.getUserId() %>.png" 
                     onerror="this.onerror=null; this.src='<%= request.getContextPath() %>/avatars/default-avatar.png';"
                     alt="User Avatar" class="avatar">
                <div class="user-name"><%= user.getName() != null ? user.getName() : "No Name" %></div>
                <div class="username">@<%= user.getUsername() != null ? user.getUsername() : "unknown_user" %></div>
            </div>
            <div class="user-info">
                <h2>User Profile</h2>
                <p class="info"><strong>User ID:</strong> <%= user.getUserId() %></p>
                <p class="info"><strong>Role:</strong> <%= user.getRole() != null ? user.getRole() : "No Role Assigned" %></p>
                <p class="info"><strong>Email:</strong> <%= user.getEmail() != null ? user.getEmail() : "No Email" %></p>
                <p class="info"><strong>Phone:</strong> <%= user.getPhone() != null ? user.getPhone() : "No Phone" %></p>
                <p class="info"><strong>Status:</strong> <%= user.getStatus() != null ? user.getStatus() : "No Status" %></p>
                <div class="button-container">
                    <a href="editProfile.jsp" class="button">Edit Profile</a>
                    <a href="<%= request.getContextPath() %>/dashboard/dashboard.jsp" class="button back-button">Back</a>
                </div>
            </div>
        </div>
    </body>
</html>
