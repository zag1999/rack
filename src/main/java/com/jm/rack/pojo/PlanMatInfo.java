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
 * @TableName plan_mat_info
 */
@TableName(value ="plan_mat_info")
@Data
public class PlanMatInfo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.NONE)
    private Long matid;



    /**
     * 0拣货中 1 已拣货
     */
    private Integer matstate;

    private String prono;
    private String carvin;

    private String matno;
    private String matname;
    private Double quantity;

    private Long planid;
    /**
     * 
     */
    private Date createtime;

    @TableField(exist = false)
    private String rackno;


    @TableField(exist = false)
    private String pos;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public PlanMatInfo(Integer matstate, String prono, String carvin, String matno, String matname, Double quantity, Date createtime,Long planid) {
        this.matstate = matstate;
        this.prono = prono;
        this.carvin = carvin;
        this.matno = matno;
        this.matname = matname;
        this.quantity = quantity;
        this.createtime = createtime;
        this.planid = planid;
    }

    public PlanMatInfo() {

    }
}