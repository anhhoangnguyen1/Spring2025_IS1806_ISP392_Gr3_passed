<%-- 
    Document   : listUsers
    Created on : Feb 18, 2025, 12:30:58 AM
    Author     : THC
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Users List</title>
        <style>
            .table-container {
                overflow-x: auto;
                max-width: 100%;
                flex-grow: 1;
            }
            .table {
                width: 100%;
                border-collapse: collapse;
            }
            .sortable {
                cursor: pointer;
            }
            .alert .close {
                position: absolute;
                top: 10px;
                right: 10px;
                padding: 0;
                border: none;
                background: transparent;
                font-size: 30px;


            }
            #pagination {
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 10px;
            }

            .page-select {
                display: flex;
                align-items: center;
            }

            .page-select label {
                margin-right: 10px;
            }

            .page-select select {
                width: 100px;
                padding: 5px;
            }

        </style>
    </head>
    <body>
        <div class="container mt-4">
            <h1>Users List</h1>
            <c:if test="${not empty sessionScope.Notification}">
                <div class="alert alert-warning alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert">
                        &times;
                    </button>
                    <strong>${sessionScope.Notification}</strong>
                </div>
                <c:remove var="Notification" scope="session" />
            </c:if>
            <c:if test="${not empty sessionScope.successMessage}">

                <div class="alert alert-success alert-dismissible fade show">
                    <strong>Success!</strong> ${sessionScope.successMessage}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <c:set var="successMessage" value="${sessionScope.successMessage}" />
                <c:remove var="successMessage" />
            </c:if>


            <div class="search-box">
                <input type="hidden" name="service" value="users" />
                <input type="text" id="searchInput" class="input-box" name="searchUser"
                       placeholder="Search for information."
                       value="${searchUser}" autocomplete="off" />

                <button type="button" class="search-btn">
                    <i class="fa-solid fa-search"></i>
                </button>
            </div>


            <div class="action-bar d-flex align-items-center">

                <a href="${pageContext.request.contextPath}/Users?service=createAccount" class="btn btn-outline-primary mr-lg-auto">
                    Add Staff
                </a>

            </div>
            <!-- Table Container -->
            <div class="table-container mt-4">

                <table class="table table-striped table-hover table-bordered" id="userTable">
                    <thead>
                        <tr>
                            <th class="sortable" data-sort="id">
                                ID <i class="fa ${sortBy == 'id' && sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down'}"></i>

                            </th>
                            <th class="resizable">Role</th>
                            <th class="sortable" data-sort="name">
                                Name <i class="fa ${sortBy == 'name' && sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down'}"></i>
                            </th>
                            <th class="resizable">Phone</th>
                            <th class="resizable">Address</th>
                            <th class="resizable">Gender</th>
                            <th class="resizable">Date of birth</th>
                            <th class="resizable">Email</th>
                            <th class="resizable">Status</th>
                            <th class="sticky-col">Actions</th>
                        </tr>
                    </thead>
                    <tbody id = "userTableBody">
                        <c:forEach var="user" items="${list}">
                            <c:if test="${(sessionScope.role == 'admin' && user.role == 'owner') || (sessionScope.role == 'owner' && user.role == 'staff' && user.storeId == sessionScope.storeId)}">
                                <tr>
                                    <td>${user.id}</td>
                                    <td>${user.role}</td>
                                    <td>${user.name}</td>
                                    <td>${user.phone}</td>
                                    <td>${user.address}</td>
                                    <td>${user.gender}</td>
                                    <td>${user.dob}</td>
                                    <td>${user.email}</td>
                                    <td>${user.status}</td>
                                    <td class="sticky-col">
                                        <a href="${pageContext.request.contextPath}/Users?service=editUser&user_id=${user.id}" class="btn btn-outline-primary">
                                            Edit
                                        </a>
                                        <c:if test="${sessionScope.role == 'owner' && user.role == 'staff'}">
                                            <c:choose>
                                                <c:when test="${user.status == 'Inactive'}">
                                                    <a href="Users?service=unBanUser&user_id=${user.id}" class="btn btn-outline-success">UnBan</a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="Users?service=banUser&user_id=${user.id}" class="btn btn-outline-danger">Ban</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>

                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </tbody>

                </table>
            </div>
            <div class="container d-flex justify-content-center mt-4" id="pagination">
                <ul class="pagination">
                    <c:if test="${index > 1}">
                        <li class="page-item">
                            <a class="page-link" href="Users?service=users&searchUser=${searchUser}&index=${index - 1}&sortBy=${sortBy}&sortOrder=${sortOrder}">
                                <i class="fa fa-angle-left"></i>
                            </a>
                        </li>
                    </c:if>

                    <li class="page-item ${index == 1 ? 'active' : ''}">
                        <a class="page-link" href="Users?service=users&searchUser=${searchUser}&index=1&sortBy=${sortBy}&sortOrder=${sortOrder}">1</a>
                    </li>

                    <c:if test="${index > 3}">
                        <li class="page-item disabled">
                            <span class="page-link">...</span>
                        </li>
                    </c:if>

                    <c:forEach var="user" items="${list}" begin="${index > 0 ? index : 1}">
                        <c:if test="${page > 1 && page < endPage}">
                            <li class="page-item ${index == page ? 'active' : ''}">
                                <a class="page-link" href="Users?service=users&searchUser=${searchUser}&index=${page}&sortBy=${sortBy}&sortOrder=${sortOrder}">
                                    ${page}
                                </a>
                            </li>
                        </c:if>
                    </c:forEach>

                    <c:if test="${index < endPage - 2}">
                        <li class="page-item disabled">
                            <span class="page-link">...</span>
                        </li>
                    </c:if>

                    <c:if test="${endPage > 1}">
                        <li class="page-item ${index == endPage ? 'active' : ''}">
                            <a class="page-link" href="Users?service=users&searchUser=${searchUser}&index=${endPage}&sortBy=${sortBy}&sortOrder=${sortOrder}">
                                ${endPage}
                            </a>
                        </li>
                    </c:if>

                    <c:if test="${index < endPage}">
                        <li class="page-item">
                            <a class="page-link" href="Users?service=users&searchUser=${searchUser}&index=${index + 1}&sortBy=${sortBy}&sortOrder=${sortOrder}">
                                <i class="fa fa-angle-right"></i>
                            </a>
                        </li>
                    </c:if>
                </ul>

                <!-- Dropdown Go to page -->
                <div class="page-select">
                    <label for="pageSelect" class="mr-2">Go to page: </label>
                    <select id="pageSelect" class="form-control w-auto">
                        <c:forEach var="page" begin="1" end="${endPage}">
                            <option value="${page}" ${page == index ? 'selected' : ''}>${page}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>



            <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
            <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
            <script type="text/javascript" src="<%= request.getContextPath() %>/css/script.js"></script>
            <script type="text/javascript">
                let timeout = null;
                let currentSortBy = '${sortBy}';
                let currentSortOrder = '${sortOrder}';

                $('#pageSelect').on('change', function () {
                    const selectedPage = $(this).val();
                    const keyword = $('#searchInput').val().trim();
                    searchUsers(keyword, selectedPage, currentSortBy, currentSortOrder);
                });

                function searchUsers(keyword, page, sortBy, sortOrder) {
                    $.ajax({
                        url: '<%= request.getContextPath() %>/Users',
                        type: 'GET',
                        data: {
                            service: 'searchUsersAjax',
                            searchUser: keyword,
                            index: page,
                            sortBy: sortBy,
                            sortOrder: sortOrder
                        },
                        success: function (response) {
                            updateTable(response.users, response.endPage, response.index, keyword);
                            updatePagination(response.endPage, page, keyword);
                        },
                        error: function () {
                            $('#userTableBody').html('<tr><td colspan="10">Error fetching users</td></tr>');
                        }
                    });
                }

                function updateTable(users, endPage, currentIndex, keyword) {
                    const tbody = $('#userTableBody');
                    tbody.empty();

                    if (users.length === 0) {
                    tbody.append('<tr><td colspan="10">No users found</td></tr>');
                    } else {
                    users.forEach(function (user) {
                    let banButton = '';
                            if ('${sessionScope.role}' === 'admin' && user.role === 'owner' ||
                                    ('${sessionScope.role}' === 'owner' && user.role === 'staff' && user.storeId === '${sessionScope.storeId}')) {

                    let banAction = '';
                            if ('${sessionScope.role}' === 'owner' && user.status === 'Inactive') {
                    banAction = '<a href="${pageContext.request.contextPath}/Users?service=unBanUser&user_id=' + user.id + '" class="btn btn-outline-success">UnBan</a>';
                    } else if ('${sessionScope.role}' === 'owner' && user.status === 'Active') {
                    banAction = '<a href="${pageContext.request.contextPath}/Users?service=banUser&user_id=' + user.id + '" class="btn btn-outline-danger">Ban</a>';
                    }


                    tbody.append(
                            '<tr>' +
                            '<td>' + user.id + '</td>' +
                            '<td>' + user.role + '</td>' +
                            '<td>' + user.name + '</td>' +
                            '<td>' + user.phone + '</td>' +
                            '<td>' + user.address + '</td>' +
                            '<td>' + user.gender + '</td>' +
                            '<td>' + user.dob + '</td>' +
                            '<td>' + user.email + '</td>' +
                            '<td>' + user.status + '</td>' +
                            '<td class="sticky-col">' +
                            '<a href="${pageContext.request.contextPath}/Users?service=editUser&user_id=' + user.id + '" class="btn btn-outline-primary">Edit</a>' +
                            banButton +
                            '</td>' +
                            '</tr>'
                            );
                    });
                    }
                    }


                    function updatePagination(endPage, currentIndex, keyword) {
                    const pagination = $('.pagination');
                    pagination.empty();

                    if (currentIndex > 1) {
                        pagination.append('<li class="page-item"><a class="page-link page-nav" data-page="' + (currentIndex - 1) + '"><i class="fa fa-angle-left"></i></a></li>');
                    }

                    pagination.append('<li class="page-item ' + (currentIndex === 1 ? 'active' : '') + '"><a class="page-link page-nav" data-page="1">1</a></li>');

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
                        pagination.append('<li class="page-item"><a class="page-link page-nav" data-page="' + (currentIndex + 1) + '"><i class="fa fa-angle-right"></i></a></li>');
                    }

                    // Cập nhật dropdown số trang
                    $('#pageSelect').empty();
                    for (let page = 1; page <= endPage; page++) {
                        $('#pageSelect').append('<option value="' + page + '" ' + (currentIndex === page ? 'selected' : '') + '>' + page + '</option>');
                    }

                    $('.page-nav').on('click', function (e) {
                        e.preventDefault();
                        const page = $(this).data('page');
                        searchUsers(keyword, page, currentSortBy, currentSortOrder);
                    });

                    // Lắng nghe sự kiện thay đổi trên dropdown để chọn trang
                    $('#pageSelect').on('change', function () {
                        const selectedPage = $(this).val();  // Lấy số trang được chọn
                        const keyword = $('#searchInput').val().trim();
                        searchUsers(keyword, selectedPage, currentSortBy, currentSortOrder);
                    });
                }

                $(document).ready(function () {
                    $('#searchInput').on('keyup', function () {
                        clearTimeout(timeout);
                        const keyword = $(this).val().trim();

                        timeout = setTimeout(function () {
                            searchUsers(keyword, 1, currentSortBy, currentSortOrder);
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

                        searchUsers(keyword, 1, currentSortBy, currentSortOrder);
                    });

                    loadDefaultUsers();
                });

                function loadDefaultUsers() {
                const currentIndex = ${index};
                        searchUsers('', currentIndex, currentSortBy, currentSortOrder);
                }

            </script>

    </body>
</html>