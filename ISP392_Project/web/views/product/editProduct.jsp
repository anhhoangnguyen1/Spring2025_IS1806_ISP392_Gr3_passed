<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Edit Product</title>
    </head>
    <body>

        <c:forEach var="product" items="${list}">
            <!-- Modal Edit Product -->
            <div id="editProductModal${product.getProductId()}" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="editProductModalLabel${product.getProductId()}" aria-hidden="true">
                <div class="modal-dialog modal-xl" role="document">
                    <div class="modal-content">
                        <form action="${pageContext.request.contextPath}/Products" method="POST" enctype="multipart/form-data">
                            <input type="hidden" name="service" value="editProduct" />
                            <input type="hidden" name="product_id" value="${product.getProductId()}" />

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
                                    <input type="text" class="form-control" name="name" value="${product.getName()}" readonly="">
                                </div>
                                <div class="form-group">
                                    <label>Product Image</label>
                                    <img id="productImage${product.getProductId()}" src="/ISP392_Project/views/product/images/${product.getImage()}" alt="Current Product Image" style="width: 150px; height: 150px; object-fit: cover; margin-bottom: 10px;">
                                    <input type="hidden" name="current_image" value="${product.getImage()}" />
                                    <input type="file" class="form-control" name="image" accept="image/*" onchange="previewImage(event, ${product.getProductId()})">
                                </div>

                                <div class="form-row">
                                    <div class="form-group col-md-4">
                                        <label>Price</label>
                                        <input type="number" step="0.01" class="form-control" name="price" value="${product.getPrice()}" required>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Quantity (kg)</label>
                                        <input type="number" class="form-control" name="quantity" value="${product.getQuantity()}" required>
                                    </div>
  
                                </div>

                                <div class="form-group">
                                    <label>Description</label>
                                    <textarea class="form-control" name="description" rows="4" >${product.getDescription()}</textarea>
                                </div>
                                <div class="form-group">
                                    <label>Status</label>
                                    <select class="form-control" name="status" required>
                                        <option value="Available" ${product.getStatus() == 'Available' ? 'selected' : ''}>Available</option>
                                        <option value="Out of Stock" ${product.getStatus() == 'Out of Stock' ? 'selected' : ''}>Out of Stock</option>
                                    </select>
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

        <script>
            function previewImage(event, productId) {
                var file = event.target.files[0];  // Lấy tệp người dùng chọn
                var reader = new FileReader();     // Tạo đối tượng FileReader

                // Kiểm tra xem tệp có hợp lệ không
                if (file) {
                    reader.onload = function (e) {
                        // Cập nhật thẻ <img> với ảnh mới
                        console.log('Image selected: ', e.target.result); // Kiểm tra đầu ra
                        document.getElementById('productImage' + productId).src = e.target.result;
                    }
                    // Đọc tệp dưới dạng URL (Data URL)
                    reader.readAsDataURL(file);
                }
            }

        </script>
    </body>
</html>
