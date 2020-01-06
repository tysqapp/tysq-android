package response.upload;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import response.cloud.FileInfoResp;

/**
 * author       : frog
 * time         : 2019/6/13 上午10:02
 * desc         : 查询文件是否存在
 * version      : 1.3.0
 */
public class FileInfoCheckResp {

    @SerializedName("status")
    private int status;
    @SerializedName("file_slice")
    private List<String> fileSlice;
    @SerializedName("file_info")
    private FileInfoResp.FileItem fileInfo;
    @SerializedName("file_chunk_size")
    private long fileChunkSize;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

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

    public long getFileChunkSize() {
        return fileChunkSize;
    }

    public void setFileChunkSize(long fileChunkSize) {
        this.fileChunkSize = fileChunkSize;
    }
}
