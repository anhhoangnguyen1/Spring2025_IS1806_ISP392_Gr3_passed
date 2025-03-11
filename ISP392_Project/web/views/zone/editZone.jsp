<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Edit Zone</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
    </head>
    <body>
        <div class="container mt-4">
            <h2>Edit Zone</h2>
            <c:if test="${not empty requestScope.nameError}">
                <div class="alert alert-danger">${requestScope.nameError}</div>
            </c:if>
            <form id="zoneForm" action="${pageContext.request.contextPath}/zones" method="POST" onsubmit="confirmSave(event)">
                <input type="hidden" name="service" value="editZone" />
                <input type="hidden" name="zone_id" value="${zone.id}" />
                <input type="hidden" name="sortOrder" value="${param.sortOrder}" />
                <div class="form-group">
                    <label for="name">Name</label>
                    <input type="text" class="form-control" name="name" value="${zone.name}" required>
                </div>
                <div class="form-group">
                    <label for="store_id">Store ID</label>
                    <input type="number" class="form-control" name="store_id" value="${zone.storeId != null ? zone.storeId.id : ''}">
                </div>
                <div class="form-group">
                    <label for="status">Status</label>
                    <select class="form-control" name="status" required>
                        <option value="Active" ${zone.status == 'Active' ? 'selected' : ''}>Active</option>
                        <option value="Inactive" ${zone.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                    </select>
                </div>
                <input type="hidden" name="updateBy" value="${userName}" />
                <div class="form-group">
                    <button type="submit" class="btn btn-primary" style="background-color: #007bff">Save Changes</button>
                    <a href="${pageContext.request.contextPath}/zones?service=zones&sortOrder=${param.sortOrder}" class="btn btn-secondary">Back to Zones List</a>
                </div>
            </form>
        </div>
        <div class="modal fade" id="confirmModal" tabindex="-1" aria-labelledby="confirmModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="confirmModalLabel">Confirm Changes</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        Are you sure to save the changes to this zone?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" style="background-color: #007bff" id="saveChangesBtn">Save</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/css/script.js"></script>
        <script>
            document.getElementById('saveChangesBtn').onclick = function () {
                document.getElementById('zoneForm').submit();
            }
            function confirmSave(event) {
                event.preventDefault();
                $('#confirmModal').modal('show');
            }
        </script>
    </body>
</html>