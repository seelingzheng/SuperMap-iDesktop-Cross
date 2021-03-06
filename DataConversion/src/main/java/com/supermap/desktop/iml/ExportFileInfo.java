package com.supermap.desktop.iml;

import com.supermap.data.conversion.ExportSetting;
import com.supermap.data.conversion.FileType;

/**
 * Created by xie on 2016/10/27.
 * 导出自定义类，包括一个导出类型类
 * 和自定义的状态字段
 */
public class ExportFileInfo {

    private ExportSetting exportSetting;

    private FileType fileType;

    private String filePath;

    public ExportFileInfo() {

    }

    public ExportSetting getExportSetting() {
        return exportSetting;
    }

    public void setExportSetting(ExportSetting exportSetting) {
        this.exportSetting = exportSetting;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

