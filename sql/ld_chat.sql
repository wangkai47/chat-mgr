-- use mysql;
-- ----------------------------
-- 1、会话管理表
-- ----------------------------
drop table if exists chat_session;
create table chat_session
(
    session_id  varchar(32) not null comment '会话id',
    user_id     bigint(20)  default 0 comment '用户id',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    primary key (session_id)

) engine = innodb comment = '会话管理表';

-- ----------------------------
-- 2、轮次管理表
-- ----------------------------
drop table if exists chat_turn;
create table chat_turn
(
    turn_id  bigint(20) not null auto_increment comment '轮次id',
    session_id  varchar(32)  default 0 comment '会话id',
    user_id     bigint(20)  default 0 comment '用户id',
    query      varchar(512)  default '' comment '问句',
    answer     varchar(512) default '' comment '回答',
    order_no   int(4) default 0 comment '排序',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    primary key (turn_id)

) engine = innodb comment = '轮次管理表';

-- ----------------------------
-- 2、消息管理表
-- ----------------------------
drop table if exists chat_message;
create table chat_message
(
    id  bigint(20) not null auto_increment comment '轮次id',
    message_id  varchar(32)  default 0 comment '消息id',
    conversation_id  varchar(32)  default 0 comment '会话id',
    message_type bigint(2)  default 0 comment '消息类型  1-user, 2-bot',
    parent_message_id  varchar(32)  default 0 comment '上一条消息id',
    message      varchar(512)  default '' comment '问句',
    message_order   int(4) default 0 comment '排序',
    user_id     bigint(20)  default 0 comment '用户id',
    ip     varchar(20)  default 0 comment '用户ip',
    cost_time INTEGER default 0 comment '请求耗时',
    api_type varchar(32)  default 0 comment 'api类型',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    primary key (id),
    unique key uk_message_id (message_id)

) engine = innodb comment = '消息管理表';