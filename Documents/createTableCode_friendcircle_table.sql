create table post_data
(
	id int not null identity(1,1) primary key,
	post_user_id int not null,
	liked_number int not null,
	post_date date not null,
	content varchar(400) not null,
	post_type int not null,
	location varchar(50)
);

insert into post_data(post_user_id, liked_number, post_date, content, post_type, location)
values(4, 0, '2015-03-31', '这是一条测试post的消息1st。', '1', 'Beijing')
insert into post_data(post_user_id, liked_number, post_date, content, post_type, location)
values(5, 0, '2015-05-21', '这是一条测试post的消息2nd。', '1', 'Beijing')
insert into post_data(post_user_id, liked_number, post_date, content, post_type, location)
values(4, 0, '2015-01-30', '这是一条测试post的消息3rd。', '1', 'Beijing')
insert into post_data(post_user_id, liked_number, post_date, content, post_type, location)
values(4, 0, '2015-12-16', '这是一条测试post的消息4th。', '1', 'Beijing')


create table comment_data
(
	id int not null identity(1,1) primary key,
	post_user_id int not null,
	post_id int not null,
	comment_user_id int not null,
	comment varchar(200) not null,
	comment_date date not null
);
insert into comment_data(post_user_id, post_id, comment_user_id, comment, comment_date)
values(4, 1, 5, '我是5评论了4的id为1的post。', '2014-12-09')
insert into comment_data(post_user_id, post_id, comment_user_id, comment, comment_date)
values(4, 1, 6, '我是6评论了4的id为1的post。', '2014-02-09')
insert into comment_data(post_user_id, post_id, comment_user_id, comment, comment_date)
values(4, 1, 7, '我是7评论了4的id为1的post。', '2014-11-09')
insert into comment_data(post_user_id, post_id, comment_user_id, comment, comment_date)
values(5, 2, 4, '我是4评论了5的id为2的post。', '2014-03-09')