package response.home;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019/4/26 下午2:25
 * desc         : 二级分类
 * version      : 1.3.0
 */
public class SubCategory implements Parcelable {

    /**
     * id : 5
     * name : 二次元
     * status : 1
     * parent_id : 2
     * sort : 3
     * note : 宅男福利
     * icon_url : http://192.168.0.33:8081//0
     */

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("status")
    private int status;
    @SerializedName("parent_id")
    private int parentId;
    @SerializedName("sort")
    private int sort;
    @SerializedName("note")
    private String note;
    @SerializedName("icon_url")
    private String iconUrl;

    // 是否选中
    private boolean isSelect = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.status);
        dest.writeInt(this.parentId);
        dest.writeInt(this.sort);
        dest.writeString(this.note);
        dest.writeString(this.iconUrl);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    }

    public SubCategory() {
    }

    protected SubCategory(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.status = in.readInt();
        this.parentId = in.readInt();
        this.sort = in.readInt();
        this.note = in.readString();
        this.iconUrl = in.readString();
        this.isSelect = in.readByte() != 0;
    }

    public static final Creator<SubCategory> CREATOR = new Creator<SubCategory>() {
        @Override
        public SubCategory createFromParcel(Parcel source) {
            return new SubCategory(source);
        }

        @Override
        public SubCategory[] newArray(int size) {
            return new SubCategory[size];
        }
    };
}
