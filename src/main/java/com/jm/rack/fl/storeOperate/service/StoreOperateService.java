package com.jm.rack.fl.storeOperate.service;

import com.jm.rack.fl.commonCache.CommonCache;
import com.jm.rack.fl.interactionSign.enums.InteractionOperator;
import com.jm.rack.fl.interactionSign.service.InteractionSignService;
import com.jm.rack.fl.plcConnect.enums.PlcShortInstruct;
import com.jm.rack.fl.plcConnect.service.PlcHandleService;
import com.jm.rack.fl.storeOperate.mapper.StoreOperateMapper;
import com.jm.rack.fl.storeOperate.po.StoveAndPlcPo;
import com.jm.rack.mapper.MatRackInfoMapper;
import com.jm.rack.mapper.PlanMatInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StoreOperateService {
    @Resource
    private CommonCache commonCache;
    @Resource
    private StoreOperateMapper storeOperateMapper;
    @Resource
    private MatRackInfoMapper matRackInfoMapper;
    @Resource
    private PlcHandleService plcHandleService;
    @Resource
    private InteractionSignService interactionSignService;
    @Resource
    private PlanMatInfoMapper planMatInfoMapper;

    public boolean subtractStore(int type) {
        // 此时灯已经被拍灭了，plc的值已经是1了
        // 根据计划传入的生产线类型获取计划id
        Long planId = commonCache.getPlanId(type);
        if (planId == null) {
            log.error("未找到类型" + type + "对应的计划ID");
            return false;
        }
        // 根据计划id查询对应的物料库存和plc地址
        List<StoveAndPlcPo> list = storeOperateMapper.selectStovePlcDataByPlanId(planId);
        // 初始化交互字
        interactionSignService.initInteractionSign();
        // 库存数量校验，如果为负库存则减库存失败
        boolean isNegative = filterNegativeStove(list);
        if (isNegative) return false;
        // 减库存
        for (StoveAndPlcPo po : list) {
            try {
                // 计算最终库存值
                int value = po.getMatnum() - po.getQuantity();
                // 写入plc
                plcHandleService.writeInt(po.getPlcip(), po.getComNo() + po.getComChildNo(), (short) value);
                //更细数据库
                matRackInfoMapper.updateMatnumByMrid(value, po.getMrid());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        //给交互字
        interactionSignService.sendInteraction(list, InteractionOperator.NUM_CHANGE);
        return true;
    }

    /**
     * 负库存校验
     *
     * @param list 这里传入的是库存的地址
     * @return
     */
    private boolean filterNegativeStove(List<StoveAndPlcPo> list) {
        List<StoveAndPlcPo> negative = new ArrayList<>();
        List<StoveAndPlcPo> exceedAlarmValue = new ArrayList<>();
        for (StoveAndPlcPo po : list) {
            if ((po.getMatnum() - po.getQuantity()) <= 0) negative.add(po);
            if ((po.getMatnum() - po.getQuantity()) <= po.getMatmin()) exceedAlarmValue.add(po);
        }
        if (negative.size() > 0) {
            sendNegativeAlarm(negative);
            return true;
        }
        if (exceedAlarmValue.size() > 0) {
            sendExceedAlarm(exceedAlarmValue);
        }
        return false;
    }

    /**
     * 负库存时触发的动作
     *
     * @param negative 这里传入的是库存的地址
     */
    private void sendNegativeAlarm(List<StoveAndPlcPo> negative) {
        // 获取料架号
        List<String> matnos = negative.stream().map(StoveAndPlcPo::getMatno).collect(Collectors.toList());
        // 根据料架号获取取料的地址
        List<StoveAndPlcPo> list = storeOperateMapper.selectTakeOutPlcDataByMatno(matnos);
        for (StoveAndPlcPo g : list) {
            try {
                int value = g.getQuantity();
                // TODO 报警喊喇叭
                // 写入数量
                plcHandleService.writeInt(g.getPlcip(), g.getComNo() + g.getComChildNo(), (short) value);
                // 亮绿灯
                plcHandleService.writeInt(g.getPlcip(), g.getComNo() + g.getOpenLight(), PlcShortInstruct.RED_LIGHT.getValue());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        // 发送交互字
        interactionSignService.sendInteraction(list, InteractionOperator.NUM_CHANGE, InteractionOperator.LIGHT_ON);
    }


    /**
     * 当数量小于警戒值时触发的动作
     *
     * @param exceedAlarmValue 这里传入的是库存的地址
     */
    private void sendExceedAlarm(List<StoveAndPlcPo> exceedAlarmValue) {
        for (StoveAndPlcPo g : exceedAlarmValue) {
            try {
                // 亮黄灯
                plcHandleService.writeInt(g.getPlcip(), g.getComNo() + g.getOpenLight(), PlcShortInstruct.RED_LIGHT.getValue());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        // 发送交互字
        interactionSignService.sendInteraction(exceedAlarmValue, InteractionOperator.NUM_CHANGE, InteractionOperator.LIGHT_ON);
    }

    /**
     * 根据当前计划和plc点值，来修改plan_mat_info的状态
     *
     * @param type
     */
    public void updateMatStatus(Integer type) {
        Long planId = commonCache.getPlanId(type);
        if (planId == null) {
            log.error("未找到类型" + type + "对应的计划ID");
            return;
        }
        // 根据计划id查询取料的plc地址
        List<StoveAndPlcPo> list = storeOperateMapper.selectTakeOutPlcDataByPlanId(planId);
        List<Long> needFinish = new ArrayList<>();
        for (StoveAndPlcPo item : list) {
            try {
//                int value = plcHandleService.readInt(item.getPlcip(), item.getComNo() + item.getState());
//                if (value == 1)
                needFinish.add(item.getMatid());
                //读到1已拣货状态时显示给页面并且需要再将状态复位
//                if (value == 1) plcHandleService.writeInt(item.getPlcip(), item.getComNo() + item.getState(), (short) 0);
            } catch (Exception e) {
                log.error("读取ip为" + item.getPlcip() + "plc点" + item.getComNo() + item.getState() + "失败", e);
            }
        }
        //当读到有拍灯的时候再去修改数据库状态
//        if (null != needFinish && needFinish.size() > 0)
            planMatInfoMapper.toFinishById(needFinish);
    }
}
