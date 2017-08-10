insert into sys_company_config values (1023,'公司名称','COMPANY_NAME','WZOPPO','1','2017-06-26 17:19:10','1','2017-06-26 17:19:13','1','0','0','1','1');
insert into sys_company_config values (1024,'EXPRESS_PRINT_QTY','EXPRESS_PRINT_QTY','14','1','2017-06-26 17:19:10','1','2017-06-26 17:19:13','1','0','0','1','1');

INSERT INTO `sys_office_rule`(name,code,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,company_id,has_point,parent_id,parent_ids,level)VALUES('办事处','areaOffice','1','2017-04-26 13:56:21','1','2017-06-26 18:10:41',NULL ,'0','0','1','1','0',NULL ,'0,','1');
INSERT INTO `sys_office_rule`(name,code,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,company_id,has_point,parent_id,parent_ids,level)VALUES('考核区域','taskOffice','1','2017-04-26 13:56:21','1','2017-06-26 18:10:41',NULL ,'0','0','1','1','0',1,'0,1,','2');

update sys_office set joint_level="二级";
update sys_office set office_rule_id=1 where type='100';
update sys_office set office_rule_id=2 where type='200';
update sys_office SET type='职能部门' where type IN ('0','2');
update sys_office SET type='业务部门' where type!='职能部门';
update sys_office of,sys_office of1  set of.area_id=of1.id where  of.parent_ids like CONCAT('%,',of1.id,',%') and of1.office_rule_id=1;
update sys_office  set area_id=id where area_id is NULL;
update sys_office set all_data_scope=1 where type='职能部门';

update hr_account set position_ids=position_id where  position_ids is NULL;
update hr_account t1 SET t1.office_ids=t1.office_id where t1.office_ids is NULL ;


