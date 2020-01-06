package vo;

/**
 * author       : frog
 * time         : 2019-08-20 15:11
 * desc         : 排序
 * version      : 1.3.0
 */
public class SortVO {

    private int type;
    private String name;
    private boolean isSelect;

    public SortVO(int type,
                  String name,
                  boolean isSelect) {
        this.type = type;
        this.name = name;
        this.isSelect = isSelect;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
