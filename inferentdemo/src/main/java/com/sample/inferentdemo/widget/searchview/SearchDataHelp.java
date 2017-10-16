package com.sample.inferentdemo.widget.searchview;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by FarinaZhang on 2017/9/19.
 */
public class SearchDataHelp {
    public static SQLiteDatabase helperDB;
    private static Context mContext;

    public static void setContext(Context context){
        mContext = context;
    }
    public static SQLiteDatabase getDB() {

        if (helperDB == null) {
            helperDB = new RecordSQLiteOpenHelper(mContext).getReadableDatabase();
        }
        return helperDB;
    }

    public static Cursor getAllData() {
        return getDB().query("records", null, null, null, null, null, null);
    }

    /**
     * 关注2：清空数据库
     */
    public static void deleteData() {

        getDB().execSQL("delete from records");

    }

    /**
     * 关注3
     * 检查数据库中是否已经有该搜索记录
     */
    public static boolean hasData(String tempName) {
        // 从数据库中Record表里找到name=tempName的id
        Cursor cursor = getDB().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //  判断是否有下一个
        return cursor.moveToNext();
    }

    /**
     * 插入数据到数据库，即写入搜索字段到历史搜索记录
     */
    public static void insertData(String tempName) {
        getDB().execSQL("insert into records(name) values('" + tempName + "')");

    }

    public static void closeDB() {
        if (helperDB != null) {
            getDB().close();
            helperDB = null;
        }
    }
}
