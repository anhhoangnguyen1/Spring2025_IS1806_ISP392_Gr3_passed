<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Danh sách kiểm kho</title>
        <style>table {
                width: 100%;
                border-collapse: collapse;
            }
            th, td {
                border: 1px solid #ddd;
                padding: 8px;
                text-align: left;
            }
            th {
                background-color: #f2f2f2;
            }
            tr:nth-child(even) {
                background-color: #f9f9f9;
            }
            a {
                text-decoration: none;
                color: blue;
            }
            a:hover {
                text-decoration: underline;
            }</style>
    </head>
    <body>
        <h1>Kiểm kho</h1>
        <div>
            <form action="stockcheck" method="get">
                <input type="text" name="search" placeholder="Tìm kiếm theo ghi chú (F3)" value="${param.search}">
                <button type="submit">Tìm kiếm</button>
            </form>
            <a href="stockcheck?action=create">Thêm sản phẩm từ file excel</a>
        </div>

        <table border="1">
            <thead>
                <tr>
                    <th><a href="stockcheck?sortBy=stockCheckId&order=${param.order == 'ASC' ? 'DESC' : 'ASC'}">STT</a></th>
                    <th><a href="stockcheck?sortBy=zoneId&order=${param.order == 'ASC' ? 'DESC' : 'ASC'}">Mã khu vực</a></th>
                    <th><a href="stockcheck?sortBy=productId&order=${param.order == 'ASC' ? 'DESC' : 'ASC'}">Mã hàng</a></th>
                    <th><a href="stockcheck?sortBy=checkedDate&order=${param.order == 'ASC' ? 'DESC' : 'ASC'}">Ngày kiểm</a></th>
                    <th><a href="stockcheck?sortBy=actualQuantity&order=${param.order == 'ASC' ? 'DESC' : 'ASC'}">SL thực tế</a></th>
                    <th><a href="stockcheck?sortBy=recordedQuantity&order=${param.order == 'ASC' ? 'DESC' : 'ASC'}">SL ghi nhận</a></th>
                    <th><a href="stockcheck?sortBy=discrepancy&order=${param.order == 'ASC' ? 'DESC' : 'ASC'}">Chênh lệch</a></th>
                    <th><a href="stockcheck?sortBy=notes&order=${param.order == 'ASC' ? 'DESC' : 'ASC'}">Ghi chú</a></th>
                    <th>Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="stockCheck" items="${stockChecks}" varStatus="loop">
                    <tr>
                        <td>${loop.count}</td>
                        <td>${stockCheck.zoneId}</td>
                        <td>${stockCheck.productId}</td>
                        <td>${stockCheck.checkedDate}</td>
                        <td>${stockCheck.actualQuantity}</td>
                        <td>${stockCheck.recordedQuantity}</td>
                        <td>${stockCheck.discrepancy}</td>
                        <td>${stockCheck.notes}</td>
                        <td>
                            <a href="stockcheck?action=edit&id=${stockCheck.stockCheckId}">Sửa</a>
                            <a href="stockcheck?action=delete&id=${stockCheck.stockCheckId}" onclick="return confirm('Bạn có chắc muốn xóa?')">Xóa</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>