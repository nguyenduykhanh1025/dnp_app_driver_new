-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th6 09, 2020 lúc 08:49 AM
-- Phiên bản máy phục vụ: 10.4.11-MariaDB
-- Phiên bản PHP: 7.4.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `eport`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `carrier_account`
--

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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Carrier account';

--
-- Đang đổ dữ liệu cho bảng `carrier_account`
--

INSERT INTO `carrier_account` (`id`, `group_id`, `carrier_code`, `email`, `password`, `salt`, `full_name`, `status`, `del_flag`, `login_ip`, `login_date`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
(1, 1, 'CNC,CMA', 'minhtc@admin.com', '1bde5aa6afd5f5bdc36c5df02594d5fe', 'ef6c96', 'Trần Minh', '0', '0', '192.168.1.68', '2020-06-09 09:14:08', NULL, '', '2020-04-07 11:18:40', 'DNG', '2020-06-09 09:14:08'),
(2, 1, 'CNC,CMA', 'nqat2003@gmail.com', '88cd9c095318aa5d9f84d589f437760f', '0d78ae', 'Anh Taif', '0', '0', '', NULL, NULL, 'DNG', '2020-04-08 10:20:33', 'DNG', '2020-04-08 10:21:33'),
(3, 2, 'CNC,CMA', 'huydp@irtech.com.vn', '00c8790aeeff0eaece5a571ee0dd41ee', '807d42', 'Huy Do', '0', '0', '42.118.93.19', '2020-04-28 13:17:07', NULL, 'DNG', '2020-04-14 11:06:16', '', '2020-04-28 13:17:07'),
(6, 5, 'CNC,CMA', 'han@gmail.com', '84db890c87b32ee0564d56983a89d7e0', '5d4101', 'Han', '0', '0', '113.176.195.221', '2020-04-15 15:54:11', NULL, 'DNG', '2020-04-15 14:42:12', '', '2020-04-15 15:54:11'),
(7, 3, 'CNC,CMA,WHL', 'minhtc@gmail.com', '072c5ea8b19f57d68bb5e7567d687cd4', 'c769b1', 'admin123', '0', '0', '192.168.1.68', '2020-06-09 13:42:37', NULL, 'DNG', '2020-04-24 13:48:32', '', '2020-06-09 13:42:37');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `carrier_group`
--

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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Carrier Group';

--
-- Đang đổ dữ liệu cho bảng `carrier_group`
--

INSERT INTO `carrier_group` (`id`, `group_code`, `group_name`, `operate_code`, `main_email`, `create_by`, `create_time`, `update_by`, `update_time`, `do_flag`, `edo_flag`) VALUES
(1, '1', 'WWHA', 'ABC BAC', 'hello@gmail.com', '123123', NULL, 'DNG', '2020-05-27 10:48:14', '1', '0'),
(2, 'SITC', 'SITC', 'S', 'sitc@abc.com', 'DNG', '2020-04-14 11:05:54', 'DNG', '2020-05-27 10:47:59', '0', '0'),
(3, 'CMA', 'CMA', 'ABC.ABC', 'abc@gmail.com', 'DNG', '2020-04-14 12:41:43', 'DNG', '2020-05-27 10:48:07', '0', '0'),
(4, 'ABC', 'ABC', 'A1, A2, A3', 'abc@abc.com', 'DNG', '2020-04-15 08:59:43', '', NULL, '0', '0'),
(5, '123', 'Wanhai', 'ABC,ABC', 'tai@gmail.com', 'DNG', '2020-04-15 14:34:40', 'DNG', '2020-05-27 10:48:23', '0', '0');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `equipment_do`
--

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
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Exchange Delivery Order';

--
-- Đang đổ dữ liệu cho bảng `equipment_do`
--

INSERT INTO `equipment_do` (`id`, `carrier_id`, `carrier_code`, `order_number`, `bill_of_lading`, `business_unit`, `consignee`, `container_number`, `expired_dem`, `empty_container_depot`, `det_free_time`, `secure_code`, `release_date`, `vessel`, `voy_no`, `do_type`, `status`, `process_status`, `document_status`, `document_receipt_date`, `release_status`, `create_source`, `remark`, `process_remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
(2, 1, 'WHA', NULL, '1', NULL, '1', '1', '2020-04-07 13:07:54', '1', 1, NULL, NULL, '', '', NULL, '0', '0', '0', NULL, '0', NULL, 'null', NULL, 'Anh Tai', '2020-04-07 13:07:54', NULL, NULL),
(3, 1, 'WHA', NULL, '1', NULL, '1', '1', '2020-04-07 13:09:24', '1', 1, NULL, NULL, '', '', NULL, '0', '0', '0', NULL, '0', NULL, 'null', NULL, 'Anh Tai', '2020-04-07 13:09:24', NULL, NULL),
(4, 1, 'WHA', NULL, '123', NULL, '3', '2131', '2020-04-07 13:09:24', 'null', NULL, NULL, NULL, '', NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, 'null', NULL, 'Anh Tai', '2020-04-07 13:09:24', 'admin', '2020-04-14 13:42:03'),
(5, 1, 'WHA', NULL, '123', NULL, '3123', '1231', '2020-04-07 13:33:30', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, 'null', NULL, 'Anh Tài', '2020-04-07 13:33:30', 'admin', '2020-04-14 13:42:03'),
(6, 1, 'WHA', NULL, '123', NULL, '123', '123', '2020-04-07 13:33:30', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, 'null', NULL, 'Anh Tài', '2020-04-07 13:33:30', 'admin', '2020-04-14 13:42:03'),
(7, 1, 'WHA', NULL, '123', NULL, '3123', '1231', '2020-04-07 13:35:05', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 13:35:05', 'admin', '2020-04-14 13:42:03'),
(8, 1, 'WHA', NULL, '123', NULL, '123', '123', '2020-04-07 13:35:05', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 13:35:05', 'admin', '2020-04-14 13:42:03'),
(9, 1, 'WHA', NULL, '123', NULL, '3123', '1231', '2010-12-25 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 13:39:19', 'admin', '2020-04-14 13:42:03'),
(10, 1, 'WHA', NULL, '123', NULL, '123', '123', '2010-12-25 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 13:39:19', 'admin', '2020-04-14 13:42:03'),
(11, 1, 'WHA', NULL, '123', NULL, '3123', '1231', '2020-03-12 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 13:40:58', 'admin', '2020-04-14 13:42:03'),
(12, 1, 'WHA', NULL, '123', NULL, '123', '123', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 13:40:58', 'admin', '2020-04-14 13:42:03'),
(13, 1, 'WHA', NULL, '3123', NULL, '23', '21', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 14:15:36', NULL, NULL),
(14, 1, 'WHA', NULL, '123', NULL, '231', '123', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 14:15:36', 'admin', '2020-04-14 13:42:03'),
(15, 1, 'WHA', NULL, '3123', NULL, '23', '21', '2020-04-05 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 14:15:56', NULL, NULL),
(16, 1, 'WHA', NULL, '123', NULL, '231', '123', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 14:15:56', 'admin', '2020-04-14 13:42:03'),
(17, 1, 'WHA', NULL, '23123', NULL, '231', '1', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '1', '2020-04-14 11:29:40', '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 14:29:05', 'admin', NULL),
(18, 1, 'WHA', NULL, '1231', NULL, '123123', '23123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 14:29:05', NULL, NULL),
(19, 1, '123', NULL, '231', NULL, '07/04/2020', '23123', '2020-04-07 16:44:18', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 11:13:43', '0', NULL, '\"1\":[nul', NULL, 'Anh Tài', '2020-04-07 16:44:18', 'admin', '2020-04-14 11:13:43'),
(20, 1, 'ádfads', NULL, '1231', NULL, '23123', '23123', '2020-04-23 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 16:49:46', NULL, NULL),
(21, 1, '1234', NULL, '123', NULL, '1231', '123', '2020-04-16 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 17:09:54', 'admin', '2020-04-14 13:42:03'),
(22, 1, '1234', NULL, '123', NULL, '123', '123', '2020-04-25 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 17:13:26', 'admin', '2020-04-14 13:42:03'),
(23, 1, '1234', NULL, '123', NULL, '123', '123', '2020-04-25 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-07 17:18:23', 'admin', '2020-04-14 13:42:03'),
(28, 1, '1234', NULL, '123', NULL, '123', '23', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-07 19:24:10', 'admin', '2020-04-14 13:42:03'),
(29, 1, '1234', NULL, '1231', NULL, '123123', '1231', '2020-04-17 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-07 19:33:34', NULL, NULL),
(30, 1, '1234', NULL, '21312', NULL, '1231', '12312', '2020-04-15 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-07 19:33:48', NULL, NULL),
(31, 1, '1234', NULL, '123', NULL, '23123', '123', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-07 19:35:32', 'admin', '2020-04-14 13:42:03'),
(32, 1, '1234', NULL, '123', NULL, '123', '123123', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-07 19:35:58', 'admin', '2020-04-14 13:42:03'),
(33, 1, '1234', NULL, '123', NULL, '23123', '2312', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-07 19:39:23', 'admin', '2020-04-14 13:42:03'),
(34, 1, '1234', NULL, '12312', NULL, '123123', '12312', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-07 19:40:10', NULL, NULL),
(35, 1, '1234', NULL, '123123', NULL, '123123', '12312', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 08:16:03', NULL, NULL),
(36, 1, '1234', NULL, '123123', NULL, '123123123', '123123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 08:18:48', NULL, NULL),
(37, 1, '1234', NULL, '123123', NULL, '123123', '123123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 08:23:01', NULL, NULL),
(38, 1, '1234', NULL, '1231', NULL, '123123', '23123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 08:23:23', NULL, NULL),
(39, 1, '1234', NULL, '123123', NULL, '3123', '1231', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 08:23:43', NULL, NULL),
(40, 1, '1234', NULL, '213123', NULL, '231231', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 08:24:05', NULL, NULL),
(41, 1, '1234', NULL, '12312', NULL, '231231', '31231', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 08:25:20', NULL, NULL),
(42, 1, '1234', NULL, '1232', NULL, '2312', '31231', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 08:25:20', NULL, NULL),
(43, 1, '1234', NULL, '31231', NULL, '3123123', '2312', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '1', '2020-04-14 11:29:17', '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 08:25:48', 'admin', NULL),
(44, 1, '1234', NULL, '1231', NULL, '12312', '231', '2020-04-15 00:00:00', '123', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 08:28:25', NULL, NULL),
(45, 1, '1234', NULL, '312', NULL, '1231231', '1231', '2020-04-16 00:00:00', '2312', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 08:28:25', NULL, NULL),
(46, 1, '1234', NULL, '12312', NULL, '123', '12312', '2020-04-10 00:00:00', '1231231', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 08:28:26', NULL, NULL),
(47, 1, '1234', NULL, '3123', NULL, '1231231', '312', '2020-04-14 00:00:00', '3123123', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 08:28:26', NULL, NULL),
(48, 1, '1234', NULL, '2312', NULL, '31', '123', '2020-04-23 00:00:00', '3123', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 08:28:26', NULL, NULL),
(49, 1, '\"WWHA\"', NULL, '123', NULL, '123', '23', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 09:29:21', 'admin', '2020-04-14 13:42:03'),
(50, 1, '\"WWHA\"', NULL, '123', NULL, '23', '3', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 09:29:21', 'admin', '2020-04-14 13:42:03'),
(51, 1, '\"WWHA\"', NULL, '123', NULL, '23', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 09:29:57', 'admin', '2020-04-14 13:42:03'),
(52, 1, 'WWHA', NULL, '123', NULL, '08/04/2020', '123', '2020-04-08 09:36:16', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 09:36:16', 'admin', '2020-04-14 13:42:03'),
(54, 1, 'WWHA', NULL, '123', NULL, '08/04/2020', '123', '2020-04-08 09:40:46', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, 'null]', NULL, 'Anh Tài', '2020-04-08 09:41:04', 'admin', '2020-04-14 13:42:03'),
(70, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1234', '2020-04-08 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:51:35', NULL, NULL),
(71, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:51:36', NULL, NULL),
(72, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:51:36', NULL, NULL),
(73, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1237', '2020-04-11 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:51:37', NULL, NULL),
(74, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1238', '2020-04-12 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:51:37', NULL, NULL),
(75, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1234', '2020-04-08 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '\"Hello\"', NULL, 'Anh Tài', '2020-04-08 10:55:05', NULL, NULL),
(76, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:55:05', NULL, NULL),
(77, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:55:06', NULL, NULL),
(78, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1237', '2020-04-11 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:55:06', NULL, NULL),
(79, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1238', '2020-04-12 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:55:06', NULL, NULL),
(80, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1234', '2020-04-08 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:57:09', NULL, NULL),
(81, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:57:12', NULL, NULL),
(82, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:57:13', NULL, NULL),
(83, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1237', '2020-04-11 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 10:57:20', NULL, NULL),
(84, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1238', '2020-04-12 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '\"hrahra\"', NULL, 'Anh Tài', '2020-04-08 10:57:23', NULL, NULL),
(85, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1234', '2020-04-08 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 11:07:00', NULL, NULL),
(86, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 11:07:02', NULL, NULL),
(87, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 11:07:03', NULL, NULL),
(88, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1237', '2020-04-11 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 11:07:04', NULL, NULL),
(89, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1238', '2020-04-12 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'fadsfkjnjadsf', NULL, 'Anh Tài', '2020-04-08 11:07:06', NULL, NULL),
(91, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '2131231', NULL, 'Anh Tài', '2020-04-08 11:21:58', NULL, NULL),
(92, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-08 11:22:03', NULL, NULL),
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
(110, 1, 'WWHA', NULL, '123', NULL, '123', '123', '2020-04-16 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-09 09:19:19', 'admin', '2020-04-14 13:42:03'),
(113, 1, 'WWHA', NULL, '123123', NULL, '123123', 'qưeqưe', '2020-04-09 00:00:00', NULL, 1, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-09 17:34:37', NULL, NULL),
(114, 1, 'WWHA', NULL, '12312', NULL, '12312312', '123123', '2020-04-23 00:00:00', NULL, 2, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-09 17:34:37', NULL, NULL),
(115, 1, 'WWHA', NULL, '123', NULL, '21323', '123', '2020-04-10 00:00:00', '123', 123, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 13:41:51', '0', NULL, NULL, NULL, 'Anh Tài', '2020-04-10 09:25:12', 'admin', '2020-04-14 13:42:03'),
(116, 1, '123546', NULL, 'BillNo 89889', NULL, 'An hTài', 'ZZZZ8888888', '2020-04-14 23:59:59', '1231231', 5, NULL, NULL, 'Không biết', NULL, NULL, '1', '0', '1', '2020-04-14 11:12:05', '0', NULL, NULL, 'Đã klàm lệnh', NULL, '2020-04-14 11:10:37', 'admin', '2020-04-14 11:12:05'),
(117, 1, '123546', NULL, 'BillNo 89889', NULL, 'An hTài', 'ZZZZ8888887', '2020-04-15 23:59:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '2020-04-14 11:12:05', '0', NULL, NULL, 'Đã klàm lệnh', NULL, '2020-04-14 11:11:54', 'admin', '2020-04-14 11:12:05'),
(118, 1, '123546', NULL, 'fkjadnfkldnà', NULL, 'dkjándkjn', 'uuuu8888888', '2020-04-14 23:59:59', 'sdfklsbdnf', 3, NULL, NULL, 'kjdsnfád', NULL, NULL, '1', '0', '1', '2020-04-14 11:19:02', '0', NULL, NULL, 'Ghi thím', NULL, '2020-04-14 11:13:22', 'admin', '2020-04-14 11:19:12'),
(119, 4, 'SITC1', NULL, '12345', NULL, 'Gỗ Quảng Nam', 'MSKU1111111', '2020-04-15 23:59:59', 'Tiên Sa', 3, NULL, NULL, 'ABC', NULL, NULL, '1', '0', '1', '2020-04-14 11:18:24', '0', NULL, NULL, 'Ghi thím', NULL, '2020-04-14 11:14:07', 'admin', '2020-04-14 11:18:24'),
(120, 4, 'SITC1', NULL, '12345', NULL, 'Gỗ Quảng Nam', 'MSKU1111112', '2020-04-15 23:59:59', 'Tiên Sa', 3, NULL, NULL, 'ABC', NULL, NULL, '1', '0', '1', '2020-04-14 11:18:24', '0', NULL, NULL, 'Ghi thím', NULL, '2020-04-14 11:14:07', 'admin', '2020-04-14 11:18:24'),
(121, 4, 'SITC1', NULL, '123456', NULL, 'Bia Huda', 'PONU1234567', '2020-04-29 23:59:59', 'Chân Thật', 3, NULL, NULL, 'XYZ', NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, NULL, '2020-04-14 11:29:34', 'Anh Dũng', '2020-04-15 21:29:26'),
(122, 3, 'SITC1', NULL, 'vsl<>/?@#', NULL, '1', 'ABCD1234567', '2020-04-14 23:59:59', '1', 0, NULL, NULL, 'vsl<>/?@#', 'vsl<>/?@#', NULL, '0', '0', '0', NULL, '0', NULL, 'vsl<>/?@#', NULL, NULL, '2020-04-14 12:37:02', NULL, NULL),
(123, 3, 'SITC1', NULL, '01', NULL, '1', 'abcd1111111', '2020-04-14 23:59:59', '1', 1, NULL, NULL, '1', '1', NULL, '1', '0', '1', '2020-04-14 13:08:44', '0', NULL, '1', '!@#$%^&*()', NULL, '2020-04-14 13:00:01', 'admin', '2020-04-14 13:05:47');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `gen_table`
--

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

--
-- Đang đổ dữ liệu cho bảng `gen_table`
--

INSERT INTO `gen_table` (`table_id`, `table_name`, `table_comment`, `class_name`, `tpl_category`, `package_name`, `module_name`, `business_name`, `function_name`, `function_author`, `options`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(2, 'sys_post', 'Job Information Sheet', 'SysPost', 'crud', 'vn.com.irtech.eport.system', 'system', 'post', 'Job Information Sheet', 'ruoyi', NULL, 'admin', '2020-03-30 00:54:19', '', NULL, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `gen_table_column`
--

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

--
-- Đang đổ dữ liệu cho bảng `gen_table_column`
--

INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
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

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `logistic_account`
--

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Logistic account';

--
-- Đang đổ dữ liệu cho bảng `logistic_account`
--

INSERT INTO `logistic_account` (`id`, `group_id`, `user_name`, `email`, `password`, `salt`, `full_name`, `status`, `del_flag`, `login_ip`, `login_date`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
(1, 1, 'mst123', 'vinconship@vincon.com', '5f56efbdf7c9ec57bdb67ffb41d8672e', 'ed8c3b', 'tester 1', '0', '0', '171.255.162.174', '2020-06-08 15:14:04', NULL, 'DNG', '2020-05-30 08:15:22', '', '2020-06-08 15:14:04'),
(2, 1, '0123456789', 'vinco@vinco.com', 'd7ace825281871c5a784469f4fa1eca2', '3ef513', 'Nguyen Nguyen', '0', '0', '192.168.1.76', '2020-06-09 11:18:21', NULL, 'DNG', '2020-05-30 08:15:45', '', '2020-06-09 11:18:21'),
(3, 1, 'nganle', 'nganle@gmail.com', 'e7817077cf50bd1f57f6a4374790ea92', '99773a', 'lê thị thúy ngân', '0', '0', '', NULL, NULL, 'DNG', '2020-06-09 11:50:35', '', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `logistic_group`
--

DROP TABLE IF EXISTS `logistic_group`;
CREATE TABLE IF NOT EXISTS `logistic_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `group_name` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Tên doanh nghiệp',
  `email_address` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Địa chỉ thư điện tử',
  `address` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Địa chỉ liên hệ',
  `mst` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Mã số thuế doanh nghiệp',
  `phone` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Điện thoại cố định',
  `mobile_phone` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Điện thoại di động',
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Logistic Group';

--
-- Đang đổ dữ liệu cho bảng `logistic_group`
--

INSERT INTO `logistic_group` (`id`, `group_name`, `email_address`, `address`, `mst`, `phone`, `mobile_phone`, `fax`, `del_flag`, `business_registration_certificate`, `date_of_issue_registration`, `place_of_issue_registration`, `authorized_representative`, `representative_position`, `following_authorization_form_no`, `sign_date`, `owned`, `identify_card_no`, `date_of_issue_identify`, `place_of_issue_identify`, `email`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
(1, 'VINCOSHIP', 'vinco@vinco.com', '40 Hoàng sa, Đà Nẵng', '0123456789', '023645066', '076 455 555', '123123123123', '0', 'Cty cổ phần VINCOSHIP', '2020-05-28 00:00:00', 'Đà Nẵng', 'Nguyễn Văn A', 'Giám đốc', '13', '2020-05-30 00:00:00', 'abc', '201734555', '2020-05-08 00:00:00', 'Đà Nẵng', 'vinco@vinco.com', '', '2020-05-29 20:24:54', '', NULL),
(2, 'dsfds', 'ds', 'đà nẵng', '1233431231321', '0981974393', '0156415631', '201561021', '1', 'sđ', '2020-06-10 00:00:00', 'dsfds', 'dsfds', 'dsfds', 'sdf', '2020-06-09 00:00:00', 'dsfd', '2123154651', '2020-06-11 00:00:00', 'dsfd', 'ngan123@gmail.com', '', '2020-06-09 11:32:21', '', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `otp_code`
--

DROP TABLE IF EXISTS `otp_code`;
CREATE TABLE IF NOT EXISTS `otp_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `shipment_detailIds` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Shipment detailIds',
  `phone_number` varchar(13) COLLATE utf8_bin NOT NULL COMMENT 'Phone number',
  `opt_code` int(11) NOT NULL COMMENT 'OTP CODE',
  `msg_status` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Status send mess',
  `verify_status` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Verify send mess',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='otp Code';

--
-- Đang đổ dữ liệu cho bảng `otp_code`
--

INSERT INTO `otp_code` (`id`, `shipment_detailIds`, `phone_number`, `opt_code`, `msg_status`, `verify_status`, `create_time`) VALUES
(1, '36,37', '076 455 555', 761436, NULL, NULL, '2020-06-08 14:20:04'),
(2, '1,2,3,4,5,6,7', '076 455 555', 371737, NULL, NULL, '2020-06-08 15:22:58'),
(3, '1,2,3,4,5,6,7', '076 455 555', 42115, NULL, NULL, '2020-06-08 15:23:53'),
(4, '1,2,3,4,5,6,7', '076 455 555', 372069, NULL, NULL, '2020-06-08 15:24:55');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_blob_triggers`
--

DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE IF NOT EXISTS `qrtz_blob_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `blob_data` blob DEFAULT NULL,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_calendars`
--

DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE IF NOT EXISTS `qrtz_calendars` (
  `sched_name` varchar(120) NOT NULL,
  `calendar_name` varchar(200) NOT NULL,
  `calendar` blob NOT NULL,
  PRIMARY KEY (`sched_name`,`calendar_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_cron_triggers`
--

DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE IF NOT EXISTS `qrtz_cron_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `cron_expression` varchar(200) NOT NULL,
  `time_zone_id` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_fired_triggers`
--

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

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_job_details`
--

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
  `job_data` blob DEFAULT NULL,
  PRIMARY KEY (`sched_name`,`job_name`,`job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_locks`
--

DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE IF NOT EXISTS `qrtz_locks` (
  `sched_name` varchar(120) NOT NULL,
  `lock_name` varchar(40) NOT NULL,
  PRIMARY KEY (`sched_name`,`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_paused_trigger_grps`
--

DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE IF NOT EXISTS `qrtz_paused_trigger_grps` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  PRIMARY KEY (`sched_name`,`trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_scheduler_state`
--

DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE IF NOT EXISTS `qrtz_scheduler_state` (
  `sched_name` varchar(120) NOT NULL,
  `instance_name` varchar(200) NOT NULL,
  `last_checkin_time` bigint(13) NOT NULL,
  `checkin_interval` bigint(13) NOT NULL,
  PRIMARY KEY (`sched_name`,`instance_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_simple_triggers`
--

DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE IF NOT EXISTS `qrtz_simple_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `repeat_count` bigint(7) NOT NULL,
  `repeat_interval` bigint(12) NOT NULL,
  `times_triggered` bigint(10) NOT NULL,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_simprop_triggers`
--

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
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_triggers`
--

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
  `job_data` blob DEFAULT NULL,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  KEY `sched_name` (`sched_name`,`job_name`,`job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `shipment`
--

DROP TABLE IF EXISTS `shipment`;
CREATE TABLE IF NOT EXISTS `shipment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `logistic_account_id` bigint(20) NOT NULL,
  `logistic_group_id` bigint(20) NOT NULL,
  `service_id` tinyint(4) NOT NULL COMMENT 'Dich Vu',
  `bl_no` varchar(20) COLLATE utf8_bin NOT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Shipment';

--
-- Đang đổ dữ liệu cho bảng `shipment`
--

INSERT INTO `shipment` (`id`, `logistic_account_id`, `logistic_group_id`, `service_id`, `bl_no`, `tax_code`, `container_amount`, `edo_flg`, `reference_no`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
(1, 1, 1, 1, 'SMJ2014SDA701', '123123123', 8, '0', NULL, 'demo lam lenh', 'tester 1', '2020-06-08 15:16:50', '', NULL),
(2, 1, 1, 1, 'SMJ2014SDA701', '123456', 8, '0', NULL, NULL, 'tester 1', '2020-06-08 15:46:47', '', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `shipment_detail`
--

DROP TABLE IF EXISTS `shipment_detail`;
CREATE TABLE IF NOT EXISTS `shipment_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `shipment_id` bigint(20) NOT NULL COMMENT 'Ma Lo',
  `logistic_group_id` bigint(20) NOT NULL,
  `register_no` varchar(6) COLLATE utf8_bin NOT NULL COMMENT 'Ma DK',
  `container_no` varchar(12) COLLATE utf8_bin NOT NULL COMMENT 'Container Number',
  `container_status` varchar(3) COLLATE utf8_bin DEFAULT NULL COMMENT 'Container Status (S,D)',
  `sztp` varchar(4) COLLATE utf8_bin NOT NULL COMMENT 'Size Type',
  `fe` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'FE',
  `booking_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'Booking Number',
  `bl_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'BL number',
  `seal_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'Seal Number',
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
  `moving_cont_amount` int(5) DEFAULT NULL,
  `transport_ids` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT 'Status',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Shipment Details';

--
-- Đang đổ dữ liệu cho bảng `shipment_detail`
--

INSERT INTO `shipment_detail` (`id`, `shipment_id`, `logistic_group_id`, `register_no`, `container_no`, `container_status`, `sztp`, `fe`, `booking_no`, `bl_no`, `seal_no`, `consignee`, `expired_dem`, `wgt`, `vsl_nm`, `voy_no`, `ope_code`, `loading_port`, `discharge_port`, `transport_type`, `empty_depot`, `vgm_chk`, `vgm`, `vgm_person_info`, `custom_declare_no`, `custom_status`, `payment_status`, `process_status`, `do_status`, `do_received_time`, `user_verify_status`, `preorder_pickup`, `moving_cont_amount`, `transport_ids`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
(1, 1, 1, '11', 'AMFU2005474', NULL, '22U0', 'F', NULL, 'SMJ2014SDA701', 'HLAG0150753', 'VINAL', '2020-06-15 00:00:00', 10200, 'HAIAN MIND', 'HAL0270', 'HAG', 'SGSIN', 'VNDAD', 'Vessel', NULL, NULL, NULL, NULL, NULL, 'R', 'N', 'Y', 'N', NULL, NULL, 'N', NULL, NULL, 2, NULL, 'tester 1', '2020-06-08 15:19:57', NULL, '2020-06-08 15:20:54'),
(2, 1, 1, '12', 'BSIU2261628', NULL, '22G0', 'F', NULL, 'SMJ2014SDA701', 'HLAG0150754', 'VINAL', '2020-06-15 00:00:00', 7200, 'HAIAN MIND', 'HAL0270', 'HAG', 'SGSIN', 'VNDAD', 'Vessel', NULL, NULL, NULL, NULL, NULL, 'R', 'N', 'Y', 'N', NULL, NULL, 'N', NULL, NULL, 2, NULL, 'tester 1', '2020-06-08 15:19:57', NULL, '2020-06-08 15:20:54'),
(3, 1, 1, '13', 'GATU1331949', NULL, '22G0', 'F', NULL, 'SMJ2014SDA701', 'HLAG0150787', 'VINAL', '2020-06-15 00:00:00', 5800, 'HAIAN MIND', 'HAL0270', 'HAG', 'SGSIN', 'VNDAD', 'Vessel', NULL, NULL, NULL, NULL, NULL, 'R', 'N', 'Y', 'N', NULL, NULL, 'Y', 2, NULL, 2, NULL, 'tester 1', '2020-06-08 08:19:57', NULL, '2020-06-08 15:22:04'),
(4, 1, 1, '14', 'GLDU5617640', NULL, '22G0', 'F', NULL, 'SMJ2014SDA701', 'HLAG0150788', 'VINAL', '2020-06-15 00:00:00', 5900, 'HAIAN MIND', 'HAL0270', 'HAG', 'SGSIN', 'VNDAD', 'Vessel', NULL, NULL, NULL, NULL, NULL, 'R', 'N', 'Y', 'N', NULL, NULL, 'N', NULL, NULL, 2, NULL, 'tester 1', '2020-06-08 15:19:57', NULL, '2020-06-08 15:20:54'),
(5, 1, 1, '15', 'HLXU3610040', NULL, '22U0', 'F', NULL, 'SMJ2014SDA701', 'HLAGO150766', 'VINAL', '2020-06-15 00:00:00', 10500, 'HAIAN MIND', 'HAL0270', 'HAG', 'SGSIN', 'VNDAD', 'Vessel', NULL, NULL, NULL, NULL, NULL, 'R', 'N', 'Y', 'N', NULL, NULL, 'N', NULL, NULL, 2, NULL, 'tester 1', '2020-06-08 15:19:58', NULL, '2020-06-08 15:20:54'),
(6, 1, 1, '16', 'HLXU3688347', NULL, '22P0', 'F', NULL, 'SMJ2014SDA701', 'YOK', 'VINAL', '2020-06-15 00:00:00', 31400, 'HAIAN MIND', 'HAL0270', 'HAG', 'SGSIN', 'VNDAD', 'Vessel', NULL, NULL, NULL, NULL, NULL, 'R', 'N', 'Y', 'N', NULL, NULL, 'N', NULL, NULL, 2, NULL, 'tester 1', '2020-06-08 15:19:58', NULL, '2020-06-08 15:20:54'),
(7, 1, 1, '17', 'TCKU3619957', NULL, '22G0', 'F', NULL, 'SMJ2014SDA701', 'HLAG0150760', 'VINAL', '2020-06-15 00:00:00', 6100, 'HAIAN MIND', 'HAL0270', 'HAG', 'SGSIN', 'VNDAD', 'Vessel', NULL, NULL, NULL, NULL, NULL, 'R', 'N', 'Y', 'N', NULL, NULL, 'N', NULL, NULL, 2, NULL, 'tester 1', '2020-06-08 15:19:58', NULL, '2020-06-08 15:20:54');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `shipment_transport`
--

DROP TABLE IF EXISTS `shipment_transport`;
CREATE TABLE IF NOT EXISTS `shipment_transport` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `logistic_group_id` bigint(20) NOT NULL,
  `shipment_id` bigint(20) NOT NULL,
  `container_no` varchar(12) COLLATE utf8_bin DEFAULT NULL,
  `transport_ids` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Thong tin dieu xe';

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_config`
--

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

--
-- Đang đổ dữ liệu cho bảng `sys_config`
--

INSERT INTO `sys_config` (`config_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(1, 'Main Frame Page', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(2, 'User Init password', 'sys.user.initPassword', '123456', 'Y', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(3, 'Main Frame Page-Sidebar Theme', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(4, 'Enable User Register', 'sys.account.registerUser', 'false', 'Y', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE IF NOT EXISTS `sys_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Department id',
  `parent_id` bigint(20) DEFAULT 0 COMMENT 'Parent department id',
  `ancestors` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT 'Ancestor list',
  `dept_name` varchar(30) COLLATE utf8_bin DEFAULT '' COMMENT 'Department name',
  `order_num` int(4) DEFAULT 0 COMMENT 'display order',
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

--
-- Đang đổ dữ liệu cho bảng `sys_dept`
--

INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `ancestors`, `dept_name`, `order_num`, `leader`, `phone`, `email`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
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

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_dict_data`
--

DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE IF NOT EXISTS `sys_dict_data` (
  `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(4) DEFAULT 0 COMMENT '字典排序',
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

--
-- Đang đổ dữ liệu cho bảng `sys_dict_data`
--

INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
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

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_dict_type`
--

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

--
-- Đang đổ dữ liệu cho bảng `sys_dict_type`
--

INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
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

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_job`
--

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

--
-- Đang đổ dữ liệu cho bảng `sys_job`
--

INSERT INTO `sys_job` (`job_id`, `job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_job_log`
--

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

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_logininfor`
--

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
) ENGINE=InnoDB AUTO_INCREMENT=525 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统访问记录';

--
-- Đang đổ dữ liệu cho bảng `sys_logininfor`
--

INSERT INTO `sys_logininfor` (`info_id`, `login_name`, `ipaddr`, `login_location`, `browser`, `os`, `status`, `msg`, `login_time`) VALUES
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
(177, 'admin', '192.168.1.16', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 10:29:15'),
(178, 'admin', '59.153.224.169', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-14 10:33:44'),
(179, 'admin', '59.153.224.169', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 10:33:50'),
(180, 'Carrier: huydp@irtech.com.vn', '59.153.224.169', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-14 10:36:15'),
(181, 'Carrier: huydp@irtech.com.vn', '59.153.224.169', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-14 10:36:22'),
(182, 'Carrier: huydp@irtech.com.vn', '59.153.224.169', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-14 10:37:14'),
(183, 'Carrier: admin', '59.153.224.169', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-14 10:37:25'),
(184, 'Carrier: lehanam@danangport.com', '59.153.224.169', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-14 10:37:36'),
(185, 'Carrier: tai@gmail.com', '59.153.224.169', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-04-14 10:38:08'),
(186, 'Carrier: huydp@irtech.com.vn', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-14 10:53:39'),
(187, 'Carrier: huydp@irtech.com.vn', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-14 10:53:44'),
(188, 'Carrier: huydp@irtech.com.vn', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-14 10:53:55'),
(189, 'Carrier: huydp@irtech.com.vn', '113.160.224.2', 'XX XX', 'Firefox 7', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-14 11:03:22'),
(190, 'Carrier: dunglv@danangport.com', '113.160.224.2', 'XX XX', 'Firefox 7', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-14 11:03:39'),
(191, 'dunglv@danangport', '113.160.224.2', 'XX XX', 'Firefox 7', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-14 11:05:03'),
(192, 'dunglv@danangport', '113.160.224.2', 'XX XX', 'Firefox 7', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-14 11:05:09'),
(193, 'admin', '113.160.224.2', 'XX XX', 'Firefox 7', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-04-14 11:05:14'),
(194, 'admin', '113.160.224.2', 'XX XX', 'Firefox 7', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 11:05:16'),
(195, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 11:05:16'),
(196, 'admin', '113.160.224.2', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-14 11:06:29'),
(197, 'admin', '113.160.224.2', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 11:06:34'),
(198, 'admin', '113.160.224.2', 'XX XX', 'Firefox 7', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-14 11:06:47'),
(199, 'Carrier: tai@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-04-14 11:09:53'),
(200, 'Carrier: tai@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 11:09:55'),
(201, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 11:10:56'),
(202, 'Carrier: dunglv@danangport.com', '113.160.224.2', 'XX XX', 'Firefox 7', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 11:11:06'),
(203, 'Carrier: tai@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-14 11:14:46'),
(204, 'Carrier: tai@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 11:14:49'),
(205, 'Carrier: tai@gmail.com', '171.255.163.95', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-04-14 12:14:25'),
(206, 'Carrier: tai@gmail.com', '171.255.163.95', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 2', '2020-04-14 12:14:28'),
(207, 'Carrier: 123456', '171.255.163.95', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-14 12:14:31'),
(208, 'Carrier: huydp@irtech.com.vn', '171.255.163.95', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 12:14:41'),
(209, 'Carrier: huydp@irtech.com.vn', '171.255.163.95', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-14 12:16:11'),
(210, 'admin', '171.255.163.95', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 12:16:18'),
(211, 'admin', '192.168.1.16', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 12:19:22'),
(212, 'admin', '192.168.1.16', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-14 12:20:12'),
(213, 'abc', '192.168.1.16', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 12:20:17'),
(214, 'abc', '192.168.1.16', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-14 12:20:20'),
(215, 'lehanam@danangport.com', '59.153.224.169', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-14 12:32:17'),
(216, 'admin', '59.153.224.169', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 12:32:29'),
(217, 'Carrier: lehanam@danangport.com', '59.153.224.169', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-14 12:33:12'),
(218, 'Carrier: lehanam@danangport.com', '59.153.224.169', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-14 12:33:29'),
(219, 'Carrier: huydp@irtech.com.vn', '59.153.224.169', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 12:33:36'),
(220, 'Carrier: huydp@irtech.com.vn', '59.153.224.169', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-14 12:49:23'),
(221, 'Carrier: lehanam@danangport.com', '59.153.224.169', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-14 12:49:37'),
(222, 'Carrier: lehanam@danangport.com', '59.153.224.169', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-14 12:52:04'),
(223, 'Carrier: lehanam@danangport.com', '59.153.224.169', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-14 12:52:06'),
(224, 'Carrier: huydp@irtech.com.vn', '59.153.224.169', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 12:53:16'),
(225, 'admin', '59.153.224.169', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 12:53:34'),
(226, 'Carrier: dunglv@danangport.com', '113.160.224.2', 'XX XX', 'Firefox 7', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-14 14:56:11'),
(227, 'admin', '192.168.1.11', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 08:24:27'),
(228, 'admin', '192.168.1.11', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-15 08:25:20'),
(229, 'nhanvien1', '192.168.1.11', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 08:25:25'),
(230, 'nhanvien1', '192.168.1.11', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-15 08:25:37'),
(231, 'admin', '192.168.1.11', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 08:25:46'),
(232, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 08:51:45'),
(233, 'Carrier: tai@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 08:55:49'),
(234, 'Carrier: tai@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-15 09:00:14'),
(235, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 09:00:48'),
(236, 'Carrier: tai@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 09:01:40'),
(237, 'Carrier: tai@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-15 09:03:02'),
(238, 'Carrier: huydp@irtech.com.vn', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 09:03:11'),
(239, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 09:03:58'),
(240, 'admin', '192.168.1.11', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 09:04:29'),
(241, 'admin', '192.168.1.11', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-15 09:06:11'),
(242, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 09:14:48'),
(243, 'admin', '192.168.1.11', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 10:02:45'),
(244, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-04-15 10:08:02'),
(245, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 10:08:08'),
(246, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 10:09:26'),
(247, 'Carrier: admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-15 13:52:34'),
(248, 'Carrier: admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-15 13:52:40'),
(249, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 13:52:54'),
(250, 'Carrier: ry@163.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-15 14:35:13'),
(251, 'Carrier: tai@Gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-15 14:35:29'),
(252, 'Carrier: tai@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 14:35:37'),
(253, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 14:38:31'),
(254, 'Carrier: tai@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-15 14:42:18'),
(255, 'Carrier: han@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 14:42:34'),
(256, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 14:43:14'),
(257, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-15 15:12:44'),
(258, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 15:12:55'),
(259, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-15 15:13:05'),
(260, 'ry', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-04-15 15:13:10'),
(261, 'ry', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 2', '2020-04-15 15:13:15'),
(262, 'ry', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 3', '2020-04-15 15:13:20'),
(263, 'ry', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 15:13:24'),
(264, 'ry', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-15 15:13:34'),
(265, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 15:13:37'),
(266, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-15 15:14:04'),
(267, 'ry', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 15:14:08'),
(268, 'Carrier: han@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 15:34:57'),
(269, 'ry', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-15 15:35:43'),
(270, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 15:35:46'),
(271, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-15 15:38:30'),
(272, 'abc', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 15:38:35'),
(273, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 15:39:05'),
(274, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-15 15:39:35'),
(275, 'abc', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 15:39:39'),
(276, 'abc', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-15 15:40:03'),
(277, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-04-15 15:40:07'),
(278, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 15:40:10'),
(279, 'Carrier: han@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 15:44:58'),
(280, 'abc', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-15 15:45:22'),
(281, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 15:45:34'),
(282, 'Carrier: han@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-15 15:54:11'),
(283, 'Carrier: tai@gmail.com', '192.168.1.11', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-22 13:37:09'),
(284, 'admin', '192.168.1.11', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-22 13:38:04'),
(285, 'Carrier: admin@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-23 18:42:54'),
(286, 'Carrier: admin@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-23 18:42:57'),
(287, 'Carrier: tai@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-23 18:43:04'),
(288, 'Carrier: tai@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-23 19:32:11'),
(289, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-23 19:47:07'),
(290, 'Carrier: tai@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-23 19:47:11'),
(291, 'tai@gmail.com', '59.153.233.173', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-23 22:07:55'),
(292, 'tai@gmail.com', '59.153.233.173', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-23 22:08:03'),
(293, 'huydp@irtech.com.vn', '59.153.233.173', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-23 22:08:18'),
(294, 'Carrier: huydp@irtech.com.vn', '59.153.233.173', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-23 22:09:05'),
(295, 'Carrier: tai@gmail.com', '171.254.133.56', 'XX XX', 'Chrome Mobile', 'Mac OS X (iPhone)', '0', 'Đăng nhập thành công', '2020-04-23 22:09:20'),
(296, 'Carrier: tai@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 10:03:30'),
(297, 'Carrier: tai@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 10:08:00'),
(298, 'Carrier: minhtc@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-04-24 13:46:35'),
(299, 'Carrier: minhtc@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 2', '2020-04-24 13:46:39'),
(300, 'Carrier: minhtc@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 3', '2020-04-24 13:46:46'),
(301, 'Carrier: minhtc@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 4', '2020-04-24 13:46:56'),
(302, 'Carrier: minhtc@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 5', '2020-04-24 13:47:00'),
(303, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 13:47:54'),
(304, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 13:48:41'),
(305, 'Carrier: huydp@irtech.com.vn', '113.160.224.2', 'XX XX', 'Microsoft Edge', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-24 13:50:56'),
(306, 'Carrier: minhtc@gmail.com', '113.160.224.2', 'XX XX', 'Microsoft Edge', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 13:51:22'),
(307, 'nqat2003@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-24 14:05:06'),
(308, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 14:27:31'),
(309, 'Carrier: tai@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-24 14:35:26'),
(310, 'Carrier: tai@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-24 14:35:31'),
(311, 'Carrier: tai@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-24 14:35:34'),
(312, 'Carrier: admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-24 14:35:39'),
(313, 'Carrier: mintc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-24 14:36:01'),
(314, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 14:36:04'),
(315, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-04-24 14:41:39'),
(316, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 14:41:46'),
(317, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-24 14:45:41'),
(318, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 14:47:46'),
(319, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 14:51:39'),
(320, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 14:52:48'),
(321, 'Carrier: huydp@irtech.com.vn', '113.160.225.15', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 15:19:28'),
(322, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-04-24 16:22:08'),
(323, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 16:22:11'),
(324, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 16:41:08'),
(325, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 16:42:09'),
(326, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 16:43:33'),
(327, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 16:45:38'),
(328, 'Carrier: minhtc@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 16:46:22'),
(329, 'Carrier: huydp@irtech.com.vn', '113.160.225.15', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 17:02:17'),
(330, 'Carrier: huydp@irtech.com.vn', '113.160.225.15', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 18:18:33'),
(331, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 18:24:33'),
(332, 'Carrier: huydp@irtech.com.vn', '113.160.225.15', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 18:25:34'),
(333, 'Carrier: trunghieu@danangport.com', '59.153.224.154', 'XX XX', 'Apple WebKit', 'Mac OS X (iPhone)', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-24 19:04:42'),
(334, 'Carrier: trunghieu@danangport.com', '59.153.224.154', 'XX XX', 'Apple WebKit', 'Mac OS X (iPhone)', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-24 19:04:57'),
(335, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-04-24 19:46:42'),
(336, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 2', '2020-04-24 19:46:45'),
(337, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-24 19:46:49'),
(338, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-04-25 08:41:18'),
(339, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-25 08:41:23'),
(340, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-25 12:58:21'),
(341, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-04-25 13:50:05'),
(342, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-25 13:50:08'),
(343, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-25 16:14:48'),
(344, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-04-25 16:17:26'),
(345, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-25 16:17:30'),
(346, 'Carrier: huydp@irtech.com.vn', '113.160.225.15', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-25 16:27:55'),
(347, 'Carrier: minhtc@gmail.com', '123.19.56.179', 'XX XX', 'Apple WebKit', 'Mac OS X (iPhone)', '0', 'Đăng nhập thành công', '2020-04-25 16:33:41'),
(348, 'Carrier: minhtc@gmail.com', '14.167.73.192', 'XX XX', 'Chrome Mobile', 'Android Mobile', '0', 'Đăng nhập thành công', '2020-04-25 16:36:25'),
(349, 'Carrier: minhtc@gmail.com', '117.2.164.66', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-26 12:42:39'),
(350, 'Carrier: minhtc@gmail,com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-27 08:14:30'),
(351, 'Carrier: minhtc@gmail,com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-27 08:14:34'),
(352, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-04-27 14:23:35'),
(353, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 14:23:38'),
(354, 'Carrier: admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-27 18:40:04'),
(355, 'Carrier: admin@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-27 18:40:11'),
(356, 'Carrier: minhtc@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-04-27 18:40:18'),
(357, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 18:40:25'),
(358, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 18:42:54'),
(359, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 18:47:30'),
(360, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 18:48:52'),
(361, 'admin@admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-27 18:57:16'),
(362, 'admin@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-27 18:57:20'),
(363, 'admin@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-27 18:57:25'),
(364, 'tai@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-27 18:57:33'),
(365, 'abc', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-04-27 18:57:55'),
(366, 'nhanvien1', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-04-27 18:58:07'),
(367, 'hieunt@irtech.com.vn', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-27 18:58:09'),
(368, 'nhanvien1', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 2', '2020-04-27 18:58:13'),
(369, 'nhanvien1', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 3', '2020-04-27 18:58:16'),
(370, 'nhanvien1', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 4', '2020-04-27 18:58:20'),
(371, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 18:58:35'),
(372, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 18:59:51'),
(373, 'Carrier: huydp@irtech.com.vn', '113.160.225.15', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 19:24:35'),
(374, 'minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-27 19:58:15'),
(375, 'minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-27 19:58:18'),
(376, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-27 19:58:29'),
(377, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-28 10:11:13'),
(378, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-28 10:55:06'),
(379, 'Carrier: tai@gmail.com', '42.118.93.19', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-28 13:03:13'),
(380, 'Carrier: tai@gmail.com', '42.118.93.19', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-28 13:03:20'),
(381, 'admin', '42.118.93.19', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-04-28 13:03:30'),
(382, 'admin', '42.118.93.19', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-28 13:03:32'),
(383, 'admin', '42.118.93.19', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-28 13:16:56'),
(384, 'Carrier: huydp@irtech.com.vn', '42.118.93.19', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-28 13:17:07'),
(385, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-28 13:34:36'),
(386, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-28 13:36:02'),
(387, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-28 13:38:44'),
(388, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-28 14:27:29'),
(389, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-28 14:27:36'),
(390, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-28 14:29:18'),
(391, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-28 15:13:25'),
(392, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-29 10:15:52'),
(393, 'Carrier: mnhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-29 10:16:06'),
(394, 'Carrier: mnhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-29 10:16:09'),
(395, 'Carrier: mnhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-29 10:16:12'),
(396, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-29 10:16:16'),
(397, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-04-29 14:16:24'),
(398, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-04-29 14:17:23'),
(399, 'Admin', '171.255.138.210', 'XX XX', 'Chrome Mobile', 'Mac OS X (iPhone)', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-29 17:22:39'),
(400, 'admin', '171.255.138.210', 'XX XX', 'Chrome Mobile', 'Mac OS X (iPhone)', '0', 'Đăng nhập thành công', '2020-04-29 17:22:52'),
(401, 'admin@admin.com', '171.255.138.210', 'XX XX', 'Chrome Mobile', 'Mac OS X (iPhone)', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-29 18:35:34'),
(402, 'admin@admin.com', '171.255.138.210', 'XX XX', 'Chrome Mobile', 'Mac OS X (iPhone)', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-29 18:35:35'),
(403, 'admin@admin.com', '171.255.138.210', 'XX XX', 'Chrome Mobile', 'Mac OS X (iPhone)', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-04-29 18:35:41'),
(404, 'admin', '171.255.138.210', 'XX XX', 'Chrome Mobile', 'Mac OS X (iPhone)', '0', 'Đăng nhập thành công', '2020-04-29 18:35:45'),
(405, 'admin', '171.227.17.69', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-04 05:27:10'),
(406, 'Carrier: tai@gmail.com', '171.227.17.69', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-04 05:27:34'),
(407, 'Carrier: minhtc@gmail.com', '171.227.17.69', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-04 05:27:43'),
(408, 'Carrier: minhtc@gmail.com', '171.227.17.69', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-05-04 05:29:01'),
(409, 'Carrier: minhtc@gmail.com', '116.110.245.48', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-04 05:29:07'),
(410, 'admin', '116.110.245.48', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-04 05:29:28'),
(411, 'Carrier: minhtc@gmail.com', '116.110.245.48', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-05-04 05:33:17'),
(412, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-04 09:47:40'),
(413, 'Carrier: minhtc@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-07 11:13:38'),
(414, 'Carrier: tai@gmail.com', '171.252.131.58', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-11 16:46:34'),
(415, 'Carrier: minhtc@gmail.com', '171.252.131.58', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-11 16:46:44'),
(416, 'admin', '171.252.131.58', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-11 16:46:51'),
(417, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-23 08:19:07'),
(418, 'Carrier: ope@danangport.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-23 08:20:55'),
(419, 'Carrier: admin', '192.168.1.2', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-23 08:22:53'),
(420, 'admin', '192.168.1.2', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-23 08:23:06'),
(421, 'Carrier: mst@dnp.com', '192.168.1.2', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-23 08:57:55'),
(422, 'Carrier: mst@dnp.com', '192.168.1.2', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-23 11:42:22'),
(423, 'admin', '171.227.17.69', 'XX XX', 'Chrome Mobile', 'Android Mobile', '0', 'Đăng nhập thành công', '2020-05-25 21:28:55'),
(424, 'Carrier: admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-26 10:43:51'),
(425, 'Carrier: admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-26 10:43:54'),
(426, 'Carrier: admin@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-26 10:43:59'),
(427, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-05-26 10:58:49'),
(428, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-26 10:58:52'),
(429, 'mst@dnp.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-26 11:01:28'),
(430, 'Carrier: mst@dnp.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-26 11:01:41'),
(431, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 10:40:52'),
(432, 'Carrier: admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-27 13:19:13'),
(433, 'Carrier: admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-27 13:19:16'),
(434, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 13:19:27'),
(435, 'Carrier: admin@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-27 14:14:24'),
(436, 'Carrier: admin@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-27 14:14:27'),
(437, 'Carrier: admin@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-27 14:15:23'),
(438, 'Carrier: mst@dnp.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 14:15:44'),
(439, 'Carrier: mst@dnp.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-05-27 14:16:09'),
(440, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 14:16:21'),
(441, 'Carrier: minhtc@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-27 14:16:32'),
(442, 'Carrier: minhtc@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-27 14:16:45'),
(443, 'Carrier: minhtc@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-27 14:16:53'),
(444, 'Carrier: minhtc@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-27 14:17:24'),
(445, 'Carrier: minhtc@admin.com', '192.168.1.4', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 14:25:55'),
(446, 'Carrier: mst@dnp.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 14:38:42'),
(447, 'Carrier: mst@dnp.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-27 15:20:46'),
(448, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-28 15:01:22'),
(449, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-28 15:55:56'),
(450, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 15:23:50'),
(451, 'Carrier: mst@dnp.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 16:58:59'),
(452, 'Carrier: admin', '192.168.1.4', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-29 17:00:56'),
(453, 'Carrier: admin', '192.168.1.4', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-29 17:01:00'),
(454, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 17:01:15'),
(455, 'Carrier: minhtc@admin.com', '192.168.1.4', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 17:01:26'),
(456, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 17:08:36'),
(457, 'admin', '171.227.17.4', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 19:17:16'),
(458, 'admin', '171.252.130.143', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 19:45:52'),
(459, 'admin', '116.110.102.253', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 20:09:34'),
(460, 'admin', '171.252.129.197', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-29 20:28:46'),
(461, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-30 08:03:22'),
(462, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-30 08:14:05'),
(463, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-30 08:14:45'),
(464, 'Carrier: mst123', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-30 08:15:32'),
(465, 'Carrier: 0123456789', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-30 08:16:21');
INSERT INTO `sys_logininfor` (`info_id`, `login_name`, `ipaddr`, `login_location`, `browser`, `os`, `status`, `msg`, `login_time`) VALUES
(466, 'Carrier: 0123456789', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-30 08:17:00'),
(467, 'Carrier: mst123', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-30 08:17:25'),
(468, 'Carrier: mst123', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-30 08:58:50'),
(469, 'Carrier: mst123', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-30 08:59:17'),
(470, 'Carrier: admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-30 09:25:12'),
(471, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-30 09:25:20'),
(472, 'Carrier: mst123', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-30 09:27:15'),
(473, 'Carrier: admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-30 11:32:12'),
(474, 'Carrier: mst123', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-30 11:32:21'),
(475, 'Carrier: mst123123', '117.2.142.16', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-05-30 14:32:46'),
(476, 'Carrier: mst123', '117.2.142.16', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-05-30 14:32:58'),
(477, 'Admin', '171.255.172.231', 'XX XX', 'Chrome Mobile', 'Mac OS X (iPhone)', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-02 12:33:08'),
(478, 'admin', '171.255.172.231', 'XX XX', 'Chrome Mobile', 'Mac OS X (iPhone)', '0', 'Đăng nhập thành công', '2020-06-02 12:33:12'),
(479, 'Carrier: mst123', '171.255.172.231', 'XX XX', 'Chrome Mobile', 'Mac OS X (iPhone)', '0', 'Đăng nhập thành công', '2020-06-02 12:34:38'),
(480, 'Carrier: mst123', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-04 17:16:28'),
(481, 'Carrier: mst123', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-06 10:04:18'),
(482, 'Carrier: admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-08 10:33:38'),
(483, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-06-08 10:33:49'),
(484, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 2', '2020-06-08 10:33:52'),
(485, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 10:33:57'),
(486, 'Carrier: mst123', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 10:47:33'),
(487, 'admin', '192.168.1.94', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 14:05:02'),
(488, 'admin', '192.168.1.66', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 14:05:14'),
(489, 'Carrier: 0123456789', '192.168.1.94', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 14:05:31'),
(490, 'admin', '192.168.1.66', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-06-08 14:05:47'),
(491, 'Carrier: 0123456789', '192.168.1.66', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 14:06:02'),
(492, 'Carrier: 0123456789', '192.168.1.66', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 14:11:34'),
(493, 'Carrier: admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-08 14:12:01'),
(494, 'Carrier: admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-08 14:12:07'),
(495, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 14:12:16'),
(496, 'admin', '192.168.1.68', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 14:15:28'),
(497, 'Carrier: 0123456789', '192.168.1.68', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 14:15:42'),
(498, 'Carrier: mst123', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 14:20:42'),
(499, 'Carrier: 0123456789', '192.168.1.66', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 14:28:19'),
(500, 'Carrier: 0123456789', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 14:29:32'),
(501, 'Carrier: 0123456789', '59.153.233.46', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 14:48:59'),
(502, 'admin', '171.255.162.174', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 15:10:10'),
(503, 'admin', '171.255.162.174', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-06-08 15:10:26'),
(504, 'admin', '171.255.162.174', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 15:10:31'),
(505, 'Carrier: mst123', '171.255.162.174', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 15:14:04'),
(506, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-08 19:03:32'),
(507, 'Carrier: admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-09 09:02:07'),
(508, 'Carrier: minh@gmail.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-09 09:02:17'),
(509, 'admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-09 09:02:33'),
(510, 'Carrier: minhtc@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-09 09:02:55'),
(511, 'Carrier: minhtc@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-09 09:02:57'),
(512, 'Carrier: minhtc@admin.com', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-09 09:03:13'),
(513, 'Carrier: admin', '192.168.1.68', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-09 09:13:42'),
(514, 'Carrier: admin', '113.176.195.221', 'XX XX', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-09 09:13:53'),
(515, 'Carrier: minhtc@admin.com', '192.168.1.68', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-09 09:14:09'),
(516, 'Carrier: admin', '192.168.1.76', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-09 11:09:01'),
(517, 'Carrier: admin', '192.168.1.76', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-09 11:09:53'),
(518, '0123456789', '192.168.1.76', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-09 11:10:48'),
(519, 'Carrier: admin', '192.168.1.76', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-09 11:11:14'),
(520, 'admin', '192.168.1.76', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-09 11:17:54'),
(521, 'Carrier: 123456789', '192.168.1.76', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-09 11:18:08'),
(522, 'Carrier: 0123456789', '192.168.1.76', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-09 11:18:21'),
(523, 'admin', '192.168.1.71', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '1', 'Nhập sai mật khẩu lần 1', '2020-06-09 13:33:09'),
(524, 'Carrier: minhtc@gmail.com', '192.168.1.68', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-09 13:42:37');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE IF NOT EXISTS `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Menu ID',
  `menu_name` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Menu Name',
  `parent_id` bigint(20) DEFAULT 0 COMMENT 'Parent ID',
  `order_num` int(4) DEFAULT 0 COMMENT 'Display Order',
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
) ENGINE=InnoDB AUTO_INCREMENT=2023 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Menu permission';

--
-- Đang đổ dữ liệu cho bảng `sys_menu`
--

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(1, 'Quản Lý Hệ Thống', 0, 1, '#', '', 'M', '0', '', 'fa fa-gear', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'System Management'),
(2, 'Theo Dõi Hệ Thống', 0, 2, '#', 'menuItem', 'M', '1', '', 'fa fa-video-camera', 'admin', '2018-03-16 11:33:00', 'admin', '2020-06-08 14:15:28', 'System Monitoring'),
(3, 'Quản Lý Hãng Tàu', 0, 3, '#', '', 'M', '0', '', 'fa fa-bars', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'System Tools目录'),
(100, 'Quản lý người dùng', 1, 1, '/system/user', '', 'C', '0', 'system:user:view', 'fa fa-address-book-o', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'User Management'),
(101, 'Quản lý vai trò', 1, 2, '/system/role', '', 'C', '0', 'system:role:view', 'fa fa-users', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'Role Management'),
(102, 'Danh mục', 1, 3, '/system/menu', '', 'C', '0', 'system:menu:view', 'fa fa-bars', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(103, 'Phòng Ban', 1, 4, '/system/dept', 'menuItem', 'C', '0', 'system:dept:view', '#', 'admin', '2018-03-16 11:33:00', 'admin', '2020-06-08 14:16:57', ''),
(104, 'Post', 1, 5, '/system/post', '', 'C', '1', 'system:post:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(105, 'Dictionary', 1, 6, '/system/dict', '', 'C', '1', 'system:dict:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(106, 'Cấu Hình Hệ Thống', 1, 7, '/system/config', 'menuItem', 'C', '0', 'system:config:view', '#', 'admin', '2018-03-16 11:33:00', 'admin', '2020-06-08 14:17:32', ''),
(107, 'Notification', 1, 8, '/system/notice', 'menuItem', 'C', '0', 'system:notice:view', '#', 'admin', '2018-03-16 11:33:00', 'admin', '2020-06-08 14:13:16', ''),
(108, 'Lịch sử', 1, 9, '#', '', 'M', '0', '', 'fa fa-hourglass-1', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(109, 'Online User', 2, 1, '/monitor/online', 'menuItem', 'C', '0', 'monitor:online:view', '#', 'admin', '2018-03-16 11:33:00', 'admin', '2020-06-08 14:14:57', ''),
(110, 'Job', 2, 2, '/monitor/job', 'menuItem', 'C', '0', 'monitor:job:view', '#', 'admin', '2018-03-16 11:33:00', 'admin', '2020-06-08 14:15:06', ''),
(111, 'Data Monitor', 2, 3, '/monitor/data', 'menuItem', 'C', '1', 'monitor:data:view', '#', 'admin', '2018-03-16 11:33:00', 'admin', '2020-06-08 14:14:47', ''),
(112, 'Server Monitor', 2, 3, '/monitor/server', '', 'C', '1', 'monitor:server:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(113, 'Form Building', 3, 1, '/tool/build', '', 'C', '1', 'tool:build:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(114, 'Code Gen', 3, 2, '/tool/gen', '', 'C', '1', 'tool:gen:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
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
(2012, 'Danh Sách Vận Đơn', 2021, 1, '/carrier/admin/do/getViewDo', 'menuItem', 'C', '0', 'carrier:admin:do:getViewDo:view', 'fa fa-file-excel-o', 'admin', '2018-03-01 00:00:00', 'admin', '2020-06-08 14:19:37', 'Exchange Delivery Order Menu'),
(2013, 'Delivery Order List', 2012, 1, '#', '', 'F', '0', 'equipment:do:list', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2014, 'Add DO', 2012, 2, '#', '', 'F', '0', 'equipment:do:add', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2015, 'Edit DO', 2012, 3, '#', '', 'F', '0', 'equipment:do:edit', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2016, 'Delete DO', 2012, 4, '#', '', 'F', '0', 'equipment:do:remove', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2017, 'Export DO', 2012, 5, '#', '', 'F', '0', 'equipment:do:export', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2018, 'Quản lý Logistic', 0, 4, '#', 'menuItem', 'M', '0', NULL, 'fa fa-bars', 'admin', '2020-05-14 17:56:22', '', NULL, ''),
(2019, 'Nhóm Logistic', 2018, 1, '/logistic/group', 'menuItem', 'C', '0', 'logistic:group:view', 'fa fa-truck', 'admin', '2020-05-14 18:03:26', '', NULL, ''),
(2020, 'Tài khoản Logistic', 2018, 1, '/logistic/account', 'menuItem', 'C', '0', 'logistic:account:view', 'fa fa-address-book-o', 'admin', '2020-05-14 18:04:20', 'admin', '2020-05-14 18:24:13', ''),
(2021, 'Quản Lý Vận Đơn', 0, 4, '#', 'menuItem', 'M', '0', NULL, 'fa fa-address-book-o', 'admin', '2020-06-08 14:19:16', '', NULL, ''),
(2022, 'Kế Hoạch Bãi Cảng', 0, 5, '#', 'menuItem', 'M', '0', NULL, 'fa fa-anchor', 'admin', '2020-06-08 14:20:16', '', NULL, '');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_notice`
--

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

--
-- Đang đổ dữ liệu cho bảng `sys_notice`
--

INSERT INTO `sys_notice` (`notice_id`, `notice_title`, `notice_type`, `notice_content`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(1, 'Warm reminder: 2018-07-01 DNG new version released', '2', 'New version content', '0', 'admin', '2018-03-16 11:33:00', 'admin', '2020-03-28 14:23:04', ''),
(2, 'Maintenance notice: 2018-07-01 Early morning maint', '1', 'Maintenance content', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_oper_log`
--

DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE IF NOT EXISTS `sys_oper_log` (
  `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Log PK',
  `title` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT 'Module title',
  `business_type` int(2) DEFAULT 0 COMMENT 'Business type (0 other 1 new 2 modified 3 deleted)',
  `method` varchar(100) COLLATE utf8_bin DEFAULT '' COMMENT 'Method name',
  `request_method` varchar(10) COLLATE utf8_bin DEFAULT '' COMMENT 'Request method',
  `operator_type` int(1) DEFAULT 0 COMMENT 'Operation category (0 others 1 background user 2 mobile phone user)',
  `oper_name` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT 'operator',
  `dept_name` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT 'Department name',
  `oper_url` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT 'Request URL',
  `oper_ip` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT 'Host address',
  `oper_location` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT 'Operating place',
  `oper_param` varchar(2000) COLLATE utf8_bin DEFAULT '' COMMENT 'Request parameter',
  `json_result` varchar(2000) COLLATE utf8_bin DEFAULT '' COMMENT 'Return parameter',
  `status` int(1) DEFAULT 0 COMMENT 'Operation status (0 normal 1 abnormal)',
  `error_msg` varchar(2000) COLLATE utf8_bin DEFAULT '' COMMENT 'Error message',
  `oper_time` datetime DEFAULT NULL COMMENT 'Operating time',
  PRIMARY KEY (`oper_id`)
) ENGINE=InnoDB AUTO_INCREMENT=202 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Operational logging';

--
-- Đang đổ dữ liệu cho bảng `sys_oper_log`
--

INSERT INTO `sys_oper_log` (`oper_id`, `title`, `business_type`, `method`, `request_method`, `operator_type`, `oper_name`, `dept_name`, `oper_url`, `oper_ip`, `oper_location`, `oper_param`, `json_result`, `status`, `error_msg`, `oper_time`) VALUES
(109, 'Carrier Account', 1, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"groupId\" : [ \"1\" ],\r\n  \"carrierCode\" : [ \"123546\" ],\r\n  \"email\" : [ \"tai@gmail.com\" ],\r\n  \"password\" : [ \"admin123\" ],\r\n  \"fullName\" : [ \"Anh Tài\" ]\r\n}', '{\r\n  \"msg\" : \"Success\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-07 11:18:42'),
(110, 'Carrier Account', 1, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"groupId\" : [ \"1\" ],\r\n  \"carrierCode\" : [ \"dnkasbdk\" ],\r\n  \"email\" : [ \"nqat2003@gmail.com\" ],\r\n  \"password\" : [ \"123qwe123\" ],\r\n  \"fullName\" : [ \"Anh Taif\" ]\r\n}', '{\r\n  \"msg\" : \"Success\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-08 10:20:34'),
(111, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/carrier/account/resetPwd/2', '127.0.0.1', 'Intranet IP', '{ }', '\"carrier/account/resetPwd\"', 0, NULL, '2020-04-08 10:20:41'),
(112, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwdSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/resetPwd', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"2\" ],\r\n  \"email\" : [ \"nqat2003@gmail.com\" ],\r\n  \"password\" : [ \"123456\" ]\r\n}', '{\r\n  \"msg\" : \"Success\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-08 10:21:33'),
(113, 'Carrier Account', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.changeStatus()', 'POST', 1, 'admin', 'R&D', '/carrier/account/changeStatus', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"status\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Success\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-08 10:22:41'),
(114, 'Carrier Account', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.changeStatus()', 'POST', 1, 'admin', 'R&D', '/carrier/account/changeStatus', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"status\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Success\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-08 10:22:48'),
(115, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/carrier/account/resetPwd/1', '127.0.0.1', 'Intranet IP', '{ }', '\"carrier/account/resetPwd\"', 0, NULL, '2020-04-10 08:45:29'),
(116, 'Carrier Group', 1, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/add', '113.176.195.221', 'XX XX', '{\r\n  \"groupCode\" : [ \"SITC\" ],\r\n  \"groupName\" : [ \"SITC\" ],\r\n  \"operateCode\" : [ \"SITC1, SITC2, SITC3\" ],\r\n  \"mainEmail\" : [ \"\" ]\r\n}', '{\r\n  \"msg\" : \"Invalid Email!\",\r\n  \"code\" : 500\r\n}', 0, NULL, '2020-04-14 11:05:44'),
(117, 'Carrier Group', 1, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/add', '113.176.195.221', 'XX XX', '{\r\n  \"groupCode\" : [ \"SITC\" ],\r\n  \"groupName\" : [ \"SITC\" ],\r\n  \"operateCode\" : [ \"SITC1, SITC2, SITC3\" ],\r\n  \"mainEmail\" : [ \"sitc@abc.com\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-14 11:05:54'),
(118, 'Carrier Account', 1, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/add', '113.176.195.221', 'XX XX', '{\r\n  \"groupId\" : [ \"2\" ],\r\n  \"carrierCode\" : [ \"SITC1, SITC3\" ],\r\n  \"email\" : [ \"huydp@irtech.com.vn\" ],\r\n  \"password\" : [ \"123456\" ],\r\n  \"fullName\" : [ \"Huy Do\" ],\r\n  \"status\" : [ \"0\" ],\r\n  \"isSendEmail\" : [ \"on\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-14 11:06:16'),
(119, 'Carrier Account', 1, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/add', '113.176.195.221', 'XX XX', '{\r\n  \"groupId\" : [ \"2\" ],\r\n  \"carrierCode\" : [ \"SITC1, SITC3\" ],\r\n  \"email\" : [ \"dunglv@danangport.com\" ],\r\n  \"password\" : [ \"123456\" ],\r\n  \"fullName\" : [ \"Anh Dũng\" ],\r\n  \"status\" : [ \"0\" ],\r\n  \"isSendEmail\" : [ \"on\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-14 11:07:03'),
(120, 'User Management', 1, 'vn.com.irtech.eport.web.controller.system.SysUserController.addSave()', 'POST', 1, 'admin', 'R&D', '/system/user/add', '192.168.1.16', 'Intranet IP', '{\r\n  \"deptId\" : [ \"103\" ],\r\n  \"userName\" : [ \"Nguyen v\" ],\r\n  \"deptName\" : [ \"R&D\" ],\r\n  \"phonenumber\" : [ \"09034567891\" ],\r\n  \"email\" : [ \"abc@gmail.com\" ],\r\n  \"loginName\" : [ \"abc\" ],\r\n  \"password\" : [ \"123456\" ],\r\n  \"sex\" : [ \"0\" ],\r\n  \"role\" : [ \"1\" ],\r\n  \"remark\" : [ \"\" ],\r\n  \"status\" : [ \"0\" ],\r\n  \"roleIds\" : [ \"1\" ],\r\n  \"postIds\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-14 12:20:08'),
(121, 'Carrier Group', 1, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/add', '59.153.224.169', 'XX XX', '{\r\n  \"groupCode\" : [ \"CMA\" ],\r\n  \"groupName\" : [ \"CMA\" ],\r\n  \"operateCode\" : [ \"CNC, APL, ANL\" ],\r\n  \"mainEmail\" : [ \"lehanm21790@gmail.com\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-14 12:41:43'),
(122, 'Carrier Account', 1, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/add', '59.153.224.169', 'XX XX', '{\r\n  \"groupId\" : [ \"3\" ],\r\n  \"carrierCode\" : [ \"CNC\" ],\r\n  \"email\" : [ \"lehanam21790@gmail.com\" ],\r\n  \"password\" : [ \"123456\" ],\r\n  \"fullName\" : [ \"nam\" ],\r\n  \"status\" : [ \"0\" ],\r\n  \"isSendEmail\" : [ \"on\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-14 12:46:53'),
(123, 'Carrier Account', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/edit', '59.153.224.169', 'XX XX', '{\r\n  \"id\" : [ \"5\" ],\r\n  \"groupId\" : [ \"3\" ],\r\n  \"carrierCode\" : [ \"CNC\" ],\r\n  \"email\" : [ \"huydp@irtech.com.vn\" ],\r\n  \"fullName\" : [ \"nam\" ],\r\n  \"status\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-14 12:53:56'),
(124, 'User Management', 1, 'vn.com.irtech.eport.web.controller.system.SysUserController.addSave()', 'POST', 1, 'admin', 'R&D', '/system/user/add', '192.168.1.11', 'Intranet IP', '{\r\n  \"deptId\" : [ \"104\" ],\r\n  \"userName\" : [ \"nhanvien1\" ],\r\n  \"deptName\" : [ \"Marketing\" ],\r\n  \"phonenumber\" : [ \"09234234233\" ],\r\n  \"email\" : [ \"hieunt@irtech.com.vn\" ],\r\n  \"loginName\" : [ \"nhanvien1\" ],\r\n  \"password\" : [ \"123123\" ],\r\n  \"sex\" : [ \"0\" ],\r\n  \"remark\" : [ \"\" ],\r\n  \"status\" : [ \"0\" ],\r\n  \"roleIds\" : [ \"\" ],\r\n  \"postIds\" : [ \"\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-15 08:25:14'),
(125, 'Carrier Group', 1, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/add', '113.176.195.221', 'XX XX', '{\r\n  \"groupCode\" : [ \"ABC\" ],\r\n  \"groupName\" : [ \"ABC\" ],\r\n  \"operateCode\" : [ \"A1, A2, A3\" ],\r\n  \"mainEmail\" : [ \"abc@abc.com\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-15 08:59:43'),
(126, 'Carrier Account', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/edit', '113.176.195.221', 'XX XX', '{\r\n  \"id\" : [ \"5\" ],\r\n  \"groupId\" : [ \"4\" ],\r\n  \"carrierCode\" : [ \"A1, A3\" ],\r\n  \"email\" : [ \"huydp@irtech.com.vn\" ],\r\n  \"fullName\" : [ \"nam\" ],\r\n  \"status\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-15 09:00:05'),
(127, 'Carrier Account', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/edit', '113.176.195.221', 'XX XX', '{\r\n  \"id\" : [ \"5\" ],\r\n  \"groupId\" : [ \"4\" ],\r\n  \"carrierCode\" : [ \"A1, A3\" ],\r\n  \"email\" : [ \"huydp@irtech.com.vn\" ],\r\n  \"fullName\" : [ \"nam\" ],\r\n  \"status\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-15 09:01:02'),
(128, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/carrier/account/resetPwd/5', '113.176.195.221', 'XX XX', '{ }', '\"carrier/account/resetPwd\"', 0, NULL, '2020-04-15 09:01:03'),
(129, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwdSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/resetPwd', '113.176.195.221', 'XX XX', '{\r\n  \"id\" : [ \"5\" ],\r\n  \"email\" : [ \"huydp@irtech.com.vn\" ],\r\n  \"password\" : [ \"123456\" ],\r\n  \"isSendEmail\" : [ \"on\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-15 09:01:08'),
(130, 'Carrier Account', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/edit', '113.176.195.221', 'XX XX', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"groupId\" : [ \"1\" ],\r\n  \"carrierCode\" : [ \"123546,1\" ],\r\n  \"email\" : [ \"tai@gmail.com\" ],\r\n  \"fullName\" : [ \"Anh Tài\" ],\r\n  \"status\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-15 09:01:28'),
(131, 'Carrier Account', 3, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.remove()', 'POST', 1, 'admin', 'R&D', '/carrier/account/remove', '113.176.195.221', 'XX XX', '{\r\n  \"ids\" : [ \"5\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-15 09:02:56'),
(132, 'Carrier Account', 1, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/add', '113.176.195.221', 'XX XX', '{\r\n  \"groupId\" : [ \"1\" ],\r\n  \"carrierCode\" : [ \"1\" ],\r\n  \"email\" : [ \"tai@gmail.com\" ],\r\n  \"password\" : [ \"123qwe123\" ],\r\n  \"fullName\" : [ \"Anh Tafi\" ],\r\n  \"status\" : [ \"0\" ],\r\n  \"isSendEmail\" : [ \"on\" ]\r\n}', '{\r\n  \"msg\" : \"Email đã tồn tại!\",\r\n  \"code\" : 500\r\n}', 0, NULL, '2020-04-15 09:04:33'),
(133, 'Carrier Account', 1, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/add', '192.168.1.11', 'Intranet IP', '{\r\n  \"groupId\" : [ \"2\" ],\r\n  \"carrierCode\" : [ \" SITC2\" ],\r\n  \"email\" : [ \"tai@gmail.com\" ],\r\n  \"password\" : [ \"123123\" ],\r\n  \"fullName\" : [ \"test\" ],\r\n  \"status\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Email đã tồn tại!\",\r\n  \"code\" : 500\r\n}', 0, NULL, '2020-04-15 09:04:53'),
(134, 'Carrier Account', 1, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/add', '192.168.1.11', 'Intranet IP', '{\r\n  \"groupId\" : [ \"2\" ],\r\n  \"carrierCode\" : [ \" SITC2\" ],\r\n  \"email\" : [ \"tai@gmail.com\" ],\r\n  \"password\" : [ \"123123\" ],\r\n  \"fullName\" : [ \"test\" ],\r\n  \"status\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Email đã tồn tại!\",\r\n  \"code\" : 500\r\n}', 0, NULL, '2020-04-15 09:04:58'),
(135, '账户解锁', 0, 'vn.com.irtech.eport.web.controller.monitor.SysLogininforController.unlock()', 'POST', 1, 'admin', 'R&D', '/monitor/logininfor/unlock', '113.176.195.221', 'XX XX', '{\r\n  \"loginName\" : [ \"Carrier: admin\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-15 14:11:01'),
(136, '账户解锁', 0, 'vn.com.irtech.eport.web.controller.monitor.SysLogininforController.unlock()', 'POST', 1, 'admin', 'R&D', '/monitor/logininfor/unlock', '113.176.195.221', 'XX XX', '{\r\n  \"loginName\" : [ \"Carrier: admin\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-15 14:11:08'),
(137, 'Carrier Group', 1, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/add', '113.176.195.221', 'XX XX', '{\r\n  \"groupCode\" : [ \"ABC\" ],\r\n  \"groupName\" : [ \"Wanhai\" ],\r\n  \"operateCode\" : [ \"A1, A2, B1, B2, C1\" ],\r\n  \"mainEmail\" : [ \"abc@abc.com\" ]\r\n}', '{\r\n  \"msg\" : \"Group code already exist\",\r\n  \"code\" : 500\r\n}', 0, NULL, '2020-04-15 14:24:53'),
(138, 'Carrier Group', 1, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/add', '113.176.195.221', 'XX XX', '{\r\n  \"groupCode\" : [ \"ABC123\" ],\r\n  \"groupName\" : [ \"Wanhai\" ],\r\n  \"operateCode\" : [ \"A1, A2, B1, B2, C1\" ],\r\n  \"mainEmail\" : [ \"abc@abc.com\" ]\r\n}', '{\r\n  \"msg\" : \"Email already exist\",\r\n  \"code\" : 500\r\n}', 0, NULL, '2020-04-15 14:25:06'),
(139, 'Carrier Group', 1, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/add', '113.176.195.221', 'XX XX', '{\r\n  \"groupCode\" : [ \"ABC123\" ],\r\n  \"groupName\" : [ \"Wanhai\" ],\r\n  \"operateCode\" : [ \"A1, A2, B1, B2, C1\" ],\r\n  \"mainEmail\" : [ \"abc@abc.com.vn\" ]\r\n}', 'null', 1, '\r\n### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'group_code\' at row 1\r\n### The error may involve vn.com.irtech.eport.carrier.mapper.CarrierGroupMapper.insertCarrierGroup-Inline\r\n### The error occurred while setting parameters\r\n### SQL: insert into carrier_group          ( group_code,             group_name,             operate_code,             main_email,             create_by,             create_time )           values ( ?,             ?,             ?,             ?,             ?,             ? )\r\n### Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'group_code\' at row 1\n; Data truncation: Data too long for column \'group_code\' at row 1; nested exception is com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'group_code\' at row 1', '2020-04-15 14:25:16'),
(140, 'Carrier Group', 1, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/add', '113.176.195.221', 'XX XX', '{\r\n  \"groupCode\" : [ \"123abc\" ],\r\n  \"groupName\" : [ \"Wanhai\" ],\r\n  \"operateCode\" : [ \"A12, B13, C14\" ],\r\n  \"mainEmail\" : [ \"tai@gmail.com\" ]\r\n}', 'null', 1, '\r\n### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'group_code\' at row 1\r\n### The error may involve vn.com.irtech.eport.carrier.mapper.CarrierGroupMapper.insertCarrierGroup-Inline\r\n### The error occurred while setting parameters\r\n### SQL: insert into carrier_group          ( group_code,             group_name,             operate_code,             main_email,             create_by,             create_time )           values ( ?,             ?,             ?,             ?,             ?,             ? )\r\n### Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'group_code\' at row 1\n; Data truncation: Data too long for column \'group_code\' at row 1; nested exception is com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'group_code\' at row 1', '2020-04-15 14:31:56'),
(141, 'Carrier Group', 1, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/add', '113.176.195.221', 'XX XX', '{\r\n  \"groupCode\" : [ \"123abc\" ],\r\n  \"groupName\" : [ \"Wanhai\" ],\r\n  \"operateCode\" : [ \"A12, B13, C14\" ],\r\n  \"mainEmail\" : [ \"tai@gmail.com\" ]\r\n}', 'null', 1, '\r\n### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'group_code\' at row 1\r\n### The error may involve vn.com.irtech.eport.carrier.mapper.CarrierGroupMapper.insertCarrierGroup-Inline\r\n### The error occurred while setting parameters\r\n### SQL: insert into carrier_group          ( group_code,             group_name,             operate_code,             main_email,             create_by,             create_time )           values ( ?,             ?,             ?,             ?,             ?,             ? )\r\n### Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'group_code\' at row 1\n; Data truncation: Data too long for column \'group_code\' at row 1; nested exception is com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'group_code\' at row 1', '2020-04-15 14:32:02'),
(142, 'Carrier Group', 1, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/add', '113.176.195.221', 'XX XX', '{\r\n  \"groupCode\" : [ \"123\" ],\r\n  \"groupName\" : [ \"Wanhai\" ],\r\n  \"operateCode\" : [ \"A12, B13, C14\" ],\r\n  \"mainEmail\" : [ \"tai@gmail.com\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-15 14:34:40'),
(143, 'Carrier Account', 1, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/add', '113.176.195.221', 'XX XX', '{\r\n  \"groupId\" : [ \"5\" ],\r\n  \"carrierCode\" : [ \"A12, B13, C14\" ],\r\n  \"email\" : [ \"han@gmail.com\" ],\r\n  \"password\" : [ \"123456\" ],\r\n  \"fullName\" : [ \"Han\" ],\r\n  \"status\" : [ \"0\" ],\r\n  \"isSendEmail\" : [ \"on\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-15 14:42:12'),
(144, 'Reset password', 2, 'vn.com.irtech.eport.web.controller.system.SysUserController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/system/user/resetPwd/2', '113.176.195.221', 'XX XX', '{ }', '\"system/user/resetPwd\"', 0, NULL, '2020-04-15 15:13:01'),
(145, 'User Management', 2, 'vn.com.irtech.eport.web.controller.system.SysUserController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/user/edit', '113.176.195.221', 'XX XX', '{\r\n  \"userId\" : [ \"2\" ],\r\n  \"deptId\" : [ \"105\" ],\r\n  \"userName\" : [ \"DNG\" ],\r\n  \"phonenumber\" : [ \"15666666666\" ],\r\n  \"loginName\" : [ \"ry\" ],\r\n  \"email\" : [ \"ry@qq.com\" ],\r\n  \"sex\" : [ \"1\" ],\r\n  \"role\" : [ \"2\" ],\r\n  \"remark\" : [ \"测试员\" ],\r\n  \"status\" : [ \"0\" ],\r\n  \"roleIds\" : [ \"2\" ],\r\n  \"postIds\" : [ \"\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-15 15:13:49'),
(146, 'User Management', 2, 'vn.com.irtech.eport.web.controller.system.SysUserController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/user/edit', '113.176.195.221', 'XX XX', '{\r\n  \"userId\" : [ \"100\" ],\r\n  \"deptId\" : [ \"103\" ],\r\n  \"userName\" : [ \"Nguyen\" ],\r\n  \"phonenumber\" : [ \"09034567891\" ],\r\n  \"loginName\" : [ \"abc\" ],\r\n  \"email\" : [ \"abc@gmail.com\" ],\r\n  \"sex\" : [ \"0\" ],\r\n  \"role\" : [ \"2\" ],\r\n  \"remark\" : [ \"\" ],\r\n  \"status\" : [ \"0\" ],\r\n  \"roleIds\" : [ \"2\" ],\r\n  \"postIds\" : [ \"\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-15 15:38:08'),
(147, 'Reset password', 2, 'vn.com.irtech.eport.web.controller.system.SysUserController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/system/user/resetPwd/100', '113.176.195.221', 'XX XX', '{ }', '\"system/user/resetPwd\"', 0, NULL, '2020-04-15 15:38:17'),
(148, 'Reset password', 2, 'vn.com.irtech.eport.web.controller.system.SysUserController.resetPwdSave()', 'POST', 1, 'admin', 'R&D', '/system/user/resetPwd', '113.176.195.221', 'XX XX', '{\r\n  \"userId\" : [ \"100\" ],\r\n  \"loginName\" : [ \"abc\" ],\r\n  \"password\" : [ \"123456\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-15 15:38:23'),
(149, 'Carrier Account', 1, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/add', '113.176.195.221', 'XX XX', '{\r\n  \"groupId\" : [ \"3\" ],\r\n  \"carrierCode\" : [ \"CNC,CMA\" ],\r\n  \"email\" : [ \"minhtc@gmail.com\" ],\r\n  \"password\" : [ \"admin123\" ],\r\n  \"fullName\" : [ \"admin123\" ],\r\n  \"status\" : [ \"0\" ],\r\n  \"isSendEmail\" : [ \"on\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-24 13:48:32'),
(150, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '113.176.195.221', 'XX XX', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"groupCode\" : [ \"1\" ],\r\n  \"groupName\" : [ \"WWHA\" ],\r\n  \"operateCode\" : [ \"1\" ],\r\n  \"mainEmail\" : [ \"hello@gmail.com\" ],\r\n  \"doFlag\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-27 19:04:16'),
(151, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '113.176.195.221', 'XX XX', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"groupCode\" : [ \"1\" ],\r\n  \"groupName\" : [ \"WWHA\" ],\r\n  \"operateCode\" : [ \"1\" ],\r\n  \"mainEmail\" : [ \"hello@gmail.com\" ],\r\n  \"doFlag\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-27 19:04:21'),
(152, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '113.176.195.221', 'XX XX', '{\r\n  \"id\" : [ \"3\" ],\r\n  \"groupCode\" : [ \"CMA\" ],\r\n  \"groupName\" : [ \"CMA\" ],\r\n  \"operateCode\" : [ \"CNC,CMA\" ],\r\n  \"mainEmail\" : [ \"lehanm21790@gmail.com\" ],\r\n  \"doFlag\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-27 19:04:42'),
(153, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '113.176.195.221', 'XX XX', '{\r\n  \"id\" : [ \"3\" ],\r\n  \"groupCode\" : [ \"CMA\" ],\r\n  \"groupName\" : [ \"CMA\" ],\r\n  \"operateCode\" : [ \"CNC,CMA\" ],\r\n  \"mainEmail\" : [ \"lehanm21790@gmail.com\" ],\r\n  \"doFlag\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-27 19:04:50'),
(154, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '171.227.17.69', 'XX XX', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"groupCode\" : [ \"1\" ],\r\n  \"groupName\" : [ \"WWHA\" ],\r\n  \"operateCode\" : [ \"1\" ],\r\n  \"mainEmail\" : [ \"hello@gmail.com\" ],\r\n  \"doFlag\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-04 05:28:57'),
(155, 'Logistic Group', 1, 'vn.com.irtech.eport.logistic.controller.LogisticGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/group/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"groupName\" : [ \"VINCOSHIP\" ],\r\n  \"mainEmail\" : [ \"\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-23 08:19:34'),
(156, 'Logistic account', 1, 'vn.com.irtech.eport.logistic.controller.LogisticAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/account/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"groupId\" : [ \"2\" ],\r\n  \"email\" : [ \"ope@danangport.com\" ],\r\n  \"password\" : [ \"123456\" ],\r\n  \"fullName\" : [ \"Operator 1\" ],\r\n  \"status\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-23 08:20:19'),
(157, 'Logistic Group', 2, 'vn.com.irtech.eport.logistic.controller.LogisticGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/logistic/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"2\" ],\r\n  \"groupName\" : [ \"DaNang Port\" ],\r\n  \"mainEmail\" : [ \"\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-23 08:20:36'),
(158, 'Logistic Group', 1, 'vn.com.irtech.eport.logistic.controller.LogisticGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/group/add', '192.168.1.2', 'Intranet IP', '{\r\n  \"groupName\" : [ \"VINCOSHIP\" ],\r\n  \"mainEmail\" : [ \"admin@_vincoship.com\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-23 08:40:36'),
(159, 'Logistic account', 1, 'vn.com.irtech.eport.logistic.controller.LogisticAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/account/add', '192.168.1.2', 'Intranet IP', '{\r\n  \"groupId\" : [ \"3\" ],\r\n  \"email\" : [ \"MST123456\" ],\r\n  \"password\" : [ \"123456\" ],\r\n  \"fullName\" : [ \"Nguyen Van A\" ],\r\n  \"status\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Email không hợp lệ!\",\r\n  \"code\" : 500\r\n}', 0, NULL, '2020-05-23 08:57:23'),
(160, 'Logistic account', 1, 'vn.com.irtech.eport.logistic.controller.LogisticAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/account/add', '192.168.1.2', 'Intranet IP', '{\r\n  \"groupId\" : [ \"3\" ],\r\n  \"email\" : [ \"mst@dnp.com\" ],\r\n  \"password\" : [ \"123456\" ],\r\n  \"fullName\" : [ \"Nguyen Van A\" ],\r\n  \"status\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-23 08:57:43'),
(161, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '113.176.195.221', 'XX XX', '{\r\n  \"id\" : [ \"3\" ],\r\n  \"groupCode\" : [ \"CMA\" ],\r\n  \"groupName\" : [ \"CMA\" ],\r\n  \"operateCode\" : [ \"CNC,CMA\" ],\r\n  \"mainEmail\" : [ \"abc@gmail.com\" ],\r\n  \"doFlag\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-27 10:47:50'),
(162, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '113.176.195.221', 'XX XX', '{\r\n  \"id\" : [ \"2\" ],\r\n  \"groupCode\" : [ \"SITC\" ],\r\n  \"groupName\" : [ \"SITC\" ],\r\n  \"operateCode\" : [ \"S\" ],\r\n  \"mainEmail\" : [ \"sitc@abc.com\" ],\r\n  \"doFlag\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-27 10:47:59'),
(163, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '113.176.195.221', 'XX XX', '{\r\n  \"id\" : [ \"3\" ],\r\n  \"groupCode\" : [ \"CMA\" ],\r\n  \"groupName\" : [ \"CMA\" ],\r\n  \"operateCode\" : [ \"ABC.ABC\" ],\r\n  \"mainEmail\" : [ \"abc@gmail.com\" ],\r\n  \"doFlag\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-27 10:48:07'),
(164, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '113.176.195.221', 'XX XX', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"groupCode\" : [ \"1\" ],\r\n  \"groupName\" : [ \"WWHA\" ],\r\n  \"operateCode\" : [ \"ABC BAC\" ],\r\n  \"mainEmail\" : [ \"hello@gmail.com\" ],\r\n  \"doFlag\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-27 10:48:14'),
(165, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '113.176.195.221', 'XX XX', '{\r\n  \"id\" : [ \"5\" ],\r\n  \"groupCode\" : [ \"123\" ],\r\n  \"groupName\" : [ \"Wanhai\" ],\r\n  \"operateCode\" : [ \"ABC,ABC\" ],\r\n  \"mainEmail\" : [ \"tai@gmail.com\" ],\r\n  \"doFlag\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-27 10:48:23'),
(166, 'Carrier Account', 3, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.remove()', 'POST', 1, 'admin', 'R&D', '/carrier/account/remove', '113.176.195.221', 'XX XX', '{\r\n  \"ids\" : [ \"4\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-27 10:49:45'),
(167, 'Logistic Group', 2, 'vn.com.irtech.eport.logistic.controller.LogisticGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/logistic/group/edit', '113.176.195.221', 'XX XX', '{\r\n  \"id\" : [ \"2\" ],\r\n  \"groupName\" : [ \"MTShip\" ],\r\n  \"mainEmail\" : [ \"\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-27 11:17:17'),
(168, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/carrier/account/resetPwd/1', '113.176.195.221', 'XX XX', '{ }', '\"carrier/account/resetPwd\"', 0, NULL, '2020-05-27 14:16:36'),
(169, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwdSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/resetPwd', '113.176.195.221', 'XX XX', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"email\" : [ \"minhtc@admin.com\" ],\r\n  \"password\" : [ \"admin123\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-27 14:16:42'),
(170, 'Carrier Account', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/edit', '113.176.195.221', 'XX XX', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"groupId\" : [ \"1\" ],\r\n  \"carrierCode\" : [ \"CNC,CMA\" ],\r\n  \"fullName\" : [ \"Trần Minh\" ],\r\n  \"status\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-27 14:17:38'),
(171, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/carrier/account/resetPwd/1', '113.176.195.221', 'XX XX', '{ }', '\"carrier/account/resetPwd\"', 0, NULL, '2020-05-27 14:17:39'),
(172, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/carrier/account/resetPwd/1', '113.176.195.221', 'XX XX', '{ }', '\"carrier/account/resetPwd\"', 0, NULL, '2020-05-27 14:17:43'),
(173, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwdSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/resetPwd', '113.176.195.221', 'XX XX', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"email\" : [ \"minhtc@admin.com\" ],\r\n  \"password\" : [ \"admin123\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-27 14:18:45'),
(174, 'Reset password', 2, 'vn.com.irtech.eport.web.controller.system.SysUserController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/system/user/resetPwd/101', '113.176.195.221', 'XX XX', '{ }', '\"system/user/resetPwd\"', 0, NULL, '2020-05-28 15:57:46'),
(175, 'Logistic Group', 1, 'vn.com.irtech.eport.logistic.controller.LogisticGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/group/add', '171.227.17.4', 'XX XX', '{\r\n  \"groupName\" : [ \"Vinconship\" ],\r\n  \"address\" : [ \"35 Cao Thang\" ],\r\n  \"mst\" : [ \"123123123\" ],\r\n  \"phone\" : [ \"123123123\" ],\r\n  \"fax\" : [ \"123123123123\" ],\r\n  \"emailAddress\" : [ \"dientu@dientu.com\" ],\r\n  \"businessRegistrationCertificate\" : [ \"giay dk doanh nghiep\" ],\r\n  \"dateOfIssueRegistration\" : [ \"2020-05-29\" ],\r\n  \"placeOfIssueRegistration\" : [ \"Da Nang\" ],\r\n  \"authorizedRepresentative\" : [ \"khong biet\" ],\r\n  \"representativePosition\" : [ \"khong biet\" ],\r\n  \"followingAuthorizationFormNo\" : [ \"1231\" ],\r\n  \"signDate\" : [ \"2020-05-29\" ],\r\n  \"owned\" : [ \"khong biet\" ],\r\n  \"identifyCardNo\" : [ \"12312312311\" ],\r\n  \"dateOfIssueIdentify\" : [ \"2020-05-29\" ],\r\n  \"placeOfIssueIdentify\" : [ \"khong biet\" ],\r\n  \"mobilePhone\" : [ \"09213231223\" ],\r\n  \"email\" : [ \"email@email.com\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-29 19:19:57'),
(176, 'Logistic Group', 1, 'vn.com.irtech.eport.logistic.controller.LogisticGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/group/add', '116.110.102.253', 'XX XX', '{\r\n  \"groupName\" : [ \"VINCOSHIP\" ],\r\n  \"address\" : [ \"40 Hoàng sa, Đà Nẵng\" ],\r\n  \"mst\" : [ \"0123456789\" ],\r\n  \"phone\" : [ \"023645066\" ],\r\n  \"fax\" : [ \"123123123123\" ],\r\n  \"emailAddress\" : [ \"vinco@vinco.com\" ],\r\n  \"businessRegistrationCertificate\" : [ \"Cty cổ phần VINCOSHIP\" ],\r\n  \"dateOfIssueRegistration\" : [ \"2020-05-28\" ],\r\n  \"placeOfIssueRegistration\" : [ \"Đà Nẵng\" ],\r\n  \"authorizedRepresentative\" : [ \"Nguyễn Văn A\" ],\r\n  \"representativePosition\" : [ \"Giám đốc\" ],\r\n  \"followingAuthorizationFormNo\" : [ \"13\" ],\r\n  \"signDate\" : [ \"2020-05-30\" ],\r\n  \"owned\" : [ \"abc\" ],\r\n  \"identifyCardNo\" : [ \"201734555\" ],\r\n  \"dateOfIssueIdentify\" : [ \"2020-05-08\" ],\r\n  \"placeOfIssueIdentify\" : [ \"Đà Nẵng\" ],\r\n  \"mobilePhone\" : [ \"076 455 555\" ],\r\n  \"email\" : [ \"vinco@vinco.com\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-29 20:24:54'),
(177, 'Logistic account', 1, 'vn.com.irtech.eport.logistic.controller.LogisticAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/account/add', '113.176.195.221', 'XX XX', '{\r\n  \"groupId\" : [ \"1\" ],\r\n  \"email\" : [ \"vinconship@vincon.com\" ],\r\n  \"userName\" : [ \"mst123\" ],\r\n  \"password\" : [ \"123456\" ],\r\n  \"fullName\" : [ \"tester 1\" ],\r\n  \"status\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-30 08:15:22'),
(178, 'Logistic account', 1, 'vn.com.irtech.eport.logistic.controller.LogisticAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/account/add', '113.176.195.221', 'XX XX', '{\r\n  \"groupId\" : [ \"1\" ],\r\n  \"email\" : [ \"vinco@vinco.com\" ],\r\n  \"userName\" : [ \"0123456789\" ],\r\n  \"password\" : [ \"123456\" ],\r\n  \"fullName\" : [ \"Nguyen Nguyen\" ],\r\n  \"status\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-30 08:15:45'),
(186, '菜单管理', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '113.176.195.221', 'XX XX', '{\r\n  \"menuId\" : [ \"103\" ],\r\n  \"parentId\" : [ \"1\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Phòng Ban\" ],\r\n  \"url\" : [ \"/system/dept\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"system:dept:view\" ],\r\n  \"orderNum\" : [ \"4\" ],\r\n  \"icon\" : [ \"#\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-08 14:16:57'),
(187, '菜单管理', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '113.176.195.221', 'XX XX', '{\r\n  \"menuId\" : [ \"106\" ],\r\n  \"parentId\" : [ \"1\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Cấu Hình Hệ Thống\" ],\r\n  \"url\" : [ \"/system/config\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"system:config:view\" ],\r\n  \"orderNum\" : [ \"7\" ],\r\n  \"icon\" : [ \"#\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-08 14:17:32'),
(188, '菜单管理', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '113.176.195.221', 'XX XX', '{\r\n  \"menuId\" : [ \"2012\" ],\r\n  \"parentId\" : [ \"1\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Quản Lý Vận Đơn\" ],\r\n  \"url\" : [ \"/carrier/admin/do/getViewDo\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"carrier:admin:do:getViewDo:view\" ],\r\n  \"orderNum\" : [ \"1\" ],\r\n  \"icon\" : [ \"fa fa-file-excel-o\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-08 14:18:15'),
(189, '菜单管理', 1, 'vn.com.irtech.eport.web.controller.system.SysMenuController.addSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/add', '113.176.195.221', 'XX XX', '{\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Quản Lý Vận Đơn\" ],\r\n  \"url\" : [ \"\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"4\" ],\r\n  \"icon\" : [ \"fa fa-address-book-o\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-08 14:19:16'),
(190, '菜单管理', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '113.176.195.221', 'XX XX', '{\r\n  \"menuId\" : [ \"2012\" ],\r\n  \"parentId\" : [ \"2021\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Danh Sách Vận Đơn\" ],\r\n  \"url\" : [ \"/carrier/admin/do/getViewDo\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"carrier:admin:do:getViewDo:view\" ],\r\n  \"orderNum\" : [ \"1\" ],\r\n  \"icon\" : [ \"fa fa-file-excel-o\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-08 14:19:37'),
(191, '菜单管理', 1, 'vn.com.irtech.eport.web.controller.system.SysMenuController.addSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/add', '113.176.195.221', 'XX XX', '{\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Kế Hoạch Bãi Cảng\" ],\r\n  \"url\" : [ \"\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"5\" ],\r\n  \"icon\" : [ \"fa fa-anchor\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-08 14:20:16'),
(192, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/carrier/account/resetPwd/1', '113.176.195.221', 'XX XX', '{ }', '\"carrier/account/resetPwd\"', 0, NULL, '2020-06-09 09:03:05'),
(193, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwdSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/resetPwd', '113.176.195.221', 'XX XX', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"email\" : [ \"minhtc@admin.com\" ],\r\n  \"password\" : [ \"admin123\" ],\r\n  \"isSendEmail\" : [ \"on\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-09 09:03:09'),
(194, 'Logistic Group', 1, 'vn.com.irtech.eport.logistic.controller.LogisticGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/group/add', '192.168.1.76', 'Intranet IP', '{\r\n  \"groupName\" : [ \"dsfds\" ],\r\n  \"address\" : [ \"đà nẵng\" ],\r\n  \"mst\" : [ \"dsfds\" ],\r\n  \"phone\" : [ \"0981974393\" ],\r\n  \"fax\" : [ \"dsf\" ],\r\n  \"emailAddress\" : [ \"ds\" ],\r\n  \"businessRegistrationCertificate\" : [ \"sđ\" ],\r\n  \"dateOfIssueRegistration\" : [ \"2020-06-10\" ],\r\n  \"placeOfIssueRegistration\" : [ \"dsfds\" ],\r\n  \"authorizedRepresentative\" : [ \"dsfds\" ],\r\n  \"representativePosition\" : [ \"dsfds\" ],\r\n  \"followingAuthorizationFormNo\" : [ \"sdf\" ],\r\n  \"signDate\" : [ \"2020-06-09\" ],\r\n  \"owned\" : [ \"dsfd\" ],\r\n  \"identifyCardNo\" : [ \"dsf\" ],\r\n  \"dateOfIssueIdentify\" : [ \"2020-06-11\" ],\r\n  \"placeOfIssueIdentify\" : [ \"dsfd\" ],\r\n  \"mobilePhone\" : [ \"dsfds\" ],\r\n  \"email\" : [ \"daily001\" ]\r\n}', '{\r\n  \"msg\" : \"Email không hợp lệ!\",\r\n  \"code\" : 500\r\n}', 0, NULL, '2020-06-09 11:31:33'),
(195, 'Logistic Group', 1, 'vn.com.irtech.eport.logistic.controller.LogisticGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/group/add', '192.168.1.76', 'Intranet IP', '{\r\n  \"groupName\" : [ \"dsfds\" ],\r\n  \"address\" : [ \"đà nẵng\" ],\r\n  \"mst\" : [ \"dsfds\" ],\r\n  \"phone\" : [ \"0981974393\" ],\r\n  \"fax\" : [ \"dsf\" ],\r\n  \"emailAddress\" : [ \"ds\" ],\r\n  \"businessRegistrationCertificate\" : [ \"sđ\" ],\r\n  \"dateOfIssueRegistration\" : [ \"2020-06-10\" ],\r\n  \"placeOfIssueRegistration\" : [ \"dsfds\" ],\r\n  \"authorizedRepresentative\" : [ \"dsfds\" ],\r\n  \"representativePosition\" : [ \"dsfds\" ],\r\n  \"followingAuthorizationFormNo\" : [ \"sdf\" ],\r\n  \"signDate\" : [ \"2020-06-09\" ],\r\n  \"owned\" : [ \"dsfd\" ],\r\n  \"identifyCardNo\" : [ \"dsf\" ],\r\n  \"dateOfIssueIdentify\" : [ \"2020-06-11\" ],\r\n  \"placeOfIssueIdentify\" : [ \"dsfd\" ],\r\n  \"mobilePhone\" : [ \"dsfds\" ],\r\n  \"email\" : [ \"ngan123@gmail.com\" ]\r\n}', '{\r\n  \"msg\" : \"MST không hợp lệ. Từ 10 -> 15 số\",\r\n  \"code\" : 500\r\n}', 0, NULL, '2020-06-09 11:31:47'),
(196, 'Logistic Group', 1, 'vn.com.irtech.eport.logistic.controller.LogisticGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/group/add', '192.168.1.76', 'Intranet IP', '{\r\n  \"groupName\" : [ \"dsfds\" ],\r\n  \"address\" : [ \"đà nẵng\" ],\r\n  \"mst\" : [ \"12334\" ],\r\n  \"phone\" : [ \"0981974393\" ],\r\n  \"fax\" : [ \"dsf\" ],\r\n  \"emailAddress\" : [ \"ds\" ],\r\n  \"businessRegistrationCertificate\" : [ \"sđ\" ],\r\n  \"dateOfIssueRegistration\" : [ \"2020-06-10\" ],\r\n  \"placeOfIssueRegistration\" : [ \"dsfds\" ],\r\n  \"authorizedRepresentative\" : [ \"dsfds\" ],\r\n  \"representativePosition\" : [ \"dsfds\" ],\r\n  \"followingAuthorizationFormNo\" : [ \"sdf\" ],\r\n  \"signDate\" : [ \"2020-06-09\" ],\r\n  \"owned\" : [ \"dsfd\" ],\r\n  \"identifyCardNo\" : [ \"dsf\" ],\r\n  \"dateOfIssueIdentify\" : [ \"2020-06-11\" ],\r\n  \"placeOfIssueIdentify\" : [ \"dsfd\" ],\r\n  \"mobilePhone\" : [ \"dsfds\" ],\r\n  \"email\" : [ \"ngan123@gmail.com\" ]\r\n}', '{\r\n  \"msg\" : \"MST không hợp lệ. Từ 10 -> 15 số\",\r\n  \"code\" : 500\r\n}', 0, NULL, '2020-06-09 11:31:54'),
(197, 'Logistic Group', 1, 'vn.com.irtech.eport.logistic.controller.LogisticGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/group/add', '192.168.1.76', 'Intranet IP', '{\r\n  \"groupName\" : [ \"dsfds\" ],\r\n  \"address\" : [ \"đà nẵng\" ],\r\n  \"mst\" : [ \"1233431231321\" ],\r\n  \"phone\" : [ \"0981974393\" ],\r\n  \"fax\" : [ \"dsf\" ],\r\n  \"emailAddress\" : [ \"ds\" ],\r\n  \"businessRegistrationCertificate\" : [ \"sđ\" ],\r\n  \"dateOfIssueRegistration\" : [ \"2020-06-10\" ],\r\n  \"placeOfIssueRegistration\" : [ \"dsfds\" ],\r\n  \"authorizedRepresentative\" : [ \"dsfds\" ],\r\n  \"representativePosition\" : [ \"dsfds\" ],\r\n  \"followingAuthorizationFormNo\" : [ \"sdf\" ],\r\n  \"signDate\" : [ \"2020-06-09\" ],\r\n  \"owned\" : [ \"dsfd\" ],\r\n  \"identifyCardNo\" : [ \"dsf\" ],\r\n  \"dateOfIssueIdentify\" : [ \"2020-06-11\" ],\r\n  \"placeOfIssueIdentify\" : [ \"dsfd\" ],\r\n  \"mobilePhone\" : [ \"dsfds\" ],\r\n  \"email\" : [ \"ngan123@gmail.com\" ]\r\n}', '{\r\n  \"msg\" : \"Chứng minh thư không hợp lệ. Từ 9->15 số\",\r\n  \"code\" : 500\r\n}', 0, NULL, '2020-06-09 11:32:02'),
(198, 'Logistic Group', 1, 'vn.com.irtech.eport.logistic.controller.LogisticGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/group/add', '192.168.1.76', 'Intranet IP', '{\r\n  \"groupName\" : [ \"dsfds\" ],\r\n  \"address\" : [ \"đà nẵng\" ],\r\n  \"mst\" : [ \"1233431231321\" ],\r\n  \"phone\" : [ \"0981974393\" ],\r\n  \"fax\" : [ \"dsf\" ],\r\n  \"emailAddress\" : [ \"ds\" ],\r\n  \"businessRegistrationCertificate\" : [ \"sđ\" ],\r\n  \"dateOfIssueRegistration\" : [ \"2020-06-10\" ],\r\n  \"placeOfIssueRegistration\" : [ \"dsfds\" ],\r\n  \"authorizedRepresentative\" : [ \"dsfds\" ],\r\n  \"representativePosition\" : [ \"dsfds\" ],\r\n  \"followingAuthorizationFormNo\" : [ \"sdf\" ],\r\n  \"signDate\" : [ \"2020-06-09\" ],\r\n  \"owned\" : [ \"dsfd\" ],\r\n  \"identifyCardNo\" : [ \"2123154651\" ],\r\n  \"dateOfIssueIdentify\" : [ \"2020-06-11\" ],\r\n  \"placeOfIssueIdentify\" : [ \"dsfd\" ],\r\n  \"mobilePhone\" : [ \"0156415631\" ],\r\n  \"email\" : [ \"ngan123@gmail.com\" ]\r\n}', '{\r\n  \"msg\" : \"Fax phải là số\",\r\n  \"code\" : 500\r\n}', 0, NULL, '2020-06-09 11:32:14'),
(199, 'Logistic Group', 1, 'vn.com.irtech.eport.logistic.controller.LogisticGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/group/add', '192.168.1.76', 'Intranet IP', '{\r\n  \"groupName\" : [ \"dsfds\" ],\r\n  \"address\" : [ \"đà nẵng\" ],\r\n  \"mst\" : [ \"1233431231321\" ],\r\n  \"phone\" : [ \"0981974393\" ],\r\n  \"fax\" : [ \"201561021\" ],\r\n  \"emailAddress\" : [ \"ds\" ],\r\n  \"businessRegistrationCertificate\" : [ \"sđ\" ],\r\n  \"dateOfIssueRegistration\" : [ \"2020-06-10\" ],\r\n  \"placeOfIssueRegistration\" : [ \"dsfds\" ],\r\n  \"authorizedRepresentative\" : [ \"dsfds\" ],\r\n  \"representativePosition\" : [ \"dsfds\" ],\r\n  \"followingAuthorizationFormNo\" : [ \"sdf\" ],\r\n  \"signDate\" : [ \"2020-06-09\" ],\r\n  \"owned\" : [ \"dsfd\" ],\r\n  \"identifyCardNo\" : [ \"2123154651\" ],\r\n  \"dateOfIssueIdentify\" : [ \"2020-06-11\" ],\r\n  \"placeOfIssueIdentify\" : [ \"dsfd\" ],\r\n  \"mobilePhone\" : [ \"0156415631\" ],\r\n  \"email\" : [ \"ngan123@gmail.com\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-09 11:32:21'),
(200, 'Logistic Group', 3, 'vn.com.irtech.eport.logistic.controller.LogisticGroupController.remove()', 'POST', 1, 'admin', 'R&D', '/logistic/group/remove', '192.168.1.76', 'Intranet IP', '{\r\n  \"ids\" : [ \"2\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-09 11:32:25'),
(201, 'Logistic account', 1, 'vn.com.irtech.eport.logistic.controller.LogisticAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/account/add', '192.168.1.76', 'Intranet IP', '{\r\n  \"groupId\" : [ \"1\" ],\r\n  \"email\" : [ \"nganle@gmail.com\" ],\r\n  \"userName\" : [ \"nganle\" ],\r\n  \"password\" : [ \"123456\" ],\r\n  \"fullName\" : [ \"lê thị thúy ngân\" ],\r\n  \"status\" : [ \"0\" ],\r\n  \"isSendEmail\" : [ \"on\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-09 11:50:35');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_post`
--

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

--
-- Đang đổ dữ liệu cho bảng `sys_post`
--

INSERT INTO `sys_post` (`post_id`, `post_code`, `post_name`, `post_sort`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(1, 'ceo', 'Chairman', 1, '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(2, 'se', 'project manager', 2, '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(3, 'hr', 'Human Resources', 3, '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(4, 'user', 'General staff', 4, '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_role`
--

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

--
-- Đang đổ dữ liệu cho bảng `sys_role`
--

INSERT INTO `sys_role` (`role_id`, `role_name`, `role_key`, `role_sort`, `data_scope`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(1, 'Administrator', 'admin', 1, '1', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'Quản trị viên'),
(2, 'Normal User', 'common', 2, '2', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'Nhân viên bình thường');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_role_dept`
--

DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE IF NOT EXISTS `sys_role_dept` (
  `role_id` bigint(20) NOT NULL COMMENT 'Role ID',
  `dept_id` bigint(20) NOT NULL COMMENT 'Department ID',
  PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Role and department';

--
-- Đang đổ dữ liệu cho bảng `sys_role_dept`
--

INSERT INTO `sys_role_dept` (`role_id`, `dept_id`) VALUES
(2, 100),
(2, 101),
(2, 105);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT 'Role ID',
  `menu_id` bigint(20) NOT NULL COMMENT 'Menu ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Role and menu association';

--
-- Đang đổ dữ liệu cho bảng `sys_role_menu`
--

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
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

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_user`
--

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
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='User Information';

--
-- Đang đổ dữ liệu cho bảng `sys_user`
--

INSERT INTO `sys_user` (`user_id`, `dept_id`, `login_name`, `user_name`, `user_type`, `email`, `phonenumber`, `sex`, `avatar`, `password`, `salt`, `status`, `del_flag`, `login_ip`, `login_date`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(1, 103, 'admin', 'DNG', '00', 'ry@163.com', '15888888888', '1', '', '29c67a30398638269fe600f73a054934', '111111', '0', '0', '192.168.1.76', '2020-06-09 11:17:54', 'admin', '2018-03-16 11:33:00', 'ry', '2020-06-09 11:17:54', '管理员'),
(2, 105, 'ry', 'DNG', '00', 'ry@qq.com', '15666666666', '1', '', '8e6d98b90472783cc73c17047ddccf36', '222222', '0', '0', '113.176.195.221', '2020-04-15 15:14:08', 'admin', '2018-03-16 11:33:00', 'admin', '2020-04-15 15:14:08', '测试员'),
(100, 103, 'abc', 'Nguyen', '00', 'abc@gmail.com', '09034567891', '0', '', 'cb97df1154efc9cd0d76864f20787035', '684420', '0', '0', '113.176.195.221', '2020-04-15 15:39:39', 'admin', '2020-04-14 12:20:08', 'admin', '2020-04-15 15:39:39', ''),
(101, 104, 'nhanvien1', 'nhanvien1', '00', 'hieunt@irtech.com.vn', '09234234233', '0', '', '3fd9c4d8e3d1ea20845ef00aa84e6359', '392d0b', '0', '0', '192.168.1.11', '2020-04-15 08:25:25', 'admin', '2020-04-15 08:25:14', '', '2020-04-15 08:25:25', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_user_online`
--

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
  `expire_time` int(5) DEFAULT 0 COMMENT '超时时间，单位为分钟',
  PRIMARY KEY (`sessionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='在线用户记录';

--
-- Đang đổ dữ liệu cho bảng `sys_user_online`
--

INSERT INTO `sys_user_online` (`sessionId`, `login_name`, `dept_name`, `ipaddr`, `login_location`, `browser`, `os`, `status`, `start_timestamp`, `last_access_time`, `expire_time`) VALUES
('89d2fcaa-9e69-4fc5-b3a2-b02c019eafe3', 'minhtc@gmail.com', 'Carrier', '192.168.1.68', 'Intranet IP', 'Chrome 8', 'Windows 10', 'on_line', '2020-06-09 13:42:29', '2020-06-09 13:42:37', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_user_post`
--

DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE IF NOT EXISTS `sys_user_post` (
  `user_id` bigint(20) NOT NULL COMMENT 'User ID',
  `post_id` bigint(20) NOT NULL COMMENT 'Post ID',
  PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='User and post';

--
-- Đang đổ dữ liệu cho bảng `sys_user_post`
--

INSERT INTO `sys_user_post` (`user_id`, `post_id`) VALUES
(1, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE IF NOT EXISTS `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT 'User ID',
  `role_id` bigint(20) NOT NULL COMMENT 'Role ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='User and role association';

--
-- Đang đổ dữ liệu cho bảng `sys_user_role`
--

INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 2),
(100, 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `transport_account`
--

DROP TABLE IF EXISTS `transport_account`;
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
  `external_rent_status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Thuê ngoài (0 nomal 1 rent)',
  `valid_date` datetime NOT NULL COMMENT 'Hieu Luc Den',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Driver login info';

--
-- Đang đổ dữ liệu cho bảng `transport_account`
--

INSERT INTO `transport_account` (`id`, `logistic_group_id`, `plate_number`, `mobile_number`, `full_name`, `password`, `salt`, `status`, `del_flag`, `external_rent_status`, `valid_date`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
(1, 3, '43A12345', '0905123456', 'Tai Xe A', '822b1fc410b7177ee3afcbe44e6f7f25', '972717', '1', b'0', '0', '2020-05-30 00:00:00', NULL, 'Nguyen Van A', '2020-05-23 10:46:13', NULL, '2020-05-23 11:46:23'),
(2, 1, '43C1-224.44', '0764505555', 'test', '0dbba4c31fef655743b03b03aee72da7', '09b336', '0', b'0', '0', '2020-05-30 00:00:00', NULL, 'Nguyen Nguyen', '2020-05-30 08:17:59', NULL, '2020-05-30 08:18:59'),
(3, 1, '43A 123.46', '0905123457', 'Nguyen Van A', '29559bb75f27e5e0ea97838b1d315d59', '6f87e6', '1', b'0', '0', '2020-07-31 00:00:00', NULL, 'tester 1', '2020-05-30 09:53:26', NULL, NULL);

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `qrtz_blob_triggers`
--
ALTER TABLE `qrtz_blob_triggers`
  ADD CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`,`trigger_name`,`trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`);

--
-- Các ràng buộc cho bảng `qrtz_cron_triggers`
--
ALTER TABLE `qrtz_cron_triggers`
  ADD CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`,`trigger_name`,`trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`);

--
-- Các ràng buộc cho bảng `qrtz_simple_triggers`
--
ALTER TABLE `qrtz_simple_triggers`
  ADD CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`,`trigger_name`,`trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`);

--
-- Các ràng buộc cho bảng `qrtz_simprop_triggers`
--
ALTER TABLE `qrtz_simprop_triggers`
  ADD CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`,`trigger_name`,`trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`);

--
-- Các ràng buộc cho bảng `qrtz_triggers`
--
ALTER TABLE `qrtz_triggers`
  ADD CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`,`job_name`,`job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
