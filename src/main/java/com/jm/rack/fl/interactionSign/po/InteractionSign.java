package com.jm.rack.fl.interactionSign.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 交互字表
 */
@Data
@TableName(value = "interaction_sign")
public class InteractionSign {
    /**
     * 主键
     */
    @TableId(value = "interaction_id", type = IdType.ASSIGN_ID)
    private Long interactionId;

    /**
     * plc地址
     */
    @TableField(value = "plcip")
    private String plcip;

    /**
     * db块地址
     */
    @TableField(value = "com_no")
    private String comNo;

    @TableField(value = "`location`")
    private String location;

    /**
     * 数字修改交互
     */
    @TableField(value = "interaction_num")
    private String interactionNum;

    /**
     * 亮灯交互
     */
    @TableField(value = "interaction_light_on")
    private String interactionLightOn;

    /**
     * 灭灯交互
     */
    @TableField(value = "interaction_light_off")
    private String interactionLightOff;

    /**
     * 库存数写入完成
     */
    @TableField(value = "interaction_stove_finish")
    private String interactionStoveFinish;

    /**
     * 补货亮灯完成
     */
    @TableField(value = "interaction_fill_light")
    private String interactionFillLight;

    /**
     * 补货完成
     */
    @TableField(value = "interaction_fill")
    private String interactionFill;

    /**
     * 全亮
     */
    @TableField(value = "interaction_light_on_all")
    private String interactionLightOnAll;

    /**
     * 全亮完成
     */
    @TableField(value = "interaction_light_on_all_f")
    private String interactionLightOnAllF;

    /**
     * 全灭
     */
    @TableField(value = "interaction_light_off_all")
    private String interactionLightOffAll;

    /**
     * 全灭完成
     */
    @TableField(value = "interaction_light_off_all_f")
    private String interactionLightOffAllF;

    /**
     * 重置
     */
    @TableField(value = "interaction_reset")
    private String interactionReset;

    /**
     * 货架类型 Q前排 Z中排 S三排
     */
    @TableField(value = "stove_type")
    private String stoveType;
}