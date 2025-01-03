create database elearning;

use elearning;

create table Users(
 USER_ID INT AUTO_INCREMENT PRIMARY KEY,
 USERNAME VARCHAR(50) NOT NULL,
 PASSWORD varchar(50) not null,
 ROLE enum('ADMIN' , 'STUDENT') not null
);

create table COURSES(
COURSE_ID int auto_increment primary key,
COURSE_NAME varchar(100) not null,
DESCRIPTION varchar(500) not null,
INSTRUCTOR varchar(25) not null
);

create table LESSONS(
LESSON_ID int auto_increment primary key,
COURSE_ID int ,
LESSON_TITLE varchar(60),
CONTENT TEXT,
foreign key (COURSE_ID) references COURSES(COURSE_ID)
);

create table PROGRESS(
PROGRESS_ID int auto_increment primary key,
USER_ID int ,
COURSE_ID int,
LESSON_ID int,
STATUS enum('COMPLETED') default ('COMPLETED'),
foreign key (USER_ID) references Users(USER_ID),
foreign key (COURSE_ID) references COURSES(COURSE_ID),
foreign key (LESSON_ID) references LESSONS(LESSON_ID)
);

alter table Users
rename USERS;
