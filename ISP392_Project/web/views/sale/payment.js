/**
 * JavaScript cho trang thanh toán
 */
document.addEventListener('DOMContentLoaded', function() {
    console.log("==== PAYMENT JS INITIALIZED ====");
    
    const checkoutForm = document.getElementById('checkoutForm');
    const submitBtn = document.querySelector('button[type="submit"]');
    const customerPaidInput = document.getElementById('customerPaidInput');
    const paymentMethodInput = document.getElementById('paymentMethodInput');
    const cartItemsInput = document.getElementById('cartItemsInput');
    const totalInput = document.getElementById('totalInput');
    
    console.log("Form elements:", {
        checkoutForm: !!checkoutForm,
        submitBtn: !!submitBtn,
        customerPaidInput: !!customerPaidInput,
        paymentMethodInput: !!paymentMethodInput,
        cartItemsInput: !!cartItemsInput,
        totalInput: !!totalInput
    });
    
    // Lấy tổng tiền từ phần tử HTML
    const totalAmountElement = document.getElementById('totalAmountValue') || document.getElementById('totalInput');
    const totalAmount = totalAmountElement ? parseFloat(totalAmountElement.value) || 0 : 0;
    console.log("Total amount:", totalAmount);
    
    // Khởi tạo giỏ hàng từ dữ liệu JSON
    try {
        // Kiểm tra dữ liệu JSON từ biến toàn cục
        if (typeof cartItemsJsonData !== 'undefined' && cartItemsJsonData.trim()) {
            // Làm sạch chuỗi JSON (loại bỏ khoảng trắng thừa, dấu xuống dòng)
            let cleanedJson = cartItemsJsonData.trim();
            console.log("Raw cart JSON data:", cleanedJson);
            
            // Kiểm tra và xử lý JSON
            try {
                // Thử parse để kiểm tra nếu JSON hợp lệ
                JSON.parse(cleanedJson);
                cartItemsInput.value = cleanedJson;
                console.log("Successfully loaded cart items from JSON data");
            } catch (jsonError) {
                console.error("Invalid JSON format, trying to clean up:", jsonError);
                // Nếu JSON không hợp lệ, thử xử lý
                cleanedJson = cleanedJson.replace(/[\r\n\t]/g, '').trim();
                // Loại bỏ các ký tự đặc biệt ở đầu và cuối
                cleanedJson = cleanedJson.replace(/^[^[{]*/, '').replace(/[^}\]]*$/, '');
                
                try {
                    JSON.parse(cleanedJson);
                    cartItemsInput.value = cleanedJson;
                    console.log("Successfully cleaned and loaded JSON");
                } catch (e) {
                    console.error("Still invalid JSON after cleanup:", e);
                    // Tạo JSON từ bảng
                    createCartItemsFromTable();
                }
            }
        } else {
            console.log("No JSON data available, creating from table");
            // Nếu không có dữ liệu JSON, tạo từ bảng sản phẩm
            createCartItemsFromTable();
        }
    } catch (e) {
        console.error("Error initializing cart items:", e);
        // Thử tạo từ bảng nếu có lỗi
        createCartItemsFromTable();
    }
    
    // Hàm tạo cartItems từ bảng HTML
    function createCartItemsFromTable() {
        const cartItems = [];
        const cartItemRows = document.querySelectorAll('table tbody tr');
        
        console.log("Found " + cartItemRows.length + " rows in product table");
        
        cartItemRows.forEach(function(row, index) {
            // Lấy dữ liệu từ thuộc tính data
            const productId = row.getAttribute('data-product-id');
            const quantity = parseInt(row.getAttribute('data-quantity'));
            const price = parseFloat(row.getAttribute('data-price'));
            const productName = row.querySelector('.product-name').textContent.trim();
            
            console.log(`Row ${index + 1}: ID=${productId}, Name=${productName}, Price=${price}, Qty=${quantity}`);
            
            // Tính tổng tiền
            const total = price * quantity;
            
            if (productId && productName && !isNaN(price) && !isNaN(quantity)) {
                cartItems.push({
                    productId: productId,
                    productName: productName,
                    price: price,
                    quantity: quantity,
                    total: total
                });
            } else {
                console.error(`Invalid data in row ${index + 1}: Missing required properties`);
            }
        });
        
        if (cartItems.length > 0) {
            const cartItemsJson = JSON.stringify(cartItems);
            cartItemsInput.value = cartItemsJson;
            console.log("Created cart items from table:", cartItemsJson);
        } else {
            console.error("No cart items found in table");
        }
    }

    // Xử lý radio buttons phương thức thanh toán
    const paymentMethodRadios = document.querySelectorAll('input[name="paymentMethod"]');
    paymentMethodRadios.forEach(function(radio) {
        radio.addEventListener('change', function() {
            paymentMethodInput.value = this.value;
            console.log("Payment method changed to:", this.value);
        });
    });

    // Tự động đặt số tiền thanh toán bằng tổng tiền
    const modalCustomerPaid = document.getElementById('modalCustomerPaid');
    if (modalCustomerPaid) {
        modalCustomerPaid.textContent = formatCurrency(totalAmount);
    }
    customerPaidInput.value = totalAmount;
    console.log("Customer paid amount set to:", totalAmount);

    // Xử lý submit form
    if (checkoutForm) {
        checkoutForm.addEventListener('submit', function(e) {
            console.log("Form submission started");
            
            // Kiểm tra xem cartItems có dữ liệu không
            if (!cartItemsInput.value || cartItemsInput.value.trim() === '') {
                e.preventDefault();
                alert('Không có thông tin giỏ hàng. Vui lòng thử lại.');
                console.error('Cart items data is empty, preventing submission');
                return;
            }
            
            // Kiểm tra dữ liệu giỏ hàng có hợp lệ không
            try {
                const cartItems = JSON.parse(cartItemsInput.value);
                if (!Array.isArray(cartItems) || cartItems.length === 0) {
                    e.preventDefault();
                    alert('Dữ liệu giỏ hàng không hợp lệ. Vui lòng thử lại.');
                    console.error('Cart items is not a valid array or is empty');
                    return;
                }
                
                // Kiểm tra xem mỗi mục có đủ thông tin cần thiết không
                const hasInvalidItems = cartItems.some(item => 
                    !item.productId || !item.productName || isNaN(item.price) || isNaN(item.quantity)
                );
                
                if (hasInvalidItems) {
                    e.preventDefault();
                    alert('Một số sản phẩm trong giỏ hàng thiếu thông tin. Vui lòng thử lại.');
                    console.error('Some cart items are missing required properties');
                    return;
                }
                
                // Cập nhật lại các giá trị input trước khi submit
                totalInput.value = totalAmount;
                customerPaidInput.value = totalAmount;
                
                console.log('Submitting form with valid cart items:', cartItemsInput.value);
                console.log('Total:', totalInput.value);
                console.log('Customer paid:', customerPaidInput.value);
                console.log('Payment method:', paymentMethodInput.value);
            } catch (e) {
                e.preventDefault();
                alert('Lỗi xử lý dữ liệu giỏ hàng. Vui lòng thử lại.');
                console.error('Error parsing cart items JSON:', e);
                return;
            }
        });
    }

    // Format currency function
    function formatCurrency(amount) {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(amount);
    }
    
    // Debug logs
    console.log('Payment form initialized:', {
        totalAmount: totalAmount,
        formAction: checkoutForm ? checkoutForm.action : 'N/A',
        submitButtonExists: !!submitBtn
    });
}); 