package com.jm.rack.pojo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hgy
 */
@Data
@ToString
public class PlcDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * db地址
     */
    private String DB;

    /**
     *读或写的数据
     */
    private byte ByteData;

    /**
     * 创建时间
     */
    private Date CreateTime;

}
