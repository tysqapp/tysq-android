package com.tysq.ty_android.feature.homePage;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.config.TyConfig;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxObservableSubscriber;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import response.AdResp;
import response.home.CategoryResp;
import response.home.SubCategory;
import response.home.TopCategory;

public final class HomePagePresenter extends BasePresenter<HomePageView> {

    @Inject
    public HomePagePresenter(HomePageView view) {
        super(view);
    }

    public void loadCategory(final String recommendTitle,
                             final String allTitle) {
        RetrofitFactory
                .getApiService()
                .getCategory()
                .compose(RxParser.handleObservableDataResult())
                .map(new Function<CategoryResp, CategoryResp>() {
                    @Override
                    public CategoryResp apply(CategoryResp categoryResp) throws Exception {

                        TopCategory recommendTop = new TopCategory();
                        recommendTop.setId(TyConfig.CATEGORY_RECOMMEND_ID);
                        recommendTop.setName(recommendTitle);
                        recommendTop.setSelect(true);

                        categoryResp.getCategoryInfo().add(0, recommendTop);
                        categoryResp.setTotalNum(categoryResp.getTotalNum() + 1);

                        for (TopCategory item : categoryResp.getCategoryInfo()) {

                            // 如果子分类为空，或长度为0，则不添加 "全部" 选项
                            if (item.getSubCategoryList() == null
                                    || item.getSubCategoryList().size() == 0) {
                                continue;
                            }

                            // 初始化，添加 "全部"
                            SubCategory subCategory = new SubCategory();
                            subCategory.setId(item.getId());
                            subCategory.setName(allTitle);
                            subCategory.setSelect(true);

                            List<SubCategory> subCategoryList = item.getSubCategoryList();
                            subCategoryList.add(0, subCategory);

                        }

                        return categoryResp;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<CategoryResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onLoadCategoryFailure();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull CategoryResp value) {
                        mView.onLoadCategory(value);
                    }
                });
    }

    public void loadAnnouncement() {
        RetrofitFactory
                .getApiService()
                .getAdvertisement(0, 20, Constant.AnnouncementType.DIALOG)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<AdResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        Log.i(TAG, "[code:" + code + "; message:" + message + "]");
                    }

                    @Override
                    protected void onSuccessRes(@NonNull AdResp value) {
                        mView.onLoadAnnouncement(value.getAdvertisementList());
                    }
                });
    }
}
