package request;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019/6/5 下午6:17
 * desc         : 删除
 * version      : 1.3.0
 */
public class CloudDeleteReq {

    @SerializedName("file_id")
    private int fileId;

    public CloudDeleteReq(int fileId) {
        this.fileId = fileId;
    }

    public int getFileId() {
        return fileId;
    }
}
