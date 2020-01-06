package eventbus;

/**
 * author       : frog
 * time         : 2019-08-28 16:04
 * desc         : 消息更新
 * version      : 1.0.0
 */
public class NotificationUpdateEvent {

    int count;

    public NotificationUpdateEvent(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
