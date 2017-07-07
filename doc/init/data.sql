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

UPDATE sys_menu_category SET enabled=0;
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
update sys_menu set enabled=false where name='商城门店';
update sys_menu set enabled=false where name='商城货品';
update sys_menu set enabled=false where name='移动订货';



update sys_menu_category set enabled=false where name='客服中心';
update sys_menu_category set enabled=false where name='数据管理';
update sys_menu_category set enabled=false where name='工资管理';
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
INSERT into sys_role(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,permission)VALUES('体专专员',1,'2017-06-30 12:22:34',1,'2017-06-30 12:22:34','',0,0,1,'体专专员');
INSERT into sys_role(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,permission)VALUES('财务部文员',1,'2017-06-30 12:22:34',1,'2017-06-30 12:22:34','',0,0,1,'财务部文员');
INSERT into sys_role(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,permission)VALUES('财务部主管',1,'2017-06-30 12:22:34',1,'2017-06-30 12:22:34','',0,0,1,'财务部主管');
INSERT into sys_role(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,permission)VALUES('办事处文员',1,'2017-06-30 12:22:34',1,'2017-06-30 12:22:34','',0,0,1,'办事处文员');
INSERT into sys_role(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,permission)VALUES('直营部文员',1,'2017-06-30 12:22:34',1,'2017-06-30 12:22:34','',0,0,1,'直营部文员');
INSERT into sys_role(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,permission)VALUES('企划部文员',1,'2017-06-30 12:22:34',1,'2017-06-30 12:22:34','',0,0,1,'企划部文员');
INSERT into sys_role(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,permission)VALUES('企划部部长',1,'2017-06-30 12:22:34',1,'2017-06-30 12:22:34','',0,0,1,'企划部部长');



update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='导购') where t1.name='市县导购';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='导购') where t1.name='流动导购';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='导购') where t1.name='乡镇导购';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='导购') where t1.name='返聘导购';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='导购') where t1.name='体专店员';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='导购') where t1.name='体专店长';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='导购') where t1.name='代理店长';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='导购') where t1.name='体专终端';

update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='体专专员') where t1.name='体专专员';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='体专主管') where t1.name='体专主管';

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
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='办事处文员') where t1.name='办事处文员';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='直营部文员') where t1.name='直营部文员';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='企划部文员') where t1.name='企划部专员';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='企划部部长') where t1.name='企划部主管';
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='企划部部长') where t1.name='企划部部长';


update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='业务') where (t1.role_id is NULL OR t1.role_id=1);
DELETE FROM sys_role where id=7;
update hr_position t1 set t1.role_id=(select t2.id from sys_role t2 where t2.name='管理员') where (t1.name="信息部专员");

INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('查看','crm:depotShop:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'208','/api/ws/future/basic/depotShop','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('业务属性修改','crm:depotShop:businessEdit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'208','/api/ws/future/basic/depotShop/saveDepot','POST');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('删除','crm:depotShop:delete',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'208','/api/ws/future/basic/depotShop/delete','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('基础属性修改','crm:depotShop:basicEdit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'208','/api/ws/future/basic/depotShop/save','POST');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('查看','crm:goodsOrderShip:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'169','/api/ws/future/crm/goodsOrderShip','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('修改','crm:goodsOrderShip:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'169','/api/ws/future/crm/goodsOrderShip/save','POST');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('删除','crm:goodsOrderShip:delete',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'169','/api/ws/future/crm/goodsOrderShip/delete','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('查看','crm:storeInventoryReport:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'219','/api/ws/future/basic/depotStore/storeReport','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('查看','sys:backendModule:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'176','/api/basic/sys/backendModule','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('修改','sys:backendModule:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'176','/api/basic/sys/backendModule/save','POST');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('删除','sys:backendModule:delete',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'176','/api/basic/sys/backendModule/delete','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('查看','sys:role:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'183','/api/basic/sys/role','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('修改','sys:role:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'183','/api/basic/sys/role/save','PSOT');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('删除','sys:role:delete',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'183','/api/basic/sys/role/delete','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('查看','sys:backend:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'177','/api/basic/sys/backend','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('修改','sys:backend:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'177','/api/basic/sys/backend/save','POST');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('删除','sys:backend:delete',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'177','/api/basic/sys/backend/delete','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('查看','sys:officeRule:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'178','/api/basic/sys/officeRule','GET');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('修改','sys:officeRule:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'178','/api/basic/sys/officeRule/delete','POST');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id,url,method)VALUES('删除','sys:officeRule:delete',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'178','/api/basic/sys/officeRule/save','GET');
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

update sys_permission set enabled=false where permission not in ("crm:goodsOrder:view","crm:goodsOrder:bill","crm:goodsOrder:edit","crm:goodsOrder:delete","crm:goodsOrder:print") and permission like "crm:goodsorder:%";

INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id)VALUES('开单','crm:goodsOrderShip:ship',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'169');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id)VALUES('签收','crm:goodsOrderShip:sign',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'169');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id)VALUES('重发','crm:goodsOrderShip:shipBack',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'169');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id)VALUES('商城订单','crm:goodsOrderShip:mallOrder',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'169');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id)VALUES('销售退回','crm:goodsOrderShip:sreturn',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'169');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id)VALUES('打印','crm:goodsOrderShip:print',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'169');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id)VALUES('商城相关状态修改','api:carrierOrder:updateStatusAndRemarks',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'46');

INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id)VALUES('查看','crm:materialPriceManager:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'222');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id)VALUES('修改','crm:materialPriceManager:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'222');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id)VALUES('删除','crm:materialPriceManager:delete',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'222');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id)VALUES('查看','crm:vivoFactoryOrderList:view',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'221');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id)VALUES('修改','crm:vivoFactoryOrderList:edit',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'221');
INSERT into sys_permission(name,permission,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,menu_id)VALUES('删除','crm:vivoFactoryOrderList:delete',1,'2017-07-01 12:21:34',1,'2017-07-01 12:21:34','',0,0,1,'221');


INSERT INTO `sys_office`(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,company_id,type,parent_id,parent_ids,point,ding_id,level,joint_type,tag,task_point,agent_code,sort,office_rule_id,area_id,joint_level,all_data_scope)VALUES('总公司内销部','1','2017-06-29 16:39:55','1','2017-06-29 16:39:55',NULL,'0','0','1','1','职能部门','127','0,',NULL,NULL,'2','直营',NULL,NULL,NULL,NULL,NULL,NULL,'一级',true);
INSERT INTO `sys_office`(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,company_id,type,parent_id,parent_ids,point,ding_id,level,joint_type,tag,task_point,agent_code,sort,office_rule_id,area_id,joint_level,all_data_scope)VALUES('总公司财务部','1','2017-06-29 16:39:55','1','2017-06-29 16:39:55',NULL,'0','0','1','1','职能部门','127','0,',NULL,NULL,'2','直营',NULL,NULL,NULL,NULL,NULL,NULL,'一级',true);
INSERT INTO `sys_office`(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,company_id,type,parent_id,parent_ids,point,ding_id,level,joint_type,tag,task_point,agent_code,sort,office_rule_id,area_id,joint_level,all_data_scope)VALUES('总公司仓储部','1','2017-06-29 16:39:55','1','2017-06-29 16:39:55',NULL,'0','0','1','1','职能部门','127','0,',NULL,NULL,'2','直营',NULL,NULL,NULL,NULL,NULL,NULL,'一级',true);
INSERT INTO `sys_office`(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,company_id,type,parent_id,parent_ids,point,ding_id,level,joint_type,tag,task_point,agent_code,sort,office_rule_id,area_id,joint_level,all_data_scope)VALUES('总公司企划部','1','2017-06-29 16:39:55','1','2017-06-29 16:39:55',NULL,'0','0','1','1','职能部门','127','0,',NULL,NULL,'2','直营',NULL,NULL,NULL,NULL,NULL,NULL,'一级',true);
INSERT INTO `sys_office`(name,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,company_id,type,parent_id,parent_ids,point,ding_id,level,joint_type,tag,task_point,agent_code,sort,office_rule_id,area_id,joint_level,all_data_scope)VALUES('总公司业务部','1','2017-06-29 16:39:55','1','2017-06-29 16:39:55',NULL,'0','0','1','1','职能部门','127','0,',NULL,NULL,'2','直营',NULL,NULL,NULL,NULL,NULL,NULL,'一级',true);


update hr_account t1 set t1.office_id=(SELECT t2.id FROM sys_office t2 where t2.name="总公司内销部") where t1.position_id in (select t3.id FROM hr_position t3 where t3.name in ("内销部文员","内销部主管","内销部部长"));
update hr_account t1 set t1.office_id=(SELECT t2.id FROM sys_office t2 where t2.name="总公司财务部") where t1.position_id in (select t3.id FROM hr_position t3 where t3.name in ("财务部主管","财务部专员","财务部部长","省公司财务出纳"));
update hr_account t1 set t1.office_id=(SELECT t2.id FROM sys_office t2 where t2.name="总公司仓储部") where t1.position_id in (select t3.id FROM hr_position t3 where t3.name in ("仓储部专员","仓储部主管"));
update hr_account t1 set t1.office_id=(SELECT t2.id FROM sys_office t2 where t2.name="总公司企划部") where t1.position_id in (select t3.id FROM hr_position t3 where t3.name in ("企划部专员","企划部主管","企划部部长"));


update sys_dict_enum set remarks=concat(remarks,"(内藏卡布灯箱,必须在原始基础尺寸上扩宽20公分)","") where `value`="室内四面包柱（2面装发光字2面内藏卡布灯箱）" and category="装修类别";
update sys_dict_enum set remarks=concat(remarks,"(门头宽度必须≥2m)","") where `value`="门头" and category="装修类别";

update sys_office set enabled=false where name like "%废弃%" or name like "%作废%";

update sys_menu set name='渠道库存' where name='库存报表';
update sys_menu set sort=10 where name='销售报表';
update sys_menu set sort=20 where name='渠道库存';
update sys_menu set sort=30 where name='仓库报表';
update sys_menu set sort=40 where name='每日打分';
update sys_menu set sort=50 where name='每日排名';
