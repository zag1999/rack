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
 * @TableName mat_rack_info
 */
@TableName(value ="mat_rack_info")
@Data
public class MatRackInfo implements Serializable {
    /**
     * 
     */
    @TableId
    private Long mrid;

    /**
     * 料架位置
     */
    private String rackno;

    /**
     * 
     */
    private String code;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Integer matnum;

    /**
     * 
     */
    private Integer matmax;

    /**
     * 
     */
    private Integer matmin;

    /**
     * 
     */
    private Date createtime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}