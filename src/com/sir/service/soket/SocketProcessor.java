package com.sir.service.soket;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sir.service.serial.DeviceBean;
import com.sir.service.serial.SerialBean;
import com.sir.service.serial.SerialService;
import com.sir.service.sys.SysKeys;
import com.sir.service.sys.SysTask;
import com.sir.service.sys.SysVoice;
import com.sir.service.uitls.Key;
import com.sir.service.uitls.LogUtils;
import com.sir.service.uitls.MyUtils;
import gnu.io.SerialPort;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            return Key.OK;
        } catch (IOException e) {
            LogUtils.e(e.getMessage());
        }
        return Key.NO;
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
            return Key.OK;
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
        return Key.NO;
    }

    /**
     * 系统声音
     *
     * @param voice
     * @return
     */
    public static String exeVol(String voice) {
        try {
            LogUtils.i("执行声音命令:" + voice);
            String order[] = voice.split(" ");
            if (order.length == 2) {
                if (Key.ADD.equals(order[0])) {
                    SysVoice.add(order[1]);
                } else if (Key.MINUS.equals(order[0])) {
                    SysVoice.minus(order[1]);
                } else {
                    return "parameter error";
                }
            } else {
                if (Key.MUTE.equals(voice)) {
                    SysVoice.mute();
                } else if (Key.ADD.equals(voice)) {
                    SysVoice.add();
                } else if (Key.MINUS.equals(voice)) {
                    SysVoice.minus();
                } else {
                    return "parameter error";
                }
            }
            return Key.OK;
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
        return Key.NO;
    }

    /**
     * 命令
     *
     * @param keys
     * @return
     */
    public static String exeKeys(String keys) {
        try {
            LogUtils.i("执行按键:" + keys);
            SysKeys.execute(keys);
            return Key.OK;
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
        return Key.NO;
    }


    /**
     * 任务
     *
     * @param keys
     * @return
     */
    public static String exeTask(String keys) {
        try {
            LogUtils.i("执行任务:" + keys);

            String order[] = keys.split(" ");

            if (order.length != 2) {
                return "parameter error";
            }

            String json = MyUtils.getJson("SmartDevice");
            Type type = new TypeToken<Map<String, List<DeviceBean>>>() {}.getType();
            Map<String, List<DeviceBean>> map = new Gson().fromJson(json, type);
            List<DeviceBean> list = map.get(order[1]);
            if (list == null || list.size() == 0) {
                return "non-task";
            }

            if ("check".equals(order[0])) {
                SysTask.getInstance().taskCheck(list);
                HashMap<String, Boolean> hashMap = new HashMap<>();
                for (DeviceBean bean : list) {
                    hashMap.put(bean.getIp(), bean.isOnLine());
                }
                return new Gson().toJson(hashMap);
            } else if ("no".equals(order[0])) {
                SysTask.getInstance().taskOpen(list);
            } else if ("nc".equals(order[0])) {
                SysTask.getInstance().taskClose(list);
            } else {
                return "parameter error";
            }
            return Key.OK;
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
        return Key.NO;
    }
}
