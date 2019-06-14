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

    /**
     * 执行虚拟按键
     * @param keys
     * @throws IOException
     * @throws InterruptedException
     */
    public static void execute(String keys) throws IOException, InterruptedException {
        File tempFile = new File("temp", "Keys_" + keys + ".vbs");
        String vbsMessage = !tempFile.exists() ? "CreateObject(\"Wscript.Shell\").Sendkeys \"" + keys + "\"" : "";
        /**
         * 当vbs文件不存在时，则创建它们，应用默认编码为 utf-8 时，创建的 vbs 脚本运行时报错
         * 于是使用 OutputStreamWriter 将 vbs 文件编码改成GBK
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
        }
        Runtime runtime = Runtime.getRuntime();
        runtime.exec("wscript " + tempFile.getAbsolutePath()).waitFor();
        LogUtils.i("按键操作执行成功.");
    }
}
