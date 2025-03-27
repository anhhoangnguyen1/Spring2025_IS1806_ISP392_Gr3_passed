<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Zone History</title>
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
                background-color: #f1f1f1;
                border-radius: 50px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                z-index: 10;
            }
            .input-box {
                border: none;
                outline: none;
                background: none;
                padding: 0 15px;
                font-size: 14px;
                width: 100%;
                height: 100%;
            }
            .clear-btn {
                border: none;
                background: none;
                padding: 0 15px;
                cursor: pointer;
                display: flex;
                align-items: center;
            }
        </style>
    </head>
    <body>
        <div class="container mt-4">
            <h2>History of Zone: ${zone.name}</h2>
            <div class="search-box">
                <input type="text" id="searchInput" class="input-box" placeholder="Search by Product Name" value="${searchHistory}" autocomplete="off"/>
                <button type="button" class="clear-btn" id="clearSearchBtn"><i class="fa-solid fa-xmark"></i></button>
            </div>
            <div class="filter-box" style="display: none;">
                <input type="text" id="filterIdInput" class="input-box" placeholder="Filter by ID" value="${filterId}" autocomplete="off"/>
                <button type="button" class="clear-btn" id="clearFilterBtn"><i class="fa-solid fa-xmark"></i></button>
            </div>
            <div class="action-bar d-flex align-items-center mb-3">
                <a href="zones?service=zones&index=${param.index}&sortBy=${param.sortBy}&sortOrder=${param.sortOrder}" class="btn btn-outline-secondary">Back to Zones</a>
                <button id="sortButton" class="btn btn-outline-info ml-3">
                    Sort by ${sortHistoryBy == 'id' ? 'ID' : 'Product Name'} (${sortHistoryOrder == 'ASC' ? 'Ascending' : 'Descending'})
                </button>
            </div>
            <div class="table-container">
                <table class="table table-striped table-hover table-bordered" id="historyTable">
                    <thead>
                        <tr>
                            <th class="sortable" data-sort="id">ID</th>
                            <th class="sortable" data-sort="productName">Product Name</th>
                            <th>Start Date</th>
                            <th>End Date</th>
                            <th>Updated By</th>
                        </tr>
                    </thead>
                    <tbody id="historyTableBody">
                        <c:choose>
                            <c:when test="${empty historyList}">
                                <tr><td colspan="5">No history available.</td></tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="entry" items="${historyList}">
                                    <tr>
                                        <td>${entry['id']}</td>
                                        <td>${entry['productName']}</td>
                                        <td>${entry['startDate']}</td>
                                        <td>${entry['endDate'] != null ? entry['endDate'] : 'Now'}</td>
                                        <td>${entry['updatedBy']}</td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
            <div class="container d-flex justify-content-center mt-4" id="pagination">
                <ul class="pagination">
                    <c:if test="${historyPageIndex > 1}">
                        <li class="page-item"><a class="page-link page-nav" data-page="${historyPageIndex - 1}"><i class="fa fa-angle-left"></i></a></li>
                    </c:if>
                    <li class="page-item ${historyPageIndex == 1 ? 'active' : ''}"><a class="page-link page-nav" data-page="1">1</a></li>
                    <c:if test="${historyPageIndex > 3}">
                        <li class="page-item disabled"><span class="page-link">...</span></li>
                    </c:if>
                    <c:forEach begin="${historyPageIndex - 1 > 0 ? historyPageIndex - 1 : 1}" end="${historyPageIndex + 1 < historyEndPage ? historyPageIndex + 1 : historyEndPage}" var="page">
                        <c:if test="${page > 1 && page < historyEndPage}">
                            <li class="page-item ${historyPageIndex == page ? 'active' : ''}"><a class="page-link page-nav" data-page="${page}">${page}</a></li>
                        </c:if>
                    </c:forEach>
                    <c:if test="${historyPageIndex < historyEndPage - 2}">
                        <li class="page-item disabled"><span class="page-link">...</span></li>
                    </c:if>
                    <c:if test="${historyEndPage > 1}">
                        <li class="page-item ${historyPageIndex == historyEndPage ? 'active' : ''}"><a class="page-link page-nav" data-page="${historyEndPage}">${historyEndPage}</a></li>
                    </c:if>
                    <c:if test="${historyPageIndex < historyEndPage}">
                        <li class="page-item"><a class="page-link page-nav" data-page="${historyPageIndex + 1}"><i class="fa fa-angle-right"></i></a></li>
                    </c:if>
                </ul>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
        <script>
            let timeout = null;
            let zoneId = ${zone.id};
            let currentSortBy = '${sortHistoryBy}';
            let currentSortOrder = '${sortHistoryOrder}';
            let currentPage = ${historyPageIndex};

            function searchHistory(keyword, filterId, page, sortBy, sortOrder) {
                if (page < 1) page = 1;
                $.ajax({
                    url: '<%= request.getContextPath() %>/zones',
                    type: 'GET',
                    data: {
                        service: 'searchHistoryAjax',
                        zone_id: zoneId,
                        searchHistory: keyword,
                        filterId: filterId,
                        sortHistoryBy: sortBy,
                        sortHistoryOrder: sortOrder,
                        historyPageIndex: page
                    },
                    success: function (response) {
                        updateTable(response.historyList, response.historyEndPage, response.historyPageIndex, keyword, filterId);
                        updatePagination(response.historyEndPage, response.historyPageIndex, keyword, filterId);
                    },
                    error: function () {
                        $('#historyTableBody').html('<tr><td colspan="5">Error fetching history</td></tr>');
                    }
                });
            }

            function loadDefaultHistory() {
                const keyword = $('#searchInput').val().trim();
                const filterId = $('#filterIdInput').val().trim();
                searchHistory(keyword, filterId, currentPage, currentSortBy, currentSortOrder);
            }

            function updateTable(historyList, endPage, currentPage, keyword, filterId) {
                const tbody = $('#historyTableBody');
                tbody.empty();
                if (historyList.length === 0) {
                    tbody.append('<tr><td colspan="5">No history available.</td></tr>');
                } else {
                    historyList.forEach(function (entry) {
                        tbody.append(
                            '<tr>' +
                            '<td>' + entry.id + '</td>' + // Đảm bảo hiển thị id dưới dạng số
                            '<td>' + entry.productName + '</td>' +
                            '<td>' + entry.startDate + '</td>' +
                            '<td>' + (entry.endDate || 'Now') + '</td>' +
                            '<td>' + entry.updatedBy + '</td>' +
                            '</tr>'
                        );
                    });
                }
            }

            function updatePagination(endPage, currentPage, keyword, filterId) {
                const pagination = $('#pagination ul');
                pagination.empty();
                if (currentPage > 1) {
                    pagination.append('<li class="page-item"><a class="page-link page-nav" data-page="' + (currentPage - 1) + '"><i class="fa fa-angle-left"></i></a></li>');
                }
                pagination.append('<li class="page-item ' + (currentPage === 1 ? 'active' : '') + '"><a class="page-link page-nav" data-page="1">1</a></li>');
                if (currentPage > 3) pagination.append('<li class="page-item disabled"><span class="page-link">...</span></li>');
                for (let page = currentPage - 1; page <= currentPage + 1; page++) {
                    if (page > 1 && page < endPage) {
                        pagination.append('<li class="page-item ' + (currentPage === page ? 'active' : '') + '"><a class="page-link page-nav" data-page="' + page + '">' + page + '</a></li>');
                    }
                }
                if (currentPage < endPage - 2) pagination.append('<li class="page-item disabled"><span class="page-link">...</span></li>');
                if (endPage > 1) {
                    pagination.append('<li class="page-item ' + (currentPage === endPage ? 'active' : '') + '"><a class="page-link page-nav" data-page="' + endPage + '">' + endPage + '</a></li>');
                }
                if (currentPage < endPage) {
                    pagination.append('<li class="page-item"><a class="page-link page-nav" data-page="' + (currentPage + 1) + '"><i class="fa fa-angle-right"></i></a></li>');
                }
                $('.page-nav').on('click', function (e) {
                    e.preventDefault();
                    currentPage = $(this).data('page');
                    searchHistory(keyword, filterId, currentPage, currentSortBy, currentSortOrder);
                });
            }

            $(document).ready(function () {
                $('#searchInput').on('keyup', function () {
                    clearTimeout(timeout);
                    const keyword = $(this).val().trim();
                    const filterId = $('#filterIdInput').val().trim();
                    timeout = setTimeout(function () {
                        currentPage = 1;
                        searchHistory(keyword, filterId, currentPage, currentSortBy, currentSortOrder);
                    }, 300);
                });

                $('#filterIdInput').on('keyup', function () {
                    clearTimeout(timeout);
                    const keyword = $('#searchInput').val().trim();
                    const filterId = $(this).val().trim();
                    // Sửa: Kiểm tra filterId là số hợp lệ trước khi gửi
                    if (filterId !== '' && !/^\d+$/.test(filterId)) {
                        $('#filterIdInput').val(''); // Xóa nếu không phải số
                        return;
                    }
                    timeout = setTimeout(function () {
                        currentPage = 1;
                        searchHistory(keyword, filterId, currentPage, currentSortBy, currentSortOrder);
                    }, 300);
                });

                $('#clearSearchBtn').on('click', function () {
                    $('#searchInput').val('');
                    loadDefaultHistory();
                });

                $('#clearFilterBtn').on('click', function () {
                    $('#filterIdInput').val('');
                    loadDefaultHistory();
                });

                $('.sortable').on('click', function () {
                    const sortBy = $(this).data('sort');
                    if (currentSortBy === sortBy) {
                        currentSortOrder = (currentSortOrder === 'ASC') ? 'DESC' : 'ASC';
                    } else {
                        currentSortBy = sortBy;
                        currentSortOrder = 'ASC';
                    }
                    $('#sortButton').text('Sort by ' + (currentSortBy === 'id' ? 'ID' : 'Product Name') + ' (' + (currentSortOrder === 'ASC' ? 'Ascending' : 'Descending') + ')');
                    const keyword = $('#searchInput').val().trim();
                    const filterId = $('#filterIdInput').val().trim();
                    currentPage = 1;
                    searchHistory(keyword, filterId, currentPage, currentSortBy, currentSortOrder);
                });

                $('#sortButton').on('click', function () {
                    currentSortOrder = (currentSortOrder === 'ASC') ? 'DESC' : 'ASC';
                    $(this).text('Sort by ' + (currentSortBy === 'id' ? 'ID' : 'Product Name') + ' (' + (currentSortOrder === 'ASC' ? 'Ascending' : 'Descending') + ')');
                    const keyword = $('#searchInput').val().trim();
                    const filterId = $('#filterIdInput').val().trim();
                    currentPage = 1;
                    searchHistory(keyword, filterId, currentPage, currentSortBy, currentSortOrder);
                });

                loadDefaultHistory();
            });
        </script>
    </body>
</html>