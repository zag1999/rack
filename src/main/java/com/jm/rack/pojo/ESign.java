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
 * @TableName e_sign
 */
@TableName(value ="e_sign")
@Data
public class ESign implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.NONE)
    private Long eid;

    private Long rackid;

    /**
     * 
     */
    private String matno;

    /**
     * 
     */
    private String comNo;

    /**
     * 
     */
    private String comChildNo;
    /**
     * 亮灯地址
     */
    private String openLight;
    /**
     * 状态地址
     */
    private String state;

    /**
     * 1 出库  0 入库
     */

    private Integer stocksign;

    /**
     * 
     */
    private Date createtime;

    private Date updatetime;

    private String plcip;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}