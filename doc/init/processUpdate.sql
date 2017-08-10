CREATE TABLE `crm_simple_process_step` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `simple_process_type_id` varchar(20) NOT NULL,
  `step` varchar(50) NOT NULL,
  `sort` int(11) NOT NULL,
  `position_id` bigint(20) NOT NULL,
  `remarks` varchar(255) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` bigint(20) NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `locked` tinyint(1) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `crm_simple_process_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `remarks` varchar(255) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` bigint(20) NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `locked` tinyint(1) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `crm_simple_process` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `simple_process_type_id` bigint(20) NOT NULL,
  `current_process_status` varchar(50) NOT NULL,
  `current_position_id` bigint(20),
  `remarks` varchar(255) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` bigint(20) NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `locked` tinyint(1) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `crm_simple_process_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `simple_process_id` bigint(20) NOT NULL,
  `process_status` varchar(50) NOT NULL,
  `opinion` varchar(50) NOT NULL,
  `remarks` varchar(255),
  `created_by` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` bigint(20) NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `locked` tinyint(1) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `crm_tmp_20170727` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `remarks` varchar(255),
  PRIMARY KEY (`id`)
);


ALTER TABLE crm_ad_goods_order
  ADD COLUMN simple_process_id bigint(20) NULL AFTER process_instance_id;

ALTER TABLE crm_bank_in
  ADD COLUMN simple_process_id bigint(20) NULL AFTER process_instance_id;


INSERT INTO crm_simple_process_type (
  SELECT
    t1.id,
    t1.name,
    t1.remarks,
    1,
    now(),
    1,
    now(),
    0,
    0,
    1
  FROM
    sys_process_type t1
  WHERE
    t1.name in ('AdGoodsOrder','BankIn') and t1.enabled = 1
);

INSERT INTO crm_simple_process_step (
  SELECT
    t1.id,
    t2.id,
    t1.name,
    t1.sort,
    t1.position_id,
    '',
    1,
    now(),
    1,
    now(),
    0,
    0,
    1
  FROM
    sys_process_flow t1
    LEFT JOIN sys_process_type t2 ON t1.process_type_id = t2.id
  WHERE
    t2.name in ('AdGoodsOrder','BankIn') and t2.enabled = 1 order by t1.sort
);

  INSERT INTO crm_simple_process (
  SELECT
    process_instance_id,
    case  name when '销售收款' then (select id from sys_process_type where name = 'BankIn' ) when '柜台订货' then  (select id from sys_process_type where name = 'AdGoodsOrder' ) else null end,
    status,
    position_id,
    remarks,
    created_by,
    created_date,
    last_modified_by,
    last_modified_date,
    version,
    locked,
    1
  FROM
    sys_process_task
  WHERE
    process_instance_id in  (select process_instance_id from crm_bank_in where process_instance_id is not null) or process_instance_id in  (select process_instance_id from crm_ad_goods_order where process_instance_id is not null)
);

INSERT INTO crm_simple_process (
  SELECT
    simple_process_id,
    (select id from sys_process_type where name = 'AdGoodsOrder' ),
    process_status,
    process_position_id,
    '',
    created_by,
    created_date,
    created_by,
    created_date,
    0,
    0,
    1
  FROM
    crm_ad_goods_order
  WHERE
    simple_process_id is not null and simple_process_id not in  (select id from crm_simple_process)
);

INSERT INTO crm_simple_process (
  SELECT
    simple_process_id,
    (select id from sys_process_type where name = 'BankIn' ),
    process_status,
    position_id,
    '',
    created_by,
    created_date,
    created_by,
    created_date,
    0,
    0,
    1
  FROM
    crm_bank_in
  WHERE
    simple_process_id is not null and simple_process_id not in  (select id from crm_simple_process)
);


INSERT INTO crm_simple_process_detail (
  SELECT
    t1.ID_,
    t1.PROC_INST_ID_,
    t1.name_,
    '',
    '',
    t1.ASSIGNEE_ ,
    t1.END_TIME_ ,
    t1.ASSIGNEE_,
    t1.END_TIME_,
    0,
    0,
    1
  FROM
    act_hi_taskinst t1
      LEFT JOIN crm_simple_process t3 ON t1.PROC_INST_ID_ = t3.id
  WHERE
    t3.id is not null and t1.ASSIGNEE_ is not null
);

INSERT INTO crm_tmp_20170727 (
  select t1.id id, t2.message_ remarks from crm_simple_process_detail t1 , act_hi_comment t2 where t1.ID = t2.TASK_ID_ and t1.simple_process_id = t2.PROC_INST_ID_
);

update crm_simple_process_detail t1 set remarks = (select t2.remarks from crm_tmp_20170727 t2 where t1.ID = t2.id );

update crm_ad_goods_order set simple_process_id = process_instance_id;
update crm_bank_in set simple_process_id = process_instance_id;
update crm_simple_process set current_position_id = NULL where current_process_status in ('已通过','未通过');

DROP TABLE IF EXISTS `crm_tmp_20170727`;
