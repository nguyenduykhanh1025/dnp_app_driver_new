-- Menu SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Carrier Group', '3', '1', '/carrier/group', 'C', '0', 'carrier:group:view', '#', 'admin', '2018-03-01', 'admin', '2018-03-01', 'Carrier Group Menu');

-- Menu Parent ID
SELECT @parentId := LAST_INSERT_ID();

-- Button SQL
insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Get Carrier Group', @parentId, '1',  '#',  'F', '0', 'carrier:group:list',         '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Add Carrier Group', @parentId, '2',  '#',  'F', '0', 'carrier:group:add',          '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Edit Carrier Group', @parentId, '3',  '#',  'F', '0', 'carrier:group:edit',         '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Delete Carrier Group', @parentId, '4',  '#',  'F', '0', 'carrier:group:remove',       '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Export Carrier Group', @parentId, '5',  '#',  'F', '0', 'carrier:group:export',       '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');
