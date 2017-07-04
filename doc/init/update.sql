### 修改工厂相关名称
rename table api_imoo_plant_basic_product to imoo_plant_basic_product;
rename table api_imoo_prdocut_imei_deliver to imoo_prdocut_imei_deliver;
rename table api_imoo_product_map to imoo_product_map;
rename table api_oppo_plant_product_itemelectron_sel to oppo_plant_product_itemelectron_sel;
rename table api_oppo_plant_send_imei_ppsel to oppo_plant_send_imei_ppsel;
rename table api_oppo_plant_agent_product_sel to oppo_plant_agent_product_sel;
rename table api_oppo_plant_product_sel to oppo_plant_product_sel;
rename table api_vivo_plant_electronicsn to vivo_plant_electronicsn;
rename table api_vivo_plant_products to vivo_plant_products;
rename table api_vivo_plant_sendimei to vivo_plant_sendimei;
rename table api_vivo_products to vivo_products;


### 修改字段
ALTER TABLE `crm_ad_goods_order` ADD COLUMN `process_position_id`  bigint(20) NULL AFTER `invest_in_cause`;
update crm_ad_goods_order t1,sys_process_flow t2 set t1.process_position_id = t2.position_id where t1.process_flow_id=t2.id;


DROP TABLE IF EXISTS `crm_ad_pricesystem_district`;

DROP TABLE IF EXISTS `crm_ad_pricesystem_office`;
CREATE TABLE `crm_ad_pricesystem_office` (
  `ad_pricesystem_id` bigint(20) NOT NULL,
  `office_id` bigint(20) NOT NULL,
  PRIMARY KEY (`ad_pricesystem_id`,`office_id`),
  KEY `office_id` (`office_id`) USING BTREE
);


ALTER TABLE `crm_bank_in`
ADD COLUMN `position_id`  bigint(20) NULL AFTER `check_id`;
update crm_bank_in t1,sys_process_flow t2 set t1.position_id = t2.position_id where t1.process_flow_id=t2.id;

###客户
DROP TABLE IF EXISTS `crm_dealer`;
DROP TABLE IF EXISTS `crm_client`;
CREATE TABLE `crm_client` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `mobile_phone` varchar(64) DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` bigint(20) NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `locked` tinyint(1) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `company_id` bigint(20) NOT NULL DEFAULT '1',
  `out_id` varchar(64) DEFAULT NULL,
  `out_group_id` varchar(64) DEFAULT NULL,
  `out_group_name` varchar(64) DEFAULT NULL,
  `out_date` datetime DEFAULT NULL,
  `out_code` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `created_by` (`created_by`) USING BTREE,
  KEY `created_date` (`created_date`) USING BTREE,
  KEY `last_modified_by` (`last_modified_by`) USING BTREE,
  KEY `last_modified_date` (`last_modified_date`) USING BTREE,
  KEY `name` (`name`) USING BTREE
);

DROP TABLE IF EXISTS `crm_depot_shop`;
CREATE TABLE `crm_depot_shop` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `depot_id` bigint(20) DEFAULT NULL,
  `area_type` varchar(64) DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` bigint(20) NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `locked` tinyint(1) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `company_id` bigint(20) NOT NULL DEFAULT '1',
  `has_guide` tinyint(1) DEFAULT NULL,
  `lng` varchar(64) DEFAULT NULL,
  `lat` varchar(64) DEFAULT NULL,
  `check_stock` tinyint(1) DEFAULT NULL,
  `town_type` varchar(64) DEFAULT NULL,
  `chain_type` varchar(64) DEFAULT NULL,
  `carrier_type` varchar(64) DEFAULT NULL,
  `turnover_type` varchar(64) DEFAULT NULL,
  `business_type` varchar(64) DEFAULT NULL,
  `channel_type` varchar(64) DEFAULT NULL,
  `sale_point_type` varchar(64) DEFAULT NULL,
  `bussiness_center` tinyint(1) DEFAULT NULL,
  `bussiness_center_name` varchar(255) DEFAULT NULL,
  `door_head` tinyint(1) DEFAULT NULL,
  `speciality_store` tinyint(1) DEFAULT NULL,
  `speciality_store_type` varchar(64) DEFAULT NULL,
  `report_name` varchar(64) DEFAULT NULL,
  `shop_area` varchar(64) DEFAULT NULL,
  `frame_num` int(20) DEFAULT NULL,
  `desk_double_num` int(20) DEFAULT NULL,
  `desk_single_num` int(20) DEFAULT NULL,
  `cabinet_num` int(20) DEFAULT NULL,
  `enable_date` datetime DEFAULT NULL,
  `town_name` varchar(255) DEFAULT NULL,
  `ad_shop_bsc` tinyint(1) DEFAULT NULL,
  `town_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
);


DROP TABLE IF EXISTS `crm_depot_store`;
CREATE TABLE `crm_depot_store` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `depot_id` bigint(20) DEFAULT NULL,
  `type` varchar(64) DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` bigint(20) NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `locked` tinyint(1) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `company_id` bigint(20) NOT NULL DEFAULT '1',
  `out_id` varchar(64) DEFAULT NULL,
  `out_group_id` varchar(64) DEFAULT NULL,
  `out_group_name` varchar(64) DEFAULT NULL,
  `out_date` datetime DEFAULT NULL,
  `store_group` varchar(64) DEFAULT NULL,
  `joint_level` varchar(10) DEFAULT NULL,
  `out_code` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `enabled` (`enabled`) USING HASH
);






### employee_phone
rename table hr_employee_phone to crm_employee_phone;
rename table hr_employee_phone_deposit to crm_employee_phone_deposit;


ALTER TABLE `crm_report_score_office`
ADD COLUMN `area_id`  bigint(20) NULL AFTER `report_score_area_id`;
update crm_report_score_office t1,crm_report_score_area t2 set t1.area_id=t2.office_id where t1.report_score_area_id=t2.id;

ALTER TABLE `crm_shop_ad`
ADD COLUMN `process_position_id`  bigint(20) NULL AFTER `process_flow_id`;
update crm_shop_ad t1,sys_process_flow t2 set t1.process_position_id = t2.position_id where t1.process_flow_id=t2.id;


ALTER TABLE `crm_shop_build`
ADD COLUMN `process_position_id`  bigint(20) NULL AFTER `process_flow_id`;
update crm_shop_build t1,sys_process_flow t2 set t1.process_position_id = t2.position_id where t1.process_flow_id=t2.id;


ALTER TABLE `crm_shop_print`
ADD COLUMN `process_position_id`  bigint(20) NULL AFTER `process_flow_id`;
update crm_shop_print t1,sys_process_flow t2 set t1.process_position_id = t2.position_id where t1.process_flow_id=t2.id;


ALTER TABLE `hr_account_change`
ADD COLUMN `position_id`  bigint(20) NULL AFTER `process_flow_id`;
update hr_account_change t1,sys_process_flow t2 set t1.position_id = t2.position_id where t1.process_flow_id=t2.id;

DROP TABLE IF EXISTS `hr_account_menu`;

DROP TABLE IF EXISTS `hr_account_office`;

DROP TABLE IF EXISTS `hr_account_task`;

DROP TABLE IF EXISTS `hr_account_token`;

DROP TABLE IF EXISTS `hr_account_permission`;
CREATE TABLE `hr_account_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `locked` tinyint(1) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `created_by` (`created_by`) USING BTREE,
  KEY `created_date` (`created_date`) USING BTREE,
  KEY `last_modified_by` (`last_modified_by`) USING BTREE,
  KEY `last_modified_date` (`last_modified_date`) USING BTREE
);

DROP TABLE IF EXISTS `hr_account_weixin`;
CREATE TABLE `hr_account_weixin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) DEFAULT NULL,
  `open_id` varchar(64) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `locked` tinyint(1) NOT NULL DEFAULT '0',
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `company_id` bigint(20) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `account_id` (`account_id`) USING BTREE,
  KEY `open_id` (`open_id`) USING BTREE,
  KEY `created_by` (`created_by`) USING BTREE,
  KEY `created_date` (`created_date`) USING BTREE,
  KEY `last_modified_by` (`last_modified_by`) USING BTREE,
  KEY `last_modified_date` (`last_modified_date`) USING BTREE
);

ALTER TABLE `hr_audit_file`
ADD COLUMN `position_id`  bigint(20) NULL AFTER `process_flow_id`;
update hr_audit_file t1,sys_process_flow t2 set t1.position_id = t2.position_id where t1.process_flow_id=t2.id;

ALTER TABLE `hr_duty_worktime`
ADD COLUMN `duty_time_start`  varchar(64) NULL AFTER `company_id`,
ADD COLUMN `duty_time_end`  varchar(64) NULL AFTER `duty_time_start`;




ALTER TABLE `hr_position` DROP FOREIGN KEY `hr_position_ibfk_1`;

ALTER TABLE `hr_position`
ADD COLUMN `company_id`  bigint(20) NULL AFTER `type`,
ADD COLUMN `role_id`  bigint(20) NULL AFTER `company_id`,
DROP INDEX `job_id`;

DROP TABLE IF EXISTS `hr_job`;

DROP TABLE IF EXISTS `hr_notice`;

DROP TABLE IF EXISTS `hr_office_leader`;
CREATE TABLE `hr_office_leader` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `office_id` bigint(20) DEFAULT NULL,
  `leader_id` bigint(20) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `locked` tinyint(1) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT NULL,
  `company_id` bigint(20) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `created_by` (`created_by`) USING BTREE,
  KEY `created_date` (`created_date`) USING BTREE,
  KEY `last_modified_by` (`last_modified_by`) USING BTREE,
  KEY `last_modified_date` (`last_modified_date`) USING BTREE
);
update hr_position set company_id=1;

DROP TABLE IF EXISTS `hr_position_permission`;

DROP TABLE IF EXISTS `hr_recruit`;

DROP TABLE IF EXISTS `sys_api_history`;

DROP TABLE IF EXISTS `sys_company`;

ALTER TABLE `sys_dict_enum`
ADD COLUMN `company_id`  bigint(20) NULL AFTER `enabled`;
update sys_dict_enum set company_id=1;


ALTER TABLE `sys_dict_map`
ADD COLUMN `company_id`  bigint(20) NULL AFTER `enabled`;
update sys_dict_enum set company_id=1;

DROP TABLE IF EXISTS `sys_factory`;
CREATE TABLE `sys_factory` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `company_id` bigint(20) DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `url` varchar(64) DEFAULT NULL,
  `username` varchar(64) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` bigint(20) NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `version` int(11) NOT NULL,
  `remarks` varchar(255) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `locked` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
);



ALTER TABLE `sys_folder_file`
ADD COLUMN `mongo_id`  varchar(255) NULL AFTER `company_id`,
ADD COLUMN `mongo_preview_id`  varchar(255) NULL AFTER `mongo_id`;



DROP TABLE IF EXISTS `sys_office_business`;
CREATE TABLE `sys_office_business` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `office_id` bigint(20) NOT NULL,
  `business_office_id` bigint(20) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` bigint(20) NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `locked` tinyint(1) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `company_id` bigint(20) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `office_id` (`office_id`,`business_office_id`) USING BTREE
);

DROP TABLE IF EXISTS `sys_office_rule`;
CREATE TABLE `sys_office_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `code` varchar(64) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` bigint(20) NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `locked` tinyint(1) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `company_id` bigint(20) NOT NULL DEFAULT '1',
  `has_point` tinyint(1) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `parent_ids` varchar(255) NOT NULL,
  `level` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`,`company_id`) USING BTREE
);




ALTER TABLE `sys_permission`
ADD COLUMN `url`  varchar(255) NULL AFTER `menu_id`,
ADD COLUMN `method`  varchar(255) NULL AFTER `url`;



DROP TABLE IF EXISTS `sys_process_task`;
CREATE TABLE `sys_process_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `process_instance_id` varchar(64) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `status` varchar(64) DEFAULT NULL,
  `office_id` bigint(20) DEFAULT NULL,
  `position_id` bigint(20) DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` bigint(20) NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `locked` tinyint(1) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `company_id` bigint(20) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `office_id` (`office_id`) USING BTREE,
  KEY `created_by` (`created_by`) USING BTREE,
  KEY `created_date` (`created_date`) USING BTREE,
  KEY `last_modified_by` (`last_modified_by`) USING BTREE,
  KEY `last_modified_date` (`last_modified_date`) USING BTREE
);



ALTER TABLE `sys_process_type`
CHANGE COLUMN `view_permission_id` `view_position_ids`  varchar(255) NULL DEFAULT NULL AFTER `name`,
CHANGE COLUMN `create_permission_id` `created_position_ids`  varchar(255) NULL DEFAULT NULL AFTER `view_position_ids`;

update sys_process_type set view_position_ids=null,create_position_ids=null;


DROP TABLE IF EXISTS `sys_product`;
CREATE TABLE `sys_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `code` varchar(64) DEFAULT NULL,
  `price1` decimal(15,9) DEFAULT NULL,
  `out_id` varchar(64) DEFAULT NULL,
  `return_out_id` varchar(64) DEFAULT NULL,
  `out_date` datetime DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` bigint(20) NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `version` int(11) NOT NULL,
  `remarks` varchar(255) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `locked` tinyint(1) DEFAULT NULL,
  `company_id` bigint(20) NOT NULL,
  `kingdee_book_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `out_id` (`out_id`,`company_id`) USING BTREE,
  KEY `out_date` (`out_date`) USING BTREE
);



DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` bigint(20) NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `locked` tinyint(1) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `permission` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `sys_role_module`;
CREATE TABLE `sys_role_module` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `backend_module_id` bigint(20) NOT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `locked` tinyint(1) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `created_by` (`created_by`) USING BTREE,
  KEY `created_date` (`created_date`) USING BTREE,
  KEY `last_modified_by` (`last_modified_by`) USING BTREE,
  KEY `last_modified_date` (`last_modified_date`) USING BTREE
);

DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `locked` tinyint(1) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `created_by` (`created_by`) USING BTREE,
  KEY `created_date` (`created_date`) USING BTREE,
  KEY `last_modified_by` (`last_modified_by`) USING BTREE,
  KEY `last_modified_date` (`last_modified_date`) USING BTREE
);


rename table hr_office to sys_office;



DROP TABLE IF EXISTS `oppo_push_customer`;
CREATE TABLE `oppo_push_customer` (
  `customerid` varchar(255) NOT NULL,
  `customername` varchar(255) NOT NULL,
  `agentid` varchar(255) DEFAULT NULL,
  `companyid` varchar(255) DEFAULT NULL,
  `dealertype` varchar(255) DEFAULT NULL,
  `dealergrade` varchar(255) DEFAULT NULL,
  `dealertel` varchar(255) DEFAULT NULL,
  `citytype` varchar(255) DEFAULT NULL,
  `bussinesscenter` varchar(255) DEFAULT NULL,
  `chainName` varchar(255) DEFAULT NULL,
  `saletype` varchar(255) DEFAULT NULL,
  `doorhead` varchar(255) DEFAULT NULL,
  `enabledate` varchar(255) DEFAULT NULL,
  `customertype` varchar(255) DEFAULT NULL,
  `keydealer` varchar(255) DEFAULT NULL,
  `isenable` varchar(255) DEFAULT NULL,
  `province` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `county` varchar(255) DEFAULT NULL,
  `village` varchar(255) DEFAULT NULL,
  `dealerarea` varchar(255) DEFAULT NULL,
  `framenum` varchar(255) DEFAULT NULL,
  `deskdoublenum` varchar(255) DEFAULT NULL,
  `desksinglenum` varchar(255) DEFAULT NULL,
  `cabinetnum` varchar(255) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`customerid`)
);

DROP TABLE IF EXISTS `oppo_push_customer_after_sale_imei`;
CREATE TABLE `oppo_push_customer_after_sale_imei` (
  `customerid` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `product_code` varchar(255) DEFAULT NULL,
  `imei` varchar(255) DEFAULT NULL,
  `trans_type` varchar(255) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL
);

DROP TABLE IF EXISTS `oppo_push_customer_allot`;
CREATE TABLE `oppo_push_customer_allot` (
  `from_customerid` varchar(255) NOT NULL,
  `to_customerid` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `qty` int(20) DEFAULT NULL,
  `productcode` varchar(255) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL
);

DROP TABLE IF EXISTS `oppo_push_customer_demo_phone`;
CREATE TABLE `oppo_push_customer_demo_phone` (
  `customerid` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `product_code` varchar(255) DEFAULT NULL,
  `imei` varchar(255) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL
);

DROP TABLE IF EXISTS `oppo_push_customer_imei_stock`;
CREATE TABLE `oppo_push_customer_imei_stock` (
  `customerid` varchar(255) NOT NULL,
  `date` datetime DEFAULT NULL,
  `imei` varchar(255) DEFAULT NULL,
  `productcode` varchar(255) DEFAULT NULL,
  `transType` varchar(255) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL
);

DROP TABLE IF EXISTS `oppo_push_customer_operator_type`;
CREATE TABLE `oppo_push_customer_operator_type` (
  `customerid` varchar(255) NOT NULL,
  `customername` varchar(255) NOT NULL,
  `operatortype` varchar(255) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL
);

DROP TABLE IF EXISTS `oppo_push_customer_sale`;
CREATE TABLE `oppo_push_customer_sale` (
  `customerid` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `total_sale_qty` int(255) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL
);

DROP TABLE IF EXISTS `oppo_push_customer_sale_count`;
CREATE TABLE `oppo_push_customer_sale_count` (
  `shop_code` varchar(255) DEFAULT NULL,
  `agent_code` varchar(255) DEFAULT NULL,
  `product_code` varchar(255) DEFAULT NULL,
  `sale_time` date DEFAULT NULL,
  `qty` int(255) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL
);

DROP TABLE IF EXISTS `oppo_push_customer_sale_imei`;
CREATE TABLE `oppo_push_customer_sale_imei` (
  `imei` varchar(255) DEFAULT NULL,
  `saletime` datetime DEFAULT NULL,
  `custname` varchar(255) DEFAULT NULL,
  `custmobile` varchar(255) DEFAULT NULL,
  `custsex` varchar(255) DEFAULT NULL,
  `salepromoter` varchar(255) DEFAULT NULL,
  `shopcode` varchar(255) DEFAULT NULL,
  `shopname` varchar(255) DEFAULT NULL,
  `agentcode` varchar(255) DEFAULT NULL,
  `agentname` varchar(255) DEFAULT NULL,
  `province` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL
);

DROP TABLE IF EXISTS `oppo_push_customer_stock`;
CREATE TABLE `oppo_push_customer_stock` (
  `customerid` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `qty` int(64) DEFAULT NULL,
  `productcode` varchar(255) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL
);

DROP TABLE IF EXISTS `sys_account_kingdee_book`;
CREATE TABLE `sys_account_kingdee_book` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `username` varchar(64) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` bigint(20) NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `version` int(11) NOT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL,
  `locked` tinyint(1) DEFAULT NULL,
  `company_id` bigint(20) NOT NULL,
  `kingdee_book_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_kingdee_book` (`account_id`,`kingdee_book_id`) USING BTREE
);


DROP TABLE IF EXISTS `sys_backend`;
CREATE TABLE `sys_backend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `locked` tinyint(1) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `created_by` (`created_by`) USING BTREE,
  KEY `created_date` (`created_date`) USING BTREE,
  KEY `last_modified_by` (`last_modified_by`) USING BTREE,
  KEY `last_modified_date` (`last_modified_date`) USING BTREE
);

DROP TABLE IF EXISTS `sys_backend_module`;
CREATE TABLE `sys_backend_module` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `backend_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `icon` varchar(64) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `locked` tinyint(1) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `created_by` (`created_by`) USING BTREE,
  KEY `created_date` (`created_date`) USING BTREE,
  KEY `last_modified_by` (`last_modified_by`) USING BTREE,
  KEY `last_modified_date` (`last_modified_date`) USING BTREE
);
DROP TABLE IF EXISTS `sys_localization`;

DROP TABLE IF EXISTS `sys_gl_voucher`;
CREATE TABLE `sys_gl_voucher` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fdate` date DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_name` varchar(64) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `locked` tinyint(1) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `status` varchar(64) NOT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  `out_code` varchar(64) DEFAULT NULL,
  `kingdee_book_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `created_by` (`created_by`) USING BTREE,
  KEY `created_date` (`created_date`) USING BTREE,
  KEY `last_modified_by` (`last_modified_by`) USING BTREE,
  KEY `last_modified_date` (`last_modified_date`) USING BTREE,
  KEY `process_status` (`status`) USING BTREE
);


DROP TABLE IF EXISTS `sys_gl_voucher_entry`;
CREATE TABLE `sys_gl_voucher_entry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gl_voucher_id` bigint(20) NOT NULL,
  `fexplanation` varchar(2000) DEFAULT NULL,
  `faccountid` varchar(255) DEFAULT NULL,
  `fdebit` decimal(10,2) DEFAULT NULL,
  `fcredit` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
);


DROP TABLE IF EXISTS `sys_gl_voucher_entry_flow`;
CREATE TABLE `sys_gl_voucher_entry_flow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gl_voucher_entry_id` bigint(20) NOT NULL,
  `name` varchar(64) DEFAULT NULL,
  `value` varchar(64) DEFAULT NULL,
  `code` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
);


DROP TABLE IF EXISTS `sys_kingdee_book`;
CREATE TABLE `sys_kingdee_book` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `company_id` bigint(20) DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `type` varchar(64) DEFAULT NULL,
  `kingdee_url` varchar(64) DEFAULT NULL,
  `kingdee_post_url` varchar(64) DEFAULT NULL,
  `kingdee_username` varchar(64) DEFAULT NULL,
  `kingdee_password` varchar(255) DEFAULT NULL,
  `kingdee_dbid` varchar(64) DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` bigint(20) NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `version` int(11) NOT NULL,
  `remarks` varchar(255) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `locked` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `sys_kingdee_syn`;
CREATE TABLE `sys_kingdee_syn` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `extend_id` bigint(20) DEFAULT NULL,
  `extend_type` varchar(64) DEFAULT NULL,
  `form_id` varchar(64) DEFAULT NULL,
  `next_form_id` varchar(64) DEFAULT NULL,
  `content` text,
  `success` tinyint(1) DEFAULT NULL,
  `result` text,
  `bill_no` varchar(64) DEFAULT NULL,
  `next_bill_no` varchar(64) DEFAULT NULL,
  `auto_audit` tinyint(1) DEFAULT NULL,
  `company_id` bigint(20) NOT NULL,
  `kingdee_book_id` bigint(20) DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` bigint(20) NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `locked` tinyint(1) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `sys_monitor`;

ALTER TABLE `sys_menu`
  ADD COLUMN `code`  varchar(255) NULL AFTER `mobile_icon`,
  ADD COLUMN `company_id`  bigint(20) NULL AFTER `code`;

ALTER TABLE `sys_menu`
  MODIFY COLUMN `href`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL AFTER `name`;



update sys_menu set company_id=1;

ALTER TABLE `sys_menu_category`
  ADD COLUMN `code`  varchar(255) NULL AFTER `enabled`,
  ADD COLUMN `backend_module_id`  bigint(20) NULL AFTER `code`,
  ADD COLUMN `company_id`  bigint(20) NULL AFTER `backend_module_id`;

UPDATE sys_menu_category set company_id=1;

ALTER TABLE `sys_office`
  ADD COLUMN `office_rule_id`  bigint(20) NULL AFTER `sort`,
  ADD COLUMN `area_id`  bigint(20) NULL AFTER `office_rule_id`,
  ADD COLUMN `joint_level`  varchar(64) NULL AFTER `area_id`;

ALTER TABLE `sys_office`
  ADD COLUMN `all_data_scope`  tinyint(1) NULL AFTER `joint_level`;

update sys_office set all_data_scope=0;

### memo(更新depot_store,depot_shop数据)
INSERT INTO crm_depot_store (
  SELECT
    id,
    id as depot_id,
    type,
    created_by,
    created_date,
    last_modified_by,
    last_modified_date,
    remarks,
    version,
    locked,
    enabled,
    1 as company_id,
    out_id,
    out_group_id,
    out_group_name,
    out_date,
    null as store_group,
    null as joint_level,
    null as out_code
  FROM
    crm_depot
  WHERE
    type <= 500
);

INSERT INTO crm_depot_shop (
SELECT
id,
id as depot_id,
area_type,
created_by,
created_date,
last_modified_by,
last_modified_date,
remarks,
version,
locked,
enabled,
'1' as company_id,
has_guide,
lng,
lat,
check_stock,
town_type,
chain_type,
carrier_type,
turnover_type,
business_type,
channel_type,
sale_point_type,
bussiness_center,
bussiness_center_name,
door_head,
speciality_store,
speciality_store_type,
report_name,
shop_area,
frame_num,
desk_double_num,
desk_single_num,
cabinet_num,
enable_date,
town_name,
ad_shop_bsc,
town_id
FROM
crm_depot
WHERE
type >= 500
);

ALTER TABLE `crm_depot`
  ADD COLUMN `depot_store_id`  bigint(20) NULL AFTER `task_qty`,
  ADD COLUMN `depot_shop_id`  bigint(20) NULL AFTER `depot_store_id`;

update crm_depot t1,crm_depot_store t2 set t1.depot_store_id=t2.id where t1.id=t2.depot_id;
update crm_depot t1,crm_depot_shop t2 set t1.depot_shop_id=t2.id where t1.id=t2.depot_id;

ALTER TABLE `crm_depot`
  ADD COLUMN `client_id`  bigint(20) NULL AFTER `depot_shop_id`;


INSERT INTO crm_client (
  SELECT
    id,
    name,
    mobile_phone,
    created_by,
    created_date,
    last_modified_by,
    last_modified_date,
    remarks,
    version,
    locked,
    enabled,
    '1' as company_id,
    out_id,
    out_group_id,
    out_group_name,
    out_date,
    null as out_code
  FROM
    crm_depot
  WHERE
    out_type='门店'
);
update crm_depot t1,crm_client t2 set t1.client_id=t2.id where t1.out_id=t2.out_id and t1.out_type='门店';

update crm_depot_store t1,crm_depot t2 set t1.joint_level='一级' where t1.depot_id=t2.id and t2.type=100;
update crm_depot_store t1,crm_depot t2 set t1.joint_level='二级' where t1.depot_id=t2.id and t2.type>100;

ALTER TABLE `crm_depot`
  ADD COLUMN `company_group`  varchar(64) NULL AFTER `client_id`;

ALTER TABLE `crm_depot`
  ADD COLUMN `pop_shop`  tinyint(1) NULL AFTER `company_group`;

update crm_depot set pop_shop = 0;

update crm_depot_shop set door_head=0 where door_head is null;
update crm_depot_shop set speciality_store=0 where speciality_store is null;

update crm_depot set pop_shop=ad_shop where ad_shop=1

# 更新分店client_id
UPDATE crm_depot t1,
  crm_depot t2
SET t1.client_id = t2.client_id
WHERE
  t1.parent_id = t2.id
  AND t1.depot_shop_id IS NOT NULL
  and t1.client_id is null
  AND t2.client_id IS NOT NULL;
