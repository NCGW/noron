-- 任务信息表
create sequence task_tid_seq start with 10001;
create sequence user_uid_seq start with 20001;
create table task(
     task_id bigint not null default task_tid_seq.nextval primary key ,
     user_id bigint not null,
     task_content varchar(512),
     task_img varchar(100),
     start_time bigint,
     end_time bigint,
     task_type int not null default 0
);
alter table TASK alter column TASK_ID BIGINT default NEXT VALUE FOR "PUBLIC"."TASK_TID_SEQ" auto_increment;
alter table TASK add task_progress int default 0 not null;
alter table TASK add priority int default 0 not null;
alter table TASK
  add constraint TASK_USER_USER_ID_fk
    foreign key (USER_ID) references USER(user_id) ;

create table user(
       user_id bigint not null default user_uid_seq.nextval auto_increment primary key ,
       user_name varchar(64),
       coins int
);

