INSERT INTO `eport`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('2026', 'Quản lý robot', '2', '6', 'system/robot/index', 'menuItem', 'C', '0', '', 'fa fa-cogs', 'admin', '2020-06-18 15:17:24', 'admin', '2020-06-18 15:17:49', '');

DROP TABLE IF EXISTS `sys_robot`;

CREATE TABLE IF NOT EXISTS `sys_robot` (
    `robot_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Robot uid',
    `uuid` VARCHAR(255) NOT NULL COLLATE UTF8_BIN COMMENT 'uuid',
    `status` CHAR(1) COLLATE UTF8_BIN DEFAULT '0' COMMENT 'Status（0 Available 1 BUSY 2 OFFLINE）',
    `is_receive_cont_full_order` tinyint(1) default 0 comment 'Is support receive container full order',
    `is_send_cont_empty_order` tinyint(1) default 0 comment 'Is support send container empty order',
    `is_receive_cont_empty_order` tinyint(1) default 0 comment 'Is support receive container empty order',
    `is_send_cont_full_order` tinyint(1) default 0 comment 'Is support send container full order',
    `ip_address` VARCHAR(255) COLLATE UTF8_BIN DEFAULT NULL COMMENT 'Ip address',
    `create_by` VARCHAR(64) COLLATE UTF8_BIN DEFAULT '' COMMENT 'Creator',
    `create_time` DATETIME DEFAULT NULL COMMENT 'Create Time',
    `update_by` VARCHAR(64) COLLATE UTF8_BIN DEFAULT '' COMMENT 'Updater',
    `update_time` DATETIME DEFAULT NULL COMMENT 'Update Time',
    `remark` VARCHAR(500) COLLATE UTF8_BIN DEFAULT NULL COMMENT 'Remark',
    PRIMARY KEY (`robot_id`),
    CONSTRAINT UC_uuid UNIQUE (uuid)
)  ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COLLATE = UTF8_BIN COMMENT='Robot';