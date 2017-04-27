package net.myspring.general.modules.sys.form;

import java.io.OutputStream;

/**
 * Created by wangzm on 2017/4/27.
 */
public class ExcelExportForm {

    private OutputStream outputStream;
    private String fileName;

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
