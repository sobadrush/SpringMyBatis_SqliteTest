
----------------------------------------------------
-- SELECT * FROM z40180_deptTB;
-- SELECT * FROM z40180_empTB;
----------------------------------------------------
-- DROP TABLE z40180_empTB;
-- DROP TABLE z40180_deptTB;
----------------------------------------------------

CREATE TABLE z40180_deptTB
(
    deptno	int IDENTITY(10,10) PRIMARY KEY,
	dname	varchar(14),
	loc		varchar(13) , 
	[version]   int 
);

CREATE TABLE z40180_empTB
(
    empno	 int IDENTITY(7001,1) PRIMARY KEY,
	ename	 varchar(14),
	job		 varchar(13),
	hiredate  date,		
	deptno int not NULL,
	[version]   int,
	FOREIGN KEY(deptno) REFERENCES z40180_deptTB(deptno)
	--Hibernate 做關聯查詢其實可以不用在實體表格設定foreign-key
);


insert into z40180_deptTB values ('財務部','臺灣台北' , 0);
insert into z40180_deptTB values ('研發部','臺灣新竹' , 0);
insert into z40180_deptTB values ('業務部','美國紐約' , 0);
insert into z40180_deptTB values ('生管部','中國上海' , 0);

insert into z40180_empTB values ('king','president','1981-11-17',10 , 0);
insert into z40180_empTB values ('blake','manager','1981-05-01',30 , 0);
insert into z40180_empTB values ('clark','manager','1981-01-09',10 , 0);
insert into z40180_empTB values ('jones','manager','1981-04-02',20 , 0);
insert into z40180_empTB values ('martin','salesman','1981-09-28',40 , 0);
insert into z40180_empTB values ('allen','salesman','1981-02-2',30 , 0);
insert into z40180_empTB values ('turner','salesman','1981-09-28',30 , 0);
insert into z40180_empTB values ('james','clerk','1981-12-03',30 , 0);
insert into z40180_empTB values ('ward','salesman','1981-02-22',30 , 0);
insert into z40180_empTB values ('ford','analyst','1981-12-03',20 , 0);
insert into z40180_empTB values ('smith','clerk','1980-12-17',20 , 0);
insert into z40180_empTB values ('scott','analyst','1981-12-09',40 , 0);
insert into z40180_empTB values ('adams','clerk','1983-01-12',20 , 0);
insert into z40180_empTB values ('miller','clerk','1982-01-23',10 , 0);

