<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Add Zone</title>
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
    </head>
    <body>
        <div class="container mt-4">
            <h2>Add Zone</h2>
            <c:if test="${not empty requestScope.nameError}">
                <div class="alert alert-danger">${requestScope.nameError}</div>
            </c:if>
            <form action="${pageContext.request.contextPath}/zones" method="POST" onsubmit="return confirmAdd()">
                <input type="hidden" name="service" value="addZone"/>
                <div class="form-group">
                    <label for="name">Name</label>
                    <input type="text" class="form-control" name="name" value="${requestScope.name}" required>
                </div>
                <div class="form-group">
                    <label for="store_id">Store ID</label>
                    <input type="number" class="form-control" name="store_id" value="${requestScope.store_id}">
                </div>
                <div class="form-group">
                    <label for="createdBy">Created By</label>
                    <input type="text" class="form-control" name="createdBy" value="${userName}" readonly>
                </div>
                <div class="form-group">
                    <label for="status">Status</label>
                    <select class="form-control" name="status" required>
                        <option value="Active" ${requestScope.status == 'Active' ? 'selected' : ''}>Active</option>
                        <option value="Inactive" ${requestScope.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                    </select>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">Add Zone</button>
                    <a href="${pageContext.request.contextPath}/zones?service=zones" class="btn btn-secondary">Back to Zones List</a>
                </div>
            </form>
        </div>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <script>
                function confirmAdd() {
                    return confirm("Are you sure you want to add this zone?");
                }
        </script>
    </body>
</html>