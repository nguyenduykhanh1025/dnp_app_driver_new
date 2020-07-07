ALTER TABLE `shipment`
	ADD COLUMN `cont_supply_status` tinyint(1) NULL DEFAULT NULL COMMENT 'Trạng thái: 0 chưa chỉ định cont, đã chỉ định cont' AFTER `specific_cont_flg`;