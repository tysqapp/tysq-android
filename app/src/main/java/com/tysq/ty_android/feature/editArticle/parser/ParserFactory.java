package com.tysq.ty_android.feature.editArticle.parser;

import com.tysq.ty_android.feature.articleDetail.config.ArticleConstants;

import java.util.ArrayList;
import java.util.List;

import response.article.ArticleDetailResp;
import vo.article.ArticleDetailVO;
import vo.article.ArticleTextVO;

/**
 * author       : frog
 * time         : 2019/5/20 下午5:39
 * desc         :
 * version      :
 */
public class ParserFactory {

    public static List<ArticleDetailVO> parser(List<ArticleDetailResp.ArticleInfoBean.VideosBean> videoList,
                                               List<ArticleDetailResp.MediaBean> audioList,
                                               List<ArticleDetailResp.MediaBean> imageList,
                                               String content) {
        AudioParser audioParser = new AudioParser();
        AudioParser2 audioParser2 = new AudioParser2();
        ImageParser imageParser = new ImageParser();
        VideoParser videoParser = new VideoParser();
        VideoParser2 videoParser2 = new VideoParser2();

        audioParser.setNextHandler(audioParser2);
        audioParser2.setNextHandler(imageParser);
        imageParser.setNextHandler(videoParser);
        videoParser.setNextHandler(videoParser2);

        List<ArticleDetailVO> mData = new ArrayList<>();
        mData.add(new ArticleDetailVO<>(ArticleConstants.CONTENT_TEXT, new ArticleTextVO(content)));

        audioParser.parser(videoList, audioList, imageList, mData);
        return mData;
    }

}
