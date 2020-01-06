package request;

/**
 * author       : liaozhenlin
 * time         : 2019-8-27 10:07
 * desc         :
 * version      : 1.0.0
 */
public class BannerIdReq {

    private int parent_id;  //一级分类id
    private int[] sub_id;   //二级分类id

    public int[] getSub_id() {
        return sub_id;
    }

    public void setSub_id(int[] sub_id) {
        this.sub_id = sub_id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }
}
