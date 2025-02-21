<%-- 
    Document   : addCustomer
    Created on : Feb 14, 2025, 5:02:47 PM
    Author     : THC
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Add Customer</title>
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-4">
            <h2>Add Customer</h2>


            <form action="${pageContext.request.contextPath}/Customers" method="POST" onsubmit="return confirmAdd()">
                <input type="hidden" name="service" value="addCustomer"/>

                <div class="form-group">
                    <label for="name">Name</label>
                    <input type="text" class="form-control" name="name" required>
                </div>

                <div class="form-group">
                    <label for="phone">Phone</label>
                    <input type="text" class="form-control" name="phone" required>
                </div>

                <div class="form-group">
                    <label for="address">Address</label>
                    <input type="text" class="form-control" name="address" required>
                </div>

                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label for="balance">Balance</label>
                        <input type="number" step="0.01" class="form-control" name="balance" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="createdBy">Created By</label>
                        <input type="text" class="form-control" name="createdBy" value="${userName}" readonly>
                    </div>
                </div>


                <div class="form-group">
                    <button type="submit" class="btn btn-primary">Add Customer</button>        
                    <a href="${pageContext.request.contextPath}/Customers?service=customers" class="btn btn-secondary">
                        Back to Customers List
                    </a>
                </div>    
            </form>
        </div>


        <script>
            function confirmAdd() {
                return confirm("Are you sure you want to add this customer?");
            }
        </script>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>

