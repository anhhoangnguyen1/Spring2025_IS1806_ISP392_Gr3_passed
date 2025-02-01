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
