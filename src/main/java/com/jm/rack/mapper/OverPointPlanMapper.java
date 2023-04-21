package com.jm.rack.mapper;
import org.apache.ibatis.annotations.Param;

import com.jm.rack.pojo.OverPointPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Entity com.jm.rack.pojo.OverPointPlan
 */

@Repository
public interface OverPointPlanMapper extends BaseMapper<OverPointPlan> {
    int updateOverPointSign(@Param("overPointSign") Byte overPointSign);
}




