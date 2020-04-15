DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT 'Role ID',
  `menu_id` bigint(20) NOT NULL COMMENT 'Menu ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Role and menu association';

-- Đang đổ dữ liệu cho bảng `sys_role_menu`
--

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 1),
(1, 3),
(1, 100),
(1, 108),
(1, 500),
(1, 501),
(1, 1000),
(1, 1001),
(1, 1002),
(1, 1003),
(1, 1004),
(1, 1005),
(1, 1006),
(1, 1007),
(1, 1008),
(1, 1009),
(1, 1010),
(1, 1011),
(1, 1012),
(1, 1013),
(1, 1014),
(1, 1015),
(1, 1039),
(1, 1040),
(1, 1041),
(1, 1042),
(1, 1043),
(1, 1044),
(1, 1045),
(1, 1046),
(1, 2000),
(1, 2001),
(1, 2002),
(1, 2003),
(1, 2004),
(1, 2005),
(1, 2006),
(1, 2007),
(1, 2008),
(1, 2009),
(1, 2010),
(1, 2011),
(1, 2012),
(1, 2013),
(1, 2014),
(1, 2015),
(1, 2016),
(1, 2017),
(2, 3),
(2,2000),
(2, 2001),
(2, 2002),
(2, 2003),
(2, 2004),
(2, 2005),
(2, 2006),
(2, 2007),
(2, 2008),
(2, 2009),
(2, 2010),
(2, 2011),
(2, 2012),
(2, 2013),
(2, 2014),
(2, 2015),
(2, 2016),
(2, 2017);


-- Chỉ mục cho bảng `sys_role_menu`
--
ALTER TABLE `sys_role_menu`
  ADD PRIMARY KEY (`role_id`,`menu_id`);