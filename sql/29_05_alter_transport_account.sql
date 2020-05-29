ALTER TABLE `transport_account`
	ADD COLUMN `external_rent_status` CHAR(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Thuê ngoài (0 nomal 1 rent)' AFTER `del_flag`;