-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.1.31-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win32
-- HeidiSQL Version:             10.1.0.5464
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for eport
DROP DATABASE IF EXISTS `eport`;
CREATE DATABASE IF NOT EXISTS `eport` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;
USE `eport`;

-- Dumping structure for table eport.carrier_account
DROP TABLE IF EXISTS `carrier_account`;
CREATE TABLE IF NOT EXISTS `carrier_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `group_id` bigint(20) NOT NULL COMMENT 'Master Account',
  `carrier_code` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Shipping Line Code',
  `email` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Email',
  `password` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Password',
  `salt` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'Salt',
  `full_name` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Ho Va Ten',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Status（0 Normal 1 Disabled）',
  `del_flag` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Delete Flag (0 nomal 2 deleted)',
  `login_ip` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT 'Login IP',
  `login_date` datetime DEFAULT NULL COMMENT 'Login Date',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Carrier account';

-- Dumping data for table eport.carrier_account: ~3 rows (approximately)
/*!40000 ALTER TABLE `carrier_account` DISABLE KEYS */;
INSERT IGNORE INTO `carrier_account` (`id`, `group_id`, `carrier_code`, `email`, `password`, `salt`, `full_name`, `status`, `del_flag`, `login_ip`, `login_date`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
	(1, 1, 'CNC,CMA,APL', 'tai@gmail.com', 'a8073909b5853562442cb342386d8a76', '0f1722', 'Anh Tài', '0', '0', '127.0.0.1', '2020-05-07 12:53:08', NULL, '', '2020-04-07 11:18:40', 'DNG', '2020-05-07 12:53:08'),
	(2, 1, 'CNC,CMA,APL', 'nqat2003@gmail.com', '88cd9c095318aa5d9f84d589f437760f', '0d78ae', 'Anh Taif', '0', '0', '', NULL, NULL, 'DNG', '2020-04-08 10:20:33', 'DNG', '2020-04-08 10:21:33'),
	(3, 1, '1', 'tronghieu8531@gmail.com', 'f674d92c99ed8e12d33338b6f4fcf727', 'f4a5e2', 'Nguyễn Trọng Hiếu', '0', '0', '127.0.0.1', '2020-04-14 11:50:12', NULL, 'DNG', '2020-04-14 11:49:30', '', '2020-04-14 11:50:12');
/*!40000 ALTER TABLE `carrier_account` ENABLE KEYS */;

-- Dumping structure for table eport.carrier_group
DROP TABLE IF EXISTS `carrier_group`;
CREATE TABLE IF NOT EXISTS `carrier_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `group_code` varchar(5) COLLATE utf8_bin NOT NULL COMMENT 'Group Code',
  `group_name` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Group Name',
  `operate_code` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Operate Codes',
  `main_email` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Main Emails',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `do_flag` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'DO Permission',
  `edo_flag` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'EDO Permission',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Carrier Group';

-- Dumping data for table eport.carrier_group: ~3 rows (approximately)
/*!40000 ALTER TABLE `carrier_group` DISABLE KEYS */;
INSERT IGNORE INTO `carrier_group` (`id`, `group_code`, `group_name`, `operate_code`, `main_email`, `create_by`, `create_time`, `update_by`, `update_time`, `do_flag`, `edo_flag`) VALUES
	(1, '1', 'WWHA', '1', 'hello@gmail.com', '123123', NULL, 'DNG', '2020-05-07 13:10:22', '0', '0'),
	(2, 'sfa', 'asdfasdf', 'wdfs', 'asdfsd@asdfa.com', 'DNG', '2020-05-07 13:10:46', 'DNG', '2020-05-07 13:12:01', '1', '0'),
	(3, 'sdf', '1asd', 'asd', 'asdfa@asdf.com', 'DNG', '2020-05-07 13:11:30', 'DNG', '2020-05-07 13:11:54', '0', '0');
/*!40000 ALTER TABLE `carrier_group` ENABLE KEYS */;

-- Dumping structure for table eport.driver_account
DROP TABLE IF EXISTS `driver_account`;
CREATE TABLE IF NOT EXISTS `driver_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `logistic_group_id` bigint(20) DEFAULT NULL COMMENT 'Logistic Group',
  `mobile_number` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'So DT',
  `full_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ho va Ten',
  `password` varchar(64) COLLATE utf8_bin NOT NULL COMMENT 'Mat Khau',
  `salt` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'Salt',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Trạng thái khóa (default 0)',
  `del_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT 'Delete Flag',
  `valid_date` datetime DEFAULT NULL COMMENT 'Hieu Luc Den',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Driver login info';

-- Dumping data for table eport.driver_account: ~10 rows (approximately)
/*!40000 ALTER TABLE `driver_account` DISABLE KEYS */;
INSERT IGNORE INTO `driver_account` (`id`, `logistic_group_id`, `mobile_number`, `full_name`, `password`, `salt`, `status`, `del_flag`, `valid_date`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
	(1, 2, '1231231231', 'Nguyễn Trọng Hiếu', '4b45af63306e999b4a999e304ac27fac', '413b85', '1', b'0', '2020-11-13 00:00:00', NULL, 'Nguyễn Trọng Hiếu', '2020-05-22 23:19:25', NULL, NULL),
	(2, 2, '1231231235', 'asdfasdfsadf', 'c0749c0de9886b4bc8d09e3f52b23a28', '7da3b7', '1', b'0', '2020-10-08 00:00:00', NULL, 'Nguyễn Trọng Hiếu', '2020-05-22 23:19:52', NULL, NULL),
	(3, 2, '1231231236', 'afasfasfsadf', '59b27d5fb719688d1a52b2436fd632af', '8342ee', '1', b'0', '2020-12-25 00:00:00', NULL, 'Nguyễn Trọng Hiếu', '2020-05-22 23:20:14', NULL, NULL),
	(4, 2, '4344343412', 'Nguyễn Trọng Hiếu', 'dba14101442756b7c0dd7363fe06ac95', '648b38', '1', b'0', '2020-08-22 00:00:00', NULL, 'Nguyễn Trọng Hiếu', '2020-05-22 23:20:35', NULL, NULL),
	(5, 2, '43423412312', 'Nguyễn Trọng Hiếu', '893e5085fcc574779cd549c16fa1b251', 'd68cba', '1', b'0', '2020-07-31 00:00:00', NULL, 'Nguyễn Trọng Hiếu', '2020-05-22 23:20:56', NULL, NULL),
	(9, 1, '12123123123', 'asfasdf', '08ffe2f36b2a2442641e895fccca49fe', '3b6b7e', '1', b'0', '2020-06-05 19:00:14', NULL, 'nguyen trong hieu', '2020-05-29 19:00:14', NULL, NULL),
	(10, 1, '1231231223', 'adsfasdf', 'c613a521ef633d5b038ce837515c4c13', 'e6fb6c', '1', b'0', '2020-06-05 19:00:14', NULL, 'nguyen trong hieu', '2020-05-29 19:00:14', NULL, NULL),
	(11, 1, '1231231231', 'sadfasf', '1db6a189f3d6a823370ac14d9dec9d2d', '3cf305', '1', b'0', '2020-06-06 07:01:35', NULL, 'nguyen trong hieu', '2020-05-30 07:01:35', NULL, NULL),
	(12, 1, '1231231231', 'asdfasdf', 'dd183823ae510665d9abad93c493ca5a', '8ac0fc', '1', b'0', '2020-06-06 07:01:35', NULL, 'nguyen trong hieu', '2020-05-30 07:01:35', NULL, NULL),
	(13, 1, '1231231231', 'ádà', 'df9e24ec0c86fa75f1eb87b2bb8d0a6f', '4e23c1', '1', b'0', '2020-06-06 07:20:35', NULL, 'nguyen trong hieu', '2020-05-30 07:20:35', NULL, NULL);
/*!40000 ALTER TABLE `driver_account` ENABLE KEYS */;

-- Dumping structure for table eport.driver_truck
DROP TABLE IF EXISTS `driver_truck`;
CREATE TABLE IF NOT EXISTS `driver_truck` (
  `driver_id` bigint(20) NOT NULL COMMENT 'ID tài xế',
  `truck_id` bigint(20) NOT NULL COMMENT 'truck_id',
  PRIMARY KEY (`driver_id`,`truck_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='driver_truck';

-- Dumping data for table eport.driver_truck: ~0 rows (approximately)
/*!40000 ALTER TABLE `driver_truck` DISABLE KEYS */;
/*!40000 ALTER TABLE `driver_truck` ENABLE KEYS */;

-- Dumping structure for table eport.equipment_do
DROP TABLE IF EXISTS `equipment_do`;
CREATE TABLE IF NOT EXISTS `equipment_do` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `carrier_id` bigint(20) NOT NULL COMMENT 'ID Nhan Vien Hang Tau',
  `carrier_code` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'Ma Hang Tau',
  `order_number` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'So Lenh (Optional)',
  `bill_of_lading` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'So B/L',
  `business_unit` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Don Vi Khai Thac (Optional)',
  `consignee` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Chu Hang',
  `container_number` varchar(11) COLLATE utf8_bin NOT NULL COMMENT 'So Cont',
  `expired_dem` datetime NOT NULL COMMENT 'Han Lenh',
  `empty_container_depot` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Noi Ha Rong',
  `det_free_time` tinyint(4) DEFAULT NULL COMMENT 'So Ngay Mien Luu Vo Cont',
  `secure_code` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ma Bao Mat (optional)',
  `release_date` datetime DEFAULT NULL COMMENT 'Ngay Release',
  `vessel` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Tau',
  `voy_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'Chuyen',
  `do_type` varchar(3) COLLATE utf8_bin DEFAULT NULL COMMENT 'DO Type',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'DO Status',
  `process_status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Process Status',
  `document_status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Document Status',
  `document_receipt_date` datetime DEFAULT NULL COMMENT 'Document Receipt Date',
  `release_status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Release Status',
  `create_source` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Nguon Tao: web, edi, catos',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `process_remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu lam lenh',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Exchange Delivery Order';

-- Dumping data for table eport.equipment_do: ~129 rows (approximately)
/*!40000 ALTER TABLE `equipment_do` DISABLE KEYS */;
INSERT IGNORE INTO `equipment_do` (`id`, `carrier_id`, `carrier_code`, `order_number`, `bill_of_lading`, `business_unit`, `consignee`, `container_number`, `expired_dem`, `empty_container_depot`, `det_free_time`, `secure_code`, `release_date`, `vessel`, `voy_no`, `do_type`, `status`, `process_status`, `document_status`, `document_receipt_date`, `release_status`, `create_source`, `remark`, `process_remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
	(1, 1, '"23"', NULL, '"213"', NULL, '"07/04/2020"', '12', '2020-04-07 11:26:08', '"23"', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, NULL, '2020-04-07 11:26:08', NULL, NULL),
	(2, 1, 'WHA', NULL, '1', NULL, '1', '1', '2020-04-07 13:07:54', '1', 1, NULL, NULL, 'null', 'null', NULL, '0', '0', '0', NULL, '0', NULL, 'null', NULL, 'Anh Tai', '2020-04-07 13:07:54', NULL, NULL),
	(3, 1, 'WHA', NULL, '1', NULL, '1', '1', '2020-04-07 13:09:24', '1', 1, NULL, NULL, 'null', 'null', NULL, '0', '0', '0', NULL, '0', NULL, 'null', NULL, 'Anh Tai', '2020-04-07 13:09:24', NULL, NULL),
	(4, 1, 'WHA', NULL, '123', NULL, '3', '2131', '2020-04-07 13:09:24', 'null', NULL, NULL, NULL, 'null', 'null', NULL, '0', '0', '0', NULL, '0', NULL, 'null', NULL, 'Anh Tai', '2020-04-07 13:09:24', NULL, NULL),
	(5, 1, 'WHA', NULL, '123', NULL, '3123', '1231', '2020-04-07 13:33:30', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null', NULL, 'Anh Tài', '2020-04-07 13:33:30', NULL, NULL),
	(6, 1, 'WHA', NULL, '123', NULL, '123', '123', '2020-04-07 13:33:30', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null', NULL, 'Anh Tài', '2020-04-07 13:33:30', NULL, NULL),
	(7, 1, 'WHA', NULL, '123', NULL, '3123', '1231', '2020-04-07 13:35:05', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 13:35:05', NULL, NULL),
	(8, 1, 'WHA', NULL, '123', NULL, '123', '123', '2020-04-07 13:35:05', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 13:35:05', NULL, NULL),
	(9, 1, 'WHA', NULL, '123', NULL, '3123', '1231', '2010-12-25 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 13:39:19', NULL, NULL),
	(10, 1, 'WHA', NULL, '123', NULL, '123', '123', '2010-12-25 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 13:39:19', NULL, NULL),
	(11, 1, 'WHA', NULL, '123', NULL, '3123', '1231', '2020-03-12 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 13:40:58', NULL, NULL),
	(12, 1, 'WHA', NULL, '123', NULL, '123', '123', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 13:40:58', NULL, NULL),
	(13, 1, 'WHA', NULL, '3123', NULL, '23', '21', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 14:15:36', NULL, NULL),
	(14, 1, 'WHA', NULL, '123', NULL, '231', '123', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 14:15:36', NULL, NULL),
	(15, 1, 'WHA', NULL, '3123', NULL, '23', '21', '2020-04-05 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 14:15:56', NULL, NULL),
	(16, 1, 'WHA', NULL, '123', NULL, '231', '123', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 14:15:56', NULL, NULL),
	(17, 1, 'WHA', NULL, '23123', NULL, '231', '1', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 14:29:05', NULL, NULL),
	(18, 1, 'WHA', NULL, '1231', NULL, '123123', '23123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 14:29:05', NULL, NULL),
	(19, 1, '123', NULL, '231', NULL, '07/04/2020', '23123', '2020-04-07 16:44:18', NULL, NULL, NULL, NULL, NULL, 'null]', NULL, '0', '0', '0', NULL, '0', NULL, '"1":[nul', NULL, 'Anh Tài', '2020-04-07 16:44:18', NULL, NULL),
	(20, 1, 'ádfads', NULL, '1231', NULL, '23123', '23123', '2020-04-23 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 16:49:46', NULL, NULL),
	(21, 1, '1234', NULL, '123', NULL, '1231', '123', '2020-04-16 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 17:09:54', NULL, NULL),
	(22, 1, '1234', NULL, '123', NULL, '123', '123', '2020-04-25 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 17:13:26', NULL, NULL),
	(23, 1, '1234', NULL, '123', NULL, '123', '123', '2020-04-25 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 17:18:23', NULL, NULL),
	(24, 1, '1234', NULL, 'asdfaf', NULL, '123', '123', '2020-04-14 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 17:21:30', NULL, NULL),
	(25, 1, '1234', NULL, 'asdasdf', NULL, '123', '12', '2020-04-15 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '1', '2020-04-14 13:05:44', '0', NULL, '"1"]', NULL, 'Anh Tài', '2020-04-07 17:22:44', 'admin', NULL),
	(26, 1, '1234', NULL, 'asdfaf', NULL, '123', '123', '2020-04-14 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 17:25:06', NULL, NULL),
	(27, 1, '1234', NULL, 'asdasdf', NULL, '123', '12', '2020-04-15 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '1', '2020-04-14 13:05:44', '0', NULL, '"1"]', NULL, 'Anh Tài', '2020-04-07 17:25:09', 'admin', NULL),
	(28, 1, '1234', NULL, '123', NULL, '123', '23', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-07 19:24:10', NULL, NULL),
	(29, 1, '1234', NULL, '1231', NULL, '123123', '1231', '2020-04-17 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-07 19:33:34', NULL, NULL),
	(30, 1, '1234', NULL, '21312', NULL, '1231', '12312', '2020-04-15 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-07 19:33:48', NULL, NULL),
	(31, 1, '1234', NULL, '123', NULL, '23123', '123', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-07 19:35:32', NULL, NULL),
	(32, 1, '1234', NULL, '123', NULL, '123', '123123', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-07 19:35:58', NULL, NULL),
	(33, 1, '1234', NULL, '123', NULL, '23123', '2312', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-07 19:39:23', NULL, NULL),
	(34, 1, '1234', NULL, '12312', NULL, '123123', '12312', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-07 19:40:10', NULL, NULL),
	(35, 1, '1234', NULL, '123123', NULL, '123123', '12312', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 08:16:03', NULL, NULL),
	(36, 1, '1234', NULL, '123123', NULL, '123123123', '123123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 08:18:48', NULL, NULL),
	(37, 1, '1234', NULL, '123123', NULL, '123123', '123123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 08:23:01', NULL, NULL),
	(38, 1, '1234', NULL, '1231', NULL, '123123', '23123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 08:23:23', NULL, NULL),
	(39, 1, '1234', NULL, '123123', NULL, '3123', '1231', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 08:23:43', NULL, NULL),
	(40, 1, '1234', NULL, '213123', NULL, '231231', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 08:24:05', NULL, NULL),
	(41, 1, '1234', NULL, '12312', NULL, '231231', '31231', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 08:25:20', NULL, NULL),
	(42, 1, '1234', NULL, '1232', NULL, '2312', '31231', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 08:25:20', NULL, NULL),
	(43, 1, '1234', NULL, '31231', NULL, '3123123', '2312', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 08:25:48', NULL, NULL),
	(44, 1, '1234', NULL, '1231', NULL, '12312', '231', '2020-04-15 00:00:00', '123', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 08:28:25', NULL, NULL),
	(45, 1, '1234', NULL, '312', NULL, '1231231', '1231', '2020-04-16 00:00:00', '2312', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '1', '2020-04-14 13:03:07', '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 08:28:25', 'admin', NULL),
	(46, 1, '1234', NULL, '12312', NULL, '123', '12312', '2020-04-10 00:00:00', '1231231', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 08:28:26', NULL, NULL),
	(47, 1, '1234', NULL, '3123', NULL, '1231231', '312', '2020-04-14 00:00:00', '3123123', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 08:28:26', NULL, NULL),
	(48, 1, '1234', NULL, '2312', NULL, '31', '123', '2020-04-23 00:00:00', '3123', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 08:28:26', NULL, NULL),
	(49, 1, '"WWHA"', NULL, '123', NULL, '123', '23', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 09:29:21', NULL, NULL),
	(50, 1, '"WWHA"', NULL, '123', NULL, '23', '3', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 09:29:21', NULL, NULL),
	(51, 1, '"WWHA"', NULL, '123', NULL, '23', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 09:29:57', NULL, NULL),
	(52, 1, 'WWHA', NULL, '123', NULL, '08/04/2020', '123', '2020-04-08 09:36:16', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 09:36:16', NULL, NULL),
	(53, 1, 'WWHA', NULL, '"123"', NULL, '123', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'nul', NULL, 'Anh Tài', '2020-04-08 09:38:50', NULL, NULL),
	(54, 1, 'WWHA', NULL, '123', NULL, '08/04/2020', '123', '2020-04-08 09:40:46', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 09:41:04', NULL, NULL),
	(55, 1, 'WWHA', NULL, '"123"', NULL, '3', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 09:46:29', NULL, NULL),
	(56, 1, 'WWHA', NULL, '"123"', NULL, '123', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 09:51:57', NULL, NULL),
	(57, 1, 'WWHA', NULL, '"12"', NULL, '123', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 09:53:48', NULL, NULL),
	(58, 1, 'WWHA', NULL, '"123"', NULL, '123', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 09:55:32', NULL, NULL),
	(59, 1, 'WWHA', NULL, '"123"', NULL, '123', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 09:56:32', NULL, NULL),
	(60, 1, 'WWHA', NULL, '"123"', NULL, '123', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 09:56:59', NULL, NULL),
	(61, 1, 'WWHA', NULL, '"123"', NULL, '12312321', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 09:58:16', NULL, NULL),
	(62, 1, 'WWHA', NULL, '"123"', NULL, '123', '123', '2020-04-09 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:02:54', NULL, NULL),
	(63, 1, 'WWHA', NULL, '"123"', NULL, '123123', '123123', '2020-04-16 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:02:55', NULL, NULL),
	(64, 1, 'WWHA', NULL, '"1231"', NULL, '12312', '231', '2020-04-16 00:00:00', '123', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:08:51', NULL, NULL),
	(65, 1, 'WWHA', NULL, '"123"', NULL, '3123', '12312', '2020-04-08 00:00:00', '123123', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:08:51', NULL, NULL),
	(66, 1, 'WWHA', NULL, '"12312"', NULL, '123', '12312', '2020-04-15 00:00:00', '1231231', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:08:52', NULL, NULL),
	(67, 1, 'WWHA', NULL, '"3123"', NULL, '1231231', '312', '2020-04-16 00:00:00', '3123123', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:08:52', NULL, NULL),
	(68, 1, 'WWHA', NULL, '"2312"', NULL, '31', '123', '2020-04-16 00:00:00', '3123', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:08:54', NULL, NULL),
	(69, 1, 'WWHA', NULL, '"123"', NULL, 'Anh Tài', '1231', '2020-03-13 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:11:59', NULL, NULL),
	(70, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1234', '2020-04-08 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:51:35', NULL, NULL),
	(71, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:51:36', NULL, NULL),
	(72, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:51:36', NULL, NULL),
	(73, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1237', '2020-04-11 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:51:37', NULL, NULL),
	(74, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1238', '2020-04-12 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:51:37', NULL, NULL),
	(75, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1234', '2020-04-08 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '"Hello"', NULL, 'Anh Tài', '2020-04-08 10:55:05', NULL, NULL),
	(76, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:55:05', NULL, NULL),
	(77, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:55:06', NULL, NULL),
	(78, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1237', '2020-04-11 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:55:06', NULL, NULL),
	(79, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1238', '2020-04-12 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:55:06', NULL, NULL),
	(80, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1234', '2020-04-08 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:57:09', NULL, NULL),
	(81, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:57:12', NULL, NULL),
	(82, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:57:13', NULL, NULL),
	(83, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1237', '2020-04-11 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:57:20', NULL, NULL),
	(84, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1238', '2020-04-12 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '"hrahra"', NULL, 'Anh Tài', '2020-04-08 10:57:23', NULL, NULL),
	(85, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1234', '2020-04-08 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 11:07:00', NULL, NULL),
	(86, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 11:07:02', NULL, NULL),
	(87, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 11:07:03', NULL, NULL),
	(88, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1237', '2020-04-11 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 11:07:04', NULL, NULL),
	(89, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1238', '2020-04-12 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'fadsfkjnjadsf', NULL, 'Anh Tài', '2020-04-08 11:07:06', NULL, NULL),
	(90, 1, 'WWHA', NULL, '[ 12315', NULL, 'Anh Taif', 'CON1234', '2020-04-08 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '213', NULL, 'Anh Tài', '2020-04-08 11:21:51', NULL, NULL),
	(91, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '2131231', NULL, 'Anh Tài', '2020-04-08 11:21:58', NULL, NULL),
	(92, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 11:22:03', NULL, NULL),
	(93, 1, 'WWHA', NULL, '[ 12315', NULL, 'Anh Taif', 'CON1234', '2020-04-08 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '213', NULL, 'Anh Tài', '2020-04-08 11:25:58', NULL, NULL),
	(94, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '2131231', NULL, 'Anh Tài', '2020-04-08 11:25:59', NULL, NULL),
	(95, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 11:25:59', NULL, NULL),
	(96, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1237', '2020-04-11 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 11:26:00', NULL, NULL),
	(97, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1238', '2020-04-12 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 11:26:01', NULL, NULL),
	(98, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1234', '2020-04-08 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '213', NULL, 'Anh Tài', '2020-04-08 11:37:13', NULL, NULL),
	(99, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '2131231', NULL, 'Anh Tài', '2020-04-08 11:37:22', NULL, NULL),
	(100, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 11:37:25', NULL, NULL),
	(101, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1237', '2020-04-11 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 11:37:26', NULL, NULL),
	(102, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1234', '2020-04-08 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '213', NULL, 'Anh Tài', '2020-04-08 11:38:47', NULL, NULL),
	(103, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '2131231', NULL, 'Anh Tài', '2020-04-08 11:38:48', NULL, NULL),
	(104, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 11:38:50', NULL, NULL),
	(105, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1237', '2020-04-11 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 11:38:51', NULL, NULL),
	(106, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1234', '2020-04-15 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '213', NULL, 'Anh Tài', '2020-04-08 11:39:48', NULL, '2020-04-09 16:01:15'),
	(107, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-10 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '2131231', NULL, 'Anh Tài', '2020-04-08 11:39:48', NULL, '2020-04-09 16:01:39'),
	(108, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 11:39:48', NULL, '2020-04-09 16:02:02'),
	(109, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1237', '2020-04-10 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 11:39:49', NULL, '2020-04-09 16:02:02'),
	(110, 1, 'WWHA', NULL, '123', NULL, '123', '123', '2020-04-16 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-09 09:19:19', NULL, '2020-04-09 15:59:35'),
	(111, 1, 'WWHA', NULL, 'Nhà', NULL, 'Nay', 'Hôm', '2020-04-10 00:00:00', 'One', 3, NULL, NULL, 'Air', NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-09 09:21:56', NULL, '2020-04-09 16:02:01'),
	(112, 1, 'WWHA', NULL, 'ádà', NULL, 'ádfae', 'fadsf', '2020-04-09 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-09 17:32:55', NULL, NULL),
	(113, 1, 'WWHA', NULL, '123123', NULL, '123123', 'qưeqưe', '2020-04-09 00:00:00', NULL, 1, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-09 17:34:37', NULL, NULL),
	(114, 1, 'WWHA', NULL, '12312', NULL, '12312312', '123123', '2020-04-23 00:00:00', NULL, 2, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-09 17:34:37', NULL, NULL),
	(115, 1, 'WWHA', NULL, '123', NULL, '21323', '123', '2020-04-10 00:00:00', '123', 123, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-10 09:25:12', NULL, NULL),
	(116, 1, '123546', NULL, 'asdf', NULL, 'sadfsd', 'sdfd1231223', '2020-04-22 23:59:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '1', '2020-04-14 13:02:21', '0', NULL, NULL, NULL, NULL, '2020-04-14 09:09:55', 'admin', NULL),
	(117, 1, '123546', NULL, 'asdff', NULL, 'sadfsd', 'sdfd1231223', '2020-04-22 23:59:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, NULL, '2020-04-14 09:10:16', NULL, NULL),
	(118, 1, '123546', NULL, 'dsfdfsda', NULL, 'sadfsad', 'dfdf2323456', '2020-04-15 23:59:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, NULL, '2020-04-14 09:13:50', NULL, NULL),
	(119, 1, '123546', NULL, '123123qwe', NULL, '1231', 'ZZZZ6666666', '2020-04-14 23:59:59', '123123', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, NULL, '2020-04-14 09:23:18', NULL, NULL),
	(120, 1, '123546', NULL, 'sdf', NULL, 'sadf', 'sdfd1231231', '2020-04-16 23:59:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:00:59', '0', NULL, NULL, NULL, NULL, '2020-04-14 09:40:35', 'admin', '2020-04-14 13:01:36'),
	(121, 1, '123546', NULL, 'sadf', NULL, 'sdafsd', 'dfsd1231231', '2020-04-16 23:59:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '1', '2020-04-14 13:46:30', '0', NULL, NULL, NULL, NULL, '2020-04-14 09:45:42', 'admin', NULL),
	(122, 1, '123546', NULL, 'sdfsd', NULL, 'sdfas', 'sdff1231123', '2020-04-15 23:59:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, NULL, '2020-04-14 09:46:19', NULL, NULL),
	(123, 1, '123546', NULL, 'sdfsad', NULL, 'asdf', 'fsds1231231', '2020-04-23 23:59:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 12:57:40', '0', NULL, NULL, NULL, NULL, '2020-04-14 09:47:44', 'admin', '2020-04-14 12:58:29'),
	(124, 1, '123546', NULL, 'asdfdfa', NULL, 'asdfff', 'sdfs1231233', '2020-04-15 23:59:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 12:59:39', '0', NULL, NULL, NULL, NULL, '2020-04-14 09:48:40', 'admin', '2020-04-14 13:46:44'),
	(125, 1, '123546', NULL, 'dsafsd', NULL, 'sadfsd', 'asds8888888', '2020-04-15 23:59:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 12:48:37', '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-14 11:46:33', 'admin', '2020-04-14 12:59:14'),
	(126, 1, '123546', NULL, 'dsafsd', NULL, 'sadfsd', 'asds8878888', '2020-04-16 23:59:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 12:48:37', '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-14 11:46:35', 'admin', '2020-04-14 12:59:14'),
	(127, 3, '1', NULL, 'bil1', NULL, 'sdf', 'dsfs1231231', '2020-04-15 23:59:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Nguyễn Trọng Hiếu', '2020-04-14 11:51:03', 'Nguyễn Trọng Hiếu', '2020-04-14 11:58:00'),
	(128, 3, '1', NULL, 'bil1', NULL, 'sdf', 'dsfs1231232', '2020-04-15 23:59:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Nguyễn Trọng Hiếu', '2020-04-14 11:51:03', 'Nguyễn Trọng Hiếu', '2020-04-14 11:58:00'),
	(129, 3, '1', NULL, 'bil1', NULL, 'sdf', 'dsfs1231290', '2020-04-15 23:59:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Nguyễn Trọng Hiếu', '2020-04-14 11:51:03', 'Nguyễn Trọng Hiếu', '2020-04-14 11:58:00');
/*!40000 ALTER TABLE `equipment_do` ENABLE KEYS */;

-- Dumping structure for table eport.gen_table
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE IF NOT EXISTS `gen_table` (
  `table_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) COLLATE utf8_bin DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) COLLATE utf8_bin DEFAULT '' COMMENT '表描述',
  `class_name` varchar(100) COLLATE utf8_bin DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) COLLATE utf8_bin DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `package_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '生成功能作者',
  `options` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='代码生成业务表';

-- Dumping data for table eport.gen_table: ~0 rows (approximately)
/*!40000 ALTER TABLE `gen_table` DISABLE KEYS */;
INSERT IGNORE INTO `gen_table` (`table_id`, `table_name`, `table_comment`, `class_name`, `tpl_category`, `package_name`, `module_name`, `business_name`, `function_name`, `function_author`, `options`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
	(2, 'sys_post', 'Job Information Sheet', 'SysPost', 'crud', 'vn.com.irtech.eport.system', 'system', 'post', 'Job Information Sheet', 'ruoyi', NULL, 'admin', '2020-03-30 00:54:19', '', NULL, NULL);
/*!40000 ALTER TABLE `gen_table` ENABLE KEYS */;

-- Dumping structure for table eport.gen_table_column
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE IF NOT EXISTS `gen_table_column` (
  `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) COLLATE utf8_bin DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) COLLATE utf8_bin DEFAULT '' COMMENT '字典类型',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`column_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='代码生成业务表字段';

-- Dumping data for table eport.gen_table_column: ~10 rows (approximately)
/*!40000 ALTER TABLE `gen_table_column` DISABLE KEYS */;
INSERT IGNORE INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
	(3, '2', 'post_id', 'Job ID', 'bigint(20)', 'Long', 'postId', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2020-03-30 00:54:19', '', NULL),
	(4, '2', 'post_code', 'Post code', 'varchar(64)', 'String', 'postCode', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2020-03-30 00:54:19', '', NULL),
	(5, '2', 'post_name', 'position Name', 'varchar(50)', 'String', 'postName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 3, 'admin', '2020-03-30 00:54:19', '', NULL),
	(6, '2', 'post_sort', 'display order', 'int(4)', 'Integer', 'postSort', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2020-03-30 00:54:19', '', NULL),
	(7, '2', 'status', 'Status (0 normal 1 disabled)', 'char(1)', 'String', 'status', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'radio', '', 5, 'admin', '2020-03-30 00:54:19', '', NULL),
	(8, '2', 'create_by', 'Creator', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 6, 'admin', '2020-03-30 00:54:19', '', NULL),
	(9, '2', 'create_time', 'Create Time', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2020-03-30 00:54:19', '', NULL),
	(10, '2', 'update_by', 'Updater', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 8, 'admin', '2020-03-30 00:54:19', '', NULL),
	(11, '2', 'update_time', 'Update Time', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 9, 'admin', '2020-03-30 00:54:19', '', NULL),
	(12, '2', 'remark', 'Remark', 'varchar(500)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'textarea', '', 10, 'admin', '2020-03-30 00:54:19', '', NULL);
/*!40000 ALTER TABLE `gen_table_column` ENABLE KEYS */;

-- Dumping structure for table eport.logistic_account
DROP TABLE IF EXISTS `logistic_account`;
CREATE TABLE IF NOT EXISTS `logistic_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `group_id` bigint(20) NOT NULL COMMENT 'Master Account',
  `user_name` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'Username',
  `email` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Email',
  `password` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Password',
  `salt` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'Salt',
  `full_name` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Ho Va Ten',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Status（0 Normal 1 Disabled）',
  `del_flag` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Delete Flag (0 nomal 1 deleted)',
  `login_ip` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT 'Login IP',
  `login_date` datetime DEFAULT NULL COMMENT 'Login Date',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Logistic account';

-- Dumping data for table eport.logistic_account: ~0 rows (approximately)
/*!40000 ALTER TABLE `logistic_account` DISABLE KEYS */;
INSERT IGNORE INTO `logistic_account` (`id`, `group_id`, `user_name`, `email`, `password`, `salt`, `full_name`, `status`, `del_flag`, `login_ip`, `login_date`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
	(1, 1, 'mst123123', 'asdfasd@sadfs.com', '054d07e1dee07d685e989fbede83d06b', '0e440a', 'nguyen trong hieu', '0', '0', '127.0.0.1', '2020-06-19 22:54:39', NULL, 'DNG', '2020-05-28 13:50:26', '', '2020-06-19 22:54:39');
/*!40000 ALTER TABLE `logistic_account` ENABLE KEYS */;

-- Dumping structure for table eport.logistic_group
DROP TABLE IF EXISTS `logistic_group`;
CREATE TABLE IF NOT EXISTS `logistic_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `group_name` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Tên doanh nghiệp',
  `email_address` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Địa chỉ thư điện tử',
  `address` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Địa chỉ liên hệ',
  `mst` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Mã số thuế doanh nghiệp',
  `phone` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Điện thoại cố định',
  `mobile_phone` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Điện thoại di động',
  `credit_flag` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Credit Card (1:có,0:không(default)))',
  `fax` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Fax',
  `del_flag` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Delete Flag (0 nomal 1 deleted)',
  `business_registration_certificate` varchar(100) COLLATE utf8_bin NOT NULL COMMENT 'Giấy đăng ký kinh doanh',
  `date_of_issue_registration` datetime NOT NULL COMMENT 'Ngày cấp giấy đăng ký',
  `place_of_issue_registration` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Nơi cấp giấy đăng ký',
  `authorized_representative` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Đại diện có thẩm quyền',
  `representative_position` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Chức vụ',
  `following_authorization_form_no` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'Theo bản ủy quyền số',
  `sign_date` datetime NOT NULL COMMENT 'Ngày ký',
  `owned` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'của ...',
  `identify_card_no` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'Số chứng minh thư',
  `date_of_issue_identify` datetime NOT NULL COMMENT 'Ngày cấp chứng minh',
  `place_of_issue_identify` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Nơi cấp chứng minh',
  `email` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'email',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Logistic Group';

-- Dumping data for table eport.logistic_group: ~0 rows (approximately)
/*!40000 ALTER TABLE `logistic_group` DISABLE KEYS */;
INSERT IGNORE INTO `logistic_group` (`id`, `group_name`, `email_address`, `address`, `mst`, `phone`, `mobile_phone`, `credit_flag`, `fax`, `del_flag`, `business_registration_certificate`, `date_of_issue_registration`, `place_of_issue_registration`, `authorized_representative`, `representative_position`, `following_authorization_form_no`, `sign_date`, `owned`, `identify_card_no`, `date_of_issue_identify`, `place_of_issue_identify`, `email`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
	(1, 'Vinconship', 'email@email.com', '35 Cao Thang', '123123123123', '0541123412341', '0935802290', '0', '1234234', '0', 'asdfsadf', '2020-05-28 00:00:00', 'asdfsadf', 'asdfsdaf', 'asdfsadf', 'asdfas', '2020-05-29 00:00:00', 'asdfasdf', '12341231231', '2020-05-14 00:00:00', 'asdfasdf', 'tronghieu8531@gmail.com', '', '2020-05-28 13:49:52', '', NULL);
/*!40000 ALTER TABLE `logistic_group` ENABLE KEYS */;

-- Dumping structure for table eport.logistic_truck
DROP TABLE IF EXISTS `logistic_truck`;
CREATE TABLE IF NOT EXISTS `logistic_truck` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `logistic_group_id` bigint(20) DEFAULT NULL COMMENT 'Logistic Group',
  `plate_number` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Bien So Xe',
  `type` char(1) COLLATE utf8_bin NOT NULL COMMENT '1:đầu kéo, 0:rơ mooc',
  `wgt` int(11) NOT NULL COMMENT 'Tải trọng',
  `registry_expiry_date` datetime DEFAULT NULL COMMENT 'Hạn đăng kiểm',
  `del_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT 'Delete Flag(default 0)',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Logistics Truck';

-- Dumping data for table eport.logistic_truck: ~0 rows (approximately)
/*!40000 ALTER TABLE `logistic_truck` DISABLE KEYS */;
/*!40000 ALTER TABLE `logistic_truck` ENABLE KEYS */;

-- Dumping structure for table eport.pickup_assign
DROP TABLE IF EXISTS `pickup_assign`;
CREATE TABLE IF NOT EXISTS `pickup_assign` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `logistic_group_id` bigint(20) NOT NULL COMMENT 'Logistic Group',
  `shipment_id` bigint(20) NOT NULL COMMENT 'Ma Lo',
  `driver_id` bigint(20) DEFAULT NULL COMMENT 'ID tài xế',
  `shipment_detail_id` bigint(20) DEFAULT NULL COMMENT 'Shipment Detail Id',
  `external_flg` bit(1) DEFAULT b'0' COMMENT 'Thue ngoai (0,1)',
  `external_secret_code` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT 'Mã nhận lệnh thuê ngoài',
  `truck_no` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Biển số xe đầu kéo (thuê ngoài)',
  `chassis_no` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Biển số xe rơ mooc (thuê ngoài)',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Pickup Assign';

-- Dumping data for table eport.pickup_assign: ~0 rows (approximately)
/*!40000 ALTER TABLE `pickup_assign` DISABLE KEYS */;
/*!40000 ALTER TABLE `pickup_assign` ENABLE KEYS */;

-- Dumping structure for table eport.pickup_hisory
DROP TABLE IF EXISTS `pickup_hisory`;
CREATE TABLE IF NOT EXISTS `pickup_hisory` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `logistic_group_id` bigint(20) NOT NULL COMMENT 'Logistic Group',
  `shipment_id` bigint(20) NOT NULL COMMENT 'Mã Lô',
  `driver_id` bigint(20) NOT NULL COMMENT 'ID Tài xế',
  `pickup_assign_id` bigint(20) NOT NULL COMMENT 'Assign ID',
  `container_no` varchar(12) COLLATE utf8_bin DEFAULT NULL COMMENT 'Số container',
  `truck_no` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Biển số xe đầu kéo',
  `chassis_no` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Biển số xe rơ mooc',
  `yard_position` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'Tọa độ cont trên bãi',
  `status` tinyint(3) DEFAULT '0' COMMENT 'Trạng thái (0:received, 1:planned, 2:gate-in, 3: gate-out)',
  `receipt_date` datetime DEFAULT NULL COMMENT 'Ngày nhận lệnh',
  `gatein_date` datetime DEFAULT NULL COMMENT 'Ngày vào cổng',
  `gateout_date` datetime DEFAULT NULL COMMENT 'Ngày ra cổng',
  `cancel_receipt_date` datetime DEFAULT NULL COMMENT 'Ngày hủy lệnh',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Pickup history';

-- Dumping data for table eport.pickup_hisory: ~0 rows (approximately)
/*!40000 ALTER TABLE `pickup_hisory` DISABLE KEYS */;
/*!40000 ALTER TABLE `pickup_hisory` ENABLE KEYS */;

-- Dumping structure for table eport.process_bill
DROP TABLE IF EXISTS `process_bill`;
CREATE TABLE IF NOT EXISTS `process_bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `shipment_id` bigint(20) NOT NULL COMMENT 'Mã Lô',
  `process_order_id` bigint(20) NOT NULL COMMENT 'Process Order ID',
  `service_type` tinyint(1) NOT NULL COMMENT 'Loại dịch vụ (bốc, hạ, gate)',
  `reference_no` varchar(64) COLLATE utf8_bin NOT NULL COMMENT 'Mã Tham Chiếu',
  `sztp` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'Size Type',
  `cont_number` smallint(6) NOT NULL COMMENT 'Số lượng cont',
  `exchange_fee` bigint(20) NOT NULL COMMENT 'Phí giao nhận',
  `preorder_cont_number` tinyint(4) DEFAULT '0' COMMENT 'Số cont chỉ định',
  `shifting_number` tinyint(4) DEFAULT '0' COMMENT 'Số dịch chuyển',
  `shifting_fee` bigint(20) DEFAULT '0' COMMENT 'Phí dịch chuyển',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Process billing';

-- Dumping data for table eport.process_bill: ~0 rows (approximately)
/*!40000 ALTER TABLE `process_bill` DISABLE KEYS */;
/*!40000 ALTER TABLE `process_bill` ENABLE KEYS */;

-- Dumping structure for table eport.process_history
DROP TABLE IF EXISTS `process_history`;
CREATE TABLE IF NOT EXISTS `process_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `process_order_id` bigint(20) NOT NULL COMMENT 'Process Order ID',
  `sys_user_id` bigint(20) DEFAULT NULL COMMENT 'User ID (OM)',
  `robot_uuid` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Robot UUID',
  `result` char(1) COLLATE utf8_bin NOT NULL COMMENT 'Kết Quả (S:Success, F:Failed)',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Process order history';

-- Dumping data for table eport.process_history: ~0 rows (approximately)
/*!40000 ALTER TABLE `process_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `process_history` ENABLE KEYS */;

-- Dumping structure for table eport.process_order
DROP TABLE IF EXISTS `process_order`;
CREATE TABLE IF NOT EXISTS `process_order` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `shipment_id` bigint(20) NOT NULL COMMENT 'Mã Lô',
  `service_type` tinyint(1) NOT NULL COMMENT 'Loại dịch vụ (bốc, hạ, gate)',
  `reference_no` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Mã Tham Chiếu',
  `pay_type` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'PT thanh toán',
  `sztp` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Kích thước cont',
  `mode` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Loại lệnh',
  `consignee` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Chủ hàng',
  `truck_co` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'MST-Tên cty',
  `tax_code` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'MST',
  `bl_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'Billing No',
  `booking_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'Booking no',
  `pickup_date` datetime DEFAULT NULL COMMENT 'Ngày bốc',
  `vessel` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Tàu',
  `voyage` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Chuyến',
  `before_after` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Trước-Sau',
  `year` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Năm',
  `cont_number` int(10) NOT NULL COMMENT 'Số lượng container',
  `status` tinyint(1) DEFAULT '0' COMMENT 'Trạng thái: 0 waiting, 1: processing, 2:done',
  `result` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Kết quả (F:Failed,S:Success)',
  `data` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT 'Detail Data (Json)',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Process order';

-- Dumping data for table eport.process_order: ~4 rows (approximately)
/*!40000 ALTER TABLE `process_order` DISABLE KEYS */;
INSERT IGNORE INTO `process_order` (`id`, `shipment_id`, `service_type`, `reference_no`, `pay_type`, `sztp`, `mode`, `consignee`, `truck_co`, `tax_code`, `bl_no`, `booking_no`, `pickup_date`, `vessel`, `voyage`, `before_after`, `year`, `cont_number`, `status`, `result`, `data`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
	(93, 0, 0, NULL, 'Cash', '45R0', 'Truck Out', 'Khach hang', '0100100110 : VIỆN NGHIÊN CỨU CƠ KHÍ', '1', 'SMJ2014SDA701', NULL, '2020-06-19 20:29:35', 'SIMO0286', NULL, 'Before', '2020', 3, 0, NULL, NULL, NULL, NULL, NULL, NULL),
	(96, 0, 2, NULL, 'Cash', NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL, 'vslNm', 'voyNo', 'Before', '2020', 2, 0, NULL, NULL, NULL, NULL, NULL, NULL),
	(98, 0, 0, NULL, 'Cash', NULL, NULL, NULL, NULL, '111110101', NULL, NULL, NULL, 'SDA', '122', 'Before', '2020', 1, 0, NULL, NULL, NULL, NULL, NULL, NULL),
	(99, 0, 0, NULL, 'Cash', NULL, NULL, NULL, NULL, '111110101', NULL, NULL, NULL, 'SDA', '122', 'Before', '2020', 1, 0, NULL, NULL, NULL, NULL, NULL, NULL);
/*!40000 ALTER TABLE `process_order` ENABLE KEYS */;

-- Dumping structure for table eport.qrtz_blob_triggers
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE IF NOT EXISTS `qrtz_blob_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `blob_data` blob,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table eport.qrtz_blob_triggers: ~0 rows (approximately)
/*!40000 ALTER TABLE `qrtz_blob_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_blob_triggers` ENABLE KEYS */;

-- Dumping structure for table eport.qrtz_calendars
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE IF NOT EXISTS `qrtz_calendars` (
  `sched_name` varchar(120) NOT NULL,
  `calendar_name` varchar(200) NOT NULL,
  `calendar` blob NOT NULL,
  PRIMARY KEY (`sched_name`,`calendar_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table eport.qrtz_calendars: ~0 rows (approximately)
/*!40000 ALTER TABLE `qrtz_calendars` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_calendars` ENABLE KEYS */;

-- Dumping structure for table eport.qrtz_cron_triggers
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE IF NOT EXISTS `qrtz_cron_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `cron_expression` varchar(200) NOT NULL,
  `time_zone_id` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table eport.qrtz_cron_triggers: ~0 rows (approximately)
/*!40000 ALTER TABLE `qrtz_cron_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_cron_triggers` ENABLE KEYS */;

-- Dumping structure for table eport.qrtz_fired_triggers
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE IF NOT EXISTS `qrtz_fired_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `entry_id` varchar(95) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `instance_name` varchar(200) NOT NULL,
  `fired_time` bigint(13) NOT NULL,
  `sched_time` bigint(13) NOT NULL,
  `priority` int(11) NOT NULL,
  `state` varchar(16) NOT NULL,
  `job_name` varchar(200) DEFAULT NULL,
  `job_group` varchar(200) DEFAULT NULL,
  `is_nonconcurrent` varchar(1) DEFAULT NULL,
  `requests_recovery` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`sched_name`,`entry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table eport.qrtz_fired_triggers: ~0 rows (approximately)
/*!40000 ALTER TABLE `qrtz_fired_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_fired_triggers` ENABLE KEYS */;

-- Dumping structure for table eport.qrtz_job_details
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE IF NOT EXISTS `qrtz_job_details` (
  `sched_name` varchar(120) NOT NULL,
  `job_name` varchar(200) NOT NULL,
  `job_group` varchar(200) NOT NULL,
  `description` varchar(250) DEFAULT NULL,
  `job_class_name` varchar(250) NOT NULL,
  `is_durable` varchar(1) NOT NULL,
  `is_nonconcurrent` varchar(1) NOT NULL,
  `is_update_data` varchar(1) NOT NULL,
  `requests_recovery` varchar(1) NOT NULL,
  `job_data` blob,
  PRIMARY KEY (`sched_name`,`job_name`,`job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table eport.qrtz_job_details: ~0 rows (approximately)
/*!40000 ALTER TABLE `qrtz_job_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_job_details` ENABLE KEYS */;

-- Dumping structure for table eport.qrtz_locks
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE IF NOT EXISTS `qrtz_locks` (
  `sched_name` varchar(120) NOT NULL,
  `lock_name` varchar(40) NOT NULL,
  PRIMARY KEY (`sched_name`,`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table eport.qrtz_locks: ~0 rows (approximately)
/*!40000 ALTER TABLE `qrtz_locks` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_locks` ENABLE KEYS */;

-- Dumping structure for table eport.qrtz_paused_trigger_grps
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE IF NOT EXISTS `qrtz_paused_trigger_grps` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  PRIMARY KEY (`sched_name`,`trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table eport.qrtz_paused_trigger_grps: ~0 rows (approximately)
/*!40000 ALTER TABLE `qrtz_paused_trigger_grps` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_paused_trigger_grps` ENABLE KEYS */;

-- Dumping structure for table eport.qrtz_scheduler_state
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE IF NOT EXISTS `qrtz_scheduler_state` (
  `sched_name` varchar(120) NOT NULL,
  `instance_name` varchar(200) NOT NULL,
  `last_checkin_time` bigint(13) NOT NULL,
  `checkin_interval` bigint(13) NOT NULL,
  PRIMARY KEY (`sched_name`,`instance_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table eport.qrtz_scheduler_state: ~0 rows (approximately)
/*!40000 ALTER TABLE `qrtz_scheduler_state` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_scheduler_state` ENABLE KEYS */;

-- Dumping structure for table eport.qrtz_simple_triggers
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE IF NOT EXISTS `qrtz_simple_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `repeat_count` bigint(7) NOT NULL,
  `repeat_interval` bigint(12) NOT NULL,
  `times_triggered` bigint(10) NOT NULL,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table eport.qrtz_simple_triggers: ~0 rows (approximately)
/*!40000 ALTER TABLE `qrtz_simple_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_simple_triggers` ENABLE KEYS */;

-- Dumping structure for table eport.qrtz_simprop_triggers
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE IF NOT EXISTS `qrtz_simprop_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `str_prop_1` varchar(512) DEFAULT NULL,
  `str_prop_2` varchar(512) DEFAULT NULL,
  `str_prop_3` varchar(512) DEFAULT NULL,
  `int_prop_1` int(11) DEFAULT NULL,
  `int_prop_2` int(11) DEFAULT NULL,
  `long_prop_1` bigint(20) DEFAULT NULL,
  `long_prop_2` bigint(20) DEFAULT NULL,
  `dec_prop_1` decimal(13,4) DEFAULT NULL,
  `dec_prop_2` decimal(13,4) DEFAULT NULL,
  `bool_prop_1` varchar(1) DEFAULT NULL,
  `bool_prop_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table eport.qrtz_simprop_triggers: ~0 rows (approximately)
/*!40000 ALTER TABLE `qrtz_simprop_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_simprop_triggers` ENABLE KEYS */;

-- Dumping structure for table eport.qrtz_triggers
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE IF NOT EXISTS `qrtz_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `job_name` varchar(200) NOT NULL,
  `job_group` varchar(200) NOT NULL,
  `description` varchar(250) DEFAULT NULL,
  `next_fire_time` bigint(13) DEFAULT NULL,
  `prev_fire_time` bigint(13) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `trigger_state` varchar(16) NOT NULL,
  `trigger_type` varchar(8) NOT NULL,
  `start_time` bigint(13) NOT NULL,
  `end_time` bigint(13) DEFAULT NULL,
  `calendar_name` varchar(200) DEFAULT NULL,
  `misfire_instr` smallint(2) DEFAULT NULL,
  `job_data` blob,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  KEY `sched_name` (`sched_name`,`job_name`,`job_group`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table eport.qrtz_triggers: ~0 rows (approximately)
/*!40000 ALTER TABLE `qrtz_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_triggers` ENABLE KEYS */;

-- Dumping structure for table eport.shipment
DROP TABLE IF EXISTS `shipment`;
CREATE TABLE IF NOT EXISTS `shipment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `logistic_account_id` bigint(20) NOT NULL,
  `logistic_group_id` bigint(20) NOT NULL,
  `service_type` tinyint(1) NOT NULL COMMENT 'Dich Vu',
  `bl_no` varchar(20) COLLATE utf8_bin NOT NULL,
  `booking_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'Booking Number',
  `tax_code` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'MST',
  `container_amount` int(11) NOT NULL COMMENT 'So Luong Container',
  `edo_flg` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'EDO Flag (1,0)',
  `reference_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'So Tham Chieu CATOS',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Shipment';

-- Dumping data for table eport.shipment: ~29 rows (approximately)
/*!40000 ALTER TABLE `shipment` DISABLE KEYS */;
INSERT IGNORE INTO `shipment` (`id`, `logistic_account_id`, `logistic_group_id`, `service_type`, `bl_no`, `booking_no`, `tax_code`, `container_amount`, `edo_flg`, `reference_no`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
	(1, 1, 1, 1, 'asdfaf', NULL, '123123', 11, '1', NULL, 'asdf', 'nguyen trong hieu', '2020-05-28 21:05:15', 'nguyen trong hieu', '2020-05-30 08:49:23'),
	(2, 1, 1, 1, 'BILL1234', NULL, '12344123', 10, '1', NULL, 'ádfffffffffffádfádfádfà', 'nguyen trong hieu', '2020-05-29 09:39:15', 'nguyen trong hieu', '2020-05-30 08:53:06'),
	(3, 1, 1, 2, 'null', NULL, '123123165', 23, '0', NULL, '', 'nguyen trong hieu', '2020-05-29 21:36:44', 'nguyen trong hieu', '2020-06-05 17:30:28'),
	(4, 1, 1, 1, 'sdfsad', NULL, '1213', 11, '1', NULL, 'ádfsadf', 'nguyen trong hieu', '2020-05-30 06:51:43', 'nguyen trong hieu', '2020-06-17 15:49:00'),
	(5, 1, 1, 1, 'sadasd', NULL, '123123', 1, '0', NULL, 'sdfasd', 'nguyen trong hieu', '2020-05-30 07:02:03', 'nguyen trong hieu', '2020-05-30 08:49:15'),
	(6, 1, 1, 2, 'null', NULL, 'asdfasd', 13, '0', NULL, '', 'nguyen trong hieu', '2020-05-30 07:13:56', 'nguyen trong hieu', '2020-06-05 17:30:24'),
	(7, 1, 1, 2, 'null', NULL, 'asdf', 11, '0', NULL, '', 'nguyen trong hieu', '2020-06-03 08:08:31', 'nguyen trong hieu', '2020-06-05 17:30:15'),
	(8, 1, 1, 2, 'ádfsad', NULL, 'sdfádf', 1, '1', NULL, '', 'nguyen trong hieu', '2020-06-05 15:41:31', 'nguyen trong hieu', '2020-06-05 17:15:24'),
	(9, 1, 1, 2, 'null', NULL, '123123', 11, '0', NULL, NULL, 'nguyen trong hieu', '2020-06-06 08:18:14', '', NULL),
	(10, 1, 1, 3, 'null', NULL, '123213', 11, NULL, NULL, NULL, 'nguyen trong hieu', '2020-06-06 09:52:11', '', NULL),
	(11, 1, 1, 3, 'null', NULL, '1231', 1, NULL, NULL, NULL, 'nguyen trong hieu', '2020-06-06 09:54:03', '', NULL),
	(12, 1, 1, 4, 'null', NULL, '1231231', 11, NULL, NULL, NULL, 'nguyen trong hieu', '2020-06-08 08:02:10', '', NULL),
	(13, 1, 1, 1, 'SMJ2014SDA7012', NULL, '12123', 11, '1', NULL, '', 'nguyen trong hieu', '2020-06-08 09:27:56', 'nguyen trong hieu', '2020-06-19 20:18:53'),
	(14, 1, 1, 1, 'SMJ2014SDA7011', NULL, '123123', 11, '1', NULL, '', 'nguyen trong hieu', '2020-06-11 13:17:40', 'nguyen trong hieu', '2020-06-19 20:18:48'),
	(15, 1, 1, 2, 'null', NULL, '1231', 11, '0', NULL, '', 'nguyen trong hieu', '2020-06-11 19:30:26', 'nguyen trong hieu', '2020-06-18 14:58:26'),
	(16, 1, 1, 1, 'ádfádf', NULL, '11', 11, '0', NULL, NULL, 'nguyen trong hieu', '2020-06-12 12:02:06', '', NULL),
	(17, 1, 1, 1, 'adfsadf', NULL, '1', 1, '1', NULL, NULL, 'nguyen trong hieu', '2020-06-12 12:08:33', '', NULL),
	(18, 1, 1, 1, 'DBA0226205', NULL, '11', 11, '1', NULL, '', 'nguyen trong hieu', '2020-06-17 16:50:55', 'nguyen trong hieu', '2020-06-17 16:51:33'),
	(19, 1, 1, 2, 'null', NULL, '1', 11, NULL, NULL, NULL, 'nguyen trong hieu', '2020-06-18 15:03:32', '', NULL),
	(20, 1, 1, 2, 'null', NULL, '123123', 2, NULL, NULL, 'sdà', 'nguyen trong hieu', '2020-06-18 17:32:04', '', NULL),
	(21, 1, 1, 3, 'null', NULL, '11', 11, NULL, NULL, NULL, 'nguyen trong hieu', '2020-06-18 20:18:56', '', NULL),
	(22, 1, 1, 3, 'null', 'asdf', '11', 11, NULL, NULL, '', 'nguyen trong hieu', '2020-06-18 20:23:09', 'nguyen trong hieu', '2020-06-19 08:48:40'),
	(23, 1, 1, 3, 'null', 'asdf', '1', 1, NULL, NULL, NULL, 'nguyen trong hieu', '2020-06-19 08:49:04', '', NULL),
	(24, 1, 1, 3, 'null', 'asdf', '1', 11, NULL, NULL, NULL, 'nguyen trong hieu', '2020-06-19 08:50:37', '', NULL),
	(25, 1, 1, 4, 'null', 'sad', '112', 1, NULL, NULL, NULL, 'nguyen trong hieu', '2020-06-19 12:48:24', '', NULL),
	(26, 1, 1, 4, 'null', 'sadd', '1', 2, NULL, NULL, '', 'nguyen trong hieu', '2020-06-19 12:49:22', 'nguyen trong hieu', '2020-06-19 13:36:12'),
	(27, 1, 1, 4, 'null', 's', '123', 11, NULL, NULL, '', 'nguyen trong hieu', '2020-06-19 12:49:53', 'nguyen trong hieu', '2020-06-19 13:48:02'),
	(28, 1, 1, 1, 'SMJ2014SDA701', NULL, '1', 11, '1', NULL, NULL, 'nguyen trong hieu', '2020-06-19 20:19:01', '', NULL),
	(29, 1, 1, 4, 'null', 'BFDKjdfj', '111110101', 3, NULL, NULL, NULL, 'nguyen trong hieu', '2020-06-19 22:53:45', '', NULL);
/*!40000 ALTER TABLE `shipment` ENABLE KEYS */;

-- Dumping structure for table eport.shipment_custom
DROP TABLE IF EXISTS `shipment_custom`;
CREATE TABLE IF NOT EXISTS `shipment_custom` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `shipment_id` bigint(20) NOT NULL COMMENT 'Shipment ID',
  `custom_declare_no` varchar(12) COLLATE utf8_bin NOT NULL COMMENT 'Số Tờ Khai HQ',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Shipment custom: Hai quan';

-- Dumping data for table eport.shipment_custom: ~0 rows (approximately)
/*!40000 ALTER TABLE `shipment_custom` DISABLE KEYS */;
/*!40000 ALTER TABLE `shipment_custom` ENABLE KEYS */;

-- Dumping structure for table eport.shipment_detail
DROP TABLE IF EXISTS `shipment_detail`;
CREATE TABLE IF NOT EXISTS `shipment_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `logistic_group_id` bigint(20) NOT NULL,
  `shipment_id` bigint(20) NOT NULL COMMENT 'Ma Lo',
  `process_order_id` bigint(20) DEFAULT NULL COMMENT 'Ma Lenh',
  `register_no` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ma DK',
  `container_no` varchar(12) COLLATE utf8_bin NOT NULL COMMENT 'Container Number',
  `container_status` varchar(3) COLLATE utf8_bin DEFAULT NULL COMMENT 'Container Status (S,D)',
  `sztp` varchar(4) COLLATE utf8_bin NOT NULL COMMENT 'Size Type',
  `fe` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'FE',
  `bl_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'BL number',
  `booking_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'Booking Number',
  `seal_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'Seal Number',
  `consignee` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Shipper/consignee',
  `expired_dem` datetime DEFAULT NULL COMMENT 'Han Lenh',
  `wgt` int(11) NOT NULL COMMENT 'Weight ',
  `vsl_nm` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'Vessel name',
  `voy_no` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'Voyage',
  `ope_code` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'Operator Code',
  `loading_port` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Cang Xep Hang',
  `discharge_port` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Cang Do Hang',
  `transport_type` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Phuong Tien',
  `empty_depot` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Noi Ha Vo',
  `cargo_type` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Cargo Type',
  `vgm_chk` bit(1) DEFAULT NULL COMMENT 'VGM Check',
  `vgm` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'VGM',
  `vgm_person_info` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT 'VGM Person Info',
  `preorder_pickup` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Boc Chi Dinh (Y,N)',
  `shifting_cont_number` int(5) DEFAULT NULL COMMENT 'Số lượng dịch chuyển',
  `custom_status` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Custom Status (N,C,H,R)',
  `payment_status` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Payment Status (Y,N,W,E)',
  `process_status` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Process Status(Y,N,E)',
  `do_status` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'T.T DO Goc',
  `do_received_time` datetime DEFAULT NULL COMMENT 'Ngay Nhan DO Goc',
  `user_verify_status` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Xac Thuc (Y,N)',
  `status` tinyint(4) DEFAULT NULL COMMENT 'Status',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Shipment Details';

-- Dumping data for table eport.shipment_detail: ~80 rows (approximately)
/*!40000 ALTER TABLE `shipment_detail` DISABLE KEYS */;
INSERT IGNORE INTO `shipment_detail` (`id`, `logistic_group_id`, `shipment_id`, `process_order_id`, `register_no`, `container_no`, `container_status`, `sztp`, `fe`, `bl_no`, `booking_no`, `seal_no`, `consignee`, `expired_dem`, `wgt`, `vsl_nm`, `voy_no`, `ope_code`, `loading_port`, `discharge_port`, `transport_type`, `empty_depot`, `cargo_type`, `vgm_chk`, `vgm`, `vgm_person_info`, `preorder_pickup`, `shifting_cont_number`, `custom_status`, `payment_status`, `process_status`, `do_status`, `do_received_time`, `user_verify_status`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
	(1, 1, 1, NULL, '11', 'CONT1234560', NULL, '22G0', 'F', 'asdfaf', NULL, 'G8331306', 'VINCOSHIP', '2020-05-28 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'Y', 4, 'R', 'N', 'Y', 'N', NULL, 'Y', 3, NULL, 'nguyen trong hieu', '2020-05-28 00:08:54', NULL, '2020-06-12 20:53:13'),
	(2, 1, 1, NULL, '12', 'CONT1234561', NULL, '22G0', 'F', 'asdfaf', NULL, 'G8331306', 'VINCOSHIP', '2020-05-28 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'Y', 'Y', 'N', NULL, 'Y', 4, NULL, 'nguyen trong hieu', '2020-05-28 21:08:54', NULL, '2020-05-29 19:00:15'),
	(3, 1, 1, NULL, '13', 'CONT1234562', NULL, '22G0', 'F', 'asdfaf', NULL, 'G8331306', 'VINCOSHIP', '2020-05-28 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'Y', 'N', NULL, 'Y', 3, NULL, 'nguyen trong hieu', '2020-05-28 21:08:54', NULL, '2020-06-12 20:53:13'),
	(4, 1, 1, NULL, '14', 'CONT1234563', NULL, '22G0', 'F', 'asdfaf', NULL, 'G8331306', 'VINCOSHIP', '2020-05-28 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'Y', 'Y', 'N', NULL, 'Y', 4, NULL, 'nguyen trong hieu', '2020-05-28 21:08:55', NULL, '2020-05-28 21:10:02'),
	(5, 1, 1, NULL, '15', 'CONT1234564', NULL, '22G0', 'F', 'asdfaf', NULL, 'G8331306', 'VINCOSHIP', '2020-05-28 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'N', 'N', 'N', 'N', NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-05-28 21:08:55', NULL, NULL),
	(6, 1, 1, NULL, '16', 'CONT1234565', NULL, '22G0', 'F', 'asdfaf', NULL, 'G8331306', 'VINCOSHIP', '2020-05-28 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'Y', 'Y', 'N', NULL, 'Y', 4, NULL, 'nguyen trong hieu', '2020-05-28 21:08:55', NULL, '2020-05-28 21:10:02'),
	(7, 1, 1, NULL, '17', 'CONT1234566', NULL, '22G0', 'F', 'asdfaf', NULL, 'G8331306', 'VINCOSHIP', '2020-05-28 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'N', 'N', 'N', 'N', NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-05-28 21:08:55', NULL, NULL),
	(8, 1, 1, NULL, '18', 'CONT1234567', NULL, '22G0', 'F', 'asdfaf', NULL, 'G8331306', 'VINCOSHIP', '2020-05-28 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'Y', 'Y', 'N', NULL, 'Y', 4, NULL, 'nguyen trong hieu', '2020-05-28 21:08:55', NULL, '2020-05-28 21:10:02'),
	(9, 1, 1, NULL, '19', 'CONT1234568', NULL, '22G0', 'F', 'asdfaf', NULL, 'G8331306', 'VINCOSHIP', '2020-05-28 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'Y', 'Y', 'N', NULL, 'Y', 4, NULL, 'nguyen trong hieu', '2020-05-28 21:08:55', NULL, '2020-05-28 21:10:03'),
	(10, 1, 1, NULL, '110', 'CONT1234569', NULL, '22G0', 'F', 'asdfaf', NULL, 'G8331306', 'VINCOSHIP', '2020-05-28 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'Y', 'Y', 'N', NULL, 'Y', 4, NULL, 'nguyen trong hieu', '2020-05-28 21:08:55', NULL, '2020-05-28 21:10:03'),
	(11, 1, 2, NULL, '21', 'CONT1234560', NULL, '22G0', 'F', 'BILL1234', NULL, 'G8331306', 'VINCOSHIP', '2020-05-29 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'N', 'N', 'N', 'N', NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-05-29 09:39:56', NULL, NULL),
	(12, 1, 2, NULL, '22', 'CONT1234561', NULL, '22G0', 'F', 'BILL1234', NULL, 'G8331306', 'VINCOSHIP', '2020-05-29 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'Y', 'Y', 'N', NULL, 'Y', 4, NULL, 'nguyen trong hieu', '2020-05-29 09:39:58', NULL, '2020-05-29 09:44:19'),
	(13, 1, 2, NULL, '23', 'CONT1234562', NULL, '22G0', 'F', 'BILL1234', NULL, 'G8331306', 'VINCOSHIP', '2020-05-29 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'N', 'N', 'N', 'N', NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-05-29 09:39:58', NULL, NULL),
	(14, 1, 2, NULL, '24', 'CONT1234563', NULL, '22G0', 'F', 'BILL1234', NULL, 'G8331306', 'VINCOSHIP', '2020-05-29 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'Y', 3, 'R', 'Y', 'Y', 'N', NULL, 'Y', 3, NULL, 'nguyen trong hieu', '2020-05-29 02:39:58', NULL, '2020-06-12 20:57:58'),
	(15, 1, 2, NULL, '25', 'CONT1234564', NULL, '22G0', 'F', 'BILL1234', NULL, 'G8331306', 'VINCOSHIP', '2020-05-29 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'N', 'N', NULL, 'Y', 2, NULL, 'nguyen trong hieu', '2020-05-29 09:39:58', NULL, '2020-06-12 20:57:58'),
	(16, 1, 2, NULL, '26', 'CONT1234565', NULL, '22G0', 'F', 'BILL1234', NULL, 'G8331306', 'VINCOSHIP', '2020-05-29 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'N', 'N', 'N', 'N', NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-05-29 09:39:58', NULL, NULL),
	(17, 1, 2, NULL, '27', 'CONT1234566', NULL, '22G0', 'F', 'BILL1234', NULL, 'G8331306', 'VINCOSHIP', '2020-05-29 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'N', 'N', 'N', 'N', NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-05-29 09:39:58', NULL, NULL),
	(18, 1, 2, NULL, '28', 'CONT1234567', NULL, '22G0', 'F', 'BILL1234', NULL, 'G8331306', 'VINCOSHIP', '2020-05-29 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'Y', 3, 'R', 'N', 'Y', 'N', NULL, 'Y', 3, NULL, 'nguyen trong hieu', '2020-05-29 02:39:58', NULL, '2020-06-12 20:57:58'),
	(19, 1, 2, NULL, '29', 'CONT1234568', NULL, '22G0', 'F', 'BILL1234', NULL, 'G8331306', 'VINCOSHIP', '2020-05-29 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'Y', 'N', NULL, 'Y', 3, NULL, 'nguyen trong hieu', '2020-05-29 09:39:58', NULL, '2020-06-12 20:55:17'),
	(20, 1, 2, NULL, '210', 'CONT1234569', NULL, '22G0', 'F', 'BILL1234', NULL, 'G8331306', 'VINCOSHIP', '2020-05-29 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'Y', 'N', NULL, 'Y', 3, NULL, 'nguyen trong hieu', '2020-05-29 09:39:58', NULL, '2020-06-12 20:55:17'),
	(22, 1, 3, NULL, '31', 'CONT1234343', NULL, 'asdf', NULL, NULL, NULL, NULL, 'asdf', '2020-06-26 00:00:00', 1, 'vslNm', 'voyNo', 'opeCode', 'asdfsa', 'dsaf', 'sdfs', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', 'Y', NULL, NULL, 'Y', 2, NULL, 'nguyen trong hieu', '2020-05-29 22:22:10', 'nguyen trong hieu', '2020-06-04 16:06:07'),
	(23, 1, 3, NULL, '32', 'CONT1234342', NULL, 'asd', NULL, NULL, NULL, NULL, 'fsdf', '2020-07-25 00:00:00', 1, 'vslNm', 'voyNo', 'opeCode', 'sadf', 'df', 'df', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y', 'Y', NULL, NULL, 'Y', 3, NULL, 'nguyen trong hieu', '2020-05-29 22:22:34', 'nguyen trong hieu', '2020-06-04 16:06:07'),
	(25, 1, 4, NULL, '41', 'CONT1234560', NULL, '22G0', 'F', 'sdfsad', NULL, 'G8331306', 'VINCOSHIP', '2020-05-30 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'N', 'N', 'N', 'N', NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-05-30 06:54:48', 'nguyen trong hieu', '2020-06-17 15:49:03'),
	(26, 1, 4, NULL, '42', 'CONT1234561', NULL, '22G0', 'F', 'sdfsad', NULL, 'G8331306', 'VINCOSHIP', '2020-05-30 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'Y', 'Y', 'N', NULL, 'Y', 4, NULL, 'nguyen trong hieu', '2020-05-30 06:54:48', 'nguyen trong hieu', '2020-06-17 15:49:03'),
	(27, 1, 4, NULL, '43', 'CONT1234562', NULL, '22G0', 'F', 'sdfsad', NULL, 'G8331306', 'VINCOSHIP', '2020-05-30 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'Y', 'Y', 'N', NULL, 'Y', 4, NULL, 'nguyen trong hieu', '2020-05-30 06:54:48', 'nguyen trong hieu', '2020-06-17 15:49:03'),
	(28, 1, 4, NULL, '44', 'CONT1234563', NULL, '22G0', 'F', 'sdfsad', NULL, 'G8331306', 'VINCOSHIP', '2020-05-30 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'Y', 'Y', 'N', NULL, 'Y', 4, NULL, 'nguyen trong hieu', '2020-05-30 06:54:48', 'nguyen trong hieu', '2020-06-17 15:49:03'),
	(29, 1, 4, NULL, '45', 'CONT1234564', NULL, '22G0', 'F', 'sdfsad', NULL, 'G8331306', 'VINCOSHIP', '2020-05-30 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'N', 'N', 'N', 'N', NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-05-30 06:54:48', 'nguyen trong hieu', '2020-06-17 15:49:03'),
	(30, 1, 4, NULL, '46', 'CONT1234565', NULL, '22G0', 'F', 'sdfsad', NULL, 'G8331306', 'VINCOSHIP', '2020-05-30 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'Y', 'N', NULL, 'Y', 3, NULL, 'nguyen trong hieu', '2020-05-30 06:54:48', 'nguyen trong hieu', '2020-06-17 15:49:03'),
	(31, 1, 4, NULL, '47', 'CONT1234566', NULL, '22G0', 'F', 'sdfsad', NULL, 'G8331306', 'VINCOSHIP', '2020-05-30 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'Y', 'N', NULL, 'Y', 3, NULL, 'nguyen trong hieu', '2020-05-30 06:54:48', 'nguyen trong hieu', '2020-06-17 15:49:03'),
	(32, 1, 4, NULL, '48', 'CONT1234567', NULL, '22G0', 'F', 'sdfsad', NULL, 'G8331306', 'VINCOSHIP', '2020-05-30 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'N', 'N', NULL, NULL, 2, NULL, 'nguyen trong hieu', '2020-05-30 06:54:49', 'nguyen trong hieu', '2020-06-17 15:49:03'),
	(33, 1, 4, NULL, '49', 'CONT1234568', NULL, '22G0', 'F', 'sdfsad', NULL, 'G8331306', 'VINCOSHIP', '2020-05-30 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'N', 'N', 'N', 'N', NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-05-30 06:54:49', 'nguyen trong hieu', '2020-06-17 15:49:03'),
	(34, 1, 4, NULL, '410', 'CONT1234569', NULL, '22G0', 'F', 'sdfsad', NULL, 'G8331306', 'VINCOSHIP', '2020-05-30 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'N', 'N', NULL, NULL, 2, NULL, 'nguyen trong hieu', '2020-05-30 06:54:49', 'nguyen trong hieu', '2020-06-17 15:49:03'),
	(35, 1, 5, NULL, '51', 'CONT1231231', NULL, '22G0', 'F', 'sadasd', NULL, 'G8331306', 'VINCOSHIP', '2020-05-30 00:00:00', 11, 'Vessel', 'Voyage', 'CMC', 'LoadingPort', 'dischargePort', 'Truck', 'emptyDepot', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'N', 'N', NULL, 'Y', 2, 'Ghi chu', 'nguyen trong hieu', '2020-05-30 07:02:16', 'nguyen trong hieu', '2020-05-30 07:05:21'),
	(36, 1, 6, NULL, '61', 'CONT1234123', NULL, 'sadf', NULL, NULL, NULL, NULL, 'sdf', '2020-06-11 00:00:00', 1, 'vslNm', 'voyNo', 'opeCode', 'sdf', 'sdf', 'sadf', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y', 'Y', NULL, NULL, 'Y', 3, 'sdf', 'nguyen trong hieu', '2020-05-30 07:14:20', 'nguyen trong hieu', '2020-06-12 11:07:32'),
	(37, 1, 6, NULL, '62', 'DFDD2342344', NULL, 'df', NULL, NULL, NULL, NULL, NULL, '2020-06-03 00:00:00', 1, 'vslNm', 'voyNo', 'rr', 'df', 'df', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y', 'Y', NULL, NULL, 'Y', 3, NULL, 'nguyen trong hieu', '2020-06-03 06:33:27', 'nguyen trong hieu', '2020-06-12 11:07:32'),
	(39, 1, 7, NULL, '71', 'CONT1234123', NULL, '22G0', NULL, NULL, NULL, NULL, NULL, '2020-06-10 00:00:00', 1, 'vslNm', 'voyNo', 'MCM', ' ', ' ', 'Truck', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y', 'Y', NULL, NULL, 'Y', 3, NULL, 'nguyen trong hieu', '2020-06-03 11:08:13', 'nguyen trong hieu', '2020-06-06 14:08:52'),
	(47, 1, 10, NULL, '101', 'containerNo', NULL, '22G0', NULL, NULL, '123123', NULL, NULL, '2020-06-15 00:00:00', 1, 'Vessel 1', ' ', 'MCM', ' ', 'Cảng đích 1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y', 'Y', NULL, NULL, 'Y', 3, NULL, 'nguyen trong hieu', '2020-06-06 11:37:40', 'nguyen trong hieu', '2020-06-06 13:32:35'),
	(48, 1, 10, NULL, '102', 'containerNo', NULL, '23G0', NULL, NULL, '2131', NULL, NULL, '2020-06-15 00:00:00', 1, ' ', ' ', 'NIC', ' ', 'voyNo', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y', 'Y', NULL, NULL, 'Y', 3, NULL, 'nguyen trong hieu', '2020-06-06 11:38:35', 'nguyen trong hieu', '2020-06-06 13:32:35'),
	(49, 1, 10, NULL, '103', 'containerNo', NULL, '23G0', NULL, NULL, '12', NULL, NULL, '2020-06-06 00:00:00', 1, ' ', ' ', 'NIC', ' ', 'voyNo', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y', 'Y', NULL, NULL, 'Y', 3, NULL, 'nguyen trong hieu', '2020-06-06 11:45:01', 'nguyen trong hieu', '2020-06-06 13:32:35'),
	(53, 1, 11, NULL, '111', 'containerNo', NULL, '22G0', NULL, NULL, '2131', NULL, NULL, '2020-06-06 00:00:00', 1, ' ', ' ', 'NIC', ' ', ' ', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', 'N', NULL, NULL, 'Y', 1, NULL, 'nguyen trong hieu', '2020-06-06 14:07:40', NULL, '2020-06-11 21:18:30'),
	(54, 1, 7, NULL, '72', 'CONT2342342', NULL, '23G0', NULL, NULL, NULL, NULL, NULL, '2020-06-06 00:00:00', 1, 'vslNm', 'voyNo', 'NIC', ' ', ' ', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', 'N', NULL, NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-06-06 14:08:52', NULL, NULL),
	(56, 1, 12, NULL, '121', 'CONT1231231', NULL, '22G0', NULL, NULL, '123123', NULL, NULL, NULL, 11, 'Vessel 1', 'Voyage 1', 'MCM', 'Cảng nguồn 1', NULL, 'Truck 1', NULL, NULL, b'1', '11', 'sdsf', NULL, NULL, NULL, 'Y', 'Y', NULL, NULL, 'Y', 3, 'asfsdfasdf', 'nguyen trong hieu', '2020-06-08 09:20:19', NULL, '2020-06-08 09:20:36'),
	(60, 1, 14, NULL, '141', 'AAMU4000665', NULL, '45R0', 'F', 'SMJ2014SDA701', NULL, 'SITH603670', 'VINAL', '2020-06-18 00:00:00', 26200, 'SITC MOJI', 'SIMO0286', 'SIT', 'HKHKG', 'VNDAD', 'Vessel', 'Cảng Tiên Sa', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'Y', 'N', 'N', NULL, 'Y', 4, NULL, 'nguyen trong hieu', '2020-06-11 13:54:02', 'nguyen trong hieu', '2020-06-17 16:34:41'),
	(61, 1, 14, NULL, '142', 'SEGU9358519', NULL, '45R0', 'F', 'SMJ2014SDA701', NULL, 'SITH603666', 'MAVI', '2020-06-18 00:00:00', 26200, 'SITC MOJI', 'SIMO0286', 'SIT', 'HKHKG', 'VNDAD', 'Vessel', 'Cảng khác', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'Y', 'Y', 'N', NULL, 'Y', 4, NULL, 'nguyen trong hieu', '2020-06-11 13:54:03', 'nguyen trong hieu', '2020-06-17 16:34:41'),
	(63, 1, 15, NULL, '151', 'CONT1231231', NULL, '22G0', NULL, NULL, NULL, NULL, NULL, '2020-06-11 00:00:00', 1, 'vslNm', 'voyNo', 'MCM', 'Cảng nguồn 1', 'Cảng đích 1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y', 'Y', NULL, NULL, 'Y', 3, NULL, 'nguyen trong hieu', '2020-06-11 19:31:04', 'nguyen trong hieu', '2020-06-18 19:11:12'),
	(64, 1, 15, NULL, '152', 'CONT1234432', NULL, '23G0', NULL, NULL, NULL, NULL, NULL, '2020-06-11 00:00:00', 1, 'vslNm', 'voyNo', 'MCM', 'Cảng nguồn 2', 'Cảng đích 2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', 'N', NULL, NULL, 'Y', 1, NULL, 'nguyen trong hieu', '2020-06-11 19:31:46', 'nguyen trong hieu', '2020-06-11 20:52:38'),
	(66, 1, 13, NULL, '131', 'AAMU4000665', NULL, '45R0', 'F', 'SMJ2014SDA701', NULL, 'SITH603670', 'MAVI', '2020-06-18 00:00:00', 26200, 'SITC MOJI', 'SIMO0286', 'SIT', 'HKHKG', 'VNDAD', 'Vessel', 'Cảng Tiên Sa', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'N', 'N', NULL, NULL, 2, NULL, 'nguyen trong hieu', '2020-06-17 16:49:15', NULL, '2020-06-17 16:49:27'),
	(67, 1, 13, NULL, '132', 'SEGU9358519', NULL, '45R0', 'F', 'SMJ2014SDA701', NULL, 'SITH603666', 'MAVI', '2020-06-18 00:00:00', 26200, 'SITC MOJI', 'SIMO0286', 'SIT', 'HKHKG', 'VNDAD', 'Vessel', 'Cảng Tiên Sa', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'N', 'N', NULL, NULL, 2, NULL, 'nguyen trong hieu', '2020-06-17 16:49:16', NULL, '2020-06-17 16:49:27'),
	(68, 1, 13, NULL, '133', 'TLLU1130582', NULL, '45R0', 'F', 'SMJ2014SDA701', NULL, 'SITH603844', 'MAVI', '2020-06-18 00:00:00', 26200, 'SITC MOJI', 'SIMO0286', 'SIT', 'HKHKG', 'VNDAD', 'Vessel', 'Cảng khác', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'N', 'N', NULL, NULL, 2, NULL, 'nguyen trong hieu', '2020-06-17 16:49:16', NULL, '2020-06-17 16:49:28'),
	(69, 1, 18, NULL, '181', 'APHU7219065', NULL, '45G0', 'F', 'DBA0226205', NULL, 'A490424', 'VINAL', '2020-06-17 00:00:00', 29300, 'CNC MARS', 'CNMA0275', 'CMA', 'SGSIN', 'VNDAD', 'Vessel', 'Cảng Tiên Sa', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'N', 'N', NULL, NULL, 2, NULL, 'nguyen trong hieu', '2020-06-17 16:51:47', NULL, '2020-06-17 16:51:53'),
	(70, 1, 18, NULL, '182', 'BEAU4027100', NULL, '45G0', 'F', 'DBA0226205', NULL, 'A490439', 'VINAL', '2020-06-17 00:00:00', 29000, 'CNC MARS', 'CNMA0275', 'CMA', 'SGSIN', 'VNDAD', 'Vessel', 'Cảng Tiên Sa', NULL, NULL, NULL, NULL, 'Y', 6, 'R', 'N', 'N', 'N', NULL, NULL, 2, NULL, 'nguyen trong hieu', '2020-06-17 09:51:47', NULL, '2020-06-17 09:51:53'),
	(71, 1, 18, NULL, '183', 'CMAU6216624', NULL, '45G0', 'F', 'DBA0226205', NULL, 'A490422', 'VINAL', '2020-06-17 00:00:00', 29000, 'CNC MARS', 'CNMA0275', 'CMA', 'SGSIN', 'VNDAD', 'Vessel', 'Cảng Tiên Sa', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'N', 'N', NULL, NULL, 2, NULL, 'nguyen trong hieu', '2020-06-17 16:51:47', NULL, '2020-06-17 16:51:53'),
	(72, 1, 18, NULL, '184', 'FSCU8352291', NULL, '45G0', 'F', 'DBA0226205', NULL, 'A490421', 'VINAL', '2020-06-17 00:00:00', 29200, 'CNC MARS', 'CNMA0275', 'CMA', 'SGSIN', 'VNDAD', 'Vessel', 'Cảng Tiên Sa', NULL, NULL, NULL, NULL, 'Y', 6, 'R', 'N', 'N', 'N', NULL, NULL, 2, NULL, 'nguyen trong hieu', '2020-06-17 09:51:47', NULL, '2020-06-17 09:51:53'),
	(73, 1, 18, NULL, '185', 'TCKU6272162', NULL, '45G0', 'F', 'DBA0226205', NULL, 'A490437', 'VINAL', '2020-06-17 00:00:00', 29100, 'CNC MARS', 'CNMA0275', 'CMA', 'SGSIN', 'VNDAD', 'Vessel', 'Cảng Tiên Sa', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'N', 'N', NULL, NULL, 2, NULL, 'nguyen trong hieu', '2020-06-17 16:51:47', NULL, '2020-06-17 16:51:53'),
	(74, 1, 18, NULL, '186', 'TCLU1500204', NULL, '45G0', 'F', 'DBA0226205', NULL, 'A490423', 'VINAL', '2020-06-17 00:00:00', 29000, 'CNC MARS', 'CNMA0275', 'CMA', 'SGSIN', 'VNDAD', 'Vessel', 'Cảng Tiên Sa', NULL, NULL, NULL, NULL, 'Y', 6, 'R', 'N', 'N', 'N', NULL, NULL, 2, NULL, 'nguyen trong hieu', '2020-06-17 09:51:47', NULL, '2020-06-17 09:51:53'),
	(75, 1, 18, NULL, '187', 'TGBU6930020', NULL, '45G0', 'F', 'DBA0226205', NULL, 'A490425', 'VINAL', '2020-06-17 00:00:00', 29000, 'CNC MARS', 'CNMA0275', 'CMA', 'SGSIN', 'VNDAD', 'Vessel', 'Cảng Tiên Sa', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'N', 'N', NULL, NULL, 2, NULL, 'nguyen trong hieu', '2020-06-17 16:51:47', NULL, '2020-06-17 16:51:53'),
	(76, 1, 18, NULL, '188', 'TLLU5017002', NULL, '45G0', 'F', 'DBA0226205', NULL, 'A490440', 'VINAL', '2020-06-17 00:00:00', 29200, 'CNC MARS', 'CNMA0275', 'CMA', 'SGSIN', 'VNDAD', 'Vessel', 'Cảng Tiên Sa', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'N', 'N', NULL, NULL, 2, NULL, 'nguyen trong hieu', '2020-06-17 16:51:47', NULL, '2020-06-17 16:51:53'),
	(77, 1, 18, NULL, '189', 'UETU5548350', NULL, '45G0', 'F', 'DBA0226205', NULL, 'A490438', 'VINAL', '2020-06-17 00:00:00', 28900, 'CNC MARS', 'CNMA0275', 'CMA', 'SGSIN', 'VNDAD', 'Vessel', 'Cảng Tiên Sa', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'N', 'N', NULL, NULL, 2, NULL, 'nguyen trong hieu', '2020-06-17 16:51:47', NULL, '2020-06-17 16:51:54'),
	(78, 1, 20, NULL, '201', 'CONT1231231', NULL, '22G0', NULL, NULL, NULL, NULL, NULL, '2020-06-18 00:00:00', 1, 'vslNm', 'voyNo', 'CMC', 'sdfasdf', 'CMTVN:CAI MEP', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', 'N', NULL, NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-06-18 18:01:26', 'nguyen trong hieu', '2020-06-19 09:09:44'),
	(79, 1, 20, NULL, '202', 'CONT2342342', NULL, '40G0', NULL, NULL, NULL, NULL, NULL, '2020-06-19 00:00:00', 1, 'vslNm', 'voyNo', 'AVS', ' ', 'CMTVN:CAI MEP', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', 'N', NULL, NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-06-19 09:09:44', NULL, NULL),
	(80, 1, 24, NULL, '241', 'containerNo', NULL, '22G0', NULL, NULL, 'asdf', NULL, NULL, '2020-06-19 00:00:00', 1, 'SDA', '3221', 'AVS', ' ', 'CMTVN:CAI MEP', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', 'N', NULL, NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-06-19 10:35:04', NULL, NULL),
	(81, 1, 24, NULL, '242', 'containerNo', NULL, '22G0', NULL, NULL, 'asdf', NULL, NULL, '2020-06-19 00:00:00', 1, 'SDA', '3221', 'AVS', ' ', 'CMTVN:CAI MEP', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', 'N', NULL, NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-06-19 10:35:04', NULL, NULL),
	(82, 1, 24, NULL, '243', 'containerNo', NULL, '22G0', NULL, NULL, 'asdf', NULL, NULL, '2020-06-19 00:00:00', 1, 'SDA', '3221', 'AVS', ' ', 'CMTVN:CAI MEP', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', 'N', NULL, NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-06-19 10:35:04', NULL, NULL),
	(83, 1, 24, NULL, '244', 'containerNo', NULL, '22G0', NULL, NULL, 'asdf', NULL, NULL, '2020-06-19 00:00:00', 1, 'SDA', '3221', 'AVS', ' ', 'CMTVN:CAI MEP', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', 'N', NULL, NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-06-19 10:35:04', NULL, NULL),
	(84, 1, 24, NULL, '245', 'containerNo', NULL, '22G0', NULL, NULL, 'asdf', NULL, NULL, '2020-06-19 00:00:00', 1, 'SDA', '3221', 'AVS', ' ', 'CMTVN:CAI MEP', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', 'N', NULL, NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-06-19 10:35:04', NULL, NULL),
	(85, 1, 24, NULL, '246', 'containerNo', NULL, '22G0', NULL, NULL, 'asdf', NULL, NULL, '2020-06-19 00:00:00', 1, 'SDA', '3221', 'AVS', ' ', 'CMTVN:CAI MEP', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', 'N', NULL, NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-06-19 10:35:04', NULL, NULL),
	(86, 1, 24, NULL, '247', 'containerNo', NULL, '22G0', NULL, NULL, 'asdf', NULL, NULL, '2020-06-19 00:00:00', 1, 'SDA', '3221', 'AVS', ' ', 'CMTVN:CAI MEP', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', 'N', NULL, NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-06-19 10:35:05', NULL, NULL),
	(87, 1, 24, NULL, '248', 'containerNo', NULL, '22G0', NULL, NULL, 'asdf', NULL, NULL, '2020-06-19 00:00:00', 1, 'SDA', '3221', 'AVS', ' ', 'CMTVN:CAI MEP', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', 'N', NULL, NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-06-19 10:35:05', NULL, NULL),
	(88, 1, 24, NULL, '249', 'containerNo', NULL, '22G0', NULL, NULL, 'asdf', NULL, NULL, '2020-06-19 00:00:00', 1, 'SDA', '3221', 'AVS', ' ', 'CMTVN:CAI MEP', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', 'N', NULL, NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-06-19 10:35:05', NULL, NULL),
	(89, 1, 24, NULL, '2410', 'CONT1231352', NULL, '22G0', NULL, NULL, 'asdf', NULL, NULL, '2020-06-19 00:00:00', 1, 'SDA', '3221', 'AVS', ' ', 'CMTVN:CAI MEP', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', 'N', NULL, NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-06-19 10:35:05', NULL, NULL),
	(90, 1, 24, NULL, '2411', 'CONT2342344', NULL, '22G0', NULL, NULL, 'asdf', NULL, NULL, '2020-06-19 00:00:00', 1, 'SDA', '3221', 'AVS', ' ', 'CMTVN:CAI MEP', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', 'N', NULL, NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-06-19 10:35:05', NULL, NULL),
	(92, 1, 27, NULL, '271', 'CONT3242323', NULL, '22G0', NULL, NULL, 's', NULL, 'adas', NULL, 1, 'SDF', '2', 'AVS', ' ', 'CNSHA:Shanghai', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'R', 'N', 'N', NULL, NULL, NULL, 2, NULL, 'nguyen trong hieu', '2020-06-19 16:13:12', 'nguyen trong hieu', '2020-06-19 16:22:53'),
	(93, 1, 28, NULL, '93', 'AAMU4000665', NULL, '45R0', 'F', 'SMJ2014SDA701', NULL, 'SITH603670', 'sdf', '2020-06-19 00:00:00', 26200, 'SITC MOJI', 'SIMO0286', 'SIT', 'HKHKG', 'VNDAD', 'Vessel', 'Cảng Tiên Sa', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'N', 'N', NULL, 'Y', 2, NULL, 'nguyen trong hieu', '2020-06-19 20:20:58', NULL, '2020-06-19 20:21:11'),
	(94, 1, 28, NULL, '93', 'SEGU9358519', NULL, '45R0', 'F', 'SMJ2014SDA701', NULL, 'SITH603666', 'sdf', '2020-06-19 00:00:00', 26200, 'SITC MOJI', 'SIMO0286', 'SIT', 'HKHKG', 'VNDAD', 'Vessel', 'Cảng Tiên Sa', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'N', 'N', NULL, 'Y', 2, NULL, 'nguyen trong hieu', '2020-06-19 20:20:58', NULL, '2020-06-19 20:21:11'),
	(95, 1, 28, NULL, '93', 'TLLU1130582', NULL, '45R0', 'F', 'SMJ2014SDA701', NULL, 'SITH603844', 'sadf', '2020-06-19 00:00:00', 26200, 'SITC MOJI', 'SIMO0286', 'SIT', 'HKHKG', 'VNDAD', 'Vessel', 'Cảng Tiên Sa', NULL, NULL, NULL, NULL, 'N', NULL, 'R', 'N', 'N', 'N', NULL, 'Y', 2, NULL, 'nguyen trong hieu', '2020-06-19 20:20:58', NULL, '2020-06-19 20:21:11'),
	(96, 1, 19, NULL, '96', 'CONT2342432', NULL, '20G0', NULL, NULL, NULL, NULL, NULL, '2020-06-19 00:00:00', 1, 'vslNm', 'voyNo', 'CMC', ' ', 'CMTVN:CAI MEP', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', 'N', NULL, NULL, 'Y', 1, NULL, 'nguyen trong hieu', '2020-06-19 21:00:00', 'nguyen trong hieu', '2020-06-19 21:00:19'),
	(97, 1, 19, NULL, '96', 'CONT4322234', NULL, '20G0', NULL, NULL, NULL, NULL, NULL, '2020-06-19 00:00:00', 1, 'vslNm', 'voyNo', 'CMC', ' ', 'CMTVN:CAI MEP', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N', 'N', NULL, NULL, 'Y', 1, NULL, 'nguyen trong hieu', '2020-06-19 21:00:20', NULL, NULL),
	(98, 1, 29, NULL, '98', 'CONT2342344', NULL, '22G0', NULL, NULL, 'BFDKjdfj', NULL, 'DSFSDF', NULL, 12, 'SDA', '122', 'AVS', ' ', 'CMTVN:CAI MEP', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'R', 'N', 'N', NULL, NULL, 'Y', 2, NULL, 'nguyen trong hieu', '2020-06-19 22:55:31', 'nguyen trong hieu', '2020-06-19 23:03:00'),
	(99, 1, 29, NULL, '99', 'CONT2342341', NULL, '22G0', NULL, NULL, 'BFDKjdfj', NULL, 'DSFSDF', NULL, 12, 'SDA', '122', 'AVS', ' ', 'CMTVN:CAI MEP', NULL, NULL, 'DG:Dangerous', NULL, NULL, NULL, NULL, NULL, 'R', 'N', 'N', NULL, NULL, 'Y', 2, NULL, 'nguyen trong hieu', '2020-06-19 22:58:55', 'nguyen trong hieu', '2020-06-19 23:03:00'),
	(100, 1, 29, NULL, '293', 'CONT2342345', NULL, '22G0', NULL, NULL, 'BFDKjdfj', NULL, 'DSFSDF', NULL, 12, 'HABE', '0235', 'AVS', ' ', 'CNSHA:Shanghai', NULL, NULL, 'DG:Dangerous', NULL, NULL, NULL, NULL, NULL, NULL, 'N', 'N', NULL, NULL, NULL, 1, NULL, 'nguyen trong hieu', '2020-06-19 23:03:00', NULL, NULL);
/*!40000 ALTER TABLE `shipment_detail` ENABLE KEYS */;

-- Dumping structure for table eport.sys_config
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE IF NOT EXISTS `sys_config` (
  `config_id` int(5) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) COLLATE utf8_bin DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) COLLATE utf8_bin DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) COLLATE utf8_bin DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) COLLATE utf8_bin DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='参数配置表';

-- Dumping data for table eport.sys_config: ~4 rows (approximately)
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT IGNORE INTO `sys_config` (`config_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
	(1, 'Main Frame Page', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(2, 'User Init password', 'sys.user.initPassword', '123456', 'Y', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(3, 'Main Frame Page-Sidebar Theme', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(4, 'Enable User Register', 'sys.account.registerUser', 'false', 'Y', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;

-- Dumping structure for table eport.sys_dept
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE IF NOT EXISTS `sys_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Department id',
  `parent_id` bigint(20) DEFAULT '0' COMMENT 'Parent department id',
  `ancestors` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT 'Ancestor list',
  `dept_name` varchar(30) COLLATE utf8_bin DEFAULT '' COMMENT 'Department name',
  `order_num` int(4) DEFAULT '0' COMMENT 'display order',
  `leader` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'leader',
  `phone` varchar(11) COLLATE utf8_bin DEFAULT NULL COMMENT 'contact number',
  `email` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'email',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Department status (0 normal 1 disabled)',
  `del_flag` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Delete flag (0 means exist 2 means delete)',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Creation time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update time',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='部门表';

-- Dumping data for table eport.sys_dept: ~10 rows (approximately)
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT IGNORE INTO `sys_dept` (`dept_id`, `parent_id`, `ancestors`, `dept_name`, `order_num`, `leader`, `phone`, `email`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
	(100, 0, '0', 'DNG Port', 0, 'DNG', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00'),
	(101, 100, '0,100', 'Head Office', 1, 'DNG', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00'),
	(102, 100, '0,100', 'Branch', 2, 'DNG', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00'),
	(103, 101, '0,100,101', 'R&D', 1, 'DNG', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00'),
	(104, 101, '0,100,101', 'Marketing', 2, 'DNG', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00'),
	(105, 101, '0,100,101', 'Test', 3, 'DNG', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00'),
	(106, 101, '0,100,101', 'Finance', 4, 'DNG', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00'),
	(107, 101, '0,100,101', 'Operation', 5, 'DNG', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00'),
	(108, 102, '0,100,102', 'Marketing', 1, 'DNG', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00'),
	(109, 102, '0,100,102', 'Finance', 2, 'DNG', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00');
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;

-- Dumping structure for table eport.sys_dict_data
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE IF NOT EXISTS `sys_dict_data` (
  `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(4) DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) COLLATE utf8_bin DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) COLLATE utf8_bin DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) COLLATE utf8_bin DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) COLLATE utf8_bin DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`dict_code`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='字典数据表';

-- Dumping data for table eport.sys_dict_data: ~29 rows (approximately)
/*!40000 ALTER TABLE `sys_dict_data` DISABLE KEYS */;
INSERT IGNORE INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
	(1, 1, 'Male', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(2, 2, 'Female', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(3, 3, 'Mystery', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(4, 1, 'Show', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(5, 2, 'Hide', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(6, 1, 'Normal', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(7, 2, 'Disabled', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(8, 1, 'Normal', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(9, 2, 'Stop', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(10, 1, 'Default', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(11, 2, 'System', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(12, 1, 'Yes', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(13, 2, 'No', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(14, 1, 'Information', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(15, 2, 'Warning', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(16, 1, 'Normal', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(17, 2, 'Shutdown', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(18, 99, 'Other', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(19, 1, 'Add', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(20, 2, 'Update', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(21, 3, 'Delete', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(22, 4, 'Authorization', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(23, 5, 'Export', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(24, 6, 'Import', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(25, 7, 'Retreat', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(26, 8, 'Gen Code', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(27, 9, 'Clear Data', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(28, 1, 'Success', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(29, 2, 'Error', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
/*!40000 ALTER TABLE `sys_dict_data` ENABLE KEYS */;

-- Dumping structure for table eport.sys_dict_type
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE IF NOT EXISTS `sys_dict_type` (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Dictionary ID',
  `dict_name` varchar(100) COLLATE utf8_bin DEFAULT '' COMMENT 'Dictionary Name',
  `dict_type` varchar(100) COLLATE utf8_bin DEFAULT '' COMMENT 'Dictionary Type',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Statiu（0 Nomal, 1 Disabled）',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`dict_id`),
  UNIQUE KEY `dict_type` (`dict_type`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Dictionary type';

-- Dumping data for table eport.sys_dict_type: ~10 rows (approximately)
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
INSERT IGNORE INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
	(1, 'User Gender', 'sys_user_sex', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(2, 'Menu status', 'sys_show_hide', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(3, 'System switch', 'sys_normal_disable', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(4, 'Task status', 'sys_job_status', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(5, 'Task grouping', 'sys_job_group', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(6, 'Is System', 'sys_yes_no', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(7, 'Notification type', 'sys_notice_type', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(8, 'Notification status', 'sys_notice_status', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(9, 'Operation type', 'sys_oper_type', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(10, 'System status', 'sys_common_status', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;

-- Dumping structure for table eport.sys_job
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE IF NOT EXISTS `sys_job` (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) COLLATE utf8_bin NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) COLLATE utf8_bin DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) COLLATE utf8_bin DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT '' COMMENT 'Remark信息',
  PRIMARY KEY (`job_id`,`job_name`,`job_group`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='定时任务调度表';

-- Dumping data for table eport.sys_job: ~3 rows (approximately)
/*!40000 ALTER TABLE `sys_job` DISABLE KEYS */;
INSERT IGNORE INTO `sys_job` (`job_id`, `job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
	(1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
/*!40000 ALTER TABLE `sys_job` ENABLE KEYS */;

-- Dumping structure for table eport.sys_job_log
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE IF NOT EXISTS `sys_job_log` (
  `job_log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) COLLATE utf8_bin NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '日志信息',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) COLLATE utf8_bin DEFAULT '' COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  PRIMARY KEY (`job_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='定时任务调度日志表';

-- Dumping data for table eport.sys_job_log: ~0 rows (approximately)
/*!40000 ALTER TABLE `sys_job_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_job_log` ENABLE KEYS */;

-- Dumping structure for table eport.sys_logininfor
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE IF NOT EXISTS `sys_logininfor` (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `login_name` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '登录账号',
  `ipaddr` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '操作系统',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`)
) ENGINE=InnoDB AUTO_INCREMENT=526 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统访问记录';

-- Dumping data for table eport.sys_logininfor: ~426 rows (approximately)
/*!40000 ALTER TABLE `sys_logininfor` DISABLE KEYS */;
INSERT IGNORE INTO `sys_logininfor` (`info_id`, `login_name`, `ipaddr`, `login_location`, `browser`, `os`, `status`, `msg`, `login_time`) VALUES
	(100, 'giaphd', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '1', '验证码错误', '2020-03-28 07:16:13'),
	(101, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-03-28 07:16:19'),
	(102, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-03-28 07:16:30'),
	(103, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-03-28 10:57:06'),
	(104, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '1', '验证码错误', '2020-03-28 11:40:35'),
	(105, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-03-28 11:40:37'),
	(106, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-03-28 11:40:44'),
	(107, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-03-28 13:29:50'),
	(108, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-03-28 13:33:19'),
	(109, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-03-28 14:08:05'),
	(110, 'giaphd', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '1', '用户不存在/密码错误', '2020-03-29 23:37:45'),
	(111, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-03-29 23:37:56'),
	(112, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '1', '验证码错误', '2020-03-30 00:51:01'),
	(113, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '1', '密码输入错误1次', '2020-03-30 00:51:06'),
	(114, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '1', '验证码错误', '2020-03-30 00:51:16'),
	(115, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '1', '密码输入错误2次', '2020-03-30 00:51:19'),
	(116, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '1', '验证码错误', '2020-03-30 00:51:27'),
	(117, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-03-30 00:51:42'),
	(118, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-07 11:11:53'),
	(119, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-07 11:12:04'),
	(120, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-07 11:12:10'),
	(121, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-07 11:17:46'),
	(122, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '1', 'Nhập sai mật khẩu lần 1', '2020-04-07 11:20:41'),
	(123, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '1', 'Nhập sai mật khẩu lần 2', '2020-04-07 11:20:46'),
	(124, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-07 11:21:38'),
	(125, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-07 13:05:00'),
	(126, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-07 13:29:54'),
	(127, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-07 14:15:17'),
	(128, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-07 16:33:11'),
	(129, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-07 16:45:31'),
	(130, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-07 17:12:22'),
	(131, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-07 17:20:54'),
	(132, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-07 17:59:59'),
	(133, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-07 18:04:12'),
	(134, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-07 18:07:20'),
	(135, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-07 21:46:03'),
	(136, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-08 08:09:55'),
	(137, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-08 09:01:17'),
	(138, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-08 09:07:42'),
	(139, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-08 09:09:41'),
	(140, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '1', 'Nhập sai mật khẩu lần 1', '2020-04-08 09:17:46'),
	(141, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-08 09:17:51'),
	(142, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-08 09:20:09'),
	(143, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-08 09:40:01'),
	(144, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-08 09:44:23'),
	(145, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-08 09:49:16'),
	(146, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-08 09:53:28'),
	(147, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '1', 'Nhập sai mật khẩu lần 1', '2020-04-08 09:57:52'),
	(148, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-08 09:57:54'),
	(149, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-08 10:02:37'),
	(150, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-08 10:15:09'),
	(151, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-08 10:29:03'),
	(152, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-08 11:05:31'),
	(153, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-08 11:08:48'),
	(154, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-08 11:25:46'),
	(155, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-08 12:52:46'),
	(156, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-08 13:06:27'),
	(157, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-08 13:11:32'),
	(158, 'Carrier: tai@admin.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-08 14:43:39'),
	(159, 'Carrier: tai@admin.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-08 14:43:44'),
	(160, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-08 14:43:50'),
	(161, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-09 08:24:16'),
	(162, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-09 09:17:16'),
	(163, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '1', 'Nhập sai mật khẩu lần 1', '2020-04-09 10:23:31'),
	(164, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-09 10:23:34'),
	(165, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-09 12:51:16'),
	(166, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-09 12:53:26'),
	(167, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-09 13:02:27'),
	(168, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-09 13:09:42'),
	(169, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-09 14:08:52'),
	(170, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-09 16:28:01'),
	(171, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-09 16:53:49'),
	(172, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-09 16:56:44'),
	(173, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-09 17:32:05'),
	(174, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-10 08:45:21'),
	(175, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-10 09:24:29'),
	(176, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-10 09:38:03'),
	(177, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 08:13:04'),
	(178, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 08:13:13'),
	(179, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 08:23:02'),
	(180, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 08:27:10'),
	(181, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 08:28:55'),
	(182, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 09:09:29'),
	(183, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 10:39:43'),
	(184, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 10:42:34'),
	(185, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-14 11:06:36'),
	(186, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 11:11:48'),
	(187, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 11:15:58'),
	(188, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-14 11:47:32'),
	(189, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-04-14 11:47:41'),
	(190, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 2', '2020-04-14 11:47:46'),
	(191, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 11:48:55'),
	(192, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-14 11:49:34'),
	(193, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 11:50:12'),
	(194, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 12:48:19'),
	(195, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 13:45:57'),
	(196, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 09:46:26'),
	(197, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-17 11:07:40'),
	(198, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-17 11:23:58'),
	(199, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-22 11:40:12'),
	(200, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 09:31:21'),
	(201, 'tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-27 08:13:06'),
	(202, 'tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-27 08:13:09'),
	(203, 'tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-27 08:19:35'),
	(204, 'tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-27 08:19:50'),
	(205, 'tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-27 08:19:54'),
	(206, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 08:32:21'),
	(207, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 09:13:45'),
	(208, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 09:32:37'),
	(209, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 10:07:20'),
	(210, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-27 10:24:18'),
	(211, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 10:24:26'),
	(212, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 10:33:29'),
	(213, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 10:37:06'),
	(214, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 11:07:48'),
	(215, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-27 11:28:34'),
	(216, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 11:28:40'),
	(217, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-27 11:29:37'),
	(218, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 11:29:41'),
	(219, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 14:18:18'),
	(220, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-27 14:33:44'),
	(221, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 14:34:01'),
	(222, 'tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-04 05:13:50'),
	(223, 'tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-04 05:15:02'),
	(224, 'tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-04 05:15:06'),
	(225, 'tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-04 05:15:09'),
	(226, 'tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-04 05:18:39'),
	(227, 'tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-04 05:18:42'),
	(228, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-04 05:42:08'),
	(229, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-05-04 05:42:39'),
	(230, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-04 05:42:48'),
	(231, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-05-04 05:42:58'),
	(232, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-04 05:43:07'),
	(233, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-04 05:44:14'),
	(234, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-05-04 05:58:11'),
	(235, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-04 05:58:18'),
	(236, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-04 12:12:16'),
	(237, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-04 12:25:46'),
	(238, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-07 12:53:09'),
	(239, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-07 13:08:17'),
	(240, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-14 13:54:10'),
	(241, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-14 14:00:59'),
	(242, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-05-14 14:16:53'),
	(243, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-14 14:17:03'),
	(244, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-05-14 14:17:23'),
	(245, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-05-14 14:17:36'),
	(246, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-14 14:17:39'),
	(247, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-14 14:55:04'),
	(248, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-14 15:35:54'),
	(249, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-14 16:19:54'),
	(250, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-14 17:47:09'),
	(251, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-14 18:15:53'),
	(252, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-14 18:32:12'),
	(253, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-14 18:41:32'),
	(254, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-14 18:48:19'),
	(255, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-05-15 04:34:28'),
	(256, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-15 04:34:34'),
	(257, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-15 04:38:14'),
	(258, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-15 04:57:21'),
	(259, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-15 05:00:57'),
	(260, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-05-15 05:18:50'),
	(261, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 2', '2020-05-15 05:18:55'),
	(262, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-15 05:18:59'),
	(263, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-15 06:07:45'),
	(264, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-15 06:57:14'),
	(265, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-15 08:03:34'),
	(266, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-16 08:17:32'),
	(267, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-16 08:18:35'),
	(268, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-16 13:25:22'),
	(269, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-05-16 13:25:32'),
	(270, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-19 08:46:24'),
	(271, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-05-19 08:50:51'),
	(272, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-19 08:51:03'),
	(273, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-19 11:31:57'),
	(274, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-19 11:55:39'),
	(275, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-19 13:25:59'),
	(276, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-19 13:32:58'),
	(277, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-19 13:35:21'),
	(278, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-19 13:53:30'),
	(279, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-19 21:24:30'),
	(280, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-20 11:37:25'),
	(281, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-05-20 13:27:40'),
	(282, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 2', '2020-05-20 13:27:45'),
	(283, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 3', '2020-05-20 13:27:58'),
	(284, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 4', '2020-05-20 13:28:44'),
	(285, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 5', '2020-05-20 13:28:59'),
	(286, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu quá lần 5，tài khoản bị khóa 10 phút', '2020-05-20 13:30:04'),
	(287, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-20 13:31:28'),
	(288, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-20 14:19:07'),
	(289, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-20 14:20:32'),
	(290, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-20 15:06:41'),
	(291, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-20 17:13:40'),
	(292, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-20 18:39:51'),
	(293, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-20 19:54:22'),
	(294, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-20 20:06:09'),
	(295, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-20 21:43:09'),
	(296, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-21 13:19:48'),
	(297, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-21 13:44:30'),
	(298, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-21 13:56:55'),
	(299, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-21 14:15:11'),
	(300, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-21 15:21:16'),
	(301, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-21 16:17:57'),
	(302, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-21 16:24:49'),
	(303, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-21 16:26:22'),
	(304, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-21 21:04:22'),
	(305, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-22 05:30:18'),
	(306, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-22 06:14:53'),
	(307, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-22 08:57:54'),
	(308, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-22 09:46:06'),
	(309, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-22 13:05:40'),
	(310, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-22 13:17:30'),
	(311, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-22 13:29:06'),
	(312, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-22 17:28:30'),
	(313, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-05-22 17:32:56'),
	(314, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-22 17:33:11'),
	(315, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-22 17:35:39'),
	(316, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-22 18:34:01'),
	(317, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-22 18:42:18'),
	(318, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-22 21:20:31'),
	(319, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-22 21:29:48'),
	(320, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-22 22:04:54'),
	(321, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-22 22:54:52'),
	(322, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-23 06:34:00'),
	(323, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-23 06:50:02'),
	(324, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-23 07:07:48'),
	(325, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-23 07:16:56'),
	(326, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-24 15:40:39'),
	(327, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-24 15:49:14'),
	(328, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-25 09:16:12'),
	(329, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-25 09:52:22'),
	(330, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-25 11:09:30'),
	(331, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-25 11:48:06'),
	(332, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-26 07:59:48'),
	(333, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-26 08:07:26'),
	(334, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-26 08:47:38'),
	(335, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-26 08:55:14'),
	(336, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-26 08:56:59'),
	(337, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-26 09:17:11'),
	(338, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-26 09:35:28'),
	(339, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-26 09:41:25'),
	(340, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-26 09:42:49'),
	(341, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-26 11:35:33'),
	(342, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-26 11:39:59'),
	(343, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-26 14:16:04'),
	(344, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-26 14:23:07'),
	(345, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-26 15:20:28'),
	(346, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 05:22:56'),
	(347, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 05:47:23'),
	(348, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 05:56:02'),
	(349, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 06:16:59'),
	(350, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 09:17:06'),
	(351, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 09:24:33'),
	(352, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 10:46:20'),
	(353, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 10:48:21'),
	(354, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 11:41:49'),
	(355, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 11:48:36'),
	(356, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 13:01:57'),
	(357, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 13:33:28'),
	(358, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-05-27 13:56:10'),
	(359, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 13:56:14'),
	(360, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 14:12:19'),
	(361, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 14:24:30'),
	(362, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-05-27 16:36:34'),
	(363, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 16:36:40'),
	(364, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 16:42:34'),
	(365, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 22:09:07'),
	(366, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 22:22:16'),
	(367, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 22:35:11'),
	(368, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-28 09:34:38'),
	(369, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-28 09:42:13'),
	(370, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-28 11:53:51'),
	(371, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-28 13:04:40'),
	(372, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-28 13:38:02'),
	(373, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-28 13:38:07'),
	(374, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-28 13:38:11'),
	(375, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-28 13:38:20'),
	(376, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-28 13:38:29'),
	(377, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-28 13:38:46'),
	(378, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-28 13:39:09'),
	(379, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-05-28 13:43:15'),
	(380, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-28 13:43:18'),
	(381, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-28 13:50:34'),
	(382, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-28 13:55:34'),
	(383, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-28 13:55:40'),
	(384, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-28 14:02:27'),
	(385, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-28 14:15:53'),
	(386, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-28 14:53:25'),
	(387, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-28 15:30:08'),
	(388, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-28 20:58:17'),
	(389, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-28 21:08:47'),
	(390, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-28 21:53:59'),
	(391, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-28 21:59:28'),
	(392, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 06:39:41'),
	(393, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-05-29 06:53:58'),
	(394, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 06:54:01'),
	(395, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 07:01:51'),
	(396, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 07:15:57'),
	(397, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 08:25:39'),
	(398, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 10:48:07'),
	(399, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 11:57:19'),
	(400, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 16:09:08'),
	(401, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 16:12:31'),
	(402, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 16:29:29'),
	(403, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 18:40:14'),
	(404, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 19:09:59'),
	(405, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 19:26:58'),
	(406, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 19:45:00'),
	(407, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 20:50:10'),
	(408, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 21:24:47'),
	(409, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 21:25:54'),
	(410, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 21:29:14'),
	(411, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 21:32:12'),
	(412, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 21:36:25'),
	(413, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-30 06:51:06'),
	(414, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-30 06:54:30'),
	(415, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-30 07:08:01'),
	(416, 'Carrier: mst123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-30 08:38:39'),
	(417, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-30 08:38:42'),
	(418, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-30 08:55:41'),
	(419, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-30 16:36:20'),
	(420, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-02 09:45:13'),
	(421, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-03 05:27:44'),
	(422, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-03 05:39:39'),
	(423, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-03 06:45:34'),
	(424, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-03 08:01:30'),
	(425, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-03 09:34:03'),
	(426, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-03 12:44:49'),
	(427, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-03 13:02:52'),
	(428, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-03 13:54:04'),
	(429, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-03 15:00:10'),
	(430, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-03 15:45:03'),
	(431, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-03 16:47:36'),
	(432, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-03 16:48:29'),
	(433, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-04 13:17:25'),
	(434, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-04 13:29:36'),
	(435, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-05 08:19:42'),
	(436, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-05 08:27:34'),
	(437, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-05 11:04:25'),
	(438, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-05 13:07:41'),
	(439, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-05 14:02:37'),
	(440, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-05 15:08:29'),
	(441, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-05 16:19:26'),
	(442, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-05 16:48:51'),
	(443, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-06 08:17:44'),
	(444, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-06 08:23:55'),
	(445, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-06 09:51:03'),
	(446, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-06-06 10:45:39'),
	(447, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-06 10:45:47'),
	(448, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-06 11:44:24'),
	(449, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-06 13:32:08'),
	(450, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-06 14:03:59'),
	(451, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 08:01:56'),
	(452, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 08:35:41'),
	(453, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 09:19:42'),
	(454, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 09:28:39'),
	(455, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 10:03:28'),
	(456, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 10:20:42'),
	(457, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 11:13:13'),
	(458, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 13:12:46'),
	(459, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-11 08:48:16'),
	(460, 'Carrier: mst123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-11 10:03:27'),
	(461, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-11 10:03:31'),
	(462, 'Carrier: mst123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-11 10:56:02'),
	(463, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-11 10:56:04'),
	(464, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-11 11:14:07'),
	(465, 'Carrier: MST123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-11 13:04:39'),
	(466, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-11 13:04:49'),
	(467, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-11 17:12:50'),
	(468, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-11 17:14:21'),
	(469, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-11 18:03:58'),
	(470, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-11 19:23:33'),
	(471, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-11 20:39:32'),
	(472, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-11 20:45:41'),
	(473, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-12 10:54:09'),
	(474, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-12 12:01:54'),
	(475, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-12 20:52:22'),
	(476, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-13 06:09:43'),
	(477, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-13 07:15:32'),
	(478, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-13 09:14:22'),
	(479, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-15 09:41:23'),
	(480, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-15 10:15:02'),
	(481, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-15 11:39:39'),
	(482, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-15 11:58:28'),
	(483, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-15 12:32:55'),
	(484, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-15 13:20:02'),
	(485, 'Carrier: mst123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-15 15:27:05'),
	(486, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-15 15:27:08'),
	(487, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-15 16:41:34'),
	(488, 'Carrier: mst123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-15 20:50:05'),
	(489, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-15 20:50:08'),
	(490, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-16 09:51:13'),
	(491, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-16 13:27:19'),
	(492, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-16 15:22:22'),
	(493, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-16 16:02:05'),
	(494, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-16 17:28:25'),
	(495, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-16 19:19:03'),
	(496, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-16 20:13:45'),
	(497, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-17 15:02:25'),
	(498, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-17 15:39:32'),
	(499, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-17 18:45:32'),
	(500, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-18 12:41:32'),
	(501, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-18 13:21:37'),
	(502, 'Carrier: mst123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-18 14:51:59'),
	(503, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-18 14:52:02'),
	(504, 'Carrier: mst123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-18 15:03:14'),
	(505, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-18 15:03:17'),
	(506, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-18 15:20:20'),
	(507, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-18 16:37:18'),
	(508, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-18 19:00:22'),
	(509, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-18 21:05:00'),
	(510, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-19 08:26:13'),
	(511, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-19 10:33:17'),
	(512, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-19 11:06:28'),
	(513, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-19 12:33:44'),
	(514, 'Carrier: asdfasd@sadfs.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-06-19 12:46:58'),
	(515, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-19 12:47:02'),
	(516, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-19 13:02:21'),
	(517, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-19 15:09:05'),
	(518, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-19 16:50:42'),
	(519, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-19 18:27:59'),
	(520, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-19 20:18:20'),
	(521, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-19 20:26:27'),
	(522, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-19 20:28:17'),
	(523, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-19 20:59:35'),
	(524, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-19 22:34:30'),
	(525, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-19 22:54:39');
/*!40000 ALTER TABLE `sys_logininfor` ENABLE KEYS */;

-- Dumping structure for table eport.sys_menu
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE IF NOT EXISTS `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Menu ID',
  `menu_name` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Menu Name',
  `parent_id` bigint(20) DEFAULT '0' COMMENT 'Parent ID',
  `order_num` int(4) DEFAULT '0' COMMENT 'Display Order',
  `url` varchar(200) COLLATE utf8_bin DEFAULT '#' COMMENT 'URL',
  `target` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT 'Target（menuItem: tabs menuBlank: new window）',
  `menu_type` char(1) COLLATE utf8_bin DEFAULT '' COMMENT 'Menu Type（M Index C Menu F Button）',
  `visible` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Menu status (0 displayed 1 hidden)',
  `perms` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT 'Permission ID',
  `icon` varchar(100) COLLATE utf8_bin DEFAULT '#' COMMENT 'Menu icon',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT '' COMMENT 'Remark',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2027 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Menu permission';

-- Dumping data for table eport.sys_menu: ~109 rows (approximately)
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
	(1, 'Quản Lý Hệ Thống', 0, 1, '#', '', 'M', '0', '', 'fa fa-gear', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'System Management'),
	(2, 'System Monitoring', 0, 2, '#', '', 'M', '1', '', 'fa fa-video-camera', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'System Monitoring'),
	(3, 'Quản Lý Hãng Tàu', 0, 3, '#', '', 'M', '0', '', 'fa fa-bars', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'System Tools目录'),
	(100, 'Quản lý người dùng', 1, 1, '/system/user', '', 'C', '0', 'system:user:view', 'fa fa-address-book-o', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'User Management'),
	(101, 'Quản lý vai trò', 1, 2, '/system/role', '', 'C', '0', 'system:role:view', 'fa fa-users', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'Role Management'),
	(102, 'Danh mục', 1, 3, '/system/menu', '', 'C', '0', 'system:menu:view', 'fa fa-bars', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(103, 'Department', 1, 4, '/system/dept', '', 'C', '1', 'system:dept:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(104, 'Post', 1, 5, '/system/post', '', 'C', '1', 'system:post:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(105, 'Dictionary', 1, 6, '/system/dict', '', 'C', '1', 'system:dict:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(106, 'Config', 1, 7, '/system/config', '', 'C', '1', 'system:config:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(107, 'Notification', 1, 8, '/system/notice', '', 'C', '1', 'system:notice:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(108, 'Lịch sử', 1, 9, '#', '', 'M', '0', '', 'fa fa-hourglass-1', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(109, 'Online User', 2, 1, '/monitor/online', '', 'C', '1', 'monitor:online:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(110, 'Job', 2, 2, '/monitor/job', '', 'C', '1', 'monitor:job:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(111, 'Data Monitor', 2, 3, '/monitor/data', '', 'C', '1', 'monitor:data:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(112, 'Server Monitor', 2, 3, '/monitor/server', '', 'C', '1', 'monitor:server:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(113, 'Form Building', 3, 1, '/tool/build', '', 'C', '1', 'tool:build:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(114, 'Code Gen', 3, 2, '/tool/gen', 'menuItem', 'C', '0', 'tool:gen:view', '#', 'admin', '2018-03-16 11:33:00', 'admin', '2020-06-19 18:28:26', ''),
	(115, 'API', 3, 3, '/tool/swagger', '', 'C', '1', 'tool:swagger:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(500, 'Lịch sử hoạt động', 108, 1, '/monitor/operlog', '', 'C', '0', 'monitor:operlog:view', 'fa fa-low-vision', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(501, 'Lịch sử đăng nhập', 108, 2, '/monitor/logininfor', '', 'C', '0', 'monitor:logininfor:view', 'fa fa-exchange', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1000, 'User List', 100, 1, '#', '', 'F', '0', 'system:user:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1001, 'User Add', 100, 2, '#', '', 'F', '0', 'system:user:add', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1002, 'User Edit', 100, 3, '#', '', 'F', '0', 'system:user:edit', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1003, 'User Delete', 100, 4, '#', '', 'F', '0', 'system:user:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1004, 'User Export', 100, 5, '#', '', 'F', '0', 'system:user:export', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1005, 'User Inport', 100, 6, '#', '', 'F', '0', 'system:user:import', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1006, 'Reset Password', 100, 7, '#', '', 'F', '0', 'system:user:resetPwd', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1007, 'Role List', 101, 1, '#', '', 'F', '0', 'system:role:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1008, 'Add Role', 101, 2, '#', '', 'F', '0', 'system:role:add', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1009, 'Edit Role', 101, 3, '#', '', 'F', '0', 'system:role:edit', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1010, 'Delete Role', 101, 4, '#', '', 'F', '0', 'system:role:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1011, 'Export Role', 101, 5, '#', '', 'F', '0', 'system:role:export', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1012, 'Menu List', 102, 1, '#', '', 'F', '0', 'system:menu:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1013, 'Add Menu', 102, 2, '#', '', 'F', '0', 'system:menu:add', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1014, 'Edit Menu', 102, 3, '#', '', 'F', '0', 'system:menu:edit', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1015, 'Delete Menu', 102, 4, '#', '', 'F', '0', 'system:menu:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1016, 'Depart List', 103, 1, '#', '', 'F', '0', 'system:dept:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1017, 'Add Depart', 103, 2, '#', '', 'F', '0', 'system:dept:add', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1018, 'Edit Depart', 103, 3, '#', '', 'F', '0', 'system:dept:edit', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1019, 'Delete Depart', 103, 4, '#', '', 'F', '0', 'system:dept:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1020, 'Post List', 104, 1, '#', '', 'F', '0', 'system:post:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1021, 'Add Post', 104, 2, '#', '', 'F', '0', 'system:post:add', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1022, 'Edit Post', 104, 3, '#', '', 'F', '0', 'system:post:edit', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1023, 'Delete Post', 104, 4, '#', '', 'F', '0', 'system:post:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1024, 'Export Port', 104, 5, '#', '', 'F', '0', 'system:post:export', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1025, 'Dict List', 105, 1, '#', '', 'F', '0', 'system:dict:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1026, 'Add Dict', 105, 2, '#', '', 'F', '0', 'system:dict:add', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1027, 'Edit Dict', 105, 3, '#', '', 'F', '0', 'system:dict:edit', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1028, 'Delete Dict', 105, 4, '#', '', 'F', '0', 'system:dict:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1029, 'Export Dict', 105, 5, '#', '', 'F', '0', 'system:dict:export', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1030, 'Config List', 106, 1, '#', '', 'F', '0', 'system:config:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1031, 'Add Config', 106, 2, '#', '', 'F', '0', 'system:config:add', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1032, 'Edit Config', 106, 3, '#', '', 'F', '0', 'system:config:edit', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1033, 'Delete Config', 106, 4, '#', '', 'F', '0', 'system:config:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1034, 'Export Config', 106, 5, '#', '', 'F', '0', 'system:config:export', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1035, 'Notification List', 107, 1, '#', '', 'F', '0', 'system:notice:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1036, 'Add Notification', 107, 2, '#', '', 'F', '0', 'system:notice:add', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1037, 'Edit Notification', 107, 3, '#', '', 'F', '0', 'system:notice:edit', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1038, 'Delete Notification', 107, 4, '#', '', 'F', '0', 'system:notice:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1039, 'Operator Log List', 500, 1, '#', '', 'F', '0', 'monitor:operlog:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1040, 'Remote Operator Log', 500, 2, '#', '', 'F', '0', 'monitor:operlog:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1041, 'View Operator Log', 500, 3, '#', '', 'F', '0', 'monitor:operlog:detail', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1042, 'Export Operator Log', 500, 4, '#', '', 'F', '0', 'monitor:operlog:export', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1043, 'Login Infor List', 501, 1, '#', '', 'F', '0', 'monitor:logininfor:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1044, 'Remove Login Info', 501, 2, '#', '', 'F', '0', 'monitor:logininfor:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1045, 'Export Login Info', 501, 3, '#', '', 'F', '0', 'monitor:logininfor:export', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1046, 'Unlock Login Info', 501, 4, '#', '', 'F', '0', 'monitor:logininfor:unlock', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1047, 'Online List', 109, 1, '#', '', 'F', '0', 'monitor:online:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1048, 'Batch Force Logout', 109, 2, '#', '', 'F', '0', 'monitor:online:batchForceLogout', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1049, 'Force Logout', 109, 3, '#', '', 'F', '0', 'monitor:online:forceLogout', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1050, 'Job List', 110, 1, '#', '', 'F', '0', 'monitor:job:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1051, 'Add Job', 110, 2, '#', '', 'F', '0', 'monitor:job:add', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1052, 'Edit Job', 110, 3, '#', '', 'F', '0', 'monitor:job:edit', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1053, 'Delete Job', 110, 4, '#', '', 'F', '0', 'monitor:job:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1054, 'Change Job Status', 110, 5, '#', '', 'F', '0', 'monitor:job:changeStatus', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1055, 'Job Detail', 110, 6, '#', '', 'F', '0', 'monitor:job:detail', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1056, 'Export Job', 110, 7, '#', '', 'F', '0', 'monitor:job:export', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1057, 'Gen List', 114, 1, '#', '', 'F', '0', 'tool:gen:list', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1058, 'Edit Gen', 114, 2, '#', '', 'F', '0', 'tool:gen:edit', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1059, 'Delete Gen', 114, 3, '#', '', 'F', '0', 'tool:gen:remove', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1060, 'Preview Gen', 114, 4, '#', '', 'F', '0', 'tool:gen:preview', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(1061, 'Code Gen', 114, 5, '#', '', 'F', '0', 'tool:gen:code', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(2000, 'Nhóm Hãng Tàu', 3, 1, '/carrier/group', '', 'C', '0', 'carrier:group:view', 'fa fa-anchor', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', 'Carrier Group Menu'),
	(2001, 'Get Carrier Group', 2000, 1, '#', '', 'F', '0', 'carrier:group:list', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
	(2002, 'Add Carrier Group', 2000, 2, '#', '', 'F', '0', 'carrier:group:add', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
	(2003, 'Edit Carrier Group', 2000, 3, '#', '', 'F', '0', 'carrier:group:edit', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
	(2004, 'Delete Carrier Group', 2000, 4, '#', '', 'F', '0', 'carrier:group:remove', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
	(2005, 'Export Carrier Group', 2000, 5, '#', '', 'F', '0', 'carrier:group:export', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
	(2006, 'Tài Khoản Hãng Tàu', 3, 1, '/carrier/account', '', 'C', '0', 'carrier:account:view', 'fa fa-user-circle', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', 'Carrier Account Menu'),
	(2007, 'Carrier Account List', 2006, 1, '#', '', 'F', '0', 'carrier:account:list', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
	(2008, 'Add Carrier Account', 2006, 2, '#', '', 'F', '0', 'carrier:account:add', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
	(2009, 'Edit Carrier Account', 2006, 3, '#', '', 'F', '0', 'carrier:account:edit', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
	(2010, 'Delete Carrier Account', 2006, 4, '#', '', 'F', '0', 'carrier:account:remove', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
	(2011, 'Export Carrier Account', 2006, 5, '#', '', 'F', '0', 'carrier:account:export', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
	(2012, 'Vận Đơn', 3, 1, '/carrier/admin/do/getViewDo', '', 'C', '0', 'carrier:admin:do:getViewDo:view', 'fa fa-file-excel-o', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', 'Exchange Delivery Order Menu'),
	(2013, 'Delivery Order List', 2012, 1, '#', '', 'F', '0', 'equipment:do:list', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
	(2014, 'Add DO', 2012, 2, '#', '', 'F', '0', 'equipment:do:add', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
	(2015, 'Edit DO', 2012, 3, '#', '', 'F', '0', 'equipment:do:edit', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
	(2016, 'Delete DO', 2012, 4, '#', '', 'F', '0', 'equipment:do:remove', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
	(2017, 'Export DO', 2012, 5, '#', '', 'F', '0', 'equipment:do:export', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
	(2018, 'Quản lý Logistic', 0, 4, '#', 'menuItem', 'M', '0', NULL, 'fa fa-bars', 'admin', '2020-05-14 17:56:22', '', NULL, ''),
	(2019, 'Nhóm Logistic', 2018, 1, '/logistic/group', 'menuItem', 'C', '0', 'logistic:group:view', 'fa fa-truck', 'admin', '2020-05-14 18:03:26', '', NULL, ''),
	(2020, 'Tài khoản Logistic', 2018, 1, '/logistic/account', 'menuItem', 'C', '0', 'logistic:account:view', 'fa fa-address-book-o', 'admin', '2020-05-14 18:04:20', 'admin', '2020-05-14 18:24:13', ''),
	(2021, 'Quản Lý Vận Đơn', 0, 4, '#', 'menuItem', 'M', '0', NULL, 'fa fa-address-book-o', 'admin', '2020-06-08 14:19:16', '', NULL, ''),
	(2022, 'Kế Hoạch Bãi Cảng', 0, 5, '#', 'menuItem', 'M', '0', NULL, 'fa fa-anchor', 'admin', '2020-06-08 14:20:16', '', NULL, ''),
	(2023, 'Bộ phận làm thủ tục', 0, 6, '#', 'menuItem', 'M', '0', NULL, 'fa fa-cog', 'admin', '2020-06-09 20:23:58', '', NULL, ''),
	(2024, 'Hỗ trợ Robot làm lệnh', 2023, 1, '/om/executeCatos/index', 'menuItem', 'C', '0', '', 'fa fa-navicon', 'admin', '2020-06-09 20:56:31', 'admin', NULL, ''),
	(2026, 'Quản lý robot', 2, 6, 'system/robot/index', 'menuItem', 'C', '0', '', 'fa fa-cogs', 'admin', '2020-06-18 15:17:24', 'admin', '2020-06-18 15:17:49', '');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;

-- Dumping structure for table eport.sys_notice
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE IF NOT EXISTS `sys_notice` (
  `notice_id` int(4) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '公告标题',
  `notice_type` char(1) COLLATE utf8_bin NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` varchar(2000) COLLATE utf8_bin DEFAULT NULL COMMENT '公告内容',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='通知公告表';

-- Dumping data for table eport.sys_notice: ~2 rows (approximately)
/*!40000 ALTER TABLE `sys_notice` DISABLE KEYS */;
INSERT IGNORE INTO `sys_notice` (`notice_id`, `notice_title`, `notice_type`, `notice_content`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
	(1, 'Warm reminder: 2018-07-01 DNG new version released', '2', 'New version content', '0', 'admin', '2018-03-16 11:33:00', 'admin', '2020-03-28 14:23:04', ''),
	(2, 'Maintenance notice: 2018-07-01 Early morning maint', '1', 'Maintenance content', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
/*!40000 ALTER TABLE `sys_notice` ENABLE KEYS */;

-- Dumping structure for table eport.sys_oper_log
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE IF NOT EXISTS `sys_oper_log` (
  `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Log PK',
  `title` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT 'Module title',
  `business_type` int(2) DEFAULT '0' COMMENT 'Business type (0 other 1 new 2 modified 3 deleted)',
  `method` varchar(100) COLLATE utf8_bin DEFAULT '' COMMENT 'Method name',
  `request_method` varchar(10) COLLATE utf8_bin DEFAULT '' COMMENT 'Request method',
  `operator_type` int(1) DEFAULT '0' COMMENT 'Operation category (0 others 1 background user 2 mobile phone user)',
  `oper_name` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT 'operator',
  `dept_name` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT 'Department name',
  `oper_url` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT 'Request URL',
  `oper_ip` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT 'Host address',
  `oper_location` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT 'Operating place',
  `oper_param` varchar(2000) COLLATE utf8_bin DEFAULT '' COMMENT 'Request parameter',
  `json_result` varchar(2000) COLLATE utf8_bin DEFAULT '' COMMENT 'Return parameter',
  `status` int(1) DEFAULT '0' COMMENT 'Operation status (0 normal 1 abnormal)',
  `error_msg` varchar(2000) COLLATE utf8_bin DEFAULT '' COMMENT 'Error message',
  `oper_time` datetime DEFAULT NULL COMMENT 'Operating time',
  PRIMARY KEY (`oper_id`)
) ENGINE=InnoDB AUTO_INCREMENT=141 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Operational logging';

-- Dumping data for table eport.sys_oper_log: ~32 rows (approximately)
/*!40000 ALTER TABLE `sys_oper_log` DISABLE KEYS */;
INSERT IGNORE INTO `sys_oper_log` (`oper_id`, `title`, `business_type`, `method`, `request_method`, `operator_type`, `oper_name`, `dept_name`, `oper_url`, `oper_ip`, `oper_location`, `oper_param`, `json_result`, `status`, `error_msg`, `oper_time`) VALUES
	(109, 'Carrier Account', 1, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/add', '127.0.0.1', 'Intranet IP', '{\r\n  "groupId" : [ "1" ],\r\n  "carrierCode" : [ "123546" ],\r\n  "email" : [ "tai@gmail.com" ],\r\n  "password" : [ "admin123" ],\r\n  "fullName" : [ "Anh Tài" ]\r\n}', '{\r\n  "msg" : "Success",\r\n  "code" : 0\r\n}', 0, NULL, '2020-04-07 11:18:42'),
	(110, 'Carrier Account', 1, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/add', '127.0.0.1', 'Intranet IP', '{\r\n  "groupId" : [ "1" ],\r\n  "carrierCode" : [ "dnkasbdk" ],\r\n  "email" : [ "nqat2003@gmail.com" ],\r\n  "password" : [ "123qwe123" ],\r\n  "fullName" : [ "Anh Taif" ]\r\n}', '{\r\n  "msg" : "Success",\r\n  "code" : 0\r\n}', 0, NULL, '2020-04-08 10:20:34'),
	(111, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/carrier/account/resetPwd/2', '127.0.0.1', 'Intranet IP', '{ }', '"carrier/account/resetPwd"', 0, NULL, '2020-04-08 10:20:41'),
	(112, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwdSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/resetPwd', '127.0.0.1', 'Intranet IP', '{\r\n  "id" : [ "2" ],\r\n  "email" : [ "nqat2003@gmail.com" ],\r\n  "password" : [ "123456" ]\r\n}', '{\r\n  "msg" : "Success",\r\n  "code" : 0\r\n}', 0, NULL, '2020-04-08 10:21:33'),
	(113, 'Carrier Account', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.changeStatus()', 'POST', 1, 'admin', 'R&D', '/carrier/account/changeStatus', '127.0.0.1', 'Intranet IP', '{\r\n  "id" : [ "1" ],\r\n  "status" : [ "1" ]\r\n}', '{\r\n  "msg" : "Success",\r\n  "code" : 0\r\n}', 0, NULL, '2020-04-08 10:22:41'),
	(114, 'Carrier Account', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.changeStatus()', 'POST', 1, 'admin', 'R&D', '/carrier/account/changeStatus', '127.0.0.1', 'Intranet IP', '{\r\n  "id" : [ "1" ],\r\n  "status" : [ "0" ]\r\n}', '{\r\n  "msg" : "Success",\r\n  "code" : 0\r\n}', 0, NULL, '2020-04-08 10:22:48'),
	(115, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/carrier/account/resetPwd/1', '127.0.0.1', 'Intranet IP', '{ }', '"carrier/account/resetPwd"', 0, NULL, '2020-04-10 08:45:29'),
	(116, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/carrier/account/resetPwd/1', '127.0.0.1', 'Intranet IP', '{ }', '"carrier/account/resetPwd"', 0, NULL, '2020-04-14 08:19:17'),
	(117, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/carrier/account/resetPwd/1', '127.0.0.1', 'Intranet IP', '{ }', '"carrier/account/resetPwd"', 0, NULL, '2020-04-14 08:29:43'),
	(118, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/carrier/account/resetPwd/1', '127.0.0.1', 'Intranet IP', '{ }', '"carrier/account/resetPwd"', 0, NULL, '2020-04-14 11:49:10'),
	(119, 'Carrier Account', 1, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/add', '127.0.0.1', 'Intranet IP', '{\r\n  "groupId" : [ "1" ],\r\n  "carrierCode" : [ "1" ],\r\n  "email" : [ "tronghieu8531@gmail.com" ],\r\n  "password" : [ "123123" ],\r\n  "fullName" : [ "Nguyễn Trọng Hiếu" ],\r\n  "status" : [ "0" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-04-14 11:49:31'),
	(120, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  "id" : [ "1" ],\r\n  "groupCode" : [ "1" ],\r\n  "groupName" : [ "WWHA" ],\r\n  "operateCode" : [ "1" ],\r\n  "mainEmail" : [ "hello@gmail.com" ],\r\n  "doFlag" : [ "1" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-04-27 09:36:45'),
	(121, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  "id" : [ "1" ],\r\n  "groupCode" : [ "1" ],\r\n  "groupName" : [ "WWHA" ],\r\n  "operateCode" : [ "1" ],\r\n  "mainEmail" : [ "hello@gmail.com" ],\r\n  "doFlag" : [ "1" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-04-27 09:37:33'),
	(122, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  "id" : [ "1" ],\r\n  "groupCode" : [ "1" ],\r\n  "groupName" : [ "WWHA" ],\r\n  "operateCode" : [ "1" ],\r\n  "mainEmail" : [ "hello@gmail.com" ],\r\n  "doFlag" : [ "0" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-04-27 09:38:27'),
	(123, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  "id" : [ "1" ],\r\n  "groupCode" : [ "1" ],\r\n  "groupName" : [ "WWHA" ],\r\n  "operateCode" : [ "1" ],\r\n  "mainEmail" : [ "hello@gmail.com" ],\r\n  "doFlag" : [ "1" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-04-27 09:46:20'),
	(124, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  "id" : [ "1" ],\r\n  "groupCode" : [ "1" ],\r\n  "groupName" : [ "WWHA" ],\r\n  "operateCode" : [ "1" ],\r\n  "mainEmail" : [ "hello@gmail.com" ],\r\n  "doFlag" : [ "0" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-04-27 09:46:26'),
	(125, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  "id" : [ "1" ],\r\n  "groupCode" : [ "1" ],\r\n  "groupName" : [ "WWHA" ],\r\n  "operateCode" : [ "1" ],\r\n  "mainEmail" : [ "hello@gmail.com" ],\r\n  "doFlag" : [ "1" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-04-27 09:46:32'),
	(126, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  "id" : [ "1" ],\r\n  "groupCode" : [ "1" ],\r\n  "groupName" : [ "WWHA" ],\r\n  "operateCode" : [ "1" ],\r\n  "mainEmail" : [ "hello@gmail.com" ],\r\n  "doFlag" : [ "1" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-05-04 05:44:22'),
	(127, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  "id" : [ "1" ],\r\n  "groupCode" : [ "1" ],\r\n  "groupName" : [ "WWHA" ],\r\n  "operateCode" : [ "1" ],\r\n  "mainEmail" : [ "hello@gmail.com" ],\r\n  "doFlag" : [ "0" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-05-04 05:44:29'),
	(128, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  "id" : [ "1" ],\r\n  "groupCode" : [ "1" ],\r\n  "groupName" : [ "WWHA" ],\r\n  "operateCode" : [ "1" ],\r\n  "mainEmail" : [ "hello@gmail.com" ],\r\n  "doFlag" : [ "1" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-05-04 05:57:00'),
	(129, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  "id" : [ "1" ],\r\n  "groupCode" : [ "1" ],\r\n  "groupName" : [ "WWHA" ],\r\n  "operateCode" : [ "1" ],\r\n  "mainEmail" : [ "hello@gmail.com" ],\r\n  "doFlag" : [ "0" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-05-04 05:57:19'),
	(130, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  "id" : [ "1" ],\r\n  "groupCode" : [ "1" ],\r\n  "groupName" : [ "WWHA" ],\r\n  "operateCode" : [ "1" ],\r\n  "mainEmail" : [ "hello@gmail.com" ],\r\n  "doFlag" : [ "1" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-05-04 05:58:01'),
	(131, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  "id" : [ "1" ],\r\n  "groupCode" : [ "1" ],\r\n  "groupName" : [ "WWHA" ],\r\n  "operateCode" : [ "1" ],\r\n  "mainEmail" : [ "hello@gmail.com" ],\r\n  "doFlag" : [ "0" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-05-07 13:10:22'),
	(132, 'Carrier Group', 1, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/add', '127.0.0.1', 'Intranet IP', '{\r\n  "groupCode" : [ "sfa" ],\r\n  "groupName" : [ "asdfasdf" ],\r\n  "operateCode" : [ "wdfs" ],\r\n  "mainEmail" : [ "asdfsd@asdfa.com" ],\r\n  "doFlag" : [ "" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-05-07 13:10:46'),
	(133, 'Carrier Group', 1, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/add', '127.0.0.1', 'Intranet IP', '{\r\n  "groupCode" : [ "sdf" ],\r\n  "groupName" : [ "1asd" ],\r\n  "operateCode" : [ "asd" ],\r\n  "mainEmail" : [ "asdfa@asdf.com" ],\r\n  "doFlag" : [ "1" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-05-07 13:11:30'),
	(134, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  "id" : [ "3" ],\r\n  "groupCode" : [ "sdf" ],\r\n  "groupName" : [ "1asd" ],\r\n  "operateCode" : [ "asd" ],\r\n  "mainEmail" : [ "asdfa@asdf.com" ],\r\n  "doFlag" : [ "0" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-05-07 13:11:55'),
	(135, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  "id" : [ "2" ],\r\n  "groupCode" : [ "sfa" ],\r\n  "groupName" : [ "asdfasdf" ],\r\n  "operateCode" : [ "wdfs" ],\r\n  "mainEmail" : [ "asdfsd@asdfa.com" ],\r\n  "doFlag" : [ "1" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-05-07 13:12:02'),
	(136, 'Logistic Group', 1, 'vn.com.irtech.eport.logistic.controller.LogisticGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/group/add', '127.0.0.1', 'Intranet IP', '{\r\n  "groupName" : [ "Công ty abc" ],\r\n  "mainEmail" : [ "abc@abc.com" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-05-19 08:49:52'),
	(137, 'Logistic account', 1, 'vn.com.irtech.eport.logistic.controller.LogisticAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/account/add', '127.0.0.1', 'Intranet IP', '{\r\n  "groupId" : [ "2" ],\r\n  "email" : [ "tronghieu8531@gmail.com" ],\r\n  "password" : [ "hieu123" ],\r\n  "fullName" : [ "Nguyễn Trọng Hiếu" ],\r\n  "status" : [ "0" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-05-19 08:50:47'),
	(138, 'Logistic Group', 1, 'vn.com.irtech.eport.logistic.controller.LogisticGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/group/add', '127.0.0.1', 'Intranet IP', '{\r\n  "groupName" : [ "Vinconship" ],\r\n  "address" : [ "35 Cao Thang" ],\r\n  "mst" : [ "123123123123" ],\r\n  "phone" : [ "0541123412341" ],\r\n  "fax" : [ "1234234" ],\r\n  "emailAddress" : [ "email@email.com" ],\r\n  "businessRegistrationCertificate" : [ "asdfsadf" ],\r\n  "dateOfIssueRegistration" : [ "2020-05-28" ],\r\n  "placeOfIssueRegistration" : [ "asdfsadf" ],\r\n  "authorizedRepresentative" : [ "asdfsdaf" ],\r\n  "representativePosition" : [ "asdfsadf" ],\r\n  "followingAuthorizationFormNo" : [ "asdfas" ],\r\n  "signDate" : [ "2020-05-29" ],\r\n  "owned" : [ "asdfasdf" ],\r\n  "identifyCardNo" : [ "12341231231" ],\r\n  "dateOfIssueIdentify" : [ "2020-05-14" ],\r\n  "placeOfIssueIdentify" : [ "asdfasdf" ],\r\n  "mobilePhone" : [ "0935802290" ],\r\n  "email" : [ "tronghieu8531@gmail.com" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-05-28 13:49:54'),
	(139, 'Logistic account', 1, 'vn.com.irtech.eport.logistic.controller.LogisticAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/account/add', '127.0.0.1', 'Intranet IP', '{\r\n  "groupId" : [ "1" ],\r\n  "email" : [ "asdfasd@sadfs.com" ],\r\n  "userName" : [ "mst123123" ],\r\n  "password" : [ "123456" ],\r\n  "fullName" : [ "nguyen trong hieu" ],\r\n  "status" : [ "0" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-05-28 13:50:26'),
	(140, '菜单管理', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  "menuId" : [ "114" ],\r\n  "parentId" : [ "3" ],\r\n  "menuType" : [ "C" ],\r\n  "menuName" : [ "Code Gen" ],\r\n  "url" : [ "/tool/gen" ],\r\n  "target" : [ "menuItem" ],\r\n  "perms" : [ "tool:gen:view" ],\r\n  "orderNum" : [ "2" ],\r\n  "icon" : [ "#" ],\r\n  "visible" : [ "0" ]\r\n}', '{\r\n  "msg" : "Thành công",\r\n  "code" : 0\r\n}', 0, NULL, '2020-06-19 18:28:26');
/*!40000 ALTER TABLE `sys_oper_log` ENABLE KEYS */;

-- Dumping structure for table eport.sys_otp
DROP TABLE IF EXISTS `sys_otp`;
CREATE TABLE IF NOT EXISTS `sys_otp` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `opt_code` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'OTP CODE',
  `msg_status` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Status send mess',
  `verify_status` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Verify send mess',
  `expired_time` datetime NOT NULL COMMENT 'Expired Time',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='otp Code';

-- Dumping data for table eport.sys_otp: ~27 rows (approximately)
/*!40000 ALTER TABLE `sys_otp` DISABLE KEYS */;
INSERT IGNORE INTO `sys_otp` (`id`, `opt_code`, `msg_status`, `verify_status`, `expired_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
	(1, '226127', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(2, '59575', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(3, '830817', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(4, '665824', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(5, '453820', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(6, '884704', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(7, '743117', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(8, '846944', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(9, '700207', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(10, '300856', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(11, '351186', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(12, '373786', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(13, '481434', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(25, '130452', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(26, '569205', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(28, '435774', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(29, '353948', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(30, '155833', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(31, '874378', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(32, '568873', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(33, '762481', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(34, '672782', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(35, '253571', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(37, '900581', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(38, '647969', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(39, '236639', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL),
	(40, '348763', NULL, NULL, '0000-00-00 00:00:00', NULL, NULL, NULL, NULL);
/*!40000 ALTER TABLE `sys_otp` ENABLE KEYS */;

-- Dumping structure for table eport.sys_post
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE IF NOT EXISTS `sys_post` (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Job ID',
  `post_code` varchar(64) COLLATE utf8_bin NOT NULL COMMENT 'Post code',
  `post_name` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'position Name',
  `post_sort` int(4) NOT NULL COMMENT 'display order',
  `status` char(1) COLLATE utf8_bin NOT NULL COMMENT 'Status (0 normal 1 disabled)',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Job Information Sheet';

-- Dumping data for table eport.sys_post: ~4 rows (approximately)
/*!40000 ALTER TABLE `sys_post` DISABLE KEYS */;
INSERT IGNORE INTO `sys_post` (`post_id`, `post_code`, `post_name`, `post_sort`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
	(1, 'ceo', 'Chairman', 1, '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(2, 'se', 'project manager', 2, '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(3, 'hr', 'Human Resources', 3, '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
	(4, 'user', 'General staff', 4, '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
/*!40000 ALTER TABLE `sys_post` ENABLE KEYS */;

-- Dumping structure for table eport.sys_robot
DROP TABLE IF EXISTS `sys_robot`;
CREATE TABLE IF NOT EXISTS `sys_robot` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `uuid` varchar(64) COLLATE utf8_bin NOT NULL COMMENT 'uuid',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Status（0 Available 1 BUSY 2 OFFLINE）',
  `is_receive_cont_full_order` tinyint(1) DEFAULT '0' COMMENT 'Is support receive container full order',
  `is_send_cont_empty_order` tinyint(1) DEFAULT '0' COMMENT 'Is support send container empty order',
  `is_receive_cont_empty_order` tinyint(1) DEFAULT '0' COMMENT 'Is support receive container empty order',
  `is_send_cont_full_order` tinyint(1) DEFAULT '0' COMMENT 'Is support send container full order',
  `is_gate_in_order` tinyint(1) DEFAULT '0' COMMENT 'Is support gate in order',
  `ip_address` varchar(16) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ip address',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UC_uuid` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Robot';

-- Dumping data for table eport.sys_robot: ~0 rows (approximately)
/*!40000 ALTER TABLE `sys_robot` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_robot` ENABLE KEYS */;

-- Dumping structure for table eport.sys_role
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE IF NOT EXISTS `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Role ID',
  `role_name` varchar(30) COLLATE utf8_bin NOT NULL COMMENT 'Role Name',
  `role_key` varchar(100) COLLATE utf8_bin NOT NULL COMMENT 'Role permission string',
  `role_sort` int(4) NOT NULL COMMENT 'display order',
  `data_scope` char(1) COLLATE utf8_bin DEFAULT '1' COMMENT 'Data range (1: All data permissions 2: Custom data permissions 3: Data permissions of this department 4: Data permissions of this department and below)',
  `status` char(1) COLLATE utf8_bin NOT NULL COMMENT 'Role status (0 normal 1 disabled)',
  `del_flag` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Delete Flag (0 nomal 2 deleted)',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Role Information';

-- Dumping data for table eport.sys_role: ~2 rows (approximately)
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT IGNORE INTO `sys_role` (`role_id`, `role_name`, `role_key`, `role_sort`, `data_scope`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
	(1, 'Administrator', 'admin', 1, '1', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'Quản trị viên'),
	(2, 'Normal User', 'common', 2, '2', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'Nhân viên bình thường');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;

-- Dumping structure for table eport.sys_role_dept
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE IF NOT EXISTS `sys_role_dept` (
  `role_id` bigint(20) NOT NULL COMMENT 'Role ID',
  `dept_id` bigint(20) NOT NULL COMMENT 'Department ID',
  PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Role and department';

-- Dumping data for table eport.sys_role_dept: ~3 rows (approximately)
/*!40000 ALTER TABLE `sys_role_dept` DISABLE KEYS */;
INSERT IGNORE INTO `sys_role_dept` (`role_id`, `dept_id`) VALUES
	(2, 100),
	(2, 101),
	(2, 105);
/*!40000 ALTER TABLE `sys_role_dept` ENABLE KEYS */;

-- Dumping structure for table eport.sys_role_menu
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT 'Role ID',
  `menu_id` bigint(20) NOT NULL COMMENT 'Menu ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Role and menu association';

-- Dumping data for table eport.sys_role_menu: ~83 rows (approximately)
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
	(2, 1),
	(2, 2),
	(2, 3),
	(2, 100),
	(2, 101),
	(2, 102),
	(2, 103),
	(2, 104),
	(2, 105),
	(2, 106),
	(2, 107),
	(2, 108),
	(2, 109),
	(2, 110),
	(2, 111),
	(2, 112),
	(2, 113),
	(2, 114),
	(2, 115),
	(2, 500),
	(2, 501),
	(2, 1000),
	(2, 1001),
	(2, 1002),
	(2, 1003),
	(2, 1004),
	(2, 1005),
	(2, 1006),
	(2, 1007),
	(2, 1008),
	(2, 1009),
	(2, 1010),
	(2, 1011),
	(2, 1012),
	(2, 1013),
	(2, 1014),
	(2, 1015),
	(2, 1016),
	(2, 1017),
	(2, 1018),
	(2, 1019),
	(2, 1020),
	(2, 1021),
	(2, 1022),
	(2, 1023),
	(2, 1024),
	(2, 1025),
	(2, 1026),
	(2, 1027),
	(2, 1028),
	(2, 1029),
	(2, 1030),
	(2, 1031),
	(2, 1032),
	(2, 1033),
	(2, 1034),
	(2, 1035),
	(2, 1036),
	(2, 1037),
	(2, 1038),
	(2, 1039),
	(2, 1040),
	(2, 1041),
	(2, 1042),
	(2, 1043),
	(2, 1044),
	(2, 1045),
	(2, 1046),
	(2, 1047),
	(2, 1048),
	(2, 1049),
	(2, 1050),
	(2, 1051),
	(2, 1052),
	(2, 1053),
	(2, 1054),
	(2, 1055),
	(2, 1056),
	(2, 1057),
	(2, 1058),
	(2, 1059),
	(2, 1060),
	(2, 1061);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;

-- Dumping structure for table eport.sys_user
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE IF NOT EXISTS `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'User ID',
  `dept_id` bigint(20) DEFAULT NULL COMMENT 'Department ID',
  `login_name` varchar(30) COLLATE utf8_bin NOT NULL COMMENT 'Login Name',
  `user_name` varchar(30) COLLATE utf8_bin DEFAULT '' COMMENT 'User Name',
  `user_type` varchar(2) COLLATE utf8_bin DEFAULT '00' COMMENT 'User Type(00 system users 01 registered users)',
  `email` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT 'Email',
  `phonenumber` varchar(11) COLLATE utf8_bin DEFAULT '' COMMENT 'Phone',
  `sex` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Gender（0 Male 1 Female 2 Unknow）',
  `avatar` varchar(100) COLLATE utf8_bin DEFAULT '' COMMENT 'Avatar',
  `password` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT 'Password',
  `salt` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT 'Password Salt',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Status（0 Normal 1 Disabled）',
  `del_flag` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Delete Flag (0 nomal 2 deleted)',
  `login_ip` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT 'Login IP',
  `login_date` datetime DEFAULT NULL COMMENT 'Login Date',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='User Information';

-- Dumping data for table eport.sys_user: ~2 rows (approximately)
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT IGNORE INTO `sys_user` (`user_id`, `dept_id`, `login_name`, `user_name`, `user_type`, `email`, `phonenumber`, `sex`, `avatar`, `password`, `salt`, `status`, `del_flag`, `login_ip`, `login_date`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
	(1, 103, 'admin', 'DNG', '00', 'ry@163.com', '15888888888', '1', '', '29c67a30398638269fe600f73a054934', '111111', '0', '0', '127.0.0.1', '2020-06-19 18:27:59', 'admin', '2018-03-16 11:33:00', 'ry', '2020-06-19 18:27:59', '管理员'),
	(2, 105, 'ry', 'DNG', '00', 'ry@qq.com', '15666666666', '1', '', '8e6d98b90472783cc73c17047ddccf36', '222222', '0', '0', '127.0.0.1', '2018-03-16 11:33:00', 'admin', '2018-03-16 11:33:00', 'ry', '2020-03-28 07:21:51', '测试员');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;

-- Dumping structure for table eport.sys_user_online
DROP TABLE IF EXISTS `sys_user_online`;
CREATE TABLE IF NOT EXISTS `sys_user_online` (
  `sessionId` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '用户会话id',
  `login_name` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '登录账号',
  `dept_name` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '部门名称',
  `ipaddr` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '操作系统',
  `status` varchar(10) COLLATE utf8_bin DEFAULT '' COMMENT '在线状态on_line在线off_line离线',
  `start_timestamp` datetime DEFAULT NULL COMMENT 'sessionCreate Time',
  `last_access_time` datetime DEFAULT NULL COMMENT 'session最后访问时间',
  `expire_time` int(5) DEFAULT '0' COMMENT '超时时间，单位为分钟',
  PRIMARY KEY (`sessionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='在线用户记录';

-- Dumping data for table eport.sys_user_online: ~2 rows (approximately)
/*!40000 ALTER TABLE `sys_user_online` DISABLE KEYS */;
INSERT IGNORE INTO `sys_user_online` (`sessionId`, `login_name`, `dept_name`, `ipaddr`, `login_location`, `browser`, `os`, `status`, `start_timestamp`, `last_access_time`, `expire_time`) VALUES
	('0e1b5540-c9bc-4824-99b5-10584c90fe0f', 'asdfasd@sadfs.com', 'Logistic', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', 'on_line', '2020-06-19 22:34:22', '2020-06-19 23:02:21', NULL),
	('ee921558-62b0-4be5-ab8d-35f409695cb8', 'asdfasd@sadfs.com', 'Logistic', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', 'on_line', '2020-06-19 22:54:34', '2020-06-19 23:02:35', NULL);
/*!40000 ALTER TABLE `sys_user_online` ENABLE KEYS */;

-- Dumping structure for table eport.sys_user_post
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE IF NOT EXISTS `sys_user_post` (
  `user_id` bigint(20) NOT NULL COMMENT 'User ID',
  `post_id` bigint(20) NOT NULL COMMENT 'Post ID',
  PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='User and post';

-- Dumping data for table eport.sys_user_post: ~2 rows (approximately)
/*!40000 ALTER TABLE `sys_user_post` DISABLE KEYS */;
INSERT IGNORE INTO `sys_user_post` (`user_id`, `post_id`) VALUES
	(1, 1),
	(2, 2);
/*!40000 ALTER TABLE `sys_user_post` ENABLE KEYS */;

-- Dumping structure for table eport.sys_user_role
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE IF NOT EXISTS `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT 'User ID',
  `role_id` bigint(20) NOT NULL COMMENT 'Role ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='User and role association';

-- Dumping data for table eport.sys_user_role: ~2 rows (approximately)
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT IGNORE INTO `sys_user_role` (`user_id`, `role_id`) VALUES
	(1, 1),
	(2, 2);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
