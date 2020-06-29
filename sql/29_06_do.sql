-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.4.10-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             10.3.0.5771
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for eport
CREATE DATABASE IF NOT EXISTS `eport` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;
USE `eport`;

-- Dumping structure for table eport.edo
CREATE TABLE IF NOT EXISTS `edo` (
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
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'eDO Status (new, đã khai báo, đã làm lệnh, gatein, gateout)',
  `release_status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Release Status (0:tren bai cang, 1: released)',
  `create_source` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Nguon Tao: web, edi, api',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `del_flg` bit(1) DEFAULT NULL COMMENT 'Delete Flag',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='Exchange Delivery Order';

-- Dumping data for table eport.edo: ~19 rows (approximately)
DELETE FROM `edo`;
/*!40000 ALTER TABLE `edo` DISABLE KEYS */;
INSERT INTO `edo` (`id`, `carrier_id`, `carrier_code`, `order_number`, `bill_of_lading`, `business_unit`, `consignee`, `container_number`, `expired_dem`, `empty_container_depot`, `det_free_time`, `secure_code`, `release_date`, `vessel`, `voy_no`, `status`, `release_status`, `create_source`, `remark`, `del_flg`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
	(1, 1, 'MCA', '213123213', '0279704439', 'WHA', 'BRANCH OF VIET PEARL SERVICES COMPANY LIMITE', 'WHSU2493875', '2020-01-28 00:00:00', NULL, 6, 'web', NULL, NULL, NULL, '0', '0', NULL, NULL, b'0', NULL, '2020-06-27 09:24:01', NULL, NULL),
	(2, 1, 'MCA', 'VUQs084v', 'A78AA01260', 'IAL', 'TAX NO 3801211382 CONG TY TNHH IRTECH', 'CAIU6405266', '2020-03-21 00:00:00', NULL, 0, 'web', NULL, NULL, NULL, '0', '0', NULL, NULL, b'0', NULL, '2020-06-27 09:24:03', NULL, NULL),
	(3, 1, 'MCA', 'VUQs084v', 'A78AA01260', 'IAL', 'TAX NO 3801211382 CONG TY TNHH MOT THANH VIEN LONG PHAT IN', 'UUAU2730379', '2020-03-21 00:00:00', NULL, 6, 'web', NULL, NULL, NULL, '0', '0', NULL, NULL, b'0', NULL, '2020-06-27 09:24:03', NULL, NULL),
	(4, 1, 'MCA', 'S0071974', '32174123', 'YML', 'K010-901705380', 'BEAU4576096', '2020-04-03 00:00:00', NULL, 14, 'web', NULL, NULL, NULL, '0', '0', NULL, NULL, b'0', NULL, '2020-06-27 09:24:04', NULL, NULL),
	(5, 1, 'MCA', 'X0071986', '16373098', 'YML', 'THAM VUONG CO.,LTD', 'SEGU1671105', '2020-04-04 00:00:00', NULL, 3, 'web', NULL, NULL, NULL, '0', '0', NULL, NULL, b'0', NULL, '2020-06-27 09:24:05', NULL, NULL),
	(6, 1, 'MCA', 'X0071994', '16370598', 'YML', 'TO ORDER OF VIETINBANK DON', 'BEAU2289380', '2020-04-08 00:00:00', NULL, 21, 'web', NULL, NULL, NULL, '0', '0', NULL, NULL, b'0', NULL, '2020-06-27 09:24:05', NULL, NULL),
	(7, 1, 'MCA', 'X0071994', '16370598', 'YML', 'TO ORDER OF VIETINBANK DON', 'YMMU1090080', '2020-04-08 00:00:00', NULL, 21, 'web', NULL, NULL, NULL, '0', '0', NULL, NULL, b'0', NULL, '2020-06-27 09:24:06', NULL, NULL),
	(8, 1, 'MCA', 'X0071994', '16370598', 'YML', 'TO ORDER OF VIETINBANK DON', 'BEAU2567705', '2020-04-08 00:00:00', NULL, 21, 'web', NULL, NULL, NULL, '0', '0', NULL, NULL, b'0', NULL, '2020-06-27 09:24:06', NULL, NULL),
	(9, 1, 'MCA', '144490529', 'MCB771989', 'MAEU', 'SAMSUNG ELECTRONICS HCMC CE COMPLE', 'FCLU9407394', '2019-03-06 00:00:00', NULL, 6, 'web', NULL, NULL, NULL, '0', '0', NULL, NULL, b'0', NULL, '2020-06-27 09:24:07', NULL, NULL),
	(10, 1, 'MCA', 'VUQs084v', 'A78AA01260', 'IAL', 'TAX NO 3801211382 CONG TY TNHH MOT THANH VIEN LONG PHAT IN', 'IAAU2730379', '2020-03-21 00:00:00', NULL, 0, 'web', NULL, NULL, NULL, '0', '0', NULL, NULL, b'0', NULL, '2020-06-27 09:24:08', NULL, NULL),
	(11, 1, 'MCA', 'QTMqAEDn', '0279704439', 'WHA', 'BRANCH OF VIET PEARL SERVICES COMPANY LIMITE', 'WHSU2493475', '2020-01-28 00:00:00', NULL, 6, 'web', NULL, NULL, NULL, '0', '0', NULL, NULL, b'0', NULL, '2020-06-27 09:24:10', NULL, NULL),
	(12, 1, 'MCA', 'QTMqAEDn', '0279704433', 'WHA', 'BRANCH OF VIET PEARL SERVICES COMPANY LIMITE', 'WHSU2493845', '2020-01-28 00:00:00', NULL, 6, 'web', NULL, NULL, NULL, '0', '0', NULL, NULL, b'0', NULL, '2020-06-27 09:24:10', NULL, NULL),
	(13, 1, 'MCA', 'QTMqAEDn', '0279704438', 'WHA', 'BRANCH OF VIET PEARL SERVICES COMPANY LIMITE', 'WHSU2493845', '2020-01-28 00:00:00', NULL, 6, 'web', NULL, NULL, NULL, '0', '0', NULL, NULL, b'0', NULL, '2020-06-27 09:24:11', NULL, NULL),
	(14, 1, 'MCA', 'QTMqAEDn', '0279704439', 'WHA', 'BRANCH OF VIET PEARL SERVICES COMPANY LIMITE', 'WHSU2493873', '2020-01-28 00:00:00', NULL, 6, 'web', NULL, NULL, NULL, '0', '0', NULL, NULL, b'0', NULL, '2020-06-27 09:24:12', NULL, NULL),
	(15, 1, 'MCA', 'QTMqAEDn', '0279704432', 'WHA', 'BRANCH OF VIET PEARL SERVICES COMPANY LIMITE', 'WHSU2493845', '2020-01-28 00:00:00', NULL, 6, 'web', NULL, NULL, NULL, '0', '0', NULL, NULL, b'0', NULL, '2020-06-27 09:24:13', NULL, NULL),
	(16, 1, 'MCA', 'QTMqAEDn', '0279704430', 'WHA', 'BRANCH OF VIET PEARL SERVICES COMPANY LIMITE', 'WHSU2493877', '2020-01-28 00:00:00', NULL, 6, 'web', NULL, NULL, NULL, '0', '0', NULL, NULL, b'0', NULL, '2020-06-27 09:24:14', NULL, NULL),
	(17, 1, 'MCA', 'QTMqAEDn', '0279704439', 'WHA', 'BRANCH OF VIET PEARL SERVICES COMPANY LIMITE', 'WHSU2493871', '2020-01-28 00:00:00', NULL, 6, 'web', NULL, NULL, NULL, '0', '0', NULL, NULL, b'0', NULL, '2020-06-27 09:24:14', NULL, NULL),
	(18, 1, 'MCA', 'QTMqAEDn', '0279704439', 'WHA', 'BRANCH OF VIET PEARL SERVICES COMPANY LIMITE', 'WHSU2493874', '2020-01-28 00:00:00', NULL, 6, 'web', NULL, NULL, NULL, '0', '0', NULL, NULL, b'0', NULL, '2020-06-27 09:24:15', NULL, NULL),
	(19, 1, 'MCA', 'QTMqAEDn', '0279704439', 'WHA', 'BRANCH OF VIET PEARL SERVICES COMPANY LIMITE', 'WHSU2493876', '2020-01-28 00:00:00', NULL, 6, 'web', NULL, NULL, NULL, '0', '0', NULL, NULL, b'0', NULL, '2020-06-27 09:24:17', NULL, NULL);
/*!40000 ALTER TABLE `edo` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
