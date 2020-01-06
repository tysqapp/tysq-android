package response.upload;

import com.google.gson.annotations.SerializedName;

import response.cloud.FileInfoResp;

/**
 * author       : frog
 * time         : 2019/6/16 下午3:46
 * desc         :
 * version      :
 */
public class FileMergeResp {

    @SerializedName("file_info")
    private FileInfoResp.FileItem fileInfo;

    public FileInfoResp.FileItem getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfoResp.FileItem fileInfo) {
        this.fileInfo = fileInfo;
    }
}
