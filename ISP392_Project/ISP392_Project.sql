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


-- Insert into Role
INSERT INTO Role (role_id, name) VALUES
(1, 'Admin'),
(2, 'Staff'),
(3, 'Customer'),
(4, 'Manager'),
(5, 'Operator');

-- Insert into Users
INSERT INTO Users (role_id, username, password, email, phone, created_by) VALUES
(1, 'admin', 'password123', 'admin@example.com', '0123456789', 'System'),
(2, 'staff1', 'password123', 'staff1@example.com', '0987654321', 'System'),
(3, 'customer1', 'password123', 'customer1@example.com', '0912345678', 'System'),
(2, 'staff2', 'password123', 'staff2@example.com', '0911111111', 'System'),
(3, 'customer2', 'password123', 'customer2@example.com', '0922222222', 'System');

-- Insert into Products
INSERT INTO Products (name, image, price, wholesale_price, retail_price, weight, location, description) VALUES
('Product A', 'image_a.jpg', 10.00, 8.00, 12.00, 500, 'A1', 'Description A'),
('Product B', 'image_b.jpg', 20.00, 15.00, 25.00, 1000, 'B2', 'Description B'),
('Product C', 'image_c.jpg', 30.00, 25.00, 35.00, 200, 'C3', 'Description C'),
('Product D', 'image_d.jpg', 40.00, 30.00, 45.00, 300, 'D4', 'Description D');

-- Insert into Zone
INSERT INTO Zone (name, capacity, remain_capacity) VALUES
('Zone 1', 100, 80),
('Zone 2', 50, 40),
('Zone 3', 200, 150);

-- Insert into Product_Zone
INSERT INTO Product_Zone (product_id, zone_id, quantity) VALUES
(1, 1, 10),
(2, 2, 5),
(3, 3, 20),
(4, 1, 15);

-- Insert into Customers
INSERT INTO Customers (type, name, phone, address, created_by) VALUES
('wholesale', 'Customer A', '0900000001', 'Address A', 'System'),
('retail', 'Customer B', '0900000002', 'Address B', 'System'),
('wholesale', 'Customer C', '0900000003', 'Address C', 'System'),
('retail', 'Customer D', '0900000004', 'Address D', 'System');

-- Insert into Staffs
INSERT INTO Staffs (name, phone, address, gender, dob, users_id) VALUES
('Staff A', '0900000003', 'Address C', 'Male', '1990-01-01', 2),
('Staff B', '0900000004', 'Address D', 'Female', '1995-02-02', 4);

-- Insert into Invoice
INSERT INTO Invoice (quantity, price, payment, total, customers_id, staff_id) VALUES
(2, 20.00, 40.00, 40.00, 1, 1),
(5, 50.00, 250.00, 250.00, 2, 2);

-- Insert into Import_Export_Product
INSERT INTO Import_Export_Product (type, transaction_date, quantity, price, invoice_id, note) VALUES
('import', '2025-02-04 10:00:00', 5, 50.00, 1, 'Imported items'),
('export', '2025-02-05 12:30:00', 3, 30.00, 2, 'Exported items');

-- Insert into Number_of_bags
INSERT INTO Number_of_bags (type, quantity, price, import_export_id, created_by) VALUES
('yes', 1, 5.00, 1, 'System'),
('no', 2, 10.00, 2, 'System');

-- Insert into Payment_history
INSERT INTO Payment_history (note, invoice_id, total_invoice) VALUES
('Payment made', 1, 40.00),
('Second payment', 2, 250.00);

-- Insert into Debt
INSERT INTO Debt (type, amount, payment_history_id, import_export_id, customers_id, created_by) VALUES
('debt', 10.00, 1, 1, 1, 'System'),
('repay', 20.00, 2, 2, 2, 'System');

-- Insert into Invoice_details
INSERT INTO Invoice_details (invoice_id, product_id, quantity, price) VALUES
(1, 1, 2, 20.00),
(2, 2, 5, 50.00);

-- Insert into Import_Export_details
INSERT INTO Import_Export_details (import_export_id, product_id, quantity, price) VALUES
(1, 2, 3, 60.00),
(2, 3, 4, 80.00);
