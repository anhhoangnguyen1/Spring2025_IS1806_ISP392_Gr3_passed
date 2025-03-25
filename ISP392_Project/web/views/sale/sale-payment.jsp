<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 3/11/2025
  Time: 5:08 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán - Hệ thống quản lý bán hàng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/sale/sale-css.css">
    <style>
        body {
            background-color: #f8f9fa;
        }

        .payment-container {
            max-width: 800px;
            margin: 50px auto;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 20px;
        }

        .payment-header {
            border-bottom: 1px solid #eee;
            padding-bottom: 15px;
            margin-bottom: 20px;
        }

        .payment-body {
            padding: 15px 0;
        }

        .payment-footer {
            border-top: 1px solid #eee;
            padding-top: 20px;
            margin-top: 20px;
        }

        .payment-amount-btn {
            margin-bottom: 8px;
        }

        .summary-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 12px;
            padding: 8px 0;
        }

        .summary-row:last-child {
            border-top: 1px solid #eee;
            padding-top: 15px;
            margin-top: 10px;
        }

        .payment-actions {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
        }

        .product-list {
            margin-bottom: 20px;
            border: 1px solid #eee;
            border-radius: 4px;
            overflow: hidden;
        }

        .product-list-header {
            background-color: #f8f9fa;
            padding: 10px 15px;
            font-weight: bold;
            border-bottom: 1px solid #eee;
        }

        .product-item {
            padding: 12px 15px;
            border-bottom: 1px solid #eee;
        }

        .product-item:last-child {
            border-bottom: none;
        }

        .product-name {
            font-weight: 500;
        }

        .product-price, .product-quantity, .product-total {
            text-align: right;
        }
    </style>
</head>
<body>
<!-- Header with search bar -->
<div class="top-header">
    <div class="search-container">
        <i class="bi bi-search"></i>
        <input type="text" placeholder="Tìm hàng hóa (F3)" id="productSearchInput">
    </div>

    <div class="tab-container">
        <span>Hóa đơn 1</span>
        <span class="tab-close">×</span>
    </div>

    <div class="header-icons">
        <a href="#" class="header-icon"><i class="bi bi-cart"></i></a>
        <a href="#" class="header-icon"><i class="bi bi-arrow-left"></i></a>
        <a href="#" class="header-icon"><i class="bi bi-arrow-clockwise"></i></a>
        <a href="#" class="header-icon"><i class="bi bi-printer"></i></a>
        <span class="header-icon">0912345678</span>
        <a href="#" class="header-icon"><i class="bi bi-list"></i></a>
    </div>
</div>

<div class="container">
    <div class="payment-container">
        <div class="payment-header">
            <div class="d-flex justify-content-between align-items-center">
                <h4>Thanh toán</h4>
                <a href="sale" class="btn btn-outline-secondary">
                    <i class="bi bi-x-lg"></i>
                </a>
            </div>
            <div class="mt-2">
                <span class="text-muted">Khách hàng: </span>
                <c:if test="${customerName != null}">
                    <span id="customerName">${customerName}</span>
                </c:if>
                <c:if test="${customerName == null}">
                    <span id="customerName">Khách lẻ</span>
                </c:if>
            </div>
        </div>

        <form id="checkoutForm" action="sale" method="post">
            <input type="hidden" name="action" value="checkout">
            
            <!-- Đảm bảo cartItems được gửi dưới dạng JSON -->
            <input type="hidden" name="cartItems" id="cartItemsInput">
            
            <!-- Thông tin khách hàng -->
            <input type="hidden" name="customerId" id="customerIdInput" value="${customerId}">
            <input type="hidden" name="customerName" id="customerNameInput" value="${customerName}">
            <input type="hidden" name="customerPhone" id="customerPhoneInput" value="${customerPhone}">
            
            <!-- Thông tin thanh toán -->
            <input type="hidden" name="total" id="totalInput" value="${totalAmount - discount}">
            <input type="hidden" name="customerPaid" id="customerPaidInput" value="${totalAmount - discount}">
            <input type="hidden" name="paymentMethod" id="paymentMethodInput" value="cash">
            <input type="hidden" name="appliedDiscountCode" id="appliedDiscountCodeInput" value="${discountCode}">
            
            <!-- Lưu trữ dữ liệu cartItems dưới dạng JSON để JavaScript có thể truy cập -->
            <script type="text/javascript">
                // Lưu trữ dữ liệu cartItems dưới dạng chuỗi JSON
                var cartItemsJsonData = `
                    <c:out value="${cartItemsJson}" escapeXml="false" />
                `;
            </script>
            
            <div class="payment-body">
                <!-- Danh sách sản phẩm -->
                <div class="product-list">
                    <table class="table table-hover">
                        <thead class="table-light">
                            <tr>
                                <th style="width: 40%">Sản phẩm</th>
                                <th style="width: 20%" class="text-end">Đơn giá</th>
                                <th style="width: 15%" class="text-end">Số lượng</th>
                                <th style="width: 25%" class="text-end">Thành tiền</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${cartItems}">
                                <tr data-product-id="${item.productId}" data-quantity="${item.quantity}" data-price="${item.price}">
                                    <td class="product-name">${item.productName}</td>
                                    <td class="text-end product-price">
                                        <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                                    </td>
                                    <td class="text-end product-quantity">${item.quantity}</td>
                                    <td class="text-end product-total">
                                        <fmt:formatNumber value="${item.total}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                        <tfoot class="table-light">
                            <tr>
                                <td colspan="3" class="text-end fw-bold">Tổng tiền hàng:</td>
                                <td class="text-end fw-bold">
                                    <fmt:formatNumber value="${totalAmount}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                                </td>
                            </tr>
                        </tfoot>
                    </table>
                </div>

                <div class="summary-row">
                    <span>Tổng tiền hàng</span>
                    <span id="modalCartTotal" data-value="${totalAmount}">
                        <fmt:formatNumber value="${totalAmount}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                    </span>
                </div>
                
<!--                 Thêm mục nhập mã giảm giá 
                <div class="summary-row">
                    <span>Mã giảm giá</span>
                    <div class="input-group">
                        <input type="text" class="form-control" id="discountCodeInput"  name="discountCode"
                               value="${discountCode}" placeholder="Nhập mã giảm giá">
                        <button class="btn btn-outline-primary" type="button" id="applyDiscountBtn">Áp dụng</button>
                    </div>
                </div>
                
                <div id="discountMessageContainer" class="mt-2 mb-2 ${discount > 0 ? 'text-success' : 'text-danger'}" 
                     style="${not empty discountMessage ? '' : 'display:none;'}">
                    ${discountMessage}
                </div>
                
                <div class="summary-row">
                    <span>Giảm giá</span>
                    <span id="modalDiscount" data-value="${discount}" class="${discount > 0 ? 'text-success' : ''}">
                        <fmt:formatNumber value="${discount}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                    </span>
                </div>
-->
                <div class="summary-row">
                    <span>Khách cần trả</span>
                    <span id="modalTotalPayable" class="text-primary fw-bold">
                        <fmt:formatNumber value="${totalAmount - discount}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                    </span>
                </div>
                <div class="summary-row">
                    <span>Khách thanh toán</span>
                    <span id="modalCustomerPaid" class="fw-bold">0 ₫</span>
                </div>

                <div class="payment-methods mb-4">
                    <c:forEach var="paymentMethod" items="${paymentMethods}" varStatus="status">
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="paymentMethod" 
                                   id="payment${paymentMethod.paymentID}" 
                                   value="${paymentMethod.paymentMethods}"
                                   ${status.index == 0 ? 'checked' : ''}>
                            <label class="form-check-label" for="payment${paymentMethod.paymentID}">
                                ${paymentMethod.paymentMethods}
                            </label>
                        </div>
                    </c:forEach>
                </div>

                <div class="payment-amounts">
                    <!-- Đã xóa các nút thanh toán nhanh -->
                </div>
            </div>

            <div class="payment-footer">
                <div class="payment-actions">
                    <a href="sale" class="btn btn-outline-secondary">HỦY</a>
                    <button type="submit" class="btn btn-primary">XÁC NHẬN THANH TOÁN</button>
                </div>
            </div>
        </form>
    </div>
</div>

<!-- Thêm tham chiếu đến file JavaScript mới -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/views/sale/payment.js"></script>

<!-- Thêm script để xử lý AJAX cho mã giảm giá -->
<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Lưu trữ dữ liệu cartItems dưới dạng chuỗi JSON
        var cartItemsJsonData = `<c:out value="${cartItemsJson}" escapeXml="false" />`;
        
        // Xử lý nút áp dụng mã giảm giá
        const applyDiscountBtn = document.getElementById('applyDiscountBtn');
        
        if (applyDiscountBtn) {
            applyDiscountBtn.addEventListener('click', function() {
                // Hiển thị thông báo loading
                document.getElementById('discountMessageContainer').innerHTML = 'Đang xử lý...';
                document.getElementById('discountMessageContainer').style.display = 'block';
                document.getElementById('discountMessageContainer').className = 'mt-2 mb-2 text-info';
                
                // Lấy các giá trị cần thiết
                const discountCode = document.getElementById('discountCodeInput').value;
                
                // Tạo object chứa dữ liệu để gửi
                const requestData = {
                    action: 'applyDiscountAjax',
                    discountCode: discountCode,
                    customerId: '${customerId}',
                    customerName: '${customerName}',
                    customerPhone: '${customerPhone}',
                    cartItems: cartItemsJsonData.trim()
                };
                
                // Gửi request AJAX
                fetch('sale', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: new URLSearchParams(requestData)
                })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    // Cập nhật UI với kết quả
                    console.log("Response data:", data); // Debug log
                    
                    if(data.success) {
                        document.getElementById('discountMessageContainer').innerHTML = data.message;
                        document.getElementById('discountMessageContainer').className = 'mt-2 mb-2 text-success';
                        
                        // Cập nhật số tiền giảm giá
                        document.getElementById('modalDiscount').setAttribute('data-value', data.discount);
                        document.getElementById('modalDiscount').innerHTML = 
                            new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(data.discount);
                        document.getElementById('modalDiscount').className = 'text-success';
                        
                        // Cập nhật tổng tiền thanh toán
                        updateTotalPayable();
                    } else {
                        document.getElementById('discountMessageContainer').innerHTML = data.message;
                        document.getElementById('discountMessageContainer').className = 'mt-2 mb-2 text-danger';
                    }
                    document.getElementById('discountMessageContainer').style.display = 'block';
                })
                .catch(error => {
                    console.error('Error applying discount:', error);
                    document.getElementById('discountMessageContainer').innerHTML = 'Đã xảy ra lỗi khi áp dụng mã giảm giá: ' + error.message;
                    document.getElementById('discountMessageContainer').className = 'mt-2 mb-2 text-danger';
                    document.getElementById('discountMessageContainer').style.display = 'block';
                });
            });
        }
        
        // Cập nhật tổng tiền khi trang được tải
        updateTotalPayable();
    });
    
    function updateTotalPayable() {
        // Store values in JavaScript variables instead of using JSP expressions directly
        var totalAmount = parseInt(document.getElementById('modalCartTotal').getAttribute('data-value') || 0);
        var discount = parseInt(document.getElementById('modalDiscount').getAttribute('data-value') || 0);
        var totalPayable = totalAmount - discount;
        
        document.getElementById('modalTotalPayable').innerHTML = 
            new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(totalPayable);
        
        // Cập nhật giá trị input hidden
        document.getElementById('totalInput').value = totalPayable;
        document.getElementById('customerPaidInput').value = totalPayable;
        
        // Hiển thị số tiền khách thanh toán
        document.getElementById('modalCustomerPaid').innerHTML = 
            new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(totalPayable);
    }
</script>

</body>
</html>
