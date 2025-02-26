<%-- 
    Document   : detaildebt
    Created on : Jan 27, 2025, 9:28:37 PM
    Author     : phamh
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>JSP Page</title>
    </head>
    <body>

        <c:forEach var="customer" items="${list}">
            <div id="debtListModal${customer.id}" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="debtListLabel${customer.id}" aria-hidden="true">
                <div class="modal-dialog modal-xl" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4 class="modal-title">Debt List - record ${customer.debtNotes.size()}</h4>
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        </div>
                            <h3 class="font-weight-bold">${customer.name} - ${customer.phone}</h3>
                            
                        <button type="button" class="btn btn-outline-success ml-auto mr-5" data-toggle="modal" data-target="#DebtModal${customer.id}">
                            <i class="fas fa-plus"></i> Add
                        </button>

                        <div class="modal-body">
                            <div class="table-container">
                                <table class="table-bordered" id="myTable1-${customer.id}" >

                                    <thead>
                                        <tr>
                                            <th class="resizable" onclick="sortTable1(${customer.id}, 0)">ID</th>
                                            <th class="resizable" onclick="sortTable1(${customer.id}, 1)">Amount</th>
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


                                        <c:forEach var="debt" items="${customer.debtNotes}">
                                            <tr>
                                                <td>${debt.debt_note_id}</td>
                                                <td class="${debt.type == '-' ? 'text-danger' : ''}">
                                                    <fmt:formatNumber value="${debt.type == '-' ? -debt.amount : debt.amount}" pattern="###,##0.00"/>
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
                                                    <!-- Nút xem chi tiết nợ -->
                                                    <button type="button" class="btn btn-outline-primary openDebtModal" 
                                                            data-id="${debt.id}" 
                                                            data-amount="${debt.amount}" 
                                                            data-type="${debt.type}"
                                                            data-createdat="${debt.createdAt}"
                                                            data-description="${debt.description}" 
                                                            data-status="${debt.status}"
                                                            data-image="images/${debt.image}">
                                                        <i class="fas fa-info-circle"></i>
                                                    </button>

                                                  
                                                </td>
                                            </tr>

                                            <!-- Modal hiển thị chi tiết nợ -->


                                        </c:forEach>

                                        <c:forEach begin="1" end="${10 - customer.debtNotes.size()}" varStatus="loop">
                                            <tr>
                                                <td colspan="9" style="height: 40px;">&nbsp;</td> <!-- Dòng trống -->
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                                <h4>Total Amount: <fmt:formatNumber value="${customer.balance}"/></h4>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>

        </c:forEach>   
        <div class="modal fade" id="debtDetailModal" tabindex="-1" role="dialog" aria-labelledby="debtDetailLabel" aria-hidden="true">
            <div class="modal-dialog modal-xl" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Debt Details - <strong id="modalDebtId"></strong></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <label><strong>Amount:</strong></label>
                            <input id="modalDebtAmount" type="text" class="form-control" readonly>
                        </div>
                        <div class="form-group">
                            <label><strong>Type:</strong></label>
                            <input id="modalDebtType" type="text" class="form-control" readonly>
                        </div>
                        <div class="form-group">
                            <label><strong>Created At:</strong></label>
                            <input id="modalDebtCreatedAt" type="text" class="form-control" readonly>
                        </div>
                        <div class="form-group">
                            <label><strong>Description:</strong></label>
                            <input id="modalDebtDescription" type="text" class="form-control" readonly>
                        </div>
                        <div class="form-group">
                            <label><strong>Status:</strong></label>
                            <input id="modalDebtStatus" type="text" class="form-control" readonly>
                        </div>
                        <div class="form-group">
                            <label><strong>Evidence:</strong></label>
                            <img id="modalDebtImage" class="myImg" style="width: 100%; height: auto; object-fit: cover; border-radius: 8px;" alt="Debt evidence">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>


        <!-- Modal để hiển thị ảnh -->
        <div id="myModal" class="modalImage">
            <span class="close">&times;</span>
            <img class="modalImage-content" id="img01">
            <div id="caption"></div>
        </div>
        <script>
            function myFunction(customerId) {
                var input, filter, table, tr, td, i, txtValue;
                input = document.getElementById("myInput");
                filter = input.value.toUpperCase();

                // Tạo ID động dựa trên customerId
                table = document.getElementById(`myTable1-${customer.id}`);

                if (!table)
                    return; // Tránh lỗi nếu bảng không tồn tại

                tr = table.getElementsByTagName("tr");

                for (i = 0; i < tr.length; i++) {
                    td = tr[i].getElementsByTagName("td")[0];
                    if (td) {
                        txtValue = td.textContent || td.innerText;
                        if (txtValue.toUpperCase().indexOf(filter) > -1) {
                            tr[i].style.display = "";
                        } else {
                            tr[i].style.display = "none";
                        }
                    }
                }
            }
            document.addEventListener("DOMContentLoaded", function () {
                document.querySelectorAll(".openDebtModal").forEach(button => {
                    button.addEventListener("click", function () {
                        // Lấy dữ liệu từ data-attribute của nút được click
                        const debtId = this.getAttribute("data-id");
                        const amount = this.getAttribute("data-amount");
                        const type = this.getAttribute("data-type");
                        const createdAt = this.getAttribute("data-createdat");
                        const description = this.getAttribute("data-description");
                        const status = this.getAttribute("data-status");
                        const image = this.getAttribute("data-image");

                        // Gán dữ liệu vào input readonly
                        document.getElementById("modalDebtId").textContent = debtId;
                        document.getElementById("modalDebtAmount").value = amount;
                        document.getElementById("modalDebtType").value = type;
                        document.getElementById("modalDebtCreatedAt").value = createdAt;
                        document.getElementById("modalDebtDescription").value = description;
                        document.getElementById("modalDebtStatus").value = status;
                        document.getElementById("modalDebtImage").src = image;

                        // Hiển thị modal
                        $("#debtDetailModal").modal("show");
                    });
                });
            });

// Get the modal
            var modal = document.getElementById("myModal");

// Get all images with class "myImg"
            var imgs = document.getElementsByClassName("myImg");
            var modalImg = document.getElementById("img01");
            var captionText = document.getElementById("caption");

// Loop through all images and add event listener to each
            document.querySelectorAll(".myImg").forEach(img => {
                img.addEventListener("click", function () {
                    modal.style.display = "block";
                    modalImg.src = this.src;
                    captionText.innerHTML = this.alt;
                });
            });


// Get the <span> element that closes the modal
            document.querySelectorAll(".close").forEach(closeBtn => {
                closeBtn.addEventListener("click", function () {
                    modal.style.display = "none";
                });
            });


        </script>
        <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/css/script.js"></script>
    </body>
</html>
