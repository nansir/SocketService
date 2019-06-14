package com.sir.service.sys;

import com.sir.service.manage.ControlComputer;
import com.sir.service.serial.DeviceBean;
import com.sir.service.uitls.LogUtils;
import com.sir.service.uitls.MyUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhuyinan on 2019/6/14.
 */
public class SysTask {

    private static SysTask mTask = null;

    ExecutorService mPool = Executors.newFixedThreadPool(6);

    ControlComputer computer;

    private SysTask() {
        computer = new ControlComputer();
    }

    public static SysTask getInstance() {
        if (mTask == null) {
            mTask = new SysTask();
        }
        return mTask;
    }


    /**
     * 任务开放
     *
     * @param devices
     */
    public void taskOpen(List<DeviceBean> devices) {
        Collections.sort(devices, Comparator.comparingInt(DeviceBean::getWtNo));
        Collections.reverse(devices);
        mPool.execute(() -> {
            for (DeviceBean bean : devices) {
                try {
                    if (bean.getType() == 0) { //电源
                        computer.sendTCP(bean.getIp(), bean.getPort(), bean.getProNo());
                        Thread.sleep(5000);
                    } else if (bean.getType() == 1) {//电脑
                        computer.wakeUpDevice(bean.getIp(), bean.getMac());
                        Thread.sleep(bean.getSleep());
                    } else if (bean.getType() == 2) {//非电脑
                        computer.sendTCP(bean.getIp(), bean.getPort(), bean.getProNo());
                        Thread.sleep(bean.getSleep());
                    }
                } catch (InterruptedException e) {
                    LogUtils.e(e.getMessage());
                }
            }
        });
    }

    /**
     * 任务结束
     *
     * @param devices
     */
    public void taskClose(List<DeviceBean> devices) {
        //关闭任务 根据设备权重
        Collections.sort(devices, Comparator.comparingInt(DeviceBean::getWtNc));
        Collections.reverse(devices);
        mPool.execute(() -> {
            for (DeviceBean bean : devices) {
                try {
                    if (bean.getType() == 0) {//电源
                        Thread.sleep(bean.getSleep());
                        computer.sendTCP(bean.getIp(), bean.getPort(), bean.getProNc());
                    } else if (bean.getType() == 1) {//电脑
                        computer.shutdown(bean.getIp(), bean.getPort());
                        Thread.sleep(bean.getSleep());
                    } else if (bean.getType() == 2) {//非电脑
                        computer.sendTCP(bean.getIp(), bean.getPort(), bean.getProNc());
                        Thread.sleep(bean.getSleep());
                    }
                } catch (InterruptedException e) {
                    LogUtils.e(e.getMessage());
                }
            }
        });
    }

    /**
     * 检查任务
     *
     * @param devices
     */
    public void taskCheck(List<DeviceBean> devices) {
        CountDownLatch latch = new CountDownLatch(devices.size());
        for (int i = 0; i < devices.size(); i++) {
            int finalI = i;
            DeviceBean bean = devices.get(finalI);
            mPool.execute(() -> {
                boolean online = MyUtils.ping(bean.getIp());
                devices.get(finalI).setOnLine(online);
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            LogUtils.e(e.getMessage());
        }
    }
}
