package response.home;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author       : frog
 * time         : 2019-10-15 14:43
 * desc         : 页面切换
 * version      : 1.4.1
 */
public class PageInfo implements Parcelable {

    // 总数
    private long total;
    // 当前页
    private long curPage;
    // 一次数量
    private final int pageSize;

    public PageInfo(long total, long curPage, int pageSize) {
        this.total = total;
        this.curPage = curPage;
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getCurPage() {
        return curPage;
    }

    public void setCurPage(long curPage) {
        this.curPage = curPage;
    }

    public int getPageSize() {
        return pageSize;
    }

//    public void setPageSize(long pageSize) {
//        this.pageSize = pageSize;
//    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.total);
        dest.writeLong(this.curPage);
        dest.writeInt(this.pageSize);
    }

    protected PageInfo(Parcel in) {
        this.total = in.readLong();
        this.curPage = in.readLong();
        this.pageSize = in.readInt();
    }

    public static final Creator<PageInfo> CREATOR = new Creator<PageInfo>() {
        @Override
        public PageInfo createFromParcel(Parcel source) {
            return new PageInfo(source);
        }

        @Override
        public PageInfo[] newArray(int size) {
            return new PageInfo[size];
        }
    };
}
