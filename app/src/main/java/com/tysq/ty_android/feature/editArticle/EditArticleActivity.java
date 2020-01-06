package com.tysq.ty_android.feature.editArticle;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bit.config.BitManager;
import com.bit.view.activity.BitBaseActivity;
import com.bit.widget.StateLayout;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.articleDetail.activity.ArticleDetailActivity;
import com.tysq.ty_android.feature.cloudChoose.CloudChooseActivity;
import com.tysq.ty_android.feature.editArticle.di.DaggerEditArticleComponent;
import com.tysq.ty_android.feature.editArticle.di.EditArticleModule;
import com.tysq.ty_android.feature.editArticle.dialog.CategoryChooseDialog;
import com.tysq.ty_android.feature.editArticle.labelChoose.LabelChooseActivity;
import com.tysq.ty_android.feature.editArticle.listener.OnEditCategoryResultListener;
import com.tysq.ty_android.feature.rank.tip.RankTipDialog;
import com.tysq.ty_android.widget.TagFlowLayout;
import com.zinc.lib_jerry_editor.JerryEditor;
import com.zinc.lib_jerry_editor.style.common.IJerryStyle;
import com.zinc.lib_jerry_editor.style.fontsize.JerryH1Style;
import com.zinc.lib_jerry_editor.style.fontsize.JerryH2Style;
import com.zinc.lib_jerry_editor.style.fontsize.JerryH3Style;
import com.zinc.lib_jerry_editor.style.fontsize.JerryH4Style;
import com.zinc.lib_jerry_editor.style.other.JerryBlockQuoteStyle;
import com.zinc.lib_jerry_editor.style.other.JerryHrStyle;
import com.zinc.lib_jerry_editor.style.typeface.JerryBoldStyle;
import com.zinc.lib_jerry_editor.style.typeface.JerryItalicStyle;
import com.zinc.lib_jerry_editor.style.typeface.JerryStrikeThroughStyle;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import eventbus.EditLabelChangeEvent;
import jerrEditor.config.MediaType;
import jerrEditor.getter.JerryImageGetter;
import jerrEditor.jerry_media.model.AudioInfo;
import jerrEditor.jerry_media.model.ImgInfo;
import jerrEditor.jerry_media.model.VideoInfo;
import jerrEditor.jerry_media.style.JerryAudioStyle;
import jerrEditor.jerry_media.style.JerryImgStyle;
import jerrEditor.jerry_media.style.JerryVideoStyle;
import jerrEditor.parser.Html;
import jerrEditor.processor.OutputProcessor;
import model.VideoModel;
import response.LabelResp;
import response.article.ArticleDetailResp;
import response.article.PublishArticleResp;
import response.cloud.FileInfoResp;
import response.home.CategoryResp;
import response.home.SubCategory;
import response.home.TopCategory;
import vo.cloud.CloudFileVO;

/**
 * author       : frog
 * time         : 2019/5/29 下午3:05
 * desc         : 编辑文章
 * version      : 1.3.0
 */

public final class EditArticleActivity
        extends BitBaseActivity<EditArticlePresenter>
        implements EditArticleView, View.OnClickListener,
        OnEditCategoryResultListener {

    private static final String DATA_CATEGORY = "DATA_CATEGORY";
    private static final String ARTICLE_ID = "ARTICLE_ID";
    private static final int NOT_SELECT = -121;
    private static final String IS_NEED_CODE = "IS_NEED_CODE";

    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.v_divider)
    View vDivider;
    @BindView(R.id.et_article_title)
    EditText etArticleTitle;
    @BindView(R.id.iv_category)
    ImageView ivCategory;
    @BindView(R.id.iv_right_arrow)
    ImageView ivRightArrow;
    @BindView(R.id.rl_add_label)
    RelativeLayout rlAddLabel;
    @BindView(R.id.iv_label)
    ImageView ivLabel;
    @BindView(R.id.tag_flow)
    TagFlowLayout tagFlow;
    @BindView(R.id.ll_category)
    LinearLayout llCategory;

    @BindView(R.id.tv_choose_category)
    TextView tvChooseCategory;
    @BindView(R.id.tv_top)
    TextView tvTop;
    @BindView(R.id.iv_top_sub_arrow)
    ImageView ivTopSubArrow;
    @BindView(R.id.tv_sub)
    TextView tvSub;

    @BindView(R.id.tv_publish)
    TextView tvPublish;

    @BindView(R.id.tv_save_draft)
    TextView tvSaveDraft;

    private JerryEditor jerryEditor;

    private ImageView ivTypeface;
    private LinearLayout llBar;
    private ImageView ivImg;
    private ImageView ivVideo;
    private ImageView ivMusic;
    private ImageView ivBlock;
    private ImageView ivHr;

    private ImageView ivDownTriangle;
    private LinearLayout llTypeface;
    private ImageView ivBold;
    private ImageView ivItalic;
    private ImageView ivStrikeThrough;
    private ImageView ivH1;
    private ImageView ivH2;
    private ImageView ivH3;
    private ImageView ivH4;

    private CategoryChooseDialog mCategoryDialog;

    private TopCategory mTopCategory;
    private SubCategory mSubCategory;

    // 标签 数据
    private final ArrayList<LabelResp.LabelItem> mLabelList = new ArrayList<>();
    // 标签 View
    private final List<View> mLabelViewList = new ArrayList<>();

    // 分类数据
    private ArrayList<TopCategory> mCategoryData = new ArrayList<>();
    private String mArticleId;
    private boolean mIsNeedCode;

    private JerryImgStyle jerryImgStyle;
    private JerryAudioStyle jerryAudioStyle;
    private JerryVideoStyle jerryVideoStyle;

    public static void startActivity(Context context,
                                     String articleId,
                                     boolean isNeedCode) {
        Intent intent = new Intent(context, EditArticleActivity.class);

        intent.putExtra(ARTICLE_ID, articleId);
        intent.putExtra(IS_NEED_CODE, isNeedCode);

        context.startActivity(intent);
    }

    /**
     * 编辑文章
     *
     * @param context      上下文
     * @param categoryData 分类数据
     * @param articleId    文章
     */
    public static void startActivity(Context context,
                                     ArrayList<TopCategory> categoryData,
                                     String articleId) {
        Intent intent = new Intent(context, EditArticleActivity.class);

        intent.putExtra(DATA_CATEGORY, categoryData);
        intent.putExtra(ARTICLE_ID, articleId);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_edit_article;
    }

    StateLayout mStateLayout;

    @Override
    protected void initContentView() {
        mStateLayout = wrapFragmentView();
        setContentView(mStateLayout);
    }

    @Override
    protected void initIntent(Intent intent) {
        /*mCategoryData = intent.getParcelableArrayListExtra(DATA_CATEGORY);

        mArticleId = intent.getStringExtra(ARTICLE_ID);

        Iterator<TopCategory> iterator = mCategoryData.iterator();
        while (iterator.hasNext()) {
            TopCategory topCategory = iterator.next();

            // 如果是推荐的 ID，将该项移除
            if (topCategory.getId() == TyConfig.CATEGORY_RECOMMEND_ID) {
                iterator.remove();
                continue;
            }

            Iterator<SubCategory> subIterator = topCategory.getSubCategoryList().iterator();

            while (subIterator.hasNext()) {
                SubCategory subCategory = subIterator.next();
                // 如果和一级同id，说明是全部分类，移除
                if (subCategory.getId() == topCategory.getId()) {
                    subIterator.remove();
                    break;
                }
            }

        }*/

        mArticleId = intent.getStringExtra(ARTICLE_ID);
        mIsNeedCode = intent.getBooleanExtra(IS_NEED_CODE,false);

        if (isEditArticle()) {
            // 编辑文章
            mStateLayout.showLoading();
            //读取分类和文章详情
            mPresenter.loadCategory(mArticleId, mIsNeedCode);

        } else {
            // 新文章
            mStateLayout.showContent();
            //读取分类
            mPresenter.loadCategory();
        }

    }

    @Override
    protected void initView() {
        llBar = findViewById(R.id.ll_bar);
        ivImg = findViewById(R.id.iv_img);
        ivVideo = findViewById(R.id.iv_video);
        ivMusic = findViewById(R.id.iv_music);
        ivBlock = findViewById(R.id.iv_block);
        ivHr = findViewById(R.id.iv_hr);
        ivTypeface = findViewById(R.id.iv_typeface);
        jerryEditor = findViewById(R.id.rich_edit);

        ivDownTriangle = findViewById(R.id.iv_down_triangle);
        llTypeface = findViewById(R.id.ll_typeface);
        ivBold = findViewById(R.id.iv_bold);
        ivItalic = findViewById(R.id.iv_italic);
        ivStrikeThrough = findViewById(R.id.iv_strike_through);
        ivH1 = findViewById(R.id.iv_h1);
        ivH2 = findViewById(R.id.iv_h2);
        ivH3 = findViewById(R.id.iv_h3);
        ivH4 = findViewById(R.id.iv_h4);

        ivTypeface.setOnClickListener(this);

        initJerryEditor();

        rlAddLabel.setOnClickListener(this);
        llCategory.setOnClickListener(this);

        tvPublish.setOnClickListener(this);
        tvSaveDraft.setOnClickListener(this);
        ivClose.setOnClickListener(this);

        jerryEditor.setOnFocusChangeListener((v, hasFocus) -> {
            showJerryEditBar(hasFocus);
        });

        showJerryEditBar(jerryEditor.hasFocus());

    }

    @Override
    protected void registerDagger() {
        DaggerEditArticleComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .editArticleModule(new EditArticleModule(this))
                .build()
                .inject(this);
    }

    /**
     * 显示 分类选择框
     */
    private void showCategoryDialog() {
        if (mCategoryDialog == null) {
            mCategoryDialog = CategoryChooseDialog.newInstance();
        }

        mCategoryDialog.setTopItem(mTopCategory);
        mCategoryDialog.setSubItem(mSubCategory);

        mCategoryDialog.setCategoryData(mCategoryData);
        mCategoryDialog.setListener(this);
        mCategoryDialog.show(this);
    }

    /**
     * 创建标签
     */
    public void createLabel(final LabelResp.LabelItem labelItem) {
        View item = getLayoutInflater()
                .inflate(R.layout.widget_label_add, tagFlow, false);

        TextView tvName = item.findViewById(R.id.tv_name);
        tvName.setText(labelItem.getName());

        ImageView ivClose = item.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(v -> {
            refreshTagFlow(item);
        });

        // 将自己添加进列表
        mLabelViewList.add(item);

        // 将 item 添加进流式布局
        tagFlow.addView(item);
    }

    /**
     * 刷新流式标签
     */
    public void refreshTagFlow(View deleteView) {
        tagFlow.removeAllViews();
        if (mLabelList != null && mLabelList.size() > 0) {
            tagFlow.setVisibility(View.VISIBLE);
        } else {
            tagFlow.setVisibility(View.GONE);
        }

        if (deleteView != null) {
            int index = mLabelViewList.indexOf(deleteView);
            mLabelList.remove(index);
            mLabelViewList.remove(deleteView);
        }

        for (View view : mLabelViewList) {
            tagFlow.addView(view);
        }
    }

    /**
     * 初始化标签
     */
    public void initTagFlow() {
        tagFlow.removeAllViews();
        mLabelViewList.clear();

        if (mLabelList != null && mLabelList.size() > 0) {
            tagFlow.setVisibility(View.VISIBLE);
        } else {
            tagFlow.setVisibility(View.GONE);
        }

        for (LabelResp.LabelItem labelItem : mLabelList) {
            createLabel(labelItem);
        }
    }

    /**
     * 控制分类显示
     */
    private void controlCategoryDisplay() {

        if (mTopCategory == null) {
            tvChooseCategory.setVisibility(View.VISIBLE);

            tvTop.setVisibility(View.GONE);
            ivTopSubArrow.setVisibility(View.GONE);
            tvSub.setVisibility(View.GONE);
            return;
        }

        tvChooseCategory.setVisibility(View.GONE);

        tvTop.setText(mTopCategory.getName());
        tvTop.setVisibility(View.VISIBLE);

        if (mSubCategory != null) {
            tvSub.setText(mSubCategory.getName());

            tvSub.setVisibility(View.VISIBLE);
            ivTopSubArrow.setVisibility(View.VISIBLE);
        } else {
            tvSub.setVisibility(View.INVISIBLE);
            ivTopSubArrow.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 分类选择回调
     *
     * @param topCategory 一级分类
     * @param subCategory 二级分类
     */
    @Override
    public void onCategoryChoose(TopCategory topCategory, SubCategory subCategory) {

        mTopCategory = topCategory;
        mSubCategory = subCategory;

        controlCategoryDisplay();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initTagFlow();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_add_label:
                LabelChooseActivity.startActivity(this, mLabelList);
                break;

            case R.id.ll_category:
                showCategoryDialog();
                break;

            case R.id.iv_typeface:
                showTypeface();
                break;

            case R.id.iv_img:
                CloudChooseActivity.startActivity(this, TAG,
                        CloudChooseActivity.IMAGE, CloudChooseActivity.NO_LIMIT);
                break;

            case R.id.iv_video:
                CloudChooseActivity.startActivity(this, TAG,
                        CloudChooseActivity.VIDEO, CloudChooseActivity.NO_LIMIT);
                break;

            case R.id.iv_music:
                CloudChooseActivity.startActivity(this, TAG,
                        CloudChooseActivity.AUDIO, CloudChooseActivity.NO_LIMIT);
                break;

            case R.id.tv_publish:
                handlePublish(true);
                break;

            case R.id.iv_close:
                back();

                break;

            case R.id.tv_save_draft:
                handlePublish(false);
                break;

        }

    }

    private void back() {
        if (isEditArticle()) {
            new MaterialDialog.Builder(this)
                    .title(getString(R.string.edit_article_quit_title))
                    .content(getString(R.string.edit_article_content_clear_tip))
                    .negativeColor(ContextCompat.getColor(this, R.color.et_tip_text_color))
                    .negativeText(getString(R.string.common_confirm))
                    .onNegative((dialog, which) -> {
                        finish();
                    })
                    .positiveColor(ContextCompat.getColor(this, R.color.main_blue_color))
                    .positiveText(getString(R.string.common_cancel))
                    .show();
            return;
        }

        boolean quitQuick = safeQuit();

        if (quitQuick) {
            finish();
        } else {
            new MaterialDialog.Builder(this)
                    .title(getString(R.string.edit_quit_title))
                    .content(getString(R.string.edit_content_clear_tip))
                    .negativeColor(ContextCompat.getColor(this, R.color.et_tip_text_color))
                    .negativeText(getString(R.string.common_confirm))
                    .onNegative((dialog, which) -> {
                        finish();
                    })
                    .positiveColor(ContextCompat.getColor(this, R.color.main_blue_color))
                    .positiveText(getString(R.string.common_cancel))
                    .show();
        }
    }

    private boolean safeQuit() {
        String title = etArticleTitle.getText().toString().trim();

        String html = Html.toHtml(jerryEditor.getText());

        ArrayList imageList = Html.getMediaInfo(MediaType.IMAGE);
        ArrayList audioList = Html.getMediaInfo(MediaType.AUDIO);
        ArrayList videoList = Html.getMediaInfo(MediaType.VIDEO);

        if (TextUtils.isEmpty(title)
                && TextUtils.isEmpty(html)
                && imageList.size() <= 0
                && audioList.size() <= 0
                && videoList.size() <= 0) {
            return true;
        }

        return false;
    }

    /**
     * 检测内容是否为空
     *
     * @return true: 有为空的；false: 不为空
     */
    private boolean checkIsEmpty() {
        // 标题
        String title = etArticleTitle.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            ToastUtils.show(getString(R.string.edit_enter_title));
            return true;
        }

        // 内容
        String content = jerryEditor.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtils.show(getString(R.string.edit_enter_content));
            return true;
        }

        // 分类
        int categoryId = getCategoryId();
        if (categoryId == NOT_SELECT) {
            ToastUtils.show(getString(R.string.edit_choose_category_tip));
            return true;
        }

        return false;
    }

    /**
     * 获取选中的分类id
     */
    private int getCategoryId() {
        int categoryId = NOT_SELECT;
        if (mTopCategory != null) {
            if (mSubCategory == null) {
                categoryId = mTopCategory.getId();
            } else {
                categoryId = mSubCategory.getId();
            }
        }

        Log.d(TAG, "categoryId:" + categoryId);
        return categoryId;
    }

    /**
     * 处理发布
     *
     * @param isPublish 是否发布
     */
    private void handlePublish(boolean isPublish) {

        boolean isEmpty = checkIsEmpty();

        if (isEmpty) {
            return;
        }

        showDialog();

        String title = etArticleTitle.getText().toString().trim();
        int categoryId = getCategoryId();

        // 获取数据
        String html = Html.toHtml(jerryEditor.getText());

        Log.i(TAG, "handlePublish: original html: " + html);
        html = OutputProcessor.handle(html);
        Log.i(TAG, "handlePublish: real html: " + html);

        ArrayList imageList = Html.getMediaInfo(MediaType.IMAGE);
        ArrayList audioList = Html.getMediaInfo(MediaType.AUDIO);
        ArrayList videoList = Html.getMediaInfo(MediaType.VIDEO);

        Log.i("HTML Content", html);
        Log.i("Image Content", imageList.toString());
        Log.i("Audio Content", audioList.toString());
        Log.i("Video Content", videoList.toString());

        List<Integer> labelList = new ArrayList<>();
        for (LabelResp.LabelItem labelItem : mLabelList) {
            labelList.add(labelItem.getLabelId());
        }

        if (isEditArticle()) {

            mPresenter.updateArticle(
                    mArticleId,
                    title,
                    categoryId,
                    labelList,
                    html,
                    imageList,
                    audioList,
                    videoList,
                    isPublish
            );

        } else {

            mPresenter.publishArticle(
                    title,
                    categoryId,
                    labelList,
                    html,
                    imageList,
                    audioList,
                    videoList,
                    isPublish
            );

        }

    }

    /**
     * 控制样式栏
     *
     * @param isShow true:显示；false:不显示
     */
    private void showJerryEditBar(boolean isShow) {
        if (isShow) {
            llBar.setVisibility(View.VISIBLE);
            ivDownTriangle.setVisibility(View.GONE);
            llTypeface.setVisibility(View.GONE);
            ivTypeface.setSelected(false);
        } else {
            llBar.setVisibility(View.GONE);
            ivDownTriangle.setVisibility(View.GONE);
            llTypeface.setVisibility(View.GONE);
            ivTypeface.setSelected(false);
        }
    }

    /**
     * 控制字体样式
     */
    private void showTypeface() {
        if (llTypeface.getVisibility() == View.VISIBLE) {
            ivDownTriangle.setVisibility(View.GONE);
            llTypeface.setVisibility(View.GONE);
            ivTypeface.setSelected(false);
        } else {
            ivDownTriangle.setVisibility(View.VISIBLE);
            llTypeface.setVisibility(View.VISIBLE);
            ivTypeface.setSelected(true);
        }
    }

    @Override
    protected int getInitRegister() {
        return BUTTER_KNIFE | EVENT_BUS;
    }

    /**
     * 标签变动
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshTag(EditLabelChangeEvent event) {
        mLabelList.clear();
        mLabelList.addAll(event.getLabelItemList());

        initTagFlow();
    }

    /**
     * 选择文件回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelResult(CloudFileVO cloudFileVO) {
        if (!cloudFileVO.getFrom().equals(TAG)) {
            return;
        }

        Log.i(TAG, "onSelResult: " + cloudFileVO.toString());
        switch (cloudFileVO.getType()) {
            case CloudChooseActivity.IMAGE:
                for (FileInfoResp.FileItem fileItem : cloudFileVO.getFileList()) {
                    String cover;
                    if (!TextUtils.isEmpty(fileItem.getUrl())) {
                        cover = fileItem.getUrl();
                    } else {
                        cover = null;
                    }

                    jerryImgStyle.addOrCancelStyle
                            (new ImgInfo(
                                    fileItem.getId(),
                                    cover,
                                    R.drawable.ic_cloud_choose_img));

                }
                break;
            case CloudChooseActivity.VIDEO:
                for (FileInfoResp.FileItem fileItem : cloudFileVO.getFileList()) {
                    String cover;

                    if (fileItem.getCoverList() != null
                            && fileItem.getCoverList().size() > 0) {
                        cover = fileItem.getCoverList().get(0).getCoverUrl();
                    } else if (fileItem.getScreenshotList() != null
                            && fileItem.getScreenshotList().size() > 0) {
                        cover = fileItem.getScreenshotList().get(0).getScreenshotsUrl();
                    } else {
                        cover = null;
                    }

                    VideoModel videoModel = new VideoModel(fileItem.getId());

                    if (fileItem.getCoverList() != null) {
                        for (FileInfoResp.CoverInfo coverInfo : fileItem.getCoverList()) {
                            videoModel.getCover().add(coverInfo.getId());
                        }
                    }

                    if (fileItem.getScreenshotList() != null) {
                        for (FileInfoResp.ScreenshotInfo info : fileItem.getScreenshotList()) {
                            videoModel.getScreenShot().add(info.getId());
                        }
                    }

                    jerryVideoStyle.addOrCancelStyle(new VideoInfo(
                            fileItem.getId(),
                            fileItem.getUrl(),
                            cover,
                            R.drawable.ic_cloud_choose_video,
                            videoModel));

                }
                break;
            case CloudChooseActivity.AUDIO:
                for (FileInfoResp.FileItem fileItem : cloudFileVO.getFileList()) {
                    jerryAudioStyle.addOrCancelStyle
                            (new AudioInfo(
                                    fileItem.getId(),
                                    fileItem.getUrl(),
                                    fileItem.getFilename()));
                }
                break;
        }
    }

    @Override
    public void onPublishArticle(boolean isPublish, PublishArticleResp publishArticleResp) {
        hideDialog();

        if (isPublish) { // 发布

            if (publishArticleResp.isReview()) {    // 需要审核
                showExamTipDialog(publishArticleResp.getArticleId(), publishArticleResp.getLimitScore());
            } else {    // 无需审核
                String tip;
                if (publishArticleResp.getLimitScore() <= 0) {
                    tip = getString(R.string.edit_article_success);
                }else{
                    tip = getString(R.string.edit_article_success_with_score);
                    tip = String.format(tip, publishArticleResp.getLimitScore());
                }

                ToastUtils.show(tip);
                goToArticle(publishArticleResp.getArticleId());
            }

        } else {  // 保存为草稿
            ToastUtils.show(getString(R.string.edit_save_draft_success));
            goToArticle(publishArticleResp.getArticleId());
        }

    }

    /**
     * 显示审核提示框
     *
     * @param articleId
     */
    public void showExamTipDialog(String articleId, int limitScore) {
        String tip;
        if (limitScore <= 0) {
            tip = getString(R.string.edit_review_title);
        } else {
            tip = getString(R.string.edit_review_title_with_score);
            tip = String.format(tip, limitScore);
        }

        new MaterialDialog
                .Builder(this)
                .title(tip)
                .content(R.string.edit_review_content)
                .negativeText(R.string.edit_review_cancel)
                .positiveText(R.string.edit_review_visit)
                .cancelable(false)
                .positiveColor(
                        ContextCompat.getColor(this, R.color.main_blue_color)
                )
                .negativeColor(
                        ContextCompat.getColor(this, R.color.et_tip_text_color)
                )
                .onNegative((dialog, which) -> {
                    finish();
                })
                .onPositive((dialog, which) -> {
                    goToArticle(articleId);
                })
                .show();
    }

    private void goToArticle(String articleId) {

        ArticleDetailActivity.startActivity(this, articleId);

        finish();
    }

    @Override
    public void onUpdateArticle(PublishArticleResp resp, boolean isPublish) {
        hideDialog();

        if (isPublish) {
            // 发布

            // 需要审核
            if (resp.isReview()) {
                showExamTipDialog(resp.getArticleId(), resp.getLimitScore());
                return;
            }

            // 不需要审核
            if (resp.getLimitScore() <= 0) {
                ToastUtils.show(getString(R.string.edit_article_success));
            } else {
                ToastUtils.show(
                        String.format(getString(R.string.edit_article_success_with_score),
                                resp.getLimitScore())
                );
            }
            ArticleDetailActivity.startActivity(this, resp.getArticleId());
            finish();

        } else {
            // 草稿
            ToastUtils.show(getString(R.string.edit_save_draft_success));
            ArticleDetailActivity.startActivity(this, resp.getArticleId());
            finish();
        }

    }

    @Override
    public void onNotEnoughRank(PublishArticleResp value) {
        hideDialog();

        if (value.getLimitScore() <= 0) {
            ArticleDetailActivity.startActivity(this, value.getArticleId());
            finish();
            return;
        }

        RankTipDialog
                .newInstance(value.getLimitScore(), Constant.JudgementType.CREATE)
                .show(this);
    }

    @Override
    public void onLoadCategory(CategoryResp value) {
        this.mCategoryData.clear();
        this.mCategoryData.addAll(value.getCategoryInfo());
    }

    @Override
    public void onLoadArticleInfoError() {
        mStateLayout.showRetry();
    }

    @Override
    public void onLoadArticleInfo(ArticleDetailResp.ArticleInfoBean articleInfo, CategoryResp categoryResp) {

        mCategoryData.clear();
        mCategoryData.addAll(categoryResp.getCategoryInfo());

        String content = articleInfo.getContentOriginal();

        jerryEditor.setLoading(true);

        Html.clearMediaInfo();

        if (articleInfo.getImages() != null || articleInfo.getImages().size() > 0) {
            ArrayList<ImgInfo> imgInfoList = new ArrayList<>();
            for (ArticleDetailResp.MediaBean image : articleInfo.getImages()) {
                imgInfoList.add(new ImgInfo(image.getId(),
                        image.getUrl(),
                        R.drawable.ic_cloud_choose_img));
            }

            Html.addMediaInfoForParserHtml(MediaType.IMAGE, imgInfoList);
        }


        if (articleInfo.getAudios() != null || articleInfo.getAudios().size() > 0) {
            ArrayList<AudioInfo> audioInfoList = new ArrayList<>();
            for (ArticleDetailResp.MediaBean audio : articleInfo.getAudios()) {
                audioInfoList.add(new AudioInfo(audio.getId(), audio.getUrl(), audio.getName()));
            }

            Html.addMediaInfoForParserHtml(MediaType.AUDIO, audioInfoList);
        }

        if (articleInfo.getVideos() != null || articleInfo.getVideos().size() > 0) {
            ArrayList<VideoInfo> videoInfoList = new ArrayList<>();
            for (ArticleDetailResp.ArticleInfoBean.VideosBean video : articleInfo.getVideos()) {

                String cover;

                if (video.getCover() != null
                        && video.getCover().size() > 0) {
                    cover = video.getCover().get(0).getUrl();
                } else if (video.getScreenshot() != null
                        && video.getScreenshot().size() > 0) {
                    cover = video.getScreenshot().get(0).getUrl();
                } else {
                    cover = null;
                }

                VideoModel videoModel = new VideoModel(video.getVideo().getId());
                if (video.getCover() != null) {
                    for (ArticleDetailResp.MediaBean mediaBean : video.getCover()) {
                        videoModel.getCover().add(mediaBean.getId());
                    }
                }

                if (video.getScreenshot() != null) {
                    for (ArticleDetailResp.MediaBean mediaBean : video.getScreenshot()) {
                        videoModel.getScreenShot().add(mediaBean.getId());
                    }
                }

                videoInfoList.add(new VideoInfo(video.getVideo().getId(),
                        video.getVideo().getUrl(),
                        cover,
                        R.drawable.ic_cloud_choose_video,
                        videoModel));

                Html.addMediaInfoForParserHtml(MediaType.VIDEO, videoInfoList);
            }


        }

        jerryEditor.setText(Html.fromHtml(content,
                new JerryImageGetter(this, jerryEditor)));

        jerryEditor.setLoading(false);

        // 初始化标题
        etArticleTitle.setText(articleInfo.getTitle());

        Log.d("fwew", String.valueOf(mCategoryData.get(5).getId()));

        // 初始化分类
        TopCategory topCategory = null;
        SubCategory subCategory = null;
        for (TopCategory topItem : mCategoryData) {
            if (topItem.getId() == articleInfo.getCategoryId()) {
                topCategory = topItem;
                break;
            } else if (topItem.getSubCategoryList() == null || topItem.getSubCategoryList().size() <= 0){
                continue;
            }

            for (SubCategory subItem : topItem.getSubCategoryList()) {
                if (subItem.getId() == articleInfo.getCategoryId()) {
                    topCategory = topItem;
                    subCategory = subItem;
                    break;
                }
            }
        }

        onCategoryChoose(topCategory, subCategory);

        controlCategoryDisplay();

        // 初始化标签
        mLabelList.addAll(articleInfo.getLabel());
        initTagFlow();

        mStateLayout.showContent();

    }

    /**
     * 是否需要编辑文章
     *
     * @return true 需要；false 不需要
     */
    private boolean isEditArticle() {
        return !TextUtils.isEmpty(mArticleId);
    }

    @Override
    public void onBackPressed() {
        back();
    }

    protected StateLayout wrapFragmentView() {
        StateLayout stateLayout = new StateLayout(this);
        stateLayout.setContentView(getLayoutInflater().inflate(R.layout.activity_edit_article,
                stateLayout, false));
        stateLayout.setRetryView(BitManager.getInstance().getRetryViewLayout());
        stateLayout.setEmptyView(BitManager.getInstance().getEmptyViewLayout());
        stateLayout.setLoadingView(BitManager.getInstance().getLoadingViewLayout());
        return stateLayout;
    }

    private void initJerryEditor() {

        IJerryStyle boldStyle = new JerryBoldStyle();
        initJerryStyle(boldStyle,
                ivBold,
                R.drawable.ic_jedit_select_bold,
                R.drawable.ic_jedit_unselect_bold);

        IJerryStyle italicStyle = new JerryItalicStyle();
        initJerryStyle(italicStyle,
                ivItalic,
                R.drawable.ic_jedit_select_italic,
                R.drawable.ic_jedit_unselect_italic);

        IJerryStyle strikeThroughStyle = new JerryStrikeThroughStyle();
        initJerryStyle(strikeThroughStyle,
                ivStrikeThrough,
                R.drawable.ic_jedit_select_strike_through,
                R.drawable.ic_jedit_unselect_strike_through);

        JerryH1Style h1Style = new JerryH1Style();
        initJerryStyle(h1Style,
                ivH1,
                R.drawable.ic_jedit_select_h1,
                R.drawable.ic_jedit_unselect_h1);

        JerryH2Style h2Style = new JerryH2Style();
        initJerryStyle(h2Style,
                ivH2,
                R.drawable.ic_jedit_select_h2,
                R.drawable.ic_jedit_unselect_h2);

        JerryH3Style h3Style = new JerryH3Style();
        initJerryStyle(h3Style,
                ivH3,
                R.drawable.ic_jedit_select_h3,
                R.drawable.ic_jedit_unselect_h3);

        JerryH4Style h4Style = new JerryH4Style();
        initJerryStyle(h4Style,
                ivH4,
                R.drawable.ic_jedit_select_h4,
                R.drawable.ic_jedit_unselect_h4);

        JerryBlockQuoteStyle quoteStyle = new JerryBlockQuoteStyle();
        initJerryStyle(quoteStyle,
                ivBlock,
                R.drawable.ic_jedit_select_block,
                R.drawable.ic_jedit_unselect_block);

        JerryHrStyle jerryHrStyle = new JerryHrStyle();
        initJerryStyle(jerryHrStyle,
                ivHr,
                R.drawable.ic_jedit_unselect_under_line,
                R.drawable.ic_jedit_unselect_under_line);

        jerryImgStyle = new JerryImgStyle();
        initJerryStyle(jerryImgStyle,
                ivImg,
                R.drawable.ic_jedit_unselect_img,
                R.drawable.ic_jedit_unselect_img);

        jerryAudioStyle = new JerryAudioStyle();
        initJerryStyle(jerryAudioStyle,
                ivMusic,
                R.drawable.ic_jedit_unselect_audio,
                R.drawable.ic_jedit_unselect_audio);

        jerryVideoStyle = new JerryVideoStyle();
        initJerryStyle(jerryVideoStyle,
                ivVideo,
                R.drawable.ic_jedit_unselect_video,
                R.drawable.ic_jedit_unselect_video);


        jerryEditor.addStyle(boldStyle);
        jerryEditor.addStyle(italicStyle);
        jerryEditor.addStyle(strikeThroughStyle);

        jerryEditor.addStyle(h1Style);
        jerryEditor.addStyle(h2Style);
        jerryEditor.addStyle(h3Style);
        jerryEditor.addStyle(h4Style);
        jerryEditor.addStyle(quoteStyle);
        jerryEditor.addStyle(jerryHrStyle);

        jerryEditor.addStyle(jerryImgStyle);
        jerryEditor.addStyle(jerryAudioStyle);
        jerryEditor.addStyle(jerryVideoStyle);

        ivImg.setOnClickListener(this);
        ivMusic.setOnClickListener(this);
        ivVideo.setOnClickListener(this);

    }

    private void initJerryStyle(IJerryStyle jerryStyle,
                                ImageView imageView,
                                int selectDrawable,
                                int unSelectDrawable) {
        jerryStyle.setImageView(imageView);
        jerryStyle.setSelDrawable(
                ContextCompat
                        .getDrawable(this, selectDrawable));
        jerryStyle.setUnSelDrawable(
                ContextCompat
                        .getDrawable(this, unSelectDrawable));
    }


}
