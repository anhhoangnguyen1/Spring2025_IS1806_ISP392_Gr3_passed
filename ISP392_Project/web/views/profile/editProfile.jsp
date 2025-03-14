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

        .save-button {
            background-color: #007BFF;
        }
        .save-button:hover {
            background-color: #0056b3;
        }

        .back-button {
            background-color: #6c757d;
            text-decoration: none;
            padding: 10px 15px;
            display: inline-block;
            border-radius: 5px;
            font-size: 16px;
            text-align: center;
        }
        .back-button:hover {
            background-color: #5a6268;
        }
    </style>

</head>
<body>
    <div class="profile-container">
        <h2>Edit Profile</h2>
        
        <div id="avatar-preview" class="avatar" 
             style="background-image: url('${pageContext.request.contextPath}/avatars/${user.image}');"></div>

        <form action="${pageContext.request.contextPath}/editprofile" method="post" enctype="multipart/form-data" onsubmit="return confirmSave();">
            <div class="form-group">
                <label for="avatar">Change Avatar:</label>
                <input type="file" id="avatar" name="avatar" accept="image/*" onchange="previewImage(event)">
            </div>
            <div class="form-group">
                <label for="name">Name:</label>
                <input type="text" id="name" name="name" value="${user.name}">
            </div>
            <div class="form-group">
                <label for="gender">Gender:</label>
                <input type="text" id="gender" name="gender" value="${user.gender}">
            </div>
            <div class="form-group">
                <label for="dob">Date of Birth:</label>
                <input type="date" id="dob" name="dob" value="${user.dob}">
            </div>
            <div class="form-group">
                <label for="phone">Phone:</label>
                <input type="text" id="phone" name="phone" value="${user.phone}">
            </div>
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" value="${user.email}">
            </div>
            <div class="form-group">
                <label for="address">Address:</label>
                <input type="text" id="address" name="address" value="${user.address}">
            </div>
            
            <div class="button-container">
                <button type="submit" class="button save-button">Save Changes</button>
                <button type="button" class="button back-button" onclick="goBack();">Back</button>
            </div>
        </form>
    </div>
</body>
</html>
