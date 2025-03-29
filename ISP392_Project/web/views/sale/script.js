/**
 * Hệ thống quản lý trả hàng - JavaScript
 */

// Biến toàn cục để theo dõi sản phẩm đã được thêm vào
let recentlyAddedProducts = new Set();
let lastClickTime = 0;

document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM loaded'); // Debug log
    // Khởi tạo các chức năng chung
    initializeQuantityControls();
    initializeActionMenus();
    calculateTotals();
    
    // Thêm xử lý nút trả hàng
    initializeReturnButton();
    
    // Khởi tạo chức năng tìm kiếm khách hàng
    initializeCustomerSearch();
    
    // Khởi tạo chức năng tìm kiếm sản phẩm
    initializeProductSearch();
    
    // Khởi tạo chức năng giỏ hàng
    initializeCart();
    
    // Khởi tạo nút thanh toán
    initializeCheckoutButton();
    
    // Khởi tạo modal thanh toán
    initializePaymentModal();
    
    // Khởi tạo sự kiện cho các sản phẩm trong grid
    initializeProductGrid();
    
    // Thêm phím tắt F4 để focus vào ô tìm kiếm khách hàng
    document.addEventListener('keydown', function(e) {
        if (e.key === 'F4') {
            e.preventDefault();
            const customerSearchInput = document.getElementById('customerSearchInput');
            if (customerSearchInput) customerSearchInput.focus();
        }
    });
});

// Khởi tạo điều khiển số lượng
function initializeQuantityControls() {
    const minusBtns = document.querySelectorAll('.minus-btn');
    const plusBtns = document.querySelectorAll('.plus-btn');
    const quantityInputs = document.querySelectorAll('.quantity-input');
    const quantityDisplays = document.querySelectorAll('.quantity-display');

    // Thiết lập sự kiện cho nút giảm
    minusBtns.forEach((btn) => {
        btn.addEventListener('click', function() {
            const input = this.parentElement.querySelector('.quantity-input');
            const productId = input.getAttribute('data-product-id');
            const currentValue = parseInt(input.value);
            if (currentValue > 0) {
                input.value = currentValue - 1;
                updateQuantityDisplay(input);
                updateProductTotal(input);
                calculateTotals(); // Cập nhật tổng tiền
            }
        });
    });

    // Thiết lập sự kiện cho nút tăng
    plusBtns.forEach((btn) => {
        btn.addEventListener('click', function() {
            const input = this.parentElement.querySelector('.quantity-input');
            const productId = input.getAttribute('data-product-id');
            const currentValue = parseInt(input.value);
            const maxValue = parseInt(input.getAttribute('max') || 9999);
            if (currentValue < maxValue) {
                input.value = currentValue + 1;
                updateQuantityDisplay(input);
                updateProductTotal(input);
                calculateTotals(); // Cập nhật tổng tiền
            }
        });
    });

    // Thiết lập sự kiện cho thay đổi trực tiếp
    quantityInputs.forEach((input) => {
        input.addEventListener('change', function() {
            const maxValue = parseInt(this.getAttribute('max') || 9999);
            let value = parseInt(this.value) || 0;
            
            // Đảm bảo giá trị nằm trong phạm vi hợp lệ
            if (value < 0) value = 0;
            if (value > maxValue) value = maxValue;
            
            this.value = value;
            updateQuantityDisplay(this);
            updateProductTotal(this);
            calculateTotals(); // Cập nhật tổng tiền
        });
    });
}

// Cập nhật hiển thị số lượng
function updateQuantityDisplay(input) {
    const display = input.closest('.quantity-container').querySelector('.quantity-display');
    if (!display) return;
    
    const maxValue = input.getAttribute('max') || '';
    const currentValue = input.value;
    
    display.textContent = `${currentValue} / ${maxValue}`;
}

// Cập nhật thành tiền cho mỗi sản phẩm
function updateProductTotal(input) {
    const productId = input.getAttribute('data-product-id');
    const currentValue = parseInt(input.value);
    const price = parseInt(input.getAttribute('data-price'));
    const total = currentValue * price;
    
    const productTotalElement = document.querySelector(`.product-total[data-product-id="${productId}"]`);
    if (productTotalElement) {
        productTotalElement.textContent = formatCurrency(total);
    }
    
    // Cập nhật input hidden cho form nếu có
    const hiddenInput = document.querySelector(`input[name="returnQuantity_${productId}"]`);
    if (hiddenInput) {
        hiddenInput.value = currentValue;
    }
}

// Khởi tạo menu hành động
function initializeActionMenus() {
    const actionMenuToggles = document.querySelectorAll('.action-menu-toggle');
    const actionMenus = document.querySelectorAll('.action-menu');

    actionMenuToggles.forEach((toggle, index) => {
        toggle.addEventListener('click', function(e) {
            e.stopPropagation();
            
            // Đóng tất cả các menu khác
            actionMenus.forEach((menu) => {
                if (menu !== this.closest('.product-row').querySelector('.action-menu')) {
                    menu.classList.remove('show');
                }
            });
            
            // Chuyển đổi menu hiện tại
            const menu = this.closest('.product-row').querySelector('.action-menu');
            menu.classList.toggle('show');
        });
    });

    // Đóng menu hành động khi nhấp vào nơi khác
    document.addEventListener('click', function() {
        actionMenus.forEach(menu => {
            menu.classList.remove('show');
        });
    });

    // Ngăn menu đóng khi nhấp vào bên trong nó
    actionMenus.forEach(menu => {
        menu.addEventListener('click', function(e) {
            e.stopPropagation();
        });
    });
}

// Tính toán tổng tiền
function calculateTotals() {
    const quantityInputs = document.querySelectorAll('.quantity-input');
    let subtotal = 0;
    
    quantityInputs.forEach((input) => {
        const quantity = parseInt(input.value) || 0;
        const price = parseInt(input.getAttribute('data-price')) || 0;
        
        subtotal += quantity * price;
    });
    
    // Cập nhật hiển thị tổng tiền
    const subtotalElement = document.getElementById('subtotal');
    const totalElement = document.getElementById('total');
    
    if (subtotalElement) {
        subtotalElement.textContent = formatCurrency(subtotal);
    }
    
    if (totalElement) {
        totalElement.textContent = formatCurrency(subtotal);
    }
    
    // Cập nhật input hidden cho form nếu có
    const totalAmountInput = document.querySelector('input[name="totalAmount"]');
    if (totalAmountInput) {
        totalAmountInput.value = subtotal;
    }
}

// Định dạng tiền tệ
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN').format(amount);
}

function initializeReturnButton() {
    const submitReturnBtn = document.getElementById('submitReturnBtn');
    console.log('Submit button:', submitReturnBtn); // Debug log
    
    if (submitReturnBtn) {
        submitReturnBtn.addEventListener('click', function(e) {
            console.log('Button clicked'); // Debug log
            e.preventDefault();
            
            // Kiểm tra xem có sản phẩm nào được chọn để trả không
            const quantityInputs = document.querySelectorAll('.quantity-input');
            let hasItems = false;
            let totalQuantity = 0;
            
            quantityInputs.forEach(input => {
                const quantity = parseInt(input.value) || 0;
                totalQuantity += quantity;
                if (quantity > 0) {
                    hasItems = true;
                }
            });
            
            if (!hasItems) {
                alert('Vui lòng chọn ít nhất một sản phẩm để trả');
                return;
            }

            // Cập nhật tổng tiền lần cuối
            calculateTotals();
            
            // Xác nhận trước khi submit
            if (confirm(`Xác nhận trả ${totalQuantity} sản phẩm?`)) {
                const returnForm = document.getElementById('returnForm');
                console.log('Form:', returnForm); // Debug log
                
                if (returnForm) {
                    returnForm.submit();
                } else {
                    console.error('Form not found!');
                }
            }
        });
    } else {
        console.error('Return button not found!'); // Debug log
    }
}

// Khởi tạo chức năng tìm kiếm khách hàng
function initializeCustomerSearch() {
    const customerSearchInput = document.getElementById('customerSearchInput');
    const customerSearchResults = document.getElementById('customerSearchResults');
    let searchTimeout;
    
    customerSearchInput.addEventListener('input', function() {
        clearTimeout(searchTimeout);
        
        const searchTerm = this.value.trim();
        if (searchTerm.length < 1) {
            customerSearchResults.innerHTML = '';
            customerSearchResults.style.display = 'none';
            customerSearchResults.classList.remove('show');
            return;
        }
        
        searchTimeout = setTimeout(function() {
            fetch('sale?action=searchCustomers&query=' + encodeURIComponent(searchTerm))
                .then(function(response) {
                    if (!response.ok) {
                        throw new Error('Network response was not ok: ' + response.status);
                    }
                    return response.json();
                })
                .then(function(data) {
                    console.log('Customer search results:', data);
                    customerSearchResults.innerHTML = '';
                    
                    if (!Array.isArray(data) || data.length === 0) {
                        var noResults = document.createElement('div');
                        noResults.className = 'customer-item';
                        noResults.textContent = 'Không tìm thấy khách hàng';
                        customerSearchResults.appendChild(noResults);
                    } else {
                        data.forEach(function(customer) {
                            var item = document.createElement('div');
                            item.className = 'customer-item';
                            item.innerHTML = `
                                <div class="d-flex align-items-center p-2">
                                    <div class="customer-details flex-grow-1">
                                        <div class="customer-id" style="display: none;">${customer.id}</div>
                                        <div class="customer-name">${customer.customerName || 'Không có tên'}</div>
                                        <div class="customer-phone">${customer.phone || 'Không có SĐT'}</div>
                                    </div>
                                </div>
                            `;
                            
                            item.addEventListener('click', function() {
                                selectCustomer(customer);
                                customerSearchInput.value = customer.customerName || '';
                                customerSearchResults.style.display = 'none';
                                customerSearchResults.classList.remove('show');
                            });
                            
                            customerSearchResults.appendChild(item);
                        });
                    }
                    
                    // Đảm bảo hiển thị dropdown
                    customerSearchResults.style.display = 'block';
                    customerSearchResults.classList.add('show');
                })
                .catch(function(error) {
                    console.error('Error searching customers:', error);
                    customerSearchResults.innerHTML = '';
                    var errorDiv = document.createElement('div');
                    errorDiv.className = 'customer-item';
                    errorDiv.textContent = 'Lỗi tìm kiếm: ' + error.message;
                    customerSearchResults.appendChild(errorDiv);
                    customerSearchResults.style.display = 'block';
                    customerSearchResults.classList.add('show');
                });
        }, 300);
    });
    
    // Đóng dropdown khi click bên ngoài
    document.addEventListener('click', function(e) {
        if (!customerSearchInput.contains(e.target) && !customerSearchResults.contains(e.target)) {
            customerSearchResults.style.display = 'none';
            customerSearchResults.classList.remove('show');
        }
    });
}

// Chọn khách hàng
function selectCustomer(customer) {
    const customerInfo = document.getElementById('customerInfo');
    if (customerInfo) {
        customerInfo.innerHTML = `
            <div class="selected-customer">
                <div class="customer-name">${customer.customerName || 'Không có tên'}</div>
                <div class="customer-phone">${customer.phone || 'Không có SĐT'}</div>
                <input type="hidden" name="customerId" value="${customer.id}">
            </div>
        `;
    }
    
    // Cập nhật tiêu đề modal thanh toán
    const paymentModalHeader = document.querySelector('.payment-modal-header h5');
    if (paymentModalHeader) {
        paymentModalHeader.textContent = customer.customerName || 'Walk-in Customer';
    }
    
    // Lưu thông tin khách hàng vào biến toàn cục để sử dụng khi thanh toán
    window.selectedCustomer = {
        id: customer.id,
        name: customer.customerName || 'Không có tên',
        phone: customer.phone || 'Không có SĐT'
    };
    
    // Hiển thị thông tin khách hàng đã chọn trên giao diện
    const customerDisplay = document.getElementById('selectedCustomerDisplay');
    if (customerDisplay) {
        customerDisplay.innerHTML = `
            <div class="selected-customer-info">
                <strong>${customer.customerName || 'Không có tên'}</strong>
                <span>${customer.phone || 'Không có SĐT'}</span>
            </div>
        `;
        customerDisplay.style.display = 'block';
    }
}

// Khởi tạo chức năng tìm kiếm sản phẩm
function initializeProductSearch() {
    const newProductSearchInput = document.getElementById('productSearchInput');
    const newProductSearchResults = document.getElementById('productSearchResults');
    let searchTimeout;
    
    newProductSearchInput.addEventListener('input', function() {
        clearTimeout(searchTimeout);
        
        const searchTerm = this.value.trim();
        if (searchTerm.length < 1) {
            newProductSearchResults.innerHTML = '';
            newProductSearchResults.style.display = 'none';
            newProductSearchResults.classList.remove('show');
            return;
        }
        
        searchTimeout = setTimeout(function() {
            // Thay đổi URL để phù hợp với endpoint trong SaleController.java
            const url = 'sale?action=searchProducts&query=' + encodeURIComponent(searchTerm);
            
            console.log(`[${new Date().toISOString()}] Sending search request to URL: ${url}`);
            
            fetch(url)
                .then(function(response) { 
                    if (!response.ok) {
                        throw new Error('Network response was not ok: ' + response.status);
                    }
                    return response.json(); 
                })
                .then(function(data) {
                    console.log(`[${new Date().toISOString()}] Search results received:`, data);
                    newProductSearchResults.innerHTML = '';
                    
                    if (!Array.isArray(data) || data.length === 0) {
                        var noResults = document.createElement('div');
                        noResults.className = 'product-item';
                        noResults.textContent = 'Không tìm thấy sản phẩm';
                        newProductSearchResults.appendChild(noResults);
                    } else {
                        data.forEach(function(product) {
                            var item = document.createElement('div');
                            item.className = 'product-item';
                            item.setAttribute('data-product-id', product.id);
                            
                            var itemContent = `
                                <div class="d-flex align-items-center p-2">
                                    <div class="product-image me-3">
                                        <img src="${product.imageURL || 'images/no-image.png'}" alt="${product.productName}" width="50" height="50">
                                    </div>
                                    <div class="product-details flex-grow-1">
                                        <div class="product-name">${product.productName}</div>
                                        <div class="product-code text-muted small">${product.productCode || ''}</div>
                                        <div class="product-stock small text-success">Tồn: ${product.stockQuantity}</div>
                                    </div>
                                    <div class="product-price text-primary fw-bold">${new Intl.NumberFormat('vi-VN').format(product.price)}</div>
                                </div>
                            `;
                            
                            item.innerHTML = itemContent;
                            
                            // Xử lý sự kiện click
                            item.addEventListener('mousedown', function(e) {
                                e.stopPropagation();
                                e.preventDefault();
                                
                                console.log(`[${new Date().toISOString()}] Product search item clicked:`, product);
                                
                                // Thêm sản phẩm vào giỏ hàng
                                addProductToCart(product, e);
                                
                                // Đóng dropdown sau khi chọn
                                newProductSearchInput.value = '';
                                newProductSearchResults.style.display = 'none';
                                newProductSearchResults.classList.remove('show');
                            });
                            
                            newProductSearchResults.appendChild(item);
                        });
                    }
                    
                    // Hiển thị dropdown
                    newProductSearchResults.style.display = 'block';
                    newProductSearchResults.classList.add('show');
                })
                .catch(function(error) {
                    console.error('Error searching products:', error);
                    newProductSearchResults.innerHTML = '';
                    var errorDiv = document.createElement('div');
                    errorDiv.className = 'product-item';
                    errorDiv.textContent = 'Lỗi tìm kiếm: ' + error.message;
                    newProductSearchResults.appendChild(errorDiv);
                    newProductSearchResults.style.display = 'block';
                    newProductSearchResults.classList.add('show');
                });
        }, 300);
    });
    
    // Đóng dropdown khi click bên ngoài
    document.addEventListener('mousedown', function(e) {
        if (!newProductSearchInput.contains(e.target) && !newProductSearchResults.contains(e.target)) {
            newProductSearchResults.style.display = 'none';
            newProductSearchResults.classList.remove('show');
        }
    });
    
    // Thêm phím tắt F3 để focus vào ô tìm kiếm sản phẩm
    document.addEventListener('keydown', function(e) {
        if (e.key === 'F3') {
            e.preventDefault();
            newProductSearchInput.focus();
        }
    });
}

// Thêm sản phẩm vào giỏ hàng từ kết quả tìm kiếm
function addProductToCart(product, event) {
    // Thực hiện phòng chống double-click
    const currentTime = new Date().getTime();
    if (currentTime - lastClickTime < 500) {
        console.log('Click quá nhanh, bỏ qua!');
        if (event) event.preventDefault();
        return false;
    }
    lastClickTime = currentTime;
    
    // Kiểm tra sản phẩm đã được thêm gần đây chưa
    const productKey = `${product.id}-${currentTime}`;
    if (recentlyAddedProducts.has(product.id)) {
        console.log('Sản phẩm vừa được thêm, bỏ qua để tránh thêm hai lần');
        if (event) event.preventDefault();
        return false;
    }
    
    // Đánh dấu sản phẩm đã được thêm và xóa sau một khoảng thời gian
    recentlyAddedProducts.add(product.id);
    setTimeout(() => {
        recentlyAddedProducts.delete(product.id);
    }, 1000);
    
    // Lấy thông tin sản phẩm
    const productId = product.id;
    const productName = product.productName;
    const productPrice = product.price;
    const stockQuantity = product.stockQuantity;
    
    console.log(`[${new Date().toISOString()}] Adding product to cart: ID=${productId}, Name=${productName}, Price=${productPrice}, Stock=${stockQuantity}`);
    
    // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
    const existingItem = document.querySelector(`#cartItems li[data-product-id="${productId}"]`);
    
    if (existingItem) {
        // Nếu đã có, tăng số lượng lên 1
        const quantityInput = existingItem.querySelector('.quantity-input');
        console.log('Quantity input:', quantityInput);
        
        // Lấy giá trị hiện tại và chuyển đổi thành số
        let currentQuantity = parseInt(quantityInput.value || '1', 10);
        console.log(`[${new Date().toISOString()}] Current quantity before update:`, currentQuantity);
        
        // Tăng số lượng lên 1
        let newQuantity = currentQuantity + 1;
        console.log(`[${new Date().toISOString()}] New quantity after update:`, newQuantity);
        
        // Kiểm tra số lượng tồn kho
        if (stockQuantity > 0 && newQuantity > stockQuantity) {
            alert(`Chỉ còn ${stockQuantity} sản phẩm trong kho!`);
            newQuantity = stockQuantity;
        }
        
        // Cập nhật số lượng mới
        quantityInput.value = newQuantity;
        console.log(`[${new Date().toISOString()}] Updated input value:`, quantityInput.value);
        
        // Cập nhật tổng giá
        updateCartTotal();
    } else {
        // Nếu chưa có, thêm mới
        const li = document.createElement('li');
        li.setAttribute('data-product-id', productId);
        li.setAttribute('data-product-price', productPrice);
        
        // Định dạng giá tiền
        const formattedPrice = new Intl.NumberFormat('vi-VN').format(productPrice);
        
        li.innerHTML = `
            <div class="cart-item-header">
                <div class="cart-item-name">${productName}</div>
                <div class="cart-item-price">${formattedPrice}</div>
            </div>
            <div class="cart-item-controls">
                <div class="quantity-control">
                    <button class="quantity-decrease">-</button>
                    <input type="text" value="1" class="quantity-input" data-product-id="${productId}" data-product-price="${productPrice}">
                    <button class="quantity-increase">+</button>
                </div>
                <button class="remove-item"><i class="bi bi-x"></i></button>
            </div>
        `;
        
        document.getElementById('cartItems').appendChild(li);
        console.log(`[${new Date().toISOString()}] Added new product to cart with quantity = 1`);
        
        // Thêm sự kiện cho nút tăng giảm số lượng
        const decreaseBtn = li.querySelector('.quantity-decrease');
        const increaseBtn = li.querySelector('.quantity-increase');
        const quantityInput = li.querySelector('.quantity-input');
        const removeBtn = li.querySelector('.remove-item');
        
        decreaseBtn.addEventListener('click', function(e) {
            e.preventDefault();
            let quantity = parseInt(quantityInput.value || '1', 10);
            if (quantity > 1) {
                quantity--;
                quantityInput.value = quantity;
                updateCartTotal();
            }
        });
        
        increaseBtn.addEventListener('click', function(e) {
            e.preventDefault();
            let quantity = parseInt(quantityInput.value || '1', 10);
            if (stockQuantity > 0 && quantity >= stockQuantity) {
                alert(`Chỉ còn ${stockQuantity} sản phẩm trong kho!`);
                return;
            }
            quantity++;
            quantityInput.value = quantity;
            updateCartTotal();
        });
        
        quantityInput.addEventListener('change', function() {
            let quantity = parseInt(this.value || '1', 10);
            if (quantity < 1) quantity = 1;
            if (stockQuantity > 0 && quantity > stockQuantity) {
                alert(`Chỉ còn ${stockQuantity} sản phẩm trong kho!`);
                quantity = stockQuantity;
            }
            this.value = quantity;
            updateCartTotal();
        });
        
        removeBtn.addEventListener('click', function(e) {
            e.preventDefault();
            li.remove();
            updateCartTotal();
        });
        
        // Cập nhật tổng giá
        updateCartTotal();
    }
    
    return true;
}

// Khởi tạo nút thanh toán
function initializeCheckoutButton() {
    const checkoutBtn = document.getElementById('checkoutBtn2');
    const checkoutForm = document.getElementById('checkoutForm2');
    
    if (checkoutBtn && checkoutForm) {
        checkoutBtn.addEventListener('click', function() {
            // Kiểm tra xem giỏ hàng có trống không
            const cartItems = document.getElementById('cartItems');
            if (!cartItems || cartItems.children.length === 0) {
                alert('Giỏ hàng trống. Vui lòng thêm sản phẩm vào giỏ hàng.');
                return;
            }
            
            // Lấy thông tin khách hàng
            const customerNameData = document.getElementById('customerNameData');
            const customerPhoneData = document.getElementById('customerPhoneData');
            const customerIdData = document.getElementById('customerIdData');
            const totalData = document.getElementById('totalData');
            
            // Sử dụng thông tin khách hàng đã chọn từ biến toàn cục
            if (window.selectedCustomer) {
                customerNameData.value = window.selectedCustomer.name;
                customerPhoneData.value = window.selectedCustomer.phone;
                customerIdData.value = window.selectedCustomer.id;
            } else {
                customerNameData.value = "Walking Customer";
                customerPhoneData.value = "";
                customerIdData.value = "0";
            }
            
            console.log("Sending customer data:", {
                name: customerNameData.value,
                phone: customerPhoneData.value,
                id: customerIdData.value
            });
            
            // Lấy thông tin giỏ hàng
            const cartItemsArray = [];
            const cartItemElements = document.querySelectorAll('#cartItems li');
            
            cartItemElements.forEach(function(item) {
                const productId = item.getAttribute('data-product-id');
                const productName = item.querySelector('.cart-item-name').textContent;
                const priceText = item.querySelector('.cart-item-price').textContent;
                const price = parseInt(priceText.replace(/[^\d]/g, ''));
                const quantity = parseInt(item.querySelector('.quantity-input').value);
                
                cartItemsArray.push({
                    productId: productId,
                    productName: productName,
                    price: price,
                    quantity: quantity,
                    total: price * quantity
                });
            });
            
            // Cập nhật dữ liệu form
            document.getElementById('cartItemsData').value = JSON.stringify(cartItemsArray);
            totalData.value = document.getElementById('cartTotal').textContent.replace(/[^\d]/g, '');
            
            // Thay đổi action thành showPayment
            const actionInput = checkoutForm.querySelector('input[name="action"]');
            if (actionInput) {
                actionInput.value = 'showPayment';
            }
            
            // Submit form
            checkoutForm.submit();
        });
    } else {
        console.error('Checkout button or form not found!');
    }
}

// Khởi tạo chức năng giỏ hàng
function initializeCart() {
    // Thêm sự kiện click cho các sản phẩm
    document.querySelectorAll('.product-card').forEach(function(card) {
        card.addEventListener('click', function() {
            // Lấy thông tin sản phẩm từ thuộc tính data-*
            const productId = this.getAttribute('data-product-id') || '';
            const productName = this.getAttribute('data-product-name') || '';
            
            // Lấy giá trị price từ thuộc tính data-product-price và chuyển đổi thành số
            let productPrice = 0;
            try {
                const priceStr = this.getAttribute('data-product-price');
                console.log(`Raw price from attribute: "${priceStr}"`);
                productPrice = parseInt(priceStr) || 0;
            } catch (e) {
                console.error('Error parsing price:', e);
                productPrice = 0;
            }
            
            // Lấy số lượng tồn kho
            let stockQuantity = 0;
            try {
                const stockStr = this.getAttribute('data-product-stockquantity');
                console.log(`Raw stock from attribute: "${stockStr}"`);
                stockQuantity = parseInt(stockStr) || 0;
            } catch (e) {
                console.error('Error parsing stock quantity:', e);
                stockQuantity = 0;
            }
            
            console.log(`Clicked product: ID=${productId}, Name=${productName}, Price=${productPrice}, Stock=${stockQuantity}`);
            
            // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
            const existingItem = document.querySelector(`#cartItems li[data-product-id="${productId}"]`);
            
            if (existingItem) {
                // Nếu đã có, tăng số lượng
                const quantityInput = existingItem.querySelector('.quantity-control input');
                let quantity = parseInt(quantityInput.value || '1') + 1;
                
                // Kiểm tra số lượng tồn kho
                if (stockQuantity > 0 && quantity > stockQuantity) {
                    alert(`Chỉ còn ${stockQuantity} sản phẩm trong kho!`);
                    quantity = stockQuantity;
                }
                
                quantityInput.value = quantity;
                
                // Cập nhật tổng giá
                updateCartTotal();
            } else {
                // Nếu chưa có, thêm mới
                const li = document.createElement('li');
                li.setAttribute('data-product-id', productId);
                li.setAttribute('data-product-price', productPrice.toString());
                
                console.log(`Setting data-price attribute: ${productPrice.toString()}`);
                
                // Định dạng giá tiền
                const formattedPrice = new Intl.NumberFormat('vi-VN').format(productPrice);
                
                li.innerHTML = `
                    <div class="cart-item-header">
                        <div class="cart-item-name">${productName}</div>
                        <div class="cart-item-price">${formattedPrice}</div>
                    </div>
                    <div class="cart-item-controls">
                        <div class="quantity-control">
                            <button class="quantity-decrease">-</button>
                            <input type="text" value="1" class="quantity-input" data-product-id="${productId}" data-product-price="${productPrice}">
                            <button class="quantity-increase">+</button>
                        </div>
                        <button class="remove-item"><i class="bi bi-x"></i></button>
                    </div>
                `;
                
                document.getElementById('cartItems').appendChild(li);
                
                // Thêm sự kiện cho nút tăng giảm số lượng
                const decreaseBtn = li.querySelector('.quantity-decrease');
                const increaseBtn = li.querySelector('.quantity-increase');
                const quantityInput = li.querySelector('.quantity-input');
                const removeBtn = li.querySelector('.remove-item');
                
                decreaseBtn.addEventListener('click', function() {
                    let quantity = parseInt(quantityInput.value || '1');
                    if (quantity > 1) {
                        quantity--;
                        quantityInput.value = quantity;
                        updateCartTotal();
                    }
                });
                
                increaseBtn.addEventListener('click', function() {
                    let quantity = parseInt(quantityInput.value || '1');
                    if (stockQuantity > 0 && quantity >= stockQuantity) {
                        alert(`Chỉ còn ${stockQuantity} sản phẩm trong kho!`);
                        return;
                    }
                    quantity++;
                    quantityInput.value = quantity;
                    updateCartTotal();
                });
                
                quantityInput.addEventListener('change', function() {
                    let quantity = parseInt(this.value || '1');
                    if (quantity < 1) quantity = 1;
                    if (stockQuantity > 0 && quantity > stockQuantity) {
                        alert(`Chỉ còn ${stockQuantity} sản phẩm trong kho!`);
                        quantity = stockQuantity;
                    }
                    this.value = quantity;
                    updateCartTotal();
                });
                
                removeBtn.addEventListener('click', function() {
                    li.remove();
                    updateCartTotal();
                });
                
                // Cập nhật tổng giá
                updateCartTotal();
            }
        });
    });
}

// Cập nhật tổng giá trị giỏ hàng
function updateCartTotal() {
    const cartItems = document.getElementById('cartItems');
    const cartTotal = document.getElementById('cartTotal');
    const cartQuantity = document.getElementById('cartQuantity');
    
    if (!cartItems || !cartTotal || !cartQuantity) return;
    
    let total = 0;
    let quantity = 0;
    
    const items = cartItems.querySelectorAll('li');
    items.forEach(function(item) {
        const quantityInput = item.querySelector('.quantity-input');
        const itemQuantity = parseInt(quantityInput.value || '1', 10);
        const itemPrice = parseInt(item.getAttribute('data-product-price') || quantityInput.getAttribute('data-product-price'), 10);
        
        total += itemPrice * itemQuantity;
        quantity += itemQuantity;
    });
    
    cartTotal.textContent = new Intl.NumberFormat('vi-VN').format(total);
    cartQuantity.textContent = quantity;
    
    // Cập nhật thông tin trong modal thanh toán nếu đang hiển thị
    const modalCartTotal = document.getElementById('modalCartTotal');
    const modalTotalPayable = document.getElementById('modalTotalPayable');
    const totalPayableInput = document.getElementById('totalPayableInput');
    
    if (modalCartTotal) modalCartTotal.textContent = new Intl.NumberFormat('vi-VN').format(total);
    if (modalTotalPayable) modalTotalPayable.textContent = new Intl.NumberFormat('vi-VN').format(total);
    if (totalPayableInput) totalPayableInput.value = total;
}

// Khởi tạo modal thanh toán
function initializePaymentModal() {
    const checkoutBtn = document.getElementById('checkoutBtn');
    const paymentModal = document.getElementById('paymentModal');
    const modalOverlay = document.getElementById('modalOverlay');
    const closePaymentModal = document.getElementById('closePaymentModal');
    const paymentAmountBtns = document.querySelectorAll('.payment-amount-btn');
    const checkoutForm = document.getElementById('checkoutForm');
    
    if (!checkoutBtn || !paymentModal || !modalOverlay || !closePaymentModal) return;
    
    // Mở modal thanh toán
    checkoutBtn.addEventListener('click', function() {
        // Cập nhật thông tin trong modal
        updatePaymentModalInfo();
        
        // Hiển thị modal
        paymentModal.style.display = 'block';
        modalOverlay.style.display = 'block';
    });
    
    // Đóng modal thanh toán
    closePaymentModal.addEventListener('click', function() {
        paymentModal.style.display = 'none';
        modalOverlay.style.display = 'none';
    });
    
    // Xử lý các nút số tiền nhanh
    paymentAmountBtns.forEach(function(btn) {
        btn.addEventListener('click', function() {
            const amount = parseInt(this.getAttribute('data-amount'));
            const modalCustomerPaid = document.getElementById('modalCustomerPaid');
            const customerPaidInput = document.getElementById('customerPaidInput');
            
            if (modalCustomerPaid && customerPaidInput) {
                modalCustomerPaid.textContent = new Intl.NumberFormat('vi-VN').format(amount);
                customerPaidInput.value = amount;
            }
        });
    });
    
    // Xử lý form thanh toán
    if (checkoutForm) {
        checkoutForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            // Chuẩn bị dữ liệu
            const cartItems = document.getElementById('cartItems');
            const cartItemsInput = document.getElementById('cartItemsInput');
            const totalPayableInput = document.getElementById('totalPayableInput');
            const customerPaidInput = document.getElementById('customerPaidInput');
            
            if (!cartItems || !cartItemsInput || !totalPayableInput || !customerPaidInput) {
                console.error('Missing form elements');
                return;
            }
            
            // Kiểm tra giỏ hàng có sản phẩm không
            const items = cartItems.querySelectorAll('li');
            if (items.length === 0) {
                alert('Giỏ hàng trống. Vui lòng thêm sản phẩm trước khi thanh toán.');
                paymentModal.style.display = 'none';
                modalOverlay.style.display = 'none';
                return;
            }
            
            // Tạo dữ liệu giỏ hàng
            const cartData = [];
            items.forEach(function(item) {
                const productId = item.getAttribute('data-product-id');
                const quantity = parseInt(item.querySelector('.quantity-input').value);
                
                cartData.push({
                    productId: productId,
                    quantity: quantity
                });
            });
            
            // Cập nhật input hidden
            cartItemsInput.value = JSON.stringify(cartData);
            
            // Lấy tổng tiền cần trả
            const totalPayable = document.getElementById('modalTotalPayable');
            if (totalPayable) {
                const totalValue = totalPayable.textContent.replace(/[^\d]/g, '');
                totalPayableInput.value = totalValue;
            }
            
            // Kiểm tra khách đã thanh toán chưa
            const customerPaid = document.getElementById('modalCustomerPaid');
            if (customerPaid) {
                const paidValue = customerPaid.textContent.replace(/[^\d]/g, '');
                if (parseInt(paidValue) === 0) {
                    alert('Vui lòng nhập số tiền khách thanh toán.');
                    return;
                }
            }
            
            // Submit form
            this.submit();
        });
    }
}

// Khởi tạo sự kiện cho các sản phẩm trong grid
function initializeProductGrid() {
    // Xóa tất cả các event listener hiện có bằng cách sao chép và thay thế
    const productGridContainer = document.querySelector('.product-grid');
    if (!productGridContainer) return;
    
    const productCards = document.querySelectorAll('.product-card');
    
    productCards.forEach(function(card) {
        const newCard = card.cloneNode(true);
        card.parentNode.replaceChild(newCard, card);
        
        // Thêm event listener cho card mới
        newCard.addEventListener('mousedown', function handleProductClick(e) {
            // Ngăn chặn sự kiện lan truyền
            e.stopPropagation();
            e.preventDefault();
            
            // Tạm thời loại bỏ sự kiện để tránh gọi nhiều lần
            newCard.removeEventListener('mousedown', handleProductClick);
            
            const productId = this.getAttribute('data-product-id');
            const productName = this.getAttribute('data-product-name');
            const productPrice = this.getAttribute('data-product-price');
            const productStockQuantity = this.getAttribute('data-product-stockquantity');
            const productCode = this.getAttribute('data-product-code');
            const productImage = this.querySelector('img')?.src || '';
            
            console.log(`[${new Date().toISOString()}] Grid product clicked:`, {
                id: productId,
                productName: productName,
                price: productPrice,
                stockQuantity: productStockQuantity
            });
            
            // Chuyển đổi giá trị sang số
            const price = parseInt(productPrice, 10);
            const stockQuantity = parseInt(productStockQuantity, 10);
            
            const product = {
                id: productId,
                productName: productName,
                price: price,
                stockQuantity: stockQuantity,
                productCode: productCode,
                imageURL: productImage
            };
            
            // Thêm sản phẩm vào giỏ hàng
            addProductToCart(product, e);
            
            // Kích hoạt lại sự kiện sau 1 giây
            setTimeout(() => {
                newCard.addEventListener('mousedown', handleProductClick);
            }, 1000);
        });
    });
}

// Khởi tạo tất cả các chức năng khi trang được tải
document.addEventListener('DOMContentLoaded', function() {
    console.log(`[${new Date().toISOString()}] Page loaded, initializing functions`);
    
    // Đảm bảo chỉ chạy một lần
    if (window.functionsInitialized) {
        console.log('Functions already initialized, skipping');
        return;
    }
    
    window.functionsInitialized = true;
    
    initializeProductSearch();
    initializeProductGrid();
    
    // Thêm các chức năng khác nếu cần
    console.log(`[${new Date().toISOString()}] All functions initialized`);
});