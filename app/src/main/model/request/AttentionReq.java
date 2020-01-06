package request;

import com.google.gson.annotations.SerializedName;

public class AttentionReq {
    @SerializedName("attention_id")
    private int attentionId;
    @SerializedName("is_follow")
    private boolean isFollow;

    public AttentionReq(int attentionId, boolean isFollow){
        this.attentionId = attentionId;
        this.isFollow = isFollow;
    }
    public int getAttentionId() {
        return attentionId;
    }

    public void setAttentionId(int attentionId) {
        this.attentionId = attentionId;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }
}
