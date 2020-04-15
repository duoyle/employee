create database if not exists employee character set utf8;

use employee;

-- 用户
create table User (
    USERID int auto_increment primary key,
    NAME varchar(40),
    PASSWORD varchar(40),
    EMAIL varchar(40),
    FULLNAME varchar(40),
    TYPE int default 1, -- 1表示普通用户，2表示管理员
);
-- 角色
create table Role (
    ROLEID int auto_increment primary key,
    NAME varchar(40) not null,
    REMARK varchar(100)
);
-- 每个用户的角色分配
create table Permission (
    PERMID int auto_increment primary key,
    USERID int not null, -- 用户
    ROLEID int not null, -- 分配给用户的权限角色
    foreign key (USERID) references User(USERID),
    foreign key (ROLEID) references Role(ROLEID)
)
-- 所有功能
create table Function (
    FUNCID int auto_increment primary key,
    NAME varchar(20) not null, -- 功能名称
    URL varchar(200), -- 功能跳转链接
    ICON varchar(60), -- 功能图标
    DIGIT bigint -- 二进制位，一个2的N次方的数字
)
-- 每个角色的功能权限
create table Privilege (
    PRIVID int auto_increment primary key,
    ROLENO int not null, -- 角色
    FUNCID int not null, -- 对应角色的功能权限
)

-- 部门
create table Department (
    DEPTNO varchar(20) primary key,
    DEPTNAME varchar(20) not null unique
);

-- 员工
create table Employee (
    EMPNO varchar(20) primary key,
    DEPTNO varchar(20) not null,
    EMPNAME varchar(40) not null,
    EMPSEX int not null comment '性别',
    ENTRYDATE date not null,
    EMPPHONE varchar(40) not null,
    EMPADDR varchar(100),
    SALARY int not null,
    LEAVEDATE date default '9999-12-31',
    HOBBY int not null default 0, -- 按位运算的值
    REMARK varchar(1024),
    STATE int not null default 1 comment '在职/离职/减员',
    foreign key(DEPTNO) references Department(DEPTNO) -- 外键约束
);

-- 变更历史记录
create table History (
    CHANGENO int auto_increment primary key,
    EMPNO varchar(20) not null,
    SALARY int not null,
    CHANGEDATE date, -- 变更日期（包含入职日期）
    CHANGEREASON  varchar(100), -- 变更原因
    foreign key(EMPNO) references Employee(EMPNO)
);
-- 字典表
drop table if exists Dictionary;
create table Dictionary (
    NUMBER int primary key,
    CATEGORY int not null,
    NAME varchar(200) not null,
    VALUE bigint not null,
    index(CATEGORY, VALUE)
);

insert into Department values('A01','技术部');
insert into Department values('B01','宣传部');
insert into Department values('B02','人力资源部');

-- 前两位表示类别，后两位表示值
insert into Dictionary(NUMBER, CATEGORY, NAME, VALUE) values(1001, 10, '在职', 1);
insert into Dictionary(NUMBER, CATEGORY, NAME, VALUE) values(1002, 10, '离职', 2);
insert into Dictionary(NUMBER, CATEGORY, NAME, VALUE) values(1003, 10, '减员', 4);
insert into Dictionary(NUMBER, CATEGORY, NAME, VALUE) values(1101, 11, '男', 1);
insert into Dictionary(NUMBER, CATEGORY, NAME, VALUE) values(1102, 11, '女', 2);
insert into Dictionary(NUMBER, CATEGORY, NAME, VALUE) values(1201, 12, '运动', 1);
insert into Dictionary(NUMBER, CATEGORY, NAME, VALUE) values(1202, 12, '音乐', 2);
insert into Dictionary(NUMBER, CATEGORY, NAME, VALUE) values(1203, 12, '读书', 4);
insert into Dictionary(NUMBER, CATEGORY, NAME, VALUE) values(1204, 12, '写作', 8);
insert into Dictionary(NUMBER, CATEGORY, NAME, VALUE) values(1205, 12, '画画', 16);
insert into Dictionary(NUMBER, CATEGORY, NAME, VALUE) values(1206, 12, '养殖', 32);
insert into Dictionary(NUMBER, CATEGORY, NAME, VALUE) values(1207, 12, '旅行', 64);
insert into Dictionary(NUMBER, CATEGORY, NAME, VALUE) values(1208, 12, '喝茶', 128);
insert into Dictionary(NUMBER, CATEGORY, NAME, VALUE) values(1209, 12, '游戏', 256);
insert into Dictionary(NUMBER, CATEGORY, NAME, VALUE) values(1210, 12, '影视', 512);
insert into Dictionary(NUMBER, CATEGORY, NAME, VALUE) values(1211, 12, '学习', 1024);

-- 以下函数不属于标准SQL，暂时未使用
-- 取得兴趣爱好
drop function if exists getHobby;
create function getHobby(VALSUM bigint)
  returns varchar(200)
  begin
    declare result varchar(200);
    -- group_concat默认逗号分割，如果用|分割可以group_concat(NAME separator '|')
    select group_concat(NAME) into result from Dictionary where CATEGORY = 12 and VALUE & VALSUM = VALUE;
    return result; -- mysql 不允许返回结果集
  end;
-- 取得性别
drop function if exists getSex;
create function getSex(VAL bigint)
  returns varchar(200)
  begin
    declare result varchar(200);
    select NAME into result from Dictionary where CATEGORY = 11 and VALUE = VAL;
    return result;
  end;
-- 取得状态
drop function if exists getState;
create function getState(VAL bigint)
  returns varchar(200)
  begin
    declare result varchar(200);
    select NAME into result from Dictionary where CATEGORY = 10 and VALUE = VAL;
    return result;
  end;

-- -- 弃用：添加约束，empsex是列名。5.6.30中不起作用
-- alter table Employee add constraint chk_employee_sex check(empsex in(0,1));

-- -- 完善SQL使用，已完善到上述SQL
-- alter table Dept add unique key(DEPTNAME); -- 添加唯一约束和对应唯一索引
-- alter table Employee add foreign key(DEPTNO) references Dept(DEPTNO); -- 添加外键约束
-- alter table Employee alter column LEAVEDATE set default '9999-12-31';
-- alter table History drop column DIMISSIONDATE;
-- alter table History drop column DIMISSIONREASON;
-- -- 下面语句修改外键
-- alter table History drop foreign key history_ibfk_2; -- 删除外键，只删除外键约束（不删除索引）
-- alter table History drop column DEPTNO; -- 删除列自动删除该列的索引
--  -- 添加外键约束，同时添加索引，如果该列存在索引则索引key_name命名为这里的约束名，也就是下面语句约束名和索引名同名
-- alter table History add constraint history_ibfk_1 foreign key (EMPNO) references Employee (EMPNO);
-- alter table Dept rename to Department;
-- alter table Department change DEPTNO DEPTID int default 1; -- 修改字段名和类型
-- alter table Department modify column DEPTNO varchar(20); -- 修改字段类型
-- alter table Employee add column INTEREST int not null default 0;
-- alter table Employee add column REMARK varchar(1024);
-- create index dict_category_value on Dictionary (CATEGORY, VALUE);

select * from Dictionary;

select E.EMPNO,E.EMPNAME,E.EMPSEX,E.EMPADDR,E.EMPPHONE,E.SALARY,E.ENTRYDATE,E.LEAVEDATE,E.STATE,E.DEPTNO,D.DEPTNAME from Employee E left join Dept D on E.DEPTNO = D.DEPTNO
select Employee.EMPNO,Employee.EMPNAME,Employee.EMPSEX,Employee.EMPADDR,Employee.EMPPHONE,Employee.SALARY,Employee.ENTRYDATE,Employee.LEAVEDATE,Employee.STATE,Employee.DEPTNO,Department.DEPTNAME from Employee left join Dept on Employee.DEPTNO = Department.DEPTNO