package mvp;

import com.bit.presenter.BasePresenter;
import com.bit.view.IView;
import com.bit.view.activity.BitBaseActivity;
import com.bit.view.fragment.BitBaseFragment;
import com.tysq.lib_creator_mvp.utils.ClzNameHelper;
import com.tysq.lib_creator_mvp.utils.MVPFactory;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;

import java.io.IOException;

/**
 * author       : frog
 * time         : 2019/4/11 下午3:27
 * desc         : mvp 自动创建
 * version      : 1.3.0
 */

public class MVPCreator {

    // 存放的根路径
    private static final String path = "app/src/main/java/";
    // 包名
    private static final String pkgName = "com.tysq.ty_android.feature";

    public static void main(String[] args) throws IOException {

        // 模块名字（改这里）
        String preFixName = "TopArticleList";
        // 是否为fragment true：为fragment；false：为activity
        boolean isFragment = true;

        ClzNameHelper.getInstance()
                .putClass(ClzNameHelper.VIEW, IView.class)
                .putClass(ClzNameHelper.PRESENTER, BasePresenter.class)
                .putClass(ClzNameHelper.FRAGMENT, BitBaseFragment.class)
                .putClass(ClzNameHelper.ACTIVITY, BitBaseActivity.class)
                .putClass(ClzNameHelper.SCOPE, ActivityScope.class)
                .putClass(ClzNameHelper.COMPONENT, AppComponent.class)
                .setAppClass(TyApplication.class);

        new MVPFactory(preFixName,
                path,
                pkgName,
                isFragment).launch();

    }

}