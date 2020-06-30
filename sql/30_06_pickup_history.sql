DROP table if exists `pickup_hisory`;

CREATE TABLE `pickup_history` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `logistic_group_id` bigint NOT NULL COMMENT 'Logistic Group',
  `shipment_id` bigint NOT NULL COMMENT 'Mã Lô',
  `shipment_detail_id_1` bigint DEFAULT NULL COMMENT 'Shipment Detail Id 1',
  `shipment_detail_id_2` bigint DEFAULT NULL COMMENT 'Shipment Detail Id 2',
  `driver_id` bigint NOT NULL COMMENT 'ID Tài xế',
  `pickup_assign_id` bigint NOT NULL COMMENT 'Assign ID',
  `container_no_1` varchar(12) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'Số container 1',
  `container_no_2` varchar(12) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'Số container 2',
  `truck_no` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Biển số xe đầu kéo',
  `chassis_no` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Biển số xe rơ mooc',
  `yard_position_1` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'Tọa độ cont 1 trên bãi',
  `yard_position_2` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'Tọa độ cont 2 trên bãi',
  `status` tinyint DEFAULT '0' COMMENT 'Trạng thái (0:received, 1:planned, 2:gate-in, 3: gate-out)',
  `receipt_date` datetime DEFAULT NULL COMMENT 'Ngày nhận lệnh',
  `gatein_date` datetime DEFAULT NULL COMMENT 'Ngày vào cổng',
  `gateout_date` datetime DEFAULT NULL COMMENT 'Ngày ra cổng',
  `cancel_receipt_date` datetime DEFAULT NULL COMMENT 'Ngày hủy lệnh',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'Ghi chu',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'Create By',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'Update By',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Pickup history';
