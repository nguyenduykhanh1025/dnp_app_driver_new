ALTER TABLE `pickup_assign`
	ADD COLUMN `driver_owner` VARCHAR(100) NULL DEFAULT NULL COMMENT 'Đơn vị chủ quản ( thuê ngoài)' COLLATE 'utf8_bin' AFTER `external_secret_code`,
	ADD COLUMN `phone_number` VARCHAR(15) NULL DEFAULT NULL COMMENT 'Số điện thoại (thuê ngoài)' COLLATE 'utf8_bin' AFTER `driver_owner`,
	ADD COLUMN `full_name` VARCHAR(50) NULL DEFAULT NULL COMMENT 'Họ và tên (thuê ngoài)' COLLATE 'utf8_bin' AFTER `phone_number`;