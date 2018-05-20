package com.ctbc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ctbc.model.vo.EmpVO;
import com.ctbc.model.vo.EmpVOExample;

public interface EmpMapper {
   public long countByExample(EmpVOExample example);
   
   public int deleteByExample(EmpVOExample example);

   public int deleteByPrimaryKey(Integer empno);

   public int insert(EmpVO record);

   public int insertSelective(EmpVO record);

   public List<EmpVO> selectByExample(EmpVOExample example);
 
   public EmpVO selectByPrimaryKey(Integer empno);

   public int updateByExampleSelective(@Param("record") EmpVO record, @Param("example") EmpVOExample example);

   public int updateByExample(@Param("record") EmpVO record, @Param("example") EmpVOExample example);
 
   public int updateByPrimaryKeySelective(EmpVO record);

   public int updateByPrimaryKey(EmpVO record);
   
   public int updateEmpsBatchForSqlite2(@Param("empList") List<EmpVO> empList);
   
}

