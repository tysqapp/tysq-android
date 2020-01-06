package eventbus;
/**
 * author       : liaozhenlin
 * time         : 2019/11/13 16:25
 * desc         : 置顶文章
 * version      : 1.5.0
 */
public class TopArticlePositionEvent {
    private int position;

    public TopArticlePositionEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
