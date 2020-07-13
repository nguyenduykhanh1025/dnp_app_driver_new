ALTER TABLE `logistic_truck`
	ADD COLUMN `self_wgt` INT(10) NOT NULL COMMENT 'Trọng tải bản thân' AFTER `type`,
	CHANGE COLUMN `wgt` `wgt` INT(11) NOT NULL COMMENT 'Tải trọng cho phép chở' AFTER `self_wgt`;