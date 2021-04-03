create database sf_mybatis;

use sf_mybatis;

create table student (id int unsigned auto_increment, student_name varchar(30), student_age int, primary key(id));

insert into student (id, student_name, student_age) values(5, 'mountain', 12);

