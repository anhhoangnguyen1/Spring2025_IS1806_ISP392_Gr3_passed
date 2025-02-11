<%-- 
    Document   : editProfile
    Created on : Feb 4, 2025, 2:15:00 PM
    Author     : THC
--%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="entity.Users"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Edit Profile</title>
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
                width: 120px;
                height: 120px;
                border-radius: 50%;
                object-fit: cover;
                border: 3px solid #007BFF;
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
                display: flex;
                justify-content: center;
                gap: 10px;
                margin-top: 20px;
            }
            .button {
                padding: 10px 20px;
                border-radius: 10px;
                border: none;
                font-size: 16px;
                color: white;
                cursor: pointer;
            }
            .save-button {
                background-color: #28a745;
            }
            .save-button:hover {
                background-color: #218838;
            }
            .back-button {
                background-color: #007BFF;
            }
            .back-button:hover {
                background-color: #0056b3;
            }
        </style>
    </head>
    <body>
        <div class="profile-container">
            <h2>Edit Profile</h2>
            <img src="${user.avatar}" 
                 onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/avatars/default-avatar.png';"
                 alt="User Avatar" class="avatar">
            <form action="<%= request.getContextPath() %>/EditProfileServlet" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="avatar">Change Avatar:</label>
                    <input type="file" id="avatar" name="avatar" accept="image/*">
                </div>
                <div class="form-group">
                    <label for="name">Name:</label>
                    <input type="text" id="name" name="name" value="${user.name}" required>
                </div>
                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" value="${user.email}" required>
                </div>
                <div class="form-group">
                    <label for="phone">Phone:</label>
                    <input type="text" id="phone" name="phone" value="${user.phone}" required>
                </div>
               
                <div class="button-container">
                    <button type="submit" class="button save-button">Save Changes</button>
                    <a href="<%= request.getContextPath() %>/profile" class="button back-button">Back</a>
                </div>
            </form>
        </div>
    </body>
</html>
