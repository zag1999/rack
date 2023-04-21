package com.jm.rack.pojo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hgy
 */
@Data
public class PlanMatDTO implements Serializable {

    private static final long serialVersionUID = 1L;



    /**
     * db地址
     */
    private String DB;

    /**
     * carvin
     */
    private String CarVin;

    /**
     * 计划编号
     */
    private String PlanNo;

    /**
     * 状态
     */
    private Integer State;


    /**
     * 物料编号
     */
    private String MatNo;

    /**
     * 物料名称
     */
    private String MatName;

    /**
     * 所需物料数量
     */
    private Double MatCount;


    /**
     * 创建时间
     */
    private Date CreateTime;

}
