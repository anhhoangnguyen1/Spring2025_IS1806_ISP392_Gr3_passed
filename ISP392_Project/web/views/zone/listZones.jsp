<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Zone List</title>
        <style>
            .table-container {
                overflow-x: auto;
                max-width: 100%;
            }
            .table {
                width: 100%;
                border-collapse: collapse;
            }
            /*            .table th, .table td {
                            padding: 8px;
                            text-align: left;
                            white-space: nowrap;
                            overflow: hidden;
                            text-overflow: ellipsis;
                        }
                        .table th {
                            background-color: #f8f9fa;
                            cursor: pointer;
                        }
                        .table th a {
                            color: #007bff;
                            text-decoration: none;
                        }
                        .table td {
                            max-width: 200px;  Giới hạn chiều rộng tối đa của cột 
                        }
                        .sticky-col {
                            position: sticky;
                            right: 0;
                            background: white;
                            z-index: 1;
                            width: 150px;
                        }
                        .resizable {
                            position: relative;
                        }
                        .resizable::after {
                            content: "";
                            position: absolute;
                            right: 0;
                            top: 0;
                            width: 5px;
                            height: 100%;
                            cursor: col-resize;
                            background-color: transparent;
                        }*/
        </style>
    </head>
    <body>
        <div class="container mt-4">
            <h1>Zones List</h1>
            <c:if test="${not empty requestScope.Notification}">
                <div class="alert alert-warning alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <strong>${requestScope.Notification}</strong>
                </div>
            </c:if>
            <form action="${pageContext.request.contextPath}/zones" method="GET">
                <div class="search-box">
                    <input type="hidden" name="service" value="zones" />
                    <input type="text" id="myInput" class="input-box" name="searchZone"
                           placeholder="Search for zones" value="${searchZone}" autocomplete="off" />
                    <button type="button" class="clear-btn" onclick="window.location.href = '${pageContext.request.contextPath}/zones?service=zones'">
                        <i class="fa-solid fa-xmark"></i>
                    </button>
                    <button type="submit" class="search-btn">
                        <i class="fa-solid fa-search"></i>
                    </button>
                </div>
            </form>
            <div class="action-bar d-flex align-items-center">
                <a href="${pageContext.request.contextPath}/zones?service=addZone" class="btn btn-outline-primary mr-lg-auto">
                    Add Zone
                </a>
            </div>
            <div class="table-container mt-4">
                <table class="table table-striped table-hover table-bordered" id="myTable">
                    <thead>
                        <tr>
                            <th class="resizable">
                                <a href="zones?service=zones&searchZone=${searchZone}&index=${index}&sortBy=id&sortOrder=${sortBy == 'id' && sortOrder == 'ASC' ? 'DESC' : 'ASC'}">
                                    ID <i class="fa ${sortBy == 'id' && sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down'}"></i>
                                </a>
                            </th>
                            <th class="resizable">
                                <a href="zones?service=zones&searchZone=${searchZone}&index=${index}&sortBy=name&sortOrder=${sortBy == 'name' && sortOrder == 'ASC' ? 'DESC' : 'ASC'}">
                                    Name <i class="fa ${sortBy == 'name' && sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down'}"></i>
                                </a>
                            </th>
                            <th class="resizable">Store ID</th>
                            <th class="resizable">Created By</th>
                            <th class="resizable">Status</th>
                            <th class="sticky-col">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="zone" items="${list}">
                            <tr>
                                <td>${zone.id}</td>
                                <td style="text-align: left;">${zone.name}</td>
                                <td>${zone.storeId != null ? zone.storeId.id : 'N/A'}</td>
                                <td>${zone.createdBy}</td>
                                <td>${zone.status}</td>
                                <td class="sticky-col">
                                    <a href="${pageContext.request.contextPath}/zones?service=getZoneById&zone_id=${zone.id}" class="btn btn-outline-primary">
                                        View
                                    </a>
                                    <a href="${pageContext.request.contextPath}/zones?service=editZone&zone_id=${zone.id}" class="btn btn-outline-primary">
                                        Edit
                                    </a>
                                    <a style="color: red" href="${pageContext.request.contextPath}/zones?service=deleteZone&zone_id=${zone.id}" class="btn btn-outline-primary">
                                        Ban
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="container d-flex justify-content-center mt-4">
                <ul class="pagination">
                    <c:if test="${index > 1}">
                        <li class="page-item">
                            <a class="page-link" href="zones?service=zones&searchZone=${searchZone}&index=${index - 1}&sortBy=${sortBy}&sortOrder=${sortOrder}">
                                <i class="fa fa-angle-left"></i>
                            </a>
                        </li>
                    </c:if>
                    <li class="page-item ${index == 1 ? 'active' : ''}">
                        <a class="page-link" href="zones?service=zones&searchZone=${searchZone}&index=1&sortBy=${sortBy}&sortOrder=${sortOrder}">1</a>
                    </li>
                    <c:if test="${index > 3}">
                        <li class="page-item disabled"><span class="page-link">...</span></li>
                        </c:if>
                        <c:forEach begin="${index - 1}" end="${index + 1}" var="page">
                            <c:if test="${page > 1 && page < endPage}">
                            <li class="page-item ${index == page ? 'active' : ''}">
                                <a class="page-link" href="zones?service=zones&searchZone=${searchZone}&index=${page}&sortBy=${sortBy}&sortOrder=${sortOrder}">
                                    ${page}
                                </a>
                            </li>
                        </c:if>
                    </c:forEach>
                    <c:if test="${index < endPage - 2}">
                        <li class="page-item disabled"><span class="page-link">...</span></li>
                        </c:if>
                        <c:if test="${endPage > 1}">
                        <li class="page-item ${index == endPage ? 'active' : ''}">
                            <a class="page-link" href="zones?service=zones&searchZone=${searchZone}&index=${endPage}&sortBy=${sortBy}&sortOrder=${sortOrder}">
                                ${endPage}
                            </a>
                        </li>
                    </c:if>
                    <c:if test="${index < endPage}">
                        <li class="page-item">
                            <a class="page-link" href="zones?service=zones&searchZone=${searchZone}&index=${index + 1}&sortBy=${sortBy}&sortOrder=${sortOrder}">
                                <i class="fa fa-angle-right"></i>
                            </a>
                        </li>
                    </c:if>
                </ul>
            </div>
        </div>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/css/script.js"></script>
    </body>
</html>