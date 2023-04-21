package com.jm.rack.mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jm.rack.pojo.StaSeat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Entity com.jm.rack.pojo.StaSeat
 */

@Repository
@DS("sqlserver")
public interface StaSeatMapper extends BaseMapper<StaSeat> {
    StaSeat selectOneByVin(@Param("vin") String vin);
}




