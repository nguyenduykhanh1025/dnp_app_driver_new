ALTER TABLE `shipment_detail`
	ADD COLUMN `move_cont_price` decimal(20,2) NULL DEFAULT NULL COMMENT 'Phi Dich Chuyen Cont' AFTER `preorder_pickup`;
	