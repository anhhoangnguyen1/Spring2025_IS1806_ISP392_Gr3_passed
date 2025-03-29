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
    created_by VARCHAR(255),
    isDeleted TINYINT(1) DEFAULT 0 CHECK (isDeleted IN (0,1)),
    deletedAt DATETIME,
    deleteBy VARCHAR(255),
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status VARCHAR(255)
);

-- Table Users
CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    image VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL unique,
    address TEXT NOT NULL,
    gender VARCHAR(15) NOT NULL,
    dob DATE,
    role ENUM('admin', 'staff', 'owner') NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    store_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    deletedAt DATETIME,
    deleteBy VARCHAR(255),
    isDeleted TINYINT(1) DEFAULT 0 CHECK (isDeleted IN (0,1)),
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status VARCHAR(255),
    FOREIGN KEY (store_id) REFERENCES Stores(id) ON DELETE SET NULL
);

-- Table Products
CREATE TABLE Products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    image VARCHAR(255) NOT NULL,
    price DECIMAL(18,2) NOT NULL,
    quantity INT NOT NULL,
    store_id INT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    deletedAt DATETIME,
    deleteBy VARCHAR(255),
    isDeleted TINYINT(1) DEFAULT 0 CHECK (isDeleted IN (0,1)),
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status VARCHAR(255),
	FOREIGN KEY (store_id) REFERENCES Stores(id) ON DELETE SET NULL,
    FULLTEXT(name,description)
);

-- Table Zones
CREATE TABLE Zones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    product_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    deletedAt DATETIME,
    deleteBy VARCHAR(255),
    isDeleted TINYINT(1) DEFAULT 0 CHECK (isDeleted IN (0,1)),
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    store_id INT,
    status VARCHAR(255),
    FOREIGN KEY (product_id) REFERENCES Products(id) ON DELETE SET NULL,
	FOREIGN KEY (store_id) REFERENCES Stores(id) ON DELETE SET NULL
);

ALTER TABLE Zones ADD COLUMN history JSON;

-- Table Customers
CREATE TABLE Customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    address TEXT NOT NULL,
    balance DECIMAL(18,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    deletedAt DATETIME,
    deleteBy VARCHAR(255),
    isDeleted TINYINT(1) DEFAULT 0 CHECK (isDeleted IN (0,1)),
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    store_id INT,
    status VARCHAR(255),
	FOREIGN KEY (store_id) REFERENCES Stores(id) ON DELETE SET NULL
);


-- Table Debt note
CREATE TABLE Debt_note (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type ENUM('+', '-') NOT NULL,
    amount DECIMAL(18,2) NOT NULL, 
    customers_id INT,
    store_id INT,
    FOREIGN KEY (customers_id) REFERENCES Customers(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    deletedAt DATETIME,
    deleteBy VARCHAR(255),
    isDeleted TINYINT(1) DEFAULT 0 CHECK (isDeleted IN (0,1)),
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status VARCHAR(255),
    description TEXT,
    image VARCHAR(255),
	FOREIGN KEY (store_id) REFERENCES Stores(id) ON DELETE SET NULL
);

CREATE TABLE StockChecks (
    stockCheckId INT PRIMARY KEY AUTO_INCREMENT,
    zoneId INT, -- Liên kết với bảng Zones
    productId INT, -- Liên kết với bảng Products
    checkedDate DATETIME, -- Ngày kiểm kho
    actualQuantity INT, -- Số lượng thực tế sau kiểm kho
    recordedQuantity INT, -- Số lượng ghi nhận trước đó trong hệ thống
    discrepancy INT, -- Chênh lệch (actualQuantity - recordedQuantity)
    notes VARCHAR(255), -- Ghi chú (nếu có)
    FOREIGN KEY (zoneId) REFERENCES Zones(id),
    FOREIGN KEY (productId) REFERENCES Products(id)
);

-- Thêm trường status vào bảng StockChecks
ALTER TABLE StockChecks
ADD COLUMN status VARCHAR(255);

ALTER TABLE StockChecks
ADD COLUMN checked_by VARCHAR(255);

-- Table structure for table `Orders`

CREATE TABLE `Orders` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `OrderDate` date NOT NULL,
  `TotalAmount` int NOT NULL,
  `CustomerID` int DEFAULT NULL,
  `EmployeesID` int NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_customer_id_idx` (`CustomerID`),
  KEY `fk_employee_id_idx` (`EmployeesID`),
  CONSTRAINT `fk_customer_id` FOREIGN KEY (`CustomerID`) REFERENCES `Customers` (`id`),
  CONSTRAINT `fk_employee_id` FOREIGN KEY (`EmployeesID`) REFERENCES `Users` (`id`)
);

-- Table structure for table `OrderDetails`

CREATE TABLE `OrderDetails` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Quantity` int NOT NULL,
  `Price` int NOT NULL,
  `OrdersID` int NOT NULL,
  `ProductsID` int NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_product_id_idx` (`ProductsID`),
  KEY `fk_order_id_idx` (`OrdersID`),
  CONSTRAINT `fk_order_id` FOREIGN KEY (`OrdersID`) REFERENCES `Orders` (`ID`),
  CONSTRAINT `fk_product_id` FOREIGN KEY (`ProductsID`) REFERENCES `Products` (`id`)
);

-- Insert into Stores
INSERT INTO Stores(name, address, phone, email, status)
VALUES 
('Store 1', '65 Đường Nguyễn Trãi, Hà Nội', '0912639622', 'store1@gmail.com', 'Active');

-- Insert into Users
INSERT INTO Users (username, password, image, name, phone, address, gender, dob, role, email, store_id, status)
VALUES 
('admin', '482c811da5d5b4bc6d497ffa98491e38', 'admin.jpg', 'Nguyễn Văn Hoàng', '0987654321', '123 Đường Nguyễn Trãi, Hà Nội', 'Male', '1980-01-01', 'admin', 'hoangnahe181458@fpt.edu.vn', 1, 'Active'),
('owner', '482c811da5d5b4bc6d497ffa98491e38', 'owner.jpg', 'Phan Ngọc Mai', '0987654322', '456 Đường Khuất Duy Tiến, Hà Nội', 'Female', '1990-02-02', 'owner', 'phanngocmai2411@gmail.com', 1, 'Active'),
('staff1', '482c811da5d5b4bc6d497ffa98491e38', 'staff1.jpg', 'Lê Phương Linh', '0987654323', '789 Đường Trần Hưng Đạo, Hà Nội', 'Female', '2000-03-03', 'staff', 'phuonglinh2611.cv@gmail.com', 1, 'Active'),
('staff2', '482c811da5d5b4bc6d497ffa98491e38', 'staff2.jpg', 'Phạm Hoàng Anh', '0987654324', '101 Đường Hai Bà Trưng, Hà Nội', 'Male', '1999-04-04', 'staff', 'anhhoangyh3@gmail.com', 1, 'Active');

INSERT INTO Products (name, image, price, quantity, store_id, description, created_by, status)
VALUES 
('Gạo ST25', 'gao_st25.jpg', 25000.00, 100, 1, 'Gạo thơm chất lượng cao, đặc sản Sóc Trăng', 'Phan Ngọc Mai', 'Active'),
('Gạo Tám Thơm Điện Biên', 'gao_tam_thom.jpg', 22000.00, 150, 1, 'Gạo dẻo thơm, đặc sản Điện Biên', 'Lê Phương Linh', 'Active'),
('Gạo Nếp Cái Hoa Vàng', 'gao_nep_cai_hoa_vang.jpg', 30000.00, 80, 1, 'Gạo nếp thơm ngon, dùng làm bánh chưng', 'Phạm Hoàng Anh', 'Active'),
('Gạo Lứt Huyết Rồng', 'gao_lut_huet_rong.jpg', 28000.00, 120, 1, 'Gạo lứt giàu dinh dưỡng, tốt cho sức khỏe', 'Phan Ngọc Mai', 'Active'),
('Gạo Bắc Hương', 'gao_bac_huong.jpg', 20000.00, 200, 1, 'Gạo trắng thơm, giá phổ thông', 'Lê Phương Linh', 'Active'),
('Gạo Hương Lài', 'gao_huong_lai.jpg', 23000.00, 90, 1, 'Gạo thơm nhẹ, hạt dài, mềm cơm', 'Phan Ngọc Mai', 'Active'),
('Gạo Nàng Hương', 'gao_nang_huong.jpg', 26000.00, 110, 1, 'Gạo đặc sản Chợ Đào, thơm đậm', 'Lê Phương Linh', 'Active'),
('Gạo Nếp Tú Lệ', 'gao_nep_tu_le.jpg', 32000.00, 70, 1, 'Gạo nếp dẻo thơm, đặc sản Yên Bái', 'Phạm Hoàng Anh', 'Active'),
('Gạo Tấm Thơm', 'gao_tam_thom.jpg', 18000.00, 150, 1, 'Gạo tấm mềm, dùng nấu cháo hoặc cơm tấm', 'Phan Ngọc Mai', 'Active'),
('Gạo Thơm Thái', 'gao_thom_thai.jpg', 18000.00, 150, 1, 'Gạo sẽ có độ dẻo mềm vừa phải và rất thơm', 'Phan Ngọc Mai', 'Active'),
('Gạo Tám Xoan', 'gao_tam_xoan.jpg', 32000.00, 70, 1, 'Hạt màu trong xanh, không bạc bụng, mùi thơm lại dịu và tự nhiên', 'Phạm Hoàng Anh', 'Active'),
('Gạo Hàm Châu', 'gao_ham_chau.jpg', 26000.00, 110, 1, 'Gạo hương thơm tự nhiên, vị ngọt đậm. Gạo khi nấu xong nở và xốp', 'Lê Phương Linh', 'Active'),
('Gạo Nàng Xuân', 'gao_nang_xuan.jpg', 22000.00, 150, 1, 'Gạo khi nấu xong mềm dẻo, ngọt và có mùi thơm đặc trưng', 'Lê Phương Linh', 'Active'),
('Gạo Tài Nguyên', 'gao_tai_nguyen.jpg', 28000.00, 120, 1, 'Khi nấu sẽ cho cơm ráo, mềm, xốp, ngọt cơm. Đặc biệt, cơm vẫn ngon khi để nguội', 'Phan Ngọc Mai', 'Active'),
('Gạo lứt', 'gao_lut.jpg', 18000.00, 150, 1, 'Gạo lứt với lớp cám gạo chưa được xay xát, có màu tím hoặc đỏ, mang đến hàm lượng dinh dưỡng dồi dào cho người tiêu dùng', 'Phan Ngọc Mai', 'Active'),
('Gạo Japonica', 'gao_japonica.jpg', 27000.00, 100, 1, 'Gạo Nhật tròn hạt, dẻo cơm', 'Lê Phương Linh', 'Active');

INSERT INTO Zones (name, description, product_id, store_id, created_by, status)
VALUES 
('T1', 'Trái 1', 6, 1, 'Phan Ngọc Mai', 'Active'),
('T2', 'Trái 2', 7, 1, 'Lê Phương Linh', 'Active'),
('P1', 'Phải 1', 8, 1, 'Phạm Hoàng Anh', 'Active'),
('P2', 'Phải 2', 9, 1, 'Phan Ngọc Mai', 'Active'),
('P3', 'Phải 3', 10, 1, 'Lê Phương Linh', 'Active');

-- Insert into Customers
INSERT INTO Customers (name, phone, address, balance, created_by, store_id, status)
VALUES 
('Nguyễn Văn Minh', '0912345678', '123 Nguyễn Huệ, Hà Nội', 0, 'Phan Ngọc Mai', 1, 'Active'),
('Trần Thị Lan', '0912345679', '456 Quang Trung, Hà Nội', 0, 'Lê Phương Linh', 1, 'Active'),
('Lê Hoàng Nam', '0912345680', '789 Lê Lợi, Hà Nội', 0, 'Phan Ngọc Mai', 1, 'Active'),
('Phạm Văn Hùng', '0912345681', '101 Trần Hưng Đạo, Hải Dương',0, 'Lê Phương Linh', 1, 'Active'),
('Hoàng Thị Hạnh', '0912345682', '222 Hai Bà Trưng, Hà Nội', 0, 'Lê Phương Linh', 1, 'Active');

-- Dumping data for table `Orders`

INSERT INTO `Orders` VALUES 
(1,'2023-01-15',250000,1,3),
(2,'2023-02-03',175000,2,3),
(3,'2023-02-17',320000,3,3),
(4,'2023-03-05',450000,4,3),
(5,'2023-03-22',195000,5,3),
(6,'2023-04-10',280000,1,3),
(7,'2023-04-28',350000,2,3),
(8,'2023-05-15',225000,3,3),
(9,'2023-06-02',410000,4,3),
(10,'2023-06-20',185000,5,3),
(11,'2023-07-08',295000,1,3),
(12,'2023-07-25',330000,2,3),
(13,'2023-08-12',275000,3,3),
(14,'2023-08-30',420000,4,3),
(15,'2023-09-17',240000,5,3),
(17,'2025-03-25',47000,1,3),
(18,'2025-03-25',55000,4,3),
(19,'2025-03-25',47000,NULL,3),
(20,'2025-03-25',47000,NULL,3),
(21,'2025-03-25',47000,NULL,3),
(22,'2025-03-25',47000,NULL,3),
(23,'2025-03-25',47000,NULL,3),
(24,'2025-03-25',47000,NULL,3),
(25,'2025-03-25',47000,NULL,3),
(26,'2025-03-25',47000,NULL,3),
(27,'2025-03-25',55000,NULL,3),
(28,'2025-03-25',129000,1,3);

INSERT INTO `OrderDetails` VALUES 
(1,2,75000,1,3),(2,1,100000,1,5),
(3,3,25000,2,1),(4,2,50000,2,7),
(5,1,120000,3,4),(6,2,100000,3,8),
(7,3,50000,4,2),(8,1,150000,4,9),
(9,2,75000,4,6),(10,1,195000,5,10),
(11,2,60000,6,3),(12,1,80000,6,5),
(13,3,40000,6,1),(14,2,85000,7,7),
(15,1,180000,7,4),(16,2,45000,8,2),
(17,1,135000,8,9),(18,3,90000,9,6),
(19,2,115000,9,10),(20,1,185000,10,8),
(21,2,70000,11,3),(22,3,55000,11,1),
(23,1,95000,12,5),(24,2,65000,12,7),
(25,1,145000,13,4),(26,3,35000,13,2),
(27,2,125000,14,9),(28,1,170000,14,6),
(29,2,110000,15,10),(30,1,130000,15,8),
(31,1,25000,17,1),(32,1,25000,18,1),
(33,1,30000,18,3),(34,1,25000,19,1),
(35,1,22000,19,2),(36,1,25000,20,1),
(37,1,22000,20,2),(38,1,25000,21,1),
(39,1,22000,21,2),(40,1,25000,22,1),
(41,1,22000,22,2),(42,1,25000,23,1),
(43,1,22000,23,2),(44,1,25000,24,1),
(45,1,22000,24,2),(46,1,25000,25,1),
(47,1,22000,25,2),(48,1,25000,26,1),
(49,1,22000,26,2),(50,1,25000,27,1),
(51,1,30000,27,3),(52,2,26000,28,7),
(53,1,27000,28,10),(54,1,22000,28,2),
(55,1,28000,28,4);



