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
            .checkbox-container {
                margin-top: 10px;
                margin-bottom: -15px;
            }
        </style>
    </head>
    <body>
        <div class="container mt-4">
            <h1>Zones List</h1>
            <c:if test="${not empty requestScope.Notification}">
                <div class="alert alert-warning alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert">×</button>
                    <strong>${requestScope.Notification}</strong>
                </div>
            </c:if>
            <div class="search-box">
                <input type="hidden" name="service" value="zones" />
                <input type="text" id="searchInput" class="input-box" name="searchZone"
                       placeholder="Search by Zone or Product Name" value="${searchZone}" autocomplete="off" />
                <button type="button" class="clear-btn" id="clearBtn">
                    <i class="fa-solid fa-xmark"></i>
                </button>
            </div>
            <div class="action-bar d-flex align-items-center">
                <c:if test="${sessionScope.role == 'owner'}">
                    <a href="${pageContext.request.contextPath}/zones?service=addZone" class="btn btn-outline-primary mr-2">
                        Add Zone
                    </a>
                </c:if>
            </div>
            <div class="checkbox-container">
                <input type="checkbox" id="showInactive" name="showInactive" ${requestScope.showInactive ? 'checked' : ''}>
                <label for="showInactive">Show Inactive Zones</label>
            </div>
            <div class="showing-info" style="margin-top: 10px; margin-bottom: -15px">
                Showing: 
                <select id="pageSizeSelect">
                    <option value="5" ${pageSize == 5 ? 'selected' : ''}>5</option>
                    <option value="10" ${pageSize == 10 ? 'selected' : ''}>10</option>
                    <option value="20" ${pageSize == 20 ? 'selected' : ''}>20</option>
                </select>
                of ${totalRecords} zones
            </div>
            <div class="table-container mt-4">
                <table class="table table-striped table-hover table-bordered" id="zoneTable">
                    <thead>
                        <tr>
                            <th class="sortable" data-sort="id">
                                ID <i class="fa ${sortBy == 'id' && sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down'}"></i>
                            </th>
                            <th class="sortable" data-sort="name">
                                Name <i class="fa ${sortBy == 'name' && sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down'}"></i>
                            </th>
                            <th>Store ID</th>
                            <th>Product Name</th>
                            <th>Created By</th>
                            <th>Status</th>
                                <c:if test="${sessionScope.role == 'owner'}">
                                <th>Actions</th>
                                </c:if>
                        </tr>
                    </thead>
                    <tbody id="zoneTableBody">
                        <c:forEach var="zone" items="${list}">
                            <tr>
                                <td>${zone.id}</td>
                                <td style="text-align: left;">${zone.name}</td>
                                <td>${zone.storeId != null ? zone.storeId.id : 'null'}</td>
                                <td>${zone.productId != null ? zone.productId.name : 'N/A'}</td>
                                <td>${zone.createdBy}</td>
                                <td>${zone.status}</td>
                                <c:if test="${sessionScope.role == 'owner'}">
                                    <td>
                                        <a href="${pageContext.request.contextPath}/zones?service=getZoneById&zone_id=${zone.id}&index=${index}&sortBy=${sortBy}&sortOrder=${sortOrder}" class="btn btn-outline-primary">View</a>
                                        <a href="${pageContext.request.contextPath}/zones?service=editZone&zone_id=${zone.id}&index=${index}&sortBy=${sortBy}&sortOrder=${sortOrder}" class="btn btn-outline-primary">Edit</a>
                                        <a href="${pageContext.request.contextPath}/zones?service=stockCheck&zone_id=${zone.id}" class="btn btn-outline-success">Stock Check</a>
                                        <a href="${pageContext.request.contextPath}/zones?service=viewStockCheckHistory&zone_id=${zone.id}" class="btn btn-outline-info">History</a>
                                    </td>
                                </c:if>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="container d-flex justify-content-center mt-4" id="pagination">
                <ul class="pagination">
                    <c:if test="${index > 1}">
                        <li class="page-item">
                            <a class="page-link" href="zones?service=zones&searchZone=${searchZone}&index=${index - 1}&sortBy=${sortBy}&sortOrder=${sortOrder}&pageSize=${pageSize}&showInactive=${requestScope.showInactive}">
                                <i class="fa fa-angle-left"></i>
                            </a>
                        </li>
                    </c:if>
                    <li class="page-item ${index == 1 ? 'active' : ''}">
                        <a class="page-link" href="zones?service=zones&searchZone=${searchZone}&index=1&sortBy=${sortBy}&sortOrder=${sortOrder}&pageSize=${pageSize}&showInactive=${requestScope.showInactive}">1</a>
                    </li>
                    <c:if test="${index > 3}">
                        <li class="page-item disabled"><span class="page-link">...</span></li>
                        </c:if>
                        <c:forEach begin="${index - 1}" end="${index + 1}" var="page">
                            <c:if test="${page > 1 && page < endPage}">
                            <li class="page-item ${index == page ? 'active' : ''}">
                                <a class="page-link" href="zones?service=zones&searchZone=${searchZone}&index=${page}&sortBy=${sortBy}&sortOrder=${sortOrder}&pageSize=${pageSize}&showInactive=${requestScope.showInactive}">
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
                            <a class="page-link" href="zones?service=zones&searchZone=${searchZone}&index=${endPage}&sortBy=${sortBy}&sortOrder=${sortOrder}&pageSize=${pageSize}&showInactive=${requestScope.showInactive}">
                                ${endPage}
                            </a>
                        </li>
                    </c:if>
                    <c:if test="${index < endPage}">
                        <li class="page-item">
                            <a class="page-link" href="zones?service=zones&searchZone=${searchZone}&index=${index + 1}&sortBy=${sortBy}&sortOrder=${sortOrder}&pageSize=${pageSize}&showInactive=${requestScope.showInactive}">
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
        <script>
            let timeout = null;
            let currentSortBy = '${sortBy}';
            let currentSortOrder = '${sortOrder}';
            let currentPageSize = ${pageSize != null ? pageSize : 5};
            let totalRecords = ${totalRecords != null ? totalRecords : 0};
            const role = '${sessionScope.role}';
            let showInactive = ${requestScope.showInactive != null ? requestScope.showInactive : true};
            function searchZones(keyword, page, sortBy, sortOrder, pageSize, showInactive) {
            $.ajax({
            url: '<%= request.getContextPath() %>/zones',
                    type: 'GET',
                    data: {
                    service: 'searchZonesAjax',
                            keyword: keyword,
                            index: page,
                            sortBy: sortBy,
                            sortOrder: sortOrder,
                            pageSize: pageSize,
                            showInactive: showInactive
                    },
                    success: function (response) {
                    console.log("AJAX response:", response);
                    if (typeof response !== 'object' || !response.zones || !response.totalRecords) {
                    console.error("Invalid JSON response:", response);
                    $('#zoneTableBody').html('<tr><td colspan="' + (role === 'owner' ? 7 : 6) + '">No zone found</td></tr>');
                    return;
                    }
                    totalRecords = response.totalRecords;
                    updateTable(response.zones, response.endPage, response.index, keyword);
                    updatePagination(response.endPage, page, keyword);
                    updateShowingInfo(pageSize, response.totalRecords);
                    },
                    error: function (xhr, status, error) {
                    console.error("AJAX error:", status, error);
                    $('#zoneTableBody').html('<tr><td colspan="' + (role === 'owner' ? 7 : 6) + '">Error fetching zones</td></tr>');
                    }
            });
            }

            function loadDefaultZones() {
            const currentIndex = ${index};
            console.log("Loading default zones with index:", currentIndex);
            searchZones('', currentIndex, currentSortBy, currentSortOrder, currentPageSize, showInactive);
            }

            function updateTable(zones, endPage, currentIndex, keyword) {
            console.log("Zones data:", zones);
            const tbody = $('#zoneTableBody');
            tbody.empty();
            if (zones.length === 0) {
            tbody.append('<tr><td colspan="' + (role === 'owner' ? 7 : 6) + '">No zones found</td></tr>');
            } else {
            zones.forEach(function (zone) {
            console.log("Processing zone:", zone);
            let row = '<tr>' +
                    '<td>' + zone.id + '</td>' +
                    '<td style="text-align: left;">' + zone.name + '</td>' +
                    '<td>' + (zone.storeId ? zone.storeId.id : 'null') + '</td>' +
                    '<td>' + (zone.productName ? zone.productName : 'N/A') + '</td>' +
                    '<td>' + zone.createdBy + '</td>' +
                    '<td>' + zone.status + '</td>';
            if (role === 'owner') {
            row += '<td>' +
                    '<a href="<%= request.getContextPath() %>/zones?service=getZoneById&zone_id=' + zone.id + '&index=' + currentIndex + '&sortBy=' + currentSortBy + '&sortOrder=' + currentSortOrder + '" class="btn btn-outline-primary">View</a> ' +
                    '<a href="<%= request.getContextPath() %>/zones?service=editZone&zone_id=' + zone.id + '&index=' + currentIndex + '&sortBy=' + currentSortBy + '&sortOrder=' + currentSortOrder + '" class="btn btn-outline-primary">Edit</a>' +
                    '<a href="<%= request.getContextPath() %>/zones?service=stockCheck&zone_id=' + zone.id" class="btn btn - outline - success">Stock Check</a> ' +
                    '<a href="<%= request.getContextPath() %>/zones?service=viewStockCheckHistory&zone_id=' + zone.id" class="btn btn - outline - info">History</a> ' +
                    '</td>';
            }
            row += '</tr>';
            tbody.append(row);
            });
            }

            updatePagination(endPage, currentIndex, keyword);
            }

            function updatePagination(endPage, currentIndex, keyword) {
            const pagination = $('#pagination ul');
            pagination.empty();
            if (currentIndex > 1) {
            pagination.append(
                    '<li class="page-item"><a class="page-link page-nav" data-page="' + (currentIndex - 1) + '"><i class="fa fa-angle-left"></i></a></li>'
                    );
            }

            pagination.append(
                    '<li class="page-item ' + (currentIndex === 1 ? 'active' : '') + '"><a class="page-link page-nav" data-page="1">1</a></li>'
                    );
            if (currentIndex > 3) {
            pagination.append('<li class="page-item disabled"><span class="page-link">...</span></li>');
            }

            for (let page = currentIndex - 1; page <= currentIndex + 1; page++) {
            if (page > 1 && page < endPage) {
            pagination.append(
                    '<li class="page-item ' + (currentIndex === page ? 'active' : '') + '"><a class="page-link page-nav" data-page="' + page + '">' + page + '</a></li>'
                    );
            }
            }

            if (currentIndex < endPage - 2) {
            pagination.append('<li class="page-item disabled"><span class="page-link">...</span></li>');
            }

            if (endPage > 1) {
            pagination.append(
                    '<li class="page-item ' + (currentIndex === endPage ? 'active' : '') + '"><a class="page-link page-nav" data-page="' + endPage + '">' + endPage + '</a></li>'
                    );
            }

            if (currentIndex < endPage) {
            pagination.append(
                    '<li class="page-item"><a class="page-link page-nav" data-page="' + (currentIndex + 1) + '"><i class="fa fa-angle-right"></i></a></li>'
                    );
            }

            $('.page-nav').on('click', function (e) {
            e.preventDefault();
            const page = $(this).data('page');
            searchZones(keyword, page, currentSortBy, currentSortOrder, currentPageSize, showInactive);
            });
            }

            function updateShowingInfo(pageSize, totalRecords) {
            $('.showing-info').html('Showing: <select id="pageSizeSelect">' +
                    '<option value="5" ' + (pageSize == 5 ? 'selected' : '') + '>5</option>' +
                    '<option value="10" ' + (pageSize == 10 ? 'selected' : '') + '>10</option>' +
                    '<option value="20" ' + (pageSize == 20 ? 'selected' : '') + '>20</option>' +
                    '</select> of ' + totalRecords + ' zones');
            $('#pageSizeSelect').on('change', function () {
            currentPageSize = parseInt($(this).val());
            searchZones($('#searchInput').val().trim(), 1, currentSortBy, currentSortOrder, currentPageSize, showInactive);
            });
            }

            function updateSortIcons() {
            $('.sortable').each(function () {
            const sortBy = $(this).data('sort');
            const icon = $(this).find('i');
            if (sortBy === currentSortBy) {
            icon.removeClass('fa-sort-up fa-sort-down');
            icon.addClass(currentSortOrder === 'ASC' ? 'fa-sort-up' : 'fa-sort-down');
            } else {
            icon.removeClass('fa-sort-up fa-sort-down');
            icon.addClass('fa-sort-down');
            }
            });
            }

            $(document).ready(function () {
            $('#searchInput').on('keyup', function () {
            clearTimeout(timeout);
            const keyword = $(this).val().trim();
            timeout = setTimeout(function () {
            searchZones(keyword, 1, currentSortBy, currentSortOrder, currentPageSize, showInactive);
            }, 300);
            });
            $('.sortable').on('click', function () {
            const sortBy = $(this).data('sort');
            const keyword = $('#searchInput').val().trim();
            if (currentSortBy === sortBy) {
            currentSortOrder = currentSortOrder === 'ASC' ? 'DESC' : 'ASC';
            } else {
            currentSortBy = sortBy;
            currentSortOrder = 'ASC';
            }
            updateSortIcons();
            searchZones(keyword, 1, currentSortBy, currentSortOrder, currentPageSize, showInactive);
            });
            $('#clearBtn').on('click', function () {
            $('#searchInput').val('');
            searchZones('', 1, currentSortBy, currentSortOrder, currentPageSize, showInactive);
            });
            $('#pageSizeSelect').on('change', function () {
            currentPageSize = parseInt($(this).val());
            searchZones($('#searchInput').val().trim(), 1, currentSortBy, currentSortOrder, currentPageSize, showInactive);
            });
            $('#showInactive').on('change', function () {
            showInactive = $(this).is(':checked');
            searchZones($('#searchInput').val().trim(), 1, currentSortBy, currentSortOrder, currentPageSize, showInactive);
            });
            updateSortIcons();
            loadDefaultZones();
            });
        </script>
    </body>
</html>