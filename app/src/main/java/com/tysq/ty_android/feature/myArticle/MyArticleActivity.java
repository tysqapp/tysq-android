//package com.tysq.ty_android.feature.myArticle;
//
//import android.content.Context;
//import android.content.Intent;
//import android.support.v4.app.Fragment;
//
//import com.tysq.ty_android.R;
//import com.tysq.ty_android.base.activity.CommonBarActivity;
//import com.tysq.ty_android.local.sp.UserCache;
//
///**
// * author       : frog
// * time         : 2019/5/27 下午12:00
// * desc         : 我的文章
// * version      : 1.3.0
// */
//public class MyArticleActivity extends CommonBarActivity {
//
//    public static void startActivity(Context context) {
//        Intent intent = new Intent(context, MyArticleActivity.class);
//
//        context.startActivity(intent);
//    }
//
//    @Override
//    protected int getPageTitle() {
//        return R.string.my_article_title;
//    }
//
//    @Override
//    protected Fragment getContentFragment() {
//        return MyArticleFragment
//                .newInstance(UserCache.getDefault().getAccountId(),
//                        true,
//                        true,
//                        true);
//    }
//}
