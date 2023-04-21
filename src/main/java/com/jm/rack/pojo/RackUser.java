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
 * @TableName rack_user
 */
@TableName(value ="rack_user")
@Data
public class RackUser implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.NONE)
    private Long userid;

    /**
     * 
     */
    private String username;


    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private Date createtime;

    /**
     * 
     */
    private Integer levels;

    /**
     * 
     */
    private String secondlevel;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}