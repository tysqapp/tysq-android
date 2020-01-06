package vo.search;
/**
 * author       : liaozhenlin
 * time         : 2019/9/18 11:34
 * desc         : 首页搜索的列表VO
 * version      : 1.5.0
 */
public class HomePageSearchVO<T> {

    private int type;
    private T data;

    public HomePageSearchVO(int type, T data){
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
