package com.example.minami;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnLogin;
    private Button mBtnsignup;
    private EditText mEtuser;
    private EditText mEtpassword;
    private int flag = 0;

    private MyOpenHelp myOpenHelp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteStudioService.instance().start(this);

        mBtnLogin = findViewById(R.id.btn_login);
        mBtnsignup = findViewById(R.id.btn_signup);
        mEtuser = findViewById(R.id.et_1);
        mEtpassword = findViewById(R.id.et_2);

        myOpenHelp = new MyOpenHelp(this,"MyUser.db",null,1);

        mBtnLogin.setOnClickListener(this);
        mBtnsignup.setOnClickListener(this);

    }


    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_login:
                String username = mEtuser.getText().toString();
                String password = mEtpassword.getText().toString();
                Intent intent = null;


                if(username.length()==0||password.length()==0){
                    Toast.makeText(getApplicationContext(),"用户名或密码不能为空！",Toast.LENGTH_SHORT).show();
                }

                query(username,password);


                break;
            case R.id.btn_signup:
                Intent intent2 = null;
                intent2 = new Intent(MainActivity.this,signupActivity.class);
                //调试用代码，删除表
                //drop();

                startActivity(intent2);

        }

    }



    private void query(String username,String password){
        int flag=-1;
        Intent intent;
        SQLiteDatabase db = myOpenHelp.getWritableDatabase();
        Cursor cursor = db.query("User",null,null,null,
                null,null,null);

        if(cursor.moveToFirst()){
            do{
                if(username.equals(cursor.getString(cursor.getColumnIndex("name")))&&password.equals(cursor.getString(cursor.getColumnIndex("password")))){
                    intent = new Intent(MainActivity.this, InterfaceActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"登录成功！",Toast.LENGTH_SHORT).show();
                    UserHelp userHelp = new UserHelp();
                    userHelp.setUsername(username);
                    userHelp.setOpenHelp(myOpenHelp);
                    ReciteActivity.userHelp = userHelp;
                    LearnWordActivity.userHelp=userHelp;
                    PlanActivity.userHelp=userHelp;
                    InterfaceActivity.userHelp=userHelp;

                    flag =1;
                    break;
                }else if(!username.equals(cursor.getString(cursor.getColumnIndex("name")))||!password.equals(cursor.getString(cursor.getColumnIndex("password"))))
                {
                    flag =0;
                }
            }while (cursor.moveToNext());
        }

        if(flag == 0&&username.length()!=0&&password.length()!=0)
        {

            Toast.makeText(getApplicationContext(),"登录失败！请检查用户名或密码是否正确！",Toast.LENGTH_SHORT).show();

        }
        cursor.close();

    }

    public void drop(){
        SQLiteDatabase db = myOpenHelp.getWritableDatabase();
        db.execSQL("drop table Vocabulary");
        db.execSQL("drop table User");
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            this.finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }


}