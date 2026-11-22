CREATE DATABASE  IF NOT EXISTS `vibe` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `vibe`;
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: vibe
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `admins`
--

DROP TABLE IF EXISTS `admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admins` (
  `admin_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `password_salt` varchar(128) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admins`
--

LOCK TABLES `admins` WRITE;
/*!40000 ALTER TABLE `admins` DISABLE KEYS */;
/*!40000 ALTER TABLE `admins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking_seats`
--

DROP TABLE IF EXISTS `booking_seats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking_seats` (
  `booking_id` int NOT NULL,
  `seat_id` int NOT NULL,
  `ticket_price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`booking_id`,`seat_id`),
  KEY `seat_id` (`seat_id`),
  CONSTRAINT `booking_seats_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`),
  CONSTRAINT `booking_seats_ibfk_2` FOREIGN KEY (`seat_id`) REFERENCES `seats` (`seat_id`),
  CONSTRAINT `booking_seats_chk_1` CHECK ((`ticket_price` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking_seats`
--

LOCK TABLES `booking_seats` WRITE;
/*!40000 ALTER TABLE `booking_seats` DISABLE KEYS */;
/*!40000 ALTER TABLE `booking_seats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `booking_id` int NOT NULL AUTO_INCREMENT,
  `booking_reference` varchar(30) NOT NULL,
  `customer_id` int NOT NULL,
  `event_id` int NOT NULL,
  `booking_datetime` datetime DEFAULT CURRENT_TIMESTAMP,
  `total_amount` decimal(10,2) NOT NULL,
  `booking_status` enum('PENDING','CONFIRMED','CANCELLED') DEFAULT 'PENDING',
  PRIMARY KEY (`booking_id`),
  UNIQUE KEY `booking_reference` (`booking_reference`),
  KEY `customer_id` (`customer_id`),
  KEY `event_id` (`event_id`),
  CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`),
  CONSTRAINT `bookings_ibfk_2` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`),
  CONSTRAINT `bookings_chk_1` CHECK ((`total_amount` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `customer_id` int NOT NULL AUTO_INCREMENT,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(120) NOT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `password_hash` varchar(255) NOT NULL,
  `password_salt` varchar(128) NOT NULL,
  `registered_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `events`
--

DROP TABLE IF EXISTS `events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `events` (
  `event_id` int NOT NULL AUTO_INCREMENT,
  `event_name` varchar(150) NOT NULL,
  `category` enum('MOVIE','CONCERT','COMEDY') NOT NULL,
  `description` text,
  `venue` varchar(150) NOT NULL,
  `event_date` date NOT NULL,
  `event_time` time NOT NULL,
  `ticket_price` decimal(10,2) NOT NULL,
  `event_status` enum('ACTIVE','INACTIVE') DEFAULT 'ACTIVE',
  `created_by_admin_id` int DEFAULT NULL,
  PRIMARY KEY (`event_id`),
  KEY `created_by_admin_id` (`created_by_admin_id`),
  CONSTRAINT `events_ibfk_1` FOREIGN KEY (`created_by_admin_id`) REFERENCES `admins` (`admin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `events`
--

LOCK TABLES `events` WRITE;
/*!40000 ALTER TABLE `events` DISABLE KEYS */;
INSERT INTO `events` VALUES (1,'Movie Night','MOVIE','Movie screening event','Screen 1','2026-12-15','18:00:00',250.00,'ACTIVE',NULL),(2,'Live Music Concert','CONCERT','Live music performance','Main Auditorium','2026-12-20','19:00:00',500.00,'ACTIVE',NULL),(3,'Stand-Up Comedy Show','COMEDY','Stand-up comedy performance','Hall A','2026-12-25','17:00:00',300.00,'ACTIVE',NULL);
/*!40000 ALTER TABLE `events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `payment_id` int NOT NULL AUTO_INCREMENT,
  `booking_id` int NOT NULL,
  `payment_method` enum('UPI','CARD','NET_BANKING') NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `payment_status` enum('SUCCESS','FAILED','REFUNDED') NOT NULL,
  `transaction_reference` varchar(60) NOT NULL,
  `payment_datetime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`payment_id`),
  UNIQUE KEY `transaction_reference` (`transaction_reference`),
  KEY `booking_id` (`booking_id`),
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`),
  CONSTRAINT `payments_chk_1` CHECK ((`amount` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seats`
--

DROP TABLE IF EXISTS `seats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seats` (
  `seat_id` int NOT NULL AUTO_INCREMENT,
  `event_id` int NOT NULL,
  `row_label` varchar(5) NOT NULL,
  `seat_number` int NOT NULL,
  `seat_status` enum('AVAILABLE','BOOKED') DEFAULT 'AVAILABLE',
  PRIMARY KEY (`seat_id`),
  UNIQUE KEY `event_id` (`event_id`,`row_label`,`seat_number`),
  CONSTRAINT `seats_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`),
  CONSTRAINT `seats_chk_1` CHECK ((`seat_number` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seats`
--

LOCK TABLES `seats` WRITE;
/*!40000 ALTER TABLE `seats` DISABLE KEYS */;
INSERT INTO `seats` VALUES (1,1,'C',1,'AVAILABLE'),(2,2,'C',1,'AVAILABLE'),(3,3,'C',1,'AVAILABLE'),(4,1,'B',1,'AVAILABLE'),(5,2,'B',1,'AVAILABLE'),(6,3,'B',1,'AVAILABLE'),(7,1,'A',1,'AVAILABLE'),(8,2,'A',1,'AVAILABLE'),(9,3,'A',1,'AVAILABLE'),(10,1,'C',2,'AVAILABLE'),(11,2,'C',2,'AVAILABLE'),(12,3,'C',2,'AVAILABLE'),(13,1,'B',2,'AVAILABLE'),(14,2,'B',2,'AVAILABLE'),(15,3,'B',2,'AVAILABLE'),(16,1,'A',2,'AVAILABLE'),(17,2,'A',2,'AVAILABLE'),(18,3,'A',2,'AVAILABLE'),(19,1,'C',3,'AVAILABLE'),(20,2,'C',3,'AVAILABLE'),(21,3,'C',3,'AVAILABLE'),(22,1,'B',3,'AVAILABLE'),(23,2,'B',3,'AVAILABLE'),(24,3,'B',3,'AVAILABLE'),(25,1,'A',3,'AVAILABLE'),(26,2,'A',3,'AVAILABLE'),(27,3,'A',3,'AVAILABLE'),(28,1,'C',4,'AVAILABLE'),(29,2,'C',4,'AVAILABLE'),(30,3,'C',4,'AVAILABLE'),(31,1,'B',4,'AVAILABLE'),(32,2,'B',4,'AVAILABLE'),(33,3,'B',4,'AVAILABLE'),(34,1,'A',4,'AVAILABLE'),(35,2,'A',4,'AVAILABLE'),(36,3,'A',4,'AVAILABLE');
/*!40000 ALTER TABLE `seats` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-22 23:40:30
