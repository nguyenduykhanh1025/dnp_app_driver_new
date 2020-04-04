-- Menu SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Carrier Account', '3', '1', '/carrier/account', 'C', '0', 'carrier:account:view', '#', 'admin', '2018-03-01', 'admin', '2018-03-01', 'Carrier Account Menu');

-- Menu Parent ID
SELECT @parentId := LAST_INSERT_ID();

-- Button SQL
insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Carrier Account List', @parentId, '1',  '#',  'F', '0', 'carrier:account:list',         '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Add Carrier Account', @parentId, '2',  '#',  'F', '0', 'carrier:account:add',          '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Edit Carrier Account', @parentId, '3',  '#',  'F', '0', 'carrier:account:edit',         '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Delete Carrier Account', @parentId, '4',  '#',  'F', '0', 'carrier:account:remove',       '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Export Carrier Account', @parentId, '5',  '#',  'F', '0', 'carrier:account:export',       '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');
