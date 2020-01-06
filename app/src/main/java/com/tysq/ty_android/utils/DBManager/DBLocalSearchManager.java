package com.tysq.ty_android.utils.DBManager;

import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.tysq.ty_android.config.Constant;

import java.util.Collections;
import java.util.List;

import db.LocalLabel;
import db.LocalLabel_Table;

/**
 * author       : liaozhenlin
 * time         : 2019/10/15 14:55
 * desc         : 本地搜索数据库操作管理
 * version      : 1.5.0
 */
public class DBLocalSearchManager {

    public static void addLocalHistory(String label){
        Log.d("DBLocalSearchManager", "执行到这了");
        ModelAdapter<LocalLabel> manager =
                FlowManager.getModelAdapter(LocalLabel.class);

        LocalLabel oldModel = new Select()
                .from(LocalLabel.class)
                .where(LocalLabel_Table.label.eq(label))
                .querySingle();

        if (oldModel != null){
            manager.delete(oldModel);
        }

        LocalLabel model = new LocalLabel();
        model.setLabel(label);
        manager.insert(model);
        judgeHistoryNum();
    }

    public static void deleteLocalHistory(LocalLabel model){
        ModelAdapter<LocalLabel> manager =
                FlowManager.getModelAdapter(LocalLabel.class);
        manager.delete(model);
    }

    public static List<LocalLabel> queryHistoryList(){
        List<LocalLabel> list = new Select().from(LocalLabel.class).queryList();
        Collections.reverse(list);
        return list;
    }

    public static void judgeHistoryNum(){
        List<LocalLabel> list = new Select().from(LocalLabel.class).queryList();
        ModelAdapter<LocalLabel> manager = FlowManager.getModelAdapter(LocalLabel.class);
        if (list.size() <= Constant.LOCAL_LABEL_NUM){
            return;
        }

        manager.delete(list.get(0));
    }
}
