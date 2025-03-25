<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Chỉnh sửa kiểm kho</title>
    </head>
    <body>
        <h1>Chỉnh sửa kiểm kho</h1>
        <form action="stockcheck" method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="stockCheckId" value="${stockCheck.stockCheckId}">
            <label>Khu vực:</label>
            <input type="text" value="${stockCheck.zoneId}" disabled><br>
            <label>Mã sản phẩm:</label>
            <input type="text" value="${stockCheck.productId}" disabled><br>
            <label>Ngày kiểm:</label>
            <input type="text" value="${stockCheck.checkedDate}" disabled><br>
            <label>Số lượng thực tế:</label>
            <input type="text" value="${stockCheck.actualQuantity}" disabled><br>
            <label>Số lượng ghi nhận:</label>
            <input type="text" value="${stockCheck.recordedQuantity}" disabled><br>
            <label>Chênh lệch:</label>
            <input type="text" value="${stockCheck.discrepancy}" disabled><br>
            <label>Ghi chú:</label>
            <input type="text" name="notes" value="${stockCheck.notes}"><br>
            <button type="submit">Cập nhật</button>
        </form>
    </body>
</html>