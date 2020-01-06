package eventbus;

/**
 * author       : frog
 * time         : 2019/6/6 上午11:25
 * desc         : 视频封面更换
 * version      : 1.3.0
 */
public class VideoCoverChangeEvent {

    private String coverUrl;
    private int coverId;
    private int fileId;

    public VideoCoverChangeEvent(String coverUrl, int coverId, int fileId) {
        this.coverUrl = coverUrl;
        this.coverId = coverId;
        this.fileId = fileId;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public int getCoverId() {
        return coverId;
    }

    public int getFileId() {
        return fileId;
    }

}
