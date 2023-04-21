package com.jm.rack;

import com.jm.rack.untils.CommonUtils;
import com.jm.rack.untils.HslHelper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

@Log4j2
class RackApplicationTests {

    @Test
    synchronized void  contextLoads() {
        for (int i = 0; i < 100; i++) {

            int finalI = i;
            CommonUtils.getInstance().getExecutorService().execute(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    new Thread().sleep(1000);
                    //模拟耗时操作
                    System.out.println(Thread.currentThread().getName()+"aaaa"+ finalI);
                }
            });
        }
    }

    @Test
    void t1(){
        byte b = HslHelper.getInstance().readByte("DB93.", "1", 3);
        System.out.println(b);
    }
}
