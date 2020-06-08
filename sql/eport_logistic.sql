CREATE TABLE `otp_code` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`shipment_detailIds` VARCHAR(255) NOT NULL DEFAULT NULL COMMENT 'Shipment detailIds' COLLATE 'utf8_bin',
	`phone_number` varchar(13) NOT NULL COMMENT 'Phone number' COLLATE 'utf8_bin',
	`opt_code` int(11) NOT NULL  DEFAULT '' COMMENT 'OTP CODE' COLLATE 'utf8_bin',
        `msg_status` char(1) NULL DEFAULT NULL COMMENT 'Status send mess',
        `verify_status` char(1) NULL DEFAULT NULL COMMENT 'Verify send mess',
	`create_time` DATETIME NULL DEFAULT NULL COMMENT 'Create Time',
	
	
	PRIMARY KEY (`id`)
) COMMENT='otpCode' COLLATE='utf8_bin' ENGINE=InnoDB;


COMMENT='otpCode'
COLLATE='utf8_bin'
ENGINE=InnoDB
;

