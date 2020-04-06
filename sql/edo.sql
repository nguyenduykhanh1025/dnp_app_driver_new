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

-- Dumping structure for table eport.carrier_account
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Carrier account';

-- Dumping data for table eport.carrier_account: ~0 rows (approximately)
/*!40000 ALTER TABLE `carrier_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `carrier_account` ENABLE KEYS */;

-- Dumping structure for table eport.carrier_group
CREATE TABLE IF NOT EXISTS `carrier_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_code` varchar(5) COLLATE utf8_bin NOT NULL,
  `group_name` varchar(255) COLLATE utf8_bin NOT NULL,
  `operate_code` varchar(50) COLLATE utf8_bin NOT NULL,
  `main_email` varchar(255) COLLATE utf8_bin NOT NULL,
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Carrier Group';

-- Dumping data for table eport.carrier_group: ~0 rows (approximately)
/*!40000 ALTER TABLE `carrier_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `carrier_group` ENABLE KEYS */;

-- Dumping structure for table eport.equipment_do
CREATE TABLE IF NOT EXISTS `equipment_do` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `carrier_id` bigint(20) NOT NULL COMMENT 'ID Nhan Vien Hang Tau',
  `carrier_code` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'Ma Hang Tau',
  `order_number` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'So Lenh (Optional)',
  `bill_of_lading` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'So B/L',
  `business_unit` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Don Vi Khai Thac (Optional)',
  `consignee` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Chu Hang',
  `container_number` varchar(11) COLLATE utf8_bin NOT NULL COMMENT 'So Cont',
  `expired_dem` datetime NOT NULL COMMENT 'Han Lenh',
  `empty_container_depot` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Noi Ha Rong',
  `det_free_time` tinyint(4) DEFAULT NULL COMMENT 'So Ngay Mien Luu Vo Cont',
  `secure_code` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ma Bao Mat (optional)',
  `release_date` datetime DEFAULT NULL COMMENT 'Ngay Release',
  `vessel` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Tau',
  `voy_no` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Chuyen',
  `do_type` varchar(3) COLLATE utf8_bin DEFAULT NULL COMMENT 'DO Type',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'DO Status',
  `process_status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Process Status',
  `document_status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Document Status',
  `document_receipt_date` datetime DEFAULT NULL COMMENT 'Document Receipt Date',
  `release_status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Release Status',
  `create_source` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Nguon Tao: web, edi, catos',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Exchange Delivery Order';

-- Dumping data for table eport.equipment_do: ~0 rows (approximately)
/*!40000 ALTER TABLE `equipment_do` DISABLE KEYS */;
/*!40000 ALTER TABLE `equipment_do` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
