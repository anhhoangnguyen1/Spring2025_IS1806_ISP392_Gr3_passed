<%-- 
    Document   : listCustomers
    Created on : Feb 14, 2025, 5:03:55 PM
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
        <title>Customer List</title>
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
            <h1>Customers List</h1>
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
                <input type="hidden" name="service" value="customers" />
                <input type="text" id="searchInput" class="input-box" name="searchCustomer"
                       placeholder="Search for information."
                       value="${searchCustomer}" autocomplete="off" />              
                <button type="button" class="search-btn">
                    <i class="fa-solid fa-search"></i>
                </button>
            </div>



            <div class="action-bar d-flex align-items-center">
                <a href="${pageContext.request.contextPath}/Customers?service=addCustomer" class="btn btn-outline-primary mr-lg-auto">
                    Add Customer
                </a>

                <div class="filter-box">
                    <label for="balanceFilter">Balance Filter: </label>
                    <select id="balanceFilter" class="form-control w-auto">
                        <option value="all" ${balanceFilter == 'all' ? 'selected' : ''}>All</option>
                        <option value="0" ${balanceFilter == '0' ? 'selected' : ''}>No debt (balance = 0)</option>
                        <option value="positive" ${balanceFilter == 'positive' ? 'selected' : ''}>Positive balance (balance > 0)</option>
                        <option value="negative" ${balanceFilter == 'negative' ? 'selected' : ''}>Negative balance (balance < 0)</option>
                    </select>
                </div>
            </div>
            <!-- Table Container -->
            <div class="table-container mt-4">

                <table class="table table-striped table-hover table-bordered" id="customerTable">
                    <thead>
                        <tr>
                            <th class="sortable" data-sort="id">
                                ID <i class="fa ${sortBy == 'id' && sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down'}"></i>
                            </th>
                            <th class="resizable" data-sort="name">Name</th>
                            <th class="resizable">Phone</th>
                            <th class="resizable">Address</th>
                            <th class="sortable" data-sort="balance">
                                Balance <i class="fa ${sortBy == 'balance' && sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down'}"></i>
                            </th>
                            <th class="sortable" data-sort="createdAt">Created At <i class="fa fa-sort-down"></i></th>
                            <th class="resizable">Created By</th>
                            <th class="sticky-col">Actions</th>
                        </tr>
                    </thead>
                    <tbody id = "customerTableBody">
                        <c:forEach var="customer" items="${list}">
                            <c:if test="${(sessionScope.role == 'staff' && user.role == 'owner') || (sessionScope.role == 'owner' && user.storeId == sessionScope.storeId) || sessionScope.role == 'staff'}">
                                <tr>
                                    <td>${customer.id}</td>
                                    <td>${customer.name}</td>
                                    <td>
                                        <c:if test="${sessionScope.role == 'owner'}">
                                            ${customer.phone} 
                                        </c:if>
                                        <c:if test="${sessionScope.role == 'staff'}">
                                            ${customer.phone.substring(0, 3)}xxxxx${customer.phone.substring(customer.phone.length() - 2)} <!-- Hiển thị số điện thoại ẩn nếu là staff -->
                                        </c:if>
                                    </td>
                                    <td>${customer.address}</td>
                                    <td><fmt:formatNumber value="${customer.balance}" type="number" pattern="#,###" /></td>
                                    <td>${customer.createdAt}</td>
                                    <td>${customer.createdBy}</td>
                                    <td class="sticky-col">
                                        <button type="button" class="btn btn-outline-danger mr-lg-auto" data-toggle="modal" data-target="#debtListModal${customer.id}">
                                            Debt note
                                        </button>
                                        <c:if test="${sessionScope.role != 'staff'}">
                                            <a href="${pageContext.request.contextPath}/Customers?service=editCustomer&customer_id=${customer.id}" class="btn btn-outline-primary">
                                                Edit
                                            </a>
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
                            <a class="page-link" href="Customers?service=customers&searchCustomer=${searchCustomer}&index=${index - 1}&sortBy=${sortBy}&sortOrder=${sortOrder}">
                                <i class="fa fa-angle-left"></i>
                            </a>
                        </li>
                    </c:if>

                    <li class="page-item ${index == 1 ? 'active' : ''}">
                        <a class="page-link" href="Customers?service=customers&searchCustomer=${searchCustomer}&index=1&sortBy=${sortBy}&sortOrder=${sortOrder}">1</a>
                    </li>

                    <c:if test="${index > 3}">
                        <li class="page-item disabled">
                            <span class="page-link">...</span>
                        </li>
                    </c:if>
                    <c:forEach var="customer" items="${list}" begin="${index > 0 ? index : 1}">
                        <c:if test="${page > 1 && page < endPage}">
                            <li class="page-item ${index == page ? 'active' : ''}">
                                <a class="page-link" href="Customers?service=customers&searchCustomer=${searchCustomer}&index=${page}&sortBy=${sortBy}&sortOrder=${sortOrder}">
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
                            <a class="page-link" href="Customers?service=customers&searchCustomer=${searchCustomer}&index=${endPage}&sortBy=${sortBy}&sortOrder=${sortOrder}">
                                ${endPage}
                            </a>
                        </li>
                    </c:if>


                    <c:if test="${index < endPage}">
                        <li class="page-item">
                            <a class="page-link" href="Customers?service=customers&searchCustomer=${searchCustomer}&index=${index + 1}&sortBy=${sortBy}&sortOrder=${sortOrder}">
                                <i class="fa fa-angle-right"></i>
                            </a>
                        </li>
                    </c:if>
                </ul>

                <div class="page-select">
                    <label for="pageSelect">Go to page: </label>
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
                let storeID = '${storeID}';
                let balanceFilter = '${balanceFilter}';




                $('#balanceFilter').on('change', function () {
                    balanceFilter = $(this).val();
                    const keyword = $('#searchInput').val().trim();
                    searchCustomers(keyword, 1, currentSortBy, currentSortOrder, storeID, balanceFilter);
                });


                $('#pageSelect').on('change', function () {
                    const selectedPage = $(this).val();
                    const keyword = $('#searchInput').val().trim();
                    searchCustomers(keyword, selectedPage, currentSortBy, currentSortOrder, storeID, balanceFilter);
                });

                function searchCustomers(keyword, page, sortBy, sortOrder, storeID, balanceFilter) {
                    $.ajax({
                        url: '<%= request.getContextPath() %>/Customers',
                        type: 'GET',
                        data: {
                            service: 'searchCustomersAjax',
                            searchCustomer: keyword,
                            index: page,
                            sortBy: sortBy,
                            sortOrder: sortOrder,
                            storeID: storeID,
                            balanceFilter: balanceFilter
                        },
                        success: function (response) {
                            updateTable(response.customers, response.endPage, response.index, keyword);

                            updatePagination(response.endPage, page, keyword);
                        },
                        error: function () {
                            $('#customerTablebody').html('<tr><td colspan="8">Error fetching customers</td></tr>');
                        }
                    });
                }

                function loadDefaultCustomers() {
                    const currentIndex = ${index};
                    searchCustomers('', currentIndex, currentSortBy, currentSortOrder, storeID, balanceFilter);
                }


                function updateTable(customers, endPage, currentIndex, keyword) {
                    const tbody = $('#customerTableBody');
                    tbody.empty();

                    if (customers.length === 0) {
                        tbody.append('<tr><td colspan="8">No customers found</td></tr>');
                    } else {
                        customers.forEach(function (customer) {
                            let phoneDisplay = customer.phone;
                            if ('${sessionScope.role}' === 'staff') {
                                phoneDisplay = customer.phone.substring(0, 3) + 'xxxxx' + customer.phone.substring(customer.phone.length - 2);
                            }
                            let editButton = '';
                            if ('${sessionScope.role}' !== 'staff') {
                                editButton = '<a href="${pageContext.request.contextPath}/Customers?service=editCustomer&customer_id=' + customer.id + '" class="btn btn-outline-primary">Edit</a>';
                            }
                            tbody.append(
                                    '<tr>' +
                                    '<td>' + customer.id + '</td>' +
                                    '<td>' + customer.name + '</td>' +
                                    '<td>' + phoneDisplay + '</td>' +
                                    '<td>' + customer.address + '</td>' +
                                    '<td>' + formatNumber(customer.balance) + '</td>' +
                                    '<td>' + customer.createdAt + '</td>' +
                                    '<td>' + customer.createdBy + '</td>' +
                                    '<td class="sticky-col">' +
                                    '<a href="${pageContext.request.contextPath}/Debts?service=debtInCustomers&customer_id=' + customer.id + '" class="btn btn-outline-danger mr-lg-auto">Debt note</a> ' +
                                    editButton + // Chỉ hiển thị nút Edit nếu không phải staff
                                    '</td>' +
                                    '</tr>'
                                    );
                        });
                    }
                }

                function formatNumber(number) {

                    return Number(number).toFixed(0).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                }



// cnflict in master
//                $(".openDebtModal").on("click", function () {
//                    let currentPage = sessionStorage.getItem("customerPageIndex") || 1;
//                    sessionStorage.setItem("prevCustomerPageIndex", currentPage); // Lưu trang trước đó
//                });
//					document.addEventListener("DOMContentLoaded", function () {
//                    let savedIndex = sessionStorage.getItem("prevCustomerPageIndex") || 1;
//                    searchCustomers('', savedIndex, 'name', 'asc'); // Load trang đã xem trước đó
//                });
//
                function updatePagination(endPage, currentIndex, keyword) {
                    const pagination = $('.pagination');
                    pagination.empty();

                    if (currentIndex > 1) {
                        pagination.append(
                                '<li class="page-item"><a class="page-link page-nav" data-page="' + (currentIndex - 1) + '"><i class="fa fa-angle-left"></i></a></li>'
                                );
                    }

                    pagination.append(
                            '<li class="page-item ' + (currentIndex == 1 ? 'active' : '') + '"><a class="page-link page-nav" data-page="1">1</a></li>'
                            );

                    if (currentIndex > 3) {
                        pagination.append('<li class="page-item disabled"><span class="page-link">...</span></li>');
                    }

                    for (let page = currentIndex - 1; page <= currentIndex + 1; page++) {
                        if (page > 1 && page < endPage) {
                            pagination.append(
                                    '<li class="page-item ' + (currentIndex == page ? 'active' : '') + '"><a class="page-link page-nav" data-page="' + page + '">' + page + '</a></li>'
                                    );
                        }
                    }

                    if (currentIndex < endPage - 2) {
                        pagination.append('<li class="page-item disabled"><span class="page-link">...</span></li>');
                    }

                    if (endPage > 1) {
                        pagination.append(
                                '<li class="page-item ' + (currentIndex == endPage ? 'active' : '') + '"><a class="page-link page-nav" data-page="' + endPage + '">' + endPage + '</a></li>'
                                );
                    }

                    if (currentIndex < endPage) {
                        pagination.append(
                                '<li class="page-item"><a class="page-link page-nav" data-page="' + (currentIndex + 1) + '"><i class="fa fa-angle-right"></i></a></li>'
                                );
                    }

// cnflict in master
//                    $('#pageSelect').empty();
//                    for (let page = 1; page <= endPage; page++) {
//                        $('#pageSelect').append('<option value="' + page + '" ' + (currentIndex == page ? 'selected' : '') + '>' + page + '</option>');
//                    }

                    $('.page-nav').on('click', function (e) {
                        e.preventDefault();
                        const page = $(this).data('page');
                        searchCustomers(keyword, page, currentSortBy, currentSortOrder, storeID, balanceFilter);

                    });
                }


                function addCustomer(customer) {

                    $('#customerTableBody').append(
                            '<tr>' +
                            '<td>' + customer.id + '</td>' +
                            '<td>' + customer.name + '</td>' +
                            '<td>' + customer.phone + '</td>' +
                            '<td>' + customer.address + '</td>' +
                            '<td>' + customer.balance + '</td>' +
                            '<td>' + customer.createdAt + '</td>' +
                            '<td>' + customer.createdBy + '</td>' +
                            '</tr>'
                            );
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
                            searchCustomers(keyword, 1, currentSortBy, currentSortOrder);
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


                        searchCustomers(keyword, 1, currentSortBy, currentSortOrder);
                    });


                    $('#clearBtn').on('click', function () {
                        $('#searchInput').val('');
                        loadDefaultCustomers();
                    });


                    updateSortIcons();


                    loadDefaultCustomers();
                });
            </script>
    </body>
</html>