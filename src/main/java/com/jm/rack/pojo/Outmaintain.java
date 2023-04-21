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
 * @TableName outmaintain
 */
@TableName(value ="outmaintain")
@Data
public class Outmaintain implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.NONE)
    private Long outid;

    /**
     * 
     */
    private String prono;

    /**
     * 
     */
    private String matcode;

    /**
     * 
     */
    private String matname;

    /**
     * 
     */
    private Integer count;

    /**
     * 
     */
    private Date creatrtime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}