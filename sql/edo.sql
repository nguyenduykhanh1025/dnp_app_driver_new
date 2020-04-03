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

-- Dumping structure for table eport.equipment_do
CREATE TABLE IF NOT EXISTS `equipment_do` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `shipping_line_id` bigint(20) NOT NULL COMMENT 'ID Hang Tau',
  `shipping_line_code` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'Ma Hang Tau',
  `document_id` bigint(20) DEFAULT NULL COMMENT 'Document',
  `order_number` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'So Lenh',
  `bill_of_lading` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'So B/L',
  `business_unit` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Don Vi Khai Thac',
  `consignee` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Chu Hang',
  `container_number` varchar(11) COLLATE utf8_bin NOT NULL COMMENT 'So Cont',
  `expired_dem` datetime NOT NULL COMMENT 'Han Lenh',
  `empty_container_depot` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Noi Ha Rong',
  `det_free_time` tinyint(4) DEFAULT NULL COMMENT 'So Ngay Mien Luu Vo Cont',
  `secure_code` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ma Bao Mat',
  `release_date` datetime DEFAULT NULL COMMENT 'Ngay Release',
  `vessel` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Tau',
  `voy_no` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Chuyen',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'DO Status',
  `process_status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Process Status',
  `document_status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Document Status',
  `release_status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Release Status',
  `create_source` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Nguon Tao: eport, edi, catos',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Exchange Delivery Order';

-- Dumping data for table eport.equipment_do: ~0 rows (approximately)
/*!40000 ALTER TABLE `equipment_do` DISABLE KEYS */;
/*!40000 ALTER TABLE `equipment_do` ENABLE KEYS */;

-- Dumping structure for table eport.equipment_document
CREATE TABLE IF NOT EXISTS `equipment_document` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `file_name` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'File Name',
  `original_file_name` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Original File Name',
  `file_path` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'File Path',
  `mime_type` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Mime Type',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Upload By',
  `create_time` datetime DEFAULT NULL COMMENT 'Upload Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Equipment attached document';

-- Dumping data for table eport.equipment_document: ~0 rows (approximately)
/*!40000 ALTER TABLE `equipment_document` DISABLE KEYS */;
/*!40000 ALTER TABLE `equipment_document` ENABLE KEYS */;

-- Dumping structure for table eport.shipping_account
CREATE TABLE IF NOT EXISTS `shipping_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `email` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Email',
  `password` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Password',
  `salt` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'Salt',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Status（0 Normal 1 Disabled）',
  `shipping_code` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'Shipping Line Code',
  `shipping_name` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Shipping Line Name',
  `del_flag` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Delete Flag (0 nomal 2 deleted)',
  `login_ip` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT 'Login IP',
  `login_date` datetime DEFAULT NULL COMMENT 'Login Date',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Shipping line account';

-- Dumping data for table eport.shipping_account: ~0 rows (approximately)
/*!40000 ALTER TABLE `shipping_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `shipping_account` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
