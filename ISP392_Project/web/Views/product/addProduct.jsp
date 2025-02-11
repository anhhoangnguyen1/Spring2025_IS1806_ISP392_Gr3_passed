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

        <title>JSP Page</title>
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
                                <label>Image</label>
                                    <input  type="file" name="image" required>
                            </div>
                            <div class="form-row">
                                <div class="form-group col-md-3">
                                    <label>Price</label>
                                    <input type="number" step="0.01" class="form-control" name="price" required>
                                </div>
                                <div class="form-group col-md-3">
                                    <label>Quantity (kg)</label>
                                    <input type="number" class="form-control" name="quantity" required>
                                </div>
                            </div>

                            <div class="form-row">
                                <label>Zone_id</label>
                                <input type="text" class="form-control" name="zone_id" required>
                            </div>

                            <div class="form-group">
                                <label>Description</label>
                                <textarea class="form-control" name="description" rows="3"></textarea>
                            </div>

                            <div class="form-group">
                                <label>Status</label>
                                <select class="form-control" name="status" required>
                                    <option value="Available">Available</option>
                                    <option value="Out of Stock">Out of Stock</option>
                                </select>
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
