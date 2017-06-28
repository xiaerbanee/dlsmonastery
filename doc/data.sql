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

update sys_menu set code="clientList" where name="客户管理";

update sys_menu set code="depotStoreList" where name="仓库管理";

update sys_permission set url='/api/ws/future/crm/goodsOrder',method='GET' where permission='crm:goodsOrder:view';
update sys_permission set url='/api/ws/future/crm/goodsOrder/save',method='POST' where permission='crm:goodsOrder:edit';
update sys_permission set url='/api/ws/future/crm/goodsOrder/delete',method='GET' where permission='crm:goodsOrder:delete';
update sys_permission set url='/api/ws/future/crm/goodsOrder/bill',method='POST' where permission='crm:goodsOrder:bill';
update sys_permission set url='/api/ws/future/crm/goodsOrderShip/print',method='GET' where permission='crm:goodsOrder:ship';
update sys_permission set url='/api/ws/future/crm/goodsOrderShip/shipPrint',method='GET' where permission='crm:goodsOrder:print';
update sys_permission set url='/api/ws/future/crm/expressOrder/batchAdd',method='POST' where permission='crm:goodsOrder:batchAdd';
update sys_permission set url='/api/ws/future/crm/expressOrder/shipBack',method='POST' where permission='crm:goodsOrder:shipBack';
update sys_permission set url='/api/ws/future/crm/expressOrder/delete',method='GET' where permission='crm:expressOrder:delete';
update sys_permission set url='/api/ws/future/crm/expressOrder/save,/api/ws/future/crm/expressOrder/resetPrintStatus',method='POST' where permission='crm:expressOrder:edit';
update sys_permission set url='/api/ws/future/crm/expressOrder',method='GET' where permission='crm:expressOrder:view';
update sys_permission set url='/api/ws/future/crm/bankIn/audit',method='POST' where permission='crm:bankIn:audit';
update sys_permission set url='/api/ws/future/crm/bankIn/delete',method='GET' where permission='crm:bankIn:delete';
update sys_permission set url='/api/ws/future/crm/bankIn/save',method='POST' where permission='crm:bankIn:edit';
update sys_permission set url='/api/ws/future/crm/bankIn',method='GET' where permission='crm:bankIn:view';
update sys_permission set url='/api/ws/future/layout/adGoodsOrder/bill',method='POST' where permission='crm:adGoodsOrder:bill';
update sys_permission set url='/api/ws/future/layout/adGoodsOrder/delete',method='GET' where permission='crm:adGoodsOrder:delete';
update sys_permission set url='/api/ws/future/layout/adGoodsOrder/save',method='POST' where permission='crm:adGoodsOrder:edit';
update sys_permission set url='/api/ws/future/layout/adGoodsOrder/print',method='GET' where permission='crm:adGoodsOrder:print';
update sys_permission set url='/api/ws/future/layout/adGoodsOrder/ship',method='POST' where permission='crm:adGoodsOrder:ship';
update sys_permission set url='/api/ws/future/layout/adGoodsOrder/sign',method='POST' where permission='crm:adGoodsOrder:sign';
update sys_permission set url='/api/ws/future/layout/adGoodsOrder',method='GET' where permission='crm:adGoodsOrder:view';
update sys_permission set url='/api/ws/future/layout/adApply/bill',method='POST' where permission='crm:adApply:bill';
update sys_permission set url='/api/ws/future/layout/adApply/delete',method='GET' where permission='crm:adApply:delete';
update sys_permission set url='/api/ws/future/layout/adApply/save,/api/ws/future/layout/adApply/billSave',method='POST' where permission='crm:adApply:edit';
update sys_permission set url='/api/ws/future/layout/adApply/goodsSave',method='POST' where permission='crm:adApply:goods';
update sys_permission set url='/api/ws/future/layout/adApply',method='GET' where permission='crm:adApply:view';
update sys_permission set url='/api/ws/future/crm/storeAllot/delete',method='GET' where permission='crm:storeAllot:delete';
update sys_permission set url='/api/ws/future/crm/storeAllot/save',method='POST' where permission='crm:storeAllot:edit';
update sys_permission set url='/api/ws/future/crm/storeAllot/ship',method='POST' where permission='crm:storeAllot:ship';
update sys_permission set url='/api/ws/future/crm/storeAllot',method='GET' where permission='crm:storeAllot:view';
update sys_permission set url='/api/ws/future/crm/shopAllot/audit',method='POST' where permission='crm:shopAllot:audit';
update sys_permission set url='/api/ws/future/crm/shopAllot/logicDelete',method='GET' where permission='crm:shopAllot:delete';
update sys_permission set url='/api/ws/future/crm/shopAllot/save',method='POST' where permission='crm:shopAllot:edit';
update sys_permission set url='/api/ws/future/crm/shopAllot',method='GET' where permission='crm:shopAllot:view';

update sys_menu set enabled=false where name='国际化设置';
update sys_menu set enabled=false where name='报表监控';
update sys_menu set enabled=false where name='公司管理';
update sys_menu set enabled=false where name='问答列表';
update sys_menu set enabled=false where name='文章管理';
update sys_menu set enabled=false where name='单元编辑';
update sys_menu set enabled=false where name='机构调整';
update sys_menu set enabled=false where name='职位管理';
update sys_menu set enabled=false where name='公告列表';
update sys_menu set enabled=false where name='私信列表';
update sys_menu set enabled=false where name='招聘信息';
update sys_menu set enabled=false where name='终端统计';
update sys_menu set enabled=false where name='抽奖管理';
update sys_menu set enabled=false where name='上报报表';
update sys_menu set enabled=false where name='串码查询';
update sys_menu set enabled=false where name='应收报表报错';
update sys_menu set enabled=false where name='调价提成';
update sys_menu set enabled=false where name='货品订货报错';
update sys_menu set enabled=false where name='库存盘点';
update sys_menu set enabled=false where name='售后列表';
update sys_menu set enabled=false where name='垫机录入';
update sys_menu set enabled=false where name='售后盘点';
update sys_menu set enabled=false where name='售后调拨';
update sys_menu set enabled=false where name='调拨列表';
update sys_menu set enabled=false where name='保卡统计';
update sys_menu set enabled=false where name='报表中心';
update sys_menu set enabled=false where name='OPPO电子保卡';
update sys_menu set enabled=false where name='OPPO颜色编码';
update sys_menu set enabled=false where name='OPPO发货串码';
update sys_menu set enabled=false where name='考勤查询';
update sys_menu set enabled=false where name='积分查询';
update sys_menu set enabled=false where name='费用报销';
update sys_menu set enabled=false where name='员工报表';
update sys_menu set enabled=false where name='抽奖管理';
update sys_menu set enabled=false where name='仓库调整';
update sys_menu set enabled=false where name='门店报表';
update sys_menu set enabled=false where name='坏机返厂';
update sys_menu set enabled=false where name='好机返库';



update sys_menu_category set enabled=false where name='客服中心';
update sys_menu_category set enabled=false where name='数据管理';
update sys_menu_category set enabled=false where name='工资管理';
update sys_menu_category set enabled=false where name='移动商城';
update sys_menu_category set enabled=false where name='任务政策';
update sys_menu_category set enabled=false where name='提成基准';
update sys_menu_category set enabled=false where name='K3Cloud';
update sys_menu_category set enabled=false where name='报表中心';


