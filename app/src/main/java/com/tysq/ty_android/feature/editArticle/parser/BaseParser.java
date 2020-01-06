package com.tysq.ty_android.feature.editArticle.parser;

import com.tysq.ty_android.feature.articleDetail.config.ArticleConstants;

import java.util.ArrayList;
import java.util.List;

import response.article.ArticleDetailResp;
import vo.article.ArticleDetailVO;
import vo.article.ArticleTextVO;

/**
 * author       : frog
 * time         : 2019/5/20 下午4:44
 * desc         :
 * version      :
 */
public abstract class BaseParser {

    private static final String _P = "</p>";
    private static final String P_ = "<p>";

    //指向下一个处理类
    private BaseParser mNextParser;

    public final void parser(List<ArticleDetailResp.ArticleInfoBean.VideosBean> videoList,
                             List<ArticleDetailResp.MediaBean> audioList,
                             List<ArticleDetailResp.MediaBean> imageList,
                             List<ArticleDetailVO> mArticleData) {

        List<ArticleDetailVO> result = new ArrayList<>();
        int resPos = 0;

        for (int j = 0; j < mArticleData.size(); ++j) {

            ArticleDetailVO articleDetailVO = mArticleData.get(j);

            // 只有文本类型才有可能含有 各种多媒体
            if (articleDetailVO.getType() != ArticleConstants.CONTENT_TEXT) {
                result.add(articleDetailVO);
                continue;
            }

            ArticleTextVO articleTextVO = (ArticleTextVO) articleDetailVO.getData();
            String[] split = articleTextVO.getContent().split(getSplitText());

            // 如果长度为1，直接略过
            if (split.length == 1) {
                result.add(articleDetailVO);
                continue;
            }

            for (int i = 0; i < split.length; ++i) {
                String item = split[i];

                if (i == 0) {
                    item = item + _P;
                } else if (i == split.length - 1) {
                    item = P_ + item;
                } else {
                    item = P_ + item + _P;
                }

                // 如果 为 "<p></p>" 则不添加
                if (!item.trim().equals(P_ + _P)) {
                    result.add(new ArticleDetailVO<>(ArticleConstants.CONTENT_TEXT, new ArticleTextVO(item)));
                }

                // 夹缝中添加类型
                if (i != split.length - 1) {
                    result.add(new ArticleDetailVO<>(getType(),
                            getVO(resPos, videoList, audioList, imageList)));
                    resPos++;
                }

            }

        }

        mArticleData.clear();
        mArticleData.addAll(result);

        if (mNextParser != null) {
            mNextParser.parser(videoList, audioList, imageList, mArticleData);
        }

    }

    void setNextHandler(BaseParser nextParser) {
        this.mNextParser = nextParser;
    }

    protected abstract int getType();

    protected abstract String getSplitText();

    protected abstract Object getVO(int position,
                                    List<ArticleDetailResp.ArticleInfoBean.VideosBean> videoList,
                                    List<ArticleDetailResp.MediaBean> audioList,
                                    List<ArticleDetailResp.MediaBean> imageList);

}
