-- Menu SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Exchange Delivery Order', '3', '1', '/edo/edo', 'C', '0', 'edo:edo:view', '#', 'admin', '2018-03-01', 'admin', '2018-03-01', 'Exchange Delivery Order Menu');

-- Menu Parent ID
SELECT @parentId := LAST_INSERT_ID();

-- Button SQL
insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Get Exchange Delivery Order', @parentId, '1',  '#',  'F', '0', 'edo:edo:list',         '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Add Exchange Delivery Order', @parentId, '2',  '#',  'F', '0', 'edo:edo:add',          '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Edit Exchange Delivery Order', @parentId, '3',  '#',  'F', '0', 'edo:edo:edit',         '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Delete Exchange Delivery Order', @parentId, '4',  '#',  'F', '0', 'edo:edo:remove',       '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Export Exchange Delivery Order', @parentId, '5',  '#',  'F', '0', 'edo:edo:export',       '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');
