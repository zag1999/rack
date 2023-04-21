package com.jm.rack.fl.storeOperate.mapper;

import com.jm.rack.fl.storeOperate.po.StoveAndPlcPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreOperateMapper {
    /**
     * 查询入库的plc点
     *
     * @param planId
     * @return
     */
    List<StoveAndPlcPo> selectStovePlcDataByPlanId(Long planId);

    /**
     * 查询取料的plc点
     *
     * @param planId
     * @return
     */
    List<StoveAndPlcPo> selectTakeOutPlcDataByPlanId(Long planId);

    /**
     * 查询出库的plc点
     *
     * @param matnos
     * @return
     */
    List<StoveAndPlcPo> selectTakeOutPlcDataByMatno(List<String> matnos);

    /**
     * 查询所有的库存与plc点
     *
     * @return
     */
    List<StoveAndPlcPo> selectStovePlcDataAll();
}
