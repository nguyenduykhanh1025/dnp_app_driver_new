INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(2021, 'Quản Lý Vận Đơn', 0, 4, '#', 'menuItem', 'M', '0', NULL, 'fa fa-address-book-o', 'admin', '2020-06-08 14:19:16', '', NULL, ''),
(2022, 'Kế Hoạch Bãi Cảng', 0, 5, '#', 'menuItem', 'M', '0', NULL, 'fa fa-anchor', 'admin', '2020-06-08 14:20:16', '', NULL, ''),
(2023,'Bộ phận làm thủ tục',0,6,'#','menuItem','M','0',NULL,'fa fa-cog','admin','2020-06-09 20:23:58','',NULL,''),
(2024,'Hỗ trợ Robot làm lệnh',2023,1,'/om/executeCatos/index','menuItem','C','0','','fa fa-navicon','admin','2020-06-09 20:56:31','admin',NULL,'');