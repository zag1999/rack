package com.jm.rack.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName rack_info
 */
@TableName(value ="rack_info")
@Data
public class RackInfo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.NONE)
    private Long infoId;

    /**
     * 
     */
    private String infoName;

    /**
     * 
     */
    private Integer currow;

    /**
     * 
     */
    private Integer columns;

    /**
     * 
     */
    private Date createtime;

    /**
     * 
     */
    private Date updatetime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}