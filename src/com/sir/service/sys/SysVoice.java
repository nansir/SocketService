package com.sir.service.sys;

import com.sir.service.uitls.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 系统声音
 * Created by zhuyinan on 2019/1/16.
 * Contact by 445181052@qq.com
 */
public class SysVoice {

    /**
     * 控制电脑系统音量
     * volumeMute.vbs：静音
     * volumeAdd.vbs：增音量
     * volumeMinus.vbs：减音量
     *
     * @param type MUTE：静音/取消静音 ADD：增加音量 MINUS：减小音量
     */
    private static void controlVoice(Voice type) {
        try {
            String vbsMessage;
            File tempFile;
            Runtime runtime = Runtime.getRuntime();
            switch (type) {
                case MUTE:
                    tempFile = new File("temp", "volumeMute.vbs");
                    vbsMessage = !tempFile.exists() ? "CreateObject(\"Wscript.Shell\").Sendkeys \"棴\"" : "";
                    break;
                case ADD:
                    tempFile = new File("temp", "volumeAdd.vbs");
                    vbsMessage = !tempFile.exists() ? "CreateObject(\"Wscript.Shell\").Sendkeys \"棷\"" : "";
                    break;
                case MINUS:
                    tempFile = new File("temp", "volumeMinus.vbs");
                    vbsMessage = !tempFile.exists() ? "CreateObject(\"Wscript.Shell\").Sendkeys \"棶\"" : "";
                    break;
                default:
                    return;
            }
            /**
             * 当3个vbs文件不存在时，则创建它们，应用默认编码为 utf-8 时，创建的 vbs 脚本运行时报错
             * 于是使用 OutputStreamWriter 将 vbs 文件编码改成gbd就正常了
             */
            if (!tempFile.exists() && !vbsMessage.equals("")) {
                if (!tempFile.getParentFile().exists()) {
                    tempFile.getParentFile().mkdirs();
                }
                tempFile.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "GBK");
                outputStreamWriter.write(vbsMessage);
                outputStreamWriter.flush();
                outputStreamWriter.close();
                LogUtils.i("vbs文件不存在,新建成功：" + tempFile.getAbsolutePath());
            }
            runtime.exec("wscript " + tempFile.getAbsolutePath()).waitFor();
            LogUtils.i("音量控制执行成功.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 声音类型
     */
    public enum Voice {
        MUTE,
        ADD,
        MINUS
    }


    /**
     * 静音/取消静音
     */
    public static void mute() {
        controlVoice(Voice.MUTE);
    }

    /**
     * 增加音量
     */
    public static void add() {
        controlVoice(Voice.ADD);
    }

    /**
     * 增加音量
     */
    public static void add(String volume) throws InterruptedException {
        int vol = Integer.parseInt(volume);
        for (int i = 0; i < vol / 2; i++) {
            controlVoice(Voice.ADD);
            Thread.sleep(200);
        }
    }

    /**
     * 减小音量
     */
    public static void minus() {
        controlVoice(Voice.MINUS);
    }

    /**
     * 减小音量
     *
     * @param volume 10,20,
     */
    public static void minus(String volume) throws InterruptedException {
        int vol = Integer.parseInt(volume);
        for (int i = 0; i < vol / 2; i++) {
            controlVoice(Voice.MINUS);
            Thread.sleep(200);
        }
    }
}
