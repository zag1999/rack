package com.jm.rack.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jm.rack.pojo.AaBom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Entity com.jm.rack.pojo.AaBom
 */

@Repository
@DS("sqlserver")
public interface AaBomMapper extends BaseMapper<AaBom> {

}




