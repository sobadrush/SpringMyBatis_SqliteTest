----------------------------------------------------
-- SELECT * FROM z40180_deptTB;
-- SELECT * FROM z40180_empTB;
----------------------------------------------------
DROP TABLE z40180_empTB;
DROP TABLE z40180_deptTB;
----------------------------------------------------

CREATE TABLE z40180_deptTB
(
    deptno	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	dname	VARCHAR CHECK( LENGTH(dname) <= 14 ),
	loc		VARCHAR CHECK( LENGTH(loc) <= 13 )
);

CREATE TABLE z40180_empTB
(
    empno	 INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	ename	 varchar(14) CHECK( LENGTH(ename) <= 14 ) ,
	job		 varchar(13) CHECK( LENGTH(job) <= 13 ),
	hiredate  date,
	deptno int not NULL,
	FOREIGN KEY(deptno) REFERENCES z40180_deptTB(deptno)
	--Hibernate 做關聯查詢其實可以不用在實體表格設定foreign-key
);


insert into z40180_deptTB(deptno,dname,loc) values (10,'財務部','臺灣台北');
insert into z40180_deptTB(deptno,dname,loc) values (20,'研發部','臺灣新竹');
insert into z40180_deptTB(deptno,dname,loc) values (30,'業務部','美國紐約');
insert into z40180_deptTB(deptno,dname,loc) values (40,'生管部','中國上海');

insert into z40180_empTB(empno,ename,job,hiredate,deptno) values (7001,'king','president','1981-11-17',10);
insert into z40180_empTB(ename,job,hiredate,deptno) values ('blake','manager','1981-05-01',30);
insert into z40180_empTB(ename,job,hiredate,deptno) values ('clark','manager','1981-01-09',10);
insert into z40180_empTB(ename,job,hiredate,deptno) values ('jones','manager','1981-04-02',20);
insert into z40180_empTB(ename,job,hiredate,deptno) values ('martin','salesman','1981-09-28',40);
insert into z40180_empTB(ename,job,hiredate,deptno) values ('allen','salesman','1981-02-2',30);
insert into z40180_empTB(ename,job,hiredate,deptno) values ('turner','salesman','1981-09-28',30);
insert into z40180_empTB(ename,job,hiredate,deptno) values ('james','clerk','1981-12-03',30);
insert into z40180_empTB(ename,job,hiredate,deptno) values ('ward','salesman','1981-02-22',30);
insert into z40180_empTB(ename,job,hiredate,deptno) values ('ford','analyst','1981-12-03',20);
insert into z40180_empTB(ename,job,hiredate,deptno) values ('smith','clerk','1980-12-17',20);
insert into z40180_empTB(ename,job,hiredate,deptno) values ('scott','analyst','1981-12-09',40);
insert into z40180_empTB(ename,job,hiredate,deptno) values ('adams','clerk','1983-01-12',20);
insert into z40180_empTB(ename,job,hiredate,deptno) values ('miller','clerk','1982-01-23',10);


