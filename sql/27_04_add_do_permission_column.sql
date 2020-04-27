ALTER TABLE carrier_group ADD COLUMN do_flag char(1) DEFAULT '0' COMMENT 'DO Permission';
ALTER TABLE carrier_group ADD COLUMN edo_flag char(1) DEFAULT '0' COMMENT 'EDO Permission';