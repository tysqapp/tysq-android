package vo.cloud;

import com.abc.lib_multi_download.listener.DownloadListener;
import com.abc.lib_multi_download.model.DownloadInfo;

/**
 * author       : frog
 * time         : 2019-10-18 10:32
 * desc         : 云盘下载
 * version      : 1.5.0
 */
public class CloudDownloadVO {

    public interface TYPE {
        int HEAD = 1;
        int CONTENT = 2;
    }

    private final int type;

    private DownloadInfo downloadInfo;

    private int size;

    private boolean isStartDownload;

    // 最后的状态
    private int lastState;

    public CloudDownloadVO(int type) {
        this.type = type;
        this.isStartDownload = false;
    }

    public int getType() {
        return type;
    }

    public DownloadInfo getDownloadInfo() {
        return downloadInfo;
    }

    public void setDownloadInfo(DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isStartDownload() {
        return isStartDownload;
    }

    public void setStartDownload(boolean startDownload) {
        isStartDownload = startDownload;
    }

    public int getLastState() {
        return lastState;
    }

    public void setLastState(int lastState) {
        this.lastState = lastState;
    }

    public void setListener(DownloadListener listener) {
        if (downloadInfo != null) {
            downloadInfo.setListener(listener);
        }
    }

    public DownloadListener getListener() {
        if (downloadInfo != null) {
            return downloadInfo.getListener();
        }
        return null;
    }
}
