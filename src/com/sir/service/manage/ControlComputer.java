package com.sir.service.manage;


import com.google.gson.Gson;
import com.sir.service.serial.SerialBean;
import com.sir.service.soket.SocketClient;
import com.sir.service.uitls.LogUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;

/**
 * 控制计算机
 * Created by zhuyinan on 2019/3/2.
 * Contact by 445181052@qq.com
 */
public class ControlComputer extends SocketClient {

    /**
     * 唤醒主机
     *
     * @param ip         主机ip
     * @param mac        主机mac
     * @param subnetMask 主机子网掩码
     */
    public void wakeUpDevice(final String ip, final String mac, final String subnetMask) {
        Runnable mRunnable = () -> {
            String broadcastAddress = getBroadcastAddress(ip.trim(), subnetMask.trim());
            String processMac = mac.replace("-", "").replace(":", "");
            wakeBy(broadcastAddress, processMac, 4343);
            try {
                //防止系统不及时能释放的问题
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        mPool.execute(mRunnable);
    }

    /**
     * 唤醒主机
     *
     * @param ip  主机ip
     * @param mac 主机mac
     */
    public void wakeUpDevice(final String ip, final String mac) {
        wakeUpDevice(ip, mac, "255.255.255.0");
    }

    /**
     * 根据子网掩码和ip得到主机的广播地址
     *
     * @param ip
     * @param subnetMask
     * @return
     */
    private String getBroadcastAddress(String ip, String subnetMask) {
        String ipBinary = toBinary(ip);
        String subnetBinary = toBinary(subnetMask);
        String broadcastBinary = getBroadcastBinary(ipBinary, subnetBinary);
        String wholeBroadcastBinary = spiltBinary(broadcastBinary);
        return binaryToDecimal(wholeBroadcastBinary);
    }

    /**
     * 网络唤醒
     *
     * @param ip   主机ip
     * @param mac  主机mac
     * @param port 端口
     */
    private void wakeBy(String ip, String mac, int port) {
        //构建magic魔术包
        String MagicPackage = "FFFFFFFFFFFF";
        for (int i = 0; i < 16; i++) {
            MagicPackage += mac;
        }
        byte[] MPBinary = hexStr2BinArr(MagicPackage);
        try {
            InetAddress address = InetAddress.getByName(ip);
            DatagramSocket socket = new DatagramSocket(port);
            DatagramPacket packet = new DatagramPacket(MPBinary, MPBinary.length, address, port);
            //发送udp数据包到广播地址
            socket.send(packet);
            socket.close();
        } catch (IOException e) {
            LogUtils.e(e.getMessage());
        }
    }

    /**
     * 转二进制
     *
     * @param content
     * @return
     */
    private String toBinary(String content) {
        String binaryString = "";
        String[] ipSplit = content.split("\\.");
        for (String split : ipSplit) {
            String s = Integer.toBinaryString(Integer.valueOf(split));
            int length = s.length();
            for (int i = length; i < 8; i++) {
                s = "0" + s;
            }
            binaryString = binaryString + s;
        }
        return binaryString;
    }

    /**
     * 得到广播地址的二进制码
     *
     * @param ipBinary
     * @param subnetBinary
     * @return
     */
    private String getBroadcastBinary(String ipBinary, String subnetBinary) {
        int i = subnetBinary.lastIndexOf('1');
        String broadcastIPBinary = ipBinary.substring(0, i + 1);
        for (int j = broadcastIPBinary.length(); j < 32; j++) {
            broadcastIPBinary = broadcastIPBinary + "1";
        }
        return broadcastIPBinary;
    }

    /**
     * 按8位分割二进制字符串
     *
     * @param broadcastBinary
     * @return
     */
    private String spiltBinary(String broadcastBinary) {
        StringBuilder stringBuilder = new StringBuilder(40);
        char[] chars = broadcastBinary.toCharArray();
        int count = 0;
        for (int j = 0; j < chars.length; j++) {
            if (count == 8) {
                stringBuilder.append(".");
                count = 0;
            }
            stringBuilder.append(chars[j]);
            count++;
        }
        return stringBuilder.toString();
    }

    /**
     * 二进制的ip字符串转十进制
     *
     * @param wholeBroadcastBinary
     * @return
     */
    private String binaryToDecimal(String wholeBroadcastBinary) {
        String[] strings = wholeBroadcastBinary.split("\\.");
        StringBuilder sb = new StringBuilder(40);
        for (int j = 0; j < strings.length; j++) {
            String s = Integer.valueOf(strings[j], 2).toString();
            sb.append(s).append(".");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

    /***
     * 转换二进制
     * @param hexString
     * @return
     */
    private byte[] hexStr2BinArr(String hexString) {
        String hexStr = "0123456789ABCDEF";
        int len = hexString.length() / 2;
        byte[] bytes = new byte[len];
        byte high, low;
        for (int i = 0; i < len; i++) {
            high = (byte) ((hexStr.indexOf(hexString.charAt(2 * i))) << 4);
            low = (byte) hexStr.indexOf(hexString.charAt(2 * i + 1));
            bytes[i] = (byte) (high | low);
        }
        return bytes;
    }


    /**
     * 关机指令
     * 可接收执行CMD命令
     *
     * @param ip
     * @param port
     */
    public void shutdown(String ip, int port) {
        sendTCP(ip, port, "cmd=shutdown -s -t 0");
    }

    /**
     * 重启指令
     *
     * @param ip
     */
    public void restart(String ip, int port) {
        sendTCP(ip, port, "cmd=shutdown -r -t 0");
    }


    /**
     * 音量增加
     */
    public void voiceAdd(String ip, int port) {
        sendTCP(ip, port, "voice=add 10");
    }

    /**
     * 音量减小
     */
    public void voiceMinus(String ip, int port) {
        sendTCP(ip, port, "voice=minus 10");
    }

    /**
     * 按键
     *
     * @param keys
     */
    public void sendKeys(String ip, int port, String keys) {
        sendTCP(ip, port, "keys=" + keys);
    }

    /**
     * 发送串口
     *
     * @param ip
     * @param bean
     */
    public void sendSerial(final String ip, int port, SerialBean bean) {
        sendTCP(ip, port, "serial=" + new Gson().toJson(bean));
    }

    /**
     * 发送阻塞串口
     *
     * @param ip
     * @param bean
     */
    public void sendSerial(final String ip, int port, SerialBean bean, CountDownLatch latch) {
        sendTCP(ip, port, "serial=" + new Gson().toJson(bean), latch);
    }
}
