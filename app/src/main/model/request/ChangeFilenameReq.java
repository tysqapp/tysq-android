package request;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019-10-29 18:17
 * desc         :
 * version      :
 */
public class ChangeFilenameReq {

    @SerializedName("file_name")
    private String filename;

    public ChangeFilenameReq(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
