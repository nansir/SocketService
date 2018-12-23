package com.sir.service.soket;

import com.sir.service.uitls.LogUtils;

import java.io.*;
import java.net.Socket;

/**
 * Socket 收发器
 * Created by zhuyinan on 2018/9/6.
 * Contact by 445181052@qq.com
 */
public class SocketTransceiver implements Runnable {

    private Socket mSocket = null;

    private SocketProcessor mProcessor;

    public SocketTransceiver(Socket socket, SocketProcessor processor) {
        this.mSocket = socket;
        this.mProcessor = processor;
    }

    public void run() {
        try {
            String str = receive();
            if (str != null && !str.trim().equals("")) {
                LogUtils.i("收到：" + str);
                String order[] = str.split("=");
                String feedback;
                if (order == null || order.length != 2) {
                    feedback = "invalid header";
                } else if (order[0].equals("cmd")) {
                    feedback = mProcessor.exeCmd(order[1]);
                } else if (order[0].equals("serial")) {
                    feedback = mProcessor.exeSerial(order[1]);
                } else {
                    feedback = "invalid header";
                }
                LogUtils.i("反馈：" + feedback);
                send(feedback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!mSocket.isClosed())
                try {
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * 接收数据
     */
    public String receive() throws IOException {
        InputStream in = mSocket.getInputStream();
        InputStreamReader streamReader = new InputStreamReader(in, "UTF-8");
        BufferedReader reader = new BufferedReader(streamReader);
        return reader.readLine();
    }

    /**
     * 发送数据
     */
    public void send(String obj) throws IOException {
        OutputStream out = mSocket.getOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(out, "UTF-8");
        BufferedWriter writer = new BufferedWriter(streamWriter);
        writer.append(obj);
        writer.newLine();
        writer.flush();
    }
}
