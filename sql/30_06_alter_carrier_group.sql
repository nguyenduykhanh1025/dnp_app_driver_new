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

-- Dumping structure for table eport.carrier_group
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
  `api_private_key` text COLLATE utf8_bin DEFAULT NULL COMMENT 'API private key',
  `api_public_key` text COLLATE utf8_bin DEFAULT NULL COMMENT 'API public key',
  `path_edi_receive` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Path edi file',
  `path_edi_backup` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Path_edi_moving',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Carrier Group';

-- Dumping data for table eport.carrier_group: ~3 rows (approximately)
DELETE FROM `carrier_group`;
/*!40000 ALTER TABLE `carrier_group` DISABLE KEYS */;
INSERT INTO `carrier_group` (`id`, `group_code`, `group_name`, `operate_code`, `main_email`, `do_type`, `create_by`, `create_time`, `update_by`, `update_time`, `do_flag`, `edo_flag`, `api_private_key`, `api_public_key`, `path_edi_receive`, `path_edi_backup`) VALUES
	(1, 'CMA', 'WWHA', 'CNC,CMA,APL', 'hello@gmail.com', '0', '123123', NULL, 'DNG', '2020-06-30 13:53:01', '1', '1', NULL, NULL, 'D:\\testReadFile', 'D:\\DaNangPort\\eDO'),
	(2, 'CMA', 'asdfasdf', 'wdfs', 'asdfsd@asdfa.com', '0', 'DNG', '2020-05-07 13:10:46', 'DNG', '2020-06-30 13:53:07', '0', '1', NULL, NULL, 'D:\\testReadFile', 'D:\\testReadFile'),
	(3, 'CMA', '1asd', 'asd', 'asdfa@asdf.com', '0', 'DNG', '2020-05-07 13:11:30', 'DNG', '2020-06-30 13:53:15', '0', '1', NULL, NULL, 'D:\\testReadFile', 'D:\\testReadFile');
/*!40000 ALTER TABLE `carrier_group` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
