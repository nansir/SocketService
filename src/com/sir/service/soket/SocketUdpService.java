package com.sir.service.soket;

import com.sir.service.uitls.LogUtils;
import com.sir.service.uitls.MyUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Socket UDP 服务
 * Created by zhuyinan on 2018/9/6.
 * Contact by 445181052@qq.com
 */
public class SocketUdpService extends Thread {

    private DatagramSocket dSocket = null;

    private byte[] msg = new byte[2048];

    private static SocketUdpService mService;

    //处理器
    private SocketProcessor mProcessor;

    //启动状态
    private volatile boolean mStartup;

    public SocketUdpService() {
        mProcessor = new SocketProcessor();
        mStartup = true;
    }

    public static SocketUdpService getInstance() {
        if (mService == null) {
            synchronized (SocketUdpService.class) {
                if (mService == null) {
                    mService = new SocketUdpService();
                }
            }
        }
        return mService;
    }

    /**
     * stop service
     */
    public void stopRun() {
        mStartup = false;
    }


    @Override
    public void run() {

        DatagramPacket packet = new DatagramPacket(msg, msg.length);

        //获取配置文件端口号
        try {
            String value = MyUtils.getConfig("port");
            int port = Integer.parseInt(value);
            dSocket = new DatagramSocket(port);
        } catch (IOException e) {
            try {
                dSocket = new DatagramSocket(1222);
            } catch (SocketException e1) {
                e1.printStackTrace();
            }
        }

        try {
            LogUtils.i("启动UDP服务器,端口号：" + dSocket.getLocalPort());
            while (mStartup) {
                dSocket.receive(packet);
                String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
                if (msg != null && !msg.trim().equals("")) {
                    LogUtils.i("收到：" + msg);
                    String order[] = msg.split("=");
                    if (order == null || order.length != 2) {
                    } else if (order[0].equals("cmd")) {
                        mProcessor.exeCmd(order[1]);
                    } else if (order[0].equals("serial")) {
                        mProcessor.exeSerial(order[1]);
                    }
                }
            }
            dSocket.close();
            LogUtils.i("服务已停止");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
