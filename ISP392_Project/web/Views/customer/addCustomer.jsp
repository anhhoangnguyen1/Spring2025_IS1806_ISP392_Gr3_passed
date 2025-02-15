<%-- 
    Document   : addCustomer
    Created on : Feb 14, 2025, 5:02:47 PM
    Author     : THC
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Add Customer</title>
</head>
<body>
    <div id="addCustomerModal" class="modal fade show">
        <div class="modal-dialog modal-xl">
            <div class="modal-content">
                <form action="${pageContext.request.contextPath}/Customers" method="POST">
                    <input type="hidden" name="service" value="addCustomer"/>

                    <div class="modal-header">
                        <h4 class="modal-title">Add Customer</h4>
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    </div>

                    <div class="modal-body">
                        <div class="form-group">
                            <label>Name</label>
                            <input type="text" class="form-control" name="name" required>
                        </div>
                        <div class="form-group">
                            <label>Phone</label>
                            <input type="text" class="form-control" name="phone" required>
                        </div>
                        <div class="form-group">
                            <label>Address</label>
                            <input type="text" class="form-control" name="address" required>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label>Balance</label>
                                <input type="number" step="0.01" class="form-control" name="balance" required>
                            </div>
                            <div class="form-group col-md-6">
                                <label>Created By</label>
                                <input type="text" class="form-control" name="createdBy" required>
                            </div>
                        </div>
                       
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
                        <input type="submit" class="btn btn-primary" value="Add">
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>