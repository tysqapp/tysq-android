package com.tysq.ty_android.feature.cloud.cloudDownload;

import com.abc.lib_multi_download.jerry.JerryDownload;
import com.abc.lib_multi_download.model.DownloadInfo;
import com.abc.lib_multi_download.model.status.RunningStatus;
import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.local.sp.NetCache;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.net.rx.RxObservableSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import vo.cloud.CloudDownloadVO;

public final class CloudDownloadPresenter extends BasePresenter<CloudDownloadView> {
    @Inject
    public CloudDownloadPresenter(CloudDownloadView view) {
        super(view);
    }

    public void getDownloadInfo() {

        Observable.just(true)
                .map(new Function<Boolean, List<Object>>() {
                    @Override
                    public List<Object> apply(Boolean aBoolean) throws Exception {

                        int accountId = UserCache.getDefault().getAccountId();

                        List<DownloadInfo> downloadInfoList
                                = JerryDownload.getInstance()
                                .getDownloadList(accountId, NetCache.getDefault().getDomain());

                        int size = downloadInfoList.size();

                        // 结果列表
                        List<CloudDownloadVO> downloadResult = new ArrayList<>();
                        // 完成列表
                        List<CloudDownloadVO> finishList = new ArrayList<>();

                        for (DownloadInfo downloadInfo : downloadInfoList) {

                            CloudDownloadVO cloudDownloadVO
                                    = new CloudDownloadVO(CloudDownloadVO.TYPE.CONTENT);
                            cloudDownloadVO.setDownloadInfo(downloadInfo);

                            if (downloadInfo.getRunningStatus() == RunningStatus.SUCCESS) {
                                finishList.add(cloudDownloadVO);
                            } else {
                                downloadResult.add(cloudDownloadVO);
                            }

                        }

                        if (finishList.size() > 0) {
                            CloudDownloadVO titleVO
                                    = new CloudDownloadVO(CloudDownloadVO.TYPE.HEAD);
                            titleVO.setSize(finishList.size());
                            downloadResult.add(titleVO);

                            downloadResult.addAll(finishList);
                        }

                        List<Object> result = new ArrayList<>(2);
                        result.add(size);
                        result.add(downloadResult);

                        return result;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<Object>>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onLoadError();
                    }

                    @Override
                    protected void onSuccessRes(List<Object> value) {
                        int size = (int) value.get(0);
                        List<CloudDownloadVO> cloudDownloadVOList = (List<CloudDownloadVO>) value.get(1);

                        mView.onLoadInfo(cloudDownloadVOList, size);
                    }
                });

    }

}
