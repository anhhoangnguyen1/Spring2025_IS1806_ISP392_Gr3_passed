<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Zones List (Staff)</title>
        <style>
            .table-container {
                overflow-x: auto;
                max-width: 100%;
            }
            .table {
                width: 100%;
                border-collapse: collapse;
            }
            .sortable {
                cursor: pointer;
            }
            .container.mt-4 {
                position: relative;
                padding-top: 15px;
            }
            .search-box {
                position: absolute;
                top: 20px;
                left: 50%;
                transform: translateX(-50%);
                width: 400px;
                height: 37px;
                background-color: var(--bg-ternary);
                border-radius: 50px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                z-index: 10;
            }
            .showing-info {
                margin-bottom: 10px;
                font-size: 14px;
            }
        </style>
    </head>
    <body>
        <div class="container mt-4">
            <h1>Zones List (Staff)</h1>
            <c:if test="${not empty requestScope.Notification}">
                <div class="alert alert-warning alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert">×</button>
                    <strong>${requestScope.Notification}</strong>
                </div>
            </c:if>
            <form action="zones" method="get">
                <div class="search-box">
                    <input type="hidden" name="service" value="staffZones" />
                    <input type="text" id="searchInput" class="input-box" name="searchZone"
                           placeholder="Search for zones" value="${searchZone}" autocomplete="off" />
                    <button type="submit" class="clear-btn" id="clearBtn">
                        <i class="fa-solid fa-xmark"></i>
                    </button>
                </div>
            </form>
            <div class="showing-info" style="margin-top: 10px; margin-bottom: -15px">
                Showing: 
                <form action="zones" method="get" style="display: inline;">
                    <input type="hidden" name="service" value="staffZones" />
                    <input type="hidden" name="searchZone" value="${searchZone}" />
                    <input type="hidden" name="sortBy" value="${sortBy}" />
                    <input type="hidden" name="sortOrder" value="${sortOrder}" />
                    <select name="pageSize" onchange="this.form.submit()">
                        <option value="5" ${pageSize == 5 ? 'selected' : ''}>5</option>
                        <option value="10" ${pageSize == 10 ? 'selected' : ''}>10</option>
                        <option value="20" ${pageSize == 20 ? 'selected' : ''}>20</option>
                    </select>
                </form>
                of ${totalRecords} zones
            </div>
            <div class="table-container mt-4">
                <table class="table table-striped table-hover table-bordered" id="zoneTable">
                    <thead>
                        <tr>
                            <th class="sortable">
                                <a href="zones?service=staffZones&searchZone=${searchZone}&index=${index}&sortBy=id&sortOrder=${sortBy == 'id' && sortOrder == 'ASC' ? 'DESC' : 'ASC'}&pageSize=${pageSize}">
                                    ID <i class="fa ${sortBy == 'id' && sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down'}"></i>
                                </a>
                            </th>
                            <th class="sortable">
                                <a href="zones?service=staffZones&searchZone=${searchZone}&index=${index}&sortBy=name&sortOrder=${sortBy == 'name' && sortOrder == 'ASC' ? 'DESC' : 'ASC'}&pageSize=${pageSize}">
                                    Name <i class="fa ${sortBy == 'name' && sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down'}"></i>
                                </a>
                            </th>
                            <th>Store ID</th>
                            <th>Product Name</th>
                            <th>Created By</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody id="zoneTableBody">
                        <c:forEach var="zone" items="${list}">
                            <tr>
                                <td>${zone.id}</td>
                                <td style="text-align: left;">${zone.name}</td>
                                <td>${zone.storeId != null ? zone.storeId.id : 'null'}</td>
                                <td>${zone.productId != null ? zone.productId.name : 'null'}</td>
                                <td>${zone.createdBy}</td>
                                <td>${zone.status}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty list}">
                            <tr>
                                <td colspan="6">No zones found</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
            <div class="container d-flex justify-content-center mt-4" id="pagination">
                <ul class="pagination">
                    <c:if test="${index > 1}">
                        <li class="page-item">
                            <a class="page-link" href="zones?service=staffZones&searchZone=${searchZone}&index=${index - 1}&sortBy=${sortBy}&sortOrder=${sortOrder}&pageSize=${pageSize}">
                                <i class="fa fa-angle-left"></i>
                            </a>
                        </li>
                    </c:if>
                    <li class="page-item ${index == 1 ? 'active' : ''}">
                        <a class="page-link" href="zones?service=staffZones&searchZone=${searchZone}&index=1&sortBy=${sortBy}&sortOrder=${sortOrder}&pageSize=${pageSize}">1</a>
                    </li>
                    <c:if test="${index > 3}">
                        <li class="page-item disabled"><span class="page-link">...</span></li>
                    </c:if>
                    <c:forEach begin="${index - 1}" end="${index + 1}" var="page">
                        <c:if test="${page > 1 && page < endPage}">
                            <li class="page-item ${index == page ? 'active' : ''}">
                                <a class="page-link" href="zones?service=staffZones&searchZone=${searchZone}&index=${page}&sortBy=${sortBy}&sortOrder=${sortOrder}&pageSize=${pageSize}">
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
                            <a class="page-link" href="zones?service=staffZones&searchZone=${searchZone}&index=${endPage}&sortBy=${sortBy}&sortOrder=${sortOrder}&pageSize=${pageSize}">
                                ${endPage}
                            </a>
                        </li>
                    </c:if>
                    <c:if test="${index < endPage}">
                        <li class="page-item">
                            <a class="page-link" href="zones?service=staffZones&searchZone=${searchZone}&index=${index + 1}&sortBy=${sortBy}&sortOrder=${sortOrder}&pageSize=${pageSize}">
                                <i class="fa fa-angle-right"></i>
                            </a>
                        </li>
                    </c:if>
                </ul>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
        <script>
            $(document).ready(function () {
                $('#clearBtn').on('click', function () {
                    $('#searchInput').val('');
                    window.location.href = 'zones?service=staffZones';
                });
            });
        </script>
    </body>
</html>