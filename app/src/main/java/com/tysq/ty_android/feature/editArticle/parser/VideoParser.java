package com.tysq.ty_android.feature.editArticle.parser;

import android.util.Log;

import com.tysq.ty_android.feature.articleDetail.config.ArticleConstants;

import java.util.List;

import jerrEditor.config.JerryEditorConstant;
import response.article.ArticleDetailResp;
import vo.article.ArticleVideoVO;

/**
 * author       : frog
 * time         : 2019/5/20 下午5:34
 * desc         :
 * version      :
 */
public class VideoParser extends BaseParser {
    @Override
    protected int getType() {
        return ArticleConstants.CONTENT_VIDEO;
    }

    @Override
    protected String getSplitText() {
        return JerryEditorConstant.PARSER_VIDEO;
    }

    @Override
    protected Object getVO(int position,
                           List<ArticleDetailResp.ArticleInfoBean.VideosBean> videoList,
                           List<ArticleDetailResp.MediaBean> audioList,
                           List<ArticleDetailResp.MediaBean> imageList) {
        if (position > videoList.size() - 1) {
            Log.e("AudioParser", "AudioList is NULL ");
            throw new RuntimeException("Parser ERROR...");
        }

        ArticleDetailResp.ArticleInfoBean.VideosBean videosBean = videoList.get(position);

        ArticleVideoVO articleVideoVO = new ArticleVideoVO();
        articleVideoVO.setStatus(videosBean.getStatus());
        articleVideoVO.setId(videosBean.getVideo().getId());
        articleVideoVO.setUrl(videosBean.getVideo().getUrl());
        articleVideoVO.setTitle("");

        if (videosBean.getCover() != null && videosBean.getCover().size() > 0) {
            articleVideoVO.setCoverId(videosBean.getCover().get(0).getId());
            articleVideoVO.setCover(videosBean.getCover().get(0).getUrl());
            articleVideoVO.setOriginalUrl(videosBean.getCover().get(0).getOriginalUrl());
        } else if (videosBean.getScreenshot() != null && videosBean.getScreenshot().size() > 0) {
            articleVideoVO.setCoverId(videosBean.getScreenshot().get(0).getId());
            articleVideoVO.setCover(videosBean.getScreenshot().get(0).getUrl());
            articleVideoVO.setOriginalUrl(videosBean.getScreenshot().get(0).getOriginalUrl());
        } else {
            articleVideoVO.setCoverId(-1);
            articleVideoVO.setCover("");
            articleVideoVO.setOriginalUrl("");
        }

        return articleVideoVO;
    }
}
