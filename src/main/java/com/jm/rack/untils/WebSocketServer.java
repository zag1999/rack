package com.jm.rack.untils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jm.rack.mapper.ESignMapper;
import com.jm.rack.mapper.PlanInfoMapper;
import com.jm.rack.pojo.ESign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;


@ServerEndpoint("/websocket/{id}")
@Component
public class WebSocketServer {

    @Autowired
    ESignMapper eSignMapper;

    @Autowired
    PlanInfoMapper planInfoMapper;


    private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    private static Map<String, Session> sessionMap = new HashMap<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private static  WebSocketServer webSocketServer;
    private static  String ID;


    @PostConstruct
    public void init(){
        webSocketServer = this;
    }


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "id") String id) {
//        Map<String, String> pathParameters = session.getPathParameters();
//        String sid = pathParameters.get("id");
//        if (id.equals(sid)){
//            log.info("此用户已连接socket" + getOnlineCount());
//            return;AaBom
//        }
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        sessionMap.put(id, session);
        log.info("有新窗口开始监听,当前在线人数为" + getOnlineCount());

        log.info("用户名" + id);
        try {
            sendMessage(id+"连接成功");
        } catch (Exception e) {
            log.error("websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        try {
            webSocketSet.remove(this);  //从set中删除
            subOnlineCount();           //在线数减1
            session.close();
            log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 收到客户端消息后调用的方法   类型 1  4 5 6 7
     *importDataByExcel
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到的信息:" + message);
        Map<String, String> pathParameters = session.getPathParameters();
        String id = pathParameters.get("id");
//        if (ID.equals(id)){
//            return;
//        }
//        ID=id;
        if (!message.contains("heartbeat")){
            JSONObject jsonObject = JSON.parseObject(message);
            switch (jsonObject.getInteger("type")){
                case 6:
                    //拍灯
                    System.out.println("aaaaaaaaaaaaaaaaa");
                    String com = jsonObject.getString("com");
                    Integer comchild = jsonObject.getIntValue("comchild");
                    if (!CommonUtils.getInstance().IsStringEmpty(com)&&com.length()>3){
                        QueryWrapper<ESign> eSignQueryWrapper = new QueryWrapper<>();
                        eSignQueryWrapper.eq("com_no",com.substring(3)).eq("com_child_no",comchild);
                        if (webSocketServer.eSignMapper == null){
                            System.out.println("aaaaaaaa");
                        }
                        ESign eSign = webSocketServer.eSignMapper.selectOne(eSignQueryWrapper);
                        //通知页面刷新
                        String pos = "";
                        if (eSign.getMatno().contains("Q")){
                            pos = "mat0";
                        }else if(eSign.getMatno().contains("Z")){
                            pos = "mat1";
                        }else {
                            pos = "mat2";
                        }
                        JSONObject jsonObject2 = new JSONObject();
                        jsonObject2.put("type","clap");
                        jsonObject2.put("rackno",eSign.getMatno());
                        sendSingleInfo(pos,jsonObject2);
                    }
                    break;
                case 10:
                    //要箱子,读取plc有空箱子是否可以放行,1可以放行
//                    byte b = 1;
                        byte b = HslHelper.getInstance().readByte(HslHelper.lastH, "1", jsonObject.getInteger("mat"));
                        System.out.println(b);
                        if (b == 1 ){
                            //通知plc完成空箱子放行
                            byte a = 1;
                            HslHelper.getInstance().writeByte(HslHelper.lastH, "0", a,jsonObject.getInteger("mat"));
                        }
                        break;
//                    }
                case 11:
                    //放行箱子,读取plc拣料工位是否有空位允许放行箱子,1有空位允许放行
                    byte c = HslHelper.getInstance().readByte(HslHelper.lastH, "3", jsonObject.getInteger("mat"));
//                    byte c = 1;
                    if (c == 1 ){
                        //通知plc完成箱子放行
                        byte a = 1;
                        HslHelper.getInstance().writeByte(HslHelper.lastH, "2", a,jsonObject.getInteger("mat"));
                    }
                    break;
                case 50:
                    //模拟拍灯
                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put("type","clap1");
                    sendSingleInfo("mat0",jsonObject2);
                    break;
                case 52:
                    String code ="";
                    switch (jsonObject.getInteger("color")){
                        case 1:
                            code = "红";
                            break;
                        case 2:
                            code = "黄";
                            break;
                        case 3:
                            code = "绿";
                            break;
                        case 4:
                            code = "无";
                            break;
                        default:
                            break;
                    }
                    JSONObject jsonObject3 = new JSONObject();
                    jsonObject3.put("type","5");
                    jsonObject3.put("color",code);
                    sendSingleInfo("cplus1",jsonObject3);
                    break;
            }
            return;
        }
        if ("heartbeat".equals(message)){
            Map<String, Object> maps = new HashMap<>();
            maps.put("type", message);
            sendInfo(maps);
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        log.error(error.getMessage());
       // error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(Object obj) {
        try {
            synchronized (this.session) {
                this.session.getBasicRemote().sendText((JSON.toJSONString(obj)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 群发自定义消息
     */
    public static void sendInfo(Object obj) {
        for (WebSocketServer item : webSocketSet) {
            System.out.println(item.session.getRequestParameterMap().get("id").get(0));
            try {
                item.sendMessage(obj);
            } catch (Exception e) {
                continue;
            }
        }
    }


    /*单人发消息
    *
    * front center last mat cplus
    *
    * */
    public static void sendSingleInfo(String id, Object obj) {
        Session session = sessionMap.get(id);
        try {
            if (session==null){
                return;
            }
            if (session.isOpen()){
                synchronized(session){
                    session.getBasicRemote().sendText(JSON.toJSONString(obj));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
