package com.jm.rack.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName Mes_PlanDetail
 */
@TableName(value ="Mes_PlanDetail")
@Data
public class MesPlandetail implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer iid;

    /**
     * 
     */
    private String factorycode;

    /**
     * 
     */
    private String planno;

    /**
     * 
     */
    private Integer iseq;

    /**
     * 
     */
    private String prono;

    /**
     * 
     */
    private String proname;

    /**
     * 
     */
    private Integer quantity;

    /**
     * 
     */
    private String procode;

    /**
     * 
     */
    private String verno;

    /**
     * 
     */
    private String cartype;

    /**
     * 
     */
    private String carbody;

    /**
     * 
     */
    private String carvin;

    /**
     * 
     */
    private Integer process;

    /**
     * 
     */
    private Integer meter;

    /**
     * 
     */
    private Integer flag;

    /**
     * 
     */
    private Integer isprint;

    /**
     * 
     */
    private Date printtime;

    /**
     * 
     */
    private Integer isproducted;

    /**
     * 
     */
    private Date producttime;

    /**
     * 
     */
    private Date creattime;

    /**
     * 
     */
    private Date updatetime;

    /**/
    @TableField(exist = false)
    private String updatetimeformat;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}