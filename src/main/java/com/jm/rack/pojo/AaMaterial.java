package com.jm.rack.pojo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @TableName AA_Material
 */
@TableName(value ="AA_Material")
@Data
@JsonInclude(Include.NON_NULL)
public class AaMaterial implements Serializable {
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
    private String matno;

    /**
     * 
     */
    private String matname;

    /**
     * 
     */
    private String matsimple;

    /**
     * 
     */
    private String unit;

    /**
     * 
     */
    private Double state;

    /**
     * 
     */
    private Double sscan;

    /**
     * 
     */
    private Double scanlevel;

    /**
     * 
     */
    private String statecode;

    /**
     * 
     */
    private String batch;

    /**
     * 
     */
    private String createuser;

    /**
     * 
     */
    private Date createtime;

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