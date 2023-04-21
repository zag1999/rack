package com.jm.rack.mapper;
import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jm.rack.pojo.MesPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @Entity com.jm.rack.pojo.MesPlan
 */
@Repository
@DS("sqlserver")
public interface MesPlanMapper extends BaseMapper<MesPlan> {
    List<MesPlan> selectMesPlanByLineName(Map map);
}




