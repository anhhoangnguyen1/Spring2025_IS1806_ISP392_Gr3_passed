<%-- 
    Document   : editCustomer
    Created on : Feb 14, 2025, 5:03:29 PM
    Author     : THC
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Edit Customer</title>
    </head>
    <body>
        <c:forEach var="customer" items="${list}">
            <div id="editCustomerModal${customer.id}" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="editCustomerModalLabel${customer.id}" aria-hidden="true">
                <div class="modal-dialog modal-xl" role="document">
                    <div class="modal-content">
                        <form action="${pageContext.request.contextPath}/Customers" method="POST">
                            <input type="hidden" name="service" value="editCustomer" />
                            <input type="hidden" name="customer_id" value="${customer.id}" />

                            <div class="modal-header">
                                <h4 class="modal-title" id="editCustomerModalLabel${customer.id}">Edit Customer: ${customer.name}</h4>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>

                            <div class="modal-body">
                                <div class="form-group">
                                    <label>Name</label>
                                    <input type="text" class="form-control" name="name" value="${customer.name}" readonly>
                                </div>
                                <div class="form-group">
                                    <label>Phone</label>
                                    <input type="text" class="form-control" name="phone" value="${customer.phone}" required>
                                </div>
                                <div class="form-group">
                                    <label>Address</label>
                                    <input type="text" class="form-control" name="address" value="${customer.address}" required>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label>Balance</label>
                                        <input type="number" step="0.01" class="form-control" name="balance" value="${customer.balance}" required>
                                    </div>
                                    <div class="form-group">
                                        <label>Updated By</label>
                                        <input type="text" class="form-control" name="updatedBy" value="${username}" readonly>
                                    </div>


                                </div>

                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-primary">Save Changes</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </c:forEach>
    </body>
</html>