package com.ld.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ld.chat.domain.SensitiveWord;

public interface SensitiveWordMapper extends BaseMapper<SensitiveWord> {
    
    int deleteByPrimaryKey(Long id);
    
    int insert(SensitiveWord record);
    
    int insertSelective(SensitiveWord record);
    
    SensitiveWord selectByPrimaryKey(Long id);
    
    int updateByPrimaryKeySelective(SensitiveWord record);
    
    int updateByPrimaryKey(SensitiveWord record);
}