package com.tysq.ty_android.feature.editArticle.parser;

import android.util.Log;

import com.tysq.ty_android.feature.articleDetail.config.ArticleConstants;

import java.util.List;

import jerrEditor.config.JerryEditorConstant;
import response.article.ArticleDetailResp;
import vo.article.ArticleImageVO;

/**
 * author       : frog
 * time         : 2019/5/20 下午5:34
 * desc         :
 * version      :
 */
public class ImageParser extends BaseParser {
    @Override
    protected int getType() {
        return ArticleConstants.CONTENT_IMAGE;
    }

    @Override
    protected String getSplitText() {
        return JerryEditorConstant.PARSER_IMG;
    }

    @Override
    protected Object getVO(int position,
                           List<ArticleDetailResp.ArticleInfoBean.VideosBean> videoList,
                           List<ArticleDetailResp.MediaBean> audioList,
                           List<ArticleDetailResp.MediaBean> imageList) {

        if (position > imageList.size() - 1) {
            Log.e("ImageParser", "ImageList is NULL ");
            throw new RuntimeException("Parser ERROR...");
        }

        ArticleDetailResp.MediaBean mediaBean = imageList.get(position);

        return new ArticleImageVO(mediaBean.getId(),
                mediaBean.getUrl(),
                mediaBean.getOriginalUrl(),
                mediaBean.getWidth(),
                mediaBean.getHeight(),
                position);
    }

}
