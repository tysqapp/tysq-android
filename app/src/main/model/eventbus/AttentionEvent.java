package eventbus;
/**
 * author       : liaozhenlin
 * time         : 2019/10/21 14:44
 * desc         : 取消/添加关注
 * version      : 1.5.0
 */
public class AttentionEvent {

    private int accountId;
    private boolean isFollow;

    public AttentionEvent(int accountId, boolean isFollow) {
        this.accountId = accountId;
        this.isFollow = isFollow;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public boolean getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(boolean isFollow) {
        this.isFollow = isFollow;
    }
}
