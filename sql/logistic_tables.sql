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

-- Dumping structure for table eport.logistic_account
CREATE TABLE IF NOT EXISTS `logistic_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `group_id` bigint(20) NOT NULL COMMENT 'Master Account',
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Logistic account';

-- Data exporting was unselected.
-- Dumping structure for table eport.logistic_group
CREATE TABLE IF NOT EXISTS `logistic_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `group_name` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Group Name',
  `main_email` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Main Emails',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Logistic Group';

-- Data exporting was unselected.
-- Dumping structure for table eport.shipment
CREATE TABLE IF NOT EXISTS `shipment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `logistic_account_id` bigint(20) NOT NULL,
  `logistic_group_id` bigint(20) NOT NULL,
  `service_id` tinyint(4) NOT NULL COMMENT 'Dich Vu',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Shipment';

-- Data exporting was unselected.
-- Dumping structure for table eport.shipment_detail
CREATE TABLE IF NOT EXISTS `shipment_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `shipment_id` bigint(20) NOT NULL COMMENT 'Ma Lo',
  `register_no` varchar(6) COLLATE utf8_bin NOT NULL COMMENT 'Ma DK',
  `container_no` varchar(12) COLLATE utf8_bin NOT NULL COMMENT 'Container Number',
  `container_status` varchar(3) COLLATE utf8_bin DEFAULT NULL COMMENT 'Container Status (S,D)',
  `sztp` varchar(4) COLLATE utf8_bin NOT NULL COMMENT 'Size Type',
  `fe` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'FE',
  `booking_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'Booking Number',
  `bl_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'BL number',
  `seal_no` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Seal Number',
  `consignee` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Shipper/consignee',
  `expired_dem` datetime DEFAULT NULL COMMENT 'Han Lenh',
  `wgt` int(11) NOT NULL COMMENT 'Weight ',
  `vsl_nm` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'Vessel name',
  `voy_no` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'Voyage',
  `ope_code` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'Operator Code',
  `loading_port` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Cang Chuyen Tai',
  `discharge_port` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Cang Dich',
  `transport_type` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Phuong Tien',
  `empty_depot` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Noi Ha Vo',
  `vgm_chk` bit(1) DEFAULT NULL COMMENT 'VGM Check',
  `vgm` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'VGM',
  `vgm_person_info` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT 'VGM Person Info',
  `custom_declare_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'Custom Declare Number',
  `custom_status` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Custom Status (N,C,H,R)',
  `payment_status` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Payment Status (Y,N,W,E)',
  `process_status` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Process Status(Y,N,E)',
  `do_status` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'T.T DO Goc',
  `do_received_time` datetime DEFAULT NULL COMMENT 'Ngay Nhan DO Goc',
  `user_verify_status` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Xac Thuc (Y,N)',
  `preorder_pickup` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Boc Chi Dinh (Y,N)',
  `status` tinyint(4) DEFAULT NULL COMMENT 'Status',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Shipment Details';

-- Data exporting was unselected.
-- Dumping structure for table eport.shipment_transport
CREATE TABLE IF NOT EXISTS `shipment_transport` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `logistic_group_id` bigint(20) NOT NULL,
  `shipment_id` bigint(20) NOT NULL,
  `container_no` varchar(12) COLLATE utf8_bin DEFAULT NULL,
  `transport_ids` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Thong tin dieu xe';

-- Data exporting was unselected.
-- Dumping structure for table eport.transport_account
CREATE TABLE IF NOT EXISTS `transport_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `logistic_group_id` bigint(20) NOT NULL COMMENT 'Logistic Group',
  `plate_number` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Bien So Xe',
  `mobile_number` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'So DT',
  `full_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ho va Ten',
  `password` varchar(64) COLLATE utf8_bin NOT NULL COMMENT 'Mat Khau',
  `salt` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'Salt',
  `status` char(1) COLLATE utf8_bin NOT NULL COMMENT 'Trang Thai',
  `del_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT 'Delete Flag',
  `valid_date` datetime NOT NULL COMMENT 'Hieu Luc Den',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Driver login info';

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
=======
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

-- Dumping structure for table eport.logistic_account
CREATE TABLE IF NOT EXISTS `logistic_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `group_id` bigint(20) NOT NULL COMMENT 'Master Account',
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Logistic account';

-- Data exporting was unselected.
-- Dumping structure for table eport.logistic_group
CREATE TABLE IF NOT EXISTS `logistic_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `group_name` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Group Name',
  `main_email` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Main Emails',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Logistic Group';

-- Data exporting was unselected.
-- Dumping structure for table eport.shipment
CREATE TABLE IF NOT EXISTS `shipment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `logistic_account_id` bigint(20) NOT NULL,
  `logistic_group_id` bigint(20) NOT NULL,
  `service_id` tinyint(4) NOT NULL COMMENT 'Dich Vu',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Shipment';

-- Data exporting was unselected.
-- Dumping structure for table eport.shipment_detail
CREATE TABLE IF NOT EXISTS `shipment_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `shipment_id` bigint(20) NOT NULL COMMENT 'Ma Lo',
  `register_no` varchar(6) COLLATE utf8_bin NOT NULL COMMENT 'Ma DK',
  `container_no` varchar(12) COLLATE utf8_bin NOT NULL COMMENT 'Container Number',
  `container_status` varchar(3) COLLATE utf8_bin DEFAULT NULL COMMENT 'Container Status (S,D)',
  `sztp` varchar(4) COLLATE utf8_bin NOT NULL COMMENT 'Size Type',
  `fe` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'FE',
  `booking_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'Booking Number',
  `bl_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'BL number',
  `seal_no` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Seal Number',
  `consignee` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Shipper/consignee',
  `expired_dem` datetime DEFAULT NULL COMMENT 'Han Lenh',
  `wgt` int(11) NOT NULL COMMENT 'Weight ',
  `vsl_nm` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'Vessel name',
  `voy_no` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'Voyage',
  `ope_code` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'Operator Code',
  `loading_port` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Cang Chuyen Tai',
  `discharge_port` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Cang Dich',
  `transport_type` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Phuong Tien',
  `empty_depot` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Noi Ha Vo',
  `vgm_chk` bit(1) DEFAULT NULL COMMENT 'VGM Check',
  `vgm` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'VGM',
  `vgm_person_info` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT 'VGM Person Info',
  `custom_declare_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'Custom Declare Number',
  `custom_status` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Custom Status (N,C,H,R)',
  `payment_status` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Payment Status (Y,N,W,E)',
  `process_status` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Process Status(Y,N,E)',
  `do_status` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'T.T DO Goc',
  `do_received_time` datetime DEFAULT NULL COMMENT 'Ngay Nhan DO Goc',
  `user_verify_status` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Xac Thuc (Y,N)',
  `preorder_pickup` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Boc Chi Dinh (Y,N)',
  `status` tinyint(4) DEFAULT NULL COMMENT 'Status',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Shipment Details';

-- Data exporting was unselected.
-- Dumping structure for table eport.shipment_transport
CREATE TABLE IF NOT EXISTS `shipment_transport` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `logistic_group_id` bigint(20) NOT NULL,
  `shipment_id` bigint(20) NOT NULL,
  `container_no` varchar(12) COLLATE utf8_bin DEFAULT NULL,
  `transport_ids` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Thong tin dieu xe';

-- Data exporting was unselected.
-- Dumping structure for table eport.transport_account
CREATE TABLE IF NOT EXISTS `transport_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `logistic_group_id` bigint(20) NOT NULL COMMENT 'Logistic Group',
  `plate_number` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Bien So Xe',
  `mobile_number` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'So DT',
  `full_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ho va Ten',
  `password` varchar(64) COLLATE utf8_bin NOT NULL COMMENT 'Mat Khau',
  `salt` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'Salt',
  `status` char(1) COLLATE utf8_bin NOT NULL COMMENT 'Trang Thai',
  `del_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT 'Delete Flag',
  `valid_date` datetime NOT NULL COMMENT 'Hieu Luc Den',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Driver login info';

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
