package eventbus;

import java.util.List;

import response.LabelResp;

/**
 * author       : frog
 * time         : 2019/5/7 下午5:45
 * desc         : 编辑页——标签变动事件
 * version      : 1.3.0
 */
public class EditLabelChangeEvent {

    private final List<LabelResp.LabelItem> mLabelItemList;

    public EditLabelChangeEvent(List<LabelResp.LabelItem> labelItemList) {
        this.mLabelItemList = labelItemList;
    }

    public List<LabelResp.LabelItem> getLabelItemList() {
        return mLabelItemList;
    }
}
