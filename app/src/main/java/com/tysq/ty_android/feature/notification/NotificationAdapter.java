package com.tysq.ty_android.feature.notification;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bit.utils.DateUtils;
import com.bit.view.fragment.BitBaseFragment;
import com.bumptech.glide.Glide;
import com.jerry.websocket.JWebSocket;
import com.jerry.websocket.WebSocketConstant;
import com.jerry.websocket.model.WsRemindModel;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.adapter.CommonHeaderSimpleAdapter;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.articleDetail.activity.ArticleDetailActivity;
import com.tysq.ty_android.feature.editArticle.EditArticleActivity;
import com.tysq.ty_android.feature.notification.listener.OnClickNotificationRead;
import com.tysq.ty_android.feature.personalHomePage.PersonalHomePageActivity;
import com.tysq.ty_android.feature.reportDetail.ReportDetailActivity;
import com.tysq.ty_android.utils.MyLinkedMovementMethod;
import com.tysq.ty_android.utils.TyUtils;


import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.notification.NotificationResp;
import response.common.TitleCountVO;

/**
 * author       : frog
 * time         : 2019-08-29 17:04
 * desc         : 通知的适配器
 * version      : 1.0.0
 */
public class NotificationAdapter
        extends CommonHeaderSimpleAdapter<TitleCountVO, NotificationResp> {

    private final WeakReference<BitBaseFragment> mFragment;
    private final WeakReference<Context> mContext;
    private OnClickNotificationRead mListener;

    public NotificationAdapter(BitBaseFragment fragment,
                               Context context,
                               TitleCountVO header,
                               List<NotificationResp> notificationResps,
                               OnClickNotificationRead listener) {
        super(context, header, notificationResps);
        this.mFragment = new WeakReference<>(fragment);
        this.mContext = new WeakReference<>(context);
        this.mListener = listener;
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TitleViewHolder(
                mInflater
                        .inflate(R.layout.item_notification_count_title, parent, false)
        );
    }

    @Override
    protected RecyclerView.ViewHolder getContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(
                mInflater
                        .inflate(R.layout.item_notification, parent, false)
        );
    }

    @Override
    protected RecyclerView.ViewHolder getEmptyViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof TitleViewHolder) {

            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;

            String format = String.format(mContext.get()
                    .getString(R.string.notification_count), String.valueOf(mHeader.getCount()));

            titleViewHolder.tvTitle.setText(format);

            titleViewHolder.tvRead.setOnClickListener(v -> mListener.onClickNotificationAllRead());

        } else if (holder instanceof ContentViewHolder) {

            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;

            NotificationResp item = mDataList.get(position - 1);

            if (item.getAction().equals(Constant.NotificationType.ARTICLE_NEW_COMMENT)
                    || item.getAction().equals(Constant.NotificationType.COMMENT_NEW_REPLY)) {
                contentViewHolder.tvTitle.setText(item.getSenderName());
                contentViewHolder.tvTitle.setOnClickListener(view ->
                        PersonalHomePageActivity.startActivity(
                                mContext.get(), item.getSender())
                );
            } else {
                contentViewHolder.tvTitle.setText(item.getRemindType());
            }

            //设置头像
            if (item.getAction().equals(Constant.NotificationType.ARTICLE_REVIEW)
                    || item.getAction().equals(Constant.NotificationType.REVIEW_PASS)
                    || item.getAction().equals(Constant.NotificationType.REVIEW_UNPASS)) {
                Glide
                        .with(mContext.get())
                        .load(R.drawable.ic_verify_notify)
                        .into(contentViewHolder.ivPhoto);
            } else if (item.getAction().equals(Constant.NotificationType.REPORT_HANDLER)
                    || item.getAction().equals(Constant.NotificationType.EFFECTIVE_REPORT_HANDLER)
                    || item.getAction().equals(Constant.NotificationType.INVALID_REPORT_HANDLER)
                    || item.getAction().equals(Constant.NotificationType.NEW_REPORT_HANDLER)) {

                Glide
                        .with(mContext.get())
                        .load(R.drawable.ic_report_notify)
                        .into(contentViewHolder.ivPhoto);
            } else if (item.getAction().equals(Constant.NotificationType.DELETE_CATEGORY)){
                Glide
                        .with(mContext.get())
                        .load(R.drawable.ic_delete_notify)
                        .into(contentViewHolder.ivPhoto);
            } else{
                TyUtils.initUserPhoto(
                        mFragment.get(),
                        mContext.get(),
                        item.getAvatarUrl(),
                        contentViewHolder.ivPhoto);
            }

            //设置时间戳
            contentViewHolder.tvTime.setText(DateUtils.getY_M_D_H_M_S_ViaTimestamp(item.getTime() * 1000L));
            //设置通知内容
            setContent(item.getAction(),
                    item.getSenderName(),
                    item.getTitle(),
                    item.getReportNumber(),
                    item.getSender(),
                    contentViewHolder.tvContent);

            //判断是否为已读
            if (item.isRead()) {
                contentViewHolder.redDot.setVisibility(View.GONE);
            } else {
                contentViewHolder.redDot.setVisibility(View.VISIBLE);
            }

            contentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (item.getAction().equals(Constant.NotificationType.REPORT_HANDLER)
                            || item.getAction().equals(Constant.NotificationType.EFFECTIVE_REPORT_HANDLER)
                            || item.getAction().equals(Constant.NotificationType.INVALID_REPORT_HANDLER)) {
                        ReportDetailActivity.startActivity(mContext.get(), item.getReportId());

                    } else if (item.getAction().equals(Constant.NotificationType.DELETE_CATEGORY)){
                        EditArticleActivity.startActivity(mContext.get(), item.getArticleId(), false);
                    }
                    else{
                        ArticleDetailActivity.startActivity(mContext.get(),item.getArticleId());
                    }

                    if (!item.isRead()) {
                        mListener.onClickNotificationRead(item.getNotifyId());
                        contentViewHolder.redDot.setVisibility(View.GONE);
                    }

                }
            });
        }
    }


    private void setContent(String type,
                            String name,
                            String title,
                            String reportNumber,
                            int userId,
                            TextView textView) {
        String content;
        switch (type) {
            case Constant.NotificationType.REVIEW_PASS:
                setContextText(R.string.notification_review_pass, textView, title);
                break;
            case Constant.NotificationType.REVIEW_UNPASS:
                setContextText(R.string.notification_review_unpass, textView, title);
                break;
            case Constant.NotificationType.ARTICLE_REVIEW:
                String format = String.format(mContext.get().getString(R.string.notification_article_review), name, title);
                setContentAndNameClick(format, name, userId, textView);
                break;
            case Constant.NotificationType.ARTICLE_NEW_COMMENT:
                setContextText(R.string.notification_article_new_comment, textView, title);
                break;
            case Constant.NotificationType.COMMENT_NEW_REPLY:
                setContextText(R.string.notification_comment_new_reply, textView, title);
                break;
            case Constant.NotificationType.NEW_REPORT_HANDLER:
                content = String.format(
                        mContext.get()
                                .getString(R.string.notification_new_report_handler), name, title, reportNumber
                );

                setContentAndNameClick(content, name, userId, textView);
                break;
            case Constant.NotificationType.EFFECTIVE_REPORT_HANDLER:
                textView.setText(R.string.notification_effective_report_handler);
                break;
            case Constant.NotificationType.INVALID_REPORT_HANDLER:
                textView.setText(R.string.notification_invalid_report_handler);
                break;
            case Constant.NotificationType.DELETE_CATEGORY:
                setContextText(R.string.notification_delete_category, textView, title);
                break;

        }

    }

    /**
     * 设置富文本内容，并设置昵称的点击事件
     *
     * @param content  内容
     * @param name     昵称
     * @param userId   用户id
     * @param textView
     */
    private void setContentAndNameClick(String content, String name, int userId, TextView textView) {
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                PersonalHomePageActivity.startActivity(mContext.get(), userId);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(mContext.get().getResources().getColor(R.color.main_text_color));
            }
        };

        builder.setSpan(
                clickableSpan,
                0,
                name.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(builder);
        textView.setMovementMethod(MyLinkedMovementMethod.getInstance());
    }

    /**
     * 设置富文本内容
     *
     * @param stringId 资源文件
     * @param textView
     * @param title    标题
     */
    private void setContextText(int stringId, TextView textView, String title) {
        String content = String.format(mContext.get().getString(stringId), title);
        textView.setText(content);
    }

    public static class TitleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_read)
        TextView tvRead;

        public TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.red_dot)
        View redDot;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.rl_notification)
        RelativeLayout relativeLayout;


        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }


}
