-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th4 10, 2020 lúc 04:47 AM
-- Phiên bản máy phục vụ: 10.4.11-MariaDB
-- Phiên bản PHP: 7.4.2

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

CREATE TABLE `carrier_account` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
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
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Carrier account';

--
-- Đang đổ dữ liệu cho bảng `carrier_account`
--

INSERT INTO `carrier_account` (`id`, `group_id`, `carrier_code`, `email`, `password`, `salt`, `full_name`, `status`, `del_flag`, `login_ip`, `login_date`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
(1, 1, '123546', 'tai@gmail.com', 'a8073909b5853562442cb342386d8a76', '0f1722', 'Anh Tài', '0', '0', '127.0.0.1', '2020-04-10 09:38:02', NULL, '', '2020-04-07 11:18:40', 'DNG', '2020-04-10 09:38:02'),
(2, 1, 'dnkasbdk', 'nqat2003@gmail.com', '88cd9c095318aa5d9f84d589f437760f', '0d78ae', 'Anh Taif', '0', '0', '', NULL, NULL, 'DNG', '2020-04-08 10:20:33', 'DNG', '2020-04-08 10:21:33');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `carrier_group`
--

CREATE TABLE `carrier_group` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `group_code` varchar(5) COLLATE utf8_bin NOT NULL COMMENT 'Group Code',
  `group_name` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Group Name',
  `operate_code` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Operate Codes',
  `main_email` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Main Emails',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Carrier Group';

--
-- Đang đổ dữ liệu cho bảng `carrier_group`
--

INSERT INTO `carrier_group` (`id`, `group_code`, `group_name`, `operate_code`, `main_email`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
(1, '1', 'WWHA', '1', 'hello@gmail.com', '123123', NULL, '123123123', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `equipment_do`
--

CREATE TABLE `equipment_do` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
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
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Exchange Delivery Order';

--
-- Đang đổ dữ liệu cho bảng `equipment_do`
--

INSERT INTO `equipment_do` (`id`, `carrier_id`, `carrier_code`, `order_number`, `bill_of_lading`, `business_unit`, `consignee`, `container_number`, `expired_dem`, `empty_container_depot`, `det_free_time`, `secure_code`, `release_date`, `vessel`, `voy_no`, `do_type`, `status`, `process_status`, `document_status`, `document_receipt_date`, `release_status`, `create_source`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
(1, 1, '\"23\"', NULL, '\"213\"', NULL, '\"07/04/2020\"', '12', '2020-04-07 11:26:08', '\"23\"', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, NULL, '2020-04-07 11:26:08', NULL, NULL),
(2, 1, 'WHA', NULL, '1', NULL, '1', '1', '2020-04-07 13:07:54', '1', 1, NULL, NULL, 'null', 'null', NULL, '0', '0', '0', NULL, '0', NULL, 'null', 'Anh Tai', '2020-04-07 13:07:54', NULL, NULL),
(3, 1, 'WHA', NULL, '1', NULL, '1', '1', '2020-04-07 13:09:24', '1', 1, NULL, NULL, 'null', 'null', NULL, '0', '0', '0', NULL, '0', NULL, 'null', 'Anh Tai', '2020-04-07 13:09:24', NULL, NULL),
(4, 1, 'WHA', NULL, '123', NULL, '3', '2131', '2020-04-07 13:09:24', 'null', NULL, NULL, NULL, 'null', 'null', NULL, '0', '0', '0', NULL, '0', NULL, 'null', 'Anh Tai', '2020-04-07 13:09:24', NULL, NULL),
(5, 1, 'WHA', NULL, '123', NULL, '3123', '1231', '2020-04-07 13:33:30', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null', 'Anh Tài', '2020-04-07 13:33:30', NULL, NULL),
(6, 1, 'WHA', NULL, '123', NULL, '123', '123', '2020-04-07 13:33:30', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null', 'Anh Tài', '2020-04-07 13:33:30', NULL, NULL),
(7, 1, 'WHA', NULL, '123', NULL, '3123', '1231', '2020-04-07 13:35:05', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-07 13:35:05', NULL, NULL),
(8, 1, 'WHA', NULL, '123', NULL, '123', '123', '2020-04-07 13:35:05', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-07 13:35:05', NULL, NULL),
(9, 1, 'WHA', NULL, '123', NULL, '3123', '1231', '2010-12-25 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-07 13:39:19', NULL, NULL),
(10, 1, 'WHA', NULL, '123', NULL, '123', '123', '2010-12-25 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-07 13:39:19', NULL, NULL),
(11, 1, 'WHA', NULL, '123', NULL, '3123', '1231', '2020-03-12 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-07 13:40:58', NULL, NULL),
(12, 1, 'WHA', NULL, '123', NULL, '123', '123', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-07 13:40:58', NULL, NULL),
(13, 1, 'WHA', NULL, '3123', NULL, '23', '21', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-07 14:15:36', NULL, NULL),
(14, 1, 'WHA', NULL, '123', NULL, '231', '123', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-07 14:15:36', NULL, NULL),
(15, 1, 'WHA', NULL, '3123', NULL, '23', '21', '2020-04-05 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-07 14:15:56', NULL, NULL),
(16, 1, 'WHA', NULL, '123', NULL, '231', '123', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-07 14:15:56', NULL, NULL),
(17, 1, 'WHA', NULL, '23123', NULL, '231', '1', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-07 14:29:05', NULL, NULL),
(18, 1, 'WHA', NULL, '1231', NULL, '123123', '23123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-07 14:29:05', NULL, NULL),
(19, 1, '123', NULL, '231', NULL, '07/04/2020', '23123', '2020-04-07 16:44:18', NULL, NULL, NULL, NULL, NULL, 'null]', NULL, '0', '0', '0', NULL, '0', NULL, '\"1\":[nul', 'Anh Tài', '2020-04-07 16:44:18', NULL, NULL),
(20, 1, 'ádfads', NULL, '1231', NULL, '23123', '23123', '2020-04-23 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-07 16:49:46', NULL, NULL),
(21, 1, '1234', NULL, '123', NULL, '1231', '123', '2020-04-16 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-07 17:09:54', NULL, NULL),
(22, 1, '1234', NULL, '123', NULL, '123', '123', '2020-04-25 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-07 17:13:26', NULL, NULL),
(23, 1, '1234', NULL, '123', NULL, '123', '123', '2020-04-25 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-07 17:18:23', NULL, NULL),
(24, 1, '1234', NULL, 'asdfaf', NULL, '123', '123', '2020-04-14 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-07 17:21:30', NULL, NULL),
(25, 1, '1234', NULL, 'asdasdf', NULL, '123', '12', '2020-04-15 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '\"1\"]', 'Anh Tài', '2020-04-07 17:22:44', NULL, NULL),
(26, 1, '1234', NULL, 'asdfaf', NULL, '123', '123', '2020-04-14 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-07 17:25:06', NULL, NULL),
(27, 1, '1234', NULL, 'asdasdf', NULL, '123', '12', '2020-04-15 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '\"1\"]', 'Anh Tài', '2020-04-07 17:25:09', NULL, NULL),
(28, 1, '1234', NULL, '123', NULL, '123', '23', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-07 19:24:10', NULL, NULL),
(29, 1, '1234', NULL, '1231', NULL, '123123', '1231', '2020-04-17 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-07 19:33:34', NULL, NULL),
(30, 1, '1234', NULL, '21312', NULL, '1231', '12312', '2020-04-15 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-07 19:33:48', NULL, NULL),
(31, 1, '1234', NULL, '123', NULL, '23123', '123', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-07 19:35:32', NULL, NULL),
(32, 1, '1234', NULL, '123', NULL, '123', '123123', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-07 19:35:58', NULL, NULL),
(33, 1, '1234', NULL, '123', NULL, '23123', '2312', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-07 19:39:23', NULL, NULL),
(34, 1, '1234', NULL, '12312', NULL, '123123', '12312', '2020-04-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-07 19:40:10', NULL, NULL),
(35, 1, '1234', NULL, '123123', NULL, '123123', '12312', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-08 08:16:03', NULL, NULL),
(36, 1, '1234', NULL, '123123', NULL, '123123123', '123123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-08 08:18:48', NULL, NULL),
(37, 1, '1234', NULL, '123123', NULL, '123123', '123123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-08 08:23:01', NULL, NULL),
(38, 1, '1234', NULL, '1231', NULL, '123123', '23123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-08 08:23:23', NULL, NULL),
(39, 1, '1234', NULL, '123123', NULL, '3123', '1231', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-08 08:23:43', NULL, NULL),
(40, 1, '1234', NULL, '213123', NULL, '231231', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-08 08:24:05', NULL, NULL),
(41, 1, '1234', NULL, '12312', NULL, '231231', '31231', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 08:25:20', NULL, NULL),
(42, 1, '1234', NULL, '1232', NULL, '2312', '31231', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-08 08:25:20', NULL, NULL),
(43, 1, '1234', NULL, '31231', NULL, '3123123', '2312', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-08 08:25:48', NULL, NULL),
(44, 1, '1234', NULL, '1231', NULL, '12312', '231', '2020-04-15 00:00:00', '123', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 08:28:25', NULL, NULL),
(45, 1, '1234', NULL, '312', NULL, '1231231', '1231', '2020-04-16 00:00:00', '2312', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 08:28:25', NULL, NULL),
(46, 1, '1234', NULL, '12312', NULL, '123', '12312', '2020-04-10 00:00:00', '1231231', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 08:28:26', NULL, NULL),
(47, 1, '1234', NULL, '3123', NULL, '1231231', '312', '2020-04-14 00:00:00', '3123123', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 08:28:26', NULL, NULL),
(48, 1, '1234', NULL, '2312', NULL, '31', '123', '2020-04-23 00:00:00', '3123', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-08 08:28:26', NULL, NULL),
(49, 1, '\"WWHA\"', NULL, '123', NULL, '123', '23', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 09:29:21', NULL, NULL),
(50, 1, '\"WWHA\"', NULL, '123', NULL, '23', '3', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-08 09:29:21', NULL, NULL),
(51, 1, '\"WWHA\"', NULL, '123', NULL, '23', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-08 09:29:57', NULL, NULL),
(52, 1, 'WWHA', NULL, '123', NULL, '08/04/2020', '123', '2020-04-08 09:36:16', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-08 09:36:16', NULL, NULL),
(53, 1, 'WWHA', NULL, '\"123\"', NULL, '123', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'nul', 'Anh Tài', '2020-04-08 09:38:50', NULL, NULL),
(54, 1, 'WWHA', NULL, '123', NULL, '08/04/2020', '123', '2020-04-08 09:40:46', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-08 09:41:04', NULL, NULL),
(55, 1, 'WWHA', NULL, '\"123\"', NULL, '3', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 09:46:29', NULL, NULL),
(56, 1, 'WWHA', NULL, '\"123\"', NULL, '123', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 09:51:57', NULL, NULL),
(57, 1, 'WWHA', NULL, '\"12\"', NULL, '123', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 09:53:48', NULL, NULL),
(58, 1, 'WWHA', NULL, '\"123\"', NULL, '123', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-08 09:55:32', NULL, NULL),
(59, 1, 'WWHA', NULL, '\"123\"', NULL, '123', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 09:56:32', NULL, NULL),
(60, 1, 'WWHA', NULL, '\"123\"', NULL, '123', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'null]', 'Anh Tài', '2020-04-08 09:56:59', NULL, NULL),
(61, 1, 'WWHA', NULL, '\"123\"', NULL, '12312321', '123', '2020-04-08 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 09:58:16', NULL, NULL),
(62, 1, 'WWHA', NULL, '\"123\"', NULL, '123', '123', '2020-04-09 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:02:54', NULL, NULL),
(63, 1, 'WWHA', NULL, '\"123\"', NULL, '123123', '123123', '2020-04-16 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:02:55', NULL, NULL),
(64, 1, 'WWHA', NULL, '\"1231\"', NULL, '12312', '231', '2020-04-16 00:00:00', '123', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:08:51', NULL, NULL),
(65, 1, 'WWHA', NULL, '\"123\"', NULL, '3123', '12312', '2020-04-08 00:00:00', '123123', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:08:51', NULL, NULL),
(66, 1, 'WWHA', NULL, '\"12312\"', NULL, '123', '12312', '2020-04-15 00:00:00', '1231231', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:08:52', NULL, NULL),
(67, 1, 'WWHA', NULL, '\"3123\"', NULL, '1231231', '312', '2020-04-16 00:00:00', '3123123', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:08:52', NULL, NULL),
(68, 1, 'WWHA', NULL, '\"2312\"', NULL, '31', '123', '2020-04-16 00:00:00', '3123', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:08:54', NULL, NULL),
(69, 1, 'WWHA', NULL, '\"123\"', NULL, 'Anh Tài', '1231', '2020-03-13 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:11:59', NULL, NULL),
(70, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1234', '2020-04-08 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:51:35', NULL, NULL),
(71, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:51:36', NULL, NULL),
(72, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:51:36', NULL, NULL),
(73, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1237', '2020-04-11 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:51:37', NULL, NULL),
(74, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1238', '2020-04-12 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:51:37', NULL, NULL),
(75, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1234', '2020-04-08 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '\"Hello\"', 'Anh Tài', '2020-04-08 10:55:05', NULL, NULL),
(76, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:55:05', NULL, NULL),
(77, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:55:06', NULL, NULL),
(78, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1237', '2020-04-11 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:55:06', NULL, NULL),
(79, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1238', '2020-04-12 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:55:06', NULL, NULL),
(80, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1234', '2020-04-08 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:57:09', NULL, NULL),
(81, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:57:12', NULL, NULL),
(82, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:57:13', NULL, NULL),
(83, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1237', '2020-04-11 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 10:57:20', NULL, NULL),
(84, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1238', '2020-04-12 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '\"hrahra\"', 'Anh Tài', '2020-04-08 10:57:23', NULL, NULL),
(85, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1234', '2020-04-08 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 11:07:00', NULL, NULL),
(86, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 11:07:02', NULL, NULL),
(87, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 11:07:03', NULL, NULL),
(88, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1237', '2020-04-11 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 11:07:04', NULL, NULL),
(89, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1238', '2020-04-12 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, 'fadsfkjnjadsf', 'Anh Tài', '2020-04-08 11:07:06', NULL, NULL),
(90, 1, 'WWHA', NULL, '[ 12315', NULL, 'Anh Taif', 'CON1234', '2020-04-08 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '213', 'Anh Tài', '2020-04-08 11:21:51', NULL, NULL),
(91, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '2131231', 'Anh Tài', '2020-04-08 11:21:58', NULL, NULL),
(92, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 11:22:03', NULL, NULL),
(93, 1, 'WWHA', NULL, '[ 12315', NULL, 'Anh Taif', 'CON1234', '2020-04-08 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '213', 'Anh Tài', '2020-04-08 11:25:58', NULL, NULL),
(94, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '2131231', 'Anh Tài', '2020-04-08 11:25:59', NULL, NULL),
(95, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 11:25:59', NULL, NULL),
(96, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1237', '2020-04-11 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 11:26:00', NULL, NULL),
(97, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1238', '2020-04-12 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 11:26:01', NULL, NULL),
(98, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1234', '2020-04-08 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '213', 'Anh Tài', '2020-04-08 11:37:13', NULL, NULL),
(99, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '2131231', 'Anh Tài', '2020-04-08 11:37:22', NULL, NULL),
(100, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 11:37:25', NULL, NULL),
(101, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1237', '2020-04-11 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 11:37:26', NULL, NULL),
(102, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1234', '2020-04-08 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '213', 'Anh Tài', '2020-04-08 11:38:47', NULL, NULL),
(103, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-09 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '2131231', 'Anh Tài', '2020-04-08 11:38:48', NULL, NULL),
(104, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 11:38:50', NULL, NULL),
(105, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1237', '2020-04-11 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 11:38:51', NULL, NULL),
(106, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1234', '2020-04-15 00:00:00', 'dsfasdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '213', 'Anh Tài', '2020-04-08 11:39:48', NULL, '2020-04-09 16:01:15'),
(107, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1235', '2020-04-10 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, '2131231', 'Anh Tài', '2020-04-08 11:39:48', NULL, '2020-04-09 16:01:39'),
(108, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1236', '2020-04-10 00:00:00', 'sadf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 11:39:48', NULL, '2020-04-09 16:02:02'),
(109, 1, 'WWHA', NULL, '12315', NULL, 'Anh Taif', 'CON1237', '2020-04-10 00:00:00', 'asdf', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-08 11:39:49', NULL, '2020-04-09 16:02:02'),
(110, 1, 'WWHA', NULL, '123', NULL, '123', '123', '2020-04-16 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-09 09:19:19', NULL, '2020-04-09 15:59:35'),
(111, 1, 'WWHA', NULL, 'Nhà', NULL, 'Nay', 'Hôm', '2020-04-10 00:00:00', 'One', 3, NULL, NULL, 'Air', NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-09 09:21:56', NULL, '2020-04-09 16:02:01'),
(112, 1, 'WWHA', NULL, 'ádà', NULL, 'ádfae', 'fadsf', '2020-04-09 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-09 17:32:55', NULL, NULL),
(113, 1, 'WWHA', NULL, '123123', NULL, '123123', 'qưeqưe', '2020-04-09 00:00:00', NULL, 1, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-09 17:34:37', NULL, NULL),
(114, 1, 'WWHA', NULL, '12312', NULL, '12312312', '123123', '2020-04-23 00:00:00', NULL, 2, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-09 17:34:37', NULL, NULL),
(115, 1, 'WWHA', NULL, '123', NULL, '21323', '123', '2020-04-10 00:00:00', '123', 123, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', NULL, '0', NULL, NULL, 'Anh Tài', '2020-04-10 09:25:12', NULL, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `gen_table`
--

CREATE TABLE `gen_table` (
  `table_id` bigint(20) NOT NULL COMMENT '编号',
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
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='代码生成业务表';

--
-- Đang đổ dữ liệu cho bảng `gen_table`
--

INSERT INTO `gen_table` (`table_id`, `table_name`, `table_comment`, `class_name`, `tpl_category`, `package_name`, `module_name`, `business_name`, `function_name`, `function_author`, `options`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(2, 'sys_post', 'Job Information Sheet', 'SysPost', 'crud', 'vn.com.irtech.eport.system', 'system', 'post', 'Job Information Sheet', 'ruoyi', NULL, 'admin', '2020-03-30 00:54:19', '', NULL, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `gen_table_column`
--

CREATE TABLE `gen_table_column` (
  `column_id` bigint(20) NOT NULL COMMENT '编号',
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
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='代码生成业务表字段';

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
-- Cấu trúc bảng cho bảng `qrtz_blob_triggers`
--

CREATE TABLE `qrtz_blob_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `blob_data` blob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_calendars`
--

CREATE TABLE `qrtz_calendars` (
  `sched_name` varchar(120) NOT NULL,
  `calendar_name` varchar(200) NOT NULL,
  `calendar` blob NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_cron_triggers`
--

CREATE TABLE `qrtz_cron_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `cron_expression` varchar(200) NOT NULL,
  `time_zone_id` varchar(80) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_fired_triggers`
--

CREATE TABLE `qrtz_fired_triggers` (
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
  `requests_recovery` varchar(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_job_details`
--

CREATE TABLE `qrtz_job_details` (
  `sched_name` varchar(120) NOT NULL,
  `job_name` varchar(200) NOT NULL,
  `job_group` varchar(200) NOT NULL,
  `description` varchar(250) DEFAULT NULL,
  `job_class_name` varchar(250) NOT NULL,
  `is_durable` varchar(1) NOT NULL,
  `is_nonconcurrent` varchar(1) NOT NULL,
  `is_update_data` varchar(1) NOT NULL,
  `requests_recovery` varchar(1) NOT NULL,
  `job_data` blob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_locks`
--

CREATE TABLE `qrtz_locks` (
  `sched_name` varchar(120) NOT NULL,
  `lock_name` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_paused_trigger_grps`
--

CREATE TABLE `qrtz_paused_trigger_grps` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_group` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_scheduler_state`
--

CREATE TABLE `qrtz_scheduler_state` (
  `sched_name` varchar(120) NOT NULL,
  `instance_name` varchar(200) NOT NULL,
  `last_checkin_time` bigint(13) NOT NULL,
  `checkin_interval` bigint(13) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_simple_triggers`
--

CREATE TABLE `qrtz_simple_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `repeat_count` bigint(7) NOT NULL,
  `repeat_interval` bigint(12) NOT NULL,
  `times_triggered` bigint(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_simprop_triggers`
--

CREATE TABLE `qrtz_simprop_triggers` (
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
  `bool_prop_2` varchar(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `qrtz_triggers`
--

CREATE TABLE `qrtz_triggers` (
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
  `job_data` blob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_config`
--

CREATE TABLE `sys_config` (
  `config_id` int(5) NOT NULL COMMENT '参数主键',
  `config_name` varchar(100) COLLATE utf8_bin DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) COLLATE utf8_bin DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) COLLATE utf8_bin DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) COLLATE utf8_bin DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='参数配置表';

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

CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) NOT NULL COMMENT 'Department id',
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
  `update_time` datetime DEFAULT NULL COMMENT 'Update time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='部门表';

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

CREATE TABLE `sys_dict_data` (
  `dict_code` bigint(20) NOT NULL COMMENT '字典编码',
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
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='字典数据表';

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

CREATE TABLE `sys_dict_type` (
  `dict_id` bigint(20) NOT NULL COMMENT 'Dictionary ID',
  `dict_name` varchar(100) COLLATE utf8_bin DEFAULT '' COMMENT 'Dictionary Name',
  `dict_type` varchar(100) COLLATE utf8_bin DEFAULT '' COMMENT 'Dictionary Type',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Statiu（0 Nomal, 1 Disabled）',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Dictionary type';

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

CREATE TABLE `sys_job` (
  `job_id` bigint(20) NOT NULL COMMENT '任务ID',
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
  `remark` varchar(500) COLLATE utf8_bin DEFAULT '' COMMENT 'Remark信息'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='定时任务调度表';

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

CREATE TABLE `sys_job_log` (
  `job_log_id` bigint(20) NOT NULL COMMENT '任务日志ID',
  `job_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) COLLATE utf8_bin NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '日志信息',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) COLLATE utf8_bin DEFAULT '' COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='定时任务调度日志表';

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_logininfor`
--

CREATE TABLE `sys_logininfor` (
  `info_id` bigint(20) NOT NULL COMMENT '访问ID',
  `login_name` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '登录账号',
  `ipaddr` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '操作系统',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统访问记录';

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
(176, 'Carrier: tai@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome Mobile', 'Android 6.x', '0', 'Đăng nhập thành công', '2020-04-10 09:38:03');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_menu`
--

CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL COMMENT 'Menu ID',
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
  `remark` varchar(500) COLLATE utf8_bin DEFAULT '' COMMENT 'Remark'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Menu permission';

--
-- Đang đổ dữ liệu cho bảng `sys_menu`
--

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
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
(2012, 'Vận Đơn', 3, 1, '/carrier/admin/do/getViewDo', '', 'C', '0', 'carrier:admin:do:getViewDo:view', 'fa fa-file-excel-o', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', 'Exchange Delivery Order Menu'),
(2013, 'Delivery Order List', 2012, 1, '#', '', 'F', '0', 'equipment:do:list', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2014, 'Add DO', 2012, 2, '#', '', 'F', '0', 'equipment:do:add', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2015, 'Edit DO', 2012, 3, '#', '', 'F', '0', 'equipment:do:edit', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2016, 'Delete DO', 2012, 4, '#', '', 'F', '0', 'equipment:do:remove', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2017, 'Export DO', 2012, 5, '#', '', 'F', '0', 'equipment:do:export', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', '');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_notice`
--

CREATE TABLE `sys_notice` (
  `notice_id` int(4) NOT NULL COMMENT '公告ID',
  `notice_title` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '公告标题',
  `notice_type` char(1) COLLATE utf8_bin NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` varchar(2000) COLLATE utf8_bin DEFAULT NULL COMMENT '公告内容',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='通知公告表';

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

CREATE TABLE `sys_oper_log` (
  `oper_id` bigint(20) NOT NULL COMMENT 'Log PK',
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
  `oper_time` datetime DEFAULT NULL COMMENT 'Operating time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Operational logging';

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
(115, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/carrier/account/resetPwd/1', '127.0.0.1', 'Intranet IP', '{ }', '\"carrier/account/resetPwd\"', 0, NULL, '2020-04-10 08:45:29');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_post`
--

CREATE TABLE `sys_post` (
  `post_id` bigint(20) NOT NULL COMMENT 'Job ID',
  `post_code` varchar(64) COLLATE utf8_bin NOT NULL COMMENT 'Post code',
  `post_name` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'position Name',
  `post_sort` int(4) NOT NULL COMMENT 'display order',
  `status` char(1) COLLATE utf8_bin NOT NULL COMMENT 'Status (0 normal 1 disabled)',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Job Information Sheet';

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

CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL COMMENT 'Role ID',
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
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Role Information';

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

CREATE TABLE `sys_role_dept` (
  `role_id` bigint(20) NOT NULL COMMENT 'Role ID',
  `dept_id` bigint(20) NOT NULL COMMENT 'Department ID'
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

CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT 'Role ID',
  `menu_id` bigint(20) NOT NULL COMMENT 'Menu ID'
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

CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL COMMENT 'User ID',
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
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='User Information';

--
-- Đang đổ dữ liệu cho bảng `sys_user`
--

INSERT INTO `sys_user` (`user_id`, `dept_id`, `login_name`, `user_name`, `user_type`, `email`, `phonenumber`, `sex`, `avatar`, `password`, `salt`, `status`, `del_flag`, `login_ip`, `login_date`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(1, 103, 'admin', 'DNG', '00', 'ry@163.com', '15888888888', '1', '', '29c67a30398638269fe600f73a054934', '111111', '0', '0', '127.0.0.1', '2020-04-10 08:45:20', 'admin', '2018-03-16 11:33:00', 'ry', '2020-04-10 08:45:20', '管理员'),
(2, 105, 'ry', 'DNG', '00', 'ry@qq.com', '15666666666', '1', '', '8e6d98b90472783cc73c17047ddccf36', '222222', '0', '0', '127.0.0.1', '2018-03-16 11:33:00', 'admin', '2018-03-16 11:33:00', 'ry', '2020-03-28 07:21:51', '测试员');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_user_online`
--

CREATE TABLE `sys_user_online` (
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
  `expire_time` int(5) DEFAULT 0 COMMENT '超时时间，单位为分钟'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='在线用户记录';

--
-- Đang đổ dữ liệu cho bảng `sys_user_online`
--

INSERT INTO `sys_user_online` (`sessionId`, `login_name`, `dept_name`, `ipaddr`, `login_location`, `browser`, `os`, `status`, `start_timestamp`, `last_access_time`, `expire_time`) VALUES
('29094623-2797-4d26-8e55-a8ccd49435e2', 'tai@gmail.com', 'Carrier', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', 'on_line', '2020-04-10 09:23:35', '2020-04-10 09:44:06', 1800000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_user_post`
--

CREATE TABLE `sys_user_post` (
  `user_id` bigint(20) NOT NULL COMMENT 'User ID',
  `post_id` bigint(20) NOT NULL COMMENT 'Post ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='User and post';

--
-- Đang đổ dữ liệu cho bảng `sys_user_post`
--

INSERT INTO `sys_user_post` (`user_id`, `post_id`) VALUES
(1, 1),
(2, 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sys_user_role`
--

CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT 'User ID',
  `role_id` bigint(20) NOT NULL COMMENT 'Role ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='User and role association';

--
-- Đang đổ dữ liệu cho bảng `sys_user_role`
--

INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 2);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `carrier_account`
--
ALTER TABLE `carrier_account`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `carrier_group`
--
ALTER TABLE `carrier_group`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `equipment_do`
--
ALTER TABLE `equipment_do`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `gen_table`
--
ALTER TABLE `gen_table`
  ADD PRIMARY KEY (`table_id`);

--
-- Chỉ mục cho bảng `gen_table_column`
--
ALTER TABLE `gen_table_column`
  ADD PRIMARY KEY (`column_id`);

--
-- Chỉ mục cho bảng `qrtz_blob_triggers`
--
ALTER TABLE `qrtz_blob_triggers`
  ADD PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`);

--
-- Chỉ mục cho bảng `qrtz_calendars`
--
ALTER TABLE `qrtz_calendars`
  ADD PRIMARY KEY (`sched_name`,`calendar_name`);

--
-- Chỉ mục cho bảng `qrtz_cron_triggers`
--
ALTER TABLE `qrtz_cron_triggers`
  ADD PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`);

--
-- Chỉ mục cho bảng `qrtz_fired_triggers`
--
ALTER TABLE `qrtz_fired_triggers`
  ADD PRIMARY KEY (`sched_name`,`entry_id`);

--
-- Chỉ mục cho bảng `qrtz_job_details`
--
ALTER TABLE `qrtz_job_details`
  ADD PRIMARY KEY (`sched_name`,`job_name`,`job_group`);

--
-- Chỉ mục cho bảng `qrtz_locks`
--
ALTER TABLE `qrtz_locks`
  ADD PRIMARY KEY (`sched_name`,`lock_name`);

--
-- Chỉ mục cho bảng `qrtz_paused_trigger_grps`
--
ALTER TABLE `qrtz_paused_trigger_grps`
  ADD PRIMARY KEY (`sched_name`,`trigger_group`);

--
-- Chỉ mục cho bảng `qrtz_scheduler_state`
--
ALTER TABLE `qrtz_scheduler_state`
  ADD PRIMARY KEY (`sched_name`,`instance_name`);

--
-- Chỉ mục cho bảng `qrtz_simple_triggers`
--
ALTER TABLE `qrtz_simple_triggers`
  ADD PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`);

--
-- Chỉ mục cho bảng `qrtz_simprop_triggers`
--
ALTER TABLE `qrtz_simprop_triggers`
  ADD PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`);

--
-- Chỉ mục cho bảng `qrtz_triggers`
--
ALTER TABLE `qrtz_triggers`
  ADD PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  ADD KEY `sched_name` (`sched_name`,`job_name`,`job_group`);

--
-- Chỉ mục cho bảng `sys_config`
--
ALTER TABLE `sys_config`
  ADD PRIMARY KEY (`config_id`);

--
-- Chỉ mục cho bảng `sys_dept`
--
ALTER TABLE `sys_dept`
  ADD PRIMARY KEY (`dept_id`);

--
-- Chỉ mục cho bảng `sys_dict_data`
--
ALTER TABLE `sys_dict_data`
  ADD PRIMARY KEY (`dict_code`);

--
-- Chỉ mục cho bảng `sys_dict_type`
--
ALTER TABLE `sys_dict_type`
  ADD PRIMARY KEY (`dict_id`),
  ADD UNIQUE KEY `dict_type` (`dict_type`);

--
-- Chỉ mục cho bảng `sys_job`
--
ALTER TABLE `sys_job`
  ADD PRIMARY KEY (`job_id`,`job_name`,`job_group`);

--
-- Chỉ mục cho bảng `sys_job_log`
--
ALTER TABLE `sys_job_log`
  ADD PRIMARY KEY (`job_log_id`);

--
-- Chỉ mục cho bảng `sys_logininfor`
--
ALTER TABLE `sys_logininfor`
  ADD PRIMARY KEY (`info_id`);

--
-- Chỉ mục cho bảng `sys_menu`
--
ALTER TABLE `sys_menu`
  ADD PRIMARY KEY (`menu_id`);

--
-- Chỉ mục cho bảng `sys_notice`
--
ALTER TABLE `sys_notice`
  ADD PRIMARY KEY (`notice_id`);

--
-- Chỉ mục cho bảng `sys_oper_log`
--
ALTER TABLE `sys_oper_log`
  ADD PRIMARY KEY (`oper_id`);

--
-- Chỉ mục cho bảng `sys_post`
--
ALTER TABLE `sys_post`
  ADD PRIMARY KEY (`post_id`);

--
-- Chỉ mục cho bảng `sys_role`
--
ALTER TABLE `sys_role`
  ADD PRIMARY KEY (`role_id`);

--
-- Chỉ mục cho bảng `sys_role_dept`
--
ALTER TABLE `sys_role_dept`
  ADD PRIMARY KEY (`role_id`,`dept_id`);

--
-- Chỉ mục cho bảng `sys_role_menu`
--
ALTER TABLE `sys_role_menu`
  ADD PRIMARY KEY (`role_id`,`menu_id`);

--
-- Chỉ mục cho bảng `sys_user`
--
ALTER TABLE `sys_user`
  ADD PRIMARY KEY (`user_id`);

--
-- Chỉ mục cho bảng `sys_user_online`
--
ALTER TABLE `sys_user_online`
  ADD PRIMARY KEY (`sessionId`);

--
-- Chỉ mục cho bảng `sys_user_post`
--
ALTER TABLE `sys_user_post`
  ADD PRIMARY KEY (`user_id`,`post_id`);

--
-- Chỉ mục cho bảng `sys_user_role`
--
ALTER TABLE `sys_user_role`
  ADD PRIMARY KEY (`user_id`,`role_id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `carrier_account`
--
ALTER TABLE `carrier_account`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID', AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `carrier_group`
--
ALTER TABLE `carrier_group`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID', AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `equipment_do`
--
ALTER TABLE `equipment_do`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID', AUTO_INCREMENT=116;

--
-- AUTO_INCREMENT cho bảng `gen_table`
--
ALTER TABLE `gen_table`
  MODIFY `table_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号', AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `gen_table_column`
--
ALTER TABLE `gen_table_column`
  MODIFY `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号', AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT cho bảng `sys_config`
--
ALTER TABLE `sys_config`
  MODIFY `config_id` int(5) NOT NULL AUTO_INCREMENT COMMENT '参数主键', AUTO_INCREMENT=100;

--
-- AUTO_INCREMENT cho bảng `sys_dept`
--
ALTER TABLE `sys_dept`
  MODIFY `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Department id', AUTO_INCREMENT=200;

--
-- AUTO_INCREMENT cho bảng `sys_dict_data`
--
ALTER TABLE `sys_dict_data`
  MODIFY `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码', AUTO_INCREMENT=100;

--
-- AUTO_INCREMENT cho bảng `sys_dict_type`
--
ALTER TABLE `sys_dict_type`
  MODIFY `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Dictionary ID', AUTO_INCREMENT=100;

--
-- AUTO_INCREMENT cho bảng `sys_job`
--
ALTER TABLE `sys_job`
  MODIFY `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID', AUTO_INCREMENT=100;

--
-- AUTO_INCREMENT cho bảng `sys_job_log`
--
ALTER TABLE `sys_job_log`
  MODIFY `job_log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID';

--
-- AUTO_INCREMENT cho bảng `sys_logininfor`
--
ALTER TABLE `sys_logininfor`
  MODIFY `info_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID', AUTO_INCREMENT=177;

--
-- AUTO_INCREMENT cho bảng `sys_menu`
--
ALTER TABLE `sys_menu`
  MODIFY `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Menu ID', AUTO_INCREMENT=2018;

--
-- AUTO_INCREMENT cho bảng `sys_notice`
--
ALTER TABLE `sys_notice`
  MODIFY `notice_id` int(4) NOT NULL AUTO_INCREMENT COMMENT '公告ID', AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT cho bảng `sys_oper_log`
--
ALTER TABLE `sys_oper_log`
  MODIFY `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Log PK', AUTO_INCREMENT=116;

--
-- AUTO_INCREMENT cho bảng `sys_post`
--
ALTER TABLE `sys_post`
  MODIFY `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Job ID', AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `sys_role`
--
ALTER TABLE `sys_role`
  MODIFY `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Role ID', AUTO_INCREMENT=100;

--
-- AUTO_INCREMENT cho bảng `sys_user`
--
ALTER TABLE `sys_user`
  MODIFY `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'User ID', AUTO_INCREMENT=100;

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
