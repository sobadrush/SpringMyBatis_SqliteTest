package com.ctbc.mybatis.util;

import java.util.List;

import org.apache.ibatis.executor.BatchResult;

/** 
 * 校验批处理结果 
 * Created by cd_huang on 2017/8/24. 
 */  
public interface CheckBatchResultHook {  
  
    public abstract boolean checkBatchResult(List<BatchResult> results);  
    
}  