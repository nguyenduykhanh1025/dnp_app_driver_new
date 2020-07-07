-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th7 07, 2020 lúc 03:36 PM
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
-- Cấu trúc bảng cho bảng `edo_audit_log`
--

CREATE TABLE `edo_audit_log` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `carrier_id` bigint(20) NOT NULL COMMENT 'ID Nhan Vien Hang Tau',
  `carrier_code` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'Ma Hang Tau',
  `edo_id` bigint(20) NOT NULL COMMENT 'EDO ID',
  `seq_no` int(11) NOT NULL COMMENT 'Sequence Number 1->n',
  `field_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Data Field Name',
  `old_value` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Old Value',
  `new_value` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'New Value',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='eDO Audit Trail Log' ROW_FORMAT=DYNAMIC;

--
-- Đang đổ dữ liệu cho bảng `edo_audit_log`
--

INSERT INTO `edo_audit_log` (`id`, `carrier_id`, `carrier_code`, `edo_id`, `seq_no`, `field_name`, `old_value`, `new_value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
(1, 1, 'CMA', 1, 1, 'Expired Dem', NULL, 'Sat Mar 21 00:00:00 ICT 2020', 'CMA', '2020-07-07 20:23:15', NULL, NULL),
(2, 1, 'CMA', 1, 2, 'Det Free Time', NULL, '0', 'CMA', '2020-07-07 20:23:15', NULL, NULL),
(3, 2, 'CMA', 2, 1, 'Expired Dem', NULL, 'Sat Mar 21 00:00:00 ICT 2020', 'CMA', '2020-07-07 20:23:15', NULL, NULL),
(4, 2, 'CMA', 2, 2, 'Det Free Time', NULL, '6', 'CMA', '2020-07-07 20:23:15', NULL, NULL),
(5, 2, 'CMA', 2, 3, 'Expired Dem', 'Sat Mar 21 00:00:00 ICT 2020', 'Sat Mar 21 00:00:00 ICT 2020', 'CMA', '2020-07-07 20:23:15', NULL, NULL),
(6, 2, 'CMA', 2, 4, 'Det Free Time', '6', '6', 'CMA', '2020-07-07 20:23:15', NULL, NULL),
(7, 3, 'CMA', 3, 1, 'Expired Dem', NULL, 'Tue Jan 28 00:00:00 ICT 2020', 'CMA', '2020-07-07 20:23:15', NULL, NULL),
(8, 3, 'CMA', 3, 2, 'Det Free Time', NULL, '6', 'CMA', '2020-07-07 20:23:15', NULL, NULL),
(9, 3, 'CMA', 3, 3, 'Expired Dem', 'Tue Jan 28 00:00:00 ICT 2020', 'Tue Jan 28 00:00:00 ICT 2020', 'CMA', '2020-07-07 20:23:15', NULL, NULL),
(10, 3, 'CMA', 3, 4, 'Det Free Time', '6', '6', 'CMA', '2020-07-07 20:23:15', NULL, NULL),
(11, 4, 'CMA', 4, 1, 'Expired Dem', NULL, 'Tue Jan 28 00:00:00 ICT 2020', 'CMA', '2020-07-07 20:23:15', NULL, NULL),
(12, 4, 'CMA', 4, 2, 'Det Free Time', NULL, '6', 'CMA', '2020-07-07 20:23:15', NULL, NULL),
(13, 5, 'CMA', 5, 1, 'Expired Dem', NULL, 'Tue Jan 28 00:00:00 ICT 2020', 'CMA', '2020-07-07 20:23:15', NULL, NULL),
(14, 5, 'CMA', 5, 2, 'Det Free Time', NULL, '6', 'CMA', '2020-07-07 20:23:15', NULL, NULL),
(15, 1, 'CMA', 1, 3, 'Det Free Time', '0', '5', NULL, '2020-07-07 20:24:26', NULL, NULL),
(16, 1, 'CMA', 1, 4, 'Det Free Time', '5', '9', NULL, '2020-07-07 20:24:37', NULL, NULL),
(17, 1, 'CMA', 1, 5, 'Expired Dem', 'Sat Mar 21 00:00:00 ICT 2020', 'Sat Mar 28 00:00:00 ICT 2020', NULL, '2020-07-07 20:24:51', NULL, NULL),
(18, 1, 'CMA', 1, 6, 'Expired Dem', 'Sat Mar 28 00:00:00 ICT 2020', 'Tue Mar 31 00:00:00 ICT 2020', NULL, '2020-07-07 20:25:02', NULL, NULL),
(19, 1, 'CMA', 1, 7, 'Expired Dem', 'Tue Mar 31 00:00:00 ICT 2020', 'Thu Apr 02 00:00:00 ICT 2020', NULL, '2020-07-07 20:25:15', NULL, NULL),
(20, 1, 'CMA', 1, 8, 'Det Free Time', '9', '6', NULL, '2020-07-07 20:25:15', NULL, NULL);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `edo_audit_log`
--
ALTER TABLE `edo_audit_log`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `edo_audit_log`
--
ALTER TABLE `edo_audit_log`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID', AUTO_INCREMENT=21;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
