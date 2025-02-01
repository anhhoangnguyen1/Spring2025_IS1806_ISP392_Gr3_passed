// Script File
var hamburgerBtn = document.querySelector(".hamburger-btn");
var sideBar = document.querySelector(".side-bar");

hamburgerBtn.addEventListener("click", sidebarToggle);
function sidebarToggle() {
  sideBar.classList.toggle("active");
}

// Code For Light/Dark Mode Toggle
var modeSwitcher = document.querySelector(".mode-switch i");
var body = document.querySelector("body");
modeSwitcher.addEventListener("click", modeSwitch);
function modeSwitch() {
  body.classList.toggle("active");
}

// Set default mode to light mode (white background) on page load
window.addEventListener("DOMContentLoaded", function () {
  if (!body.classList.contains("active")) {
    body.classList.add("active");
  }
});

// Set menu profile
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


// Set Notification
function showNotification(message, type = "success", duration = 3000) {
    let container = document.querySelector(".notification-container");

    if (!container) {
        container = document.createElement("div");
        container.className = "notification-container";
        document.body.appendChild(container);
    }

    let notification = document.createElement("div");
    notification.className = `notification ${type} show`;
    notification.innerHTML = `
        <span>${message}</span>
        <span class="close-btn" onclick="closeNotification(this)">&times;</span>
        <div class="progress-bar">
            <div class="progress"></div>
        </div>
    `;

    container.appendChild(notification);

    // Thanh tiến trình chạy
    setTimeout(() => notification.querySelector(".progress").style.width = "0%", 10);

    // Đóng thông báo sau thời gian duration
    setTimeout(() => closeNotification(notification), duration);
}

// Hàm đóng thông báo
function closeNotification(element) {
    let notification = element.closest(".notification");
    if (notification) {
        notification.classList.add("hide");
        setTimeout(() => notification.remove(), 500);
    }
}

