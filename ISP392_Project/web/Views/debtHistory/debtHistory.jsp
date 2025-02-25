<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Debt List</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
    </head>

    <body>
        <div class="container">
            <h2 class="text-center">Debt List</h2>
            <button type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#DebtModal">
                <i class="fas fa-plus"></i> Add Debt
            </button>
            <div class="table-container">
                <table class="table-bordered">
                    <thead>
                        <tr>
                            <th class="resizable">ID</th>
                            <th class="resizable">Amount</th>
                            <th>Images</th>
                            <th class="resizable">Description</th>
                            <th class="resizable">Created At</th>
                            <th class="resizable">Updated At</th>
                            <th class="resizable">Created By</th>
                            <th class="resizable">Status</th>
                            <th class="sticky-col1">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="totalAmount" value="0" />
                        <c:forEach var="debt" items="${list}">
                            <tr>
                                <td>${debt.id}</td>
                                <td class="${debt.type == '-' ? 'text-danger' : ''}">
                                    <fmt:formatNumber value="${debt.amount}" pattern="###,##0.00"/>
                                </td>


                                <td>
                                    <img src="images/${debt.image}" class="myImg" style="width: 100px; height: 100px; object-fit: cover; cursor: pointer;" alt="Debt evidence"/>
                                </td>
                                <td>${debt.description}</td>
                                <td>${debt.createdAt}</td>
                                <td>${debt.updatedAt}</td>
                                <td>${debt.createdBy}</td>
                                <td>${debt.status}</td>
                                <td class="sticky-col1">

                                </td>
                            </tr>
                            <c:set var="totalAmount" value="${totalAmount + (debt.type == '-' ? -debt.amount : debt.amount)}" />
                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="7" class="text-right "></td>
                            <td class="sticky-col1"><strong>Total Amount: <fmt:formatNumber value="${totalAmount}" /></strong></td>
                        </tr>
                    </tfoot>
                </table>
            </div>
            <div id="DebtModal" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
                <div class="modal-dialog modal-lg" role="document">
                    <div class="modal-content">
                        <form action="${pageContext.request.contextPath}/Debts" method="POST" enctype="multipart/form-data">
                            <input type="hidden" name="service" value="updateDebt" />

                            <input type="hidden" name="createdBy" value="${sessionScope.username}" />
                            <c:forEach var="debt" items="${list}">
                                <input type="hidden" name="customer_id" value="${debt.customer_id}" />
                                <input type="hidden" name="id" value="${debt.debt_note_id}" />
                            </c:forEach>   
                            <div class="modal-header">
                                <h5 class="modal-title" id="DebtModalLabel">Add Debt</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>



                            <div class="modal-body">
                                <div class="form-group">
                                    <label>Debt Type</label>
                                    <select class="form-control" name="type"  required>
                                        <option value="+">+</option>
                                        <option value="-">-</option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Amount</label>
                                    <input type="text" oninput="formatNumber(event)" onblur="cleanInputBeforeSubmit(event)" class="form-control" name="amount"  required />
                                </div>

                                <div class="form-group">
                                    <label>Image</label>
                                    <input type="file" class="form-control-file" name="image"  />
                                </div>

                                <div class="form-group">
                                    <label>Created at</label>
                                    <input type="datetime-local" class="form-control" name="created_at"  required />
                                </div>

                                <div class="form-group">
                                    <label>Description</label>
                                    <textarea class="form-control" name="description"  rows="3"></textarea>
                                </div>

                                <div class="form-group">
                                    <label>Status</label>
                                    <select class="form-control" name="status" id="status_${debt.id}" required>
                                        <option value="Pending">Pending</option>
                                        <option value="Paid">Paid</option>
                                        <option value="Overdue">Overdue</option>
                                    </select>
                                </div>
                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-primary">Add Debt</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>                            
    </body>
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/css/script.js"></script>
</html>
