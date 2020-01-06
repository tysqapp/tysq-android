package vo.cloud;

import java.util.List;

import response.cloud.FileInfoResp;

/**
 * author       : frog
 * time         : 2019/5/14 下午6:24
 * desc         :
 * version      : 1.3.0
 */
public class CloudFileVO {

    private String from;
    private int type;
    private List<FileInfoResp.FileItem> fileList;

    public CloudFileVO(String from, int type, List<FileInfoResp.FileItem> fileList) {
        this.from = from;
        this.type = type;
        this.fileList = fileList;
    }

    public String getFrom() {
        return from;
    }

    public int getType() {
        return type;
    }

    public List<FileInfoResp.FileItem> getFileList() {
        return fileList;
    }

    @Override
    public String toString() {
        return "CloudFileVO{" +
                "type=" + type +
                ", fileList=" + fileList +
                '}';
    }
}
