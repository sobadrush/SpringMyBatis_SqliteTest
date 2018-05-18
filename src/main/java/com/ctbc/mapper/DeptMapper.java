package com.ctbc.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ctbc.model.vo.DeptVO;

/**
 * (1)
 * org.mybatis.'spring'.annotation.@MapperScan 掃描
 * 
 * @Mapper 所在的包
 *         (2)
 *         映射xml中，<mapper namespace="com.ctbc.mapper.DeptMapper">
 *         namespace 要指到 Mapper介面
 *         (3)
 *         Mapper介面的抽象方法名稱 ooo，要與映射xml中 <select id="ooo" ...> 一致
 *         (4)
 * @Param(value = "ggg") vs XML中的 #{ggg}
 */
@Mapper
public interface DeptMapper {

	public abstract List<DeptVO> getAll();
	
	public abstract List<DeptVO> getDeptByIdList(@Param("deptIdList") List<Integer> idList);
	
	public abstract DeptVO getDeptById(@Param("deptIdGGG") int deptId);
	
	public abstract DeptVO getDeptByIdMap(@Param("deptMap") Map<String, String> deptMap);
	
	public abstract int addDept(@Param("deptVOGGG") DeptVO deptVO);
}
