CREATE DATABASE  IF NOT EXISTS `ISP392_Project2` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `ISP392_Project2`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: ISP392_Project2
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Customers`
--

DROP TABLE IF EXISTS `Customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Customers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `address` text NOT NULL,
  `balance` decimal(18,2) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(255) DEFAULT NULL,
  `deletedAt` datetime DEFAULT NULL,
  `deleteBy` varchar(255) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT '0',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` varchar(255) DEFAULT NULL,
  `store_id` int DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`),
  KEY `store_id` (`store_id`),
  CONSTRAINT `Customers_ibfk_1` FOREIGN KEY (`store_id`) REFERENCES `Stores` (`id`) ON DELETE SET NULL,
  CONSTRAINT `Customers_chk_1` CHECK ((`isDeleted` in (0,1)))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Customers`
--

LOCK TABLES `Customers` WRITE;
/*!40000 ALTER TABLE `Customers` DISABLE KEYS */;
INSERT INTO `Customers` VALUES (1,'Nguyễn Văn Minh','0912345678','123 Nguyễn Huệ, Hà Nội',0.00,'2025-03-20 18:25:49','Phan Ngọc Mai',NULL,NULL,0,'2025-03-20 18:25:49',NULL,1,'Active'),(2,'Trần Thị Lan','0912345679','456 Quang Trung, Hà Nội',0.00,'2025-03-20 18:25:49','Lê Phương Linh',NULL,NULL,0,'2025-03-20 18:25:49',NULL,1,'Active'),(3,'Lê Hoàng Nam','0912345680','789 Lê Lợi, Hà Nội',0.00,'2025-03-20 18:25:49','Phan Ngọc Mai',NULL,NULL,0,'2025-03-20 18:25:49',NULL,1,'Active'),(4,'Phạm Văn Hùng','0912345681','101 Trần Hưng Đạo, Hải Dương',0.00,'2025-03-20 18:25:49','Lê Phương Linh',NULL,NULL,0,'2025-03-20 18:25:49',NULL,1,'Active'),(5,'Hoàng Thị Hạnh','0912345682','222 Hai Bà Trưng, Hà Nội',0.00,'2025-03-20 18:25:49','Lê Phương Linh',NULL,NULL,0,'2025-03-20 18:25:49',NULL,1,'Active');
/*!40000 ALTER TABLE `Customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Debt_note`
--

DROP TABLE IF EXISTS `Debt_note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Debt_note` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` enum('+','-') NOT NULL,
  `amount` decimal(18,2) NOT NULL,
  `customers_id` int DEFAULT NULL,
  `store_id` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(255) DEFAULT NULL,
  `deletedAt` datetime DEFAULT NULL,
  `deleteBy` varchar(255) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT '0',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` varchar(255) DEFAULT NULL,
  `description` text,
  `image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `customers_id` (`customers_id`),
  KEY `store_id` (`store_id`),
  CONSTRAINT `Debt_note_ibfk_1` FOREIGN KEY (`customers_id`) REFERENCES `Customers` (`id`) ON DELETE CASCADE,
  CONSTRAINT `Debt_note_ibfk_2` FOREIGN KEY (`store_id`) REFERENCES `Stores` (`id`) ON DELETE SET NULL,
  CONSTRAINT `Debt_note_chk_1` CHECK ((`isDeleted` in (0,1)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Debt_note`
--

LOCK TABLES `Debt_note` WRITE;
/*!40000 ALTER TABLE `Debt_note` DISABLE KEYS */;
/*!40000 ALTER TABLE `Debt_note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Invoice`
--

DROP TABLE IF EXISTS `Invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Invoice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` enum('import','export') NOT NULL,
  `total` decimal(18,2) NOT NULL,
  `payment` decimal(18,2) NOT NULL,
  `customers_id` int DEFAULT NULL,
  `store_id` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(255) DEFAULT NULL,
  `deletedAt` datetime DEFAULT NULL,
  `deleteBy` varchar(255) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `customers_id` (`customers_id`),
  KEY `store_id` (`store_id`),
  CONSTRAINT `Invoice_ibfk_1` FOREIGN KEY (`customers_id`) REFERENCES `Customers` (`id`),
  CONSTRAINT `Invoice_ibfk_2` FOREIGN KEY (`store_id`) REFERENCES `Stores` (`id`) ON DELETE SET NULL,
  CONSTRAINT `Invoice_chk_1` CHECK ((`isDeleted` in (0,1)))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Invoice`
--

LOCK TABLES `Invoice` WRITE;
/*!40000 ALTER TABLE `Invoice` DISABLE KEYS */;
INSERT INTO `Invoice` VALUES (1,'export',22000000.00,22000000.00,1,1,'2025-03-20 18:25:49',NULL,NULL,NULL,0),(2,'export',9000000.00,9000000.00,2,1,'2025-03-20 18:25:49',NULL,NULL,NULL,0),(3,'export',11200000.00,11200000.00,3,1,'2025-03-20 18:25:49',NULL,NULL,NULL,0),(4,'export',19200000.00,19200000.00,4,1,'2025-03-20 18:25:49',NULL,NULL,NULL,0),(5,'import',15000000.00,15000000.00,5,1,'2025-03-20 18:25:49',NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `Invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OrderDetails`
--

DROP TABLE IF EXISTS `OrderDetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OrderDetails`
--

LOCK TABLES `OrderDetails` WRITE;
/*!40000 ALTER TABLE `OrderDetails` DISABLE KEYS */;
INSERT INTO `OrderDetails` VALUES (1,2,75000,1,3),(2,1,100000,1,5),(3,3,25000,2,1),(4,2,50000,2,7),(5,1,120000,3,4),(6,2,100000,3,8),(7,3,50000,4,2),(8,1,150000,4,9),(9,2,75000,4,6),(10,1,195000,5,10),(11,2,60000,6,3),(12,1,80000,6,5),(13,3,40000,6,1),(14,2,85000,7,7),(15,1,180000,7,4),(16,2,45000,8,2),(17,1,135000,8,9),(18,3,90000,9,6),(19,2,115000,9,10),(20,1,185000,10,8),(21,2,70000,11,3),(22,3,55000,11,1),(23,1,95000,12,5),(24,2,65000,12,7),(25,1,145000,13,4),(26,3,35000,13,2),(27,2,125000,14,9),(28,1,170000,14,6),(29,2,110000,15,10),(30,1,130000,15,8),(31,1,25000,17,1),(32,1,25000,18,1),(33,1,30000,18,3),(34,1,25000,19,1),(35,1,22000,19,2),(36,1,25000,20,1),(37,1,22000,20,2),(38,1,25000,21,1),(39,1,22000,21,2),(40,1,25000,22,1),(41,1,22000,22,2),(42,1,25000,23,1),(43,1,22000,23,2),(44,1,25000,24,1),(45,1,22000,24,2),(46,1,25000,25,1),(47,1,22000,25,2),(48,1,25000,26,1),(49,1,22000,26,2),(50,1,25000,27,1),(51,1,30000,27,3),(52,2,26000,28,7),(53,1,27000,28,10),(54,1,22000,28,2),(55,1,28000,28,4);
/*!40000 ALTER TABLE `OrderDetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Orders`
--

DROP TABLE IF EXISTS `Orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Orders`
--

LOCK TABLES `Orders` WRITE;
/*!40000 ALTER TABLE `Orders` DISABLE KEYS */;
INSERT INTO `Orders` VALUES (1,'2023-01-15',250000,1,3),(2,'2023-02-03',175000,2,3),(3,'2023-02-17',320000,3,3),(4,'2023-03-05',450000,4,3),(5,'2023-03-22',195000,5,3),(6,'2023-04-10',280000,1,3),(7,'2023-04-28',350000,2,3),(8,'2023-05-15',225000,3,3),(9,'2023-06-02',410000,4,3),(10,'2023-06-20',185000,5,3),(11,'2023-07-08',295000,1,3),(12,'2023-07-25',330000,2,3),(13,'2023-08-12',275000,3,3),(14,'2023-08-30',420000,4,3),(15,'2023-09-17',240000,5,3),(17,'2025-03-25',47000,1,3),(18,'2025-03-25',55000,4,3),(19,'2025-03-25',47000,NULL,3),(20,'2025-03-25',47000,NULL,3),(21,'2025-03-25',47000,NULL,3),(22,'2025-03-25',47000,NULL,3),(23,'2025-03-25',47000,NULL,3),(24,'2025-03-25',47000,NULL,3),(25,'2025-03-25',47000,NULL,3),(26,'2025-03-25',47000,NULL,3),(27,'2025-03-25',55000,NULL,3),(28,'2025-03-25',129000,1,3);
/*!40000 ALTER TABLE `Orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Products`
--

DROP TABLE IF EXISTS `Products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Products` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `image` varchar(255) NOT NULL,
  `price` decimal(18,2) NOT NULL,
  `quantity` int NOT NULL,
  `store_id` int DEFAULT NULL,
  `description` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(255) DEFAULT NULL,
  `deletedAt` datetime DEFAULT NULL,
  `deleteBy` varchar(255) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT '0',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `store_id` (`store_id`),
  CONSTRAINT `Products_ibfk_1` FOREIGN KEY (`store_id`) REFERENCES `Stores` (`id`) ON DELETE SET NULL,
  CONSTRAINT `Products_chk_1` CHECK ((`isDeleted` in (0,1)))
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Products`
--

LOCK TABLES `Products` WRITE;
/*!40000 ALTER TABLE `Products` DISABLE KEYS */;
INSERT INTO `Products` VALUES (1,'Gạo ST25','gao_st25.jpg',25000.00,90,1,'Gạo thơm chất lượng cao, đặc sản Sóc Trăng','2025-03-20 18:25:49','Phan Ngọc Mai',NULL,NULL,0,'2025-03-25 16:33:32','Active'),(2,'Gạo Tám Thơm Điện Biên','gao_tam_thom.jpg',22000.00,141,1,'Gạo dẻo thơm, đặc sản Điện Biên','2025-03-20 18:25:49','Lê Phương Linh',NULL,NULL,0,'2025-03-25 16:34:51','Active'),(3,'Gạo Nếp Cái Hoa Vàng','gao_nep_cai_hoa_vang.jpg',30000.00,78,1,'Gạo nếp thơm ngon, dùng làm bánh chưng','2025-03-20 18:25:49','Phạm Hoàng Anh',NULL,NULL,0,'2025-03-25 16:33:32','Active'),(4,'Gạo Lứt Huyết Rồng','gao_lut_huet_rong.jpg',28000.00,119,1,'Gạo lứt giàu dinh dưỡng, tốt cho sức khỏe','2025-03-20 18:25:49','Phan Ngọc Mai',NULL,NULL,0,'2025-03-25 16:34:51','Active'),(5,'Gạo Bắc Hương','gao_bac_huong.jpg',20000.00,200,1,'Gạo trắng thơm, giá phổ thông','2025-03-20 18:25:49','Lê Phương Linh',NULL,NULL,0,'2025-03-20 18:25:49','Active'),(6,'Gạo Hương Lài','gao_huong_lai.jpg',23000.00,90,1,'Gạo thơm nhẹ, hạt dài, mềm cơm','2025-03-20 18:25:49','Phan Ngọc Mai',NULL,NULL,0,'2025-03-20 18:25:49','Active'),(7,'Gạo Nàng Hương','gao_nang_huong.jpg',26000.00,108,1,'Gạo đặc sản Chợ Đào, thơm đậm','2025-03-20 18:25:49','Lê Phương Linh',NULL,NULL,0,'2025-03-25 16:34:51','Active'),(8,'Gạo Nếp Tú Lệ','gao_nep_tu_le.jpg',32000.00,70,1,'Gạo nếp dẻo thơm, đặc sản Yên Bái','2025-03-20 18:25:49','Phạm Hoàng Anh',NULL,NULL,0,'2025-03-20 18:25:49','Active'),(9,'Gạo Tấm Thơm','gao_tam_thom.jpg',18000.00,150,1,'Gạo tấm mềm, dùng nấu cháo hoặc cơm tấm','2025-03-20 18:25:49','Phan Ngọc Mai',NULL,NULL,0,'2025-03-20 18:25:49','Active'),(10,'Gạo Japonica','gao_japonica.jpg',27000.00,99,1,'Gạo Nhật tròn hạt, dẻo cơm','2025-03-20 18:25:49','Lê Phương Linh',NULL,NULL,0,'2025-03-25 16:34:51','Active');
/*!40000 ALTER TABLE `Products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Stores`
--

DROP TABLE IF EXISTS `Stores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Stores` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `address` text NOT NULL,
  `phone` varchar(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(255) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT '0',
  `deletedAt` datetime DEFAULT NULL,
  `deleteBy` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  CONSTRAINT `Stores_chk_1` CHECK ((`isDeleted` in (0,1)))
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Stores`
--

LOCK TABLES `Stores` WRITE;
/*!40000 ALTER TABLE `Stores` DISABLE KEYS */;
INSERT INTO `Stores` VALUES (1,'Store 1','65 Đường Nguyễn Trãi, Hà Nội','0912639622','store1@gmail.com','2025-03-20 18:25:49',NULL,0,NULL,NULL,'2025-03-20 18:25:49','Active');
/*!40000 ALTER TABLE `Stores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `image` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `address` text NOT NULL,
  `gender` varchar(15) NOT NULL,
  `dob` date DEFAULT NULL,
  `role` enum('admin','staff','owner') NOT NULL,
  `email` varchar(255) NOT NULL,
  `store_id` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(255) DEFAULT NULL,
  `deletedAt` datetime DEFAULT NULL,
  `deleteBy` varchar(255) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT '0',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `email` (`email`),
  KEY `store_id` (`store_id`),
  CONSTRAINT `Users_ibfk_1` FOREIGN KEY (`store_id`) REFERENCES `Stores` (`id`) ON DELETE SET NULL,
  CONSTRAINT `Users_chk_1` CHECK ((`isDeleted` in (0,1)))
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Users`
--

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;
INSERT INTO `Users` VALUES (1,'admin','482c811da5d5b4bc6d497ffa98491e38','admin.jpg','Nguyễn Văn Hoàng','0987654321','123 Đường Nguyễn Trãi, Hà Nội','Male','1980-01-01','admin','hoangnahe181458@fpt.edu.vn',NULL,'2025-03-20 18:25:49',NULL,NULL,NULL,0,'2025-03-20 18:25:49','Active'),(2,'owner','482c811da5d5b4bc6d497ffa98491e38','owner.jpg','Phan Ngọc Mai','0987654322','456 Đường Khuất Duy Tiến, Hà Nội','Female','1990-02-02','owner','phanngocmai2411@gmail.com',1,'2025-03-20 18:25:49',NULL,NULL,NULL,0,'2025-03-20 18:25:49','Active'),(3,'staff1','482c811da5d5b4bc6d497ffa98491e38','staff1.jpg','Lê Phương Linh','0987654323','789 Đường Trần Hưng Đạo, Hà Nội','Female','2000-03-03','staff','phuonglinh2611.cv@gmail.com',1,'2025-03-20 18:25:49',NULL,NULL,NULL,0,'2025-03-20 18:25:49','Active'),(4,'staff2','482c811da5d5b4bc6d497ffa98491e38','staff2.jpg','Phạm Hoàng Anh','0987654324','101 Đường Hai Bà Trưng, Hà Nội','Male','1999-04-04','staff','anhhoangyh3@gmail.com',1,'2025-03-20 18:25:49',NULL,NULL,NULL,0,'2025-03-20 18:25:49','Active');
/*!40000 ALTER TABLE `Users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Zones`
--

DROP TABLE IF EXISTS `Zones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Zones` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(255) DEFAULT NULL,
  `deletedAt` datetime DEFAULT NULL,
  `deleteBy` varchar(255) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT '0',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `store_id` int DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  KEY `store_id` (`store_id`),
  CONSTRAINT `Zones_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `Products` (`id`) ON DELETE SET NULL,
  CONSTRAINT `Zones_ibfk_2` FOREIGN KEY (`store_id`) REFERENCES `Stores` (`id`) ON DELETE SET NULL,
  CONSTRAINT `Zones_chk_1` CHECK ((`isDeleted` in (0,1)))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Zones`
--

LOCK TABLES `Zones` WRITE;
/*!40000 ALTER TABLE `Zones` DISABLE KEYS */;
INSERT INTO `Zones` VALUES (1,'T1','Trái 1',6,'2025-03-20 18:25:49','Phan Ngọc Mai',NULL,NULL,0,'2025-03-20 18:25:49',1,'Active'),(2,'T2','Trái 2',7,'2025-03-20 18:25:49','Lê Phương Linh',NULL,NULL,0,'2025-03-20 18:25:49',1,'Active'),(3,'P1','Phải 1',8,'2025-03-20 18:25:49','Phạm Hoàng Anh',NULL,NULL,0,'2025-03-20 18:25:49',1,'Active'),(4,'P2','Phải 2',9,'2025-03-20 18:25:49','Phan Ngọc Mai',NULL,NULL,0,'2025-03-20 18:25:49',1,'Active'),(5,'P3','Phải 3',10,'2025-03-20 18:25:49','Lê Phương Linh',NULL,NULL,0,'2025-03-20 18:25:49',1,'Active');
/*!40000 ALTER TABLE `Zones` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-25 23:40:36
