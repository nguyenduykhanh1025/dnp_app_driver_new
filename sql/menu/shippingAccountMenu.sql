-- Menu SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Shipping line account', '3', '1', '/shipping/account', 'C', '0', 'shipping:account:view', '#', 'admin', '2018-03-01', 'admin', '2018-03-01', 'Shipping line account Menu');

-- Menu Parent ID
SELECT @parentId := LAST_INSERT_ID();

-- Button SQL
insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Get Shipping line account', @parentId, '1',  '#',  'F', '0', 'shipping:account:list',         '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Add Shipping line account', @parentId, '2',  '#',  'F', '0', 'shipping:account:add',          '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Edit Shipping line account', @parentId, '3',  '#',  'F', '0', 'shipping:account:edit',         '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Delete Shipping line account', @parentId, '4',  '#',  'F', '0', 'shipping:account:remove',       '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Export Shipping line account', @parentId, '5',  '#',  'F', '0', 'shipping:account:export',       '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');
