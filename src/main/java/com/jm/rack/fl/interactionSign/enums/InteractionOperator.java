package com.jm.rack.fl.interactionSign.enums;

import com.jm.rack.fl.interactionSign.po.InteractionSign;
import com.jm.rack.fl.plcConnect.service.PlcHandleService;
import com.jm.rack.fl.utils.spring.SpringUtils;

public enum InteractionOperator {
    /**
     * 交互字
     */
    NUM_CHANGE() {
        @Override
        public void apply(InteractionSign item) throws Exception {
            plcHandleService.writeInt(item.getPlcip(), item.getComNo() + item.getInteractionNum(), (short) 1);
        }
    },
    /**
     * 亮灯
     */
    LIGHT_ON {
        @Override
        public void apply(InteractionSign item) throws Exception {
            plcHandleService.writeInt(item.getPlcip(), item.getComNo() + item.getInteractionLightOn(), (short) 1);
        }
    },
    /**
     * 灭灯
     */
    LIGHT_OFF {
        @Override
        public void apply(InteractionSign item) throws Exception {
            plcHandleService.writeInt(item.getPlcip(), item.getComNo() + item.getInteractionLightOff(), (short) 1);
        }
    },
    /**
     * 库存数量写入完成
     */
    STOVE_FINISH {
        @Override
        public void apply(InteractionSign item) throws Exception {
            plcHandleService.writeInt(item.getPlcip(), item.getComNo() + item.getInteractionStoveFinish(), (short) 1);
        }
    },
    /**
     * 补货亮灯完成
     */
    FILL_LIGHT {
        @Override
        public void apply(InteractionSign item) throws Exception {
            plcHandleService.writeInt(item.getPlcip(), item.getComNo() + item.getInteractionFillLight(), (short) 1);
        }
    },
    /**
     * 补货完成
     */
    FILL {
        @Override
        public void apply(InteractionSign item) throws Exception {
            plcHandleService.writeInt(item.getPlcip(), item.getComNo() + item.getInteractionFill(), (short) 1);
        }
    },
    /**
     * 灯全亮
     */
    ALL_LIGHT_ON {
        @Override
        public void apply(InteractionSign item) throws Exception {
            plcHandleService.writeInt(item.getPlcip(), item.getComNo() + item.getInteractionLightOnAll(), (short) 1);
        }
    },
    /**
     * 灯全亮完成
     */
    ALL_LIGHT_ON_FINISH {
        @Override
        public void apply(InteractionSign item) throws Exception {
            plcHandleService.writeInt(item.getPlcip(), item.getComNo() + item.getInteractionLightOnAllF(), (short) 1);
        }
    },
    /**
     * 灯全灭
     */
    ALL_LIGHT_OFF {
        @Override
        public void apply(InteractionSign item) throws Exception {
            plcHandleService.writeInt(item.getPlcip(), item.getComNo() + item.getInteractionLightOffAll(), (short) 1);
        }
    },
    /**
     * 灯全灭完成
     */
    ALL_LIGHT_OFF_FINISH {
        @Override
        public void apply(InteractionSign item) throws Exception {
            plcHandleService.writeInt(item.getPlcip(), item.getComNo() + item.getInteractionLightOffAllF(), (short) 1);
        }
    },
    /**
     * 重置
     */
    RESET {
        @Override
        public void apply(InteractionSign item) throws Exception {
            plcHandleService.writeInt(item.getPlcip(), item.getComNo() + item.getInteractionReset(), (short) 1);
        }
    };


    final PlcHandleService plcHandleService = SpringUtils.getBean(PlcHandleService.class);

    public abstract void apply(InteractionSign item) throws Exception;
}