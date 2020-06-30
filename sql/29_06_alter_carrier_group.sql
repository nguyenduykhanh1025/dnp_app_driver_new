ALTER TABLE `carrier_group`
	ADD COLUMN `path_edi_file` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Path edi file' COLLATE 'utf8_bin' AFTER `edo_flag`,
ALTER TABLE `carrier_group`
	ADD COLUMN `api_private_key` TEXT NULL DEFAULT NULL COMMENT 'API private key' AFTER `edo_flag`,
	ADD COLUMN `api_public_key` TEXT NULL DEFAULT NULL COMMENT 'API public key' AFTER `api_private_key`
ALTER TABLE `carrier_group`
CHANGE COLUMN `path_edi_file` `path_edi_receive` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Path edi file' COLLATE 'utf8_bin' AFTER `api_public_key`,
	ADD COLUMN `path_edi_backup` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Path_edi_moving' COLLATE 'utf8_bin' AFTER `path_edi_receive`;
SELECT `DEFAULT_COLLATION_NAME` FROM `information_schema`.`SCHEMATA` WHERE `SCHEMA_NAME`='eport'
