drop table if exists `logistic_account`;
--
-- Cấu trúc bảng cho bảng `logistic_account`
--

CREATE TABLE `logistic_account` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `group_id` bigint(20) NOT NULL COMMENT 'Master Account',
  `user_name` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'Username',
  `email` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Email',
  `password` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Password',
  `salt` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'Salt',
  `full_name` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Ho Va Ten',
  `status` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Status（0 Normal 1 Disabled）',
  `del_flag` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT 'Delete Flag (0 nomal 1 deleted)',
  `login_ip` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT 'Login IP',
  `login_date` datetime DEFAULT NULL COMMENT 'Login Date',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Logistic account';

--
-- Chỉ mục cho bảng `logistic_account`
--
ALTER TABLE `logistic_account`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `logistic_account`
--
ALTER TABLE `logistic_account`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID';