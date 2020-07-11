INSERT INTO `eport`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('2027', 'Lập tọa độ cho container', '2022', '1', 'mc/plan/request/index', 'menuItem', 'C', '0', '', 'fa fa-compass', 'admin', '2020-06-27 06:30:39', 'admin', '2020-07-04 02:13:16', '');
INSERT INTO `eport`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('2029', 'Danh sách lệnh đã lập tọa độ', '2022', '2', 'mc/plan/history/index', 'menuItem', 'C', '0', '', 'fa fa-history', 'admin', '2020-07-04 02:13:55', 'admin', '2020-07-04 02:14:53', '');

DROP TABLE IF EXISTS `pickup_history`;

CREATE TABLE `pickup_history` (
	`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`logistic_group_id` BIGINT NOT NULL COMMENT 'Logistic Group',
	`shipment_id` BIGINT NOT NULL COMMENT 'Mã Lô',
	`shipment_detail_id` BIGINT NULL DEFAULT NULL COMMENT 'Shipment Detail Id',
	`driver_id` BIGINT NOT NULL COMMENT 'ID Tài xế',
	`pickup_assign_id` BIGINT NOT NULL COMMENT 'Assign ID',
	`container_no` VARCHAR(12) NULL DEFAULT NULL COMMENT 'Số container' COLLATE 'utf8_bin',
	`truck_no` VARCHAR(15) NOT NULL COMMENT 'Biển số xe đầu kéo' COLLATE 'utf8_bin',
	`chassis_no` VARCHAR(15) NOT NULL COMMENT 'Biển số xe rơ mooc' COLLATE 'utf8_bin',
	`area` VARCHAR(20) NULL DEFAULT NULL COMMENT 'area' COLLATE 'utf8_bin',
	`bay` VARCHAR(20) NULL DEFAULT NULL COMMENT 'bay' COLLATE 'utf8_bin',
	`block` VARCHAR(20) NULL DEFAULT NULL COMMENT 'block' COLLATE 'utf8_bin',
	`line` VARCHAR(20) NULL DEFAULT NULL COMMENT 'row' COLLATE 'utf8_bin',
	`tier` VARCHAR(20) NULL DEFAULT NULL COMMENT 'tier' COLLATE 'utf8_bin',
	`status` TINYINT(1) NULL DEFAULT '0' COMMENT 'Trạng thái (0:received, 1:planned, 2:gate-in, 3: gate-out)',
	`receipt_date` DATETIME NULL DEFAULT NULL COMMENT 'Ngày nhận lệnh',
	`gatein_date` DATETIME NULL DEFAULT NULL COMMENT 'Ngày vào cổng',
	`gateout_date` DATETIME NULL DEFAULT NULL COMMENT 'Ngày ra cổng',
	`cancel_receipt_date` DATETIME NULL DEFAULT NULL COMMENT 'Ngày hủy lệnh',
	`remark` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Ghi chu' COLLATE 'utf8_bin',
	`planning_date` DATETIME NULL DEFAULT NULL COMMENT 'Ngày lập tọa độ',
	`create_by` VARCHAR(64) NULL DEFAULT NULL COMMENT 'Create By' COLLATE 'utf8_bin',
	`create_time` DATETIME NULL DEFAULT NULL COMMENT 'Create Time',
	`update_by` VARCHAR(64) NULL DEFAULT NULL COMMENT 'Update By' COLLATE 'utf8_bin',
	`update_time` DATETIME NULL DEFAULT NULL COMMENT 'Update Time',
	PRIMARY KEY (`id`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Pickup history';
