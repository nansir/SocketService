package com.sir.service.soket;


import com.sir.service.uitls.LogUtils;
import com.sir.service.uitls.MyUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 控制基础 短连接Socket
 * Created by zhuyinan on 2018/9/21.
 * Contact by 445181052@qq.com
 */
public class SocketClient {

    //创建太多的线程，可能会使系统由于过度消耗内存或“切换过度”而导致系统资源不足,线程池可以很好的避免
    protected static ExecutorService mPool = Executors.newFixedThreadPool(5);


    public void sendTCP(final String ip, final int port, final String data) {
        mPool.execute(new ExecuteTCP(ip, port, data));
    }

    public void sendTCP(final String ip, final int port, final byte[] data) {
        mPool.execute(new ExecuteTCP(ip, port, data));
    }

    public void sendTCP(final String ip, final int port, final String data, CountDownLatch latch) {
        mPool.execute(new ExecuteTCP(ip, port, data, latch));
    }

    public void sendUDP(final String ip, final int port, final String data) {
        mPool.execute(new ExecuteUDP(ip, port, data));
    }


    /**
     * Socket TCP 发送
     */
    private static class ExecuteTCP implements Runnable {

        String mIp, mData;

        byte[] mSwitch;

        int mPort;

        CountDownLatch latch;

        int retry = 3; //重试次数

        public ExecuteTCP(String ip, int port, byte[] data) {
            this.mIp = ip;
            this.mPort = port;
            this.mSwitch = data;
        }

        public ExecuteTCP(String ip, int port, String data) {
            this.mIp = ip;
            this.mPort = port;
            this.mData = data;
        }

        public ExecuteTCP(String ip, int port, String data, CountDownLatch latch) {
            this.mIp = ip;
            this.mPort = port;
            this.mData = data;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(mIp, mPort), 5000);
                OutputStream os = socket.getOutputStream();
                Thread.sleep(200);
                os.write(MyUtils.isEmpty(mData) ? mSwitch : mData.getBytes());
                Thread.sleep(200);
                os.flush();
                os.close();
                socket.close();
                //TODO 防止系统不能及时释放的问题
                Thread.sleep(400);
                String msg = MyUtils.isEmpty(mData) ? MyUtils.bytesToString(mSwitch) : mData;
                LogUtils.i(mIp + ":" + mPort + "\t" + msg);
                if (latch != null) {
                    latch.countDown();
                }
            } catch (IOException e) {
                executionError();
            } catch (InterruptedException e) {
                LogUtils.e(e.getMessage());
            }
        }

        /**
         * 处理发送失败后阻塞线程并且5秒后重试
         */
        public void executionError() {
            String msg = MyUtils.isEmpty(mData) ? MyUtils.bytesToString(mSwitch) : mData;
            try {
                if (latch != null && retry > 0) {
                    retry = retry - 1;
                    Thread.sleep(5000);
                    LogUtils.e("Retry：" + mIp + ":" + mPort + "\t" + msg);
                    mPool.execute(this);
                } else {
                    if (latch != null) {
                        latch.countDown();
                    }
                    LogUtils.e("Connection failed：" + mIp + ":" + mPort + "\t" + msg);
                }
            } catch (InterruptedException e) {
                LogUtils.e(e.getMessage());
            }
        }
    }


    /**
     * Socket UDP发送
     */
    private static class ExecuteUDP implements Runnable {

        String mIp;
        byte[] mData;
        int mPort;

        public ExecuteUDP(String ip, int port, String data) {
            this.mIp = ip;
            this.mPort = port;
            this.mData = data.getBytes();
            LogUtils.i(mIp + ":" + mPort + "\t" + data);
        }

        public ExecuteUDP(String ip, int port, byte[] data) {
            this.mIp = ip;
            this.mPort = port;
            this.mData = data;
            LogUtils.i(mIp + ":" + mPort + "\t" + MyUtils.bytesToString(data));
        }

        @Override
        public void run() {
            try {
                DatagramSocket socket = new DatagramSocket(9999);
                SocketAddress address = new InetSocketAddress(mIp, mPort);
                DatagramPacket packet = new DatagramPacket(mData, mData.length, address);
                socket.send(packet);
                socket.close();
            } catch (SocketException e) {
                LogUtils.e(mIp + ": Failed to send");
            } catch (IOException e) {
                LogUtils.e(mIp + ": Failed to send");
            }
        }
    }
}
