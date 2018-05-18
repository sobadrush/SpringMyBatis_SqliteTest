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

