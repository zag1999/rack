package com.jm.rack.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jm.rack.pojo.PlanInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Entity com.jm.rack.pojo.PlanInfo
 */
@Repository
public interface PlanInfoMapper extends BaseMapper<PlanInfo> {

    List<PlanInfo> selectAllByStateAndLinetype(@Param("state") Integer state, @Param("linetype") String linetype);

    List<PlanInfo> selectAllByCarvin(@Param("carvin") String carvin);

    int countByStateAndLinetype(@Param("state") Integer state, @Param("linetype") String linetype);

    int updateStateByPronoAndCarvin(@Param("state") Integer state, @Param("prono") String prono, @Param("carvin") String carvin);


    List<PlanInfo> selectPlannoByLinetype(@Param("linetype") String linetype);


    int updateStateByPlanno(@Param("state") Integer state, @Param("planno") String planno);

    @Select("SELECT * FROM plan_info WHERE carvin != '' and ISNULL(productdate)")
    List<PlanInfo>  selectByCarvinAndTime();

    String selectcarvinbByPlanid(@Param("planId") Integer planId);
}




