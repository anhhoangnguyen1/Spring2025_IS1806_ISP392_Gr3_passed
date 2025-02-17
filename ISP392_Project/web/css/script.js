

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


function sortTable(n) {
  var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
  table = document.getElementById("myTable");
  switching = true;
  dir = "asc"; 

  while (switching) {
    switching = false;
    rows = table.rows;
    
    for (i = 1; i < (rows.length - 1); i++) {
      shouldSwitch = false;
      
      x = rows[i].getElementsByTagName("TD")[n];
      y = rows[i + 1].getElementsByTagName("TD")[n];

      // Xử lý giá trị trống thành "\uFFFF" để luôn ở cuối khi sắp xếp
      let xVal = x.innerHTML.trim() || "\uFFFF";
      let yVal = y.innerHTML.trim() || "\uFFFF";

      if (dir == "asc") {
        if (xVal > yVal) {
          shouldSwitch = true;
          break;
        }
      } else if (dir == "desc") {
        if (xVal < yVal) {
          shouldSwitch = true;
          break;
        }
      }
    }

    if (shouldSwitch) {
      rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
      switching = true;
      switchcount++;      
    } else {
      if (switchcount == 0 && dir == "asc") {
        dir = "desc";
        switching = true;
      }
    }
  }

  // Gọi lại hàm tìm kiếm sau khi sắp xếp
  searchTable();
}


function sortTable1(customerId, columnIndex) {
    var table = document.getElementById(`myTable1-${customerId}`);
    if (!table) return;

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
        if (!cellA && !cellB) return 0; // Cả hai đều rỗng => không đổi vị trí
        if (!cellA) return 1; // Dòng trống xuống cuối
        if (!cellB) return -1; // Dòng trống xuống cuối

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
            const type = this.getAttribute("data-type");
            const createdAt = this.getAttribute("data-createdat");
            const description = this.getAttribute("data-description");
            const status = this.getAttribute("data-status");
            const image = this.getAttribute("data-image");

            // Gán dữ liệu vào input readonly
            document.getElementById("modalDebtId").textContent = debtId;
            document.getElementById("modalDebtAmount").value = amount;
            document.getElementById("modalDebtType").value = type;
            document.getElementById("modalDebtCreatedAt").value = createdAt;
            document.getElementById("modalDebtDescription").value = description;
            document.getElementById("modalDebtStatus").value = status;
            document.getElementById("modalDebtImage").src = image;

            // Hiển thị modal
            $("#debtDetailModal").modal("show");
        });
    });
});

