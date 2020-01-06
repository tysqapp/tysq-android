package com.tysq.ty_android.feature.homePageSearch;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.homePageSearch.config.HomePageSearchConstants;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;
import com.tysq.ty_android.utils.DBManager.DBLocalSearchManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import db.LocalLabel;
import response.LabelResp;
import response.search.SearchResultResp;
import vo.search.HomePageSearchVO;

public final class HomePageSearchPresenter extends BasePresenter<HomePageSearchView> {

    private boolean isRefresh = false;
    private boolean isNeedAdvertisement = false;
    @Inject
    public HomePageSearchPresenter(HomePageSearchView view) {
      super(view);
    }

    /**
     * 第一次进入搜索页面
     */
    public void onFirstLoad(){
        List<HomePageSearchVO> resultList = new ArrayList<>();
        List<LocalLabel> labelList = DBLocalSearchManager.queryHistoryList();
        resultList.add(new HomePageSearchVO<>(HomePageSearchConstants.HISTORY, labelList));
        mView.onFirstLoad(resultList);
    }

    public void handleTheResult(String label, String content, boolean isFirst, int size, int start){
        RetrofitFactory
                .getApiService()
                .getSearchResult(label, content, size, start)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<SearchResultResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {

                    }

                    @Override
                    protected void onSuccessRes(@NonNull SearchResultResp value) {
                        List<HomePageSearchVO> resultList = new ArrayList<>();
                        resultList.clear();
                        //如果是第一次
                        if (isFirst){
                            firstGetSearchData(label, resultList, value, content);
                            mView.onLoadHomePage(resultList, isRefresh, isNeedAdvertisement, value.getCount());
                        }else{
                            getSearchListResult(label, resultList, value, content);
                            mView.onLoadMoreData(resultList);
                        }
                    }
                });
    }

    /**
     * 第一次访问数据
     * @param resultList
     * @param value
     * @param content
     */
    private void firstGetSearchData(String label,
                                    List<HomePageSearchVO> resultList,
                                    SearchResultResp value,
                                    String content){

        if ((value.getArticlesList() == null || value.getArticlesList().size() <= 0)
                &&(value.getUsersList() == null || value.getUsersList().size() <= 0)
                || content.isEmpty()){

            resultList.add(new HomePageSearchVO<>(HomePageSearchConstants.NODATA, new Object()));
            List<LocalLabel> labelList = DBLocalSearchManager.queryHistoryList();
            resultList.add(new HomePageSearchVO<>(HomePageSearchConstants.HISTORY, labelList));
            isRefresh = false;
            isNeedAdvertisement = true;
        }else{
            resultList.add(new HomePageSearchVO<>(HomePageSearchConstants.TITLE, new Object()));
            getSearchListResult(label, resultList, value, content);
            isRefresh = true;
            isNeedAdvertisement = false;
        }
    }

    /**
     * 当数据不为空时读取数据
     * @param label
     * @param resultList
     * @param value
     * @param content
     */
    private void getSearchListResult(String label,
                                     List<HomePageSearchVO> resultList,
                                     SearchResultResp value,
                                     String content){

        switch (label){
            case Constant.HomePageSearchType.TYPE_ARTICLE:
                for (int i = 0; i < value.getArticlesList().size(); i++){
                    resultList.add(new HomePageSearchVO<>(
                            HomePageSearchConstants.ARTICLE,
                            value.getArticlesList().get(i)));
                }
                break;

            case Constant.HomePageSearchType.TYPE_TAG:
                for (int i = 0; i < value.getArticlesList().size(); i++){
                    if (value.getArticlesList().get(i).getLabels() != null ||
                            value.getArticlesList().get(i).getLabels().size() > 0){
                        changeLabelPosition(value.getArticlesList().get(i).getLabels(), content);
                    }
                    resultList.add(new HomePageSearchVO<>(
                            HomePageSearchConstants.LABEL,
                            value.getArticlesList().get(i)));
                }
                break;
            case Constant.HomePageSearchType.TYPE_ADMIN:
                for (int i = 0; i < value.getUsersList().size(); i++){
                    resultList.add(new HomePageSearchVO<>(
                            HomePageSearchConstants.ADMIN,
                            value.getUsersList().get(i)));

                }
                break;
        }
    }

    /**
     * 将含有搜索内容的标签移到最前面
     * @param labels
     * @param content
     */
    private void changeLabelPosition(List<String> labels, String content){
        for (int i = 0; i < labels.size(); i++){
            if (labels.get(i).contains(content)){
                String label = labels.get(i);
                labels.remove(i);
                labels.add(0,label);
            }
        }

        /*Iterator<String> iterator = labels.iterator();
        while (iterator.hasNext()){
            String label = iterator.next();
            if (label.contains(content)){
                String newLabel = label;
                iterator.remove();
            }
        }*/
    }


}
