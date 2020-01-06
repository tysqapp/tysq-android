package com.jerry.image_watcher.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.jerry.image_watcher.utils.ImageStatusHelper;

/**
 * author       : frog
 * time         : 2019-08-30 14:11
 * desc         : 图片展示模型
 * version      : 1.4.0
 */
public class WatchImageVO implements Parcelable {

    // 模糊图 链接
    private String blurryUrl;
    // 原始图 链接
    private String originalUrl;

    // 是否为原图
    private boolean isOriginal;

    /**
     * 状态 {@link ImageStatusHelper}
     */
    private int status = ImageStatusHelper.IS_LOADING;

    public WatchImageVO() {
    }

    public WatchImageVO(String blurryUrl,
                        String originalUrl) {
        this.blurryUrl = blurryUrl;
        this.originalUrl = originalUrl;
    }

    public String getBlurryUrl() {
        return blurryUrl;
    }

    public void setBlurryUrl(String blurryUrl) {
        this.blurryUrl = blurryUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public boolean isOriginal() {
        return isOriginal;
    }

    public void setOriginal(boolean original) {
        isOriginal = original;
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
        dest.writeString(this.blurryUrl);
        dest.writeString(this.originalUrl);
        dest.writeByte(this.isOriginal ? (byte) 1 : (byte) 0);
        dest.writeInt(this.status);
    }

    protected WatchImageVO(Parcel in) {
        this.blurryUrl = in.readString();
        this.originalUrl = in.readString();
        this.isOriginal = in.readByte() != 0;
        this.status = in.readInt();
    }

    public static final Creator<WatchImageVO> CREATOR = new Creator<WatchImageVO>() {
        @Override
        public WatchImageVO createFromParcel(Parcel source) {
            return new WatchImageVO(source);
        }

        @Override
        public WatchImageVO[] newArray(int size) {
            return new WatchImageVO[size];
        }
    };
}
