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
            <c:if test="${not empty sessionScope.Notification}">
                <div class="alert alert-warning alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert">
                        &times;
                    </button>
                    <strong>${sessionScope.Notification}</strong>
                </div>
                <c:remove var="Notification" scope="session" />
            </c:if>
            <div class="container">
                <div class="row">
                    <div class="col-md-4">
                        <h6 class="text-muted">Name:</h6>
                        <p class="fw-bold">${name}</p>
                    </div>
                    <div class="col-md-4">
                        <h6 class="text-muted">Address:</h6>
                        <p class="fw-bold">${address}</p>
                    </div>
                    <div class="col-md-4">
                        <h6 class="text-muted">Phone:</h6>
                        <p class="fw-bold">${phone}</p>
                    </div>
                </div>
            </div>


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
                        <c:forEach var="debt" items="${list}">
                            <tr>
                                <td>${debt.id}</td>
                                <td class="amount ${debt.type == '-' ? 'text-danger' : ''}">
                                    <fmt:formatNumber value="${debt.amount}" pattern="###,##0"/>
                                </td>

                                <td>
                                    <img src="images/${debt.image}" class="myImg" style="width: 100px; height: 100px; object-fit: cover; cursor: pointer;" alt="Debt evidence"/>
                                </td>
                                <td style="white-space: normal; word-wrap: break-word; max-width: 200px;">
                                    ${debt.description}
                                </td>
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
                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="7"></td>
                            <td class="sticky-col1 text-right font-weight-bold" id="totalAmountCell">
                                Total Amount: <span id="totalAmount">0</span>
                            </td>
                        </tr>
                    </tfoot>
                </table>
            </div>

            <div id="DebtModal" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
                <div class="modal-dialog modal-lg" role="document">
                    <div class="modal-content">
                        <form action="${pageContext.request.contextPath}/Debts" method="POST" enctype="multipart/form-data" onsubmit="adjustAmountBeforeSubmit(event)">
                            <input type="hidden" name="service" value="updateDebt" />
                            <input type="hidden" name="createdBy" value="${sessionScope.username}" />

                            <c:forEach var="debt" items="${list}">
                                <input type="hidden" name="customer_id" value="${debt.customer_id}" />
                                <input type="hidden" name="id" value="${debt.debt_note_id}" />
                            </c:forEach>
                                <input type="hidden" name="name" value="${name}" />
                                <input type="hidden" name="phone" value="${phone}" />
                                <input type="hidden" name="address" value="${address}" />
                            <div class="modal-header">
                                <h5 class="modal-title" id="DebtModalLabel">Add Debt</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">×</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <div class="form-group">
                                    <label>Debt Type</label>
                                    <select class="form-control" name="type" id="debtType" required>
                                        <option value="+">+</option>
                                        <option value="-">-</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>Amount</label>
                                    <input type="text" oninput="formatNumber(event); this.value = this.value.replace('-', '');" onblur="cleanInputBeforeSubmit(event)" class="form-control" name="amount" id="debtAmount" required />
                                </div>
                                <div class="form-group">
                                    <label>Image</label>
                                    <input type="file" class="form-control-file" name="image" />
                                </div>
                                <div class="form-group">
                                    <label>Created at</label>
                                    <input type="datetime-local" class="form-control" name="created_at" required />
                                </div>
                                <div class="form-group">
                                    <label>Description</label>
                                    <textarea class="form-control" name="description" rows="3"></textarea>
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
                                <button type="submit" class="btn btn-primary">Add Debt</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div> 
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
        <div id="myModal" class="modalImage">
            <span class="close">&times;</span>
            <img class="modalImage-content" id="img01">
            <div id="caption"></div>
        </div>
    </body>
    <script>

        document.addEventListener("DOMContentLoaded", function () {
            let totalAmount = 0;

            // Lấy tất cả các ô có class 'amount'
            document.querySelectorAll(".amount").forEach(function (cell) {
                let amountText = cell.innerText.replace(/,/g, "").trim(); // Xóa dấu phẩy, khoảng trắng

                // Kiểm tra nếu có dấu '-' thì chuyển thành số âm
                let amount = amountText.startsWith("-") ? -parseFloat(amountText.substring(1)) : parseFloat(amountText);

                if (!isNaN(amount)) {
                    totalAmount += amount;
                }
            });

            // Hiển thị tổng số tiền, nếu là số nguyên thì không hiển thị .00
            let formattedAmount = totalAmount % 1 === 0 ? totalAmount.toLocaleString("en-US") : totalAmount.toLocaleString("en-US", {minimumFractionDigits: 2});
            document.getElementById("totalAmount").innerText = formattedAmount;
        });
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
    </script>

    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/css/script.js"></script>

</html>
