package com.jm.rack.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 
 * @TableName material_info
 */
@TableName(value ="material_info")
@Data
public class MaterialInfo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.NONE)
    private Long materialInfoId;

    /**
     * 
     */
    private Integer currow;

    /**
     * 
     */
    private Integer curcolumn;

    /**
     * 
     */
    private Long rackInfoId;

    /**
     * 
     */


    private String matno;



    /**
     * 
     */
    private Date createtime;

    /**
     * 
     */
    private Date updatetime;

    @TableField(exist = false)
    private List<MatRackInfo> child;




    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}