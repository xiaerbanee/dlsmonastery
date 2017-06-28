### 修改工厂相关名称
rename table api_imoo_plant_basic_product to imoo_plant_basic_product;
rename table api_imoo_prdocut_imei_deliver to imoo_prdocut_imei_deliver;
rename table api_imoo_product_map to imoo_product_map;
rename table api_oppo_plant_product_itemelectron_sel to oppo_plant_product_itemelectron_sel;
rename table api_oppo_plant_send_imei_ppsel to oppo_plant_send_imei_ppsel;
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


DROP TABLE IF EXISTS `crm_after_sale`;
CREATE TABLE `crm_after_sale` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(64) DEFAULT NULL,
  `business_id` bigint(20) DEFAULT NULL,
  `bad_product_id` bigint(20) DEFAULT NULL,
  `bad_product_ime_id` bigint(20) DEFAULT NULL,
  `bad_type` varchar(64) DEFAULT NULL,
  `package_status` varchar(64) DEFAULT NULL,
  `memory` varchar(64) DEFAULT NULL,
  `bad_depot_id` bigint(20) DEFAULT NULL,
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
  UNIQUE KEY `bad_product_ime_id` (`bad_product_ime_id`,`company_id`) USING BTREE,
  KEY `business_id` (`business_id`) USING BTREE,
  KEY `bad_depot_id` (`bad_depot_id`) USING BTREE,
  KEY `created_date` (`created_date`) USING BTREE,
  KEY `created_by` (`created_by`) USING BTREE,
  KEY `last_modified_by` (`last_modified_by`) USING BTREE,
  KEY `last_modified_date` (`last_modified_date`) USING BTREE
);

DROP TABLE IF EXISTS `crm_after_sale_detail`;
CREATE TABLE `crm_after_sale_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(64) DEFAULT NULL,
  `from_depot_id` bigint(20) DEFAULT NULL,
  `to_depot_id` bigint(20) DEFAULT NULL,
  `input_date` date DEFAULT NULL,
  `replace_date` date DEFAULT NULL,
  `replace_product_ime_id` bigint(20) DEFAULT NULL,
  `replace_product_id` bigint(20) DEFAULT NULL,
  `replace_amount` decimal(10,0) DEFAULT NULL,
  `input_out_code` varchar(255) DEFAULT NULL,
  `replace_out_code` varchar(255) DEFAULT NULL,
  `after_sale_id` bigint(20) DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` bigint(20) NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `locked` tinyint(1) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `company_id` bigint(20) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `crm_after_sale_flee`;
CREATE TABLE `crm_after_sale_flee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ime` varchar(255) DEFAULT NULL,
  `flee_shop_name` varchar(255) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `mobile_phone` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `buy_amount` decimal(10,0) DEFAULT NULL,
  `after_sale_id` bigint(20) DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` bigint(20) NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `locked` tinyint(1) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `company_id` bigint(20) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
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
CHANGE COLUMN `create_permission_id` `create_position_ids`  varchar(255) NULL DEFAULT NULL AFTER `view_position_ids`;

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

### memo(更新depot_store,depot_shop数据)
