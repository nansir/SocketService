package com.sir.service.soket;

import com.google.gson.Gson;
import com.sir.service.serial.SerialBean;
import com.sir.service.serial.SerialService;
import com.sir.service.uitls.LogUtils;
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

    public static String exeVoice(String s) {
        try {
            LogUtils.i("执行声音命令:" + s);


            return "ok";
        } catch (Exception e) {
            return e.toString();
        }
    }
}
