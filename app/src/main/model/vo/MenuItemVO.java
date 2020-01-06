package vo;

/**
 * author       : frog
 * time         : 2019/4/11 下午6:00
 * desc         :
 * version      : 1.3.0
 */
public class MenuItemVO {

    // id值
    private int id;
    // 显示的文字
    private int titleRes;
    // 选中时显示的图标
    private int selIcon;
    // 为选中时显示的图标
    private int unselIcon;
    // 是否选中
    private boolean isSelect;
    // 消息条数
    private int notifyNum;

    public MenuItemVO(int id,
                      int titleRes,
                      int selIcon,
                      int unselIcon,
                      boolean isSelect) {
        this.id = id;
        this.titleRes = titleRes;
        this.selIcon = selIcon;
        this.unselIcon = unselIcon;
        this.isSelect = isSelect;
        this.notifyNum = 0;
    }

    public int getId() {
        return id;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public int getSelIcon() {
        return selIcon;
    }

    public int getUnselIcon() {
        return unselIcon;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getNotifyNum() {
        return notifyNum;
    }

    public void setNotifyNum(int notifyNum) {
        this.notifyNum = notifyNum;
    }
}
