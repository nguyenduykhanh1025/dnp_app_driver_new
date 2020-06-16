ALTER TABLE `transport_account` 
    DROP COLUMN `plate_number`,
    DROP COLUMN `external_rent_status`,
    CHANGE `logistic_group_id` `logistic_group_id` BIGINT(20) NULL COMMENT 'Logistic Group',
    CHANGE `status` `status` CHAR(1) COLLATE utf8_bin NULL DEFAULT '0' COMMENT 'Trạng thái khóa (default 0)';