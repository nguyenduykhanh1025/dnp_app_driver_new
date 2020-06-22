ALTER TABLE `shipment_detail` 
    ADD `cargo_type` varchar(50) NULL DEFAULT null COMMENT 'Cargo Type' AFTER `empty_depot`;