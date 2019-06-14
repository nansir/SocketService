package com.sir.service.soket;

import com.sir.service.uitls.Key;
import com.sir.service.uitls.LogUtils;
import com.sir.service.uitls.MyUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Socket TCP 服务
 * Created by zhuyinan on 2018/9/6.
 * Contact by 445181052@qq.com
 */
public class SocketTCPService extends Thread {

    //处理器
    private SocketProcessor mProcessor;
    //
    private static SocketTCPService mService = null;
    // 线程池，用于支持并发。
    private ExecutorService mThreadPool;
    //
    private ServerSocket mServerSocket;
    //启动状态
    private volatile boolean mStartup;

    /**
     * 只能存在单实例
     */
    private SocketTCPService() {
        mThreadPool = Executors.newCachedThreadPool();
        mProcessor = new SocketProcessor();
        mStartup = true;
        mServerSocket = null;
    }

    public static SocketTCPService getInstance() {
        if (mService == null) {
            synchronized (SocketTCPService.class) {
                if (mService == null) {
                    mService = new SocketTCPService();
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

    public void run() {

        try {
            //获取配置文件端口号
            String value = MyUtils.getConfig(Key.PORT);
            int port = Integer.parseInt(value);
            mServerSocket = new ServerSocket(port);
        } catch (IOException e) {
            try {
                mServerSocket = new ServerSocket(1222);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        LogUtils.i("启动TCP服务,端口号：" + mServerSocket.getLocalPort());

        while (mStartup) {
            try {
                Socket socket = mServerSocket.accept();
                if (mStartup) {
                    mThreadPool.execute(new SocketTransceiver(socket, mProcessor));
                } else {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        LogUtils.i("服务已停止");

        try {
            mThreadPool.shutdown();
            mThreadPool.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return;
    }
}
