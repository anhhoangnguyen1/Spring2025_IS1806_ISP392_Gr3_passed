<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <title>Stock Check - ${zone.name}</title>
</head>
<body>
    <div class="container mt-4">
        <h1>Stock Check for Zone: ${zone.name}</h1>
        <form action="zones" method="post">
            <input type="hidden" name="service" value="submitStockCheck">
            <input type="hidden" name="zone_id" value="${zone.id}">
            <input type="hidden" name="product_id" value="${zone.productId != null ? zone.productId.productId : ''}">
            <div class="form-group">
                <label>Product Name</label>
                <input type="text" class="form-control" value="${zone.productId != null ? zone.productId.name : 'No product assigned'}" readonly>
            </div>
            <div class="form-group">
                <label>System Quantity</label>
                <input type="number" class="form-control" name="system_quantity" value="${systemQuantity}" readonly>
            </div>
            <div class="form-group">
                <label>Actual Quantity</label>
                <input type="number" class="form-control" name="actual_quantity" required>
            </div>
            <div class="form-group">
                <label>Note (Optional)</label>
                <textarea class="form-control" name="note"></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Submit Stock Check</button>
            <a href="zones?service=zones" class="btn btn-secondary">Cancel</a>
        </form>
    </div>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
</body>
</html>