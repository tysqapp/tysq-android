package com.tysq.ty_android.feature.editArticle.parser;

import android.util.Log;

import com.jerry.media.model.MediaInfo;
import com.tysq.ty_android.feature.articleDetail.config.ArticleConstants;

import java.util.List;

import jerrEditor.config.JerryEditorConstant;
import response.article.ArticleDetailResp;
import vo.article.ArticleAudioVO;

/**
 * author       : frog
 * time         : 2019/5/20 下午5:34
 * desc         :
 * version      :
 */
public class AudioParser extends BaseParser {
    @Override
    protected int getType() {
        return ArticleConstants.CONTENT_AUDIO;
    }

    @Override
    protected String getSplitText() {
        return JerryEditorConstant.PARSER_AUDIO;
    }

    @Override
    protected Object getVO(int position,
                           List<ArticleDetailResp.ArticleInfoBean.VideosBean> videoList,
                           List<ArticleDetailResp.MediaBean> audioList,
                           List<ArticleDetailResp.MediaBean> imageList) {
        if (position > audioList.size() - 1) {
            Log.e("AudioParser", "AudioList is NULL ");
            throw new RuntimeException("Parser ERROR...");
        }

        ArticleDetailResp.MediaBean mediaBean = audioList.get(position);

        MediaInfo mediaInfo = new MediaInfo(mediaBean.getUrl());
        mediaInfo.setId("audio" + position);

        return new ArticleAudioVO(mediaBean.getId(), mediaBean.getUrl(), mediaBean.getName(), mediaInfo);
    }
}
