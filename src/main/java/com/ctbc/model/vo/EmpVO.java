package com.ctbc.model.vo;

import java.io.Serializable;
import java.sql.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

/**
 * sqlSessionFactory.setTypeAliasesPackage("com.ctbc.model.vo");
 * 搭配 @Alias(value = "fuckEmpVO")
 * → <resultMap id="resultMapDept" type="fuckEmpVO">
 */
@Alias(value = "fuckEmpVO")
public class EmpVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
    private Integer empno;
    private String ename;
    private String job;
    private Date hiredate;
    private Integer deptno;

    public EmpVO() {
		super();
	}

	public EmpVO(String ename, String job, Date hiredate, Integer deptno) {
		this.ename = ename;
		this.job = job;
		this.hiredate = hiredate;
		this.deptno = deptno;
	}

	public EmpVO(Integer empno, String ename, String job, Date hiredate, Integer deptno) {
		this.empno = empno;
		this.ename = ename;
		this.job = job;
		this.hiredate = hiredate;
		this.deptno = deptno;
	}

	public Integer getEmpno() {
        return empno;
    }

    public void setEmpno(Integer empno) {
        this.empno = empno;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    public Integer getDeptno() {
        return deptno;
    }

    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        EmpVO other = (EmpVO) that;
        return (this.getEmpno() == null ? other.getEmpno() == null : this.getEmpno().equals(other.getEmpno()))
            && (this.getEname() == null ? other.getEname() == null : this.getEname().equals(other.getEname()))
            && (this.getJob() == null ? other.getJob() == null : this.getJob().equals(other.getJob()))
            && (this.getHiredate() == null ? other.getHiredate() == null : this.getHiredate().equals(other.getHiredate()))
            && (this.getDeptno() == null ? other.getDeptno() == null : this.getDeptno().equals(other.getDeptno()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getEmpno() == null) ? 0 : getEmpno().hashCode());
        result = prime * result + ((getEname() == null) ? 0 : getEname().hashCode());
        result = prime * result + ((getJob() == null) ? 0 : getJob().hashCode());
        result = prime * result + ((getHiredate() == null) ? 0 : getHiredate().hashCode());
        result = prime * result + ((getDeptno() == null) ? 0 : getDeptno().hashCode());
        return result;
    }

	@Override
	public String toString() {
		boolean outputTransients = false;
		boolean outputStatics = false;
		return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE , outputTransients, outputStatics);
	}
}