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

-- Dumping structure for table eport.gen_table
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

-- Dumping data for table eport.gen_table_column: ~0 rows (approximately)
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

-- Dumping structure for table eport.sys_config
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
) ENGINE=InnoDB AUTO_INCREMENT=118 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统访问记录';

-- Dumping data for table eport.sys_logininfor: ~0 rows (approximately)
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
	(117, 'admin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '0', '登录成功', '2020-03-30 00:51:42');
/*!40000 ALTER TABLE `sys_logininfor` ENABLE KEYS */;

-- Dumping structure for table eport.sys_menu
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
) ENGINE=InnoDB AUTO_INCREMENT=2000 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Menu permission';

-- Dumping data for table eport.sys_menu: ~83 rows (approximately)
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
	(1, 'System Management', 0, 1, '#', '', 'M', '0', '', 'fa fa-gear', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'System Management目录'),
	(2, 'System Monitoring', 0, 2, '#', '', 'M', '0', '', 'fa fa-video-camera', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'System Monitoring目录'),
	(3, 'System Tools', 0, 3, '#', '', 'M', '0', '', 'fa fa-bars', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'System Tools目录'),
	(100, 'User Management', 1, 1, '/system/user', '', 'C', '0', 'system:user:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'User Management菜单'),
	(101, 'Role Management', 1, 2, '/system/role', '', 'C', '0', 'system:role:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', 'Role Management菜单'),
	(102, 'Menu', 1, 3, '/system/menu', '', 'C', '0', 'system:menu:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '菜单管理菜单'),
	(103, 'Department', 1, 4, '/system/dept', '', 'C', '0', 'system:dept:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '部门管理菜单'),
	(104, 'Post', 1, 5, '/system/post', '', 'C', '0', 'system:post:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '岗位管理菜单'),
	(105, 'Dictionary', 1, 6, '/system/dict', '', 'C', '0', 'system:dict:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '字典管理菜单'),
	(106, 'Config', 1, 7, '/system/config', '', 'C', '0', 'system:config:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '参数设置菜单'),
	(107, 'Notification', 1, 8, '/system/notice', '', 'C', '0', 'system:notice:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '通知公告菜单'),
	(108, 'Log Management', 1, 9, '#', '', 'M', '0', '', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '日志管理菜单'),
	(109, 'Online User', 2, 1, '/monitor/online', '', 'C', '0', 'monitor:online:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '在线用户菜单'),
	(110, 'Job', 2, 2, '/monitor/job', '', 'C', '0', 'monitor:job:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '定时任务菜单'),
	(111, 'Data Monitor', 2, 3, '/monitor/data', '', 'C', '0', 'monitor:data:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '数据监控菜单'),
	(112, 'Server Monitor', 2, 3, '/monitor/server', '', 'C', '0', 'monitor:server:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '服务监控菜单'),
	(113, 'Form Building', 3, 1, '/tool/build', '', 'C', '0', 'tool:build:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '表单构建菜单'),
	(114, 'Code Gen', 3, 2, '/tool/gen', '', 'C', '0', 'tool:gen:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '代码生成菜单'),
	(115, 'API', 3, 3, '/tool/swagger', '', 'C', '0', 'tool:swagger:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '系统接口菜单'),
	(500, 'Operator Log', 108, 1, '/monitor/operlog', '', 'C', '0', 'monitor:operlog:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '操作日志菜单'),
	(501, 'Login Log', 108, 2, '/monitor/logininfor', '', 'C', '0', 'monitor:logininfor:view', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '登录日志菜单'),
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
	(1061, 'Code Gen', 114, 5, '#', '', 'F', '0', 'tool:gen:code', '#', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;

-- Dumping structure for table eport.sys_notice
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
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Operational logging';

-- Dumping data for table eport.sys_oper_log: ~0 rows (approximately)
/*!40000 ALTER TABLE `sys_oper_log` DISABLE KEYS */;
INSERT IGNORE INTO `sys_oper_log` (`oper_id`, `title`, `business_type`, `method`, `request_method`, `operator_type`, `oper_name`, `dept_name`, `oper_url`, `oper_ip`, `oper_location`, `oper_param`, `json_result`, `status`, `error_msg`, `oper_time`) VALUES
	(100, '代码生成', 6, 'com.ruoyi.generator.controller.GenController.importTableSave()', 'POST', 1, 'admin', 'R&D', '/tool/gen/importTable', '127.0.0.1', '内网IP', '{\r\n  "tables" : [ "sys_user_post" ]\r\n}', '{\r\n  "msg" : "操作成功",\r\n  "code" : 0\r\n}', 0, NULL, '2020-03-28 07:19:06'),
	(101, '代码生成', 3, 'com.ruoyi.generator.controller.GenController.remove()', 'POST', 1, 'admin', 'R&D', '/tool/gen/remove', '127.0.0.1', '内网IP', '{\r\n  "ids" : [ "1" ]\r\n}', '{\r\n  "msg" : "操作成功",\r\n  "code" : 0\r\n}', 0, NULL, '2020-03-28 07:19:37'),
	(102, '用户管理', 2, 'com.ruoyi.web.controller.system.SysUserController.changeStatus()', 'POST', 1, 'admin', 'R&D', '/system/user/changeStatus', '127.0.0.1', '内网IP', '{\r\n  "userId" : [ "1" ],\r\n  "status" : [ "1" ]\r\n}', 'null', 1, '不允许操作超级管理员用户', '2020-03-28 07:21:44'),
	(103, '用户管理', 2, 'com.ruoyi.web.controller.system.SysUserController.changeStatus()', 'POST', 1, 'admin', 'R&D', '/system/user/changeStatus', '127.0.0.1', '内网IP', '{\r\n  "userId" : [ "2" ],\r\n  "status" : [ "1" ]\r\n}', '{\r\n  "msg" : "操作成功",\r\n  "code" : 0\r\n}', 0, NULL, '2020-03-28 07:21:48'),
	(104, '用户管理', 2, 'com.ruoyi.web.controller.system.SysUserController.changeStatus()', 'POST', 1, 'admin', 'R&D', '/system/user/changeStatus', '127.0.0.1', '内网IP', '{\r\n  "userId" : [ "2" ],\r\n  "status" : [ "0" ]\r\n}', '{\r\n  "msg" : "操作成功",\r\n  "code" : 0\r\n}', 0, NULL, '2020-03-28 07:21:51'),
	(105, '账户解锁', 0, 'com.ruoyi.web.controller.monitor.SysLogininforController.unlock()', 'POST', 1, 'admin', 'R&D', '/monitor/logininfor/unlock', '127.0.0.1', '内网IP', '{\r\n  "loginName" : [ "admin" ]\r\n}', '{\r\n  "msg" : "操作成功",\r\n  "code" : 0\r\n}', 0, NULL, '2020-03-28 14:22:43'),
	(106, '账户解锁', 0, 'com.ruoyi.web.controller.monitor.SysLogininforController.unlock()', 'POST', 1, 'admin', 'R&D', '/monitor/logininfor/unlock', '127.0.0.1', '内网IP', '{\r\n  "loginName" : [ "admin" ]\r\n}', '{\r\n  "msg" : "操作成功",\r\n  "code" : 0\r\n}', 0, NULL, '2020-03-28 14:22:47'),
	(107, '通知公告', 2, 'com.ruoyi.web.controller.system.SysNoticeController.editSave()', 'POST', 1, 'admin', 'R&D', '/system/notice/edit', '127.0.0.1', '内网IP', '{\r\n  "noticeId" : [ "1" ],\r\n  "noticeTitle" : [ "Warm reminder: 2018-07-01 DNG new version released" ],\r\n  "noticeType" : [ "2" ],\r\n  "noticeContent" : [ "New version content" ],\r\n  "status" : [ "0" ]\r\n}', '{\r\n  "msg" : "操作成功",\r\n  "code" : 0\r\n}', 0, NULL, '2020-03-28 14:23:04'),
	(108, '代码生成', 6, 'vn.com.irtech.eport.generator.controller.GenController.importTableSave()', 'POST', 1, 'admin', 'R&D', '/tool/gen/importTable', '127.0.0.1', '内网IP', '{\r\n  "tables" : [ "sys_post" ]\r\n}', '{\r\n  "msg" : "操作成功",\r\n  "code" : 0\r\n}', 0, NULL, '2020-03-30 00:54:19');
/*!40000 ALTER TABLE `sys_oper_log` ENABLE KEYS */;

-- Dumping structure for table eport.sys_post
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

-- Dumping structure for table eport.sys_role
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
	(1, 'Administrator', 'admin', 1, '1', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '管理员'),
	(2, 'Normal User', 'common', 2, '2', '0', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '普通角色');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;

-- Dumping structure for table eport.sys_role_dept
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
	(1, 103, 'admin', 'DNG', '00', 'ry@163.com', '15888888888', '1', '', '29c67a30398638269fe600f73a054934', '111111', '0', '0', '127.0.0.1', '2020-03-30 00:51:42', 'admin', '2018-03-16 11:33:00', 'ry', '2020-03-30 00:51:42', '管理员'),
	(2, 105, 'ry', 'DNG', '00', 'ry@qq.com', '15666666666', '1', '', '8e6d98b90472783cc73c17047ddccf36', '222222', '0', '0', '127.0.0.1', '2018-03-16 11:33:00', 'admin', '2018-03-16 11:33:00', 'ry', '2020-03-28 07:21:51', '测试员');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;

-- Dumping structure for table eport.sys_user_online
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

-- Dumping data for table eport.sys_user_online: ~0 rows (approximately)
/*!40000 ALTER TABLE `sys_user_online` DISABLE KEYS */;
INSERT IGNORE INTO `sys_user_online` (`sessionId`, `login_name`, `dept_name`, `ipaddr`, `login_location`, `browser`, `os`, `status`, `start_timestamp`, `last_access_time`, `expire_time`) VALUES
	('3197a121-9df4-4260-9e17-df4b9b2aa957', 'admin', 'R&D', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', 'on_line', '2020-03-30 00:50:50', '2020-03-30 00:58:58', 1800000);
/*!40000 ALTER TABLE `sys_user_online` ENABLE KEYS */;

-- Dumping structure for table eport.sys_user_post
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
