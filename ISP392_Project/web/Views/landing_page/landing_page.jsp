<%-- 
    Document   : landing_page
    Created on : Feb 5, 2025, 1:06:21 AM
    Author     : bsd12418
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <link
            href="https://cdn.jsdelivr.net/npm/remixicon@4.1.0/fonts/remixicon.css"
            rel="stylesheet"
            />
        <link
            rel="stylesheet"
            href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css"
            />
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" href="styles.css" />
        <title>Rice Retail Management</title>
    </head>
    <body>
        <header class="header">
            <nav>
                <div class="nav__header">
                    <div class="nav__logo">
                        <a href="#">Rice<span>Vietnam</span></a>
                    </div>
                    <div class="nav__menu__btn" id="menu-btn">
                        <span><i class="ri-menu-line"></i></span>
                    </div>
                </div>
                <ul class="nav__links" id="nav-links">
                    <li><a href="#home">Home</a></li>
                    <li><a href="#about">About</a></li>
                    <li><a href="#faq">FAQ</a></li>
                </ul>
                <div class="nav__btns">
                    <a href="/ISP392_Project/views/loginServlet">
                        <button class="btn">Sign In</button>
                    </a>
                </div>
            </nav>

            <div class="section__container header__container" id="home">
                <p>RICE BRAND</p>
                <h1>Efficient Rice Retail Management System</h1>
                <div class="header__flex">
                    <div class="header__card">
                        <span><i class="ri-store-2-fill"></i></span>
                        <div>
                            <h5>Suppliers</h5>
                            <h4>Quality</h4>
                        </div>
                    </div>
                    <div class="header__card">
                        <span><i class="ri-building-fill"></i></span>
                        <div>
                            <h5>System</h5>
                            <h4>Warehouse Management</h4>
                        </div>
                    </div>
                    <div class="header__card">
                        <span><i class="ri-stackshare-line"></i></span>
                        <div>
                            <h5>Delivery</h5>
                            <h4>Fast & Reliable</h4>
                        </div>
                    </div>
                </div>
            </div>
        </header>
        <button onclick="topFunction()" id="myBtn" title="Go to top"><i class="fa-solid fa-arrow-up"></i></button>
        <section class="section__container about__container" id="about">
            <div class="about__header">
                <div>
                    <h3 class="section__subheader">About Us</h3>
                    <h2 class="section__header">Trusted Rice Retail Management System</h2>
                </div>
                <p class="section__description">
                    We provide an optimized solution to help rice retailers efficiently
                    manage inventory, orders, and transactions with suppliers.
                </p>
            </div>
            <div class="about__grid">
                <div class="about__card">
                    <p>QUALITY</p>
                    <h4>Clean and Safe Rice</h4>
                </div>
                <div class="about__card">
                    <p>MANAGEMENT</p>
                    <h4>Smart System</h4>
                </div>
                <div class="about__card">
                    <p>TRANSPORTATION</p>
                    <h4>Fast Delivery</h4>
                </div>
            </div>
        </section>

        <a href="/ISP392_Project/views/loginServlet">
            <button class="subscribe-btn">Sign In</button>
        </a>

        <section class="section__container faq__container" id="faq">
            <div class="faq__image">
                <img src="images/faq.jpg" alt="faq" />
            </div>
            <div class="faq__content">
                <h3 class="section__subheader">Customer Questions</h3>
                <h2 class="section__header">Frequently Asked Questions</h2>
                <p class="section__description">
                    Get answers to common questions about our products, services, and
                    ordering process to ensure a smooth shopping experience.
                </p>
                <div class="faq__grid">
                    <div class="faq__card">
                        <div class="faq__header">
                            <h4>What types of rice does the store offer?</h4>
                            <span><i class="ri-arrow-down-s-line"></i></span>
                        </div>
                        <div class="faq__description">
                            We provide various types of rice such as ST25, Jasmine rice, brown
                            rice, sticky rice, and many others to meet diverse customer needs.
                        </div>
                    </div>
                    <div class="faq__card">
                        <div class="faq__header">
                            <h4>How can I place an order?</h4>
                            <span><i class="ri-arrow-down-s-line"></i></span>
                        </div>
                        <div class="faq__description">
                            Customers can order directly on our website by selecting products,
                            adding them to the cart, and completing the payment. We also
                            support orders via phone and Zalo.
                        </div>
                    </div>
                    <div class="faq__card">
                        <div class="faq__header">
                            <h4>How long does delivery take?</h4>
                            <span><i class="ri-arrow-down-s-line"></i></span>
                        </div>
                        <div class="faq__description">
                            Delivery usually takes 1-3 business days depending on your
                            location. We offer same-day delivery for orders within the city.
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <section class="banner">
            <div class="section__container banner__container">
                <div class="banner__content">
                    <h3 class="section__subheader">Join Us</h3>
                    <h2 class="section__header">Stay Updated!</h2>
                    <p class="section__description">
                        From technological innovations to market updates, our curated
                        content keeps you informed.
                    </p>
                </div>
                <div class="banner__form">
                    <a href="/ISP392_Project/views/loginServlet">
                        <button class="subscribe-btn">Sign In</button>
                    </a>
                </div>
            </div>
        </section>

        <footer>
            <div class="footer__bar">
                Copyright © 2024 Rice Retail Management. All rights reserved.
            </div>
        </footer>


        <script src="https://unpkg.com/scrollreveal"></script>
        <script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>
        <script src="main.js"></script>
    </body>
</html>

