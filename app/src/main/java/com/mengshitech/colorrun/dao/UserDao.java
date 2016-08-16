package com.mengshitech.colorrun.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mengshitech.colorrun.bean.UserEntiy;
import com.mengshitech.colorrun.fragment.me.DatabaseHelper;

/**
 * Created by kanghuicong on 2016/7/20  14:21.
 * 515849594@qq.com
 */
public class UserDao {
    /**
     *
     */
    private DatabaseHelper dbHelper;
    SQLiteDatabase db;

    public UserDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // 插入
    public void add(String user_id,String user_name,String user_header,String user_phone,String user_email,String user_sex,String user_height,String user_weight,String user_address,String user_sign) {
        db = dbHelper.getWritableDatabase();
        db.execSQL("insert into user (user_id,user_name,user_header,user_phone,user_email,user_sex,user_height,user_weight,user_address,user_sign) values(?,?,?,?,?,?,?,?,?,?)",
                new Object[]{user_id,user_name,user_header,user_phone,user_email,user_sex,user_height,user_weight,user_address,user_sign});
        db.close();
        Log.i("getUser_height2",user_weight);
    }

    //更新数据
    public void update_data(String type, String values, String user_id) {
        db = dbHelper.getWritableDatabase();
        db.execSQL("update user set ?=? where user_id=?",
                new Object[]{type, values, user_id});
        db.close();
    }

    //查找数据
    public UserEntiy find(String id) {
        db = dbHelper.getWritableDatabase();// 初始化SQLiteDatabase对象
        Log.i("mingzizizi",id+"");
        Cursor cursor = db.rawQuery("select * from user where user_id=?",
                      new String[]{String.valueOf(id)});
        if (cursor.moveToNext())// 遍历查找到的便签信息
        {Log.i("mingzizizi",cursor.getColumnIndex("user_id")+"");
            return new UserEntiy(

                    cursor.getString(cursor.getColumnIndex("user_id")),
                    cursor.getString(cursor.getColumnIndex("user_name")),
                    cursor.getString(cursor.getColumnIndex("user_pwd")),
                    cursor.getString(cursor.getColumnIndex("user_birthday")),
                    cursor.getString(cursor.getColumnIndex("user_sex")),
                    cursor.getString(cursor.getColumnIndex("user_header")),
                    cursor.getString(cursor.getColumnIndex("user_identity")),
                    cursor.getString(cursor.getColumnIndex("user_address")),
                    cursor.getString(cursor.getColumnIndex("user_bankid")),
                    cursor.getString(cursor.getColumnIndex("user_fullname")),
                    cursor.getString(cursor.getColumnIndex("user_level")),
                    cursor.getString(cursor.getColumnIndex("user_health")),
                    cursor.getString(cursor.getColumnIndex("user_height")),
                    cursor.getString(cursor.getColumnIndex("user_weight")),
                    cursor.getString(cursor.getColumnIndex("user_sign")),
                    cursor.getString(cursor.getColumnIndex("user_phone")),
                    cursor.getString(cursor.getColumnIndex("user_email")),
                    cursor.getString(cursor.getColumnIndex("user_state")),
                    cursor.getString(cursor.getColumnIndex("user_otherid")));
        }
        return null;// 如果没有信息，则返回null
    }

    public void delete(Integer... ids) {
        if (ids.length > 0)
        {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < ids.length; i++)
            {
                sb.append('?').append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            db = dbHelper.getWritableDatabase();
            db.execSQL("delete from user where user_id in (" + sb
                    + ")", (Object[]) ids);
        }
    }

    public void delete_user (String user_id) {
        db = dbHelper.getWritableDatabase();
        db.execSQL("delete from user where user_id = ?" +
                new Object[]{user_id});
        Log.i("delete_user","delete_user");
        db.close();
    }
}
