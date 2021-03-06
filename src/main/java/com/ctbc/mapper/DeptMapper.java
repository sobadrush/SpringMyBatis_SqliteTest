package com.ctbc.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.ctbc.model.vo.DeptVO;

import net.coderbee.mybatis.batch.BatchParameter;

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
	
	public abstract List<Map<String, String>> getAllForMap();
	
	public abstract List<DeptVO> getDeptByIdList(@Param("deptIdList") List<Integer> idList);
	
	public abstract DeptVO getDeptById(@Param("deptIdGGG") int deptId);
	
	public abstract Map<String, String> getDeptByIdForMap(@Param("deptIdGGG") int deptId);
	
	public abstract DeptVO getDeptByIdMap(@Param("deptMap") Map<String, String> deptMap);
	
	public abstract int addDept(@Param("deptVOGGG") DeptVO deptVO);
	
	public abstract int addDeptReturnLatestKey(@Param("deptVOGGG") DeptVO deptVO);
	
	public abstract int updateDeptById(@Param("deptVO") DeptVO deptVO);
	
	public abstract int delDeptById(int deptId);
	
	public abstract int delDeptByIdArray(@Param("deptIdsArr") int[] deptIds);
	
	public abstract int addDeptsBatch(@Param("deptList") List<DeptVO> dList);
	
	public abstract int addDeptsBatchV2(@Param("list") List<DeptVO> dList);
	
	public abstract int addDeptsBatchV3(BatchParameter<DeptVO> dList);
	
	public abstract int addDeptsBatchV3_ByMap(BatchParameter<Map<String, Object>> deptMapList);
	
	@Insert("INSERT INTO z40180_deptTB ( [dname] , [loc] ) " + 
			 "VALUES ( #{ deptName } , #{ deptLoc } );")
	public abstract int addDeptsBatchV3_ByMapUseAnnotation(BatchParameter<?> deptMapList);
	
	public abstract int updateDeptsBatch(@Param("deptList") List<DeptVO> dList);
	
	/**
	 * 使用CASE... WHEN + <trim>
	 */
	public abstract int updateDeptsBatchForSqlite(@Param("deptList") List<DeptVO> dList);
	
	/**
	 * 使用CASE... WHEN + <foreach>
	 */
	public abstract int updateDeptsBatchForSqlite2(@Param("deptList") List<DeptVO> dList);
	
	//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	@Select("SELECT * FROM z40180_deptTB")
	@ResultMap(value = {"resultMapDept"})
	public abstract List<DeptVO> getAllUseAnnotation();
	
	@Select("SELECT * FROM z40180_deptTB")
	public abstract List<Map<String, String>> getAllForMapUseAnnotation();
	
	@Select("SELECT * FROM z40180_deptTB WHERE deptno = #{ _deptId }")
	public abstract Map<String, String> getDeptByIdForMapUseAnnotation(@Param("_deptId") int deptId);
	
}






