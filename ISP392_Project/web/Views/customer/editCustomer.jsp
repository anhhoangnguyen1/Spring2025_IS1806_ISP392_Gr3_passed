<%-- 
    Document   : editCustomer
    Created on : Feb 14, 2025, 5:03:29 PM
    Author     : THC
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Edit Customer</title>
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-4">
            <h2>Edit Customer</h2>


            <form action="${pageContext.request.contextPath}/Customers" method="POST" onsubmit="return confirmSave()">
                <input type="hidden" name="service" value="editCustomer" />
                <input type="hidden" name="customer_id" value="${customer.id}" />

                <div class="form-group">
                    <label for="name">Name</label>
                    <input type="text" class="form-control" name="name" value="${customer.name}" required>
                </div>

                <div class="form-group">
                    <label for="phone">Phone</label>
                    <input type="text" class="form-control" name="phone" value="${customer.phone}" required>
                </div>

                <div class="form-group">
                    <label for="address">Address</label>
                    <input type="text" class="form-control" name="address" value="${customer.address}" required>
                </div>

                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label for="balance">Balance</label>
                        <input type="number" class="form-control" name="balance" value="${customer.balance}" readonly>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="updatedBy">Updated By</label>
                        <input type="text" class="form-control" name="updatedBy" value="${userName}" readonly>
                    </div>
                </div>


                <div class="form-group">
                    <button type="submit" class="btn btn-primary ">Save Changes</button>          
                    <a href="${pageContext.request.contextPath}/Customers?service=customers" class="btn btn-secondary">
                        Back to Customers List
                    </a>
                </div>           
            </form>
        </div>


        <script>
            function confirmSave() {
                return confirm("Are you sure you want to save the changes?");
            }
        </script>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
