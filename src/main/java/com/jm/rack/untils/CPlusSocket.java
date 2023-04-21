package com.jm.rack.untils;
/*与c#服务端通信*/

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class CPlusSocket {
    Socket socket ;
    InputStream is = null;
    private CPlusSocket() throws IOException {
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"),7562);
            is = socket.getInputStream();
        }catch (Exception e){
            socket = null;
        }
    }

    public boolean makeScoket(String info){
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"),7562);
            is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            String str = info;
            os.write(str.getBytes());
            os.close();
            return true;
        }catch (Exception e){
            socket = null;
            return false;
        }
    }
    private void makeScoket1(){
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"),7562);
            is = socket.getInputStream();
        }catch (Exception e){

        }
    }
    private static volatile CPlusSocket instance = null;

    public static CPlusSocket getInstance() throws IOException {
        if (instance == null) {
            synchronized (CPlusSocket.class) {
                if (instance == null) {
                    instance = new CPlusSocket();
                }
            }
        }
        return instance;
    }

    public boolean sendInfo(String info) throws IOException {
        if (socket != null){
            OutputStream os = socket.getOutputStream();
            String str = info;
            os.write(str.getBytes());
            os.close();
            return true;
        }else{
            return makeScoket(info);
        }
    }

    public void waitInfo(){
        new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(5000);
                    if (socket != null){
                        byte[] bys = new byte[1024];
                        int len = 0;	//用于存储读到的字节个数
                        try {
                            len = is.read(bys);
                            String content  =new String(bys,0,len);
                            if (CommonUtils.getInstance().IsStringEmpty(content)){
                                System.out.println("空信息");
                            }else {
                                System.out.println(content);
                            }
                        } catch (IOException e) {
                            try {
                                socket = null;
                                is.close();
                                makeScoket1();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }

                    }else {
                        makeScoket1();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }




}
