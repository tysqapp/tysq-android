package com.tysq.ty_android.utils.DBManager;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.tysq.ty_android.config.Constant;

import java.util.Collections;
import java.util.List;

import db.LocalHistoryModel;
import db.LocalHistoryModel_Table;
/**
 * author       : liaozhenlin
 * time         : 2019/10/15 14:55
 * desc         : 数据库操作管理
 * version      : 1.5.0
 */
public class DBLocalDataSourceManager {

    public static void addLocalHistory(String url){
        ModelAdapter<LocalHistoryModel> manager =
                FlowManager.getModelAdapter(LocalHistoryModel.class);

        LocalHistoryModel oldModel = new Select()
                .from(LocalHistoryModel.class)
                .where(LocalHistoryModel_Table.url.eq(url))
                .querySingle();

        if (oldModel != null){
            manager.delete(oldModel);
        }

        LocalHistoryModel model = new LocalHistoryModel();
        model.setUrl(url);
        manager.insert(model);
        judgeHistoryNum();
    }

    public static void deleteLocalHistory(LocalHistoryModel model){
        ModelAdapter<LocalHistoryModel> manager =
                FlowManager.getModelAdapter(LocalHistoryModel.class);
        manager.delete(model);
    }

    public static List<LocalHistoryModel> queryHistoryList(){
        List<LocalHistoryModel> list = new Select().from(LocalHistoryModel.class).queryList();
        Collections.reverse(list);
        return list;
    }

    public static void judgeHistoryNum(){
        List<LocalHistoryModel> list = new Select().from(LocalHistoryModel.class).queryList();
        ModelAdapter<LocalHistoryModel> manager = FlowManager.getModelAdapter(LocalHistoryModel.class);
        if (list.size() <= Constant.DATABASE_NUM){
            return;
        }

        manager.delete(list.get(0));
    }
}
