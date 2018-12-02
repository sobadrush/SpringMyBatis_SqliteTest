----------------------------------------------------
-- SELECT * FROM z40180_deptTB;
-- SELECT * FROM z40180_empTB;
----------------------------------------------------
DROP TABLE z40180_empTB;
DROP TABLE z40180_deptTB;
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

