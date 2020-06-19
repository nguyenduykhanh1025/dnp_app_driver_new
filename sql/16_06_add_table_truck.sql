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

CREATE TABLE IF NOT EXISTS `driver_truck`(
    `driver_id` bigint(20) NOT NULL COMMENT 'ID tài xế',
    `truck_id` bigint(20) NOT NULL COMMENT 'truck_id',
    PRIMARY KEY (`driver_id`,`truck_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='driver_truck';

CREATE TABLE IF NOT EXISTS `assign`(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `logistic_group_id` bigint(20) NULL COMMENT 'Logistic Group',
    `shipment_id` bigint(20) NOT NULL COMMENT 'Ma Lo',
    `shipment_detail_id` bigint(20) COLLATE utf8_bin NULL COMMENT 'Container id',
    `driver_id` bigint(20) NOT NULL COMMENT 'ID tài xế',
    `phone_number` varchar(15)  NULL COMMENT 'số điện thoại',
    `full_name` varchar(50)  NULL COMMENT 'Họ và Tên',
    `tractor_truck` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT 'Biển số xe đầu kéo(thuê ngoài)',
    `trailer_truck` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT 'Biển số xe rơ mooc(thuê ngoài)',
    `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
    `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
    `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
    `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
    `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='driver_truck';

CREATE TABLE IF NOT EXISTS `pickup_his`(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `logistic_group_id` bigint(20) NULL COMMENT 'Logistic Group',
    `shipment_id` bigint(20) NOT NULL COMMENT 'Ma Lo',
    `shipment_detail_id` bigint(20) COLLATE utf8_bin NULL COMMENT 'Container id',
    `driver_id` bigint(20) NOT NULL COMMENT 'ID tài xế',
    `phone_number` varchar(15)  NULL COMMENT 'số điện thoại',
    `full_name` varchar(50)  NULL COMMENT 'Họ và Tên',
    `tractor_truck` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT 'Biển số xe đầu kéo',
    `trailer_truck` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT 'Biển số xe rơ mooc',
    `coordinate` varchar(20) NULL COMMENT 'Tọa độ cont',
    `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
    `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
    `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
    `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
    `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='driver_truck';