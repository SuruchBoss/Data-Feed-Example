package com.codemobiles.myauthen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by suruchboss on 3/13/2017 AD.
 */

public class DBAdapter
{
    public SQLiteDatabase mDatabase;

    public DBAdapter(Context context)
    {
        String dbPath = CMAssetBundle.getAppPackagePath(context) + "/mydb.sqlite";

        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void insert(UserBean userBean)
    {
        ContentValues values = new ContentValues();

        values.put(UserBean.COLUMN_USERNAME, userBean.username);
        values.put(UserBean.COLUMN_PASSWORD, userBean.password);
        values.put(UserBean.COLUMN_PASSWORD_REM, userBean.isPasswordRemembered);
        mDatabase.insert(userBean.TABLE_NAME, null, values);
    }

    public UserBean query(String _username)
    {
        UserBean result = null;
        String[] selectionArgs = new String[]{_username};
        String[] columns = new String[]{UserBean.COLUMN_USERNAME, UserBean.COLUMN_PASSWORD, UserBean.COLUMN_PASSWORD_REM};

        Cursor cursor = mDatabase.query(UserBean.TABLE_NAME, columns, "Username =?", selectionArgs, null, null, "Username asc");

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();

            String _password = cursor.getString(cursor.getColumnIndex(UserBean.COLUMN_PASSWORD));
            int _isPasswordRemember = cursor.getInt(cursor.getColumnIndex(UserBean.COLUMN_PASSWORD_REM));

            return new UserBean(_username, _password, _isPasswordRemember);
        }
        return null;
    }

    public void update(UserBean userBean)
    {
        ContentValues values = new ContentValues();

        values.put(UserBean.COLUMN_USERNAME, userBean.username);
        values.put(UserBean.COLUMN_PASSWORD, userBean.password);
        values.put(UserBean.COLUMN_PASSWORD_REM, userBean.isPasswordRemembered);

        String whereClause = "Username = ?";
        String[] whereArgs = new String[]{userBean.username};

        mDatabase.update(UserBean.TABLE_NAME, values, whereClause, whereArgs);
    }
}
