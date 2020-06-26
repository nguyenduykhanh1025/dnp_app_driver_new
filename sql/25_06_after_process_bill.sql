alter table `process_bill`
	drop `preorder_cont_number`,
	drop `shifting_number`,
	drop `cont_number`,
	drop `shifting_fee`,
	add `vat_rate` int(5) null default null comment 'tỉ lệ % thuế vat' after `exchange_fee`,
	add `vat_after_fee` bigint(20) null default null comment 'phí sau thuế vat' after `vat_rate`,
	add `container_no` varchar(12) null default null comment 'số container' after `vat_after_fee`;
	
	