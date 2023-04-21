package com.jm.rack.fl.interactionSign.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jm.rack.fl.interactionSign.po.InteractionSign;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InteractionSignMapper extends BaseMapper<InteractionSign> {
    List<InteractionSign> selectAll();
}