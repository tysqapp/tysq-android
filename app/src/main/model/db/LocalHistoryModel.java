package db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tysq.ty_android.local.db.TyDB;

/**
 * author       : liaozhenlin
 * time         : 2019/10/15 9:42
 * desc         : 历史记录
 * version      : 1.5.0
 */
@Table(database = TyDB.class)
public class LocalHistoryModel extends BaseModel {

    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
