package model;

import response.cloud.FileInfoResp;

/**
 * author       : frog
 * time         : 2019/6/16 下午2:54
 * desc         : 上传更新事件
 * version      : 1.3.0
 */
public class UploadUpdateEvent {

    private final FileInfoResp.FileItem fileItem;
    private final FileModel fileModel;

    public UploadUpdateEvent(FileInfoResp.FileItem fileItem,
                             FileModel fileModel) {
        this.fileItem = fileItem;
        this.fileModel = fileModel;
    }

    public FileInfoResp.FileItem getFileItem() {
        return fileItem;
    }

    public FileModel getFileModel() {
        return fileModel;
    }
}
