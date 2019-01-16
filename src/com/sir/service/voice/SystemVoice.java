package com.sir.service.voice;

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
public class SystemVoice {

    /**
     * 控制电脑系统音量
     * volumeMute.vbs：静音
     * volumeAdd.vbs：增音量
     * volumeMinus.vbs：减音量
     *
     * @param type 0：静音/取消静音 1：增加音量 2：减小音量
     */
    public static void controlVoice(String type) {
        try {
            if (type == null || "".equals(type.trim())) {
                LogUtils.i("type 参数为空,不进行操作...");
            }
            /**tempFile：vbs 文件 vbsMessage：vbs 文件的内容*/
            String vbsMessage;
            File tempFile;
            Runtime runtime = Runtime.getRuntime();
            switch (type) {
                case "0":
                    tempFile = new File("temp", "volumeMute.vbs");
                    vbsMessage = !tempFile.exists() ? "CreateObject(\"Wscript.Shell\").Sendkeys \"棴\"" : "";
                    break;
                case "1":
                    tempFile = new File("temp", "volumeAdd.vbs");
                    vbsMessage = !tempFile.exists() ? "CreateObject(\"Wscript.Shell\").Sendkeys \"棷\"" : "";
                    break;
                case "2":
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
                LogUtils.i("vbs 文件不存在,新建成功：" + tempFile.getAbsolutePath());
            }
            runtime.exec("wscript " + tempFile.getAbsolutePath()).waitFor();
            LogUtils.i("音量控制完成.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws InterruptedException {

        Thread.sleep(1000);
        controlVoice("0");

//        logger.info("1 秒后开始取消静音.");
//        Thread.sleep(1000);
//        controlVoice("0");
//
//        logger.info("1 秒后开始增加 2 格音量，可以使用循环，持续增加音量.");
//        Thread.sleep(1000);
//        controlVoice("1");
//
//        logger.info("1 秒后开始减小音量，可以使用循环持续减小.");
//        Thread.sleep(1000);
//        for (int i = 0; i < 3; i++) {
//            controlVoice("2");
//            Thread.sleep(500);
//        }

    }
}
