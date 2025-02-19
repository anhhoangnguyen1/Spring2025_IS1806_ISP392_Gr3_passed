<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Debt Detail</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    </head>
    <body>
        <c:forEach var="debt" items="${list}">
            <div class="container mt-4">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h3 class="mb-0">Debt Details - <strong>${debt.getId()}</strong></h3>
                    </div>
                    <div class="card-body">
                        <div class="form-group">
                            <label><strong>Amount:</strong></label>
                            <input type="text" class="form-control" value="<fmt:formatNumber value='${debt.amount}' />" readonly>
                        </div>
                        <div class="form-group">
                            <label><strong>Type:</strong></label>
                            <input type="text" class="form-control" value="${debt.getType()}" readonly>
                        </div>
                        <div class="form-group">
                            <label><strong>Created At:</strong></label>
                            <input type="text" class="form-control" value="${debt.getCreatedAt()}" readonly>
                        </div>
                        <div class="form-group">
                            <label><strong>Description:</strong></label>
                            <textarea class="form-control" rows="3" readonly>${debt.getDescription()}</textarea>
                        </div>
                        <div class="form-group">
                            <label><strong>Status:</strong></label>
                            <input type="text" class="form-control" value="${debt.getStatus()}" readonly>
                        </div>
                        <div class="form-group">
                            <label><strong>Evidence:</strong></label>
                            <img src="images/${debt.image}" class="myImg" style="width: 100px; height: 100px; object-fit: cover; cursor: pointer;" alt="Debt evidence"/>
                        </div>
                    </div>
                    <div class="text-right">
                        <a href="Debts" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Back to List
                        </a>
                    </div>
                </div>
            </div>
        </c:forEach>
        <div id="myModal" class="modalImage">
            <span class="close">&times;</span>
            <img class="modalImage-content" id="img01">
            <div id="caption"></div>
        </div>
        <script>   var modal = document.getElementById("myModal");

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
            });</script>
        <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/css/script.js"></script>
    </body>
</html>
