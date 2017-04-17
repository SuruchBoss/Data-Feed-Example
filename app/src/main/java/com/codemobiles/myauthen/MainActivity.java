package com.codemobiles.myauthen;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class MainActivity extends AppCompatActivity
{

    private EditText mUsername;
    private EditText mPassword;
    private Button mLoginBtn;
    private Intent i;
    private EditText edtUsername;
    private EditText edtPassword;

    // Check Runtime Permission -- BEGIN
    public void checkRuntimPermission()
    {
        Nammu.init(this);
        // Check Runtime Permission
        Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback()
        {
            @Override
            public void permissionGranted()
            {
                Toast.makeText(MainActivity.this, "Manifest.permission.WRITE_EXTERNAL_STORAGE - Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void permissionRefused()
            {
                finish();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    // Check Runtime Persion -- END

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkRuntimPermission();
        bindWdiget();

        // setup preference
        new Prefs.Builder()
                .setContext(getApplicationContext())
                .setMode(ContextWrapper.MODE_PRIVATE) //set mode
                .setPrefsName(getPackageName())// name registry
                .setUseDefaultSharedPreference(true)//Use Sharepref
                .build();

        //restore data
        mUsername.setText(Prefs.getString(UserBean.COLUMN_USERNAME, ""));
        mPassword.setText(Prefs.getString(UserBean.COLUMN_PASSWORD, ""));

    }


    private void bindWdiget()
    {
        mUsername = (EditText) findViewById(R.id.usrEditText);
        mPassword = (EditText) findViewById(R.id.pwdEditText);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
    }

    public void OnClickLogin(View view)
    {
        //Clone Database
        //CMAssetBundle.copyAssetFile("Account.db", false, getApplicationContext());
        CMAssetBundle.copyAssetFile("mydb.sqlite", false, getApplicationContext());
        DBAdapter dbAdapter = new DBAdapter(getApplicationContext());

        String _username = mUsername.getText().toString().trim();
        String _password = mPassword.getText().toString().trim();

        //Test DB
        UserBean resultBNn = dbAdapter.query(_username);
        if (resultBNn == null)
        {
            dbAdapter.insert(new UserBean(_username, _password, 0));
            Toast.makeText(this, "Insert Complete", Toast.LENGTH_SHORT).show();
        } else if (resultBNn.password.equals(_password) == false)
        {
            dbAdapter.update(new UserBean(_username, _password, 0));
            Toast.makeText(this, "Update", Toast.LENGTH_SHORT).show();
        } else
        {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            i = new Intent(this, SuccessActivity.class);

            i.putExtra(UserBean.TABLE_NAME, new UserBean(_username, _password, 0));
            startActivity(i);

            Prefs.putString(UserBean.COLUMN_USERNAME, mUsername.getText().toString());
            Prefs.putString(UserBean.COLUMN_PASSWORD, mPassword.getText().toString());
        }
    }

}
