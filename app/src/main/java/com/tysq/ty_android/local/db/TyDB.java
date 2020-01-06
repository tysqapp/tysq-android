package com.tysq.ty_android.local.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * author       : frog
 * time         : 2019-10-12 16:04
 * desc         :
 * version      :
 */

@Database(version = TyDB.VERSION)
public class TyDB {

    public static final int VERSION = 1;

    public static final String NAME = "LocalDB";

}
