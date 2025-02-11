CREATE SCHEMA ISP392_Project;
USE ISP392_Project;
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

-- Insert into Users
INSERT INTO Users (username, password, name, phone, address, gender, dob, role, email, status)
VALUES 
('admin1', 'password123', 'Nguyễn Văn A', '0987654321', '123 Đường Nguyễn Trãi, Hà Nội', 'Male', '1980-01-01', 'admin', 'admin1@example.com', 'Active'),
('owner1', 'password123', 'Trần Thị B', '0987654322', '456 Đường Lê Lợi, TP.HCM', 'Female', '1985-02-02', 'owner', 'owner1@example.com', 'Active'),
('staff1', 'password123', 'Lê Văn C', '0987654323', '789 Đường Trần Hưng Đạo, Đà Nẵng', 'Male', '1990-03-03', 'staff', 'staff1@example.com', 'Active'),
('staff2', 'password123', 'Phạm Thị D', '0987654324', '101 Đường Hai Bà Trưng, Cần Thơ', 'Female', '1992-04-04', 'staff', 'staff2@example.com', 'Active');

-- Insert into Zones
INSERT INTO Zones (name, capacity, remain_capacity, status)
VALUES 
('Kho Miền Bắc', 100, 90, 'Active'),
('Kho Miền Trung', 200, 150, 'Active'),
('Kho Miền Nam', 150, 130, 'Active');

-- Insert into Products (10 values with real rice names)
INSERT INTO Products (name, image, price, quantity, zone_id, description, status)
VALUES 
('Gạo ST25', 'st25.jpg', 30.00, 100, 1, 'Gạo thơm ngon đạt giải nhất thế giới', 'Available'),
('Gạo Lài Sữa', 'laisua.jpg', 22.00, 80, 1, 'Gạo thơm mềm, xuất xứ từ An Giang', 'Available'),
('Gạo Tám Thơm', 'tamthom.jpg', 18.00, 120, 2, 'Gạo thơm đặc sản Nam Định', 'Available'),
('Gạo Nàng Hoa', 'nanghoa.jpg', 25.50, 50, 2, 'Gạo dẻo, thơm, không bạc bụng', 'Available'),
('Gạo Japonica', 'japonica.jpg', 40.00, 60, 3, 'Gạo nhập khẩu từ Nhật Bản', 'Available'),
('Gạo Hương Lài', 'huonglai.jpg', 21.00, 200, 1, 'Gạo đặc sản Sóc Trăng', 'Available'),
('Gạo Tài Nguyên', 'tainguyen.jpg', 19.00, 90, 2, 'Gạo mềm, thơm, thích hợp nấu cơm tấm', 'Available'),
('Gạo Đài Loan', 'dailoan.jpg', 28.00, 75, 3, 'Gạo ngon, dẻo, nhập khẩu Đài Loan', 'Available'),
('Gạo ST24', 'st24.jpg', 27.50, 110, 1, 'Gạo thơm ngon gần giống ST25', 'Available'),
('Gạo Hương Sen', 'huongsen.jpg', 23.50, 95, 3, 'Gạo thơm tự nhiên, dẻo', 'Available');

-- Insert into Customers (15 values with real names)
INSERT INTO Customers (name, phone, address, balance, created_by, status)
VALUES 
('Nguyễn Văn Minh', '0912345678', '123 Nguyễn Huệ, TP.HCM', 500.00, 'admin1', 'Active'),
('Trần Thị Lan', '0912345679', '456 Quang Trung, Hà Nội', 600.75, 'admin1', 'Active'),
('Lê Hoàng Nam', '0912345680', '789 Lê Lợi, Đà Nẵng', 700.00, 'admin1', 'Active'),
('Phạm Văn Hùng', '0912345681', '101 Trần Hưng Đạo, Hải Phòng', 150.00, 'staff1', 'Active'),
('Hoàng Thị Hạnh', '0912345682', '222 Hai Bà Trưng, Cần Thơ', 225.25, 'staff1', 'Active'),
('Nguyễn Văn Khải', '0912345683', '333 Nguyễn Du, Bình Dương', 175.50, 'staff2', 'Active'),
('Lê Thị Mai', '0912345684', '444 Phan Chu Trinh, Đồng Nai', 275.00, 'staff2', 'Active'),
('Bùi Minh Tuấn', '0912345685', '555 Cách Mạng Tháng 8, Quảng Ninh', 190.00, 'admin1', 'Active'),
('Đặng Hồng Phúc', '0912345686', '666 Nguyễn Đình Chiểu, Hà Nội', 320.60, 'staff1', 'Active'),
('Nguyễn Văn Quang', '0912345687', '777 Điện Biên Phủ, TP.HCM', 450.80, 'staff2', 'Active'),
('Trần Thị Thu', '0912345688', '888 Võ Văn Kiệt, Đà Nẵng', 280.90, 'admin1', 'Active'),
('Lê Quang Hiếu', '0912345689', '999 Nguyễn Trãi, Hải Phòng', 510.00, 'staff1', 'Active'),
('Hoàng Văn Sơn', '0912345690', '000 Lý Thường Kiệt, Cần Thơ', 160.50, 'staff2', 'Active'),
('Võ Thị Ngọc', '0912345691', '111 Trần Phú, Bình Định', 145.30, 'staff1', 'Active'),
('Nguyễn Văn Long', '0912345692', '222 Bạch Đằng, Tây Ninh', 195.75, 'staff2', 'Active');

-- Insert into Debt_note (10 values with real names)
INSERT INTO Debt_note (type, amount, customers_id, created_by, status, description, image)
VALUES 
('debt', 500.50, 1, 'staff1', 'Pending', 'Khách hàng nợ đơn hàng lớn', 'debt1.jpg'),
('repay', 300.00, 2, 'staff2', 'Completed', 'Khách hàng đã thanh toán một phần', 'repay1.jpg'),
('debt', 650.75, 3, 'staff1', 'Pending', 'Khách hàng mua gạo ST25 nhưng chưa thanh toán', 'debt2.jpg'),
('repay', 500.00, 4, 'staff2', 'Completed', 'Khách hàng trả xong đơn hàng', 'repay2.jpg'),
('debt', 800.00, 5, 'staff1', 'Pending', 'Khách hàng chưa trả tiền nhập kho', 'debt3.jpg'),
('repay', 450.50, 6, 'staff2', 'Completed', 'Khách hàng thanh toán đủ', 'repay3.jpg'),
('debt', 900.00, 7, 'staff1', 'Pending', 'Nợ lâu chưa thanh toán', 'debt4.jpg'),
('repay', 600.00, 8, 'staff2', 'Completed', 'Đã thanh toán đơn hàng', 'repay4.jpg'),
('debt', 1200.00, 9, 'staff1', 'Pending', 'Khách hàng đặt hàng số lượng lớn chưa thanh toán', 'debt5.jpg'),
('repay', 750.00, 10, 'staff2', 'Completed', 'Khách hàng trả đủ', 'repay5.jpg');


