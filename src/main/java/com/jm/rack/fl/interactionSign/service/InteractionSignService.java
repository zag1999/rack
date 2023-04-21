package com.jm.rack.fl.interactionSign.service;

import com.jm.rack.fl.interactionSign.enums.InteractionOperator;
import com.jm.rack.fl.interactionSign.mapper.InteractionSignMapper;
import com.jm.rack.fl.interactionSign.po.InteractionSign;
import com.jm.rack.fl.storeOperate.po.StoveAndPlcPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
public class InteractionSignService {
    @Resource
    private InteractionSignMapper interactionSignMapper;
    @Resource
    private InteractionSignService interactionSignService;
    private final ConcurrentMap<String, InteractionSign> map = new ConcurrentHashMap<>();

    public void initInteractionSign() {
        List<InteractionSign> list = interactionSignMapper.selectAll();
        for (InteractionSign item : list) {
            map.put(item.getLocation(), item);
        }
    }

    public void sendInteractionSign(String location, InteractionOperator operator) throws Exception {
        InteractionSign item = map.get(location);
        if (item == null) throw new Exception("未找到" + location + "对应的交互字");
        operator.apply(item);
    }

    public void sendInteractionSign(Set<String> location, InteractionOperator operator) throws Exception {
        for (String l : location) {
            sendInteractionSign(l, operator);
        }
    }
    public void sendInterStoveFinishSign(Set<String> location, InteractionOperator operator) throws Exception {
        for (String l : location) {
            sendInteractionSign(l, operator);
        }
    }

    public void sendInteractionSign(String ip, String db, InteractionOperator operator) throws Exception {
        sendInteractionSign(ip + db, operator);
    }

    /**
     * 发送交互字
     *
     * @param list
     * @param operator
     */
    public void sendInteraction(List<StoveAndPlcPo> list, InteractionOperator... operator) {
        Set<String> locationList = new HashSet<>();
        for (StoveAndPlcPo item : list) {
            locationList.add(item.getPlcip() + item.getComNo());
        }
        for (InteractionOperator op : operator) {
            try {
                interactionSignService.sendInteractionSign(locationList, op);
            } catch (Exception e) {
                log.error("交互字发送失败", e);
            }
        }
    }

    /**
     * 根据货架类型获取交互字数据
     *
     * @param type
     * @return
     */
    public List<InteractionSign> getListByType(String type) {
        List<InteractionSign> list = new ArrayList<>();
        for (String key : map.keySet()) {
            InteractionSign item = map.get(key);
            if (item.getStoveType().equals(type)) {
                list.add(item);
            }
        }
        return list;
    }


    /**
     * 给库存数写入完成总信号 ---应写入2
     * @param list
     * @param operator
     */
    public void sendInterStoveFinishSign(List<StoveAndPlcPo> list, InteractionOperator ... operator) {
        Set<String> locationList = new HashSet<>();
        for (StoveAndPlcPo item : list) {
            locationList.add(item.getPlcip() + item.getComNo());
        }
        for (InteractionOperator op : operator) {
            try {
                interactionSignService.sendInterStoveFinishSign(locationList, op);
            } catch (Exception e) {
                log.error("完成总信号发送失败", e);
            }
        }
    }
}
