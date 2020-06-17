ALTER TABLE `shipment`
     ADD `booking_no` varchar(20) COLLATE utf8_bin DEFAULT null COMMENT 'Booking Number' AFTER `bl_no`;