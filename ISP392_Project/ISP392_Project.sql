CREATE SCHEMA ISP392_Project2;
USE ISP392_Project2;

-- Table Store
CREATE TABLE Stores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address TEXT NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,   
    is_deleted BOOLEAN DEFAULT FALSE,
    deletedAt DATETIME,
    status VARCHAR(255)
);

-- Table Users
CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    image VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    address TEXT NOT NULL,
    gender VARCHAR(15) NOT NULL,
    dob DATE,
    role ENUM('admin', 'staff', 'owner') NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    store_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    isDeleted TINYINT(1) DEFAULT 0 CHECK (isDeleted IN (0,1)),
    status VARCHAR(255),
    deletedAt DATETIME,
    FOREIGN KEY (store_id) REFERENCES Stores(id) ON DELETE SET NULL
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
    status VARCHAR(255)
);

-- Table Invoice
CREATE TABLE Invoice (
    id INT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    type ENUM('import', 'export') NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    payment DECIMAL(10,2) NOT NULL,
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
	quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
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

-- Insert into Stores
INSERT INTO Stores(name, address, phone, email, status)
VALUES 
('Store 1', '65 Đường Nguyễn Trãi, Hà Nội', '0912639622', 'store1@gmail.com', 'Active');

-- Insert into Users
INSERT INTO Users (username, password, image, name, phone, address, gender, dob, role, email, store_id, status)
VALUES 
('admin', 'password123', 'admin.jpg', 'Nguyễn Văn Hoàng', '0987654321', '123 Đường Nguyễn Trãi, Hà Nội', 'Male', '1980-01-01', 'admin', 'hoangnahe181458@fpt.edu.vn', null, 'Active'),
('owner', 'password123', 'owner.jpg', 'Phan Ngọc Mai', '0987654322', '456 Đường Khuất Duy Tiến, Hà Nội', 'Female', '1990-02-02', 'owner', 'phanngocmai2411@gmail.com', 1, 'Active'),
('staff1', 'password123', 'staff1.jpg', 'Lê Phương Linh', '0987654323', '789 Đường Trần Hưng Đạo, Hà Nội', 'Female', '2000-03-03', 'staff', 'phuonglinh2611.cv@gmail.com', 1, 'Active'),
('staff2', 'password123', 'staff2.jpg', 'Phạm Hoàng Anh', '0987654324', '101 Đường Hai Bà Trưng, Hà Nội', 'Male', '1999-04-04', 'staff', 'anhhoangyh3@gmail.com', 1, 'Active');

-- Insert into Zones
INSERT INTO Zones (name, capacity, remain_capacity, status)
VALUES 
('Gạo ST25, Gạo Lài Sữa, Gạo Hương Lài, Gạo ST24', 30000, 2000, 'Active'), -- 28.000
('Gạo Tám Thơm, Gạo Nàng Hoa, Gạo Tài Nguyên', 30000, 14800, 'Active'), -- 15.200
('Gạo Japonica, Gạo Đài Loan, Gạo Hương Sen', 30000, 6500, 'Active'); -- 23.500

-- Insert into Products
INSERT INTO Products (name, image, price, quantity, zone_id, description, status)
VALUES 
('Gạo ST25', 'st25.jpg', 44000, 10000, 1, 'Gạo đặc sản Việt Nam, thơm nhẹ, dẻo vừa. Phù hợp nấu cơm thường, cơm chiên, sushi.', 'Available'),
('Gạo Lài Sữa', 'laisua.jpg', 30000, 8000, 1, 'Dẻo mềm, thơm sữa tự nhiên, xuất xứ từ An Giang. Phù hợp nấu cơm trắng, cháo.', 'Available'),
('Gạo Tám Thơm', 'tamthom.jpg', 28000, 1200, 2, 'Gạo thơm nồng, dẻo vừa, đặc sản Nam Định. Phù hợp nấu cơm trắng, cơm niêu.', 'Available'),
('Gạo Nàng Hoa', 'nanghoa.jpg', 32000, 5000, 2, 'Gạo dẻo, thơm nhẹ, không bạc bụng, giữ cơm lâu. Phù hợp nấu cơm tấm, cơm chiên.', 'Available'),
('Gạo Japonica', 'japonica.jpg', 50000, 6000, 3, 'Gạo hạt tròn, dẻo nhiều, thơm nhẹ, nhập khẩu từ Nhật Bản. Phù hợp nấu cơm cuộn, sushi.', 'Available'),
('Gạo Hương Lài', 'huonglai.jpg', 28000, 5000, 1, 'Gạo thơm đặc trưng như hoa lài, dẻo vừa, đặc sản Sóc Trăng. Phù hợp nấu cơm trắng, cơm chiên.', 'Available'),
('Gạo Tài Nguyên', 'tainguyen.jpg', 24000, 9000, 2, 'Hạt to, xốp, ít dẻo. Phù hợp nấu cơm quán ăn, nhà hàng.', 'Available'),
('Gạo Đài Loan', 'dailoan.jpg', 35000, 7500, 3, 'Gạo ngon, dẻo nhiều, nhập khẩu Đài Loan. Phù hợp làm sushi, nấu xôi.', 'Available'),
('Gạo ST24', 'st24.jpg', 33000, 5000, 1, 'Gạo thơm ngon, giữ cơm lâu mềm. Phù hợp nấu cơm thường, làm cơm cuộn.', 'Available'),
('Gạo Hương Sen', 'huongsen.jpg', 30000, 10000, 3, 'Gạo thơm tự nhiên mùi sen, dẻo vừa. Phù hợp nấu cơm trắng, cháo', 'Available');


-- Insert into Customers
INSERT INTO Customers (name, phone, address, balance, created_by, status)
VALUES 
('Nguyễn Văn Minh', '0912345678', '123 Nguyễn Huệ, Hà Nội', -22000000, 'Phan Ngọc Mai', 'Active'),
('Trần Thị Lan', '0912345679', '456 Quang Trung, Hà Nội', 0, 'Lê Phương Linh', 'Active'),
('Lê Hoàng Nam', '0912345680', '789 Lê Lợi, Hà Nội', 0, 'Phan Ngọc Mai', 'Active'),
('Phạm Văn Hùng', '0912345681', '101 Trần Hưng Đạo, Hải Dương', -10000000, 'Lê Phương Linh', 'Active'),
('Hoàng Thị Hạnh', '0912345682', '222 Hai Bà Trưng, Hà Nội', 0, 'Lê Phương Linh', 'Active');

-- Insert into Invoice
INSERT INTO Invoice (type, total, payment, customers_id, users_id)
VALUES 
('export', 22000000, 0, 1, 1), -- nợ 22tr
('export', 9000000,  9000000, 2, 2), -- ko nợ
('export', 11200000, 11200000, 3, 3), -- Không nợ
('export', 19200000, 9200000, 4, 4), -- Trả nợ 1 phần
('import', 15000000, 15000000, 5, 1);

-- Insert into Invoice_detail 
INSERT INTO Invoice_detail (invoice_id, product_id, quantity, unit_price, description)
VALUES
(1, 1, 500, 44000, 'Gạo ST25, 10 bao 50kg'),
(2, 2, 300, 30000, 'Gạo Lài Sữa, 5 bao 60kg'),
(3, 3, 400, 28000, 'Gạo Tám Thơm, 8 bao 50kg'),
(4, 4, 600, 32000, 'Gạo Nàng Hoa, 12 bao 50kg'),
(5, 1, 500, 30000, 'Nhập khẩu Gạo ST25, 10 bao 50kg');

-- Insert into Debt_note
INSERT INTO Debt_note (type, amount, customers_id, created_by, status, description, image)
VALUES 
('debt', 22000000, 1, 'Lê Phương Linh', 'Pending', 'Khách hàng nợ', 'debt1.jpg'),
('debt', 10000000, 4, 'Phan Ngọc Mai', 'Pending', 'Khách hàng trả nợ một phần', 'debt2.jpg');

select * from customers;