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
            .btn-danger {
                background-color: transparent;
                color: red;
                border: 1px solid red;
                padding: 8px 16px;
                text-align: center;
            }
            .btn-danger:hover {
                background-color: red;
                color: white;
                border: 1px solid red;
            }
            .sortable {
                cursor: pointer;
            }
            .container.mt-4 {
                position: relative; /* Đảm bảo container là phần tử cha để cố định search-box */
                padding-top: 15px; /* Tạo khoảng trống cho search-box */
            }

            .search-box {
                position: absolute;
                top: 20px; /* Đặt cố định ở trên cùng của container */
                left: 50%;
                transform: translateX(-50%);
                width: 400px;
                height: 37px;
                background-color: var(--bg-ternary);
                border-radius: 50px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                z-index: 10; /* Đảm bảo search-box nằm trên các phần tử khác */
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
                       placeholder="Search for zones" value="${searchZone}" autocomplete="off" />
                <button type="button" class="clear-btn" id="clearBtn">
                    <i class="fa-solid fa-xmark"></i>
                </button>
            </div>
            <div class="action-bar d-flex align-items-center">
                <a href="${pageContext.request.contextPath}/zones?service=addZone" class="btn btn-outline-primary mr-lg-auto">
                    Add Zone
                </a>
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
                            <th>Created By</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody id="zoneTableBody">
                        <c:forEach var="zone" items="${list}">
                            <tr>
                                <td>${zone.id}</td>
                                <td style="text-align: left;">${zone.name}</td>
                                <td>${zone.storeId != null ? zone.storeId.id : 'N/A'}</td>
                                <td>${zone.createdBy}</td>
                                <td>${zone.status}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/zones?service=getZoneById&zone_id=${zone.id}" class="btn btn-outline-primary">View</a>
                                    <a href="${pageContext.request.contextPath}/zones?service=editZone&zone_id=${zone.id}" class="btn btn-outline-primary">Edit</a>
                                    <a href="${pageContext.request.contextPath}/zones?service=deleteZone&zone_id=${zone.id}&index=${index}&searchZone=${searchZone}&sortBy=${sortBy}&sortOrder=${sortOrder}" 
                                       class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this zone?');">Ban</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="container d-flex justify-content-center mt-4" id="pagination">
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
        <script>

       let timeout = null;
       let currentSortBy = '${sortBy}'; // Lưu giá trị sortBy hiện tại
       let currentSortOrder = '${sortOrder}'; // Lưu giá trị sortOrder hiện tại
       // [NOTE: Sửa tại đây] Định nghĩa hàm searchZones ngoài phạm vi $(document).ready() để nó trở thành hàm toàn cục
       function searchZones(keyword, page, sortBy, sortOrder) {
           $.ajax({
               url: '<%= request.getContextPath() %>/zones',
               type: 'GET',
               data: {
                   service: 'searchZonesAjax',
                   keyword: keyword,
                   index: page,
                   sortBy: sortBy,
                   sortOrder: sortOrder
               },
               success: function (response) {
                   updateTable(response.zones, response.endPage, response.index, keyword);
                   // Cập nhật phân trang với page hiện tại
                   updatePagination(response.endPage, page, keyword);
               },
               error: function () {
                   $('#zoneTableBody').html('<tr><td colspan="6">Error fetching zones</td></tr>');
               }
           });
       }

       // [NOTE: Sửa tại đây] Định nghĩa hàm loadDefaultZones ngoài phạm vi $(document).ready()
       function loadDefaultZones() {
           const currentIndex = ${index}; // Lấy index từ server
           searchZones('', currentIndex, currentSortBy, currentSortOrder);
       }

       // Hàm cập nhật bảng
       function updateTable(zones, endPage, currentIndex, keyword) {
           const tbody = $('#zoneTableBody');
           tbody.empty();

           if (zones.length === 0) {
               tbody.append('<tr><td colspan="6">No zones found</td></tr>');
           } else {
               zones.forEach(function (zone) {
                   tbody.append(
                           '<tr>' +
                           '<td>' + zone.id + '</td>' +
                           '<td style="text-align: left;">' + zone.name + '</td>' +
                           '<td>' + (zone.storeId ? zone.storeId.id : 'N/A') + '</td>' +
                           '<td>' + zone.createdBy + '</td>' +
                           '<td>' + zone.status + '</td>' +
                           '<td>' +
                           '<a href="<%= request.getContextPath() %>/zones?service=getZoneById&zone_id=' + zone.id + '" class="btn btn-outline-primary">View</a> ' +
                           '<a href="<%= request.getContextPath() %>/zones?service=editZone&zone_id=' + zone.id + '" class="btn btn-outline-primary">Edit</a> ' +
                           '<a href="<%= request.getContextPath() %>/zones?service=deleteZone&zone_id=' + zone.id + '" ' +
                           'class="btn btn-danger" onclick="return confirm(\'Are you sure you want to delete this zone?\');">Ban</a>' +
                           '</td>' +
                           '</tr>'
                           );
               });
           }

           updatePagination(endPage, currentIndex, keyword);
       }

       // Hàm cập nhật phân trang
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

           // [NOTE: Sửa tại đây] Gắn sự kiện click cho các liên kết phân trang bằng jQuery
           $('.page-nav').on('click', function (e) {
               e.preventDefault();
               const page = $(this).data('page');
               searchZones(keyword, page, currentSortBy, currentSortOrder);
           });
       }

       // Hàm cập nhật biểu tượng sắp xếp
       function updateSortIcons() {
           $('.sortable').each(function () {
               const sortBy = $(this).data('sort');
               const icon = $(this).find('i');
               if (sortBy === currentSortBy) {
                   icon.removeClass('fa-sort-up fa-sort-down');
                   icon.addClass(currentSortOrder === 'ASC' ? 'fa-sort-up' : 'fa-sort-down');
               } else {
                   icon.removeClass('fa-sort-up fa-sort-down');
                   icon.addClass('fa-sort-down'); // Mặc định là mũi tên xuống
               }
           });
       }

       $(document).ready(function () {

           // Xử lý tìm kiếm tự động bằng AJAX
           $('#searchInput').on('keyup', function () {
               clearTimeout(timeout);
               const keyword = $(this).val().trim();

               timeout = setTimeout(function () {
                   searchZones(keyword, 1, currentSortBy, currentSortOrder); // Bắt đầu từ trang 1 khi tìm kiếm
               }, 300);
           });

           // Xử lý sắp xếp khi nhấp vào cột
           $('.sortable').on('click', function () {
               const sortBy = $(this).data('sort');
               const keyword = $('#searchInput').val().trim();

               // Đảo ngược sortOrder nếu nhấp lại vào cùng cột
               if (currentSortBy === sortBy) {
                   currentSortOrder = currentSortOrder === 'ASC' ? 'DESC' : 'ASC';
               } else {
                   currentSortBy = sortBy;
                   currentSortOrder = 'ASC'; // Mặc định ASC khi chọn cột mới
               }

               // Cập nhật biểu tượng sắp xếp
               updateSortIcons();

               // Tải lại dữ liệu với sắp xếp mới
               searchZones(keyword, 1, currentSortBy, currentSortOrder);
           });



           // Xử lý nút Clear
           $('#clearBtn').on('click', function () {
               $('#searchInput').val(''); // Xóa nội dung ô tìm kiếm
               loadDefaultZones(); // Gọi loadDefaultZones để làm mới toàn bộ danh sách và phân trang
           });

           // Khởi tạo biểu tượng sắp xếp ban đầu
           updateSortIcons();

           // [NOTE: Sửa tại đây] Tải danh sách mặc định khi trang load
           loadDefaultZones();
       });
        </script>
    </body>
</html>