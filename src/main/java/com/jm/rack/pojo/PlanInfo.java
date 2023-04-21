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
 * @TableName plan_info
 */
@TableName(value ="plan_info")
@Data
public class PlanInfo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.NONE)
    private Long planId;


    private Long logicNo;

    /**
     * 
     */
    private String planno;

    /**
     * 
     */
    private Date productdate;

    /**
     * 
     */
    private String carvin;

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
    private String cartype;

    /**
     * 
     */
    private String linetype;

    /**
     * 0 未开始  1  执行中  2 已完成
     */
    private Integer state;

    /**
     * 
     */
    private Date createtime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableField(exist = false) //用于临时顶替c#关灯
    private String str;

    @TableField(exist = false) //用于临时顶替c#关灯
    private String time;

    public PlanInfo(String planno,Long logicNo ,Date productdate, String carvin, String prono, String proname, String cartype, String linetype, Integer state, Date createtime) {

        this.planno = planno;
        this.logicNo = logicNo;
        this.productdate = productdate;
        this.carvin = carvin;
        this.prono = prono;
        this.proname = proname;
        this.cartype = cartype;
        this.linetype = linetype;
        this.state = state;
        this.createtime = createtime;
    }

    public PlanInfo() {

    }
}