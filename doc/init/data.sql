insert into sys_company_config values (1023,'公司名称','COMPANY_NAME','IDVIVO','1','2017-06-26 17:19:10','1','2017-06-26 17:19:13','1','0','0','1','1');

INSERT INTO `sys_office_rule`(name,code,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,company_id,has_point,parent_id,parent_ids,level)VALUES('大区','districtOffice','1','2017-04-26 13:56:21','1','2017-06-26 18:10:41',NULL ,'0','0','1','1','0',NULL ,'0,','1');
INSERT INTO `sys_office_rule`(name,code,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,company_id,has_point,parent_id,parent_ids,level)VALUES('省份','provinceOffice','1','2017-04-26 13:56:21','1','2017-06-26 18:10:41',NULL ,'0','0','1','1','0',1 ,'0,1,','1');
INSERT INTO `sys_office_rule`(name,code,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,company_id,has_point,parent_id,parent_ids,level)VALUES('办事处','areaOffice','1','2017-04-26 13:56:21','1','2017-06-26 18:10:41',NULL ,'0','0','1','1','0',2 ,'0,1,2,','1');
INSERT INTO `sys_office_rule`(name,code,created_by,created_date,last_modified_by,last_modified_date,remarks,version,locked,enabled,company_id,has_point,parent_id,parent_ids,level)VALUES('考核区域','taskOffice','1','2017-04-26 13:56:21','1','2017-06-26 18:10:41',NULL ,'0','0','1','1','0',3 ,'0,1,2,3','1');

update sys_office set joint_level="二级";
update sys_office set office_rule_id=1 where type='30';
update sys_office set office_rule_id=2 where type='50';
update sys_office set office_rule_id=3 where type='100';
update sys_office set office_rule_id=4 where type='200';
update sys_office SET type='职能部门' where type='0';
update sys_office SET type='业务部门' where type!='职能部门';
update sys_office of,sys_office of1  set of.area_id=of1.id where  of.parent_ids like CONCAT('%,',of1.id,',%') and of1.office_rule_id=1;
update sys_office  set area_id=id where area_id is NULL;
update sys_office set all_data_scope=1 where type='职能部门';
