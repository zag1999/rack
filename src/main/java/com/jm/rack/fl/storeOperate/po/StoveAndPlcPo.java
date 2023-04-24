package com.jm.rack.fl.storeOperate.po;

import com.jm.rack.pojo.ESign;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StoveAndPlcPo extends ESign {
    /**
     * matinfo表的主键
     */
    private Long matid;

    /**
     * rackinfo表的主键
     */
    private Long mrid;
    /**
     * 库存减去多少
     */
    private Integer quantity;
    /**
     * 物料库存数
     */
    private Integer matnum;
    /**
     * 物料库存数最大值
     */
    private Integer matmax;
    /**
     * 物料库存数最小值
     */
    private Integer matmin;
}
