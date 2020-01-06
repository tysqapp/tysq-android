package response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : frog
 * time         : 2019/5/15 下午5:28
 * desc         : 标签
 * version      : 1.3.0
 */
public class LabelResp {

    @SerializedName("label_infos")
    private List<LabelItem> labelList;
    @SerializedName("total_num")
    private int totalNum;

    public List<LabelItem> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<LabelItem> labelList) {
        this.labelList = labelList;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public static class LabelItem implements Parcelable {

        /**
         * label_id : 6
         * name : 大撒大撒撒大
         * note :
         * status : 0
         */

        @SerializedName("label_id")
        private int labelId;
        @SerializedName("label_name")
        private String name;
        @SerializedName("note")
        private String note;
        @SerializedName("status")
        private int status;

        public int getLabelId() {
            return labelId;
        }

        public void setLabelId(int labelId) {
            this.labelId = labelId;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.labelId);
            dest.writeString(this.name);
            dest.writeString(this.note);
            dest.writeInt(this.status);
        }

        public LabelItem() {
        }

        protected LabelItem(Parcel in) {
            this.labelId = in.readInt();
            this.name = in.readString();
            this.note = in.readString();
            this.status = in.readInt();
        }

        public static final Creator<LabelItem> CREATOR = new Creator<LabelItem>() {
            @Override
            public LabelItem createFromParcel(Parcel source) {
                return new LabelItem(source);
            }

            @Override
            public LabelItem[] newArray(int size) {
                return new LabelItem[size];
            }
        };
    }
}
