package response.home;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * author       : frog
 * time         : 2019/4/26 下午2:24
 * desc         : 顶级分类
 * version      : 1.3.0
 */
public class TopCategory implements Parcelable {

    /**
     * id : 2
     * name : 生活
     * note : 天下大事
     * sort : 3
     * status : 1
     * icon_url : http://192.168.0.33:8081//0
     * sub_categories: []
     */

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("note")
    private String note;
    @SerializedName("sort")
    private int sort;
    @SerializedName("status")
    private int status;
    @SerializedName("icon_url")
    private String iconUrl;
    @SerializedName("subcategories")
    private List<SubCategory> subCategoryList;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public List<SubCategory> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(List<SubCategory> subCategoryList) {
        this.subCategoryList = subCategoryList;
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
        dest.writeString(this.note);
        dest.writeInt(this.sort);
        dest.writeInt(this.status);
        dest.writeString(this.iconUrl);
        dest.writeList(this.subCategoryList);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    }

    public TopCategory() {
    }

    protected TopCategory(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.note = in.readString();
        this.sort = in.readInt();
        this.status = in.readInt();
        this.iconUrl = in.readString();
        this.subCategoryList = new ArrayList<SubCategory>();
        in.readList(this.subCategoryList, SubCategory.class.getClassLoader());
        this.isSelect = in.readByte() != 0;
    }

    public static final Creator<TopCategory> CREATOR = new Creator<TopCategory>() {
        @Override
        public TopCategory createFromParcel(Parcel source) {
            return new TopCategory(source);
        }

        @Override
        public TopCategory[] newArray(int size) {
            return new TopCategory[size];
        }
    };
}
