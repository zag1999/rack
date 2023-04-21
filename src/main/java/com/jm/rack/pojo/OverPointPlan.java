package com.jm.rack.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName over_point_plan
 */
@TableName(value ="over_point_plan")
@Data
public class OverPointPlan implements Serializable {
    /**
     * 
     */
    private Byte overPointSign;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}