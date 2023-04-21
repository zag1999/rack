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
 * @TableName mat_replenish
 */
@TableName(value ="mat_replenish")
@Data
public class MatReplenish implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.NONE)
    private Long mrid;

    /**
     * 
     */
    private String rackname;

    /**
     * 
     */
    private String rackmatno;

    /**
     * 
     */
    private Integer matcountAdd;

    /**
     * 
     */
    private Integer matcountFront;

    /**
     * 
     */
    private Integer matcountLast;

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
    private String staffname;

    /**
     * 
     */
    private Date createtime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public MatReplenish() {

    }

    public MatReplenish(String rackname, String rackmatno, Integer matcountAdd, Integer matcountFront, Integer matcountLast, String matcode, String matname, String staffname, Date createtime) {
        this.rackname = rackname;
        this.rackmatno = rackmatno;
        this.matcountAdd = matcountAdd;
        this.matcountFront = matcountFront;
        this.matcountLast = matcountLast;
        this.matcode = matcode;
        this.matname = matname;
        this.staffname = staffname;
        this.createtime = createtime;
    }
}