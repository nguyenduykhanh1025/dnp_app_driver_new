CREATE TABLE `logistic_group` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`group_name` VARCHAR(255) NOT NULL COMMENT 'Group Name' COLLATE 'utf8_bin',
	`main_email` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Main Emails' COLLATE 'utf8_bin',
	`create_by` VARCHAR(64) NULL DEFAULT '' COMMENT 'Creator' COLLATE 'utf8_bin',
	`create_time` DATETIME NULL DEFAULT NULL COMMENT 'Create Time',
	`update_by` VARCHAR(64) NULL DEFAULT '' COMMENT 'Updater' COLLATE 'utf8_bin',
	`update_time` DATETIME NULL DEFAULT NULL COMMENT 'Update Time',
	PRIMARY KEY (`id`)
) COMMENT='Logistic Group' COLLATE='utf8_bin' ENGINE=InnoDB;

CREATE TABLE `logistic_account` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`group_id` BIGINT(20) NOT NULL COMMENT 'Master Account',
	`email` VARCHAR(50) NOT NULL COMMENT 'Email' COLLATE 'utf8_bin',
	`password` VARCHAR(50) NOT NULL COMMENT 'Password' COLLATE 'utf8_bin',
	`salt` VARCHAR(20) NOT NULL COMMENT 'Salt' COLLATE 'utf8_bin',
	`full_name` VARCHAR(255) NOT NULL COMMENT 'Ho Va Ten' COLLATE 'utf8_bin',
	`status` CHAR(1) NULL DEFAULT '0' COMMENT 'Status（0 Normal 1 Disabled）' COLLATE 'utf8_bin',
	`del_flag` CHAR(1) NULL DEFAULT '0' COMMENT 'Delete Flag (0 nomal 2 deleted)' COLLATE 'utf8_bin',
	`login_ip` VARCHAR(50) NULL DEFAULT '' COMMENT 'Login IP' COLLATE 'utf8_bin',
	`login_date` DATETIME NULL DEFAULT NULL COMMENT 'Login Date',
	`remark` VARCHAR(500) NULL DEFAULT NULL COMMENT 'Remark' COLLATE 'utf8_bin',
	`create_by` VARCHAR(64) NULL DEFAULT '' COMMENT 'Creator' COLLATE 'utf8_bin',
	`create_time` DATETIME NULL DEFAULT NULL COMMENT 'Create Time',
	`update_by` VARCHAR(64) NULL DEFAULT '' COMMENT 'Updater' COLLATE 'utf8_bin',
	`update_time` DATETIME NULL DEFAULT NULL COMMENT 'Update Time',
	PRIMARY KEY (`id`)
) COMMENT='Logistic account' COLLATE='utf8_bin' ENGINE=InnoDB;

CREATE TABLE `shipment` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`logistic_account_id` BIGINT(20) NOT NULL,
	`logistic_group_id` BIGINT(20) NOT NULL,
	`service_id` TINYINT(4) NOT NULL COMMENT 'Dich Vu',
	`tax_code` VARCHAR(15) NOT NULL COMMENT 'MST' COLLATE 'utf8_bin',
	`container_amount` INT(11) NOT NULL COMMENT 'So Luong Container',
	`edo_flg` VARCHAR(1) NULL DEFAULT NULL COMMENT 'EDO Flag (1,0)' COLLATE 'utf8_bin',
	`remak` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Ghi chu' COLLATE 'utf8_bin',
	`create_by` VARCHAR(64) NULL DEFAULT '' COMMENT 'Creator' COLLATE 'utf8_bin',
	`create_time` DATETIME NULL DEFAULT NULL COMMENT 'Create Time',
	`update_by` VARCHAR(64) NULL DEFAULT '' COMMENT 'Updater' COLLATE 'utf8_bin',
	`update_time` DATETIME NULL DEFAULT NULL COMMENT 'Update Time',
	PRIMARY KEY (`id`)
)
COMMENT='Shipment'
COLLATE='utf8_bin'
ENGINE=InnoDB
;

CREATE TABLE `shipment_detail` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`shipment_id` BIGINT(20) NOT NULL COMMENT 'Ma Lo',
	`register_no` VARCHAR(6) NOT NULL COMMENT 'Ma DK' COLLATE 'utf8_bin',
	`container_no` VARCHAR(12) NOT NULL COMMENT 'Container Number' COLLATE 'utf8_bin',
	`container_status` VARCHAR(3) NULL DEFAULT NULL COMMENT 'Container Status (S,D)' COLLATE 'utf8_bin',
	`sztp` VARCHAR(4) NOT NULL COMMENT 'Size Type' COLLATE 'utf8_bin',
	`fe` VARCHAR(1) NULL DEFAULT NULL COMMENT 'FE' COLLATE 'utf8_bin',
	`booking_no` VARCHAR(20) NULL DEFAULT NULL COMMENT 'Booking Number' COLLATE 'utf8_bin',
	`bl_no` VARCHAR(20) NULL DEFAULT NULL COMMENT 'BL number' COLLATE 'utf8_bin',
	`seal_no` VARCHAR(10) NULL DEFAULT NULL COMMENT 'Seal Number' COLLATE 'utf8_bin',
	`consignee` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Shipper/consignee' COLLATE 'utf8_bin',
	`expired_dem` DATETIME NULL DEFAULT NULL COMMENT 'Han Lenh',
	`wgt` INT(11) NOT NULL COMMENT 'Weight ',
	`vsl_nm` VARCHAR(20) NOT NULL COMMENT 'Vessel name' COLLATE 'utf8_bin',
	`voy_no` VARCHAR(20) NOT NULL COMMENT 'Voyage' COLLATE 'utf8_bin',
	`ope_code` VARCHAR(10) NOT NULL COMMENT 'Operator Code' COLLATE 'utf8_bin',
	`loading_port` VARCHAR(50) NOT NULL COMMENT 'Cang Chuyen Tai' COLLATE 'utf8_bin',
	`discharge_port` VARCHAR(50) NULL DEFAULT NULL COMMENT 'Cang Dich' COLLATE 'utf8_bin',
	`transport_type` VARCHAR(10) NULL DEFAULT NULL COMMENT 'Phuong Tien' COLLATE 'utf8_bin',
	`vgm_chk` BIT(1) NULL DEFAULT NULL COMMENT 'VGM Check',
	`vgm` VARCHAR(10) NULL DEFAULT NULL COMMENT 'VGM' COLLATE 'utf8_bin',
	`vgm_person_info` VARCHAR(100) NULL DEFAULT NULL COMMENT 'VGM Person Info' COLLATE 'utf8_bin',
	`custom_declare_no` VARCHAR(20) NULL DEFAULT NULL COMMENT 'Custom Declare Number' COLLATE 'utf8_bin',
	`custom_status` VARCHAR(1) NULL DEFAULT NULL COMMENT 'Custom Status (H,R)' COLLATE 'utf8_bin',
	`payment_status` VARCHAR(1) NULL DEFAULT NULL COMMENT 'Payment Status (Y,N,W,E)' COLLATE 'utf8_bin',
	`process_status` VARCHAR(1) NULL DEFAULT NULL COMMENT 'Process Status(Y,N,E)' COLLATE 'utf8_bin',
	`user_verify_status` VARCHAR(1) NULL DEFAULT NULL COMMENT 'Xac Thuc (Y,N)' COLLATE 'utf8_bin',
	`status` TINYINT(4) NULL DEFAULT NULL COMMENT 'Status',
	`remark` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Ghi chu' COLLATE 'utf8_bin',
	`create_by` VARCHAR(64) NULL DEFAULT NULL COMMENT 'Create By' COLLATE 'utf8_bin',
	`create_time` DATETIME NULL DEFAULT NULL COMMENT 'Create Time',
	`update_by` VARCHAR(64) NULL DEFAULT NULL COMMENT 'Update By' COLLATE 'utf8_bin',
	`update_time` DATETIME NULL DEFAULT NULL COMMENT 'Update Time',
	PRIMARY KEY (`id`)
)
COMMENT='Shipment Details'
COLLATE='utf8_bin'
ENGINE=InnoDB
;

