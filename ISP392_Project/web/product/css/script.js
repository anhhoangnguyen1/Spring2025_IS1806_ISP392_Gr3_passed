

var hamburgerBtn = document.querySelector(".hamburger-btn");
var sideBar = document.querySelector(".side-bar");
var modeSwitcher = document.querySelector(".mode-switch i");
var body = document.querySelector("body");


hamburgerBtn.addEventListener("click", sidebarToggle);
function sidebarToggle() {
    sideBar.classList.toggle("active");
}


modeSwitcher.addEventListener("click", modeSwitch);
function modeSwitch() {
    body.classList.toggle("active");

    if (body.classList.contains("active")) {
        localStorage.setItem("darkMode", "enabled");
    } else {
        localStorage.setItem("darkMode", "disabled");
    }
}

window.addEventListener("DOMContentLoaded", function () {

    const savedMode = localStorage.getItem("darkMode");


    if (savedMode === "enabled") {
        body.classList.add("active");
    } else {
        body.classList.remove("active");
    }
});
$(document).ready(function () {
    $("table").DataTable({
        scrollX: true,
        colReorder: true,
    });
});
document.addEventListener("DOMContentLoaded", function () {
    const table = document.querySelector(".resizable-table");
    const headers = table.querySelectorAll("th");

    headers.forEach((header, index) => {
        // Create resizer
        const resizer = document.createElement("div");
        resizer.classList.add("resizer");
        resizer.style.height = `${table.offsetHeight}px`;

        // Add resizer to header
        header.style.position = "relative";
        header.appendChild(resizer);

        let startX, startWidth;

        resizer.addEventListener("mousedown", (e) => {
            startX = e.pageX;
            startWidth = header.offsetWidth;

            document.addEventListener("mousemove", resizeColumn);
            document.addEventListener("mouseup", stopResize);
        });

        function resizeColumn(e) {
            const width = startWidth + (e.pageX - startX);
            header.style.width = `${width}px`;

            // Apply width to all cells in this column
            table.querySelectorAll(`tr td:nth-child(${index + 1})`).forEach((cell) => {
                cell.style.width = `${width}px`;
            });
        }

        function stopResize() {
            document.removeEventListener("mousemove", resizeColumn);
            document.removeEventListener("mouseup", stopResize);
        }
    });
});

function doDelete(Id) {
    return confirm(`Are you sure you want to delete this ?`);
}
function confirmDeleteSelected(event) {
    event.preventDefault();

    let confirmDelete = confirm("Do you really want to delete these?");
    if (confirmDelete) {
        deleteSelected();
    }
}

function toggleSelectAll(source) {
    const checkboxes = document.querySelectorAll('.product-checkbox');
    checkboxes.forEach(checkbox => {
        checkbox.checked = source.checked;
    });
}

function deleteSelected() {

    const selectedCheckboxes = document.querySelectorAll('.product-checkbox:checked');

    if (selectedCheckboxes.length === 0) {
        alert('No products selected.');
        return;
    }

    let query = 'Products?service=deleteProduct';
    selectedCheckboxes.forEach((checkbox, index) => {
        query += `&id=${checkbox.value}`;
    });

    window.location.href = query;
}

document.getElementById("toggle-checkbox-btn").addEventListener("click", function () {
    const checkboxColumns = document.querySelectorAll('.checkbox-column');

    if (checkboxColumns.length === 0)
        return;

    const isHidden = checkboxColumns[0].style.display === 'none' || checkboxColumns[0].style.display === '';

    checkboxColumns.forEach(column => {
        column.style.display = isHidden ? 'table-cell' : 'none';
    });
    const icon = this.querySelector("i");
    if (icon) {
        icon.classList.toggle("fa-list-check");
        icon.classList.toggle("fa-eye-slash");
    }
});
//Check box tất cả sản phẩm
function toggleSelectAll(source) {
    const checkboxes = document.querySelectorAll('.product-checkbox');
    checkboxes.forEach(checkbox => {
        checkbox.checked = source.checked;
    });
}
const dropArea = document.getElementById("dropArea");
const fileInput = document.getElementById("file");

// Tìm kiếm trực quan
$(document).ready(function () {
    // Lưu trữ thời gian của sự kiện debounce
    var debounceTimeout;
    $("#myInput").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        // Hủy bỏ sự kiện cũ nếu còn đợi
        clearTimeout(debounceTimeout);
        // Chạy sự kiện sau 500ms khi người dùng ngừng gõ
        debounceTimeout = setTimeout(function () {
            $("#myTable tr").each(function () {
                $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
            });
        }, 500);  // 500ms là thời gian debounce
    });
});

function previewImage(event) {
    var input = event.target;
    var reader = new FileReader();

    reader.onload = function () {
        var imagePreview = document.getElementById("imagePreview");
        imagePreview.src = reader.result; // Cập nhật src của ảnh
    }

    if (input.files && input.files[0]) {
        reader.readAsDataURL(input.files[0]); // Đọc dữ liệu ảnh
    }
}
function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}

document.addEventListener("DOMContentLoaded", function() {
    const searchBox = document.getElementById("searchBox");
    const optionsContainer = document.getElementById("optionsContainer");
    const selectedOption = document.getElementById("selectedOption");
    const options = document.querySelectorAll(".option");

    // Toggle options container visibility
    selectedOption.addEventListener("click", function() {
        optionsContainer.style.display = optionsContainer.style.display === "block" ? "none" : "block";
    });

    // Hide options when clicked outside
    document.addEventListener("click", function(e) {
        if (!e.target.closest(".custom-select")) {
            optionsContainer.style.display = "none";
        }
    });

    // Search functionality
    searchBox.addEventListener("input", function() {
        const searchTerm = searchBox.value.toLowerCase();
        options.forEach(option => {
            const optionText = option.textContent.toLowerCase();
            if (optionText.includes(searchTerm)) {
                option.style.display = "block";
            } else {
                option.style.display = "none";
            }
        });
    });

    // Select option and update the selected option text
    options.forEach(option => {
        option.addEventListener("click", function() {
            selectedOption.textContent = option.textContent;
            optionsContainer.style.display = "none"; // Hide options after selection
            searchBox.value = ""; // Clear the search box after selection
        });
    });
});
    document.addEventListener("DOMContentLoaded", function () {
        const search = document.getElementById("name");
        const optionsContainer = document.getElementById("optionsContainer");

        // Bắt sự kiện khi click vào danh sách khách hàng
        optionsContainer.addEventListener("click", function (event) {
            const clickedOption = event.target.closest(".option");
            if (clickedOption) {
                search.value = clickedOption.getAttribute("data-value"); // Gán giá trị vào input
            }
        });

        // Ngăn auto-submit khi nhấn Enter trong ô tìm kiếm
        search.addEventListener("keydown", function (event) {
            if (event.key === "Enter") {
                event.preventDefault(); 
            }
        });
    });