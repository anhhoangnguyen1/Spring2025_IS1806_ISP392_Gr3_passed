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
        <style>
            .error-message {
                color: red;
                font-size: 0.875em;
                margin-top: 0.25em;
            }
        </style>
    </head>
    <body>
        <div id="addProductModal" class="modal fade show">
            <div class="modal-dialog modal-xl">
                <div class="modal-content">
                    <form action="${pageContext.request.contextPath}/Products" method="POST" enctype="multipart/form-data" onsubmit="return validateAddProductForm(this);">
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
                                <img id="productImagePreview" 
                                     alt="Current Product Image" 
                                     style="width: 150px; height: 150px; object-fit: cover; margin-bottom: 10px; display: none;" />
                                <input type="file" name="image" accept="image/*" onchange="previewImage(event)">
                                <div class="error-message" id="imageError"></div>
                            </div>

                            <div class="form-row">
                                <div class="form-group col-md-6">
                                    <label>Price</label>
                                    <input type="number" step="0.01" min="0" class="form-control" name="price" required>
                                    <div class="error-message" id="priceError"></div>
                                </div>

                                <div class="form-group col-md-6">
                                    <label>Zone name</label>
                                    <select class="form-control selectpicker" name="zoneName" multiple data-live-search="true">
                                        <c:forEach var="zone" items="${zoneName}">
                                            <option value="${zone}">${zone}</option>
                                        </c:forEach>
                                    </select>
                                    <div class="error-message" id="zoneError"></div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label>Description</label>
                                <textarea class="form-control" name="description" rows="3" minlength="1"></textarea>
                                <div class="error-message" id="descriptionError"></div>
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
    </body>
</html>
