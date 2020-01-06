package eventbus;

import model.FileModel;

/**
 * author       : frog
 * time         : 2019/6/17 下午4:01
 * desc         : 提交上传文件事件
 * version      : 1.3.0
 */
public class UploadAddTaskEvent {

    private FileModel fileModel;

    public UploadAddTaskEvent(FileModel fileModel) {
        this.fileModel = fileModel;
    }

    public FileModel getFileModel() {
        return fileModel;
    }
}
