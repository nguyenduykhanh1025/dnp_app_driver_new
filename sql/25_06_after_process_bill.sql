ALTER TABLE `process_bill`
    ADD `inv_no` VARCHAR(20) NULL DEFAULT NULL COMMENT 'Số hóa đơn' AFTER `shifting_fee`,
    ADD `vat_rate` INT(5) NULL DEFAULT NULL COMMENT 'Tỉ lệ % thuế VAT' AFTER `inv_no`,
    ADD `vat_after_fee` BIGINT(20) NULL DEFAULT NULL COMMENT 'Phí sau thuế VAT' AFTER `vat_rate`,
    ADD `container_no` VARCHAR(12) NULL DEFAULT NULL COMMENT 'số container' AFTER `vat_after_fee`;