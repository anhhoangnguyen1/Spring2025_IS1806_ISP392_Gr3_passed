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
    var profileImg = document.getElementById("profile-img");
    var profileMenu = document.querySelector(".profile-menu");

    profileImg.addEventListener("click", function (event) {
        event.stopPropagation(); // Ngăn chặn sự kiện lan ra ngoài
        profileMenu.style.display =
                profileMenu.style.display === "block" ? "none" : "block";
    });

    document.addEventListener("click", function (event) {
        if (
                !profileImg.contains(event.target) &&
                !profileMenu.contains(event.target)
                ) {
            profileMenu.style.display = "none";
        }
    });
});
document.addEventListener("DOMContentLoaded", function () {
    const resizableHeaders = document.querySelectorAll(".resizable");
    let isResizing = false;
    let currentHeader, startX, startWidth;

    resizableHeaders.forEach(header => {
        header.addEventListener("mousedown", function (event) {
            if (event.offsetX > header.clientWidth - 5) {
                isResizing = true;
                currentHeader = header;
                startX = event.pageX;
                startWidth = header.clientWidth;
                document.addEventListener("mousemove", resizeColumn);
                document.addEventListener("mouseup", stopResize);
            }
        });
    });

    function resizeColumn(event) {
        if (isResizing && currentHeader) {
            let newWidth = startWidth + (event.pageX - startX);
            currentHeader.style.width = newWidth + "px";
        }
    }

    function stopResize() {
        isResizing = false;
        document.removeEventListener("mousemove", resizeColumn);
        document.removeEventListener("mouseup", stopResize);
    }
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


const dropArea = document.getElementById("dropArea");
const fileInput = document.getElementById("file");

// Tìm kiếm trực quan
$(document).ready(function () {
    var debounceTimeout;
    $("#myInput").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        clearTimeout(debounceTimeout);
        debounceTimeout = setTimeout(function () {
            $("#myTable tbody tr").each(function () {
                var rowText = $(this).text().toLowerCase();
                $(this).toggle(rowText.indexOf(value) > -1);
            });
        }, 200); // Giảm debounce để phản hồi nhanh hơn
    });
});


function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}

document.addEventListener("DOMContentLoaded", function () {
    const searchBox = document.getElementById("searchBox");
    const optionsContainer = document.getElementById("optionsContainer");
    const selectedOption = document.getElementById("selectedOption");
    const options = document.querySelectorAll(".option");




    // Select option and update the selected option text
    options.forEach(option => {
        option.addEventListener("click", function () {
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
document.addEventListener("DOMContentLoaded", function () {
    const headers = document.querySelectorAll(".resizable");

    headers.forEach(header => {
        if (!header.classList.contains("sticky-col")) { // Bỏ qua cột "Actions"
            const resizer = document.createElement("div");
            resizer.classList.add("resizer");
            header.appendChild(resizer);
            resizer.addEventListener("mousedown", initResize);
        }
    });

    function initResize(e) {
        const header = e.target.parentElement;
        const startX = e.clientX;
        const startWidth = header.offsetWidth;

        function onMouseMove(event) {
            const newWidth = startWidth + (event.clientX - startX);
            header.style.width = `${newWidth}px`;
        }

        function onMouseUp() {
            document.removeEventListener("mousemove", onMouseMove);
            document.removeEventListener("mouseup", onMouseUp);
        }

        document.addEventListener("mousemove", onMouseMove);
        document.addEventListener("mouseup", onMouseUp);
    }
});


function sortTable(columnIndex) {
    var table = document.getElementById("myTable");
    var tbody = table.getElementsByTagName("tbody")[0];
    var rows = Array.from(tbody.rows);

    // Lấy trạng thái sắp xếp hiện tại
    var dir = table.getAttribute("data-sort-dir-" + columnIndex) === "asc" ? "desc" : "asc";

    rows.sort(function (rowA, rowB) {
        var cellA = rowA.cells[columnIndex]?.textContent.trim() || "";
        var cellB = rowB.cells[columnIndex]?.textContent.trim() || "";

        // Kiểm tra nếu là số
        var numA = parseFloat(cellA.replace(/,/g, ""));
        var numB = parseFloat(cellB.replace(/,/g, ""));
        if (!isNaN(numA) && !isNaN(numB)) {
            return dir === "asc" ? numA - numB : numB - numA;
        }

        // Kiểm tra nếu là ngày (YYYY-MM-DD hoặc DD/MM/YYYY)
        var dateA = new Date(cellA);
        var dateB = new Date(cellB);
        if (!isNaN(dateA) && !isNaN(dateB)) {
            return dir === "asc" ? dateA - dateB : dateB - dateA;
        }

        // Sắp xếp chuỗi
        return dir === "asc" ? cellA.localeCompare(cellB) : cellB.localeCompare(cellA);
    });

    // Xóa tbody cũ và thêm hàng đã sắp xếp
    tbody.innerHTML = "";
    rows.forEach(row => tbody.appendChild(row));

    // Cập nhật trạng thái sắp xếp
    table.setAttribute("data-sort-dir-" + columnIndex, dir);
}


function sortTable1(customerId, columnIndex) {
    var table = document.getElementById(`myTable1-${customerId}`);
    if (!table)
        return;

    var tbody = table.querySelector("tbody");
    var rows = Array.from(tbody.querySelectorAll("tr"));

    var isAscending = table.getAttribute("data-sort-dir") !== "asc";

    rows.sort((rowA, rowB) => {
        var cellA = rowA.cells[columnIndex]?.innerText.trim() || "";
        var cellB = rowB.cells[columnIndex]?.innerText.trim() || "";

        // Xử lý giá trị số
        var numA = parseFloat(cellA.replace(/,/g, ""));
        var numB = parseFloat(cellB.replace(/,/g, ""));

        // Nếu cả hai đều là số, sắp xếp theo số
        if (!isNaN(numA) && !isNaN(numB)) {
            return isAscending ? numA - numB : numB - numA;
        }

        // Nếu một trong hai giá trị rỗng (dòng trống), luôn đặt dòng trống ở cuối
        if (!cellA && !cellB)
            return 0; // Cả hai đều rỗng => không đổi vị trí
        if (!cellA)
            return 1; // Dòng trống xuống cuối
        if (!cellB)
            return -1; // Dòng trống xuống cuối

        // Nếu không phải số, so sánh như chuỗi
        return isAscending ? cellA.localeCompare(cellB) : cellB.localeCompare(cellA);
    });

    rows.forEach(row => tbody.appendChild(row));
    table.setAttribute("data-sort-dir", isAscending ? "asc" : "desc");
}


// Get the modal
var modal = document.getElementById("myModal");

// Get all images with class "myImg"
var imgs = document.getElementsByClassName("myImg");
var modalImg = document.getElementById("img01");
var captionText = document.getElementById("caption");

// Loop through all images and add event listener to each
document.querySelectorAll(".myImg").forEach(img => {
    img.addEventListener("click", function () {
        modal.style.display = "block";
        modalImg.src = this.src;
        captionText.innerHTML = this.alt;
    });
});


// Get the <span> element that closes the modal
document.querySelectorAll(".close").forEach(closeBtn => {
    closeBtn.addEventListener("click", function () {
        modal.style.display = "none";
    });
});


document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".openDebtModal").forEach(button => {
        button.addEventListener("click", function () {
            // Lấy dữ liệu từ data-attribute của nút được click
            const debtId = this.getAttribute("data-id");
            const amount = this.getAttribute("data-amount");
            const createdAt = this.getAttribute("data-createdat");
            const description = this.getAttribute("data-description");
            const status = this.getAttribute("data-status");
            const image = this.getAttribute("data-image");

            // Gán dữ liệu vào input readonly
            document.getElementById("modalDebtId").textContent = debtId;
            document.getElementById("modalDebtAmount").value = amount;
            document.getElementById("modalDebtCreatedAt").value = createdAt;
            document.getElementById("modalDebtDescription").value = description;
            document.getElementById("modalDebtStatus").value = status;
            document.getElementById("modalDebtImage").src = image;

            // Hiển thị modal
            $("#debtDetailModal").modal("show");
        });
    });
});
var modal = document.getElementById("myModal");

// Get all images with class "myImg"
var imgs = document.getElementsByClassName("myImg");
var modalImg = document.getElementById("img01");
var captionText = document.getElementById("caption");

// Loop through all images and add event listener to each
document.querySelectorAll(".myImg").forEach(img => {
    img.addEventListener("click", function () {
        modal.style.display = "block";
        modalImg.src = this.src;
        captionText.innerHTML = this.alt;
    });
});


// Get the <span> element that closes the modal
document.querySelectorAll(".close").forEach(closeBtn => {
    closeBtn.addEventListener("click", function () {
        modal.style.display = "none";
    });
});
// Function to format numbers with commas as thousand separators
// Function to format numbers with commas as thousand separators
function formatNumber(event) {
    var input = event.target;
    var value = input.value.replace(/\D/g, ''); // Remove non-digit characters

    // Format the number as 1,000,000
    value = value.replace(/\B(?=(\d{3})+(?!\d))/g, '.');

    // Update the input field with the formatted value
    input.value = value;
}

// Function to clean the formatted number before submitting to servlet
function cleanInputBeforeSubmit(event) {
    var input = event.target;
    // Remove the thousand separators before sending the value
    input.value = input.value.replace(/\./g, '');
}

