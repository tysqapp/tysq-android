package listener;

/**
 * author       : frog
 * time         : 2019/6/14 下午3:01
 * desc         : 分片上传的监听器
 * version      : 1.3.0
 */

public interface SliceListener {

    void onTransferred(long size);

}