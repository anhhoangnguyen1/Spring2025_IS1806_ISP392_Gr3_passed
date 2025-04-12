<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Edit Product</title>
        <style>
            .readonly-field {
                background-color: #e9ecef;
                pointer-events: none;
            }
        </style>
    </head>
    <body>

        <c:forEach var="product" items="${list}">
            <!-- Modal Edit Product -->
            <div id="editProductModal${product.getProductId()}" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="editProductModalLabel${product.getProductId()}" aria-hidden="true">
                <div class="modal-dialog modal-xl" role="document">
                    <div class="modal-content">
                        <form action="${pageContext.request.contextPath}/Products" method="POST" enctype="multipart/form-data" onsubmit="return validateForm(this);">
                            <input type="hidden" name="service" value="editProduct" />
                            <input type="hidden" name="product_id" value="${product.getProductId()}" />
                            <input type="hidden" name="original_name" value="${product.getName()}" />

                            <!-- Modal Header -->
                            <div class="modal-header">
                                <h4 class="modal-title" id="editProductModalLabel${product.getProductId()}">Edit Product: ${product.getName()}</h4>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>

                            <!-- Modal Body -->
                            <div class="modal-body">
                                <div class="form-group">
                                    <label>Product Name</label>
                                    <input type="text" class="form-control readonly-field" name="name" value="${product.getName()}" readonly data-original="${product.getName()}">
                                </div>
                                <div class="form-group">
                                    <label>Product Image</label>
                                    <img id="productImage${product.getProductId()}" src="/ISP392_Project/views/product/images/${product.getImage()}" alt="Current Product Image" style="width: 150px; height: 150px; object-fit: cover; margin-bottom: 10px;">
                                    <input type="hidden" name="current_image" value="${product.getImage()}" />
                                    <input type="file" class="form-control" name="image" accept="image/*" onchange="previewImage(event, ${product.getProductId()})">
                                </div>

                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label>Price</label>
                                        <input type="number" step="0.01" class="form-control" name="price" value="${product.getPrice()}" required>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Zone name</label>
                                        <select class="form-control selectpicker" name="zoneName" multiple data-live-search="true">
                                            <c:forEach var="zone" items="${zoneName}">
                                                <option value="${zone}" 
                                                        <c:if test="${product.zoneName != null && product.zoneName.contains(zone)}">selected</c:if>>${zone}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <input type="hidden" name="createdAt" value="${product.createdAt}">
                                <input type="hidden" name="status" value="${product.status}">
                                <input type="hidden" name="quantity" value="${product.quantity}">
                                <input type="hidden" name="index" value="${index}">
                                <div class="form-group">
                                    <label>Description</label>
                                    <textarea class="form-control" name="description" rows="4" >${product.getDescription()}</textarea>
                                </div>

                            </div>

                            <!-- Modal Footer -->
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
