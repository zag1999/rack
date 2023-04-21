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
 * @TableName Mes_Plan
 */
@TableName(value ="Mes_Plan")
@Data
public class MesPlan implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer iid;

    /**
     * 
     */
    private Integer psn;

    /**
     * 
     */
    private String planno;

    /**
     * 
     */
    private String workshop;

    /**
     * 
     */
    private String lineno;

    /**
     * 
     */
    private String linename;

    /**
     * 
     */
    private Date productdate;

    /**
     * 
     */
    private String shift;

    /**
     * 
     */
    private Integer flag;

    /**
     * 
     */
    private Integer isgujia;

    /**
     * 
     */
    private Date createtime;

    /**
     * 
     */
    private String createuser;

    /**
     * 
     */
    private Date updatetime;

    /**
     * 
     */
    private String updateuser;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}