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
 * @TableName sta_seat
 */
@TableName(value ="sta_seat")
@Data
public class StaSeat implements Serializable {
    /**
     * 
     */
    private Integer iid;

    /**
     * 
     */
    private String vin;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    private Integer flag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}