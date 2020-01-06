package request;

import com.google.gson.annotations.SerializedName;

public class HideArticleReq {
    @SerializedName("status")
    private int status;

    public HideArticleReq(int status){
        this.status = status;
    }

    public int getState() {
        return status;
    }

    public void setState(int state) {
        this.status = state;
    }
}
