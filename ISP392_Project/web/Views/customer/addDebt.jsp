
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <body>
        <c:forEach var="customer" items="${list}">
       <div id="DebtModal${customer.id}" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl" role="document">
        <div class="modal-content">
            
            <form action="${pageContext.request.contextPath}/Debts" method="POST" enctype="multipart/form-data">
                <input type="hidden" name="service" value="addDebt" />
                
                <div class="modal-header">
                    <h4 class="modal-title">Add Debt</h4>
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                </div>

                <div class="modal-body">
                    <!-- Debt Type Field -->
                    <div class="form-group">
                        <label>Debt Type</label>
                        <select class="form-control" name="type">
                            <option value="+">Debt</option>
                            <option value="-">Repay</option>
                        </select>
                    </div>

                    <!-- Amount Field -->
                    <div class="form-group">
                        <label>Amount</label>
                        <input type="number" step="0.01" class="form-control" name="amount" required />
                    </div>

                    <!-- Image Upload -->
                    <div class="form-group">
                        <label>Image</label>
                        <input type="file" name="image">
                    </div>
                    
                    <!-- Customer ID -->
                    <input type="hidden" name="customer_id" value="${customer.id}" />
                    <div class="form-group">
                        <label>Created by</label>
                        <input type="text" name="createdBy" value="${sessionScope.username}" class="form-control" required />
                    </div>

                    <!-- Description -->
                    <div class="form-group">
                        <label>Description</label>
                        <textarea class="form-control" name="description" rows="3"></textarea>
                    </div>

                    <!-- Created By (Hidden) -->
                    <input type="hidden" name="createdBy" value="owner" />

                    <!-- Status -->
                    <div class="form-group">
                        <label>Status</label>
                        <select class="form-control" name="status" required>
                            <option value="Pending">Pending</option>
                            <option value="Paid">Paid</option>
                            <option value="Overdue">Overdue</option>
                        </select>
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
                    <input type="submit" class="btn btn-primary" value="Add Debt" />
                </div>
            </form>
        </div>
    </div>
</div>
</c:forEach>
    </body>
</html>
