<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <body>
            <div id="addDebtModal" class="modal fade show" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
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
                                    <option value="+">Debt</option> <!-- + người ta nợ mik -->
                                    <option value="-">Repay</option><!-- - Mik nợ người ta -->
                                </select>
                            </div>

                            <!-- Amount Field -->
                            <div class="form-group">
                                <label>Amount</label>
                                <input type="text" oninput="formatNumber(event)" onblur="cleanInputBeforeSubmit(event)" class="form-control" name="amount" required />
                            </div>

                            <div class="form-group">
                                <label>Image</label>
                                <input  type="file" name="image">
                            </div>
                            
                              <div class="form-group">
                                <label>Name</label>
                                <input type="text" name="debtorName" class="form-control" value="" required/>
                            </div>
                              <div class="form-group">
                                <label>Address</label>
                                <input type="text" name="debtorAddress" class="form-control" value="" required/>
                            </div>
                              <div class="form-group">
                                <label>Create at</label>
                                <input type="datetime-local" name="created_at" class="form-control" value="" required/>
                            </div>
                            
                              <div class="form-group">
                                <label>Phone</label>
                                <input type="text" name="debtorPhone"  class="form-control" value="" required/>
                            </div>

                            
                            <div class="form-group">
                                <label>Description</label>
                                <textarea class="form-control" name="description" rows="3"></textarea>
                            </div>
                            
                            <div class="form-group">
                                <input type="hidden" name="createdBy" value="${sessionScope.username}" />
                            </div>
                            
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
    </body>
</html>
