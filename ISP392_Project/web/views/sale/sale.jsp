<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Hệ thống quản lý bán hàng</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/views/sale/sale-css.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/izitoast/1.4.0/css/iziToast.min.css">

        <style>
            .product-search {
                position: relative;
            }

            #customerSearchResults, #productSearchResults {
                position: absolute;
                top: 100%;
                left: 0;
                right: 0;
                z-index: 1050;
                max-height: 400px;
                overflow-y: auto;
                background: white;
                border: 1px solid #ddd;
                border-radius: 4px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                display: none;
                margin-top: 2px;
                width: 100%;
            }

            #customerSearchResults.show, #productSearchResults.show {
                display: block !important;
            }

            .customer-item, .product-item {
                border-bottom: 1px solid #eee;
                cursor: pointer;
            }

            .customer-item:hover, .product-item:hover {
                background-color: #f0f8ff;
            }

            .customer-item:last-child, .product-item:last-child {
                border-bottom: none;
            }

            .customer-name, .product-name {
                color: #000;
                font-weight: 500;
            }

            .customer-phone, .product-code {
                color: #6c757d;
                font-size: 0.9em;
            }

            .product-price {
                color: #0d6efd;
                min-width: 70px;
                text-align: right;
            }

            .product-stock {
                color: #28a745;
                font-size: 0.85em;
            }

            .product-image img {
                object-fit: contain;
                background-color: #f8f9fa;
                border-radius: 4px;
            }

            /* Đảm bảo dropdown hiển thị đúng vị trí */
            .search-container {
                position: relative;
            }

            /* Styles cho giỏ hàng */
            #cartItems {
                list-style: none;
                padding: 0;
                margin: 0;
            }

            #cartItems li {
                padding: 10px 15px;
                border-bottom: 1px solid #eee;
                display: flex;
                flex-direction: column;
            }

            .cart-item-header {
                display: flex;
                justify-content: space-between;
                margin-bottom: 8px;
            }

            .cart-item-name {
                font-weight: 500;
                color: #000;
            }

            .cart-item-price {
                color: #0d6efd;
            }

            .cart-item-controls {
                display: flex;
                align-items: center;
                justify-content: space-between;
            }

            .quantity-control {
                display: flex;
                align-items: center;
            }

            .quantity-control button {
                width: 30px;
                height: 30px;
                background: #f8f9fa;
                border: 1px solid #dee2e6;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 16px;
                cursor: pointer;
            }

            .quantity-control button:first-child {
                border-radius: 4px 0 0 4px;
            }

            .quantity-control button:last-child {
                border-radius: 0 4px 4px 0;
            }

            .quantity-control input {
                width: 40px;
                height: 30px;
                border: 1px solid #dee2e6;
                border-left: none;
                border-right: none;
                text-align: center;
            }

            .remove-item {
                color: #dc3545;
                background: none;
                border: none;
                cursor: pointer;
                font-size: 18px;
            }
        </style>
    </head>
    <body>
        <!-- Header with search bar -->
        <div class="top-header">
            <div class="search-container">
                <i class="bi bi-search"></i>
                <input type="text" placeholder="Tìm hàng hóa (F3)" id="productSearchInput">
                <div id="productSearchResults"></div>
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

        <div class="main-container">
            <!-- Left panel - Cart -->
            <div class="left-panel">
                <ul class="product-list" id="cartItems">
                    <!-- Cart items will be dynamically added here -->
                </ul>

                <div class="cart-summary">
                    <div class="summary-row">
                        <span>Tổng tiền hàng</span>
                        <span id="cartTotal">0</span>
                    </div>

                    <div class="summary-row">
                        <span>Số lượng</span>
                        <span id="cartQuantity">0</span>
                    </div>
                </div>

                <div class="cart-actions">
                    <!--                    <button class="btn btn-secondary me-2 d-flex align-items-center">
                                            <i class="bi bi-lightning me-1"></i> Bán nhanh
                                        </button>
                    -->

                    <button class="btn btn-primary me-2 d-flex align-items-center" id="checkoutBtn2">
                        <i class="bi bi-bag me-1"></i> Bán
                    </button>

                    <!--                    <button class="btn btn-outline-secondary d-flex align-items-center">
                                            <i class="bi bi-truck me-1"></i> Bán giao hàng
                                        </button>
                    -->

                    <!-- Thêm form ẩn để gửi dữ liệu thanh toán -->
                    <form id="checkoutForm2" action="sale" method="post" style="display: none;">
                        <input type="hidden" name="action" value="showPayment">
                        <input type="hidden" name="cartItems" id="cartItemsData">
                        <input type="hidden" name="customerName" id="customerNameData">
                        <input type="hidden" name="customerPhone" id="customerPhoneData">
                        <input type="hidden" name="customerId" id="customerIdData">
                        <input type="hidden" name="total" id="totalData">
                    </form>
                </div>
            </div>

            <!-- Right panel - Product selection -->
            <div class="right-panel">
                <div class="product-search position-relative">
                    <div class="input-group">
                        <span class="input-group-text bg-light border-0">
                            <i class="bi bi-search"></i>
                        </span>
                        <input type="text" 
                               class="form-control" 
                               placeholder="Tìm khách hàng (F4)" 
                               id="customerSearchInput" 
                               autocomplete="off">
                        <div id="customerSearchResults"></div>
                        <button class="btn btn-outline-secondary" onclick="window.location.href = '${pageContext.request.contextPath}/Customers?service=addCustomer'">
                            <i class="bi bi-plus"></i>
                        </button>
                        <button class="btn btn-outline-secondary">
                            <i class="bi bi-list"></i>
                        </button>
                        <button class="btn btn-outline-secondary">
                            <i class="bi bi-funnel"></i>
                        </button>
                        <button class="btn btn-outline-secondary">
                            <i class="bi bi-grid"></i>
                        </button>
                    </div>
                </div>

                <div class="product-grid">
                    <c:forEach var="product" items="${products}">
                        <div class="product-card" 
                             data-product-id="${product.productId}" 
                             data-product-name="${product.name}" 
                             data-product-price="${product.price}"
                             data-product-stockquantity="${product.quantity}">
                            <img src="${not empty product.image ? product.image : 'https://via.placeholder.com/60'}" 
                                 alt="${product.name}">
                            <div class="product-card-name">${product.name}</div>
                            <div class="product-card-price">
                                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <div class="d-flex justify-content-between mt-3">
                    <div class="d-flex align-items-center">
                        <i class="bi bi-telephone me-2"></i>
                        <span>1900 6522</span>
                    </div>
                    <div>
                        <i class="bi bi-question-circle me-2"></i>
                        <i class="bi bi-chat"></i>
                    </div>
                </div>
            </div>
        </div>


        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/views/sale/script.js"></script>

        <!-- Thêm thư viện iziToast -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/izitoast/1.4.0/js/iziToast.min.js"></script>

        <!-- Toast message script using iziToast -->
        <script>
                            document.addEventListener('DOMContentLoaded', function () {
                                // Toast message display
                                var toastMessage = "${sessionScope.toastMessage}";
                                var toastType = "${sessionScope.toastType}";

                                if (toastMessage && toastMessage.trim() !== "") {
                                    console.log("Toast message found:", toastMessage, "Type:", toastType);

                                    // Use iziToast
                                    if (toastType === "success") {
                                        iziToast.success({
                                            title: 'Thành công',
                                            message: toastMessage,
                                            position: 'topRight',
                                            timeout: 5000
                                        });
                                    } else {
                                        iziToast.error({
                                            title: 'Lỗi',
                                            message: toastMessage,
                                            position: 'topRight',
                                            timeout: 5000
                                        });
                                    }

                                    // Clear toast message from session
                                    fetch('${pageContext.request.contextPath}/sale?action=clearToast', {
                                        method: 'POST'
                                    }).catch(error => {
                                        console.error('Error clearing toast:', error);
                                    });
                                }
                            });
        </script>
    </body>
</html>