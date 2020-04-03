-- Menu SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Equipment attached document', '3', '1', '/edo/document', 'C', '0', 'edo:document:view', '#', 'admin', '2018-03-01', 'admin', '2018-03-01', 'Equipment attached document Menu');

-- Menu Parent ID
SELECT @parentId := LAST_INSERT_ID();

-- Button SQL
insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Get Equipment attached document', @parentId, '1',  '#',  'F', '0', 'edo:document:list',         '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Add Equipment attached document', @parentId, '2',  '#',  'F', '0', 'edo:document:add',          '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Edit Equipment attached document', @parentId, '3',  '#',  'F', '0', 'edo:document:edit',         '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Delete Equipment attached document', @parentId, '4',  '#',  'F', '0', 'edo:document:remove',       '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('Export Equipment attached document', @parentId, '5',  '#',  'F', '0', 'edo:document:export',       '#', 'admin', '2018-03-01', 'admin', '2018-03-01', '');
