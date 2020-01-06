package request;

import com.google.gson.annotations.SerializedName;

/**
 * author       : liaozhenlin
 * time         : 2019/11/11 15:13
 * desc         : 置顶文章
 * version      : 1.5.0
 */
public class TopArticleReq {
    @SerializedName("top_position")
    private int topPosition;

    public TopArticleReq(int topPosition) {
        this.topPosition = topPosition;
    }

    public int getTopPosition() {
        return topPosition;
    }

    public void setTopPosition(int topPosition) {
        this.topPosition = topPosition;
    }
}
