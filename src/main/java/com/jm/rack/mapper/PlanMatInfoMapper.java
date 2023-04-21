package com.jm.rack.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import java.util.Date;

import com.jm.rack.pojo.PlanMatInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Entity com.jm.rack.pojo.PlanMatInfo
 */
@Repository
public interface PlanMatInfoMapper extends BaseMapper<PlanMatInfo> {

    int deleteByCreatetime();

    int updateMatstateByPronoAndCarvin(@Param("matstate") Integer matstate, @Param("prono") String prono, @Param("carvin") String carvin);

    int updateMatstateByPlanid(@Param("matstate") Integer matstate, @Param("planid") Long planid);

    List<PlanMatInfo> selectAllByPlanid(@Param("planid") Long planid);

    int updateMatstateByProno(@Param("matstate") Integer matstate, @Param("prono") String prono,@Param("planid") long planid);

    int toFinishById(List<Long> needFinish);
}




