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

-- Table ProductPriceHistory
CREATE TABLE ProductPriceHistory (
	id INT AUTO_INCREMENT PRIMARY KEY,
    price DECIMAL(18,2) NOT NULL,
    importPrice DECIMAL(18,2) NOT NULL,
    type VARCHAR(255) NOT NULL,
    store_id INT,
    product_id INT,
    order_id INT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    deletedAt DATETIME,
    deleteBy VARCHAR(255),
    isDeleted TINYINT(1) DEFAULT 0 CHECK (isDeleted IN (0,1)),
    FOREIGN KEY (product_id) REFERENCES Products(id) ON DELETE SET NULL,
    FOREIGN KEY (store_id) REFERENCES Stores(id) ON DELETE SET NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(id) ON DELETE SET NULL
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
-- Table Orders
CREATE TABLE Orders (
	id INT AUTO_INCREMENT PRIMARY KEY,
    customers_id INT,
    store_id INT,
    user_id INT,
    type ENUM('Import', 'Export') NOT NULL,
    amount DECIMAL(18,2) NOT NULL, 
    paidAmount DECIMAL(18,2) NOT NULL, 
    FOREIGN KEY (customers_id) REFERENCES Customers(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    deletedAt DATETIME,
    deleteBy VARCHAR(255),
    isDeleted TINYINT(1) DEFAULT 0 CHECK (isDeleted IN (0,1)),
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status VARCHAR(255),
    description TEXT,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE SET NULL,
	FOREIGN KEY (store_id) REFERENCES Stores(id) ON DELETE SET NULL
);

-- Table Order Details
CREATE TABLE OrderDetails (
	id INT AUTO_INCREMENT PRIMARY KEY,
	order_id INT,
    store_id INT,
    product_id INT,
    productName VARCHAR(255) NOT NULL,
	importPrice DECIMAL(18,2) NOT NULL,
    price DECIMAL(18,2) NOT NULL,
    unitPrice DECIMAL(18,2) NOT NULL,
    quantity INT NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    deletedAt DATETIME,
    deleteBy VARCHAR(255),
    isDeleted TINYINT(1) DEFAULT 0 CHECK (isDeleted IN (0,1)),
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status VARCHAR(255),
	FOREIGN KEY (store_id) REFERENCES Stores(id) ON DELETE SET NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(id) ON DELETE SET NULL,
    FOREIGN KEY (product_id) REFERENCES Products(id) ON DELETE SET NULL
);


-- Table Debt note
CREATE TABLE Debt_note (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type ENUM('+', '-') NOT NULL,
    amount DECIMAL(18,2) NOT NULL, 
    customers_id INT,
    store_id INT,
	order_id INT,
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
	FOREIGN KEY (store_id) REFERENCES Stores(id) ON DELETE SET NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(id) ON DELETE SET NULL
);




-- Insert into Stores
INSERT INTO Stores(name, address, phone, email, status)
VALUES 
('Store 1', '65 Đường Nguyễn Trãi, Hà Nội', '0912639622', 'store1@gmail.com', 'Active');

-- Insert into Users
INSERT INTO Users (username, password, image, name, phone, address, gender, dob, role, email, store_id, status)
VALUES 
('admin', '482c811da5d5b4bc6d497ffa98491e38', 'admin.jpg', 'Nguyễn Văn Hoàng', '0987654321', '123 Đường Nguyễn Trãi, Hà Nội', 'Male', '1980-01-01', 'admin', 'hoangnahe181458@fpt.edu.vn', null, 'Active'),
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
