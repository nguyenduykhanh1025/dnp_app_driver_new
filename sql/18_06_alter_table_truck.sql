ALTER TABLE `truck` 
    ADD `del_flag` BIT(1) NOT NULL DEFAULT b'0' COMMENT 'Delete Flag(default 0)' AFTER `registry_expiry_date`;