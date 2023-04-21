package com.jm.rack.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jm.rack.pojo.AaMaterial;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Entity com.jm.rack.pojo.AaMaterial
 *
 *
 *  10.57.31.10
 * JmZyMesData
 * jh  123@qwe
 */
@Repository
@DS("sqlserver")

public interface AaMaterialMapper extends BaseMapper<AaMaterial> {

}




