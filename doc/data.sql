DELETE FROM sys_backend;
INSERT INTO sys_backend SELECT
	t1.*
FROM
	db_oppo_test.sys_backend t1
WHERE
	t1.enabled = 1;
DELETE FROM sys_backend_module;
INSERT INTO sys_backend_module
SELECT
	t1.*
FROM
	db_oppo_test.sys_backend_module t1
WHERE
	t1.enabled = 1;

UPDATE sys_menu_category SET enabled=1;
INSERT INTo sys_menu_category(
id,
name,
sort,
created_by,
created_date,
last_modified_by,
last_modified_date,
remarks,
version,
locked,
enabled,
code,
backend_module_id,
company_id
) SELECT t1.* from db_oppo_test.sys_menu_category t1 where t1.enabled=1;
update sys_menu t1,db_oppo_test.sys_menu t2 set t1.menu_category_id=t2.menu_category_id,t1.code=t2.code where t1.name=t2.name;
DELETE FROM sys_office_rule;
INSERT INTO sys_office_rule SELECT
	t1.*
FROM
	db_oppo_test.sys_office_rule t1
WHERE
	t1.enabled = 1;

update sys_office t1,db_oppo_test.sys_office t2 set t1.office_rule_id=t2.office_rule_id,t1.area_id=t2.area_id where t1.name=t2.name;

INSERT into sys_process_task SELECT t1. * FROM db_oppo_test.sys_process_task t1 where t1.enabled=1;



