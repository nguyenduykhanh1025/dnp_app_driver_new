CREATE TABLE IF NOT EXISTS `robot_status`(
    `id`bigint(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `sevice_id` tinyint(4) NOT NULL COMMENT 'Dich Vu',
    `active_flag`varchar(1) NOT NULL COMMENT 'Robot active',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Robot Status';