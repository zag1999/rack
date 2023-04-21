package com.jm.rack.fl.plcConnect.enums;

public enum PlcShortInstruct {
    /**
     * 绿灯
     */
    GREEN_LIGHT((short) 0x03E0),
    /**
     * 黄灯
     */
    YELLOW_LIGHT((short) 0x03FF),
    /**
     * 红灯
     */
    RED_LIGHT((short) 0x7C00),
    /**
     * 蓝灯
     */
    BLUE_LIGHT((short) 0x001F),
    /**
     * 灭灯
     */
    CLOSE_LIGHT((short) 0x2020),
    /**
     * 取货完成时PLC数值
     */
    TAKE_FINISH((short) 1),
    /**
     * 收到取货完成后应该给plc回复的值
     */
    TAKE_FINISH_ANSWER((short) 2),
    /**
     * 复位的值
     */
    RESET((short) 1);

    private final Short value;

    PlcShortInstruct(short value) {
        this.value = value;
    }

    public Short getValue() {
        return value;
    }
}
