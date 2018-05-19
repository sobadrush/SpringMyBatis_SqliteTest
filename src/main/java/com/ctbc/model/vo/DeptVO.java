package com.ctbc.model.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

/**
 * sqlSessionFactory.setTypeAliasesPackage("com.ctbc.model.vo");
 * 搭配 @Alias(value = "fuckDeptVO")
 * → <resultMap id="resultMapDept" type="fuckDeptVO">
 */
@Alias(value = "fuckDeptVO")
public class DeptVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer deptNo;
	private String deptName;
	private String deptLoc;

	public DeptVO() {
	}

	public DeptVO(String deptName, String deptLoc) {
		this.deptName = deptName;
		this.deptLoc = deptLoc;
	}

	public DeptVO(Integer deptNo, String deptName, String deptLoc) {
		this.deptNo = deptNo;
		this.deptName = deptName;
		this.deptLoc = deptLoc;
	}

	public Integer getDeptNo() {
		return deptNo;
	}

	public void setDeptNo(Integer deptNo) {
		this.deptNo = deptNo;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptLoc() {
		return deptLoc;
	}

	public void setDeptLoc(String deptLoc) {
		this.deptLoc = deptLoc;
	}

	@Override
	public String toString() {
		boolean outputTransients = false;
		boolean outputStatics = false;
		return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE , outputTransients, outputStatics);
	}

}
