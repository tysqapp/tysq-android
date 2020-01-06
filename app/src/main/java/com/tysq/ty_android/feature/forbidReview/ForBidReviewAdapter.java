package com.tysq.ty_android.feature.forbidReview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tysq.ty_android.R;

import org.greenrobot.eventbus.EventBus;

import request.ForbidCommentReq;
import request.ForbidReviewReq;
import response.forbid.ForbidReviewBean;
import response.forbid.ForbidReviewSubBean;
import response.forbidlist.ForbidCategoryListBean;
import response.forbidlist.ForbidSubCategoryListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : liaozhenlin
 * time         : 2019-8-27 10:07
 * desc         :禁止评论数据适配器
 * version      : 1.0.0
 */

public class ForBidReviewAdapter extends BaseExpandableListAdapter {

    private final List<ForbidReviewBean> mData = new ArrayList<>();
    private Context context;

    public ForBidReviewAdapter(Context context){
        this.context = context;
    }

    public void setData(List<ForbidReviewBean> data){
        mData.clear();
        mData.addAll(data);
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mData.get(i).getForbidReviewSubBeanList().size();
    }

    @Override
    public Object getGroup(int i) {
        return mData.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mData.get(i).getForbidReviewSubBeanList().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        View v;
        GroupHolderView groupHolderView;
        if (view == null){
            groupHolderView = new GroupHolderView();
            v = View.inflate(context, R.layout.item_forbid_review_father, null);
            groupHolderView.tvName = v.findViewById(R.id.tvName);
            groupHolderView.cbCheck = v.findViewById(R.id.cb_check);
            groupHolderView.divider = v.findViewById(R.id.divider);
            groupHolderView.ivArrow = v.findViewById(R.id.iv_arrow);
            groupHolderView.llGroup = v.findViewById(R.id.ll_cb_check);
            v.setTag(groupHolderView);
        }else{
            v = view;
            groupHolderView = (GroupHolderView) v.getTag();
        }
        ForbidReviewBean bean = mData.get(i);
        groupHolderView.tvName.setText(bean.getCategoryName());
        groupHolderView.cbCheck.setFocusable(false);
        groupHolderView.cbCheck.setClickable(false);
        groupHolderView.cbCheck.setChecked(bean.isChecked());

        if (mData.get(i).getForbidReviewSubBeanList() == null
                || mData.get(i).getForbidReviewSubBeanList().size() <= 0){
            groupHolderView.ivArrow.setVisibility(View.GONE);
        }

        groupHolderView.llGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mData.get(i).isChecked()){  //一级板块之前点击了，只取消一级板块，二级不变
                    mData.get(i).setChecked(!groupHolderView.cbCheck.isChecked());
                }else{  //一级板块之前没点击，点击后二级板块全部勾选
                    mData.get(i).setChecked(!groupHolderView.cbCheck.isChecked());
                    List<ForbidReviewSubBean> forbidChildResps = mData.get(i)
                            .getForbidReviewSubBeanList();
                    //二级板块点击
                    for (ForbidReviewSubBean forbidChildResp : forbidChildResps){
                        forbidChildResp.setChecked(!groupHolderView.cbCheck.isChecked());
                    }
                }
                notifyDataSetChanged();
            }
        });

        //判断当父项为第一项则去除divider
        if (i == 0){
            groupHolderView.divider.setVisibility(View.GONE);
        }else{
            groupHolderView.divider.setVisibility(View.VISIBLE);
        }

        //判断展开后更换箭头
        if (b){
            groupHolderView.ivArrow.setImageResource(R.drawable.ic_arrow_up_grey);
        }else {
            groupHolderView.ivArrow.setImageResource(R.drawable.ic_arrow_down_grey);
        }
        return v;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        View v ;
        ChildViewHolder childViewHolder;
        if (view == null){
            childViewHolder = new ChildViewHolder();
            v = View.inflate(context, R.layout.item_forbid_review_child, null);
            childViewHolder.tvName = v.findViewById(R.id.tvName);
            childViewHolder.cbCheck = v.findViewById(R.id.cb);
            childViewHolder.llChild = v.findViewById(R.id.ll_child);
            v.setTag(childViewHolder);
        }else{
            v = view;
            childViewHolder = (ChildViewHolder) v.getTag();
        }
        ForbidReviewSubBean subBean = mData.get(i).getForbidReviewSubBeanList().get(i1);
        childViewHolder.tvName.setText(subBean.getCategoryName());

        childViewHolder.cbCheck.setFocusable(false);
        childViewHolder.cbCheck.setClickable(false);
        childViewHolder.cbCheck.setChecked(subBean.isChecked());

        childViewHolder.llChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childViewHolder.cbCheck.setChecked(!childViewHolder.cbCheck.isChecked());
                subBean.setChecked(childViewHolder.cbCheck.isChecked());
                if (!isChildChecked(mData.get(i).getForbidReviewSubBeanList())){
                    mData.get(i).setChecked(false);
                    notifyDataSetChanged();
                }
            }
        });

        return v;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    /**
     * 判断该一级板块的二级板块是否都选中
     * @param subBeans
     * @return
     */
    private boolean isChildChecked(List<ForbidReviewSubBean> subBeans){
        for (int i = 0; i < subBeans.size(); i++){
            ForbidReviewSubBean subBean = subBeans.get(i);
            if (!subBean.isChecked()){
                return false;
            }
        }
        return true;
    }
    class GroupHolderView{
        CheckBox cbCheck;
        TextView tvName;
        View divider;
        ImageView ivArrow;
        LinearLayout llGroup;
    }

    class ChildViewHolder{
        CheckBox cbCheck;
        TextView tvName;
        LinearLayout llChild;
    }


    /**
     * 查找checkbox中被选中的id
     */
    public List<ForbidCommentReq> queryCategoryId(){
        List<ForbidCommentReq> forbidCommentReqList = new ArrayList<>();
        List<ForbidReviewSubBean> subBeanList = new ArrayList<>();

        //用来存储没有点击一级板块的但点击了二级板块的id
        List<Integer> checkedList = new ArrayList<>();

        for (int i = 0; i < mData.size(); i++){
            ForbidCommentReq forbidComment = new ForbidCommentReq();
            subBeanList.clear();
            subBeanList.addAll(mData.get(i).getForbidReviewSubBeanList());

            //判断一级板块是否被点击，是则将二级板块全部添加
            if (mData.get(i).isChecked()){
                forbidComment.setParentId(mData.get(i).getCategoryId());
                int[] subIds= new int[subBeanList.size()];
                for (int j = 0; j < subBeanList.size(); j++){
                    subIds[j] = subBeanList.get(j).getCategoryId();
                }
                forbidComment.setSubId(subIds);
            }else{
                checkedList.clear();
                forbidComment.setParentId(0);
                int[] subIds ;

                //将被选中的二级板块id抽取出来，添加到一级数组
                for (int j = 0; j < subBeanList.size(); j++){
                    if (subBeanList.get(j).isChecked()){
                        checkedList.add(subBeanList.get(j).getCategoryId());
                    }
                }

                //若checklist不为空，则赋值给一维数组，若为空则返回个空
                if (!checkedList.isEmpty()){
                    subIds = new int[checkedList.size()];
                    for (int j = 0; j < subIds.length; j++){
                        subIds[j] = checkedList.get(j);
                    }
                    forbidComment.setSubId(subIds);
                }else{
                    subIds = new int[]{};
                    forbidComment.setSubId(subIds);
                }

            }
            forbidCommentReqList.add(forbidComment);
        }



        return forbidCommentReqList;
    }
}

