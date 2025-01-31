CREATE SCHEMA ISP392_Project;
USE ISP392_Project;

-- Table Role
CREATE TABLE Role (
    role_id INT PRIMARY KEY,
    name VARCHAR(10) NOT NULL
);

-- Table Users
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    role_id INT NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL UNIQUE,
    FOREIGN KEY (role_id) REFERENCES Role(role_id) ON DELETE CASCADE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    created_by VARCHAR(255),
    isDelete TINYINT(1) DEFAULT 0,
    deletedBy VARCHAR(255),
    deletedAt DATETIME,
    status VARCHAR(255)
);

-- Table Products
CREATE TABLE Products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    image VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    wholesale_price DECIMAL(10, 2) NOT NULL,
    retail_price DECIMAL(10, 2) NOT NULL,
    weight INT NOT NULL,
    location VARCHAR(255),
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    isDelete TINYINT(1) DEFAULT 0,
    deletedAt DATETIME,
    status VARCHAR(255)
);

-- Table Zone
CREATE TABLE Zone (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    remain_capacity INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    isDelete TINYINT(1) DEFAULT 0,
    deletedAt DATETIME,
    status VARCHAR(255)
);

-- Table Product_Zone
CREATE TABLE Product_Zone (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    zone_id INT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE,
    FOREIGN KEY (zone_id) REFERENCES Zone(id) ON DELETE CASCADE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    isDelete TINYINT(1) DEFAULT 0,
    deletedAt DATETIME,
    status VARCHAR(255)
);

-- Table Customers
CREATE TABLE Customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type ENUM('wholesale', 'retail') NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    address TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    created_by VARCHAR(255),
    isDelete TINYINT(1) DEFAULT 0,
    deletedBy VARCHAR(255),
    deletedAt DATETIME,
    status VARCHAR(255)
);

-- Table Staffs
CREATE TABLE Staffs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    address TEXT NOT NULL,
    gender VARCHAR(15) NOT NULL,
    dob DATE,
    users_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status VARCHAR(255),
    deletedAt DATETIME,
    FOREIGN KEY (users_id) REFERENCES Users(user_id)
);

-- Table Invoice
CREATE TABLE Invoice (
    id INT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    payment DECIMAL(10, 2) NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    customers_id INT,
    staff_id INT,
    FOREIGN KEY (staff_id) REFERENCES Staffs(id),
    FOREIGN KEY (customers_id) REFERENCES Customers(id)
);

-- Table Import_Export_Product
CREATE TABLE Import_Export_Product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type ENUM('import', 'export') NOT NULL,
    transaction_date DATETIME NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    invoice_id INT,
    FOREIGN KEY (invoice_id) REFERENCES Invoice(id),
    note TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    isDelete TINYINT(1) DEFAULT 0,
    deletedBy VARCHAR(255),
    deletedAt DATETIME,
    status VARCHAR(255)
);

-- Table Number_of_bags
CREATE TABLE Number_of_bags (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type ENUM('yes', 'no') NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    import_export_id INT,
    FOREIGN KEY (import_export_id) REFERENCES Import_Export_Product(id),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    note TEXT
);

-- Table Payment_history
CREATE TABLE Payment_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    note TEXT,
    invoice_id INT,
    total_invoice DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (invoice_id) REFERENCES Invoice(id),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status VARCHAR(255)
);

-- Table Debt
CREATE TABLE Debt (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type ENUM('debt', 'repay') NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_history_id INT,
    import_export_id INT,
    customers_id INT,
    FOREIGN KEY (customers_id) REFERENCES Customers(id),
    FOREIGN KEY (payment_history_id) REFERENCES Payment_history(id),
    FOREIGN KEY (import_export_id) REFERENCES Import_Export_Product(id),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    status VARCHAR(255)
);

-- Table Invoice_details
CREATE TABLE Invoice_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (invoice_id) REFERENCES Invoice(id),
    FOREIGN KEY (product_id) REFERENCES Products(product_id)
);

-- Table Import_Export_details
CREATE TABLE Import_Export_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    import_export_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (import_export_id) REFERENCES Import_Export_Product(id),
    FOREIGN KEY (product_id) REFERENCES Products(product_id)
);
