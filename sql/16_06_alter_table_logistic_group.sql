ALTER TABLE `logistic_group`
     ADD `credit_flag` CHAR(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Credit (1:có,0:không(default)))' AFTER `mobile_phone`;