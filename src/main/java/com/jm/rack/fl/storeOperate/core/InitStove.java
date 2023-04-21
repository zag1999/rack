package com.jm.rack.fl.storeOperate.core;

import com.jm.rack.fl.interactionSign.enums.InteractionOperator;
import com.jm.rack.fl.interactionSign.service.InteractionSignService;
import com.jm.rack.fl.plcConnect.enums.PlcShortInstruct;
import com.jm.rack.fl.plcConnect.service.PlcHandleService;
import com.jm.rack.fl.storeOperate.mapper.StoreOperateMapper;
import com.jm.rack.fl.storeOperate.po.StoveAndPlcPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
@Order(1)
public class InitStove {
    @Resource
    private StoreOperateMapper storeOperateMapper;
    @Resource
    private InteractionSignService interactionSignService;
    @Resource
    private PlcHandleService plcHandleService;

    /**
     * 在程序启动时初始化库存的plc值
     */
    @PostConstruct
    public void initStove() {
        // 初始化交互字
        interactionSignService.initInteractionSign();
        List<StoveAndPlcPo> list = storeOperateMapper.selectStovePlcDataAll();
        for (StoveAndPlcPo po : list) {
            try {
                // 库存值
                int value = po.getMatnum();
                // 库存值写入plc
                plcHandleService.writeShort(po.getPlcip(), po.getComNo() + po.getComChildNo(), (short) value);
                if (value <= po.getMatmin()) {
                    plcHandleService.writeShort(po.getPlcip(), po.getComNo() + po.getOpenLight(), PlcShortInstruct.YELLOW_LIGHT.getValue());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        // 发送交互字
        interactionSignService.sendInteraction(list, InteractionOperator.NUM_CHANGE, InteractionOperator.LIGHT_ON);
    }
}
