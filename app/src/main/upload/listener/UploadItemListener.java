package listener;

/**
 * author       : frog
 * time         : 2019/6/14 下午3:02
 * desc         :
 * version      : 1.3.0
 */
public interface UploadItemListener {

    void onProgress();

    void onError();

    void onDelete();

}
