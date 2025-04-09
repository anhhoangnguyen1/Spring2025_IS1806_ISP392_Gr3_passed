<%-- 
    Document   : addProduct
    Created on : Jan 27, 2025, 2:29:26 PM
    Author     : phamh
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>

        <title>Product</title>
    </head>
    <body>

        <div id="addProductModal" class="modal fade show">
            <div class="modal-dialog modal-xl">
                <div class="modal-content">
                    <form action="${pageContext.request.contextPath}/Products" method="POST" enctype="multipart/form-data">
                        <input type="hidden" name="service" value="addProduct"/>

                        <!-- Tiêu đề Modal -->
                        <div class="modal-header">
                            <h4 class="modal-title">Add Product</h4>
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        </div>

                        <!-- Nội dung Modal -->
                        <div class="modal-body">					
                            <div class="form-group">
                                <label>Product Name</label>
                                <input type="text" class="form-control" name="name" required>
                            </div>

                            <div class="form-group">
                                <label>Product Image</label>
                                <!-- Ảnh hiện tại -->
                                <img id="productImagePreview" 
                                     
                                     alt="Current Product Image" 
                                     style="width: 150px; height: 150px; object-fit: cover; margin-bottom: 10px; display: block;" />
                                <input type="file" name="image" accept="image/*" onchange="previewImage(event)">
                            </div>

                            <div class="form-row">
                                <div class="form-group col-md-6">
                                    <label>Price</label>
                                    <input type="text" oninput="formatNumber(event)" onblur="cleanInputBeforeSubmit(event)" class="form-control" name="price" required />
                                </div>

                                <div class="form-group col-md-6">
                                    <label>Zone name</label>
                                    <select class="form-control selectpicker" name="zoneName" multiple data-live-search="true">
                                        <c:forEach var="zone" items="${zoneName}">
                                            <option value="${zone}">${zone}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>




                            <div class="form-group">
                                <label>Description</label>
                                <textarea class="form-control" name="description" rows="3"></textarea>
                            </div>

                        </div>

                        <!-- Chân Modal -->
                        <div class="modal-footer">
                            <button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
                            <input type="submit" class="btn btn-primary" value="Add">
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <script>
            function previewImage(event) {
                const file = event.target.files[0]; // Lấy file đầu tiên
                const reader = new FileReader(); // Đọc nội dung file

                if (file && file.type.startsWith("image/")) {
                    reader.onload = function (e) {
                        const imgPreview = document.getElementById('productImagePreview');
                        imgPreview.src = e.target.result;
                        imgPreview.style.display = "block"; // Hiện ảnh
                    };
                    reader.readAsDataURL(file); // Đọc file dạng base64
                } else {
                    alert("Vui lòng chọn một tệp hình ảnh hợp lệ.");
                }
            }
        </script>
    </body>
</html>
