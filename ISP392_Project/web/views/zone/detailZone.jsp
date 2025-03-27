<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
        <title>Zone Details</title>
    </head>
    <body>
        <style>
            .zone-container {
                max-width: 800px;
                margin: 20px auto;
                padding: 20px;
                background-color: #f9f9f9;
                border-radius: 10px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            }
            .zone-detail {
                display: flex;
                justify-content: space-between;
                padding: 10px 0;
                border-bottom: 1px solid #ddd;
            }
            .zone-detail label {
                font-weight: bold;
                color: #333;
            }
            .zone-detail span {
                text-align: right;
            }
        </style>
        <div class="container">
            <div class="zone-container">
                <h2>Zone Details</h2>
                <div class="zone-detail"><label>Zone ID:</label><span>${zone.id}</span></div>
                <div class="zone-detail"><label>Name:</label><span>${zone.name}</span></div>
                <div class="zone-detail"><label>Description:</label><span>${zone.description != null ? zone.description : 'N/A'}</span></div>
                <div class="zone-detail"><label>Store ID:</label><span>${zone.storeId != null ? zone.storeId.id : 'N/A'}</span></div>
                <div class="zone-detail"><label>Product Name:</label><span>${zone.productId != null ? zone.productId.name : 'N/A'}</span></div>
                <div class="zone-detail"><label>Created By:</label><span>${zone.createdBy}</span></div>
                <div class="zone-detail"><label>Status:</label><span>${zone.status}</span></div>
                <div class="zone-detail">
                    <label>History:</label>
                    <span>
                        <c:choose>
                            <c:when test="${empty zone.historyList}">
                                N/A
                            </c:when>
                            <c:otherwise>
                                <ul>
                                    <c:forEach var="entry" items="${zone.historyList}">
                                        <li>${entry['productName']} (From ${entry['startDate']} to ${entry['endDate'] != null ? entry['endDate'] : 'Now'})</li>
                                    </c:forEach>
                                </ul>
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>
                <div style="display: flex; gap: 20px;">
                    <a href="${pageContext.request.contextPath}/zones?service=editZone&zone_id=${zone.id}&index=${param.index}&sortBy=${param.sortBy}&sortOrder=${param.sortOrder}" class="btn btn-outline-warning">Edit Zone</a>
                    <a href="zones?service=viewZoneHistory&zone_id=${zone.id}&index=${param.index}&sortBy=${param.sortBy}&sortOrder=${param.sortOrder}" class="btn btn-outline-info">View Zone History</a>
                    <a href="zones?service=zones&index=${param.index}&sortBy=${param.sortBy}&sortOrder=${param.sortOrder}" class="btn btn-outline-secondary">Exit</a>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>