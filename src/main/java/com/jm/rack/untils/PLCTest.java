package com.jm.rack.untils;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class PLCTest {
    public static void main(String[] args) throws InterruptedException {
        boolean b1 = HslHelper.getInstance().writeByte("DB93.", "2", (byte)1, 2);
        System.out.println(b1);
        byte b = HslHelper.getInstance().readByte("DB93.", "2",2);
        System.out.println(b);
//        boolean b2 = HslHelper.getInstance().writeByte(HslHelper.lastH, "2", (byte) 4, 2);
//        System.out.println(b2);
//        byte c = HslHelper.getInstance().readByte(HslHelper.lastH, "2",2);
//        System.out.println(c);
//            for (int i = 0; i < 100; i++) {
//
//                int finalI = i;
//                CommonUtils.getInstance().getExecutorService().execute(new Runnable() {
//                    @SneakyThrows
//                    @Override
//                    public void run() {
//                        new Thread().sleep(1000);
//                        //模拟耗时操作
//                        System.out.println(Thread.currentThread().getName()+"aaaa"+ finalI);
//                    }
//                });
            }


//        int[] a = {1,2,3,4,5,6,7,8,9};
//        List<String> stringList = new ArrayList<>();
//        CommonUtils.getInstance().getExecutorService().execute(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < a.length; i++) {
//                    try {
//                        Thread.sleep(1000);
//                        log.info(a[i]);
//                        Thread.sleep(1000);
//                        stringList.add(String.valueOf(a[i]));
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                for (int q = 0; q < stringList.size(); q++) {
//                    log.info("县城里"+stringList.get(q));
//                }
//            }
//        });
//
//        for (int q = 0; q < stringList.size(); q++) {
//            log.info(stringList.get(q));
//        }



//        System.out.println("com3".substring(3));
//        byte byte0 = 0;
//        byte byte1 = 1;
        // 0 2 放行
        //中
    //    HslHelper.getInstance().writeByte1(HslHelper.lastH,"0",byte1);
//        Thread.sleep(3000);
//        HslHelper.getInstance().writeByte1(HslHelper.lastH,"0",byte1);
    //   HslHelper.getInstance().writeByte(HslHelper.QDB,"2",byte1);

    //    Thread.sleep(3000);
//        HslHelper.getInstance().writeByte1(HslHelper.lastH,"0",byte1);
//
//HslHelper.getInstance().writeByte1(HslHelper.lastH,"0",byte1);
//        HslHelper.getInstance().writeByte(HslHelper.QDB,"2",byte1);
        //
        //前
       // HslHelper.getInstance().writeByte(HslHelper.DB1,"0",byte1);
        //三排
       // HslHelper.getInstance().writeByte(HslHelper.DB95,"0",byte1);


    }

