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

  delete FROM  sys_company_config where code in ("COMPANY_NAME","DEFAULT_PROVINCE_ID","EXPRESS_PRINT_QTY","MERGE_STORE_IDS","PRODUCT_NAME");
	INSERT into sys_company_config SELECT t1.* FROM db_oppo_test.sys_company_config t1 where t1.code in ("COMPANY_NAME","DEFAULT_PROVINCE_ID","EXPRESS_PRINT_QTY","MERGE_STORE_IDS","PRODUCT_NAME");

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
update sys_menu set enabled=false where code='goodsOrderShip';
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

update sys_menu set mobile=FALSE;
update sys_menu set mobile=TRUE where name in ("每日排名","仓库管理","广告申请","串码核销","请假申请","签到列表","加班申请","调休申请","免打考勤","出差申请");

INSERT into sys_role(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,permission)VALUES('内销部文员',1,'2017-06-30 12:22:34',1,'2017-06-30 12:22:34','',0,0,1,'内销部文员');
INSERT into sys_role(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,permission)VALUES('内销部主管',1,'2017-06-30 12:22:34',1,'2017-06-30 12:22:34','',0,0,1,'内销部主管');
INSERT into sys_role(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,permission)VALUES('仓储部文员',1,'2017-06-30 12:22:34',1,'2017-06-30 12:22:34','',0,0,1,'仓储部文员');
INSERT into sys_role(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,permission)VALUES('仓储部主管',1,'2017-06-30 12:22:34',1,'2017-06-30 12:22:34','',0,0,1,'仓储部主管');
INSERT into sys_role(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,permission)VALUES('导购',1,'2017-06-30 12:22:34',1,'2017-06-30 12:22:34','',0,0,1,'导购');
INSERT into sys_role(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,permission)VALUES('业务',1,'2017-06-30 12:22:34',1,'2017-06-30 12:22:34','',0,0,1,'业务');
INSERT into sys_role(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,permission)VALUES('财务部文员',1,'2017-06-30 12:22:34',1,'2017-06-30 12:22:34','',0,0,1,'财务部文员');
INSERT into sys_role(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,permission)VALUES('财务部主管',1,'2017-06-30 12:22:34',1,'2017-06-30 12:22:34','',0,0,1,'财务部主管');


update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='导购') where t1.name='市县导购';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='导购') where t1.name='流动导购';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='导购') where t1.name='乡镇导购';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='导购') where t1.name='返聘导购';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='导购') where t1.name='体专店员';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='导购') where t1.name='体专店长';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='导购') where t1.name='代理店长';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='导购') where t1.name='体专专员';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='导购') where t1.name='体专主管';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='导购') where t1.name='体专终端';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='产品经理';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='阿米巴巴长';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='联电经理';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='市县业务主管';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='乡镇业务主管';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='培训主管';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='终端主管';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='推广主管';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='市县督导';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='培训专员';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='终端专员';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='助理培训师';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='推广专员';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='乡镇业务';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='乡镇组长';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='乡镇培训专员';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='乡镇督导';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='别动队员';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='O2O专员';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='运营商专员';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where t1.name='办事处司机';

update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='内销部文员') where t1.name='内销部专员';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='内销部主管') where t1.name='内销部主管';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='内销部主管') where t1.name='内销部部长';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='仓储部文员') where t1.name='仓储部专员';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='仓储部主管') where t1.name='仓储部主管';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='财务部文员') where t1.name='办事处财务';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='财务部文员') where t1.name='财务部专员';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='财务部文员') where t1.name='省公司财务出纳';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='财务部主管') where t1.name='财务部主管';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='财务部主管') where t1.name='财务部部长';

update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where (t1.role_id is NULL OR t1.role_id=1);
DELETE FROM sys_role where id=7;
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='管理员') where (t1.name="信息部专员");

INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('crm:depotShop:view','crm:depotShop:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'208','/api/ws/future/basic/depotShop','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('crm:depotShop:businessEdit','crm:depotShop:businessEdit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'208','/api/ws/future/basic/depotShop/saveDepot','POST');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('crm:depotShop:delete','crm:depotShop:delete',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'208','/api/ws/future/basic/depotShop/delete','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('crm:depotShop:basicEdit','crm:depotShop:basicEdit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'208','/api/ws/future/basic/depotShop/save','POST');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('crm:goodsOrderShip:view','crm:goodsOrderShip:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'169','/api/ws/future/crm/goodsOrderShip','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('crm:goodsOrderShip:edit','crm:goodsOrderShip:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'169','/api/ws/future/crm/goodsOrderShip/save','POST');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('crm:goodsOrderShip:delete','crm:goodsOrderShip:delete',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'169','/api/ws/future/crm/goodsOrderShip/delete','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('crm:afterSaleArea:view','crm:afterSaleArea:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'206','/api/ws/future/crm/afterSaleArea','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('crm:afterSaleArea:edit','crm:afterSaleArea:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'206','/api/ws/future/crm/afterSaleArea/save','POST');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('crm:afterSaleArea:delete','crm:afterSaleArea:delete',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'206','/api/ws/future/crm/afterSaleArea/delete','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('crm:afterSaleHead:view','crm:afterSaleHead:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'207','/api/ws/future/crm/afterSaleHead','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('crm:afterSaleHead:edit','crm:afterSaleHead:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'207','/api/ws/future/crm/afterSaleHead/save','POST');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('crm:afterSaleHead:delete','crm:afterSaleHead:delete',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'207','/api/ws/future/crm/afterSaleHead/delete','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('crm:afterSaleCompany:view','crm:afterSaleCompany:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'218','/api/ws/future/crm/afterSaleCompany','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('crm:afterSaleCompany:edit','crm:afterSaleCompany:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'218','/api/ws/future/crm/afterSaleCompany/save','POST');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('crm:afterSaleCompany:delete','crm:afterSaleCompany:delete',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'218','/api/ws/future/crm/afterSaleCompany/delete','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('crm:storeInventoryReport:view','crm:storeInventoryReport:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'219','/api/ws/future/basic/depotStore/storeReport','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('sys:backendModule:view','sys:backendModule:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'176','/api/basic/sys/backendModule','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('sys:backendModule:edit','sys:backendModule:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'176','/api/basic/sys/backendModule/save','POST');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('sys:backendModule:delete','sys:backendModule:delete',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'176','/api/basic/sys/backendModule/delete','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('sys:role:view','sys:role:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'183','/api/basic/sys/role','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('sys:role:edit','sys:role:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'183','/api/basic/sys/role/save','PSOT');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('sys:rolw:delete','sys:rolw:delete',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'183','/api/basic/sys/role/delete','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('sys:backend:view','sys:backend:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'177','/api/basic/sys/backend','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('sys:backend:edit','sys:backend:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'177','/api/basic/sys/backend/save','POST');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('sys:backend:delete','sys:backend:delete',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'177','/api/basic/sys/backend/delete','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('sys:officeRule:view','sys:officeRule:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'178','/api/basic/sys/officeRule','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('sys:officeRule:edit','sys:officeRule:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'178','/api/basic/sys/officeRule/delete','POST');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('sys:officeRule:delete','sys:officeRule:delete',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'178','/api/basic/sys/officeRule/save','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('修改','input:salOutStock:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'179','input:salOutStock:edit','input:salOutStock:edit');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('修改','input:salReturnStock:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'184','input:salReturnStock:edit','input:salReturnStock:edit');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('修改','input:stkMisDelivery:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'185','input:stkMisDelivery:edit','input:stkMisDelivery:edit');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('修改','input:cnJournalForCash:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'216','input:cnJournalForCash:edit','input:cnJournalForCash:edit');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('修改','input:cnJournalForBank:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'217','input:cnJournalForBank:edit','input:cnJournalForBank:edit');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('修改','input:purMrb:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'200','input:purMrb:edit','input:purMrb:edit');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('修改','input:stkInStock:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'138','input:stkInStock:edit','input:stkInStock:edit');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('修改','input:arOtherRecAble:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'202','input:arOtherRecAble:edit','input:arOtherRecAble:edit');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('修改','input:arRefundBill:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'203','input:arRefundBill:edit','input:arRefundBill:edit');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('修改','input:apPayBill:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'204','input:apPayBill:edit','input:apPayBill:edit');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('查看','report:supplierPayable:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'180','report:supplierPayable:view','report:supplierPayable:view');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('导出','report:supplierPayable:export',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'180','report:supplierPayable:export','report:supplierPayable:export');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('查看','report:customerReceive:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'182','report:customerReceive:view','report:customerReceive:view');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('导出','report:customerReceive:export',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'182','report:customerReceive:export','report:customerReceive:export');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('查看','report:consignmentReport:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'186','report:consignmentReport:view','report:consignmentReport:view');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('导出','report:consignmentReport:export',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'186','report:consignmentReport:export','report:consignmentReport:export');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('查看','report:retailAccount:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'187','report:retailAccount:view','report:retailAccount:view');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('导出','report:retailAccount:export',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'187','report:retailAccount:export','report:retailAccount:export');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('查看','sys:kingdeeBookList:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'181','sys:kingdeeBookList:view','sys:kingdeeBookList:view');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('修改','sys:kingdeeBookForm:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'181','sys:kingdeeBookForm:edit','sys:kingdeeBookForm:edit');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('删除','sys:kingdeeBook:delete',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'181','sys:kingdeeBook:delete','sys:kingdeeBook:delete');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('查看','sys:accountKingdeeBookList:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'188','sys:accountKingdeeBookList:view','sys:accountKingdeeBookList:view');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('修改','sys:accountKingdeeBookForm:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'188','sys:accountKingdeeBookForm:edit','sys:accountKingdeeBookForm:edit');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('删除','sys:accountKingdeeBook:delete',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'188','sys:accountKingdeeBook:delete','sys:accountKingdeeBook:delete');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('修改','sys:productManager:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'188','sys:productManager:edit','sys:productManager:edit');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('查看','sys:voucherList:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'220','sys:voucherList:view','sys:voucherList:view');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('修改','sys:voucherForm:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'220','sys:voucherForm:edit','sys:voucherForm:edit');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('审核','sys:voucherForm:audit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'220','sys:voucherForm:audit','sys:voucherForm:audit');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('删除','sys:voucherForm:delete',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'220','sys:voucherForm:delete','sys:voucherForm:delete');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('查看','sys:kingdeeSynList:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'168','sys:kingdeeSynList:view','sys:kingdeeSynList:view');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('查看明细','sys:kingdeeSynDetail:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'168','sys:kingdeeSynDetail:view','sys:kingdeeSynDetail:view');

ALTER TABLE `sys_permission`
DROP COLUMN `url`,
DROP COLUMN `method`;

INSERT INTO `sys_role_module` VALUES ('5', '21', '4', '1', '2017-06-29 10:57:00', '1', '2017-06-29 10:57:00', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('6', '20', '4', '1', '2017-06-29 10:57:07', '1', '2017-06-29 10:57:07', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('7', '19', '3', '1', '2017-06-29 10:57:18', '1', '2017-06-29 10:57:18', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('8', '19', '4', '1', '2017-06-29 10:57:18', '1', '2017-06-29 10:57:18', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('9', '18', '3', '1', '2017-06-29 10:57:29', '1', '2017-06-29 10:57:29', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('10', '18', '4', '1', '2017-06-29 10:57:29', '1', '2017-06-29 10:57:29', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('11', '1', '1', '1', '2017-06-29 10:57:34', '1', '2017-06-29 10:57:34', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('12', '1', '2', '1', '2017-06-29 10:57:34', '1', '2017-06-29 10:57:34', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('13', '1', '3', '1', '2017-06-29 10:57:34', '1', '2017-06-29 10:57:34', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('14', '1', '4', '1', '2017-06-29 10:57:34', '1', '2017-06-29 10:57:34', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('15', '1', '5', '1', '2017-06-29 10:57:34', '1', '2017-06-29 10:57:34', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('16', '1', '6', '1', '2017-06-29 10:57:34', '1', '2017-06-29 10:57:34', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('17', '1', '7', '1', '2017-06-29 10:57:34', '1', '2017-06-29 10:57:34', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('18', '1', '8', '1', '2017-06-29 10:57:34', '1', '2017-06-29 10:57:34', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('19', '1', '9', '1', '2017-06-29 10:57:34', '1', '2017-06-29 10:57:34', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('20', '1', '10', '1', '2017-06-29 10:57:34', '1', '2017-06-29 10:57:34', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('21', '25', '7', '1', '2017-06-29 10:57:44', '1', '2017-06-29 10:57:44', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('22', '25', '9', '1', '2017-06-29 10:57:44', '1', '2017-06-29 10:57:44', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('23', '25', '10', '1', '2017-06-29 10:57:44', '1', '2017-06-29 10:57:44', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('24', '24', '7', '1', '2017-06-29 10:57:52', '1', '2017-06-29 10:57:52', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('25', '24', '9', '1', '2017-06-29 10:57:52', '1', '2017-06-29 10:57:52', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('26', '24', '10', '1', '2017-06-29 10:57:52', '1', '2017-06-29 10:57:52', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('27', '18', '5', '1', '2017-06-29 11:24:40', '1', '2017-06-29 11:24:40', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('28', '19', '5', '1', '2017-06-29 11:24:51', '1', '2017-06-29 11:24:51', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('29', '22', '2', '1', '2017-06-29 11:34:37', '1', '2017-06-29 11:34:37', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('30', '22', '4', '1', '2017-06-29 11:34:37', '1', '2017-06-29 11:34:37', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('31', '22', '8', '1', '2017-06-29 11:34:37', '1', '2017-06-29 11:34:37', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('32', '23', '2', '1', '2017-06-29 11:34:49', '1', '2017-06-29 11:34:49', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('33', '23', '4', '1', '2017-06-29 11:34:49', '1', '2017-06-29 11:34:49', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('34', '22', '3', '1', '2017-06-29 11:36:59', '1', '2017-06-29 11:36:59', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('35', '22', '5', '1', '2017-06-29 11:36:59', '1', '2017-06-29 11:36:59', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('36', '23', '3', '1', '2017-06-29 11:37:07', '1', '2017-06-29 11:37:07', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('37', '23', '5', '1', '2017-06-29 11:37:07', '1', '2017-06-29 11:37:07', '', '0', '0', '1');
INSERT INTO `sys_role_module` VALUES ('38', '23', '8', '1', '2017-06-29 11:37:07', '1', '2017-06-29 11:37:07', '', '0', '0', '1');

INSERT INTO `sys_role_permission` VALUES ('1', '18', '121', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('2', '18', '122', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('3', '18', '123', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('4', '18', '358', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('5', '18', '359', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('6', '18', '360', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('7', '18', '361', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('8', '18', '362', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('9', '18', '363', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('16', '18', '444', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('17', '18', '445', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('20', '18', '491', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('21', '18', '492', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('22', '18', '493', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('23', '18', '584', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('24', '18', '585', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('29', '18', '626', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('30', '18', '637', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('31', '18', '690', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('32', '18', '695', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('33', '18', '783', '1', '2017-06-29 11:25:46', '1', '2017-06-29 11:25:46', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('40', '19', '121', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('41', '19', '122', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('42', '19', '123', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('43', '19', '358', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('44', '19', '359', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('45', '19', '360', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('46', '19', '361', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('47', '19', '362', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('48', '19', '363', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('61', '19', '444', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('62', '19', '445', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('65', '19', '491', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('66', '19', '492', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('67', '19', '493', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('71', '19', '584', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('72', '19', '585', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('77', '19', '626', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('78', '19', '637', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('79', '19', '690', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('80', '19', '695', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('84', '19', '748', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('85', '19', '749', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('86', '19', '750', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('87', '19', '754', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('88', '19', '783', '1', '2017-06-29 11:26:20', '1', '2017-06-29 11:26:20', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('218', '25', '788', '1', '2017-06-29 11:31:22', '1', '2017-06-29 11:31:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('219', '25', '789', '1', '2017-06-29 11:31:22', '1', '2017-06-29 11:31:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('220', '25', '790', '1', '2017-06-29 11:31:22', '1', '2017-06-29 11:31:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('224', '22', '112', '1', '2017-06-29 11:36:51', '1', '2017-06-29 11:36:51', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('225', '22', '113', '1', '2017-06-29 11:36:51', '1', '2017-06-29 11:36:51', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('226', '22', '114', '1', '2017-06-29 11:36:51', '1', '2017-06-29 11:36:51', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('227', '22', '426', '1', '2017-06-29 11:36:51', '1', '2017-06-29 11:36:51', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('228', '22', '540', '1', '2017-06-29 11:36:51', '1', '2017-06-29 11:36:51', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('229', '22', '600', '1', '2017-06-29 11:36:51', '1', '2017-06-29 11:36:51', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('230', '22', '694', '1', '2017-06-29 11:36:51', '1', '2017-06-29 11:36:51', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('231', '22', '696', '1', '2017-06-29 11:36:51', '1', '2017-06-29 11:36:51', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('235', '22', '807', '1', '2017-06-29 11:38:15', '1', '2017-06-29 11:38:15', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('236', '22', '809', '1', '2017-06-29 11:38:15', '1', '2017-06-29 11:38:15', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('237', '22', '810', '1', '2017-06-29 11:38:15', '1', '2017-06-29 11:38:15', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('241', '23', '112', '1', '2017-06-29 11:39:17', '1', '2017-06-29 11:39:17', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('242', '23', '113', '1', '2017-06-29 11:39:17', '1', '2017-06-29 11:39:17', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('243', '23', '114', '1', '2017-06-29 11:39:17', '1', '2017-06-29 11:39:17', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('244', '23', '426', '1', '2017-06-29 11:39:17', '1', '2017-06-29 11:39:17', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('245', '23', '540', '1', '2017-06-29 11:39:17', '1', '2017-06-29 11:39:17', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('246', '23', '600', '1', '2017-06-29 11:39:17', '1', '2017-06-29 11:39:17', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('247', '23', '694', '1', '2017-06-29 11:39:17', '1', '2017-06-29 11:39:17', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('248', '23', '696', '1', '2017-06-29 11:39:17', '1', '2017-06-29 11:39:17', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('249', '23', '807', '1', '2017-06-29 11:39:17', '1', '2017-06-29 11:39:17', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('250', '23', '809', '1', '2017-06-29 11:39:17', '1', '2017-06-29 11:39:17', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('251', '23', '810', '1', '2017-06-29 11:39:17', '1', '2017-06-29 11:39:17', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('252', '1', '8', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('253', '1', '10', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('254', '1', '11', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('255', '1', '14', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('256', '1', '23', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('257', '1', '24', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('258', '1', '25', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('259', '1', '26', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('260', '1', '27', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('261', '1', '34', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('262', '1', '35', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('263', '1', '36', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('264', '1', '37', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('265', '1', '38', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('266', '1', '39', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('267', '1', '40', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('268', '1', '41', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('269', '1', '42', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('273', '1', '46', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('274', '1', '47', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('275', '1', '48', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('279', '1', '103', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('280', '1', '104', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('281', '1', '105', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('282', '1', '112', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('283', '1', '113', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('284', '1', '114', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('285', '1', '115', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('286', '1', '116', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('287', '1', '117', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('288', '1', '118', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('289', '1', '119', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('290', '1', '120', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('291', '1', '121', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('292', '1', '122', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('293', '1', '123', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('294', '1', '124', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('295', '1', '125', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('296', '1', '126', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('297', '1', '134', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('298', '1', '135', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('299', '1', '136', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('300', '1', '137', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('301', '1', '138', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('302', '1', '139', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('303', '1', '140', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('304', '1', '141', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('305', '1', '142', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('306', '1', '143', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('307', '1', '144', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('308', '1', '145', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('309', '1', '146', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('310', '1', '147', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('311', '1', '148', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('312', '1', '149', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('313', '1', '150', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('314', '1', '151', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('315', '1', '154', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('316', '1', '155', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('317', '1', '297', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('318', '1', '298', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('319', '1', '321', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('320', '1', '322', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('321', '1', '323', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('322', '1', '324', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('323', '1', '325', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('324', '1', '326', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('325', '1', '327', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('326', '1', '328', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('327', '1', '329', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('328', '1', '330', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('329', '1', '331', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('330', '1', '332', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('331', '1', '336', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('332', '1', '337', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('333', '1', '338', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('334', '1', '339', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('335', '1', '340', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('336', '1', '341', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('337', '1', '342', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('338', '1', '343', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('339', '1', '344', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('340', '1', '345', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('341', '1', '346', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('342', '1', '347', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('343', '1', '348', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('344', '1', '349', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('345', '1', '350', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('346', '1', '351', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('347', '1', '352', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('348', '1', '353', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('349', '1', '354', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('350', '1', '355', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('351', '1', '356', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('352', '1', '357', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('353', '1', '358', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('354', '1', '359', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('355', '1', '360', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('356', '1', '361', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('357', '1', '362', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('358', '1', '363', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('371', '1', '378', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('372', '1', '379', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('373', '1', '380', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('374', '1', '381', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('375', '1', '392', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('376', '1', '393', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('377', '1', '394', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('378', '1', '396', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('379', '1', '397', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('380', '1', '398', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('381', '1', '402', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('382', '1', '403', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('383', '1', '404', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('384', '1', '408', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('385', '1', '409', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('386', '1', '410', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('387', '1', '426', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('388', '1', '427', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('389', '1', '428', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('390', '1', '432', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('391', '1', '433', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('392', '1', '434', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('393', '1', '441', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('394', '1', '442', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('395', '1', '443', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('396', '1', '444', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('397', '1', '445', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('398', '1', '458', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('399', '1', '459', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('400', '1', '460', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('401', '1', '461', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('402', '1', '462', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('403', '1', '463', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('404', '1', '467', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('405', '1', '468', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('406', '1', '469', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('409', '1', '491', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('410', '1', '492', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('411', '1', '493', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('412', '1', '497', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('413', '1', '498', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('414', '1', '499', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('415', '1', '509', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('416', '1', '510', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('417', '1', '511', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('418', '1', '512', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('419', '1', '513', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('420', '1', '514', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('421', '1', '515', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('422', '1', '516', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('423', '1', '517', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('424', '1', '518', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('425', '1', '519', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('426', '1', '520', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('427', '1', '521', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('428', '1', '522', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('429', '1', '523', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('430', '1', '530', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('431', '1', '531', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('432', '1', '532', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('436', '1', '540', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('437', '1', '542', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('438', '1', '543', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('439', '1', '544', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('440', '1', '545', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('441', '1', '546', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('442', '1', '547', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('443', '1', '548', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('444', '1', '549', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('445', '1', '550', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('446', '1', '551', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('447', '1', '552', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('448', '1', '553', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('449', '1', '554', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('450', '1', '555', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('451', '1', '556', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('452', '1', '557', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('453', '1', '558', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('454', '1', '559', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('455', '1', '560', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('456', '1', '561', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('457', '1', '562', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('458', '1', '563', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('459', '1', '564', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('460', '1', '565', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('461', '1', '566', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('462', '1', '567', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('463', '1', '568', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('464', '1', '569', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('465', '1', '570', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('466', '1', '571', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('467', '1', '572', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('468', '1', '573', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('469', '1', '574', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('470', '1', '575', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('471', '1', '576', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('472', '1', '577', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('473', '1', '578', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('474', '1', '579', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('475', '1', '580', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('476', '1', '581', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('477', '1', '582', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('478', '1', '583', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('479', '1', '584', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('480', '1', '585', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('481', '1', '600', '1', '2017-06-29 15:19:22', '1', '2017-06-29 15:19:22', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('482', '1', '601', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('483', '1', '602', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('486', '1', '612', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('488', '1', '624', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('490', '1', '626', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('491', '1', '628', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('492', '1', '630', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('493', '1', '637', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('494', '1', '649', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('495', '1', '650', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('496', '1', '651', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('497', '1', '653', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('498', '1', '654', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('499', '1', '655', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('500', '1', '656', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('501', '1', '690', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('502', '1', '694', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('503', '1', '695', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('504', '1', '696', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('505', '1', '721', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('506', '1', '734', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('510', '1', '741', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('511', '1', '748', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('512', '1', '749', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('513', '1', '750', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('514', '1', '754', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('515', '1', '780', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('516', '1', '781', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('517', '1', '782', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('518', '1', '783', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('519', '1', '784', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('520', '1', '785', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('521', '1', '786', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('522', '1', '787', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('523', '1', '788', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('524', '1', '789', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('525', '1', '790', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('526', '1', '791', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('527', '1', '796', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('528', '1', '797', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('529', '1', '798', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('530', '1', '799', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('531', '1', '800', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('532', '1', '801', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('533', '1', '803', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('534', '1', '804', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('535', '1', '805', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('536', '1', '806', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('537', '1', '807', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('538', '1', '808', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('539', '1', '809', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('540', '1', '810', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('544', '1', '814', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('545', '1', '815', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('546', '1', '816', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('547', '1', '817', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('548', '1', '818', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('549', '1', '819', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('550', '1', '820', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('551', '1', '821', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('552', '1', '822', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('553', '1', '823', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('554', '1', '824', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('555', '1', '825', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('556', '1', '826', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('557', '1', '827', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('558', '1', '828', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('559', '1', '829', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('560', '1', '830', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('561', '1', '831', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('562', '1', '832', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('563', '1', '833', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('564', '1', '834', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('565', '1', '835', '1', '2017-06-29 15:19:23', '1', '2017-06-29 15:19:23', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('566', '24', '836', '1', '2017-06-29 15:50:25', '1', '2017-06-29 15:50:25', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('567', '24', '837', '1', '2017-06-29 15:50:25', '1', '2017-06-29 15:50:25', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('568', '24', '838', '1', '2017-06-29 15:50:25', '1', '2017-06-29 15:50:25', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('569', '24', '839', '1', '2017-06-29 15:50:25', '1', '2017-06-29 15:50:25', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('570', '24', '840', '1', '2017-06-29 15:50:25', '1', '2017-06-29 15:50:25', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('571', '24', '841', '1', '2017-06-29 15:50:25', '1', '2017-06-29 15:50:25', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('572', '24', '843', '1', '2017-06-29 15:50:25', '1', '2017-06-29 15:50:25', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('573', '24', '844', '1', '2017-06-29 15:50:25', '1', '2017-06-29 15:50:25', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('574', '24', '845', '1', '2017-06-29 15:50:25', '1', '2017-06-29 15:50:25', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('575', '25', '836', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('576', '25', '837', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('577', '25', '838', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('578', '25', '839', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('579', '25', '840', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('580', '25', '841', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('581', '25', '843', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('582', '25', '844', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('583', '25', '845', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('584', '25', '846', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('585', '25', '847', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('586', '25', '848', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('587', '25', '849', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('588', '25', '850', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('589', '25', '851', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('590', '25', '852', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('591', '25', '853', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('592', '25', '854', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('593', '25', '855', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('594', '25', '856', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('595', '25', '857', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('596', '25', '858', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('597', '25', '859', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('598', '25', '860', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('599', '25', '861', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('600', '25', '862', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('601', '25', '863', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('602', '25', '864', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('603', '25', '865', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');
INSERT INTO `sys_role_permission` VALUES ('604', '25', '866', '1', '2017-06-29 15:50:35', '1', '2017-06-29 15:50:35', '', '0', '0', '1');

INSERT INTO `sys_office`(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,company_id,type,parent_id,parent_ids,point,ding_id,level,joint_type,tag,task_point,agent_code,sort,office_rule_id,area_idjoint_level)VALUES('总公司内销部','1','2017-06-29 16:39:55','1','2017-06-29 16:39:55',NULL,'0','0','1','1','职能部门','127','0,',NULL,NULL,'2','直营',NULL,NULL,NULL,NULL,NULL,NULL,'一级');
INSERT INTO `sys_office`(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,company_id,type,parent_id,parent_ids,point,ding_id,level,joint_type,tag,task_point,agent_code,sort,office_rule_id,area_idjoint_level)VALUES('总公司财务部','1','2017-06-29 16:39:55','1','2017-06-29 16:39:55',NULL,'0','0','1','1','职能部门','127','0,',NULL,NULL,'2','直营',NULL,NULL,NULL,NULL,NULL,NULL,'一级');
