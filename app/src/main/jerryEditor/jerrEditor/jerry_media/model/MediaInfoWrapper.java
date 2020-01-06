package jerrEditor.jerry_media.model;

import com.zinc.lib_jerry_editor.exception.NoDataException;

import java.util.List;

/**
 * author       : frog
 * time         : 2019-07-10 09:01
 * desc         : 多媒体数据包装类别
 * version      : 1.3.0
 */
public class MediaInfoWrapper {

    private List data;

    private int count;

    public MediaInfoWrapper(List data) {
        this.data = data;
        this.count = 0;
    }

    public Object getData() throws NoDataException {
        if (data == null || count > data.size()) {
            throw new NoDataException("There is no enough data in list");
        }

        Object data = this.data.get(count);

        count++;

        return data;

    }

}
