package response.upload;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import response.cloud.FileInfoResp;

/**
 * author       : frog
 * time         : 2019/6/13 上午10:02
 * desc         :
 * version      : 1.3.0
 */
public class FileUploadResp {

    @SerializedName("file_slice")
    private List<String> fileSlice;
    @SerializedName("file_info")
    private FileInfoResp.FileItem fileInfo;

    public List<String> getFileSlice() {
        return fileSlice;
    }

    public void setFileSlice(List<String> fileSlice) {
        this.fileSlice = fileSlice;
    }

    public FileInfoResp.FileItem getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfoResp.FileItem fileInfo) {
        this.fileInfo = fileInfo;
    }
}
