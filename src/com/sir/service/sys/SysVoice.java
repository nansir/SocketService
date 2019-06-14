package com.sir.service.sys;

import com.sir.service.uitls.LogUtils;

import java.io.*;

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
    private static void controlVoice(Voice type) throws IOException, InterruptedException {
        String vbsMessage;
        File tempFile;
        switch (type) {
            case MUTE:
                tempFile = new File("temp", "Voice_Mute.vbs");
                vbsMessage = !tempFile.exists() ? "CreateObject(\"Wscript.Shell\").Sendkeys \"棴\"" : "";
                break;
            case ADD:
                tempFile = new File("temp", "Voice_Add.vbs");
                vbsMessage = !tempFile.exists() ? "CreateObject(\"Wscript.Shell\").Sendkeys \"棷\"" : "";
                break;
            case MINUS:
                tempFile = new File("temp", "Voice_Minus.vbs");
                vbsMessage = !tempFile.exists() ? "CreateObject(\"Wscript.Shell\").Sendkeys \"棶\"" : "";
                break;
            default:
                return;
        }
        /**
         * 当vbs文件不存在时，则创建它们，应用默认编码为 utf-8 时，创建的 vbs 脚本运行时报错
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
            LogUtils.i("vbs文件不存在,新建：" + tempFile.getAbsolutePath());
        }
        Runtime runtime = Runtime.getRuntime();
        runtime.exec("wscript " + tempFile.getAbsolutePath()).waitFor();
        LogUtils.i("音量控制执行成功.");
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
    public static void mute() throws IOException, InterruptedException {
        controlVoice(Voice.MUTE);
    }

    /**
     * 增加音量
     */
    public static void add() throws IOException, InterruptedException {
        controlVoice(Voice.ADD);
    }

    /**
     * 增加音量
     */
    public static void add(String volume) throws InterruptedException, IOException {
        int vol = Integer.parseInt(volume);
        for (int i = 0; i < vol / 2; i++) {
            controlVoice(Voice.ADD);
            Thread.sleep(200);
        }
    }

    /**
     * 减小音量
     */
    public static void minus() throws IOException, InterruptedException {
        controlVoice(Voice.MINUS);
    }

    /**
     * 减小音量
     *
     * @param volume 10,20,
     */
    public static void minus(String volume) throws InterruptedException, IOException {
        int vol = Integer.parseInt(volume);
        for (int i = 0; i < vol / 2; i++) {
            controlVoice(Voice.MINUS);
            Thread.sleep(200);
        }
    }
}
