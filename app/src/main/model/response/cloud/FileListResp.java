package response.cloud;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : frog
 * time         : 2019/6/5 下午2:46
 * desc         : 云盘模型
 * version      : 1.3.0
 */
public class FileListResp {

    /**
     * file_info : [{"id":392,"account_id":1,"filename":"吃、购、行.xls","type":"other","covers":null,"screenshots":null,"duration":0,"create_at":1559717690,"size":2915840,"hash":"012747aa467badaa52aaca4db4c032decad38c4a8907d6db41a7f900927606be","url":"http://192.168.0.33:8081/v1.0/file/redirect/392"}]
     * total_number : 7
     */

    @SerializedName("total_number")
    private int totalNumber;
    @SerializedName("file_info")
    private List<FileInfoResp.FileItem> fileInfo;

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public List<FileInfoResp.FileItem> getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(List<FileInfoResp.FileItem> fileInfo) {
        this.fileInfo = fileInfo;
    }

}
