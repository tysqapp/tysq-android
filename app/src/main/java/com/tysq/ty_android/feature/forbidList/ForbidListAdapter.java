package com.tysq.ty_android.feature.forbidList;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.base.activity.CommonToolbarStrengthenActivity;
import com.tysq.ty_android.base.adapter.CommonSimpleAdapter;
import com.tysq.ty_android.feature.forbidReview.ForbidReviewActivity;
import com.tysq.ty_android.feature.forbidReview.ForbidReviewFragment;
import com.tysq.ty_android.widget.TagFlowLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.forbidlist.ForbidCategoryListBean;
import response.forbidlist.ForbidCommentResp;
import response.forbidlist.ForbidSubCategoryListBean;

import static utils.UploadUtils.getString;

/**
 * author       : liaozhenlin
 * time         : 2019/8/28 16:29
 * desc         : 禁止评论列表的适配器
 * version      : 1.0.0
 */
public class ForbidListAdapter
        extends CommonSimpleAdapter<ForbidCommentResp.ForbidCommentBean,
        ForbidListAdapter.ContentViewHolder> {

    public static final int NOSUNPOSITION = 0;


    private WeakReference<Context> mContext;
    List<ForbidCategoryListBean> forbidCategoryListBeans = new ArrayList<>();

    public ForbidListAdapter(Context context,
                             List<ForbidCommentResp.ForbidCommentBean> detailsInfoBeans) {
        super(context, detailsInfoBeans);
        this.mContext = new WeakReference<>(context);
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(
                mInflater.inflate(R.layout.item_bannerlist_content, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {


        ForbidCommentResp.ForbidCommentBean data = mDataList.get(position);

        if (data.getCategoryList() != null){

            forbidCategoryListBeans.clear();
            forbidCategoryListBeans.addAll(data.getCategoryList());

            int accountId = data.getAccountId();

            SpannableStringBuilder accountName = setAccountName(data.getAccount());
            holder.accountName.setText(accountName);

            holder.tagFlow.removeAllViews();

            for (int i = 0; i < forbidCategoryListBeans.size(); i++) {

                if (forbidCategoryListBeans.get(i).getSubCategory() != null
                        && forbidCategoryListBeans.get(i).getCategoryId() == 0){

                    List<ForbidSubCategoryListBean> subCategoryListBeans = new ArrayList<>();
                    subCategoryListBeans.addAll(forbidCategoryListBeans.get(i).getSubCategory());

                    for (int j = 0 ; j < subCategoryListBeans.size(); j++){

                        createLabel(forbidCategoryListBeans,
                                subCategoryListBeans,
                                holder.tagFlow,
                                i, j);
                    }
                }else if (forbidCategoryListBeans.get(i).getCategoryId() != 0){

                    createLabel(forbidCategoryListBeans,
                            null,
                            holder.tagFlow,
                            i,NOSUNPOSITION);

                } else{
                    holder.tagFlow.removeAllViews();
                }
            }

            holder.tvModify.setOnClickListener(view ->
                    ForbidReviewActivity.startActivity(mContext.get(), data.getAccount(), accountId));
        }

    }

    /**
     * 创造流式布局的标签
     * @param forbidCategoryListBeans
     * @param subCategoryListBeans
     * @param tagFlow
     * @param topPosition
     * @param subPosition
     */
    private void    createLabel(List<ForbidCategoryListBean> forbidCategoryListBeans,
                             List<ForbidSubCategoryListBean> subCategoryListBeans,
                             TagFlowLayout tagFlow,
                             int topPosition, int subPosition){
        String label = forbidCategoryListBeans.get(topPosition).getCategoryName();
        View view = LayoutInflater.from(mContext.get()).
                inflate(R.layout.widdget_forbidlist_label_add,
                        tagFlow,
                        false);
        TextView labelName = view.findViewById(R.id.tv_name);
        if (subCategoryListBeans != null){
            label += "-"+subCategoryListBeans.get(subPosition).getCategoryName();
        }
        labelName.setText(label);
        tagFlow.addView(view);

    }

    /**
     * 对用户名进行加粗并进行组装内容
     * @param accountName
     * @return
     */
    private SpannableStringBuilder setAccountName(String accountName){
        SpannableStringBuilder spannableString = new SpannableStringBuilder(accountName);
        //设置字体粗细
        StyleSpan span = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(span,
                0,
                spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }


    public static class ContentViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.accout_name)
        TextView accountName;
        @BindView(R.id.tag_flow)
        TagFlowLayout tagFlow;
        @BindView(R.id.tv_modify)
        TextView tvModify;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
