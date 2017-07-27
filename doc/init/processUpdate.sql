
CREATE TABLE `crm_simple_process` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL,
  `current_process_status` varchar(50) NOT NULL,
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

  INSERT INTO crm_simple_process (
  SELECT
    process_instance_id,
    name,
    status,
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

INSERT INTO crm_simple_process_detail (
  SELECT
    t1.ID_,
    t1.PROC_INST_ID_,
    t1.name_,
    "",
    '',
    case WHEN t1.ASSIGNEE_ IS NULL THEN 1 ELSE t1.ASSIGNEE_ END,
    case WHEN t1.END_TIME_ IS NULL THEN now() ELSE t1.END_TIME_ END,
    case WHEN t1.ASSIGNEE_ IS NULL THEN 1 ELSE t1.ASSIGNEE_ END,
    case WHEN t1.END_TIME_ IS NULL THEN now() ELSE t1.END_TIME_ END,
    0,
    0,
    1
  FROM
    act_hi_taskinst t1
      LEFT JOIN crm_simple_process t3 ON t1.PROC_INST_ID_ = t3.id
  WHERE
    t3.id is not null
);

INSERT INTO crm_tmp_20170727 (
  select t1.id id, t2.message_ remarks from crm_simple_process_detail t1 , act_hi_comment t2 where t1.ID = t2.TASK_ID_ and t1.simple_process_id = t2.PROC_INST_ID_
);

update crm_simple_process_detail t1 set remarks = (select t2.remarks from crm_tmp_20170727 t2 where t1.ID = t2.id );

ALTER TABLE crm_ad_goods_order
  ADD COLUMN simple_process_id bigint(20) NULL AFTER process_instance_id;

ALTER TABLE crm_bank_in
  ADD COLUMN simple_process_id bigint(20) NULL AFTER process_instance_id;

update crm_ad_goods_order set simple_process_id = process_instance_id;
update crm_bank_in set simple_process_id = process_instance_id;


DROP TABLE IF EXISTS `crm_tmp_20170727`;