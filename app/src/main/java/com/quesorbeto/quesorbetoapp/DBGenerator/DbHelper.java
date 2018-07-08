package com.quesorbeto.quesorbetoapp.DBGenerator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import QueSorbetoDataBase.DaoMaster;
import QueSorbetoDataBase.DaoSession;

public class DbHelper {

    private SQLiteDatabase db;
    //ORM GreenDAO
    private DaoMaster daoMaster;
    public DaoSession daoSession;

    public DbHelper(Context pAppContext) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(pAppContext, "QueSorbeto", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }
}
