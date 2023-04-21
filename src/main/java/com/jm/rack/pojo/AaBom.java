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
 * @TableName AA_Bom
 */
@TableName(value ="AA_Bom")
@Data
public class AaBom implements Serializable {
    /**
     * 
     */
    private Double iid;

    /**
     * 
     */
    private String factorycode;

    /**
     * 
     */
    private String xuhao;

    /**
     * 
     */
    private String jzbomid;

    /**
     * 
     */
    private String prono;

    /**
     * 
     */
    private String matno;

    /**
     * 
     */
    private String matname;

    /**
     * 
     */
    private Double quantity;

    /**
     * 
     */
    private String unit;

    /**
     * 
     */
    private String sapver;

    /**
     * 
     */
    private Double state;

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
    private String updateuser;

    /**
     * 
     */
    private Date updatetime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}