package factory;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.utils.DateUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.utils.TyUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.FileModel;
import thread.FileRunnable;
import utils.UploadConfig;

/**
 * author       : frog
 * time         : 2019/6/14 上午11:43
 * desc         :
 * version      : 1.3.0
 */
public class UploadFactory {

    private volatile static JerryUpload instance;

    public static JerryUpload getInstance() {
        if (instance == null) {
            synchronized (JerryUpload.class) {
                if (instance == null) {
                    instance = new JerryUpload();
                }
            }
        }

        return instance;
    }

    public static FileModel submitTask(String fileUrl) {
        return getInstance().submitTask(fileUrl);
    }

    public static void removeModel(FileModel fileModel) {
        getInstance().stopTask(fileModel);
    }

    public static List<FileModel> getUploadData() {
        return new ArrayList<>(getInstance().mFileMap.keySet());
    }

    static class JerryUpload {

        private final ExecutorService mThreadPool;
        private final LinkedHashMap<FileModel, FileRunnable> mFileMap;

        JerryUpload() {
            mThreadPool = Executors.newFixedThreadPool(UploadConfig.UPLOAD_THREAD_COUNT);
            mFileMap = new LinkedHashMap<>();
        }

        synchronized FileModel submitTask(String fileUrl) {

            Set<FileModel> fileModelList = mFileMap.keySet();
            for (FileModel fileModel : fileModelList) {
                if (fileModel.getUrl().equals(fileUrl)) {
                    ToastUtils.show(UploadConfig.CONTEXT.getString(R.string.upload_task_exist));
                    return null;
                }
            }

            File file = new File(fileUrl);
            Integer iconId = TyUtils.CLOUD_TYPE_IMG.get(TyUtils.clipTheType(file.getName()));
            if (iconId == null) {
                iconId = TyUtils.CLOUD_TYPE_IMG.get(TyUtils.OTHER);
            }

            FileModel fileModel = new FileModel(file.getName(),
                    file.getAbsolutePath(),
                    file.length(),
                    DateUtils.getCurTime(),
                    iconId);

            // 文件不存在
            if (!file.exists()) {
                fileModel.setError(true);
                fileModel.setErrorMsg(UploadConfig.CONTEXT.getString(R.string.upload_file_not_exist));
            }

            FileRunnable fileRunnable = new FileRunnable(fileModel);
            mFileMap.put(fileModel, fileRunnable);

            mThreadPool.submit(fileRunnable);

            return fileModel;

        }

        /**
         * 删除上传任务
         *
         * @param fileModel
         */
        synchronized void stopTask(FileModel fileModel) {
            fileModel.setListener(null);

            FileModel removeModel = null;

            for (FileModel model : mFileMap.keySet()) {
                if (model.getUrl().equals(fileModel.getUrl())) {
                    removeModel = model;
                    break;
                }
            }

            if (removeModel == null) {
                return;
            }

            FileRunnable fileRunnable = mFileMap.get(removeModel);
            fileRunnable.stop();

            mFileMap.remove(removeModel);
        }

    }

}
