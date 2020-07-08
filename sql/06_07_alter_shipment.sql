ALTER TABLE `shipment`
	ADD COLUMN `specific_cont_flg` tinyint(1) NULL DEFAULT NULL COMMENT 'Trạng thái: 0 ko chỉ định cont, 1 có chỉ định cont' AFTER `edo_flg`;