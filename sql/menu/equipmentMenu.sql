-- Menu SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Delivery Order', '3', '1', '/equipment/do', 'C', '0', 'equipment:do:view', '#', 'admin', '2018-03-01', 'admin', '2018-03-01', 'Exchange Delivery Order Menu');

-- Menu Parent ID
SELECT @parentId := LAST_INSERT_ID();

-- Button SQL
insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Delivery Order List', @parentId, '1',  '#',  'F', '0', 'equipment:do:list',         '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Add DO', @parentId, '2',  '#',  'F', '0', 'equipment:do:add',          '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Edit DO', @parentId, '3',  '#',  'F', '0', 'equipment:do:edit',         '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Delete DO', @parentId, '4',  '#',  'F', '0', 'equipment:do:remove',       '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Export DO', @parentId, '5',  '#',  'F', '0', 'equipment:do:export',       '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');
