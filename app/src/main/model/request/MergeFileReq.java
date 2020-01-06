package request;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019/6/16 下午3:48
 * desc         :
 * version      : 1.3.0
 */
public class MergeFileReq {

    /**
     * total_chunks : 20
     * hash : 4d9dae9bb2c76f9b34c699e39f74cdc4c8a247eb6e0f9af5f0e1ed816bb57595
     */

    @SerializedName("total_chunks")
    private int totalChunks;
    @SerializedName("hash")
    private String hash;

    public MergeFileReq(int totalChunks, String hash) {
        this.totalChunks = totalChunks;
        this.hash = hash;
    }

    public int getTotalChunks() {
        return totalChunks;
    }

    public void setTotalChunks(int totalChunks) {
        this.totalChunks = totalChunks;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
