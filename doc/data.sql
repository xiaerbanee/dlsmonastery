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

INSERT INTO sys_role SELECT
	t1.*
FROM
	db_oppo_test.sys_role t1
WHERE
	t1.enabled = 1;

	update hr_position t1,db_oppo_test.hr_position t2 set t1.role_id=t2.role_id where t1.`name`=t2.`name`;

  delete FROM  sys_company_config where code in ("COMPANY_NAME","DEFAULT_PROVINCE_ID","EXPRESS_PRINT_QTY");
	INSERT into sys_company_config SELECT t1.* FROM db_oppo_test.sys_company_config t1 where t1.code in ("COMPANY_NAME","DEFAULT_PROVINCE_ID","EXPRESS_PRINT_QTY");

	INSERT INTO sys_menu (
	id,
	NAME,
	sort,
	mobile,
	menu_category_id,
	created_by,
	created_date,
	last_modified_by,
	last_modified_date,
	remarks,
	version,
	locked,
	enabled,
	visible,
	mobile_href,
	mobile_icon,
	`code`,
	href,
	company_id
) SELECT
	t1.id,
	t1.`NAME`,
	t1.sort,
	t1.mobile,
	t1.menu_category_id,
	t1.created_by,
	t1.created_date,
	t1.last_modified_by,
	t1.last_modified_date,
	t1.remarks,
	t1.version,
	t1.locked,
	t1.enabled,
	t1.visible,
	t1.mobile_href,
	t1.mobile_icon,
	t1.`code`,
	"" AS href,
	1 AS company_id
FROM
	db_oppo_test.sys_menu t1
WHERE
	t1.`name` not in(
		SELECT
			t2. NAME
		FROM
			sys_menu t2
	)
AND t1.enabled = 1;


