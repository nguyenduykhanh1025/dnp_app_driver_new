CREATE TABLE IF NOT EXISTS `truck`(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `logistic_group_id` bigint(20) NULL COMMENT 'Logistic Group',
    `plate_number` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Bien So Xe',
    `type` char(1) COLLATE utf8_bin NOT NULL COMMENT '1:đầu kéo, 0:rơ mooc',
    `wgt` int(11) NOT NULL COMMENT 'Weight ',
    `registry_expiry_date` datetime DEFAULT NULL COMMENT 'Hạn đăng kiểm',
    `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
    `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
    `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
    `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
    `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Truck';