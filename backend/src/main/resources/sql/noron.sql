-- 任务信息表
create sequence task_tid_seq start with 10001;
create sequence user_uid_seq start with 20001;
create table task(
     task_id bigint not null default task_tid_seq.nextval primary key ,
     user_id bigint not null default user_uid_seq.nextval,
     task_content varchar(512),
     task_img varchar(100),
     start_time bigint,
     end_time bigint,
     task_type int not null default 0
);
alter table TASK alter column TASK_ID BIGINT default NEXT VALUE FOR "PUBLIC"."TASK_TID_SEQ" auto_increment;
alter table TASK alter column USER_ID BIGINT default NEXT VALUE FOR "PUBLIC"."USER_UID_SEQ" auto_increment;
alter table TASK add task_progress int default 0 not null;