package listener;

import model.SliceModel;
import thread.SliceThread;

/**
 * author       : frog
 * time         : 2019/6/14 上午11:27
 * desc         : 文件线程回调
 * version      : 1.3.0
 */
public interface FileListener {

    // 分片线程停止回调
    void onExist(SliceThread thread, boolean isStop);

    // 分片任务完成回调
    void onFinishTask(SliceThread thread,
                      SliceModel sliceModel);

    // 线程异常回调
    void onError(SliceThread thread);

}
