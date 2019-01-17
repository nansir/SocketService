package com.sir.service.soket;

import com.google.gson.Gson;
import com.sir.service.serial.SerialBean;
import com.sir.service.serial.SerialService;
import com.sir.service.uitls.LogUtils;
import com.sir.service.voice.SysVoice;
import gnu.io.SerialPort;

import java.io.IOException;

/**
 * 处理器
 * Created by zhuyinan on 2018/12/20.
 */
public class SocketProcessor {

    /**
     * 执行CMD命令
     *
     * @param s
     * @return
     */
    public static String exeCmd(String s) {
        try {
            LogUtils.i("执行CMD命令:" + s);
            Runtime ec = Runtime.getRuntime();
            ec.exec(s);
            return "OK";
        } catch (IOException e) {
            return "Invalid instruction";
        }
    }

    /**
     * 执行串口命令
     *
     * @param json
     * @return
     */
    public static String exeSerial(String json) {
        try {
            LogUtils.i("执行串口命令:" + json);
            //解析数据
            SerialBean bean = new Gson().fromJson(json, SerialBean.class);
            //开启一个串口
            SerialPort serialPort = SerialService.getInstance().openPort(bean.getName(), bean.getRate());
            //往串口发送数据
            SerialService.getInstance().sendToPort(serialPort, bean.getOrder());
            //关闭端口
            SerialService.getInstance().closePort(serialPort);
            return "ok";
        } catch (Exception e) {
            return e.toString();
        }
    }

    /**
     * 系统声音
     *
     * @param voice
     * @return
     */
    public static String exeVoice(String voice) {
        try {
            LogUtils.i("执行声音命令:" + voice);
            String order[] = voice.split(" ");
            if (order.length == 2) {
                if ("add".equals(order[0])) {
                    SysVoice.add(order[1]);
                } else if ("minus".equals(order[0])) {
                    SysVoice.minus(order[1]);
                }
            } else {
                if ("mute".equals(voice)) {
                    SysVoice.mute();
                } else if ("add".equals(voice)) {
                    SysVoice.add();
                } else if ("minus".equals(voice)) {
                    SysVoice.minus();
                }
            }
            return "ok";
        } catch (Exception e) {
            return e.toString();
        }
    }
}
