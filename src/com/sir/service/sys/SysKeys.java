package com.sir.service.sys;

import com.sir.service.uitls.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 系统按键
 * Created by zhuyinan on 2019/3/13.
 */
public class SysKeys {

    private static void controlKeys(Keys value) {
        try {
            String vbsMessage;
            File tempFile;
            Runtime runtime = Runtime.getRuntime();
            switch (value) {
                case F1:
                    tempFile = new File("temp", "KeysF1.vbs");
                    vbsMessage = !tempFile.exists() ? "CreateObject(\"Wscript.Shell\").Sendkeys \"{F1}\"" : "";
                    break;
                case F2:
                    tempFile = new File("temp", "KeysF2.vbs");
                    vbsMessage = !tempFile.exists() ? "CreateObject(\"Wscript.Shell\").Sendkeys \"{F2}\"" : "";
                    break;
                case F5:
                    tempFile = new File("temp", "KeysF5.vbs");
                    vbsMessage = !tempFile.exists() ? "CreateObject(\"Wscript.Shell\").Sendkeys \"{F5}\"" : "";
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
            LogUtils.i("按键操作执行成功.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 声音类型
     */
    public enum Keys {
        F1,
        F2,
        F5
    }

    public static void F5() {
        controlKeys(Keys.F5);
    }
}
