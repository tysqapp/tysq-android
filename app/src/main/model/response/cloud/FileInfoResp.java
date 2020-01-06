package response.cloud;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : frog
 * time         : 2019/5/14 上午10:39
 * desc         : 文件信息 响应
 * version      : 1.3.0
 */
public class FileInfoResp {

    @SerializedName("file_info")
    private List<FileItem> fileInfo;

    public List<FileItem> getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(List<FileItem> fileInfo) {
        this.fileInfo = fileInfo;
    }

    public static class FileItem {

        /**
         * id : 2
         * account_id : 662
         * filename : 87a4543d82a03a97c85dd221194a85bbf98800e0e94badc1aa2109ab50ce0eb2_1.gif
         * type : image
         * cover : null
         * screen_shot : null
         * create_at : 1557742316
         * size : 1339829
         * hash : f710075e61e91cae181fe66b32224a9b5004a8c4d887504cbf2be1a0b6b91869
         * url : http://192.168.0.33:8081/v1.0/file?file_id=2
         * download_url : https://xxx.tysqapp.com/api/file/originals/796/802.jpg
         */

        @SerializedName("id")
        private int id;
        @SerializedName("account_id")
        private int accountId;
        @SerializedName("filename")
        private String filename;
        @SerializedName("type")
        private String type;
        @SerializedName("covers")
        private List<CoverInfo> coverList;
        @SerializedName("screenshots")
        private List<ScreenshotInfo> screenshotList;
        @SerializedName("create_at")
        private long createAt;
        @SerializedName("size")
        private int size;
        @SerializedName("hash")
        private String hash;
        @SerializedName("url")
        private String url;
        @SerializedName("duration")
        private long duration;
        @SerializedName("download_url")
        private String downloadUrl;
        @SerializedName("storage")
        private int storage;

        private boolean isSelect = false;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
            this.accountId = accountId;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<CoverInfo> getCoverList() {
            return coverList;
        }

        public void setCoverList(List<CoverInfo> coverList) {
            this.coverList = coverList;
        }

        public List<ScreenshotInfo> getScreenshotList() {
            return screenshotList;
        }

        public void setScreenshotList(List<ScreenshotInfo> screenshotList) {
            this.screenshotList = screenshotList;
        }

        public long getCreateAt() {
            return createAt;
        }

        public void setCreateAt(long createAt) {
            this.createAt = createAt;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public int getStorage() {
            return storage;
        }

        public void setStorage(int storage) {
            this.storage = storage;
        }

        @Override
        public String toString() {
            return "FileItem{" +
                    "id=" + id +
                    ", accountId=" + accountId +
                    ", filename='" + filename + '\'' +
                    ", type='" + type + '\'' +
                    ", coverList=" + coverList +
                    ", screenshotList=" + screenshotList +
                    ", createAt=" + createAt +
                    ", size=" + size +
                    ", hash='" + hash + '\'' +
                    ", url='" + url + '\'' +
                    ", duration=" + duration +
                    ", isSelect=" + isSelect +
                    '}';
        }
    }

    public static class ScreenshotInfo{

        /**
         * id : 19
         * screenshots_url : http://192.168.0.33:8081/v1.0/file/redirect/19
         */

        @SerializedName("id")
        private int id;
        @SerializedName("screenshots_url")
        private String screenshotsUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getScreenshotsUrl() {
            return screenshotsUrl;
        }

        public void setScreenshotsUrl(String screenshotsUrl) {
            this.screenshotsUrl = screenshotsUrl;
        }
    }

    public static class CoverInfo{

        /**
         * id : 83
         * cover_url : http://192.168.0.33:8081/v1.0/file/redirect/83
         */

        @SerializedName("id")
        private int id;
        @SerializedName("cover_url")
        private String coverUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }
    }

}
