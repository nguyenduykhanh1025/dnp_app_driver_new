-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 09, 2020 at 06:56 AM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `eport`
--
DROP DATABASE IF EXISTS `eport`;
CREATE DATABASE IF NOT EXISTS `eport` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;
USE `eport`;
-- --------------------------------------------------------

--
-- Table structure for table `carrier_account`
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
-- Dumping data for table `carrier_account`
--

INSERT INTO `carrier_account` (`id`, `group_id`, `carrier_code`, `email`, `password`, `salt`, `full_name`, `status`, `del_flag`, `login_ip`, `login_date`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
(1, 1, 'CNC,CMA,APL', 'tai@gmail.com', 'a8073909b5853562442cb342386d8a76', '0f1722', 'Anh Tài', '0', '0', '127.0.0.1', '2020-05-07 12:53:08', NULL, '', '2020-04-07 11:18:40', 'DNG', '2020-05-07 12:53:08'),
(2, 1, 'CNC,CMA,APL', 'nqat2003@gmail.com', '88cd9c095318aa5d9f84d589f437760f', '0d78ae', 'Anh Taif', '0', '0', '', NULL, NULL, 'DNG', '2020-04-08 10:20:33', 'DNG', '2020-04-08 10:21:33'),
(3, 1, '1', 'tronghieu8531@gmail.com', '8a00639deeeda4efc9b7f0056a541c71', 'dccc02', 'Nguyễn Trọng Hiếu', '0', '0', '127.0.0.1', '2020-07-08 16:09:23', NULL, 'DNG', '2020-04-14 11:49:30', 'DNG', '2020-07-08 16:09:23');

-- --------------------------------------------------------

--
-- Table structure for table `carrier_group`
--

CREATE TABLE `carrier_group` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `group_code` varchar(5) COLLATE utf8_bin NOT NULL COMMENT 'Group Code',
  `group_name` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Group Name',
  `operate_code` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Operate Codes',
  `main_email` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Main Emails',
  `do_type` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '0: DO, 1:eDO',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `do_flag` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'DO Permission',
  `edo_flag` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'EDO Permission',
  `api_private_key` text COLLATE utf8_bin DEFAULT NULL COMMENT 'API private key',
  `api_public_key` text COLLATE utf8_bin DEFAULT NULL COMMENT 'API public key',
  `path_edi_receive` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Path edi file',
  `path_edi_backup` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Path_edi_moving'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Carrier Group';

--
-- Dumping data for table `carrier_group`
--

INSERT INTO `carrier_group` (`id`, `group_code`, `group_name`, `operate_code`, `main_email`, `do_type`, `create_by`, `create_time`, `update_by`, `update_time`, `do_flag`, `edo_flag`, `api_private_key`, `api_public_key`, `path_edi_receive`, `path_edi_backup`) VALUES
(1, 'CMA', 'WWHA', 'CNC,CMA,APL', 'hello@gmail.com', '0', '123123', NULL, 'DNG', '2020-06-30 13:53:01', '1', '1', NULL, NULL, 'D:\\testReadFile', 'D:\\DaNangPort\\eDO'),
(2, 'CMA', 'asdfasdf', 'wdfs', 'asdfsd@asdfa.com', '0', 'DNG', '2020-05-07 13:10:46', 'DNG', '2020-06-30 13:53:07', '0', '1', NULL, NULL, 'D:\\testReadFile', 'D:\\testReadFile'),
(3, 'CMA', '1asd', 'asd', 'asdfa@asdf.com', '0', 'DNG', '2020-05-07 13:11:30', 'DNG', '2020-06-30 13:53:15', '0', '1', NULL, NULL, 'D:\\testReadFile', 'D:\\testReadFile');

-- --------------------------------------------------------

--
-- Table structure for table `driver_account`
--

CREATE TABLE `driver_account` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `logistic_group_id` bigint(20) DEFAULT NULL COMMENT 'Logistic Group',
  `mobile_number` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'So DT',
  `full_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ho va Ten',
  `identify_card_no` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'CMND',
  `password` varchar(64) COLLATE utf8_bin NOT NULL COMMENT 'Mat Khau',
  `salt` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'Salt',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Trạng thái khóa (default 0)',
  `del_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT 'Delete Flag',
  `valid_date` datetime DEFAULT NULL COMMENT 'Hieu Luc Den',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Driver login info';

--
-- Dumping data for table `driver_account`
--

INSERT INTO `driver_account` (`id`, `logistic_group_id`, `mobile_number`, `full_name`, `identify_card_no`, `password`, `salt`, `status`, `del_flag`, `valid_date`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
(1, 2, '1231231231', 'Nguyễn Trọng Hiếu', NULL, '4b45af63306e999b4a999e304ac27fac', '413b85', '1', b'0', '2020-11-13 00:00:00', NULL, 'Nguyễn Trọng Hiếu', '2020-05-22 23:19:25', NULL, NULL),
(2, 2, '1231231235', 'asdfasdfsadf', NULL, 'c0749c0de9886b4bc8d09e3f52b23a28', '7da3b7', '1', b'0', '2020-10-08 00:00:00', NULL, 'Nguyễn Trọng Hiếu', '2020-05-22 23:19:52', NULL, NULL),
(3, 2, '1231231236', 'afasfasfsadf', NULL, '59b27d5fb719688d1a52b2436fd632af', '8342ee', '1', b'0', '2020-12-25 00:00:00', NULL, 'Nguyễn Trọng Hiếu', '2020-05-22 23:20:14', NULL, NULL),
(4, 2, '4344343412', 'Nguyễn Trọng Hiếu', NULL, 'dba14101442756b7c0dd7363fe06ac95', '648b38', '1', b'0', '2020-08-22 00:00:00', NULL, 'Nguyễn Trọng Hiếu', '2020-05-22 23:20:35', NULL, NULL),
(5, 2, '43423412312', 'Nguyễn Trọng Hiếu', NULL, '893e5085fcc574779cd549c16fa1b251', 'd68cba', '1', b'0', '2020-07-31 00:00:00', NULL, 'Nguyễn Trọng Hiếu', '2020-05-22 23:20:56', NULL, NULL),
(9, 1, '12123123123', 'asfasdf', NULL, '08ffe2f36b2a2442641e895fccca49fe', '3b6b7e', '1', b'0', '2020-06-05 19:00:14', NULL, 'nguyen trong hieu', '2020-05-29 19:00:14', NULL, NULL),
(10, 1, '1231231223', 'adsfasdf', NULL, 'c613a521ef633d5b038ce837515c4c13', 'e6fb6c', '1', b'0', '2020-06-05 19:00:14', NULL, 'nguyen trong hieu', '2020-05-29 19:00:14', NULL, NULL),
(11, 1, '1231231231', 'sadfasf', NULL, '1db6a189f3d6a823370ac14d9dec9d2d', '3cf305', '1', b'0', '2020-06-06 07:01:35', NULL, 'nguyen trong hieu', '2020-05-30 07:01:35', NULL, NULL),
(12, 1, '1231231231', 'asdfasdf', NULL, 'dd183823ae510665d9abad93c493ca5a', '8ac0fc', '1', b'0', '2020-06-06 07:01:35', NULL, 'nguyen trong hieu', '2020-05-30 07:01:35', NULL, NULL),
(13, 1, '1231231231', 'ádà', NULL, 'df9e24ec0c86fa75f1eb87b2bb8d0a6f', '4e23c1', '1', b'0', '2020-06-06 07:20:35', NULL, 'nguyen trong hieu', '2020-05-30 07:20:35', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `driver_truck`
--

CREATE TABLE `driver_truck` (
  `driver_id` bigint(20) NOT NULL COMMENT 'ID tài xế',
  `truck_id` bigint(20) NOT NULL COMMENT 'truck_id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='driver_truck';

-- --------------------------------------------------------

--
-- Table structure for table `edo`
--

CREATE TABLE `edo` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
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
  `transaction_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Transaction id',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `del_flg` bit(1) DEFAULT NULL COMMENT 'Delete Flag',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Exchange Delivery Order' ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- Table structure for table `edo_audit_log`
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

-- --------------------------------------------------------

--
-- Table structure for table `edo_history`
--

CREATE TABLE `edo_history` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `carrier_id` bigint(20) NOT NULL COMMENT 'ID Nhan Vien Hang Tau',
  `carrier_code` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'Ma Hang Tau',
  `edo_id` bigint(20) NOT NULL COMMENT 'EDO ID',
  `order_number` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'So Lenh (Optional)',
  `bill_of_lading` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'So B/L',
  `container_number` varchar(11) COLLATE utf8_bin NOT NULL COMMENT 'So Cont',
  `action` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Action(insert,update, delete)',
  `edi_content` text COLLATE utf8_bin DEFAULT NULL,
  `file_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ten file EDI',
  `create_source` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Nguồi File EDI',
  `send_mail_flag` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT ' 0: chua send mail, 1 : da send mail',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='eDO Action History' ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- Table structure for table `equipment_do`
--

CREATE TABLE `equipment_do` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
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
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Exchange Delivery Order';

-- --------------------------------------------------------

--
-- Table structure for table `gen_table`
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
-- Dumping data for table `gen_table`
--

INSERT INTO `gen_table` (`table_id`, `table_name`, `table_comment`, `class_name`, `tpl_category`, `package_name`, `module_name`, `business_name`, `function_name`, `function_author`, `options`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(2, 'sys_post', 'Job Information Sheet', 'SysPost', 'crud', 'vn.com.irtech.eport.system', 'system', 'post', 'Job Information Sheet', 'ruoyi', NULL, 'admin', '2020-03-30 00:54:19', '', NULL, NULL),
(3, 'process_order', 'Process order', 'ProcessOrder', 'crud', 'vn.com.irtech.eport.system', 'system', 'order', 'Process order', 'ruoyi', NULL, 'admin', '2020-06-23 10:57:21', '', NULL, NULL),
(4, 'process_history', 'Process order history', 'ProcessHistory', 'crud', 'vn.com.irtech.eport.logistic', 'logistic', 'history', 'Process order history', 'ruoyi', '{\"treeName\":\"\",\"treeParentCode\":\"\",\"treeCode\":\"\"}', 'admin', '2020-06-27 10:23:52', '', '2020-06-27 10:28:04', ''),
(5, 'pickup_assign', 'Pickup Assign', 'PickupAssign', 'crud', 'vn.com.irtech.eport.logistic', 'logistic', 'assign', 'Pickup Assign', 'ruoyi', '{\"treeName\":\"\",\"treeParentCode\":\"\",\"treeCode\":\"\"}', 'admin', '2020-06-30 13:41:24', '', '2020-06-30 13:42:16', ''),
(6, 'pickup_hisory', 'Pickup history', 'PickupHisory', 'crud', 'vn.com.irtech.eport.logistic', 'logistic', 'hisory', 'Pickup history', 'ruoyi', '{\"treeName\":\"\",\"treeParentCode\":\"\",\"treeCode\":\"\"}', 'admin', '2020-06-30 13:41:24', '', '2020-06-30 13:42:53', ''),
(7, 'notifications', '', 'Notifications', 'crud', 'vn.com.irtech.eport.system', 'system', 'notifications', NULL, 'ruoyi', NULL, 'admin', '2020-07-06 14:08:49', '', NULL, NULL),
(8, 'notification_receiver', '', 'NotificationReceiver', 'crud', 'vn.com.irtech.eport.system', 'system', 'receiver', NULL, 'ruoyi', NULL, 'admin', '2020-07-06 14:08:49', '', NULL, NULL),
(9, 'user_devices', '', 'UserDevices', 'crud', 'vn.com.irtech.eport.system', 'system', 'devices', NULL, 'ruoyi', NULL, 'admin', '2020-07-06 14:08:49', '', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `gen_table_column`
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
-- Dumping data for table `gen_table_column`
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
(12, '2', 'remark', 'Remark', 'varchar(500)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'textarea', '', 10, 'admin', '2020-03-30 00:54:19', '', NULL),
(13, '3', 'id', 'ID', 'bigint(20)', 'Long', 'id', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2020-06-23 10:57:22', '', NULL),
(14, '3', 'shipment_id', 'Mã Lô', 'bigint(20)', 'Long', 'shipmentId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2020-06-23 10:57:22', '', NULL),
(15, '3', 'service_type', 'Loại dịch vụ (bốc, hạ, gate)', 'tinyint(1)', 'Integer', 'serviceType', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'select', '', 3, 'admin', '2020-06-23 10:57:22', '', NULL),
(16, '3', 'reference_no', 'Mã Tham Chiếu', 'varchar(64)', 'String', 'referenceNo', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2020-06-23 10:57:22', '', NULL),
(17, '3', 'pay_type', 'PT thanh toán', 'varchar(10)', 'String', 'payType', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'select', '', 5, 'admin', '2020-06-23 10:57:22', '', NULL),
(18, '3', 'sztp', 'Kích thước cont', 'varchar(10)', 'String', 'sztp', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2020-06-23 10:57:22', '', NULL),
(19, '3', 'mode', 'Loại lệnh', 'varchar(50)', 'String', 'mode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2020-06-23 10:57:22', '', NULL),
(20, '3', 'consignee', 'Chủ hàng', 'varchar(255)', 'String', 'consignee', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2020-06-23 10:57:22', '', NULL),
(21, '3', 'truck_co', 'MST-Tên cty', 'varchar(255)', 'String', 'truckCo', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2020-06-23 10:57:22', '', NULL),
(22, '3', 'tax_code', 'MST', 'varchar(15)', 'String', 'taxCode', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 10, 'admin', '2020-06-23 10:57:22', '', NULL),
(23, '3', 'bl_no', 'Billing No', 'varchar(20)', 'String', 'blNo', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 11, 'admin', '2020-06-23 10:57:22', '', NULL),
(24, '3', 'booking_no', 'Booking no', 'varchar(20)', 'String', 'bookingNo', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 12, 'admin', '2020-06-23 10:57:22', '', NULL),
(25, '3', 'pickup_date', 'Ngày bốc', 'datetime', 'Date', 'pickupDate', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'datetime', '', 13, 'admin', '2020-06-23 10:57:22', '', NULL),
(26, '3', 'vessel', 'Tàu', 'varchar(10)', 'String', 'vessel', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 14, 'admin', '2020-06-23 10:57:22', '', NULL),
(27, '3', 'voyage', 'Chuyến', 'varchar(10)', 'String', 'voyage', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 15, 'admin', '2020-06-23 10:57:22', '', NULL),
(28, '3', 'before_after', 'Trước-Sau', 'varchar(10)', 'String', 'beforeAfter', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 16, 'admin', '2020-06-23 10:57:22', '', NULL),
(29, '3', 'year', 'Năm', 'varchar(10)', 'String', 'year', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 17, 'admin', '2020-06-23 10:57:22', '', NULL),
(30, '3', 'cont_number', 'Số lượng container', 'int(10)', 'Integer', 'contNumber', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 18, 'admin', '2020-06-23 10:57:22', '', NULL),
(31, '3', 'status', 'Trạng thái: 0 waiting, 1: processing, 2:done', 'tinyint(1)', 'Integer', 'status', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'radio', '', 19, 'admin', '2020-06-23 10:57:22', '', NULL),
(32, '3', 'result', 'Kết quả (F:Failed,S:Success)', 'char(1)', 'String', 'result', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 20, 'admin', '2020-06-23 10:57:22', '', NULL),
(33, '3', 'data', 'Detail Data (Json)', 'varchar(1024)', 'String', 'data', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'textarea', '', 21, 'admin', '2020-06-23 10:57:22', '', NULL),
(34, '3', 'create_by', 'Create By', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 22, 'admin', '2020-06-23 10:57:22', '', NULL),
(35, '3', 'create_time', 'Create Time', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 23, 'admin', '2020-06-23 10:57:22', '', NULL),
(36, '3', 'update_by', 'Update By', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 24, 'admin', '2020-06-23 10:57:22', '', NULL),
(37, '3', 'update_time', 'Update Time', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 25, 'admin', '2020-06-23 10:57:22', '', NULL),
(38, '4', 'id', 'ID', 'bigint(20)', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2020-06-27 10:23:52', NULL, '2020-06-27 10:28:05'),
(39, '4', 'process_order_id', 'Process Order ID', 'bigint(20)', 'Long', 'processOrderId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2020-06-27 10:23:52', NULL, '2020-06-27 10:28:05'),
(40, '4', 'sys_user_id', 'User ID (OM)', 'bigint(20)', 'Long', 'sysUserId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2020-06-27 10:23:52', NULL, '2020-06-27 10:28:05'),
(41, '4', 'robot_uuid', 'Robot UUID', 'varchar(64)', 'String', 'robotUuid', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2020-06-27 10:23:52', NULL, '2020-06-27 10:28:05'),
(42, '4', 'result', 'Kết Quả (S:Success, F:Failed)', 'char(1)', 'String', 'result', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2020-06-27 10:23:52', NULL, '2020-06-27 10:28:05'),
(43, '4', 'remark', 'Ghi chu', 'varchar(255)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'input', '', 6, 'admin', '2020-06-27 10:23:52', NULL, '2020-06-27 10:28:05'),
(44, '4', 'create_by', 'Create By', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 7, 'admin', '2020-06-27 10:23:52', NULL, '2020-06-27 10:28:05'),
(45, '4', 'create_time', 'Create Time', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2020-06-27 10:23:52', NULL, '2020-06-27 10:28:05'),
(46, '4', 'update_by', 'Update By', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 9, 'admin', '2020-06-27 10:23:52', NULL, '2020-06-27 10:28:05'),
(47, '4', 'update_time', 'Update Time', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 10, 'admin', '2020-06-27 10:23:52', NULL, '2020-06-27 10:28:05'),
(48, '5', 'id', 'ID', 'bigint(20)', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:16'),
(49, '5', 'logistic_group_id', 'Logistic Group', 'bigint(20)', 'Long', 'logisticGroupId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:16'),
(50, '5', 'shipment_id', 'Ma Lo', 'bigint(20)', 'Long', 'shipmentId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:16'),
(51, '5', 'driver_id', 'ID tài xế', 'bigint(20)', 'Long', 'driverId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:16'),
(52, '5', 'shipment_detail_id', 'Shipment Detail Id', 'bigint(20)', 'Long', 'shipmentDetailId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:16'),
(53, '5', 'external_flg', 'Thue ngoai (0,1)', 'bit(1)', 'Long', 'externalFlg', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:16'),
(54, '5', 'external_secret_code', 'Mã nhận lệnh thuê ngoài', 'varchar(15)', 'String', 'externalSecretCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:16'),
(55, '5', 'truck_no', 'Biển số xe đầu kéo (thuê ngoài)', 'varchar(10)', 'String', 'truckNo', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:16'),
(56, '5', 'chassis_no', 'Biển số xe rơ mooc (thuê ngoài)', 'varchar(10)', 'String', 'chassisNo', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:16'),
(57, '5', 'remark', 'Ghi chu', 'varchar(255)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'input', '', 10, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:16'),
(58, '5', 'create_by', 'Create By', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 11, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:16'),
(59, '5', 'create_time', 'Create Time', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 12, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:16'),
(60, '5', 'update_by', 'Update By', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 13, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:16'),
(61, '5', 'update_time', 'Update Time', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 14, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:16'),
(62, '6', 'id', 'ID', 'bigint(20)', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:53'),
(63, '6', 'logistic_group_id', 'Logistic Group', 'bigint(20)', 'Long', 'logisticGroupId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:53'),
(64, '6', 'shipment_id', 'Mã Lô', 'bigint(20)', 'Long', 'shipmentId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:53'),
(65, '6', 'driver_id', 'ID Tài xế', 'bigint(20)', 'Long', 'driverId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:53'),
(66, '6', 'pickup_assign_id', 'Assign ID', 'bigint(20)', 'Long', 'pickupAssignId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:53'),
(67, '6', 'container_no', 'Số container', 'varchar(12)', 'String', 'containerNo', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:53'),
(68, '6', 'truck_no', 'Biển số xe đầu kéo', 'varchar(15)', 'String', 'truckNo', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:53'),
(69, '6', 'chassis_no', 'Biển số xe rơ mooc', 'varchar(15)', 'String', 'chassisNo', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:53'),
(70, '6', 'yard_position', 'Tọa độ cont trên bãi', 'varchar(20)', 'String', 'yardPosition', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:53'),
(71, '6', 'status', 'Trạng thái (0:received, 1:planned, 2:gate-in, 3: gate-out)', 'tinyint(3)', 'Integer', 'status', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'radio', '', 10, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:53'),
(72, '6', 'receipt_date', 'Ngày nhận lệnh', 'datetime', 'Date', 'receiptDate', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'datetime', '', 11, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:53'),
(73, '6', 'gatein_date', 'Ngày vào cổng', 'datetime', 'Date', 'gateinDate', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'datetime', '', 12, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:53'),
(74, '6', 'gateout_date', 'Ngày ra cổng', 'datetime', 'Date', 'gateoutDate', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'datetime', '', 13, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:53'),
(75, '6', 'cancel_receipt_date', 'Ngày hủy lệnh', 'datetime', 'Date', 'cancelReceiptDate', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'datetime', '', 14, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:53'),
(76, '6', 'remark', 'Ghi chu', 'varchar(255)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'input', '', 15, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:53'),
(77, '6', 'create_by', 'Create By', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 16, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:53'),
(78, '6', 'create_time', 'Create Time', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 17, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:53'),
(79, '6', 'update_by', 'Update By', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 18, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:53'),
(80, '6', 'update_time', 'Update Time', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 19, 'admin', '2020-06-30 13:41:24', NULL, '2020-06-30 13:42:53'),
(81, '7', 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2020-07-06 14:08:49', '', NULL),
(82, '7', 'title', NULL, 'varchar(255)', 'String', 'title', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2020-07-06 14:08:49', '', NULL),
(83, '7', 'content', NULL, 'text', 'String', 'content', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2020-07-06 14:08:49', '', NULL),
(84, '7', 'create_time', NULL, 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 4, 'admin', '2020-07-06 14:08:49', '', NULL),
(85, '7', 'create_by', NULL, 'varchar(50)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 5, 'admin', '2020-07-06 14:08:49', '', NULL),
(86, '7', 'update_time', NULL, 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2020-07-06 14:08:49', '', NULL),
(87, '7', 'update_by', NULL, 'varchar(50)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 7, 'admin', '2020-07-06 14:08:49', '', NULL),
(88, '8', 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2020-07-06 14:08:49', '', NULL),
(89, '8', 'notification_id', NULL, 'int(11)', 'Long', 'notificationId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2020-07-06 14:08:49', '', NULL),
(90, '8', 'user_device_id', NULL, 'int(11)', 'Long', 'userDeviceId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2020-07-06 14:08:49', '', NULL),
(91, '8', 'create_time', NULL, 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 4, 'admin', '2020-07-06 14:08:49', '', NULL),
(92, '8', 'create_by', NULL, 'datetime', 'Date', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 5, 'admin', '2020-07-06 14:08:49', '', NULL),
(93, '8', 'update_time', NULL, 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2020-07-06 14:08:49', '', NULL),
(94, '8', 'update_by', NULL, 'datetime', 'Date', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2020-07-06 14:08:49', '', NULL),
(95, '9', 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2020-07-06 14:08:49', '', NULL),
(96, '9', 'user_token', NULL, 'varchar(100)', 'String', 'userToken', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2020-07-06 14:08:49', '', NULL),
(97, '9', 'device_token', NULL, 'varchar(100)', 'String', 'deviceToken', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2020-07-06 14:08:49', '', NULL),
(98, '9', 'user_type', '1: Logistic, 2: Driver, 3: Staff', 'int(11)', 'Long', 'userType', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'select', '', 4, 'admin', '2020-07-06 14:08:49', '', NULL),
(99, '9', 'create_time', NULL, 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 5, 'admin', '2020-07-06 14:08:49', '', NULL),
(100, '9', 'create_by', NULL, 'varchar(50)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 6, 'admin', '2020-07-06 14:08:49', '', NULL),
(101, '9', 'update_time', NULL, 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2020-07-06 14:08:49', '', NULL),
(102, '9', 'update_by', NULL, 'varchar(50)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 8, 'admin', '2020-07-06 14:08:49', '', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `logistic_account`
--

CREATE TABLE `logistic_account` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
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
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Logistic account';

--
-- Dumping data for table `logistic_account`
--

INSERT INTO `logistic_account` (`id`, `group_id`, `user_name`, `email`, `password`, `salt`, `full_name`, `status`, `del_flag`, `login_ip`, `login_date`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
(1, 1, 'mst123123', 'asdfasd@sadfs.com', '054d07e1dee07d685e989fbede83d06b', '0e440a', 'nguyen trong hieu', '0', '0', '127.0.0.1', '2020-07-09 10:23:49', NULL, 'DNG', '2020-05-28 13:50:26', '', '2020-07-09 10:23:49'),
(2, 1, 'hieu123', 'tronghieu8531@gmail.com', '7ca885ab49c72cd10992eff49acbfd45', '7373e1', 'sdfas', '0', '0', '', NULL, NULL, 'DNG', '2020-06-26 18:45:54', '', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `logistic_group`
--

CREATE TABLE `logistic_group` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
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
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Logistic Group';

--
-- Dumping data for table `logistic_group`
--

INSERT INTO `logistic_group` (`id`, `group_name`, `email_address`, `address`, `mst`, `phone`, `mobile_phone`, `credit_flag`, `fax`, `del_flag`, `business_registration_certificate`, `date_of_issue_registration`, `place_of_issue_registration`, `authorized_representative`, `representative_position`, `following_authorization_form_no`, `sign_date`, `owned`, `identify_card_no`, `date_of_issue_identify`, `place_of_issue_identify`, `email`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
(1, 'Vinconship', 'email@email.com', '35 Cao Thang', '123123123123', '0541123412341', '0935802290', '1', '1234234', '0', 'asdfsadf', '2020-05-28 00:00:00', 'asdfsadf', 'asdfsdaf', 'asdfsadf', 'asdfas', '2020-05-29 00:00:00', 'asdfasdf', '12341231231', '2020-05-14 00:00:00', 'asdfasdf', 'tronghieu8531@gmail.com', '', '2020-05-28 13:49:52', '', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `logistic_truck`
--

CREATE TABLE `logistic_truck` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `logistic_group_id` bigint(20) DEFAULT NULL COMMENT 'Logistic Group',
  `plate_number` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Bien So Xe',
  `type` char(1) COLLATE utf8_bin NOT NULL COMMENT '0: đầu kéo, 1: rơ mooc',
  `wgt` int(11) NOT NULL COMMENT 'Tải trọng',
  `registry_expiry_date` datetime DEFAULT NULL COMMENT 'Hạn đăng kiểm',
  `del_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT 'Delete Flag(default 0)',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Logistics Truck';

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

CREATE TABLE `notifications` (
  `id` int(11) NOT NULL,
  `title` varchar(255) COLLATE utf8_bin NOT NULL,
  `content` text COLLATE utf8_bin NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(50) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `notification_receiver`
--

CREATE TABLE `notification_receiver` (
  `id` int(11) NOT NULL,
  `notification_id` int(11) NOT NULL,
  `user_device_id` int(11) NOT NULL,
  `seen_flg` tinyint(1) DEFAULT 0 COMMENT 'Trạng thái: 0 unseen, 1 seen',
  `create_time` datetime DEFAULT NULL,
  `create_by` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `pickup_assign`
--

CREATE TABLE `pickup_assign` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
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
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Pickup Assign';

-- --------------------------------------------------------

--
-- Table structure for table `pickup_history`
--

CREATE TABLE `pickup_history` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `logistic_group_id` bigint(20) NOT NULL COMMENT 'Logistic Group',
  `shipment_id` bigint(20) NOT NULL COMMENT 'Mã Lô',
  `shipment_detail_id_1` bigint(20) DEFAULT NULL COMMENT 'Shipment Detail Id 1',
  `shipment_detail_id_2` bigint(20) DEFAULT NULL COMMENT 'Shipment Detail Id 2',
  `driver_id` bigint(20) NOT NULL COMMENT 'ID Tài xế',
  `pickup_assign_id` bigint(20) NOT NULL COMMENT 'Assign ID',
  `container_no_1` varchar(12) COLLATE utf8_bin DEFAULT NULL COMMENT 'Số container 1',
  `container_no_2` varchar(12) COLLATE utf8_bin DEFAULT NULL COMMENT 'Số container 2',
  `truck_no` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Biển số xe đầu kéo',
  `chassis_no` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Biển số xe rơ mooc',
  `yard_position_1` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'Tọa độ cont 1 trên bãi',
  `yard_position_2` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'Tọa độ cont 2 trên bãi',
  `status` tinyint(4) DEFAULT 0 COMMENT 'Trạng thái (0:received, 1:planned, 2:gate-in, 3: gate-out)',
  `receipt_date` datetime DEFAULT NULL COMMENT 'Ngày nhận lệnh',
  `gatein_date` datetime DEFAULT NULL COMMENT 'Ngày vào cổng',
  `gateout_date` datetime DEFAULT NULL COMMENT 'Ngày ra cổng',
  `cancel_receipt_date` datetime DEFAULT NULL COMMENT 'Ngày hủy lệnh',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Pickup history';

-- --------------------------------------------------------

--
-- Table structure for table `process_bill`
--

CREATE TABLE `process_bill` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `shipment_id` bigint(20) NOT NULL COMMENT 'Mã Lô',
  `logistic_group_id` bigint(20) NOT NULL COMMENT 'Mã logistic',
  `process_order_id` bigint(20) NOT NULL COMMENT 'Process Order ID',
  `service_type` tinyint(1) NOT NULL COMMENT 'Loại dịch vụ (bốc, hạ, gate)',
  `pay_type` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'PT thanh toán',
  `payment_status` varchar(1) COLLATE utf8_bin NOT NULL COMMENT 'Payment Status (Y,N)',
  `payment_time` datetime DEFAULT NULL COMMENT 'Payment Time',
  `reference_no` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Mã tham chiếu',
  `sztp` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'Size Type',
  `exchange_fee` bigint(20) DEFAULT NULL COMMENT 'Phí giao nhận',
  `vat_rate` int(5) DEFAULT NULL COMMENT 'tỉ lệ % thuế vat',
  `vat_after_fee` bigint(20) DEFAULT NULL COMMENT 'phí sau thuế vat',
  `container_no` varchar(12) COLLATE utf8_bin DEFAULT NULL COMMENT 'số container',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Process billing';

-- --------------------------------------------------------

--
-- Table structure for table `process_history`
--

CREATE TABLE `process_history` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `process_order_id` bigint(20) NOT NULL COMMENT 'Process Order ID',
  `sys_user_id` bigint(20) DEFAULT NULL COMMENT 'User ID (OM)',
  `robot_uuid` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Robot UUID',
  `result` char(1) COLLATE utf8_bin NOT NULL COMMENT 'Kết Quả (S:Success, F:Failed)',
  `status` tinyint(1) DEFAULT NULL COMMENT 'Trạng thái: 1 start, 2 finish',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Process order history';

-- --------------------------------------------------------

--
-- Table structure for table `process_order`
--

CREATE TABLE `process_order` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `shipment_id` bigint(20) NOT NULL COMMENT 'Mã Lô',
  `logistic_group_id` bigint(20) NOT NULL COMMENT 'Mã logistic',
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
  `status` tinyint(1) DEFAULT 0 COMMENT 'Trạng thái: 0 waiting, 1: processing, 2:done',
  `result` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Kết quả (F:Failed,S:Success)',
  `data` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT 'Detail Data (Json)',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Process order';

-- --------------------------------------------------------

--
-- Table structure for table `qrtz_blob_triggers`
--

CREATE TABLE `qrtz_blob_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `blob_data` blob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `qrtz_calendars`
--

CREATE TABLE `qrtz_calendars` (
  `sched_name` varchar(120) NOT NULL,
  `calendar_name` varchar(200) NOT NULL,
  `calendar` blob NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `qrtz_cron_triggers`
--

CREATE TABLE `qrtz_cron_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `cron_expression` varchar(200) NOT NULL,
  `time_zone_id` varchar(80) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `qrtz_cron_triggers`
--

INSERT INTO `qrtz_cron_triggers` (`sched_name`, `trigger_name`, `trigger_group`, `cron_expression`, `time_zone_id`) VALUES
('RuoyiScheduler', 'TASK_CLASS_NAME1', 'DEFAULT', '0/10 * * * * ?', 'Asia/Barnaul'),
('RuoyiScheduler', 'TASK_CLASS_NAME2', 'DEFAULT', '0/15 * * * * ?', 'Asia/Barnaul'),
('RuoyiScheduler', 'TASK_CLASS_NAME3', 'DEFAULT', '0/20 * * * * ?', 'Asia/Barnaul');

-- --------------------------------------------------------

--
-- Table structure for table `qrtz_fired_triggers`
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
-- Table structure for table `qrtz_job_details`
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

--
-- Dumping data for table `qrtz_job_details`
--

INSERT INTO `qrtz_job_details` (`sched_name`, `job_name`, `job_group`, `description`, `job_class_name`, `is_durable`, `is_nonconcurrent`, `is_update_data`, `requests_recovery`, `job_data`) VALUES
('RuoyiScheduler', 'TASK_CLASS_NAME1', 'DEFAULT', NULL, 'vn.com.irtech.eport.quartz.util.QuartzDisallowConcurrentExecution', '0', '1', '0', '0', 0xaced0005737200156f72672e71756172747a2e4a6f62446174614d61709fb083e8bfa9b0cb020000787200266f72672e71756172747a2e7574696c732e537472696e674b65794469727479466c61674d61708208e8c3fbc55d280200015a0013616c6c6f77735472616e7369656e74446174617872001d6f72672e71756172747a2e7574696c732e4469727479466c61674d617013e62ead28760ace0200025a000564697274794c00036d617074000f4c6a6176612f7574696c2f4d61703b787001737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c7708000000100000000174000f5441534b5f50524f5045525449455373720028766e2e636f6d2e6972746563682e65706f72742e71756172747a2e646f6d61696e2e5379734a6f6200000000000000010200084c000a636f6e63757272656e747400124c6a6176612f6c616e672f537472696e673b4c000e63726f6e45787072657373696f6e71007e00094c000c696e766f6b6554617267657471007e00094c00086a6f6247726f757071007e00094c00056a6f6249647400104c6a6176612f6c616e672f4c6f6e673b4c00076a6f624e616d6571007e00094c000d6d697366697265506f6c69637971007e00094c000673746174757371007e000978720031766e2e636f6d2e6972746563682e65706f72742e636f6d6d6f6e2e636f72652e646f6d61696e2e42617365456e7469747900000000000000010200074c0008637265617465427971007e00094c000a63726561746554696d657400104c6a6176612f7574696c2f446174653b4c0006706172616d7371007e00034c000672656d61726b71007e00094c000b73656172636856616c756571007e00094c0008757064617465427971007e00094c000a75706461746554696d6571007e000c787074000561646d696e7372000e6a6176612e7574696c2e44617465686a81014b59741903000078707708000001622d15186078707400007070707400013174000e302f3130202a202a202a202a203f74001172795461736b2e72794e6f506172616d7374000744454641554c547372000e6a6176612e6c616e672e4c6f6e673b8be490cc8f23df0200014a000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b02000078700000000000000001740018e7b3bbe7bb9fe9bb98e8aea4efbc88e697a0e58f82efbc8974000133740001317800),
('RuoyiScheduler', 'TASK_CLASS_NAME2', 'DEFAULT', NULL, 'vn.com.irtech.eport.quartz.util.QuartzDisallowConcurrentExecution', '0', '1', '0', '0', 0xaced0005737200156f72672e71756172747a2e4a6f62446174614d61709fb083e8bfa9b0cb020000787200266f72672e71756172747a2e7574696c732e537472696e674b65794469727479466c61674d61708208e8c3fbc55d280200015a0013616c6c6f77735472616e7369656e74446174617872001d6f72672e71756172747a2e7574696c732e4469727479466c61674d617013e62ead28760ace0200025a000564697274794c00036d617074000f4c6a6176612f7574696c2f4d61703b787001737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c7708000000100000000174000f5441534b5f50524f5045525449455373720028766e2e636f6d2e6972746563682e65706f72742e71756172747a2e646f6d61696e2e5379734a6f6200000000000000010200084c000a636f6e63757272656e747400124c6a6176612f6c616e672f537472696e673b4c000e63726f6e45787072657373696f6e71007e00094c000c696e766f6b6554617267657471007e00094c00086a6f6247726f757071007e00094c00056a6f6249647400104c6a6176612f6c616e672f4c6f6e673b4c00076a6f624e616d6571007e00094c000d6d697366697265506f6c69637971007e00094c000673746174757371007e000978720031766e2e636f6d2e6972746563682e65706f72742e636f6d6d6f6e2e636f72652e646f6d61696e2e42617365456e7469747900000000000000010200074c0008637265617465427971007e00094c000a63726561746554696d657400104c6a6176612f7574696c2f446174653b4c0006706172616d7371007e00034c000672656d61726b71007e00094c000b73656172636856616c756571007e00094c0008757064617465427971007e00094c000a75706461746554696d6571007e000c787074000561646d696e7372000e6a6176612e7574696c2e44617465686a81014b59741903000078707708000001622d15186078707400007070707400013174000e302f3135202a202a202a202a203f74001572795461736b2e7279506172616d7328277279272974000744454641554c547372000e6a6176612e6c616e672e4c6f6e673b8be490cc8f23df0200014a000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b02000078700000000000000002740018e7b3bbe7bb9fe9bb98e8aea4efbc88e69c89e58f82efbc8974000133740001317800),
('RuoyiScheduler', 'TASK_CLASS_NAME3', 'DEFAULT', NULL, 'vn.com.irtech.eport.quartz.util.QuartzDisallowConcurrentExecution', '0', '1', '0', '0', 0xaced0005737200156f72672e71756172747a2e4a6f62446174614d61709fb083e8bfa9b0cb020000787200266f72672e71756172747a2e7574696c732e537472696e674b65794469727479466c61674d61708208e8c3fbc55d280200015a0013616c6c6f77735472616e7369656e74446174617872001d6f72672e71756172747a2e7574696c732e4469727479466c61674d617013e62ead28760ace0200025a000564697274794c00036d617074000f4c6a6176612f7574696c2f4d61703b787001737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c7708000000100000000174000f5441534b5f50524f5045525449455373720028766e2e636f6d2e6972746563682e65706f72742e71756172747a2e646f6d61696e2e5379734a6f6200000000000000010200084c000a636f6e63757272656e747400124c6a6176612f6c616e672f537472696e673b4c000e63726f6e45787072657373696f6e71007e00094c000c696e766f6b6554617267657471007e00094c00086a6f6247726f757071007e00094c00056a6f6249647400104c6a6176612f6c616e672f4c6f6e673b4c00076a6f624e616d6571007e00094c000d6d697366697265506f6c69637971007e00094c000673746174757371007e000978720031766e2e636f6d2e6972746563682e65706f72742e636f6d6d6f6e2e636f72652e646f6d61696e2e42617365456e7469747900000000000000010200074c0008637265617465427971007e00094c000a63726561746554696d657400104c6a6176612f7574696c2f446174653b4c0006706172616d7371007e00034c000672656d61726b71007e00094c000b73656172636856616c756571007e00094c0008757064617465427971007e00094c000a75706461746554696d6571007e000c787074000561646d696e7372000e6a6176612e7574696c2e44617465686a81014b59741903000078707708000001622d15186078707400007070707400013174000e302f3230202a202a202a202a203f74003872795461736b2e72794d756c7469706c65506172616d7328277279272c20747275652c20323030304c2c203331362e3530442c203130302974000744454641554c547372000e6a6176612e6c616e672e4c6f6e673b8be490cc8f23df0200014a000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b02000078700000000000000003740018e7b3bbe7bb9fe9bb98e8aea4efbc88e5a49ae58f82efbc8974000133740001317800);

-- --------------------------------------------------------

--
-- Table structure for table `qrtz_locks`
--

CREATE TABLE `qrtz_locks` (
  `sched_name` varchar(120) NOT NULL,
  `lock_name` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `qrtz_locks`
--

INSERT INTO `qrtz_locks` (`sched_name`, `lock_name`) VALUES
('RuoyiScheduler', 'STATE_ACCESS'),
('RuoyiScheduler', 'TRIGGER_ACCESS');

-- --------------------------------------------------------

--
-- Table structure for table `qrtz_paused_trigger_grps`
--

CREATE TABLE `qrtz_paused_trigger_grps` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_group` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `qrtz_scheduler_state`
--

CREATE TABLE `qrtz_scheduler_state` (
  `sched_name` varchar(120) NOT NULL,
  `instance_name` varchar(200) NOT NULL,
  `last_checkin_time` bigint(13) NOT NULL,
  `checkin_interval` bigint(13) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `qrtz_scheduler_state`
--

INSERT INTO `qrtz_scheduler_state` (`sched_name`, `instance_name`, `last_checkin_time`, `checkin_interval`) VALUES
('RuoyiScheduler', 'DESKTOP-0U94TFA1594267487348', 1594270582462, 15000);

-- --------------------------------------------------------

--
-- Table structure for table `qrtz_simple_triggers`
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
-- Table structure for table `qrtz_simprop_triggers`
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
-- Table structure for table `qrtz_triggers`
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

--
-- Dumping data for table `qrtz_triggers`
--

INSERT INTO `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`, `job_name`, `job_group`, `description`, `next_fire_time`, `prev_fire_time`, `priority`, `trigger_state`, `trigger_type`, `start_time`, `end_time`, `calendar_name`, `misfire_instr`, `job_data`) VALUES
('RuoyiScheduler', 'TASK_CLASS_NAME1', 'DEFAULT', 'TASK_CLASS_NAME1', 'DEFAULT', NULL, 1594267490000, -1, 5, 'PAUSED', 'CRON', 1594267487000, 0, NULL, 2, ''),
('RuoyiScheduler', 'TASK_CLASS_NAME2', 'DEFAULT', 'TASK_CLASS_NAME2', 'DEFAULT', NULL, 1594267500000, -1, 5, 'PAUSED', 'CRON', 1594267488000, 0, NULL, 2, ''),
('RuoyiScheduler', 'TASK_CLASS_NAME3', 'DEFAULT', 'TASK_CLASS_NAME3', 'DEFAULT', NULL, 1594267500000, -1, 5, 'PAUSED', 'CRON', 1594267488000, 0, NULL, 2, '');

-- --------------------------------------------------------

--
-- Table structure for table `shipment`
--

CREATE TABLE `shipment` (
  `id` bigint(20) NOT NULL,
  `logistic_account_id` bigint(20) NOT NULL,
  `logistic_group_id` bigint(20) NOT NULL,
  `service_type` tinyint(1) NOT NULL COMMENT 'Dich Vu',
  `bl_no` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `booking_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'Booking Number',
  `ope_code` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'Mã hãng tàu',
  `tax_code` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'MST',
  `group_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Tên cty theo MST',
  `container_amount` int(11) NOT NULL COMMENT 'So Luong Container',
  `edo_flg` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'EDO Flag (1,0)',
  `specific_cont_flg` tinyint(1) DEFAULT NULL COMMENT 'Trạng thái: 0 ko chỉ định cont, 1 có chỉ định cont',
  `cont_supply_status` tinyint(1) DEFAULT NULL COMMENT 'Trạng thái: 0 chưa chỉ định cont, đã chỉ định cont',
  `reference_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'So Tham Chieu CATOS',
  `status` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Shipment Status',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Shipment';

-- --------------------------------------------------------

--
-- Table structure for table `shipment_custom`
--

CREATE TABLE `shipment_custom` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `shipment_id` bigint(20) NOT NULL COMMENT 'Shipment ID',
  `custom_declare_no` varchar(12) COLLATE utf8_bin NOT NULL COMMENT 'Số Tờ Khai HQ',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Shipment custom: Hai quan';

-- --------------------------------------------------------

--
-- Table structure for table `shipment_detail`
--

CREATE TABLE `shipment_detail` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `logistic_group_id` bigint(20) NOT NULL,
  `shipment_id` bigint(20) NOT NULL COMMENT 'Ma Lo',
  `process_order_id` bigint(20) DEFAULT NULL COMMENT 'Ma Lenh',
  `register_no` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ma DK',
  `container_no` varchar(12) COLLATE utf8_bin DEFAULT NULL COMMENT 'Container Number',
  `container_status` varchar(3) COLLATE utf8_bin DEFAULT NULL COMMENT 'Container Status (S,D)',
  `sztp` varchar(4) COLLATE utf8_bin NOT NULL COMMENT 'Size Type',
  `fe` varchar(1) COLLATE utf8_bin NOT NULL COMMENT 'FE',
  `bl_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'BL number',
  `booking_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'Booking Number',
  `seal_no` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'Seal Number',
  `consignee` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Shipper/consignee',
  `expired_dem` datetime DEFAULT NULL COMMENT 'Han Lenh',
  `wgt` int(11) DEFAULT NULL COMMENT 'Weight ',
  `vsl_nm` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'Vessel name',
  `voy_no` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'Voyage',
  `ope_code` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'Operator Code',
  `loading_port` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Cang Xep Hang',
  `discharge_port` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Cang Do Hang',
  `transport_type` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'Phuong Tien',
  `empty_depot` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Noi Ha Vo',
  `cargo_type` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Cargo Type',
  `vgm_chk` bit(1) DEFAULT NULL COMMENT 'VGM Check',
  `vgm` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'VGM',
  `vgm_person_info` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT 'VGM Person Info',
  `preorder_pickup` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Boc Chi Dinh (Y,N)',
  `shifting_cont_number` int(5) DEFAULT NULL COMMENT 'Số lượng dịch chuyển',
  `custom_status` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Custom Status (N,C,H,R)',
  `payment_status` varchar(1) COLLATE utf8_bin NOT NULL COMMENT 'Payment Status (Y,N,W,E)',
  `process_status` varchar(1) COLLATE utf8_bin NOT NULL COMMENT 'Process Status(Y,N,E)',
  `do_status` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'T.T DO Goc',
  `do_received_time` datetime DEFAULT NULL COMMENT 'Ngay Nhan DO Goc',
  `user_verify_status` varchar(1) COLLATE utf8_bin NOT NULL COMMENT 'Xac Thuc (Y,N)',
  `status` tinyint(4) NOT NULL COMMENT 'Status',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Shipment Details';

-- --------------------------------------------------------

--
-- Table structure for table `sys_config`
--

CREATE TABLE `sys_config` (
  `config_id` int(5) NOT NULL COMMENT '参数主键',
  `config_name` varchar(100) COLLATE utf8_bin DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) COLLATE utf8_bin DEFAULT '' COMMENT '参数键名',
  `config_value` text COLLATE utf8_bin DEFAULT NULL,
  `config_type` char(1) COLLATE utf8_bin DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='参数配置表';

--
-- Dumping data for table `sys_config`
--

INSERT INTO `sys_config` (`config_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(1, 'Main Frame Page', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(2, 'User Init password', 'sys.user.initPassword', '123456', 'Y', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(3, 'Main Frame Page-Sidebar Theme', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(4, 'Enable User Register', 'sys.account.registerUser', 'false', 'Y', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(100, 'Da Nang Port Name', 'danang.port.name', 'Cảng Tiên Sa', 'Y', 'admin', '2020-06-30 11:22:55', 'admin', '2020-06-30 19:53:02', 'Tên cảng Đà Nãng'),
(101, 'Firebase Credential', 'firebase.credential', '{\r\n  \"type\": \"service_account\",\r\n  \"project_id\": \"eport-89c50\",\r\n  \"private_key_id\": \"bd327b559b106bd8b2480ed0b4c1f5d8f27de17b\",\r\n  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQD02alcYTopCglu\\nIoNAQ+Y+zos1BDIBtEH8sv3rSEDPUQeEke+HwOhEgB0sB+M2j55wAVtqgptYpLwj\\nDruoo/UDAeRqGhX4bkDTXl42zXiXoJmVmUIciZ/k4HzqJ40DT6DR7MtkfAdmiaCG\\n8E3U8OzUM4FLMIJfrp00RceNnInlw9OzJez2rbPSvHKIiAr9jtl9Hfpfx9piAcra\\nak3FCuQ+lMEjHibiVLFczHAnlDQKGJfTPpPQWZ2/UTBvLnXGl2HiACEVWrO6MZG/\\n3ig96G4NjOiIzQjeCeVrvWvXnwe3RkZxuC+RH4Hf+IyyZBoGAQ2NrGMAwD5b+69C\\nsrnCYDYDAgMBAAECggEAaR00ZvNai5nCeKSaYjGEG/yBqK33ZeHj3j9dYO+w2w+3\\nQYOBKG95h0bUvz1XnunDI+SBQVV2qXR3TDmb+IcCvVrHm4E9Y1y5ucQugCpvlfCd\\nXqrfxe6TVSfGmKOIFx7NNLIKk0Xny25UGvn4/+y6T1MZM5VMsxT8ah92zuWgEaHM\\nNnOSJNh42RXEa8DAwBcw0dDJ8ayb7chZEmYC1LUjMZubmtAcg8kJs2DIdX7U6ZNp\\nA05qin0qFSc/JoRC7geh9KmB1q2h3n0wzg93+0u6rfWbJyLbQr+kVpXkjhcqjmLK\\nHn3I2YeV6lGHQCREWry4+ss68gdqJluMnYcy3eOxIQKBgQD+sMIouSD2fmcBcDcg\\n0EIBaGJHogtjFTGNDe9ic+0cT3Pe5ybPUxwUu82/vhl8vjuPrC0P8ltaxy/1xcBe\\ndghdhSWpKH2Hv1KthcSLwxJAJU8c29wliQvyIwBvH6iJWjfv7/ZzKbtYixjiEPm+\\n0osUh0T3K94ZEfAkOY0XWXSicQKBgQD2G/NjfweR0CEElbd7qX4m/MLML9LHxJcJ\\nkqfEGlA8W6K7woifYPKgiaR5YPpvTSu0lxBeudh6NyV5waEthc0CMcie6/x1Tr6d\\nXvB5bsPRWunPmXy8X6zSri5ErsHZnw6R5LWKSvryIPy7jG2w6dcZESON4wBxk8SP\\n6X5KlesxswKBgFxCM1MFHMetqip/N7kPN5nC8jb4oB9YQgbSkXCchbvHnDWWjhx0\\nAqwQC8v1VM43KuQ0fm5UYHtVxC3HYJPXNdiKrsXEART3XT+2QShPlYDfAvV1Px3p\\nswYXX8ThNu/qWnDz/9Zfu5mraWwash1Jr0/UYEsY/O8f7Fly74URxopBAoGBAOhe\\nX6FTsRv1fRdNHN6/m4LIKEyN4uAHN+wr8gbwKU2z36ST+lcxPCRjkU2hSROJs3hh\\nIW2u3zwVkWaycbH/oR8vThLvEYDZBpSjrT2aXXzv7865RtK9KvoIx1rF/fWxfho1\\n7UpTnTi7+KRD5NWjFBpw2jb/W83hDTgr57gYcOaNAoGAEMnwVH+opj8pPkNnCeqR\\nBT0ur/NefDZ1XfJ9nscKAToQVQxSJjPc+4/KEhOMogWOUfUUjvB06SZ3yhjoT5gh\\nr7oNsd35OpiXEJtXSfAo6Kx7XSO61MgEaz4pj4fWuBVuPQzrdX4ZWrP209XrLKyK\\nWWK9bvx/QcpsU6V/YmTCY+E=\\n-----END PRIVATE KEY-----\\n\",\r\n  \"client_email\": \"firebase-adminsdk-mwrl6@eport-89c50.iam.gserviceaccount.com\",\r\n  \"client_id\": \"110980307582234542256\",\r\n  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\r\n  \"token_uri\": \"https://oauth2.googleapis.com/token\",\r\n  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\r\n  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-mwrl6%40eport-89c50.iam.gserviceaccount.com\"\r\n}\r\n', 'Y', 'admin', '2020-07-07 11:45:05', NULL, NULL, NULL),
(102, 'Firebase URL', 'firebase.url', 'https://eport-89c50.firebaseio.com', 'Y', 'admin', '2020-07-07 11:45:05', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `sys_dept`
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
-- Dumping data for table `sys_dept`
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
-- Table structure for table `sys_dict_data`
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
-- Dumping data for table `sys_dict_data`
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
-- Table structure for table `sys_dict_type`
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
-- Dumping data for table `sys_dict_type`
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
-- Table structure for table `sys_job`
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
-- Dumping data for table `sys_job`
--

INSERT INTO `sys_job` (`job_id`, `job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');

-- --------------------------------------------------------

--
-- Table structure for table `sys_job_log`
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
-- Table structure for table `sys_logininfor`
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
-- Dumping data for table `sys_logininfor`
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
(460, 'Carrier: mst123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-11 10:03:27');
INSERT INTO `sys_logininfor` (`info_id`, `login_name`, `ipaddr`, `login_location`, `browser`, `os`, `status`, `msg`, `login_time`) VALUES
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
(525, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-19 22:54:39'),
(526, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-23 10:56:39'),
(527, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-23 13:37:50'),
(528, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-23 14:10:13'),
(529, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-23 14:21:58'),
(530, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-23 14:23:13'),
(531, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-23 14:24:36'),
(532, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-23 14:29:33'),
(533, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-23 14:54:59'),
(534, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-23 14:57:06'),
(535, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-23 16:24:39'),
(536, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-23 16:32:45'),
(537, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-23 16:39:36'),
(538, 'mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-23 16:46:38'),
(539, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-23 16:46:42'),
(540, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-23 16:48:54'),
(541, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-23 18:50:27'),
(542, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-23 18:57:59'),
(543, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-23 19:46:05'),
(544, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-23 19:47:57'),
(545, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-24 08:40:43'),
(546, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-06-24 09:37:17'),
(547, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-24 09:37:20'),
(548, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-24 10:44:11'),
(549, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-24 12:29:14'),
(550, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-24 15:38:49'),
(551, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-24 16:25:43'),
(552, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-24 17:24:41'),
(553, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-24 18:07:05'),
(554, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-24 18:13:06'),
(555, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-24 18:13:10'),
(556, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-24 20:07:39'),
(557, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-24 20:58:45'),
(558, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-25 08:18:02'),
(559, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-25 09:23:20'),
(560, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-25 11:20:09'),
(561, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-25 11:54:53'),
(562, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-25 13:05:26'),
(563, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-25 15:33:06'),
(564, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-25 15:36:26'),
(565, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-25 15:57:57'),
(566, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-25 16:45:01'),
(567, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-25 16:46:30'),
(568, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-25 16:59:43'),
(569, 'Carrier: ry@163.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-06-25 17:22:03'),
(570, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-06-25 17:22:08'),
(571, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-25 17:22:10'),
(572, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-06-25 17:23:25'),
(573, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-25 17:23:29'),
(574, 'Carrier: asdfasd@sadfs.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-06-25 17:26:23'),
(575, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-25 17:26:28'),
(576, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-25 18:29:23'),
(577, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-25 18:37:29'),
(578, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-25 20:02:56'),
(579, 'Carrier: mst123123', '192.168.1.93', 'Intranet IP', 'Unknown', 'Unknown', '0', 'Đăng nhập thành công', '2020-06-26 09:23:34'),
(580, 'Carrier: mst123123', '192.168.1.93', 'Intranet IP', 'Unknown', 'Unknown', '0', 'Đăng nhập thành công', '2020-06-26 09:31:28'),
(581, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-26 09:45:15'),
(582, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-26 09:50:46'),
(583, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-26 09:54:45'),
(584, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-26 09:57:12'),
(585, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-26 10:49:22'),
(586, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-26 11:23:31'),
(587, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-06-26 15:33:43'),
(588, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-26 15:33:47'),
(589, 'Carrier: mst123123', '192.168.1.93', 'Intranet IP', 'Unknown', 'Unknown', '0', 'Đăng nhập thành công', '2020-06-26 16:29:45'),
(590, 'Carrier: mst123123', '192.168.1.93', 'Intranet IP', 'Unknown', 'Unknown', '0', 'Đăng nhập thành công', '2020-06-26 16:29:49'),
(591, 'Carrier: mst123123', '192.168.1.93', 'Intranet IP', 'Unknown', 'Unknown', '0', 'Đăng nhập thành công', '2020-06-26 16:55:36'),
(592, 'Carrier: mst123123', '192.168.1.93', 'Intranet IP', 'Unknown', 'Unknown', '0', 'Đăng nhập thành công', '2020-06-26 16:57:24'),
(593, 'Carrier: mst123123', '192.168.1.93', 'Intranet IP', 'Unknown', 'Unknown', '0', 'Đăng nhập thành công', '2020-06-26 18:42:09'),
(594, 'Carrier: mst123123', '192.168.1.93', 'Intranet IP', 'Unknown', 'Unknown', '0', 'Đăng nhập thành công', '2020-06-26 18:42:23'),
(595, 'Carrier: mst123123', '192.168.1.93', 'Intranet IP', 'Unknown', 'Unknown', '0', 'Đăng nhập thành công', '2020-06-26 18:42:49'),
(596, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-26 18:45:35'),
(597, 'Carrier: mst123123', '192.168.1.93', 'Intranet IP', 'Unknown', 'Unknown', '0', 'Đăng nhập thành công', '2020-06-26 18:46:20'),
(598, 'Carrier: mst123123', '192.168.1.93', 'Intranet IP', 'Unknown', 'Unknown', '0', 'Đăng nhập thành công', '2020-06-26 18:46:35'),
(599, 'Carrier: mst123123', '192.168.1.93', 'Intranet IP', 'Unknown', 'Unknown', '0', 'Đăng nhập thành công', '2020-06-26 18:47:07'),
(600, 'Carrier: mst123123', '192.168.1.93', 'Intranet IP', 'Unknown', 'Unknown', '0', 'Đăng nhập thành công', '2020-06-26 18:47:22'),
(601, 'Carrier: admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-26 20:41:33'),
(602, 'Carrier: admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-26 20:41:38'),
(603, 'Carrier: admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-26 20:41:43'),
(604, 'Carrier: admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-26 20:41:50'),
(605, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-26 20:41:56'),
(606, 'Carrier: asdfasd@sadfs.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng xuất thành công', '2020-06-26 20:44:04'),
(607, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-26 20:44:22'),
(608, 'Carrier: mst123123', '192.168.1.93', 'Intranet IP', 'Unknown', 'Unknown', '0', 'Đăng nhập thành công', '2020-06-26 20:57:35'),
(609, 'Carrier: mst123123', '192.168.1.93', 'Intranet IP', 'Unknown', 'Unknown', '0', 'Đăng nhập thành công', '2020-06-26 21:03:29'),
(610, 'Carrier: mst123123', '192.168.1.93', 'Intranet IP', 'Unknown', 'Unknown', '0', 'Đăng nhập thành công', '2020-06-26 21:04:00'),
(611, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-26 22:22:14'),
(612, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-26 22:59:30'),
(613, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-27 09:30:10'),
(614, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-27 09:46:07'),
(615, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-27 10:11:50'),
(616, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-27 10:21:53'),
(617, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-27 11:25:54'),
(618, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-27 12:03:51'),
(619, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-27 12:53:44'),
(620, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-29 13:41:17'),
(621, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-29 15:51:39'),
(622, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-29 16:07:05'),
(623, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-29 16:23:53'),
(624, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-29 18:08:35'),
(625, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-06-29 18:45:00'),
(626, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-29 18:45:03'),
(627, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-29 20:16:51'),
(628, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-30 08:50:19'),
(629, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-30 10:29:27'),
(630, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-30 10:40:16'),
(631, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-30 11:43:59'),
(632, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-30 11:46:44'),
(633, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-30 11:48:10'),
(634, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-30 13:26:25'),
(635, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-30 13:37:01'),
(636, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-30 14:01:42'),
(637, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-30 17:01:24'),
(638, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-30 18:19:43'),
(639, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-30 18:53:33'),
(640, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-30 19:31:45'),
(641, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-06-30 19:52:39'),
(642, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-01 09:05:05'),
(643, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-01 10:35:39'),
(644, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-01 10:41:13'),
(645, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-07-01 11:27:35'),
(646, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 2', '2020-07-01 11:27:37'),
(647, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-01 11:27:40'),
(648, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-01 14:30:25'),
(649, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-07-01 15:04:09'),
(650, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 2', '2020-07-01 15:04:11'),
(651, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-01 15:04:13'),
(652, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-01 16:13:11'),
(653, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-01 17:18:13'),
(654, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-01 18:32:11'),
(655, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-02 08:04:39'),
(656, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-02 08:26:36'),
(657, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-02 09:00:40'),
(658, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-02 09:40:01'),
(659, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-02 10:14:27'),
(660, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-02 13:33:28'),
(661, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-02 14:01:32'),
(662, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-02 15:08:25'),
(663, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-02 16:58:20'),
(664, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-02 19:13:44'),
(665, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-02 19:52:31'),
(666, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-03 11:32:17'),
(667, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-03 14:33:35'),
(668, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-03 14:37:31'),
(669, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-03 14:43:53'),
(670, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-03 14:48:49'),
(671, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-03 17:49:17'),
(672, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-03 18:50:38'),
(673, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-03 19:18:49'),
(674, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-03 19:58:10'),
(675, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-03 20:29:41'),
(676, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-03 21:19:56'),
(677, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-03 21:51:02'),
(678, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-03 21:58:23'),
(679, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-03 22:13:01'),
(680, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-03 22:13:41'),
(681, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-03 22:16:25'),
(682, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-03 22:20:54'),
(683, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-04 10:26:18'),
(684, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-04 10:31:09'),
(685, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-04 11:04:43'),
(686, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-04 19:27:19'),
(687, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-06 08:01:24'),
(688, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-06 09:38:36'),
(689, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-06 11:41:07'),
(690, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-06 13:32:43'),
(691, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-06 14:08:12'),
(692, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-06 15:15:21'),
(693, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-06 17:00:54'),
(694, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-06 18:07:41'),
(695, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-06 20:17:00'),
(696, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-07 08:23:35'),
(697, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-07 08:31:53'),
(698, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-07 10:40:29'),
(699, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-07 11:34:58'),
(700, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-07 13:20:58'),
(701, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-07 14:14:31'),
(702, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-07 15:20:04'),
(703, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Nhập sai mật khẩu lần 1', '2020-07-07 15:20:21'),
(704, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-07 15:20:36'),
(705, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-07 16:18:52'),
(706, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-07 18:58:47'),
(707, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-07 20:00:45'),
(708, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-07 21:17:52'),
(709, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-07 21:55:40'),
(710, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-07 23:15:42'),
(711, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-07 23:40:32'),
(712, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-08 08:20:57'),
(713, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-08 08:24:50'),
(714, 'tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-07-08 09:10:59'),
(715, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-08 09:11:06'),
(716, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-08 09:11:39'),
(717, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-08 09:14:40'),
(718, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-08 09:46:15'),
(719, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-08 10:10:02'),
(720, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-08 10:14:02'),
(721, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-08 11:44:38'),
(722, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-08 15:24:59'),
(723, 'Carrier: tronghieu@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-07-08 16:09:12'),
(724, 'Carrier: tronghieu853@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-07-08 16:09:18'),
(725, 'Carrier: tronghieu8531@gmail.com', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-08 16:09:23'),
(726, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-08 20:07:09'),
(727, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-08 20:09:43'),
(728, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-08 20:15:08'),
(729, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-08 20:38:45'),
(730, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-08 20:42:02'),
(731, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-08 20:47:58'),
(732, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-08 20:50:56'),
(733, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-09 08:09:17'),
(734, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '1', 'Tên đăng nhập hoặc mật khẩu không đúng', '2020-07-09 08:16:20'),
(735, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-09 08:16:24'),
(736, 'Carrier: mst123123', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-09 10:23:49'),
(737, 'admin', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', '0', 'Đăng nhập thành công', '2020-07-09 10:57:29');

-- --------------------------------------------------------

--
-- Table structure for table `sys_menu`
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
-- Dumping data for table `sys_menu`
--

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(1, 'Quản Lý Hệ Thống', 0, 1, '#', '', 'M', '0', '', 'fa fa-gear', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'System Management'),
(2, 'System Monitoring', 0, 3, '#', 'menuItem', 'M', '1', '', 'fa fa-video-camera', 'admin', '2018-03-16 11:33:00', 'admin', '2020-07-09 11:47:11', 'System Monitoring'),
(3, 'Hỗ Trợ Kỹ Thuật', 0, 2, '#', 'menuItem', 'M', '1', '', 'fa fa-bars', 'admin', '2018-03-16 11:33:00', 'admin', '2020-07-09 11:47:06', 'System Tools目录'),
(100, 'Quản Lý Người Dùng', 1, 1, '/system/user', 'menuItem', 'C', '0', 'system:user:view', 'fa fa-address-book-o', 'admin', '2018-03-16 11:33:00', 'admin', '2020-07-09 11:32:35', 'User Management'),
(101, 'Quản Lý Vai Trò', 1, 2, '/system/role', 'menuItem', 'C', '0', 'system:role:view', 'fa fa-users', 'admin', '2018-03-16 11:33:00', 'admin', '2020-07-09 11:32:51', 'Role Management'),
(102, 'Danh Mục', 1, 3, '/system/menu', 'menuItem', 'C', '0', 'system:menu:view', 'fa fa-bars', 'admin', '2018-03-16 11:33:00', 'admin', '2020-07-09 11:33:05', ''),
(103, 'Department', 1, 4, '/system/dept', '', 'C', '1', 'system:dept:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(104, 'Post', 1, 5, '/system/post', '', 'C', '1', 'system:post:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(105, 'Dictionary', 1, 6, '/system/dict', 'menuItem', 'C', '0', 'system:dict:view', '#', 'admin', '2018-03-16 11:33:00', 'admin', '2020-06-30 11:18:47', ''),
(106, 'Config', 1, 7, '/system/config', 'menuItem', 'C', '0', 'system:config:view', '#', 'admin', '2018-03-16 11:33:00', 'admin', '2020-06-30 11:18:36', ''),
(107, 'Notification', 1, 8, '/system/notice', '', 'C', '1', 'system:notice:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(108, 'Lịch sử', 1, 9, '#', '', 'M', '0', '', 'fa fa-hourglass-1', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(109, 'Online User', 2, 1, '/monitor/online', 'menuItem', 'C', '0', 'monitor:online:view', '#', 'admin', '2018-03-16 11:33:00', 'admin', '2020-07-09 11:30:52', ''),
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
(2000, 'Nhóm Hãng Tàu', 2034, 1, '/carrier/group', 'menuItem', 'C', '0', 'carrier:group:view', 'fa fa-anchor', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-09 11:08:20', 'Carrier Group Menu'),
(2001, 'Get Carrier Group', 2000, 1, '#', '', 'F', '0', 'carrier:group:list', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2002, 'Add Carrier Group', 2000, 2, '#', '', 'F', '0', 'carrier:group:add', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2003, 'Edit Carrier Group', 2000, 3, '#', '', 'F', '0', 'carrier:group:edit', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2004, 'Delete Carrier Group', 2000, 4, '#', '', 'F', '0', 'carrier:group:remove', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2005, 'Export Carrier Group', 2000, 5, '#', '', 'F', '0', 'carrier:group:export', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2006, 'Tài Khoản Hãng Tàu', 2034, 1, '/carrier/account', 'menuItem', 'C', '0', 'carrier:account:view', 'fa fa-user-circle', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-09 11:09:28', 'Carrier Account Menu'),
(2007, 'Carrier Account List', 2006, 1, '#', '', 'F', '0', 'carrier:account:list', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2008, 'Add Carrier Account', 2006, 2, '#', '', 'F', '0', 'carrier:account:add', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2009, 'Edit Carrier Account', 2006, 3, '#', '', 'F', '0', 'carrier:account:edit', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2010, 'Delete Carrier Account', 2006, 4, '#', '', 'F', '0', 'carrier:account:remove', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2011, 'Export Carrier Account', 2006, 5, '#', '', 'F', '0', 'carrier:account:export', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2012, 'Vận Đơn', 3, 1, '/carrier/admin/do/getViewDo', 'menuItem', 'C', '1', 'carrier:admin:do:getViewDo:view', 'fa fa-file-excel-o', 'admin', '2018-03-01 00:00:00', 'admin', '2020-07-09 11:12:34', 'Exchange Delivery Order Menu'),
(2013, 'Delivery Order List', 2012, 1, '#', '', 'F', '0', 'equipment:do:list', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2014, 'Add DO', 2012, 2, '#', '', 'F', '0', 'equipment:do:add', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2015, 'Edit DO', 2012, 3, '#', '', 'F', '0', 'equipment:do:edit', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2016, 'Delete DO', 2012, 4, '#', '', 'F', '0', 'equipment:do:remove', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2017, 'Export DO', 2012, 5, '#', '', 'F', '0', 'equipment:do:export', '#', 'admin', '2018-03-01 00:00:00', 'admin', '2018-03-01 00:00:00', ''),
(2019, 'Nhóm Logistic', 2034, 1, '/logistic/group', 'menuItem', 'C', '0', 'logistic:group:view', 'fa fa-truck', 'admin', '2020-05-14 18:03:26', 'admin', '2020-07-09 11:16:33', ''),
(2020, 'Tài khoản Logistic', 2034, 1, '/logistic/account', 'menuItem', 'C', '0', 'logistic:account:view', 'fa fa-address-book-o', 'admin', '2020-05-14 18:04:20', 'admin', '2020-07-09 11:17:20', ''),
(2021, 'Bộ Phận OM', 0, 5, '#', 'menuItem', 'M', '0', '', 'fa fa-address-book-o', 'admin', '2020-06-08 14:19:16', 'admin', '2020-07-09 11:47:20', ''),
(2022, 'Kế Hoạch Bãi Cảng', 0, 6, '#', 'menuItem', 'M', '0', '', 'fa fa-anchor', 'admin', '2020-06-08 14:20:16', 'admin', '2020-07-09 11:47:27', ''),
(2024, 'Hỗ trợ Robot làm lệnh', 2036, 2, '/om/executeCatos/index', 'menuItem', 'C', '0', '', 'fa fa-navicon', 'admin', '2020-06-09 20:56:31', 'admin', '2020-07-09 11:38:51', ''),
(2026, 'Giám Sát Robot', 2036, 1, 'system/robot/index', 'menuItem', 'C', '0', '', 'fa fa-cogs', 'admin', '2020-06-18 15:17:24', 'admin', '2020-07-09 11:37:46', ''),
(2028, 'Hoạt động của xe và nhật ký giao nhận', 2043, 1, '/history/truck/', 'menuItem', 'C', '0', '', 'fa fa-bus', 'admin', '2020-06-30 10:37:51', 'admin', '2020-07-09 11:44:54', ''),
(2030, 'Lịch sử robot', 2043, 2, '/history/robot/', 'menuItem', 'C', '0', '', 'fa fa-cogs', 'admin', '2020-07-01 18:41:32', 'admin', '2020-07-09 11:45:31', ''),
(2031, 'Bộ phận cấp container', 0, 7, '/container/supplier', 'menuItem', 'C', '0', '', 'fa fa-cubes', 'admin', '2020-07-07 16:21:19', 'admin', '2020-07-09 11:47:32', ''),
(2032, 'Đọc file EDI', 2035, 2, '/edo/manage/viewFileEdi', 'menuItem', 'C', '0', '', 'fa fa-check-square', 'admin', '2020-06-26 10:32:57', 'admin', '2020-07-09 11:26:43', ''),
(2033, 'Danh Sách eDO', 2035, 3, '/edo/manage/index', 'menuItem', 'C', '0', '', 'fa fa-handshake-o', 'admin', '2020-06-26 18:53:09', 'admin', '2020-07-09 11:27:42', ''),
(2034, 'Quản Lý Tài Khoản', 0, 4, '#', 'menuItem', 'M', '0', '', 'fa fa-bars', 'admin', '2020-07-09 11:07:40', 'admin', '2020-07-09 11:47:15', ''),
(2035, 'Quản Lý eDO', 2021, 1, '#', 'menuItem', 'M', '0', NULL, 'fa fa-bars', 'admin', '2020-07-09 11:25:18', '', NULL, ''),
(2036, 'Kiểm Soát Luồng Công VIệc', 2021, 2, '#', 'menuItem', 'M', '0', NULL, 'fa fa-bars', 'admin', '2020-07-09 11:29:27', '', NULL, ''),
(2037, 'Thông Báo', 2021, 3, '#', 'menuItem', 'M', '0', NULL, 'fa fa-bell', 'admin', '2020-07-09 11:35:31', '', NULL, ''),
(2039, 'Quản Lý Thông Báo', 2037, 1, '/notifications', 'menuItem', 'C', '0', '', '#', 'admin', '2020-07-09 11:37:00', 'admin', '2020-07-09 11:39:53', ''),
(2041, 'Gửi Thông Báo', 2037, 2, '/notifications/add', 'menuItem', 'C', '0', '', 'fa fa-commenting-o', 'admin', '2020-07-09 11:42:02', 'admin', '2020-07-09 11:42:27', ''),
(2043, 'Lịch Sử', 2021, 4, '#', 'menuItem', 'M', '0', NULL, 'fa fa-book', 'admin', '2020-07-09 11:44:27', '', NULL, '');

-- --------------------------------------------------------

--
-- Table structure for table `sys_notice`
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
-- Dumping data for table `sys_notice`
--

INSERT INTO `sys_notice` (`notice_id`, `notice_title`, `notice_type`, `notice_content`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(1, 'Warm reminder: 2018-07-01 DNG new version released', '2', 'New version content', '0', 'admin', '2018-03-16 11:33:00', 'admin', '2020-03-28 14:23:04', ''),
(2, 'Maintenance notice: 2018-07-01 Early morning maint', '1', 'Maintenance content', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');

-- --------------------------------------------------------

--
-- Table structure for table `sys_oper_log`
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
-- Dumping data for table `sys_oper_log`
--

INSERT INTO `sys_oper_log` (`oper_id`, `title`, `business_type`, `method`, `request_method`, `operator_type`, `oper_name`, `dept_name`, `oper_url`, `oper_ip`, `oper_location`, `oper_param`, `json_result`, `status`, `error_msg`, `oper_time`) VALUES
(109, 'Carrier Account', 1, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"groupId\" : [ \"1\" ],\r\n  \"carrierCode\" : [ \"123546\" ],\r\n  \"email\" : [ \"tai@gmail.com\" ],\r\n  \"password\" : [ \"admin123\" ],\r\n  \"fullName\" : [ \"Anh Tài\" ]\r\n}', '{\r\n  \"msg\" : \"Success\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-07 11:18:42'),
(110, 'Carrier Account', 1, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"groupId\" : [ \"1\" ],\r\n  \"carrierCode\" : [ \"dnkasbdk\" ],\r\n  \"email\" : [ \"nqat2003@gmail.com\" ],\r\n  \"password\" : [ \"123qwe123\" ],\r\n  \"fullName\" : [ \"Anh Taif\" ]\r\n}', '{\r\n  \"msg\" : \"Success\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-08 10:20:34'),
(111, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/carrier/account/resetPwd/2', '127.0.0.1', 'Intranet IP', '{ }', '\"carrier/account/resetPwd\"', 0, NULL, '2020-04-08 10:20:41'),
(112, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwdSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/resetPwd', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"2\" ],\r\n  \"email\" : [ \"nqat2003@gmail.com\" ],\r\n  \"password\" : [ \"123456\" ]\r\n}', '{\r\n  \"msg\" : \"Success\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-08 10:21:33'),
(113, 'Carrier Account', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.changeStatus()', 'POST', 1, 'admin', 'R&D', '/carrier/account/changeStatus', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"status\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Success\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-08 10:22:41'),
(114, 'Carrier Account', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.changeStatus()', 'POST', 1, 'admin', 'R&D', '/carrier/account/changeStatus', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"status\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Success\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-08 10:22:48'),
(115, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/carrier/account/resetPwd/1', '127.0.0.1', 'Intranet IP', '{ }', '\"carrier/account/resetPwd\"', 0, NULL, '2020-04-10 08:45:29'),
(116, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/carrier/account/resetPwd/1', '127.0.0.1', 'Intranet IP', '{ }', '\"carrier/account/resetPwd\"', 0, NULL, '2020-04-14 08:19:17'),
(117, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/carrier/account/resetPwd/1', '127.0.0.1', 'Intranet IP', '{ }', '\"carrier/account/resetPwd\"', 0, NULL, '2020-04-14 08:29:43'),
(118, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/carrier/account/resetPwd/1', '127.0.0.1', 'Intranet IP', '{ }', '\"carrier/account/resetPwd\"', 0, NULL, '2020-04-14 11:49:10'),
(119, 'Carrier Account', 1, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"groupId\" : [ \"1\" ],\r\n  \"carrierCode\" : [ \"1\" ],\r\n  \"email\" : [ \"tronghieu8531@gmail.com\" ],\r\n  \"password\" : [ \"123123\" ],\r\n  \"fullName\" : [ \"Nguyễn Trọng Hiếu\" ],\r\n  \"status\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-14 11:49:31'),
(120, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"groupCode\" : [ \"1\" ],\r\n  \"groupName\" : [ \"WWHA\" ],\r\n  \"operateCode\" : [ \"1\" ],\r\n  \"mainEmail\" : [ \"hello@gmail.com\" ],\r\n  \"doFlag\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-27 09:36:45'),
(121, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"groupCode\" : [ \"1\" ],\r\n  \"groupName\" : [ \"WWHA\" ],\r\n  \"operateCode\" : [ \"1\" ],\r\n  \"mainEmail\" : [ \"hello@gmail.com\" ],\r\n  \"doFlag\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-27 09:37:33'),
(122, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"groupCode\" : [ \"1\" ],\r\n  \"groupName\" : [ \"WWHA\" ],\r\n  \"operateCode\" : [ \"1\" ],\r\n  \"mainEmail\" : [ \"hello@gmail.com\" ],\r\n  \"doFlag\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-27 09:38:27'),
(123, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"groupCode\" : [ \"1\" ],\r\n  \"groupName\" : [ \"WWHA\" ],\r\n  \"operateCode\" : [ \"1\" ],\r\n  \"mainEmail\" : [ \"hello@gmail.com\" ],\r\n  \"doFlag\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-27 09:46:20'),
(124, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"groupCode\" : [ \"1\" ],\r\n  \"groupName\" : [ \"WWHA\" ],\r\n  \"operateCode\" : [ \"1\" ],\r\n  \"mainEmail\" : [ \"hello@gmail.com\" ],\r\n  \"doFlag\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-27 09:46:26'),
(125, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"groupCode\" : [ \"1\" ],\r\n  \"groupName\" : [ \"WWHA\" ],\r\n  \"operateCode\" : [ \"1\" ],\r\n  \"mainEmail\" : [ \"hello@gmail.com\" ],\r\n  \"doFlag\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-04-27 09:46:32'),
(126, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"groupCode\" : [ \"1\" ],\r\n  \"groupName\" : [ \"WWHA\" ],\r\n  \"operateCode\" : [ \"1\" ],\r\n  \"mainEmail\" : [ \"hello@gmail.com\" ],\r\n  \"doFlag\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-04 05:44:22'),
(127, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"groupCode\" : [ \"1\" ],\r\n  \"groupName\" : [ \"WWHA\" ],\r\n  \"operateCode\" : [ \"1\" ],\r\n  \"mainEmail\" : [ \"hello@gmail.com\" ],\r\n  \"doFlag\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-04 05:44:29'),
(128, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"groupCode\" : [ \"1\" ],\r\n  \"groupName\" : [ \"WWHA\" ],\r\n  \"operateCode\" : [ \"1\" ],\r\n  \"mainEmail\" : [ \"hello@gmail.com\" ],\r\n  \"doFlag\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-04 05:57:00'),
(129, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"groupCode\" : [ \"1\" ],\r\n  \"groupName\" : [ \"WWHA\" ],\r\n  \"operateCode\" : [ \"1\" ],\r\n  \"mainEmail\" : [ \"hello@gmail.com\" ],\r\n  \"doFlag\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-04 05:57:19'),
(130, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"groupCode\" : [ \"1\" ],\r\n  \"groupName\" : [ \"WWHA\" ],\r\n  \"operateCode\" : [ \"1\" ],\r\n  \"mainEmail\" : [ \"hello@gmail.com\" ],\r\n  \"doFlag\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-04 05:58:01'),
(131, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"1\" ],\r\n  \"groupCode\" : [ \"1\" ],\r\n  \"groupName\" : [ \"WWHA\" ],\r\n  \"operateCode\" : [ \"1\" ],\r\n  \"mainEmail\" : [ \"hello@gmail.com\" ],\r\n  \"doFlag\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-07 13:10:22'),
(132, 'Carrier Group', 1, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"groupCode\" : [ \"sfa\" ],\r\n  \"groupName\" : [ \"asdfasdf\" ],\r\n  \"operateCode\" : [ \"wdfs\" ],\r\n  \"mainEmail\" : [ \"asdfsd@asdfa.com\" ],\r\n  \"doFlag\" : [ \"\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-07 13:10:46'),
(133, 'Carrier Group', 1, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"groupCode\" : [ \"sdf\" ],\r\n  \"groupName\" : [ \"1asd\" ],\r\n  \"operateCode\" : [ \"asd\" ],\r\n  \"mainEmail\" : [ \"asdfa@asdf.com\" ],\r\n  \"doFlag\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-07 13:11:30'),
(134, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"3\" ],\r\n  \"groupCode\" : [ \"sdf\" ],\r\n  \"groupName\" : [ \"1asd\" ],\r\n  \"operateCode\" : [ \"asd\" ],\r\n  \"mainEmail\" : [ \"asdfa@asdf.com\" ],\r\n  \"doFlag\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-07 13:11:55'),
(135, 'Carrier Group', 2, 'vn.com.irtech.eport.carrier.controller.CarrierGroupController.editSave()', 'POST', 1, 'admin', 'R&D', '/carrier/group/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"2\" ],\r\n  \"groupCode\" : [ \"sfa\" ],\r\n  \"groupName\" : [ \"asdfasdf\" ],\r\n  \"operateCode\" : [ \"wdfs\" ],\r\n  \"mainEmail\" : [ \"asdfsd@asdfa.com\" ],\r\n  \"doFlag\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-07 13:12:02'),
(136, 'Logistic Group', 1, 'vn.com.irtech.eport.logistic.controller.LogisticGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/group/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"groupName\" : [ \"Công ty abc\" ],\r\n  \"mainEmail\" : [ \"abc@abc.com\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-19 08:49:52'),
(137, 'Logistic account', 1, 'vn.com.irtech.eport.logistic.controller.LogisticAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/account/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"groupId\" : [ \"2\" ],\r\n  \"email\" : [ \"tronghieu8531@gmail.com\" ],\r\n  \"password\" : [ \"hieu123\" ],\r\n  \"fullName\" : [ \"Nguyễn Trọng Hiếu\" ],\r\n  \"status\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-19 08:50:47'),
(138, 'Logistic Group', 1, 'vn.com.irtech.eport.logistic.controller.LogisticGroupController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/group/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"groupName\" : [ \"Vinconship\" ],\r\n  \"address\" : [ \"35 Cao Thang\" ],\r\n  \"mst\" : [ \"123123123123\" ],\r\n  \"phone\" : [ \"0541123412341\" ],\r\n  \"fax\" : [ \"1234234\" ],\r\n  \"emailAddress\" : [ \"email@email.com\" ],\r\n  \"businessRegistrationCertificate\" : [ \"asdfsadf\" ],\r\n  \"dateOfIssueRegistration\" : [ \"2020-05-28\" ],\r\n  \"placeOfIssueRegistration\" : [ \"asdfsadf\" ],\r\n  \"authorizedRepresentative\" : [ \"asdfsdaf\" ],\r\n  \"representativePosition\" : [ \"asdfsadf\" ],\r\n  \"followingAuthorizationFormNo\" : [ \"asdfas\" ],\r\n  \"signDate\" : [ \"2020-05-29\" ],\r\n  \"owned\" : [ \"asdfasdf\" ],\r\n  \"identifyCardNo\" : [ \"12341231231\" ],\r\n  \"dateOfIssueIdentify\" : [ \"2020-05-14\" ],\r\n  \"placeOfIssueIdentify\" : [ \"asdfasdf\" ],\r\n  \"mobilePhone\" : [ \"0935802290\" ],\r\n  \"email\" : [ \"tronghieu8531@gmail.com\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-28 13:49:54'),
(139, 'Logistic account', 1, 'vn.com.irtech.eport.logistic.controller.LogisticAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/account/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"groupId\" : [ \"1\" ],\r\n  \"email\" : [ \"asdfasd@sadfs.com\" ],\r\n  \"userName\" : [ \"mst123123\" ],\r\n  \"password\" : [ \"123456\" ],\r\n  \"fullName\" : [ \"nguyen trong hieu\" ],\r\n  \"status\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-05-28 13:50:26'),
(140, '菜单管理', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"114\" ],\r\n  \"parentId\" : [ \"3\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Code Gen\" ],\r\n  \"url\" : [ \"/tool/gen\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"tool:gen:view\" ],\r\n  \"orderNum\" : [ \"2\" ],\r\n  \"icon\" : [ \"#\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-19 18:28:26'),
(141, '代码生成', 6, 'vn.com.irtech.eport.generator.controller.GenController.importTableSave()', 'POST', 1, 'admin', 'R&D', '/tool/gen/importTable', '127.0.0.1', 'Intranet IP', '{\r\n  \"tables\" : [ \"process_order\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-23 10:57:22'),
(142, '代码生成', 8, 'vn.com.irtech.eport.generator.controller.GenController.batchGenCode()', 'GET', 1, 'admin', 'R&D', '/tool/gen/batchGenCode', '127.0.0.1', 'Intranet IP', '{\r\n  \"tables\" : [ \"process_order\" ]\r\n}', 'null', 0, NULL, '2020-06-23 10:58:49'),
(143, '菜单管理', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2\" ],\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"System Monitoring\" ],\r\n  \"url\" : [ \"#\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"2\" ],\r\n  \"icon\" : [ \"fa fa-video-camera\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-23 16:33:48'),
(144, 'Logistic account', 1, 'vn.com.irtech.eport.logistic.controller.LogisticAccountController.addSave()', 'POST', 1, 'admin', 'R&D', '/logistic/account/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"groupId\" : [ \"1\" ],\r\n  \"email\" : [ \"tronghieu8531@gmail.com\" ],\r\n  \"userName\" : [ \"hieu123\" ],\r\n  \"password\" : [ \"123456\" ],\r\n  \"fullName\" : [ \"sdfas\" ],\r\n  \"status\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-26 18:45:54'),
(145, '代码生成', 6, 'vn.com.irtech.eport.generator.controller.GenController.importTableSave()', 'POST', 1, 'admin', 'R&D', '/tool/gen/importTable', '127.0.0.1', 'Intranet IP', '{\r\n  \"tables\" : [ \"process_history\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-27 10:23:53'),
(146, '代码生成', 8, 'vn.com.irtech.eport.generator.controller.GenController.genCode()', 'GET', 1, 'admin', 'R&D', '/tool/gen/genCode/process_history', '127.0.0.1', 'Intranet IP', '{ }', 'null', 0, NULL, '2020-06-27 10:24:15'),
(147, '代码生成', 2, 'vn.com.irtech.eport.generator.controller.GenController.editSave()', 'POST', 1, 'admin', 'R&D', '/tool/gen/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"tableId\" : [ \"4\" ],\r\n  \"tableName\" : [ \"process_history\" ],\r\n  \"tableComment\" : [ \"Process order history\" ],\r\n  \"className\" : [ \"ProcessHistory\" ],\r\n  \"functionAuthor\" : [ \"ruoyi\" ],\r\n  \"remark\" : [ \"\" ],\r\n  \"columns[0].columnId\" : [ \"38\" ],\r\n  \"columns[0].sort\" : [ \"1\" ],\r\n  \"columns[0].columnComment\" : [ \"ID\" ],\r\n  \"columns[0].javaType\" : [ \"Long\" ],\r\n  \"columns[0].javaField\" : [ \"id\" ],\r\n  \"columns[0].isInsert\" : [ \"1\" ],\r\n  \"columns[0].queryType\" : [ \"EQ\" ],\r\n  \"columns[0].htmlType\" : [ \"input\" ],\r\n  \"columns[0].dictType\" : [ \"\" ],\r\n  \"columns[1].columnId\" : [ \"39\" ],\r\n  \"columns[1].sort\" : [ \"2\" ],\r\n  \"columns[1].columnComment\" : [ \"Process Order ID\" ],\r\n  \"columns[1].javaType\" : [ \"Long\" ],\r\n  \"columns[1].javaField\" : [ \"processOrderId\" ],\r\n  \"columns[1].isInsert\" : [ \"1\" ],\r\n  \"columns[1].isEdit\" : [ \"1\" ],\r\n  \"columns[1].isList\" : [ \"1\" ],\r\n  \"columns[1].isQuery\" : [ \"1\" ],\r\n  \"columns[1].queryType\" : [ \"EQ\" ],\r\n  \"columns[1].isRequired\" : [ \"1\" ],\r\n  \"columns[1].htmlType\" : [ \"input\" ],\r\n  \"columns[1].dictType\" : [ \"\" ],\r\n  \"columns[2].columnId\" : [ \"40\" ],\r\n  \"columns[2].sort\" : [ \"3\" ],\r\n  \"columns[2].columnComment\" : [ \"User ID (OM)\" ],\r\n  \"columns[2].javaType\" : [ \"Long\" ],\r\n  \"columns[2].javaField\" : [ \"sysUserId\" ],\r\n  \"columns[2].isInsert\" : [ \"1\" ],\r\n  \"columns[2].isEdit\" : [ \"1\" ],\r\n  \"columns[2].isList\" : [ \"1\" ],\r\n  \"columns[2].isQuery\" : [ \"1\" ],\r\n  \"columns[2].queryType\" : [ \"EQ\" ],\r\n  \"columns[2].htmlType\" : [ \"input\" ],\r\n  \"columns[2].dictType\" : [ \"\" ],\r\n  \"columns[3].columnId\" : [ \"41\" ],\r\n  \"columns[3].sort\" : [ \"4\" ],\r\n  \"columns[3].columnComment\" : [ \"Robot UUID\" ],\r\n  \"columns[3].javaType\" : [ \"String\" ],\r\n  \"columns[3].javaField\" : [ \"robotUuid\" ],\r\n  \"columns[3].isInsert\" : [ \"1\" ],\r\n  \"columns[3].isEdit\" : [ \"1\" ],\r\n  \"columns[3].isList\" : [ \"1\" ],\r\n  \"columns[3].isQuery\" : [ \"1\" ],\r\n  \"columns[3].queryType\" : [ \"EQ\" ],\r\n  \"columns[3].htmlType\" : [ \"input\" ],\r\n  \"columns[3].dictType\" : [ \"\" ],\r\n  \"columns[4].columnId\" : [ \"42\" ', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-27 10:28:05'),
(148, '代码生成', 8, 'vn.com.irtech.eport.generator.controller.GenController.genCode()', 'GET', 1, 'admin', 'R&D', '/tool/gen/genCode/process_history', '127.0.0.1', 'Intranet IP', '{ }', 'null', 0, NULL, '2020-06-27 10:28:56'),
(149, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"106\" ],\r\n  \"parentId\" : [ \"1\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Config\" ],\r\n  \"url\" : [ \"/system/config\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"system:config:view\" ],\r\n  \"orderNum\" : [ \"7\" ],\r\n  \"icon\" : [ \"#\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-30 11:18:36'),
(150, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"105\" ],\r\n  \"parentId\" : [ \"1\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Dictionary\" ],\r\n  \"url\" : [ \"/system/dict\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"system:dict:view\" ],\r\n  \"orderNum\" : [ \"6\" ],\r\n  \"icon\" : [ \"#\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-30 11:18:47'),
(151, 'Parameter management', 1, 'vn.com.irtech.eport.web.controller.system.SysConfigController.addSave()', 'POST', 1, 'admin', 'R&D', '/system/config/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"configName\" : [ \"Da Nang Port Name\" ],\r\n  \"configKey\" : [ \"danangPortName\" ],\r\n  \"configValue\" : [ \"Cảng Tiên Sa\" ],\r\n  \"configType\" : [ \"Y\" ],\r\n  \"remark\" : [ \"Tên cảng Đà Nãng\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-30 11:22:55'),
(152, 'Parameter management', 2, 'vn.com.irtech.eport.web.controller.system.SysConfigController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/config/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"configId\" : [ \"100\" ],\r\n  \"configName\" : [ \"Da Nang Port Name\" ],\r\n  \"configKey\" : [ \"danang.port.name\" ],\r\n  \"configValue\" : [ \"Cảng Tiên Sa\" ],\r\n  \"configType\" : [ \"Y\" ],\r\n  \"remark\" : [ \"Tên cảng Đà Nãng\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-30 11:30:19'),
(153, 'Parameter management', 2, 'vn.com.irtech.eport.web.controller.system.SysConfigController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/config/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"configId\" : [ \"100\" ],\r\n  \"configName\" : [ \"Da Nang Port Name\" ],\r\n  \"configKey\" : [ \"danang.port.name\" ],\r\n  \"configValue\" : [ \"Cảng Tiên Sasaa\" ],\r\n  \"configType\" : [ \"Y\" ],\r\n  \"remark\" : [ \"Tên cảng Đà Nãng\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-30 11:43:24'),
(154, 'Parameter management', 2, 'vn.com.irtech.eport.web.controller.system.SysConfigController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/config/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"configId\" : [ \"100\" ],\r\n  \"configName\" : [ \"Da Nang Port Name\" ],\r\n  \"configKey\" : [ \"danang.port.name\" ],\r\n  \"configValue\" : [ \"Cảng Tiên Sa\" ],\r\n  \"configType\" : [ \"Y\" ],\r\n  \"remark\" : [ \"Tên cảng Đà Nãng\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-30 11:48:44'),
(155, 'Parameter management', 2, 'vn.com.irtech.eport.web.controller.system.SysConfigController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/config/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"configId\" : [ \"100\" ],\r\n  \"configName\" : [ \"Da Nang Port Name\" ],\r\n  \"configKey\" : [ \"danang.port.name\" ],\r\n  \"configValue\" : [ \"Cảng Tiên Saa\" ],\r\n  \"configType\" : [ \"Y\" ],\r\n  \"remark\" : [ \"Tên cảng Đà Nãng\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-30 11:50:48'),
(156, 'Parameter management', 2, 'vn.com.irtech.eport.web.controller.system.SysConfigController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/config/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"configId\" : [ \"100\" ],\r\n  \"configName\" : [ \"Da Nang Port Name\" ],\r\n  \"configKey\" : [ \"danang.port.name\" ],\r\n  \"configValue\" : [ \"Cảng Tiên Saaa\" ],\r\n  \"configType\" : [ \"Y\" ],\r\n  \"remark\" : [ \"Tên cảng Đà Nãng\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-30 11:51:09'),
(157, '代码生成', 6, 'vn.com.irtech.eport.generator.controller.GenController.importTableSave()', 'POST', 1, 'admin', 'R&D', '/tool/gen/importTable', '127.0.0.1', 'Intranet IP', '{\r\n  \"tables\" : [ \"pickup_hisory,pickup_assign\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-30 13:41:24'),
(158, '代码生成', 2, 'vn.com.irtech.eport.generator.controller.GenController.editSave()', 'POST', 1, 'admin', 'R&D', '/tool/gen/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"tableId\" : [ \"5\" ],\r\n  \"tableName\" : [ \"pickup_assign\" ],\r\n  \"tableComment\" : [ \"Pickup Assign\" ],\r\n  \"className\" : [ \"PickupAssign\" ],\r\n  \"functionAuthor\" : [ \"ruoyi\" ],\r\n  \"remark\" : [ \"\" ],\r\n  \"columns[0].columnId\" : [ \"48\" ],\r\n  \"columns[0].sort\" : [ \"1\" ],\r\n  \"columns[0].columnComment\" : [ \"ID\" ],\r\n  \"columns[0].javaType\" : [ \"Long\" ],\r\n  \"columns[0].javaField\" : [ \"id\" ],\r\n  \"columns[0].isInsert\" : [ \"1\" ],\r\n  \"columns[0].queryType\" : [ \"EQ\" ],\r\n  \"columns[0].htmlType\" : [ \"input\" ],\r\n  \"columns[0].dictType\" : [ \"\" ],\r\n  \"columns[1].columnId\" : [ \"49\" ],\r\n  \"columns[1].sort\" : [ \"2\" ],\r\n  \"columns[1].columnComment\" : [ \"Logistic Group\" ],\r\n  \"columns[1].javaType\" : [ \"Long\" ],\r\n  \"columns[1].javaField\" : [ \"logisticGroupId\" ],\r\n  \"columns[1].isInsert\" : [ \"1\" ],\r\n  \"columns[1].isEdit\" : [ \"1\" ],\r\n  \"columns[1].isList\" : [ \"1\" ],\r\n  \"columns[1].isQuery\" : [ \"1\" ],\r\n  \"columns[1].queryType\" : [ \"EQ\" ],\r\n  \"columns[1].isRequired\" : [ \"1\" ],\r\n  \"columns[1].htmlType\" : [ \"input\" ],\r\n  \"columns[1].dictType\" : [ \"\" ],\r\n  \"columns[2].columnId\" : [ \"50\" ],\r\n  \"columns[2].sort\" : [ \"3\" ],\r\n  \"columns[2].columnComment\" : [ \"Ma Lo\" ],\r\n  \"columns[2].javaType\" : [ \"Long\" ],\r\n  \"columns[2].javaField\" : [ \"shipmentId\" ],\r\n  \"columns[2].isInsert\" : [ \"1\" ],\r\n  \"columns[2].isEdit\" : [ \"1\" ],\r\n  \"columns[2].isList\" : [ \"1\" ],\r\n  \"columns[2].isQuery\" : [ \"1\" ],\r\n  \"columns[2].queryType\" : [ \"EQ\" ],\r\n  \"columns[2].isRequired\" : [ \"1\" ],\r\n  \"columns[2].htmlType\" : [ \"input\" ],\r\n  \"columns[2].dictType\" : [ \"\" ],\r\n  \"columns[3].columnId\" : [ \"51\" ],\r\n  \"columns[3].sort\" : [ \"4\" ],\r\n  \"columns[3].columnComment\" : [ \"ID tài xế\" ],\r\n  \"columns[3].javaType\" : [ \"Long\" ],\r\n  \"columns[3].javaField\" : [ \"driverId\" ],\r\n  \"columns[3].isInsert\" : [ \"1\" ],\r\n  \"columns[3].isEdit\" : [ \"1\" ],\r\n  \"columns[3].isList\" : [ \"1\" ],\r\n  \"columns[3].isQuery\" : [ \"1\" ],\r\n  \"columns[3].queryType\" : [ \"EQ\" ],\r\n  \"columns[3].htmlType\" : [ \"input\" ],\r\n  \"columns[3].dictType\" : [ \"\" ],\r\n  \"columns[4].colu', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-30 13:42:16'),
(159, '代码生成', 2, 'vn.com.irtech.eport.generator.controller.GenController.editSave()', 'POST', 1, 'admin', 'R&D', '/tool/gen/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"tableId\" : [ \"6\" ],\r\n  \"tableName\" : [ \"pickup_hisory\" ],\r\n  \"tableComment\" : [ \"Pickup history\" ],\r\n  \"className\" : [ \"PickupHisory\" ],\r\n  \"functionAuthor\" : [ \"ruoyi\" ],\r\n  \"remark\" : [ \"\" ],\r\n  \"columns[0].columnId\" : [ \"62\" ],\r\n  \"columns[0].sort\" : [ \"1\" ],\r\n  \"columns[0].columnComment\" : [ \"ID\" ],\r\n  \"columns[0].javaType\" : [ \"Long\" ],\r\n  \"columns[0].javaField\" : [ \"id\" ],\r\n  \"columns[0].isInsert\" : [ \"1\" ],\r\n  \"columns[0].queryType\" : [ \"EQ\" ],\r\n  \"columns[0].htmlType\" : [ \"input\" ],\r\n  \"columns[0].dictType\" : [ \"\" ],\r\n  \"columns[1].columnId\" : [ \"63\" ],\r\n  \"columns[1].sort\" : [ \"2\" ],\r\n  \"columns[1].columnComment\" : [ \"Logistic Group\" ],\r\n  \"columns[1].javaType\" : [ \"Long\" ],\r\n  \"columns[1].javaField\" : [ \"logisticGroupId\" ],\r\n  \"columns[1].isInsert\" : [ \"1\" ],\r\n  \"columns[1].isEdit\" : [ \"1\" ],\r\n  \"columns[1].isList\" : [ \"1\" ],\r\n  \"columns[1].isQuery\" : [ \"1\" ],\r\n  \"columns[1].queryType\" : [ \"EQ\" ],\r\n  \"columns[1].isRequired\" : [ \"1\" ],\r\n  \"columns[1].htmlType\" : [ \"input\" ],\r\n  \"columns[1].dictType\" : [ \"\" ],\r\n  \"columns[2].columnId\" : [ \"64\" ],\r\n  \"columns[2].sort\" : [ \"3\" ],\r\n  \"columns[2].columnComment\" : [ \"Mã Lô\" ],\r\n  \"columns[2].javaType\" : [ \"Long\" ],\r\n  \"columns[2].javaField\" : [ \"shipmentId\" ],\r\n  \"columns[2].isInsert\" : [ \"1\" ],\r\n  \"columns[2].isEdit\" : [ \"1\" ],\r\n  \"columns[2].isList\" : [ \"1\" ],\r\n  \"columns[2].isQuery\" : [ \"1\" ],\r\n  \"columns[2].queryType\" : [ \"EQ\" ],\r\n  \"columns[2].isRequired\" : [ \"1\" ],\r\n  \"columns[2].htmlType\" : [ \"input\" ],\r\n  \"columns[2].dictType\" : [ \"\" ],\r\n  \"columns[3].columnId\" : [ \"65\" ],\r\n  \"columns[3].sort\" : [ \"4\" ],\r\n  \"columns[3].columnComment\" : [ \"ID Tài xế\" ],\r\n  \"columns[3].javaType\" : [ \"Long\" ],\r\n  \"columns[3].javaField\" : [ \"driverId\" ],\r\n  \"columns[3].isInsert\" : [ \"1\" ],\r\n  \"columns[3].isEdit\" : [ \"1\" ],\r\n  \"columns[3].isList\" : [ \"1\" ],\r\n  \"columns[3].isQuery\" : [ \"1\" ],\r\n  \"columns[3].queryType\" : [ \"EQ\" ],\r\n  \"columns[3].isRequired\" : [ \"1\" ],\r\n  \"columns[3].htmlType\" : [ \"input\" ],\r\n  \"columns[3].', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-30 13:42:53'),
(160, 'Parameter management', 2, 'vn.com.irtech.eport.web.controller.system.SysConfigController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/config/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"configId\" : [ \"100\" ],\r\n  \"configName\" : [ \"Da Nang Port Name\" ],\r\n  \"configKey\" : [ \"danang.port.name\" ],\r\n  \"configValue\" : [ \"Cảng Tiên Sa\" ],\r\n  \"configType\" : [ \"Y\" ],\r\n  \"remark\" : [ \"Tên cảng Đà Nãng\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-30 19:53:02'),
(161, 'Parameter management', 9, 'vn.com.irtech.eport.web.controller.system.SysConfigController.clearCache()', 'GET', 1, 'admin', 'R&D', '/system/config/clearCache', '127.0.0.1', 'Intranet IP', '{ }', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-30 19:53:15'),
(162, 'Parameter management', 9, 'vn.com.irtech.eport.web.controller.system.SysConfigController.clearCache()', 'GET', 1, 'admin', 'R&D', '/system/config/clearCache', '127.0.0.1', 'Intranet IP', '{ }', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-30 19:53:27'),
(163, 'Parameter management', 9, 'vn.com.irtech.eport.web.controller.system.SysConfigController.clearCache()', 'GET', 1, 'admin', 'R&D', '/system/config/clearCache', '127.0.0.1', 'Intranet IP', '{ }', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-06-30 19:53:30'),
(164, '代码生成', 6, 'vn.com.irtech.eport.generator.controller.GenController.importTableSave()', 'POST', 1, 'admin', 'R&D', '/tool/gen/importTable', '127.0.0.1', 'Intranet IP', '{\r\n  \"tables\" : [ \"notifications,notification_receiver,user_devices\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-06 14:08:50'),
(165, '代码生成', 8, 'vn.com.irtech.eport.generator.controller.GenController.batchGenCode()', 'GET', 1, 'admin', 'R&D', '/tool/gen/batchGenCode', '127.0.0.1', 'Intranet IP', '{\r\n  \"tables\" : [ \"notifications,notification_receiver,user_devices\" ]\r\n}', 'null', 0, NULL, '2020-07-06 14:10:23'),
(166, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwd()', 'GET', 1, 'admin', 'R&D', '/carrier/account/resetPwd/3', '127.0.0.1', 'Intranet IP', '{ }', '\"carrier/account/resetPwd\"', 0, NULL, '2020-07-07 15:20:28'),
(167, 'Reset password', 2, 'vn.com.irtech.eport.carrier.controller.CarrierAccountController.resetPwdSave()', 'POST', 1, 'admin', 'R&D', '/carrier/account/resetPwd', '127.0.0.1', 'Intranet IP', '{\r\n  \"id\" : [ \"3\" ],\r\n  \"email\" : [ \"tronghieu8531@gmail.com\" ],\r\n  \"password\" : [ \"123456\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-07 15:20:33'),
(168, 'Menu Management', 1, 'vn.com.irtech.eport.web.controller.system.SysMenuController.addSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Bộ phận cấp container\" ],\r\n  \"url\" : [ \"/container/supplier\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"1\" ],\r\n  \"icon\" : [ \"fa fa-cubes\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-07 16:21:20'),
(169, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2031\" ],\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Bộ phận cấp container\" ],\r\n  \"url\" : [ \"/container/supplier\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"6\" ],\r\n  \"icon\" : [ \"fa fa-cubes\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-07 16:21:43'),
(170, 'Menu Management', 1, 'vn.com.irtech.eport.web.controller.system.SysMenuController.addSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Quản Lý Tài Khoản\" ],\r\n  \"url\" : [ \"\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"2\" ],\r\n  \"icon\" : [ \"fa fa-bars\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:07:40'),
(171, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2000\" ],\r\n  \"parentId\" : [ \"2034\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Nhóm Hãng Tàu\" ],\r\n  \"url\" : [ \"/carrier/group\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"carrier:group:view\" ],\r\n  \"orderNum\" : [ \"1\" ],\r\n  \"icon\" : [ \"fa fa-anchor\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:08:20'),
(172, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2006\" ],\r\n  \"parentId\" : [ \"2034\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Tài Khoản Hãng Tàu\" ],\r\n  \"url\" : [ \"/carrier/account\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"carrier:account:view\" ],\r\n  \"orderNum\" : [ \"1\" ],\r\n  \"icon\" : [ \"fa fa-user-circle\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:09:28'),
(173, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2012\" ],\r\n  \"parentId\" : [ \"3\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Vận Đơn\" ],\r\n  \"url\" : [ \"/carrier/admin/do/getViewDo\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"carrier:admin:do:getViewDo:view\" ],\r\n  \"orderNum\" : [ \"1\" ],\r\n  \"icon\" : [ \"fa fa-file-excel-o\" ],\r\n  \"visible\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:12:34'),
(174, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"3\" ],\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Hỗ Trợ Kỹ Thuật\" ],\r\n  \"url\" : [ \"#\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"3\" ],\r\n  \"icon\" : [ \"fa fa-bars\" ],\r\n  \"visible\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:16:02'),
(175, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2019\" ],\r\n  \"parentId\" : [ \"2034\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Nhóm Logistic\" ],\r\n  \"url\" : [ \"/logistic/group\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"logistic:group:view\" ],\r\n  \"orderNum\" : [ \"1\" ],\r\n  \"icon\" : [ \"fa fa-truck\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:16:33'),
(176, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2020\" ],\r\n  \"parentId\" : [ \"2034\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Tài khoản Logistic\" ],\r\n  \"url\" : [ \"/logistic/account\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"logistic:account:view\" ],\r\n  \"orderNum\" : [ \"1\" ],\r\n  \"icon\" : [ \"fa fa-address-book-o\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:17:20'),
(177, 'Menu Management', 3, 'vn.com.irtech.eport.web.controller.system.SysMenuController.remove()', 'GET', 1, 'admin', 'R&D', '/system/menu/remove/2018', '127.0.0.1', 'Intranet IP', '{ }', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:17:36'),
(178, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2021\" ],\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Bộ Phận OM\" ],\r\n  \"url\" : [ \"#\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"4\" ],\r\n  \"icon\" : [ \"fa fa-address-book-o\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:24:06'),
(179, 'Menu Management', 1, 'vn.com.irtech.eport.web.controller.system.SysMenuController.addSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"parentId\" : [ \"2021\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Quản Lý eDO\" ],\r\n  \"url\" : [ \"\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"1\" ],\r\n  \"icon\" : [ \"fa fa-bars\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:25:19'),
(180, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2032\" ],\r\n  \"parentId\" : [ \"2035\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Đọc file EDI\" ],\r\n  \"url\" : [ \"/edo/manage/viewFileEdi\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"2\" ],\r\n  \"icon\" : [ \"fa fa-check-square\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:26:43'),
(181, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2033\" ],\r\n  \"parentId\" : [ \"2035\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Danh Sách eDO\" ],\r\n  \"url\" : [ \"/edo/manage/index\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"3\" ],\r\n  \"icon\" : [ \"fa fa-handshake-o\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:27:42'),
(182, 'Menu Management', 1, 'vn.com.irtech.eport.web.controller.system.SysMenuController.addSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"parentId\" : [ \"2021\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Kiểm Soát Luồng Công VIệc\" ],\r\n  \"url\" : [ \"\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"2\" ],\r\n  \"icon\" : [ \"fa fa-bars\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:29:27'),
(183, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"109\" ],\r\n  \"parentId\" : [ \"2\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Online User\" ],\r\n  \"url\" : [ \"/monitor/online\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"monitor:online:view\" ],\r\n  \"orderNum\" : [ \"1\" ],\r\n  \"icon\" : [ \"#\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:30:52'),
(184, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2026\" ],\r\n  \"parentId\" : [ \"2036\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Quản lý robot\" ],\r\n  \"url\" : [ \"system/robot/index\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"1\" ],\r\n  \"icon\" : [ \"fa fa-cogs\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:31:18'),
(185, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2\" ],\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"System Monitoring\" ],\r\n  \"url\" : [ \"#\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"2\" ],\r\n  \"icon\" : [ \"fa fa-video-camera\" ],\r\n  \"visible\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:31:34'),
(186, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"100\" ],\r\n  \"parentId\" : [ \"1\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Quản Lý Người Dùng\" ],\r\n  \"url\" : [ \"/system/user\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"system:user:view\" ],\r\n  \"orderNum\" : [ \"1\" ],\r\n  \"icon\" : [ \"fa fa-address-book-o\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:32:35'),
(187, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"101\" ],\r\n  \"parentId\" : [ \"1\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Quản Lý Vai Trò\" ],\r\n  \"url\" : [ \"/system/role\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"system:role:view\" ],\r\n  \"orderNum\" : [ \"2\" ],\r\n  \"icon\" : [ \"fa fa-users\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:32:51'),
(188, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"102\" ],\r\n  \"parentId\" : [ \"1\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Danh Mục\" ],\r\n  \"url\" : [ \"/system/menu\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"system:menu:view\" ],\r\n  \"orderNum\" : [ \"3\" ],\r\n  \"icon\" : [ \"fa fa-bars\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:33:05'),
(189, 'Menu Management', 1, 'vn.com.irtech.eport.web.controller.system.SysMenuController.addSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"parentId\" : [ \"2021\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Thông Báo\" ],\r\n  \"url\" : [ \"\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"3\" ],\r\n  \"icon\" : [ \"fa fa-bell\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:35:32'),
(190, 'Menu Management', 1, 'vn.com.irtech.eport.web.controller.system.SysMenuController.addSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Quản Lý Thông Báo\" ],\r\n  \"url\" : [ \"/notifications\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"1\" ],\r\n  \"icon\" : [ \"fa fa-bell\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:36:00'),
(191, 'Menu Management', 3, 'vn.com.irtech.eport.web.controller.system.SysMenuController.remove()', 'GET', 1, 'admin', 'R&D', '/system/menu/remove/2038', '127.0.0.1', 'Intranet IP', '{ }', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:36:26'),
(192, 'Menu Management', 1, 'vn.com.irtech.eport.web.controller.system.SysMenuController.addSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"parentId\" : [ \"2037\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Quản Lý Thông Báo\" ],\r\n  \"url\" : [ \"\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"1\" ],\r\n  \"icon\" : [ \"\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:37:00'),
(193, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2026\" ],\r\n  \"parentId\" : [ \"2036\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Giám Sát Robot\" ],\r\n  \"url\" : [ \"system/robot/index\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"1\" ],\r\n  \"icon\" : [ \"fa fa-cogs\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:37:46'),
(194, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2024\" ],\r\n  \"parentId\" : [ \"2036\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Hỗ trợ Robot làm lệnh\" ],\r\n  \"url\" : [ \"/om/executeCatos/index\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"2\" ],\r\n  \"icon\" : [ \"fa fa-navicon\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:38:51'),
(195, 'Menu Management', 3, 'vn.com.irtech.eport.web.controller.system.SysMenuController.remove()', 'GET', 1, 'admin', 'R&D', '/system/menu/remove/2023', '127.0.0.1', 'Intranet IP', '{ }', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:38:58'),
(196, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2039\" ],\r\n  \"parentId\" : [ \"2037\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Quản Lý Thông Báo\" ],\r\n  \"url\" : [ \"/notifications\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"1\" ],\r\n  \"icon\" : [ \"#\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:39:53'),
(197, 'Menu Management', 1, 'vn.com.irtech.eport.web.controller.system.SysMenuController.addSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Gửi Thông Báo\" ],\r\n  \"url\" : [ \"/notification/add\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"2\" ],\r\n  \"icon\" : [ \"fa fa-bell-o\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:40:55');
INSERT INTO `sys_oper_log` (`oper_id`, `title`, `business_type`, `method`, `request_method`, `operator_type`, `oper_name`, `dept_name`, `oper_url`, `oper_ip`, `oper_location`, `oper_param`, `json_result`, `status`, `error_msg`, `oper_time`) VALUES
(198, 'Menu Management', 3, 'vn.com.irtech.eport.web.controller.system.SysMenuController.remove()', 'GET', 1, 'admin', 'R&D', '/system/menu/remove/2040', '127.0.0.1', 'Intranet IP', '{ }', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:41:07'),
(199, 'Menu Management', 1, 'vn.com.irtech.eport.web.controller.system.SysMenuController.addSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"parentId\" : [ \"2037\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Gửi Thông Báo\" ],\r\n  \"url\" : [ \"/notification/add\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"2\" ],\r\n  \"icon\" : [ \"fa fa-commenting-o\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:42:02'),
(200, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2041\" ],\r\n  \"parentId\" : [ \"2037\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Gửi Thông Báo\" ],\r\n  \"url\" : [ \"/notifications/add\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"2\" ],\r\n  \"icon\" : [ \"fa fa-commenting-o\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:42:27'),
(201, 'Menu Management', 1, 'vn.com.irtech.eport.web.controller.system.SysMenuController.addSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Lịch Sử\" ],\r\n  \"url\" : [ \"\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"4\" ],\r\n  \"icon\" : [ \"fa fa-book\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:43:07'),
(202, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2028\" ],\r\n  \"parentId\" : [ \"2042\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Hoạt động của xe và nhật ký giao nhận\" ],\r\n  \"url\" : [ \"/history/truck/\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"1\" ],\r\n  \"icon\" : [ \"fa fa-bus\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:43:36'),
(203, 'Menu Management', 1, 'vn.com.irtech.eport.web.controller.system.SysMenuController.addSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/add', '127.0.0.1', 'Intranet IP', '{\r\n  \"parentId\" : [ \"2021\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Lịch Sử\" ],\r\n  \"url\" : [ \"\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"4\" ],\r\n  \"icon\" : [ \"fa fa-book\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:44:28'),
(204, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2028\" ],\r\n  \"parentId\" : [ \"2043\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Hoạt động của xe và nhật ký giao nhận\" ],\r\n  \"url\" : [ \"/history/truck/\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"1\" ],\r\n  \"icon\" : [ \"fa fa-bus\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:44:54'),
(205, 'Menu Management', 3, 'vn.com.irtech.eport.web.controller.system.SysMenuController.remove()', 'GET', 1, 'admin', 'R&D', '/system/menu/remove/2042', '127.0.0.1', 'Intranet IP', '{ }', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:45:07'),
(206, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2030\" ],\r\n  \"parentId\" : [ \"2043\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Lịch sử robot\" ],\r\n  \"url\" : [ \"/history/robot/\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"2\" ],\r\n  \"icon\" : [ \"fa fa-cogs\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:45:31'),
(207, 'Menu Management', 3, 'vn.com.irtech.eport.web.controller.system.SysMenuController.remove()', 'GET', 1, 'admin', 'R&D', '/system/menu/remove/2027', '127.0.0.1', 'Intranet IP', '{ }', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:45:37'),
(208, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2034\" ],\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Quản Lý Tài Khoản\" ],\r\n  \"url\" : [ \"#\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"3\" ],\r\n  \"icon\" : [ \"fa fa-bars\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:45:49'),
(209, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2034\" ],\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Quản Lý Tài Khoản\" ],\r\n  \"url\" : [ \"#\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"4\" ],\r\n  \"icon\" : [ \"fa fa-bars\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:45:55'),
(210, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2034\" ],\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Quản Lý Tài Khoản\" ],\r\n  \"url\" : [ \"#\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"5\" ],\r\n  \"icon\" : [ \"fa fa-bars\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:46:00'),
(211, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2034\" ],\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Quản Lý Tài Khoản\" ],\r\n  \"url\" : [ \"#\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"6\" ],\r\n  \"icon\" : [ \"fa fa-bars\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:46:04'),
(212, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2034\" ],\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Quản Lý Tài Khoản\" ],\r\n  \"url\" : [ \"#\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"7\" ],\r\n  \"icon\" : [ \"fa fa-bars\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:46:09'),
(213, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2034\" ],\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Quản Lý Tài Khoản\" ],\r\n  \"url\" : [ \"#\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"2\" ],\r\n  \"icon\" : [ \"fa fa-bars\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:46:38'),
(214, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2034\" ],\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Quản Lý Tài Khoản\" ],\r\n  \"url\" : [ \"#\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"3\" ],\r\n  \"icon\" : [ \"fa fa-bars\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:46:49'),
(215, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"3\" ],\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Hỗ Trợ Kỹ Thuật\" ],\r\n  \"url\" : [ \"#\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"4\" ],\r\n  \"icon\" : [ \"fa fa-bars\" ],\r\n  \"visible\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:46:55'),
(216, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"3\" ],\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Hỗ Trợ Kỹ Thuật\" ],\r\n  \"url\" : [ \"#\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"2\" ],\r\n  \"icon\" : [ \"fa fa-bars\" ],\r\n  \"visible\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:47:06'),
(217, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2\" ],\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"System Monitoring\" ],\r\n  \"url\" : [ \"#\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"3\" ],\r\n  \"icon\" : [ \"fa fa-video-camera\" ],\r\n  \"visible\" : [ \"1\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:47:11'),
(218, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2034\" ],\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Quản Lý Tài Khoản\" ],\r\n  \"url\" : [ \"#\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"4\" ],\r\n  \"icon\" : [ \"fa fa-bars\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:47:15'),
(219, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2021\" ],\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Bộ Phận OM\" ],\r\n  \"url\" : [ \"#\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"5\" ],\r\n  \"icon\" : [ \"fa fa-address-book-o\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:47:20'),
(220, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2022\" ],\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"M\" ],\r\n  \"menuName\" : [ \"Kế Hoạch Bãi Cảng\" ],\r\n  \"url\" : [ \"#\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"6\" ],\r\n  \"icon\" : [ \"fa fa-anchor\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:47:27'),
(221, 'Menu Management', 2, 'vn.com.irtech.eport.web.controller.system.SysMenuController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/menu/edit', '127.0.0.1', 'Intranet IP', '{\r\n  \"menuId\" : [ \"2031\" ],\r\n  \"parentId\" : [ \"0\" ],\r\n  \"menuType\" : [ \"C\" ],\r\n  \"menuName\" : [ \"Bộ phận cấp container\" ],\r\n  \"url\" : [ \"/container/supplier\" ],\r\n  \"target\" : [ \"menuItem\" ],\r\n  \"perms\" : [ \"\" ],\r\n  \"orderNum\" : [ \"7\" ],\r\n  \"icon\" : [ \"fa fa-cubes\" ],\r\n  \"visible\" : [ \"0\" ]\r\n}', '{\r\n  \"msg\" : \"Thành công\",\r\n  \"code\" : 0\r\n}', 0, NULL, '2020-07-09 11:47:32');

-- --------------------------------------------------------

--
-- Table structure for table `sys_otp`
--

CREATE TABLE `sys_otp` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `opt_code` varchar(10) COLLATE utf8_bin NOT NULL COMMENT 'OTP CODE',
  `transaction_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'transaction id',
  `otp_type` char(1) COLLATE utf8_bin NOT NULL COMMENT 'Loai OTP (1 - lamlenh, 2 quen mat khau)',
  `phone_number` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Số điện thoại nhận',
  `msg_status` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Status send mess',
  `verify_status` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT 'Verify send mess',
  `expired_time` datetime NOT NULL COMMENT 'Expired Time',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='otp Code';

-- --------------------------------------------------------

--
-- Table structure for table `sys_post`
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
-- Dumping data for table `sys_post`
--

INSERT INTO `sys_post` (`post_id`, `post_code`, `post_name`, `post_sort`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(1, 'ceo', 'Chairman', 1, '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(2, 'se', 'project manager', 2, '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(3, 'hr', 'Human Resources', 3, '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', ''),
(4, 'user', 'General staff', 4, '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');

-- --------------------------------------------------------

--
-- Table structure for table `sys_robot`
--

CREATE TABLE `sys_robot` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `uuid` varchar(64) COLLATE utf8_bin NOT NULL COMMENT 'uuid',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Status（0 Available 1 BUSY 2 OFFLINE）',
  `is_receive_cont_full_order` tinyint(1) DEFAULT 0 COMMENT 'Is support receive container full order',
  `is_send_cont_empty_order` tinyint(1) DEFAULT 0 COMMENT 'Is support send container empty order',
  `is_receive_cont_empty_order` tinyint(1) DEFAULT 0 COMMENT 'Is support receive container empty order',
  `is_send_cont_full_order` tinyint(1) DEFAULT 0 COMMENT 'Is support send container full order',
  `is_gate_in_order` tinyint(1) DEFAULT 0 COMMENT 'Is support gate in order',
  `ip_address` varchar(16) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ip address',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Robot';

--
-- Dumping data for table `sys_robot`
--

INSERT INTO `sys_robot` (`id`, `uuid`, `status`, `is_receive_cont_full_order`, `is_send_cont_empty_order`, `is_receive_cont_empty_order`, `is_send_cont_full_order`, `is_gate_in_order`, `ip_address`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
(4, 'PC', '2', 1, 0, 1, 0, 0, NULL, NULL, '', '2020-07-08 20:15:27', '', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `sys_role`
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
-- Dumping data for table `sys_role`
--

INSERT INTO `sys_role` (`role_id`, `role_name`, `role_key`, `role_sort`, `data_scope`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(1, 'Administrator', 'admin', 1, '1', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'Quản trị viên'),
(2, 'Normal User', 'common', 2, '2', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'Nhân viên bình thường');

-- --------------------------------------------------------

--
-- Table structure for table `sys_role_dept`
--

CREATE TABLE `sys_role_dept` (
  `role_id` bigint(20) NOT NULL COMMENT 'Role ID',
  `dept_id` bigint(20) NOT NULL COMMENT 'Department ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Role and department';

--
-- Dumping data for table `sys_role_dept`
--

INSERT INTO `sys_role_dept` (`role_id`, `dept_id`) VALUES
(2, 100),
(2, 101),
(2, 105);

-- --------------------------------------------------------

--
-- Table structure for table `sys_role_menu`
--

CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT 'Role ID',
  `menu_id` bigint(20) NOT NULL COMMENT 'Menu ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Role and menu association';

--
-- Dumping data for table `sys_role_menu`
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
-- Table structure for table `sys_user`
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
-- Dumping data for table `sys_user`
--

INSERT INTO `sys_user` (`user_id`, `dept_id`, `login_name`, `user_name`, `user_type`, `email`, `phonenumber`, `sex`, `avatar`, `password`, `salt`, `status`, `del_flag`, `login_ip`, `login_date`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(1, 103, 'admin', 'DNG', '00', 'ry@163.com', '15888888888', '1', '', '29c67a30398638269fe600f73a054934', '111111', '0', '0', '127.0.0.1', '2020-07-09 10:57:29', 'admin', '2018-03-16 11:33:00', 'ry', '2020-07-09 10:57:29', '管理员'),
(2, 105, 'ry', 'DNG', '00', 'ry@qq.com', '15666666666', '1', '', '8e6d98b90472783cc73c17047ddccf36', '222222', '0', '0', '127.0.0.1', '2018-03-16 11:33:00', 'admin', '2018-03-16 11:33:00', 'ry', '2020-03-28 07:21:51', '测试员');

-- --------------------------------------------------------

--
-- Table structure for table `sys_user_online`
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
-- Dumping data for table `sys_user_online`
--

INSERT INTO `sys_user_online` (`sessionId`, `login_name`, `dept_name`, `ipaddr`, `login_location`, `browser`, `os`, `status`, `start_timestamp`, `last_access_time`, `expire_time`) VALUES
('329058d8-8f9e-4f22-b40a-c44e39bbb6c7', 'admin', 'R&D', '127.0.0.1', 'Intranet IP', 'Chrome 8', 'Windows 10', 'on_line', '2020-07-09 10:23:43', '2020-07-09 11:48:03', 1800000);

-- --------------------------------------------------------

--
-- Table structure for table `sys_user_post`
--

CREATE TABLE `sys_user_post` (
  `user_id` bigint(20) NOT NULL COMMENT 'User ID',
  `post_id` bigint(20) NOT NULL COMMENT 'Post ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='User and post';

--
-- Dumping data for table `sys_user_post`
--

INSERT INTO `sys_user_post` (`user_id`, `post_id`) VALUES
(1, 1),
(2, 2);

-- --------------------------------------------------------

--
-- Table structure for table `sys_user_role`
--

CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT 'User ID',
  `role_id` bigint(20) NOT NULL COMMENT 'Role ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='User and role association';

--
-- Dumping data for table `sys_user_role`
--

INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 2);

-- --------------------------------------------------------

--
-- Table structure for table `user_devices`
--

CREATE TABLE `user_devices` (
  `id` int(11) NOT NULL,
  `user_token` varchar(100) COLLATE utf8_bin NOT NULL,
  `device_token` varchar(100) COLLATE utf8_bin NOT NULL,
  `user_type` int(11) NOT NULL COMMENT '1: Logistic, 2: Driver, 3: Staff',
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(50) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `carrier_account`
--
ALTER TABLE `carrier_account`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `carrier_group`
--
ALTER TABLE `carrier_group`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `driver_account`
--
ALTER TABLE `driver_account`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `driver_truck`
--
ALTER TABLE `driver_truck`
  ADD PRIMARY KEY (`driver_id`,`truck_id`);

--
-- Indexes for table `edo`
--
ALTER TABLE `edo`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `edo_audit_log`
--
ALTER TABLE `edo_audit_log`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `edo_history`
--
ALTER TABLE `edo_history`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `equipment_do`
--
ALTER TABLE `equipment_do`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `gen_table`
--
ALTER TABLE `gen_table`
  ADD PRIMARY KEY (`table_id`);

--
-- Indexes for table `gen_table_column`
--
ALTER TABLE `gen_table_column`
  ADD PRIMARY KEY (`column_id`);

--
-- Indexes for table `logistic_account`
--
ALTER TABLE `logistic_account`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `logistic_group`
--
ALTER TABLE `logistic_group`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `logistic_truck`
--
ALTER TABLE `logistic_truck`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `notification_receiver`
--
ALTER TABLE `notification_receiver`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pickup_assign`
--
ALTER TABLE `pickup_assign`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pickup_history`
--
ALTER TABLE `pickup_history`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `process_bill`
--
ALTER TABLE `process_bill`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `process_history`
--
ALTER TABLE `process_history`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `process_order`
--
ALTER TABLE `process_order`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `qrtz_blob_triggers`
--
ALTER TABLE `qrtz_blob_triggers`
  ADD PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`);

--
-- Indexes for table `qrtz_calendars`
--
ALTER TABLE `qrtz_calendars`
  ADD PRIMARY KEY (`sched_name`,`calendar_name`);

--
-- Indexes for table `qrtz_cron_triggers`
--
ALTER TABLE `qrtz_cron_triggers`
  ADD PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`);

--
-- Indexes for table `qrtz_fired_triggers`
--
ALTER TABLE `qrtz_fired_triggers`
  ADD PRIMARY KEY (`sched_name`,`entry_id`);

--
-- Indexes for table `qrtz_job_details`
--
ALTER TABLE `qrtz_job_details`
  ADD PRIMARY KEY (`sched_name`,`job_name`,`job_group`);

--
-- Indexes for table `qrtz_locks`
--
ALTER TABLE `qrtz_locks`
  ADD PRIMARY KEY (`sched_name`,`lock_name`);

--
-- Indexes for table `qrtz_paused_trigger_grps`
--
ALTER TABLE `qrtz_paused_trigger_grps`
  ADD PRIMARY KEY (`sched_name`,`trigger_group`);

--
-- Indexes for table `qrtz_scheduler_state`
--
ALTER TABLE `qrtz_scheduler_state`
  ADD PRIMARY KEY (`sched_name`,`instance_name`);

--
-- Indexes for table `qrtz_simple_triggers`
--
ALTER TABLE `qrtz_simple_triggers`
  ADD PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`);

--
-- Indexes for table `qrtz_simprop_triggers`
--
ALTER TABLE `qrtz_simprop_triggers`
  ADD PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`);

--
-- Indexes for table `qrtz_triggers`
--
ALTER TABLE `qrtz_triggers`
  ADD PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  ADD KEY `sched_name` (`sched_name`,`job_name`,`job_group`);

--
-- Indexes for table `shipment`
--
ALTER TABLE `shipment`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `shipment_custom`
--
ALTER TABLE `shipment_custom`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `shipment_detail`
--
ALTER TABLE `shipment_detail`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sys_config`
--
ALTER TABLE `sys_config`
  ADD PRIMARY KEY (`config_id`);

--
-- Indexes for table `sys_dept`
--
ALTER TABLE `sys_dept`
  ADD PRIMARY KEY (`dept_id`);

--
-- Indexes for table `sys_dict_data`
--
ALTER TABLE `sys_dict_data`
  ADD PRIMARY KEY (`dict_code`);

--
-- Indexes for table `sys_dict_type`
--
ALTER TABLE `sys_dict_type`
  ADD PRIMARY KEY (`dict_id`),
  ADD UNIQUE KEY `dict_type` (`dict_type`);

--
-- Indexes for table `sys_job`
--
ALTER TABLE `sys_job`
  ADD PRIMARY KEY (`job_id`,`job_name`,`job_group`);

--
-- Indexes for table `sys_job_log`
--
ALTER TABLE `sys_job_log`
  ADD PRIMARY KEY (`job_log_id`);

--
-- Indexes for table `sys_logininfor`
--
ALTER TABLE `sys_logininfor`
  ADD PRIMARY KEY (`info_id`);

--
-- Indexes for table `sys_menu`
--
ALTER TABLE `sys_menu`
  ADD PRIMARY KEY (`menu_id`);

--
-- Indexes for table `sys_notice`
--
ALTER TABLE `sys_notice`
  ADD PRIMARY KEY (`notice_id`);

--
-- Indexes for table `sys_oper_log`
--
ALTER TABLE `sys_oper_log`
  ADD PRIMARY KEY (`oper_id`);

--
-- Indexes for table `sys_otp`
--
ALTER TABLE `sys_otp`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sys_post`
--
ALTER TABLE `sys_post`
  ADD PRIMARY KEY (`post_id`);

--
-- Indexes for table `sys_robot`
--
ALTER TABLE `sys_robot`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UC_uuid` (`uuid`);

--
-- Indexes for table `sys_role`
--
ALTER TABLE `sys_role`
  ADD PRIMARY KEY (`role_id`);

--
-- Indexes for table `sys_role_dept`
--
ALTER TABLE `sys_role_dept`
  ADD PRIMARY KEY (`role_id`,`dept_id`);

--
-- Indexes for table `sys_role_menu`
--
ALTER TABLE `sys_role_menu`
  ADD PRIMARY KEY (`role_id`,`menu_id`);

--
-- Indexes for table `sys_user`
--
ALTER TABLE `sys_user`
  ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `sys_user_online`
--
ALTER TABLE `sys_user_online`
  ADD PRIMARY KEY (`sessionId`);

--
-- Indexes for table `sys_user_post`
--
ALTER TABLE `sys_user_post`
  ADD PRIMARY KEY (`user_id`,`post_id`);

--
-- Indexes for table `sys_user_role`
--
ALTER TABLE `sys_user_role`
  ADD PRIMARY KEY (`user_id`,`role_id`);

--
-- Indexes for table `user_devices`
--
ALTER TABLE `user_devices`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `carrier_account`
--
ALTER TABLE `carrier_account`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID', AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `carrier_group`
--
ALTER TABLE `carrier_group`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID', AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `driver_account`
--
ALTER TABLE `driver_account`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID', AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `edo`
--
ALTER TABLE `edo`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID';

--
-- AUTO_INCREMENT for table `edo_audit_log`
--
ALTER TABLE `edo_audit_log`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID';

--
-- AUTO_INCREMENT for table `edo_history`
--
ALTER TABLE `edo_history`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID';

--
-- AUTO_INCREMENT for table `equipment_do`
--
ALTER TABLE `equipment_do`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID';

--
-- AUTO_INCREMENT for table `gen_table`
--
ALTER TABLE `gen_table`
  MODIFY `table_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号', AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `gen_table_column`
--
ALTER TABLE `gen_table_column`
  MODIFY `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号', AUTO_INCREMENT=103;

--
-- AUTO_INCREMENT for table `logistic_account`
--
ALTER TABLE `logistic_account`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID', AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `logistic_group`
--
ALTER TABLE `logistic_group`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID', AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `logistic_truck`
--
ALTER TABLE `logistic_truck`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID';

--
-- AUTO_INCREMENT for table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `notification_receiver`
--
ALTER TABLE `notification_receiver`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pickup_assign`
--
ALTER TABLE `pickup_assign`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID';

--
-- AUTO_INCREMENT for table `pickup_history`
--
ALTER TABLE `pickup_history`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID';

--
-- AUTO_INCREMENT for table `process_bill`
--
ALTER TABLE `process_bill`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID';

--
-- AUTO_INCREMENT for table `process_history`
--
ALTER TABLE `process_history`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID';

--
-- AUTO_INCREMENT for table `process_order`
--
ALTER TABLE `process_order`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID';

--
-- AUTO_INCREMENT for table `shipment`
--
ALTER TABLE `shipment`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `shipment_custom`
--
ALTER TABLE `shipment_custom`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID';

--
-- AUTO_INCREMENT for table `shipment_detail`
--
ALTER TABLE `shipment_detail`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID';

--
-- AUTO_INCREMENT for table `sys_config`
--
ALTER TABLE `sys_config`
  MODIFY `config_id` int(5) NOT NULL AUTO_INCREMENT COMMENT '参数主键', AUTO_INCREMENT=103;

--
-- AUTO_INCREMENT for table `sys_dept`
--
ALTER TABLE `sys_dept`
  MODIFY `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Department id', AUTO_INCREMENT=200;

--
-- AUTO_INCREMENT for table `sys_dict_data`
--
ALTER TABLE `sys_dict_data`
  MODIFY `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码', AUTO_INCREMENT=100;

--
-- AUTO_INCREMENT for table `sys_dict_type`
--
ALTER TABLE `sys_dict_type`
  MODIFY `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Dictionary ID', AUTO_INCREMENT=100;

--
-- AUTO_INCREMENT for table `sys_job`
--
ALTER TABLE `sys_job`
  MODIFY `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID', AUTO_INCREMENT=100;

--
-- AUTO_INCREMENT for table `sys_job_log`
--
ALTER TABLE `sys_job_log`
  MODIFY `job_log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID';

--
-- AUTO_INCREMENT for table `sys_logininfor`
--
ALTER TABLE `sys_logininfor`
  MODIFY `info_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID', AUTO_INCREMENT=738;

--
-- AUTO_INCREMENT for table `sys_menu`
--
ALTER TABLE `sys_menu`
  MODIFY `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Menu ID', AUTO_INCREMENT=2044;

--
-- AUTO_INCREMENT for table `sys_notice`
--
ALTER TABLE `sys_notice`
  MODIFY `notice_id` int(4) NOT NULL AUTO_INCREMENT COMMENT '公告ID', AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `sys_oper_log`
--
ALTER TABLE `sys_oper_log`
  MODIFY `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Log PK', AUTO_INCREMENT=222;

--
-- AUTO_INCREMENT for table `sys_otp`
--
ALTER TABLE `sys_otp`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID';

--
-- AUTO_INCREMENT for table `sys_post`
--
ALTER TABLE `sys_post`
  MODIFY `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Job ID', AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `sys_robot`
--
ALTER TABLE `sys_robot`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID', AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `sys_role`
--
ALTER TABLE `sys_role`
  MODIFY `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Role ID', AUTO_INCREMENT=100;

--
-- AUTO_INCREMENT for table `sys_user`
--
ALTER TABLE `sys_user`
  MODIFY `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'User ID', AUTO_INCREMENT=100;

--
-- AUTO_INCREMENT for table `user_devices`
--
ALTER TABLE `user_devices`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `qrtz_blob_triggers`
--
ALTER TABLE `qrtz_blob_triggers`
  ADD CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`,`trigger_name`,`trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`);

--
-- Constraints for table `qrtz_cron_triggers`
--
ALTER TABLE `qrtz_cron_triggers`
  ADD CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`,`trigger_name`,`trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`);

--
-- Constraints for table `qrtz_simple_triggers`
--
ALTER TABLE `qrtz_simple_triggers`
  ADD CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`,`trigger_name`,`trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`);

--
-- Constraints for table `qrtz_simprop_triggers`
--
ALTER TABLE `qrtz_simprop_triggers`
  ADD CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`,`trigger_name`,`trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`);

--
-- Constraints for table `qrtz_triggers`
--
ALTER TABLE `qrtz_triggers`
  ADD CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`,`job_name`,`job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
