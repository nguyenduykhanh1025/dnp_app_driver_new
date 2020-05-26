drop table if exists `logistic_group`;
CREATE TABLE `logistic_group` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `group_name` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Tên doanh nghiệp',
  `email_address` varchar(255) COLLATE utf8_bin  NOT NULL COMMENT 'Địa chỉ thư điện tử',
  `address` varchar(255) COLLATE utf8_bin  NOT NULL COMMENT 'Địa chỉ liên hệ',
  `mst` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Mã số thuế doanh nghiệp',
  `phone`    varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Điện thoại cố định',
  `mobile_phone` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Điện thoại di động',
  `fax` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'Fax',
  `business_registration_certificate`varchar(100) COLLATE utf8_bin NOT NULL COMMENT 'Giấy đăng ký kinh doanh',
  `date_of_issue_registration` datetime  NOT NULL COMMENT 'Ngày cấp giấy đăng ký',
  `place_of_issue_registration`varchar(50) COLLATE utf8_bin  NOT NULL COMMENT 'Nơi cấp giấy đăng ký',
  `authorized_representative` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Đại diện có thẩm quyền',
  `representative_position` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Chức vụ',
  `following_authorization_form_no` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'Theo bản ủy quyền số',
  `sign_date` datetime  NOT NULL COMMENT 'Ngày ký',
  `owned` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'của ...',
  `identify_card_no`varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'Số chứng minh thư',
  `date_of_issue_identify` datetime  NOT NULL COMMENT 'Ngày cấp chứng minh',
  `place_of_issue_identify` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Nơi cấp chứng minh',
  `email` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'email',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Logistic Group';

ALTER TABLE `logistic_group`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `logistic_group`
--
ALTER TABLE `logistic_group`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID';