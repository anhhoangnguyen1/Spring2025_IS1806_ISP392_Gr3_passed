CREATE SCHEMA ISP392_Project2;
USE ISP392_Project2;

-- Table Users
CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    address TEXT NOT NULL,
    gender VARCHAR(15) NOT NULL,
    dob DATE,
    role ENUM('admin', 'staff', 'owner') NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status VARCHAR(255),
    deletedAt DATETIME
);


-- Table Zones
CREATE TABLE Zones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    remain_capacity INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    isDeleted TINYINT(1) DEFAULT 0 CHECK (isDeleted IN (0,1)),
    deletedAt DATETIME,
    status VARCHAR(255)
);

-- Table Products
CREATE TABLE Products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    image VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    zone_id INT,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    isDeleted TINYINT(1) DEFAULT 0 CHECK (isDeleted IN (0,1)),
    deletedAt DATETIME,
    status VARCHAR(255),
    FOREIGN KEY (zone_id) REFERENCES Zones(id)
);

-- Table Customers
CREATE TABLE Customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    address TEXT NOT NULL,
    balance DECIMAL(10,2) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    created_by VARCHAR(255),
    isDeleted TINYINT(1) DEFAULT 0 CHECK (isDeleted IN (0,1)),
    deletedBy VARCHAR(255),
    deletedAt DATETIME,
    status VARCHAR(255)
);

-- Table Invoice
CREATE TABLE Invoice (
    id INT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    name VARCHAR(255) NOT NULL,
    type ENUM('import', 'export') NOT NULL,
    transaction_date DATETIME NOT NULL,
    quantity INT NOT NULL,
    weight INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    payment DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    customers_id INT,
    users_id INT,
    FOREIGN KEY (customers_id) REFERENCES Customers(id),
    FOREIGN KEY (users_id) REFERENCES Users(id)
);

-- Table Invoice detail
CREATE TABLE Invoice_detail (
	id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT,
    product_id INT,
	price DECIMAL(10,2) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    description TEXT,
    FOREIGN KEY (invoice_id) REFERENCES Invoice(id),
    FOREIGN KEY (product_id) REFERENCES Products(id)
);

-- Table Debt note
CREATE TABLE Debt_note (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type ENUM('debt', 'repay') NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    customers_id INT,
    FOREIGN KEY (customers_id) REFERENCES Customers(id) ON DELETE CASCADE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    status VARCHAR(255),
    description TEXT,
    image VARCHAR(255) NOT NULL
);

/*
-- Insert data into Accounts
INSERT INTO Accounts (username, password, email, role, created_by, updated_by, status) VALUES
('admin_user', 'adminpass', 'admin@example.com', 'admin', 'system', 'system', 'active'),
('owner_user', 'ownerpass', 'owner@example.com', 'owner', 'system', 'system', 'active'),
('staff_user1', 'staffpass1', 'staff1@example.com', 'staff', 'system', 'system', 'active'),
('staff_user2', 'staffpass2', 'staff2@example.com', 'staff', 'system', 'system', 'active'),
('staff_user3', 'staffpass3', 'staff3@example.com', 'staff', 'system', 'system', 'active');

-- Insert data into Users
INSERT INTO Users (name, phone, address, gender, dob, account_id, status) VALUES
('Admin Name', '1234567890', '123 Admin St', 'Male', '1980-01-01', 1, 'active'),
('Owner Name', '1112223333', '789 Owner St', 'Male', '1985-04-04', 2, 'active'),
('Staff One', '0987654321', '456 Staff St', 'Female', '1990-02-02', 3, 'active'),
('Staff Two', '2223334444', '999 Staff St', 'Female', '1993-05-05', 4, 'active'),
('Staff Three', '5556667777', '222 Staff St', 'Male', '1995-07-07', 5, 'active');

-- Insert data into Products
INSERT INTO Products (name, image, price, wholesale_price, retail_price, weight, location, description, status) VALUES
('Product A', 'imageA.jpg', 50.00, 45.00, 55.00, 10, 'Shelf A', 'Description A', 'available'),
('Product B', 'imageB.jpg', 30.00, 25.00, 35.00, 5, 'Shelf B', 'Description B', 'available'),
('Product C', 'imageC.jpg', 40.00, 35.00, 45.00, 8, 'Shelf C', 'Description C', 'available'),
('Product D', 'imageD.jpg', 60.00, 55.00, 65.00, 12, 'Shelf D', 'Description D', 'available');

-- Insert data into Zone
INSERT INTO Zone (name, capacity, remain_capacity, status) VALUES
('Zone 1', 100, 80, 'active'),
('Zone 2', 200, 150, 'active'),
('Zone 3', 300, 250, 'active');

-- Insert data into Product_Zone
INSERT INTO Product_Zone (product_id, zone_id, quantity, status) VALUES
(1, 1, 20, 'available'),
(2, 2, 30, 'available'),
(3, 3, 50, 'available'),
(4, 1, 10, 'available');

-- Insert data into Customers
INSERT INTO Customers (type, name, phone, address, created_by, updated_by, status) VALUES
('wholesale', 'Wholesale Customer', '4445556666', 'Wholesale Address', 'admin', 'admin', 'active'),
('retail', 'Retail Customer', '7778889999', 'Retail Address', 'staff', 'staff', 'active'),
('wholesale', 'Big Buyer', '8889990000', 'Warehouse Address', 'owner', 'owner', 'active'),
('retail', 'Small Shopper', '9990001111', 'Mall Address', 'staff', 'staff', 'active');

-- Insert data into Invoice
INSERT INTO Invoice (name, type, transaction_date, quantity, weight, price, payment, total, customers_id, product_id, users_id, product_name) VALUES
('Invoice 1', 'import', '2025-02-06 10:00:00', 10, 100, 500.00, 250.00, 750.00, 1, 1, 1, 'Product A'),
('Invoice 2', 'export', '2025-02-06 11:00:00', 5, 50, 250.00, 125.00, 375.00, 2, 2, 2, 'Product B'),
('Invoice 3', 'import', '2025-02-07 12:00:00', 15, 120, 600.00, 300.00, 900.00, 3, 3, 3, 'Product C'),
('Invoice 4', 'export', '2025-02-08 14:00:00', 8, 80, 480.00, 240.00, 720.00, 4, 4, 4, 'Product D');

-- Insert data into Number_of_bags
INSERT INTO Number_of_bags (type, quantity, price, invoice_id, created_by, note) VALUES
('yes', 2, 20.00, 1, 'owner', 'Includes packaging'),
('no', 1, 10.00, 2, 'owner', 'No packaging included'),
('yes', 3, 30.00, 3, 'owner', 'Special wrapping'),
('no', 2, 15.00, 4, 'owner', 'No additional packaging');

-- Insert data into Debt
INSERT INTO Debt (type, amount, customers_id, created_by, status) VALUES
('debt', 300.00, 1, 'owner', 'pending'),
('repay', 150.00, 2, 'owner', 'completed'),
('debt', 500.00, 3, 'owner', 'pending'),
('repay', 200.00, 4, 'owner', 'completed');

*/
