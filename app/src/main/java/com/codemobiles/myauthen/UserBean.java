package com.codemobiles.myauthen;

import android.os.Parcel;

/**
 * Created by suruchboss on 3/13/2017 AD.
 */

class UserBean implements android.os.Parcelable
{
    public String username;
    public String password;
    public Integer isPasswordRemembered;

    public final static String TABLE_NAME = "USERPASSWORD";
    public final static String COLUMN_USERNAME = "USERNAME";
    public final static String COLUMN_PASSWORD = "PASSWORD";
    public final static String COLUMN_PASSWORD_REM = "PASSWORD_REM";

    public UserBean(String _username, String _password, Integer _isRemember)
    {
        username = _username;
        password = _password;
        isPasswordRemembered = _isRemember;
    }

    public String toString()
    {
        return COLUMN_USERNAME + ": " + username + "\n" + COLUMN_PASSWORD + ": " + password + "\n" + COLUMN_PASSWORD_REM + ": " + isPasswordRemembered;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeValue(this.isPasswordRemembered);
    }

    protected UserBean(Parcel in)
    {
        this.username = in.readString();
        this.password = in.readString();
        this.isPasswordRemembered = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>()
    {
        @Override
        public UserBean createFromParcel(Parcel source)
        {
            return new UserBean(source);
        }

        @Override
        public UserBean[] newArray(int size)
        {
            return new UserBean[size];
        }
    };
}
