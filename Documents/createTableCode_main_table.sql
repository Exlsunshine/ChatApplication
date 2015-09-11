create database JMMSRDB 
on  primary  -- 默认就属于primary文件组,可省略
(
	/*--数据文件的具体描述--*/
    name='JMMSR_data',  -- 主数据文件的逻辑名称
    filename='D:\Data\Graduate_1_Spring\IM\Database\JMMSR_data.mdf', -- 主数据文件的物理名称
    size=5mb, --主数据文件的初始大小
    maxsize=100mb, -- 主数据文件增长的最大值
    filegrowth=15%--主数据文件的增长率
)
log on
(
	/*--日志文件的具体描述,各参数含义同上--*/
    name='JMMSR_log',
    filename='D:\Data\Graduate_1_Spring\IM\Database\JMMSR_log.ldf',
    size=2mb,
    filegrowth=1mb
)

--用户基本信息表
create table user_basic_info
(
	id int not null identity(1,1) primary key,
	login_account varchar(80) not null,
	login_pwd varchar(128) not null,
	nick_name varchar(20) not null,
	email varchar(80) not null,
	sex varchar(7) not null check (sex in ('male', 'female', 'secrete')),
	birthday date not null,
	portrait_path varchar(300) not null,
	hometown varchar(30) not null,
	phone_number varchar(20) not null
);
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('UserA@126.com', 'pwdUserA', 'UserA_nickname', 'UserA@126.com', 'male', '1991-10-11', 'UserA_path', '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('UserB@126.com', 'pwdUserB', 'UserB_nickname', 'UserB@126.com', 'male', '1991-10-11', 'UserB_path', '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('UserC@126.com', 'pwdUserC', 'UserC_nickname', 'UserC@126.com', 'male', '1991-10-11', 'UserC_path', '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('UserD@126.com', 'pwdUserD', 'UserD_nickname', 'UserD@126.com', 'male', '1991-10-11', 'UserD_path', '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('UserE@126.com', 'pwdUserE', 'UserE_nickname', 'UserE@126.com', 'male', '1991-10-11', 'UserE_path', '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('UserF@126.com', 'pwdUserF', 'UserF_nickname', 'UserF@126.com', 'male', '1991-10-11', 'UserF_path', '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('UserG@126.com', 'pwdUserG', 'UserG_nickname', 'UserG@126.com', 'male', '1991-10-11', 'UserG_path', '1', '15801212189')



insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('User001@126.com', '001', 'User001_nickname', 'User001@126.com', 'male', '1991-10-11', 'C:/Users/USER007/Desktop/IM/data/portrait/portrait_userID_18_2015-04-08-09-42-15.jpg' '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('User002@126.com', '002', 'User002_nickname', 'User002@126.com', 'male', '1991-10-11', 'C:/Users/USER007/Desktop/IM/data/portrait/portrait_userID_18_2015-04-08-09-42-15.jpg' '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('User003@126.com', '003', 'User003_nickname', 'User003@126.com', 'male', '1991-10-11', 'C:/Users/USER007/Desktop/IM/data/portrait/portrait_userID_18_2015-04-08-09-42-15.jpg' '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('User004@126.com', '004', 'User004_nickname', 'User004@126.com', 'male', '1991-10-11', 'C:/Users/USER007/Desktop/IM/data/portrait/portrait_userID_18_2015-04-08-09-42-15.jpg' '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('User005@126.com', '005', 'User005_nickname', 'User005@126.com', 'male', '1991-10-11', 'C:/Users/USER007/Desktop/IM/data/portrait/portrait_userID_18_2015-04-08-09-42-15.jpg' '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('User006@126.com', '006', 'User006_nickname', 'User006@126.com', 'male', '1991-10-11', 'C:/Users/USER007/Desktop/IM/data/portrait/portrait_userID_18_2015-04-08-09-42-15.jpg' '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('User007@126.com', '007', 'User007_nickname', 'User007@126.com', 'male', '1991-10-11', 'C:/Users/USER007/Desktop/IM/data/portrait/portrait_userID_18_2015-04-08-09-42-15.jpg' '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('User008@126.com', '008', 'User008_nickname', 'User008@126.com', 'male', '1991-10-11', 'C:/Users/USER007/Desktop/IM/data/portrait/portrait_userID_18_2015-04-08-09-42-15.jpg' '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('User009@126.com', '009', 'User009_nickname', 'User009@126.com', 'male', '1991-10-11', 'C:/Users/USER007/Desktop/IM/data/portrait/portrait_userID_18_2015-04-08-09-42-15.jpg' '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('User010@126.com', '010', 'User010_nickname', 'User010@126.com', 'male', '1991-10-11', 'C:/Users/USER007/Desktop/IM/data/portrait/portrait_userID_18_2015-04-08-09-42-15.jpg' '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('User011@126.com', '011', 'User011_nickname', 'User011@126.com', 'male', '1991-10-11', 'C:/Users/USER007/Desktop/IM/data/portrait/portrait_userID_18_2015-04-08-09-42-15.jpg' '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('User012@126.com', '012', 'User012_nickname', 'User012@126.com', 'male', '1991-10-11', 'C:/Users/USER007/Desktop/IM/data/portrait/portrait_userID_18_2015-04-08-09-42-15.jpg' '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('User013@126.com', '013', 'User013_nickname', 'User013@126.com', 'male', '1991-10-11', 'C:/Users/USER007/Desktop/IM/data/portrait/portrait_userID_18_2015-04-08-09-42-15.jpg' '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('User014@126.com', '014', 'User014_nickname', 'User014@126.com', 'male', '1991-10-11', 'C:/Users/USER007/Desktop/IM/data/portrait/portrait_userID_18_2015-04-08-09-42-15.jpg' '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('User015@126.com', '015', 'User015_nickname', 'User015@126.com', 'male', '1991-10-11', 'C:/Users/USER007/Desktop/IM/data/portrait/portrait_userID_18_2015-04-08-09-42-15.jpg' '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('User016@126.com', '016', 'User016_nickname', 'User016@126.com', 'male', '1991-10-11', 'C:/Users/USER007/Desktop/IM/data/portrait/portrait_userID_18_2015-04-08-09-42-15.jpg' '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('User017@126.com', '017', 'User017_nickname', 'User017@126.com', 'male', '1991-10-11', 'C:/Users/USER007/Desktop/IM/data/portrait/portrait_userID_18_2015-04-08-09-42-15.jpg' '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('User018@126.com', '018', 'User018_nickname', 'User018@126.com', 'male', '1991-10-11', 'C:/Users/USER007/Desktop/IM/data/portrait/portrait_userID_18_2015-04-08-09-42-15.jpg' '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('User019@126.com', '019', 'User019_nickname', 'User019@126.com', 'male', '1991-10-11', 'C:/Users/USER007/Desktop/IM/data/portrait/portrait_userID_18_2015-04-08-09-42-15.jpg' '1', '15801212189')
insert into user_basic_info (login_account, login_pwd, nick_name, email, sex, birthday, portrait_path, hometown, phone_number)
values ('User020@126.com', '020', 'User020_nickname', 'User020@126.com', 'male', '1991-10-11', 'C:/Users/USER007/Desktop/IM/data/portrait/portrait_userID_18_2015-04-08-09-42-15.jpg' '1', '15801212189')






--用户关系表
create table user_relationship
(
	id int not null identity(1,1) primary key,
	first_userid int not null foreign key references user_basic_info(id),
	second_userid int not null foreign key references user_basic_info(id),
	group_name varchar(20) not null default 'friend',
	alias varchar(20) default null,
	close_friend_flag bit not null default 0 check (close_friend_flag in (1, 0))
);
insert into user_relationship (first_userid, second_userid) values (4, 5)
insert into user_relationship (first_userid, second_userid) values (5, 4)
insert into user_relationship (first_userid, second_userid) values (4, 6)
insert into user_relationship (first_userid, second_userid) values (6, 4)
insert into user_relationship (first_userid, second_userid) values (4, 7)
insert into user_relationship (first_userid, second_userid) values (7, 4)
insert into user_relationship (first_userid, second_userid) values (4, 8)
insert into user_relationship (first_userid, second_userid) values (8, 4)
insert into user_relationship (first_userid, second_userid, group_name, alias, close_friend_flag)
values (4, 9, 'AA_group_BB', 'alias_BB', 1)
insert into user_relationship (first_userid, second_userid, group_name, alias, close_friend_flag)
values (9, 4, 'BB_group_AA', 'alias_AA', 0)





--文件传输信息表
create table file_transportation
(
	id int not null identity(1,1) primary key,
	from_userid int not null foreign key references user_basic_info(id),
	to_userid int not null foreign key references user_basic_info(id),
	file_path varchar(300) not null,
	upload_date date not null default getdate(),
	download_flag bit not null default 0
);

--图片传输信息表
create table picture_transportation
(
	id int not null identity(1,1) primary key,
	from_userid int not null foreign key references user_basic_info(id),
	to_userid int not null foreign key references user_basic_info(id),
	pic_path varchar(300) not null,
	upload_date date not null default getdate(),
	download_flag bit not null default 0
);